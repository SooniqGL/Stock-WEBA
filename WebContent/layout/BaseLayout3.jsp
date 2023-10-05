<!DOCTYPE HTML>

<%@ taglib prefix="s" uri="/struts-tags" %>
<%@ taglib uri="http://tiles.apache.org/tags-tiles" prefix="tiles" %>
<html>
<head>

  <title><tiles:insertAttribute name="title" ignore="true" /></title>
  <meta name="description" content="website description" />
  <meta name="keywords" content="website keywords, website keywords" />
  <meta http-equiv="content-type" content="text/html; charset=windows-1252" />
  <meta http-equiv="robots" content="noindex,nofollow" />

	<%  
	// meta seems not working, setHeader() works fine
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Expires", "-1");
	response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
	%> 
	
  <LINK href="../theme/baselayout3.css" rel="stylesheet" type="text/css">
  <LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
  
  <tiles:insertAttribute name="head_include" />
   
  <%-- set timer: 1800000 sec = 30 minutes to direct to login screen --%>
  <script type="text/javascript">
   		var sTargetURL = "/WEBA/p/login_input.do";	
		function startTimer() {
			window.setTimeout("window.location.href = sTargetURL", 1800000);
		}
		<%--
		if (window.addEventListener) {
			window.addEventListener("load", function(){
	    		startTimer(); }, false);
		} else if (window.attachEvent) {
			window.attachEvent("onload", function(){
				startTimer(); });
		} --%>
  </script>
	
</head>

<!-- This layout is designed with two major columns, for account profile management. -->
<body>
  <div id="main">
    <header>
      <div id="logo">
        <tiles:insertAttribute name="header" />
      </div>
      <nav>
        <div id="menu_container">
          <tiles:insertAttribute name="menu" ignore="true" />
        </div>
      </nav>
    </header>
    
    <div id="site_content">
      	<div id="subtitle">
			<span style="FONT-FAMILY: Times New Roman, sans-serif, Verdana; COLOR:#006699; FONT-SIZE:28pt; FONT-WEIGHT:bold; display:table; margin:auto;"><tiles:insertAttribute name="subtitle" ignore="true" /></span>
      	</div>
      	
      	<div id="leftsidebar">
      		<tiles:insertAttribute name="leftside1" ignore="true" />
      		<tiles:insertAttribute name="leftside2" ignore="true" />
      		<tiles:insertAttribute name="leftside3" ignore="true" />
      		<tiles:insertAttribute name="leftside4" ignore="true" />
      		<tiles:insertAttribute name="leftside5" ignore="true" />
      	</div>
        <div id="content">
        	<tiles:insertAttribute name="content1" ignore="true" />
			<tiles:insertAttribute name="content2" ignore="true" />
		    <tiles:insertAttribute name="content3" ignore="true" />
		    <tiles:insertAttribute name="content4" ignore="true" />
		    <tiles:insertAttribute name="content5" ignore="true" />
        </div>
      
      
        <div class="clear"></div>
	    <div id="tail">
			<tiles:insertAttribute name="tail1" ignore="true" />
			<tiles:insertAttribute name="tail2" ignore="true" />
			<tiles:insertAttribute name="tail3" ignore="true" />
	    </div>
	    <div id="scroll">
	      <tiles:insertAttribute name="scroll" ignore="true" />
	    </div>
      </div>  
  </div>
 
    <!-- to move footer outside of main, the idea is to configure main/footer to make footer to stay on the bottom of screen. -->
 	<footer>
	      <tiles:insertAttribute name="footer" ignore="true" />
    </footer>
    
    <%-- This ismobile is obtained in request for the first call.  Put here for client script to use. --%>
	 <form name="data">
	 	<input type="hidden" id="ismobile" value="<s:property value='%{sessionContext.contentMap[\"ISMOBILE\"]}'/>">
	 </form>
</body>
</html>
    