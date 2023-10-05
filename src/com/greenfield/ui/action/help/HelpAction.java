/*
 * Created on Jul 15, 2006
 */
package com.greenfield.ui.action.help;

import javax.servlet.http.HttpServletRequest;





import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.user.User;

/**
 * @author qin
 */
public class HelpAction extends BaseAction {
	public static final String GENERAL_INFO = "generalinfo";
	
	public String executeAction()
		throws Exception {
						
		//String method = request.getMethod();
		//if (method.equalsIgnoreCase("get")) {
		//	return LOGIN; 
		//}
		
		String retString = GENERAL_INFO;

		try {
			// do nothing
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			//request.setAttribute("error", "Some system error: " + e.getMessage());
		}
				
		
		return retString;  
	}
	

}