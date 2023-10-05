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

  <LINK href="../theme/baselayout1.css" rel="stylesheet" type="text/css">
  <LINK href="../theme/public_style.css" rel="stylesheet" type="text/css">
  
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
</head>

<!-- for the pages that user has not logged to the system yet.  Such as login, registration, public info, etc. -->
<body><%-- 
<jsp:include page="../public/login.jsp"></jsp:include>
--%>
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
      <div id="sidebar_container">
			<tiles:insertAttribute name="box1" ignore="true" />
			<tiles:insertAttribute name="box2" ignore="true" />
      </div>
      <div class="content">
		<tiles:insertAttribute name="content" ignore="true" />
      </div>
    </div>
    
    <div class="clear"></div>
    <div id="appender">
		<tiles:insertAttribute name="appender" ignore="true" />
    </div>
      
    <div id="scroll">
      <tiles:insertAttribute name="scroll" ignore="true" />
    </div>
  </div>
   <!-- to move footer outside of main, the idea is to configure main/footer to make footer to stay on the bottom of screen. -->
 	<footer>
	      <tiles:insertAttribute name="footer" ignore="true" />
    </footer>
</body>
</html>
