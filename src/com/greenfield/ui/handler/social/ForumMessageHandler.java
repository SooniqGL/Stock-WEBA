/*
 * Created on Dec 30, 2006
 */
package com.greenfield.ui.handler.social;

import java.util.Date;
import java.util.Vector;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.util.IDGenerator;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.object.forum.ForumMessage;
import com.greenfield.common.object.forum.ForumVO;

/**
 * @author qin
 */
public class ForumMessageHandler  extends BaseHandler {
	public static final int PAGE_SIZE = 10;   // 10 SUBJECTS per page

	private ForumMessageDAO dao;
	private double lastSubjectId;
	private double firstSubjectId;
	
	/* (non-Javadoc)
	 * @see com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object.BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		ForumVO forumVO = (ForumVO) obj;
		String mode = forumVO.getMode();
		
		try {
			dao = new ForumMessageDAO();
			dao.setDatabase(database);
			if (mode != null && mode.equals("nb")) {
				// load a blank form
				double parentId = forumVO.getParentId();
				ForumMessage fm = new ForumMessage();
				if (parentId != 0) {
					fm.setParentId(parentId);
					Vector messageList = dao.getForumMessageListFromDB(parentId, 0);
					if (messageList != null && messageList.size() > 0) {
						ForumMessage fmParent = (ForumMessage) messageList.get(0);
						String sub = "";
						if (fmParent.getSubject() != null && !fmParent.getSubject().startsWith("Re:")) {
							sub = "Re: " + fmParent.getSubject();
						} else if (fmParent.getSubject() != null) {
							sub = fmParent.getSubject();
						}
						
						fm.setSubject(sub);
					} else {
						// error
						System.out.println("Error: message not found for id: " + parentId);
					}
				} 
				
				forumVO.setForumMessage(fm);
			} else if (mode != null && mode.equals("na")) {
				// create message
				double parentId = forumVO.getParentId();
				double pageSubjectId = forumVO.getSubjectId();
				ForumMessage fm = forumVO.getForumMessage();
				String messageIdStr = IDGenerator.getNextId(database, IDGenerator.FORUM_MESSAGE_ID_PREFIX);
				double messageId = (new Double(messageIdStr)).doubleValue();
				fm.setMessageId(messageId);
				
				double subjectId = 0;
				if (parentId != 0) {
					// reply message
					dao.updateForumMessageToDB(parentId);
					subjectId = dao.getForumSubjectIdFromDB(parentId);
					fm.setParentId(parentId);
					fm.setSubjectId(subjectId);
				} else {
					// new subject message
					String subjectIdStr = IDGenerator.getNextId(database, IDGenerator.FORUM_SUBJECT_ID_PREFIX);
					subjectId = (new Double(subjectIdStr)).doubleValue();
					fm.setSubjectId(subjectId);
					dao.insertForumSubjectToDB(fm);
					fm.setParentId(messageId);
					pageSubjectId = subjectId;
				}
				
				fm.setHasChildren("N");
				fm.setUserId(forumVO.getUserId());
				fm.setMDate(OracleUtil.getOracleInputTimestamp(new Date()));   // use Timestamp, not date for PostgreSQL
				dao.insertForumMessageToDB(fm);
				
				firstSubjectId = dao.getFirstSubjectIdFromDB();
				lastSubjectId = dao.getLastSubjectIdFromDB();
				
				// get the last
				if (pageSubjectId == 0) {
					pageSubjectId = lastSubjectId;
				}
				
				forumVO.setSuccess(true);
				
				// load data
				forumVO.setSubjectId(pageSubjectId);
				loadData(forumVO, pageSubjectId);					
			} else if (mode != null && mode.equals("q")){
				// query the message
				double subjectId = forumVO.getSubjectId();
				firstSubjectId = dao.getFirstSubjectIdFromDB();
				lastSubjectId = dao.getLastSubjectIdFromDB();
				if (subjectId != 0) {
					// subjectId given,
				} else {
					// find the market Id
					subjectId = lastSubjectId;
					forumVO.setSubjectId(subjectId);
				}
				
				loadData(forumVO, subjectId);
			}
			
		} catch (Exception e) {
			e.printStackTrace();	
			forumVO.setSuccess(false);
		}
		
		return forumVO;
	}
	
	/**
	 * Load data in range: subjectId - delta -> subjectId
	 * 
	 * Rules for tagId:
	 * First level: "A" + {i}
	 * Level later: prev + "K" + {i}; So, the first child will be "{...}K1"
	 * Content: current tag + "C"
	 * These rules are used by the screen scripts to handle the layer display.
	 * @param forumVO
	 * @param subjectId
	 */
	private void loadData(ForumVO forumVO, double subjectId) throws Exception {
		if (subjectId < firstSubjectId) {
			return;
		}
		
		double startId = subjectId - PAGE_SIZE + 1;
		if (startId < firstSubjectId) {
			startId = firstSubjectId;
		}
		
		double prevSubjectId = 0;
		if (startId > firstSubjectId) {
			prevSubjectId = startId - 1;
		}
		forumVO.setPrevSubjectId(prevSubjectId);
		
		double nextId = 0;
		if (subjectId  < lastSubjectId) {
			nextId = subjectId + PAGE_SIZE;
			if (nextId > lastSubjectId) {
				nextId = lastSubjectId;
			}
		} else {
			nextId = 0;
		}
		forumVO.setNextSubjectId(nextId);

		Vector subjectList = dao.getForumSubjectListFromDB(startId, subjectId);
		Vector messageList = new Vector();
						
		if (subjectList != null && subjectList.size() > 0) {
			String tagId = "";
			for (int i = 0; i < subjectList.size(); i ++) {
				ForumMessage fm = (ForumMessage) subjectList.get(i);
				fm.setIndent(0);
				tagId = "A" + String.valueOf(i + 1);
				fm.setTagId(tagId);
				messageList.add(fm);
				if (fm.getHasChildren() != null && fm.getHasChildren().equals("Y")) {
					subloadData(forumVO, messageList, fm, 0, tagId);
				}
			}
		} 
		
		forumVO.setMessageList(messageList);
		
	}
	
	/**
	 * For each message, load its children to the list or hash.
	 * @param forumVO
	 * @param fm
	 */
	private void subloadData(ForumVO forumVO, Vector messageList, ForumMessage fm, int indent, String tagId) throws Exception {
		Vector childList = dao.getForumMessageListFromDB(0, fm.getMessageId());
		
		if (childList != null && childList.size() > 0) {
			String locTagId = "";
			
			for (int i = 0; i < childList.size(); i ++) {
				ForumMessage childM = (ForumMessage) childList.get(i);
				childM.setIndent(indent + 1);
				locTagId = tagId + "K" + String.valueOf(i + 1);
				childM.setTagId(locTagId);
				messageList.add(childM);
				
				// if has child
				if (childM.getHasChildren() != null && childM.getHasChildren().equals("Y")) {
					subloadData(forumVO, messageList, childM, indent + 1, locTagId);
				}
			}		
			
		}
	}
}
