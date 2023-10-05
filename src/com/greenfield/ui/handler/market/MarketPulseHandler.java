/*
 * Created on Nov 21, 2006
 */
package com.greenfield.ui.handler.market;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.MarketTypes;
import com.greenfield.common.dao.analyze.MarketPulseDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.market.MarketPulseVO;

import java.util.Vector;

/**
 * @author zhangqx
 *
 */
public class MarketPulseHandler extends BaseHandler {

	/* (non-Javadoc)
	 * @see com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object.BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		MarketPulseVO marketVO = (MarketPulseVO) obj;
		
		try {
                    // load the market pulse date list from pool
                    MarketPulseDAO marketDao = new MarketPulseDAO();
                    marketDao.setDatabase(database);

                    String marketType = marketVO.getMarketType();
                    if (marketType == null || marketType.equals("")) {
                        // do some default market type if not set
                        marketType = MarketTypes.NEWYORK;
                        marketVO.setMarketType(marketType);
                    }

                    // get the market pulse list from pool or DB
                    try {
                        Vector pulseDateList = MarketCachePool.getMarketPulseDateList(marketType, marketDao);
                        if (pulseDateList != null && pulseDateList.size() > 0) {
                            marketVO.setMarketPulseDateList(pulseDateList);
                        } else {
                            marketVO.setMarketPulseDateList(new Vector());
                        }
                    } catch (Exception e) {
                        // ignore
                        marketVO.setMarketPulseDateList(new Vector());
                    }
			
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		return marketVO;
	}
	
}
