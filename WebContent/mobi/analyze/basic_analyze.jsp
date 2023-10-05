<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3c.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">

    <%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>

<%--@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
--%>
      
<HEAD> 
<meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
<meta http-equiv="expires" content="Mon, 04 Jul 1900 07:07:07 GMT" />
<meta http-equiv="Cache-Control" content="no-cache" />
<meta http-equiv="PRAGMA" content="no-cache" />

<%--meta http-equiv="expires" content="-1"/--%>

<LINK href="../../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../../theme/main_style.css" rel="stylesheet" type="text/css">
<TITLE>analyze_basic.jsp</TITLE>

<SCRIPT language="javascript">   
		
	function setFocus() {
		var form = document.mobiAnalyzeForm;
		form.all["inputVO.ticker"].select();
		form.all["inputVO.ticker"].focus();
	}

        function goByDay(days) {
		var form = document.mobiAnalyzeForm;
		form.all["inputVO.ticker"].select();
		form.all["inputVO.ticker"].focus();
	}
	
</SCRIPT>
<style type="text/css">
.date_button {
   width: 50px;
}

.date_button_lrg {
   width: 100px;
}
</style>	  
</HEAD>
<jsp:useBean id="mobiAnalyzeForm" scope="session" type="com.greenfield.ui.mobi.action.AnalyzeActionForm"/>
<bean:define id="inputVO" name="mobiAnalyzeForm" property="inputVO" type="com.greenfield.common.object.analyze.AnalyzeVO"/>
<bean:define id="stockList" name="inputVO" property="stockList" type="java.util.Vector"/>
<bean:define id="stockExt" name="inputVO" property="stockExt" type="com.greenfield.common.object.stock.StockExt"/>
<bean:define id="stock" name="stockExt" property="stock" type="com.greenfield.common.object.stock.Stock"/>
<bean:define id="recentList" name="inputVO" property="recentList" type="java.util.Vector"/>

<BODY topmargin="0" leftmargin ="0" marginheight="0" marginwidth="0">
    <html:form action="/mobi/analyze">
        <html:hidden property="inputVO.mode" value="analyze"/>
        <html:hidden property="inputVO.type" value="ticker"/>
        <input type="hidden" name="inputVO.stockId" value="<bean:write name='inputVO' property='stockId'/>"/>
        <input type="hidden" name="inputVO.period" value="<bean:write name='inputVO' property='period'/>"/>
        <input type="hidden" name="inputVO.option" value="<bean:write name='inputVO' property='option'/>"/>
        <input type="hidden" name="backupTicker" value="<bean:write name='inputVO' property='ticker'/>">
        <input type="hidden" name="inputVO.pageStyle" value="S"/>
        <%-- for the date calculation --%>
        <input type="hidden" name="startDate" value="<bean:write name='inputVO' property='startDate'/>">
        <input type="hidden" name="endDate" value="<bean:write name='inputVO' property='endDate'/>">

    <center>
    <table width="450" cellpadding="0" cellspacing="0" border="1">
        <tr><td class="head_color" valign="top" width="100%"><jsp:include page="../base/header.jsp" flush="true"><jsp:param name="subtitle" value="Analysis"/></jsp:include></td></tr>

        <tr><td valign="top">
                <logic:equal name="inputVO" property="display" value="one">
                <table cellpadding="0" cellspacing="1" class="page_back">
                    <tr><td  colspan="2" width="100%"><img id="stock_chart" src="../../drawchart?stockId=<bean:write name='stock' property='stockId'/>&chartType=MC&period=<bean:write name='inputVO' property='period'/>&option=<bean:write name='inputVO' property='option'/>"></td>
                    </tr>

                    <tr><td class="table_cell" valign="top" colspan="2">
                    <input type="button" name="button_now" value="Now" class="date_button_lrg" onClick="goNow()" disabled /><br>
                    <input type="button" name="button_left_1m" value="Set Test" class="date_button" onClick="setTestOption('bt')"/>
                    <input type="button" name="button_left_6m" value="< 6M" class="date_button" onClick="goByMonth('-6')"/>
                    <input type="button" name="button_left_12m" value="< 12M" class="date_button" onClick="goByMonth('-12')"/>
                    <input type="button" name="button_left_1d" value="< 1d" class="date_button" onClick="goByDay('-1')"/>
                    <input type="button" name="button_left_5d" value="< 5d" class="date_button" onClick="goByDay('-5')"/>
                    <input type="button" name="button_left_10d" value="< 10d" class="date_button" onClick="goByDay('-10')"/>

                        <input type="button" name="button_right_1m" value="> 1M" class="date_button" onClick="goByMonth('1')" disabled />
                        <input type="button" name="button_right_6m" value="> 6M" class="date_button" onClick="goByMonth('6')" disabled />
                        <input type="button" name="button_right_12m" value="> 12M" class="date_button" onClick="goByMonth('12')" disabled />
                        <input type="button" name="button_right_1d" value="> 1d" class="date_button" onClick="goByDay('1')" disabled />
                        <input type="button" name="button_right_5d" value="> 5d" class="date_button" onClick="goByDay('5')" disabled />
                        <input type="button" name="button_right_10d" value="> 10d" class="date_button" onClick="goByDay('10')" disabled />

                        </td>
                    </tr>
                    <tr><td colspan="2">&nbsp;<b><bean:write name='stock' property='companyName'/></b>
                            ( <b><bean:write name='stock' property='ticker'/></b> )
                                <logic:equal name="inputVO" property="onWatchList" value="N">
                                            [ <a href="../m/watch.do?inputVO.mode=addwatch&inputVO.type=blank&inputVO.stockId=<bean:write name='stock' property='stockId'/>" class="datalink">Add</a> ]
                                </logic:equal>
                                <logic:equal name="inputVO" property="onWatchList" value="Y">
                                        [On]
                                </logic:equal>
                            </td>
                    </tr>

                    <tr><td class="table_cell">Price</td>
                            <td class="table_cell"><b><bean:write name='stock' property='price'/></b></td>
                    </tr>
                    <tr><td class="table_cell">Power</td>
                    <td class="table_cell"><b><bean:write name="stockExt" property="powerIndex"/></b></td>
                    </tr>
                    <tr><td class="table_cell">Correlation</td>
                    <td class="table_cell"><b><bean:write name="stockExt" property="correlationIndex"/></b></td>
                    </tr>
                    <tr><td class="table_cell">Short Term</td>
                            <td class="table_cell">
                                    <logic:equal name="stockExt" property="shortTermStatus" value="BUY">
                                    <font color="#00ff00"><b>Buy</b></font>
                                    </logic:equal>
                                    <logic:equal name="stockExt" property="shortTermStatus" value="SELL">
                                    <font color="#ff0000"><b>Sell</b></font>
                                    </logic:equal>
                                    <logic:equal name="stockExt" property="shortTermStatus" value="HOLD">
                                    <font color="#0000ff"><b>Hold</b></font>
                                    </logic:equal>
                                    <%--(<bean:write name="stockExt" property="longTermTrend"/>)--%>
                            </td>
                    </tr>
                    <tr><td class="table_cell">Long Term</td>
                            <td class="table_cell">
                                    <logic:equal name="stockExt" property="majorTrend" value="U">
                                    <font color="#00ff00"><b>Up</b></font>
                                    </logic:equal>
                                    <logic:equal name="stockExt" property="majorTrend" value="D">
                                    <font color="#ff0000"><b>Down</b></font>
                                    </logic:equal>
                            </td>
                    </tr>

                    <logic:notEqual name="stockExt" property="warningMessage" value="">
                            <tr>
                                    <td colspan="2" class="table_cell">Warning:<br> <font color="#ff0000"><bean:write name="stockExt" property="warningMessage"/></font></td>
                            </tr>
                    </logic:notEqual>
                    <logic:equal name="stockExt" property="warningMessage" value="">
                            <tr>
                                    <td class="table_cell">Warning</td><td class="table_cell"><b>None</b></td>
                            </tr>
                    </logic:equal>

                    <%--
                    <tr><td class="table_cell">Market</td>
                            <td class="table_cell">
                                    <logic:equal name="inputVO" property="marketTrend" value="Up">
                                    <font color="#00ff00"><b>Up</b></font>
                                    </logic:equal>
                                    <logic:equal name="inputVO" property="marketTrend" value="Down">
                                    <font color="#ff0000"><b>Down</b></font>
                                    </logic:equal>

                            </td>
                    </tr>--%>
                    </table>
		
		</logic:equal>
					
				
                <logic:equal name="inputVO" property="display" value="list">
                    <table>
                        <TR><td colspan="2" align="center">More than one stock in DB:<br><br>
                                <logic:iterate id="stk" name="stockList">
                                <A href="javascript:submitIdForAnalyze('<bean:write name="stk" property="stockId"/>')"  class="datalink">
                                                <bean:write name="stk" property="ticker"/> (Market: <bean:write name="stk" property="marketType"/>)
                                </A><br><br>
                                </logic:iterate>
                                </td>
                        </TR>
                    </table>
                </logic:equal>
                <logic:equal name="inputVO" property="display" value="zero">
                    <table>
                        <TR><td colspan="2"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif">
                        <font color="#ff0000"><b>This ticker has not been found in our DB.</b></font></td>
                        </TR>
                    </table>
                </logic:equal>
                <logic:equal name="inputVO" property="display" value="error">
                    <table>
                        <TR><td width="800" colspan="2"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif"><font color="#ff0000">Error</font></td></TR>
                    </table>
                </logic:equal>
            </td></tr>

            <tr class="gray_back"><td>
            Ticker: <input type="text" name="inputVO.ticker" value="<bean:write name='inputVO' property='ticker'/>" size="10">
            <input type="button" value="Go" onclick="submit()">
                    <a href="../../mobi/recentlist.do">[ LIST ]</a>
                </td></tr>

            <tr class="gray_back"><td>Period:
                    <!--
                    <select name="inputVO.selectPeriod">
                        <option value="3">3 Months</option>
                        <option value="6">6 Months</option>
                        <option value="12">1 Year</option>
                        <option value="24">2 Years</option>
                    </select>
                    -->
                    <input type="radio" name="ckPeriod" value="3" onClick="setTestOption('opt')">Set Option
                    <input type="radio" name="ckPeriod" value="6" onClick="setPeriod('6')">6m
                    <input type="radio" name="ckPeriod" value="12" onClick="setPeriod('12')">1y
                    <input type="radio" name="ckPeriod" value="24" onClick="setPeriod('24')">2y
                </td></tr>

            <tr class="gray_back"><td>EMA's:
                    <input type="checkbox" name="doEMA20" onClick="setTestOption('ck')" />Set check
                    <input type="checkbox" name="doEMA50"/>50EMA
                    <input type="checkbox" name="doEMA100"/>100EMA
                    <input type="checkbox" name="doZone"/>Zone
                </td></tr>
                            
                           

                   <tr><td>
                    Market Indexes:
                </td></tr>



                    <tr>
				<td align="center"><A href="../analyze.do?inputVO.mode=analyze&inputVO.type=id&inputVO.period=6&inputVO.stockId=MKT001" class="toplink">Nasdaq Index</A> |
				<A href="../analyze.do?inputVO.mode=analyze&inputVO.type=id&inputVO.period=6&inputVO.stockId=MKT002" class="toplink">Dow Index</A> |
				<A href="../analyze.do?inputVO.mode=analyze&inputVO.type=id&inputVO.period=6&inputVO.stockId=MKT003" class="toplink">SP500 Index</A></td>
			</tr>

			<tr><td valign="bottom"><h2>Most Recent Ticker List!</h2></td></tr>
                        <tr><td><logic:iterate id="ticker" name="recentList">
                                    <a href="../analyze.do?inputVO.mode=analyze&inputVO.ticker=<bean:write name="ticker"/>&inputVO.type=ticker&inputVO.period=6" class="datalink2"><bean:write name="ticker"/></a>
                            </logic:iterate></td></tr>
			<tr><td height="100%" align="center" >&nbsp;</td></tr>
			<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>     
			
		</table></center>
	</html:form>
</BODY>
</HTML>
