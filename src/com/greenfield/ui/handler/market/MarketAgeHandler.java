/*
 * Created on Jan 6, 2007
 */
package com.greenfield.ui.handler.market;

import java.util.Vector;

import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.constant.MarketTypes;
import com.greenfield.common.dao.analyze.MarketPulseDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.object.market.MarketPulse;
import com.greenfield.common.object.market.MarketPulseVO;

/**
 * @author qin
 *
 * Prepare data for market age display
 */
public class MarketAgeHandler extends BaseHandler {

	/* (non-Javadoc)
	 * @see com.greenfield.ui.handler.BaseHandler#doAction(com.greenfield.common.object.BaseObject)
	 */
	protected BaseObject doAction(BaseObject obj, WebSessionContext sessionContext) throws Exception {
		MarketPulseVO marketVO = (MarketPulseVO) obj;
		
		try {
			// query for the data
			MarketPulseDAO dao = new MarketPulseDAO();
			dao.setDatabase(database);
			int period = marketVO.getPeriod();
			if (period == 0) {
				period = 6;
			}
			
			String stDate = OracleUtil.getPrevDateInOracleFormat(null, period);
			String marketType = marketVO.getMarketType();
			if (marketType == null || marketType.equals("")) {
				// in case - but not cruash
				marketType = MarketTypes.NEWYORK;	
			}
			Vector marketList = dao.getMarketPulseListFromDB(marketType, stDate, null, true);
			
			StringBuffer ageListBuf = new StringBuffer();
			if (marketList != null) {
				ageListBuf.append(MarketTypes.getMarketName(marketType));
				for (int i = 0; i < marketList.size(); i ++) {
					MarketPulse marketPulse = (MarketPulse) marketList.get(i);
					ageListBuf.append("#" + marketPulse.getMDate() + "|" + marketPulse.getAgeList());
				}
			}
			
			marketVO.setAgeList(ageListBuf.toString());
			
			// System.out.println("size: " + ageListBuf.length() + ", content: " + ageListBuf.toString().substring(0, 500));
		} catch (Exception e) {
			e.printStackTrace();	
		}
		
		return marketVO;
	}
	
}
