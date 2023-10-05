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
  <meta name="viewport" content="width=device-width, initial-scale=1">
  
  <%  
	// meta seems not working, setHeader() works fine
	response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
	response.setHeader("Cache-Control","no-store"); //HTTP 1.1
	response.setHeader("Pragma","no-cache"); //HTTP 1.0
	response.setHeader("Expires", "-1");
	response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
	%> 

  
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
		}--%>
	</script>
</head>

<!-- This layout is designed for all the general purpose pages, after customers logged in to the site. -->
<body>
  <div id="main">
    <header>
      <div  class="container-fluid toparea">
      	<div class="row">
        <tiles:insertAttribute name="header" />
        </div>
      </div>
      <nav class="navbar navbar-default" style="background: #0077aa;margin:0px;border:0px;" role="navigation">
          <tiles:insertAttribute name="menu" ignore="true" />
      </nav>
    </header>
    
      <!-- Main hero unit for a primary marketing message or call to action -->
            
    <div class="container-fluid paddingfooter">
    	<div class="row">
    	    <div class="col-sm-2 col-md-2 col-lg-2 nopadding">
				<tiles:insertAttribute name="leftside1" ignore="true" />
	      		<tiles:insertAttribute name="leftside2" ignore="true" />
	      		<tiles:insertAttribute name="leftside3" ignore="true" />
	      		<tiles:insertAttribute name="leftside4" ignore="true" />
	      		<tiles:insertAttribute name="leftside5" ignore="true" />
		    </div>
            <div class="col-sm-7 col-md-8 col-lg-8 nopadding">
				<tiles:insertAttribute name="content1" ignore="true" />
				<tiles:insertAttribute name="content2" ignore="true" />
			    <tiles:insertAttribute name="content3" ignore="true" />
			    <tiles:insertAttribute name="content4" ignore="true" />
			    <tiles:insertAttribute name="content5" ignore="true" />
	        </div>
	        
	        <div class="col-sm-3 col-md-2 col-lg-2 nopadding">
				<tiles:insertAttribute name="rightside1" ignore="true" />
	      	    <tiles:insertAttribute name="rightside2" ignore="true" />
	      	    <tiles:insertAttribute name="rightside3" ignore="true" />
	      	    <tiles:insertAttribute name="rightside4" ignore="true" />
	      	    <tiles:insertAttribute name="rightside5" ignore="true" />
	        </div>
	        
		</div>
	</div>
		
    
	    <footer>
	      <tiles:insertAttribute name="footer" ignore="true" />
	    </footer>
  </div>
  
	 <%-- This ismobile is obtained in request for the first call.  Put here for client script to use. --%>
	 <form name="data">
	 	<input type="hidden" id="ismobile" value="<s:property value='%{sessionContext.contentMap[\"ISMOBILE\"]}'/>">
	 </form>
</body>
</html>
