<%@ taglib prefix="s" uri="/struts-tags" %>



<script type="text/javascript" >
// Init section 
	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	//run_after_body();
</script>
<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

.big_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; 
	FONT-SIZE: 9pt;
	COLOR: #0000cf; 
	TEXT-DECORATION: none; 
	WIDTH: 130px;
}

.sml_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; 
	FONT-SIZE: 9pt; 
	COLOR: #0000cf; 
	TEXT-DECORATION: none; 
	WIDTH: 80px;
}

.dropdown {
   FONT-SIZE: 12pt; 
   width: 400px;
   COLOR: #005588; 
   background-color: #fffff0;
   TEXT-DECORATION: none; 
   FONT-WEIGHT: bold;
}

</style>


<s:set var="scanList" value="inputVO.scanList" />
<s:set var="scanDateList" value="inputVO.scanDateList" />
<s:set var="scanModeList" value="inputVO.scanModeList" />


<div id="overlay_cloak">
    <!--[if IE 6]><iframe src="../base/empty.html" class="cloak_iframe"></iframe><![endif]-->
</div>
<div id="overlay_content"><img id="img_200" class="border_b" width="680" height="400"></div>

<div class="contentwrapper"><div style="margin:2px;">               
                <form name="scanForm" action="../s/scan_basicscan.do"> 
		<input type="hidden" name="inputVO.mode" id="inputVO.mode" value="basic"/>		
		<input type="hidden" name="inputVO.type" id="inputVO.type" value="show"/>
		<input type="hidden" name="inputVO.sortColumn" id="inputVO.sortColumn" value="<s:property value='%{inputVO.sortColumn}'/>"/>
		<input type="hidden" name="inputVO.sortOrder" id="inputVO.sortOrder" value="<s:property value='%{inputVO.sortOrder}'/>"/>
		<input type="hidden" name="inputVO.scanKey" id="inputVO.scanKey" value="<s:property value='%{inputVO.scanKey}'/>"/>
		<input type="hidden" name="inputVO.scanDate" id="inputVO.scanDate" value="<s:property value='%{inputVO.scanDate}'/>"/>
                <input type="hidden" name="inputVO.selectType" id="inputVO.selectType" value="<s:property value='%{inputVO.selectType}'/>"/>
                <input type="hidden" name="inputVO.period" id="inputVO.period" value="<s:property value='%{inputVO.period}'/>"/>
		<table border="1" width="100%" class="light_back">
			<%-- if (AppContext.getMarketTrendColor().equals("G")) { 
			<tr>
				<td align="center" colspan="8"><input name="sb1" type="button" value="Momentum Up"   onclick="submitRequest('MU')" 
				class="big_button"/><input name="sb2" type="button" value="Momentum Down" onclick="submitRequest('MD')" 
				class="big_button" /><input name="sb5" type="button" value="Progress Up"   onclick="submitRequest('PU')" 
				class="big_button"/><input name="sb6" type="button" value="Progress Down" onclick="submitRequest('PD')" 
				class="big_button" /><input name="sb7" type="button" value="Linear Up"   onclick="submitRequest('LU')" 
				class="big_button" /><input name="sb8" type="button" value="Linear Down" onclick="submitRequest('LD')" 
				class="big_button" /></td>	
			</tr> 
			--%>
			 
			 <s:if test="%{inputVO.selectType != NULL && inputVO.selectType != \"D\"}">
			 	<s:set var="numColumns" value="9" />
			 </s:if>
			 <s:else>
			 	<s:set var="numColumns" value="8" />
			 </s:else>
			 
					
			<tr>
				<td colspan="<s:property value='#numColumns'/>">&nbsp;&nbsp;<b>Scan Mode: </b>&nbsp;&nbsp;<s:property value='inputVO.showTitle'/></td>	
			</tr> 


	<s:if test="%{inputVO.mode != \"blank\"}">
		<tr class="light_back"><td colspan="<s:property value='#numColumns'/>">
		<table><tr><td>&nbsp;<b>Scan Date: <font color="#005588"><s:property value='inputVO.scanDate'/></font></b>
			&nbsp;&nbsp;
				
				<s:if test="%{inputVO.type != \"charts\"}">
					<input type="button" name="sb" value="Charts" class="sml_button" onClick="submitRequest2('charts')" />
				</s:if>
				<s:else>
					<input type="button" name="sb" value="List" class="sml_button" onClick="submitRequest2('list')" />
				</s:else>
				
				<s:if test="%{inputVO.prevScanDate == \"\"}">
					<input type="button" name="sb2" value="Prev" class="sml_button" disabled>
				</s:if>
				<s:else>
					<input type="button" name="sb2" value="Prev" class="sml_button" title="<s:property value='inputVO.prevScanDate'/>" onClick="submitRequest3('show', '<s:property value="inputVO.prevScanDate"/>')" />
				</s:else>

                <s:if test="%{inputVO.nextScanDate == \"\"}">
					<input type="button" name="sb3" value="Next" class="sml_button" disabled>
				</s:if>
				<s:else>
					<input type="button" name="sb3" value="Next" class="sml_button" title="<s:property value='inputVO.nextScanDate'/>" onClick="submitRequest3('show', '<s:property value="inputVO.nextScanDate"/>')" />
				</s:else>

				<s:if test="%{inputVO.selectType == \"B\"}">
					<input type="button" name="sb4" value="Best" class="sml_button" title='Select Best Performers in past 30 days' disabled />
				</s:if>
				<s:else>
					<input type="button" name="sb4" value="Best" class="sml_button" title='Select Best Performers in past 30 days' onClick="submitRequest4('B')" />
				</s:else>
				
				<s:if test="%{inputVO.selectType == \"W\"}">
					<input type="button" name="sb5" value="Worst" class="sml_button" title='Select Worst Performers in past 30 days' disabled />
				</s:if>
				<s:else>
					<input type="button" name="sb5" value="Worst" class="sml_button" title='Select Worst Performers in past 30 days' onClick="submitRequest4('W')" />
				</s:else>
				
				<%-- default value is 30 --%>
				<s:if test="%{inputVO.period == \"10\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" checked >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')" >120
                </s:if>
                
                <s:if test="%{inputVO.period == \"60\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" checked >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')" >120
                </s:if>
                
                <s:if test="%{inputVO.period == \"120\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')" checked >120
                </s:if>
                                
                <%-- else set to 30 --%>               
				<s:if test="%{inputVO.period != \"10\" && inputVO.period != \"60\" && inputVO.period != \"120\"}">
                    <input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" checked >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')">120
                </s:if>
                              
			
			</td>
		</tr>
		
		
		<s:set var="scanDate" value="%{inputVO.scanDate}" />
		<s:if test="%{#scanDate != NULL && #scanDate != \"\"}">
			<tr><td><table style="cellpadding: 0px; cellspacing: 0px;"><tr><td>
				<%--  request.setAttribute("scandate_list", <s:property value='#scanDateList'/> ); --%>
				<%-- push the object got from action to request, like: reqeust.setAttribute() --%>
				<s:set var="scandate_list" value="#scanDateList" scope="request"></s:set>
				
					<jsp:include page="scandate_select.jsp" flush="true" >
                            <jsp:param name="form_name" value="scanForm" />
					</jsp:include>
			</td><td>
				<s:iterator var="dateItem" value="#scanDateList" status="rowStatus" begin="0" end="34">
					<s:set var="dt" value="%{#dateItem.substring(3, 5)}" />
					<s:if test="%{#rowStatus.index <= 34 && #dataItem != #scanDate}">
						<input type="button" name="sb" value="<s:property value='#dt'/>" title='<s:property value="#dateItem"/>' onClick="submitRequest3('show', '<s:property value="#dateItem"/>')">
					</s:if>
					<s:if test="%{#rowStatus.index <= 34 && #dataItem == #scanDate}">
						<input type="button" name="sb" value="<s:property value='#dt'/>" disabled>
					</s:if>
				</s:iterator>
			
			</tr></table></td></tr>
		</s:if>
		
		
		
		
		
		</table>
		</td>
		</tr>
		
		<tr class="gray_back"><td align="center" class="my_border">Order</td>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=t&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Ticker</a></td>
			<td align="center" class="my_border">Charts</td>
                        
                        <s:if test="%{#numColumns == \"9\"}">
                            <td align="center" class="my_border">
                            <a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.sortColumn=sd&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Date</a></td>
			
                        </s:if>

			<td align="center" class="my_border">Trade</td>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=c&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Company</a></td>
			
			<%--td class="my_border">Comments</td--%>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=sp&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Scan Price</a></td>
			
			
			<%--td class="my_border">Close Date</td><td>Close Price</td--%>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=cp&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Current Price</a></td>
			
			
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=g&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Gain (%)</a></td>
			
		
		</tr>
			
		    
		    <s:iterator value="#scanList" var="itemVO" status="rowStatus">
		    	<s:if test="%{#rowStatus.even == true}">
		    		<s:set var="rowColor" value="#ffffdf" />
		    	</s:if>
		    	<s:else>
		    		<s:set var="rowColor" value="#ffffff" />
		    	</s:else>
		    	
				<s:set var="label" value="%{#rowStatus.index + 1}" />
				<%-- s:set var="label" value="%{String.valueOf(#rowStatus.index + 1)}" />
				<s:if test="%{#label.length < 2}">
					<s:set var="label" value="%{\"0\" + #label}" />
				</s:if --%>
			
			 
			<TR bgcolor="<s:property value='#rowColor'/>">
			<TD align="center" class="my_border"><s:property value='#label'/></TD>
			
			<TD align="center" class="my_border">
                            <A href="../a/analyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.ticker=<s:property value='ticker'/>&inputVO.period=6&inputVO.stockId=<s:property value='stockId'/>" class="datalink" target="new"><s:property value='ticker'/></A>
				[ <A href="../m/watch.do?inputVO.mode=addwatch&inputVO.type=blank&inputVO.stockId=<s:property value='stockId'/>" class="datalink">Add</a> ]
			</td>

                        <td align="center" class="my_border"><input type="button" name="a" value="3m"
					onClick='show_image("../drawchart?stockId=<s:property value='stockId'/>&period=3&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'><input type="button" name="a" value="6m"
					onClick='show_image("../drawchart?stockId=<s:property value='stockId'/>&period=6&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'><input type="button" name="a" value="12m"
					onClick='show_image("../drawchart?stockId=<s:property value='stockId'/>&period=12&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'><input type="button" name="a" value="24m"
					onClick='show_image("../drawchart?stockId=<s:property value='stockId'/>&period=24&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'></td>
					
					<s:if test="%{#numColumns == \"9\"}">
                    	<td align="center" class="my_border"><s:property value='scanDate'/></td>
                    </s:if>

					<td align="center" class="my_border"><s:property value='longShort'/></td>

		    <%-- escape="false" will let the entities to display --%>
		    <td class="my_border"><s:property value='companyName' escape='false'/></td>
		    <%--<td><bean:write name="itemVO" property="comments"/>&nbsp;</td> --%>
			<td align="center" class="my_border"><s:property value='scanPrice'/></td>
			
			<%--  
			<logic:equal name="itemVO" property="closeDate" value="">		    
		    <td>&nbsp;</td>
		    <td>&nbsp;</td>
		    </logic:equal>
		    <logic:notEqual name="itemVO" property="closeDate" value="">		    
		    <td align="center"><bean:write name="itemVO" property="closeDate"/></td>
		    <td align="center"><bean:write name="itemVO" property="closePrice"/></td>
		    </logic:notEqual>
		    --%>
		    
		    <td align="center" class="my_border"><s:property value='currentPrice'/></td>
		    
		    <s:if test="%{#itemVO.gain < 0}" >
		    	<td align="center" class="my_border"><font color="#ff0000"><b><s:property value='gain'/></b></font></td>
		    </s:if>
		    <s:else>
		    	<td align="center" class="my_border"><font color="#009955"><b><s:property value='gain'/></b></font></td>
		     </s:else>
	     
			</TR>

			<s:if test="%{inputVO.type != \"list\"}" >
			<tr><td align="center" colspan="<s:property value='numColumns'/>" class="my_border"><img src="../drawchart?stockId=<s:property value='stockId'/>&period=12&option=NNNY"></td></tr>
			</s:if>
			</s:iterator> 
			
			<s:if test="%{#scanList.size == 0}" >
				<TR>
					<TD colspan="<s:property value='numColumns'/>" align="center" class="my_border">&nbsp;
						<FONT color="red">No Items found for the searched criteria.</FONT>
					</TD>
				</TR>
			</s:if>
			
	</s:if>
	</table>
		 </form>



	<%
		String marketSkill = com.greenfield.ui.cache.MarketCachePool.getMarketIndicators().getMarketSkill();
	%>
        <table>
	<tr><td colspan="2">&nbsp;<font color="#ffffef"> Please notice our suggestion for current market:</font> <b><font color="#ff0000"><%= marketSkill %>.</font></b>
	<br>&nbsp; <font color="#ffffef">So only look for proper group of search results to get the best performance.</font>
		</td></tr>

	
	</table>




            <form name="goStockAgeForm" action="../s/agesrch_agesrchbasic.do">
                <input type="hidden" name="inputVO.mode" value="search"/>
                <input type="hidden" name="inputVO.selectRange" value=""/>
            </form>
              
</div></div>
