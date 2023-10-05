/*
 * Created on Jan 6, 2007
 */
package com.greenfield.ui.applet.market;

/**
 * @author qin
 */
public class MarketAgeDaily {
	private String mDate;
	private String dateInLongFormat;
	private int totalTickers;
	private int[] cpList = null;
	private int[] dpList = null;
	private int[] cnList = null;
	private int[] dnList = null;
	
	private int totalUp;
	private int totalDown;
	private int totalToUp;
	private int totalToDown;
	
	/**
	 * @return
	 */
	public int[] getCnList() {
		return cnList;
	}

	/**
	 * @return
	 */
	public int[] getCpList() {
		return cpList;
	}

	/**
	 * @return
	 */
	public int[] getDnList() {
		return dnList;
	}

	/**
	 * @return
	 */
	public int[] getDpList() {
		return dpList;
	}

	/**
	 * @return
	 */
	public String getMDate() {
		return mDate;
	}

	/**
	 * @return
	 */
	public int getTotalTickers() {
		return totalTickers;
	}

	/**
	 * @param is
	 */
	public void setCnList(int[] is) {
		cnList = is;
	}

	/**
	 * @param is
	 */
	public void setCpList(int[] is) {
		cpList = is;
	}

	/**
	 * @param is
	 */
	public void setDnList(int[] is) {
		dnList = is;
	}

	/**
	 * @param is
	 */
	public void setDpList(int[] is) {
		dpList = is;
	}

	/**
	 * @param string
	 */
	public void setMDate(String string) {
		mDate = string;
	}

	/**
	 * @param i
	 */
	public void setTotalTickers(int i) {
		totalTickers = i;
	}

	/**
	 * @return
	 */
	public String getDateInLongFormat() {
		return dateInLongFormat;
	}

	/**
	 * @param string
	 */
	public void setDateInLongFormat(String string) {
		dateInLongFormat = string;
	}

	/**
	 * @return
	 */
	public int getTotalDown() {
		return totalDown;
	}

	/**
	 * @return
	 */
	public int getTotalToDown() {
		return totalToDown;
	}

	/**
	 * @return
	 */
	public int getTotalToUp() {
		return totalToUp;
	}

	/**
	 * @return
	 */
	public int getTotalUp() {
		return totalUp;
	}

	/**
	 * @param i
	 */
	public void setTotalDown(int i) {
		totalDown = i;
	}

	/**
	 * @param i
	 */
	public void setTotalToDown(int i) {
		totalToDown = i;
	}

	/**
	 * @param i
	 */
	public void setTotalToUp(int i) {
		totalToUp = i;
	}

	/**
	 * @param i
	 */
	public void setTotalUp(int i) {
		totalUp = i;
	}

}
