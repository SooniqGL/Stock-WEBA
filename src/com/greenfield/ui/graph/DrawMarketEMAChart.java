/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.greenfield.ui.graph;

import com.greenfield.ui.cache.MarketCachePool;
import com.greenfield.common.constant.MarketTypes;
import com.greenfield.common.dao.analyze.MarketPulseDAO;
import com.greenfield.common.util.DBComm;
import com.greenfield.common.util.DateUtil;
import com.greenfield.common.util.MarketUtil;
import com.greenfield.common.util.OracleUtil;
import com.greenfield.common.util.StringHelper;
import com.greenfield.common.object.market.MarketPulse;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Stroke;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Vector;

import org.apache.log4j.Logger;

/**
 *
 * @author QZ69042
 */
public class DrawMarketEMAChart extends BaseGraphics {
	private static final Logger LOGGER = Logger.getLogger(DrawMarketEMAChart.class); 
	
	// which time to draw
	public final static int DEFAULT_PERIOD = 6;   // six months chart

	// option values
	// public final static String GROWTH_OPTION = "G";
	public final static String REGULAR_COPYRIGHT = "Copyright 2006, Sooniq Technology Corporation";
	// public final static String SHORT_COPYRIGHT = "Copyright 2006, Sooniq Tech.";

	// variables for input and stock related.
	private String marketType;
	private int screenIndex;
	private String option;

	// data need to display
	private String dataStDate;
	private String dataEdDate;
	private String extraOption;

	private int startIndex;
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
	private FontMetrics fontMetrics;

	// format
	DecimalFormat oneDigitFormatter = new DecimalFormat("0.0");
	DecimalFormat roundFormatter = new DecimalFormat("0");

	// measures
	private int chartWidth;
	// private int chartHeight;
	private int origX;
	private int origY;
	private int y_20g50;
	//private int y_20g100;
	private int y_50g100;

	// MEASURE CONSTANTS
	public static final int DEFAULT_IMAGE_WIDTH = 1260;
	public static final int DEFAULT_IMAGE_HEIGHT = 700;

	private int X_LEFT = 35;
	private int X_RIGHT = 12;
	private int Y_TOP = 10;
	private int Y_BOTTOM = 12;
	private int BOX_HEIGHT = 300; // 110; // for 20g50 box, 20g100 box, 50g100 box
	private int BOX_GAP = 15;
	private int SCALE_LENGTH = 3; // bars in y axis 
	private int TEXT_HEIGHT = 15;
	private int SMALL_GAP = 5;
	private int TINY_GAP = 3;

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
		float[] dashPattern = {10f};
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
			pad.drawString("No market info for market: " + MarketTypes.getMarketName(marketType), 200, 290);
			return;
		}

		if (extraOption == null || extraOption.equals("")) {
			extraOption = "A";
		}
		
		// prepare the font metrics
		Font currentFont = pad.getFont();
		font = currentFont.deriveFont(currentFont.getSize() * 1.7F);
		pad.setFont(font);
		fontMetrics = frame.getFontMetrics(font);

		// measures
		y_50g100 = imageHeight - Y_BOTTOM - TEXT_HEIGHT;
		// y_20g100 = y_50g100 - BOX_HEIGHT - BOX_GAP - TEXT_HEIGHT;
		//y_20g50 = y_20g100 - BOX_HEIGHT - BOX_GAP - TEXT_HEIGHT;
		y_20g50 = y_50g100 - BOX_HEIGHT - BOX_GAP - TEXT_HEIGHT;

		chartWidth = imageWidth - X_LEFT - X_RIGHT;
		// chartHeight = 3* BOX_HEIGHT + 2 * BOX_GAP + 2 * TEXT_HEIGHT;

		origX = X_LEFT;
		origY = y_50g100;

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

		// fill the background of boxes
		savedColor = pad.getColor();
		pad.setColor(chartColor);
		pad.fillRect(origX, origY - BOX_HEIGHT, chartWidth, BOX_HEIGHT);
		// pad.fillRect(origX, y_20g100 - BOX_HEIGHT, chartWidth, BOX_HEIGHT);
        pad.fillRect(origX, y_20g50 - BOX_HEIGHT, chartWidth, BOX_HEIGHT);
		pad.setColor(savedColor);

		// Gif maker complains the too many colors for following
		// JPEG - JUST SHOW NOTHING - WHY?
		//GradientPaint gp = new GradientPaint(50.0f, 50.0f, colorBlue,
		//		50.0f, 250.0f, colorGreen, true);
		//pad.setPaint(gp);

		String currentDate = "";
		String dt = "";
		try {
			if (marketPulseList != null) {
				dt = ((MarketPulse) marketPulseList.get(marketPulseList.size() - 1)).getMDate();

				// do not use static formatter!
				SimpleDateFormat MMM_DD_YYYY_formatter = new SimpleDateFormat("MMM dd, yyyy");
				SimpleDateFormat YYYY_MM_DD_formatter = new SimpleDateFormat("yyyy/MM/dd");
				currentDate = MMM_DD_YYYY_formatter.format(YYYY_MM_DD_formatter.parse(dt));
			}
		} catch (Exception e) {
			LOGGER.warn("Date format error:" + dt);
			currentDate = DateUtil.getDateStringInLongFormat(null);
		}

		// draw title
		savedColor = pad.getColor();
		pad.setColor(colorBlack);
		// String title1 = "Chart of Market (EMA50/100) for " +
		// MarketTypes.getMarketName(marketType) + ", as of " + currentDate +
		// ", " + REGULAR_COPYRIGHT;
		// String title2 = "Chart of Market (EMA20/100) for " +
		// MarketTypes.getMarketName(marketType) + ", as of " + currentDate +
		// ", " + REGULAR_COPYRIGHT;
		String title3 = "Chart of Market (EMA20/50/100) for "
				+ MarketTypes.getMarketName(marketType) + ", as of "
				+ currentDate; // + ", " + REGULAR_COPYRIGHT;

		// pad.drawString(title1, origX, y_20g100 + TEXT_HEIGHT + SMALL_GAP);
		// pad.drawString(title2, origX, y_20g50 + TEXT_HEIGHT + SMALL_GAP);
		pad.drawString(title3, origX, Y_TOP + TEXT_HEIGHT + SMALL_GAP);
		pad.setColor(savedColor);

	}

	/**
	 * Draw these rectangles last so to overwrite others.
	 */
	private void drawChartFramePost() {
            pad.setColor(colorBlack);
            pad.drawRect(origX, origY - BOX_HEIGHT, chartWidth, BOX_HEIGHT);
            //pad.drawRect(origX, y_20g100 - BOX_HEIGHT, chartWidth, BOX_HEIGHT);
            pad.drawRect(origX, y_20g50 - BOX_HEIGHT, chartWidth, BOX_HEIGHT);
	}

	/**
	 * Draw the price chart.
	 */
	@SuppressWarnings("unchecked")
	private void drawBarChart() {
		if (marketPulseList == null || marketPulseList.size() == 0) {
			pad.drawString("Data Error", 200, 200);
			return;
		}

		MarketUtil.findAverageEMA(marketPulseList, startIndex);

		int totalItems 	= marketPulseList.size() - startIndex;
		double xStep 	= (double) chartWidth / (totalItems + 1);
		double yStep 	= (double) BOX_HEIGHT / 100;

		// debug
		//System.out.println("In draw, size: " + totalItems + "/" + marketPulseList.size());

		double xx = origX + xStep;
		//double y20g50Ht = 0, y20g100Ht = 0, y50g100Ht = 0;
		//double y20g50AvgHt = 0, y20g100AvgHt = 0, y50g100AvgHt = 0;
		double y20g50Ht = 0, y50g100Ht = 0;
		double y20g50AvgHt = 0, y50g100AvgHt = 0;

		// to determine whether to display the label on top
		double quarterWidth = (double) chartWidth / 4;
		boolean lab20g50AtTop = true;
		//boolean lab20g100AtTop = true;
		boolean lab50g100AtTop = true;

                // month list to display month labels
		Vector monthList = new Vector();
		String prevMonthNum = "";
		

		int[] xPolyline20g50 = new int[totalItems];
		int[] yPolyline20g50 = new int[totalItems];
		//int[] xPolyline20g100 = new int[totalItems];
		//int[] yPolyline20g100 = new int[totalItems];
		int[] xPolyline50g100 = new int[totalItems];
		int[] yPolyline50g100 = new int[totalItems];
		int[] xPolyline20g50avg = new int[totalItems];
		int[] yPolyline20g50avg = new int[totalItems];
		//int[] xPolyline20g100avg = new int[totalItems];
		//int[] yPolyline20g100avg = new int[totalItems];
		int[] xPolyline50g100avg = new int[totalItems];
		int[] yPolyline50g100avg = new int[totalItems];
		int totalPoints = 0;

		// 0, 10, 20, ..., 100
		drawScaleLines(yStep, y_20g50);
		//drawScaleLines(yStep, y_20g100);
		drawScaleLines(yStep, y_50g100);


		int ballSize = 4;
		for (int i = startIndex; i < marketPulseList.size(); i ++, xx += xStep) {
			MarketPulse marketPulse = (MarketPulse) marketPulseList.get(i);

			// study line
                        /*
			if (i % 20 == 0) {
				pad.setColor(colorDark);

				// draw the vertical lines, but leave some gap on the left
				//if (xx > origX + SMALL_GAP + leftTextWidth + SMALL_GAP) {
					pad.drawLine((int)xx, origY, (int) xx, (int) origY - BOX_HEIGHT);
					pad.drawLine((int)xx, y_20g100, (int) xx, (int) y_20g100 - BOX_HEIGHT);
                                        pad.drawLine((int)xx, y_20g50, (int) xx, (int) y_20g50 - BOX_HEIGHT);
				//}
			} */

			// figure out the month string
			if (prevMonthNum.equals("")) {
				prevMonthNum = getMonthNumber(marketPulse.getMDate());
			} else {
				String thisMonthNum = getMonthNumber(marketPulse.getMDate());
				if (!prevMonthNum.equals(thisMonthNum)) {
					monthList.add(String.valueOf(xx) + "#" + DateUtil.getShortMonthString(thisMonthNum));

					// draw line right on the month start point
					pad.setColor(colorDark);
					pad.drawLine((int) xx, origY, (int) xx, (int) origY - BOX_HEIGHT);
					//pad.drawLine((int) xx, y_20g100, (int) xx, (int) y_20g100 - BOX_HEIGHT);
					pad.drawLine((int) xx, y_20g50, (int) xx, (int) y_20g50 - BOX_HEIGHT);
				}

				prevMonthNum = thisMonthNum;
			}

			// find the positions
			if (extraOption.equals("A")) {
				y20g50Ht = y_20g50 - (double) yStep * marketPulse.getTotal20g50() * 100;
				//y20g100Ht = y_20g100 - (double) yStep * marketPulse.getTotal20g100() * 100;
				y50g100Ht = y_50g100 - (double) yStep * marketPulse.getTotal50g100() * 100;
	
				y20g50AvgHt = y_20g50 - (double) yStep * marketPulse.getAverage20g50() * 100;
				//y20g100AvgHt = y_20g100 - (double) yStep * marketPulse.getAverage20g100() * 100;
				y50g100AvgHt = y_50g100 - (double) yStep * marketPulse.getAverage50g100() * 100;
	
				// check the quarter area to see if we need to display the label on
				// top or bottom
				if (xx < quarterWidth) { // if (i - startIndex < 60) {
					if (lab20g50AtTop && marketPulse.getTotal20g50() > 0.7) {
						lab20g50AtTop = false;
					}
	
					//if (lab20g100AtTop && marketPulse.getTotal20g100() > 0.7) {
					//	lab20g100AtTop = false;
					//}
	
					if (lab50g100AtTop && marketPulse.getTotal50g100() > 0.7) {
						lab50g100AtTop = false;
					}
				}
			} else if (extraOption.equals("B")) {
				y20g50Ht = y_20g50 - (double) yStep * marketPulse.getTotal20g50() * 100;
				// y20g100Ht = y_20g100 - (double) yStep * marketPulse.getTotal20g100() * 100;
				// y50g100Ht = y_50g100 - (double) yStep * marketPulse.getTotal50g100() * 100;
				y50g100Ht = y_50g100 - (double) yStep * marketPulse.getTotal20g100() * 100;
	
				y20g50AvgHt = y_20g50 - (double) yStep * marketPulse.getAverage20g50() * 100;
				// y20g100AvgHt = y_20g100 - (double) yStep * marketPulse.getAverage20g100() * 100;
				// y50g100AvgHt = y_50g100 - (double) yStep * marketPulse.getAverage50g100() * 100;
				y50g100AvgHt = y_50g100 - (double) yStep * marketPulse.getAverage20g100() * 100;
	
				// check the quarter area to see if we need to display the label on
				// top or bottom
				if (xx < quarterWidth) { // if (i - startIndex < 60) {
					if (lab20g50AtTop && marketPulse.getTotal20g50() > 0.7) {
						lab20g50AtTop = false;
					}
	
					//if (lab20g100AtTop && marketPulse.getTotal20g100() > 0.7) {
					//	lab20g100AtTop = false;
					//}
	
					if (lab50g100AtTop && marketPulse.getTotal20g100() > 0.7) {
						lab50g100AtTop = false;
					}
				}
			} else {  // "C"
				// y20g50Ht = y_20g50 - (double) yStep * marketPulse.getTotal20g50() * 100;
				// y20g100Ht = y_20g100 - (double) yStep * marketPulse.getTotal20g100() * 100;
				y20g50Ht = y_20g50 - (double) yStep * marketPulse.getTotal20g100() * 100;
				y50g100Ht = y_50g100 - (double) yStep * marketPulse.getTotal50g100() * 100;
	
				// y20g50AvgHt = y_20g50 - (double) yStep * marketPulse.getAverage20g50() * 100;
				// y20g100AvgHt = y_20g100 - (double) yStep * marketPulse.getAverage20g100() * 100;
				y20g50AvgHt = y_20g50 - (double) yStep * marketPulse.getAverage20g100() * 100;
				y50g100AvgHt = y_50g100 - (double) yStep * marketPulse.getAverage50g100() * 100;
	
				// check the quarter area to see if we need to display the label on
				// top or bottom
				if (xx < quarterWidth) { // if (i - startIndex < 60) {
					if (lab20g50AtTop && marketPulse.getTotal20g100() > 0.7) {
						lab20g50AtTop = false;
					}
	
					//if (lab20g100AtTop && marketPulse.getTotal20g100() > 0.7) {
					//	lab20g100AtTop = false;
					//}
	
					if (lab50g100AtTop && marketPulse.getTotal50g100() > 0.7) {
						lab50g100AtTop = false;
					}
				}
			}
			
			// dots
			//pad.drawLine((int)xx, (int) volumeY, (int) xx, (int) (volumeY - volumeHt));
			//pad.setColor(colorRed);
			//pad.fillOval((int)xx, (int) above50Ht, ballSize, ballSize);
			//pad.fillOval((int)xx, (int) updayHt,   ballSize, ballSize);

			xPolyline20g50[totalPoints] = (int) xx;
			yPolyline20g50[totalPoints] = (int) y20g50Ht;
			//xPolyline20g100[totalPoints] = (int) xx;
			//yPolyline20g100[totalPoints] = (int) y20g100Ht;
			xPolyline50g100[totalPoints] = (int) xx;
			yPolyline50g100[totalPoints] = (int) y50g100Ht;

			xPolyline20g50avg[totalPoints] = (int) xx;
			yPolyline20g50avg[totalPoints] = (int) y20g50AvgHt;
			//xPolyline20g100avg[totalPoints] = (int) xx;
			//yPolyline20g100avg[totalPoints] = (int) y20g100AvgHt;
			xPolyline50g100avg[totalPoints] = (int) xx;
			yPolyline50g100avg[totalPoints] = (int) y50g100AvgHt;
			
			totalPoints++;

		}


		// display average volume - line
		pad.setColor(colorDark);
		//volumeHt =  (double) stockExt.getAverageVolume() * volumeStep;
		//int volumeRealHt = (int) (volumeY - volumeHt);
		//pad.drawLine(origX, volumeRealHt, imageWidth - X_RIGHT, volumeRealHt);

		// display max volume
		pad.setColor(colorBlue);
		//pad.drawString(getMaxVolumeString(), origX + SMALL_GAP, volumeY - VOLUME_HEIGHT + SMALL_GAP + TEXT_HEIGHT);


		// draw polyline
		if (totalPoints > 0) {
			Stroke savedStroke = pad.getStroke();
			pad.setStroke(thickStroke);
			pad.setColor(colorRed);
			pad.drawPolyline(xPolyline20g50, yPolyline20g50, totalPoints);
			//pad.drawPolyline(xPolyline20g100, yPolyline20g100, totalPoints);
			pad.drawPolyline(xPolyline50g100, yPolyline50g100, totalPoints);

			pad.setColor(colorBlue);
			pad.drawPolyline(xPolyline20g50avg, yPolyline20g50avg, totalPoints);
			//pad.drawPolyline(xPolyline20g100avg, yPolyline20g100avg, totalPoints);
			pad.drawPolyline(xPolyline50g100avg, yPolyline50g100avg, totalPoints);
			pad.setStroke(savedStroke);
		}

		// draw month labels
		displayMonth(monthList, origY, true, false);
		displayMonth(monthList, y_20g50, true, false);
		//displayMonth(monthList, y_20g100, true, false);

		if (extraOption.equals("A")) {
			// draw curve labels
			drawCurveLabels("#{20 > 50}", y_20g50 - BOX_HEIGHT, lab20g50AtTop);
			//drawCurveLabels("#{20 > 100}", y_20g100 - BOX_HEIGHT, lab20g100AtTop);
			drawCurveLabels("#{50 > 100}", y_50g100 - BOX_HEIGHT, lab50g100AtTop);
		} else if (extraOption.equals("B")) {
			// draw curve labels
			drawCurveLabels("#{20 > 50}", y_20g50 - BOX_HEIGHT, lab20g50AtTop);
			//drawCurveLabels("#{20 > 100}", y_20g100 - BOX_HEIGHT, lab20g100AtTop);
			drawCurveLabels("#{20 > 100}", y_50g100 - BOX_HEIGHT, lab50g100AtTop);
		} else {
			// draw curve labels
			drawCurveLabels("#{20 > 100}", y_20g50 - BOX_HEIGHT, lab20g50AtTop);
			//drawCurveLabels("#{20 > 100}", y_20g100 - BOX_HEIGHT, lab20g100AtTop);
			drawCurveLabels("#{50 > 100}", y_50g100 - BOX_HEIGHT, lab50g100AtTop);
		}

	}

	private void drawScaleLines(double yStep, double startHt) {
		double displayHt = 0;
		for (int i = 0; i <= 10; i++) {
			double ht = (double) yStep * i * 10;
			displayHt = startHt - ht;
			String text = String.valueOf(i * 10);
			double textWidth = (double) fontMetrics.stringWidth(text);

			// debug
			// System.out.println("draw: " + runP);

			if (i % 2 == 1) {
				pad.setColor(colorBlue);
				pad.drawString(text, (int) (origX - textWidth - 3),
						(int) displayHt + SMALL_GAP);
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
		}
	}

	private void drawCurveLabels(String displayStr, int boxTopY, boolean onTop) {

		String displayStr2 = MarketUtil.EMA_AVERAGE_RANGE + "-Day EMA";
		int strLen = (int) (fontMetrics.stringWidth(displayStr));
		int strLen2 = (int) (fontMetrics.stringWidth(displayStr2));

		int posStrX = (int) (origX + 5 * SMALL_GAP); // fix all boxes
		int posStrY = boxTopY + SMALL_GAP + TEXT_HEIGHT;
		if (!onTop) {
			// redefine if display on bottom
			posStrY = boxTopY + BOX_HEIGHT + TINY_GAP - TEXT_HEIGHT;
		}

		// backgroud
		pad.setColor(chartColor);
		pad.fillRect(posStrX - 18, posStrY - 20, strLen + strLen2 + 37, 23);
		pad.setColor(colorBlack);
		pad.drawRect(posStrX - 18, posStrY - 20, strLen + strLen2 + 37, 23);

		// str
		pad.setColor(colorRed);
		pad.fillRect(posStrX - 12, posStrY - 11, 6, 6);
		pad.drawString(displayStr, posStrX, posStrY);

		// 50-ema
		pad.setColor(colorBlue);
		posStrX = (int) (origX + 5 * SMALL_GAP);
		pad.fillRect(posStrX + strLen + 4, posStrY - 11, 6, 6);
		pad.drawString(displayStr2, posStrX + strLen + 16, posStrY);
	}


	private void displayMonth(Vector monthList, double positionY,
			boolean showMonth, boolean isAbove) {
		// // display the months
		pad.setColor(colorBlue);
		for (int i = 0; i < monthList.size(); i++) {
			String monthPosStr = (String) monthList.get(i);
			Vector list = StringHelper.mySplit(monthPosStr, "#");
			if (list != null && list.size() == 2) {
				double xxP = (new Double((String) list.get(0))).doubleValue();
				String monthStr = (String) list.get(1);

				if (isAbove) {
					pad.drawLine((int) xxP, (int) positionY, (int) xxP,
							(int) (positionY - SCALE_LENGTH));
				} else {
					pad.drawLine((int) xxP, (int) positionY, (int) xxP,
							(int) (positionY + SCALE_LENGTH));
				}

				if (showMonth) {
					double halfStrLen = (double) fontMetrics
							.stringWidth(monthStr) / 2;

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
						if (isAbove) {
							pad.drawString(monthStr, (int) xxPLoc,
									(int) positionY - SMALL_GAP);
						} else {
							pad.drawString(monthStr, (int) xxPLoc,
									(int) positionY + SCALE_LENGTH
											+ TEXT_HEIGHT);
						}
					}
				}

			}
		}
	}

	private void drawLine(int ax, double ay, int bx, double by,
		int origX, int origY, double xStep, double yStep,
		double minPLog) {
		double x1Loc = origX + (ax + 1) * xStep;
		double x2Loc = origX + (bx + 1) * xStep;
		double y1Loc = 0; //origY - PRICE_BOTTOM - (double) yStep * (ay - minPLog);
		double y2Loc = 0; // origY - PRICE_BOTTOM - (double) yStep * (by - minPLog);
		pad.drawLine((int) x1Loc, (int) y1Loc, (int) x2Loc, (int) y2Loc);
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
	 * Find the price list for the range.
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
				// set the dates by screenIndex
				if (dataStDate == null || dataStDate.equals("")) {
					// set the dates by screenIndex
					checkStEndDates(marketDao);
				}
				
				String startDate = OracleUtil.getPrevDateInOracleFormat2(dataStDate, 0, EXTRA_DAYS);
				String endDate = OracleUtil.getPrevDateInOracleFormat2(dataEdDate, 0, 0);

				marketPulseList = marketDao.getMarketPulseListFromDB(marketType, startDate, endDate);
				startIndex = MarketUtil.findStartIndexForMarketPulseList(marketPulseList, dataStDate);

				// System.out.println("start date: " + startDate);
				// System.out.println("start date in real: " + startDateInReal);
				// System.out.println("startIndex: " + startIndex);
			} else {
				// error
			}
		} catch (Exception e) {
			e.printStackTrace();
			LOGGER.warn("Error in loading stock price info in Draw Market Chart: " + e.getMessage());
		} finally {
			try {
				database.closeConnection();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
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
				System.out.println("draw chart (Market EMA), start date: "
						+ dataStDate + ", end date: " + dataEdDate);
			}

		} catch (Exception e) {
			// ignore
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

	public String getExtraOption() {
		return extraOption;
	}

	public void setExtraOption(String extraOption) {
		this.extraOption = extraOption;
	}
}
