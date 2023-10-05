package com.greenfield.ui.handler.member;

import java.util.Map;
import java.util.Vector;

import com.greenfield.ui.cache.DBCachePool;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.IDGenerator;
import com.greenfield.common.object.ajax.AddwatchResponse;
import com.greenfield.common.object.member.WatchInfo;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.dao.member.WatchListDAO;
import com.greenfield.common.object.stock.Stock;

public class WatchListAjaxHandler {
	
	public static AddwatchResponse getInitData(DBComm database, String userId, Map<String, String> input) throws Exception {
		AddwatchResponse ajaxResponse = new AddwatchResponse();
		WatchListDAO watchDao = new WatchListDAO();
		StockAdminDAO stockDao = new StockAdminDAO();
		watchDao.setDatabase(database);
		stockDao.setDatabase(database);
				
		// debug
		//System.out.println("getInitData for stock: " + input.get("stockId"));
		
		try {
			String stockId = input.get("stockId");
			if (stockId == null || stockId.equals("")) {
				throw new Exception("WatchListAjaxHandler - Add watch: stock id is null.");
			}
			
			// get folder list, check if it exists in watch list or not
			Vector folderList = watchDao.getFolderListFromDB(userId);
			Vector list = watchDao.getWatchListFromDB(userId, null, stockId);
			if (list != null && list.size() > 0) {
				// prepare the upate page
				WatchInfo watchInfo = (WatchInfo) list.get(0);
				ajaxResponse.setFolderList(folderList);
				ajaxResponse.setWatchInfo(watchInfo);
				ajaxResponse.setStockId(stockId);	
				ajaxResponse.setFolderId(watchInfo.getFolderId());
				ajaxResponse.setCurrentDate(DateUtil.getDateStringInYYYYMMDDFormat(null));
				ajaxResponse.setCurrentPrice(String.valueOf(watchInfo.getPrice()));
				ajaxResponse.setNewFolderName("");
			} else {
				// blank form
				if (folderList == null || folderList.size() == 0) {
					// no folder, set default
					ajaxResponse.setNewFolderName("First Watch List");
				} else {
					ajaxResponse.setNewFolderName("");
					ajaxResponse.setFolderList(folderList);
				}
					
				// get stock info
				Stock stock = stockDao.getStockFromDB(stockId);	
				ajaxResponse.setStockId(stockId);
				ajaxResponse.setFolderId("");
				if (stock != null) {
					WatchInfo info = new WatchInfo();
					info.setStockId(stockId);
					info.setTicker(stock.getTicker());
					info.setCompanyName(stock.getCompanyName());
					ajaxResponse.setWatchInfo(info);
					ajaxResponse.setCurrentDate(DateUtil.getDateStringInYYYYMMDDFormat(null));
					ajaxResponse.setCurrentPrice(String.valueOf(stock.getPrice()));	
				} else {
					// error, stock not found
					throw new Exception("WatchListAjaxHandler - Add watch: stock id not found in DB: " + stockId);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return ajaxResponse;
	}
	
	/** form submitted, if exists, update; if not exists, insert */
	public static AddwatchResponse processAddwatch(DBComm database, String userId, Map<String, String> input) {

		AddwatchResponse ajaxResponse = new AddwatchResponse();
		WatchListDAO watchDao = new WatchListDAO();
		StockAdminDAO stockDao = new StockAdminDAO();
		watchDao.setDatabase(database);
		stockDao.setDatabase(database);
		
		// debug
		//System.out.println("processAddwatch for stock: " + input.get("stockId"));
				
		try {
			// add stock to folder, but folder may not exists
			String newFolderName = input.get("newFolderName");
			String folderId = null;
			
			if (newFolderName != null && !newFolderName.equals("")) {
				folderId = IDGenerator.getNextId(database, IDGenerator.FOLDER_ID_PREFIX);
				watchDao.addFolderToDB(userId, folderId, newFolderName);
			} else {
				folderId = input.get("folderId");
			}
				
			if (folderId == null || folderId.equals("")) {
				// error to create the folder, or the folder id is not good
				throw new Exception("Addwatch:folderId and newFolderName both null or some issue.");
			} 
			
			String stockId = input.get("stockId");
			
			// folder_id, stock_id, trade_type, open_date, open_price, close_date, close_price, comments
			WatchInfo info = new WatchInfo();
			info.setUserId(userId);
			info.setFolderId(folderId);
			info.setStockId(stockId);
			info.setTradeType(input.get("tradeType"));
			info.setOpenDate(input.get("openDate"));
			info.setCloseDate(input.get("closeDate"));
			
			try {  // should not happen, as client is checked
				info.setOpenPrice((new Double(input.get("openPrice"))).doubleValue());
				info.setClosePrice((new Double(input.get("closePrice"))).doubleValue());
			} catch (Exception ex) {
				System.out.println("Warning: " + ex.getMessage());
			}
			
			info.setComments(input.get("comments"));
			Vector list = watchDao.getWatchListFromDB(userId, null, stockId);
			if (list != null && list.size() > 0) {
				// need to update
				watchDao.updateWatchListEntryToDB(info);
			} else {
				watchDao.addWatchListEntryToDB2(info);
			}
			
			

            // reset cache
            DBCachePool.resetWatchStockIdListInCache(userId);

            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Watch list is updated successfully.");
		} catch (Exception e) {
			// warning
			System.out.println("Error: Addwatch: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}
	
	/** form submitted, remove the stock from folder */
	public static AddwatchResponse processDeleteWatch(DBComm database, String userId, Map<String, String> input) {

		AddwatchResponse ajaxResponse = new AddwatchResponse();
		WatchListDAO watchDao = new WatchListDAO();
		StockAdminDAO stockDao = new StockAdminDAO();
		watchDao.setDatabase(database);
		stockDao.setDatabase(database);
		
		// debug
		//System.out.println("processDeleteWatch for stock: " + input.get("stockId"));
				
		try {
			String stockId = input.get("stockId");
			String folderId = input.get("folderId");	
			if (stockId == null || stockId.equals("") || folderId == null || folderId.equals("")) {
				// error to detele
				throw new Exception("DeleteWatch:folderId or stockId is null or some issue.");
			} 
			
			WatchInfo info =  new WatchInfo();
			info.setFolderId(folderId);
			info.setStockId(stockId);
			watchDao.deleteWatchListEntryFromDB(info);
				
            // reset cache
            DBCachePool.resetWatchStockIdListInCache(userId);

            ajaxResponse.setSuccess(true);
            ajaxResponse.setMessage("Stock is removed from watch list successfully.");
		} catch (Exception e) {
			// warning
			System.out.println("Error: DeleteWatch: " + e.getMessage());
			ajaxResponse.setSuccess(false);
			ajaxResponse.setMessage("Error: there was general error in handle your request.  Please try later.");
		}
		
		return ajaxResponse;
	}

}
