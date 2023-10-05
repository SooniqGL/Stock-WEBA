/*
 * Created on Nov 11, 2007
 */
package com.greenfield.ui.graph;

/**
 * @author qin
 *
 * Basic line
 */
public class LineItem {
	private int startX;
	private int startY;
	private int endX;
	private int endY;
	
	private String colorCode;
	
	// easy to make
	public LineItem(int x1, int y1, int x2, int y2, String color) {
		startX = x1;
		startY = y1;
		endX = x2;
		endY = y2;
		colorCode = color;
	}
	
	/**
	 * @return
	 */
	public String getColorCode() {
		return colorCode;
	}

	/**
	 * @return
	 */
	public int getEndX() {
		return endX;
	}

	/**
	 * @return
	 */
	public int getEndY() {
		return endY;
	}

	/**
	 * @return
	 */
	public int getStartX() {
		return startX;
	}

	/**
	 * @return
	 */
	public int getStartY() {
		return startY;
	}

	/**
	 * @param string
	 */
	public void setColorCode(String string) {
		colorCode = string;
	}

	/**
	 * @param i
	 */
	public void setEndX(int i) {
		endX = i;
	}

	/**
	 * @param i
	 */
	public void setEndY(int i) {
		endY = i;
	}

	/**
	 * @param i
	 */
	public void setStartX(int i) {
		startX = i;
	}

	/**
	 * @param i
	 */
	public void setStartY(int i) {
		startY = i;
	}

}
