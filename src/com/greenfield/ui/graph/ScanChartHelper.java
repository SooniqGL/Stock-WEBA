package com.greenfield.ui.graph;

import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.object.scan.ScanChartVO;
import com.greenfield.common.object.scan.ScanHistoryVO;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.OracleUtil;

import java.util.Vector;

public class ScanChartHelper {
	
	public final static int BEFORE_MONTHS = 15;
	public final static int AFTER_MONTHS = 7;
	public final static int BEFORE_DAYS = 256;   // roughly one year including the scan date
	public final static int AFTER_DAYS = 132;      // roughly six month
	
	public final static double PRICE_TOP_CUT 		= 1.5; 
	public final static double PRICE_BOTTOM_CUT 	= -0.5; 
	
	@SuppressWarnings("rawtypes")
	public static Vector<ScanChartVO> loadScanHistoryList(ScanDAO scanDao, StockAdminDAO stockAdminDao,
			String scanKey, String scanStDate, String scanEdDate) throws Exception {
		Vector<ScanChartVO> scanChartList = new Vector<ScanChartVO>();
		
		try {
			if (scanKey == null || scanKey.equals("") || scanStDate == null || scanStDate.equals("")
					|| scanEdDate == null || scanEdDate.equals("")) {
				throw new Exception("scan key, scan start/end dates is null");
			}
			
	        // convert to oracle format from YYYY/MM/DD TO DD-MON-YY
	        String scanStDateInOracle = OracleUtil.convertYYYYMMDDToOracle2(scanStDate);
	        String scanEdDateInOracle = OracleUtil.convertYYYYMMDDToOracle2(scanEdDate);
	        
	        // find scan history entries, that need to be worked on
	        Vector scanHistoryList = scanDao.getScanHistoryFromDB(scanKey, null, null, null, scanStDateInOracle, scanEdDateInOracle);
	        if (scanHistoryList != null && scanHistoryList.size() > 0) {
	        	for (int i = 0; i < scanHistoryList.size(); i ++) {
	        		ScanHistoryVO vo = (ScanHistoryVO) scanHistoryList.get(i);
	        		String scanDt = vo.getScanDate();
	        		String stockId = vo.getStockId();
	        		String ticker = vo.getTicker();
	        		if (scanDt != null && !scanDt.equals("") && stockId != null && !stockId.equals("")) {
	        			ScanChartVO scanChartVo = new ScanChartVO();
	        			scanChartVo.setScanDate(scanDt);
	        			scanChartVo.setScanKey(scanKey);
	        			scanChartVo.setTicker(ticker);
	        			scanChartVo.setStockId(stockId);
	        			
	        			String startPDt = DateUtil.getDateStringInYYYYMMDDFormat(scanDt, BEFORE_MONTHS);   // 15 month before
	        			String endPDt = DateUtil.getDateStringInYYYYMMDDFormat(scanDt, - AFTER_MONTHS);   // 7 month after
	        			
	        			Vector priceList = stockAdminDao.getStockDailyPriceListFromDB(stockId, startPDt, endPDt);
	        			scanChartVo.setPriceList(preparePriceList(ticker, scanDt, priceList));
	        			
	        			scanChartList.add(scanChartVo);
	        		} else {
	        			System.out.println("scan date or stock id is null");
	        		}
	        	}
	        }
		} catch (Exception e) {
			System.out.println("loadScanHistoryList error: " + e.getMessage());
			throw e;
		}
		
		return scanChartList;
	}

	/**
	 * find the scan index, take "BEFORE_DAYS" on left, "END_DATS" on right.
	 * Use the max/min in [BEFORE_DATES, INDEX] to scale all the data
	 * Right hands of data is optional.
	 * @throws Exception 
	 * 
	 */
	@SuppressWarnings("rawtypes")
	private static Vector<StockDailyInfo> preparePriceList(String ticker, String scanDt, Vector priceList) throws Exception {
		Vector<StockDailyInfo> adjList = new Vector<StockDailyInfo>();
		
		try {
			if (priceList == null || priceList.size() < BEFORE_DAYS) {
				return null;
			}
			
			int scanIndex = findScanIndex(scanDt, priceList);
			if (scanIndex < 0) {
				throw new Exception("Scan dt error: " + scanDt);
			}
			
			int startPIndex = scanIndex - BEFORE_DAYS + 1;
			int endPIndex = scanIndex + AFTER_DAYS;
			
			if (startPIndex < 0) {
				System.out.println("Data is not enough: " + ticker);
				return null;
			}
			
			double maxP = findMaxPrice(startPIndex, scanIndex, priceList);
			double minP = findMinPrice(startPIndex, scanIndex, priceList);
			if (maxP <= minP) {
				System.out.println("Max = min, not handled: " + ticker);
				return null;    // error
			}
			
			for (int i = startPIndex; i < priceList.size() && i <= endPIndex; i ++) {
				StockDailyInfo info = (StockDailyInfo) priceList.get(i);
				double adj = (double) (info.getClose() - minP) / (maxP - minP);
				if (adj >= PRICE_BOTTOM_CUT && adj <= PRICE_TOP_CUT) {   // ignore the out of range points
					info.setAdjustedClose(adj);
					adjList.add(info);
				}
			}
		} catch (Exception e) {
			System.out.println("preparePriceList error: " + e.getMessage());
			throw e;
		}
		return adjList;
	}

	@SuppressWarnings("rawtypes")
	private static double findMinPrice(int startPIndex, int scanIndex, Vector priceList) {
		double minP = 0;
		for (int i = startPIndex; i < priceList.size() && i <= scanIndex; i ++) {
			StockDailyInfo info = (StockDailyInfo) priceList.get(i);
			double price = info.getClose();
			
			if (i == startPIndex) {
				minP = price;
			} else {
				if (price < minP) {
					minP = price;
				}
			}
		}
		return minP;
	}

	@SuppressWarnings("rawtypes")
	private static double findMaxPrice(int startPIndex, int scanIndex, Vector priceList) {
		double maxP = 0;
		for (int i = startPIndex; i < priceList.size() && i <= scanIndex; i ++) {
			StockDailyInfo info = (StockDailyInfo) priceList.get(i);
			double price = info.getClose();
			
			if (i == startPIndex) {
				maxP = price;
			} else {
				if (price > maxP) {
					maxP = price;
				}
			}
		}
		return maxP;
	}

	@SuppressWarnings("rawtypes")
	private static int findScanIndex(String scanDt, Vector priceList) {
		int scanIndex = -1;
		for (int i = 0; i < priceList.size(); i ++) {
			StockDailyInfo info = (StockDailyInfo) priceList.get(i);
			if (scanDt.equals(info.getMDate())) {
				scanIndex = i;
				break;
			}
		}
		return scanIndex;
	}
}
