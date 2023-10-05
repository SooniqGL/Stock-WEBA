/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.cache;

import com.greenfield.common.dao.analyze.AgeSrchDAO;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.util.StringHelper;
import com.greenfield.common.object.scan.AgeSrchInfo;
import com.greenfield.common.object.scan.ScanHistoryVO;
import com.greenfield.common.object.scan.ScanReportVO;

import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 *
 * @author qz69042
 * This is general cache, which will be shared by all users.
 *
 */
public class ScanCachePool {
	private static final Logger LOGGER = Logger.getLogger(ScanCachePool.class); 
	
    /**
     * This "reset" function should be called at the time
     * after scan is done.
     */
    public static void resetPool() {
        try {
            // simple remove the cached data from the pool
            ScanCacheStore pool = ScanCacheStore.getInstance();
            /*
            pool.modifyObject(EhcachePool.REMOVE_PREFIX_FUNC,
                    ObjectPoolKeys.SCAN_DATE_LIST_KEY, null, EhcacheNames.SCAN_CACHE);
            pool.modifyObject(
                    EhcachePool.REMOVE_PREFIX_FUNC,
                    ObjectPoolKeys.LATEST_SCAN_DATE_KEY, null, EhcacheNames.SCAN_CACHE);
            pool.modifyObject(
                    EhcachePool.REMOVE_PREFIX_FUNC,
                    ObjectPoolKeys.SCAN_REPORTDATE_LIST_KEY, null, EhcacheNames.SCAN_CACHE);
            */
            // just call
            LOGGER.warn("ScanCachePool resetPool() is called.");
            pool.reset();
        } catch (Exception e) {
            LOGGER.warn("ScanCachePool: cannot reset pool: " + e.getMessage());
            e.printStackTrace();
        }

    }

    public static String getLatestScanDate(String scanKey, ScanDAO scanDao) throws Exception {
        if (scanKey == null || scanKey.equals("")) {
            return null;
        }

        String poolKey = ObjectPoolKeys.LATEST_SCAN_DATE_KEY + scanKey;
        ScanCacheStore pool = ScanCacheStore.getInstance();
        String latestDate = null;
        
        try {
            latestDate = (String) pool.getObject(poolKey);
        } catch (Exception e) {
            // catch exception,
            LOGGER.warn("Pool for latest scan date error: " + e.getMessage());
        }

        // if it is not in pool
        if (latestDate == null) {
            latestDate = scanDao.getLatestScanDate(scanKey, null);

            // and put this to pool
            if (latestDate == null) {
                latestDate = "";
            }
            
            pool.loadObject(poolKey, latestDate);
        } else {
            // info - debug
            //LOGGER.warn("Info: Pool is used to get latest scan date with key: " + poolKey);
        }

        return latestDate;

    }

    // Entire date list for a scan key.
    public static Vector getScanDateList(String scanKey, ScanDAO scanDao) throws Exception {
        if (scanKey == null || scanKey.equals("")) {
            return null;
        }

        String poolKey = ObjectPoolKeys.SCAN_DATE_LIST_KEY + scanKey;
        ScanCacheStore pool = ScanCacheStore.getInstance();
        Vector dateList = null;

        try {
            dateList = (Vector) pool.getObject(poolKey);
        } catch (Exception e) {
            // catch exception,
            LOGGER.warn("Pool for scan date list error: " + e.getMessage());
        }

        // if it is not in pool
        if (dateList == null) {
            dateList = scanDao.getScanHistoryDateListFromDB(scanKey);

            // and put this to pool
            pool.loadObject(poolKey, dateList);
        } else {
            // info - debug
            //LOGGER.warn("Info: Pool is used to get scan date list with key: " + poolKey);
        }

        return dateList;

    }


    /**
     * Find all the scan dates for a stock to a HashMap.
     * @param scanKey
     * @param stockId
     * @param scanDao
     * @return
     * @throws Exception
     */
    public static HashMap getScanDateListForStock(String scanKey, String stockId, ScanDAO scanDao) throws Exception {

        if (stockId == null || stockId.equals("")) {
            return null;
        }

        // do not try to do this for market indexes
        if (stockId.startsWith("MKT")) {
            return new HashMap();
        }

        String poolKey = ObjectPoolKeys.SCAN_DATE_LIST_KEY + StringHelper.nvl(scanKey, "*") + "-" + stockId;
        ScanCacheStore pool = ScanCacheStore.getInstance();
        HashMap dateHash = null;

        try {
            dateHash = (HashMap) pool.getObject(poolKey);
        } catch (Exception e) {
            // catch exception,
            LOGGER.warn("Pool for scan date list for stock error: " + e.getMessage());
        }

        // if it is not in pool
        if (dateHash == null) {
            Vector dateList = scanDao.getScanHistoryDateListFromDB2(scanKey, stockId);
            dateHash = buildDateHashFromScanDateList(dateList);

            // and put this to pool
            pool.loadObject(poolKey, dateHash);
            
            // debug
            //System.out.println("Pool key: " + poolKey);
            //DumpObject.toString(dateHash);
        } else {
            // info - debug
            //LOGGER.warn("Info: Pool is used to get scan date list with key: " + poolKey);
        }

        return dateHash;

    }

    /**
     * @param scanKey
     * @param pairIndex
     * @param scanDao
     * @return
     * @throws Exception
     */
    public static Vector getScanReportDateList(String scanKey, ScanDAO scanDao) throws Exception {
        if (scanKey == null || scanKey.equals("")) {
            return null;
        }

        String poolKey = ObjectPoolKeys.SCAN_REPORTDATE_LIST_KEY + scanKey;
        ScanCacheStore pool = ScanCacheStore.getInstance();
        Vector dateList = null;

        try {
            dateList = (Vector) pool.getObject(poolKey);
        } catch (Exception e) {
            // catch exception,
            LOGGER.warn("Pool for scan report date list error: " + e.getMessage());
        }

        // if it is not in pool
        if (dateList == null) {
            Vector scanReportList = scanDao.getScanReportDateListFromDB(scanKey);
            dateList = retrieveDateList(scanReportList);

            // and put this to pool
            pool.loadObject(poolKey, dateList);
        } else {
            // info - debug
            //LOGGER.warn("Info: Pool is used to get scan report date list with key: " + poolKey);
        }
  
        return dateList;

    }


    public static Vector getStockAgeSummaryList(AgeSrchDAO ageDao) throws Exception {

        String poolKey = ObjectPoolKeys.STOCKAGE_SUMMARY_LIST_KEY + "A";
        ScanCacheStore pool = ScanCacheStore.getInstance();;
        Vector sumList = null;

        try {
            sumList = (Vector) pool.getObject(poolKey);
        } catch (Exception e) {
            // catch exception,
            LOGGER.warn("Pool for stock age summary list error: " + e.getMessage());
        }

        // if it is not in pool
        if (sumList == null) {
            sumList = PoolUtil.buildStockAgeSummaryList(ageDao);
            if (sumList == null) {
                sumList = new Vector();
            } 

            // and put this to pool
            pool.loadObject(poolKey, sumList);
        } else {
            // info - debug
            //LOGGER.warn("Info: Pool is used to get stock age summary list list with key: " + poolKey);
        }

        return sumList;

    }
    
    public static Vector getStockAgeBestList(AgeSrchDAO ageDao) throws Exception {

        String poolKey = ObjectPoolKeys.STOCKAGE_BEST_LIST_KEY + "A";
        ScanCacheStore pool = ScanCacheStore.getInstance();;
        Vector bestList = null;

        try {
        	bestList = (Vector) pool.getObject(poolKey);
        } catch (Exception e) {
            // catch exception,
            LOGGER.warn("Pool for stock age best list error: " + e.getMessage());
        }

        // if it is not in pool
        if (bestList == null) {
        	bestList = PoolUtil.buildStockAgeBestList(ageDao);
            if (bestList == null) {
            	bestList = new Vector();
            } 

            // and put this to pool
            pool.loadObject(poolKey, bestList);
        } else {
            // info - debug
            //LOGGER.warn("Info: Pool is used to get stock age best list list with key: " + poolKey);
        }

        return bestList;

    }

    /**
     * Only retrieve the Date to display in pairs
     * for each screen size.
     * @param reportList
     * @return
     */
    @SuppressWarnings({ "unchecked", "rawtypes" })
	private static Vector retrieveDateList(Vector reportList) {
        int SCREEN_ITEM_SIZE = 100;
        Vector pairList = new Vector();
        if (reportList != null) {
            boolean hasTail = false;
            int listSize = reportList.size();
            for (int i = 0; i < listSize; i += SCREEN_ITEM_SIZE) {
                String scanDate = ((ScanReportVO) reportList.get(i)).getScanDate();
                pairList.add(new String(scanDate));
                if (i != (listSize - 1)) {
                    hasTail = true;
                }
            }

            if (hasTail) {
                // add the very last one to the list
                String lastScanDate = ((ScanReportVO) reportList.get(listSize - 1)).getScanDate();
                pairList.add(new String(lastScanDate));
            }
        }
        
        return pairList;
    }

    /**
     * Key format: YYYY/MM/DD.
     * value format: {scan_key} + {:}
     * @param dateList
     * @return
     */
    private static HashMap buildDateHashFromScanDateList(Vector dateList) {
        HashMap hash = new HashMap();

        if (dateList != null && dateList.size() > 0) {
            ScanHistoryVO vo = null;
            String scanKey = null;
            String scanDate = null;

            for (int i = 0; i < dateList.size(); i ++) {
                vo = (ScanHistoryVO) dateList.get(i);
                scanKey = vo.getScanKey();
                scanDate = vo.getScanDate();
                if (hash.containsKey(scanDate)) {
                    String val = (String) hash.get(scanDate) + ":" + scanKey;
                    hash.put(scanDate, val);      // format of the key is: YYYY/MM/DD
                } else {
                    hash.put(scanDate, scanKey);  // format of the key is: YYYY/MM/DD
                }
            }
        }

        return hash;
    }

}
