<%@ taglib prefix="s" uri="/struts-tags" %>


<%-- jQuery dialog is setting focus automatically:

1 The first element within the dialog with the autofocus attribute
2 The first :tabbable element within the dialog's content
3 The first :tabbable element within the dialog's buttonpane
4 The dialog's close button
5 The dialog itself

To disable the feature: put the following into first in dialog.
<span class=\"ui-helper-hidden-accessible\"><input type=\"text\"/></span>
 --%>
<script type="text/javascript">

$( document ).ready(function() {
/* D, B, W - by date, best, worst 
 * scanKey, selectType, period, scanDate
 */
 
	// Q.scan.populateBasicScan("XU", "B", "6", "03/12/2014");
	Q.scan.loadScanList();
	
	QQ_basicscan.addDateButtons();

	QQ_basicscan.adjustButtons();
	
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
<%--s:set var="scanDateList" value="inputVO.scanDateList" /--%>
<s:set var="scanModeList" value="inputVO.scanModeList" />


<%-- overlaycontent will be hidden, and show after some click --%>
<div id="overlaycontent"  title="The image ... Click outside of the Dialog (or ESC) to close." >This is in the overly</div>

<div id="overlaypage" title="Manage watch list.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper"><div style="margin:2px;">               
                <form name="scanForm" action="../s/scan_basicscan.do"> 
		<input type="hidden" name="inputVO.mode" id="inputVO.mode" value="basic"/>		
		<input type="hidden" name="inputVO.type" id="inputVO.type" value="<s:property value='%{inputVO.type}'/>"/>
		<input type="hidden" name="inputVO.sortColumn" id="inputVO.sortColumn" value="<s:property value='%{inputVO.sortColumn}'/>"/>
		<input type="hidden" name="inputVO.sortOrder" id="inputVO.sortOrder" value="<s:property value='%{inputVO.sortOrder}'/>"/>
		<input type="hidden" name="inputVO.scanKey" id="inputVO.scanKey" value="<s:property value='%{inputVO.scanKey}'/>"/>
		<input type="hidden" name="inputVO.scanDate" id="inputVO.scanDate" value="<s:property value='%{inputVO.scanDate}'/>"/>
                <input type="hidden" name="inputVO.selectType" id="inputVO.selectType" value="<s:property value='%{inputVO.selectType}'/>"/>
                <input type="hidden" name="inputVO.period" id="inputVO.period" value="<s:property value='%{inputVO.period}'/>"/>
         
         <%-- pass scan dates to client, as soon as it is used, null it. --%>
         <input type="hidden" name="dateListStr" id="dateListStr" value="<s:property value='%{inputVO.scanDateListStr}'/>"/>          
                
		<table width="100%" class="gridtable">
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
				<td colspan="<s:property value='#numColumns'/>">&nbsp;&nbsp;<b>Scan Mode: </b>&nbsp;&nbsp;<font class="sub_title"><s:property value='inputVO.showTitle'/></font></td>	
			</tr> 


	<s:if test="%{inputVO.mode != \"blank\"}">
		<tr class="light_back"><td colspan="<s:property value='#numColumns'/>">
		<table class="gridtable_noborder"><tr><td>&nbsp;<b>Scan Date: <font color="#005588"><span id="displayDate"></span></font></b>
			&nbsp;&nbsp;
				

			   <input type="button" id="typeButton" name="sb1" value="List" class="sml_button" onClick="Q.scan.submitRequest2()" />

			   <input type="button" id="prevButton" name="sb2" value="Prev" class="sml_button" title="<s:property value='inputVO.prevScanDate'/>" onClick="Q.scan.submitRequest3('prev')" />

			   <input type="button" id="nextButton" name="sb3" value="Next" class="sml_button" title="<s:property value='inputVO.nextScanDate'/>" onClick="Q.scan.submitRequest3('next')" />

			   <input type="button" id="bestButton" name="sb4" value="Best" class="sml_button" title='Select Best Performers in past 30 days' onClick="Q.scan.submitRequest4('B')" />

			   <input type="button" id="worstButton" name="sb5" value="Worst" class="sml_button" title='Select Worst Performers in past 30 days' onClick="Q.scan.submitRequest4('W')" />

				
				<%-- default value is 30 --%>
				<s:if test="%{inputVO.period == \"10\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="Q.scan.setPeriod('10')" checked >10
                    <input type="radio" name="ckPeriod" value="30" onClick="Q.scan.setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="Q.scan.setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="Q.scan.setPeriod('120')" >120
                    <input type="radio" name="ckPeriod" value="180" onClick="Q.scan.setPeriod('180')" >180
                </s:if>
                
                <s:if test="%{inputVO.period == \"60\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="Q.scan.setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="Q.scan.setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="Q.scan.setPeriod('60')" checked >60
                    <input type="radio" name="ckPeriod" value="120" onClick="Q.scan.setPeriod('120')" >120
                    <input type="radio" name="ckPeriod" value="180" onClick="Q.scan.setPeriod('180')" >180
                </s:if>
                
                <s:if test="%{inputVO.period == \"120\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="Q.scan.setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="Q.scan.setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="Q.scan.setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="Q.scan.setPeriod('120')" checked >120
                    <input type="radio" name="ckPeriod" value="180" onClick="Q.scan.setPeriod('180')" >180
                </s:if>
                
                <s:if test="%{inputVO.period == \"180\"}">
					<input type="radio" name="ckPeriod" value="10" onClick="Q.scan.setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="Q.scan.setPeriod('30')" >30
                    <input type="radio" name="ckPeriod" value="60" onClick="Q.scan.setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="Q.scan.setPeriod('120')" >120
                    <input type="radio" name="ckPeriod" value="180" onClick="Q.scan.setPeriod('180')" checked >180
                </s:if>
                                
                <%-- else set to 30 --%>               
				<s:if test="%{inputVO.period != \"10\" && inputVO.period != \"60\" && inputVO.period != \"120\" && inputVO.period != \"180\"}">
                    <input type="radio" name="ckPeriod" value="10" onClick="Q.scan.setPeriod('10')" >10
                    <input type="radio" name="ckPeriod" value="30" onClick="Q.scan.setPeriod('30')" checked >30
                    <input type="radio" name="ckPeriod" value="60" onClick="Q.scan.setPeriod('60')" >60
                    <input type="radio" name="ckPeriod" value="120" onClick="Q.scan.setPeriod('120')" >120
                    <input type="radio" name="ckPeriod" value="180" onClick="Q.scan.setPeriod('180')" >180
                </s:if>
                              
			
			</td>
		</tr>
		
		
		<s:set var="scanDate" value="%{inputVO.scanDate}" />
		<%-- s:if test="%{#scanDate != NULL && #scanDate != \"\"}"> --%>
			<tr><td style="padding:0px;"><table class="gridtable_noborder"><tr><td style="padding:0px;">
				<%--  request.setAttribute("scandate_list", <s:property value='#scanDateList'/> ); --%>
				<%-- push the object got from action to request, like: reqeust.setAttribute() --%>
				<%-- <s:set var="scandate_list" value="#scanDateList" scope="request"></s:set> --%>
				
					<jsp:include page="scandate_select.jsp" flush="true" >
                            <jsp:param name="form_name" value="scanForm" />
					</jsp:include>
			</td><td style="padding:0px;"><div id="dateButtons"></div>
			
			<%-- // move this to client for performance on server.  The iterator() is terrible slow.
				<s:iterator var="dateItem" value="#scanDateList" status="rowStatus" begin="0" end="34">
					<s:set var="dt" value="%{#dateItem.substring(3, 5)}" />
					<s:if test="%{'#dataItem' != '#scanDate'}">
						<input type="button" name="sb" value="<s:property value='#dt'/>" title='<s:property value="#dateItem"/>' onClick="Q.scan.submitRequest3('<s:property value="#dateItem"/>')">
					</s:if>
					<s:else>
						<input type="button" name="sb" value="<s:property value='#dt'/>" disabled>
					</s:else>
				</s:iterator>--%>
				</td>
			
			</tr></table></td></tr> 
		<%-- /s:if>--%>
		
	
		
		</table>
		</td>
		</tr>
		
		
		<%-- 
		<tr class="gray_back"><td align="center">Order</td>
			<td align="center"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=t&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Ticker</a></td>
			<td align="center">Charts</td>
                        
                        <s:if test="%{#numColumns == \"9\"}">
                            <td align="center">
                            <a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.sortColumn=sd&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Date</a></td>
			
                        </s:if>

			<td align="center">Trade</td>
			<td align="center"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=c&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Company</a></td>
			
			<td align="center"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=sp&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Scan Price</a></td>
			
			
			<td align="center"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=cp&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Current Price</a></td>
			
			
			<td align="center"><a href="../s/scan_basicscan.do?inputVO.mode=basic&inputVO.type=<s:property value='inputVO.type'/>&inputVO.scanKey=<s:property value='inputVO.scanKey'/>&inputVO.selectType=<s:property value='inputVO.selectType'/>&inputVO.scanDate=<s:property value='inputVO.scanDate'/>&inputVO.sortColumn=g&inputVO.sortOrder=<s:property value='inputVO.nextSortOrder'/>" class="datalink">Gain (%)</a></td>
			
		
		</tr>--%>
		
		<tr class="light_back"><td colspan="<s:property value='#numColumns'/>" style="padding:0px;">
		<%-- <div id="displayNode">Something here need to be replaced.</div> --%>
		<table id="displayNode" style="width:100%; border-width: 0px;"  class="gridtable">
		
		</table>
		</td></tr>	
		    

			
	</s:if>
	</table>
		 </form>



	<%
		String marketSkill = com.greenfield.ui.cache.MarketCachePool.getMarketIndicators().getMarketSkill();
	%>
        <table>
	<tr><td colspan="2">&nbsp;<font color="#005533"> Please notice our suggestion for current market:</font> <b><font color="#ff0000"><%= marketSkill %>.</font></b>
	<br>&nbsp; <font color="#005533">So only look for proper group of search results to get the best performance.</font>
		</td></tr>

	
	</table>




            <form name="goStockAgeForm" action="../s/agesrch_agesrchbasic.do">
                <input type="hidden" name="inputVO.mode" value="search"/>
                <input type="hidden" name="inputVO.selectRange" value=""/>
            </form>
              
</div></div>


<%-- tempalte for jsrender.min.js to use, "{{>" - html encoded and "{{:" - not encoded, but if string, it works the same; == and === are different (?) --%>
<script id="scanTitleTemplate" type="text/html">

		<tr style="background-color:#ff0"><th align="center">Order</th>
 			{{if colspan === "9"}}
               <th align="center"><a href="#" onClick="Javascript:Q.scan.submitRequest5('sd')" class="datalink">Date</a></th>
            {{/if}}
			<th align="center"><a href="#" onClick="Javascript:Q.scan.submitRequest5('t')" class="datalink">Ticker</a></th>
			<th align="center">Charts</th>
			<th align="center">Trade</th>
			<th align="center"><a href="#" onClick="Javascript:Q.scan.submitRequest5('c')" class="datalink">Company</a></th>
			<th align="center"><a href="#" onClick="Javascript:Q.scan.submitRequest5('sp')" class="datalink">Scan Price</a></th>
			<th align="center"><a href="#" onClick="Javascript:Q.scan.submitRequest5('cp')" class="datalink">Current Price</a></th>
			<th align="center"><a href="#" onClick="Javascript:Q.scan.submitRequest5('g')" class="datalink">Gain (%)</a></th>	
		</tr>


</script>
<script id="scanListTemplate" type="text/html">
	
			<tr>
				<td align="center" style="background-color:{{: rowColor }}">{{:#getIndex() + 1}}</td>
				{{if colspan === "9"}}
					<td align="center" style="background-color:{{: rowColor }}">{{: scanDate }}</td>
				{{/if}}
				
				<td align="left" style="background-color:{{: rowColor }}">{{: ticker }}</td>
				
				<td align="center" style="background-color:{{: rowColor }}">
		
					<input type="button" name="a" value="3m"
					onClick='QQ_chartoverlay.show_image("../drawchart?stockId={{: stockId}}&period=3&option=NNNY")'
					>
					<input type="button" name="a" value="6m"
					onClick='QQ_chartoverlay.show_image("../drawchart?stockId={{: stockId}}&period=6&option=NNNY")'
					>
					<input type="button" name="a" value="12m"
					onClick='QQ_chartoverlay.show_image("../drawchart?stockId={{: stockId}}&period=12&option=NNNY")'
					>
					<input type="button" name="a" value="24m"
					onClick='QQ_chartoverlay.show_image("../drawchart?stockId={{: stockId}}&period=24&option=NNNY")'
					>

                    <input type="button" name="b" value="Watch"
				 	onclick="QQ_addwatch.getInitData('{{: stockId}}', 'Edit');" 
					>
                                    
				</td>
				<td align="center" style="background-color:{{: rowColor }}">{{: longShort }}</td>
				<td align="left" style="background-color:{{: rowColor }}">{{: companyName }}</td>
				<td align="center" style="background-color:{{: rowColor }}">{{: scanPrice }}</td>
				<td align="center" style="background-color:{{: rowColor }}">{{: currentPrice }}</td>
				<td align="center" style="background-color:{{: rowColor }}"><font color="{{: gainColor }}"><b>{{: gain }}</b></font></td>
			</tr>
			{{if showChart === "charts"}}
			<tr>
				<td colspan="{{: colspan }}" align="center" style="background:#eaeaea;" >
<img src="../drawchart?stockId={{: stockId }}&period=12&option=NNNY"></td></tr>
			</td>
			</tr>
			{{/if}}

		
</script>
<jsp:include page="../template/watchlist/addwatch_template.jsr"></jsp:include>