/*
 * Created on Dec 15, 2006
 */
package com.greenfield.ui.cache;

import java.util.Comparator;

import com.greenfield.common.object.scan.StockAge;

/**
 * @author qin
 */
public class StockAgeComp implements Comparator<StockAge> {

	/**
	 * Compare two objects
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public int compare(StockAge stk1, StockAge stk2) {
		int result = 0;

		double profit1 = (double) (stk1.getCurrPrice() - stk1.getPtPrice()) / stk1.getPtPrice();
		double profit2 = (double) (stk2.getCurrPrice() - stk2.getPtPrice()) / stk2.getPtPrice();
		
		// descending order
		if (profit1 > profit2) {
			result = -1;
		} else if (profit1 < profit2) {
			result = 1;
		}
		
		return result;
   }
}


    
   

