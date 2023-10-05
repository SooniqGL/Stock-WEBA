/*
 * 7/7/2010
 */

package com.greenfield.ui.graph;

import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.ui.cache.ScanCachePool;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.NumberUtil;
import com.greenfield.common.util.ScanUtil;
import com.greenfield.common.util.StringHelper;
import com.greenfield.common.object.scan.ScanReportVO;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.object.stock.MarketIndicators;

import java.awt.BasicStroke;
//import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.geom.AffineTransform;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author QZ69042
 */
public class DrawReportChart2 extends BaseGraphics {
	private static final Logger LOGGER = Logger.getLogger(DrawReportChart2.class); 
	
	public final static String REGULAR_COPYRIGHT = "Copyright 2006, Sooniq Technology Corporation";
	public final static String SHORT_COPYRIGHT = "Copyright 2006, Sooniq Tech.";

	// some local constants
	private final static int PERCENT_10_INDEX = 0;
	private final static int PERCENT_20_INDEX = 1;
	private final static int PERCENT_30_INDEX = 2;
	private final static int PERCENT_60_INDEX = 3;

	// variables for input and stock related.
	private String scanKey;
	private String scanIndex;
	private String scanStDate;
	private String scanEdDate;

	//
	private Vector reportList; // queried from DB

	private long maxTickerCount; // for the tickers
	private double avgTickerCount;

	private double maxPercent = -100; // max gain percent
	private double minPercent = 100;
	private double avgPercent;

	// local variables - for color and style
	private Color colorBlack;
	private Color colorRed;
	private Color colorGreen;
	private Color colorBlue;
	private Color colorDark;
	private Color imageColor;
	private Color chartColor;
	private Color savedColor;

	// font
	private Font bigFont;
	private FontMetrics fontMetrics;

	// format
	DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
	DecimalFormat roundFormatter = new DecimalFormat("0");

	// measures
	private int chartWidth;
	private int chartHeight;
	private int origX;
	private int origY;

	// y positions
	private int[] percentY;
	private int topY;

	// STEPS
	int totalItems = 0;
	double xStep = 0;

	// market indicators
	HashMap marketAvg20Hash = null;
	HashMap marketAvg50Hash = null;

	// MEASURE CONSTANTS
	public static final int DEFAULT_IMAGE_WIDTH = 1260;
	public static final int DEFAULT_IMAGE_HEIGHT = 845; 

	private int X_LEFT = 35;
	private int X_RIGHT = 12;
	private int Y_TOP = 40;
	private int Y_BOTTOM = 35;
	private int COUNT_HEIGHT = 100; // for count box
	private int PERCENT_HEIGHT = 150; // for percent box
	//private int TITLE_HEIGHT = 20;
	private int BOX_GAP = 8;
	private int TEXT_HEIGHT = 15;
	private int VERTICAL_TEXT_X1 = 30; // price
	private int BIG_GAP = 20;
	private int SMALL_GAP = 5;
	private int TINY_GAP = 3;
	private int SCALE_LENGTH = 5;

	public void init() {
		colorBlack = Color.black;
		colorRed = Color.red;
		colorGreen = Color.green;
		colorBlue = Color.blue;
		colorDark = new Color(200, 200, 200);
		imageColor = new Color(235, 235, 235);
		chartColor = new Color(255, 255, 250);

		// A dashed stroke
		//float strokeThickness = 1.0f;
		//float miterLimit = 10f;
		//float[] dashPattern = { 10f };
		//float dashPhase = 5f;
		//dashStroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
		//		BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		
		
	}

	/** test */
	public static void main(String[] argv) {
		try {
			// debug
			// System.out.println("in drawicon ...");
			String fileName = "E:\\temp\\good.gif";

			DrawReportChart draw = new DrawReportChart();
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
	public void paint() {
		prepareReportInfo();
		if (reportList == null || reportList.size() == 0) {
			// no enough data found
			pad.setColor(colorGreen);
			pad.drawString("Error: No chart for this report: " + scanKey, 200,
					290);
			return;
		}

		// prepare the font metrics
		Font currentFont = pad.getFont();
		Font midFont = currentFont.deriveFont(currentFont.getSize() * 1.9F);
		bigFont = currentFont.deriveFont(currentFont.getSize() * 2.3F);
		pad.setFont(midFont);
		fontMetrics = frame.getFontMetrics(midFont);
		
		BasicStroke regularThickStroke = new BasicStroke(2.2f, BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);
		pad.setStroke(regularThickStroke);

		// Define the location
		origX = X_LEFT;
		origY = imageHeight - Y_BOTTOM;

		// vertical positions
		percentY = new int[4];
		percentY[0] = origY - COUNT_HEIGHT - BOX_GAP;
		percentY[1] = percentY[0] - BOX_GAP - PERCENT_HEIGHT;
		percentY[2] = percentY[1] - BOX_GAP - PERCENT_HEIGHT;
		percentY[3] = percentY[2] - BOX_GAP - PERCENT_HEIGHT;
		topY = percentY[3] - BOX_GAP - PERCENT_HEIGHT;

		// calculate measures
		chartWidth = imageWidth - X_LEFT - X_RIGHT;
		chartHeight = imageHeight - Y_TOP - Y_BOTTOM; // origY - topY;

		pad.setColor(imageColor);
		pad.fillRect(0, 0, imageWidth, imageHeight);

		// draw the frame
		pad.setColor(colorBlack);
		drawChartFrame();

		// draw the chart
		pad.setColor(colorBlack);
		drawReportChart();

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
		// percent rectangle
		savedColor = pad.getColor();

		// draw chart rectange
		pad.setColor(chartColor);
		pad.fillRect(origX, origY - chartHeight, chartWidth, chartHeight);

		// draw title
		// do not use static formatter!
		//SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat("MMM dd, yyyy");
		//String currentDate = MMM_DD_YYYY_formatter.format(new Date());
		savedColor = pad.getColor();
		pad.setColor(colorBlue);
		String title = "Chart for Scan Report"; // + currentDate;  // + ", " + REGULAR_COPYRIGHT;
		
		pad.drawString(title, origX, Y_TOP - TEXT_HEIGHT + SMALL_GAP + 2);
		pad.setColor(savedColor);

		// This set will print vertical strings on screen
		AffineTransform at = new AffineTransform();
		at.setToRotation(1.5 * Math.PI); // same as: at.rotate(1.5 * Math.PI);
		pad.transform(at);
		pad.setColor(colorBlue);

		pad.drawString("Stocks", -origY, VERTICAL_TEXT_X1);

		pad.drawString("Percent 10", -percentY[PERCENT_10_INDEX] + BOX_GAP,
				VERTICAL_TEXT_X1);
		pad.drawString("Percent 20", -percentY[PERCENT_20_INDEX] + BOX_GAP,
				VERTICAL_TEXT_X1);
		pad.drawString("Percent 30", -percentY[PERCENT_30_INDEX] + BOX_GAP,
				VERTICAL_TEXT_X1);
		pad.drawString("Percent 60", -percentY[PERCENT_60_INDEX] + BOX_GAP,
				VERTICAL_TEXT_X1);

		// reverse the coordinate system
		AffineTransform atInv = new AffineTransform();
		try {
			atInv = at.createInverse();
			pad.transform(atInv);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("DrawReportChart2: Inverse transform error: "
					+ e.getMessage());
		}
	}

	/**
	 * Draw these rectangles last so to overwrite others.
	 */
	private void drawChartFramePost() {
		pad.setColor(colorBlack);
		pad.drawRect(origX, origY - chartHeight, chartWidth, chartHeight);
		// pad.drawRect(origX, origY - PERCENT_HEIGHT, chartWidth,
		// PERCENT_HEIGHT);
	}

	/**
	 * Draw the price chart.
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private void drawReportChart() {
		if (reportList == null || reportList.size() == 0) {
			pad.drawString("Data Error", 200, 200);
			return;
		}

		// define the steps
		totalItems = reportList.size();
		xStep = (double) chartWidth / (totalItems + 1);

		// figure out min/max
		findMaxTickerCount();

		// just incase
		if (maxTickerCount == 0) {
			maxTickerCount = 1;
		}

		// draw percent sections
		drawPercentChart(PERCENT_10_INDEX);
		drawPercentChart(PERCENT_20_INDEX);
		drawPercentChart(PERCENT_30_INDEX);
		drawPercentChart(PERCENT_60_INDEX);

		// debug
		// System.out.println("In draw, size: " + totalItems);

		// begin to draw the count part of graph
		double countStep = (double) (COUNT_HEIGHT - TINY_GAP) / maxTickerCount;
		double halfCountHeight = COUNT_HEIGHT * 0.5;
		double countLabelSize = (double) chartWidth / 8;

		// more variables
		double xx = origX + xStep;
		double countHt = 0;
		Vector monthList = new Vector();
		String prevMonthNum = "";

		//String avg20Trend = null;
		//String avg50Trend = null;

		boolean inFindingXX = true;
		double labelStartXX = origX;
		for (int i = reportList.size() - 1; i >= 0; i--, xx += xStep) {
			ScanReportVO daily = (ScanReportVO) reportList.get(i);
			if (daily.getTotalTicker() < 0) {
				LOGGER.warn("Error: negative in the value for "
						+ daily.getScanDate());
				continue;
			}

			// study line
			/*
			 * if (i % 3 == 0) { pad.setColor(colorDark);
			 * 
			 * // draw the vertical lines, but leave some gap on the left if (xx
			 * > origX + SMALL_GAP + SMALL_GAP) { pad.drawLine((int)xx, origY,
			 * (int) xx, (int) origY - chartHeight); } }
			 */

			// figure out the month string
			if (prevMonthNum.equals("")) {
				prevMonthNum = getMonthNumber(daily.getScanDate());
			} else {
				String thisMonthNum = getMonthNumber(daily.getScanDate());
				if (!prevMonthNum.equals(thisMonthNum)) {
					monthList.add(String.valueOf(xx) + "#"
							+ DateUtil.getShortMonthString(thisMonthNum));
				}

				prevMonthNum = thisMonthNum;
			}

			// draw count
			pad.setColor(colorBlue);
			countHt = (double) daily.getTotalTicker() * countStep;
			pad.drawLine((int) xx, (int) origY, (int) xx,
					(int) (origY - countHt));

			// check if the lable position is found
			if (inFindingXX) {
				if (countHt > halfCountHeight) {
					// reset
					labelStartXX = xx;
				}

				if (xx - labelStartXX > countLabelSize) {
					// enough location is found
					inFindingXX = false;
				}
			}

			// draw market flag - AVG 20 indicator
			/*
			if (marketAvg20Hash.containsKey(daily.getScanDate())) {
				avg20Trend = (String) marketAvg20Hash.get(daily.getScanDate());
				if (avg20Trend.equals(TrendTypes.GREEN)) {
					// draw green ball
					pad.setColor(colorGreen);
					pad.fillOval((int) xx - 2, (int) (Y_TOP + SMALL_GAP
							+ TEXT_HEIGHT + 2), 4, 4);
				} else if (avg20Trend.equals(TrendTypes.RED)) {
					pad.setColor(colorRed);
					pad.fillOval((int) xx - 2, (int) (Y_TOP + SMALL_GAP
							+ TEXT_HEIGHT + 2), 4, 4);
				}
			}

			if (marketAvg50Hash.containsKey(daily.getScanDate())) {
				avg50Trend = (String) marketAvg50Hash.get(daily.getScanDate());
				if (avg50Trend.equals(TrendTypes.GREEN)) {
					// draw green ball
					pad.setColor(colorGreen);
					pad.fillOval((int) xx - 2, (int) (Y_TOP + SMALL_GAP
							+ TEXT_HEIGHT + 8), 4, 4);
				} else if (avg50Trend.equals(TrendTypes.RED)) {
					pad.setColor(colorRed);
					pad.fillOval((int) xx - 2, (int) (Y_TOP + SMALL_GAP
							+ TEXT_HEIGHT + 8), 4, 4);
				}
			} */

		}

		// display max count
		pad.setColor(colorBlue);
		int labelPosX = 0;
		if (!inFindingXX) {
			// position found
			if (labelStartXX > origX) {
				labelPosX = (int) labelStartXX + BIG_GAP;
			} else {
				labelPosX = (int) labelStartXX + SMALL_GAP;
			}
		} else {
			// put default
			labelPosX = origX + SMALL_GAP;
		}

		// draw
		pad.drawString("Max = " + maxTickerCount, labelPosX, origY
				- COUNT_HEIGHT + SMALL_GAP * 3 + TEXT_HEIGHT);

		// draw the date range title
		Font savedFont = pad.getFont();
		pad.setFont(bigFont);
		String subtitle = "Scan Date Range: " + scanStDate + " - " + scanEdDate
				+ "; Scan Mode: " + ScanModes.getModeString(scanKey);
		pad.setColor(colorBlack);
		pad.drawString(subtitle, origX + SMALL_GAP, Y_TOP + TEXT_HEIGHT + SMALL_GAP * 3);
		pad.setFont(savedFont);

		// draw the top line
		pad.drawLine(origX, (int) (origY - COUNT_HEIGHT), imageWidth - X_RIGHT,
				(int) (origY - COUNT_HEIGHT));

		// draw month labels
		drawMonthLabels(monthList);
	}

	@SuppressWarnings("rawtypes")
	private void drawMonthLabels(Vector monthList) {
		// display the months
		pad.setColor(colorBlue);
		double prevPosition = origX;
		for (int i = 0; i < monthList.size(); i++) {
			String monthPosStr = (String) monthList.get(i);
			Vector list = StringHelper.mySplit(monthPosStr, "#");
			if (list != null && list.size() == 2) {

				double xxP = (new Double((String) list.get(0))).doubleValue();
				String monthStr = (String) list.get(1);

				pad.drawLine((int) xxP, (int) origY, (int) xxP,
						(int) (origY + SCALE_LENGTH));
				double halfStrLen = (double) fontMetrics.stringWidth(monthStr) / 2;

				// draw the string, only if it fits insider of the box.
				double xxPLoc = 0;
				if (xxP - halfStrLen < origX) {
					xxPLoc = origX + SMALL_GAP;
				} else if (xxP + halfStrLen > origX + chartWidth) {
					xxPLoc = origX + chartWidth - halfStrLen - halfStrLen
							- SMALL_GAP;
				} else {
					xxPLoc = xxP - halfStrLen;
				}

				if (i == 0) {
					prevPosition = xxP;
				} else {
					if (xxP - prevPosition < halfStrLen + halfStrLen) {
						// skip the month string, since space issue
						continue;
					}
				}

				prevPosition = xxP;
				pad.drawString(monthStr, (int) xxPLoc, (int) origY
						+ TEXT_HEIGHT + SMALL_GAP * 2);

			}
		}

	}

	/**
	 * Already defined: totalItems, xStep
	 * 
	 * @param percentIndex
	 */
	private void drawPercentChart(int percentIndex) {

		// figure out min/max and average
		findMinMaxPercent(percentIndex);

		double xx = origX + xStep;
		double percentHt = 0;
		double zeroPercentHt = 0;
		double span = 0;
		double percentStep = 0;

		double quarterHeight = percentY[percentIndex] - PERCENT_HEIGHT * 0.75;
		String maxminStr = "Max = " + maxPercent + ", Min = " + minPercent;
		double labelXPosition = origX + SMALL_GAP
				+ fontMetrics.stringWidth(maxminStr);

		// set to the middle
		zeroPercentHt = percentY[percentIndex] - (double) PERCENT_HEIGHT / 2;

		// find span
		if (minPercent < 0 && maxPercent > 0) {
			if (maxPercent > -minPercent) {
				span = 2 * maxPercent;
			} else {
				span = -2 * minPercent;
			}
		} else if (minPercent >= 0) {
			span = 2 * maxPercent;
		} else {
			span = -2 * minPercent;
		}

		if (span <= 0) {
			span = 1;
		}

		// define the y step based on the span and height
		percentStep = (double) PERCENT_HEIGHT / span;

		// debug
		// System.out.println("In draw, size: " + totalItems + ", step: " +
		// percentStep);
		// System.out.println("min/max: " + minPercent + "/" + maxPercent);

		int[] xPolylinePercent = new int[totalItems];
		int[] yPolylinePercent = new int[totalItems];
		int totalPoints = 0;

		boolean displayLabelOnTop = true;
		for (int i = reportList.size() - 1; i >= 0; i--, xx += xStep) {
			ScanReportVO daily = (ScanReportVO) reportList.get(i);

			// study line
			/*
			 * if (i % 3 == 0) { pad.setColor(colorDark);
			 * 
			 * // draw the vertical lines, but leave some gap on the left if (xx
			 * > origX + SMALL_GAP + SMALL_GAP) { pad.drawLine((int)xx, origY,
			 * (int) xx, (int) origY - chartHeight); } }
			 */

			// draw the percent
			percentHt = zeroPercentHt - daily.getGain(percentIndex)
					* percentStep;
			if (daily.getGain(percentIndex) > 0) {
				pad.setColor(colorGreen);
			} else {
				pad.setColor(colorRed);
			}

			pad.drawLine((int) xx, (int) zeroPercentHt, (int) xx,
					(int) percentHt);
			if (displayLabelOnTop && (xx < labelXPosition)) {
				if (percentHt < quarterHeight) {
					displayLabelOnTop = false;
				}
			}

			xPolylinePercent[totalPoints] = (int) xx;
			yPolylinePercent[totalPoints] = (int) percentHt;
			totalPoints++;

		}

		// draw 0 percent line
		pad.setColor(colorDark);
		// if (minPercent < 0 && maxPercent > 0) {
		pad.drawLine(origX, (int) zeroPercentHt, imageWidth - X_RIGHT,
				(int) zeroPercentHt);
		// }

		// draw polyline
		if (totalPoints > 0) {
			pad.setColor(colorDark);
			pad.drawPolyline(xPolylinePercent, yPolylinePercent, totalPoints);
		}

		// display max/min/avg string
		pad.setColor(colorBlue);
		if (displayLabelOnTop) {
			pad.drawString(maxminStr, origX + SMALL_GAP, percentY[percentIndex]
					- PERCENT_HEIGHT + SMALL_GAP + TEXT_HEIGHT);
		} else {
			pad.drawString(maxminStr, origX + SMALL_GAP, percentY[percentIndex]
					+ SMALL_GAP - TEXT_HEIGHT);
		}

		// draw box
		pad.setColor(colorBlack);
		pad.drawRect(origX, percentY[percentIndex] - PERCENT_HEIGHT,
				chartWidth, PERCENT_HEIGHT);

	}

	/**
	 * Find min/max/avg of the count and percent
	 */
	private void findMaxTickerCount() {
		double sumCount = 0;
		for (int i = 0; i < reportList.size(); i++) {
			ScanReportVO daily = (ScanReportVO) reportList.get(i);
			long theCount = Math.round(daily.getTotalTicker());

			sumCount += theCount;
			if (i == 0) {
				maxTickerCount = theCount;
			} else {
				if (maxTickerCount < theCount) {
					maxTickerCount = theCount;
				}
			}
		}

		avgTickerCount = (double) sumCount / totalItems;
		// System.out.println("avgTicketCount: " + avgTickerCount + ", sum: " +
		// sumCount);
		avgTickerCount = NumberUtil.formatDoubleTwo(avgTickerCount);

		// System.out.println("Max/Avg: " + maxTickerCount + "/" +
		// avgTickerCount);
	}

	/**
	 * Find min/max/avg of the count and percent This is called for each percent
	 * chart drawing
	 */
	private void findMinMaxPercent(int percentIndex) {
		double sumPercent = 0;
		for (int i = 0; i < reportList.size(); i++) {
			ScanReportVO daily = (ScanReportVO) reportList.get(i);
			double theGain = daily.getGain(percentIndex);
			sumPercent += theGain;

			if (i == 0) {
				minPercent = theGain;
				maxPercent = theGain;
			} else {
				if (minPercent > theGain) {
					minPercent = theGain;
				}

				if (maxPercent < theGain) {
					maxPercent = theGain;
				}
			}
		}

		avgPercent = (double) sumPercent / totalItems;
		avgPercent = NumberUtil.formatDoubleTwo(avgPercent);

		// round to 2 digits
		maxPercent = NumberUtil.formatDoubleTwo(maxPercent);
		minPercent = NumberUtil.formatDoubleTwo(minPercent);

		// debug
		// System.out.println("index: " + percentIndex + ", min/Max: " +
		// minPercent + "/" + maxPercent);
	}

	/**
	 * scanDate format: MM/DD/YYYY
	 * 
	 * @param scanDate
	 * @return
	 */
	private String getMonthNumber(String scanDate) {
		if (scanDate == null) {
			return "";
		}

		Vector list = StringHelper.mySplit(scanDate, "/");
		if (list != null && list.size() > 0) {
			return (String) list.get(1);
		}

		return "";
	}

	/**
	 * Find the price list for the range.
	 * 
	 * @throws Exception
	 */
	private void prepareReportInfo() {
		DBComm database = new DBComm();
		ScanDAO scanDao = new ScanDAO();
		// StockAdminDAO stockAdminDao = new StockAdminDAO();

		try {
			database.openConnection();
			scanDao.setDatabase(database);
			if (scanKey != null && !scanKey.equals("")) {
				checkStEndDates(scanDao);
				reportList = scanDao.getScanReportFromDB(scanKey, null, null,
						scanStDate, scanEdDate);
				ScanUtil.findAveragesForScanReport(reportList, 0);
			}

			// point to the market indicators
			try {
				MarketIndicators marketIndicators = MarketCachePool
						.getMarketIndicators();
				marketAvg20Hash = marketIndicators.getAverage20TrendFlagHash();
				marketAvg50Hash = marketIndicators.getAverage50TrendFlagHash();
			} catch (Exception ex) {
				// ignore
			}

			// if they are not available, set to empty
			if (marketAvg20Hash == null || marketAvg50Hash == null) {
				marketAvg20Hash = new HashMap();
				marketAvg50Hash = new HashMap();
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportList = null;
			LOGGER
					.warn("Error in loading scan report info in DrawReportChart2: "
							+ e.getMessage());
		} finally {
			try {
				database.closeConnection();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	private void checkStEndDates(ScanDAO scanDao) {
		// if these twodatgesare not set, get default
		int scanIndexInt = 0;

		try {
			scanIndexInt = (new Integer(scanIndex)).intValue();
			Vector dateList = ScanCachePool.getScanReportDateList(scanKey,
					scanDao);

			if (dateList != null && scanIndexInt >= 0
					&& scanIndexInt < dateList.size() - 1) {
				scanStDate = (String) dateList.get(scanIndexInt + 1);
				scanEdDate = (String) dateList.get(scanIndexInt);

				// debug
				System.out.println("draw chart, start date: " + scanStDate
						+ ", end date: " + scanEdDate);
			}

		} catch (Exception e) {
			// ignore
		}
	}

	public String getScanEdDate() {
		return scanEdDate;
	}

	public void setScanEdDate(String scanEdDate) {
		this.scanEdDate = scanEdDate;
	}

	public String getScanStDate() {
		return scanStDate;
	}

	public void setScanStDate(String scanStDate) {
		this.scanStDate = scanStDate;
	}

	/**
	 * @return
	 */
	public String getScanKey() {
		return scanKey;
	}

	/**
	 * @param string
	 */
	public void setScanKey(String string) {
		scanKey = string;
	}

	public String getScanIndex() {
		return scanIndex;
	}

	public void setScanIndex(String scanIndex) {
		this.scanIndex = scanIndex;
	}

}

/* end of code */