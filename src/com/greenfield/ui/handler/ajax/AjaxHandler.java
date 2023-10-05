package com.greenfield.ui.handler.ajax;

import java.util.List;
import java.util.Map;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.analyze.AgeSrchDAO;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.dao.member.WatchListDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.action.group.GroupAction;
import com.greenfield.ui.action.scan.AgeSrchAction;
import com.greenfield.ui.action.scan.ScanAction;
import com.greenfield.ui.handler.agesrch.AgeSrchHandler;
import com.greenfield.ui.handler.group.GroupAjaxHandler;
import com.greenfield.ui.handler.group.GroupHandler;
import com.greenfield.ui.handler.group.GroupUtil;
import com.greenfield.ui.handler.member.WatchListAjaxHandler;
import com.greenfield.ui.handler.member.WatchListHandler;
import com.greenfield.ui.handler.scan.ScanHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.ajax.AddwatchResponse;
import com.greenfield.common.object.ajax.AgesrchResponse;
import com.greenfield.common.object.ajax.AjaxVO;
import com.greenfield.common.object.ajax.BasicScanResponse;
import com.greenfield.common.object.ajax.GroupRequestListResponse;
import com.greenfield.common.object.ajax.GroupUserListResponse;
import com.greenfield.common.object.ajax.SearchGroupResponse;
import com.greenfield.common.object.ajax.UpdateGroupResponse;
import com.greenfield.common.object.ajax.UserGroupResponse;
import com.greenfield.common.object.group.GroupInfo;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.common.object.scan.ScanHistoryVO;
import com.greenfield.common.object.scan.StockAge;
import com.greenfield.common.object.user.User;
import com.greenfield.common.paging.GenericPagination;
import com.greenfield.common.paging.PageInfo;

public class AjaxHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(AjaxHandler.class); 
	

	protected boolean preSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		AjaxVO ajaxVO = (AjaxVO) obj;
		
		String requestType = ajaxVO.getRequestType();
		String userId = user.getUserId();
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		
		
		boolean pass = false;
		if (requestType != null && !requestType.equals("")) {
			try {
				if (requestType.equals(GroupAction.UPDATE_GROUP)) {
					// for now, only the own can update the group info
					String mode = inputParams.get("mode");  // GET_INIT_DATA, SUBMIT_FORM
					if (mode != null && mode.equals("SUBMIT_FORM")) {
						String groupId = inputParams.get("groupId");
						
						GroupDAO groupDao = new GroupDAO();
						groupDao.setDatabase(database);
						boolean isOwner = GroupUtil.isOwner(groupDao, userId, sessionContext, groupId);
						if (isOwner) {
							pass = true;
						}
					} else {
						pass = true;
					}
				} else if (requestType.equals(GroupAction.ADD_WATCH)) {
					// there is no security issue for add_watch
					// as USERID is used from session to add the watch entry.	
					pass = true;

				} else if (requestType.equals(GroupAction.UPDATE_USER_BY_OWNER)) {
					// for now only the owner can update one user's status
					String mode = inputParams.get("mode");  
					if (mode != null && mode.equals(GroupAction.UPDATE_USER_BY_OWNER)) {
						String groupId = inputParams.get("groupId");
						
						GroupDAO groupDao = new GroupDAO();
						groupDao.setDatabase(database);
						boolean isOwner = GroupUtil.isOwner(groupDao, userId, sessionContext, groupId);
						if (isOwner) {
							pass = true;
						}
					} else {
						pass = true;
					}
				} else if (requestType.equals(GroupAction.GROUP_REQUEST_LIST)) {
					// for now only the owner can update one user's status for the requests
					String mode = inputParams.get("mode");  
					if (mode != null && mode.equals(GroupAction.GROUP_REQUEST_LIST)) {
						String groupId = inputParams.get("groupId");
						
						GroupDAO groupDao = new GroupDAO();
						groupDao.setDatabase(database);
						boolean isOwner = GroupUtil.isOwner(groupDao, userId, sessionContext, groupId);
						if (isOwner) {
							pass = true;
						}
					} else {
						pass = true;
					}
				} else if (requestType.equals(GroupAction.PROCESS_REQUESTS_BY_OWNER)) {
					// for now only the owner can update one user's status for the requests
					String mode = inputParams.get("mode");  
					if (mode != null && mode.equals(GroupAction.PROCESS_REQUESTS_BY_OWNER)) {
						String groupId = inputParams.get("groupId");
						
						GroupDAO groupDao = new GroupDAO();
						groupDao.setDatabase(database);
						boolean isOwner = GroupUtil.isOwner(groupDao, userId, sessionContext, groupId);
						if (isOwner) {
							pass = true;
						}
					} else {
						pass = true;
					}
				
				} else if (requestType.equals(GroupAction.ANSWER_INVITE)) {
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
				
				if (requestType.equals(GroupAction.GROUP_USER_LIST)) {
					// to get a group list, the user has to be in the group
					// do this check on post-process
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

		System.out.println("Ajax request type: " + ajaxVO.getRequestType());
		try {
			String requestType = ajaxVO.getRequestType();
			if (requestType != null && requestType.equals(ScanAction.BASIC_SCAN)) {
				handleBasicScan(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(AgeSrchAction.AGESRCH_RESULT)) { 
				handleAgeSearch(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.ADD_WATCH)) { 
				handleAddWatch(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.DELETE_WATCH)) { 
				handleDeleteWatch(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.UPDATE_GROUP)) { 
				handleUpdateGroup(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.SEARCH_GROUP)) { 
				handleSearchGroup(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.USER_GROUP)) { 
				handleUserGroup(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.GROUP_USER_LIST)) { 
				handleGroupUserList(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.UPDATE_USER_BY_OWNER)) { 
				handleUpdateUserByOwner(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.GROUP_REQUEST_LIST)) { 
				handleGroupRequestList(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.PROCESS_REQUESTS_BY_OWNER)) { 
				handleProcessRequestsByOwner(ajaxVO, sessionContext);
			} else if (requestType != null && requestType.equals(GroupAction.ANSWER_INVITE)) { 
				handleAnswerInvite(ajaxVO, sessionContext);
			}
		} catch (Exception e) {
			e.printStackTrace();
			ajaxVO.setSuccess(false);
			ajaxVO.setMessage("Program error, please report this to Admin.");
		}

		return ajaxVO;
	}
	
	
	private void handleAnswerInvite(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String mode = inputParams.get("mode");
		String groupId = inputParams.get("groupId");
		
		if (groupId != null && !groupId.equals("")) {
			// check on thing
			if (mode.equals(GroupAction.ANSWER_INVITE)) {
				GroupDAO groupDao = new GroupDAO();
				groupDao.setDatabase(database);
				boolean haveJoined = GroupUtil.haveJoined(groupDao, ajaxVO.getUserId(), sessionContext, groupId);
				if (haveJoined) {
					inputParams.put("haveJoined", ""); 
				}
				
				UserGroupResponse responseObj = GroupAjaxHandler.processAnswerInvite(database, ajaxVO.getUserId(), inputParams);
				
				if (responseObj != null && responseObj.getSuccess() == true) {
					ajaxVO.setResponseObj(new UserGroupResponse());
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
	
	
	private void handleProcessRequestsByOwner(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String mode = inputParams.get("mode");
		String groupId = inputParams.get("groupId");
		
		if (groupId != null && !groupId.equals("")) {
			// check on thing
			if (mode.equals(GroupAction.PROCESS_REQUESTS_BY_OWNER)) {
				UserGroupResponse responseObj = GroupAjaxHandler.processRequestsByOwner(database, ajaxVO.getUserId(), inputParams);
				
				if (responseObj != null && responseObj.getSuccess() == true) {
					ajaxVO.setResponseObj(new UserGroupResponse());
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
	
	
	
	
	@SuppressWarnings("unchecked")
	private void handleGroupRequestList(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String groupId = inputParams.get("groupId");
		
		// return error if group id is not passed - should not happen
		if (groupId == null || groupId.equals("")) {
			ajaxVO.setSuccess(false);
		} else {
			GroupRequestListResponse responseObj = null;

			responseObj = GroupAjaxHandler.getGroupRequestList(database,
					ajaxVO.getUserId(), inputParams);

			// put it to session
			if (responseObj != null) {
				ajaxVO.setResponseObj(responseObj);
				ajaxVO.setSuccess(true);
			} else {
				ajaxVO.setSuccess(false);
			}
		}
	}
	
	private void handleUpdateUserByOwner(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String mode = inputParams.get("mode");
		String groupId = inputParams.get("groupId");
		
		if (groupId != null && !groupId.equals("")) {
			// check on thing
			if (mode.equals(GroupAction.UPDATE_USER_BY_OWNER)) {
				UserGroupResponse responseObj = GroupAjaxHandler.updateUserStatusByOwner(database, ajaxVO.getUserId(), inputParams);
				
				if (responseObj != null && responseObj.getSuccess() == true) {
					ajaxVO.setResponseObj(new UserGroupResponse());
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
	
	/**
	 * Group id is given, to get all the users in that group;
	 * for now, we do not do paging, and get all in one return.
	 * @param ajaxVO
	 * @param sessionContext
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void handleGroupUserList(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String groupId = inputParams.get("groupId");
		
		// return error if group id is not passed - should not happen
		if (groupId == null || groupId.equals("")) {
			ajaxVO.setSuccess(false);
		} else {
			GroupUserListResponse responseObj = null;

			responseObj = GroupAjaxHandler.getGroupUserList(database,
					ajaxVO.getUserId(), inputParams);

			// put it to session
			if (responseObj != null) {
				ajaxVO.setResponseObj(responseObj);
				ajaxVO.setSuccess(true);
			} else {
				ajaxVO.setSuccess(false);
			}
		}
	}
	
	/**
	 * 1) init load the overlay;
	 * 2) handle "apply to join"
	 * @param ajaxVO
	 * @param sessionContext
	 * @throws Exception
	 */
	private void handleUserGroup(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String mode = inputParams.get("mode");
		
		
		System.out.println(" get init ... " + mode);
		// GET-INIT-DATA, or, APPLY-TO-JOIN ...
		if (mode == null || mode.equals("")) {
			mode = "GET_INIT_DATA";
			inputParams.put("mode", "GET_INIT_DATA");
		}
		
		String groupId = inputParams.get("groupId");
		
		if (groupId != null && !groupId.equals("")) {
			if (mode.equals("GET_INIT_DATA")) {
				UserGroupResponse responseObj = GroupAjaxHandler.getUserGroup(database, ajaxVO.getUserId(), inputParams);

				// put it to session
				if (responseObj != null) {
					ajaxVO.setResponseObj(responseObj);
					ajaxVO.setSuccess(true);
				} else {
					// group data not found ... most likely
					ajaxVO.setSuccess(false);
				}
			} else if (mode.equals("APPLY_TO_JOIN")) {
				// ?? if you are in the group, or applied before, deny??
				UserGroupResponse responseObj = GroupAjaxHandler.applyToJoin(database, ajaxVO.getUserId(), inputParams);
				
				if (responseObj != null && responseObj.getSuccess() == true) {
					ajaxVO.setResponseObj(new UserGroupResponse());
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
	
	/**
	 * Put paging info to session.  This will be kept for one keywords search only;
	 * If another comes, compare the keywords and see if necessary to re-search.
	 * @param ajaxVO
	 * @param sessionContext
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	private void handleSearchGroup(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		String keywords = inputParams.get("keywords");
		
		// allow empty keywords in the search - for now, so, set it to NONE for tracking ...
		if (keywords == null || keywords.equals("")) {
			keywords = "NONE";
			inputParams.put("keywords", "NONE");
		}
		
		if (keywords != null && !keywords.equals("")) {
			keywords = keywords.toLowerCase().trim();
			SearchGroupResponse responseObj = null;
			
			// check the one in cache/session is the one with the same keywords
			GenericPagination<GroupInfo> pagination = (GenericPagination<GroupInfo>) sessionContext.getObject(GroupAction.SEARCH_GROUP);
			if (pagination == null || !keywords.equals(pagination.getKeyInfo())) {
				// not find it
				// debug
				System.out.println("search object NOT found in session ....");
				
				pagination = GroupAjaxHandler.getSearchGroup(database, ajaxVO.getUserId(), inputParams);
				
				// put it to session
				if (pagination != null) {
					// debug
					System.out.println("Insert search object into session ....");
					
					pagination.setKeyInfo(keywords);
					sessionContext.setObject(GroupAction.SEARCH_GROUP, pagination);
				}
			} else {
				// debug
				System.out.println("search object found in session ....");
			}
			
			// debug
			System.out.println("index: " + pagination.getIndexList().size());
			
			String pageIndex = inputParams.get("pageIndex");
			responseObj = constructSearchGroupResponse(pagination, pageIndex);

			ajaxVO.setResponseObj(responseObj);
			ajaxVO.setSuccess(true);
		} else {
			ajaxVO.setSuccess(false);
		}
	}
	
	/** get the right page content from the pagination object */
	@SuppressWarnings("unchecked")
	private SearchGroupResponse constructSearchGroupResponse(GenericPagination<GroupInfo> pagination, String pageIndex) {
		SearchGroupResponse responseObj = new SearchGroupResponse();
		
		int pageIndexInt = 0;
		try {
			pageIndexInt = (new Integer(pageIndex)).intValue();
		} catch (Exception e) {
			// warning here
			LOGGER.warn("Search group, pageIndex not integer: <" + pageIndex + ">");
		}
		
		PageInfo<GroupInfo> pageInfo = pagination.getPageInfo(pageIndexInt, null, true);
		responseObj.setIndexList(pagination.getIndexList());
		responseObj.setRowList(pageInfo.getPageItemList());
		
		int startCnt = pageInfo.getStartIndex() + 1;
		int endCnt = pageInfo.getEndIndex() + 1;
		String title = "Results: " + startCnt + " - " + endCnt;
		responseObj.setPageTitle(title);
		
		return responseObj;
	}
	
	private void handleUpdateGroup(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		
		UpdateGroupResponse responseObj = null;
		String mode = inputParams.get("mode");  // GET_INIT_DATA, SUBMIT_FORM
		if (mode != null && mode.equals("GET_INIT_DATA")) {
			responseObj = GroupAjaxHandler.getInitDataForUpdateGroup(database, ajaxVO.getUserId(), inputParams);
		} else if (mode != null && mode.equals("SUBMIT_FORM")) {
			responseObj = GroupAjaxHandler.processUpdateGroup(database, ajaxVO.getUserId(), inputParams);
		} else {
			// not handled
			throw new Exception("Aajx, update group: mode not handled: " + mode);
		}

		ajaxVO.setResponseObj(responseObj);
		ajaxVO.setSuccess(true);
	}
	
	private void handleAddWatch(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		
		AddwatchResponse responseObj = null;
		String mode = inputParams.get("mode");  // GET_INIT_DATA, SUBMIT_FORM
		if (mode != null && mode.equals("GET_INIT_DATA")) {
			responseObj = WatchListAjaxHandler.getInitData(database, ajaxVO.getUserId(), inputParams);
		} else if (mode != null && mode.equals("SUBMIT_FORM")) {
			responseObj = WatchListAjaxHandler.processAddwatch(database, ajaxVO.getUserId(), inputParams);
		} else {
			// not handled
			throw new Exception("Aajx, addwatch: mode not handled: " + mode);
		}

		ajaxVO.setResponseObj(responseObj);
		ajaxVO.setSuccess(true);
	}
	
	private void handleDeleteWatch(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		
		AddwatchResponse responseObj = null;
		String mode = inputParams.get("mode");  // GET_INIT_DATA, SUBMIT_FORM
		if (mode != null && mode.equals("SUBMIT_FORM")) {
			responseObj = WatchListAjaxHandler.processDeleteWatch(database, ajaxVO.getUserId(), inputParams);
		} else {
			// not handled
			throw new Exception("Aajx, deletewatch: mode not handled: " + mode);
		}

		ajaxVO.setResponseObj(responseObj);
		ajaxVO.setSuccess(true);
	}

	private void handleAgeSearch(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		AgeSrchDAO ageDao = new AgeSrchDAO();
		ageDao.setDatabase(database);
		
		// query for AgeSrch handler to get agelist
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		AgeSrchHandler handler = new AgeSrchHandler();
		
		String ageType = inputParams.get("ageType");  // not used for now, doing 50/100 aging now
		String selectRange = inputParams.get("selectRange");
		if (selectRange == null) {
			selectRange = "0";  //??? error or default
		}
			
		@SuppressWarnings("unchecked")
		Vector<StockAge> ageList = handler.getStockAgeList(ageType, selectRange, ageDao);
		
		AgesrchResponse responseObj = new AgesrchResponse();
		responseObj.setRowList(ageList);
		ajaxVO.setResponseObj(responseObj);
		ajaxVO.setSuccess(true);
	}

	private void handleBasicScan(AjaxVO ajaxVO, WebSessionContext sessionContext) throws Exception {
		// scan history
		ScanDAO scanDao = new ScanDAO();
		scanDao.setDatabase(database);
		
		// query for basic scan to call the following method
		// public Vector getScanList(String scanKey, String selectType,
		// 		String scanDate, int period, ScanDAO scanDao) throws Exception {
		Map<String, String> inputParams = ajaxVO.getRequestParams();
		ScanHandler handler = new ScanHandler();
		
		int period = 0;
		if (inputParams.containsKey("period")) {
			period = (new Integer(inputParams.get("period"))).intValue();
		}
		
		String scanKey = inputParams.get("scanKey");
		if (scanKey == null) {
			scanKey = "XU";  //??? error or default
		}
			
		@SuppressWarnings("unchecked")
		Vector<ScanHistoryVO> scanList = handler.getScanListNew(scanKey, 
				inputParams.get("selectType"), inputParams.get("scanDate"), period,
				inputParams.get("sortColumn"), inputParams.get("sortOrder"),scanDao);
		
		BasicScanResponse responseObj = new BasicScanResponse();
		responseObj.setRowList(scanList);
		ajaxVO.setResponseObj(responseObj);
		ajaxVO.setSuccess(true);
	}
	
	

}