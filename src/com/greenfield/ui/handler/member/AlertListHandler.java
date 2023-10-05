/*
 * Created on Oct 28, 2006
 */
package com.greenfield.ui.handler.member;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.member.UserStoreDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.member.UserStore;
import com.greenfield.common.object.member.WatchListVO;

/**
 * @author qin
 */
public class AlertListHandler extends BaseHandler {

	/* (non-Javadoc)
	 * @see com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object.BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		WatchListVO watchVO = (WatchListVO) obj;
		String userId = watchVO.getUserId();
		String tickerListStr = "";
		
		try {
			UserStoreDAO storeDao = new UserStoreDAO();
			storeDao.setDatabase(database);
			UserStore userStore = storeDao.getUserStoreFromDB(userId);
			
			if (userStore != null) {
				tickerListStr = userStore.getAlertList();
			}
			
			watchVO.setTickerListStr(tickerListStr);	
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		return watchVO;
	}
	
	/**
	 * If add new, format is: {ticker}:{comments}#{ticker}:{comments}
	 * If udpate, format is: {stockId}:{comments}#{stockId}:{comments}
	 * # in comments are replaced in client site to ##.
	 * @param tickerListStr
	 * @return
	 */
	/*
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
				if (entries != null && entries.size() == 2) {
					WatchInfo info =  new WatchInfo();
					info.setTicker( (String) entries.get(0));
					info.setComments(StringHelper.decodeString((String) entries.get(1)));
					list.add(info);
				} else {
					// error
					System.out.println("Error: add watch list, string error: " + tickerListStr);
				}
			}
		}

		return list;
	} */
}
