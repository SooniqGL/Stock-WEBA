/*
 * Created on Jan 5, 2007
 */
package com.greenfield.ui.applet.market;

import java.applet.Applet;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;

// import netscape.javascript.JSObject;

/**
 * @author zhangqx
 * 
 * Show market age distribution.
 */
public class MarketAgeApplet  extends Applet implements Runnable
{
	
	private MarketAgeContext processContext;
	private String content;
	
	public void start() {
		try {
			// get values to applet		   			  
			processContext = new MarketAgeContext();
			content = getParameter("content");
			processContext.setAgeContent(content);
			processContext.setSpeed(MarketAgeContext.FAST_SPEED);
			MarketAgeHelper helper = new MarketAgeHelper();
			helper.prepareData(processContext);
		} catch (Exception e) {
			// show the error
			myShowStatus("Tree start - Error: " + e.getMessage());	    // error
		}
	 
	}
     
	public void myShowStatus(String status) {
		try {
			//JSObject window = JSObject.getWindow(this); // this=applet
			//window.eval("showComments(\"" + status + "\")");
		} catch (Exception ex) { 	 
		}
	}
        
	public void paint(Graphics g) {	
		try {
			// erase the work space
			Color loc_color = g.getColor();
			g.setColor(MarketAgeContext.IMAGE_BGCOLOR);
			g.clearRect(0, 0, MarketAgeContext.IMAGE_WIDTH, MarketAgeContext.IMAGE_HEIGHT); 
			g.fillRect(0, 0, MarketAgeContext.IMAGE_WIDTH, MarketAgeContext.IMAGE_HEIGHT); 
	 
			// restore the color
			g.setColor(loc_color);	    
			
			/*
			int halfY = MarketAgeContext.IMAGE_HEIGHT / 2;
			String msg = "This will show how the stock ages are changing for " + processContext.getMarketName();
			g.setColor(new Color(0, 155, 255));  
			Font origFont = g.getFont();  
			Font font = new Font("TimesRoman", Font.ITALIC, 24);
			g.setFont(font);
			FontMetrics fm = g.getFontMetrics(font);
			int halfX = (int) (MarketAgeContext.IMAGE_WIDTH - fm.stringWidth(msg)) / 2;
			g.drawString(msg, halfX, halfY);
			
			g.setFont(origFont);
			
			try {		
				Thread.sleep(2000);
			} catch (Exception e) {
				//
			}
			*/
			// paint the last day screen
			int size = processContext.getAgeDailyList().size();
			processContext.setCurrentPos(size - 1);
			MarketAgePainter painter = new MarketAgePainter(processContext, getGraphics());
			painter.runProcess(false);
						
		} catch (Exception e) {
			// myShowStatus("Paint Error: " + StringHelper.printStackTrace(e, true));
		}

	}
	
	public void startStopPlay() {
		if (!processContext.isRunning()) {
			processContext.setRunning(true);
			MarketAgePainter painter = new MarketAgePainter(processContext, getGraphics());
			StringBuffer msg = new StringBuffer();
			painter.start(msg);
		} else {
			processContext.setRunning(false);
		}
	}
	
	public void goLeftPlay() {
		if (!processContext.isRunning()) {		
			processContext.setRunning(true);
			int currentPos = processContext.getCurrentPos();
			int size = processContext.getAgeDailyList().size();
			if (currentPos == 0) {
				currentPos = size - 1;
			} else {
				currentPos --;
			}
			
			processContext.setCurrentPos(currentPos);
			MarketAgePainter painter = new MarketAgePainter(processContext, getGraphics());
			painter.runProcess(false);
		}
	}
	
	public void goRightPlay() {
		if (!processContext.isRunning()) {		
			processContext.setRunning(true);
			int currentPos = processContext.getCurrentPos();
			int size = processContext.getAgeDailyList().size();
			if (currentPos == size - 1) {
				currentPos = 0;
			} else {
				currentPos ++;
			}
		
			processContext.setCurrentPos(currentPos);
			MarketAgePainter painter = new MarketAgePainter(processContext, getGraphics());
			painter.runProcess(false);
		}
	}
	
	public void setSlowSpeed() {
		processContext.setSpeed(MarketAgeContext.SLOW_SPEED);
	}
	
	public void setFastSpeed() {
		processContext.setSpeed(MarketAgeContext.FAST_SPEED);
	}

	// do nothing
	public void run() {
	/**
		try {
			while (true) {
				Thread.currentThread().sleep(min_paint_time);
				if (need_paint) {
					need_paint = false;
					paint(getGraphics());
				}
			}
		} catch (Exception e) {
			// ok
		}  **/
	 }


}
