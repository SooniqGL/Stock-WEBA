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
  
  
  <%-- following is used to use bootstrap library --%>
  <meta name="viewport" content="width=device-width, initial-scale=1">
  <link rel="stylesheet" href="../bootstrap/css/bootstrap.min.css">
  <link rel="stylesheet" href="../bootstrap/css/bootstrap-theme.min.css">
  <script src="../script/jquery-2.1.1.min.js" type="text/javascript"></script>
  <script src="../bootstrap/js/bootstrap.min.js" type="text/javascript"></script>

  <LINK href="../theme/baselayout1.css" rel="stylesheet" type="text/css">
  <LINK href="../theme/public_style.css" rel="stylesheet" type="text/css">
  
  <s:if test="%{sessionContext.contentMap[\"ISMOBILE\"] == \"Y\"}">
  	<SCRIPT SRC="/WEBA/script/layout_helper.js"></SCRIPT>
  </s:if>
  
  <%--  
  <LINK href="/WEBA/theme/jquery-ui-1.10.4.custom.min.css" rel="stylesheet" type="text/css">

  <SCRIPT SRC="/WEBA/script/jquery-2.1.1.min.js" type="text/javascript"></SCRIPT>
  <SCRIPT SRC="/WEBA/script/jquery-ui-1.10.4.min.js" type="text/javascript"></SCRIPT>

  <SCRIPT SRC="/WEBA/public/login_helper.js"></SCRIPT>
 
 <script type="text/javascript">

	$( document ).ready(function() {
	
		// this is ugly, as the dialog is running different in IE
		var ua = window.navigator.userAgent;
		var msie = ua.indexOf('MSIE ');
	    var trident = ua.indexOf('Trident/');
	
	    if (msie > 0 || trident > 0) {
			QQ_loginoverlay.init("loginoverlay", 350, 250);
	    } else {
	    	QQ_loginoverlay.init("loginoverlay", 350, 280);
	    }
		
	});

</script> 
--%>

<%--
To make mobile to work, steps:
1) add "div_*" for the DIV tags to display in mobile horizontally;
2) add condition to remove "footer" tag from layout, as it is not good for the display;  Need to add copyright in other ways;
3) Check ismobile in Action, as first time when customer logins; put the flag to sessioncontext;
4) Condition the "header.jsp" to show different header;
5) Remove the regular "menu" section for mobile;
6) Add "menu" as a separated DIV for mobile;
7) add divList to all the layouts - the first one will be show as default;  
	pass it from tiles configuration to layout*.jsp; and then to client scripts;
8) The layout_script is included and called for mobile devices;
9) add "divparent" class to row/column parent bootstrap tags in layout;  Need to adjust the width dynamically for mobile;

 --%>

<script type="text/javascript">
	
		$( document ).ready(function() {
			<%-- only if it is mobile, we start the mobile setup --%>
			var ismobile = document.getElementById("ismobile").value;
			if (ismobile == "Y") {
				<%-- the divlist is defined in struts tiles; the first DIV in the list will be shown
				   in mobile first; others controlled by prev/next;  --%>
				QQ_layout.init("<tiles:insertAttribute name='divlist'/>");
				checkbuttons();
			} else {
				// alert("not mobile");
			}
		});

</script> 
</head>

<!-- for the pages that user has not logged to the system yet.  Such as login, registration, public info, etc. -->
<body><%-- 
<jsp:include page="../public/login.jsp"></jsp:include>
--%>
  <div id="main">
    <header>
      <div  class="container-fluid toparea">
      	<div class="row">
        <tiles:insertAttribute name="header" />
        </div>
      </div>
      
      <%-- provide menu only if it is not mobile --%>
      <s:if test="%{sessionContext.contentMap[\"ISMOBILE\"] != \"Y\"}">
	      <nav class="navbar navbar-default" style="background: #0077aa;margin:0px;border:0px;" role="navigation">
	          <tiles:insertAttribute name="menu" ignore="true" />
	      </nav>
      </s:if>
    </header>
    <div class="container paddingfooter">
    	<%-- divparent class is used for mobile --%>
    	<div class="row divparent">
    	<%-- use the push/pull can change order when it is in mobile case --%>
    	<%--
    	    <div class="col-sm-6 col-sm-push-6 col-md-5 col-md-push-7 col-lg-4 col-lg-push-8">
				<tiles:insertAttribute name="box1" ignore="true" />
				<tiles:insertAttribute name="box2" ignore="true" />
		    </div>
            <div class="col-sm-6 col-sm-pull-6 col-md-7 col-md-pull-5 col-lg-8 col-lg-pull-4">
				<tiles:insertAttribute name="content" ignore="true" />
	        </div>--%>
	        
	        <%-- divparent class is used for mobile --%>
	        <div class="col-sm-6 col-md-7 col-lg-8 divparent">
	        	<div id="div_1">
					<tiles:insertAttribute name="content" ignore="true" />
				</div>
	        </div>
	        <div class="col-sm-6 col-md-5 col-lg-4 divparent">
		        <div id="div_2">
					<tiles:insertAttribute name="box1" ignore="true" />
				</div>
				<div id="div_3">
					<tiles:insertAttribute name="box2" ignore="true" />
				</div>
				
				<%-- provide menu as a separated DIV when it is mobile --%>
				<s:if test="%{sessionContext.contentMap[\"ISMOBILE\"] == \"Y\"}">
					<div id="div_4">
						<tiles:insertAttribute name="box3" ignore="true" />
					</div>
				</s:if>
		    </div>
            
	        
		</div>
		
	    <div  class="row" id="appender">
			<tiles:insertAttribute name="appender" ignore="true" />
	    </div>
	      
	    <div  class="row" id="scroll">
	      <tiles:insertAttribute name="scroll" ignore="true" />
	    </div>
  </div>
  </div>
   <%-- to move footer outside of main, the idea is to configure main/footer to make footer to stay on the bottom of screen.
      Note: for now, do not include the footer for mobile, it is not fit the layout now.  Need some fix!
      
      
     
      
      // use bootstrap - style to fix footer -- but not good on mobile!
      // need to set the footer to end of each "DIV" dynamically - position absolutely
    --%>  
   <s:if test="%{sessionContext.contentMap[\"ISMOBILE\"] != \"Y\"}">
 	<footer>
	      <tiles:insertAttribute name="footer" ignore="true" />
    </footer>
  	</s:if>
 
   
 <%-- This ismobile is obtained in request for the first call.  Put here for client script to use. --%>
 <form name="data">
 	<input type="hidden" id="ismobile" value="<s:property value='%{sessionContext.contentMap[\"ISMOBILE\"]}'/>">
 </form>

</body>
</html>
