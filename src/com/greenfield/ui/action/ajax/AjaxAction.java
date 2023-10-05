package com.greenfield.ui.action.ajax;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts2.ServletActionContext;

import com.greenfield.ui.handler.ajax.AjaxHandler;
import com.greenfield.ui.handler.ajax.SecondAjaxHandler;
import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.util.JSONUtil;
import com.greenfield.common.object.ajax.AjaxVO;


public class AjaxAction extends BaseAction {
	private static final long serialVersionUID = 1L;
	
	public static final String SUCCESS = "success";
	
	// list that current Ajax supports
	public static final ArrayList<String> pagelist = new ArrayList<String>(
			Arrays.asList(
					"basicscan", "agesrchresult", "addwatch", "deletewatch",
					"updategroup", "searchgroup", "usergroup", 
					"groupuserlist", "updateuserbyowner", "grouprequestlist",
					"processrequestsbyowner", "answerinvite"    
					));
	
	public static final ArrayList<String> messagePagelist = new ArrayList<String>(
			Arrays.asList(
					"getpagelist", "getmessagelist", "messagedetail", "getreplylist", "searchmessage",
					"getthreadlist"
					));
	
	private BaseObject responseObj;
	
	/** 
	 * Notes: Struts2 JSON plug in takes on List/Map here, ArrayList, or HashMap will not work in the declare.
	 * We use Map for all our input parameters.
	 **/
	// private List<String> dataList = new ArrayList<String>();
	private Map<String, String> data = new HashMap<String, String>();
	
	AjaxVO inputVO = new AjaxVO();
	
	public String executeAction() throws Exception {
		String retString = SUCCESS;
		
		// HANDLER -> is the return string
		System.out.println("Ajax request mode: " + HANDLER  + ", data: " + getData());
		
		// pass the data to inputVO, so to handler
		inputVO.setRequestParams(data);
		long bgT = System.currentTimeMillis();
		
		try {
			/**
			 * Handle only if the HANDLER IS REGISTERED HERE
			 */
			if (HANDLER != null && !HANDLER.equals("")) {
				inputVO.setRequestType(HANDLER);
				inputVO.setUserId(user.getUserId());
				
				if (pagelist.contains(HANDLER)) {	
					AjaxHandler ajaxHandler = new AjaxHandler();
					inputVO = (AjaxVO) ajaxHandler.execute(user, inputVO, sessionContext);
				} else if (messagePagelist.contains(HANDLER)) {
					SecondAjaxHandler ajaxHandler = new SecondAjaxHandler();
					inputVO = (AjaxVO) ajaxHandler.execute(user, inputVO, sessionContext);
				}
			}
		} catch (Exception e) {	
			e.printStackTrace();
			// request.setAttribute("error", "Some system error: " + e.getMessage());
			inputVO.setSuccess(false);
		}
		
		long spT = System.currentTimeMillis() - bgT;
		System.out.println("Ajax time spent: " + spT);
		
		// double check if the response has good status
		if (inputVO.getSuccess() != false) {
			// need to return error JSON TO client
			responseObj = inputVO.getResponseObj();
			responseObj.setSuccess(true);
			if (responseObj.getMessage() == null) {
				responseObj.setMessage("OK");
			}
		} else {
			responseObj = new BaseObject();
			responseObj.setSuccess(false);
			responseObj.setMessage("General Error");
		}
				
		
		return retString;  
	}
	


	public AjaxVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(AjaxVO inputVO) {
		this.inputVO = inputVO;
	}


	public BaseObject getResponseObj() {
		return responseObj;
	}

	public void setResponseObj(BaseObject responseObj) {
		this.responseObj = responseObj;
	}

	public Map<String, String> getData() {
		return data;
	}

	public void setData(Map<String, String> data) {
		this.data = data;
	}

	

}