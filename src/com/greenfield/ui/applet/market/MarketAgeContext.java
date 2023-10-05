/*
 * Created on Jan 5, 2007
 */
package com.greenfield.ui.applet.market;

import java.awt.Color;
import java.util.Vector;

/**
 * @author qin
 *
 */
public class MarketAgeContext {
	public static int IMAGE_WIDTH 	= 800;
	public static int IMAGE_HEIGHT 	= 400;
	public static Color IMAGE_BGCOLOR = new Color(235, 235, 235);
	public static Color CHART_COLOR = new Color(255, 255, 250);
	public final static String REGULAR_COPYRIGHT = "Copyright 2006, Sooniq Technology Corporation";
	
	public static String SLOW_SPEED 	= "SLOW";
	public static String FAST_SPEED 	= "FAST";
		
	private boolean isRunning;
	private int currentPos;
	private String ageContent;
	
	private String speed;
	
	// The following two will be set by converting the ageContent string
	private String marketName;
	private Vector ageDailyList;   // each day has one entry
	private int maxCount;

	public void addAgeDailyToList(MarketAgeDaily daily) {
		if (daily != null) {
			if (ageDailyList == null) {
				ageDailyList = new Vector();
			}
			
			ageDailyList.add(daily);
		}
	}
	/**
	 * @return
	 */
	public int getCurrentPos() {
		return currentPos;
	}

	/**
	 * @return
	 */
	public boolean isRunning() {
		return isRunning;
	}

	/**
	 * @param i
	 */
	public void setCurrentPos(int i) {
		currentPos = i;
	}

	/**
	 * @param b
	 */
	public void setRunning(boolean b) {
		isRunning = b;
	}

	/**
	 * @return
	 */
	public String getAgeContent() {
		return ageContent;
	}

	/**
	 * @param string
	 */
	public void setAgeContent(String string) {
		ageContent = string;
	}

	/**
	 * @return
	 */
	public Vector getAgeDailyList() {
		return ageDailyList;
	}

	/**
	 * @return
	 */
	public String getMarketName() {
		return marketName;
	}

	/**
	 * @param vector
	 */
	public void setAgeDailyList(Vector vector) {
		ageDailyList = vector;
	}

	/**
	 * @param string
	 */
	public void setMarketName(String string) {
		marketName = string;
	}

	/**
	 * @return
	 */
	public int getMaxCount() {
		return maxCount;
	}

	/**
	 * @param i
	 */
	public void setMaxCount(int i) {
		maxCount = i;
	}

	/**
	 * @return
	 */
	public String getSpeed() {
		return speed;
	}

	/**
	 * @param string
	 */
	public void setSpeed(String string) {
		speed = string;
	}

	public boolean isFastSpeed() {
		if (speed != null && speed.equals(SLOW_SPEED)) {
			return false;
		}
		
		return true;
	}
}
