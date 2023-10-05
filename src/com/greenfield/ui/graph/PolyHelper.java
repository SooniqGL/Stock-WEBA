/*
 * Created on Feb 8, 2008
 */
package com.greenfield.ui.graph;

import java.awt.Color;
import java.awt.Graphics2D;
import java.util.Vector;

/**
 * @author zhangqx
 */
public class PolyHelper {
	private Vector polyList = new Vector();
	private Vector topLineList;
	private Vector bottomLineList;
	private int currentOrder = 0;

	public void addPointPair(int ax, int ay, int bx, int by, boolean isLast) {
		addPointPair(new PointItem(ax, ay), new PointItem(bx, by), isLast);
	}
	
	public void addPointPair(PointItem a, PointItem b, boolean isLast) {
		if (a == null || b == null) {
			// exception
			return;
		}
		
		if (topLineList == null || bottomLineList == null) {
			topLineList = new Vector();
			bottomLineList = new Vector();
		}
		
		if (topLineList.size() == 0) {
			if (a.getY() >= b.getY()) {
				topLineList.add(a);
				bottomLineList.add(b);
				currentOrder = 1;
			} else {
				topLineList.add(b);
				bottomLineList.add(a);
				currentOrder = -1;
			}
		} else {
			if (a.getY() >= b.getY()) {
				if (currentOrder == 1) {
					topLineList.add(a);
					bottomLineList.add(b);
				} else {
					// time to switch
					breakPointPair(a, b, isLast);
					currentOrder = 1;
				}
			} else {
				if (currentOrder == 1) {
					// time to switch
					breakPointPair(b, a, isLast);
					currentOrder = -1;
				} else {
					topLineList.add(b);
					bottomLineList.add(a);
				}
			}
		}
		
		if (isLast) {
			closeOnePoly(null, null, null, true);
		}
		
		
	}
	
	/**
	 * a is always above b, 
	 * but since the pair point order has changed,
	 * need to close the previous ploy, and open a new one.
	 * @param a
	 * @param b
	 */
	public void breakPointPair(PointItem a, PointItem b, boolean isLast) {
		if (topLineList == null || topLineList.size() < 1
			|| bottomLineList == null || bottomLineList.size() < 1) {
			// error - should not come to here
			return;
		}
		
		PointItem prevTop = (PointItem) topLineList.get(topLineList.size() - 1);
		PointItem prevBottom = (PointItem) bottomLineList.get(bottomLineList.size() - 1);
		
		if (a.getY() == b.getY()) {
			topLineList.add(b);
			closeOnePoly(null, a, a, isLast);
			return;
		} else if (a.getX() == prevTop.getX()) {
			// error - need to log info
			return;
		}
		
		double k1 = (double) (b.getY() - prevTop.getY()) / (a.getX() - prevTop.getX());
		double k2 = (double) (a.getY() - prevBottom.getY()) / (a.getX() - prevTop.getX());
		if (k1 == k2) {
			// impossible error
			return;
		}
		
		double midY = (double) (k2 * prevTop.getY() - k1 * prevBottom.getY()) / (k2 - k1);
		double midX = 0;
		if (k1 != 0) {
			midX = prevTop.getX() + (double) (midY - prevTop.getY()) / k1;
		} else {
			midX = prevTop.getX() + (double) (midY - prevBottom.getY()) / k2;
		}
		
		// add this point to the old then close
		PointItem midP = new PointItem((int) midX, (int) midY);
		topLineList.add(midP);
		closeOnePoly(midP, a, b, false);
		
	}
	
	/**
	 * Close the current poly, start the next one.
	 * @param startP
	 */
	public void closeOnePoly(PointItem midP, PointItem a, PointItem b, boolean isLast) {
		
		if (topLineList != null && bottomLineList != null) {
			int length = topLineList.size() + bottomLineList.size();
			int[] xArray = new int[length];
			int[] yArray = new int[length];
			
			int j = 0;
			for (int i = 0; i < topLineList.size(); i ++, j++) {
				xArray[j] = ((PointItem) topLineList.get(i)).getX();
				yArray[j] = ((PointItem) topLineList.get(i)).getY();
			}
						
			for (int i = bottomLineList.size() - 1; i >= 0; i --, j++) {
				xArray[j] = ((PointItem) bottomLineList.get(i)).getX();
				yArray[j] = ((PointItem) bottomLineList.get(i)).getY();
			}
			
			polyList.add(xArray);
			polyList.add(yArray);
			
			// close these
			topLineList = null;
			bottomLineList = null;
		}
		
		if (!isLast) {
			// reset next
			topLineList = new Vector();
			bottomLineList = new Vector();
			if (midP != null) {
				topLineList.add(midP);
				bottomLineList.add(midP);
			}
			
			topLineList.add(a);
			bottomLineList.add(b);
		}
		
	}
	
	/**
	 * Fill all the poly's
	 * @param pad
	 */
	public void fillAllPoly(Graphics2D pad, Color fillColor) {
		pad.setColor(fillColor);
		if (polyList != null) {
			for (int i = 0; i < polyList.size() - 1; i += 2) {
				int[] xArray = (int[]) polyList.get(i);
				int[] yArray = (int[]) polyList.get(i + 1);
				int size = xArray.length;
				pad.fillPolygon(xArray, yArray, size);
			}
		}
	}
}
