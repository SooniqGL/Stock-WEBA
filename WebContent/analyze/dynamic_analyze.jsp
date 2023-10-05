<%@ taglib prefix="s" uri="/struts-tags" %>


<style type="text/css">
.go_button {
   width: 50px;
   height: 17px;
}

</style>	

<script type="text/javascript">

$( document ).ready(function() {
 	// set focus, buttons
 	Q.chart.setFocus();
	
	// setup the dialog overlay
	QQ_chartoverlay.init("overlaycontent");
	
	// this is ugly, as the dialog is running different in IE
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
    var trident = ua.indexOf('Trident/');

    if (msie > 0 || trident > 0) {
		QQ_pageoverlay.init("overlaypage", 700, 490);
    } else {
    	QQ_pageoverlay.init("overlaypage", 700, 540);
    }
	
});


$(window).resize(function() {
	QQ_barchart.clearCanvas();
	QQ_barchart.adjustCanvasWidth();
	QQ_barchart.drawChart();
});

</script>


<s:set var="stockList" value="inputVO.stockList" />
<s:set var="stockExt" value="inputVO.stockExt" />
<s:set var="stock" value="#stockExt.stock" />


<%-- this overlay will not show at the load --%>
<div id="overlaycontent" title="Click outside of the dialog (or ESC) to close.">Ticker List
            <div id="tickerList" style="background:#ffffef;margin:10px;width:680px;height:360px;overflow-x:hidden;overflow-y:scroll;overflow:-moz-scrollbars-vertical !important;">
                        <%-- do not use it any more
                        <logic:equal name="inputVO" property="showFlag" value="M">
                            <input type="button" onclick="flipTickerListR()" value="#" class="tug_button" />
                            <logic:iterate id="ticker" name="recentList">
                                    <A href="javaScript:goTicker('<bean:write name="ticker"/>')" class="datalink2"><bean:write name="ticker"/></a>
                            </logic:iterate>
                        </logic:equal> --%></div>
</div>
<div id="overlaypage" title="Manage watch list.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper"><div style="margin:2px;background-color:#dfdfdf;">
		<form name="analyzeForm" action="../s/analyze_dynamicanalyze.do" onsubmit="javascript:Q.chart.submitTickerForAnalyze()"> 
				<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="analyze" />
                <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
                <input type="hidden" id="inputVO.stockId" name="inputVO.stockId" value="<s:property value='%{inputVO.stockId}'/>"/>
                <input type="hidden" id="inputVO.period" name="inputVO.period" value="<s:property value='%{inputVO.period}'/>"/>
                <input type="hidden" id="inputVO.option" name="inputVO.option" value="<s:property value='%{inputVO.option}'/>"/>
                <input type="hidden" id="backupTicker" name="backupTicker" value="<s:property value='%{inputVO.ticker}'/>">
                <input type="hidden" id="inputVO.pageStyle" name="inputVO.pageStyle" value="D"/>
                
                <input type="hidden" id="dailyDataStr" name="dailyDataStr" value="<s:property value='%{inputVO.dailyDataStr}'/>">
                <input type="hidden" id="weeklyDataStr" name="weeklyDataStr" value="<s:property value='%{inputVO.weeklyDataStr}'/>">
                <input type="hidden" id="marketFlagList" name="marketFlagList" value="<s:property value='%{inputVO.marketFlagList}'/>">
                <input type="hidden" id="marketWeeklyFlagList" name="marketWeeklyFlagList" value="<s:property value='%{inputVO.marketWeeklyFlagList}'/>">

				<input type="hidden" id="inputVO.showFlag" name="inputVO.showFlag" value="<s:property value='%{inputVO.showFlag}'/>">
                

			<%-- what are these stuff ...  html created, but not used now --%>

                <input type="hidden" id="mTickerList" name="mTickerList" value="<s:property value='%{inputVO.MTickerList}'/>" />
                <input type="hidden" id="rTickerList" name="rTickerList" value="<s:property value='%{inputVO.RTickerList}'/>" />
             
            
			<table style="width:100%; background-color:#dfdfdf;">  				
				<tr>
				<td height="25" colspan="10" class="mycell1"><table class="mytable1"><tr><td class="mycell1">
                                                &nbsp; Ticker: <input type="text" id="inputVO.ticker" name="inputVO.ticker" value="<s:property value='%{inputVO.ticker}'/>" 
					size="10" onClick="Q.chart.removeTicker()" onBlur="Q.chart.resetTicker()">
					<input type="submit" value="Go" class="go_button">
					&nbsp;&nbsp;
						<input type="radio" name="ckPeriod" value="3" onClick="Q.chart.setPeriod('3')">3m
						<input type="radio" name="ckPeriod" value="6" onClick="Q.chart.setPeriod('6')">6m
						<input type="radio" name="ckPeriod" value="12" onClick="Q.chart.setPeriod('12')">1y
						<input type="radio" name="ckPeriod" value="24" onClick="Q.chart.setPeriod('24')">2y
						<input type="checkbox" id="doEMA20" name="doEMA20" onClick="Q.chart.setOption()" />20-E
						<input type="checkbox" id="doEMA50" name="doEMA50" onClick="Q.chart.setOption()" />50-E
						<input type="checkbox" id="doEMA100" name="doEMA100" onClick="Q.chart.setOption()"/>100-E
                                                <input type="checkbox" id="doZone" onClick="Q.chart.setOption()"/>Zone
                                                <input type="checkbox" id="doBARS" onClick="Q.chart.setOption()"/>Bars
                                                <input type="checkbox" id="doWeekly" onClick="Q.chart.setWeekly()"/>Weekly
                                                <input type="checkbox" id="doReverse" onClick="Q.chart.setReverse()"/>Reverse
                                            </td><td class="mycell1">&nbsp;&nbsp;<img id="ajax_loading" width="20" height="20" src="../image/processing.gif" style="display: none"></td></tr></table>
                                </td></tr>
					
					
					<s:if test="%{inputVO.display == \"list\"}">
                        <TR><td width="800" colspan="10" align="center" class="mycell1">More than one stock in DB:<br><br>
                                <s:iterator value="#stockList" >
                                <A href="javascript:Q.chart.submitIdForAnalyze('<s:property value="stockId"/>')"  class="datalink">
                                                <s:property value="ticker"/> (Market: <s:property value="marketType"/>)
                                </A><br><br>
                                </s:iterator>
                                </td>
                        </TR>
                	</s:if>	
					
					<s:if test="%{inputVO.display == \"zero\"}">
                        <TR><td width="800" colspan="10" class="mycell1"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif">
                        <font color="#ff0000"><b>This ticker has not been found in our DB.</b></font></td>
                        </TR>
                	</s:if>
                	
                	<s:if test="%{inputVO.display == \"none\"}">
                        <TR><td style="width:800px; padding:5px;" colspan="10"><br>
                        <span class="instruction_text">
                        &nbsp;&nbsp;<img src="../image/reddot.gif">Please enter a ticker or click ticker from the history list.
                        </span>
                        <br><br>
                        <span class="instruction_text">
                        &nbsp;&nbsp;Please read the help to understand the functions on this screen.
                        </span>
                        </td></TR>
                    </s:if>
					
					<s:if test="%{inputVO.display == \"error\"}">
                        <TR><td width="800" colspan="10" class="mycell1"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif"><font color="#ff0000">Error: Please try a different Ticker or try later.</font></td></TR>
                	</s:if>
					
					<s:if test="%{inputVO.display == \"one\"}">
						<tr>
							<td colspan="10" align="center" class="mycell1"><%--img align="right" id="reddot" src="../image/reddot.gif" alt="Indicator Image" width="14" height="14"--%>
							<canvas id="chart"  width="825" height="450" style="position:relative;background-color: #ff0; ">Canvas is not supported by your browser.</canvas>
							</td>
						</tr>	
						<TR><td rowspan="2" class="mycell3" align="center"><b><s:property value='%{#stock.companyName}'/>(<s:property value='%{#stock.marketType}'/>)</b>
                                                        <a href="#" onclick="Q.chart.getChart(); return false;" class="datalink">Download</a>
                                                        <input type="checkbox" id="doMacd" name="doMacd" /> MACD
                                                        <input type="checkbox" id="gfFlag" name="gfFlag" /> G&F
                                                    </td>
								<td align="center" class="mycell3">Ticker</td><td align="center" class="mycell3">Watch?</td><td align="center" class="mycell3">Price</td>
								<td align="center" class="mycell3">William</td><td align="center" class="mycell3">Growth</td><td align="center" class="mycell3">Power</td>
								<td align="center" class="mycell3">Correlation</td><td align="center" class="mycell3">Strength</td><td align="center" width="100" class="mycell3">Short Term</td>
							</tr>
							<tr>
								<td align="center" class="mycell3"><b><s:property value='%{#stock.ticker}'/></b></td>
								<td align="center" class="mycell3">
                                    <s:if test="%{inputVO.onWatchList == \"N\"}">
                                         [ <A href="#" onclick="QQ_addwatch.getInitData('<s:property value="%{inputVO.stockId}"/>', 'Add');" class="datalink">Watch/Add</a> ]
                                    
                                    </s:if>
                                    <s:if test="%{inputVO.onWatchList == \"Y\"}">
                                    [ <A href="#" onclick="QQ_addwatch.getInitData('<s:property value="%{inputVO.stockId}"/>', 'Update');" class="datalink">Watch/Edit</a> ]              
                                    </s:if>
                                            
								</td>
								<td align="center" class="mycell3"><b><s:property value='%{#stock.price}'/></b></td>
							 	<td align="center" class="mycell3"><b><s:property value='%{#stockExt.williamIndex}'/>/<s:property value='%{#stockExt.williamIndex2}'/></b></td>
								<td align="center" class="mycell3"><b><s:property value='%{#stockExt.growthPerMonth}'/>%</b></td>
							 
								<%--RSquare: 
								<bean:write name="stockExt" property="rrSquare"/>,
								--%>
								
								<td align="center" class="mycell3"><b><s:property value='%{#stockExt.powerIndex}'/></b></td>
								<td align="center" class="mycell3"><b><s:property value='%{#stockExt.correlationIndex}'/></b></td>
							
								<%--
								Oscillation:
								<bean:write name="stockExt" property="oscillateIndex"/>,
								--%>
								<td align="center" class="mycell3"><b><s:property value='%{#stockExt.signalStrength}'/></b></td>
								<td align="center" class="mycell3">
									<s:if test="%{#stockExt.shortTermStatus == \"BUY\"}">
										<font color="#00ff00"><b>Buy</b></font>
									</s:if> <s:if test="%{#stockExt.shortTermStatus == \"SELL\"}">
										<font color="#ff0000"><b>Sell</b></font>
									</s:if> <s:if test="%{#stockExt.shortTermStatus == \"HOLD\"}">
										<font color="#0000ff"><b>Hold</b></font>
									</s:if>
								<%--(<bean:write name="stockExt" property="longTermTrend"/>)--%>
								</td>
							</tr>
							<%--
							<tr>
								<td colspan="9"><bean:write name="stock" property="aboutCompany"/>&nbsp;</td>
							</tr>
							
							<logic:notEqual name="stockExt" property="warningMessage" value="">
							<tr>
								<td colspan="10"><b>Warning:</b> <font color="#ff0000"><bean:write name="stockExt" property="warningMessage"/></font></td>
							</tr>
							</logic:notEqual>
							--%>
						<%--
						<tr class="gray_back"><td colspan="10">Note: When market is open, this screen shows 20-minutes delay data.</td></tr>
						--%>
					</s:if>
					
				</table>

	</form>		
<%-- form to download image for the display, is_save = Y for attachment download --%>
<form name="getChartForm" method="post" action="../drawchart">
    <input id="stockId" name="stockId" type="hidden" value="<s:property value='%{inputVO.stockId}'/>"></input>
    <input id="period" name="period" type="hidden" value="<s:property value='%{inputVO.period}'/>"></input>
    <input id="option" name="option" type="hidden" value="<s:property value='%{inputVO.option}'/>"></input>
    <input id="is_save" name="is_save" type="hidden" value="Y"></input>
    <input id="weekly" name="weekly" type="hidden" value="N"></input>
    <input id="displayDate" name="displayDate" type="hidden" value=""></input>
    <input id="chartType" name="chartType" type="hidden" value=""></input>
    <input id="drawGF" name="drawGF" type="hidden" value=""></input>
</form>											

</div></div>

<jsp:include page="../template/watchlist/addwatch_template.jsr"></jsp:include>