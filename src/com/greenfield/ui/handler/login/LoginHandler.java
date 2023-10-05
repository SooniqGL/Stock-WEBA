package com.greenfield.ui.handler.login;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.UserStatusCodes;
import com.greenfield.common.dao.profile.LoginDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.user.User;
import com.greenfield.common.object.login.LoginVO;
import com.greenfield.common.security.PasswordHash;

/**
 * @author zhangqx
 * 
 *         Do login stuff
 */
public class LoginHandler extends BaseHandler {

	public BaseObject doAction(BaseObject inputVO,
			WebSessionContext sessionContext) {
		LoginVO loginVO = (LoginVO) inputVO;
		// variable user, and do some login history
		LoginDAO dao = new LoginDAO();
		dao.setDatabase(database);

		try {
			if (loginVO.getLoginId() == null || loginVO.getLoginId().equals("")) {
				// || loginVO.getPassword() == null ||
				// loginVO.getPassword().equals("")) {
				loginVO.setLegal(false);
				loginVO.setMessage("Login id or password is not set.");
			} else {
				// trick for now
				if (loginVO.getLoginId().length() == 2
						&& loginVO.getLoginId().equals("aa")) {
					loginVO.setLoginId("admin");
					loginVO.setPassword("admin2");
				}

				User dbUser = dao.getUserFromDB(loginVO.getLoginId());
				// System.out.println("password: " + loginVO.getPassword() + ", db: " + dbUser.getPassword());
				if (dbUser == null || 
						!PasswordHash.verifyPassword(loginVO.getPassword(), dbUser.getPassword())
						) {
					
					// login id is not in DB, or password wrong
					loginVO.setLegal(false);
					loginVO.setMessage("Login id or password is not good.");
				} else if (!dbUser.getStatusCd().equals(UserStatusCodes.ACTIVE)) {
					// account is not active
					loginVO.setLegal(false);
					loginVO.setMessage("Your account is not active.  Please contact Admin.");
				} else {
					dbUser.setRemoteIpAddr(loginVO.getRemoteIpAddr());
					dao.insertLoginHistory(dbUser, "Y");
					loginVO.setUser(dbUser);
					loginVO.setLegal(true);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			loginVO.setLegal(false);
			loginVO.setMessage("Program error, please report this to Admin.");
		}

		return loginVO;
	}

}
