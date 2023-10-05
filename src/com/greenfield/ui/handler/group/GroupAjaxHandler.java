package com.greenfield.ui.handler.group;

import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.constant.GroupConstants;
import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.object.ajax.GroupRequestListResponse;
import com.greenfield.common.object.ajax.GroupUserListResponse;
import com.greenfield.common.object.ajax.UpdateGroupResponse;
import com.greenfield.common.object.ajax.UserGroupResponse;
import com.greenfield.common.object.group.GroupInfo;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.common.object.group.UserInviteVO;
import com.greenfield.common.object.group.UserRequestVO;
import com.greenfield.common.paging.GenericPagination;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;

public class GroupAjaxHandler {
	private static final Logger LOGGER = Logger.getLogger(GroupAjaxHandler.class); 
	
	private static final int SEARCH_GROUP_PAGE_SIZE = 50;
	
	
	public static UserGroupResponse processAnswerInvite(DBComm database, String userId, Map<String, String> input) {

		UserGroupResponse ajaxResponse = new UserGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		
		// debug
		//System.out.println("processUpdateGroup for group info: " + input.get("groupId"));
				
		try {
			// get the group id 
			String groupId = input.get("groupId");
			if (groupId == null || groupId.equals("")) {
				throw new Exception("GroupAjaxHandler - processAnswerInvite, groupId not defined.");
			}
			
			// action type: A or D
			String actionType = input.get("actionType");
			if (actionType == null || actionType.equals("")) {
				throw new Exception("GroupAjaxHandler - processAnswerInvite, actionType not defined.");
			}
			
			String inviteBy = input.get("inviteBy");
			if (inviteBy == null || inviteBy.equals("")) {
				throw new Exception("GroupAjaxHandler - processAnswerInvite, inviteBy not defined.");
			}
			
			// log history here - need to track to DB later
			LOGGER.warn("User: " + userId + " answering: " + groupId + ", action type: " + actionType + ", invite by: " + inviteBy);
			
			// verify if this user is invited to this group (only open status is queried)
			Vector list = groupDao.getUserInviteListFromDBLite(groupId, userId, inviteBy, GroupConstants.INVITE_OPEN);
			if (list == null || list.size() == 0) {
				throw new Exception("GroupAjaxHandler - processAnswerInvite, user is not invited to group: " + groupId + ", userId: " + userId + ", invite by: " + inviteBy);
			}
			
			UserInviteVO inviteVO = (UserInviteVO) list.get(0);
			//if (GroupConstants.INVITE_OPEN.equals(inviteVO.getStatus())) {
				// someone does invite you to the group, and the status is open (we already know you are not in the group yet)
				// 1) insert you to the new group; 2) update the invite status
				// But, if it is denying, then only update the invite
				String now = DateUtil.getDateStringInYYYYMMDDFormat(null);
				
				String status = null;
				if (actionType.equals(GroupConstants.INVITE_ACCEPTED)) {
					// it is possible, at the time to accept, it is already in group
					// for example, request is accepted by owner.
					if (!input.containsKey("haveJoined")) {
						UserGroupVO vo = new UserGroupVO();
						vo.setUserId(userId);
						vo.setGroupId(groupId);
						vo.setUserGroupStatus(GroupConstants.ACCOUNT_ACTIVE);
						vo.setCreDate(now);
						groupDao.addUserGroupToDB(vo);
					}
					status = GroupConstants.INVITE_ACCEPTED;
				} else {
					status = GroupConstants.INVITE_DENIED;
				}
				
				inviteVO.setStatus(status);
				inviteVO.setResponseDt(now);
				groupDao.updateUserInviteToDB(inviteVO);
				
				ajaxResponse.setSuccess(true);
		        ajaxResponse.setMessage("Invite is updated successfully.");
		    /*    
			} else {
				// only when it is open, we process the invite; if it is already answered, deny here
				// status is not good
				ajaxResponse.setSuccess(false);
		        ajaxResponse.setMessage("Invite is not open and maybe have been updated previously.");
				// throw new Exception("GroupAjaxHandler - processAnswerInvite, invite status is not OPEN, for group: " + groupId + ", userId: " + userId);
			} */
			
		} catch (Exception e) {
			// warning
			LOGGER.warn("Error: update status by owner: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}
	
	
	public static UpdateGroupResponse getInitDataForUpdateGroup(DBComm database, String userId, Map<String, String> input) throws Exception {
		UpdateGroupResponse ajaxResponse = new UpdateGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
				
		// debug
		//System.out.println("getInitData for group info: " + input.get("groupId"));
		
		try {
			String groupId = input.get("groupId");
			if (groupId == null || groupId.equals("")) {
				throw new Exception("GroupAjaxHandler - update group, group id is null.");
			}
			
			// get from DB
			GroupInfo groupInfo = groupDao.getGroupInfoFromDB(groupId);
			if (groupInfo != null) {
				ajaxResponse.setGroupId(groupId);
				ajaxResponse.setAccepting(groupInfo.getAccepting());
				ajaxResponse.setGroupName(groupInfo.getGroupName());
				ajaxResponse.setPublicNote(groupInfo.getPublicNote());
			} else {
					// error, group not found
					throw new Exception("GroupAjaxHandler - update group, group not found in DB: " + groupId);
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return ajaxResponse;
	}
	
	public static UserGroupResponse getUserGroup(DBComm database, String userId, Map<String, String> input) throws Exception {
		UserGroupResponse ajaxResponse = new UserGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
				
		// debug
		//System.out.println("getInitData for group info: " + input.get("groupId"));
		
		try {
			String groupId = input.get("groupId");
			if (groupId == null || groupId.equals("")) {
				throw new Exception("GroupAjaxHandler - getUserGroup, group id is null.");
			}
			
			// get from DB
			GroupInfo groupInfo = groupDao.getGroupInfoFromDB(groupId);
			if (groupInfo != null) {
				ajaxResponse.setGroupId(groupId);
				ajaxResponse.setAccepting(groupInfo.getAccepting());
				ajaxResponse.setGroupName(groupInfo.getGroupName());
				ajaxResponse.setPublicNote(groupInfo.getPublicNote());
				ajaxResponse.setNumUsers(groupInfo.getNumUsers());
				if (groupInfo.getOwnerId().equals(userId)) {
					ajaxResponse.setIsOwner("You");
				} else {
					ajaxResponse.setIsOwner(groupInfo.getOwnerFname());
				}
			} else {
					// error, group not found
					throw new Exception("GroupAjaxHandler -getUserGroup, group not found in DB: " + groupId);
			}
			
			// check if user in the group or not
			Vector<UserGroupVO> voList = groupDao.getUserGroupListFromDBLite(groupId, userId, null);
			if (voList != null && voList.size() > 0) {
				// should be exactly one
				UserGroupVO vo = voList.get(0);
				ajaxResponse.setUserGroupStatus(vo.getUserGroupStatus());
				
			} else {
				// user is not in the group yet
				ajaxResponse.setUserGroupStatus("");
				
				// check if user has appending request
				Vector<UserRequestVO> reqList = groupDao.getUserRequestListFromDBLite(groupId, userId, GroupConstants.REQUEST_OPEN);
				if (reqList != null && reqList.size() > 0) {
					// should be exactly one - as user id and group id are both given
					UserRequestVO req = reqList.get(0);
					ajaxResponse.setRequestDate(req.getRequestDt());
					ajaxResponse.setRequestStatus(req.getStatus());
				} else {
					ajaxResponse.setRequestDate("");
					ajaxResponse.setRequestStatus("");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return ajaxResponse;
	}
	
	/**
	 * Need to check if the group has "accepting" to "Y";
	 * If a request previously is done for the same group, and denied, then cannot request again!
	 * An error will be thrown if we cannot insert the user request to the table: key (group id, request id).
	 * @param database
	 * @param userId
	 * @param input
	 * @return
	 */
	public static UserGroupResponse applyToJoin(DBComm database, String userId, Map<String, String> input) {

		UserGroupResponse ajaxResponse = new UserGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		
		// debug
		//System.out.println("processUpdateGroup for group info: " + input.get("groupId"));
				
		try {
			// get the group id 
			String groupId = input.get("groupId");
			if (groupId == null || groupId.equals("")) {
				throw new Exception("GroupAjaxHandler -applyToJoin, groupId not defined.");
			}
			
			// check group info before allow to request ...
			GroupInfo groupInfo = groupDao.getGroupInfoFromDB(groupId);
			if (groupInfo != null && "Y".equals(groupInfo.getAccepting())) {
				UserRequestVO vo = new UserRequestVO();
				vo.setGroupId(groupId);
				vo.setRequestDt(DateUtil.getDateStringInYYYYMMDDFormat(null));
				vo.setRequestId(userId);
				vo.setStatus(GroupConstants.REQUEST_OPEN);
				groupDao.addUserRequestToDB(vo);
				
				ajaxResponse.setSuccess(true);
	            ajaxResponse.setMessage("Request is queued successfully.");
			} else {
					// error, group not found
					throw new Exception("GroupAjaxHandler -applyToJoin, group not found in DB, or not accepting: " + groupId);
			}
			
		} catch (Exception e) {
			// warning
			LOGGER.warn("Error: apply to join: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}

	/**
	 * The current user is the owner of the group.  It is verified in the pre-security check.
	 * grougId, userIdList {ID + ":"}, statusFlag (A or D) are passed from client.
	 * 
	 * Idealy, need to check if these users do requested online.
	 * 
	 * @param database
	 * @param userId
	 * @param input
	 * @return
	 */
	public static UserGroupResponse processRequestsByOwner(DBComm database, String userId, Map<String, String> input) {

		UserGroupResponse ajaxResponse = new UserGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		
		// debug
		//System.out.println("processRequestsByOwner for group info: " + input.get("groupId"));
				
		try {
			// get the group id 
			String groupId = input.get("groupId");
			if (groupId == null || groupId.equals("")) {
				throw new Exception("GroupAjaxHandler - processRequestsByOwner, groupId not defined.");
			}
			
			String userIdList = input.get("userIdList");
			if (userIdList == null || userIdList.equals("")) {
				throw new Exception("GroupAjaxHandler - processRequestsByOwner, userIdList not defined.");
			}
			
			String statusFlag = input.get("statusFlag");
			if (statusFlag == null || statusFlag.equals("")) {
				throw new Exception("GroupAjaxHandler - processRequestsByOwner, statusFlag not defined.");
			}
			
			// log history here - need to track to DB later
			LOGGER.warn("User: " + userId + " modified group request: " + groupId + ", users <" + userIdList + ">, status to: " + statusFlag);
			
			// potential issues: 1) userId - may not in our DB - can find who does as well.
			// 2) the user may not required the request - can be added later if find someone abuse this system.
			String[] userIds = userIdList.split(":");
			String now = DateUtil.getDateStringInYYYYMMDDFormat(null);
			for (int i = 0; i < userIds.length; i ++) {
				String status = "";
				if (statusFlag.equals(GroupConstants.REQUEST_DENIED)) {
					// for deny, only need to update the request status
					status = GroupConstants.REQUEST_DENIED;
				} else {
					// other values - approve the join
					status = GroupConstants.REQUEST_APPROVED;
					
					// check to check, if it is already in group, the insert will fail
					// in Postgres, if one fails, it will ignore all the following calls before fallback is called.
					Vector list2 = groupDao.getUserGroupListFromDBLite(groupId, userIds[i], null);
					if (list2 != null && list2.size() > 0) {
						// ignore
					} else {
						// insert the user to DB
						UserGroupVO vo = new UserGroupVO();
						vo.setUserId(userIds[i]);
						vo.setGroupId(groupId);
						vo.setUserGroupStatus(GroupConstants.ACCOUNT_ACTIVE);
						vo.setCreDate(now);
						groupDao.addUserGroupToDB(vo);
					}

				}
				
				UserRequestVO userRequest = new UserRequestVO();
				userRequest.setGroupId(groupId);
				userRequest.setRequestId(userIds[i]);
				userRequest.setResponseId(userId);
				userRequest.setResponseDt(now);
				userRequest.setStatus(status);
				groupDao.updateUserRequestToDB(userRequest);
			}
			
			ajaxResponse.setSuccess(true);
	        ajaxResponse.setMessage("Request is updated successfully.");
			
			
		} catch (Exception e) {
			// warning
			LOGGER.warn("Error: update status by owner: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}
	
	
	/**
	 * Change user group status - by owner of the group.
	 * @param database
	 * @param userId
	 * @param input
	 * @return
	 */
	public static UserGroupResponse updateUserStatusByOwner(DBComm database, String userId, Map<String, String> input) {

		UserGroupResponse ajaxResponse = new UserGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		
		// debug
		//System.out.println("processUpdateGroup for group info: " + input.get("groupId"));
				
		try {
			// get the group id 
			String groupId = input.get("groupId");
			if (groupId == null || groupId.equals("")) {
				throw new Exception("GroupAjaxHandler -updateUserStatusByOwner, groupId not defined.");
			}
			
			String targetUserId = input.get("userId");
			if (targetUserId == null || targetUserId.equals("")) {
				throw new Exception("GroupAjaxHandler -updateUserStatusByOwner, target UserId not defined.");
			}
			
			String newStatus = input.get("userGroupStatus");
			if (newStatus == null || newStatus.equals("")) {
				throw new Exception("GroupAjaxHandler -updateUserStatusByOwner, new Status not defined.");
			}
			
			// log history here - need to track to DB later
			LOGGER.warn("User: " + userId + " modified group: " + groupId + ", user <" + targetUserId + ">, status to: " + newStatus);
			
			UserGroupVO vo = new UserGroupVO();
			vo.setUserId(targetUserId);
			vo.setGroupId(groupId);
			vo.setUserGroupStatus(newStatus);
			groupDao.updateUserGroupToDB(vo);
			
			ajaxResponse.setSuccess(true);
	        ajaxResponse.setMessage("Request is updated successfully.");
			
			
		} catch (Exception e) {
			// warning
			LOGGER.warn("Error: update status by owner: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}
	
	
	/** form submitted, if exists, update; if not exists, insert */
	public static UpdateGroupResponse processUpdateGroup(DBComm database, String userId, Map<String, String> input) {

		UpdateGroupResponse ajaxResponse = new UpdateGroupResponse();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		
		// debug
		//System.out.println("processUpdateGroup for group info: " + input.get("groupId"));
				
		try {
			// update the data - suppose the security is done 
			String groupId = input.get("groupId");
			
			// get from hashmap parameter and update db
			GroupInfo info = new GroupInfo();
			info.setGroupId(groupId);
			info.setAccepting(input.get("accepting"));
			info.setGroupName(input.get("groupName"));
			info.setPublicNote(input.get("publicNote"));
			
			// log history here - need to track to DB later
			LOGGER.warn("User: " + userId + " has modified group info: " + groupId);
						
			// need to update
			groupDao.updateGroupInfoToDB(info);

            // reset cache
            // DBCachePool.resetWatchStockIdListInCache(userId);

            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Group Info is updated successfully.");
		} catch (Exception e) {
			// warning
			LOGGER.warn("Error: update group: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}

	/**
	 * for given keywords, find the search.
	 * @param database
	 * @param userId
	 * @param input
	 * @return
	 * @throws Exception 
	 */
	public static GenericPagination<GroupInfo> getSearchGroup(DBComm database, String userId, Map<String, String> input) throws Exception {
		GenericPagination<GroupInfo> pagination = null;
		String keywords = input.get("keywords");
		if (keywords == null || keywords.equals("")) {
			return null;   // give up; should not go to here if normally
		}
		
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		Vector<GroupInfo> itemList = groupDao.getGroupInfoListFromDBByKeywords(keywords);
		
		if (itemList == null || itemList.size() == 0) {
			// if not found, then try group id
			itemList = groupDao.getGroupInfoListFromDB(keywords.toUpperCase(), null, null);
		}
		
		pagination = new GenericPagination<GroupInfo>();
		pagination.init(itemList, SEARCH_GROUP_PAGE_SIZE, "pageName", true);
		
		return pagination;
	}
	
	/**
	 * group id is given, and find all the users in that group.
	 * @param database
	 * @param userId
	 * @param input
	 * @return
	 * @throws Exception
	 */
	
	public static GroupUserListResponse getGroupUserList(DBComm database, String userId, Map<String, String> input) throws Exception {
		GroupUserListResponse responseObj = new GroupUserListResponse();
		
		String groupId = input.get("groupId");
		if (groupId == null || groupId.equals("")) {
			return null;   // give up; should not go to here if normally
		}
		
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		Vector<UserGroupVO> itemList = groupDao.getUserGroupListFromDB(groupId, null, null, null);
		if (itemList != null && itemList.size() > 0) {
			responseObj.setRowList(itemList);
			
			// pull value from the first row - the value is "no-normalized" here
			// will see the performance, this is just for group management, should be fine
			// May consider to write light queries for the heavily used queries later.
			UserGroupVO vo = itemList.get(0);
			responseObj.setGroupId(groupId);
			responseObj.setNumUsers(String.valueOf(vo.getNumUsers()));
			responseObj.setGroupName(vo.getGroupName());
			responseObj.setOwnerFname(vo.getOwnerFname());
			responseObj.setOwnerFname(vo.getOwnerFname());
			responseObj.setOwnerLname(vo.getOwnerLname());
			
			if (userId.equals(vo.getOwnerId())) {
				responseObj.setIsOwner("Y");
			} else {
				responseObj.setIsOwner("N");
			}
			
			
		} else {
			responseObj.setRowList(new Vector<UserGroupVO>());
		}
		
		
		
		return responseObj;
	}
	
	
	public static GroupRequestListResponse getGroupRequestList(DBComm database, String userId, Map<String, String> input) throws Exception {
		GroupRequestListResponse responseObj = new GroupRequestListResponse();
		
		String groupId = input.get("groupId");
		if (groupId == null || groupId.equals("")) {
			return null;   // give up; should not go to here if normally
		}
		
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
		Vector<UserRequestVO> itemList = groupDao.getUserRequestListFromDB(groupId, null, GroupConstants.REQUEST_OPEN);
		if (itemList != null && itemList.size() > 0) {
			responseObj.setRowList(itemList);
			
			// pull value from the first row - the value is "no-normalized" here
			// will see the performance, this is just for group management, should be fine
			// May consider to write light queries for the heavily used queries later.
			UserRequestVO vo = itemList.get(0);
			responseObj.setGroupId(groupId);
			responseObj.setNumUsers(String.valueOf(vo.getNumUsers()));
			responseObj.setGroupName(vo.getGroupName());
			responseObj.setOwnerFname(vo.getOwnerFname());
			responseObj.setOwnerFname(vo.getOwnerFname());
			responseObj.setOwnerLname(vo.getOwnerLname());
	
		} else {
			responseObj.setRowList(new Vector<UserRequestVO>());
		}
		
		return responseObj;
	}
	
}
