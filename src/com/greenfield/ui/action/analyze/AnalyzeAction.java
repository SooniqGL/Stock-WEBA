package com.greenfield.ui.action.analyze;

import javax.servlet.http.HttpServletRequest;

import com.greenfield.common.base.BaseAction;
import com.greenfield.ui.handler.analyze.AnalyzeHandler;
import com.greenfield.common.object.analyze.AnalyzeVO;

/**
 * @author zhangqx
 */
public class AnalyzeAction extends BaseAction {
	private static final long serialVersionUID = 1L;

	public static final String BASIC_ANALYZE = "basicanalyze";
	public static final String DYNAMIC_ANALYZE = "dynamicanalyze";
	public static final String BASIC_CALCULATOR = "basiccalculator";
	public static final String DYNAMIC_AJAX = "dynamicajax";

	public static final String BLANK = "blank";
	public static final String DYNAMIC_D = "D"; // dynamic page
	public static final String STATIC_S = "S"; // static page
	public static final String DYNAMIC_A = "A"; // for Ajax call

	private AnalyzeVO inputVO = new AnalyzeVO();

	/**
	 * Performs the action for the user logoff request.
	 * 
	 * @param form
	 *            the action form.
	 * @param request
	 *            the http request.
	 * @return the action string as a result of the action performed.
	 * @exception CnetException
	 */
	public String executeAction() throws Exception {
		AnalyzeVO analyzeVO = getInputVO();
		String mode = analyzeVO.getMode();

		// 1) pageStyle - has to be set for every call, because
		// if not set, the previous call pageStyle in session will take action,
		// which may not be good.
		// 2) For Static page, "option" can set to empty, but has to be set;
		// o.w. previous session value
		// will be taken. The worst case is to take value from Dynamical case.
		// 3) "mode" currently only needed for Ajax calls.
		String returnStr = "";
		String pageStyle = analyzeVO.getPageStyle();
		if (pageStyle != null && pageStyle.equals(DYNAMIC_D)) {
			returnStr = DYNAMIC_ANALYZE;
		} else if (pageStyle != null && pageStyle.equals(DYNAMIC_A)) {
			returnStr = DYNAMIC_AJAX;
		} else if (HANDLER != null && HANDLER.equals(DYNAMIC_ANALYZE)) {
			// if page style is not set, use HANDLER to adjust
			// this happens when bookmark is used, and while the client is logged in another TAB
			// In that case, the IE will use the session, and try to access the page, but 
			// it does not pass the mode/pageStyle variables.  So, here we need to use HANDLER to go to the right page
			// and include the right JavaScripts.
			returnStr = DYNAMIC_ANALYZE;
			analyzeVO.setPageStyle(DYNAMIC_D);
		} else {
			// default
			returnStr = BASIC_ANALYZE;
			analyzeVO.setPageStyle(STATIC_S);
		}

		// debug
		// System.out.println("analyze: " + mode + ", pageStyle: " + pageStyle
		// + ", stockId: " + analyzeVO.getStockId() + ", display date: "
		// + analyzeVO.getDisplayDate());

		try {
			if (mode != null && mode.equals(BASIC_CALCULATOR)) {
				returnStr = BASIC_CALCULATOR;
			} else {
				AnalyzeHandler handler = new AnalyzeHandler();

				if (mode != null && !mode.equals("") && !mode.equals(BLANK)) {
					if (analyzeVO.getTicker() != null) {
						analyzeVO.setTicker(analyzeVO.getTicker().toUpperCase());
					}

					analyzeVO.setUserId(user.getUserId());
					handler.execute(user, analyzeVO, sessionContext);
				} else {
					// set and reset the content
					if (analyzeVO == null) {
						analyzeVO = new AnalyzeVO();
					}

					analyzeVO.setMode(BLANK);
					analyzeVO.setStockId("");
					analyzeVO.setType("");
					analyzeVO.setTicker("");
					analyzeVO.setPeriod(6);
					if (pageStyle != null && pageStyle.equals(DYNAMIC_D)) {
						analyzeVO.setOption("YNNYY"); // default
					} else {
						analyzeVO.setOption(""); // default
					}

					analyzeVO.setUserId(user.getUserId());
					handler.execute(user, analyzeVO, sessionContext);

				}

				// get market trend
				// analyzeVO.setMarketTrend(AppContext.getMarketIndicators().getShortTrend());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return the user to the welcome screen.
		return returnStr;
	}

	public AnalyzeVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(AnalyzeVO inputVO) {
		this.inputVO = inputVO;
	}

}