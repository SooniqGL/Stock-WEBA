package com.greenfield.ui.action.analyze;


import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.ui.handler.perform.PerformHandler;
import com.greenfield.common.object.perform.PerformVO;

/**
 * @author zhangqx
 */
public class PerformAction extends BaseAction {
	
	public static final String BASIC_PERFORM = "basicperform";

	private PerformVO inputVO = new PerformVO();
	
	/**
	 * Performs the action for the user logoff request. 
	 * 
	 * @param	form		the action form.
	 * @param	request		the http request. 
	 * @return	the action string as a result of the action performed.
	 * @exception	CnetException
	 */
	public String executeAction()
		throws Exception {
		String mode = inputVO.getMode();
		String type = inputVO.getType();
		String trend = inputVO.getTrend();
		String returnStr = BASIC_PERFORM;
	
		System.out.println("scan mode:" + mode);
		System.out.println("scan type:" + type);
		System.out.println("scan trend:" + inputVO.getTrend());
			
		try {
			if (mode == null || mode.equals("") || mode.equals(ScanModes.BLANK)) {
				inputVO.setScanList(new Vector());
				inputVO.setShowTitle("");
			} else {
				PerformHandler handler = new PerformHandler();
				handler.execute(user, inputVO, sessionContext);				
			} 
		} catch (Exception e) {
			// log error
			e.printStackTrace();
		}
	
		//return the user to the scan screen.
		return returnStr;

	}

	public PerformVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(PerformVO inputVO) {
		this.inputVO = inputVO;
	}
	
	
}