/*
 * Created on Jan 7, 2007
 */
package com.greenfield.ui.servlet;


import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.ServletException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpSession;


/**
 * This class is used to download a file.
 */

public class DownloadServlet extends HttpServlet {

	private static final long serialVersionUID = 1L;

	/**
	 * Name of the file id string
	 */
	final static String FILE_ID = "fileid";
    
	// Absolute path
	//
	private String path_;

	// File name
	//
	private String filename_;

	// MIME type
	//
	private String mimetype_;

	private ServletConfig config;
     

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		this.config = config;
	}

	public void destroy() {
	}

	/**
	 * Set path
	 */
	public void setPath( String path )
	{
		path_ = path;
	}

	/**
	 * Get path
	 */
	public String getPath()
	{
		return path_;
	}

	/**
	 * Set file name
	 */
	public void setFileName( String filename )
	{
		filename_ = filename;
	}

	/**
	 * Get file name
	 */
	public String getFileName()
	{
		return filename_;
	}

	/**
	 * Set MIME type
	 */
	public void setMimeType( String mimetype )
	{
		mimetype_ = mimetype;
	}

	/**
	 * Get MIME type
	 */
	public String getMimeType()
	{
		return mimetype_;
	}

	/**
	 * Handle file download request. The request object must contain
	 * the document id.
	 *
	 * @param request the HTTP request object. It must contain the
	 *      document id under the name doc.
	 * @param response the HTTP response object
	 */
	public void service(HttpServletRequest req, HttpServletResponse res)
									 throws ServletException, IOException {
   
		try {
			String what = req.getParameter("class");
	
			if ( what == null ) {
				 // Don't do anything
			} else if ( what.equals("public") ) {
				downloadPublicFile(req, res);
			} 
	
		} catch (Exception ex) {
			// error
			System.out.println("download error:");
			ex.printStackTrace();
			sendErrorMessage(req, res, ex.getMessage());
		} 
	}

	// if error, write message to the response object
	private void sendErrorMessage(HttpServletRequest req, HttpServletResponse res, String message) {
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


	// no security is needed when class is set to "public"
	protected void downloadPublicFile(HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(false);
		if (session == null) {
			// Debug
			// System.out.println("Session is not valid, cannot get data");
			response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			return;
		}
		
		String publicFileDir = "c:\\sooniq\\stocktek\\public";
		String fileName = request.getParameter("file");
		String jreFile = "j2re-1_4_2_06-windows-i586-p-iftw.exe";
		
		if (fileName != null && fileName.equals("jre")) {
			download(publicFileDir + "\\" + jreFile, "", jreFile, response, "");
		}
	}

	/**
	 * Download the file
	 * @param fullPath the full path (including the file name )
	 * @param mimeType the MIME type of the file
	 * @param fileName the original file name
	 * @param pageCtx PageContext object
	 */
     
    
	protected void download( String fullPath, String mimeType, String fileName,
							 HttpServletResponse response, String is_save )
	{
		File file = new File( fullPath );
		if ( file.isFile() ) {
			// mime type
			//String mimeType = request.getParameter( "mimeType" );
			if (mimeType == null || mimeType.equals(""))
			{
				// We didn't have a MIME type from the database, so
				// get it from the container.
				mimeType = config.getServletContext().getMimeType(fileName);

				if (mimeType == null)
				{
					 // If all else fails, assume text
					 //
					 mimeType = "text/plain";
				}
			}

		//Debug
		// System.out.println ("DownloadFile.java->download->mimeType: " + mimeType );

			/* response.setDateHeader("Expires", 0);
		response.setHeader("Cache-Control", "no-cache");
		response.setContentType("download");
		response.setHeader("Content-Disposition","filename=whatever.txt;");   */

			response.setContentType(mimeType);
		//if (is_save != null && is_save.equals("Y")) {
		//    response.setHeader("Content-Disposition",
			//         "attachment; filename=\"" + fileName + "\"");
		//} else {
		response.setHeader("Content-Disposition",
					 "filename=\"" + fileName + "\"");
		//}

			OutputStream os = null;
			FileInputStream is = null;

			try
			{
				  os = response.getOutputStream();
				  is = new FileInputStream(file);

				  byte [] bytes = new byte[1024];
				  int len = 0;
				  while ( (len = is.read(bytes)) > -1 ) {
					  os.write (bytes,0, len );
				  }
				  is.close();

				  os.flush();
				  os.close();
			}
			catch (Exception e)
			{
				  // Something went wrong with the transfer; set the
				  // error status.
				  //
				  response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
			}

		}
	}

 
}

