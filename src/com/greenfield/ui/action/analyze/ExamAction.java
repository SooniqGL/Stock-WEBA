/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.action.analyze;

import com.greenfield.ui.handler.exam.ExamHandler;
import com.greenfield.common.object.exam.ExamVO;
import com.greenfield.common.object.exam.PerformVO;
import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockExt;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.apache.struts2.ServletActionContext;

/**
 * 
 * @author QZ69042
 */
public class ExamAction extends BaseAction {
	// page strings
	public static final String EXAM_CONTENT 		= "examcontent"; // exam page
	public static final String EXAM_RESULT_PAGE 	= "examresult";
	public static final String AJAX_UPDATE_PAGE 	= "ajaxupdate";
	public static final String EXAM_SUMMARY_PAGE 	= "examsummary";
	public static final String EXAM_FAQ_PAGE 		= "examfaq";

	// key to keep the tick hash
	private static final String TICKER_HASH_KEY = "tickerhashkey";

	// constants from requests - mode parameter
	public static final String BLANK = "blank"; // request for init
	public static final String AUTO_SELECT = "A"; // AUTO SELECT TICKER
	public static final String SELF_SELECT = "S"; // self selected ticker
	public static final String EXAM_RESULT = "E"; // close/or skip one position
	public static final String AJAX_UPDATE = "AU"; // Ajax to update one
													// transaction
	public static final String EXAM_SUMMARY = "ES"; // Summery

	private ExamVO inputVO = new ExamVO();

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
		ExamVO examVO = getInputVO();
		String mode = examVO.getMode();

		String returnStr = EXAM_CONTENT;

		System.out.println("exam: " + mode);
		try {
			// default to the blank page
			if (mode == null || mode.equals("") || mode.equals(BLANK)) {
				mode = BLANK;
				examVO.setMode(mode);
				examVO.setStockExt(null); // remove
				examVO.setTicker(null); // remove
			} else if (mode.equals(EXAM_RESULT)) {
				returnStr = EXAM_RESULT_PAGE;
			} else if (mode.equals(AJAX_UPDATE)) {
				// debug
				// System.out.println("ajax: " + examVO.getAjaxStr());

				returnStr = AJAX_UPDATE_PAGE;
			} else if (mode.equals(EXAM_SUMMARY)) {
				returnStr = EXAM_SUMMARY_PAGE;
			} else if (mode.equals(EXAM_FAQ_PAGE)) {
				returnStr = EXAM_FAQ_PAGE;
			}

			ExamHandler handler = new ExamHandler();
			examVO.setUserId(user.getUserId()); // pass user id to handler
			handler.execute(user, examVO, sessionContext);

			// if page is not loading a stock, set it to avoid null
			if (examVO.getStockExt() == null) {
				StockExt stkExt = new StockExt();
				stkExt.setStock(new Stock());
				examVO.setStockExt(stkExt);
			}
			// get market trend
			// analyzeVO.setMarketTrend(AppContext.getMarketIndicators().getShortTrend());
		} catch (Exception e) {
			e.printStackTrace();
		}

		// return the user to the welcome screen.
		return returnStr;
	}

	public ExamVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(ExamVO inputVO) {
		this.inputVO = inputVO;
	}

}