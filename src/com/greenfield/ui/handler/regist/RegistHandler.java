/*
 * Created on Jun 10, 2006
 */
package com.greenfield.ui.handler.regist;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.profile.LoginDAO;
import com.greenfield.common.dao.profile.RegistDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.user.User;
import com.greenfield.common.object.regist.RegistVO;

/**
 * @author qin
 *
 * Do public use registration
 */
public class RegistHandler  extends BaseHandler {
	public BaseObject doAction(BaseObject inputVO, WebSessionContext sessionContext) {
		RegistVO registVO = (RegistVO) inputVO;
		
		// call dao regist it
		RegistDAO dao = new RegistDAO();
		dao.setDatabase(database);
			
		try {
			if (registVO.getLoginId() == null || registVO.getLoginId().equals("") ||
				registVO.getPassword() == null || registVO.getPassword().equals("") ||
				registVO.getLname() == null || registVO.getLname().equals("") ||
				registVO.getEmail() == null || registVO.getEmail().equals("")) {
					
				// fail this guy
				registVO.setLegal(false);
				registVO.setMessage("Login id, password, Lname, or Email is not given.");
			} else {
				//	check if the login id is already in DB
				LoginDAO loginDAO = new LoginDAO();
				loginDAO.setDatabase(database);
				User dbUser = loginDAO.getUserFromDB(registVO.getLoginId());
				
				if (dbUser != null) {
					registVO.setLegal(false);
					registVO.setMessage("Login id has already in our DB.  Please select another one.");
				} else {
					// register to DB
					dao.registUser(registVO);
				}		
			} 
		} catch (Exception e) {
			e.printStackTrace();
			registVO.setLegal(false);
			registVO.setMessage("Program error, please report this to Admin: " + e.getMessage());
		}
			
		return registVO;
	}
}
