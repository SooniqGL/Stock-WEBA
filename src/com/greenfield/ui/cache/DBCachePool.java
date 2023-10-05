/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.cache;

import com.greenfield.common.service.ServiceContext;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.StringHelper;
import com.greenfield.ui.handler.exam.ExamHandler;
import com.greenfield.ui.util.UIStockUtil;
import com.greenfield.common.object.member.WatchInfo;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.dao.member.PortfolioDAO;
import com.greenfield.common.dao.member.WatchListDAO;
import com.greenfield.common.object.stock.Stock;

import java.util.Collections;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author QZ69042
 */
public class DBCachePool {
	private static final Logger LOGGER = Logger.getLogger(DBCachePool.class);

	public static void resetPool() {
		try {
			// simple remove the cached data from the pool
			DBCacheStore pool = DBCacheStore.getInstance();

			// just call
			LOGGER.warn("DBCachePool resetPool() is called.");
			pool.reset();
		} catch (Exception e) {
			LOGGER.warn("DBCachePool: cannot reset pool: " + e.getMessage());
			e.printStackTrace();
		}

	}

	public static Vector getStockListFromDB(String marketType,
			StockAdminDAO stockDao) throws Exception {
		return getStockListFromDB(marketType, null, stockDao);
	}

	/**
	 * Get stock list from the db.
	 * 
	 * @return
	 * @throws Exception
	 */
	public static Vector getStockListFromDB(String marketType, String ticker,
			StockAdminDAO stockDao) throws Exception {
		DBCacheStore pool = DBCacheStore.getInstance();

		// in this call, we allow null for marketType and/or ticker
		String key = ObjectPoolKeys.DB_STOCKLIST_KEY
				+ StringHelper.nvl(marketType, "*") + "-"
				+ StringHelper.nvl(ticker, "*");

		Vector stockList = (Vector) pool.getObject(key);

		if (stockList == null) {
			// not in cache
			stockList = stockDao.getStockListFromDB(marketType, ticker, null);

			if (stockList != null) {
				pool.loadObject(key, stockList);
			}
		}

		return stockList;
	}

	public static Stock getStockFromDB(String stockId, StockAdminDAO stockDao)
			throws Exception {
		if (stockId == null || stockId.equals("")) {
			// bad - we want to prevent from over write or get
			throw new Exception("DBCachePool: stockId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		String key = ObjectPoolKeys.DB_STOCK_KEY + stockId;

		Stock stock = (Stock) pool.getObject(key);

		if (stock == null) {
			// not in cache
			stock = stockDao.getStockFromDB(stockId);

			if (stock != null) {
				pool.loadObject(key, stock);
			}
		}

		return stock;
	}

	public static String[] getStockDateRangeFromDB(String stockId,
			StockAdminDAO stockDao) throws Exception {
		if (stockId == null || stockId.equals("")) {
			// bad - we want to prevent from over write or get
			throw new Exception("DBCachePool: stockId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		String key = ObjectPoolKeys.DB_STOCKDATE_RANGE_KEY + stockId;

		String[] dateRange = (String[]) pool.getObject(key);

		if (dateRange == null) {
			// not in cache
			dateRange = stockDao.getMinMaxDates(stockId);

			if (dateRange != null) {
				pool.loadObject(key, dateRange);
			}
		}

		return dateRange;
	}

	public static Vector getWatchStockIdListFromDB(String userId,
			WatchListDAO watchDao) throws Exception {
		if (userId == null || userId.equals("")) {
			// bad - we want to prevent from over write or get
			throw new Exception("DBCachePool: userId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		// user id cannot be null
		String key = ObjectPoolKeys.DB_WATCH_STOCKIDLIST_KEY + userId;

		Vector tickerList = (Vector) pool.getObject(key);

		if (tickerList == null) {
			// not in cache
			Vector watchInfoList = watchDao.getWatchListFromDB(userId);
			tickerList = buildTickerListFromWatchList(watchInfoList);

			if (tickerList == null) {
				tickerList = new Vector();
			}

			pool.loadObject(key, tickerList);
		}

		return tickerList;
	}
	
	
	public static Vector getWatchStockTickerListFromDB(String userId,
			WatchListDAO watchDao) throws Exception {
		if (userId == null || userId.equals("")) {
			throw new Exception("getWatchStockIdListFromDB2: userId is null.");
		}

		// query every time
		Vector watchInfoList = watchDao.getWatchListFromDB(userId);
		Vector tickerList = buildTickerListFromWatchList(watchInfoList);

		if (tickerList == null) {
			tickerList = new Vector();
		}

		return tickerList;
	}

	/**
	 * This one is used if the user is going to insert or delete watch list.
	 * 
	 * @param userId
	 * @throws Exception
	 */
	public static void resetWatchStockIdListInCache(String userId)
			throws Exception {
		if (userId == null || userId.equals("")) {
			// bad - we want to prevent from over write or get
			throw new Exception("DBCachePool: userId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		// userId cannot be null
		String key = ObjectPoolKeys.DB_WATCH_STOCKIDLIST_KEY + userId;

		pool.removeObject(key);
	}

	public static ServiceContext getServiceContext(Stock stock, int period,
			String displayDate, String weekly, String doRSI,
			StockAdminDAO stockDao) throws Exception {
		if (stock == null || stock.getStockId() == null) {
			// bad - we want to prevent from over write or get
			throw new Exception("DBCachePool: stockId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		String key = ObjectPoolKeys.DB_SERVICE_CONTEXT_KEY + stock.getStockId()
				+ ":" + period + ":" + StringHelper.nvl(displayDate, "*") + ":"
				+ weekly + ":" + doRSI;

		ServiceContext svcContext = (ServiceContext) pool.getObject(key);

		if (svcContext == null) {
			// not in cache
			svcContext = UIStockUtil.buildServiceContext(stock, period,
					displayDate, weekly, doRSI, stockDao);

			if (svcContext != null) {
				pool.loadObject(key, svcContext);
			}
		}

		return svcContext;
	}
	
	// slope is MACD for this case
	public static ServiceContext getServiceContextX(Stock stock, int period,
			String displayDate, String weekly, String doRSI,
			StockAdminDAO stockDao) throws Exception {
		if (stock == null || stock.getStockId() == null) {
			// bad - we want to prevent from over write or get
			throw new Exception("DBCachePool: stockId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		String key = ObjectPoolKeys.DB_SERVICE_CONTEXT_KEY_X + stock.getStockId()
				+ ":" + period + ":" + StringHelper.nvl(displayDate, "*") + ":"
				+ weekly + ":" + doRSI;

		ServiceContext svcContext = (ServiceContext) pool.getObject(key);

		if (svcContext == null) {
			// not in cache
			svcContext = UIStockUtil.buildServiceContextX(stock, period,
					displayDate, weekly, doRSI, stockDao);

			if (svcContext != null) {
				pool.loadObject(key, svcContext);
			}
		}

		return svcContext;
	}

	// get stock id list from watchInfo list
	// and sort them, remove the duplicates
	private static Vector buildTickerListFromWatchList(Vector watchInfoList) {
		int MAX_TICKERS = 140;
		if (watchInfoList == null || watchInfoList.size() == 0) {
			return null;
		}

		Vector tickerList = new Vector();
		for (int i = 0; i < watchInfoList.size(); i++) {
			tickerList.add(((WatchInfo) watchInfoList.get(i)).getTicker());
		}

		// sort it
		Collections.sort(tickerList);

		// remove the duplicate
		String prevTicker = "";
		int cnt = 0;
		for (int i = tickerList.size() - 1; i >= 0 && cnt < MAX_TICKERS; i--) {
			String ticker = (String) tickerList.get(i);
			if (ticker.equals(prevTicker)) {
				// remove this one
				tickerList.remove(i);

				// debug
				// System.out.println("Removed from memeber watch id list: " +
				// ticker);
			} else {
				// reset
				prevTicker = ticker;
				cnt++;
			}
		}

		return tickerList;

	}

	public static Vector getPortfolioListFromDB(String userId,
			PortfolioDAO portfolioDao) throws Exception {
		if (userId == null || userId.equals("")) {
			// bad - we want to prevent from over write or get
			throw new Exception(
					"DBCachePool: getPortfolioListFromDB - userId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		// user id cannot be null
		String key = ObjectPoolKeys.DB_PORTFOLIO_LIST_KEY + userId;

		Vector portfolioList = (Vector) pool.getObject(key);

		if (portfolioList == null) {
			// not in cache
			portfolioList = portfolioDao.getPortfolioListFromDB(userId, null);

			if (portfolioList == null) {
				portfolioList = new Vector();
			}

			pool.loadObject(key, portfolioList);
		}

		return portfolioList;
	}

	/**
	 * this method is used when a user has add/update some portfolios
	 * 
	 * @param userId
	 * @param portfolioDao
	 * @throws Exception
	 */
	public static void resetPortfolioListFromDB(String userId,
			PortfolioDAO portfolioDao) throws Exception {
		if (userId == null || userId.equals("")) {
			// bad - we want to prevent from over write or get
			throw new Exception(
					"DBCachePool: getPortfolioListFromDB - userId is null.");
		}

		DBCacheStore pool = DBCacheStore.getInstance();

		// user id cannot be null
		String key = ObjectPoolKeys.DB_PORTFOLIO_LIST_KEY + userId;

		// get from DB and reset the one in pool
		Vector portfolioList = portfolioDao
				.getPortfolioListFromDB(userId, null);

		if (portfolioList == null) {
			portfolioList = new Vector();
		}

		pool.loadObject(key, portfolioList);

	}
	
	// called at the system start up time
	public static void generalExamStockList() {
		DBComm database = new DBComm();
		
		try {
			long bt = System.currentTimeMillis();
			database.openConnection();
			
			StockAdminDAO stockDao = new StockAdminDAO();
			stockDao.setDatabase(database);
			
			ExamHandler handler = new ExamHandler();
			Vector examList = handler.generateExamStockList(stockDao);
			DBCacheStore pool = DBCacheStore.getInstance();

			String key = ObjectPoolKeys.DB_EXAMSTOCKLIST_KEY + "examlist";
			if (examList != null) {
				pool.loadObject(key, examList);
			}
			long sp = System.currentTimeMillis() - bt;
			LOGGER.warn("DBCachePool: time to load the exam list: " + sp);
		} catch (Exception e) {
			LOGGER.warn("DBCachePool: cannot load exam list: " + e.getMessage());
			e.printStackTrace();
		} finally {
			try {
				database.closeConnection();
			} catch (Exception ex) {
				// ignore
			}
		}
	}
	
	public static Vector getExamStockList() {
		Vector examList = null;
		
		try {
			DBCacheStore pool = DBCacheStore.getInstance();

			String key = ObjectPoolKeys.DB_EXAMSTOCKLIST_KEY + "examlist";
			examList = (Vector) pool.getObject(key);

			if (examList == null) {
				// may be not in the cache anymore, generate again
				generalExamStockList();
				examList = (Vector) pool.getObject(key);
				LOGGER.warn("DBCachePool: regenerate exam list ... !!!");
				if (examList == null) {
					LOGGER.warn("DBCachePool: exam list, still null after regenerating ... !!!");
				}
			}
		} catch (Exception e) {
			LOGGER.warn("DBCachePool: cannot get exam list: " + e.getMessage());
			e.printStackTrace();
		}
		
		return examList;
	}
}
