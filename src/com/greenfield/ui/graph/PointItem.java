/*
 * Created on Feb 6, 2008
 */
package com.greenfield.ui.graph;

/**
 * @author zhangqx
 */
public class PointItem {
	private int x;
	private int y;
	
	public PointItem(int intX, int intY) {
		x = intX;
		y = intY;
	}
	/**
	 * @return
	 */
	public int getX() {
		return x;
	}

	/**
	 * @return
	 */
	public int getY() {
		return y;
	}

	/**
	 * @param i
	 */
	public void setX(int i) {
		x = i;
	}

	/**
	 * @param i
	 */
	public void setY(int i) {
		y = i;
	}

}
