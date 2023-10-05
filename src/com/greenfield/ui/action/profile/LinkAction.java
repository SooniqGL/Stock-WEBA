package com.greenfield.ui.action.profile;


import java.util.ArrayList;
import java.util.Arrays;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.user.User;
import com.greenfield.ui.handler.login.LoginHandler;
import com.greenfield.common.object.login.LoginVO;

/**
 * @author zhangqx
 *
 */
public class LinkAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	public static final String HOME = "home";
	public static final String ABOUTUS = "aboutus";
	public static final String CONTACTUS = "contactus";	
	public static final String RANDOM = "random";	
	
	public static final String MARKET_FLAG = "market-flag";	
	
	public static final ArrayList<String> pagelist = new ArrayList<String>(Arrays.asList("aboutus", "contactus", "register", "random"));
	
	private LoginVO inputVO = new LoginVO();
	
	private ArrayList<String>  countryCodeList = new ArrayList<String>(Arrays.asList("US"));
	
	public String executeAction() throws Exception {
		HttpServletRequest request = ServletActionContext.getRequest();
		
		String retString = LOGIN;
		
		// HANDLER -> is the return string
		
		System.out.println("HANDLER: " + HANDLER);
		
		try {
			if (HANDLER == null || HANDLER.equals("")) {
				retString = LOGIN;
			} else {
				if (pagelist.contains(HANDLER)) {
					retString = HANDLER;
				} 
			}
		} catch (Exception e) {
			
			//c_logger.instr(ke);	
			e.printStackTrace();
			request.setAttribute("error", "Some system error: " + e.getMessage());
		}
				
		
		return retString;  
	}
	

	public LoginVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(LoginVO inputVO) {
		this.inputVO = inputVO;
	}


	public ArrayList<String> getCountryCodeList() {
		return countryCodeList;
	}


	public void setCountryCodeList(ArrayList<String> countryCodeList) {
		this.countryCodeList = countryCodeList;
	}
	
	


}