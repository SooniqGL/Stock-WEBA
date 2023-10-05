<%@ taglib prefix="s" uri="/struts-tags" %>


<%--
<script type="text/javascript" language="JavaScript">
// Init section

	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	run_after_body();
        
        
</script>
--%>


<style type="text/css">
.go_button {
   width: 50px;
   height: 17px;
}

.date_button {
   width: 55px;
   height: 20px;
}

.date_button_lrg {
   width: 100px;
}

.tug_button {
   width: 30px;
   height: 17px;
}


</style>

<script type="text/javascript">

$( document ).ready(function() {
 	// set focus, buttons
 	Q.chart.setFocus();
	QQ_staticchart.adjustButtons();

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

</script>


<s:set var="stockList" value="inputVO.stockList" />
<s:set var="stockExt" value="inputVO.stockExt" />
<s:set var="stock" value="#stockExt.stock" />


<div id="overlaycontent" title="Click outside of the dialog (or ESC) to close.">Ticker List
            <div id="tickerList" style="background:#ffffef;margin:10px; width:680px;height:360px;overflow-x:hidden;overflow-y:scroll;overflow:-moz-scrollbars-vertical !important;">
                        <%-- do not use it any more
                        <logic:equal name="inputVO" property="showFlag" value="M">
                            <input type="button" onclick="flipTickerListR()" value="#" class="tug_button" />
                            <logic:iterate id="ticker" name="recentList">
                                    <A href="javaScript:goTicker('<bean:write name="ticker"/>')" class="datalink2"><bean:write name="ticker"/></a>
                            </logic:iterate>
                        </logic:equal> --%></div>
</div>
<div id="overlaypage" title="Manage watch list.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper"><div style="margin:2px; background-color:#dfdfdf;">
    <form name="analyzeForm" action="../s/analyze_basicanalyze.do" onsubmit="javascript:Q.chart.submitTickerForAnalyze()">
                <input type="hidden" id="inputVO.mode" name="inputVO.mode" value="analyze" />
                <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
                <input type="hidden" id="inputVO.stockId" name="inputVO.stockId" value="<s:property value='%{inputVO.stockId}'/>"/>
                <input type="hidden" id="inputVO.period" name="inputVO.period" value="<s:property value='%{inputVO.period}'/>"/>
                <input type="hidden" id="inputVO.option" name="inputVO.option" value="<s:property value='%{inputVO.option}'/>"/>
                <input type="hidden" id="backupTicker" name="backupTicker" value="<s:property value='%{inputVO.ticker}'/>">
                <input type="hidden" id="inputVO.pageStyle" name="inputVO.pageStyle" value="S"/>
                <input type="hidden" id="inputVO.showFlag" name="inputVO.showFlag" value="<s:property value='%{inputVO.showFlag}'/>">
                <input type="hidden" id="inputVO.doSP500" name="inputVO.doSP500" value="<s:property value='%{inputVO.doSP500}'/>">

                <%-- for the date calculation --%>
                <input type="hidden" id="startDate" name="startDate" value="<s:property value='%{inputVO.startDate}'/>">
                <input type="hidden" id="endDate" name="endDate" value="<s:property value='%{inputVO.endDate}'/>">


<%-- what are these stuff ...  html created, but not used now --%>

                <input type="hidden" id="mTickerList" name="mTickerList" value="<s:property value='%{inputVO.MTickerList}'/>" />
                <input type="hidden" id="rTickerList" name="rTickerList" value="<s:property value='%{inputVO.RTickerList}'/>" />
    

              
           
      <table style="width:100%; min-height:100px;" class="mytable1"> 
      
	<%--tr class="tail_color"><td height="40" align="center" colspan="2"><font  class="main_title">Stock Analysis</font></td></tr--%>
	<%--&nbsp;&nbsp;
	[<A href="../a/analyze.do?inputVO.mode=blank&inputVO.pageStyle=D" class="sublink">Dynamic</A>]&nbsp;--%>

                <%--<tr><%-- overflow:scroll -> or overflow:auto -> may show both --%>
                <%--<td colspan="2"><%--div id="tickerList" style="background:#fffff0;width:800px;height:80px;overflow-x:hidden;overflow-y:scroll;overflow:-moz-scrollbars-vertical !important;">
                        <%-- do not use it any more
                        <logic:equal name="inputVO" property="showFlag" value="M">
                            <input type="button" onclick="flipTickerListR()" value="#" class="tug_button" />
                            <logic:iterate id="ticker" name="recentList">
                                    <A href="javaScript:goTicker('<bean:write name="ticker"/>')" class="datalink2"><bean:write name="ticker"/></a>
                            </logic:iterate>
                        </logic:equal> </div> 
                </td></tr>--%>

                <tr>
                <td height="20" colspan="2" class="mycell1">
                &nbsp; Ticker: <input type="text" id="inputVO.ticker" name="inputVO.ticker" value="<s:property value='%{inputVO.ticker}'/>"
                        size="10" onClick="Q.chart.removeTicker()" onBlur="Q.chart.resetTicker()">
                        <input type="submit" value="Go" class="go_button"> <%--<input type="button" value=">" onclick="Javascript:expendIt()"> --%>
                        <input type="radio" name="ckPeriod" value="3" onClick="Q.chart.setPeriod('3')">3m
                        <input type="radio" name="ckPeriod" value="6" onClick="Q.chart.setPeriod('6')">6m
                        <input type="radio" name="ckPeriod" value="12" onClick="Q.chart.setPeriod('12')">1y
                        <input type="radio" name="ckPeriod" value="24" onClick="Q.chart.setPeriod('24')">2y
                        <input type="checkbox" id="doEMA20" onClick="Q.chart.setOption()"/>20EMA
                        <input type="checkbox" id="doEMA50" onClick="Q.chart.setOption()"/>50EMA
                        <input type="checkbox" id="doEMA100" onClick="Q.chart.setOption()"/>100EMA
                        <input type="checkbox" id="doZone" onClick="Q.chart.setOption()"/>Zone 
                        <input type="checkbox" id="doMacd" onClick="Q.chart.setOption()"/>MACD &nbsp;
                        

                </td>
                </tr> 
					<s:if test="%{inputVO.display == \"one\"}">
					
                            <tr><td colspan="2" class="mycell1">&nbsp;<b><s:property value='%{#stock.companyName}'/>, Market: <s:property value='%{#stock.marketType}'/></b>
                                    
                                    <%--
                                    <a href="#" class="datalink2" onClick='show_div("div_200", "overlay_cloak2")' onBlur='hide_div()'><b><bean:write name='stock' property='companyName'/></b></a>
                                    --%>
                                    ( <b><s:property value='%{#stock.ticker}'/></b> )
                                    
                                    <s:if test="%{inputVO.onWatchList == \"N\"}">
                                         [ <A href="#" onclick="QQ_addwatch.getInitData('<s:property value="%{inputVO.stockId}"/>', 'Add');" class="datalink">Watch/Add</a> ]
                                    
                                    </s:if>
                                    <s:if test="%{inputVO.onWatchList == \"Y\"}">
                                    [ <A href="#" onclick="QQ_addwatch.getInitData('<s:property value="%{inputVO.stockId}"/>', 'Update');" class="datalink">Watch/Edit</a> ]              
                                    </s:if>
                                                    <%--
                                                    <a href="#" onclick="doAjaxCall(); return false;">Ajax</a>
                                                    --%>
                                            
                                    </td>
                            </tr>
                            <tr><td width="686" align="center" class="mycell1"><img id="stock_chart" src="../drawchart?stockId=<s:property value='%{inputVO.stockId}'/>&period=<s:property value='%{inputVO.period}'/>&option=<s:property value='%{inputVO.option}'/>">
                                    <s:if test="%{inputVO.doSP500 == \"Y\"}">
                                        <img id="stock_chart2" src="../drawchart?stockId=MKT003&period=<s:property value='%{inputVO.period}'/>&option=<s:property value='%{inputVO.option}'/>">
                                    </s:if>
                                    
                                    <s:if test="%{#stockExt.warningMessage != \"\"}">    
                                        Warning: <font color="#ff0000"><s:property value='%{#stockExt.warningMessage}'/></font>
                                    </s:if>
                                </td>
                                    <td valign="top" align="center" class="mycell3">
                                    <table class="gridtable_noborder" width="100%">
                                    <tr><td class="table_cell" valign="top">
                                    <input type="button" id="button_now" value="Now" class="date_button_lrg" onClick="QQ_staticchart.goNow()" disabled /><br>
                                    <input type="button" id="button_left_1m" value="< 1M" class="date_button" onClick="QQ_staticchart.goByMonth('-1')"/><input type="button" id="button_right_1m" value="> 1M" class="date_button" onClick="QQ_staticchart.goByMonth('1')" disabled /><br>
                                    <input type="button" id="button_left_6m" value="< 6M" class="date_button" onClick="QQ_staticchart.goByMonth('-6')"/><input type="button" id="button_right_6m" value="> 6M" class="date_button" onClick="QQ_staticchart.goByMonth('6')" disabled /><br>
                                    <input type="button" id="button_left_12m" value="< 12M" class="date_button" onClick="QQ_staticchart.goByMonth('-12')"/><input type="button" id="button_right_12m" value="> 12M" class="date_button" onClick="QQ_staticchart.goByMonth('12')" disabled /><br>
                                    <input type="button" id="button_left_1d" value="< 1d" class="date_button" onClick="QQ_staticchart.goByDay('-1')"/><input type="button" id="button_right_1d" value="> 1d" class="date_button" onClick="QQ_staticchart.goByDay('1')" disabled /><br>
                                    <input type="button" id="button_left_5d" value="< 5d" class="date_button" onClick="QQ_staticchart.goByDay('-5')"/><input type="button" id="button_right_5d" value="> 5d" class="date_button" onClick="QQ_staticchart.goByDay('5')" disabled /><br>
                                    <input type="button" id="button_left_10d" value="< 10d" class="date_button" onClick="QQ_staticchart.goByDay('-10')"/><input type="button" id="button_right_10d" value="> 10d" class="date_button" onClick="QQ_staticchart.goByDay('10')" disabled /><br>

                                    <input type="checkbox" id="doBARS" onClick="Q.chart.setOption()"/>Include BARS<br/>
                                    <input type="checkbox" id="doSP500" onClick="Q.chart.setOption2()"/>Include SP500
                                    </td>
                                    </tr>
                                    </table>
                                    <hr>
                                    <table  class="gridtable_noborder" width="100%">
                                    <tr><td class="table_cell">Price</td>
                                    <td class="table_cell"><b><s:property value='%{#stock.price}'/></b></td>
                            </tr>
                            <tr><td class="table_cell">Power</td>
                            <td class="table_cell"><b><s:property value='%{#stockExt.powerIndex}'/></b></td>
                            </tr>
                            <tr><td class="table_cell">Correlation</td>
                            <td class="table_cell"><b><s:property value='%{#stockExt.correlationIndex}'/></b></td>
                            </tr>
                            <tr><td class="table_cell">MaxUp</td>
                            <td class="table_cell"><font color="#00ff00"><b><s:property value='%{#stockExt.maxUpPercent}'/></b></font></td>
                            </tr>
                            <tr><td class="table_cell">MaxDown</td>
                            <td class="table_cell"><font color="#ff0000"><b>-<s:property value='%{#stockExt.maxDownPercent}'/></b></font></td>
                            </tr>
                            <tr><td class="table_cell">Short Term</td>
                                    <td class="table_cell">
                                    <s:if test="%{#stockExt.shortTermStatus == \"BUY\"}">
										<font color="#00ff00"><b>Buy</b></font>
									</s:if> <s:if test="%{#stockExt.shortTermStatus == \"SELL\"}">
										<font color="#ff0000"><b>Sell</b></font>
									</s:if> <s:if test="%{#stockExt.shortTermStatus == \"HOLD\"}">
										<font color="#0000ff"><b>Hold</b></font>
									</s:if> <%--(<bean:write name="stockExt" property="longTermTrend"/>)--%>
                                    </td>
                            </tr>
                            <tr><td class="table_cell">Long Term</td>
                                    <td class="table_cell">
                                    <s:if test="%{#stockExt.majorTrend == \"U\"}">
										<font color="#00ff00"><b>Up</b></font>
									</s:if> 
									<s:if test="%{#stockExt.majorTrend == \"D\"}">
										<font color="#ff0000"><b>Down</b></font>
									</s:if>
								</td>
                            </tr>
                            
                            
                            <s:if test="%{#stockExt.warningMessage == \"\"}">    
                                    <tr>
                                            <td class="table_cell">Warning</td><td class="table_cell"><b>None</b></td>
                                    </tr>
                            </s:if>
                            
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
                            <tr><td height="100%" colspan="2">&nbsp;</td>
                            </tr></table></td></tr>


                            <%--
                            <TR><td rowspan="2"><b><bean:write name='stock' property='companyName'/></b></td>
                                            <td align="center">Ticker</td><td align="center">Watch?</td><td align="center">Price</td>
                                            <td align="center">William</td><td align="center">Growth</td><td align="center">Power</td>
                                            <td align="center">Correlation</td><td align="center">Strength</td><td align="center" width="100">Short Term</td>
                                    </tr>
                                    <tr>
                                            <td align="center"><b><bean:write name='stock' property='ticker'/></b></td>
                                            <td align="center">
                                            <logic:equal name="inputVO" property="onWatchList" value="N">
                                                    [ <A href="../m/watch.do?inputVO.mode=addwatch&inputVO.type=blank&inputVO.stockId=<bean:write name='stock' property='stockId'/>" class="datalink">Add</a> ]
                                            </logic:equal>
                                            <logic:equal name="inputVO" property="onWatchList" value="Y">
                                                    [On]
                                            </logic:equal>
                                            </td>
                                            <td align="center"><b><bean:write name='stock' property='price'/></b></td>
                                            <td align="center"><b><bean:write name="stockExt" property="williamIndex"/>/<bean:write name="stockExt" property="williamIndex2"/></b></td>
                                            <td align="center"><b><bean:write name="stockExt" property="growthPerMonth"/>%</b></td>


                                            <td align="center"><b><bean:write name="stockExt" property="powerIndex"/></b></td>
                                            <td align="center"><b><bean:write name="stockExt" property="correlationIndex"/></b></td>

                                            <td align="center"><b><bean:write name="stockExt" property="signalStrength"/></b></td>

                                            <td align="center">
                                            <logic:equal name="stockExt" property="shortTermStatus" value="BUY">
                                            <font color="#00ff00"><b>Buy</b></font>
                                            </logic:equal>
                                            <logic:equal name="stockExt" property="shortTermStatus" value="SELL">
                                            <font color="#ff0000"><b>Sell</b></font>
                                            </logic:equal>
                                            <logic:equal name="stockExt" property="shortTermStatus" value="HOLD">
                                            <font color="#0000ff"><b>Hold</b></font>
                                            </logic:equal>
                                            <%--(<bean:write name="stockExt" property="longTermTrend"/>)
                                            </td>
                                    </tr>--%>
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
				
				<s:if test="%{inputVO.display == \"list\"}">
                        <TR><td width="800" colspan="2" align="center" class="mycell3">More than one stock in DB:<br><br>
                                <s:iterator value="#stockList" >
                                <A href="javascript:Q.chart.submitIdForAnalyze('<s:property value="stockId"/>')"  class="datalink">
                                                <s:property value="ticker"/> (Market: <s:property value="marketType"/>)
                                </A><br><br>
                                </s:iterator>
                                </td>
                        </TR>
                </s:if>
                
                <s:if test="%{inputVO.display == \"zero\"}">
                        <TR><td width="800" colspan="2"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif">
                        <font color="#ff0000"><b>This ticker has not been found in our DB.</b></font></td>
                        </TR>
                </s:if>
                
                <s:if test="%{inputVO.display == \"error\"}">
                        <TR><td width="800" colspan="2"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif"><font color="#ff0000">Error: Please try a different Ticker or try later.</font></td></TR>
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
			</table>
             
                        
	</form>
</div></div>

<jsp:include page="../template/watchlist/addwatch_template.jsr"></jsp:include>