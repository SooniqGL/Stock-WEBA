package com.greenfield.ui.graph;



import com.greenfield.ui.cache.ScanCachePool;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.object.scan.ScanChartVO;
import com.greenfield.common.object.scan.ScanHistoryVO;
import com.greenfield.common.base.AppContext;
import com.greenfield.common.constant.ScanModes;
import com.greenfield.common.dao.analyze.ScanDAO;
import com.greenfield.common.dao.analyze.StockAdminDAO;
import com.greenfield.common.object.stock.StockDailyInfo;

import java.awt.Color;
import java.awt.Font;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 * 
 * @author QZ69042
 */
public class DrawScanChart extends BaseGraphics {
	private static final Logger LOGGER = Logger.getLogger(DrawReportChart2.class); 
	
	public final static String REGULAR_COPYRIGHT = ""; // "Copyright 2006, Sooniq Technology Corporation";
	public final static String SHORT_COPYRIGHT = ""; // Copyright 2006, Sooniq Tech.";

	// variables for input and stock related.
	private String scanKey;
	private String scanIndex;
	private String scanStDate;
	private String scanEdDate;

	//
	private Vector<ScanChartVO> reportList; // queried from DB

	// local variables - for color and style
	private Color colorBlack;
	//private Color colorWhite;
	private Color colorRed;
	private Color colorGreen;
	private Color colorBlue;
	//private Color colorDark;
	//private Color colorYellow;
	//private Color colorLtYellow;
	//private Color colorDYellow;
	//private Color colorLtBlue;
	//private Color colorPink;
	private Color imageColor;
	private Color chartColor;
	private Color savedColor;
	//private BasicStroke dashStroke;

	// font
	private Font font;
	//private Font bigFont;
	//private Font midFont;

	//private FontMetrics fontMetrics;

	// format
	DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
	DecimalFormat roundFormatter = new DecimalFormat("0");

	// measures
	private int chartWidth;
	private int chartHeight;
	private int origX;
	private int origY;

	// STEPS
	int totalItems = 0;
	double xStep = 0;
	double yStep = 0;

	// MEASURE CONSTANTS
	public static final int DEFAULT_IMAGE_WIDTH = 650;
	public static final int DEFAULT_IMAGE_HEIGHT = 500;

	private int X_LEFT = 25;
	private int X_RIGHT = 5;
	private int Y_TOP = 30;
	private int Y_BOTTOM = 30;

	//private int TITLE_HEIGHT = 20;
	//private int BOX_GAP = 5;
	private int TEXT_HEIGHT = 15;
	//private int VERTICAL_TEXT_X1 = 15; // price
	//private int BIG_GAP = 20;
	private int SMALL_GAP = 5;
	//private int TINY_GAP = 3;
	//private int SCALE_LENGTH = 3;

	public void init() {
		colorBlack = Color.black;
		//colorWhite = Color.white;
		colorRed = Color.red;
		colorGreen = Color.green;
		colorBlue = Color.blue;
		//colorYellow = Color.yellow;
		//colorLtYellow = new Color(255, 235, 100);
		//colorDYellow = new Color(230, 230, 0);
		//colorLtBlue = new Color(100, 100, 255);
		//colorDark = new Color(200, 200, 200);
		//colorPink = new Color(255, 0, 255);
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
			AppContext.init();
			
			// debug
			// System.out.println("in drawicon ...");
			String fileName = "c:\\temp\\good.jpeg";

			String scanKey = ScanModes.CROSS_20_100_UP;
			String scanIndex = "100";
			DrawScanChart draw = new DrawScanChart();
			draw.setScanKey(scanKey);
			draw.setScanIndex(scanIndex);
			
			draw.init();
			draw.build(DEFAULT_IMAGE_WIDTH, DEFAULT_IMAGE_HEIGHT);

			// save the image to file
			draw.saveAsJpeg(fileName);
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
			pad.drawString("Error: No chart for this report: " + scanKey, 200, 290);
			return;
		}

		// prepare the font metrics
		if (font == null) {
			font = pad.getFont();
		}

		//fontMetrics = frame.getFontMetrics(font);

		// Define the location
		origX = X_LEFT;
		origY = imageHeight - Y_BOTTOM;

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
		
		// line at the cut point
		pad.setColor(colorGreen);
		int cutPtX = (int) (origX + ScanChartHelper.BEFORE_DAYS * xStep);
		pad.drawLine(cutPtX, origY, cutPtX,
				(int) origY - chartHeight);

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
		SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat(
				"MMM dd, yyyy");
		String currentDate = MMM_DD_YYYY_formatter.format(new Date());
		savedColor = pad.getColor();
		pad.setColor(colorBlue);
		String title = "Chart for report, " + currentDate; // + ", " + REGULAR_COPYRIGHT;
		pad.drawString(title, origX, Y_TOP - TEXT_HEIGHT + SMALL_GAP + 2);
		pad.setColor(savedColor);

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
	 * Price is narrowed to: PRICE_BOTTOM_CUT to PRICE_TOP_CUT
	 */
	private void drawReportChart() {
		if (reportList == null || reportList.size() == 0) {
			pad.drawString("Data Error", 200, 200);
			return;
		}

		// define the steps - 
		totalItems = ScanChartHelper.BEFORE_DAYS + ScanChartHelper.AFTER_DAYS;
		xStep = (double) chartWidth / (totalItems + 1);
		yStep = (double) chartHeight / (ScanChartHelper.PRICE_TOP_CUT - ScanChartHelper.PRICE_BOTTOM_CUT);

		// debug
		// System.out.println("In draw, size: " + totalItems);

		// more variables
		double xxZero = origX + xStep;

		for (int i = 0; i < reportList.size(); i ++) {
			ScanChartVO scanChartVo = (ScanChartVO) reportList.get(i);
			if (scanChartVo.getPriceList() == null || scanChartVo.getPriceList().size() == 0) {
				LOGGER.warn("Error: price list is empty for " + scanChartVo.getScanDate() 
					+ ", ticker: " + scanChartVo.getTicker());
				continue;
			}

			int[] xPolylinePercent = new int[totalItems];
			int[] yPolylinePercent = new int[totalItems];
			int totalPoints = 0;
			
			double xx = xxZero;  // start
			Vector<StockDailyInfo> priceList = scanChartVo.getPriceList();
			for (int j = 0; j < priceList.size(); j ++) {
				StockDailyInfo info = priceList.get(j);
				double priceHt = origY - (info.getAdjustedClose()- ScanChartHelper.PRICE_BOTTOM_CUT) * yStep;
				xPolylinePercent[totalPoints] = (int) xx;
				yPolylinePercent[totalPoints] = (int) priceHt;
				totalPoints++;
				xx += xStep;
			}

			if (totalPoints > 0) {
				pad.setColor(generateColor(i));
				pad.drawPolyline(xPolylinePercent, yPolylinePercent, totalPoints);
			}
		}
	}


	private Color generateColor(int i) {

		Color[] colorArray = { Color.black, Color.blue, Color.cyan, Color.darkGray, Color.gray, Color.green,
				Color.lightGray, Color.magenta, Color.orange, Color.pink, Color.red, Color.white, Color.yellow };

		int index = i % colorArray.length;
		//Random rand = new Random(i);
		//int r1 = rand.nextInt(index);

		return colorArray[index];
	}

	/**
	 * Find the price list for the range.
	 * 
	 * @throws Exception
	 */
	private void prepareReportInfo() {
		DBComm database = new DBComm();
		ScanDAO scanDao = new ScanDAO();
		StockAdminDAO stockAdminDao = new StockAdminDAO();

		try {
			database.openConnection();
			scanDao.setDatabase(database);
			stockAdminDao.setDatabase(database);
			if (scanKey != null && !scanKey.equals("")) {
				checkStEndDates(scanDao);
				reportList = ScanChartHelper.loadScanHistoryList(scanDao, stockAdminDao, scanKey, scanStDate, scanEdDate);
			}

		} catch (Exception e) {
			e.printStackTrace();
			reportList = null;
			LOGGER.warn("Error in loading scan report info in DrawScanChart: " + e.getMessage());
		} finally {
			try {
				database.closeConnection();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}

	}

	@SuppressWarnings("rawtypes")
	private void checkStEndDates(ScanDAO scanDao) {
		// if these twodatgesare not set, get default
		int scanIndexInt = 0;

		try {
			scanIndexInt = (new Integer(scanIndex)).intValue();
			Vector scanObjList = ScanCachePool.getScanDateList(scanKey, scanDao);

			if (scanObjList != null && scanIndexInt >= 0
					&& scanIndexInt < scanObjList.size()) {
				ScanHistoryVO history = (ScanHistoryVO) scanObjList.get(scanIndexInt);
				
				scanStDate = history.getCloseDate();   // close date = scan date, but in yyyy/mm/dd format
				scanEdDate = history.getCloseDate();   // same now - no used

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