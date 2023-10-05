/*
 * Created on Jul 30, 2006
 */
package com.greenfield.ui.util;

import java.text.DecimalFormat;
//import java.util.Date;
import java.util.Vector;

import com.greenfield.common.service.LinearModelService;
import com.greenfield.common.service.RSIService;
import com.greenfield.common.service.RangeService;
import com.greenfield.common.service.ServiceContext;
import com.greenfield.common.service.TrendService;
import com.greenfield.common.service.WarningService;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.ScanUtil;
import com.greenfield.common.util.StockUtil;
//import com.greenfield.common.base.AppContext;
//import com.greenfield.ui.cache.MarketCachePool;
//import com.greenfield.common.object.market.MarketPulse;
//import com.greenfield.ui.handler.pattern.PatternUtil;
import com.greenfield.common.constant.MarketCodes;
import com.greenfield.common.constant.TrendTypes;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.object.stock.MarketIndicators;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.object.stock.StockExt;
//import com.greenfield.ui.service.TrendService;




import com.greenfield.ui.cache.MarketCachePool;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author qin
 *
 */
public class UIStockUtil extends StockUtil {
	private static final Logger LOGGER = Logger.getLogger(UIStockUtil.class); 
	
        // Do not use static formatter here, that might have some concurrent issue.
	// private final static DecimalFormat twoDigitFormatter = new DecimalFormat("0.##");
	
	public static double getCorrelationIndex(int startIndex, Vector priceList) throws Exception {
		return getCorrelationIndex(startIndex, priceList, false);
	}
	
	public static double getCorrelationIndex(int startIndex, Vector priceList, boolean isWeekly) throws Exception {
		double coIndex = 0;
	
		Vector marketFlagList = null;
                MarketIndicators marketIndicators = MarketCachePool.getMarketIndicators();
		if (isWeekly) {
			marketFlagList = marketIndicators.getMarketWeeklyFlagList();
		} else {
			marketFlagList = marketIndicators.getMarketFlagList();
		}
		 
		if (marketFlagList == null || priceList == null ||
			marketFlagList.size() < priceList.size() - startIndex) {
			return coIndex;
		}
	
		int delta = marketFlagList.size() - priceList.size();
		int agreeCnt = 0;
		int totalCnt = priceList.size() - startIndex;
		for (int i = startIndex; i < priceList.size(); i ++) {
			StockDailyInfo daily = (StockDailyInfo) priceList.get(i);
			String marketF = (String) marketFlagList.get(delta + i);
			if (marketF.equals(daily.getTrendColor())) {
				agreeCnt ++;
			}
		
		}
	
		// System.out.println("cnts: " + agreeCnt + " / " + totalCnt);
                DecimalFormat twoDigitFormatter = new DecimalFormat("0.00");
		String coIndexStr = twoDigitFormatter.format((double) agreeCnt / totalCnt);
		return (new Double(coIndexStr)).doubleValue();
	}
	
	
	// This will used by the analysis page and graphics page.
    // weekly - three values: O/N/Y - if it is O, then use period to determine whether do weekly or daily
public static ServiceContext buildServiceContext(Stock stock, int period,
            String displayDate, String weekly, String doRSI, StockAdminDAO stockAdminDao) {
        ServiceContext svrContext = new ServiceContext();

        int EXTRA_DAYS  = 200;
        int LARGE_EXTRA = 800;

        // do not use static formatter!
        SimpleDateFormat YYYY_MM_DD_formatter = new SimpleDateFormat ("yyyy/MM/dd");

        Date lastDate = null;

        try {
            int extra = EXTRA_DAYS;
            if (period > 12) {
                    extra = LARGE_EXTRA;
            }

            // get the end date in query if defined.
            boolean needAdjust = false;
            if (displayDate != null && !displayDate.equals("")) {
                    lastDate = DateUtil.parseDateStringInYYYYMMDDFormat(displayDate);
                    needAdjust = true;
            }

            // in error or not defined, use the last day
            if (lastDate == null) {
                //System.out.println("in draw: last updated: " + stock.getLastUpdated());
                lastDate = YYYY_MM_DD_formatter.parse(stock.getLastUpdated());
            }

            // get the stock data
            String startDate = OracleUtil.getPrevDateInOracleFormat(lastDate, period, extra);
            String startDateInReal = DateUtil.getDateStringInYYYYMMDDFormat(lastDate, period);
            String endDate = OracleUtil.getDateInOracleFormat(lastDate);

            //System.out.println("stock id: " + stock.getStockId() + ", startDate: " + startDate + ", endDate: " + endDate);

            Vector priceList = stockAdminDao.getStockDailyPriceListFromDB(stock.getStockId(), startDate, endDate);
            int startIndex = ScanUtil.findStartIndex(priceList, startDateInReal);

            svrContext.setStartIndex(startIndex);
            StockExt stockExt = new StockExt();
            stockExt.setStock(stock);
            svrContext.setStockExt(stockExt);
            svrContext.setPriceList(priceList);

            if (needAdjust && priceList != null && priceList.size() > 0) {
                    // adjust the last date according the data - the displayDate may not be exactly the end date.
                    StockDailyInfo lastDay = (StockDailyInfo) priceList.get(priceList.size() - 1);
                    lastDate = YYYY_MM_DD_formatter.parse(lastDay.getMDate());
            }

            svrContext.setLastDate(lastDate);

            // weekly chart for two year if weekly = "O"; also check if weekly is Y;
            //System.out.println("Price List size: " + priceList.size());
            boolean isWeekly = false;
            if (weekly != null && weekly.equals("Y")) {
                isWeekly = true;
            } else if (weekly != null && weekly.equals("O") && period > 12) {
                isWeekly = true;
            } else {
                // weekly is null, N, or (O, period <= 12), or any other values
                // isWeekly is false
            }
            
            if (isWeekly) {
                    StockUtil.adjustPriceListForWeeklyChart(svrContext);
                    startIndex = svrContext.getStartIndex();
                    priceList = svrContext.getPriceList();
            }

            if (priceList != null && priceList.size() > 0) {
                LinearModelService linearSvr = new LinearModelService();
                TrendService trendService = new TrendService();
                RangeService rangeService = new RangeService();

                // find slope/yCross
                //linearSvr.findSlopeAndRSquare(svrContext);
                // find min/max price, and max volume
                // System.out.println("start: " + svrContext.getStartIndex() + ", end: " + svrContext.getEndIndex() + ", length: " + svrContext.getPriceList().size());
                rangeService.findMinMax(svrContext);
                rangeService.findAverage20(svrContext);   // 21-day average
                // svr.findGainLossIndex(svrContext);

                rangeService.findAverage50(svrContext, false);
                rangeService.findAverage100S(svrContext, RangeService.RANGE_100);
                rangeService.findAccumulationForceForAll(svrContext, false);
                
                // find trend indicators - using 20/50/100 and slope/acc
                rangeService.findTrendIndicators(svrContext);

                // This has to be done after 21/50, and volume
                WarningService warningService = new WarningService();
                warningService.findWarning(svrContext);

                // find William Index
                //rangeService.findWilliamIndexs(svrContext);

                // predicate dates
                // trendService.findPredicatedDays(svrContext);
                
                //svrContext.setStartIndex(threeMonthIndex);
                //TrendService trendService = new TrendService();
                //trendService.findTrendLinesForAll(svrContext);
                // rangeService.findAccumulationForceForAll(svrContext);

                rangeService.get100MADirection(svrContext);

                // find max up and down percents
                rangeService.findMaxUpChange(svrContext);
                rangeService.findMaxDownChange(svrContext);

                // after slope/acc
                //rangeService.findSignalStrength(svrContext);

                // find the correlation index
                stockExt.setCorrelationIndex(UIStockUtil.getCorrelationIndex(svrContext.getStartIndex(), svrContext.getPriceList(), isWeekly));
                //stockExt.setOscillateIndex(StockUtil.getOscillateIndex(svrContext.getStartIndex(), svrContext.getPriceList()));

                // calculate the PIP pattern points
                // PatternUtil.extractPattern( svrContext.getPriceList(), svrContext.getStartIndex(),  svrContext.getPriceList().size() - 1);

                // rangeService.findAccumulationForceForAll(svrContext);
                //rangeService.findAccumulationForceForAll2(svrContext);

                // find all slope/acceleration for any points after startIndex
                //	do the trend line service
                //trendService.findBothTrendLines(svrContext);
                //trendService.findTrendLinesForAll(svrContext);

                //svrContext.setStartIndex(startIndex - LinearModelService.ACCELERATION_RANGE);
                //linearSvr.findSlopeForAll(svrContext);
                //svrContext.setStartIndex(startIndex);
                //linearSvr.findAccelerationForAll(svrContext);

                if (doRSI != null && doRSI.equals("Y")) {
                        RSIService rsiService = new RSIService();
                        rsiService.findRSI(svrContext);
                }

                // two trend lines
                /*
                TREND_LINE_LEN = (priceList.size() - startIndex) / 2;
                svrContext.setStartIndex(priceList.size() - TREND_LINE_LEN);
                svrContext.setEndIndex(priceList.size() - TREND_LINE_END - 1);
                trendService.findBothTrendLines(svrContext);
                */


            }
        } catch (Exception e) {
            LOGGER.warn("Cannot get service context for: " + stock.getStockId() + ", with error: " + e.getMessage());
            svrContext = null;
        }


        return svrContext;


    }
	
	// slope -> MACD
	public static ServiceContext buildServiceContextX(Stock stock, int period,
        String displayDate, String weekly, String doRSI, StockAdminDAO stockAdminDao) {
    ServiceContext svrContext = new ServiceContext();

    int EXTRA_DAYS  = 200;
    int LARGE_EXTRA = 800;

    // do not use static formatter!
    SimpleDateFormat YYYY_MM_DD_formatter = new SimpleDateFormat ("yyyy/MM/dd");

    Date lastDate = null;

    try {
        int extra = EXTRA_DAYS;
        if (period > 12) {
                extra = LARGE_EXTRA;
        }

        // get the end date in query if defined.
        boolean needAdjust = false;
        if (displayDate != null && !displayDate.equals("")) {
                lastDate = DateUtil.parseDateStringInYYYYMMDDFormat(displayDate);
                needAdjust = true;
        }

        // in error or not defined, use the last day
        if (lastDate == null) {
            //System.out.println("in draw: last updated: " + stock.getLastUpdated());
            lastDate = YYYY_MM_DD_formatter.parse(stock.getLastUpdated());
        }

        // get the stock data
        String startDate = OracleUtil.getPrevDateInOracleFormat(lastDate, period, extra);
        String startDateInReal = DateUtil.getDateStringInYYYYMMDDFormat(lastDate, period);
        String endDate = OracleUtil.getDateInOracleFormat(lastDate);

        //System.out.println("stock id: " + stock.getStockId() + ", startDate: " + startDate + ", endDate: " + endDate);

        Vector priceList = stockAdminDao.getStockDailyPriceListFromDB(stock.getStockId(), startDate, endDate);
        int startIndex = ScanUtil.findStartIndex(priceList, startDateInReal);

        svrContext.setStartIndex(startIndex);
        StockExt stockExt = new StockExt();
        stockExt.setStock(stock);
        svrContext.setStockExt(stockExt);
        svrContext.setPriceList(priceList);

        if (needAdjust && priceList != null && priceList.size() > 0) {
                // adjust the last date according the data - the displayDate may not be exactly the end date.
                StockDailyInfo lastDay = (StockDailyInfo) priceList.get(priceList.size() - 1);
                lastDate = YYYY_MM_DD_formatter.parse(lastDay.getMDate());
        }

        svrContext.setLastDate(lastDate);

        // weekly chart for two year if weekly = "O"; also check if weekly is Y;
        //System.out.println("Price List size: " + priceList.size());
        boolean isWeekly = false;
        if (weekly != null && weekly.equals("Y")) {
            isWeekly = true;
        } else if (weekly != null && weekly.equals("O") && period > 12) {
            isWeekly = true;
        } else {
            // weekly is null, N, or (O, period <= 12), or any other values
            // isWeekly is false
        }
        
        if (isWeekly) {
                StockUtil.adjustPriceListForWeeklyChart(svrContext);
                startIndex = svrContext.getStartIndex();
                priceList = svrContext.getPriceList();
        }

        if (priceList != null && priceList.size() > 0) {
            LinearModelService linearSvr = new LinearModelService();
            TrendService trendService = new TrendService();
            RangeService rangeService = new RangeService();

            // find slope/yCross
            //linearSvr.findSlopeAndRSquare(svrContext);
            // find min/max price, and max volume
            // System.out.println("start: " + svrContext.getStartIndex() + ", end: " + svrContext.getEndIndex() + ", length: " + svrContext.getPriceList().size());
            rangeService.findMinMax(svrContext);
            rangeService.findAverage20Only(svrContext);   // 21-day average
            rangeService.findMACD(svrContext);            // set MACD
            // svr.findGainLossIndex(svrContext);

            rangeService.findAverage50(svrContext, false);
            rangeService.findAverage100S(svrContext, RangeService.RANGE_100);
            rangeService.findAccumulationForceForAll(svrContext, false);
            
            // find trend indicators - using 20/50/100 and slope/acc
            rangeService.findTrendIndicators(svrContext);

            // This has to be done after 21/50, and volume
            WarningService warningService = new WarningService();
            warningService.findWarning(svrContext);

            // find William Index
            //rangeService.findWilliamIndexs(svrContext);

            // predicate dates
            // trendService.findPredicatedDays(svrContext);
            
            //svrContext.setStartIndex(threeMonthIndex);
            //TrendService trendService = new TrendService();
            //trendService.findTrendLinesForAll(svrContext);
            // rangeService.findAccumulationForceForAll(svrContext);

            rangeService.get100MADirection(svrContext);

            // find max up and down percents
            rangeService.findMaxUpChange(svrContext);
            rangeService.findMaxDownChange(svrContext);

            // after slope/acc
            //rangeService.findSignalStrength(svrContext);

            // find the correlation index
            stockExt.setCorrelationIndex(UIStockUtil.getCorrelationIndex(svrContext.getStartIndex(), svrContext.getPriceList(), isWeekly));
            //stockExt.setOscillateIndex(StockUtil.getOscillateIndex(svrContext.getStartIndex(), svrContext.getPriceList()));

            // calculate the PIP pattern points
            // PatternUtil.extractPattern( svrContext.getPriceList(), svrContext.getStartIndex(),  svrContext.getPriceList().size() - 1);

            // rangeService.findAccumulationForceForAll(svrContext);
            //rangeService.findAccumulationForceForAll2(svrContext);

            // find all slope/acceleration for any points after startIndex
            //	do the trend line service
            //trendService.findBothTrendLines(svrContext);
            //trendService.findTrendLinesForAll(svrContext);

            //svrContext.setStartIndex(startIndex - LinearModelService.ACCELERATION_RANGE);
            //linearSvr.findSlopeForAll(svrContext);
            //svrContext.setStartIndex(startIndex);
            //linearSvr.findAccelerationForAll(svrContext);

            if (doRSI != null && doRSI.equals("Y")) {
                    RSIService rsiService = new RSIService();
                    rsiService.findRSI(svrContext);
            }

            // two trend lines
            /*
            TREND_LINE_LEN = (priceList.size() - startIndex) / 2;
            svrContext.setStartIndex(priceList.size() - TREND_LINE_LEN);
            svrContext.setEndIndex(priceList.size() - TREND_LINE_END - 1);
            trendService.findBothTrendLines(svrContext);
            */


        }
    } catch (Exception e) {
        LOGGER.warn("Cannot get service context for: " + stock.getStockId() + ", with error: " + e.getMessage());
        svrContext = null;
    }


    return svrContext;


}
}
