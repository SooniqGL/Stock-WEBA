package com.greenfield.ui.handler.group;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;

import com.datastax.driver.core.BatchStatement;
import com.datastax.driver.core.PreparedStatement;
import com.datastax.driver.core.Session;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.nosql.base.CassandraDao;
import com.greenfield.common.nosql.object.message.DocumentStore;
import com.greenfield.common.nosql.object.message.GroupTStore;
import com.greenfield.common.nosql.object.message.MsgDetail;
import com.greenfield.common.object.group.MessageVO;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.JSONUtil;

public class MessageCassandraHelper {
	// only display/save certain content in msg_basic field
	private final static int SHORT_CONTENT_LENGTH = 100;
	
	// rowId - message row id, {groupId}.{row_seq}
	public static String getLatestReplyNodeId(CassandraDao cassDao, String rowId, String msgId) throws Exception {
		String nodeId = null;
		
		if (rowId == null || rowId.equals("") || msgId == null || msgId.equals("")) {
			throw new Exception("MessageCassandraDao: getNextReplyMessageId: rowId or msgId is null");
		}

		// select node_id from group_t_store where row_id = 'G1.1' and node_id >= 'aBVts' limit 1;
		String cql = "select node_id from group_t_store where row_id = '" + rowId + "' and node_id >= '" + msgId + "' limit 1";
		Vector list = cassDao.select(cql, GroupTStore.class.getName());
		if (list != null && list.size() > 0) {
			GroupTStore t = (GroupTStore) list.get(0);
			nodeId = t.getNodeId();
		}
		
		return nodeId;
	}
	
	/**
	 * Write message to multiple tables -
	 * If new subject, then: group_s_store, group_l_store, <group_t_store?>, group_u_store, and group_m_store
	 * If reply, then: group_l_store,  group_t_store, group_u_store, and group_m_store
	 * 
	 * Example:
	 * PreparedStatement ps = session.prepare( "BEGIN BATCH" + " INSERT INTO messages (user_id, msg_id, title, body) VALUES (?, ?, ?, ?);" 
	 * + " INSERT INTO messages (user_id, msg_id, title, body) VALUES (?, ?, ?, ?);" 
	 * + " INSERT INTO messages (user_id, msg_id, title, body) VALUES (?, ?, ?, ?);" + "APPLY BATCH" ); 
	 * session.execute(ps.bind(uid, mid1, title1, body1, uid, mid2, title2, body2, uid, mid3, title3, body3)); 
	 * @throws Exception 
	 * 
	 * 
	 */
	public static void writeMessagesToCassandraInBatch(CassandraDao cassDao, 
			String rowId, String msgId, String rootId, String replyNodeId,
			String userId, String nickName, MessageVO messageVO, WebSessionContext sessionContext, boolean isReply, Timestamp now) throws Exception {
		
		// build msgBasic, msgDetail, and msgHint - from the subject, content, doc list	
		String msgBasic  	= createMsgBasic (messageVO, msgId, rootId, userId, nickName, now);
		String msgDetail 	= createMsgDetail(messageVO, msgId, rootId, userId, nickName, now);
		String msgHint 		= createMsgHint(messageVO, msgId, now);
		
		Session session = cassDao.getSession();
		if (isReply) {
			// reply message
			StringBuilder batch = new StringBuilder();
			batch.append("BEGIN BATCH")
				.append(" INSERT INTO group_t_store (row_id, node_id, msg_basic) VALUES (?, ?, ?);")
				.append(" INSERT INTO group_l_store (row_id, msg_id, msg_basic) VALUES (?, ?, ?);")
				.append(" INSERT INTO group_m_store (msg_id, security_tag, msg_detail) VALUES (?, ?, ?);")
				.append(" INSERT INTO group_u_store (user_id, msg_id, msg_hint, group_list) VALUES (?, ?, ?, ?);")
				.append("APPLY BATCH");
			
			
			PreparedStatement ps = session.prepare(batch.toString()); 
			session.execute(ps.bind(
					rowId, replyNodeId, msgBasic, 
					rowId, msgId, msgBasic,
					msgId, messageVO.getToGroupList(), msgDetail,
					userId, msgId, msgHint, messageVO.getToGroupList()
					)); 
		} else {
			// new topic
			StringBuilder batch = new StringBuilder();
			batch.append("BEGIN BATCH")
				.append(" INSERT INTO group_s_store (row_id, msg_id, msg_basic) VALUES (?, ?, ?);")
				.append(" INSERT INTO group_l_store (row_id, msg_id, msg_basic) VALUES (?, ?, ?);")
				.append(" INSERT INTO group_m_store (msg_id, security_tag, msg_detail) VALUES (?, ?, ?);")
				.append(" INSERT INTO group_u_store (user_id, msg_id, msg_hint, group_list) VALUES (?, ?, ?, ?);")
				.append("APPLY BATCH");
			
			PreparedStatement ps = session.prepare(batch.toString()); 
			session.execute(ps.bind(
					rowId, msgId, msgBasic, 
					rowId, msgId, msgBasic,
					msgId, messageVO.getToGroupList(), msgDetail,
					userId, msgId, msgHint, messageVO.getToGroupList()
					)); 
		}
	}
	
	/**
	 *  update the message count for the group, and all the users in the group.
	 *  update group_u_count set msg_cnt = msg_cnt + 0 where group_id = 'G14' and user_id = '888-888-9973';
	 *  
	 *  PreparedStatement ps = session.prepare("INSERT INTO messages (user_id, msg_id, title, body) VALUES (?, ?, ?, ?)");
		BatchStatement batch = new BatchStatement();
		batch.add(ps.bind(uid, mid1, title1, body1));
		batch.add(ps.bind(uid, mid2, title2, body2));
		batch.add(ps.bind(uid, mid3, title3, body3));
		session.execute(batch);
	 */
	public static void updateMessageCountInCassandraInBatch(
			CassandraDao cassDao, String groupId,
			Vector<UserGroupVO> userGroupList) throws Exception {
		if (groupId == null || groupId.equals("")) {
			return;
		}
		
		// right now only one group, so for the we simple call dao, if in the future
		// we need to do multiple groups a time, we can use batch
		cassDao.executeSql("UPDATE group_m_count set msg_cnt = msg_cnt + 1 where group_id = '"+ groupId + "'");
		
		// update user level counters only for private groups
		if (groupId.startsWith("G") && userGroupList != null && userGroupList.size() > 0) {
			Session session = cassDao.getSession();
			// all the accounters - for users we like to limit the # of entries in batch
			// later - if too many users are in one group
			PreparedStatement ps = session.prepare("update group_u_count set msg_cnt = msg_cnt + 1 where group_id = ? and user_id = ?");
			BatchStatement batch = new BatchStatement(BatchStatement.Type.COUNTER);   //need to set type for counter batch!!
			
			for (int i = 0; i < userGroupList.size(); i ++) {
				UserGroupVO vo = (UserGroupVO) userGroupList.get(i);
				batch.add(ps.bind(groupId, vo.getUserId()));
			}
	
			session.execute(batch);	
		}
		
	}
	
	/**
	 * MsgDetail fields:
	 * 	private String s;   //subject;
		private String c;   // content;
		private String u;   // userId;
		private String n;   // nickname;
		private String m;   // msgId;
		private String r;   // rootId;
		private ArrayList<String> f;  // fileList;
		private Timestamp d;   //creDate;
		private String h;   // Y/N, to tell if more exists, or file list exists
		
		For basic: save content a little; and do not save file list
		But, need to tell if there are: 1) more content, or 2) has file or not
	 * @param messageVO
	 * @return
	 * @throws Exception 
	 */
	private static String createMsgBasic(MessageVO messageVO, 
			String msgId, String rootId, String userId, String nickName, Timestamp now) throws Exception {
		MsgDetail msgBasic = new MsgDetail();
		msgBasic.setS(messageVO.getMsgSubject());
		msgBasic.setU(userId);
		msgBasic.setN(nickName);
		msgBasic.setM(msgId);
		msgBasic.setR(rootId);
		msgBasic.setD(now);    
		// msgBasic.setI(rowId);  ?? save row id, or group id + seq
		
		String hasMore = "N";
		String content = messageVO.getMsgContent();
		if (content != null) {	
			if (content.length() > SHORT_CONTENT_LENGTH) {
				msgBasic.setC(content.substring(0, SHORT_CONTENT_LENGTH));
				hasMore = "Y";
			} else {
				msgBasic.setC(content);
			}
		}
		
		if (messageVO.getFileList() != null && messageVO.getFileList().size() > 0) {
			// there are documents
			hasMore = "Y";
		}
		
		msgBasic.setH(hasMore);
		String jsonStr = JSONUtil.convertJsonObjectToString(msgBasic);
		
		// debug
		System.out.println("msg basic: " + jsonStr);
		return jsonStr;
		
	}
	
	// for detail message, need to save full content, and file list
	private static String createMsgDetail(MessageVO messageVO, 
			String msgId, String rootId, String userId, String nickName, Timestamp now) throws Exception {
		MsgDetail msgDetail = new MsgDetail();
		msgDetail.setS(messageVO.getMsgSubject());
		msgDetail.setU(userId);
		msgDetail.setN(nickName);
		msgDetail.setM(msgId);
		msgDetail.setR(rootId);
		msgDetail.setD(now);
		msgDetail.setC(messageVO.getMsgContent());
			
		if (messageVO.getFileList() != null && messageVO.getFileList().size() > 0) {
			// there are documents - save ID list, type here to the detail?
			List<DocumentStore> fileList = messageVO.getFileList();
			ArrayList<String> fileIdList = new ArrayList<String>();
			for (int i = 0; i < fileList.size(); i ++) {
				DocumentStore doc = fileList.get(i);
				// document id := msgId + "." + sequence - here only sequence # is saved
				// doc type and doc name are used to display before doing real download
				fileIdList.add( i + ";" + doc.getDocType() + ";" + doc.getDocName());
			}
			
			msgDetail.setF(fileIdList);
		}
		
		String jsonStr = JSONUtil.convertJsonObjectToString(msgDetail);
		
		// debug
		System.out.println("msg detail: " + jsonStr);
		return jsonStr;
		
	}
	
	// used in person message
	private static String createMsgHint(MessageVO messageVO, 
			String msgId, Timestamp now) throws Exception {
		String msgHint = "";
		
		msgHint += messageVO.getMsgSubject() + ", ";
		String content = messageVO.getMsgContent();
		if (content != null && content.length() > SHORT_CONTENT_LENGTH) {
			msgHint += content.substring(0, SHORT_CONTENT_LENGTH);
		} else if (content != null) {
			msgHint += content;
		}
		
		msgHint += ", " + DateUtil.getDateStringInMMDDYYYYHHMMSSFormat(now);
		
		// debug
		System.out.println("msg hint: " + msgHint);
		return msgHint;
		
	}

}
