package com.greenfield.ui.handler.group;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.GroupConstants;
import com.greenfield.common.constant.UserStatusCodes;
import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.dao.profile.UserAdminDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.nosql.base.CassandraDao;
import com.greenfield.common.nosql.object.message.DocumentStore;
import com.greenfield.common.nosql.object.message.GroupMCount;
import com.greenfield.common.nosql.object.message.GroupUCount;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.group.GroupInfo;
import com.greenfield.common.object.group.MessageVO;
import com.greenfield.common.object.group.PublicGroup;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.common.object.group.UserInviteVO;
import com.greenfield.common.object.group.UserRequestVO;
import com.greenfield.common.object.user.User;
import com.greenfield.common.object.user.UserInfo;
import com.greenfield.common.util.Base64D3Util;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.IDGenerator;
import com.greenfield.ui.action.group.GroupAction;
import com.greenfield.ui.action.group.GetMessageAction;
import com.greenfield.ui.action.group.PostMessageAction;

public class MessageHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(MessageHandler.class); 
	

	/**
	 * Make sure this person has permission to change records or see stuff.
	 */
	protected boolean preSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		MessageVO messageVO = (MessageVO) obj;
		
		String mode = messageVO.getMode();
		String type = messageVO.getType();
		//String userId = user.getUserId();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		
		boolean pass = false;
		if (mode != null && !mode.equals("")) {
			try {
				if (mode.equals(GetMessageAction.MESSAGE_HOME)) {
					// check if he can update the group ...
					pass = true;
				} else if (mode.equals(PostMessageAction.NEW_MESSAGE)) {
					pass = true;  // default true for this case
					if (type != null && type.equals(PostMessageAction.UPDATE)) {
						// create action is submitted, need to check if he can create message to the group
						// Note: can only post to: P (public), S (Sooniq), G* (groups users are in), U* (someone)
						// and zip - post
						String toGroupList = messageVO.getToGroupList();
						if (toGroupList != null && !toGroupList.equals("")) {
							String[] items = toGroupList.split(",");
							for (int i = 0; i < items.length; i ++) {
								if (verifyUserPostMessage(groupDao, items[i], user.getUserId(), sessionContext) == false) {
									// fails
									pass = false;
									LOGGER.warn("SecurityCheck fails for user to post message, user Id: " 
									+ user.getUserId() + ", togroups: " + toGroupList + ", failed at: " + items[i]);
									break;
								}
							}
						}
					}
				} else {
					pass = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn("MessageHandler:preSecurityCheck error: for " + user.getUserId() + ", error: " + e.getMessage());
			}
		} else {
			pass = true;
		}

		if (pass == false) {
			LOGGER.warn("Warning: User " + user.getUserId()
					+ " is trying to access something not in his permission.");
		}

		return pass;
	}
	
	// Note: can only post to: P (public), S (Sooniq), G* (groups users are in), or U* (someone)
	private boolean verifyUserPostMessage(GroupDAO groupDao, String targetId, String userId, WebSessionContext sessionContext) throws Exception {
		boolean pass = false;
		
		// if post to public, or Sooniq, always fine
		// ideally, if post to public, should not post to other groups, but let us see.
		if (targetId.equals("P") || targetId.equals("S")) {	
			pass = true;
		
		// G: assume it is a group
		} else if (targetId.startsWith("G")) {
			boolean hasJoined = GroupUtil.haveJoined(groupDao, userId, sessionContext, targetId);
			if (hasJoined) {
				pass = true;
			}
		
		// U: assume it is one user
		// if it is U, only 1-1 conversation; if need to send more, need to open a group.
		} else if (targetId.startsWith("U")) {
			// ??? check later to see if necessary to verify the User ID's in our Database.
			pass = true;
		} else {
			// zip code post
			pass = true;
		}
		
		return pass;
		
	}
	
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		MessageVO messageVO = (MessageVO) obj;
		
		System.out.println("message handler: ");
		
		String mode = messageVO.getMode();
		String type = messageVO.getType();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		CassandraDao cassDao = new CassandraDao();
		try {		
			String returnType = "";
			String userId = messageVO.getUserId();
			
			Vector userGroupList = GroupUtil.getMyGroupListFromSession(groupDao, userId, sessionContext);
			messageVO.setMyGroupList(userGroupList);
			
			// figure out the group names, from the group ID's - inputVO.getToGroupList();
			// this togroupnames only used by the "new message page"
			setToGroupNames(messageVO);
			
			// we need to figure all the message counts for the users in, also the group counters ( for public and zip counters)
			setUserMessageCounters(cassDao, messageVO);
			setPublicGroups(cassDao, messageVO, sessionContext);
			setGroupTotalMessageCountersForPublicGroups(cassDao, messageVO);
			
			
			if (mode != null && mode.equals(PostMessageAction.NEW_MESSAGE)) {
				try {
					if (type != null && type.equals(PostMessageAction.UPDATE)) {
						String msgId = IDGenerator.getNextTimeId(database, IDGenerator.MESSAGE_ID);
						
						// debug
						System.out.println("msg id: " + msgId);
						
						// use the same Timestamp for all the necessary places!
						Timestamp now = DateUtil.getTimestampAddSeconds(null, 0);
						createMessageInCassandra(cassDao, msgId, userId, messageVO, sessionContext, now);
						loadAttachmentsToCassandra(cassDao, msgId, userId, messageVO, sessionContext, now);
						updateMessageCounters(cassDao, groupDao, msgId, userId, messageVO, sessionContext, now);
						
					}
	
					messageVO.setSuccess(true);
					returnType = "success";	
				} catch (Exception ex) {
					// update error
					ex.printStackTrace();
					messageVO.setSuccess(false);
					returnType="generalerror";
					LOGGER.warn("Error in Group Home: " + ex.getMessage());
				}
			} 
			
	
			
			if (returnType.equals("generalerror")) {
				messageVO.setSuccess(false);
				messageVO.setMessage("There was a techncial error for your request.  Please try again later.");
			} 
		} finally {
			cassDao.shutdown();
		}
		
		return messageVO;
	}
	
	/**
	 * Write message to multiple tables -
	 * If new subject, then: group_s_store, group_l_store, group_t_store, group_u_store, and group_m_store
	 * If reply, then: group_l_store,  group_t_store, group_u_store, and group_m_store
	 * 
	 * @param msgId
	 * @param userId
	 * @param messageVO
	 * @param sessionContext
	 * @throws Exception 
	 */
	private void createMessageInCassandra(CassandraDao cassDao, String msgId, String userId, 
			MessageVO messageVO, WebSessionContext sessionContext, Timestamp now) throws Exception {
		String rootId = messageVO.getRootId();
		if (rootId != null && !rootId.equals("")) {
			// this is a replying message - need to query the DB to find the next available ID
			// if too many replies, then ???
			// rowId := {groupId}.{row_seq}, get from group_rowid table
			// if it is reply, the "toGroup" should have only one Group!
			// Note: for replies - should save to the same row, as the root message has.  ?? how to know, root id - row id?
			String toGroup = messageVO.getToGroupList();    
			String rowId = MessageRowidMgr.getCurrentRowIdForGroup(cassDao, toGroup);  // ???? should not use the "current" one
			String latestNodeId = MessageCassandraHelper.getLatestReplyNodeId(cassDao, rowId, rootId);   // rootId should be used, not the msgId

			String nextNodeId = null;
			if (latestNodeId != null) {
				// found
				String seqN = "";
				int index = latestNodeId.indexOf(".");
				if (index > -1) {
					// good
					seqN = latestNodeId.substring(index + 1);
					
					if (seqN.length() != 3) {
						throw new Exception("Node Id format wrong: " + latestNodeId + ", for rowId: " + rowId + ", rootId: " + rootId);
					}
				} else {
					// something serious wrong
					throw new Exception("Node Id format wrong: " + latestNodeId + ", for rowId: " + rowId + ", rootId: " + rootId);
				}
				nextNodeId = rootId + "." + Base64D3Util.getNextBase64(seqN);
			} else {
				// not found in DB, so it is the first one
				nextNodeId = rootId + "." + Base64D3Util.convertNumberToBase64(0);
			}
			
			
			// call batch writing
			MessageCassandraHelper.writeMessagesToCassandraInBatch(cassDao, rowId, msgId, rootId, 
					nextNodeId, userId, messageVO.getNickname(), messageVO, sessionContext, true, now);
			
		} else {
			// this is a new, leading topic message - can it be posted to multiple groups?
			// assume only one this time  - if we do multiple groups, then here 
			// need to loop through the groups and post the message!!!!!
			String toGroup = messageVO.getToGroupList();    
			String rowId = MessageRowidMgr.getCurrentRowIdForGroup(cassDao, toGroup);
			rootId = "";   // may define as msgId
			
			MessageCassandraHelper.writeMessagesToCassandraInBatch(cassDao, rowId, msgId, rootId, 
					null, userId, messageVO.getNickname(), messageVO, sessionContext, false, now);
		}
	}
	
	/**
	 * Create document entries in Document Store for the attachments.
	 * @param cassDao
	 * @param msgId
	 * @param userId
	 * @param messageVO
	 * @param sessionContext
	 * @throws Exception 
	 */
	private void loadAttachmentsToCassandra(CassandraDao cassDao, String msgId, String userId, 
			MessageVO messageVO, WebSessionContext sessionContext, Timestamp now) throws Exception {
		List<DocumentStore> fileList = messageVO.getFileList();
		
		if (fileList != null && fileList.size() > 0) {
			for (int i = 0; i < fileList.size(); i ++) {
				DocumentStore doc = fileList.get(i);
				doc.setDocId(msgId + "." + i);
				doc.setCreDate(now);
				cassDao.insert(doc);
			}
		}
	}
	
	/**
	 * Update the group level total message counts
	 * also update the message accounts for all users who have joined the group.
	 * @param cassDao
	 * @param msgId
	 * @param userId
	 * @param messageVO
	 * @param sessionContext
	 * @throws Exception 
	 */
	private void updateMessageCounters(CassandraDao cassDao, GroupDAO groupDao, String msgId, String userId, 
			MessageVO messageVO, WebSessionContext sessionContext, Timestamp now) throws Exception {
		String toGroupList = messageVO.getToGroupList();
		
		// !!! assume this is only one group now - o.w. go through the loop to update for each group
		String groupId = toGroupList;
		Vector<UserGroupVO> userGroupList = null;
		
		// find all the users in that group only if the group is a private group.
		if (groupId.startsWith("G")) {
			userGroupList = groupDao.getUserGroupListFromDBLite(groupId, null, null); // GroupConstants.ACCOUNT_ACTIVE);
		}
		
		MessageCassandraHelper.updateMessageCountInCassandraInBatch(cassDao, groupId, userGroupList);
		
	}
	
	private void setToGroupNames(MessageVO messageVO) {
		String toGroupList = messageVO.getToGroupList();
		if (toGroupList != null && !toGroupList.equals("")) {
			String names = "";
			String[] groups = toGroupList.split(",");
			for (int i = 0; i < groups.length; i ++) {
				if (i > 0) {
					names += ";";
				}
				if (groups[i].equals("P")) {
					names += "Public Group";
				} else if (groups[i].startsWith("G")) {
					// only need to look the groups that I am in
					names += findMyGroupName(groups[i], messageVO.getMyGroupList());
					
				} else {
					// zip, kind group
					names += groups[i];
				}
			}
			
			messageVO.setToGroupNames(names);
		}
	}
	
	/**
	 * This is a line search.  However, if not too many per user, this is not bad.
	 * O.w. we need to use hash idea.
	 * @param groupId
	 * @param myGroupList
	 * @return
	 */
	private String findMyGroupName(String groupId, Vector myGroupList) {
		String groupName = "";
		if (myGroupList != null && myGroupList.size() > 0) {
			for (int i = 0; i < myGroupList.size(); i++) {
				GroupInfo info = (GroupInfo) myGroupList.get(i);
				if (info.getGroupId().equals(groupId)) {
					groupName = info.getGroupName();
					break;
				}
			}
		}
		
		return groupName;
	}

	/**
	 * Find all the message counters that the current user have -
	 * display the # of messages that not read!
	 * @param cassDao
	 * @param messageVO
	 */
	private void setUserMessageCounters(CassandraDao cassDao, MessageVO messageVO) {
		try {
			Vector myGroupList = messageVO.getMyGroupList();
			if (myGroupList != null && myGroupList.size() > 0) {
				StringBuilder cql = new StringBuilder();
				cql.append("select group_id, msg_cnt from group_u_count where group_id in (");
				
				for (int i = 0; i < myGroupList.size(); i++) {
					if (i > 0) {
						cql.append(",");
					}
					GroupInfo info = (GroupInfo) myGroupList.get(i);
					cql.append("'").append(info.getGroupId()).append("'");
				}
				
				cql.append(") and user_id = '").append(messageVO.getUserId()).append("'");
				
				// find the counters for all the groups
				Vector list = cassDao.select(cql.toString(), GroupUCount.class.getName());
				
				// ugly, set the counters to the user group
				if (list != null && list.size() > 0) {
					HashMap<String, Long> counters = new HashMap<String, Long>();
					for (int i = 0; i < list.size(); i ++) {
						GroupUCount ct = (GroupUCount) list.get(i);
						counters.put(ct.getGroupId(), new Long(ct.getMsgCnt()));
					}
					
					// this will tell the # messages that user have not read ...
					for (int i = 0; i < myGroupList.size(); i++) {
						GroupInfo info = (GroupInfo) myGroupList.get(i);
						if (counters.containsKey(info.getGroupId())) {
							info.setMsgCount(counters.get(info.getGroupId()));
						} else {
							info.setMsgCount(0l);
						}
					}
				}
			}
			
		} catch (Exception e) {
			LOGGER.warn("ERROR to get user message counters: " + e.getMessage());
		}
	}
	
	
	// For now the public group is just one: public "P" group;
	// We may need to consider "ZIP" groups, and other categories public groups.
	private void setPublicGroups(CassandraDao cassDao, MessageVO messageVO, WebSessionContext sessionContext) {
		Vector publicGroupList = new Vector();
		PublicGroup p = new PublicGroup();
		p.setGroupId("P");
		p.setGroupName("General Public");
		
		publicGroupList.add(p);
		messageVO.setPublicGroupList(publicGroupList);
	}
	
	// get total messages for the given groups - P, Zip's  (for public groups now)
	private void setGroupTotalMessageCountersForPublicGroups(CassandraDao cassDao, MessageVO messageVO) {
		try {
			Vector publicGroupList = messageVO.getPublicGroupList();
			if (publicGroupList != null && publicGroupList.size() > 0) {
				StringBuilder cql = new StringBuilder();
				cql.append("select group_id, msg_cnt from group_m_count where group_id in (");
				
				// assume at least one in public group
				for (int i = 0; i < publicGroupList.size(); i++) {
					if (i > 0) {
						cql.append(",");
					}
					
					PublicGroup info = (PublicGroup) publicGroupList.get(i);
					cql.append("'").append(info.getGroupId()).append("'");
				}
				
				// user group's to here
				Vector myGroupList = messageVO.getMyGroupList();
				if (myGroupList != null && myGroupList.size() > 0) {
					for (int i = 0; i < myGroupList.size(); i++) {
						GroupInfo info = (GroupInfo) myGroupList.get(i);
						cql.append(",'").append(info.getGroupId()).append("'");
					}
				}
				
				cql.append(")");
				
				// find the counters for all the groups
				Vector list = cassDao.select(cql.toString(), GroupMCount.class.getName());
				
				// ugly, set the counters to the user group
				if (list != null && list.size() > 0) {
					HashMap<String, Long> counters = new HashMap<String, Long>();
					for (int i = 0; i < list.size(); i ++) {
						GroupMCount ct = (GroupMCount) list.get(i);
						counters.put(ct.getGroupId(), new Long(ct.getMsgCnt()));
					}
					
					// this will tell the # messages that user have not read ...
					for (int i = 0; i < publicGroupList.size(); i++) {
						PublicGroup info = (PublicGroup) publicGroupList.get(i);
						if (counters.containsKey(info.getGroupId())) {
							info.setMsgCount(counters.get(info.getGroupId()));
						} else {
							info.setMsgCount(0l);
						}
					}
					
					// set up the private counters too
					if (myGroupList != null && myGroupList.size() > 0) {
						for (int i = 0; i < myGroupList.size(); i++) {
							GroupInfo info = (GroupInfo) myGroupList.get(i);
							if (counters.containsKey(info.getGroupId())) {
								info.setTotalCount(counters.get(info.getGroupId()));
							} else {
								info.setTotalCount(0l);
							}
						}
					}
				}
			}
			
		} catch (Exception e) {
			LOGGER.warn("ERROR to get public group message counters: " + e.getMessage());
		}
	}
}
