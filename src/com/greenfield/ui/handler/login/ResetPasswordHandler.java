package com.greenfield.ui.handler.login;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.UserStatusCodes;
import com.greenfield.common.dao.profile.LoginDAO;
import com.greenfield.common.dao.profile.UserAdminDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.login.ResetPasswordVO;
import com.greenfield.common.object.user.User;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.GmailUtil;
import com.greenfield.common.util.PasswordUtil;

public class ResetPasswordHandler extends BaseHandler {

	public BaseObject doAction(BaseObject inputVO,
			WebSessionContext sessionContext) {

		ResetPasswordVO vo = (ResetPasswordVO) inputVO;
		// variable user, and do some login history
		LoginDAO dao = new LoginDAO();
		dao.setDatabase(database);

		try {
			if (vo.getLoginId() == null || vo.getLoginId().equals("")
				|| vo.getEmail() == null || vo.getEmail().equals("")) {
				vo.setSuccess(false);
				vo.setMessage("Login id or email is not set.");
			} else {
				// check db by user id
				User dbUser = dao.getUserFromDB(vo.getLoginId());
				if (dbUser == null || !vo.getEmail().toLowerCase().equals(dbUser.getEmail())) {
					// login id is not in DB or email is wrong
					vo.setSuccess(false);
					vo.setMessage("Record not found, or email not matching.");
				} else if (!dbUser.getStatusCd().equals(UserStatusCodes.ACTIVE)) {
					// account is not active
					vo.setSuccess(false);
					vo.setMessage("Your account is not active.  Please contact Admin.");
				} else {
					// we find the user, need to do: 1) generator temp pass; 2) Save to DB; 3) Send mail to user;
					String tempPass = PasswordUtil.generatePassword();
					
					// update the DB before send the email; set password status to T
					UserAdminDAO adminDao = new UserAdminDAO();
					adminDao.setDatabase(database);
					adminDao.changeUserPassword(dbUser.getUserId(), tempPass, "T");
					
					// send mail to customer
					String theMessge = composeMessage(dbUser, tempPass);
					GmailUtil.sendMail(vo.getEmail(), "Your temporary login for Sooniq is included", theMessge);
					
					vo.setSuccess(true);
					// vo.setMessage("Email is sent to you with temp password.");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			vo.setSuccess(false);
			vo.setMessage("Program error, please report this to Admin.");
		}

		return vo;
	}
	
	private String composeMessage(User dbUser, String password) {
		StringBuilder builder = new StringBuilder();
		
		builder.append("Dear ").append(dbUser.getFname()).append(":\n");
		builder.append("\tAs you have requsted, we generated a temporary password to you: ").append(password)
		.append(".  Please use it in next 24 hours to login to our site.  You will need to change password at login time."  )
		.append("\n\nThank you very much to use our system.\n\nSooniq System Admin\n")
		.append(DateUtil.getDateStringInMMDDYYYYFormat(null));
		
		return builder.toString();
	}

}
