/*
 * Created on May 2, 2007
 */
package com.greenfield.ui.graph;

import java.text.DecimalFormat;
//import java.text.NumberFormat;
import java.util.Date;
//import java.util.Enumeration;
import java.util.Vector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
//import java.awt.Frame;
//import java.awt.GradientPaint;
//import java.awt.Graphics;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
//import java.sql.Timestamp;
















import com.greenfield.common.service.LinearModelService;
import com.greenfield.common.service.RSIService;
import com.greenfield.common.service.RangeService;
import com.greenfield.common.service.ServiceContext;
import com.greenfield.common.service.TrendService;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.ScanUtil;
import com.greenfield.common.util.StringHelper;
import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.common.constant.TrendTypes;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.object.stock.StockExt;















//import com.greenfield.ui.service.VolumeService;
//import com.greenfield.ui.service.WarningService;
import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;


/**
 * @author UZHANQX
 *
 * Draw the basic Chart - line.
 */
public class DrawLineChart extends BaseGraphics {
	private static final Logger LOGGER = Logger.getLogger(DrawLineChart.class); 
	
	// which time to draw
	public final static int DEFAULT_PERIOD = 6;   // six months chart
		
	
	// option values
	public final static String DEFAULT_OPTION = "N";
	public final static String SMALL_TYPE = "small";
	public final static String REGULAR_TYPE = "regular";
	public final static int DAYS_IN_MONTH = 22;
	public final static String REGULAR_COPYRIGHT = "Copyright 2006, Sooniq Technology Corporation";
	public final static String SHORT_COPYRIGHT = "Copyright 2006, Sooniq Tech.";
	
	private int TREND_LINE_LEN = 0;   // half-data
	public final static int TREND_LINE_END = 2;
	
	// variables for input and stock related.
	private String stockId;
	private String ticker;
	private int period;
	private String option;
	private String type;
	private Stock stock;
	private Vector priceList;
	private StockExt stockExt;
	private int startIndex;
	private int ballLeftStart;
	private int leftScaleGap;
	private double leftLinePos = 0;
	private String serviceId;
	
	private boolean doRSI;
	private boolean doEMA20;
	private boolean doEMA50;
	
	private double maxGrowth;
	private double maxForce;
	//private double minRS;
	//private double maxRS;
	
	// local variables - for color and style
	private Color colorBlack;
	private Color colorWhite;
	private Color colorRed;
	private Color colorGreen;
	private Color colorBlue;
	private Color colorDark;
	private Color colorYellow;
	private Color colorLtYellow;
	private Color colorDYellow;
	private Color colorLtBlue;
	private Color colorPink;
	private Color imageColor;
	private Color chartColor;
	private Color savedColor;
	private BasicStroke dashStroke;
	private BasicStroke thickStroke;

	// font
	private Font font;
	private Font bigFont;
	private Font midFont;

	private FontMetrics fontMetrics;
	
	// format
	DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
	DecimalFormat roundFormatter = new DecimalFormat("0");
	
	// measures
	private int chartWidth;
	private int chartHeight;
	private int origX;
	private int origY;
	private int volumeY;
	private int growthY;
	private int forceY;
	private int rsY; 
	
	// MEASURE CONSTANTS
	public static final int DEFAULT_IMAGE_WIDTH = 800;
	public static final int DEFAULT_IMAGE_HEIGHT = 450;
	public static final int DEFAULT_IMAGE_HEIGHT_WITH_OPTION = 450;
	public static final int SMALL_IMAGE_WIDTH = 250;
	public static final int SMALL_IMAGE_HEIGHT = 300;
	public static final int SMALL_IMAGE_HEIGHT_WITH_OPTION = 340;
	
	private int X_LEFT = 25;
	private int X_RIGHT = 8;
	private int Y_TOP = 10;
	private int Y_BOTTOM = 10;
	private int VOLUME_HEIGHT = 40;    // for volume box
	private int GROWTH_HEIGHT = 30;    // for GROWTH box
	private int FORCE_HEIGHT = 30;     // for FORCE box
	private int RS_HEIGHT = 30;
	private int TITLE_HEIGHT = 20;
	private int BOX_GAP = 8;
	private int SCALE_LENGTH = 3;      // bars in y axis
	private int BAR_WIDTH = 2;         // bar width in price chart
	private int PRICE_TOP = 20;       // draw above this height
	private int PRICE_BOTTOM = 15;       // draw above this height
	private int TEXT_HEIGHT = 15;
	private int VERTICAL_TEXT_X1 = 17;   // price
	private int SMALL_GAP = 5;	
	
	public void init() {
		colorBlack = Color.black;
		colorWhite = Color.white;
		colorRed = Color.red;
		colorGreen = Color.green;
		colorBlue = Color.blue;
		colorYellow = Color.yellow;
		colorLtYellow = new Color(255, 235, 100);
		colorDYellow = new Color(230, 230, 0);
		colorLtBlue = new Color(50, 120, 0);
		colorDark = new Color(200, 200, 200);
		colorPink = new Color(255, 0, 255);
		imageColor = new Color(235, 235, 235);
		chartColor = new Color(255, 255, 245);
		
		// A dashed stroke
		float strokeThickness = 1.0f;
		float miterLimit = 10f;
		float[] dashPattern = {10f};
		float dashPhase = 5f;
		dashStroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
			BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		
		// for the MA lines
		thickStroke = new BasicStroke(1.3f, BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);
	}
	
	/** test */
	public static void main(String[] argv) {
		try {
			// debug
			// System.out.println("in drawicon ...");
			String fileName = "E:\\temp\\good.gif";

			DrawBasicChart draw = new DrawBasicChart();
			draw.init();
			draw.build(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

			// save the image to file
			draw.saveAs(fileName);
			draw.clear();

			// System.out.println("in drawicon ... 4");
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/** 
	 * Implement the parent method, draw graphics here.
	 */		       
	public void paint() throws Exception {
		// check options 
		doRSI = false;
		if (option != null && option.length() > 0 && option.charAt(0) == 'Y') {
			//doEMA9 = true;
		} else {
			//doEMA9 = false;
		}
		
		if (option != null && option.length() > 1 && option.charAt(1) == 'N') {
			doEMA50 = false;
		} else {
			doEMA50 = true;
		}
		
		// load the stock and calculate data		
		prepareStockInfo();
		if (stock == null || priceList == null || priceList.size() == 0) {
			// no enough data found
			pad.setColor(colorGreen);
			pad.drawString("No chart for this stock with stock_id: " + stockId, 200, 290);
			return;
		} 
		
		if (type != null && type.equals(SMALL_TYPE)) {
			font = new Font("TimesRoman", Font.PLAIN, 8);
			pad.setFont(font);
			VOLUME_HEIGHT = 30;
			RS_HEIGHT = 25;
			FORCE_HEIGHT = 25;
			GROWTH_HEIGHT = 25;
		}
		// prepare the font metrics
		if (font == null) {
			font = pad.getFont();
		}
		
		fontMetrics = frame.getFontMetrics(font);
		
		// If option or not
		int gfHeight = 0;
		if (doRSI) {
			forceY 		= imageHeight - Y_BOTTOM - 2 * BOX_GAP;
			growthY 	= forceY - FORCE_HEIGHT - BOX_GAP;
			rsY 		= growthY - GROWTH_HEIGHT - BOX_GAP;
			volumeY 	= rsY - RS_HEIGHT - BOX_GAP;
			gfHeight 	= FORCE_HEIGHT + GROWTH_HEIGHT + RS_HEIGHT + 5 * BOX_GAP;
		} else {
			forceY 		= imageHeight - Y_BOTTOM - 2 * BOX_GAP;
			growthY 	= forceY - FORCE_HEIGHT - BOX_GAP;
			volumeY 	= growthY - GROWTH_HEIGHT - BOX_GAP;
			gfHeight 	= FORCE_HEIGHT + GROWTH_HEIGHT + 4 * BOX_GAP;
		}
		
		// calculate measures
		chartWidth 	= imageWidth - X_LEFT - X_RIGHT;
		chartHeight = imageHeight - Y_TOP - TITLE_HEIGHT - BOX_GAP 
						- VOLUME_HEIGHT - Y_BOTTOM - gfHeight;
		origX 		= X_LEFT;
		origY 		= volumeY - VOLUME_HEIGHT - BOX_GAP;
		
		
		pad.setColor(imageColor);
		pad.fillRect(0, 0, imageWidth, imageHeight);
		
		
		// draw the frame
		pad.setColor(colorBlack);
		drawChartFrame();
		
		// draw the chart
		pad.setColor(colorBlack);
		drawBarChart();
		
		// remain frames
		pad.setColor(colorBlack);
		drawChartFramePost();
				
		/*
		bigFont = new Font("TimesRoman", Font.BOLD, 24);
		midFont = new Font("TimesRoman", Font.BOLD, 14);
		g.setFont(font);
		*/
		
		// use black as default
		// draw circle
		//Color tColor = g.getColor();
		//g.setColor(colorRed);
		//g.fillOval(startX + SQUARE_SIZE / 4 - 2, startY + SQUARE_SIZE / 4 - 2, 5, 5);
		//g.setColor(tColor);
		// int line_height = (int) Math.sqrt(radius * radius - half_radius * half_radius) + 1;
		// int arcStartDegree = 0;
		// int arcDegree = 90;
		// g.fillArc(startX, startY, SQUARE_SIZE, SQUARE_SIZE, arcStartDegree, arcDegree);

	}

	/**
	 * Draw the frame.
	 */
	private void drawChartFrame() {
		// volume rectangle
		savedColor = pad.getColor();
		pad.setColor(chartColor);
		pad.fillRect(origX, volumeY - VOLUME_HEIGHT, chartWidth, VOLUME_HEIGHT);
		pad.setColor(savedColor);	
		
		// draw option if there is setting
		savedColor = pad.getColor();
		pad.setColor(chartColor);
		pad.fillRect(origX, growthY - GROWTH_HEIGHT, chartWidth, GROWTH_HEIGHT);
		pad.fillRect(origX, forceY  - FORCE_HEIGHT,  chartWidth, FORCE_HEIGHT);
		if (doRSI) {
			pad.fillRect(origX, rsY - RS_HEIGHT, chartWidth, RS_HEIGHT);
		}
		pad.setColor(savedColor);	
	
		
		// draw chart rectange
		pad.setColor(chartColor);
		pad.fillRect(origX, origY - chartHeight, chartWidth, chartHeight);
		pad.setColor(colorBlack);	
		
		// Gif maker complains the too many colors for following
		// JPEG - JUST SHOW NOTHING - WHY?
		//GradientPaint gp = new GradientPaint(50.0f, 50.0f, colorBlue, 
		//		50.0f, 250.0f, colorGreen, true); 
		//pad.setPaint(gp); 
						
		// draw title
		savedColor = pad.getColor();
		pad.setColor(colorBlue);
		String title = "";
		if (type != null && type.equals(SMALL_TYPE)) {
			title = "Chart for " + stock.getCompanyName() + " (" + stock.getTicker() + "), " + SHORT_COPYRIGHT;
		} else {
			title = "Chart for " + stock.getCompanyName() + " (" + stock.getTicker() + "), " + REGULAR_COPYRIGHT;
		}

		pad.drawString(title, origX, Y_TOP + TEXT_HEIGHT);
		pad.setColor(savedColor);
		
		// This set will print vertical strings on screen
		AffineTransform at = new AffineTransform();
		at.setToRotation(1.5 * Math.PI);  // same as: at.rotate(1.5 * Math.PI);
		pad.transform(at); 	
		pad.setColor(colorBlue);
		pad.drawString("Volume", - volumeY, VERTICAL_TEXT_X1);

		String currentDate = "";
		try {
                    // do not use static formatter!
                    SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat ("MMM dd, yyyy");
                    SimpleDateFormat YYYY_MM_DD_formatter  = new SimpleDateFormat ("yyyy/MM/dd");
			currentDate = MMM_DD_YYYY_formatter.format(
				YYYY_MM_DD_formatter.parse(stock.getLastUpdated()));
		} catch (Exception e) {
			LOGGER.warn("Date format error:" + stock.getLastUpdated());
			currentDate = DateUtil.getDateStringInLongFormat(null);
		}
		
		pad.drawString("Daily Price Chart - " + currentDate, - origY + BOX_GAP, VERTICAL_TEXT_X1);
		pad.drawString("Grow", 	- growthY, 	VERTICAL_TEXT_X1);
		pad.drawString("Force", - forceY,  	VERTICAL_TEXT_X1);
		if (doRSI) {
			pad.drawString("RSI", 	- rsY + SMALL_GAP, 		VERTICAL_TEXT_X1);
		}
		

		// reverse the coordinate system 
		AffineTransform atInv = new AffineTransform();
		try {
			atInv = at.createInverse();
			pad.transform(atInv);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("DrawBasicChart:Inverse transform error: " + e.getMessage());
		}
		
		// draw string "SP500"
		Font savedFont = pad.getFont();
		if (type != null && type.equals(SMALL_TYPE)) {
			font = new Font("TimesRoman", Font.PLAIN, 8);
		} else {
			font = new Font("TimesRoman", Font.PLAIN, 9);
		}
		
		String code = "";
		if (stock.getTicker().equals("%5EDJI")) {
			code = "Dow";
		} else if (stock.getTicker().equals("%5EIXIC")) {
			code = "NSDQ";
		} else if (stock.getTicker().equals("%5EGSPC")) {
			code = "S&P";
		} else {
			code = stock.getTicker();
		}
		
		fontMetrics = frame.getFontMetrics(font);
		ballLeftStart = origX - 2* SMALL_GAP + fontMetrics.stringWidth(code);
		pad.setFont(font);
		pad.drawString(code,   origX - 2* SMALL_GAP,  forceY + BOX_GAP + 2);
		pad.drawString("S&P",  origX - 2* SMALL_GAP,  forceY + 2 * BOX_GAP + 2);
		pad.setFont(savedFont);
		fontMetrics = frame.getFontMetrics(savedFont);
	}
	
	/**
	 * Draw these rectangles last so to overwrite others.
	 */ 
	private void drawChartFramePost() {
		pad.setColor(colorBlack);
		pad.drawRect(origX, volumeY - VOLUME_HEIGHT, chartWidth, VOLUME_HEIGHT);	
		pad.drawRect(origX, origY - chartHeight, chartWidth, chartHeight);
		
		// draw option if there is setting
		pad.drawRect(origX, growthY - GROWTH_HEIGHT, chartWidth, GROWTH_HEIGHT);
		pad.drawRect(origX, forceY  - FORCE_HEIGHT,  chartWidth, FORCE_HEIGHT);	
		if (doRSI) {
			pad.drawRect(origX, rsY - RS_HEIGHT, chartWidth, RS_HEIGHT);
		}

	}
	
	/**
	 * Draw the price chart.
	 */
	private void drawBarChart() throws Exception {
		double maxP = stockExt.getMaxPrice();
		double minP = stockExt.getMinPrice();
		double maxVolume = stockExt.getMaxVolume();
		
		if (priceList == null || priceList.size() == 0 || maxVolume <= 0) {
			pad.drawString("Data Error", 200, 200);
			return;
		}
		
		// pick the market flag list
		Vector marketFlagList = MarketCachePool.getMarketIndicators().getMarketFlagList();
		
		// just incase
		int minHeight = 0;
		if (maxP == minP) {
			maxP = minP + 1;
		}
		
		int totalItems = priceList.size() - startIndex;
		double minPLog 		= Math.log(minP);
		double maxPLog		= Math.log(maxP);
		double yStep 		= (double) (chartHeight - PRICE_BOTTOM - PRICE_TOP) / (maxPLog - minPLog);
		double volumeStep 	= (double) VOLUME_HEIGHT / maxVolume;
		
		// debug
		//System.out.println("In draw, size: " + totalItems + "/" + priceList.size());
		double volumeHt = 0, growthHt = 0, forceHt = 0, rsHt = 0;
		double yyOpen = 0, yyClose = 0, yyMin = 0, yyMax = 0;
		Vector monthList = new Vector();
		String prevMonthNum = "";
		
		double growthStep = 0;
		double forceStep = 0;
		double rsStep = 0;
		double zeroGrowthHt = 0;
		double zeroForceHt = 0;
		double lowRSHt = 0;             // 30% line
		double highRSHt = 0;            // 70% line
		double zeroRSHt = 0;
		
		int[] xPolylineRS  		= new int[totalItems];
		int[] yPolylineRS  		= new int[totalItems];
		int[] xPolylineGrowth  	= new int[totalItems];
		int[] yPolylineGrowth  	= new int[totalItems];
		int[] xPolylineForce  	= new int[totalItems];
		int[] yPolylineForce 	= new int[totalItems];
		int[] xPolylineTrend  	= new int[totalItems];
		int[] yPolylineTrend 	= new int[totalItems];
		int[] xPolylineAvg50  	= new int[totalItems];
		int[] yPolylineAvg50 	= new int[totalItems];
		int[] xPolylineAvg21  	= new int[totalItems];
		int[] yPolylineAvg21 	= new int[totalItems];
		int[] xPolylineAvg9  	= new int[totalItems];
		int[] yPolylineAvg9 	= new int[totalItems];
		int totalPoints = 0;
		int totalPoints2 = 0;
		int totalPointsAvg50 = 0;
		int totalPointsAvg21 = 0;
		int totalPointsAvg9 = 0;

		// max/min growth
		findMinMaxGrowthForceRS();
		
		// drow steps	
		growthStep = (double) GROWTH_HEIGHT / ( 2 * maxGrowth);
		forceStep = (double) FORCE_HEIGHT / (2 * maxForce);
		if (doRSI) {
			rsStep = (double) RS_HEIGHT / 100;
		}

		
		// zero line in the middle
		zeroGrowthHt = growthY - 0.5 * GROWTH_HEIGHT;
		zeroForceHt = forceY - 0.5 * FORCE_HEIGHT;
		
		// display price ruler
		Vector scaleList = getPriceRulerList(minP, maxP);
		String displayP = "";
		double runP = 0;
		double displayHt = 0;
		double leftTextWidth = 0;
		
		for (int i = 0; i < scaleList.size(); i ++) {
			displayP = (String) scaleList.get(i);
			runP = (new Double(displayP)).doubleValue();
			displayHt = origY - PRICE_BOTTOM - (double) yStep * (Math.log(runP) - minPLog) ;
		
			if (i == scaleList.size() - 1) {
				leftTextWidth = (double) fontMetrics.stringWidth(displayP);
			}
			
			// debug
			//System.out.println("draw: " + runP);
				
			// draw a line
			pad.setColor(colorDark);
			pad.drawLine(origX, (int) displayHt, imageWidth - X_RIGHT, (int) displayHt);
			
			// draw text - do not display if it is out of box
			if (displayHt - SMALL_GAP - fontMetrics.getHeight() >= origY - chartHeight) {
				pad.setColor(colorRed);
				pad.drawString(displayP, origX + SMALL_GAP, (int) (displayHt - SMALL_GAP));
			}
		}
		
		leftScaleGap = (int) leftTextWidth + SMALL_GAP + SMALL_GAP;
		double xStep   = (double) (chartWidth - leftScaleGap) / totalItems;
		double xx = origX + leftScaleGap;
		int barrulerStep = 0;
		if (priceList.size() - startIndex < 150) {
			barrulerStep = 3;
		} else if (priceList.size() - startIndex < 280) {
			barrulerStep = 6;
		} else {
			barrulerStep = 12;
		}
		
		for (int i = startIndex; i < priceList.size(); i ++, xx += xStep) {
			StockDailyInfo daily = (StockDailyInfo) priceList.get(i);
			if (daily.getOpen() <= 0 || daily.getClose() <= 0 ||
				daily.getMax() <= 0 || daily.getMin() <= 0) {
				LOGGER.warn("Error: some zero in the value for " + stock.getTicker());
				continue;
			}
			
			// study line
			if ((i - startIndex) % barrulerStep == 0) {
				pad.setColor(colorDark);

				// draw the vertical lines, but leave some gap on the left
				pad.drawLine((int)xx, forceY, (int) xx, (int) origY - chartHeight);
				
				if (leftLinePos == 0) {
					leftLinePos = xx;
				}
			}
			
			// figure out the month string
			if (prevMonthNum.equals("")) {
				prevMonthNum = getMonthNumber(daily.getMDate());
			} else {
				String thisMonthNum = getMonthNumber(daily.getMDate());
				if (!prevMonthNum.equals(thisMonthNum)) {
					monthList.add(String.valueOf(xx) + "#" + DateUtil.getShortMonthString(thisMonthNum));
				}
				
				prevMonthNum = thisMonthNum;
			}
			
			// find the positions
			yyOpen 	= origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getOpen()) - minPLog) ;
			yyClose = origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getClose()) - minPLog);
			yyMin 	= origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getMin()) - minPLog);
			yyMax 	= origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getMax()) - minPLog);			

			if (yyOpen >= yyClose) {
				pad.setColor(colorGreen);
			} else {
				pad.setColor(colorRed);
			}
			
			// draw volume			
			volumeHt =  (double) daily.getVolume() * volumeStep;
			pad.drawLine((int)xx, (int) volumeY, (int) xx, (int) (volumeY - volumeHt));			
			
			// draw bar chart
			pad.drawLine((int)xx, (int) yyMin, (int) xx, (int) yyMax);
			pad.drawLine((int)xx, (int) yyOpen, (int) (xx - BAR_WIDTH), (int) yyOpen);
			pad.drawLine((int)xx, (int) yyClose, (int) (xx + BAR_WIDTH), (int) yyClose);				
			
			// System.out.println("xx: " + xx + ", yyOpen: " + yyOpen);
			
			// draw the growth rate
			growthHt =  (double) daily.getSlope() * growthStep;
			
			/*
			if ((daily.getSlope() >= 0 && daily.getAcceleration() <= 0)  ||
				(daily.getSlope() <= 0 && daily.getAcceleration() >= 0)) {
				pad.setColor(colorBlue);	
				pad.fillOval((int)xx - 2, (int) (growthY + 2), 4, 4);
				//pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep + 1, (int) (growthHt - zeroGrowthHt));
			}  */
			
			// growth line
			if (daily.getSlope() > 0) {
				pad.setColor(colorGreen);	
				//pad.fillRect((int)xx, (int) (growthY - growthHt), (int) xStep + 1, (int) (growthHt - zeroGrowthHt));
				//pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep + 1, (int) (growthHt - zeroGrowthHt));
			} else {
				pad.setColor(colorRed);
				//pad.fillRect((int)xx, (int) (growthY - zeroGrowthHt), (int) xStep + 1, (int) (zeroGrowthHt - growthHt));
				//pad.drawRect((int)xx, (int) (growthY - zeroGrowthHt), (int) xStep + 1, (int) (zeroGrowthHt - growthHt));
			}
			
			pad.drawLine((int)xx, (int) zeroGrowthHt, (int) xx, (int) (zeroGrowthHt - growthHt));
			
			
			// force line
			forceHt =  (double) daily.getAcceleration() * forceStep;
			if (daily.getAcceleration() > 0) {
				pad.setColor(colorGreen);	
			} else {
				pad.setColor(colorRed);
			}
			
			pad.drawLine((int)xx, (int) zeroForceHt, (int) xx, (int) (zeroForceHt - forceHt));
			
			/*
			// rs line - from zero to the value
			rsHt =  (double) (daily.getRelativeStrength() - minRS) * rsStep;
			if (rsHt > zeroRSHt) {
				pad.setColor(colorGreen);	
			} else {
				pad.setColor(colorRed);
			}
			
			pad.drawLine((int)xx, (int) (rsY - zeroRSHt), (int) xx, (int) (rsY - rsHt));
			*/
			
			xPolylineGrowth[totalPoints]  	= (int) xx;
			yPolylineGrowth[totalPoints]  	= (int) (zeroGrowthHt - growthHt);
			xPolylineForce[totalPoints]  	= (int) xx;
			yPolylineForce[totalPoints] 	= (int) (zeroForceHt - forceHt);
			if (doRSI) {
				xPolylineRS[totalPoints]  		= (int) xx;
				yPolylineRS[totalPoints] 		= (int) (rsY - daily.getRsi() * rsStep);
			}
			totalPoints++;
			
			
			double trendHt = forceY + BOX_GAP;
			double marketTrendHt = trendHt + BOX_GAP;
			if (xx - 3 > ballLeftStart) {
				if (daily.getTrendColor() != null && daily.getTrendColor().equals(TrendTypes.GREEN)) {
					// draw green
					pad.setColor(colorGreen);
					pad.fillOval((int) xx - 2, (int) trendHt - 2, 4, 4);
				} else if (daily.getTrendColor() != null && daily.getTrendColor().equals(TrendTypes.RED)) {
					// do RED
					pad.setColor(colorRed);
					pad.fillOval((int) xx - 2, (int) trendHt - 2, 4, 4);	
					
				} else {
					// do nothing
					//pad.setColor(colorBlue);
					//pad.drawLine((int) xx - 1, (int) ballHt, (int) xx + 1, (int) ballHt);
				} 
			
				// draw market flag list
				if (marketFlagList != null && marketFlagList.size() >= (priceList.size() - startIndex)) {
					
					String marketFlag = (String) marketFlagList.get(marketFlagList.size() - priceList.size() + i);
					if (marketFlag != null && marketFlag.equals(TrendTypes.GREEN)) {
						// draw green
						pad.setColor(colorGreen);
						pad.fillOval((int) xx - 2, (int) marketTrendHt - 2, 4, 4);
					} else if (marketFlag != null && marketFlag.equals(TrendTypes.RED)) {
						// do RED
						pad.setColor(colorRed);
						pad.fillOval((int) xx - 2, (int) marketTrendHt - 2, 4, 4);	
					}  else if (marketFlag != null && marketFlag.equals(TrendTypes.YELLOW)) {
						// do RED
						pad.setColor(colorDYellow);
						pad.fillOval((int) xx - 2, (int) marketTrendHt - 2, 4, 4);	
					} else {
						// do nothing
						//pad.setColor(colorBlue);
						//pad.drawLine((int) xx - 1, (int) marketBallHt, (int) xx + 1, (int) marketBallHt);
					}
				} 
				
			}

			
			// DRAW THE TREND LINES
			/*
			if (i - startIndex + 1 >= TrendService.TREND_LENGTH) {
				pad.setColor(colorDark);
				drawLine(daily.getTrendHX1(), daily.getTrendHY1(), daily.getTrendHX2(), daily.getTrendHY2(), 
										origX, origY, xStep, yStep, minPLog);
				drawLine(daily.getTrendLX1(), daily.getTrendLY1(), daily.getTrendLX2(), daily.getTrendLY2(), 
							origX, origY, xStep, yStep, minPLog);
			} */
			
			// average trend line
			/*
			pad.setColor(colorBlue);
			double yyHt = origY - PRICE_BOTTOM - (double) yStep * (daily.getTrendYAvg2() - minPLog) ;
			pad.fillOval((int) xx - 2, (int) yyHt - 2, 4, 4);
			*/
			
			// construct the trend line
			/*
			double trendYAvgAdj = 0;
			if (daily.getTrendYAvg() <= maxPLog && daily.getTrendYAvg() >= minPLog) {
				trendYAvgAdj = daily.getTrendYAvg();
			} else if (daily.getTrendYAvg() > maxPLog) {	
				trendYAvgAdj = maxPLog;				
			} else {
				trendYAvgAdj = minPLog;	
			} 
			
			xPolylineTrend[totalPoints2] = (int) xx;
			yPolylineTrend[totalPoints2] = (int) (origY - PRICE_BOTTOM - (double) yStep * (trendYAvgAdj - minPLog));
			totalPoints2++;
			*/
			
			if (doEMA50) {
				if (daily.getAverage50Log() <= maxPLog && daily.getAverage50Log() >= minPLog) {				
					xPolylineAvg50[totalPointsAvg50] = (int) xx;
					yPolylineAvg50[totalPointsAvg50] = (int) (origY - PRICE_BOTTOM - (double) yStep * (daily.getAverage50Log() - minPLog));
					totalPointsAvg50++;
				}
			}
			
			if (daily.getAverage20Log() <= maxPLog && daily.getAverage20Log() >= minPLog) {				
				xPolylineAvg21[totalPointsAvg21] = (int) xx;
				yPolylineAvg21[totalPointsAvg21] = (int) (origY - PRICE_BOTTOM - (double) yStep * (daily.getAverage20Log() - minPLog));
				totalPointsAvg21++;
			}
			
			/*
			if (doEMA9) {
				if (daily.getAverage9Log() <= maxPLog && daily.getAverage9Log() >= minPLog) {				
					xPolylineAvg9[totalPointsAvg9] = (int) xx;
					yPolylineAvg9[totalPointsAvg9] = (int) (origY - PRICE_BOTTOM - (double) yStep * (daily.getAverage9Log() - minPLog));
					totalPointsAvg9++;
				}
			} */
		}
		
		
		// display average volume - line
		pad.setColor(colorDark);
		volumeHt =  (double) stockExt.getAverageVolume() * volumeStep;
		int volumeRealHt = (int) (volumeY - volumeHt);
		pad.drawLine(origX, volumeRealHt, imageWidth - X_RIGHT, volumeRealHt);
		
		// display max volume
		// backgroud
		String volumeStr = getMaxVolumeString();
		int volumeStrLen = (int) (fontMetrics.stringWidth(volumeStr));
		int volumeStrY = volumeY - VOLUME_HEIGHT + SMALL_GAP + TEXT_HEIGHT;
		pad.setColor(chartColor);
		pad.fillRect(origX + SMALL_GAP, volumeStrY - TEXT_HEIGHT + 3, volumeStrLen + 8, TEXT_HEIGHT);
		pad.setColor(colorBlack);
		pad.drawRect(origX + SMALL_GAP, volumeStrY - TEXT_HEIGHT + 3, volumeStrLen + 8, TEXT_HEIGHT);
		
		pad.setColor(colorBlue);
		pad.drawString(volumeStr, origX + SMALL_GAP + SMALL_GAP, volumeStrY);
	
		// draw 0 growth line / force line
		pad.setColor(colorDark);
		pad.drawLine(origX, (int) zeroGrowthHt, imageWidth - X_RIGHT, (int) zeroGrowthHt);
		pad.drawLine(origX, (int) zeroForceHt, imageWidth - X_RIGHT, (int) zeroForceHt);

		
		// draw low, high, zero RSI lines
		if (doRSI) {
			Stroke savedStroke = pad.getStroke();
			pad.setStroke(dashStroke);
			pad.setColor(colorRed);
			pad.drawLine(origX, (int) (rsY - lowRSHt),  imageWidth - X_RIGHT, (int) (rsY - lowRSHt));
			pad.drawLine(origX, (int) (rsY - highRSHt), imageWidth - X_RIGHT, (int) (rsY - highRSHt));
			
			pad.setColor(colorGreen);
			pad.drawLine(origX, (int) (rsY - zeroRSHt), imageWidth - X_RIGHT, (int) (rsY - zeroRSHt));
			pad.setStroke(savedStroke);
		}
		
		// draw polyline
		if (totalPoints > 0) {
			
			pad.setColor(colorDark);
			pad.drawPolyline(xPolylineGrowth, yPolylineGrowth, totalPoints);
			pad.drawPolyline(xPolylineForce, yPolylineForce, totalPoints);
			
			if (doRSI) {
				pad.setColor(colorBlue);
				pad.drawPolyline(xPolylineRS, yPolylineRS, totalPoints);
			}
		}
		
		// draw trend line
		//pad.setColor(colorLtBlue);
		//pad.drawPolyline(xPolylineTrend, yPolylineTrend, totalPoints2);
		
		Stroke savedStroke = pad.getStroke();
		pad.setStroke(thickStroke);
					
		// draw 21-day line
		pad.setColor(colorBlue);
		pad.drawPolyline(xPolylineAvg21, yPolylineAvg21, totalPointsAvg21);
		
		if (doEMA20) {
			pad.setColor(colorLtBlue);
			pad.drawPolyline(xPolylineAvg9, yPolylineAvg9, totalPointsAvg9);
		}
					
		if (doEMA50) {
			// draw 50-day line
			pad.setColor(colorRed);
			pad.drawPolyline(xPolylineAvg50, yPolylineAvg50, totalPointsAvg50);
		}
		
		pad.setStroke(savedStroke);
		
		
		
		// draw string "50Day EMA"
		String emaStr  = RangeService.RANGE_20 + "-Day EMA";
		String emaStr2  = RangeService.RANGE_50 + "-Day EMA";
		String emaStr3 = RangeService.RANGE_100 + "-Day EMA";
		int strLen = (int) (fontMetrics.stringWidth(emaStr));
		int strLen2 = (int) (fontMetrics.stringWidth(emaStr2));
		int strLen3 = (int) (fontMetrics.stringWidth(emaStr3));
		
		int emaStrY = origY - chartHeight + TEXT_HEIGHT;
		int emaStrX = (int) (leftLinePos + 5 * SMALL_GAP);
		int totalLen = 0;
		
		totalLen = (int) (strLen2 + 22);
		if (doEMA20) {
			totalLen += (int) (strLen + 16);
		} 
				
		if (doEMA50) {
			totalLen += (int) (strLen3 + 16);
		} 
		
		// backgroud
		pad.setColor(chartColor);
		pad.fillRect(emaStrX - 18, emaStrY - 12, totalLen, 15);
		pad.setColor(colorBlack);
		pad.drawRect(emaStrX - 18, emaStrY - 12, totalLen, 15);
		
		// 9-ema
		if (doEMA20) {
			pad.setColor(colorLtBlue);
			pad.fillRect(emaStrX - 12, emaStrY - 7, 6, 6);
			pad.drawString(emaStr, emaStrX, emaStrY);
			emaStrX += strLen + 16;
		}
		
		// 21-ema
		pad.setColor(colorBlue);
		pad.fillRect(emaStrX - 12, emaStrY - 7, 6, 6);
		pad.drawString(emaStr2, emaStrX, emaStrY);
		
		if (doEMA50) {
			// 50-ema
			pad.setColor(colorRed);
			emaStrX += strLen2 + 16;
			pad.fillRect(emaStrX - 12, emaStrY - 7, 6, 6);
			pad.drawString(emaStr3, emaStrX, emaStrY);
		}
			
		// draw month ruler
		displayMonthScale(monthList);
		
		// draw the linear model line
		/*
		double y1 = stockExt.getSlope() + stockExt.getYCross();
		double y2 = stockExt.getSlope() * priceList.size() + stockExt.getYCross();
		double y1Loc = origY - PRICE_BOTTOM - (double) yStep * (y1 - minPLog);
		double y2Loc = origY - PRICE_BOTTOM - (double) yStep * (y2 - minPLog);
		pad.setColor(colorYellow);
		pad.drawLine((int) (origX + xStep), (int) y1Loc, (int) (origX + chartWidth - xStep), (int) y2Loc);
		*/
			
		// draw the trend line - TWO MAJOR TREND LINES IN THE PAST 35 DAYS
		/*
		savedStroke = pad.getStroke();
		pad.setStroke(dashStroke);
		int delta = priceList.size() - TREND_LINE_LEN - startIndex;
		pad.setColor(colorBlue);		
		drawLine(delta + stockExt.getTrendHX1(), stockExt.getTrendHY1(), delta + stockExt.getTrendHX2(), stockExt.getTrendHY2(), 
			origX, origY, xStep, yStep, minPLog);
		
		// low trend line
		drawLine(delta + stockExt.getTrendLX1(), stockExt.getTrendLY1(), delta + stockExt.getTrendLX2(), stockExt.getTrendLY2(), 
			origX, origY, xStep, yStep, minPLog);
		
		pad.setStroke(savedStroke);
		*/
		

	}
	
	private void displayMonthScale(Vector monthList) {
		//// display the months
		pad.setColor(colorBlue);
		for (int i = 0; i < monthList.size(); i ++) {
			String monthPosStr = (String) monthList.get(i);
			Vector list = StringHelper.mySplit(monthPosStr, "#");
			if (list != null && list.size() == 2) {
				double xxP = (new Double((String) list.get(0))).doubleValue();
				String monthStr = (String) list.get(1);
				
				pad.drawLine((int)xxP, (int) origY, 
					(int) xxP, (int) (origY - SCALE_LENGTH));
				double halfStrLen = (double) fontMetrics.stringWidth(monthStr) / 2;
				
				// draw the string, only if it fits insider of the box.
				double xxPLoc = 0;
				boolean printIt = true;
				if (xxP - halfStrLen >= origX && xxP + halfStrLen <= origX + chartWidth) {
					xxPLoc = xxP - halfStrLen;
				} else if (xxP - halfStrLen < origX) {
					xxPLoc = xxP;
					if (monthList.size() > 20) {
						printIt = false;
					}
				} else {
					xxPLoc = xxP - 2 * halfStrLen;
					if (monthList.size() > 20) {
						printIt = false;
					}
				}
				
				if (printIt) {
					pad.drawString(monthStr, (int) xxPLoc, (int) origY - SMALL_GAP);
				}
		
			}
		}
	}
	
	private void drawLine(int ax, double ay, int bx, double by, 
		int origX, int origY, double xStep, double yStep,
		double minPLog) {
		double x1Loc = origX + (ax + 1) * xStep + leftScaleGap;
		double x2Loc = origX + (bx + 1) * xStep + leftScaleGap;
		double y1Loc = origY - PRICE_BOTTOM - (double) yStep * (ay - minPLog);
		double y2Loc = origY - PRICE_BOTTOM - (double) yStep * (by - minPLog);
		pad.drawLine((int) x1Loc, (int) y1Loc, (int) x2Loc, (int) y2Loc);
	}
	
	/**
	 * Generate sequence of prices to be displayed on the y-axis.
	 * @param minP
	 * @param maxP
	 * @return
	 */
	private Vector getPriceRulerList(double minP, double maxP) {
		Vector list = new Vector();
	
		// System.out.println("min/max: " + minP + "/" + maxP);	
		double range = maxP - minP;
		if (range == 0) {
			range = 1;
		}
		
		double dispStep = 0;
		double initP = 0;
		DecimalFormat formatter = roundFormatter;
		
		if (range < 1) {
			dispStep = 0.1;
			formatter = oneDigitFormatter;
		} else if (range < 5) {
			dispStep = 0.5;
			formatter = oneDigitFormatter;
		} else if (range < 10) {
			dispStep = 1;
		} else if (range < 20) {
			dispStep = 2;
		} else if (range < 30) {
			dispStep = 3;
		} else if (range < 50) {
			dispStep = 5;
		} else if (range < 120) {
			dispStep = 10;
		} else if (range < 500) {
			dispStep = 50;
		} else if (range < 1000) {
			dispStep = 100;
		} else {
			dispStep = 200;
		}
		
		if (minP < 1) {
			initP = Math.round(minP * 100 - 20) / 100;
		} else if (minP < 30) {
			initP = Math.round(minP);
		} else if (minP < 100) {
			initP = Math.round(minP / 5 - 1) * 5;
		} else if (minP < 500) {
			initP = Math.round(minP / 50 - 1) * 50;
		} else if (minP < 1000) {
			initP = Math.round(minP / 100 - 1) * 100;
		}  else {
			initP =  Math.round(minP / 200 - 1) * 200;
		} 
		
		double runP = initP;
		String runPString = "";
		do {
			if (runP >= minP) {
				runPString = formatter.format(runP);
				list.add(new String(runPString));
			}
			
			runP += dispStep;
		} while (runP < maxP);
		
		return list;
	}
	
	/**
	 * Find min/max of the slope and acceleration.
	 * Also convert the slope to percentage.
	 * And reset acceleration as the force.
	 */
	private void findMinMaxGrowthForceRS() {		
		for (int i = startIndex; i < priceList.size(); i ++) {
			StockDailyInfo daily = (StockDailyInfo) priceList.get(i);
			double theForce = daily.getAcceleration();
			double theSlope = daily.getSlope();
									
			if (theSlope < 0) {
				theSlope = - theSlope;
			}
			
			if (theForce < 0) {
				theForce = - theForce;
			}
			
			if (i == startIndex) {
				maxGrowth = theSlope;
				maxForce = theForce;
			} else {
				if (maxGrowth < theSlope) {
					maxGrowth = theSlope;
				}
				
				if (maxForce < theForce) {
					maxForce = theForce;
				}
			}
			
			
		}
		
		// make sure positive
		if (maxGrowth == 0) {
			maxGrowth = 1;
		}
				
		if (maxForce == 0) {
			maxForce = 1;
		}
		
	}
	
	/**
	 * mDate format: YYYY/MM/DD
	 * @param mDate
	 * @return
	 */
	private String getMonthNumber(String mDate) {
		if (mDate == null) {
			return "";
		}
		
		Vector list = StringHelper.mySplit(mDate, "/");
		if (list != null && list.size() > 2) {
			return (String) list.get(1);
		}
		
		return "";
	}
	
	/**
	 * Find the max volume to be displayed.
	 */	
	private String getMaxVolumeString() {
		String ret = formatVolume(stockExt.getMaxVolume());
		ret += " (Average = " + formatVolume(stockExt.getAverageVolume()) + ")";
		
		return ret;
	}
	
	/** format the volume */
	private String formatVolume(double volume) {
		String displayStr = "";
		if (volume <= 1000) {
			displayStr = roundFormatter.format( volume );
		} else if (volume > 1000 && volume < 1000000){
			displayStr = String.valueOf( (int) ((double) volume / 1000)) + "K";
		} else {
			// million
			displayStr = String.valueOf( (int) ((double) volume / 1000000)) + "M";
		}
		
		return displayStr;
	}
	
	/**
	 * Find the price list for the range.
	 * @throws Exception
	 */
	private void prepareStockInfo() {
		int EXTRA_DAYS = 100;
		DBComm database = new DBComm();
		StockAdminDAO stockDao = new StockAdminDAO();
		
		try {	
			database.openConnection();
			stockDao.setDatabase(database);
			
			if (stockId != null) {
				stock = stockDao.getStockFromDB(stockId);
			} else {
				Vector stkList = stockDao.getStockListFromDB(null, ticker, null);
				if (stkList != null && stkList.size() > 0) {
					stock = (Stock) stkList.get(0);
					stockId = stock.getStockId();
				}
			}
			
			// try to get service context from AppContext
			ServiceContext svrContext = null;
			boolean serviceDone = false;
			
			if (serviceId != null && !serviceId.equals("")) {
				svrContext = null; // AppContext.popServiceContext(serviceId);
				if (svrContext != null) {
					priceList = svrContext.getPriceList();
					startIndex = svrContext.getStartIndex();
					stockExt = svrContext.getStockExt();
					serviceDone = true;
				}
			}
			
			if (!serviceDone) {
				svrContext = new ServiceContext();
				String startDate = OracleUtil.getPrevDateInOracleFormat(null, period, EXTRA_DAYS);
				String startDateInReal = DateUtil.getDateStringInYYYYMMDDFormat(new Date(), period);
				String endDate = OracleUtil.getDateInOracleFormat(null);
				
				priceList = stockDao.getStockDailyPriceListFromDB(stockId, startDate, endDate);	
				startIndex = ScanUtil.findStartIndex(priceList, startDateInReal);
				
				svrContext.setStartIndex(startIndex);
				stockExt = new StockExt();
				stockExt.setStock(stock);
				svrContext.setStockExt(stockExt);
				svrContext.setPriceList(priceList);
				
				//System.out.println("called service in chart");
			} else {
				//System.out.println("No calls for service in chart");
			}
			
			if (priceList != null && priceList.size() > 0) {
				LinearModelService linearSvr = new LinearModelService();
				TrendService trendService = new TrendService();
				RangeService rangeService = new RangeService();
				
				if (!serviceDone) {
					// find slope/yCross
					//linearSvr.findSlopeAndRSquare(svrContext);
					// find min/max price, and max volume
					
					rangeService.findMinMax(svrContext);
					rangeService.findAverage20(svrContext);   // 21-day average
					// svr.findGainLossIndex(svrContext);
					
					////rangeService.findAccumulationForceForAll(svrContext);
					rangeService.findAverage50(svrContext);

				}
				
				rangeService.findAccumulationForceForAll(svrContext);
				
				if (doEMA20) {
					//rangeService.findAverage20(svrContext);
				}
								
				// find all slope/acceleration for any points after startIndex
				//	do the trend line service
				//trendService.findBothTrendLines(svrContext);
				//trendService.findTrendLinesForAll(svrContext);
							 
				//svrContext.setStartIndex(startIndex - LinearModelService.ACCELERATION_RANGE);
				//linearSvr.findSlopeForAll(svrContext);
				//svrContext.setStartIndex(startIndex);
				//linearSvr.findAccelerationForAll(svrContext);
				
				if (doRSI) {
					RSIService rsiService = new RSIService();
					rsiService.findRSI(svrContext);
				}				
				
				// two trend lines
				/*
				TREND_LINE_LEN = (priceList.size() - startIndex) / 2;					
				svrContext.setStartIndex(priceList.size() - TREND_LINE_LEN);
				svrContext.setEndIndex(priceList.size() - TREND_LINE_END - 1);
				trendService.findBothTrendLines(svrContext);
				*/				
			}									
		} catch (Exception e) {
			e.printStackTrace();
			priceList = null;
			stock = null;
			LOGGER.warn("Error in loading stock price info in DrawBasicChart: " + e.getMessage());
		}
		
		try {
			database.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}
					
	}

	/**
	 * @return
	 */
	public String getStockId() {
		return stockId;
	}


	/**
	 * @param string
	 */
	public void setStockId(String string) {
		stockId = string;
	}

	/**
	 * @return
	 */
	public int getPeriod() {
		return period;
	}

	/**
	 * @param i
	 */
	public void setPeriod(int i) {
		period = i;
	}

	/**
	 * @return
	 */
	public String getOption() {
		return option;
	}

	/**
	 * @param string
	 */
	public void setOption(String string) {
		option = string;
	}

	/**
	 * @return
	 */
	public String getType() {
		return type;
	}

	/**
	 * @param string
	 */
	public void setType(String string) {
		type = string;
	}

	/**
	 * @return
	 */
	public String getServiceId() {
		return serviceId;
	}

	/**
	 * @param string
	 */
	public void setServiceId(String string) {
		serviceId = string;
	}

	/**
	 * @return
	 */
	public String getTicker() {
		return ticker;
	}

	/**
	 * @param string
	 */
	public void setTicker(String string) {
		ticker = string;
	}

}
