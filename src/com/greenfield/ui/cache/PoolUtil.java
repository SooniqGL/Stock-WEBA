/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.cache;

import com.greenfield.common.constant.AgeSrchConstants;
import com.greenfield.common.dao.analyze.AgeSrchDAO;
import com.greenfield.common.object.scan.AgeSrchInfo;
import com.greenfield.common.object.scan.StockAge;

import java.util.Collections;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author qz69042
 */
public class PoolUtil {
	private static final Logger LOGGER = Logger.getLogger(PoolUtil.class);

	private static final int STOCKAGE_BEST_LIST_SIZE = 50;
	
	public static void resetPools(String poolListStr) {
		if (poolListStr == null || poolListStr.equals("")) {
			LOGGER.warn("PoolUtil:resetPools: poolListStr is null");
			return;
		}

		// poolListStr format: {cache name} | {cache name} +
		String[] cacheNames = poolListStr.split(":");
		for (int i = 0; i < cacheNames.length; i++) {
			if (CacheNames.DB_CACHE.equals(cacheNames[i])) {
				DBCachePool.resetPool();
			} else if (CacheNames.MARKET_CACHE.equals(cacheNames[i])) {
				MarketCachePool.resetPool();
			} else if (CacheNames.SCAN_CACHE.equals(cacheNames[i])) {
				ScanCachePool.resetPool();
			} else {
				LOGGER.warn("PoolUtil:resetPools: cache name is not supported: <"
						+ cacheNames[i] + ">");
			}
		}

	}

	public static void resetAllPools() {
		DBCachePool.resetPool();
		MarketCachePool.resetPool();
		ScanCachePool.resetPool();
	}

	public static void snap() {
		DBCacheStore.getInstance().snap();
		MarketCacheStore.getInstance().snap();
		ScanCacheStore.getInstance().snap();
	}

	public static Vector buildStockAgeSummaryList(AgeSrchDAO ageDao) {
		Vector sumList = new Vector();

		try {
			// find all the data from database, and then go through them, and
			// make the summary list
			Vector ageList = ageDao.getStockAgeSummaryListFromDB(); // all of
																	// them

			HashMap hash = new HashMap(); // does matter the order
			int maxGroup = (int) AgeSrchConstants.MAX_AGE / 10 + 1;

			// split by age 0
			if (ageList != null && ageList.size() > 0) {

				for (int i = 0; i < ageList.size(); i++) {
					StockAge stockAge = (StockAge) ageList.get(i);

					// debug
					// System.out.println("stockage: " + stockAge.toString());

					double age = stockAge.getAge51();
					double counter = stockAge.getCounter();

					int indexFor = (int) age / 10;
					if (age > 0) {
						indexFor++;
					} else {
						indexFor--;
					}

					String key = String.valueOf(indexFor);
					if (hash.containsKey(key)) {
						counter += ((Double) hash.get(key)).doubleValue();
					}

					// System.out.println("key: " + key + ", value: " +
					// counter);

					hash.put(key, new Double(counter));
				}

				// regroup it
				for (int i = -maxGroup; i <= maxGroup; i++) {
					String key = String.valueOf(i);

					if (hash.containsKey(key)) {

						AgeSrchInfo info = new AgeSrchInfo();
						info.setAgeRange(key);

						String val = String.valueOf(((Double) hash.get(key))
								.doubleValue());
						if (val.indexOf(".") > -1) {
							val = val.substring(0, val.indexOf("."));
						}
						info.setNumEntries(val);

						// System.out.println("has key2: " + key + ", val: "+
						// val );

						sumList.add(info);
					} else {
						// warning
						// System.out.println("do not have key2: " + key );
					}

				}
			}

			if (sumList.size() > 0) {
            	// add the best entry for it
            	AgeSrchInfo info = new AgeSrchInfo();
				info.setAgeRange("BEST");
				info.setNumEntries(String.valueOf(STOCKAGE_BEST_LIST_SIZE));
				sumList.add(info);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sumList;

	}
	
	// get best 100 stocks from the age list
	public static Vector buildStockAgeBestList(AgeSrchDAO ageDao) {
		Vector<StockAge> newList = new Vector<StockAge>();

		try {
			// find all the data (age >= 50) from database, and then go through them, and
			// make the best list
			Vector ageList = ageDao.getStocAgeListFromDB(null, "50", null);
			
			// find the best
			if (ageList != null && ageList.size() > 0) {
				for (int i = 0; i < ageList.size(); i ++) {
					StockAge stk = (StockAge) ageList.get(i);
					if (stk.getCurrPrice() > 10) {
						newList.add(stk);
					}
				}
				
				StockAgeComp stockAgeComp = new StockAgeComp();
				Collections.sort(newList, stockAgeComp);
				
				if (newList.size() > STOCKAGE_BEST_LIST_SIZE) {
					int listSize = newList.size();
					for (int i = listSize - 1; i >= STOCKAGE_BEST_LIST_SIZE; i --)  {
						newList.remove(i);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return newList;

	}
}
