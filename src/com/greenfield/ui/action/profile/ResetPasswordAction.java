package com.greenfield.ui.action.profile;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.login.ResetPasswordVO;
import com.greenfield.ui.handler.login.ResetPasswordHandler;


public class ResetPasswordAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	/** forward page */
	public static final String INPUT 	= "input";
	public static final String SUBMIT 	= "submit";
		
	private ResetPasswordVO inputVO = new ResetPasswordVO();
	
	/**
	 * Performs the action for the user reset password request. 
	 * 
	 * @exception	Exception
	 */
	public String executeAction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String mode = inputVO.getMode();
		
		String returnStr = INPUT;
		
		//System.out.println("regist mode:" + mode);
		
		try {
			if (HANDLER != null && HANDLER.equals(SUBMIT)) {
				ResetPasswordHandler handler = new ResetPasswordHandler();
				inputVO = (ResetPasswordVO) handler.execute(null, inputVO, sessionContext);				
				
				if (inputVO.getSuccess() == true) {
					request.setAttribute("resetconfirm", "Password is reset successfully.  Please check your email for temporary password.");
					
					// THIS IS USED WHEN as it is forwarding to LOGIN action;
					// If we do not do this: the variable HANDLER is forwarded to next action.
					// Which then we cannot tell if the HANDLER is real or carry over from here.
					//request.setAttribute("ISFORWARD", "true");
					returnStr = INPUT; // SUCCESS;
				} else {
					request.setAttribute("reseterror", inputVO.getMessage());
					returnStr = INPUT;
				}
			}
			
		} catch (Exception e) {
			// log error
			e.printStackTrace();
			request.setAttribute("reseterror", "We have technical error to process your request.  Please try later.");
			returnStr = INPUT;
		}
		
		//return the user to the scan screen.
		return returnStr;
	}

	public ResetPasswordVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(ResetPasswordVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
}