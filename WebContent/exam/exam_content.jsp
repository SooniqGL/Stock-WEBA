<%@ taglib prefix="s" uri="/struts-tags" %>


<style type="text/css">

.button_lrg {
   width: 65px;
}

.button_lrg2 {
   width: 90px;
}

.button_lrg3 {
   width: 110px;
}

.button_sml {
   width: 40px;
}

</style>

<script type="text/javascript">

$( document ).ready(function() {
 	// set focus, buttons
 	QQ_examhelper.setFocus();
	
});

/*
$(window).resize(function() {
	QQ_exambarchart.clearCanvas();
	QQ_exambarchart.adjustCanvasWidth();
	QQ_exambarchart.drawChart();
}); */

</script>


<s:set var="stockExt" value="inputVO.stockExt" />
<s:set var="stock" value="#stockExt.stock" />

<div class="contentwrapper"><div style="margin:2px;">
		<form name="examForm" action="../s/exam_examcontent.do"> 
				<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="S" />
                <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
                <input type="hidden" id="inputVO.stockId" name="inputVO.stockId" value="<s:property value='%{inputVO.stockId}'/>"/>
                <input type="hidden" id="inputVO.period" name="inputVO.period" value="6"/>
                
                <%--input type="hidden" name="inputVO.option" value="<bean:write name='inputVO' property='option'/>"/>
                
                <input type="hidden" name="inputVO.pageStyle" value="S"/>

                <input type="hidden" name="inputVO.showFlag" value="<bean:write name='inputVO' property='showFlag'/>">
                <input type="hidden" name="inputVO.doSP500" value="<bean:write name='inputVO' property='doSP500'/>">

                <input type="hidden" name="backupTicker" value="<bean:write name='inputVO' property='ticker'/>"--%>
                
                <%-- for the date calculation --%>
                <input type="hidden" id="startDate" name="startDate" value="<s:property value='%{inputVO.startDate}'/>">
                <input type="hidden" id="endDate" name="endDate" value="<s:property value='%{inputVO.endDate}'/>">
                <input type="hidden" id="stopType" value="N"/>

        <table width="100%" class="mytable1">
	
             <tr>
                <td height="20" colspan="2" class="mycell1">&nbsp;&nbsp;Ticker: <input type="text" id="inputVO.ticker" name="inputVO.ticker" value="<s:property value='%{inputVO.ticker}'/>" size="10"/>
                        <input type="submit" name="button_self" value=" Self Select " class="button_lrg3" onClick="QQ_examhelper.doSelfSelect()"/>
                        <input type="button" name="button_auto" value=" Auto Select " class="button_lrg3" onClick="QQ_examhelper.doAutoSelect()"/>
                </td>
             </tr>

          <s:if test="%{inputVO.display == \"one\"}">
          		<%-- Note: substring() seems not work well if the size is less than the range.  So, check size here first. --%>
          	   <s:if test="%{#stock.companyName.length() > 20}">
          	   		<s:set var="comName" value="%{#stock.companyName.substring(0, 20)}"/>
          	   </s:if>
          	   <s:else>
          	   		<s:set var="comName" value="#stock.companyName"/>
          	   </s:else>
          	   
               <tr><td colspan="2" class="mycell1">&nbsp;&nbsp;Company: <b><s:property value='#comName'/></b>
                                    ( <b><s:property value='%{#stock.ticker}'/></b> )
                  
                  <input type="button" id="start_over" name="start_over"  value="Start Over" class="button_lrg2" onClick="QQ_examhelper.startOver()" /> 
                  <input type="button" id="button_right_xd" name="button_right_xd"  value=">" class="button_sml" onClick="QQ_examhelper.goByDayLoop('1', 'true')" />
                  <input type="button" id="button_right_xxd" name="button_right_xxd" value=">>" class="button_sml" onClick="QQ_examhelper.goByDayLoop('5', 'true')" />
                  <input type="button" id="button_right_1d" name="button_right_1d"  value="> 1d" class="button_lrg" onClick="QQ_examhelper.goByDay('1')" />
                  <input type="button" id="button_right_5d" name="button_right_5d"  value="> 5d" class="button_lrg" onClick="QQ_examhelper.goByDay('5')" />
                  <input type="button" id="button_right_10d" name="button_right_10d" value="> 10d" class="button_lrg" onClick="QQ_examhelper.goByDay('10')" />
                  <input type="button" id="button_right_1m" name="button_right_1m"  value="> 1M" class="button_lrg" onClick="QQ_examhelper.goByMonth('1')" />
                               </td></tr>
                <tr><td height="20" colspan="2" class="mycell1">&nbsp;&nbsp;Stop Option:
                    <input type="radio" name="ckStop" value="N" checked >None
                    <input type="radio" name="ckStop" value="R" >Regular
                    <input type="radio" name="ckStop" value="T" >Trailing(%)
                    &nbsp;&nbsp;&nbsp;&nbsp;Stop($/%) <input type="text" id="stopAmount" value="" size="6">
                    <input type="button" id="adjustStop" name="adjustStop"  value="Adjust" title="Adjust Stop" class="button_lrg" onClick="QQ_examhelper.adjustStopFunc()" disabled />
                    &nbsp; Action
                    <input type="button" id="button_long" name="button_long"  value="Long" class="button_lrg" onClick="QQ_examhelper.goLong()" />
                    <input type="button" id="button_short" name="button_short"  value="Short" class="button_lrg" onClick="QQ_examhelper.goShort()" />
                    <input type="button" id="button_close" name="button_close"  value="Close It" class="button_lrg" onClick="QQ_examhelper.goClose()" disabled />
                    
                    
                </td></tr>
				
                <tr><%-- Do not show image
                    <td width="682"><img id="stock_chart" src="../drawchart?stockId=<bean:write name='stock' property='stockId'/>&period=6&option=NNNY"></td>
                    --%>
                    <td width="682" valign="top"  class="mycell1">
                     <canvas id="canvas" width="680" height="400" style="position:relative; background-color: #ff0; ">AAAABBBBB</canvas>
                    </td>
                    <td valign="top" align="left" class="mycell3" style="padding:5px;" >
                        <div id="priceBoard"></div><hr>
                        <div id="positionBoard"></div>
                        <%--
                        <table cellpadding="0" cellspacing="0" width="100%">
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
                                                <%--(<bean:write name="stockExt" property="longTermTrend"/>)
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
                                </logic:equal> --%>
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
                            </tr>
                            <tr><td height="100%" colspan="2">&nbsp;</td>
                            </tr></table>--%>

                    </td></tr>


                <%-- performance of the holding 
                <tr class="gray_back"><td height="20" colspan="2">

                    </td></tr>--%>
                            
           </s:if>

			<s:if test="%{inputVO.mode != \"blank\"}">
			<s:if test="%{inputVO.display == \"zero\"}">
           <TR><td width="800" colspan="2" class="mycell3"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif">
                        <font color="#ff0000"><b>This ticker has not been found in our DB.</b></font></td>
                        </TR>
                </s:if>
            </s:if>
			<s:if test="%{inputVO.display == \"error\"}">
                    <TR><td width="800" colspan="2" class="mycell3"><br>
                    &nbsp;&nbsp;<img src="../image/reddot.gif"><font color="#ff0000">Error</font></td></TR>
            </s:if>
            
            <s:if test="%{inputVO.display == \"blank\"}">
                        <TR><td style="width:800px; padding:5px; " colspan="10"><br>
                        <span class="instruction_text">
                        &nbsp;&nbsp;<img src="../image/reddot.gif">Please enter a ticker, click Self Select or click Auto Select to let system to random assign stock for you.
                        </span>
                        <br><br>
                        <span class="instruction_text">
                        &nbsp;&nbsp;Please read the help to understand the functions on this screen.
                        </span>
                        </td></TR>
                    </s:if>

			<s:if test="%{inputVO.display == \"one\"}">
 			 <tr>
                <td colspan="2" class="mycell3">&nbsp;History and performance --
                    <b><s:property value='%{#stock.companyName}'/></b>
                    ( <b><s:property value='%{#stock.ticker}'/></b> )
                    <br>
                    <div id="tradeHistory"></div>
                </td></tr>
            </s:if>

        </table>
	</form>
<form name="dataForm" action=""> 
<input type="hidden" id="dailyDataStr" name="dailyDataStr" value="<s:property value='%{inputVO.dailyDataStr}'/>">
</form>
                </div>
            </div>
            