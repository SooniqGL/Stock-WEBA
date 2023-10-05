package com.greenfield.ui.handler.analyze;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.Vector;

//import com.greenfield.ui.common.constant.TrendTypes;


















import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.dao.member.UserStoreDAO;
import com.greenfield.common.dao.member.WatchListDAO;
import com.greenfield.common.handler.BaseHandler;
//
import com.greenfield.ui.action.analyze.AnalyzeAction;
import com.greenfield.ui.cache.DBCachePool;
import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.ui.cache.PoolUtil;
import com.greenfield.ui.util.UIStockUtil;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.analyze.AnalyzeVO;
import com.greenfield.common.object.member.UserStore;
//import com.greenfield.common.object.stock.MarketIndicators;
import com.greenfield.common.object.stock.MarketIndicators;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.object.stock.StockExt;
import com.greenfield.common.service.LinearModelService;
import com.greenfield.common.service.RangeService;
import com.greenfield.common.service.ServiceContext;
import com.greenfield.common.service.VolumeService;
import com.greenfield.common.service.WarningService;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.ScanUtil;
import com.greenfield.common.util.StringHelper;
//import com.greenfield.ui.service.APlusService;
//import com.greenfield.ui.service.TrendService;
//import com.greenfield.ui.service.VolumeService;

/**
 * @author zhangqx
 * 
 */
public class AnalyzeHandler extends BaseHandler {
	private final static int DAYS_IN_MONTH = 22;
	private final static int EXTRA_DAYS = 200;
	private final static int LARGE_EXTRA = 800;
	private final static String COMMA_CHAR = ",";

	// do not use the static formatter here
	// private final static SimpleDateFormat date_formatter = new
	// SimpleDateFormat ("yyyy/MM/dd");
	// private final static DecimalFormat oneDigitFormatter = new
	// DecimalFormat("0.0");
	// private final static DecimalFormat twoDigitFormatter = new
	// DecimalFormat("0.##");
	// private final static DecimalFormat fourDigitFormatter = new
	// DecimalFormat("0.####");

	private final static int MIN_DAYS = 365; // used to determine the start date

	// do not try to use Calendar as global instance, concurrency call may
	// create error.
	// still do not know if move this getInstance() to method, but if it still
	// has concurrent issue or not.
	// private final static Calendar CALENDAR = Calendar.getInstance();

	// local variable
	private String serviceId = null;

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object
	 * .BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		AnalyzeVO analVO = (AnalyzeVO) obj;
		String mode = analVO.getMode();
		String type = analVO.getType();

		System.out.println("ticker 2 : " + analVO.getTicker() + ", type: " + type);

		try {
			StockAdminDAO dao = new StockAdminDAO();
			dao.setDatabase(database);
			String ticker = null;
			boolean notFound = false;

			if (mode != null && mode.equals("ajax")) {
				// handles the ajax call here
				try {
					Stock stock = DBCachePool.getStockFromDB(
							analVO.getStockId(), dao);
					analyzeStockDynamic(dao, stock, analVO, true);
				} catch (Exception ex) {
					// ajax call failed
					ex.printStackTrace();
					analVO.setSuccess(false);
				}

			} else {
				// regular static page and dynamic page
				if (type != null && type.equals("ticker")) {
					// System.out.println("ticker: " + analVO.getTicker());
					// Vector stockList = dao.getStockListFromDB(null,
					// analVO.getTicker());
					Vector stockList = DBCachePool.getStockListFromDB(null,
							analVO.getTicker(), dao);
					if (stockList != null && stockList.size() == 1) {
						// just one
						analVO.setDisplay("one");
						Stock stock = (Stock) stockList.get(0);
						analVO.setStockExt(analyzeStock(dao, stock,
								analVO.getPeriod(), analVO));
						analVO.setOnWatchList(checkOnWatchList(
								analVO.getUserId(), stock.getStockId()));
						ticker = stock.getTicker();

						// update the dates
						updateStockDates(dao, analVO, stock.getStockId());
						analVO.setStockId(stock.getStockId());
					} else if (stockList != null && stockList.size() > 1) {
						// more than one is found
						analVO.setDisplay("list");
						analVO.setStockList(stockList);
					} else {
						// not found
						// System.out.println("not found: zero");
						ticker = analVO.getTicker();
						analVO.setDisplay("zero");
						notFound = true;
					}
				} else if (type != null && type.equals("id")) {
					analVO.setDisplay("one");
					// Stock stock = dao.getStockFromDB(analVO.getStockId());
					Stock stock = DBCachePool.getStockFromDB(
							analVO.getStockId(), dao);
					analVO.setStockExt(analyzeStock(dao, stock,
							analVO.getPeriod(), analVO));
					analVO.setTicker(stock.getTicker());
					analVO.setOnWatchList(checkOnWatchList(analVO.getUserId(),
							stock.getStockId()));
					ticker = stock.getTicker();

					// update the dates
					updateStockDates(dao, analVO, stock.getStockId());
					analVO.setStockId(stock.getStockId());
				} else {
					analVO.setDisplay("none");
				}
			}

			// get watch list, which is sorted, no duplicates
			// construct HTML from it
			WatchListDAO watchDao = new WatchListDAO();
			watchDao.setDatabase(database);
			Vector mTickerList = DBCachePool.getWatchStockIdListFromDB(
					analVO.getUserId(), watchDao);
			analVO.setMTickerList(constructHtmlFromList(mTickerList,
					"WatchList"));

			// Update the recent list, if not in watch list
			updateRecentList(analVO, ticker, mTickerList, notFound);
			analVO.setRTickerList(constructHtmlFromList(analVO.getRecentList(),
					"RecentList"));

			// default showflg
			// if (analVO.getShowFlag() == null) {
			// analVO.setShowFlag("M");
			// }
			if (ticker != null) {
				analVO.setLastVisitedTicker(ticker);
			}

			// build up the suggest list for links
			Vector suggestList = new Vector();
			suggestList.addAll(pickSomeFromList(analVO.getRecentList(),
					analVO.getLastVisitedTicker()));
			// suggestList.addAll(pickSomeFromList(mTickerList,
			//		analVO.getLastVisitedTicker()));
			analVO.setSuggestList(suggestList);
			
			// build watch list
			Vector watchTickerList = DBCachePool.getWatchStockTickerListFromDB(
					analVO.getUserId(), watchDao);
			analVO.setWatchList(watchTickerList);
			
			// debug
			PoolUtil util = new PoolUtil();
			util.snap();

		} catch (Exception e) {
			e.printStackTrace();
			analVO.setDisplay("error");
		}

		return analVO;
	}

	private String checkOnWatchList(String userId, String stockId)
			throws Exception {
		String onWatch = "N";
		WatchListDAO watchDao = new WatchListDAO();
		watchDao.setDatabase(database);
		Vector list = watchDao.getWatchListFromDB(userId, null, stockId);
		if (list != null && list.size() > 0) {
			onWatch = "Y";
		}

		return onWatch;
	}

	/**
	 * Analyze this stock.
	 * 
	 * @param dao
	 * @param stk
	 * @return
	 */
	private StockExt analyzeStock(StockAdminDAO dao, Stock stk, int period,
			AnalyzeVO analVO) throws Exception {
		String pageStyle = analVO.getPageStyle();
		if (pageStyle != null && pageStyle.equals(AnalyzeAction.DYNAMIC_D)) {
			return analyzeStockDynamic(dao, stk, analVO, false);
		} else {
			return analyzeStockStatic(dao, stk, period, false); // no mobi
		}
	}

	private StockExt analyzeStockStatic(StockAdminDAO dao, Stock stk,
			int period, boolean isMobi) throws Exception {
		// try to see if it is need to update, ignore if it fails
		try {
			// dao.updateStockDailyTableForOneStock(stk, StockAdminDao.UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// retrieve 6 month data to analyze
		if (period < 3) {
			period = 3; // make sure
		}

		// get from pool, doRSI=N, displayDate = null, weekly = O
		ServiceContext svrContext = DBCachePool.getServiceContext(stk, period,
				null, "O", "N", dao);

		StockExt stockExt = null;
		if (svrContext != null) {
			stockExt = svrContext.getStockExt();
		} else {
			throw new Exception(
					"Error: problem to generate the service context for: "
							+ stk.getStockId());
		}

		return stockExt;

	}

	private StockExt analyzeStockDynamic(StockAdminDAO dao, Stock stk,
			AnalyzeVO analVO, boolean isAjax) throws Exception {
		// try to see if it is need to update, ignore if it fails
		try {
			// dao.updateStockDailyTableForOneStock(stk, StockAdminDao.UPDATE);
		} catch (Exception e) {
			e.printStackTrace();
		}

		// period is fixed - 3 years
		int period = 36;
		int extra = LARGE_EXTRA;
		Date todayDate = new Date();

		String startDate = "";
		String endDate = "";
		String defaultMonthDate = "";

		if (isAjax) {
			period = 24; // adjust to 2 year for ajax calls
			String displayDate = analVO.getDisplayDate(); // format yyyy/mm/dd
			if (displayDate != null && !displayDate.equals("")) {
				// the current will be the display date
				Date theDisplayDate = DateUtil
						.parseDateStringInYYYYMMDDFormat(displayDate);
				if (theDisplayDate == null) {
					// error format in request
					analVO.setSuccess(false);
					analVO.setTotalWeeklyItems(0);
					analVO.setTotalDailyItems(0);
					analVO.setDailyDataStr("");
					analVO.setWeeklyDataStr("");
					return null;
				}

				startDate = OracleUtil.getPrevDateInOracleFormat(
						theDisplayDate, period, extra);
				endDate = OracleUtil.getDateInOracleFormat(theDisplayDate);
				defaultMonthDate = DateUtil.getDateStringInYYYYMMDDFormat(
						theDisplayDate, period);

			} else {
				// error
				analVO.setSuccess(false);
				analVO.setTotalWeeklyItems(0);
				analVO.setTotalDailyItems(0);
				analVO.setDailyDataStr("");
				analVO.setWeeklyDataStr("");
				return null;
			}
		} else {
			// default for the dynamic page
			startDate = OracleUtil.getPrevDateInOracleFormat(null, period,
					extra);
			endDate = OracleUtil.getDateInOracleFormat(null);
			defaultMonthDate = DateUtil.getDateStringInYYYYMMDDFormat(
					todayDate, period);
		}

		// update the stock if market is open and if it is not up to date
		// boolean done = false; // dao.marketCurrentUpdateForOneStock(stk);
		// if (done) {
		// reload the stock
		// stk = dao.getStockFromDB(stk.getStockId());
		// }

		StockExt stockExt = new StockExt();
		stockExt.setStock(stk);

		// System.out.println("Get data for: startDate: " + startDate +
		// ", endDate: " + endDate);

		Vector dailyList = dao.getStockDailyPriceListFromDB(stk.getStockId(),
				startDate, endDate);
		if (dailyList != null && dailyList.size() > 0) {
			// System.out.println("list size: " + dailyList.size());
			if (isAjax && dailyList.size() < 100) {
				// not enough data
				analVO.setSuccess(false);
				analVO.setTotalWeeklyItems(0);
				analVO.setTotalDailyItems(0);
				analVO.setDailyDataStr("");
				analVO.setWeeklyDataStr("");
				return null;
			}
			int defaultMonthIndex = ScanUtil.findStartIndex(dailyList,
					defaultMonthDate);

			ServiceContext svrContext = new ServiceContext();
			svrContext.setStartIndex(defaultMonthIndex);
			svrContext.setStockExt(stockExt);
			svrContext.setPriceList(dailyList);

			// linear model service
			LinearModelService linearModel = new LinearModelService();
			linearModel.findSlopeAndRSquare(svrContext);
			// linearModel.findSlopeAndRSquareForShortTerm(svrContext);

			// growth percentage/per month
			double slope = stockExt.getSlope();
			double percent = (Math.exp(slope * DAYS_IN_MONTH) - 1) * 100;
			DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
			stockExt.setGrowthPerMonth(oneDigitFormatter.format(percent));

			VolumeService volumeService = new VolumeService();
			// volumeService.basicVolumeCheckup(svrContext);
			// volumeService.hasBigDayInShortTerm(svrContext);
			// volumeService.basicVolumeCheckupForShortTerm(svrContext);

			//
			RangeService rangeService = new RangeService();
			rangeService.findMinMax(svrContext);
			rangeService.findAverage20(svrContext); // 21-day average
			rangeService.findAverage50(svrContext, false); // 50-day average
			rangeService.findAverage100S(svrContext, RangeService.RANGE_100); // 100-day
																				// average
			rangeService.findAccumulationForceForAll(svrContext, false);

			// find trend indicators - using 20/50/100 and slope/acc
			rangeService.findTrendIndicators(svrContext);

			// This has to be done after 21/50, and volume
			WarningService warningService = new WarningService();
			warningService.findWarning(svrContext);

			// find William Index
			rangeService.findWilliamIndexs(svrContext);

			// after slope/acc
			rangeService.findSignalStrength(svrContext);

			// find the correlation index
			stockExt.setCorrelationIndex(UIStockUtil.getCorrelationIndex(
					svrContext.getStartIndex(), svrContext.getPriceList()));
			// stockExt.setOscillateIndex(StockUtil.getOscillateIndex(svrContext.getStartIndex(),
			// svrContext.getPriceList()));

			// set OBV
			// volumeService.findOBV(svrContext);

			// collect the dataStr
			analVO.setDailyDataStr(collectDataStr(svrContext.getPriceList(),
					svrContext.getStartIndex()));
			analVO.setTotalDailyItems(svrContext.getPriceList().size()
					- svrContext.getStartIndex());

			// generate weekly data
			// System.out.println("Price List size: " + dailyList.size());
			UIStockUtil.adjustPriceListForWeeklyChart(svrContext);
			rangeService.findAverage20(svrContext); // 21-day average
			rangeService.findAverage50(svrContext, false); // 50-day average
			rangeService.findAverage100S(svrContext, RangeService.RANGE_100);
			rangeService.findAccumulationForceForAll(svrContext, false);

			// find trend indicators - using 20/50/100 and slope/acc
			rangeService.findTrendIndicators(svrContext);

			analVO.setWeeklyDataStr(collectDataStr(svrContext.getPriceList(),
					svrContext.getStartIndex()));
			analVO.setTotalWeeklyItems(svrContext.getPriceList().size()
					- svrContext.getStartIndex());

			// pack the market data
			MarketIndicators marketIndicators = MarketCachePool
					.getMarketIndicators();

			Vector marketFlagList = marketIndicators.getMarketFlagList();
			analVO.setMarketFlagList(collectMarketFlag(marketFlagList));

			marketFlagList = marketIndicators.getMarketWeeklyFlagList();
			analVO.setMarketWeeklyFlagList(collectMarketFlag(marketFlagList));

			analVO.setSuccess(true);

		} else {
			analVO.setSuccess(false);
			analVO.setTotalWeeklyItems(0);
			analVO.setTotalDailyItems(0);
			analVO.setDailyDataStr("");
			analVO.setWeeklyDataStr("");
		}

		return stockExt;

	}

	// do not put to the recent list, if it is already in the watch list
	// if notFound == true, then remove this from the ticker list;
	// else check it and add to it.
	protected void updateRecentList(AnalyzeVO analVO, String ticker,
			Vector watchList, boolean notFound) throws Exception {
		UserStoreDAO storeDao = new UserStoreDAO();
		storeDao.setDatabase(database);

		if (ticker != null && ticker.indexOf("^") > -1) {
			ticker = null;
		}

		System.out.println("updateRecentList, not found: " + notFound);

		if (!notFound && (ticker == null || ticker.equals(""))) {
			// Blank screen, no ticker entered
			if (analVO.getRecentList() != null
					&& analVO.getRecentList().size() > 0) {
				// no need to reload
				// System.out.println("No need to reload ...");
			} else {
				// System.out.println("Reload ...");
				String recentList = getRecentListStrFromDB(storeDao,
						analVO.getUserId());
				if (recentList != null && !recentList.equals("")) {
					analVO.setRecentList(convertListToVector(recentList));
				} else {
					analVO.setRecentList(new Vector());
				}
			}
		} else {
			// make sure this ticker is on
			Vector oldList = analVO.getRecentList();
			if (notFound) {
				// not found, if it is in recent list, need to remove it
				if (oldList == null || oldList.isEmpty()
						|| oldList.contains(ticker)) {
					// System.out.println("not found: old is null");
					String dbRecentList = getRecentListStrFromDB(storeDao,
							analVO.getUserId());
					String newList = removeTickerToList(dbRecentList, ticker);
					if (dbRecentList == null) {
						// nothing in DB - new List is null as well
					} else if (newList != null && newList.equals(dbRecentList)) {
						// it is not a new ticker - no need to update the DB
					} else {
						if (newList == null) {
							newList = "";
						}

						// update the DB
						storeDao.updateRecentListEntryToDB(analVO.getUserId(),
								newList);
					}

					analVO.setRecentList(convertListToVector(newList));
				} else {
					System.out.println("not found: here");
				}
			} else {
				if ((oldList != null && oldList.contains(ticker))
						|| (watchList != null && watchList.contains(ticker))) {
					// do nothing - repeat ticker in the session, or it is
					// already in watchList
					// System.out.println("Ticker already in recent list ...");
				} else {
					String dbRecentList = getRecentListStrFromDB(storeDao,
							analVO.getUserId());
					String newList = addTickerToList(dbRecentList, ticker,
							watchList);
					if (dbRecentList == null && newList != null) {
						// nothing in DB - need add
						insertUserStore(storeDao, analVO.getUserId(), newList);
					} else if (newList.equals(dbRecentList)) {
						// it is not a new ticker - no need to update the DB
					} else {
						// update the DB
						storeDao.updateRecentListEntryToDB(analVO.getUserId(),
								newList);
					}

					analVO.setRecentList(convertListToVector(newList));
				}
			}

		}

		// in case recentlist is not in bean - possible if it is not loaded
		if (analVO.getRecentList() == null) {
			String dbRecentList = getRecentListStrFromDB(storeDao,
					analVO.getUserId());
			analVO.setRecentList(convertListToVector(dbRecentList));
		}
	}

	/**
	 * Convert string to vector and sort it.
	 * 
	 * @param recentList
	 * @return
	 */

	private Vector convertListToVector(String recentList) {
		if (recentList == null || recentList.equals("")) {
			return new Vector();
		}

		Vector list = StringHelper.mySplit(recentList, COMMA_CHAR);
		Collections.sort(list);

		return list;
	}

	// ticker is not null
	private String addTickerToList(String dbRecentList, String ticker,
			Vector watchList) {
		if (dbRecentList == null || dbRecentList.equals("")) {
			return ticker;
		}

		String commaDB = COMMA_CHAR + dbRecentList + COMMA_CHAR;
		String commaTicker = COMMA_CHAR + ticker + COMMA_CHAR;
		if (commaDB.indexOf(commaTicker) > -1) {
			// already in list
			return dbRecentList;
		} else {
			if (watchList != null && watchList.contains(ticker)) {
				return dbRecentList;
			}

			String newList = dbRecentList + COMMA_CHAR + ticker;
			while (newList.length() > 1950) {
				int index = newList.indexOf(COMMA_CHAR);

				// System.out.println("Droping: " + newList.substring(0,
				// index));
				newList = newList.substring(index + 1);

			}

			return newList;
		}
	}

	private String removeTickerToList(String dbRecentList, String ticker) {
		if (dbRecentList == null || dbRecentList.equals("")) {
			return "";
		}

		String newList = null;
		String commaDB = COMMA_CHAR + dbRecentList + COMMA_CHAR;
		String commaTicker = COMMA_CHAR + ticker + COMMA_CHAR;
		int index = commaDB.indexOf(commaTicker);
		if (index > -1) {
			String leftHalf = "";
			String rightHalf = "";

			// in the list, need to remove
			if (index > 0) {
				leftHalf = commaDB.substring(0, index); // without comma at end
			}

			if (index + commaTicker.length() < commaDB.length()) {
				rightHalf = commaDB.substring(index + commaTicker.length());
			}

			String temp = leftHalf + COMMA_CHAR + rightHalf;
			newList = temp.substring(COMMA_CHAR.length(), temp.length()
					- COMMA_CHAR.length());

		} else {
			// not in the list
			newList = dbRecentList;
		}

		return newList;
	}

	private void insertUserStore(UserStoreDAO storeDao, String userId,
			String recentList) throws Exception {
		UserStore userStore = new UserStore();
		userStore.setUserId(userId);
		userStore.setAlertList("");
		userStore.setRecentList(recentList);
		storeDao.insertUserStoreEntryToDB(userStore);

	}

	private String getRecentListStrFromDB(UserStoreDAO storeDao, String userId)
			throws Exception {
		// try to load from DB
		UserStore userStore = storeDao.getUserStoreFromDB(userId);
		if (userStore != null && userStore.getRecentList() != null) {
			return userStore.getRecentList();
		} else if (userStore != null) {
			return "";
		}

		return null;
	}

	/**
	 * format: date | open | close | min | max | logAvg9 | logAvg21 | logAvg50 |
	 * volume | growth | force | trendColor
	 * 
	 * @param priceList
	 * @return
	 */
	private String collectDataStr(Vector priceList, int startIndex) {
		StringBuffer buf = new StringBuffer();
		DecimalFormat twoDigitFormatter = new DecimalFormat("0.00");
		DecimalFormat fourDigitFormatter = new DecimalFormat("0.000000"); // 6
																			// digits

		if (priceList != null && priceList.size() > startIndex) {
			for (int i = startIndex; i < priceList.size(); i++) {
				StockDailyInfo item = (StockDailyInfo) priceList.get(i);
				if (i > startIndex) {
					buf.append("#");
				}

				buf.append(item.getMDate() + "|");
				buf.append(twoDigitFormatter.format(item.getOpen()) + "|");
				buf.append(twoDigitFormatter.format(item.getClose()) + "|");
				buf.append(twoDigitFormatter.format(item.getMin()) + "|");
				buf.append(twoDigitFormatter.format(item.getMax()) + "|");
				if (item.getAverage20() > 0) {
					buf.append(fourDigitFormatter.format(item.getAverage20Log())
							+ "|");
				} else {
					buf.append("20|"); // used for not defined, use big #, all
										// number will < e^20
				}

				if (item.getAverage50() > 0) {
					buf.append(fourDigitFormatter.format(item.getAverage50Log())
							+ "|");
				} else {
					buf.append("20|"); // used for not defined
				}

				if (item.getAverage100() > 0) {
					buf.append(fourDigitFormatter.format(item
							.getAverage100Log()) + "|");
				} else {
					buf.append("20|"); // not defined
				}

				buf.append(item.getVolume() + "|");
				buf.append(fourDigitFormatter.format(item.getSlope()) + "|");
				buf.append(fourDigitFormatter.format(item.getAcceleration())
						+ "|");
				buf.append(item.getTrendColor());
			}
		}

		return buf.toString();
	}

	private String collectMarketFlag(Vector marketFlagList) {
		StringBuffer buf = new StringBuffer();
		if (marketFlagList != null) {
			for (int i = 0; i < marketFlagList.size(); i++) {
				if (i > 0) {
					buf.append("|");
				}

				buf.append((String) marketFlagList.get(i));
			}
		}

		return buf.toString();
	}

	/**
	 * Get the start date and end of dates for the ticker; This will be used at
	 * the client side to calculate the dates when user is trying to move the
	 * dates.
	 */
	private void updateStockDates(StockAdminDAO dao, AnalyzeVO analVO,
			String stockId) throws Exception {
		String[] dates = DBCachePool.getStockDateRangeFromDB(stockId, dao);
		if (dates != null && dates.length == 2) {
			Date firstDate = null;
			Date lastDate = null;

			// move this formatter here to avoid static concurrent issue
			SimpleDateFormat date_formatter = new SimpleDateFormat("yyyy/MM/dd");

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
				analVO.setStartDate(dates[1]); // same as start date
				analVO.setEndDate(dates[1]);
			} else {
				analVO.setStartDate(date_formatter.format(CALENDAR.getTime()));
				analVO.setEndDate(dates[1]);
			}
		}
	}

	private String constructHtmlFromList(Vector tickerList, String whichType) {
		StringBuffer buf = new StringBuffer();

		String scriptStr = "";
		String buttonValue = "";
		String zeroNotice = "";
		if (whichType != null && whichType.equals("RecentList")) {
			scriptStr = "flipTickerListM()";
			buttonValue = "#";
			zeroNotice = "<font color=\"#0000ff\">Your recent viewed tickers go here ...</font>";
		} else {
			scriptStr = "flipTickerListR()";
			buttonValue = "*";
			zeroNotice = "<font color=\"#0000ff\">Your watch list tickers go here ...</font>";
		}

		// buf.append("<input type='button' onclick='" + scriptStr + "' value='"
		// + buttonValue + "' class='tug_button' />");    
		// buf.append("<input type='button'  value='X' class='tug_button' />");

		// submit will auto close the window as the new window is loading
		// if we want to close right away, need to set: onclick=\"QQ_chartoverlay.close_div();\"
		if (tickerList != null && tickerList.size() > 0) {
			for (int i = 0; i < tickerList.size(); i++) {
				buf.append("<A href=\"javaScript:Q.chart.goTicker('"
						+ tickerList.get(i)
						+ "')\" class=\"datalink2\">"
						+ tickerList.get(i) + "</a> ");
			}
		} else {
			buf.append(zeroNotice);
		}

		return buf.toString();
	}

	/**
	 * masterList is sorted list; go through the list, find the next few tickers
	 * on the list and return
	 * 
	 * @param masterList
	 * @param ticker
	 * @return
	 */
	private Vector pickSomeFromList(Vector masterList, String ticker) {
		int NUM_TICKERS = 42;
		if (masterList == null || masterList.size() == 0) {
			return new Vector(); // empty
		}

		int posIndex = 0;
		if (ticker == null || ticker.equals("")) {
			posIndex = 0;
		} else {
			// try to find the posIndex
			for (int i = 0; i < masterList.size(); i++) {
				if (ticker.compareTo((String) masterList.get(i)) < 0) {
					posIndex = i;
					break;
				}
			}
		}

		// we need to pick # tickers start with posIndex
		int listSize = masterList.size();
		if (listSize <= NUM_TICKERS) {
			return masterList;
		}

		Vector retList = new Vector();
		for (int i = 0; i < NUM_TICKERS; i++) {
			if (posIndex + i <= listSize - 1) {
				retList.add(masterList.get(posIndex + i));
			} else {
				retList.add(masterList.get(posIndex + i - listSize));
			}
		}

		return retList;

	}
}

/* end */