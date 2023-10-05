/*
 * Created on Nov 1, 2006
 */
package com.greenfield.ui.handler.member;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.member.PortfolioDAO;
import com.greenfield.common.dao.member.WatchListDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.DBCachePool;








import java.util.Vector;

import org.apache.log4j.Logger;

import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.member.MemberHomeVO;

/**
 * @author zhangqx
 */
public class MemberHomeHandler extends BaseHandler {
	private static final Logger LOGGER = Logger.getLogger(MemberHomeHandler.class); 
	

	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		MemberHomeVO homeVO = (MemberHomeVO) obj;

		try {
			WatchListDAO watchDao = new WatchListDAO();
			watchDao.setDatabase(database);
			PortfolioDAO portfolioDao = new PortfolioDAO();
			portfolioDao.setDatabase(database);

			Vector folderList = WatchListHandler.getFolderListFromSession(watchDao, homeVO.getUserId(), sessionContext, false);
			if (folderList == null) {
				folderList = new Vector();
			}

			homeVO.setFolderList(folderList);

			Vector portfolioList = DBCachePool.getPortfolioListFromDB(
					homeVO.getUserId(), portfolioDao);
			if (portfolioList == null) {
				portfolioList = new Vector();
			}

			homeVO.setPortfolioList(portfolioList);
		} catch (Exception e) {
			LOGGER.warn("Error in querying user info: "
					+ e.getMessage());
		}

		return homeVO;
	}

}