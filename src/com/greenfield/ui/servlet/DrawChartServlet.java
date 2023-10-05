/*
 * Created on April 14, 2006
 */
package com.greenfield.ui.servlet;

/**
 * @author UZHANQX
 *
 */

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.constant.ChartTypes;
import com.greenfield.common.object.user.User;
import com.greenfield.ui.graph.DrawBasicChart;
import com.greenfield.ui.graph.DrawBasicChartForMobi;
import com.greenfield.ui.graph.DrawLargeChart;
import com.greenfield.ui.graph.DrawMACDChart;
import com.greenfield.ui.graph.DrawMarketBPIChart;
import com.greenfield.ui.graph.DrawMarketEMAChart;
import com.greenfield.ui.graph.DrawMarketWHLChart;
import com.greenfield.ui.graph.DrawReportChart;
import com.greenfield.ui.graph.DrawReportChart2;
import com.greenfield.ui.graph.DrawScanChart;

/**
 * @author UZHANQX
 * 
 *         Draw Chart
 */
public class DrawChartServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;
	
	@SuppressWarnings("unused")
	private ServletConfig config;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}

	public void destroy() {
	}

	/**
	 * Handle CHART download request. The request object must contain some chart
	 * information.
	 * 
	 * @param request
	 *            the HTTP request object. It must contain the document id under
	 *            the name doc.
	 * @param response
	 *            the HTTP response object
	 */
	public void service(HttpServletRequest req, HttpServletResponse res)
			throws ServletException, IOException {

		try {
			HttpSession userSession = req.getSession(true);
			User user = (User) userSession.getAttribute(BaseAction.SECURITY);
			if (user == null) {
				sendErrorMessage(req, res,
						"Sorry.  You have to login to get our services.");
				return;
			}

			String chartType = req.getParameter("chartType");
			System.out.println("Chart type: " + chartType);
			// "#" should not be used in the link in not encoded
			if (chartType == null || chartType.equals("")) {
				downloadBasicChart(req, res);
			} else if (chartType.equals(ChartTypes.BASIC_CHART2)) {
				downloadBasicChart2(req, res);
			} else if (chartType.equals(ChartTypes.LARGE_CHART)) {
				downloadLargeChart(req, res);
			} else if (chartType.equals(ChartTypes.SCAN_REPORT)) {
				downloadReportChart(req, res);
			} else if (chartType.equals(ChartTypes.SCAN_REPORT2)) {
				downloadReportChart2(req, res);
			} else if (chartType.equals(ChartTypes.SCAN_CHART)) {
				downloadScanChart(req, res);
			} else if (chartType.equals(ChartTypes.MARKET_BPI)) {
				downloadMarketBPIChart(req, res);
			} else if (chartType.equals(ChartTypes.MARKET_WHL)) {
				downloadMarketWHLChart(req, res);
			} else if (chartType.equals(ChartTypes.MARKET_EMA)) {
				downloadMarketEMAChart(req, res);

				// Add for MOBI - 11/7/2009
			} else if (chartType.equals(ChartTypes.MOBI_CHART)) {
				downloadBasicChartForMobi(req, res);
			}
		} catch (Exception ex) {
			// error
			System.out.println("download error:");
			ex.printStackTrace();
			sendErrorMessage(req, res, ex.getMessage());
		}
	}

	/**
	 * if error, write message to the response object
	 * 
	 * @author UZHANQX
	 * 
	 *         To change the template for this generated type comment go to
	 *         Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and
	 *         Comments
	 */
	private void sendErrorMessage(HttpServletRequest req,
			HttpServletResponse res, String message) {
		try {
			// write back the response to the client
			ServletOutputStream outWeb = res.getOutputStream();
			res.setContentType("text/plain");
			outWeb.println(message);
			outWeb.close();
		} catch (Exception ee) {
			// ignore
		}

	}

	/**
	 * Download FamilyTree File
	 * 
	 * @param request
	 *            HttpServletRequest object
	 * @param response
	 *            HttpServletResponse object
	 * @param pageCtx
	 *            PageContext object
	 */
	protected void downloadBasicChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String stockId = req.getParameter("stockId");
		String ticker = req.getParameter("ticker");
		String idtype = req.getParameter("idtype");
		String period = req.getParameter("period");
		String option = req.getParameter("option");
		String type = req.getParameter("type");
		String weekly = req.getParameter("weekly");
		String is_save = req.getParameter("is_save");
		String displayDate = req.getParameter("displayDate"); // defined only in
																// some case

		// set weekly to "O" as default
		// O - display weekly chart based on period - period > 12, do weekly;
		// else, daily
		// Other two values for weekly is: N and Y.
		if (weekly == null || weekly.equals("")) {
			weekly = "O";
		}

		// is_save is used to see if set attachment or not for download; default
		// = N
		if (is_save == null || is_save.equals("")) {
			is_save = "N";
		}

		int imgHeight = 0;
		int imgWidth = 0;
		if (option == null || option.equals("")) {
			option = DrawBasicChart.DEFAULT_OPTION;
		}

		if (type != null && type.equals(DrawBasicChart.SMALL_TYPE)) {
			imgWidth = DrawBasicChart.SMALL_IMAGE_WIDTH;
			if (option.charAt(0) == 'Y') {
				imgHeight = DrawBasicChart.SMALL_IMAGE_HEIGHT_WITH_OPTION;
			} else {
				imgHeight = DrawBasicChart.SMALL_IMAGE_HEIGHT;
			}
		} else {
			// regular size
			imgWidth = DrawBasicChart.DEFAULT_IMAGE_WIDTH;
			type = DrawBasicChart.REGULAR_TYPE;
			if (option.charAt(0) == 'Y') {
				imgHeight = DrawBasicChart.DEFAULT_IMAGE_HEIGHT_WITH_OPTION;
			} else {
				imgHeight = DrawBasicChart.DEFAULT_IMAGE_HEIGHT;
			}

		}

		// Debug
		System.out.println("in download servlet ..." + stockId);
		String chart_id = "chart01.jpg";
		DrawBasicChart draw = new DrawBasicChart();
		if (idtype != null && idtype.equals("ticker")) {
			draw.setTicker(ticker);
			draw.setStockId(null);
		} else {
			draw.setStockId(stockId);
		}

		draw.setOption(option);
		draw.setType(type);
		draw.setWeekly(weekly);
		if (displayDate != null) {
			draw.setDisplayDate(displayDate);
		}

		try {
			draw.setPeriod((new Integer(period)).intValue());
		} catch (Exception ex) {
			ex.printStackTrace();
			draw.setPeriod(DrawBasicChart.DEFAULT_PERIOD);
		}

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		if (is_save.equals("Y")) {
			sendImage(response, draw.getJPEGImage(), "Y", chart_id);
		} else {
			sendImage(response, draw.getJPEGImage());
		}
	}
	
	protected void downloadBasicChart2(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String stockId = req.getParameter("stockId");
		String ticker = req.getParameter("ticker");
		String idtype = req.getParameter("idtype");
		String period = req.getParameter("period");
		String option = req.getParameter("option");
		String type = req.getParameter("type");
		String weekly = req.getParameter("weekly");
		String is_save = req.getParameter("is_save");
		String displayDate = req.getParameter("displayDate"); // defined only in
					
		String drawGF = req.getParameter("drawGF");  // Y/N, draw Grow/Force flag
		if (drawGF == null || drawGF.equals("")) {
			drawGF = "Y";
		}

		// some case

		// set weekly to "O" as default
		// O - display weekly chart based on period - period > 12, do weekly;
		// else, daily
		// Other two values for weekly is: N and Y.
		if (weekly == null || weekly.equals("")) {
			weekly = "O";
		}

		// is_save is used to see if set attachment or not for download; default
		// = N
		if (is_save == null || is_save.equals("")) {
			is_save = "N";
		}

		int imgHeight = 0;
		int imgWidth = 0;
		if (option == null || option.equals("")) {
			option = DrawBasicChart.DEFAULT_OPTION;
		}
		
		imgWidth = DrawLargeChart.DEFAULT_IMAGE_WIDTH;
		if (drawGF.equals("Y")) {
			imgHeight = DrawLargeChart.DEFAULT_IMAGE_HEIGHT;
		} else {
			imgHeight = DrawLargeChart.DEFAULT_IMAGE_HEIGHT_NO_OPTION;
		}

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		String chart_id = "chart01.jpg";
		DrawMACDChart draw = new DrawMACDChart();
		if (idtype != null && idtype.equals("ticker")) {
			draw.setTicker(ticker);
			draw.setStockId(null);
		} else {
			draw.setStockId(stockId);
		}

		draw.setDrawGF(drawGF);
		draw.setOption(option);
		draw.setType(type);
		draw.setWeekly(weekly);
		if (displayDate != null) {
			draw.setDisplayDate(displayDate);
		}

		try {
			draw.setPeriod((new Integer(period)).intValue());
		} catch (Exception ex) {
			ex.printStackTrace();
			draw.setPeriod(DrawBasicChart.DEFAULT_PERIOD);
		}

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		if (is_save.equals("Y")) {
			sendImage(response, draw.getJPEGImage(), "Y", chart_id);
		} else {
			sendImage(response, draw.getJPEGImage());
		}
	}
	
	protected void downloadLargeChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String stockId = req.getParameter("stockId");
		String ticker = req.getParameter("ticker");
		String idtype = req.getParameter("idtype");
		String period = req.getParameter("period");
		String option = req.getParameter("option");
		String type = req.getParameter("type");
		String weekly = req.getParameter("weekly");
		String is_save = req.getParameter("is_save");
		String displayDate = req.getParameter("displayDate"); // defined only in
																// some case
		String drawGF = req.getParameter("drawGF");  // Y/N, draw Grow/Force flag
		if (drawGF == null || drawGF.equals("")) {
			drawGF = "Y";
		}

		// set weekly to "O" as default
		// O - display weekly chart based on period - period > 12, do weekly;
		// else, daily
		// Other two values for weekly is: N and Y.
		if (weekly == null || weekly.equals("")) {
			weekly = "O";
		}

		// is_save is used to see if set attachment or not for download; default
		// = N
		if (is_save == null || is_save.equals("")) {
			is_save = "N";
		}

		int imgHeight = 0;
		int imgWidth = 0;
		if (option == null || option.equals("")) {
			option = DrawLargeChart.DEFAULT_OPTION;
		}

		imgWidth = DrawLargeChart.DEFAULT_IMAGE_WIDTH;
		if (drawGF.equals("Y")) {
			imgHeight = DrawLargeChart.DEFAULT_IMAGE_HEIGHT;
		} else {
			imgHeight = DrawLargeChart.DEFAULT_IMAGE_HEIGHT_NO_OPTION;
		}

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		String chart_id = "chart01.jpg";
		DrawLargeChart draw = new DrawLargeChart();
		if (idtype != null && idtype.equals("ticker")) {
			draw.setTicker(ticker);
			draw.setStockId(null);
		} else {
			draw.setStockId(stockId);
		}

		draw.setOption(option);
		draw.setType(type);
		draw.setWeekly(weekly);
		if (displayDate != null) {
			draw.setDisplayDate(displayDate);
		}
		draw.setDrawGF(drawGF);
		
		try {
			draw.setPeriod((new Integer(period)).intValue());
		} catch (Exception ex) {
			ex.printStackTrace();
			draw.setPeriod(DrawLargeChart.DEFAULT_PERIOD);
		}

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		if (is_save.equals("Y")) {
			sendImage(response, draw.getJPEGImage(), "Y", chart_id);
		} else {
			sendImage(response, draw.getJPEGImage());
		}
	}

	protected void downloadReportChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String scanKey = req.getParameter("scanKey");

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		DrawReportChart draw = new DrawReportChart();
		draw.setScanKeyPrefix(scanKey);

		int imgWidth = DrawReportChart.DEFAULT_IMAGE_WIDTH;
		int imgHeight = DrawReportChart.DEFAULT_IMAGE_HEIGHT;

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}

	protected void downloadReportChart2(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String scanKey = req.getParameter("scanKey");
		String scanIndex = req.getParameter("scanIndex");

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		DrawReportChart2 draw = new DrawReportChart2();
		draw.setScanKey(scanKey);
		draw.setScanIndex(scanIndex);

		int imgWidth = DrawReportChart2.DEFAULT_IMAGE_WIDTH;
		int imgHeight = DrawReportChart2.DEFAULT_IMAGE_HEIGHT;

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}
	
	
	protected void downloadScanChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String scanKey = req.getParameter("scanKey");
		String scanIndex = req.getParameter("scanIndex");

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		DrawScanChart draw = new DrawScanChart();
		draw.setScanKey(scanKey);
		draw.setScanIndex(scanIndex);

		int imgWidth = DrawScanChart.DEFAULT_IMAGE_WIDTH;
		int imgHeight = DrawScanChart.DEFAULT_IMAGE_HEIGHT;

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}

	protected void downloadMarketBPIChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String marketType = req.getParameter("marketType");
		String screenIndex = req.getParameter("screenIndex");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		
		// Debug
		// System.out.println("in download servlet ..." + stockId);
		DrawMarketBPIChart draw = new DrawMarketBPIChart();
		draw.setMarketType(marketType);
		draw.setDataStDate(startDate);
		draw.setDataEdDate(endDate);

		if (startDate == null) {
			try {
				draw.setScreenIndex((new Integer(screenIndex)).intValue());
			} catch (Exception ex) {
				ex.printStackTrace();
				draw.setScreenIndex(0);
			}
		}

		int imgWidth = DrawMarketBPIChart.DEFAULT_IMAGE_WIDTH;
		int imgHeight = DrawMarketBPIChart.DEFAULT_IMAGE_HEIGHT;

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}

	protected void downloadMarketWHLChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String marketType = req.getParameter("marketType");
		String screenIndex = req.getParameter("screenIndex");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		DrawMarketWHLChart draw = new DrawMarketWHLChart();
		draw.setMarketType(marketType);
		draw.setDataStDate(startDate);
		draw.setDataEdDate(endDate);
		
		if (startDate == null) {
			try {
				draw.setScreenIndex((new Integer(screenIndex)).intValue());
			} catch (Exception ex) {
				ex.printStackTrace();
				draw.setScreenIndex(0);
			}
		}

		int imgWidth = DrawMarketWHLChart.DEFAULT_IMAGE_WIDTH;
		int imgHeight = DrawMarketWHLChart.DEFAULT_IMAGE_HEIGHT;

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}

	protected void downloadMarketEMAChart(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String marketType = req.getParameter("marketType");
		String screenIndex = req.getParameter("screenIndex");
		String startDate = req.getParameter("startDate");
		String endDate = req.getParameter("endDate");
		String extraOption = req.getParameter("extraOption");
		
		// Debug
		// System.out.println("in download servlet ... Market type: " +
		// marketType + ", screenIndex: " + screenIndex);
		// String chart_id = "";
		DrawMarketEMAChart draw = new DrawMarketEMAChart();
		draw.setMarketType(marketType);
		draw.setDataStDate(startDate);
		draw.setDataEdDate(endDate);
		draw.setExtraOption(extraOption);
		
		if (startDate == null) {
			try {
				draw.setScreenIndex((new Integer(screenIndex)).intValue());
			} catch (Exception ex) {
				ex.printStackTrace();
				draw.setScreenIndex(0);
			}
		}

		int imgWidth = DrawMarketBPIChart.DEFAULT_IMAGE_WIDTH;
		int imgHeight = DrawMarketBPIChart.DEFAULT_IMAGE_HEIGHT;

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}

	protected void downloadBasicChartForMobi(HttpServletRequest req,
			HttpServletResponse response) throws Exception {

		String stockId = req.getParameter("stockId");
		String ticker = req.getParameter("ticker");
		String idtype = req.getParameter("idtype");
		String period = req.getParameter("period");
		String option = req.getParameter("option");
		String type = req.getParameter("type");
		String displayDate = req.getParameter("displayDate"); // defined only in
																// some case

		int imgHeight = 0;
		int imgWidth = 0;
		if (option == null || option.equals("")) {
			option = DrawBasicChartForMobi.DEFAULT_OPTION;
		}

		// regular size
		type = DrawBasicChartForMobi.REGULAR_TYPE;
		imgWidth = DrawBasicChartForMobi.DEFAULT_IMAGE_WIDTH;
		imgHeight = DrawBasicChartForMobi.DEFAULT_IMAGE_HEIGHT;

		// Debug
		// System.out.println("in download servlet ..." + stockId);
		DrawBasicChartForMobi draw = new DrawBasicChartForMobi();
		if (idtype != null && idtype.equals("ticker")) {
			draw.setTicker(ticker);
			draw.setStockId(null);
		} else {
			draw.setStockId(stockId);
		}

		draw.setOption(option);
		draw.setType(type);
		if (displayDate != null) {
			draw.setDisplayDate(displayDate);
		}

		try {
			draw.setPeriod((new Integer(period)).intValue());
		} catch (Exception ex) {
			ex.printStackTrace();
			draw.setPeriod(DrawBasicChart.DEFAULT_PERIOD);
		}

		draw.init();
		draw.build(imgWidth, imgHeight);

		// send image
		sendImage(response, draw.getJPEGImage());
	}

	private void sendImage(HttpServletResponse response, byte[] img) {
		sendImage(response, img, "N", "");
	}

	/**
	 * is_save/chart_id pair are used to give "download as attachment option" to
	 * customer.
	 * 
	 * @param response
	 * @param img
	 * @param is_save
	 * @param chart_id
	 */
	private void sendImage(HttpServletResponse response, byte[] img,
			String is_save, String chart_id) {
		String mimeType = "image/jpeg";

		if (response.isCommitted()) {
			// ???? why it happens
			return;
		}
		/*
		 * response.setDateHeader("Expires", 0);
		 * response.setHeader("Cache-Control", "no-cache");
		 * response.setContentType("download");
		 * response.setHeader("Content-Disposition","filename=whatever.txt;");
		 */

		response.setContentType(mimeType);
		if (is_save != null && is_save.equals("Y")) {
			response.setHeader("Content-Disposition", "attachment; filename=\""
					+ chart_id + "\"");
		} else {
			response.setHeader("Content-Disposition", "filename=\"" + chart_id
					+ "\"");
		}

		try {
			OutputStream os = response.getOutputStream();
			os.write(img, 0, img.length);

			os.flush();
			os.close();
		} catch (Exception e) {
			// Something went wrong with the transfer; set the
			// error status.
			//
			if (!response.isCommitted()) {
				// ???? why it happens
				response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		}

	}

}
