<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3c.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

<HEAD>

<%--@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
--%>
<meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
<meta http-equiv="expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-cache" />

<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css"> 
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
<TITLE>home.jsp</TITLE>
</HEAD>  
<%--
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
--%>
<BODY topmargin="0" bottommargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<center><table width="450" class="table_back" cellpadding="0" cellspacing="0">
        <tr><td class="head_color" valign="top" width="100%"><jsp:include page="header.jsp" flush="true"><jsp:param name="subtitle" value="Home"/></jsp:include></td></tr>

        <tr><td valign="top"><table width="100%" cellpadding="0" cellspacing="0" height="100%"> 


            <tr><td height="10" >&nbsp;</td></tr>
            <tr><td align="center"  height="40"><font color="#008855" size="5"><b>Welcome To Our Services!</b></font></td></tr>
            <tr><td height="5" >&nbsp;</td></tr>
	
            <tr><td align="center"><A href="../mobi/scan.do?inputVO.mode=blank" class="toplink">Search</A></TD></TR>
            <tr><td align="center"><A href="../mobi/analyze.do?inputVO.mode=blank&inputVO.pageStyle=S" class="toplink">Analysis</A></td></tr>
            <tr><td align="center"><A href="../mobi/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MB&inputVO.period=24" class="toplink">Market</A>
            <tr><td align="center"><A href="../mobi/home.do?inputVO.mode=blank" class="toplink">Member</A></td></tr>
            <tr><td align="center"><A href="../mobi/help.do?inputVO.mode=blank" class="toplink">Help</A></td></tr>
            <tr><td align="center"><A href="../mobi/signoff.do" class="toplink">Logout</A></td></tr>

			
            <%
                    String marketSkill = com.greenfield.ui.cache.MarketCachePool.getMarketIndicators().getMarketSkill();
            %>
            <tr><td><br>
            <ul>
            <li>The key is to stay on the right side of the market.</li>
            <li>Our suggestion for current market: <b><font color="#0000ff"><%= marketSkill %>.</font></b></li>
            </ul></td></tr>
	
	</table></td></tr>   
	
        <tr><td height="20" align="center" colspan="2"></td></tr>
	<tr class="tail_color"><td><%@include file="login_footer.jsp" %></td></tr>
	</table>

       
</center>

</BODY>
</HTML>
