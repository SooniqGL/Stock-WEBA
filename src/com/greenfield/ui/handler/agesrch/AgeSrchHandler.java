/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.handler.agesrch;

import com.greenfield.ui.action.scan.AgeSrchAction;
import com.greenfield.common.base.WebSessionContext;
import com.greenfield.common.dao.analyze.AgeSrchDAO;
import com.greenfield.common.handler.BaseHandler;
import com.greenfield.ui.cache.ScanCachePool;
import com.greenfield.common.object.BaseObject;
import com.greenfield.common.object.scan.AgeSrchVO;
import com.greenfield.common.object.scan.StockAge;

import java.text.DecimalFormat;
import java.util.Vector;

/**
 * 
 * @author QZ69042
 */
public class AgeSrchHandler extends BaseHandler {
	// DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
	// private static String prevMonthDayInOracle =
	// OracleUtil.getPrevDateInOracleFormat(null, 1, 0);
	// private static int MAX_RETURNS_FOR_PERFORM = 30;

	public BaseObject doAction(BaseObject inputVO, WebSessionContext sessionContext) {
		AgeSrchVO vo = (AgeSrchVO) inputVO;
		String mode = vo.getMode();

		try {
			AgeSrchDAO ageDao = new AgeSrchDAO();
			ageDao.setDatabase(database);

			// System.out.println("handler is called ... " + mode);
			if (mode != null && mode.equals(AgeSrchAction.BASIC)) {

				// loading
				// System.out.println("loading ...");
				vo.setAgeSummaryList(loadAgeSummaryList(ageDao));
			} else if (mode != null && mode.equals(AgeSrchAction.SEARCH)) {
				vo.setAgeSummaryList(loadAgeSummaryList(ageDao));

				if (vo.getSelectRange() != null
						&& !vo.getSelectRange().equals("")) {
					Vector list = getStockAgeList(null, vo.getSelectRange(), ageDao);
					vo.setStockAgeList(list);
				} else {
					// not in search mode
					vo.setStockAgeList(new Vector());
				}
			}
		} catch (Exception e) {
			// do nothing
			e.printStackTrace();
		}

		return inputVO;
	}
	
	public Vector getStockAgeList(String ageType, String selectRange, AgeSrchDAO ageDao) {
		Vector list = null;
		
		try {
			if (selectRange.equals("BEST")) {
				list = ScanCachePool.getStockAgeBestList(ageDao);
			} else {
				// load the stock age list by search
				// -37, -36, ..., -1, 1, ..., 36 -> -360 to 360
				int selectRangeInt = (new Integer(selectRange)).intValue();
				int ageStart = 0;
				int ageEnd = 0;
				if (selectRangeInt < 0) {
					ageStart = (selectRangeInt * 10) + 1;
					ageEnd = (selectRangeInt + 1) * 10;
				} else {
					ageStart = (selectRangeInt - 1) * 10;
					ageEnd = selectRangeInt * 10 - 1;
				}
		
				// may need to put this to cache later
				list = ageDao.getStocAgeListFromDB(null,
						String.valueOf(ageStart), String.valueOf(ageEnd));
			}
	
			if (list == null) {
				list = new Vector();
			} else {
				// find the gain/loss
				updateGainField(list);
			}
		} catch (Exception e) {
			e.printStackTrace();
			list = new Vector();
		}
		
		return list;
	}

	/**
	 * Search stock_age table, to get summary list
	 * 
	 * @return
	 */
	private Vector loadAgeSummaryList(AgeSrchDAO ageDao) {
		Vector sumList = new Vector();
		try {
			sumList = ScanCachePool.getStockAgeSummaryList(ageDao);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return sumList;
	}

	private void updateGainField(Vector ageList) {
		if (ageList != null && ageList.size() > 0) {
			DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
			for (int i = 0; i < ageList.size(); i++) {
				StockAge stockAge = (StockAge) ageList.get(i);
				double trend = stockAge.getAge51();

				double percent = 0;
				if (stockAge.getPtPrice() > 0) {
					percent = ((double) (stockAge.getCurrPrice() - stockAge
							.getPtPrice()) / stockAge.getPtPrice()) * 100;

					if (trend < 0) {
						percent = -percent;
					}

					// format the gain
					stockAge.setGain((new Double(oneDigitFormatter
							.format(percent))).doubleValue());
				} else {
					// strange error
					System.out
							.println("Error: age crossing price is zero for: "
									+ stockAge.getTicker());
				}
			}
		}
	}
}
