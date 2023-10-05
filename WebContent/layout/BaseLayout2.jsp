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

  <link rel="stylesheet" type="text/css"
  		media="screen and (max-width: 1288px)" href="../theme/baselayout2_fix.css" />
  <%-- link rel="stylesheet" type="text/css"
  		media="screen and (min-width: 1029px) and (max-width: 1288px)" href="../theme/baselayout2_sml.css" /> --%>
  <link rel="stylesheet" type="text/css"
  		media="screen and (min-width: 1289px)" href="../theme/baselayout2_lrg.css" />  		
   
   <tiles:insertAttribute name="head_include" />
   
   <%-- set timer: 1800000 sec = 30 minutes to direct to login screen --%>
   <script type="text/javascript">
   		var sTargetURL = "/WEBA/p/login_input.do";	
   		var myTimer = null;
   		
		function startTimer() {
			myTimer = window.setTimeout("window.location.href = sTargetURL", 1800000);
		}
		
		function resetTimer() {
			clearTimeout(myTimer);
			myTimer = window.setTimeout("window.location.href = sTargetURL", 1800000);
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

<!-- This layout is designed for all the general purpose pages, after customers logged in to the site. -->
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
      <div id="leftside_container">
        <div id="subtitle">
        	<tiles:importAttribute name="subtitle" />
            <jsp:include flush="true" page="${subtitle}"></jsp:include>
            
        <%-- <jsp:include> is used to make the jsp in parent scope, to get tiles attributes - as current version "cascade not working"
           The idea is: to make the subtitle jsp dynamically, also subtitleText can be passed from tiles xml file.
			<span style="FONT-FAMILY: Times New Roman, sans-serif, Verdana; COLOR:#006699; FONT-SIZE:28pt; FONT-WEIGHT:bold; display:table; margin:auto;"><tiles:insertAttribute name="subtitle" ignore="true" /></span>
		--%>
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
      </div>
      <%-- 
      <div id="rightsidebar">
      	    <tiles:insertAttribute name="rightside1" ignore="true" />
      	    <tiles:insertAttribute name="rightside2" ignore="true" />
      	    <tiles:insertAttribute name="rightside3" ignore="true" />
      	    <tiles:insertAttribute name="rightside4" ignore="true" />
      	    <tiles:insertAttribute name="rightside5" ignore="true" />
      </div>
      --%>
      <div class="clear"></div>
	    <div id="tail">
			<tiles:insertAttribute name="tail" ignore="true" />
	    </div>
	    <div id="scroll">
	      <tiles:insertAttribute name="scroll" ignore="true" />
	    </div>
       </div>
    
	    <footer>
	      <tiles:insertAttribute name="footer" ignore="true" />
	    </footer>
  </div>
 <%-- This ismobile is obtained in request for the first call.  Put here for client script to use. 
 --%>
 <form name="data">
 	<input type="hidden" id="ismobile" value="<s:property value='%{sessionContext.contentMap[\"ISMOBILE\"]}'/>">
 </form>
</body>
</html>
