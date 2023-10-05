package com.greenfield.ui.action.profile;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.object.user.User;
import com.greenfield.ui.handler.login.LoginHandler;
import com.greenfield.common.object.login.LoginVO;

/**
 * @author zhangqx
 *
 */
public class LoginAction extends BaseAction {
	private static final Logger LOGGER = Logger.getLogger(LoginAction.class); 
	
	private static final long serialVersionUID = 1L;
	
	public static final String SUCCESS 			= "success";
	public static final String SUBMIT 			= "submit";	
	public static final String CHANGEPASSWORD 	= "changepassword";
	public static final String MESSAGEHOME 		= "messagehome";	
	
	public static final String MARKET_FLAG = "market-flag";	
	
	LoginVO inputVO = new LoginVO();
	
	public String executeAction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String retString = INPUT;
		
		// HANDLER -> is the return string
		
		System.out.println("login mode: " + HANDLER);
		
		try {
			/*
			 * When forward from other page to the login, HANDLER may not work
		     * properly.  So, we need this attribute ISFORWARD to check.
			 * For now, we forward calls from Register page after it is done successfully and logout() 
			 * is also forwarding the call to this place. (isPublic is true for both case.)
			 */
			String isForward = (String) request.getAttribute("ISFORWARD");
			if ((isForward != null && isForward.equals("true")) ||
					HANDLER == null || HANDLER.equals("")) {
				retString = INPUT;
			} else if (HANDLER != null && HANDLER.equals(SUBMIT)) {
				// see if this device is mobile
				/*
				boolean isMobile = isMobile(request);
				if (isMobile) {
					sessionContext.setObject("ISMOBILE", "Y");
				} else {
					sessionContext.setObject("ISMOBILE", "N");
				}
				
				// debug
				System.out.println("ismobile: " + isMobile);
				*/
				
				LoginHandler loginHandler = new LoginHandler();
				inputVO.setRemoteIpAddr(request.getRemoteAddr());
				inputVO = (LoginVO) loginHandler.execute(inputVO, inputVO, sessionContext);
				
				if (inputVO.isLegal()) {	
					setUserInSession(request, inputVO);
					
					// for mobile, go to message home directly
					//String ismobile = (String) sessionContext.getObject("ISMOBILE");
					//if (ismobile != null && ismobile.equals("Y")) {
					//	retString = LoginAction.MESSAGEHOME;
					//} else {	
						if ("T".equals(inputVO.getPasswordStatus())) {
							// need to direct to password change page
							retString = LoginAction.CHANGEPASSWORD;
						} else {
							retString = LoginAction.SUCCESS;
						}
					//}
					
					// tracking
					LOGGER.warn("User Login: \n" + inputVO.getLoginId() +"\nLogId: " + inputVO.getLogId());
					
					
				} else {
					request.setAttribute("error", inputVO.getMessage());
					retString = INPUT;
				}
			} else {
				retString = HANDLER;
			}
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			request.setAttribute("error", "Some system error: " + e.getMessage());
			retString = INPUT;
		}
				
		
		return retString;  
	}
	
	/**
	 * Sets the user in session.
	 * 
	 * @param request
	 * @param verifiedUser
	 */
	private void setUserInSession(HttpServletRequest request, User verifiedUser) {
		// mostly, this is the same session, but insert user info to the session
		HttpSession userSession = request.getSession(true);
		userSession.setAttribute(SECURITY, verifiedUser);
	}


	public LoginVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(LoginVO inputVO) {
		this.inputVO = inputVO;
	}

}