package com.greenfield.ui.action.scan;

import java.util.Vector;


import com.greenfield.common.base.BaseAction;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.ui.handler.scan.ScanChartHandler;
import com.greenfield.ui.handler.scan.ScanHandler;
import com.greenfield.ui.handler.scan.ScanReportHandler;
import com.greenfield.common.object.scan.ScanVO;

/**
 * @author zhangqx
 */
public class ScanAction extends BaseAction {

	private static final long serialVersionUID = 1L;
	
	/** forward page */
	public static final String BASIC_SCAN = "basicscan";
	public static final String SCAN_RESULT = "scanresult";
	public static final String STOCK_ADMIN = "stockadmin";
	public static final String SCAN_REPORT = "scanreport";
	public static final String SCAN_REPORT2 = "scanreport2";
	public static final String SCAN_CHART = "scanchart";

	/** type */
	public static final String LIST = "list";
	public static final String CHARTS = "charts";

	// SELECT TYPES
	public static final String BY_DATE = "D"; // as default, in not defined
	public static final String BY_BEST = "B";
	public static final String BY_WORST = "W";

	private ScanVO inputVO = new ScanVO();

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
	@SuppressWarnings("rawtypes")
	public String executeAction() throws Exception {
		String mode = inputVO.getMode();
		// String type = scanVO.getType();
		String returnStr = BASIC_SCAN;

		/*
		 * System.out.println("scan mode:" + mode);
		 * System.out.println("scan type:" + type);
		 * System.out.println("scan period:" + scanVO.getPeriod());
		 * System.out.println("scan range:" + scanVO.getRange());
		 * System.out.println("base pattern:" + scanVO.getBasePattern());
		 * System.out.println("scan key:" + scanVO.getScanKey());
		 * System.out.println("scan date:" + scanVO.getScanDate());
		 */
		long bT = System.currentTimeMillis();
		
		try {
			returnStr = BASIC_SCAN;
			if (mode == null || mode.equals("") || mode.equals(ScanModes.BLANK)) {
				inputVO.setScanList(new Vector());
				inputVO.setScanDateList(new Vector());
				inputVO.setShowTitle("");
				inputVO.setScanDate("");
				inputVO.setScanKey("");
			} else if (mode != null && mode.equals("basic")) {
				String scanKey = inputVO.getScanKey();
				if (scanKey == null || scanKey.equals("")) {
					// do default now
					System.out.println("default is set: wild up");
					inputVO.setScanKey(ScanModes.WILD_UP);
					inputVO.setType(LIST);
					inputVO.setSelectType(BY_DATE);
				}
				
				ScanHandler handler = new ScanHandler();
				handler.execute(user, inputVO, sessionContext);
			} else if (mode != null && mode.equals("report")) {
				ScanReportHandler handler = new ScanReportHandler();
				handler.execute(user, inputVO, sessionContext);
				returnStr = SCAN_REPORT;
			} else if (mode != null && mode.equals("report2")) {
				ScanReportHandler handler = new ScanReportHandler();
				handler.execute(user, inputVO, sessionContext);
				returnStr = SCAN_REPORT2;
			} else if (mode != null && mode.equals("chart")) {
				ScanChartHandler handler = new ScanChartHandler();
				handler.execute(user, inputVO, sessionContext);
				returnStr = SCAN_CHART;
			}

			// used to display in the screen
			inputVO.setScanModeList(ScanReportHandler.getModeList());

			/*
			 * Vector mList = scanVO.getScanModeList(); for (int i = 0; i <
			 * mList.size(); i ++) { ScanMode mode2 = (ScanMode) mList.get(i);
			 * System.out.println("Mode: " + mode2.getScanKey() + ", str: " +
			 * mode2.getModeStr()); }
			 */

		} catch (Exception e) {
			// log error
			e.printStackTrace();
		}
		
		long sT = System.currentTimeMillis() - bT;
		System.out.println("Spend: " + sT);

		// return the user to the scan screen.
		return returnStr;
	}

	public ScanVO getInputVO() {
		return inputVO;
	}

	public void setInputVO(ScanVO inputVO) {
		this.inputVO = inputVO;
	}

}