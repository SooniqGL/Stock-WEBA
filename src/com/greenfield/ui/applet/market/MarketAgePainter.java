/*
 * Created on Jan 5, 2007
 */
package com.greenfield.ui.applet.market;

import java.awt.Color;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.util.Observable;
import java.util.Observer;

/**
 * @author qin
 *
 * Try to paint the stuff in the background.
 */
public class MarketAgePainter implements Runnable {
	private int Y_TOP = 30;
	private int Y_BOTTOM = 10;
	private int X_LEFT = 10;
	private int X_RIGHT = 10;
	private int RULER_HT = 20;
	
	private int BPI_LEFT = 5;  // left indent
	private int BPI_TOP = 5;   // from top
	private int BPI_GAP = 15;   // extra for caption
	private int BPI_WIDTH = 300;
	private int BPI_HEIGHT = 110;
	
	private double xStep = 0;
	private double yStep = 0;
	private double xStepBpi = 0;
	private double yStepBpi = 0;
	private int xOrig = 0;
	private int yOrig = 0;
	private int xLeft = 0, yTop = 0, xRight = 0, yBottom = 0;
	private int chartWidth = 0, chartHeight = 0;
	
	// pass from caller	
	private MarketAgeContext processContext;
	private Graphics pad;
	
	// local variables
	private static FontMetrics fontMetrics;

	
	public MarketAgePainter(MarketAgeContext processContext, Graphics pad) {
		this.processContext = processContext;
		this.pad = pad;
				
	}

	public synchronized boolean start(StringBuffer message) {
		boolean started = true;
	
		try {
			Thread t = new Thread(this);
			t.start();
			message.append("Successfully started");
		
		} catch (Exception e) {
			// log e
			e.printStackTrace();
		
			message.append("Error to start, reason: " + e.getMessage());
			started = false;
		}

		return started;
	
	}

	/**
	 * send stopping signature to process.
	 * @param message
	 */
	public synchronized boolean stop(StringBuffer message) {
		boolean stopping = true;
	
		try {
			synchronized (processContext) {
				processContext.setRunning(false);	
			}
		} catch (Exception e) {
			// log e
			e.printStackTrace();

			message.append("Error to stop, reason: " + e.getMessage());
			stopping = false;
		}

		return stopping;
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Runnable#run()
	 */
	public void run() {
		// do the painting
		runProcess(true);
	}
	
	/**
	 * if runAll is true, go to the end from the current position;
	 * else, just run the current one.
	 * @param runAll
	 */
	public void runProcess(boolean runAll) {
		init();

		int currentPos = processContext.getCurrentPos();
		int size = processContext.getAgeDailyList().size();

		//// print copyright
		pad.setColor(Color.blue);
		pad.drawString("Stock Age Chart, " + MarketAgeContext.REGULAR_COPYRIGHT, 
				xLeft, yTop - 10);

		// draw ruler once
		drawRulers();

		if (runAll) {
			if (processContext.getCurrentPos() == size - 1) {
				processContext.setCurrentPos(0);
				currentPos = 0;
			}
			
			for (int i = currentPos; i < size && processContext.isRunning(); i ++) {			
				// paint ith data		
				drawChart(i, true);
				processContext.setCurrentPos(i);
		
			}
		} else {
			// just do the current one
			drawChart(currentPos, false);
		}
	
		processContext.setRunning(false);
	}
	
	private void init() {
		fontMetrics = pad.getFontMetrics();
		
		int maxCnt = processContext.getMaxCount();
		if (maxCnt <= 0) {
			maxCnt = 1;  // error - should not happen
		}
				
		chartWidth = MarketAgeContext.IMAGE_WIDTH - X_LEFT - X_RIGHT;
		chartHeight = MarketAgeContext.IMAGE_HEIGHT - Y_TOP - Y_BOTTOM - RULER_HT;
		
		xOrig 	= X_LEFT + chartWidth / 2;
		yOrig 	= Y_TOP + chartHeight / 2;
		xLeft 	= X_LEFT;
		xRight 	= MarketAgeContext.IMAGE_WIDTH - X_RIGHT;
		yTop 	= Y_TOP;
		yBottom = MarketAgeContext.IMAGE_HEIGHT - Y_BOTTOM - RULER_HT;

		xStep = (double) chartWidth / 26;
		yStep = (double) chartHeight / ( 2 * maxCnt);
		
		int size = processContext.getAgeDailyList().size();
		xStepBpi = (double) BPI_WIDTH / (size + 1);
		yStepBpi = (double) (BPI_HEIGHT - BPI_GAP) / 100;
	}
	
	private void drawRulers() {
		
		pad.setColor(Color.blue);
		int xx = xOrig;
		for (int j = 0; j < 12; j ++, xx += xStep) {
			pad.drawLine((int) (xx + xStep), yBottom, (int) (xx + xStep), yBottom + 3);
	
			String num = String.valueOf(10 * (j + 1));
			int len = fontMetrics.stringWidth(num) / 2;
			pad.drawString(num, (int) (xx + xStep - len), yBottom + 15);
		}
		
		xx = (int) (xOrig - xStep);
		for (int j = 0; j < 12; j ++, xx -= xStep) {
			pad.drawLine(xx, yBottom, xx, yBottom + 3);
			
			String num = String.valueOf(10 * (-1 - j));
			int len = fontMetrics.stringWidth(num) / 2;
			pad.drawString(num, xx - len, yBottom + 15);
		}
		
		String age = "Age";
		int aLen = fontMetrics.stringWidth(age) / 2;
		pad.drawString(age, xOrig - aLen, yBottom + 15);
	}
	
	private void drowBpi(int currentPos) {
		pad.setColor(Color.gray);
		pad.drawRect(xLeft + BPI_LEFT, yTop + BPI_TOP, BPI_WIDTH, BPI_HEIGHT);
		pad.setColor(Color.red);
		pad.drawString("BPI - Bullish Percent Index", xLeft + BPI_LEFT + 20, yTop + BPI_TOP + 15);
		
		int totalPoints = currentPos + 1;
		int[] xPolylineBpi  	= new int[totalPoints];
		int[] yPolylineBpi 	= new int[totalPoints];
		
		double xx = xLeft + BPI_LEFT + xStepBpi;
		pad.setColor(Color.blue);
		for (int i = 0; i <= currentPos; i++, xx += xStepBpi) {
			MarketAgeDaily daily = (MarketAgeDaily) processContext.getAgeDailyList().get(i);
			double percent = (double) daily.getTotalUp() / (daily.getTotalUp() + daily.getTotalDown());
			double height = percent * yStepBpi * 100;
			// pad.fillOval((int) xx, (int) (yTop + BPI_TOP + BPI_HEIGHT - height), 3, 3);
			xPolylineBpi[i] = (int) xx;
			yPolylineBpi[i] = (int) (yTop + BPI_TOP + BPI_HEIGHT - height);
		}
		
		pad.drawPolyline(xPolylineBpi, yPolylineBpi, totalPoints);
	} 
	
	private void drawChart(int i, boolean doSleep) {
		pad.setColor(MarketAgeContext.CHART_COLOR);
		pad.fillRect(xLeft, yTop, chartWidth, chartHeight);
					
		// daily
		MarketAgeDaily daily = (MarketAgeDaily) processContext.getAgeDailyList().get(i);
				
		int height = 0;
		int xx = xOrig;
		
		int[] cpList = daily.getCpList();
		int[] dpList = daily.getDpList();
		for (int j = 0; j < 13; j ++, xx += xStep) {
			// pad.drawString(String.valueOf(cpList[j]), xx, 250);
			pad.setColor(Color.green);
			height = (int) (yStep * cpList[j]);
			pad.fillRect(xx + 2, yOrig - height, (int) (xStep - 2), height);
			
			pad.setColor(Color.red);
			height = (int) (yStep * dpList[j] * 7);
			pad.fillRect(xx + 2, yOrig, (int) (xStep - 2), height);
		}
		
		
		int[] cnList = daily.getCnList();
		int[] dnList = daily.getDnList();
		xx = (int) (xOrig - xStep);
		for (int j = 0; j < 13; j ++, xx -= xStep) {
			// pad.drawString(String.valueOf(cpList[j]), xx, 250);
			pad.setColor(Color.red);
			height = (int) (yStep * cnList[j]);
			pad.fillRect(xx + 2, yOrig, (int) (xStep - 2), height);
			
			pad.setColor(Color.green);
			height = (int) (yStep * dnList[j] * 7);
			pad.fillRect(xx + 2, yOrig - height, (int) (xStep - 2), height);
			
		}
		
		//draw caption
		pad.setColor(Color.blue);
		String caption = "Market: " + processContext.getMarketName()
			+ ", Date: " + daily.getDateInLongFormat();
		int len = fontMetrics.stringWidth(caption);
		int captionX = xLeft + BPI_LEFT + (BPI_WIDTH - len) / 2;
		pad.drawString( caption, captionX, yTop + BPI_TOP + BPI_HEIGHT - 5 );
		
		// draw totals
		pad.setColor(Color.blue);
		pad.drawString("Total up trend today: " + daily.getTotalUp(), 500, 260);
		pad.drawString("Total down trend today: " + daily.getTotalDown(), 500, 280);
		pad.drawString("Total changed to up trend today: " + daily.getTotalToUp(), 500, 300);
		pad.drawString("Total changed to down trend today: " + daily.getTotalToDown(), 500, 320);
		
		// draw bpi
		drowBpi(i);
		
		//// draw two axies
		pad.setColor(Color.gray);
		pad.drawLine(xLeft, yOrig, xRight, yOrig);
		pad.drawLine(xOrig, yTop, xOrig, yBottom);
		
		//draw box
		pad.drawRect(xLeft, yTop, chartWidth, chartHeight);
		
		// sleep
		if (doSleep) {
			try {
				if (processContext.isFastSpeed()) {
					Thread.sleep(200);
				} else {
					Thread.sleep(1000);
				}
			} catch (Exception e) {
				//
			}
		}
	}

}
