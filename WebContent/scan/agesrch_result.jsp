<%@ taglib prefix="s" uri="/struts-tags" %>



<script type="text/javascript" language="JavaScript">

	window.onerror = function() { return true; }
	
	$( document ).ready(function() {

			Q.agesrch.loadAgeList();
			
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

<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

.big_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
	FONT-SIZE: 9pt;
	COLOR: #0000cf;
	TEXT-DECORATION: none;
	WIDTH: 133px;
}

.sml_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
	FONT-SIZE: 9pt;
	COLOR: #0000cf;
	TEXT-DECORATION: none;
	WIDTH: 100px;
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


<s:set var="sumList" value="inputVO.ageSummaryList" />
<%-- s:set var="ageList" value="inputVO.stockAgeList" /> --%>


<%-- overlaycontent will be hidden, and show after some click --%>
<div id="overlaycontent"  title="The image ... Click outside of the Dialog (or ESC) to close." >This is in the overly</div>

<div id="overlaypage" title="Manage watch list.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper" style="min-height:200px;"><div style="margin:2px;"> 
		
      <form name="ageSrchForm" action="../s/agesrch_argsrchresult.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="<s:property value='%{inputVO.mode}'/>"/>		
		<input type="hidden" id="inputVO.type" name="inputVO.type" value="<s:property value='%{inputVO.type}'/>"/>
		<input type="hidden" id="inputVO.selectRange" name="inputVO.selectRange" value="<s:property value='%{inputVO.selectRange}'/>"/>
		
		<%-- do not show charts in the first time --%>
		<input type="hidden" id="inputVO.showAllCharts" name="inputVO.showAllCharts" value="N"/>
		
                    <table width="100%" class="gridtable">
			
                <tr><td colspan="11"><font color="#000000"><b>Age Range(Number Stocks):</b></font></td></tr>
                <s:iterator var="itemVO" value="#sumList" status="rowStatus">
                	<s:if test="%{#rowStatus.index % 11 == 0}">
                	<tr>
                	</s:if>
	                	<s:if test="%{#itemVO.ageRange != \"BEST\"}">
							<td><a href="#" onclick="Q.agesrch.loadAgeList('<s:property value="%{#itemVO.ageRange}"/>')" class="datalink"><s:property value='%{#itemVO.ageRange}'/>0(<s:property value='%{#itemVO.numEntries}'/>)</a></td>
						</s:if>
						<s:else>
							<td><a href="#" onclick="Q.agesrch.loadAgeList('BEST')" class="datalink">BEST(<s:property value='%{#itemVO.numEntries}'/>)</a></td>
						</s:else>
					
                    <s:if test="%{#rowStatus.index % 11 == 10}">
                	</tr>
                	</s:if>
				
				</s:iterator>
                </table>
                
			<table id="displayNode" style="width:100%;" class="gridtable">
		
			</table>
			

	</form>
       
</div></div>

<%-- tempalte for jsrender.min.js to use, "{{>" - html encoded and "{{:" - not encoded, but if string, it works the same; == and === are different (?) --%>
<script id="agesrchTitleTemplate" type="text/html">
		<tr><td align="center" colspan="8">
		{{if selectRange == "BEST"}}
			Age Range: {{: selectRange }} 
		{{else}}
			Age Range: {{: selectRange }}0
		{{/if}}
		</td>
		<tr class="gray_back">
            <th align="center">Order</th>
			<th align="center"><a href="#" onclick="QQ_agesrch.sortIt('ticker');" class="datalink">Stock</a></th>
			{{if showChart === "Y"}}
            	<th align="center">Charts <input type="checkbox" id="showAllCheck" onClick="Q.agesrch.loadAgeList('')" checked /></th>
			{{else}}
				<th align="center">Charts <input type="checkbox" id="showAllCheck" onClick="Q.agesrch.loadAgeList('')"/></th>
			{{/if}}
			<th align="center"><a href="#" onclick="QQ_agesrch.sortIt('company');" class="datalink">Company</th>
            <th align="center">Age</th>
            <th align="center"><a href="#" onclick="QQ_agesrch.sortIt('cross');" class="datalink">Cross Price</a></th>
            <th align="center"><a href="#" onclick="QQ_agesrch.sortIt('current');" class="datalink">Current Price</a></th>
            <th align="center"><a href="#" onclick="QQ_agesrch.sortIt('gain');" class="datalink">Gain/Loss</a></th>
        </tr>

</script>

<script id="agesrchListTemplate" type="text/html">
	
			<tr>
				<td align="center" style="background-color: {{: rowColor }}">{{:#getIndex() + 1}}</td>

				<td align="left" style="background-color: {{: rowColor }}">
						<A href="../s/analyze_basicanalyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.period=6&inputVO.stockId={{: stockId }}" class="datalink">{{: ticker }}</a>
                </td>
				
				<td align="center" style="background-color: {{: rowColor }}" nowrap>
		
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
				<td align="left" style="background-color: {{: rowColor }}">{{: company }}</td>
				<td align="center" style="background-color: {{: rowColor }}">{{: age51 }}</td>
				<td align="center" style="background-color: {{: rowColor }}">{{: ptPrice }}</td>
				<td align="center" style="background-color: {{: rowColor }}">{{: currPrice }}</td>
				<td align="center" style="background-color: {{: rowColor }}"><font color="{{: gainColor }}"><b>{{: gain }}</b></font></td>
			</tr>
			{{if showChart === "Y"}}
			<tr>
				<td colspan="8" align="center" style="background:#eaeaea;" >
					<img src="../drawchart?stockId={{: stockId }}&period=12&option=NNNY">
				</td>
			</tr>
			{{/if}}

		
</script>
<jsp:include page="../template/watchlist/addwatch_template.jsr"></jsp:include>
