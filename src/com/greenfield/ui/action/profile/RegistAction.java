/*
 * Created on Jun 10, 2006
 */
package com.greenfield.ui.action.profile;


import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.regist.RegistHandler;
import com.greenfield.common.object.regist.RegistVO;

/**
 * @author zhangqx
 */
public class RegistAction extends BaseAction {
	/** forward page */
	public static final String REGISTER 	= "register";
	public static final String SUBMIT 		= "submit";
		
	private RegistVO inputVO = new RegistVO();
	
	/**
	 * Performs the action for the user logoff request. 
	 * 
	 * @param	form		the action form.
	 * @param	request		the http request. 
	 * @return	the action string as a result of the action performed.
	 * @exception	Exception
	 */
	public String executeAction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		RegistVO registVO = getInputVO();
		//String mode = registVO.getMode();
		
		String returnStr = INPUT;
		
		//System.out.println("regist mode:" + mode);
		
		try {
			if (HANDLER != null && HANDLER.equals(SUBMIT)) {
				RegistHandler handler = new RegistHandler();
				registVO = (RegistVO) handler.execute(registVO, registVO, sessionContext);				
				
				if (registVO.isLegal()) {
					request.setAttribute("confirm", "Registered successfully.  Please login.");
					
					// THIS IS USED WHEN as it is forwarding to LOGIN action;
					// If we do not do this: the variable HANDLER is forwarded to next action.
					// Which then we cannot tell if the HANDLER is real or carry over from here.
					request.setAttribute("ISFORWARD", "true");
					returnStr = SUCCESS;
				} else {
					request.setAttribute("error", registVO.getMessage());
					returnStr = INPUT;
				}
			}
			
		} catch (Exception e) {
			// log error
			e.printStackTrace();
			request.setAttribute("error", e.getMessage());
			returnStr = INPUT;
		}
		
		//return the user to the scan screen.
		return returnStr;
	}

	public RegistVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(RegistVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
}