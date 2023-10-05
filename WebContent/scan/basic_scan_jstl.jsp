<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>


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


<c:set var="scanList" value="inputVO.scanList" />
<c:set var="scanDateList" value="inputVO.scanDateList" />
<c:set var="scanModeList" value="inputVO.scanModeList" />


<div id="overlay_cloak">
    <!--[if IE 6]><iframe src="../base/empty.html" class="cloak_iframe"></iframe><![endif]-->
</div>
<div id="overlay_content"><img id="img_200" class="border_b" width="680" height="400"></div>

<div class="contentwrapper"><div style="margin:2px;">               
                <form name="scanForm" action="../s/scan_basicscan.do"> 
		<input type="hidden" name="inputVO.mode" id="inputVO.mode" value="basic"/>		
		<input type="hidden" name="inputVO.type" id="inputVO.type" value="show"/>
		<input type="hidden" name="inputVO.sortColumn" id="inputVO.sortColumn" value="<c:out value='${inputVO.sortColumn}'/>"/>
		<input type="hidden" name="inputVO.sortOrder" id="inputVO.sortOrder" value="<c:out value='${inputVO.sortOrder}'/>"/>
		<input type="hidden" name="inputVO.scanKey" id="inputVO.scanKey" value="<c:out value='${inputVO.scanKey}'/>"/>
		<input type="hidden" name="inputVO.scanDate" id="inputVO.scanDate" value="<c:out value='${inputVO.scanDate}'/>"/>
                <input type="hidden" name="inputVO.selectType" id="inputVO.selectType" value="<c:out value='${inputVO.selectType}'/>"/>
                <input type="hidden" name="inputVO.period" id="inputVO.period" value="<c:out value='${inputVO.period}'/>"/>
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
			 
			 <c:choose>
				 <c:when test="${inputVO.selectType != null && inputVO.selectType != \"D\"}">
				 	<c:set var="numColumns" value="9" />
				 </c:when>
				 <c:otherwise>
				 	<c:set var="numColumns" value="8" />
				 </c:otherwise>
			 </c:choose>
			 
					
			<tr>
				<td colspan="<c:out value='${numColumns}'/>">&nbsp;&nbsp;<b>Scan Mode: </b>&nbsp;&nbsp;<c:out value='${inputVO.showTitle}'/></td>	
			</tr> 


	<c:if test="${inputVO.mode != \"blank\"}">
		<tr class="light_back"><td colspan="<c:out value='${numColumns}'/>">
		<table><tr><td>&nbsp;<b>Scan Date: <font color="#005588"><c:out value='${inputVO.scanDate}'/></font></b>
			&nbsp;&nbsp;
				<c:choose>
					<c:when test="${inputVO.type != \"charts\"}">
						<input type="button" name="sb" value="Charts" class="sml_button" onClick="submitRequest2('charts')" />
					</c:when>
					<c:otherwise>
						<input type="button" name="sb" value="List" class="sml_button" onClick="submitRequest2('list')" />
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${inputVO.prevScanDate == \"\"}">
						<input type="button" name="sb2" value="Prev" class="sml_button" disabled>
					</c:when>
					<c:otherwise>
						<input type="button" name="sb2" value="Prev" class="sml_button" title="<c:out value='${inputVO.prevScanDate}'/>" onClick="submitRequest3('show', '<c:out value="${inputVO.prevScanDate}"/>')" />
					</c:otherwise>
				</c:choose>
				
				<c:choose>
	                <c:when test="${inputVO.nextScanDate == \"\"}">
						<input type="button" name="sb3" value="Next" class="sml_button" disabled>
					</c:when>
					<c:otherwise>
						<input type="button" name="sb3" value="Next" class="sml_button" title="<c:out value='${inputVO.nextScanDate}'/>" onClick="submitRequest3('show', '<c:out value="${inputVO.nextScanDate}"/>')" />
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${inputVO.selectType == \"B\"}">
						<input type="button" name="sb4" value="Best" class="sml_button" title='Select Best Performers in past 30 days' disabled />
					</c:when>
					<c:otherwise>
						<input type="button" name="sb4" value="Best" class="sml_button" title='Select Best Performers in past 30 days' onClick="submitRequest4('B')" />
					</c:otherwise>
				</c:choose>
				
				<c:choose>
					<c:when test="${inputVO.selectType == \"W\"}">
						<input type="button" name="sb5" value="Worst" class="sml_button" title='Select Worst Performers in past 30 days' disabled />
					</c:when>
					<c:otherwise>
						<input type="button" name="sb5" value="Worst" class="sml_button" title='Select Worst Performers in past 30 days' onClick="submitRequest4('W')" />
					</c:otherwise>
				</c:choose>
				
				<%-- default value is 30 --%>
				<c:if test="${inputVO.period == \"10\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" checked >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')" >120
                </c:if>
                
                <c:if test="${inputVO.period == \"60\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" checked >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')" >120
                </c:if>
                
                <c:if test="${inputVO.period == \"120\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')" checked >120
                </c:if>
                                
                <%-- else set to 30 --%>               
				<c:if test="${inputVO.period != \"10\" && inputVO.period != \"60\" && inputVO.period != \"120\"}">
                    <input type="radio" name="ckPeriod" value="10" onClick="setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="setPeriod('30')" checked >30
                    <input type="radio" name="ckPeriod" value="60" onClick="setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="setPeriod('120')">120
                </c:if>
                              
			
			</td>
		</tr>
		
		
		<c:set var="scanDate" value="${inputVO.scanDate}" />
		<c:if test="${scanDate != NULL && scanDate != \"\"}">
			<tr><td><table style="cellpadding: 0px; cellspacing: 0px;"><tr><td>
				<%--  request.setAttribute("scandate_list", <c:out value='#scanDateList'/> ); --%>
				<%-- push the object got from action to request, like: reqeust.setAttribute() --%>
				<c:set var="scandate_list" value="${inputVO.scanDateList}" scope="request"></c:set>
				
					<jsp:include page="scandate_select.jsp" flush="true" >
                            <jsp:param name="form_name" value="scanForm" />
					</jsp:include>
			</td><td>
			    <c:forEach items="${inputVO.scanDateList}" var="dateItem" varStatus="rowStatus"  begin="0" end="34">
					<c:set var="dt" value="${dateItem.substring(3, 5)}" />
					<c:if test="${dataItem != scanDate}">
						<input type="button" name="sb" value="<c:out value='${dt}'/>" title='<c:out value="${dateItem}"/>' onClick="submitRequest3('show', '<c:out value="${dateItem}"/>')">
					</c:if>
					<c:if test="${dataItem == scanDate}">
						<input type="button" name="sb" value="<c:out value='${dt}'/>" disabled>
					</c:if>
				</c:forEach>
			
			</tr></table></td></tr>
		</c:if>
		
		
		
		
		
		</table>
		</td>
		</tr>
		
		<tr class="gray_back"><td align="center" class="my_border">Order</td>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<c:out value='${inputVO.type}'/>&inputVO.scanKey=<c:out value='${inputVO.scanKey}'/>&inputVO.selectType=<c:out value='${inputVO.selectType}'/>&inputVO.scanDate=<c:out value='${inputVO.scanDate}'/>&inputVO.sortColumn=t&inputVO.sortOrder=<c:out value='${inputVO.nextSortOrder}'/>" class="datalink">Ticker</a></td>
			<td align="center" class="my_border">Charts</td>
                        
                        <c:if test="${numColumns == \"9\"}">
                            <td align="center" class="my_border">
                            <a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<c:out value='${inputVO.type}'/>&inputVO.scanKey=<c:out value='${inputVO.scanKey}'/>&inputVO.selectType=<c:out value='${inputVO.selectType}'/>&inputVO.sortColumn=sd&inputVO.sortOrder=<c:out value='${inputVO.nextSortOrder}'/>" class="datalink">Date</a></td>
			
                        </c:if>

			<td align="center" class="my_border">Trade</td>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<c:out value='${inputVO.type}'/>&inputVO.scanKey=<c:out value='${inputVO.scanKey}'/>&inputVO.selectType=<c:out value='${inputVO.selectType}'/>&inputVO.scanDate=<c:out value='${inputVO.scanDate}'/>&inputVO.sortColumn=c&inputVO.sortOrder=<c:out value='${inputVO.nextSortOrder}'/>" class="datalink">Company</a></td>
			
			<%--td class="my_border">Comments</td--%>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<c:out value='${inputVO.type}'/>&inputVO.scanKey=<c:out value='${inputVO.scanKey}'/>&inputVO.selectType=<c:out value='${inputVO.selectType}'/>&inputVO.scanDate=<c:out value='${inputVO.scanDate}'/>&inputVO.sortColumn=sp&inputVO.sortOrder=<c:out value='${inputVO.nextSortOrder}'/>" class="datalink">Scan Price</a></td>
			
			
			<%--td class="my_border">Close Date</td><td>Close Price</td--%>
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<c:out value='${inputVO.type}'/>&inputVO.scanKey=<c:out value='${inputVO.scanKey}'/>&inputVO.selectType=<c:out value='${inputVO.selectType}'/>&inputVO.scanDate=<c:out value='${inputVO.scanDate}'/>&inputVO.sortColumn=cp&inputVO.sortOrder=<c:out value='${inputVO.nextSortOrder}'/>" class="datalink">Current Price</a></td>
			
			
			<td align="center" class="my_border"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<c:out value='${inputVO.type}'/>&inputVO.scanKey=<c:out value='${inputVO.scanKey}'/>&inputVO.selectType=<c:out value='${inputVO.selectType}'/>&inputVO.scanDate=<c:out value='${inputVO.scanDate}'/>&inputVO.sortColumn=g&inputVO.sortOrder=<c:out value='${inputVO.nextSortOrder}'/>" class="datalink">Gain (%)</a></td>
			
		
		</tr>
			
		    
		    <c:forEach items="${inputVO.scanList}" var="itemVO" varStatus="rowStatus">
		    	<c:choose>
			    	<c:when test="${rowStatus.index % 2 == 0}">
			    		<c:set var="rowColor" value="#ffffdf" />
			    	</c:when>
			    	<c:otherwise>
			    		<c:set var="rowColor" value="#ffffff" />
			    	</c:otherwise>
		    	</c:choose>
		    	
				<c:set var="label" value="${rowStatus.index + 1}" />
				<%-- c:set var="label" value="%{String.valueOf(#rowStatus.index + 1)}" />
				<c:if test="%{#label.length < 2}">
					<c:set var="label" value="%{\"0\" + #label}" />
				</c:if --%>
			
			 
			<TR bgcolor="<c:out value='${rowColor}'/>">
			<TD align="center" class="my_border"><c:out value='${label}'/></TD>
			
			<TD align="center" class="my_border">
                            <A href="../a/analyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.ticker=<c:out value='${itemVO.ticker}'/>&inputVO.period=6&inputVO.stockId=<c:out value='${itemVO.stockId}'/>" class="datalink" target="new"><c:out value='${itemVO.ticker}'/></A>
				[ <A href="../m/watch.do?inputVO.mode=addwatch&inputVO.type=blank&inputVO.stockId=<c:out value='${itemVO.stockId}'/>" class="datalink">Add</a> ]
			</td>

                        <td align="center" class="my_border"><input type="button" name="a" value="3m"
					onClick='show_image("../drawchart?stockId=<c:out value='${itemVO.stockId}'/>&period=3&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'><input type="button" name="a" value="6m"
					onClick='show_image("../drawchart?stockId=<c:out value='${itemVO.stockId}'/>&period=6&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'><input type="button" name="a" value="12m"
					onClick='show_image("../drawchart?stockId=<c:out value='${itemVO.stockId}'/>&period=12&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'><input type="button" name="a" value="24m"
					onClick='show_image("../drawchart?stockId=<c:out value='${itemVO.stockId}'/>&period=24&option=NNNY","overlay_content","overlay_cloak")'
					onBlur='hide_image("overlay_content","overlay_cloak")'></td>
					
					<c:if test="${numColumns == \"9\"}">
                    	<td align="center" class="my_border"><c:out value='${itemVO.scanDate}'/></td>
                    </c:if>

					<td align="center" class="my_border"><c:out value='${itemVO.longShort}'/></td>

		    <%-- escape="false" will let the entities to display --%>
		    <td class="my_border"><c:out value='${itemVO.companyName}'/></td>
		    <%--<td><bean:write name="itemVO" property="comments"/>&nbsp;</td> --%>
			<td align="center" class="my_border"><c:out value='${itemVO.scanPrice}'/></td>
			
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
		    
		    <td align="center" class="my_border"><c:out value='${itemVO.currentPrice}'/></td>
		    <c:choose>
			    <c:when test="${itemVO.gain < 0}" >
			    	<td align="center" class="my_border"><font color="#ff0000"><b><c:out value='${itemVO.gain}'/></b></font></td>
			    </c:when>
			    <c:otherwise>
			    	<td align="center" class="my_border"><font color="#009955"><b><c:out value='${itemVO.gain}'/></b></font></td>
			     </c:otherwise>
		     </c:choose>
	     
			</TR>

			<c:if test="${inputVO.type != \"list\"}" >
			<tr><td align="center" colspan="<c:out value='${numColumns}'/>" class="my_border"><img src="../drawchart?stockId=<c:out value='${itemVO.stockId}'/>&period=12&option=NNNY"></td></tr>
			</c:if>
			</c:forEach> 
			
			<c:if test="${inputVO.scanList.size() == 0}" >
				<TR>
					<TD colspan="<c:out value='${numColumns}'/>" align="center" class="my_border">&nbsp;
						<FONT color="red">No Items found for the searched criteria.</FONT>
					</TD>
				</TR>
			</c:if> 
			
	</c:if>
	</table>
		 </form>




	<%
		String marketSkill = com.greenfield.ui.cache.MarketCachePool.sgetMarketIndicators().getMarketSkill();
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
