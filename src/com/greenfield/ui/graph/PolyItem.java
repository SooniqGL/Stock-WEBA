/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.graph;

/**
 *
 * @author qz69042
 */
public class PolyItem {
    private int[] xPolyline  	= null;
    private int[] yPolyline  	= null;
    private int totalPoints     = 0;

    private String colorCode;

    public int getTotalPoints() {
        return totalPoints;
    }

    public void setTotalPoints(int totalPoints) {
        this.totalPoints = totalPoints;
    }

    public int[] getxPolyline() {
        return xPolyline;
    }

    public void setxPolyline(int[] xPolyline) {
        this.xPolyline = xPolyline;
    }

    public int[] getyPolyline() {
        return yPolyline;
    }

    public void setyPolyline(int[] yPolyline) {
        this.yPolyline = yPolyline;
    }

    public String getColorCode() {
        return colorCode;
    }

    public void setColorCode(String colorCode) {
        this.colorCode = colorCode;
    }



}
