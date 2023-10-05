/*
 * Created on Dec 15, 2006
 */
package com.greenfield.ui.handler.scan;

import java.util.Comparator;

import com.greenfield.common.object.scan.ScanHistoryVO;

/**
 * @author qin
 */
public class ScanDisplayComp implements Comparator {
	public static final String BY_TICKER 		= "t";
	public static final String BY_COMPANY 		= "c";
	public static final String BY_CURR_PRICE 	= "cp";
	public static final String BY_SCAN_PRICE 	= "sp";
	public static final String BY_GAIN 		= "g";
        public static final String BY_SCAN_DATE		= "sd";
	
	// local variables
	private String byWhat;
	private boolean isAscending;

   /**
	* Construct a new comparator, set the specified column
	* and direction.
	*
	* @param column the column to sort by
	* @param ascending flag indicating whether the sort should
	*    be in ascending order
	*/
	public ScanDisplayComp(String byWhat, boolean isAscending) {
		this.byWhat = byWhat;
		this.isAscending = isAscending;
	}
	
	public ScanDisplayComp(String byWhatLoc, String sortOrder) {
		if (byWhatLoc == null || byWhatLoc.equals("")) {
			byWhat = BY_CURR_PRICE;
		} else {
			byWhat = byWhatLoc;
		}
		
		if (sortOrder != null && sortOrder.equals("1")) {
			isAscending = false;
		} else {
			isAscending = true;
		}
	}


	/**
	 * Compare two objects
	 * @param obj1
	 * @param obj2
	 * @return
	 */
	public int compare(Object obj1, Object obj2) {
		ScanHistoryVO stk1 = (ScanHistoryVO) obj1;
		ScanHistoryVO stk2 = (ScanHistoryVO) obj2;
		
		int result = 0;
		int factor = 1;

		if (isAscending) {
			factor = 1;
		} else {
			factor = -1;
		}
		
		if (byWhat == null) {
			// error
			return result;
		}
		
		if (byWhat.equals(BY_TICKER)) {
			if (stk1.getTicker().compareTo(stk2.getTicker()) < 0) {
				result = -1 * factor;
			} else if (stk1.getTicker().compareTo(stk2.getTicker()) == 0) {
				result = 0;
			} else {
				result = factor;
			}
		} else if (byWhat.equals(BY_COMPANY)) {
			if (stk1.getCompanyName().compareTo(stk2.getCompanyName()) < 0) {
				result = -1 * factor;
			} else if (stk1.getCompanyName().compareTo(stk2.getCompanyName()) == 0) {
				result = 0;
			} else {
				result = factor;
			}
		} else if (byWhat.equals(BY_CURR_PRICE)) {
			if (stk1.getCurrentPrice() < stk2.getCurrentPrice()) {
				result = -1 * factor;
			} else if (stk1.getCurrentPrice() == stk2.getCurrentPrice()) {
				result = 0;
			} else {
				result = factor;
			}
		} else if (byWhat.equals(BY_SCAN_PRICE)) {
			if (stk1.getScanPrice() < stk2.getScanPrice()) {
				result = -1 * factor;
			} else if (stk1.getScanPrice() == stk2.getScanPrice()) {
				result = 0;
			} else {
				result = factor;
			}
		} else if (byWhat.equals(BY_GAIN)) {
			if (stk1.getGain() < stk2.getGain()) {
				result = -1 * factor;
			} else if (stk1.getGain() == stk2.getGain()) {
				result = 0;
			} else {
				result = factor;
			}
                } else if (byWhat.equals(BY_SCAN_DATE)) {
			if (stk1.getScanDate().compareTo(stk2.getScanDate()) < 0) {
				result = -1 * factor;
			} else if (stk1.getScanDate().equals(stk2.getScanDate())) {
				result = 0;
			} else {
				result = factor;
			}
		}
		
		return result;
   }
}


    
   

