/*
 * Created on Nov 21, 2006
 */
package com.greenfield.ui.graph;

import com.greenfield.ui.cache.MarketCachePool;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.util.Vector;

import com.greenfield.common.constant.MarketTypes;
import com.greenfield.common.dao.analyze.MarketPulseDAO;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.MarketUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.StringHelper;
import com.greenfield.common.object.market.MarketPulse;

import java.text.SimpleDateFormat;

import org.apache.log4j.Logger;

/**
 * @author zhangqx
 * 
 *         Draw Market Statistics
 */
public class DrawMarketBPIChart extends BaseGraphics {
	private static final Logger LOGGER = Logger
			.getLogger(DrawMarketBPIChart.class);

	// which time to draw
	public final static int DEFAULT_PERIOD = 6; // six months chart

	// option values
	// public final static String GROWTH_OPTION = "G";
	public final static String REGULAR_COPYRIGHT = "Copyright 2006, Sooniq Technology Corporation";
	// public final static String SHORT_COPYRIGHT = "Copyright 2006, Sooniq Tech.";

	// variables for input and stock related.
	private String marketType;
	private int screenIndex;

	private String dataStDate;
	private String dataEdDate;

	private String option;
	private int startIndex;
	private double maxAD;
	private double minAD;
	private Vector marketPulseList;

	// local variables - for color and style
	private Color colorBlack;
	private Color colorRed;
	private Color colorGreen;
	private Color colorBlue;
	private Color colorDark;
	private Color imageColor;
	private Color chartColor;
	private Color savedColor;
	private BasicStroke dashStroke;
	private BasicStroke thickStroke;

	// font
	private Font font;
	// private Font midFont;

	private FontMetrics fontMetrics;

	// format
	DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
	DecimalFormat roundFormatter = new DecimalFormat("0");

	// measures
	private int chartWidth;
	private int chartHeight;
	private int origX;
	private int origY;
	private int above50Y;
	private int updayY;

	// MEASURE CONSTANTS
	public static final int DEFAULT_IMAGE_WIDTH = 1260;
	public static final int DEFAULT_IMAGE_HEIGHT = 700;

	private int X_LEFT = 35;
	private int X_RIGHT = 12;
	private int Y_TOP = 12;
	private int Y_BOTTOM = 10;
	private int AD_TOP = 10;
	private int AD_BOTTOM = 20;
	private int ABOVE50_HEIGHT = 300; // for above50 box
	private int UPDAY_HEIGHT = 300; // for upday box
	private int BOX_GAP = 12;
	private int SCALE_LENGTH = 3; // bars in y axis
	private int TEXT_HEIGHT = 15;
	private int SMALL_GAP = 5;

	public void init() {
		colorBlack = Color.black;
		colorRed = Color.red;
		colorGreen = Color.green;
		colorBlue = Color.blue;
		colorDark = new Color(200, 200, 200);
		imageColor = new Color(235, 235, 235);
		chartColor = new Color(255, 255, 250);

		// A dashed stroke
		float strokeThickness = 1.8f;
		float miterLimit = 10f;
		float[] dashPattern = { 10f };
		float dashPhase = 5f;
		dashStroke = new BasicStroke(strokeThickness, BasicStroke.CAP_BUTT,
				BasicStroke.JOIN_MITER, miterLimit, dashPattern, dashPhase);
		
		thickStroke = new BasicStroke(2.6f, BasicStroke.CAP_BUTT, BasicStroke.CAP_ROUND);

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
	public void paint() {
		prepareMarketPulseInfo();
		if (marketPulseList == null || marketPulseList.size() == 0) {
			// no enough data found
			pad.setColor(colorGreen);
			pad.drawString(
					"No market info for market: "
							+ MarketTypes.getMarketName(marketType), 200, 290);
			return;
		}

		// prepare the font metrics
		Font currentFont = pad.getFont();
		font = currentFont.deriveFont(currentFont.getSize() * 1.7F);
		//midFont = currentFont.deriveFont(currentFont.getSize() * 1.5F);
		pad.setFont(font);
		fontMetrics = frame.getFontMetrics(font);

		// measures
		updayY = imageHeight - Y_BOTTOM - BOX_GAP;
		above50Y = updayY - UPDAY_HEIGHT - 3 * BOX_GAP;
		chartWidth = imageWidth - X_LEFT - X_RIGHT;
		chartHeight = UPDAY_HEIGHT + ABOVE50_HEIGHT + 4 * BOX_GAP;
		origX = X_LEFT;
		origY = updayY;

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

		// fill the background of boxes
		savedColor = pad.getColor();
		pad.setColor(chartColor);
		pad.fillRect(origX, origY - UPDAY_HEIGHT, chartWidth, UPDAY_HEIGHT);
		pad.fillRect(origX, above50Y - ABOVE50_HEIGHT, chartWidth,
				ABOVE50_HEIGHT);
		pad.setColor(savedColor);

		// Gif maker complains the too many colors for following
		// JPEG - JUST SHOW NOTHING - WHY?
		// GradientPaint gp = new GradientPaint(50.0f, 50.0f, colorBlue,
		// 50.0f, 250.0f, colorGreen, true);
		// pad.setPaint(gp);

		String currentDate = "";
		String dt = "";
		try {
			if (marketPulseList != null) {
				dt = ((MarketPulse) marketPulseList
						.get(marketPulseList.size() - 1)).getMDate();

				// do not use static formatter!
				SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat(
						"MMM dd, yyyy");
				SimpleDateFormat YYYY_MM_DD_formatter = new SimpleDateFormat(
						"yyyy/MM/dd");
				currentDate = MMM_DD_YYYY_formatter.format(YYYY_MM_DD_formatter
						.parse(dt));
			}
		} catch (Exception e) {
			LOGGER.warn("Date format error:" + dt);
			currentDate = DateUtil.getDateStringInLongFormat(null);
		}

		// draw title
		savedColor = pad.getColor();
		pad.setColor(colorBlack);
		String title1 = "Chart of Bullish Percent Index for "
				+ MarketTypes.getMarketName(marketType) + ", as of "
				+ currentDate; // + ", " + REGULAR_COPYRIGHT;
		String title2 = "Chart of Advance-Decline for "
				+ MarketTypes.getMarketName(marketType) + ", as of "
				+ currentDate; // + ", " + REGULAR_COPYRIGHT;

		pad.drawString(title1, origX, Y_TOP + TEXT_HEIGHT + 1);
		pad.drawString(title2, origX, above50Y + TEXT_HEIGHT + SMALL_GAP + SMALL_GAP);
		pad.setColor(savedColor);

	}

	/*
	 * Draw these rectangles last so to overwrite others.
	 */
	private void drawChartFramePost() {
		pad.setColor(colorBlack);
		pad.drawRect(origX, origY - UPDAY_HEIGHT, chartWidth, UPDAY_HEIGHT);
		pad.drawRect(origX, above50Y - ABOVE50_HEIGHT, chartWidth,
				ABOVE50_HEIGHT);
	}

	/**
	 * Draw the price chart.
	 */
	private void drawBarChart() {
		if (marketPulseList == null || marketPulseList.size() == 0) {
			pad.drawString("Data Error", 200, 200);
			return;
		}

		findMaxMin();
		MarketUtil.findAverageBPI(marketPulseList, startIndex);

		int totalItems = marketPulseList.size() - startIndex;
		double xStep = (double) chartWidth / (totalItems + 1);
		double quarterWidth = (double) chartWidth / 4;
		double yStep = (double) ABOVE50_HEIGHT / 100;
		double yStep2 = 1;
		if (maxAD > minAD) {
			yStep2 = (double) (UPDAY_HEIGHT - AD_TOP - AD_BOTTOM)
					/ (maxAD - minAD);
		}

		// debug
		// System.out.println("In draw, size: " + totalItems + "/" +
		// marketPulseList.size());

		double xx = origX + xStep;
		double above50Ht = 0, updayHt = 0, above50AvgHt = 0, updayAvgHt = 0;
		Vector monthList = new Vector();
		String prevMonthNum = "";
		double minADHt = origY - AD_BOTTOM;
		double maxADHt = minADHt - UPDAY_HEIGHT + AD_TOP;
		boolean bpiLabOnTop = true;
		boolean adLineLabOnTop = true;

		int[] xPolylineAbove50 = new int[totalItems];
		int[] yPolylineAbove50 = new int[totalItems];
		int[] xPolylineUpday = new int[totalItems];
		int[] yPolylineUpday = new int[totalItems];
		int[] xPolylineAbove50Avg = new int[totalItems];
		int[] yPolylineAbove50Avg = new int[totalItems];
		int totalPoints = 0;
		int[] xPolylineUpdayAvg = new int[totalItems];
		int[] yPolylineUpdayAvg = new int[totalItems];
		int totalPointsUpday = 0;

		// display scale ruler
		double displayHt = 0;

		// 0, 10, 20, ..., 100
		for (int i = 0; i <= 10; i++) {
			double ht = (double) yStep * i * 10;
			displayHt = above50Y - ht;
			String text = String.valueOf(i * 10);
			double textWidth = (double) fontMetrics.stringWidth(text);

			// debug
			// System.out.println("draw: " + runP);

			if (i % 2 == 1) {
				pad.setColor(colorBlue);
				pad.drawString(text, (int) (origX - textWidth - 3),
						(int) displayHt);
			}

			// // draw a line
			Stroke savedStroke = pad.getStroke();
			if (i == 3 || i == 7) {
				pad.setColor(colorGreen);
				pad.setStroke(dashStroke);
			} else {
				pad.setColor(colorDark);
			}

			pad.drawLine(origX, (int) displayHt, imageWidth - X_RIGHT,
					(int) displayHt);
			if (i == 3 || i == 7) {
				pad.setStroke(savedStroke);
			}

			/*
			 * displayHt = origY - ht; pad.setColor(colorDark);
			 * pad.drawLine(origX, (int) displayHt, imageWidth - X_RIGHT, (int)
			 * displayHt);
			 * 
			 * if (i % 2 == 1) { pad.setColor(colorBlue); pad.drawString(text,
			 * (int) (origX - textWidth - 3), (int) displayHt); }
			 */

		}

		int ballSize = 4;
		for (int i = startIndex; i < marketPulseList.size(); i++, xx += xStep) {
			MarketPulse marketPulse = (MarketPulse) marketPulseList.get(i);

			// study line
			/*
			 * if (i % 20 == 0) { pad.setColor(colorDark);
			 * 
			 * // draw the vertical lines, but leave some gap on the left //if
			 * (xx > origX + SMALL_GAP + leftTextWidth + SMALL_GAP) {
			 * pad.drawLine((int)xx, updayY, (int) xx, (int) origY -
			 * UPDAY_HEIGHT); pad.drawLine((int)xx, above50Y, (int) xx, (int)
			 * above50Y - ABOVE50_HEIGHT); //} }
			 */

			// figure out the month string
			if (prevMonthNum.equals("")) {
				prevMonthNum = getMonthNumber(marketPulse.getMDate());
			} else {
				String thisMonthNum = getMonthNumber(marketPulse.getMDate());
				if (!prevMonthNum.equals(thisMonthNum)) {
					monthList.add(String.valueOf(xx) + "#"
							+ DateUtil.getShortMonthString(thisMonthNum));

					// draw lines
					pad.setColor(colorDark);
					pad.drawLine((int) xx, updayY, (int) xx, (int) origY
							- UPDAY_HEIGHT);
					pad.drawLine((int) xx, above50Y, (int) xx, (int) above50Y
							- ABOVE50_HEIGHT);
				}

				prevMonthNum = thisMonthNum;
			}

			// find the positions
			above50Ht = above50Y - (double) yStep
					* marketPulse.getTotalAbove50() * 100;
			updayHt = origY - AD_BOTTOM - (double) yStep2
					* (marketPulse.getTotalUpday() - minAD);
			above50AvgHt = above50Y - (double) yStep
					* marketPulse.getAverageBPI() * 100;
			updayAvgHt = origY - AD_BOTTOM - (double) yStep2
					* (marketPulse.getAverageUpday() - minAD);

			// check if need to display the BPI label on the bottom area
			if (xx < quarterWidth) {
				if (bpiLabOnTop && marketPulse.getTotalAbove50() > 0.7) {
					bpiLabOnTop = false;
				}
				
				if (adLineLabOnTop && updayHt  < (origY - UPDAY_HEIGHT + 3 * SMALL_GAP + TEXT_HEIGHT)) {
					adLineLabOnTop = false;
				}
			}

			// dots
			// pad.drawLine((int)xx, (int) volumeY, (int) xx, (int) (volumeY -
			// volumeHt));
			// pad.setColor(colorRed);
			// pad.fillOval((int)xx, (int) above50Ht, ballSize, ballSize);
			// pad.fillOval((int)xx, (int) updayHt, ballSize, ballSize);

			xPolylineAbove50[totalPoints] = (int) xx;
			yPolylineAbove50[totalPoints] = (int) above50Ht;
			xPolylineAbove50Avg[totalPoints] = (int) xx;
			yPolylineAbove50Avg[totalPoints] = (int) above50AvgHt;
			xPolylineUpday[totalPoints] = (int) xx;
			yPolylineUpday[totalPoints] = (int) updayHt;
			totalPoints++;

			if (updayAvgHt <= minADHt && updayAvgHt >= maxADHt) {
				xPolylineUpdayAvg[totalPointsUpday] = (int) xx;
				yPolylineUpdayAvg[totalPointsUpday] = (int) updayAvgHt;
				totalPointsUpday++;
			}

		}

		// display average volume - line
		pad.setColor(colorDark);
		// volumeHt = (double) stockExt.getAverageVolume() * volumeStep;
		// int volumeRealHt = (int) (volumeY - volumeHt);
		// pad.drawLine(origX, volumeRealHt, imageWidth - X_RIGHT,
		// volumeRealHt);

		// display max volume
		pad.setColor(colorBlue);
		// pad.drawString(getMaxVolumeString(), origX + SMALL_GAP, volumeY -
		// VOLUME_HEIGHT + SMALL_GAP + TEXT_HEIGHT);

		// draw polyline
		if (totalPoints > 0) {
			Stroke savedStroke = pad.getStroke();
			pad.setStroke(thickStroke);
			pad.setColor(colorRed);
			pad.drawPolyline(xPolylineAbove50, yPolylineAbove50, totalPoints);
			pad.drawPolyline(xPolylineUpday, yPolylineUpday, totalPoints);

			pad.setColor(colorBlue);
			pad.drawPolyline(xPolylineAbove50Avg, yPolylineAbove50Avg,
					totalPoints);
			pad.drawPolyline(xPolylineUpdayAvg, yPolylineUpdayAvg,
					totalPointsUpday);
			pad.setStroke(savedStroke);
		}

		displayMonth(monthList, origY);
		displayMonth(monthList, above50Y);

		drawBPILabel(bpiLabOnTop);
		drawADLineLabel(adLineLabOnTop);

	}

	private void drawBPILabel(boolean showBPIOnTop) {
		String bpiStr = "BPI";
		String bpiStr2 = MarketUtil.BPI_AVERAGE_RANGE + "-Day EMA";
		int strLen = (int) (fontMetrics.stringWidth(bpiStr));
		int strLen2 = (int) (fontMetrics.stringWidth(bpiStr2));

		int bpiStrY = origY - chartHeight + 3 * SMALL_GAP + TEXT_HEIGHT;
		int bpiStrX = (int) (origX + 5 * SMALL_GAP);
		if (!showBPIOnTop) {
			bpiStrY = above50Y - 3 * SMALL_GAP - TEXT_HEIGHT;
		}

		// backgroud
		pad.setColor(chartColor);
		pad.fillRect(bpiStrX - 18, bpiStrY - 20, strLen + strLen2 + 37, 23);
		pad.setColor(colorBlack);
		pad.drawRect(bpiStrX - 18, bpiStrY - 20, strLen + strLen2 + 37, 23);

		// bpi
		pad.setColor(colorRed);
		pad.fillRect(bpiStrX - 12, bpiStrY - 11, 6, 6);
		pad.drawString(bpiStr, bpiStrX, bpiStrY);

		// 50-ema
		pad.setColor(colorBlue);
		bpiStrX = (int) (origX + 5 * SMALL_GAP);
		pad.fillRect(bpiStrX + strLen + 4, bpiStrY - 11, 6, 6);
		pad.drawString(bpiStr2, bpiStrX + strLen + 16, bpiStrY);
	}

	private void drawADLineLabel(boolean onTop) {
		String adStr = "ADLine";
		String adStr2 = MarketUtil.UPDAY_AVERAGE_RANGE + "-Day EMA";
		int strLen = (int) (fontMetrics.stringWidth(adStr));
		int strLen2 = (int) (fontMetrics.stringWidth(adStr2));

		int adStrY = 0;
		int adStrX = (int) (origX + 5 * SMALL_GAP);
		if (onTop) {
			adStrY = origY - UPDAY_HEIGHT + 3 * SMALL_GAP + TEXT_HEIGHT;
		} else {
			adStrY = origY - 3 * SMALL_GAP - TEXT_HEIGHT;
		}

		// backgroud
		pad.setColor(chartColor);
		pad.fillRect(adStrX - 18, adStrY - 20, strLen + strLen2 + 37, 23);
		pad.setColor(colorBlack);
		pad.drawRect(adStrX - 18, adStrY - 20, strLen + strLen2 + 37, 23);

		// bpi
		pad.setColor(colorRed);
		pad.fillRect(adStrX - 12, adStrY - 11, 6, 6);
		pad.drawString(adStr, adStrX, adStrY);

		// 50-ema
		pad.setColor(colorBlue);
		adStrX = (int) (origX + 5 * SMALL_GAP);
		pad.fillRect(adStrX + strLen + 4, adStrY - 11, 6, 6);
		pad.drawString(adStr2, adStrX + strLen + 16, adStrY);
	}

	private void displayMonth(Vector monthList, double positionY) {
		// // display the months
		pad.setColor(colorBlue);
		for (int i = 0; i < monthList.size(); i++) {
			String monthPosStr = (String) monthList.get(i);
			Vector list = StringHelper.mySplit(monthPosStr, "#");
			if (list != null && list.size() == 2) {
				double xxP = (new Double((String) list.get(0))).doubleValue();
				String monthStr = (String) list.get(1);

				pad.drawLine((int) xxP, (int) positionY, (int) xxP,
						(int) (positionY - SCALE_LENGTH));
				double halfStrLen = (double) fontMetrics.stringWidth(monthStr) / 2;

				// draw the string, only if it fits insider of the box.
				double xxPLoc = -1;
				if (xxP - halfStrLen < origX) {
					xxPLoc = origX + SMALL_GAP;
				} else if (xxP + halfStrLen > origX + chartWidth) {
					// do not draw
					xxPLoc = origX + chartWidth - halfStrLen - halfStrLen;
				} else {
					xxPLoc = xxP - halfStrLen;
				}

				if (xxPLoc != -1) {
					pad.drawString(monthStr, (int) xxPLoc, (int) positionY
							- SMALL_GAP);
				}

			}
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
	 * Find the price list for the range.
	 * 
	 * @throws Exception
	 */
	private void prepareMarketPulseInfo() {
		int EXTRA_DAYS = 100;
		DBComm database = new DBComm();
		MarketPulseDAO marketDao = new MarketPulseDAO();

		try {
			database.openConnection();
			marketDao.setDatabase(database);

			if (marketType != null) {
				// System.out.println("start date: " + dataStDate);
				if (dataStDate == null || dataStDate.equals("")) {
					// System.out.println("start date ... is null");
					// set the dates by screenIndex
					checkStEndDates(marketDao);
				}
					
				String startDate = OracleUtil.getPrevDateInOracleFormat2(
						dataStDate, 0, EXTRA_DAYS);
				String endDate = OracleUtil.getPrevDateInOracleFormat2(
						dataEdDate, 0, 0);

				marketPulseList = marketDao.getMarketPulseListFromDB(
						marketType, startDate, endDate);
				startIndex = MarketUtil.findStartIndexForMarketPulseList(
						marketPulseList, dataStDate);

				// System.out.println("start date: " + startDate);
				// System.out.println("start date in real: " + dataStDate);
				// System.out.println("startIndex: " + startIndex);
				
			} else {
				// error
			}

		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Error in loading stock price info in Draw Market Chart: "
					+ e.getMessage());
		}

		try {
			database.closeConnection();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private void checkStEndDates(MarketPulseDAO marketDao) {
		// if these twodatgesare not set, get default

		try {
			// date pairs for the screen index
			Vector dateList = MarketCachePool.getMarketPulseDateList(
					marketType, marketDao);

			if (dateList != null && screenIndex >= 0
					&& screenIndex < dateList.size() - 1) {
				dataStDate = (String) dateList.get(screenIndex + 1);
				dataEdDate = (String) dateList.get(screenIndex);

				// debug
				System.out.println("draw chart (BPI), start date: "
						+ dataStDate + ", end date: " + dataEdDate);
			}

		} catch (Exception e) {
			// ignore
		}
	}

	private void findMaxMin() {
		double upday = 0;

		// reset updays, and find average
		MarketUtil.findAverageUpday(marketPulseList, startIndex);

		for (int i = startIndex; i < marketPulseList.size(); i++) {
			MarketPulse marketPulse = (MarketPulse) marketPulseList.get(i);
			upday = marketPulse.getTotalUpday();

			if (i == startIndex) {
				maxAD = upday;
				minAD = upday;
			} else {
				if (upday > maxAD) {
					maxAD = upday;
				}

				if (upday < minAD) {
					minAD = upday;
				}
			}
		}
	}

	public int getScreenIndex() {
		return screenIndex;
	}

	public void setScreenIndex(int screenIndex) {
		this.screenIndex = screenIndex;
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
	public String getMarketType() {
		return marketType;
	}

	/**
	 * @param string
	 */
	public void setMarketType(String string) {
		marketType = string;
	}

	public String getDataStDate() {
		return dataStDate;
	}

	public void setDataStDate(String dataStDate) {
		this.dataStDate = dataStDate;
	}

	public String getDataEdDate() {
		return dataEdDate;
	}

	public void setDataEdDate(String dataEdDate) {
		this.dataEdDate = dataEdDate;
	}
	
	

}
