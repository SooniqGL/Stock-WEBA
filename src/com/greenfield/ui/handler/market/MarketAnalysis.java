/*
 * Created on Dec 24, 2006
 */
package com.greenfield.ui.handler.market;

import java.util.Date;
import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.common.dao.analyze.MarketPulseDAO;
//import com.greenfield.admin.handler.process.CheckWatchProcess;
//import com.greenfield.ui.common.constant.MarketCodes;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.MarketUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.object.market.MarketPulse;
import com.greenfield.common.constant.MarketTypes;
import com.greenfield.common.constant.TrendTypes;
import com.greenfield.common.object.stock.MarketIndicators;

/**
 * @author qin
 *
 * Figure the general strategy according the market general data.
 */
public class MarketAnalysis {
	private static final Logger LOGGER = Logger.getLogger(MarketAnalysis.class); 
	
	private static final double BPI_LOW_LIMIT 	= 0.35;
	private static final double BPI_HIGH_LIMIT 	= 0.70;
	
	public static void doMarketAnalysis() throws Exception {
		int period = 12;
		int EXTRA_DAYS = 100;

                MarketIndicators marketIndicators = MarketCachePool.getMarketIndicators();
                DBComm database = new DBComm();

		try {
			database.openConnection();
		
			String startDate = OracleUtil.getPrevDateInOracleFormat(null, period, EXTRA_DAYS);
			String startDateInReal = DateUtil.getDateStringInYYYYMMDDFormat(new Date(), period);
			MarketPulseDAO marketDao = new MarketPulseDAO();
			marketDao.setDatabase(database);
			
			// use the NewYork market
			Vector pulseList = marketDao.getMarketPulseListFromDB(MarketTypes.NEWYORK, startDate, null);
			int startIndex = 0;

			if (pulseList != null && pulseList.size() > 0) {
				// find average BPI
				startIndex = MarketUtil.findStartIndexForMarketPulseList(pulseList, startDateInReal);
				MarketUtil.findAverageBPI(pulseList, startIndex);
				
				// last one
				MarketPulse marketPulse = (MarketPulse) pulseList.get(pulseList.size() - 1);
				
				// S&P index - up or down
                                String marketFlag = "";
				Vector marketFlagList = marketIndicators.getMarketFlagList();
				if (marketFlagList != null && marketFlagList.size() > 0) {
					marketFlag = (String) marketFlagList.get(marketFlagList.size() - 1);
				}
				
				// do by cases
				String marketSkill = "";
				if (marketFlag.equals(TrendTypes.GREEN)) {
					
					// above average - assume BPI is going up
					if (marketPulse.getTotalAbove50() >= BPI_LOW_LIMIT 
						&& marketPulse.getTotalAbove50() <= BPI_HIGH_LIMIT) {
						if (marketPulse.getTotalAbove50() >= marketPulse.getAverageBPI()) {
							marketSkill = "Buy - very positive";
						} else {
							marketSkill = "Buy - but market is getting weak";
						}
					} else if (marketPulse.getTotalAbove50() < BPI_LOW_LIMIT) {
						// over sold, buy
						marketSkill = "Oversold market, buy with caution";
						
					} else {
						// over bought, buy with caution
						marketSkill = "Overbought market, buy or hold with caution";
					}
					
				} else {  // sp500 - market index is going down - 
					
					// above average - assume BPI is going up
					if (marketPulse.getTotalAbove50() >= BPI_LOW_LIMIT 
						&& marketPulse.getTotalAbove50() <= BPI_HIGH_LIMIT) {
						if (marketPulse.getTotalAbove50() >= marketPulse.getAverageBPI()) {
							marketSkill = "Buy or wait for sp500 to turn to buy";
						} else {
							marketSkill = "Sell short - market is going down";
						}
						
					} else if (marketPulse.getTotalAbove50() < BPI_LOW_LIMIT) {
						// over sold, buy
						marketSkill = "Oversold market, buy with caution (or wait for sp500 to turn to buy)";
						
					} else {
						// over bought, sell with caution
						marketSkill = "Overbought market, hold with caution or sell short";
					}
				}
				
				marketIndicators.setMarketSkill(marketSkill);
			}	
			//}								
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Error in finding market analysis: " + e.getMessage());
		} finally {
                    database.closeConnection();
                }
	}

}
