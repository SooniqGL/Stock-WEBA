<%-- 
    Document   : ticker_list
    Created on : Nov 7, 2009, 9:38:52 PM
    Author     : qin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3c.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

    <head>
        <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
        <meta http-equiv="expires" content="0"/>
        <meta http-equiv="Cache-Control" content="no-cache" />
        <LINK href="../../theme/Master.css" rel="stylesheet" type="text/css">
        <LINK href="../../theme/main_style.css" rel="stylesheet" type="text/css">
        <title>JSP Page</title>
    </head>

    <jsp:useBean id="mobiRecentListForm" scope="session" type="com.greenfield.ui.mobi.action.RecentListActionForm"/>
    <bean:define id="inputVO" name="mobiRecentListForm" property="inputVO" type="com.greenfield.common.object.analyze.AnalyzeVO"/>
    <bean:define id="recentList" name="inputVO" property="recentList" type="java.util.Vector"/>

    <body topmargin="0" leftmargin ="0" marginheight="0" marginwidth="0"><center>
        <table width="450"><tr class="head_color"><td>
        <jsp:include page="../base/header.jsp" flush="true"><jsp:param name="subtitle" value="Recent List"/></jsp:include>
                </td></tr>

            <tr><td>
        <h1>Most Recent Ticker List!</h1></td></tr>
        <tr><td>
            <div id="tickerList">
            <logic:iterate id="ticker" name="recentList">
                    <a href="../../mobi/analyze.do?inputVO.mode=analyze&inputVO.ticker=<bean:write name="ticker"/>&inputVO.type=ticker&inputVO.period=6" class="datalink2"><bean:write name="ticker"/></a>
            </logic:iterate></div>
        </td></tr>
         <tr><td height="20" align="center" colspan="2"></td></tr>
	<tr class="tail_color"><td><%@include file="../base/footer.jsp" %></td></tr>
	</table>

</center>
    </body>
</html>
