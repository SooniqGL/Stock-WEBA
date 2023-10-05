package com.greenfield.ui.handler.group;

import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.GroupConstants;
import com.greenfield.common.constant.SessionKeys;
import com.greenfield.common.dao.group.GroupDAO;
import com.greenfield.common.object.group.MyGroupWrapper;
import com.greenfield.common.object.group.UserGroupVO;

public class GroupUtil {
	private static final Logger LOGGER = Logger.getLogger(GroupUtil.class); 
	
	/**
	 * Note: the session cache thing is not good for group management; as constants changes; isOwner() is only one used, as 
	 * if a new group is created by the user, cache is refreshed.
	 * However, this session cache - will be greatly used in site message control.
	 */
	
	// this can be called anywhere, group management, or site message stuff.
	public static Vector getMyGroupListFromSession(GroupDAO groupDao, String userId, WebSessionContext sessionContext) throws Exception {
		MyGroupWrapper wrapper = getMyGroupWrapper(groupDao, userId, sessionContext);
		return wrapper.getMyGroupList();
	}
	
	// reset is called, if we know some group information is changed - if I have crated one new group.
	// for invited approved, need to get next session to see it.
	public static void resetMyGroupWrapper(WebSessionContext sessionContext) {
		sessionContext.removeObject(SessionKeys.MY_GROUP_WRAPPER);
		
	}
	
	public static boolean isOwner(GroupDAO groupDao, String userId, WebSessionContext sessionContext, String groupId) throws Exception {
		MyGroupWrapper wrapper = getMyGroupWrapper(groupDao, userId, sessionContext);
		return wrapper.isOwner(groupId);
	}
	
	public static boolean haveJoined(GroupDAO groupDao, String userId, WebSessionContext sessionContext, String groupId) throws Exception {
		MyGroupWrapper wrapper = getMyGroupWrapper(groupDao, userId, sessionContext);
		return wrapper.haveJoined(groupId);
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private static MyGroupWrapper getMyGroupWrapper(GroupDAO groupDao, String userId, WebSessionContext sessionContext) {
		
		MyGroupWrapper wrapper = (MyGroupWrapper) sessionContext.getObject(SessionKeys.MY_GROUP_WRAPPER);
		if (wrapper != null) {
			// debug
			System.out.println("Good ... Get my group wrapper from session ...");
			return wrapper;
		} 
		
		// build from DB
		wrapper = new MyGroupWrapper();
		
		try {
			Vector myGroupList = groupDao.getUserGroupListFromDB(null, userId, GroupConstants.ACCOUNT_ACTIVE, null);
			
			if (myGroupList != null && myGroupList.size() > 0) {
				wrapper.setMyGroupList(myGroupList);
				
				// load the info to the HashMap for easy retrieving
				for (int i = 0; i < myGroupList.size(); i ++) {
					UserGroupVO vo = (UserGroupVO) myGroupList.get(i);
					if (userId.equals(vo.getOwnerId())) {
						wrapper.pushOwner(vo.getGroupId());
					} else {
						wrapper.pushGroup(vo.getGroupId());
					}
				}
			} 	
		} catch (Exception e) {
			LOGGER.warn("Issue To to load group wrapper: " + userId + ", error: " + e.getMessage());
		}
		
		// we need to push this to session then
		sessionContext.setObject(SessionKeys.MY_GROUP_WRAPPER, wrapper);
		
		return wrapper;
	}
}
