<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">

#tabs {
	border:1px solid #636363; 
	background:#cccccc none; padding:5px; FONT-SIZE:12pt; 
}
.ui-widget-header {
	border:0; background:#cccccc none; font-family:Georgia;
}
#tabs .ui-widget-content {
	border:1px solid #aaaaaa; background:#ffffff none;
	font-size: 12pt; heigt:800px;
}
.ui-state-default, .ui-widget-content .ui-state-default {
	background:#f6f6f6 none; border:1px solid #cccccc;
}
.ui-state-active, .ui-widget-content .ui-state-active {
	background:#ffffff none; border:1px solid #fbd850;
}

</style>

<s:set var="recentList" value="inputVO.recentList" />
<s:set var="suggestList" value="inputVO.suggestList" />
<s:set var="watchList" value="inputVO.watchList" />

<div class="leftsidewrapper2">
	<div id="tabs">
		<ul>
			<li><a href="#tabs-1">W</a></li>
			<li><a href="#tabs-2">S</a></li>
			<li><a href="#tabs-3">M</a></li>
		</ul>
		
		<div id="tabs-1">
			<s:iterator value="#watchList">
				<A href="javaScript:Q.chart.goTicker('<s:property />')"
					class="sublink3"><s:property /></a>
			</s:iterator>
		</div>
	
		<div id="tabs-2">
			<s:iterator value="#suggestList">
				<A href="javaScript:Q.chart.goTicker('<s:property />')"
					class="sublink3"><s:property /></a>
			</s:iterator>
		</div>
	
	
		<div id="tabs-3">
			<a href="javaScript:Q.chart.goStockId('MKT001')" class="sublink">Nasdaq
				Index</a><br /> <a href="javaScript:Q.chart.goStockId('MKT002')" class="sublink">Dow
				Index</a><br /> <a href="javaScript:Q.chart.goStockId('MKT003')" class="sublink">SP500
				Index</a><br />
			<br />
			<%--a href="#" onClick="popTopsection()" class="sublink">Show/Hide</a--%>
	
			<input type="button" value="Tickers" onclick="QQ_chartoverlay.show_div()"></input><br />
			<br />
		</div>
		
	</div>
</div>
<script type="text/javascript">

  $(function() {

    $( "#tabs" ).tabs();

  });
</script>