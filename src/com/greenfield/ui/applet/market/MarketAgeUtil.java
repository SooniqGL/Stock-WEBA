/*
 * Created on Jan 6, 2007
 */
package com.greenfield.ui.applet.market;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

/**
 * @author qin
 *
 * String and date util used in the client side.
 */
public class MarketAgeUtil {
	public static final SimpleDateFormat YYYY_MM_DD_formatter = new SimpleDateFormat ("yyyy/MM/dd");
	public static final SimpleDateFormat MM_DD_YYYY_formatter = new SimpleDateFormat ("MM/dd/yyyy");
	public static final SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat ("MMM dd, yyyy");

	
	public static Vector mySplit(String input, String separator) {
		if (input == null || input.equals("") || separator == null) {
			return null;
		}

		String temp = input;
		Vector vec = new Vector();
		while (temp != null) {
			int index = temp.indexOf(separator);
			if (index > -1) {
				vec.add(new String(temp.substring(0, index)));
				temp = temp.substring(index + 1);
			} else {
				vec.add(new String(temp));
				temp = null;
			}
		}

		return vec;
	}
	
	/** reverse the order from yyyy/mm/dd to mm/dd/yyyy format */
	public static String convertMMDate(String yyyymmdd) throws Exception {
		if (yyyymmdd == null) {
			return null;
		}
	
		Vector list = mySplit(yyyymmdd, "/");
		if (list != null && list.size() == 3) {
			return (String) list.get(1) + "/" + (String) list.get(2) + "/" + (String) list.get(0);
		} else {
			// error
			throw new Exception("Error, convertMMDate, input not right format: " + yyyymmdd);
		}
	}
	
	public static String getDateStringInLongFormat(String yyyymmdd) {
		if (yyyymmdd == null) {
			return "";
		}

		Date theDate = null;
		try {
			theDate = YYYY_MM_DD_formatter.parse(yyyymmdd);
		} catch (Exception e) {
			return "";
		}
		return MMM_DD_YYYY_formatter.format( theDate );
	}
}
