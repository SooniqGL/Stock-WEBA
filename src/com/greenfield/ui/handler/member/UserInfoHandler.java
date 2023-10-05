/*
 * Created on Jul 31, 2006
 */
package com.greenfield.ui.handler.member;

import org.apache.log4j.Logger;

import com.greenfield.ui.action.member.MyInfoAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.profile.UserAdminDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.user.UserInfo;
import com.greenfield.common.object.member.MyInfoVO;
import com.greenfield.common.security.PasswordHash;

/**
 * @author qin
 */
public class UserInfoHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(UserInfoHandler.class); 
	

	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		MyInfoVO myInfoVO = (MyInfoVO) obj;
		String mode = myInfoVO.getMode();
		String type = myInfoVO.getType();
		UserAdminDAO infoDao = new UserAdminDAO();
		infoDao.setDatabase(database);
				
		UserInfo info = null;
		String returnType = "";
		if (mode != null && mode.equals(MyInfoAction.UPDATEMYINFO) &&
				type != null && type.equals(MyInfoAction.UPDATE)) {
			try {
				// make sure user cannot change the type code and status could be updated.
				myInfoVO.setTypeCd(null);
				myInfoVO.setStatusCd(null);
				
				// make sure login id, is not set to null
				if (myInfoVO.getLoginId() == null || myInfoVO.getLoginId().equals("")) {
					throw new Exception("Update My Info, login id is null.");
				}
				
				// update it, without change password
				infoDao.updateUserProfile(myInfoVO, false);
				returnType = "success";	
			} catch (Exception ex) {
				// update error
				ex.printStackTrace();
				myInfoVO.setSuccess(false);
				returnType="generalerror";
				LOGGER.warn("Error in update using info: " + ex.getMessage());
			}
		} else if (mode != null && (mode.equals(MyInfoAction.CHANGEPASSWORD) 
				|| mode.equals(MyInfoAction.CHANGEPASSWORD2)) &&
				type != null && type.equals(MyInfoAction.UPDATE)) {
			try {
				info = infoDao.getUserFromDB(myInfoVO.getUserId());
				String passwordInDB = info.getPassword();  // it is hashed
				if (PasswordHash.verifyPassword(myInfoVO.getPassword(), passwordInDB)) {
					// equal so
					// update password (newPassword is used) - change DB the password.
					infoDao.changeUserPassword(myInfoVO.getUserId(), myInfoVO.getNewPassword(), "P");
					returnType = "success";		
				} else {
					// deny the request
					returnType = "deny";
				}
				
			} catch (Exception ex) {
				// update error
				ex.printStackTrace();
				returnType = "generalerror";
				LOGGER.warn("Error in update using info: " + ex.getMessage());
			}
		}
		
		
		try {
			// if it is not set yet, query it once
			if (info == null) {
				info = infoDao.getUserFromDB(myInfoVO.getUserId());
			}
			
			if (info != null) {
				myInfoVO.setValues(info);	
				
				// the following two values need to reset back
				myInfoVO.setMode(mode);
				myInfoVO.setType(type);
			}
		} catch (Exception e) {
			LOGGER.warn("Error in querying user info: " + e.getMessage());
		}
		
		if (returnType.equals("generalerror")) {
			myInfoVO.setSuccess(false);
			myInfoVO.setMessage("There was a techncial error for your request.  Please try again later.");
		} else if (returnType.equals("deny")) {
			myInfoVO.setSuccess(false);
			myInfoVO.setMessage("Your input does not match our records.  Please try again.");
		} else if (returnType.equals("success")){
			myInfoVO.setSuccess(true);
			myInfoVO.setMessage("Your request was executed successfully.");
		}
		
		return myInfoVO;
	}

}
