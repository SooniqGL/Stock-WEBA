<%@ taglib prefix="s" uri="/struts-tags" %>
 
<style type="text/css">

.date_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
	FONT-SIZE: 12pt;
	COLOR: #0000cf;
	TEXT-DECORATION: none;
	WIDTH: 100px;
}
</style>

<s:set var="dateList" value="inputVO.marketPulseDateList" />


<s:set var="dateListSize" value="#dateList.size()"></s:set>
<s:if test="%{#dateListSize <= 2}">
	<s:set var="isPrevDisabled" value="disabled"></s:set>
</s:if>
<s:else>
	<s:set var="isPrevDisabled" value=""></s:set>
</s:else>

<div class="contentwrapper3"><div style="margin:2px;"> 


	<form name="marketForm" action="">
	<input type="hidden" id="marketType" name="marketType" value="<s:property value='%{inputVO.marketType}'/>" />
	<input type="hidden" id="chartType" name="chartType" value="<s:property value='%{inputVO.chartType}'/>" />
	<input type="hidden" id="screenIndex" name="screenIndex" value="0" />

                    
        <table width="100%" cellpadding="0" cellspacing="0" height="100%"> 
	
	<tr><td colspan="2" height="40">&nbsp;
		<font class="main_title2">General Market Statistics</font></td></tr>
		
	                
                <tr><td colspan="2">&nbsp;&nbsp;
                    <input type="button" name="button_prev" value="Prev" class="date_button" onClick="Q.market.showChartWithShift('1')" <s:property value='%{#isPrevDisabled}'/> />
                    <input type="button" name="button_next" value="Next" class="date_button" onClick="Q.market.showChartWithShift('-1')" disabled />
                    <%-- try to list all the dates to here --%>
                    
                    <select id="dateList" name="dateList" onchange="Q.market.showChartFromDD()">
                    <s:iterator var="dateStr" value="#dateList" status="rowStatus">
                        <s:if test="!#rowStatus.last">
                             <option value="<s:property value='%{#rowStatus.index}'/>"><s:property value='#dateStr'/></option>
                        </s:if>
                    </s:iterator>
                    </select>
                    &nbsp;&nbsp;&nbsp;<span style="color:#ffffef"><s:property value='%{inputVO.displayTitle}'/> <br/>
                    
                    Option*: <input type="text" id="extraOption" name="extraOption" size="5" placeholder="A, B, or C" />
                    Start Date: <input type="text" id="startDate" name="startDate" size="12" />
                    End Date: <input type="text" id="endDate" name="endDate" size="12" />  
                    <input type="button" name="button_pick" value="Load" class="date_button" onClick="Q.market.showChartForDate()" />
                    
					</span>
                </td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td colspan="2" align="center">
			<img id="market_chart" src="../drawchart?marketType=<s:property value='%{inputVO.marketType}'/>&chartType=<s:property value='%{inputVO.chartType}'/>&screenIndex=0" width="800px" >
			</td>
		</tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		</table>     	
 
</form>
</div></div>
