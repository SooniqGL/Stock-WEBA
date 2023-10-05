/*
 * Created on Jun 20, 2006
 */
package com.greenfield.ui.handler.member;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.SessionKeys;
import com.greenfield.common.constant.TradeTypes;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.dao.member.WatchListDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.DBCachePool;

import java.util.Vector;

import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.member.FolderInfo;
import com.greenfield.common.object.member.WatchInfo;
import com.greenfield.common.object.member.WatchListVO;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.user.User;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.IDGenerator;
import com.greenfield.common.util.NumberUtil;
import com.greenfield.common.util.StringHelper;

/**
 * @author qin
 */
public class WatchListHandler extends BaseHandler {
	private final static String DEFAULT_FOLDER_NAME = "My First Watch List";
	
	private WatchListDAO watchDao = null;
	private StockAdminDAO stockDao = null;
				
	/**
	 * Make sure this person is operating his/her own folder.
	 */
	protected boolean preSecurityCheck(User user, BaseObject obj, WebSessionContext sessionContext) {
		WatchListVO watchVO = (WatchListVO) obj;
		String folderId = watchVO.getFolderId();
		boolean pass = false;
		if (folderId != null && !folderId.equals("")) {
			try {
				WatchListDAO watchDao = new WatchListDAO();
				watchDao.setDatabase(database);
				Vector folderList = watchDao.getFolderListFromDB(user.getUserId());
				if (folderList != null) {
					for (int i = 0; i < folderList.size(); i ++) {
						FolderInfo folderInfo = (FolderInfo) folderList.get(i);
						if (folderId.equals(folderInfo.getFolderId())) {
							pass = true;
							break;
						}
					}
				} 
			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			pass = true;
		}
		
		if (pass == false) {
			System.out.println("Warning: User " + user.getUserId() + " is trying to operate folder: " + folderId);
		}
		
		return pass;
	}
		
	/* (non-Javadoc)
	 * @see com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object.BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		WatchListVO watchVO = (WatchListVO) obj;
		String mode = watchVO.getMode();
		String type = watchVO.getType();
		watchVO.setSuccess(true);
			
		try {
			watchDao = new WatchListDAO();
			stockDao = new StockAdminDAO();
			watchDao.setDatabase(database);
			stockDao.setDatabase(database);
			
			// set it from session for all cases
			Vector folderList = getFolderListFromSession(watchDao, watchVO.getUserId(), sessionContext, false);
			watchVO.setFolderList(folderList);
				
			if (mode != null && mode.equals("newfolder")) {
				if (type != null && !type.equals("blank")) {
					String folderId = IDGenerator.getNextId(database, IDGenerator.FOLDER_ID_PREFIX);
					watchDao.addFolderToDB(watchVO.getUserId(), folderId, watchVO.getFolderName());
					
					// this is the place we need to reset the fold list!
					folderList = getFolderListFromSession(watchDao, watchVO.getUserId(), sessionContext, true);
					watchVO.setFolderList(folderList);
				} 
			} else if (mode != null && mode.equals("updatefolder")) {				
				if (type != null && type.equals("rename")) {
					watchDao.updateFolderToDB(watchVO.getFolderId(), watchVO.getFolderName());
					// this is the place we need to reset the fold list!
					folderList = getFolderListFromSession(watchDao, watchVO.getUserId(), sessionContext, true);
					watchVO.setFolderList(folderList);
					
				} else if (type != null && type.equals("delete")) {
					watchDao.deleteFolderFromDB(watchVO.getFolderId());
					watchVO.setFolderId("");
					watchVO.setFolderName("");
					
					// this is the place we need to reset the fold list!
					folderList = getFolderListFromSession(watchDao, watchVO.getUserId(), sessionContext, true);
					watchVO.setFolderList(folderList);
				} else if (type != null && type.equals("move")) {
					boolean done = watchDao.moveWatchListEntryInDB(watchVO.getUserId(), watchVO.getWatchInfo(), watchVO.getNewFolderId());
					if (done) {
						watchVO.setFolderId(watchVO.getNewFolderId());
						loadWatchListForFolder(watchVO);
						watchVO.setFolderName(findFolderName(watchVO.getNewFolderId(), watchVO.getFolderList()));
						
					} else {
						watchVO.setSuccess(false);
					}
				} else if (type != null && type.equals("copy")) {
					boolean done = watchDao.copyWatchListEntryInDB(watchVO.getUserId(), watchVO.getWatchInfo(), watchVO.getNewFolderId());
					if (done) {
						watchVO.setFolderId(watchVO.getNewFolderId());
						loadWatchListForFolder(watchVO);
						watchVO.setFolderName(findFolderName(watchVO.getNewFolderId(), watchVO.getFolderList()));
						
					} else {
						watchVO.setSuccess(false);
					}
				} else if (type != null && type.equals("addlist")) {
					// list of ticker/comments are passed in; folder_id is given in this case!!
					// System.out.println("ticker list: " + watchVO.getTickerListStr());
					StringBuffer msg = new StringBuffer();
					boolean hasError = addListToDB(watchVO, msg);
					
					if (hasError == true) {
						watchVO.setSuccess(false);
						watchVO.setMessage(msg.toString());
					}
					
					loadWatchListForFolder(watchVO);
				
				} 
				
				if (type == null || (!type.equals("rename") && !type.equals("delete"))) {
					watchVO.setFolderName(findFolderName(watchVO.getFolderId(), watchVO.getFolderList()));
				}
			
			} else if (mode != null && mode.equals("addwatch")) {
				if (type != null && type.equals("blank")) {
					//// check if it exists in watch list or not
					Vector list = watchDao.getWatchListFromDB(watchVO.getUserId(), null, watchVO.getStockId());
					if (list != null && list.size() > 0) {
						watchVO.setAlreadyInWatch(true);
						
						// prepare the upate page
						WatchInfo watchInfo = (WatchInfo) list.get(0);
						watchVO.setWatchInfo(watchInfo);
						watchVO.setFolderId(watchInfo.getFolderId());
						
						watchVO.setFolderName(findFolderName(watchVO.getFolderId(), folderList));

						// set date and price
						watchVO.setCurrentDate(DateUtil.getDateStringInYYYYMMDDFormat(null));
						watchVO.setCurrentPrice(String.valueOf(watchVO.getWatchInfo().getPrice()));
					} else {
						watchVO.setAlreadyInWatch(false);
						
						if (folderList == null || folderList.size() == 0) {
							// no folder, set default
							watchVO.setNewFolderName(DEFAULT_FOLDER_NAME);
						} else {
							watchVO.setNewFolderName("");
						}
						
						// get stock info
						String stockId = watchVO.getStockId();
						Stock stock = stockDao.getStockFromDB(stockId);		
						if (stock != null) {
							WatchInfo info = new WatchInfo();
							info.setStockId(stockId);
							info.setTicker(stock.getTicker());
							info.setCompanyName(stock.getCompanyName());
							watchVO.setWatchInfo(info);
							watchVO.setCurrentDate(DateUtil.getDateStringInYYYYMMDDFormat(null));
							watchVO.setCurrentPrice(String.valueOf(stock.getPrice()));	
						}
					}
				
					// submit by the client, with details
				} else if (type != null && type.equals("addone")){
					watchVO.setAlreadyInWatch(false);
					
					// add stock to folder, but folder may not exists
					String newFolderName = watchVO.getNewFolderName();
					String folderId = "";
					if (newFolderName != null && !newFolderName.equals("")) {
						// create this folder
						folderId = IDGenerator.getNextId(database, IDGenerator.FOLDER_ID_PREFIX);
						watchDao.addFolderToDB(watchVO.getUserId(), folderId, newFolderName);
						watchVO.setFolderId(folderId);
						
						// reload the folder list
						folderList = getFolderListFromSession(watchDao, watchVO.getUserId(), sessionContext, true);
						watchVO.setFolderList(folderList);
					} else {
						folderId = watchVO.getFolderId();
					}
					
					if (folderId != null && !folderId.equals("")) {
						WatchInfo info = watchVO.getWatchInfo();
						info.setUserId(watchVO.getUserId());
						info.setFolderId(folderId);
						watchDao.addWatchListEntryToDB2(info);

                        // reset cache
                        DBCachePool.resetWatchStockIdListInCache(watchVO.getUserId());

						// load up
                        loadWatchListForFolder(watchVO);
						watchVO.setFolderName(findFolderName(folderId, watchVO.getFolderList()));
						
						watchVO.setSuccess(true);
						watchVO.setMessage("Watch list is updated successfully.");
					} else {
						// warning
						System.out.println("Error: Addwatch:addone: folder id is null");
						watchVO.setSuccess(false);
						watchVO.setMessage("Error: there was general error in handle your request.  Please try later.");
					}
				} 
									
			} else if (mode != null && mode.equals("updatewatch")) {
				if (type != null && type.equals("update")) {					
					WatchInfo info =  watchVO.getWatchInfo();
					info.setUserId(watchVO.getUserId());
					watchDao.updateWatchListEntryToDB(info);
					watchVO.setSuccess(true);
					watchVO.setMessage("This watch list entry has been updated successfully.");
				} 
				
				// in other case, need to load the info
				Vector subWatchList = watchDao.getWatchListFromDB(
					watchVO.getUserId(), 
					watchVO.getFolderId(), 
					watchVO.getStockId());
				if (subWatchList != null && subWatchList.size() > 0) {
					WatchInfo watchInfo = (WatchInfo) subWatchList.get(0);
					watchVO.setWatchInfo(watchInfo);
				} else {
					// error
					watchVO.setWatchInfo(new WatchInfo());
				}
				
				watchVO.setFolderName(findFolderName(watchVO.getFolderId(), folderList));
				
				// set date and price
				watchVO.setCurrentDate(DateUtil.getDateStringInYYYYMMDDFormat(null));
				watchVO.setCurrentPrice(String.valueOf(watchVO.getWatchInfo().getPrice()));
				
				// reload the entire case
				if (type == null || !type.equals("blank")) {
					loadWatchListForFolder(watchVO);
				}
				
			} else if (mode != null && mode.equals("viewwatchlist")) {
				String folderId = watchVO.getFolderId();
				if (folderId != null && !folderId.equals("")) {
					loadWatchListForFolder(watchVO);
					watchVO.setFolderName(findFolderName(folderId, watchVO.getFolderList()));
				}
			} else if (mode != null && mode.equals("deletewatch")) {
				String folderId = watchVO.getFolderId();
				
				//System.out.println("deletewatch: " + folderId + ", " + watchVO.getStockId());
				if (folderId != null && !folderId.equals("")) {
					WatchInfo info =  new WatchInfo();
					info.setFolderId(watchVO.getFolderId());
					info.setStockId(watchVO.getWatchInfo().getStockId());
					watchDao.deleteWatchListEntryFromDB(info);
					watchVO.setSuccess(true);
					watchVO.setMessage("This watch list entry has been deleted successfully.");

                    // reset cache
                    DBCachePool.resetWatchStockIdListInCache(watchVO.getUserId());

					// reload the list
					loadWatchListForFolder(watchVO);
				}
			} else {
				watchVO.setSuccess(false);
				watchVO.setMessage("This mode is not handled: " + mode);
			}
			
			
		} catch (Exception e) {
			e.printStackTrace();
			//analVO.setDisplay("error");
			watchVO.setSuccess(false);
			watchVO.setMessage("Error: in program: " + e.getMessage());
		}
			
		return watchVO;
	}
	
	/**
	 * Folder id is given,
	 * find the list, always load the fold list too
	 */
	private void loadWatchListForFolder(WatchListVO watchVO) throws Exception {
		Vector watchList = watchDao.getWatchListFromDB(watchVO.getUserId(), watchVO.getFolderId(), null);
		calculateGain(watchList);
		watchVO.setWatchList(watchList);
	}
	
	public static Vector getFolderListFromSession(WatchListDAO watchDao, String userId, WebSessionContext sessionContext, boolean reload) throws Exception {
		Vector folderList = null;
		if (reload == false) {
			folderList = (Vector) sessionContext.getObject(SessionKeys.FOLDER_LIST);
			if (folderList != null) {
				return folderList;
			}
		}
		
		// need to reload and set session
		folderList = watchDao.getFolderListFromDB(userId);
		if (folderList != null) {
			sessionContext.setObject(SessionKeys.FOLDER_LIST, folderList);
		}
		
		return folderList;
		
	}
	
	private String findFolderName(String folderId, Vector folderList) throws Exception {
		//Vector folderList = watchDao.getFolderListFromDB(userId, folderId);
		String folderName = "";
		if (folderList != null && folderList.size() > 0) {
			// found
			for (int i = 0; i < folderList.size(); i++) {
				FolderInfo folder = (FolderInfo) folderList.get(i);
				if (folder.getFolderId().equals(folderId)) {
					folderName = folder.getFolderName();
					break;
				}
			}
		} 
	
		return folderName;
	}
			
	/**
	 * A list of stock are given, folder_id is given,
	 * then add to the watch list.
	 * @param watchVO
	 * @param stockDao
	 * @param watchDao
	 * @throws Exception
	 */
	private boolean addListToDB(WatchListVO watchVO, StringBuffer msg) throws Exception {
		String folderId = watchVO.getFolderId();
		if (folderId == null || folderId.equals("")) {
			msg.append("\nFolder ID is null.");
			return false;
		}
		
		Vector list = convertTickerListStrToVector(watchVO.getTickerListStr());
		boolean hasError = false;
		if (list != null && list.size() > 0) {
			for (int i = 0; i < list.size(); i ++) {
				WatchInfo info = (WatchInfo) list.get(i);
				String ticker = info.getTicker();
		
				if (ticker == null || ticker.equals("")) {
					// error
					msg.append("\nSome ticker is null.");
					hasError = true;
					continue;
				}
		
				ticker = ticker.toUpperCase();
				Vector stockList = stockDao.getStockListFromDB(null, ticker, null);
				if (stockList == null || stockList.size() == 0) {
					// ticker is not in our DB
					msg.append("\nTicker " + ticker +" is not found in our DB.");
					hasError = true;
				} else {
					// add all to the watch list
					try {
						for (int j = 0; j < stockList.size(); j ++) {
							String stockId = ((Stock) stockList.get(j)).getStockId();
							
							// check if stock is in folder
							Vector list2 = watchDao.getWatchListFromDB(watchVO.getUserId(), watchVO.getFolderId(), stockId);
							if (list2 != null && list2.size() > 0) {
								// ignore the stock
							} else {
								info.setStockId(stockId);
								info.setUserId(watchVO.getUserId());
								info.setFolderId(watchVO.getFolderId());
								watchDao.addWatchListEntryToDB(info);
							}
						}
					} catch (Exception e) {
						// let the others go on
						e.printStackTrace();
						hasError = true;
						msg.append("\nThere is a DB error.  Maybe this ticker " + ticker + " is alreay in your watch List.");
					}
				}
			}
			
			// reset cache
            DBCachePool.resetWatchStockIdListInCache(watchVO.getUserId());
		} else {
			// no ticker found
			hasError = true;
			msg.append("Error: Nothing to be added to DB or format error.");
		}
		
		return hasError;
	}
	
	/**
	 * If add new, format is: {ticker}:{comments}#{ticker}:{comments}
	 * If udpate, format is: {stockId}:{comments}#{stockId}:{comments}
	 * # in comments are replaced in client site to ##.
	 * @param tickerListStr
	 * @return
	 */
	private Vector convertTickerListStrToVector(String tickerListStr) {
		Vector list = new Vector();
		if (tickerListStr == null || tickerListStr.equals("")) {
			return list;
		}
		
		Vector itemList = StringHelper.mySplit(tickerListStr, "#");
		if (itemList != null && itemList.size() > 0) {
			for (int i = 0; i < itemList.size(); i ++) {
				String item = (String) itemList.get(i);
				Vector entries = StringHelper.mySplit(item, ":");
				if (entries != null && entries.size() >= 1) {
					WatchInfo info =  new WatchInfo();
					info.setTicker( ((String) entries.get(0)).trim());
					if (entries.size() > 1) {
						info.setComments(StringHelper.decodeString((String) entries.get(1)));
					} else {
						info.setComments("");
					}
					list.add(info);
				} else {
					// error
					System.out.println("Error: add watch list, string error: " + tickerListStr);
				}
			}
		}

		return list;
	}
	
	/**
	 * Go through the list to figure out the gain/loss
	 * for the paper trade.
	 * @param watchList
	 */
	private void calculateGain(Vector watchList) {
		if (watchList == null) {
			return;
		}
		
		for (int i = 0; i < watchList.size(); i ++) {
			WatchInfo watchInfo = (WatchInfo) watchList.get(i);
			if (watchInfo.getOpenDate() != null && !watchInfo.getOpenDate().equals("") &&
				watchInfo.getOpenPrice() > 0) {
				double percent = 0;
				if (watchInfo.getCloseDate() != null && !watchInfo.getCloseDate().equals("")) {
					// this is closed
					percent = (double) (watchInfo.getClosePrice() - watchInfo.getOpenPrice()) / watchInfo.getOpenPrice();
				} else {
					// use current date
					percent = (double) (watchInfo.getPrice() - watchInfo.getOpenPrice()) / watchInfo.getOpenPrice();
				}
				
				if (watchInfo.getTradeType() != null && !watchInfo.getTradeType().equals(TradeTypes.LONG)) {
					percent = - percent;
				}
				
				percent *= 100;
				percent = NumberUtil.formatDoubleOne(percent);
				watchInfo.setGainPercent(percent);
			}
		}
	}
}
