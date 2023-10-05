/*
 * Created on Oct 27, 2006
 */
package com.greenfield.ui.handler.scan;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.ScanCachePool;

import java.util.Vector;

import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.scan.ScanMode;
import com.greenfield.common.object.scan.ScanVO;

/**
 * @author qin
 */
public class ScanReportHandler extends BaseHandler {
	private static String SEPARATOR = "_";

	/** get the oracle to find the list of data */
	public BaseObject doAction(BaseObject inputVO, WebSessionContext sessionContext) {
		ScanVO vo = (ScanVO) inputVO;
		Vector modeList = getModeList();
		vo.setModeList(modeList);

		// load the max index for scan report 2
		String mode = vo.getMode();
		if (mode != null && mode.equals("report2")) {
			ScanDAO scanDao = new ScanDAO();
			scanDao.setDatabase(database);

			String scanKey = vo.getScanKey();
			if (scanKey == null || scanKey.equals("")) {
				// if is not defined in the request
				scanKey = ((ScanMode) modeList.get(0)).getScanKey();
				vo.setScanKey(scanKey);
			}

			// get the scan report list from pool or DB
			try {
				Vector reportDateList = ScanCachePool.getScanReportDateList(
						scanKey, scanDao);
				if (reportDateList != null && reportDateList.size() > 0) {
					vo.setScanReportDateList(reportDateList);
				} else {
					vo.setScanReportDateList(new Vector());
				}
			} catch (Exception e) {
				// ignore
				vo.setScanReportDateList(new Vector());
			}
		}

		return vo;
	}

	public static Vector getModeList() {
		Vector modeList = new Vector();

		/* up modes ... */
		ScanMode mode = new ScanMode();
		mode.setScanKey(ScanModes.CROSS_20_50_UP);
		mode.setModeStr("Cross-Over 20/50 Mode, Up Trend");
		modeList.add(mode);

		mode = new ScanMode();
		mode.setScanKey(ScanModes.CROSS_20_100_UP);
		mode.setModeStr("Cross-Over 20/100 Mode, Up Trend");
		modeList.add(mode);

		mode = new ScanMode();
		mode.setScanKey(ScanModes.MOMENTUM_UP);
		mode.setModeStr("Momentum Mode, Up Trend");
		modeList.add(mode);

		mode = new ScanMode();
		mode.setScanKey(ScanModes.NEWHIGH_UP);
		mode.setModeStr("New High Mode, Up Trend");
		modeList.add(mode);
		/*
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.PROGRESS_UP);
		 * mode.setModeStr("Progress Mode, Up Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.REVERSE_UP);
		 * mode.setModeStr("Reverse Mode, Up Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.LINEAR_UP);
		 * mode.setModeStr("Linear Mode, Up Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.ZONE_UP);
		 * mode.setModeStr("Zone Mode, Up Trend"); modeList.add(mode);
		 */
		mode = new ScanMode();
		mode.setScanKey(ScanModes.WILD_UP);
		mode.setModeStr("Wild Horse Mode, Up Trend");
		modeList.add(mode);

		/* Down Modes ... */

		mode = new ScanMode(); // need new
		mode.setScanKey(ScanModes.CROSS_20_50_DOWN);
		mode.setModeStr("Cross-Over 20/50 Mode, Down Trend");
		modeList.add(mode);

		mode = new ScanMode(); // need new
		mode.setScanKey(ScanModes.CROSS_20_100_DOWN);
		mode.setModeStr("Cross-Over 20/100 Mode, Down Trend");
		modeList.add(mode);

		mode = new ScanMode();
		mode.setScanKey(ScanModes.MOMENTUM_DOWN);
		mode.setModeStr("Momentum Mode, Down Trend");
		modeList.add(mode);

		mode = new ScanMode();
		mode.setScanKey(ScanModes.NEWHIGH_DOWN);
		mode.setModeStr("New Low Mode, Down Trend");
		modeList.add(mode);
		/*
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.PROGRESS_DOWN);
		 * mode.setModeStr("Progress Mode, Down Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.REVERSE_DOWN);
		 * mode.setModeStr("Reverse Mode, Down Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.LINEAR_DOWN);
		 * mode.setModeStr("Linear Mode, Down Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.ZONE_DOWN);
		 * mode.setModeStr("Zone Mode, Down Trend"); modeList.add(mode);
		 */
		/*
		 * 
		 * 
		 * 
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.BREAK_UP);
		 * mode.setModeStr("Break Mode, Up Trend"); modeList.add(mode);
		 * 
		 * mode = new ScanMode(); mode.setScanKey(ScanModes.BREAK_DOWN);
		 * mode.setModeStr("Break Mode, Down Trend"); modeList.add(mode);
		 */

		return modeList;
	}
}
