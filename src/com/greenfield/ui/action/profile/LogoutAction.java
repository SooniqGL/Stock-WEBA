/*
 * Created on Jun 6, 2006
 */
package com.greenfield.ui.action.profile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.object.user.User;

/**
 * @author zhangqx
 */
public class LogoutAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public String executeAction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();

		// remove user's eligibility.
		removeUserEligibility(request);

		// set confirm message.
		request.setAttribute("confirm", getLogoutConfirm());
		request.setAttribute("ISFORWARD", "true");

		// return the user to the login screen.
		return SUCCESS;
	}

	/**
	 * Removes the user credentials for the user from the user session.
	 * 
	 * @param request
	 *            the HttpRequest to obtain a handle to the user session .
	 * @param user
	 *            the user credentials.
	 * @exception Exception.
	 */
	private void removeUserEligibility(HttpServletRequest request)
			throws Exception {

		HttpSession userSession = request.getSession(true);
		User user = (User) userSession.getAttribute(SECURITY);
		if (user != null) {
			userSession.removeAttribute(SECURITY);
			
			// reset the session context as well
			sessionContext = new WebSessionContext();
			userSession.setAttribute(WebSessionContext.WebSessionContextKey, sessionContext);
		}

		// if I call invalidate(), there is a problem to forward the request to a next action.
		// also, may need to track users outside on the public pages.
		// userSession.invalidate();
	}

	/**
	 * Gets the confirm message for the logout action.
	 * 
	 * @return the confirm message.
	 */
	private String getLogoutConfirm() {
		StringBuffer buff = new StringBuffer();
		buff.append("You are successfully logged out.");
		return buff.toString();
	}

}
