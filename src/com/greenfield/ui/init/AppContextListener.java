package com.greenfield.ui.init;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.greenfield.common.base.AppContext;
import com.greenfield.ui.cache.DBCachePool;
import com.greenfield.ui.cache.MarketCachePool;

/**
 * Write a common class to be used by the PSN applications
 * 
 * In web.xml, we need to register this listener: <listener>
 * <listener-class>com.greenfield.ui.init.AppContextListener</listener-class>
 * </listener>
 * 
 * All ServletContextListeners are notified of context initialization before any
 * filters or servlets in the web application are initialized.
 * 
 * This is the default listener for all the App's to initialize the App Context.
 * 
 * @author Qin
 * 
 */
public class AppContextListener implements ServletContextListener {

	private static final Logger LOGGER = Logger
			.getLogger(AppContextListener.class);

	public void contextInitialized(ServletContextEvent event) {
		try {
			ServletContext context = event.getServletContext();

			// initialize log4j here - !!! do log4j in the first place
			// can define log4j.unix.properties
			// and use that for unix (if File.separator is different)
			String log4jConfigFile = context
					.getInitParameter("log4j-config-location");
			String fullPath = "";

			if (File.separator.equals("/")) {
				// unix
				fullPath = context.getRealPath("") + File.separator
						+ log4jConfigFile + ".unix.properties";
			} else {
				fullPath = context.getRealPath("") + File.separator
						+ log4jConfigFile + ".properties";
			}

			PropertyConfigurator.configure(fullPath);

			// get properties
			String PROPS_PREFIX = context
					.getInitParameter("APP_PROPERTIES_FILE");
			String APPLICATION_ROOT = context
					.getInitParameter("APPLICATION_ROOT");
			

			// information
			LOGGER.warn("AppContextListener: properties file defixed in web.xml is: "
					+ PROPS_PREFIX);
			LOGGER.warn("AppContextListener: APPLICATION ROOT is: "
					+ APPLICATION_ROOT);

			// Always call AppContext to init()
			if (PROPS_PREFIX != null && !PROPS_PREFIX.equals("")) {
				// AppContext.init(PROPS_PREFIX);
				AppContext.init();
			} else {
				// using the default properties - which is main (.properties)
				AppContext.init();
			}

			// may need to load Application Flags
			// ApplicationFlags.loadFlags();


			// load market indictors
			MarketCachePool.getMarketIndicators();
			DBCachePool.generalExamStockList();

			// log
			LOGGER.warn("AppContext listener ... done its init() call ...");
		} catch (Exception ex) {
			LOGGER.warn("AppContextListener: error: " + ex.getMessage());
		}

	}

	public void contextDestroyed(ServletContextEvent sce) {
		// shut down logic goes here
		// do not need to do anything now
	}

}
