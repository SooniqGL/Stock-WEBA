package com.greenfield.ui.servlet;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Vector;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;

import com.greenfield.common.base.BaseAction;
import com.greenfield.common.object.user.User;
import com.greenfield.ui.cache.PoolUtil;


/**
 * This class is handle system cache, other internal processes.
 */

public class SystemMgrServlet extends HttpServlet {


	private ServletConfig config;
     

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}

	public void destroy() {
	}


	/**
	 * Request is handled, while the remove IP is verified.
	 *
	 * @param request the HTTP request object. It must contain the
	 *      document id under the name doc.
	 * @param response the HTTP response object
	 */
	public void service(HttpServletRequest req, HttpServletResponse res)
									 throws ServletException, IOException {
   
		try {
			verifyRemoteIPAddressAndToken(req);
			
			// this is tempoarary solution - need to login.
			HttpSession userSession = req.getSession(true);
			User user = (User) userSession.getAttribute(BaseAction.SECURITY);
			if (user == null) {
				sendMessage(req, res, "Sorry.  You have to login to use our services.");
				return;
			}
			
			/**
			 * class -> action: "c" do cache; for now reset all cache ...
			 */
			String what = req.getParameter("class");
			boolean done = false;
	
			if ( what == null ) {
				 // Don't do anything
			} else if ( what.equals("c") ) {
				done = true;
				PoolUtil.resetAllPools();
			} 
	
			if (done) {
				sendMessage(req, res, "OK");
			} else {
				sendMessage(req, res, "DO NOT LIKE IT.");
			}
		} catch (Exception ex) {
			// error
			System.out.println("download error:");
			ex.printStackTrace();
			sendMessage(req, res, "General Error");
		} 
	}
	
	/**
	 * For now, use login session to control.  However, to make it more dynamic,
	 * We need to secure this servlet using some other rules: IP and token.
	 * @param req
	 * @throws Exception
	 */
	private void verifyRemoteIPAddressAndToken(HttpServletRequest req) throws Exception {
		// 1. get a list of ip from property file, and verify here.
		
		// 2. get token - should use encrypted token, with time stamp in inside.
		
	}

	// send message to client
	private void sendMessage(HttpServletRequest req, HttpServletResponse res, String message) {
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


 
}

