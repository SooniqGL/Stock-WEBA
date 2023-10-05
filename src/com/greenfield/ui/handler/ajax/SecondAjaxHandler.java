package com.greenfield.ui.handler.ajax;

import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.nosql.base.CassandraDao;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.ajax.AjaxVO;
import com.greenfield.common.object.ajax.GroupUserListResponse;
import com.greenfield.common.object.ajax.MessageQueryResponse;
import com.greenfield.common.object.ajax.UserGroupResponse;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.common.object.user.User;
import com.greenfield.ui.action.group.GetMessageAction;
import com.greenfield.ui.action.group.GroupAction;
import com.greenfield.ui.action.scan.AgeSrchAction;
import com.greenfield.ui.action.scan.ScanAction;
import com.greenfield.ui.handler.group.GroupAjaxHandler;
import com.greenfield.ui.handler.group.GroupUtil;
import com.greenfield.ui.handler.group.MessageAjaxHandler;

public class SecondAjaxHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(GroupAjaxHandler.class);

	protected boolean preSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		AjaxVO ajaxVO = (AjaxVO) obj;
		
		String requestType = ajaxVO.getRequestType();
		String userId = user.getUserId();
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		
		
		boolean pass = false;
		if (requestType != null && !requestType.equals("")) {
			try {
				if (requestType.equals(GetMessageAction.GET_PAGE_LIST) 
						|| requestType.equals(GetMessageAction.GET_MESSAGE_LIST) ) {
					// if it is private group, need to see if he/she has the joined the group.
					String mode = inputParams.get("mode");  // subject level, time series, replies
					if (mode != null && (mode.equals(MessageConstants.SUBJECT_LIST) 
							|| mode.equals(MessageConstants.TIME_LIST) 
							|| mode.equals(MessageConstants.REPLIES_LIST))) {
						String groupId = inputParams.get("groupId");
						
						// only check if the group is a private group.
						if (groupId != null && groupId.startsWith("G")) {
							GroupDAO groupDao = new GroupDAO();
							groupDao.setDatabase(database);
							boolean hasJoined = GroupUtil.haveJoined(groupDao, userId, sessionContext, groupId);
							if (hasJoined) {
								pass = true;
							}
						} else {
							pass = true;
						}
					} else {
						pass = true;
					}
				} else if (requestType.equals(GroupAction.SEARCH_GROUP)) {
					// there is no security issue for search
					// as USERID is used from search; only the joined private groups are used.	
					pass = true;

				
				} else if (requestType.equals(GetMessageAction.MESSAGE_DETAIL)) {
					// msgId is passed, how do we know if the message is public, private, and OK to the customer?
					// check on the post-security section - as before hand, we do not know if the message is accessible to the user or not.
					pass = true;
				
				} else if (requestType.equals(GetMessageAction.REPLY_MESSAGE)) {
					// someone has invited you to join this group
					// and you are answering it with A - accepting, D - Denying.
					// Note: we will allow to answer if it is already in group, just do nothing, but update the invite
					/*
					String groupId = inputParams.get("groupId");
						
					GroupDAO groupDao = new GroupDAO();
					groupDao.setDatabase(database);
					boolean haveJoined = GroupUtil.haveJoined(groupDao, userId, sessionContext, groupId);
					if (haveJoined) {
						// if you already joined, it is wrong
						pass = false;
					} else {
						pass = true;
					} */
					pass = true;
				} else {
					pass = true;
				}
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn("preSecurityCheck error: for " + user.getUserId() + ", error: " + e.getMessage());
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
	
	protected boolean postSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		AjaxVO ajaxVO = (AjaxVO) obj;
		
		String requestType = ajaxVO.getRequestType();
		String userId = user.getUserId();
		// Map<String, String> inputParams = ajaxVO.getRequestParams();
		
		boolean pass = false;
		if (requestType != null && !requestType.equals("")) {
			try {
				
				if (requestType.equals(GetMessageAction.MESSAGE_DETAIL)) {
					// message id is given, but need to see if the groups in SecurityTag 
					// has at least one group that the customer has joined.  (If it is public group, always good.)
					GroupUserListResponse objResponse = (GroupUserListResponse) ajaxVO.getResponseObj();
					List<UserGroupVO> rowList = objResponse.getRowList();
					if (rowList != null && rowList.size() > 0) {
						for (int i = 0; i < rowList.size(); i ++) {
							UserGroupVO vo = rowList.get(i);
							
							System.out.println("userid: " + userId + ", " +  vo.getUserId());
							
							if (userId.equals(vo.getUserId())) {
								if ("ACT".equals(vo.getUserGroupStatus())) {
									// block this later if we want
								}

								pass = true;
								break;
							}
						}
					} else {
						pass = true;
					}
				} else {
					pass = true;
				} 
				
				pass = true;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn("postSecurityCheck error: for " + user.getUserId() + ", error: " + e.getMessage());
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
	
	public BaseObject doAction(BaseObject inputVO, WebSessionContext sessionContext) {
		AjaxVO ajaxVO = (AjaxVO) inputVO;
		CassandraDao cassDao = new CassandraDao();
		
		System.out.println("Ajax request type: " + ajaxVO.getRequestType());
		try {
			String requestType = ajaxVO.getRequestType();
			if (requestType != null && requestType.equals(GetMessageAction.GET_PAGE_LIST)) {
				handleGetPageList(cassDao, ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GetMessageAction.GET_MESSAGE_LIST)) { 
				//handleAnswerInvite(ajaxVO, sessionContext);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxVO.setSuccess(false);
			ajaxVO.setMessage("Program error, please report this to Admin.");
		} finally {
			cassDao.shutdown();
		}

		return ajaxVO;
	}
	
	/**
	 * Note: This only handles when groupId is given - get messages for that group!
	 * 
	 * type: indexmsg, index, msg - return a list of indexes, and messages
	 * startIndex : message id -> to go forward or backward
	 * order: P/N, previous set, or next sets for indexes
	 * If for indexmsg case, msg list returned will be the first subset
	 * for msg case, only a subset of messages will be returned
	 * for indexes: 15 * 10 a set; for messages, 15 per page.
	 * @param cassDao
	 * @param ajaxVO
	 * @param sessionContext
	 * @throws Exception
	 */
	private void handleGetPageList(CassandraDao cassDao, AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String mode = inputParams.get("mode");
		String groupId = inputParams.get("groupId");
		
		if (groupId != null && !groupId.equals("")) {
			// check on thing
			if (mode != null && (mode.equals(MessageConstants.SUBJECT_LIST) 
					|| mode.equals(MessageConstants.TIME_LIST) 
					|| mode.equals(MessageConstants.REPLIES_LIST))) {
				
				String startIndex = inputParams.get("startIndex");   // message index that start the page
				String type = inputParams.get("type");     // indexmsg, index, msg
				String order = inputParams.get("order");   // P/N
				MessageQueryResponse responseObj = MessageAjaxHandler.getPageList(cassDao, groupId, mode, type, startIndex, order);

				if (responseObj != null && responseObj.getSuccess() == true) {
					ajaxVO.setResponseObj(responseObj);
					ajaxVO.setSuccess(true);
				} else {
					ajaxVO.setSuccess(false);
				}
			}
			
		} else {
			// rare case, the group id is not passed by client
			ajaxVO.setSuccess(false);
		}
	}
}