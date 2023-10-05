/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.cache;

import com.greenfield.common.util.StockUtil;
import com.greenfield.ui.handler.market.MarketAnalysis;
import com.greenfield.common.dao.analyze.MarketPulseDAO;
import com.greenfield.common.object.market.MarketPulse;
import com.greenfield.common.object.stock.MarketIndicators;

import java.util.Vector;

import org.apache.log4j.Logger;

/**
 *
 * @author QZ69042
 */
public class MarketCachePool {
	private static final Logger LOGGER = Logger.getLogger(MarketCachePool.class); 
	
        public static void resetPool() {
            try {
                // simple remove the cached data from the pool
                MarketCacheStore pool = MarketCacheStore.getInstance();

                // just call
                LOGGER.warn("MarketCachePool resetPool() is called.");
                pool.reset();

                // update the market indicator
                getMarketIndicators();

            } catch (Exception e) {
                LOGGER.warn("MarketCachePool: cannot reset pool: " + e.getMessage());
                e.printStackTrace();
            }

        }

        public static MarketIndicators getMarketIndicators() throws Exception {
            MarketCacheStore pool = MarketCacheStore.getInstance();

            // in this call, we allow null for marketType and/or ticker
            String key = ObjectPoolKeys.MARKET_INDICATORS_KEY + "A";

            MarketIndicators indicators = (MarketIndicators) pool.getObject(key);

            if (indicators == null) {
                // not in cache
                indicators = StockUtil.getMarketIndicators();

                if (indicators != null) {
                    pool.loadObject(key, indicators);

                    // update the market skill field
                    MarketAnalysis.doMarketAnalysis();
                }
            }

            return indicators;
        }

        public static Vector getMarketPulseDateList(String marketType, MarketPulseDAO marketDao) throws Exception {
            if (marketType == null || marketType.equals("")) {
                return null;
            }

            String poolKey = ObjectPoolKeys.MARKET_PULSEDATE_LIST_KEY + marketType;
            MarketCacheStore pool = MarketCacheStore.getInstance();
            Vector dateList = null;

            try {
                dateList = (Vector) pool.getObject(poolKey);
            } catch (Exception e) {
                // catch exception,
                LOGGER.warn("Pool for market pulse date list error: " + e.getMessage());
            }

            // if it is not in pool
            if (dateList == null) {
                Vector marketPulseList = marketDao.getMarketPulseDateListFromDB(marketType);
                dateList = retrieveDateList(marketPulseList);

                // and put this to pool
                pool.loadObject(poolKey, dateList);
            } else {
                // info - debug
                //LOGGER.warn("Info: Pool is used to get scan report date list with key: " + poolKey);
            }

            return dateList;

        }

        private static Vector retrieveDateList(Vector marketPulseList) {
            int SCREEN_ITEM_SIZE = 200;  // 200;  // roughly 9 MONTHS
            String DATA_TRUNC_DATE = "2005/05/01";

            Vector pairList = new Vector();
            if (marketPulseList != null) {
                boolean hasTail = false;
                int listSize = marketPulseList.size();
                for (int i = 0; i < listSize; i += SCREEN_ITEM_SIZE) {
                    String scanDate = ((MarketPulse) marketPulseList.get(i)).getMDate();
                    if (scanDate.compareTo(DATA_TRUNC_DATE) < 0) {
                        // do not want the data before the truncate date
                        hasTail = false;
                        break;
                    }
                    pairList.add(new String(scanDate));
                    if (i != (listSize - 1)) {
                        hasTail = true;
                    }
                }

                if (hasTail) {
                    // add the very last one to the list
                    String lastScanDate = ((MarketPulse) marketPulseList.get(listSize - 1)).getMDate();
                    pairList.add(new String(lastScanDate));
                }
            }

            return pairList;
        }
}
