package com.greenfield.ui.base;


//import com.greenfield.admin.handler.process.UpdateEoddataProcess;
//import com.greenfield.admin.handler.process.UpdateStockProcess;
//import com.greenfield.admin.handler.process.UpdateStockProcessContext;
import com.greenfield.common.base.AppContext;
import com.greenfield.common.process.ProcessStatusTypes;
import com.greenfield.ui.action.profile.LoginAction;
import com.greenfield.ui.cache.MarketCachePool;






import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;


/**
 * 
 */
public class UIStartupServlet extends HttpServlet {
	private static final Logger LOGGER = Logger.getLogger(UIStartupServlet.class); 
	
		
	/** indicates if this servlet has been already initialized. */
	public static boolean initialized = false;


	/**
	 * Initialize method called by the servlet engine during startup.
	 * Does top level initialization and calls for initialization for each application.
	 * 
	 * @exception	ServletException 
	 */
	public final void init() throws ServletException {		
		// read config
		try {
			// setup
			AppContext.init();
			
			// initial log
			/*
			SimpleDateFormat formatter = new SimpleDateFormat ("MMddyyyy");
			String logFile = AppContext.getLogDir() + "\\log_" + formatter.format(new Date()) + ".txt";
			File file = new File(logFile);
			if (file.exists()) {
				ErrorLogger.setLogger("mainLogger", logFile, true);
			} else {
				ErrorLogger.setLogger("mainLogger", logFile);
			} */
			
			// load market indictors
			MarketCachePool.getMarketIndicators();
			
			// start some processes
			if (AppContext.isStartProcess()) {
				startProcesses();
			}
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void startProcesses() {
		// start update EODDATA process on  the back ground
		/*
		StringBuffer msg = new StringBuffer();
		UpdateStockProcessContext context = new UpdateStockProcessContext(UpdateEoddataProcess.PROCESS_ID, UpdateEoddataProcess.PROCESS_NAME);
		context.setLastStartTime(new Date());
		context.setStatus(ProcessStatusTypes.STARTED);
		context.setDoNow("N");
		UpdateEoddataProcess process = new UpdateEoddataProcess(context);
		process.start(msg);
            */    
                /*
                // start update stock process on  the back ground
                StringBuffer msg = new StringBuffer();
		UpdateStockProcessContext context = new UpdateStockProcessContext(UpdateStockProcess.PROCESS_ID, UpdateStockProcess.PROCESS_NAME);
		context.setLastStartTime(new Date());
		context.setStatus(ProcessStatusTypes.STARTED);
		context.setDoNow("N");
		UpdateStockProcess process = new UpdateStockProcess(context);
		process.start(msg);
                */
	}

	/**
	 *  Called by the servlet container to indicate to a servlet that the servlet is being taken out of service. This method is only called once all threads within the servlet's service method have exited or after a timeout period has passed. After the servlet container calls this method, it will not call the service method again on this servlet. 
	 * This method gives the servlet an opportunity to clean up any resources that are being held (for example, memory, file handles, threads) and make sure that any persistent state is synchronized with the servlet's current state in memory.
	 * This method will tell the AppInstance to stop. 
	 */
	public void destroy() {
		super.destroy();
		// stop application

	}

	/**
	 * Listener for the http requests. Subclasses are restricted from overriding it.
	 * 
	 * @param		req	the servlet request.
	 * @param		res	the servlet response.
	 * @exception	IOException
	 * @exception	ServletException
	 */
	public final void service(HttpServletRequest req, HttpServletResponse res)
		throws ServletException, IOException {

		String mode = req.getParameter("mode");
		String userId = req.getParameter("userid");
		String password = req.getParameter("password");
		
		StringBuffer buff = new StringBuffer();
		PrintWriter out = res.getWriter();
		
		try {
			if (userId == null || password == null) {
				throw new ServletException("User Not Authorized. Request Ignored");
			} else {

			}
		} catch (Exception ex) {
			buff = new StringBuffer();
			buff.append("<?xml version=\"1.0\" ?>\n");
			buff.append("<app-details>");
			buff.append(
				"<error>Error occurred executing "
					+ mode
					+ ": "
					+ ex.getMessage()
					+ "</error>");
			buff.append("\n</app-details>");
		}
		out.println(buff.toString());
	}

}

