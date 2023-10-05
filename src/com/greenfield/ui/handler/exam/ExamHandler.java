/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.handler.exam;

import com.greenfield.ui.action.analyze.ExamAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.SessionKeys;
import com.greenfield.common.dao.analyze.ExamDAO;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.DBCachePool;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.exam.ExamVO;
import com.greenfield.common.object.exam.TransactionVO;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.object.stock.StockExt;
import com.greenfield.common.service.AnalysisService;
import com.greenfield.common.service.RangeService;
import com.greenfield.common.service.ServiceContext;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.ScanUtil;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author QZ69042
 */
public class ExamHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(ExamHandler.class);

	// analysis begin Date
	private final static String DATA_START_DATE = "1994/06/01";
	private final static String EXAM_START_DATE = "1996/01/01";

	private final static int DAYS_IN_MONTH = 22;
	private final static int EXTRA_DAYS = 200;
	private final static int LARGE_EXTRA = 800;
	private final static String COMMA_CHAR = ",";

	private final static int MIN_DAYS = 365; // used to determine the start date

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.
	 * object .BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj,
			WebSessionContext sessionContext) throws Exception {
		ExamVO examVO = (ExamVO) obj;
		String mode = examVO.getMode();
		// String type = examVO.getType();

		try {
			StockAdminDAO stockDao = new StockAdminDAO();
			stockDao.setDatabase(database);
			Stock stk = null;

			HashMap examVisitedList = (HashMap) sessionContext
					.getObject(SessionKeys.EXAM_VISITED_LIST);
			if (examVisitedList == null) {
				examVisitedList = new HashMap();
			}

			//
			if (mode != null && mode.equals(ExamAction.BLANK)) {
				// do nothing
				examVO.setDisplay("blank");
			} else {
				if (mode != null && mode.equals(ExamAction.AUTO_SELECT)) {
					// load test content to the examVO
					stk = autoStockGenerator(examVO, stockDao, examVisitedList);
					sessionContext.setObject(SessionKeys.EXAM_VISITED_LIST,
							examVisitedList);
				} else if (mode != null && mode.equals(ExamAction.SELF_SELECT)) {
					// exam the result
					stk = findStockByTicker(examVO, stockDao);
				} else if (mode != null && mode.equals(ExamAction.AJAX_UPDATE)) {
					// load the exam transaction to DB
					ExamDAO examDao = new ExamDAO();
					examDao.setDatabase(database);
					updateExamTransaction(examVO.getUserId(),
							examVO.getAjaxStr(), examDao);
				} else if (mode != null && mode.equals(ExamAction.EXAM_RESULT)) {
					// load the exam transaction to DB
					ExamDAO examDao = new ExamDAO();
					examDao.setDatabase(database);
					loadExamTransactions(examVO.getUserId(), examVO, examDao);
				}

				if (stk != null) {
					examVO.setDisplay("one");
					StockExt stockExt = getStockAnalysisDataForClient(stockDao,
							stk, examVO);
					examVO.setStockExt(stockExt);
					examVO.setStockId(stk.getStockId());
					examVO.setTicker(stk.getTicker());
					updateStockDates(stockDao, examVO, stk.getStockId());
				} else {
					examVO.setDisplay("zero");
				}
			}
		} catch (Exception e) {
			throw e;
		}

		return obj;
	}

	// auto generate the stock from DB
	private Stock autoStockGenerator(ExamVO examVO, StockAdminDAO stockDao,
			HashMap examVisitedList) throws Exception {

		Vector examList = DBCachePool.getExamStockList();
		if (examList == null || examList.size() == 0) {
			// error, but return null
			return null;
		}

		// make a random int
		Random rd = new Random(System.currentTimeMillis());
		int index = rd.nextInt(examList.size()); // find the random
		boolean foundIt = false;
		Stock stk = null;

		while (!foundIt) {

			stk = (Stock) examList.get(index);
			if (examVisitedList.containsKey(stk.getStockId())) {
				// System.out.println("visited: " + stk.getStockId());
				index += 1;
			} else {
				examVisitedList.put(stk.getStockId(), "");
				foundIt = true;
			}

		}

		return stk;
	}

	/**
	 * Do this one time at the system load, and save to the Cache.
	 * 
	 * @param examVO
	 * @param stockDao
	 * @return
	 * @throws Exception
	 */
	// * may need this for system load to save customer time
	public Vector<Stock> generateExamStockList(StockAdminDAO stockDao)
			throws Exception {
		Vector<Stock> examList = new Vector<Stock>();

		Vector stockList = DBCachePool.getStockListFromDB(null, null, stockDao);
		if (stockList == null || stockList.size() == 0) {
			// error, but return null
			return examList;
		}

		int listSize = stockList.size();
		for (int i = 0; i < listSize; i++) {
			Stock stk = (Stock) stockList.get(i);
			if (checkIfStockOK(stk, stockDao)) {
				examList.add(stk);
			}
		}

		LOGGER.warn("generateExamStockList size: " + examList.size());

		return examList;
	}

	private boolean checkIfStockOK(Stock stk, StockAdminDAO stockDao) {
		boolean passed = true;

		if (stk.getPrice() < 5 || stk.getPrice() > 500) {
			passed = false;
		} else {

			// to avoid all users to check the volume again and again,
			// we save a flag
			// to the stock if the volume check false
			double oneVolume = stk.getVolume(); // stockDao.getOneVolume(stk.getStockId());

			// debug
			// System.out.println("One volume for exam: " + oneVolume +
			// ", ticker: " + stk.getTicker());
			if (oneVolume < 200000) {
				// if too few volume, do not do it
				passed = false;
			}
		}

		// return
		return passed;
	}

	// find the stock in DB, if not there, return null
	private Stock findStockByTicker(ExamVO examVO, StockAdminDAO dao)
			throws Exception {
		String ticker = examVO.getTicker();
		if (ticker == null || ticker.equals("")) {
			return null;
		} else {
			ticker = ticker.toUpperCase();
			examVO.setTicker(ticker);
		}

		Vector stockList = DBCachePool.getStockListFromDB(null, ticker, dao);
		Stock stock = null;

		// if at least one, pick the first one, else, nothing
		if (stockList != null && stockList.size() >= 1) {
			// just one
			stock = (Stock) stockList.get(0);
			// analVO.setStockExt(analyzeStock(dao, stock, analVO.getPeriod(),
			// analVO));
			examVO.setStockId(stock.getStockId());
		}

		return stock;
	}

	// Get all the data for one ticker and wrap to client
	private StockExt getStockAnalysisDataForClient(StockAdminDAO dao,
			Stock stk, ExamVO examVO) throws Exception {

		// Date todayDate = new Date();
		// String startDate = OracleUtil.getPrevDateInOracleFormat(null, period,
		// extra);
		String endDate = OracleUtil.getDateInOracleFormat(null);

		StockExt stockExt = new StockExt();
		stockExt.setStock(stk);

		Vector dailyList = dao.getStockDailyPriceListFromDB(stk.getStockId(),
				null, endDate);
		if (dailyList != null && dailyList.size() > 0) {
			int defaultMonthIndex = ScanUtil.findStartIndex(dailyList,
					DATA_START_DATE);

			ServiceContext svrContext = new ServiceContext();
			svrContext.setStartIndex(defaultMonthIndex);
			svrContext.setStockExt(stockExt);
			svrContext.setPriceList(dailyList);

			//
			RangeService rangeService = new RangeService();
			rangeService.findMinMax(svrContext);
			rangeService.findAverage20(svrContext); // 21-day average
			rangeService.findAverage50(svrContext); // 50-day average
			rangeService.findAverage100S(svrContext, RangeService.RANGE_100); // 100-day
																				// average
			// rangeService.findAccumulationForceForAll(svrContext);

			// figure out the trend color after 20/50/100/slope/acc are done
			rangeService.findTrendIndicators(svrContext);

			// find trade point
			AnalysisService analysisService = new AnalysisService();
			analysisService.findPastTradePoints(svrContext);

			// collect the dataStr
			examVO.setDailyDataStr(collectDataStr(svrContext.getPriceList(),
					svrContext.getStartIndex()));

			// System.out.println("data: " + examVO.getDailyDataStr());
			/*
			 * // generate weekly data // System.out.println("Price List size: "
			 * + dailyList.size());
			 * StockUtil.adjustPriceListForWeeklyChart(svrContext);
			 * rangeService.findAverage20(svrContext); // 21-day average
			 * rangeService.findAverage50(svrContext); // 50-day average
			 * rangeService.findAverage100S(svrContext, RangeService.RANGE_100);
			 * //rangeService.findAccumulationForceForAll(svrContext);
			 * examVO.setWeeklyDataStr(collectDataStr(svrContext.getPriceList(),
			 * svrContext.getStartIndex()));
			 */
		}

		return stockExt;

	}

	/**
	 * format: date | open | close | min | max | logAvg9 | logAvg21 | logAvg50 |
	 * volume | growth | force | trendColor
	 * 
	 * @param priceList
	 * @return
	 */
	private String collectDataStr(Vector priceList, int startIndex) {
		StringBuilder buf = new StringBuilder();
		DecimalFormat twoDigitFormatter = new DecimalFormat("0.00");
		DecimalFormat fourDigitFormatter = new DecimalFormat("0.00000"); // 5
																			// digits

		if (priceList != null && priceList.size() > 0) {
			for (int i = 0; i < priceList.size(); i++) {
				StockDailyInfo item = (StockDailyInfo) priceList.get(i);
				if (i > 0) {
					buf.append("#");
				}

				buf.append(item.getMDate()).append("|");
				buf.append(twoDigitFormatter.format(item.getOpen()))
						.append("|");
				buf.append(twoDigitFormatter.format(item.getClose())).append(
						"|");
				buf.append(twoDigitFormatter.format(item.getMin())).append("|");
				buf.append(twoDigitFormatter.format(item.getMax())).append("|");

				if (item.getAverage20() > 0) {
					buf.append(
							fourDigitFormatter.format(item.getAverage20Log()))
							.append("|");
				} else {
					buf.append("20|"); // used for not defined, use big #, all
										// number will < e^20
				}

				if (item.getAverage50() > 0) {
					buf.append(
							fourDigitFormatter.format(item.getAverage50Log()))
							.append("|");
				} else {
					buf.append("20|"); // used for not defined
				}

				if (item.getAverage100() > 0) {
					buf.append(
							fourDigitFormatter.format(item.getAverage100Log()))
							.append("|");
				} else {
					buf.append("20|"); // not defined
				}

				buf.append(item.getVolume()).append("|");
				buf.append(fourDigitFormatter.format(item.getSlope())).append(
						"|");
				buf.append(fourDigitFormatter.format(item.getAcceleration()))
						.append("|");
				buf.append(item.getTrendColor()).append("|");
				buf.append(item.getTradePoint());
			}
		}

		return buf.toString();
	}

	/**
	 * Get the start date and end of dates for the ticker; This will be used at
	 * the client side to calculate the dates when user is trying to move the
	 * dates.
	 */
	private void updateStockDates(StockAdminDAO dao, ExamVO examVO,
			String stockId) throws Exception {
		String[] dates = DBCachePool.getStockDateRangeFromDB(stockId, dao);
		if (dates != null && dates.length == 2) {

			if (dates[0].compareTo(EXAM_START_DATE) <= 0) {
				examVO.setStartDate(EXAM_START_DATE); // same as start date
				examVO.setEndDate(dates[1]);
			} else {
				Date firstDate = null;
				Date lastDate = null;

				// move this formatter here to avoid static concurrent issue
				SimpleDateFormat date_formatter = new SimpleDateFormat(
						"yyyy/MM/dd");

				try {
					firstDate = date_formatter.parse(dates[0]);
					lastDate = date_formatter.parse(dates[1]);
				} catch (Exception e) {
					throw new Exception("Format error: " + e.getMessage());
				}

				// leave min number of days for this stock.
				// not sure if this getInstance() has concurrent issue or not!
				Calendar CALENDAR = Calendar.getInstance();
				CALENDAR.setTime(firstDate);
				CALENDAR.add(Calendar.DAY_OF_YEAR, MIN_DAYS);
				long newTime = CALENDAR.getTime().getTime();
				if (newTime >= lastDate.getTime()) {
					// not enough data, so do not move around with dates
					examVO.setStartDate(dates[1]); // same as start date
					examVO.setEndDate(dates[1]);
				} else {
					examVO.setStartDate(date_formatter.format(CALENDAR
							.getTime()));
					examVO.setEndDate(dates[1]);
				}
			}

			// System.out.println("start: " + examVO.getStartDate());
			// System.out.println("end: " + examVO.getEndDate());
		}
	}

	/**
	 * Add the result from Ajax to DB for this User format: trade type|stock
	 * id|trade amount|open date|open price|close date|close price
	 * |profit|profit percent|max postive|max negative|num days
	 * 
	 * @param userId
	 * @param ajaxStr
	 * @param dao
	 */
	private void updateExamTransaction(String userId, String ajaxStr,
			ExamDAO examDao) throws Exception {
		if (ajaxStr == null || ajaxStr.equals("")) {
			// give up if nothing is passed
			return;
		}

		String[] items = ajaxStr.split("\\|");
		if (items == null || items.length != 12) {
			// error
			System.out.println("Ajax data format error: " + ajaxStr
					+ ", item length (should be 12) = " + items.length);
			return;
		}

		TransactionVO trans = new TransactionVO();
		trans.setUserId(userId);
		if (items[0].equals("Long")) {
			trans.setTradeType("L");
		} else {
			trans.setTradeType("S");
		}

		trans.setStockId(items[1]);
		trans.setTradeAmount((new Double(items[2])).doubleValue());
		trans.setOpenDate(items[3]);
		trans.setOpenPrice((new Double(items[4])).doubleValue());
		trans.setCloseDate(items[5]);
		trans.setClosePrice((new Double(items[6])).doubleValue());
		trans.setProfit((new Double(items[7])).doubleValue());
		trans.setProfitPercent((new Double(items[8])).doubleValue());
		trans.setMaxPositive((new Double(items[9])).doubleValue());
		trans.setMaxNegative((new Double(items[10])).doubleValue());
		trans.setTotalDays((new Integer(items[11])).intValue());

		// call the DB update
		examDao.insertExamTransaction(trans);
	}

	/**
	 * load rangeList and exam-transactions to the VO when userId is given,
	 * using orderRange to determine the range if the orderRange is not given,
	 * try to load the first Range
	 */
	private void loadExamTransactions(String userId, ExamVO examVO,
			ExamDAO examDao) throws Exception {
		if (userId == null || userId.equals("")) {
			// give up if nothing is passed
			examVO.setRangeList(new Vector<String>());
			examVO.setTransList(new Vector<TransactionVO>());
			examVO.setOrderRange("NONE");
			return;
		}

		// find max order in DB
		int maxOrder = examDao.getMaxOrderNumber(userId);
		if (maxOrder <= 0) {
			// nothing is found for this user
			examVO.setRangeList(new Vector<String>());
			examVO.setTransList(new Vector<TransactionVO>());
			examVO.setOrderRange("NONE");
			return;
		}

		Vector<String> rangeList = buildRangeList(maxOrder);
		examVO.setRangeList(rangeList);

		// orderRange is assigned from client, if null, use the first range to
		// query
		// orderRange format: {start range} - {end range}
		String orderRange = examVO.getOrderRange();
		if (orderRange == null || orderRange.equals("")) {
			orderRange = rangeList.get(0);
			examVO.setOrderRange(orderRange); // set it back if not defined
		}

		String[] ranges = orderRange.split("-");
		if (ranges.length == 2) {
			examVO.setTransList(examDao.getExamTransactionsListFromDB(userId,
					ranges[0], ranges[1], false));
		} else {
			// bad range, should not happen - in case client has passed
			// something wrong
			examVO.setRangeList(new Vector<String>());
			examVO.setTransList(new Vector<TransactionVO>());
			examVO.setOrderRange("NONE");
		}

	}

	/*
	 * Use page size here to divide the order to list We know maxOrder > 0 when
	 * come to this function.
	 */
	private Vector<String> buildRangeList(int maxOrder) {
		int PAGE_SIZE = 50; // show this number of transactions per page
		Vector<String> rangeList = new Vector<String>();

		int items = maxOrder / PAGE_SIZE;
		for (int i = 0; i < items; i++) {
			int startOrder = i * PAGE_SIZE + 1;
			int endOrder = (i + 1) * PAGE_SIZE;
			rangeList.add(String.valueOf(startOrder) + "-"
					+ String.valueOf(endOrder));
		}

		// check if there is reminder
		int evenDone = items * PAGE_SIZE;
		if (maxOrder > evenDone) {
			int startOrder = evenDone + 1;
			int endOrder = maxOrder;
			rangeList.add(String.valueOf(startOrder) + "-"
					+ String.valueOf(endOrder));
		}

		// debug
		System.out.println("range list: " + rangeList);

		return rangeList;
	}
}