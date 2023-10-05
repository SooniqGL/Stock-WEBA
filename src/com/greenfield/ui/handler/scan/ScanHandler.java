package com.greenfield.ui.handler.scan;

import java.text.DecimalFormat;
import java.util.Collections;
import java.util.Vector;

import com.greenfield.ui.action.scan.ScanAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.common.constant.TrendTypes;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.ScanCachePool;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.object.scan.ScanHistoryVO;
import com.greenfield.common.object.scan.ScanVO;

/**
 * @author zhangqx
 * 
 */
public class ScanHandler extends BaseHandler {
	// If define the date string as globally, then it will not change day after
	// day.
	// private static String prevMonthDayInOracle =
	// OracleUtil.getPrevDateInOracleFormat(null, 1, 0);
	private static int MAX_RETURNS_FOR_PERFORM = 30;
	private static int DEAULT_PERIOD_FOR_PERFORM = 30;
	private static int MAX_PERIOD_FOR_PERFORM = 240;

	public BaseObject doAction(BaseObject inputVO, WebSessionContext sessionContext) {
		ScanVO vo = (ScanVO) inputVO;
		String type = vo.getType();

		if (type != null
				&& (type.equals(ScanAction.CHARTS) || type
						.equals(ScanAction.LIST))) {
			// load the search result from oracle
			try {
				String selectType = vo.getSelectType();
				if (selectType == null || selectType.equals("")) {
					vo.setSelectType(ScanAction.BY_DATE);
				}

				// load the data
				queryScanHistoryNew(vo);

			} catch (Exception e) {
				e.printStackTrace();
				// have trouble to query
				vo.setScanList(new Vector());
			}

			// Test: Check if the reset pool works
			// useCnt ++;
			// (useCnt % 5 == 0) {
			// ScanCachePool.resetPool();
			// }

		}

		return inputVO;
	}

	/**
	 * Only get scan date list for this new function; the scan list will be obtained from Ajax call.
	 * @param vo
	 * @throws Exception
	 */
	public void queryScanHistoryNew(ScanVO vo) throws Exception {
		ScanDAO scanDao = new ScanDAO();
		scanDao.setDatabase(database);

		String scanKey = vo.getScanKey();
		// String selectType = vo.getSelectType(); // NULL CHECK IS DONE PREVIOUSLY
		// String scanDate = vo.getScanDate();

		Vector<String> dateList = new Vector<String>();

		// System.out.println("scan key is: " + scanKey);
		if (scanKey != null && !scanKey.equals("")) {
			int period = vo.getPeriod();
			if (period == 0) {
				period = DEAULT_PERIOD_FOR_PERFORM; // default
				vo.setPeriod(period);
			} else if (period > MAX_PERIOD_FOR_PERFORM) {
				period = MAX_PERIOD_FOR_PERFORM; // IN CASE TOO LARGE
				vo.setPeriod(period);
			}

			// Call pool, if not there, it calls
			// scanDao.getScanHistoryDateListFromDB(scanKey)
			// Notice: The date format is MM/DD/YYYY in this case.
			Vector scanObjList = ScanCachePool
					.getScanDateList(scanKey, scanDao);
			StringBuilder builder = new StringBuilder();
			if (scanObjList != null) {		
				for (int i = 0; i < scanObjList.size(); i++) {
					ScanHistoryVO history = (ScanHistoryVO) scanObjList.get(i);
					//dateList.add(new String(history.getScanDate()));
					if (i > 0) {
						builder.append("#");
					}
					builder.append(history.getScanDate());
				}
			}

			//vo.setScanDateList(dateList);
			vo.setScanDateListStr(builder.toString());

		} else {
			// error - nothing
			vo.setScanDateList(new Vector());
		}

		vo.setShowTitle(ScanModes.getModeString(scanKey));

	}
	/**
	 * Query up to certain days to find the scan order. If scanKey is given, try
	 * to use the scan key and scan date to query. Else, default to use
	 * "isCurrent=Y" to query.
	 * 
	 * @param vo
	 */
	public void queryScanHistory(ScanVO vo) throws Exception {
		ScanDAO scanDao = new ScanDAO();
		scanDao.setDatabase(database);

		String scanKey = vo.getScanKey();
		String selectType = vo.getSelectType(); // NULL CHECK IS DONE PREVIOUSLY
		String scanDate = vo.getScanDate();

		Vector scanList = null;
		Vector dateList = new Vector();

		// System.out.println("scan key is: " + scanKey);
		if (scanKey != null && !scanKey.equals("")) {
			int period = vo.getPeriod();
			if (period == 0) {
				period = DEAULT_PERIOD_FOR_PERFORM; // default
				vo.setPeriod(period);
			} else if (period > MAX_PERIOD_FOR_PERFORM) {
				period = MAX_PERIOD_FOR_PERFORM; // IN CASE TOO LARGE
				vo.setPeriod(period);
			}

			scanList = getScanList(scanKey, selectType, scanDate, period,
					scanDao);

			// Call pool, if not there, it calls
			// scanDao.getScanHistoryDateListFromDB(scanKey)
			// Notice: The date format is MM/DD/YYYY in this case.
			Vector scanObjList = ScanCachePool
					.getScanDateList(scanKey, scanDao);
			if (scanObjList != null) {
				for (int i = 0; i < scanObjList.size(); i++) {
					ScanHistoryVO history = (ScanHistoryVO) scanObjList.get(i);
					dateList.add(new String(history.getScanDate()));
				}
			}

			vo.setScanDateList(dateList);

			if (scanList != null && scanList.size() > 0) {
				updateGainField(scanList);
				selectPerformSubsetAndSort(selectType, scanList, vo);
				vo.setScanList(scanList); // set it to vo
			} else {
				vo.setScanList(new Vector()); // set it to vo
			}

			if (selectType != null && selectType.equals(ScanAction.BY_DATE)) {
				if (scanList != null && scanList.size() > 0) {
					// pick the date, and convert it into display format
					vo.setScanDate(DateUtil
							.convertYYYYMMDDToMMDDYYYYDate(((ScanHistoryVO) scanList
									.get(0)).getScanDate()));

					// set prev/next scan date
					setPrevNextScanDates(vo, dateList);
				} else {
					vo.setScanDate(""); // impossible, there is a scan date, but
										// no data
					vo.setPrevScanDate("");
					vo.setNextScanDate("");
				}
			} else { // selectType: B or W
				if (selectType != null && selectType.equals(ScanAction.BY_BEST)) {
					vo.setScanDate(period + " D (BEST)");
				} else if (selectType != null
						&& selectType.equals(ScanAction.BY_WORST)) { // type = W
					vo.setScanDate(period + " D (WORST)");
				}

				// always disable prev/next
				vo.setPrevScanDate("");
				vo.setNextScanDate("");
			}
		} else {
			// error - nothing
			vo.setScanDateList(new Vector());
		}

		vo.setShowTitle(ScanModes.getModeString(scanKey));

	}

	/**
	 * Call from Ajax only
	 * @param scanKey
	 * @param selectType
	 * @param scanDate
	 * @param period
	 * @param showChart
	 * @param scanDao
	 * @return
	 * @throws Exception
	 */
	public Vector getScanListNew(String scanKey, String selectType,
			String scanDate, int period, String sortColumn, 
			String sortOrder, ScanDAO scanDao) throws Exception {
		Vector scanList = null;
		
		scanList = getScanList(scanKey, selectType, scanDate, period,
				scanDao);
		
		if (scanList != null && scanList.size() > 0) {
			updateGainField(scanList);
			
			ScanVO locVO = new ScanVO();
			locVO.setSortColumn(sortColumn);
			locVO.setSortOrder(sortOrder);
			selectPerformSubsetAndSort(selectType, scanList, locVO);
		} else {
			scanList = new Vector(); // set it to vo
		}
		
		return scanList;
	}
	
	
	/**
	 * service both old queryhistory and new Ajax
	 * @param scanKey
	 * @param selectType
	 * @param scanDate
	 * @param period
	 * @param scanDao
	 * @return
	 * @throws Exception
	 */
	public Vector getScanList(String scanKey, String selectType,
			String scanDate, int period, ScanDAO scanDao) throws Exception {
		Vector scanList = null;

		// debug
		// System.out.println("scanKey: " + scanKey + ", selectType: " +
		// selectType + ", scanDate: " + scanDate);

		if (selectType != null && !selectType.equals(ScanAction.BY_DATE)) {
			// load one month data, late sort and take the best/worst perform
			// data
			String latestDate = ScanCachePool.getLatestScanDate(scanKey,
					scanDao); // find the most recent scan date in DB
			String prevMonthDayInOracle = OracleUtil
					.getPrevDateInOracleFormat2(latestDate, 0, period);
			scanList = scanDao.getScanHistoryFromDB(scanKey, "Y", null, null,
					prevMonthDayInOracle);

			// debug
			System.out.println("Select best/worst in period starts at: "
					+ prevMonthDayInOracle);
		} else {
			// if scanKeyAndDate is given, try to find scankey and date
			// System.out.println("scan date is: " + scanDate);

			if (scanDate != null && !scanDate.equals("")) {
				// Use the latest one, user YYYY/MM/DD format for database query
				// The scan date from screen will be in MM/DD/YYYY format, so
				// convert.
				scanList = scanDao.getScanHistoryFromDB(scanKey, null,
						DateUtil.convertMMDDYYYYToYYYYMMDDDate(scanDate), null,
						null);
			} else {

				// find the most recent date
				// "YYYY/MM/DD" format is returned
				String latestDate = ScanCachePool.getLatestScanDate(scanKey,
						scanDao);

				// debug
				System.out.println("lastest date: " + latestDate);

				// lastestDate is null or empty if nothing in DB
				if (latestDate != null && !latestDate.equals("")) {
					// use the latest one, user YYYY/MM/DD format for database
					// query
					scanList = scanDao.getScanHistoryFromDB(scanKey, "Y",
							latestDate, null, null);
				}
			}
		}

		// get size
		if (scanList != null) {
			System.out.println("size for scan list: " + scanList.size());
		}

		return scanList;
	}

	private void selectPerformSubsetAndSort(String selectType, Vector scanList,
			ScanVO vo) {
		if (selectType != null && !selectType.equals(ScanAction.BY_DATE)) {
			// sort by performance, and truncate the data
			boolean isAsending = true;
			if (selectType != null && selectType.equals(ScanAction.BY_BEST)) {
				isAsending = false; // select the least gains
			}

			// DEBUG
			/*
			 * for (int i = 0; i < scanList.size(); i ++) { ScanHistoryVO vo2 =
			 * (ScanHistoryVO) scanList.get(i); System.out.println("date: " +
			 * vo2.getScanDate() + ", " + vo2.getTicker() + ", " +
			 * vo2.getGain()); }
			 */

			ScanDisplayComp comp = new ScanDisplayComp(ScanDisplayComp.BY_GAIN,
					isAsending);
			Collections.sort(scanList, comp);

			// truncate the data, remove one by one, setSize() does not work in
			// this case!
			// Very strange - after sort, the setSize() still work on the
			// original vectors - so
			// the following does not work as expected:
			// scanList.setSize(MAX_RETURNS_FOR_PERFORM);
			if (scanList.size() > MAX_RETURNS_FOR_PERFORM) {
				for (int i = scanList.size() - 1; i >= MAX_RETURNS_FOR_PERFORM; i--) {
					scanList.remove(i);
				}
			}

			// sort the scan list, for all the cases
			if (vo.getSortColumn() == null || vo.getSortColumn().equals("")) {
				vo.setSortColumn(ScanDisplayComp.BY_GAIN);
			}

			if (vo.getSortOrder() == null || vo.getSortOrder().equals("")) {
				if (selectType != null
						&& selectType.equals(ScanAction.BY_WORST)) {
					vo.setSortOrder("0");
				} else {
					vo.setSortOrder("1");
				}
			}

			ScanDisplayComp comp2 = new ScanDisplayComp(vo.getSortColumn(),
					vo.getSortOrder());
			Collections.sort(scanList, comp2);

		} else {

			// sort the scan list, for all the cases
			if (vo.getSortColumn() == null || vo.getSortColumn().equals("")) {
				vo.setSortColumn(ScanDisplayComp.BY_CURR_PRICE);
			}

			if (vo.getSortOrder() == null || vo.getSortOrder().equals("")) {
				vo.setSortOrder("0");
			}

			ScanDisplayComp comp2 = new ScanDisplayComp(vo.getSortColumn(),
					vo.getSortOrder());
			Collections.sort(scanList, comp2);
		}
	}

	private void updateGainField(Vector scanList) {
		if (scanList != null && scanList.size() > 0) {
			DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
			for (int i = 0; i < scanList.size(); i++) {
				ScanHistoryVO vo = (ScanHistoryVO) scanList.get(i);
				String trend = vo.getScanKey().substring(1, 2);
				if (trend != null && trend.equals(TrendTypes.UP_TREND)) {
					vo.setLongShort("Long");
				} else {
					vo.setLongShort("Short");
				}

				double percent = 0;
				if (vo.getScanPrice() > 0) {
					if (vo.getCloseDate() != null
							&& !vo.getCloseDate().equals("")) {
						percent = ((double) (vo.getClosePrice() - vo
								.getScanPrice()) / vo.getScanPrice()) * 100;
					} else {
						percent = ((double) (vo.getCurrentPrice() - vo
								.getScanPrice()) / vo.getScanPrice()) * 100;
					}

					if (trend != null && !trend.equals(TrendTypes.UP_TREND)) {
						percent = -percent;
					}

					// format the gain
					vo.setGain((new Double(oneDigitFormatter.format(percent)))
							.doubleValue());
				} else {
					// strange error
					System.out.println("Error: scan price is zero for: "
							+ vo.getTicker());
				}
			}
		}
	}

	private void setPrevNextScanDates(ScanVO vo, Vector dateList) {
		if (dateList == null || dateList.size() == 0
				|| vo.getScanDate() == null) {
			vo.setPrevScanDate("");
			vo.setNextScanDate("");
			return;
		}

		String scanDate = vo.getScanDate();
		boolean found = false;
		for (int i = 0; i < dateList.size(); i++) {
			String locS = (String) dateList.get(i);
			if (scanDate.equals(locS)) {
				if (i > 0) {
					vo.setPrevScanDate((String) dateList.get(i - 1));
				} else {
					vo.setPrevScanDate("");
				}

				if (i < (dateList.size() - 1)) {
					vo.setNextScanDate((String) dateList.get(i + 1));
				} else {
					vo.setNextScanDate("");
				}

				found = true;
				break;
			}
		}

		if (!found) {
			// weird?
			vo.setPrevScanDate("");
			vo.setNextScanDate("");
		}
	}

}
