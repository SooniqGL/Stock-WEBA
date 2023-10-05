/*
 * Created on October 27, 2006
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
//import java.awt.Stroke;
import java.awt.geom.AffineTransform;
//import java.sql.Timestamp;






//import com.greenfield.ui.common.constant.TrendTypes;








import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.NumberUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.ScanUtil;
import com.greenfield.common.util.StringHelper;
import com.greenfield.common.object.scan.ScanReportVO;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * @author UZHANQX
 * 
 *         Draw the basic Chart.
 */
public class DrawReportChart extends BaseGraphics {
	private static final Logger LOGGER = Logger.getLogger(DrawReportChart.class); 
	
	public final static String REGULAR_COPYRIGHT = "Copyright 2006, Sooniq Technology Corporation";
	public final static String SHORT_COPYRIGHT = "Copyright 2006, Sooniq Tech.";

	// variables for input and stock related.
	private String scanKeyPrefix;
	private Vector reportList;
	private long maxCount;
	private double maxPercent;
	private double minPercent;
	private double avgPercent;

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
	private int countY;
	private int percentY;

	// MEASURE CONSTANTS
	public static final int DEFAULT_IMAGE_WIDTH = 650;
	public static final int DEFAULT_IMAGE_HEIGHT = 140;

	private int X_LEFT = 25;
	private int X_RIGHT = 5;
	private int Y_TOP = 10;
	private int Y_BOTTOM = 10;
	private int COUNT_HEIGHT = 40; // for count box
	private int PERCENT_HEIGHT = 50; // for percent box
	private int TITLE_HEIGHT = 20;
	private int BOX_GAP = 8;
	private int PERCENT_BOTTOM = 10; // draw above this height
	private int TEXT_HEIGHT = 15;
	private int VERTICAL_TEXT_X1 = 15; // price
	private int BIG_GAP = 20;
	private int SMALL_GAP = 5;
	private int TINY_GAP = 3;
	private int SCALE_LENGTH = 3;

	public void init() {
		colorBlack = Color.black;
		colorWhite = Color.white;
		colorRed = Color.red;
		colorGreen = Color.green;
		colorBlue = Color.blue;
		colorYellow = Color.yellow;
		colorLtYellow = new Color(255, 235, 100);
		colorDYellow = new Color(230, 230, 0);
		colorLtBlue = new Color(100, 100, 255);
		colorDark = new Color(200, 200, 200);
		colorPink = new Color(255, 0, 255);
		imageColor = new Color(235, 235, 235);
		chartColor = new Color(255, 255, 250);

		// A dashed stroke
		float strokeThickness = 1.0f;
		float miterLimit = 10f;
		float[] dashPattern = { 10f };
		float dashPhase = 5f;
		dashStroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
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
			pad.drawString("Error: No chart for this report: " + scanKeyPrefix,
					200, 290);
			return;
		}

		// prepare the font metrics
		if (font == null) {
			font = pad.getFont();
		}

		fontMetrics = frame.getFontMetrics(font);

		// Define the location
		countY = imageHeight - Y_BOTTOM;
		percentY = countY - COUNT_HEIGHT - BOX_GAP;

		// calculate measures
		chartWidth = imageWidth - X_LEFT - X_RIGHT;
		chartHeight = PERCENT_HEIGHT + COUNT_HEIGHT + BOX_GAP;
		origX = X_LEFT;
		origY = countY;

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
		// count rectangle
		savedColor = pad.getColor();
		pad.setColor(chartColor);
		pad.fillRect(origX, origY - COUNT_HEIGHT, chartWidth, COUNT_HEIGHT);

		// draw percent box
		pad.fillRect(origX, percentY - PERCENT_HEIGHT, chartWidth,
				PERCENT_HEIGHT);
		pad.setColor(savedColor);

		// draw chart rectange
		// pad.setColor(chartColor);
		// pad.fillRect(origX, origY - chartHeight, chartWidth, chartHeight);
		// pad.setColor(colorBlack);

		// draw title
		// do not use static formatter!
		SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat(
				"MMM dd, yyyy");
		String currentDate = MMM_DD_YYYY_formatter.format(new Date());
		savedColor = pad.getColor();
		pad.setColor(colorBlue);
		String title = "Chart for report, " + currentDate + ", "
				+ REGULAR_COPYRIGHT;
		pad.drawString(title, origX, Y_TOP + TEXT_HEIGHT);
		pad.setColor(savedColor);

		// This set will print vertical strings on screen
		AffineTransform at = new AffineTransform();
		at.setToRotation(1.5 * Math.PI); // same as: at.rotate(1.5 * Math.PI);
		pad.transform(at);
		pad.setColor(colorBlue);
		pad.drawString("Percent", -percentY, VERTICAL_TEXT_X1);
		pad.drawString("Stocks", -origY, VERTICAL_TEXT_X1);

		// reverse the coordinate system
		AffineTransform atInv = new AffineTransform();
		try {
			atInv = at.createInverse();
			pad.transform(atInv);
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("DrawBasicChart:Inverse transform error: "
					+ e.getMessage());
		}
	}

	/**
	 * Draw these rectangles last so to overwrite others.
	 */
	private void drawChartFramePost() {
		pad.setColor(colorBlack);
		pad.drawRect(origX, countY - COUNT_HEIGHT, chartWidth, COUNT_HEIGHT);
		pad.drawRect(origX, percentY - PERCENT_HEIGHT, chartWidth,
				PERCENT_HEIGHT);
	}

	/**
	 * Draw the price chart.
	 */
	private void drawReportChart() {
		if (reportList == null || reportList.size() == 0) {
			pad.drawString("Data Error", 200, 200);
			return;
		}

		// figure out min/max
		findMinMax();

		// just incase
		if (maxCount == 0) {
			maxCount = 1;
		}

		int totalItems = reportList.size();
		double xStep = (double) chartWidth / (totalItems + 1);
		double countStep = (double) (COUNT_HEIGHT - TINY_GAP) / maxCount;
		double span = 0;
		double percentStep = 0;
		String maxminStr = "Max = " + maxPercent + ", Min = " + minPercent
				+ ", Avg = " + avgPercent;
		double labelXPosition = origX + SMALL_GAP
				+ fontMetrics.stringWidth(maxminStr);
		double QtPercentHeight = percentY - PERCENT_HEIGHT * 0.75;

		// for count label
		double halfCountHeight = COUNT_HEIGHT * 0.5;
		double countLabelSize = (double) chartWidth / 8;

		// set to the middle
		double zeroPercentHt = percentY - (double) PERCENT_HEIGHT / 2;

		// find the span
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
		// define the y-step
		percentStep = (double) PERCENT_HEIGHT / span;

		// debug
		// System.out.println("In draw, size: " + totalItems);

		double xx = origX + xStep;
		double countHt = 0, percentHt = 0;
		Vector monthList = new Vector();
		String prevMonthNum = "";

		// variables to draw polyline
		int[] xPolylinePercent = new int[totalItems];
		int[] yPolylinePercent = new int[totalItems];
		int totalPoints = 0;

		// draw ...
		boolean drawLabelOnTop = true;
		boolean inFindingXX = true;
		double labelStartXX = origX;
		for (int i = 0; i < reportList.size(); i++, xx += xStep) {
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
			pad.drawLine((int) xx, (int) countY, (int) xx,
					(int) (countY - countHt));

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

			// draw the percent
			percentHt = zeroPercentHt - daily.getAvgPercent() * percentStep;
			if (percentHt < zeroPercentHt) {
				pad.setColor(colorGreen);
			} else {
				pad.setColor(colorRed);
			}

			pad.drawLine((int) xx, (int) zeroPercentHt, (int) xx,
					(int) percentHt);
			// to check if we need to adjust the label position
			if (drawLabelOnTop && (xx < labelXPosition)) {
				if (percentHt < QtPercentHeight) {
					drawLabelOnTop = false;
				}
			}

			xPolylinePercent[totalPoints] = (int) xx;
			yPolylinePercent[totalPoints] = (int) percentHt;
			totalPoints++;

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

		// draw max count
		pad.drawString("Max = " + maxCount, labelPosX, countY - COUNT_HEIGHT
				+ TINY_GAP + TEXT_HEIGHT);

		int labelPosY = 0;
		if (drawLabelOnTop) {
			labelPosY = percentY - PERCENT_HEIGHT + SMALL_GAP + TEXT_HEIGHT;
		} else {
			labelPosY = percentY - SMALL_GAP;
		}

		// draw the max/min/avg label
		pad.drawString(maxminStr, origX + SMALL_GAP, labelPosY);

		// draw 0 percent line
		pad.setColor(colorDark);
		pad.drawLine(origX, (int) zeroPercentHt, imageWidth - X_RIGHT,
				(int) zeroPercentHt);

		// draw polyline
		if (totalPoints > 0) {
			pad.setColor(colorDark);
			pad.drawPolyline(xPolylinePercent, yPolylinePercent, totalPoints);
		}

		// display the months
		pad.setColor(colorBlue);
		for (int i = 0; i < monthList.size(); i++) {
			String monthPosStr = (String) monthList.get(i);
			Vector list = StringHelper.mySplit(monthPosStr, "#");
			if (list != null && list.size() == 2) {
				double xxP = (new Double((String) list.get(0))).doubleValue();
				String monthStr = (String) list.get(1);

				pad.drawLine((int) xxP, (int) origY, (int) xxP,
						(int) (origY - SCALE_LENGTH));
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

				pad.drawString(monthStr, (int) xxPLoc, (int) origY - SMALL_GAP);

			}
		}

	}

	/**
	 * Find min/max of the count and percent
	 */
	private void findMinMax() {
		double sumPercent = 0;
		for (int i = 0; i < reportList.size(); i++) {
			ScanReportVO daily = (ScanReportVO) reportList.get(i);
			long theCount = Math.round(daily.getTotalTicker());
			double percent = daily.getAvgPercent();
			sumPercent += percent;

			if (i == 0) {
				minPercent = percent;
				maxPercent = percent;
				maxCount = theCount;
			} else {
				if (minPercent > percent) {
					minPercent = percent;
				}

				if (maxPercent < percent) {
					maxPercent = percent;
				}

				if (maxCount < theCount) {
					maxCount = theCount;
				}
			}
		}

		avgPercent = (double) sumPercent / reportList.size();
		avgPercent = NumberUtil.formatDoubleTwo(avgPercent);

		// System.out.println("min/Max: " + minPercent + "/" + maxPercent);
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
			return (String) list.get(0);
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

		try {
			String startDate = getStartDate();
			database.openConnection();
			scanDao.setDatabase(database);
			Vector scanList = scanDao.getScanHistoryFromDB2(scanKeyPrefix,
					startDate);
			reportList = ScanUtil.createScanReport(scanList);
		} catch (Exception e) {
			e.printStackTrace();
			reportList = null;
			LOGGER
					.warn("Error in loading scan history info in DrawPerformChart: "
							+ e.getMessage());
		} finally {
			try {
				database.closeConnection();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	private String getStartDate() {
		int DAYS = 120;
		String startDate = OracleUtil.getPrevDateInOracleFormat(null, 0, DAYS);
		return startDate;
	}

	/**
	 * @return
	 */
	public String getScanKeyPrefix() {
		return scanKeyPrefix;
	}

	/**
	 * @param string
	 */
	public void setScanKeyPrefix(String string) {
		scanKeyPrefix = string;
	}

}

/* end of code */