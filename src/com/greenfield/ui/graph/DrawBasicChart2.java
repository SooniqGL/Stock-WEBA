package com.greenfield.ui.graph;

/*
 * Created on 11/6/2019
 */

import java.text.DecimalFormat;
//import java.text.NumberFormat;
import java.util.Date;
//import java.util.Enumeration;
import java.util.Vector;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
//import java.awt.GradientPaint;
import java.awt.Stroke;
import java.awt.geom.AffineTransform;
//import java.sql.Timestamp;

import com.greenfield.common.service.RangeService;
import com.greenfield.common.service.ServiceContext;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.StringHelper;
//import com.greenfield.ui.common.constant.TrendTypes;
//import com.greenfield.ui.common.util.OracleUtil;
//import com.greenfield.ui.common.util.StockUtil;
//import com.greenfield.ui.handler.scan.ScanUtil;
//import com.greenfield.common.base.AppContext;
import com.greenfield.ui.cache.DBCachePool;
import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.ui.cache.ScanCachePool;
import com.greenfield.common.util.StockUtil;
//import com.greenfield.ui.common.util.DumpObject;
import com.greenfield.common.constant.ColorCodes;
import com.greenfield.common.constant.TrendTypes;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.object.stock.MarketIndicators;
import com.greenfield.common.object.stock.Stock;
import com.greenfield.common.object.stock.StockDailyInfo;
import com.greenfield.common.object.stock.StockExt;

import java.text.SimpleDateFormat;
import java.util.HashMap;

import org.apache.log4j.Logger;

/**
 * @author UZHANQX
 *
 *         Draw the basic Chart.
 */
public class DrawBasicChart2 extends BaseGraphics {
	private static final Logger LOGGER = Logger.getLogger(DrawBasicChart2.class);

	// which time to draw
	public final static int DEFAULT_PERIOD = 6; // six months chart

	// LIMIT FOR RSI
	private final static int LOW_RSI_LIMIT = 25;
	private final static int HIGH_RSI_LIMIT = 75;
	private final static int MID_RSI_LIMIT = 50;

	// color flag
	public final static String GREEN = "G";
	public final static String RED = "R";
	public final static String GRAY = "Gr";

	// option values
	public final static String DEFAULT_OPTION = "N";
	public final static String SMALL_TYPE = "small";
	public final static String REGULAR_TYPE = "regular";
	public final static int DAYS_IN_MONTH = 22;
	public final static String REGULAR_COPYRIGHT = "Copyright 2014, Sooniq Technology Corporation";
	public final static String SHORT_COPYRIGHT = "Copyright 2014, Sooniq Tech.";

	private int TREND_LINE_LEN = 0; // half-data
	public final static int TREND_LINE_END = 2;

	// variables for input and stock related.
	private String stockId;
	private String ticker;
	private int period;
	private String option;
	private String weekly; // three values: O, N, Y. If it is O, use period to
							// define whether do weekly or daily
	private String type;
	private Stock stock;
	private Vector priceList;
	private StockExt stockExt;
	private int startIndex;
	private int ballLeftStart;
	private int leftScaleGap;
	private double leftLinePos = 0;
	private double leftTextWidth = 0;
	private String displayDate;
	private Date lastDate;
	private boolean hasVolumeError = false;

	private HashMap scanDateListHash = null;

	private boolean doRSI;
	private boolean doEMA20;
	private boolean doEMA50;
	private boolean doEMA100;
	private boolean doZone;
	private boolean doBARS;

	private double maxGrowth;
	private double maxForce;
	// private double minRS;
	// private double maxRS;

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
	private Color colorLtGreen;
	private Color colorDZone;
	private Color colorYZone;
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
	public static final int DEFAULT_IMAGE_WIDTH = 680;
	public static final int DEFAULT_IMAGE_HEIGHT = 400;
	public static final int DEFAULT_IMAGE_HEIGHT_WITH_OPTION = 400;
	public static final int SMALL_IMAGE_WIDTH = 250;
	public static final int SMALL_IMAGE_HEIGHT = 300;
	public static final int SMALL_IMAGE_HEIGHT_WITH_OPTION = 340;

	private int X_LEFT = 25;
	private int X_RIGHT = 8;
	private int Y_TOP = 8;
	private int Y_BOTTOM = 8;
	private int VOLUME_HEIGHT = 40; // for volume box
	private int GROWTH_HEIGHT = 30; // for GROWTH box
	private int FORCE_HEIGHT = 30; // for FORCE box
	private int RS_HEIGHT = 30;
	private int TITLE_HEIGHT = 20;
	private int BOX_GAP = 8;
	private int SCALE_LENGTH = 3; // bars in y axis
	private int BAR_WIDTH = 2; // bar width in price chart
	private int PRICE_TOP = 20; // draw above this height
	private int PRICE_BOTTOM = 15; // draw above this height
	private int TEXT_HEIGHT = 15;
	private int VERTICAL_TEXT_X1 = 17; // price
	private int SMALL_GAP = 5;
	private int TINY_GAP = 2;

	public void init() {
		// System.out.println("java version: " +
		// System.getProperty("java.version") );
		colorBlack = Color.black;
		colorWhite = Color.white;
		colorRed = Color.red;
		colorGreen = Color.green;
		colorBlue = Color.blue;
		colorYellow = Color.yellow;
		colorLtYellow = new Color(180, 180, 180);
		colorDYellow = new Color(230, 230, 0);
		colorLtGreen = new Color(30, 100, 0);
		colorDark = new Color(200, 200, 200);
		colorPink = new Color(255, 0, 255);
		imageColor = new Color(223, 223, 223);
		chartColor = new Color(255, 255, 245);

		colorYZone = new Color(255, 255, 200);
		colorDZone = new Color(225, 225, 225);

		// A dashed stroke
		float strokeThickness = 1.0f;
		float miterLimit = 10f;
		float[] dashPattern = { 10f };
		float dashPhase = 5f;
		dashStroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, miterLimit,
				dashPattern, dashPhase);

		// for the MA lines
		thickStroke = new BasicStroke(1.3f, BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);
	}

	/** test */
	public static void main(String[] argv) {
		try {
			// debug
			// System.out.println("in drawicon ...");
			String fileName = "E:\\temp\\good.gif";

			DrawBasicChart2 draw = new DrawBasicChart2();
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
		doRSI = false; // hard coded here
		if (option != null && option.length() > 0 && option.charAt(0) == 'Y') {
			doEMA20 = true;
		} else {
			doEMA20 = false;
		}

		if (option != null && option.length() > 1 && option.charAt(1) == 'Y') {
			doEMA50 = true;
		} else {
			doEMA50 = false;
		}

		if (option != null && option.length() > 2 && option.charAt(2) == 'Y') {
			doEMA100 = true;
		} else {
			doEMA100 = false;
		}

		if (option != null && option.length() > 3 && option.charAt(3) == 'N') {
			doZone = false;
		} else {
			doZone = true;
		}

		if (option != null && option.length() > 4 && option.charAt(4) == 'N') {
			doBARS = false;
		} else {
			doBARS = true;
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
			forceY = imageHeight - Y_BOTTOM - 2 * BOX_GAP;
			growthY = forceY - FORCE_HEIGHT - BOX_GAP;
			rsY = growthY - GROWTH_HEIGHT - BOX_GAP;
			volumeY = rsY - RS_HEIGHT - BOX_GAP;
			gfHeight = FORCE_HEIGHT + GROWTH_HEIGHT + RS_HEIGHT + 5 * BOX_GAP;
		} else {
			forceY = imageHeight - Y_BOTTOM; // - 2 * BOX_GAP;
			growthY = forceY - FORCE_HEIGHT - BOX_GAP;
			volumeY = growthY - GROWTH_HEIGHT - BOX_GAP;
			gfHeight = FORCE_HEIGHT + GROWTH_HEIGHT + 2 * BOX_GAP;
		}

		// calculate measures
		chartWidth = imageWidth - X_LEFT - X_RIGHT;
		chartHeight = imageHeight - Y_TOP - TITLE_HEIGHT - BOX_GAP - VOLUME_HEIGHT - Y_BOTTOM - gfHeight;
		origX = X_LEFT;
		origY = volumeY - VOLUME_HEIGHT - BOX_GAP;

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
		 * bigFont = new Font("TimesRoman", Font.BOLD, 24); midFont = new
		 * Font("TimesRoman", Font.BOLD, 14); g.setFont(font);
		 */

		// use black as default
		// draw circle
		// Color tColor = g.getColor();
		// g.setColor(colorRed);
		// g.fillOval(startX + SQUARE_SIZE / 4 - 2, startY + SQUARE_SIZE / 4 -
		// 2, 5, 5);
		// g.setColor(tColor);
		// int line_height = (int) Math.sqrt(radius * radius - half_radius *
		// half_radius) + 1;
		// int arcStartDegree = 0;
		// int arcDegree = 90;
		// g.fillArc(startX, startY, SQUARE_SIZE, SQUARE_SIZE, arcStartDegree,
		// arcDegree);

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
		pad.fillRect(origX, forceY - FORCE_HEIGHT, chartWidth, FORCE_HEIGHT);
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
		// GradientPaint gp = new GradientPaint(50.0f, 50.0f, colorBlue,
		// 50.0f, 250.0f, colorGreen, true);
		// pad.setPaint(gp);

		String currentDate = "";
		try {
			// do not use static formatter!
			SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat("MMM dd, yyyy");
			currentDate = MMM_DD_YYYY_formatter.format(lastDate);
		} catch (Exception e) {
			LOGGER.warn("Date format error:" + e.getMessage());
			currentDate = DateUtil.getDateStringInLongFormat(null);
		}
		
		// draw title
		savedColor = pad.getColor();
		pad.setColor(colorBlue);
		String title = "";
		/*
		if (type != null && type.equals(SMALL_TYPE)) {
			title = "Chart for " + stock.getCompanyName() + " (" + stock.getTicker() + "), " + SHORT_COPYRIGHT;
		} else {
			title = "Chart for " + stock.getCompanyName() + " (" + stock.getTicker() + "), " + REGULAR_COPYRIGHT;
		} */

		title = "Chart for " + stock.getCompanyName() + " (" + stock.getTicker() + ") - " + currentDate;
		
		pad.drawString(title, origX, Y_TOP + TEXT_HEIGHT);
		pad.setColor(savedColor);

		// This set will print vertical strings on screen
		AffineTransform at = new AffineTransform();
		at.setToRotation(1.5 * Math.PI); // same as: at.rotate(1.5 * Math.PI);
		pad.transform(at);
		pad.setColor(colorBlue);
		pad.drawString("Volume", -volumeY, VERTICAL_TEXT_X1);

		// pad.drawString("Daily Price Chart - " + currentDate, -origY + BOX_GAP, VERTICAL_TEXT_X1);
		pad.drawString("Price Chart", -origY + BOX_GAP, VERTICAL_TEXT_X1);
		
		// here we draw MACD
		// pad.drawString("Grow", -growthY, VERTICAL_TEXT_X1);
		pad.drawString("MACD", -growthY, VERTICAL_TEXT_X1);
		
		
		pad.drawString("Force", -forceY, VERTICAL_TEXT_X1);
		if (doRSI) {
			pad.drawString("RSI", -rsY + SMALL_GAP, VERTICAL_TEXT_X1);
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
		ballLeftStart = origX - 2 * SMALL_GAP + fontMetrics.stringWidth(code);
		pad.setFont(font);
		// pad.drawString(code, origX - 2* SMALL_GAP, forceY + BOX_GAP + 2);
		// pad.drawString("S&P", origX - 2* SMALL_GAP, forceY + 2 * BOX_GAP +
		// 2);
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
		pad.drawRect(origX, forceY - FORCE_HEIGHT, chartWidth, FORCE_HEIGHT);
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

		if (priceList == null || priceList.size() == 0) {
			int listSize = 0;
			if (priceList != null) {
				listSize = priceList.size();
			}

			pad.drawString("Data Error, list size: " + listSize, 200, 150);
			return;
		}

		// if the volume has error, do not drow volumes
		if (maxVolume <= 0) {
			hasVolumeError = true;
		}

		// pick the market flag list
		Vector marketFlagList = null;
		MarketIndicators marketIndicators = MarketCachePool.getMarketIndicators();
		if (period > 12) {
			marketFlagList = marketIndicators.getMarketWeeklyFlagList();
		} else {
			marketFlagList = marketIndicators.getMarketFlagList();
		}

		// just incase
		int minHeight = 0;
		if (maxP == minP) {
			maxP = minP + 1;
		}

		int totalItems = priceList.size() - startIndex;
		double minPLog = Math.log(minP);
		double maxPLog = Math.log(maxP);
		double yStep = (double) (chartHeight - PRICE_BOTTOM - PRICE_TOP) / (maxPLog - minPLog);
		double volumeStep = 0;

		if (!hasVolumeError) {
			volumeStep = (double) VOLUME_HEIGHT / maxVolume;
		}

		// bars are limited to the following range
		int barTop = origY - chartHeight + PRICE_TOP;
		int barBottom = origY - PRICE_BOTTOM;

		// System.out.println("barTop: " + barTop + ", barBottom: " +
		// barBottom);

		// debug
		// System.out.println("In draw, size: " + totalItems + "/" +
		// priceList.size());
		double volumeHt = 0, growthHt = 0, forceHt = 0, rsHt = 0;
		double yyOpen = 0, yyClose = 0, yyMin = 0, yyMax = 0;
		Vector monthList = new Vector();
		String prevMonthNum = "";
		String colorCode = "";

		double growthStep = 0;
		double forceStep = 0;
		double rsStep = 0;
		double zeroGrowthHt = 0;
		double zeroForceHt = 0;
		double lowRSHt = 0; // 30% line
		double highRSHt = 0; // 70% line
		double zeroRSHt = 0;

		int[] xPolylineRS = new int[totalItems];
		int[] yPolylineRS = new int[totalItems];
		int[] xPolylineGrowth = new int[totalItems];
		int[] yPolylineGrowth = new int[totalItems];
		int[] xPolylineForce = new int[totalItems];
		int[] yPolylineForce = new int[totalItems];
		int[] xPolylineAvg50 = new int[totalItems];
		int[] yPolylineAvg50 = new int[totalItems];
		int[] xPolylineAvg20 = new int[totalItems];
		int[] yPolylineAvg20 = new int[totalItems];
		int[] xPolylineAvg100 = new int[totalItems];
		int[] yPolylineAvg100 = new int[totalItems];
		int totalPoints = 0;
		int totalPointsAvg50 = 0;
		int totalPointsAvg20 = 0;
		int totalPointsAvg100 = 0;
		PolyHelper darkZone = new PolyHelper();
		PolyHelper yellowZone = new PolyHelper();

		// keep all the bars before draw later
		Vector barList = new Vector();

		// keep the ruler lines
		Vector rulerList = new Vector();

		// max/min growth
		findMinMaxGrowthForceRS();

		// drow steps
		growthStep = (double) GROWTH_HEIGHT / (2 * maxGrowth);
		forceStep = (double) FORCE_HEIGHT / (2 * maxForce);
		if (doRSI) {
			rsStep = (double) RS_HEIGHT / 100;
		}

		// define the two lines
		if (doRSI) {
			lowRSHt = LOW_RSI_LIMIT * rsStep;
			highRSHt = HIGH_RSI_LIMIT * rsStep;
			zeroRSHt = MID_RSI_LIMIT * rsStep;
		}

		// zero line in the middle
		zeroGrowthHt = growthY - 0.5 * GROWTH_HEIGHT;
		zeroForceHt = forceY - 0.5 * FORCE_HEIGHT;

		// display price ruler
		displayPriceRuler(minP, maxP, minPLog, yStep, rulerList);

		leftScaleGap = (int) leftTextWidth + SMALL_GAP + SMALL_GAP;
		double xStep = (double) (chartWidth - leftScaleGap) / totalItems;
		double xx = origX + leftScaleGap;
		int barrulerStep = 0;
		if (priceList.size() - startIndex < 110) {
			barrulerStep = 10; // regular = 3
		} else if (priceList.size() - startIndex < 180) {
			barrulerStep = 10; // regular = 6
		} else if (priceList.size() - startIndex < 280) {
			barrulerStep = 20;
		} else {
			barrulerStep = 40; // 24;
		}

		int lastBarIndex = 0;
		boolean lastBarUp = false, currentBarUp = true;
		boolean isFirst = true, drawBar = true;
		int avg20Y = 0, avg50Y = 0, avg100Y = 0;

		for (int i = startIndex; i < priceList.size(); i++, xx += xStep) {
			StockDailyInfo daily = (StockDailyInfo) priceList.get(i);
			if (daily.getOpen() <= 0 || daily.getClose() <= 0 || daily.getMax() <= 0 || daily.getMin() <= 0) {
				LOGGER.warn("Error: some zero in the value for " + stock.getTicker());
				continue;
			}

			// study line
			/*
			 * if ((i - startIndex) % barrulerStep == 0) {
			 * pad.setColor(colorDark);
			 * 
			 * //// draw the vertical lines, but leave some gap on the left
			 * pad.drawLine((int)xx, forceY, (int) xx, (int) origY -
			 * chartHeight); }
			 */

			if (leftLinePos == 0) {
				leftLinePos = xx;
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

			// moving average lines
			avg20Y = (int) (origY - PRICE_BOTTOM - (double) yStep * (daily.getAverage20Log() - minPLog));
			avg50Y = (int) (origY - PRICE_BOTTOM - (double) yStep * (daily.getAverage50Log() - minPLog));
			avg100Y = (int) (origY - PRICE_BOTTOM - (double) yStep * (daily.getAverage100Log() - minPLog));

			if (doEMA50 || doZone) {
				if (daily.getAverage50Log() <= maxPLog && daily.getAverage50Log() >= minPLog) {
					xPolylineAvg50[totalPointsAvg50] = (int) xx;
					yPolylineAvg50[totalPointsAvg50] = avg50Y;
					totalPointsAvg50++;
				}
			}

			if (doEMA20 || doZone) {
				if (daily.getAverage20Log() <= maxPLog && daily.getAverage20Log() >= minPLog) {
					xPolylineAvg20[totalPointsAvg20] = (int) xx;
					yPolylineAvg20[totalPointsAvg20] = avg20Y;
					totalPointsAvg20++;
				}
			}

			if (doEMA100 || doZone) {
				if (daily.getAverage100() > 0) { // only if it is defined
					if (daily.getAverage100Log() <= maxPLog && daily.getAverage100Log() >= minPLog) {
						xPolylineAvg100[totalPointsAvg100] = (int) xx;
						yPolylineAvg100[totalPointsAvg100] = avg100Y;
						totalPointsAvg100++;
					}
				}
			}

			// System.out.println("avg50Y: " + avg50Y + ", avg100Y: " +
			// avg100Y);

			// draw the dark zone
			// data sometimes is not enough to find avg100Y for weekly chart
			if (doZone) {
				boolean isLast = false;
				if (i == priceList.size() - 1) {
					isLast = true;
				}

				if (daily.getAverage100() > 0) {
					if (avg50Y < barTop) {
						avg50Y = barTop;
					} else if (avg50Y > barBottom) {
						avg50Y = barBottom;
					}

					if (avg100Y < barTop) {
						avg100Y = (int) barTop;
					} else if (avg100Y > barBottom) {
						avg100Y = barBottom;
					}

					if ((avg100Y != barTop || avg50Y != barTop) && (avg100Y != barBottom || avg50Y != barBottom)) {
						pad.setColor(colorLtYellow);
						// pad.drawLine((int)xx, avg50Y, (int) xx, avg100Y);

						darkZone.addPointPair((int) xx, avg50Y, (int) xx, avg100Y, isLast);
					}
				}
				// draw the yellow zone
				if (daily.getAverage50() > 0 && daily.getAverage100() > 0) {
					if (avg20Y < barTop) {
						avg20Y = barTop;
					} else if (avg20Y > barBottom) {
						avg20Y = barBottom;
					}

					pad.setColor(colorYellow);
					if (avg20Y > avg50Y && avg20Y > avg100Y) {
						if (avg50Y > avg100Y) {
							// pad.drawLine((int)xx, avg20Y, (int) xx, avg50Y);
							yellowZone.addPointPair((int) xx, avg20Y, (int) xx, avg50Y, isLast);
						} else {
							// pad.drawLine((int)xx, avg20Y, (int) xx, avg100Y);
							yellowZone.addPointPair((int) xx, avg20Y, (int) xx, avg100Y, isLast);
						}
					} else if (avg20Y < avg50Y && avg20Y < avg100Y) {
						if (avg50Y > avg100Y) {
							// pad.drawLine((int)xx, avg20Y, (int) xx, avg100Y);
							yellowZone.addPointPair((int) xx, avg20Y, (int) xx, avg100Y, isLast);
						} else {
							// pad.drawLine((int)xx, avg20Y, (int) xx, avg50Y);
							yellowZone.addPointPair((int) xx, avg20Y, (int) xx, avg50Y, isLast);
						}
					} else {
						yellowZone.closeOnePoly(null, null, null, true);
					}

				}
			}

			// find the positions
			if (doBARS) {
				yyOpen = origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getOpen()) - minPLog);
				yyClose = origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getClose()) - minPLog);
				yyMin = origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getMin()) - minPLog);
				yyMax = origY - PRICE_BOTTOM - (double) yStep * (Math.log(daily.getMax()) - minPLog);
			}

			if (daily.getClose() >= daily.getOpen()) {
				pad.setColor(colorGreen);
				colorCode = ColorCodes.GREEN;
			} else {
				pad.setColor(colorRed);
				colorCode = ColorCodes.RED;
			}

			// draw volume - only if there is volume
			if (!hasVolumeError) {
				volumeHt = (double) daily.getVolume() * volumeStep;
				pad.drawLine((int) xx, (int) volumeY, (int) xx, (int) (volumeY - volumeHt));
			}

			if (doBARS) {
				// keep bar chart info - and draw it later
				barList.add(new LineItem((int) xx, (int) yyMin, (int) xx, (int) yyMax, colorCode));
				barList.add(new LineItem((int) xx, (int) yyOpen, (int) (xx - BAR_WIDTH), (int) yyOpen, colorCode));
				barList.add(new LineItem((int) xx, (int) yyClose, (int) (xx + BAR_WIDTH), (int) yyClose, colorCode));
			}

			// System.out.println("yyMin: " + yyMin + ", yyMax: " + yyMax);

			// draw the growth rate
			growthHt = (double) daily.getSlope() * growthStep;

			// draw the signal balls
			if (daily.getTrendColor().equals(TrendTypes.GREEN)) {
				pad.setColor(colorGreen);
				pad.fillOval((int) xx - 2, (int) (growthY + 2), 4, 4);
				// pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep
				// + 1, (int) (growthHt - zeroGrowthHt));

			} else if (daily.getTrendColor().equals(TrendTypes.BLUE)) {
				pad.setColor(colorLtGreen);
				pad.fillOval((int) xx - 2, (int) (growthY + 2), 4, 4);
				// pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep
				// + 1, (int) (growthHt - zeroGrowthHt));

			} else if (daily.getTrendColor().equals(TrendTypes.RED)) {
				pad.setColor(colorRed);
				pad.fillOval((int) xx - 2, (int) (growthY + 2), 4, 4);
				// pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep
				// + 1, (int) (growthHt - zeroGrowthHt));

			} else {
				/* draw pink for trendColor = "Y" */
				pad.setColor(colorPink);
				pad.fillOval((int) xx - 2, (int) (growthY + 2), 4, 4);
				// pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep
				// + 1, (int) (growthHt - zeroGrowthHt));

			}

			// Draw the pattern points - not useful, skip
			// if (daily.getSuccess()) {
			// pad.setColor(colorDark);
			// pad.drawLine((int)xx, forceY, (int) xx, (int) origY -
			// chartHeight);
			// pad.drawLine((int)xx, (int) growthY, (int) xx, (int) (growthY -
			// GROWTH_HEIGHT));
			// }

			// growth line
			if (daily.getSlope() > 0) {
				pad.setColor(colorGreen);
				// pad.fillRect((int)xx, (int) (growthY - growthHt), (int) xStep
				// + 1, (int) (growthHt - zeroGrowthHt));
				// pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep
				// + 1, (int) (growthHt - zeroGrowthHt));
			} else {
				pad.setColor(colorRed);
				// pad.fillRect((int)xx, (int) (growthY - zeroGrowthHt), (int)
				// xStep + 1, (int) (zeroGrowthHt - growthHt));
				// pad.drawRect((int)xx, (int) (growthY - zeroGrowthHt), (int)
				// xStep + 1, (int) (zeroGrowthHt - growthHt));
			}

			pad.drawLine((int) xx, (int) zeroGrowthHt, (int) xx, (int) (zeroGrowthHt - growthHt));

			// force line
			forceHt = (double) daily.getAcceleration() * forceStep;
			if (daily.getAcceleration() > 0) {
				pad.setColor(colorGreen);
			} else {
				pad.setColor(colorRed);
			}

			pad.drawLine((int) xx, (int) zeroForceHt, (int) xx, (int) (zeroForceHt - forceHt));

			// draw the scan result if it is asked
			if (scanDateListHash.containsKey(daily.getMDate())) {
				// drawScanFlag(xx, (String) scanDateListHash.get(daily.getMDate()));
			}

			// draw flag if it is predicated
			/*
			 * if (daily.getPredicatedDays() != 0) { pad.setColor(colorGreen);
			 * pad.fillOval((int)xx - 2, (int) (volumeY + 2), 4, 4); }
			 */

			// if (i == startIndex) {
			// System.out.println("in draw, date: " + daily.getMDate());
			// DumpObject.toString(scanDateListHash);
			// }

			/*
			 * // rs line - from zero to the value rsHt = (double)
			 * (daily.getRelativeStrength() - minRS) * rsStep; if (rsHt >
			 * zeroRSHt) { pad.setColor(colorGreen); } else {
			 * pad.setColor(colorRed); }
			 * 
			 * pad.drawLine((int)xx, (int) (rsY - zeroRSHt), (int) xx, (int)
			 * (rsY - rsHt));
			 */

			xPolylineGrowth[totalPoints] = (int) xx;
			yPolylineGrowth[totalPoints] = (int) (zeroGrowthHt - growthHt);
			xPolylineForce[totalPoints] = (int) xx;
			yPolylineForce[totalPoints] = (int) (zeroForceHt - forceHt);
			if (doRSI) {
				xPolylineRS[totalPoints] = (int) xx;
				yPolylineRS[totalPoints] = (int) (rsY - daily.getRsi() * rsStep);
			}
			totalPoints++;

			double trendHt = forceY + BOX_GAP;
			double marketTrendHt = trendHt + BOX_GAP;
			if (xx - 3 > ballLeftStart) {
				/*
				 * if (daily.getTrendColor() != null &&
				 * daily.getTrendColor().equals(TrendTypes.GREEN)) { // draw
				 * green pad.setColor(colorGreen); pad.fillOval((int) xx - 2,
				 * (int) trendHt - 2, 4, 4); } else if (daily.getTrendColor() !=
				 * null && daily.getTrendColor().equals(TrendTypes.RED)) { // do
				 * RED pad.setColor(colorRed); pad.fillOval((int) xx - 2, (int)
				 * trendHt - 2, 4, 4);
				 * 
				 * } else { // do nothing //pad.setColor(colorBlue);
				 * //pad.drawLine((int) xx - 1, (int) ballHt, (int) xx + 1,
				 * (int) ballHt); }
				 */

				// draw market flag list
				/*
				 * if (marketFlagList != null && marketFlagList.size() >=
				 * (priceList.size() - startIndex)) {
				 * 
				 * String marketFlag = (String)
				 * marketFlagList.get(marketFlagList.size() - priceList.size() +
				 * i); if (marketFlag != null &&
				 * marketFlag.equals(TrendTypes.GREEN)) { // draw green
				 * pad.setColor(colorGreen); pad.fillOval((int) xx - 2, (int)
				 * marketTrendHt - 2, 4, 4); } else if (marketFlag != null &&
				 * marketFlag.equals(TrendTypes.RED)) { // do RED
				 * pad.setColor(colorRed); pad.fillOval((int) xx - 2, (int)
				 * marketTrendHt - 2, 4, 4); } else if (marketFlag != null &&
				 * marketFlag.equals(TrendTypes.YELLOW)) { // do RED
				 * pad.setColor(colorDYellow); pad.fillOval((int) xx - 2, (int)
				 * marketTrendHt - 2, 4, 4); } else { // do nothing
				 * //pad.setColor(colorBlue); //pad.drawLine((int) xx - 1, (int)
				 * marketBallHt, (int) xx + 1, (int) marketBallHt); } }
				 */

			}

			// DRAW THE TREND LINES
			/*
			 * if (i - startIndex + 1 >= TrendService.TREND_LENGTH) {
			 * pad.setColor(colorDark); drawLine(daily.getTrendHX1(),
			 * daily.getTrendHY1(), daily.getTrendHX2(), daily.getTrendHY2(),
			 * origX, origY, xStep, yStep, minPLog);
			 * drawLine(daily.getTrendLX1(), daily.getTrendLY1(),
			 * daily.getTrendLX2(), daily.getTrendLY2(), origX, origY, xStep,
			 * yStep, minPLog); }
			 */

			// average trend line
			/*
			 * pad.setColor(colorBlue); double yyHt = origY - PRICE_BOTTOM -
			 * (double) yStep * (daily.getTrendYAvg2() - minPLog) ;
			 * pad.fillOval((int) xx - 2, (int) yyHt - 2, 4, 4);
			 */

			// construct the trend line
			/*
			 * double trendYAvgAdj = 0; if (daily.getTrendYAvg() <= maxPLog &&
			 * daily.getTrendYAvg() >= minPLog) { trendYAvgAdj =
			 * daily.getTrendYAvg(); } else if (daily.getTrendYAvg() > maxPLog)
			 * { trendYAvgAdj = maxPLog; } else { trendYAvgAdj = minPLog; }
			 * 
			 * xPolylineTrend[totalPoints2] = (int) xx;
			 * yPolylineTrend[totalPoints2] = (int) (origY - PRICE_BOTTOM -
			 * (double) yStep * (trendYAvgAdj - minPLog)); totalPoints2++;
			 */

		}

		// fill the zones
		yellowZone.fillAllPoly(pad, colorYZone);
		darkZone.fillAllPoly(pad, colorDZone);

		//// display price ruler
		pad.setColor(colorDark);
		drawLineList(rulerList);

		// display average volume - line
		if (!hasVolumeError) {
			pad.setColor(colorDark);
			volumeHt = (double) stockExt.getAverageVolume() * volumeStep;
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
		}

		// draw 0 growth line / force line
		pad.setColor(colorDark);
		pad.drawLine(origX, (int) zeroGrowthHt, imageWidth - X_RIGHT, (int) zeroGrowthHt);
		pad.drawLine(origX, (int) zeroForceHt, imageWidth - X_RIGHT, (int) zeroForceHt);

		// draw low, high, zero RSI lines
		if (doRSI) {
			Stroke savedStroke = pad.getStroke();
			pad.setStroke(dashStroke);
			pad.setColor(colorRed);
			pad.drawLine(origX, (int) (rsY - lowRSHt), imageWidth - X_RIGHT, (int) (rsY - lowRSHt));
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
		// pad.setColor(colorLtBlue);
		// pad.drawPolyline(xPolylineTrend, yPolylineTrend, totalPoints2);

		Stroke savedStroke = pad.getStroke();
		pad.setStroke(thickStroke);

		// draw 50-day line
		if (doEMA50 || doZone) {
			if (doEMA50) {
				pad.setColor(colorBlue);
			} else {
				pad.setColor(colorLtYellow);
			}

			pad.drawPolyline(xPolylineAvg50, yPolylineAvg50, totalPointsAvg50);
		}

		// draw 100-day line
		if (doEMA100 || doZone) {
			if (doEMA100) {
				pad.setColor(colorRed);
			} else {
				pad.setColor(colorLtYellow);
			}

			pad.drawPolyline(xPolylineAvg100, yPolylineAvg100, totalPointsAvg100);
		}

		// draw bar here
		pad.setStroke(savedStroke);
		if (doBARS) {
			drawLineList(barList);
		}

		// draw 20-day line
		pad.setStroke(thickStroke);
		if (doEMA20 || doZone) {
			if (doEMA20) {
				pad.setColor(colorLtGreen);
			} else if (doZone) {
				pad.setColor(colorLtYellow);
			}

			pad.drawPolyline(xPolylineAvg20, yPolylineAvg20, totalPointsAvg20);
		}

		pad.setStroke(savedStroke);

		// draw string "50Day EMA"
		if (doEMA100 || doEMA20 || doEMA50) {
			displayEMAText();
		}

		// draw month ruler
		displayMonthScale(monthList);

		// draw the linear model line
		/*
		 * double y1 = stockExt.getSlope() + stockExt.getYCross(); double y2 =
		 * stockExt.getSlope() * priceList.size() + stockExt.getYCross(); double
		 * y1Loc = origY - PRICE_BOTTOM - (double) yStep * (y1 - minPLog);
		 * double y2Loc = origY - PRICE_BOTTOM - (double) yStep * (y2 -
		 * minPLog); pad.setColor(colorYellow); pad.drawLine((int) (origX +
		 * xStep), (int) y1Loc, (int) (origX + chartWidth - xStep), (int)
		 * y2Loc);
		 */

		// draw the trend line - TWO MAJOR TREND LINES IN THE PAST 35 DAYS
		/*
		 * savedStroke = pad.getStroke(); pad.setStroke(dashStroke); int delta =
		 * priceList.size() - TREND_LINE_LEN - startIndex;
		 * pad.setColor(colorBlue); drawLine(delta + stockExt.getTrendHX1(),
		 * stockExt.getTrendHY1(), delta + stockExt.getTrendHX2(),
		 * stockExt.getTrendHY2(), origX, origY, xStep, yStep, minPLog);
		 * 
		 * // low trend line drawLine(delta + stockExt.getTrendLX1(),
		 * stockExt.getTrendLY1(), delta + stockExt.getTrendLX2(),
		 * stockExt.getTrendLY2(), origX, origY, xStep, yStep, minPLog);
		 * 
		 * pad.setStroke(savedStroke);
		 */

	}

	private void displayMonthScale(Vector monthList) {
		//// display the months
		pad.setColor(colorBlue);
		double xxPrevPLocEnd = -100;
		Vector<TextItem> textList = new Vector<TextItem>();
		int maxSkip = 0;
		int numSkip = 0;
		
		for (int i = 0; i < monthList.size(); i++) {
			String monthPosStr = (String) monthList.get(i);
			Vector list = StringHelper.mySplit(monthPosStr, "#");
			if (list != null && list.size() == 2) {
				double xxP = (new Double((String) list.get(0))).doubleValue();
				String monthStr = (String) list.get(1);

				pad.drawLine((int) xxP, (int) origY, (int) xxP, (int) (origY - SCALE_LENGTH));
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
					//if (monthList.size() > 20) {
					//	printIt = false;
					//}
				}

				// check if too close to the previous position
				if (printIt && (xxPLoc < xxPrevPLocEnd)) {
					// skip the display, and keep xxPrevPLocEnd as it is
					printIt = false;
				}

				if (printIt) {
					// update the end position, only if it gets drawn
					xxPrevPLocEnd = xxPLoc + 2 * halfStrLen + 2;

					// draw the month string here
					// pad.drawString(monthStr, (int) xxPLoc, (int) origY - SMALL_GAP);
					if (numSkip > maxSkip) {
						maxSkip = numSkip;
					}
					numSkip = 0;
				} else {
					numSkip ++;
				}
			}
		}
		
		// in case the last one needs to be skipped
		if (numSkip > maxSkip) {
			maxSkip = numSkip;
		}
		
		// how many to skip?
		for (int i = 0; i < textList.size(); i = i + maxSkip + 1) {
			TextItem textItem = textList.get(i);
			pad.drawString(textItem.getText(), textItem.getXPos(), textItem.getYPos());
		}
	}

	private void displayEMAText() {
		String emaStr = RangeService.RANGE_20 + "-EMA";
		String emaStr2 = RangeService.RANGE_50 + "-EMA";
		String emaStr3 = RangeService.RANGE_100 + "-EMA";
		int strLen = (int) (fontMetrics.stringWidth(emaStr));
		int strLen2 = (int) (fontMetrics.stringWidth(emaStr2));
		int strLen3 = (int) (fontMetrics.stringWidth(emaStr3));

		int emaStrY = origY - chartHeight + TEXT_HEIGHT;
		int emaStrX = (int) (leftLinePos + 5 * SMALL_GAP);
		int totalLen = 6;

		if (doEMA20) {
			totalLen += (int) (strLen + 16);
		}

		if (doEMA50) {
			totalLen += (int) (strLen2 + 16);
		}

		if (doEMA100) {
			totalLen += (int) (strLen3 + 16);
		}

		// backgroud
		pad.setColor(chartColor);
		pad.fillRect(emaStrX - 18, emaStrY - 12, totalLen, 15);
		pad.setColor(colorBlack);
		pad.drawRect(emaStrX - 18, emaStrY - 12, totalLen, 15);

		// 20-ema
		if (doEMA20) {
			pad.setColor(colorLtGreen);
			pad.fillRect(emaStrX - 12, emaStrY - 7, 6, 6);
			pad.drawString(emaStr, emaStrX, emaStrY);
			emaStrX += strLen + 16;
		}

		// 50-ema
		if (doEMA50) {
			pad.setColor(colorBlue);
			pad.fillRect(emaStrX - 12, emaStrY - 7, 6, 6);
			pad.drawString(emaStr2, emaStrX, emaStrY);
			emaStrX += strLen2 + 16;
		}

		if (doEMA100) {
			// 100-ema
			pad.setColor(colorRed);
			pad.fillRect(emaStrX - 12, emaStrY - 7, 6, 6);
			pad.drawString(emaStr3, emaStrX, emaStrY);
		}
	}

	private void drawLine(int ax, double ay, int bx, double by, int origX, int origY, double xStep, double yStep,
			double minPLog) {
		double x1Loc = origX + (ax + 1) * xStep + leftScaleGap;
		double x2Loc = origX + (bx + 1) * xStep + leftScaleGap;
		double y1Loc = origY - PRICE_BOTTOM - (double) yStep * (ay - minPLog);
		double y2Loc = origY - PRICE_BOTTOM - (double) yStep * (by - minPLog);
		pad.drawLine((int) x1Loc, (int) y1Loc, (int) x2Loc, (int) y2Loc);
	}

	/**
	 * Go through the bar line list, and draw them
	 * 
	 * @param barList
	 */
	private void drawLineList(Vector barList) {
		if (barList == null || barList.size() == 0) {
			return;
		}

		String prevColor = "";
		for (int i = 0; i < barList.size(); i++) {
			LineItem line = (LineItem) barList.get(i);
			if (line.getColorCode() != null) {
				if (!line.getColorCode().equals(prevColor)) {
					if (line.getColorCode().equals(ColorCodes.GREEN)) {
						pad.setColor(colorGreen);
					} else {
						pad.setColor(colorRed);
					}
				}
			}

			pad.drawLine(line.getStartX(), line.getStartY(), line.getEndX(), line.getEndY());
			prevColor = line.getColorCode();
		}
	}

	/**
	 * Draw the ruler lines and the price labels.
	 * 
	 * @param minP
	 * @param maxP
	 * @param minPLog
	 * @param yStep
	 */
	private void displayPriceRuler(double minP, double maxP, double minPLog, double yStep, Vector rulerList) {
		Vector scaleList = getPriceRulerList(minP, maxP);
		String displayP = "";
		double runP = 0;
		double displayHt = 0;

		for (int i = 0; i < scaleList.size(); i++) {
			displayP = (String) scaleList.get(i);
			runP = (new Double(displayP)).doubleValue();
			displayHt = origY - PRICE_BOTTOM - (double) yStep * (Math.log(runP) - minPLog);

			if (i == scaleList.size() - 1) {
				leftTextWidth = (double) fontMetrics.stringWidth(displayP);
			}

			// debug
			// System.out.println("draw: " + runP);

			// draw a line
			// pad.setColor(colorDark);
			// pad.drawLine(origX, (int) displayHt, imageWidth - X_RIGHT, (int)
			// displayHt);
			rulerList.add(new LineItem(origX, (int) displayHt, imageWidth - X_RIGHT, (int) displayHt, null));

			// draw text - do not display if it is out of box
			if (displayHt - SMALL_GAP - fontMetrics.getHeight() >= origY - chartHeight) {
				pad.setColor(colorRed);
				pad.drawString(displayP, origX + SMALL_GAP, (int) (displayHt - TINY_GAP));
			}
		}
	}

	// draw scan flag for that position
	private void drawScanFlag(double xx, String scanKeyList) {
		if (scanKeyList == null || scanKeyList.equals("")) {
			return; // do not do anything
		}

		if (scanKeyList.indexOf("D") > -1) {
			pad.setColor(colorRed);
		} else {
			pad.setColor(colorGreen);
		}

		String showStr = "";
		if (scanKeyList.indexOf(":") > -1) {
			// for now, just show M
			String[] keys = scanKeyList.split(":");
			for (int i = 0; i < keys.length; i++) {
				showStr += keys[i].substring(0, 1);
			}
		} else {
			showStr = scanKeyList.substring(0, 1);
		}

		Font currentFont = pad.getFont(); // save it
		pad.setFont(new Font("TimesRoman", Font.PLAIN, 8));

		double textWidth = (double) fontMetrics.stringWidth(showStr) / 2;

		int xPos = (int) (xx - textWidth);
		int yPos = (int) (volumeY + fontMetrics.getHeight() / 2);
		pad.drawString(showStr, xPos, yPos);

		// set back
		pad.setFont(currentFont);

		// DEBUG
		// System.out.println("Draw: <" + showStr + "> at: " + xPos + ", " +
		// yPos);

	}

	/**
	 * Generate sequence of prices to be displayed on the y-axis.
	 * 
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
		} else if (range < 2000) {
			dispStep = 200;
		} else if (range < 3000) {
			dispStep = 300;
		} else {
			dispStep = Math.ceil(range * 0.001) * 100;
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
		} else {
			initP = Math.round(minP / 200 - 1) * 200;
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
	 * Find min/max of the slope and acceleration. Also convert the slope to
	 * percentage. And reset acceleration as the force.
	 */
	private void findMinMaxGrowthForceRS() {
		for (int i = startIndex; i < priceList.size(); i++) {
			StockDailyInfo daily = (StockDailyInfo) priceList.get(i);
			double theForce = daily.getAcceleration();
			double theSlope = daily.getSlope();
			// System.out.println("in draw, slope: " + theSlope);

			if (theSlope < 0) {
				theSlope = -theSlope;
			}

			if (theForce < 0) {
				theForce = -theForce;
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
	 * 
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
			displayStr = roundFormatter.format(volume);
		} else if (volume > 1000 && volume < 1000000) {
			displayStr = String.valueOf((int) ((double) volume / 1000)) + "K";
		} else {
			// million
			displayStr = String.valueOf((int) ((double) volume / 1000000)) + "M";
		}

		return displayStr;
	}

	/**
	 * Find the price list for the range.
	 * 
	 * @throws Exception
	 */
	private void prepareStockInfo() {

		DBComm database = new DBComm();
		StockAdminDAO stockDao = new StockAdminDAO();
		ScanDAO scanDao = new ScanDAO();

		// debug
		// System.out.println("In draw: stock id: " + stockId);
		// System.out.println("In draw: period: " + period);
		// System.out.println("In draw: option: " + option);

		try {
			database.openConnection();
			stockDao.setDatabase(database);
			scanDao.setDatabase(database);

			if (stockId != null) {
				stock = DBCachePool.getStockFromDB(stockId, stockDao);
			} else {
				Vector stkList = DBCachePool.getStockListFromDB(null, ticker, stockDao);
				if (stkList != null && stkList.size() > 0) {
					stock = (Stock) stkList.get(0);
					stockId = stock.getStockId();
				}
			}

			// set up the scan date list hashmap
			scanDateListHash = ScanCachePool.getScanDateListForStock(null, stockId, scanDao);
			if (scanDateListHash == null) {
				// incase something wrong
				LOGGER.warn("scan date hash is null for: " + stockId);
				scanDateListHash = new HashMap();
			}

			// try to get service context from Pool, doRSI = N
			ServiceContext svrContext = DBCachePool.getServiceContextX(stock, period, displayDate, weekly, "N",
					stockDao);
			if (svrContext != null) {
				priceList = svrContext.getPriceList();
				startIndex = svrContext.getStartIndex();
				stockExt = svrContext.getStockExt();
				lastDate = svrContext.getLastDate();
			}

		} catch (Exception e) {
			e.printStackTrace();
			priceList = null;
			stock = null;
			LOGGER.warn("Error in loading stock price info in DrawBasicChart: " + e.getMessage());
		} finally {
			try {
				database.closeConnection();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
	public String getTicker() {
		return ticker;
	}

	/**
	 * @param string
	 */
	public void setTicker(String string) {
		ticker = string;
	}

	/**
	 * @return
	 */
	public String getDisplayDate() {
		return displayDate;
	}

	/**
	 * @param string
	 */
	public void setDisplayDate(String string) {
		displayDate = string;
	}

	public String getWeekly() {
		return weekly;
	}

	public void setWeekly(String weekly) {
		this.weekly = weekly;
	}

}
