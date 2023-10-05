package com.greenfield.ui.action.scan;

import java.util.Vector;

import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.ui.handler.agesrch.AgeSrchHandler;
import com.greenfield.common.object.scan.AgeSrchVO;

/**
 * @author zhangqx
 */
public class AgeSrchAction extends BaseAction {
	/** forward page - used here and strus-config.xml */
	public static final String AGESRCH_BASIC = "agesrchbasic";
	public static final String AGESRCH_RESULT = "agesrchresult";
	public static final String AGESRCH_REPORT = "agesrchreport";

	// These TYPES used by pages
	public static final String BASIC = "basic"; // as default, in not defined
	public static final String SEARCH = "search";
	public static final String REPORT = "report";

	private AgeSrchVO inputVO = new AgeSrchVO();

	/**
	 * Performs the action for the user logoff request.
	 * 
	 * @param form
	 *            the action form.
	 * @param request
	 *            the http request.
	 * @return the action string as a result of the action performed.
	 * @exception Exception
	 */
	public String executeAction() throws Exception {

		String mode = inputVO.getMode();
		String type = inputVO.getType();
		String returnStr = AGESRCH_BASIC;
		if (inputVO.getShowAllCharts() == null
				|| inputVO.getSelectRange() == null
				|| inputVO.getSelectRange().equals("")) {
			inputVO.setShowAllCharts("N");
		}

		// *
		System.out.println("action mode:" + mode);
		System.out.println("action type:" + type);

		try {
			if (mode == null || mode.equals("") || mode.equals(ScanModes.BLANK)) {
				// go to the basic page
				inputVO.setMode(BASIC);
				AgeSrchHandler handler = new AgeSrchHandler();
				handler.execute(user, inputVO, sessionContext);
				returnStr = AGESRCH_BASIC;
			} else if (mode != null && mode.equals(SEARCH)) {
				AgeSrchHandler handler = new AgeSrchHandler();
				handler.execute(user, inputVO, sessionContext);
				returnStr = AGESRCH_RESULT;
			} else if (mode != null && mode.equals(REPORT)) {
				AgeSrchHandler handler = new AgeSrchHandler();
				handler.execute(user, inputVO, sessionContext);
				returnStr = AGESRCH_REPORT;
			}

		} catch (Exception e) {
			// log error
			e.printStackTrace();
		}

		// return the user to the scan screen.
		return returnStr;
	}

	public AgeSrchVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(AgeSrchVO inputVO) {
		this.inputVO = inputVO;
	}

}

/* end of code */