<!DOCTYPE HTML>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@page errorPage="/error.jsp" %>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>  
<%@ page import="java.util.Vector" %>
<%@ page import="com.greenfield.common.util.StringHelper" %>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
<SCRIPT SRC="../script/onmouse.js"></SCRIPT>

<TITLE>Alert List</TITLE> 

<%  
// meta seems not working, setHeader() works fine
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "-1");
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
%> 

<SCRIPT language="javascript">    
 	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		form.submit();
	}
	
</SCRIPT>
<script type="text/javascript" language="JavaScript">
// Init section
	var obj_float_div = false;
	var show_delay = null;
 
	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	run_after_body();
</script>
<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

.float{
   visibility: hidden;
   position: absolute;
   left: -3000px;
   z-index: 10;
}
</style>

<jsp:useBean id="watchForm" scope="session" type="com.greenfield.ui.action.member.WatchListActionForm"/>
<bean:define id="inputVO" name="watchForm" property="inputVO" type="com.greenfield.common.object.member.WatchListVO"/>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />

</HEAD>
<BODY class="page_back">
<div class="float" id="div_200" style="left: -3000px; background: #ffffff;"><img id="img_200" class="border_b" width="800" height="450"></div>

            
        <div id="outcontainer">
        <div id="maincontainer">

            <div id="topsection"><div class="innertube2">
                    <jsp:include page="../base/topmenu.jsp">
                        <jsp:param name="subtitle" value="Member Account" />
                        <jsp:param name="fname" value="<%= security.getFname() %>" />
                        <jsp:param name="lname" value="<%= security.getLname() %>" />  
                    </jsp:include>
            </div></div>

            <div id="contentwrapper">
            <div id="contentcolumn">
                <div class="innertube2">    
            
            <table width="100%" height="100%" cellpadding="0" cellspacing="0" class="light_back"> 
	 
	<tr><td colspan="2" align="center" height="50"><a href="../m/home.do?inputVO.mode=blank" class="toplink">Member Home</a>
		&nbsp;|&nbsp;<a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="toplink">Profile</a>
		&nbsp;|&nbsp;<a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="toplink">Alert List</a></td></tr>
	<tr><td colspan="2" valign="top">
		<html:form action="/m/watch"> 
		<html:hidden property="inputVO.mode" value="basic"/>		
		<html:hidden property="inputVO.type" value=""/>
		</html:form>
     
		<table border="1" width="100%">
			<tr class="member_back"><td colspan="2" height="30">&nbsp;
			<font class="sub_title">My Alert List</font></td></tr>
			<tr><td>Tickers</td><td>
			<% String tickerStr = inputVO.getTickerListStr();
		   Vector tickerList = StringHelper.mySplit(tickerStr, ",");
		   if (tickerList != null && tickerList.size() > 0) {
		   for (int i = 0; i < tickerList.size(); i ++) {
		   	String tickerDisplay = (String) tickerList.get(i);
		   	String ticker = "";
		   	int index = tickerDisplay.indexOf("(");
		   	if (index > -1) {
		   		ticker = tickerDisplay.substring(0, index);
		   	} else {
		   		ticker = tickerDisplay;
		   	} %>
		   
		   <A href="../a/analyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=ticker&inputVO.ticker=<%= ticker %>&inputVO.period=6" 
				onMouseOver='show_200("../drawchart?idtype=ticker&ticker=<%= ticker %>&period=6&option=G","71895",400,350)' onMouseOut="hide_div()" 
				class="datalink">
			<%= tickerDisplay %>
			</A>&nbsp;&nbsp;
			
			<% }} else { %>
			Nothing in the alert list.
			<% } %>
			</td></tr>
		</table>
                        &nbsp; Note: This page is coming soon.
	</td></tr> 

	</table>
        </div>
            </div>
            </div>

            <div id="leftcolumn">
            <div class="innertube"><%--@include file="../base/left2.jsp" --%>
                <a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="sublink">Member Profile</a><br />
                <a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="sublink">Alert List</a><br />
                <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=S&inputVO.currLink=1" class="sublink">My Messages</a>
            </div>
            </div>

            <form name="goStockAgeForm" action="../s/agesrch.do">
                <input type="hidden" name="inputVO.mode" value="search"/>
                <input type="hidden" name="inputVO.selectRange" value=""/>
            </form>
            <%@include file="../base/footer.jsp" %>

        </div>
    </div>              
</BODY>
</HTML>
