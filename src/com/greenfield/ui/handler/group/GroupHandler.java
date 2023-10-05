package com.greenfield.ui.handler.group;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.GroupConstants;
import com.greenfield.common.constant.UserStatusCodes;
import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.dao.member.PortfolioDAO;
import com.greenfield.common.dao.profile.UserAdminDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.group.GroupInfo;
import com.greenfield.common.object.group.UserGroupVO;
import com.greenfield.common.object.group.UserInviteVO;
import com.greenfield.common.object.group.UserRequestVO;
import com.greenfield.common.object.member.MyInfoVO;
import com.greenfield.common.object.member.PortfolioInfo;
import com.greenfield.common.object.member.PortfolioVO;
import com.greenfield.common.object.user.User;
import com.greenfield.common.object.user.UserInfo;
import com.greenfield.common.security.PasswordHash;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.IDGenerator;
import com.greenfield.ui.action.group.GroupAction;
import com.greenfield.ui.action.member.MyInfoAction;
import com.greenfield.ui.cache.DBCachePool;


public class GroupHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(GroupHandler.class); 
	

	/**
	 * Make sure this person has permission to change records or see stuff.
	 */
	protected boolean preSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		UserGroupVO userGroupVO = (UserGroupVO) obj;
		
		String mode = userGroupVO.getMode();
		String type = userGroupVO.getType();
		String userId = user.getUserId();
		
		boolean pass = false;
		if (mode != null && !mode.equals("")) {
			try {
				if (mode.equals(GroupAction.UPDATE_GROUP)) {
					String groupId = userGroupVO.getGroupId();
					// check if he can update the group ...
					pass=true;
				}
				
				pass = true;
			} catch (Exception e) {
				e.printStackTrace();
				LOGGER.warn("GroupHandler:preSecurityCheck error: for " + user.getUserId() + ", error: " + e.getMessage());
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
	
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		UserGroupVO userGroupVO = (UserGroupVO) obj;
		
		String mode = userGroupVO.getMode();
		String type = userGroupVO.getType();
		GroupDAO groupDao = new GroupDAO();
		groupDao.setDatabase(database);
				
		String returnType = "";
		String userId = userGroupVO.getUserId();
		if (mode != null && mode.equals(GroupAction.GROUP_HOME)) {
			try {
				// the group user has joined (do not used the one from session cache for group management)
				Vector userGroupList = groupDao.getUserGroupListFromDB(null, userId, GroupConstants.ACCOUNT_ACTIVE, null);
				userGroupVO.setUserGroupList(userGroupList);
				
				// see if any request pending ...
				Vector userRequestList = groupDao.getUserRequestListFromDB(null, userId, GroupConstants.REQUEST_OPEN);
				if (userRequestList == null) {
					userRequestList = new Vector<UserRequestVO>();
				}
				userGroupVO.setUserRequestList(userRequestList);
				
				// check if I am invited
				Vector userInviteList = groupDao.getUserInviteListFromDB(userId, null, GroupConstants.INVITE_OPEN);
				if (userInviteList == null) {
					userInviteList = new Vector<UserInviteVO>();
				}
				
				userGroupVO.setUserInviteList(userInviteList);

				returnType = "success";	
			} catch (Exception ex) {
				// update error
				ex.printStackTrace();
				userGroupVO.setSuccess(false);
				returnType="generalerror";
				LOGGER.warn("Error in Group Home: " + ex.getMessage());
			}
		} else if (mode != null && mode.equals(GroupAction.GROUP_USER_LIST)) {
			try {
				// the group user has joined
				Vector userGroupList = groupDao.getUserGroupListFromDB(null, userId, GroupConstants.ACCOUNT_ACTIVE, null);
				userGroupVO.setUserGroupList(userGroupList);
				returnType = "success";	
			} catch (Exception ex) {
				// update error
				ex.printStackTrace();
				userGroupVO.setSuccess(false);
				returnType="generalerror";
				LOGGER.warn("Error in Group Home: " + ex.getMessage());
			}
		} else if (mode != null && mode.equals(GroupAction.GROUP_REQUEST_LIST)) {
			try {
				// get all the groups own by you, and has outstanding requests
				Vector groupRequestList = groupDao.getUserRequestListByOwner(userId, GroupConstants.REQUEST_OPEN);
				userGroupVO.setGroupRequestList(groupRequestList);
				
				// verify if groupId is passed, and if is in the groupRequestList list
				String groupId = userGroupVO.getGroupId();
				boolean found = false;
				if (groupId != null && !groupId.equals("") 
						&& groupRequestList != null && groupRequestList.size() > 0) {
					for (int i = 0; i < groupRequestList.size(); i++) {
						GroupInfo info = (GroupInfo) groupRequestList.get(i);
						if (groupId.equals(info.getGroupId())) {
							found = true;
							break;
						}
					}
				}
				
				// reset if not found; if we found it, we will load the list at the page load.
				if (found == false) {
					userGroupVO.setGroupId("");
				}
				
				returnType = "success";	
			} catch (Exception ex) {
				// update error
				ex.printStackTrace();
				userGroupVO.setSuccess(false);
				returnType="generalerror";
				LOGGER.warn("Error in Group Home: " + ex.getMessage());
			}
		} else if (mode != null && mode.equals(GroupAction.NEW_GROUP) &&
				type != null && type.equals(GroupAction.UPDATE)) {
			// need to create the group - as this person is owner of the group
			/*group_id   	varchar(16) primary key,
				owner_id		varchar(16) not null,
				cre_date		timestamp,
				name	           varchar(50),
				type            varchar(5),
				status          char(1),
				accepting       char(1),
				public_note     varchar(2000)
				*/
			if (userGroupVO.getGroupName() == null || userGroupVO.getGroupName().equals("")) {
				// not good
				userGroupVO.setSuccess(false);
			} else {
				String today = DateUtil.getDateStringInYYYYMMDDFormat(null);
				String groupId = IDGenerator.getNextId(database, IDGenerator.USERGROUP_ID_PREFIX);
				userGroupVO.setGroupId(groupId);
				userGroupVO.setCreDate(today);
				userGroupVO.setOwnerId(userId);
				userGroupVO.setGroupStatus("A");    // FOR active
				userGroupVO.setGroupType("P");   // get from screen?
				userGroupVO.setAccepting("Y");
				// name, public note - from screen - not null
				groupDao.addGroupInfoToDB(userGroupVO);
				
				// add user/group table
				userGroupVO.setUserCreDate(today);
				userGroupVO.setUserId(userId);
				userGroupVO.setPermission("OWN");   // what the permission?
				userGroupVO.setUserGroupStatus("ACT");
				groupDao.addUserGroupToDB(userGroupVO);
				
				// reset the wrapper
				GroupUtil.resetMyGroupWrapper(sessionContext);
				
				// add the group list to user for display in home area
				// it is OK to use from session this time, as it is right after reset!
				Vector userGroupList = GroupUtil.getMyGroupListFromSession(groupDao, userId, sessionContext);
				userGroupVO.setUserGroupList(userGroupList);
				
				userGroupVO.setSuccess(true);
			}
			
		} else if (mode != null && mode.equals(GroupAction.NEW_INVITE)) {
			// only load the group info if: add invite fails, or it is first time to load the page.
			boolean loadGroupInfo = true;
			String groupId = userGroupVO.getGroupId();
			String inviteId = userGroupVO.getInviteId();
			
			if (type != null && type.equals(GroupAction.UPDATE)) {
				System.out.println("here new invite udpate");
				// group id, invite id are given, the current user will be the "invite by" guy
				// security is done as follows: 
				// 1) you are in the group; 2) group is accepting; 3) inviteId is one of users (active); 
				// 4) the invited user is not in the group;

				if (groupId == null || groupId.equals("") || inviteId == null || inviteId.equals("")) {
					throw new Exception("doing invite, but group id, or invite id are null.");
				}
				
				// change to upper case
				inviteId = inviteId.toUpperCase();
				
				// verify 
				boolean hasPassed = true;
				UserAdminDAO adminDao = new UserAdminDAO();
				adminDao.setDatabase(database);
				UserInfo userInfo = adminDao.getUserFromDBLite(inviteId);
				if (userInfo == null || !userInfo.getStatusCd().equals(UserStatusCodes.ACTIVE)) {
					// not found, or not active
					userGroupVO.setSuccess(false);
					userGroupVO.setMessage("The invited person's ID not valid one, or user not active.");
					hasPassed = false;
				} else {
					// good, check other stuff
					Vector<UserGroupVO> voList = groupDao.getUserGroupListFromDB(groupId, userId, GroupConstants.ACCOUNT_ACTIVE, null);
					if (voList != null && voList.size() > 0) {
						// should be just 1
						UserGroupVO vo = (UserGroupVO) voList.get(0);
						if (GroupConstants.ACCEPTING_NO.equals(vo.getAccepting())) {
							// error, should not come here, if it is not acceptings, UI should block this guy.
							throw new Exception("doing invite, but group is not accepting.");
						}
					} else {
						// user is not in this group, how he gets to here - should not get here
						// check this for security reason
						throw new Exception("doing invite, the current user is not in the group.");
					}
				
					// check if it is the group already - active or not active
					Vector<UserGroupVO> voList2 = groupDao.getUserGroupListFromDBLite(groupId, inviteId, null);
					if (voList2 != null && voList2.size() > 0) {
						// this user is already in DB - should not create any invite
						userGroupVO.setSuccess(false);
						userGroupVO.setMessage("The invited person is already in the group.");
						hasPassed = false;
					} 
				}
				
				if (hasPassed) {
					try {
						// check once
						Vector list2 = groupDao.getUserInviteListFromDBLite(groupId, inviteId, userId, null);
						if (list2 != null && list2.size() > 0) {
							userGroupVO.setSuccess(false);
							userGroupVO.setMessage("There is technical error.  Most likely you may have invited the same user before.");
						} else {
							UserInviteVO info = new UserInviteVO();
							info.setGroupId(groupId);
							info.setInviteId(inviteId);
							info.setInviteBy(userId);
							
							info.setInviteDt(DateUtil.getDateStringInYYYYMMDDFormat(null));
							info.setStatus(GroupConstants.INVITE_OPEN);
							groupDao.addUserInviteToDB(info);
							
							// add the invite successfully
							userGroupVO.setSuccess(true);
							userGroupVO.setMessage("Successfully sent the invite.");
						}
						//loadGroupInfo = false;
					} catch (Exception ex) {
						// most likely the user is invited the same user before
						LOGGER.warn("doing invite - problem to insert for " + groupId 
								+ ", invited id: " + inviteId + ", by: " 
								+ userId + ", error: " + ex.getMessage());
						userGroupVO.setSuccess(false);
						userGroupVO.setMessage("There is technical error.  Most likely you have invited the same user before.");
					}
				}
				
			}
			
			if (loadGroupInfo == true && groupId != null && !groupId.equals("")) {
				// .....
				GroupInfo info = new GroupInfo();
				info.setGroupId(groupId);
				GroupInfo infoInDB = (GroupInfo) groupDao.getGroupInfoFromDB(groupId);
				
				userGroupVO.setGroupName(infoInDB.getGroupName());
				userGroupVO.setNumUsers(infoInDB.getNumUsers());
			}
		} else if (mode != null && mode.equals(GroupAction.UPDATE_GROUP)) {
			//???? this mode is not used - Ajax is used instead ...
			if (type != null && type.equals(GroupAction.UPDATE)) {
				// CHECK IF HE CAN UPDATE OR NOT
				/*	group_id   	varchar(16) primary key,
					owner_id		varchar(16) not null,
					cre_date		timestamp,
					name	           varchar(50),
					type            varchar(5),
					status          char(1),
					accepting       char(1),
					public_note     varchar(2000)
					*/
				//userGroupVO.setOwnerId(userId);
				//userGroupVO.setStatus("ACT");
				//userGroupVO.setType("P");   // get from screen?
				userGroupVO.setAccepting("Y");
				// name, public note - from screen - not null
				groupDao.updateGroupInfoToDB(userGroupVO);
			}
		} else if (mode != null && mode.equals(GroupAction.UPDATE_USER_ACCT)) {
			// owner to update another user?
			userGroupVO.setUserId(userId);
			userGroupVO.setPermission("OWN");   // what the permission?
			userGroupVO.setUserGroupStatus("ACT");
			groupDao.addUserGroupToDB(userGroupVO);
		} 
		
		
		
		if (returnType.equals("generalerror")) {
			userGroupVO.setSuccess(false);
			userGroupVO.setMessage("There was a techncial error for your request.  Please try again later.");
		} else if (returnType.equals("deny")) {
			userGroupVO.setSuccess(false);
			userGroupVO.setMessage("Your input does not match our records.  Please try again.");
		} else if (returnType.equals("success")){
			userGroupVO.setSuccess(true);
			userGroupVO.setMessage("Your request was executed successfully.");
		}
		
		return userGroupVO;
	}

}
