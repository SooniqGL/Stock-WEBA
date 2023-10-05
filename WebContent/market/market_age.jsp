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

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">

<TITLE>Market Home Page</TITLE>

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
	
	function updateEntry(mode, type, stockId) {
		var form = document.watchForm;
		
		if (mode == "deletewatch") {
			var ret = confirm("You are about to delete the entry. \nClick OK to continue or Cancel to abort action.");
			if (ret == false) {
				return false;
			}
		}
		
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		form.all["inputVO.stockId"].value = stockId;
		form.submit();
	}
	
	function startApplet() {
		document.marketageapplet.startStopPlay();
		//document.ageForm.startBt.disabled = true;
		//document.ageForm.stopBt.disabled  = false;
	}
	
	function stopApplet() {
		document.marketageapplet.stopPlay();
		//document.ageForm.startBt.disabled = false;
		//document.ageForm.stopBt.disabled = true;
	}
	
	function setSlowSpeed() {
		document.marketageapplet.setSlowSpeed();
	}
	
	function setFastSpeed() {
		document.marketageapplet.setFastSpeed();
	}
	
	function goLeftApplet() {
		document.marketageapplet.goLeftPlay();
	}
	
	function goRightApplet() {
		document.marketageapplet.goRightPlay();
	}
	
	function showComments(msg) {
		alert("msg: " + msg);
	}
	
</SCRIPT>	
<jsp:useBean id="marketForm" scope="session" type="com.greenfield.ui.action.market.MarketPulseActionForm"/>
<bean:define id="inputVO" name="marketForm" property="inputVO" type="com.greenfield.common.object.market.MarketPulseVO"/>
<!--bean:define id="folderList" name="inputVO" property="folderList" type="java.util.Vector"/-->

</HEAD>
<BODY class="page_back">
<center><table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0"> 
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" cellpadding="0" cellspacing="0" height="100%"> 
	<tr><td height="40" align="center" colspan="2" class="tail_color"><font  class="main_title">Market Analysis</font></td></tr>
	
	<tr><td colspan="2" height="40">&nbsp;
		<font class="main_title2">General Market Statistics</font></td></tr>
		
	<tr><td colspan="2">
	<form name="ageForm" action="" method="post">
	<table border="1">
		<tr><td colspan="2"><table width="100%">
			<tr>
                            <td>New York</td><td> [ <a href="../k/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MB&inputVO.period=24" class="toplink">BPI / A-D</a> |
                                <a href="../k/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=ME&screenIndex=0" class="toplink">EMA Stats</a> |
				<a href="../k/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MW&inputVO.period=24" class="toplink">William / New H-L</a> |
				<a href="../k/market.do?inputVO.mode=marketage&inputVO.marketType=N&inputVO.period=6" class="toplink">Age Chart</a> ]</td>
                        </tr><tr>
                            <td>NasDaq</td><td> [ <a href="../k/market.do?inputVO.mode=blank&inputVO.marketType=Q&inputVO.chartType=MB&inputVO.period=24" class="toplink">BPI / A-D</a> |
                                <a href="../k/market.do?inputVO.mode=blank&inputVO.marketType=Q&inputVO.chartType=ME&screenIndex=0" class="toplink">EMA Stats</a> |
				<a href="../k/market.do?inputVO.mode=blank&inputVO.marketType=Q&inputVO.chartType=MW&inputVO.period=24" class="toplink">William / New H-L</a> |
				<a href="../k/market.do?inputVO.mode=marketage&inputVO.marketType=Q&inputVO.period=6" class="toplink">Age Chart</a> ]</td>
			</tr>
		</table></td>
		</tr>
		<tr><td colspan="2" align="center">
		<input type="radio" name="ckSpeed" value="3" onClick="setSlowSpeed()"> Slow
		<input type="radio" name="ckSpeed" value="6" onClick="setFastSpeed()" checked> Fast
		&nbsp;&nbsp;				
		<input type="button" name="startBt" value="Start/Stop" onclick="startApplet()">
		<input type="button" name="startBt2" value="&lt;&lt;" onclick="goLeftApplet()">
		<input type="button" name="startBt3" value="&gt;&gt;" onclick="goRightApplet()">
		
		&nbsp;&nbsp;
		[ <logic:equal name="inputVO" property="period" value="6">
			6M &nbsp;|&nbsp;
			<a href="../k/market.do?inputVO.mode=marketage&inputVO.marketType=<bean:write name='inputVO' property='marketType'/>&inputVO.period=12" class="toplink">12M</a>	
		</logic:equal>
		
		<logic:notEqual name="inputVO" property="period" value="6">
			<a href="../k/market.do?inputVO.mode=marketage&inputVO.marketType=<bean:write name='inputVO' property='marketType'/>&inputVO.period=6" class="toplink">6M</a>	
			&nbsp;|&nbsp; 12M
		</logic:notEqual> ]
		<%--input type="button" name="stopBt" value="Stop" onclick="stopApplet()"--%>
		</td></tr>
		<tr><td colspan="2" align="center">
			<OBJECT classid="clsid:8AD9C100%-044E-11D1-B3E9-00805F499D93"
		    id="marketageapplet" width="800" height="400" align="baseline" 
		    codebase="/WebApp2/plugin/jinstall-1_4_0-win.cab">
		    <PARAM name="code" value="com.greenfield.ui.applet.market.MarketAgeApplet.class">
		    <PARAM name="archive" value="market_applet.jar">
		    <PARAM name="codebase" value="/WebApp2/applet">
		    <PARAM name="type" value="application/x-java-applet;jpi-version=1.4">
		    <PARAM name="content" value="<bean:write name='inputVO' property='ageList'/>">
		        No Java 2 SDK, Standard Edition v 1.4.0 support for APPLET!!
			</OBJECT>
		</td>
		</tr>
		<tr><td colspan="2"><ul>
			<li>To display the chart right, you may need to download JRE (Java runtime library) version 1.4 or later from <a href="http://java.sun.com" target="_top">SUN</a>.  Or you can
			click the following link to get sun's installation file from this site. <a href="../download?class=public&file=jre">JRE Install</a>.</li>
			<li>A stock's age is defined as the days for the current trend.  It is positive
			if the trend is up, else is negative.  In the chart, the X-value is the age, and 
			the Y-value is the number of stocks with that age.</li>
			<li>The first quarant (top and right quarter) shows the number of up trends.  The second quarant
			(top and left quarter) shows the number of changes from down to up.  The third
			quarant (bottom and left quarter) shows the number of down trends.  The fourth
			auarant (bottom and right quarter) shows the number of changes from up to down.</li>
			<li>You may adjust the speed (slow/fast) any time during the play.  
			&lt;&lt; and &gt;&gt; bottons only work when the auto playing is stopped.</li>
			<li>To make small numbers visible, the second and fourth quarants are using
			an enlarged scale.</li></ul>
			</td></tr>
		</table>     	
		</form>
	<tr><td height="30" colspan="2">&nbsp;
	</td></tr>
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
	</table>
	
</td></tr>    
	
</table></center>
</BODY>
</HTML>

