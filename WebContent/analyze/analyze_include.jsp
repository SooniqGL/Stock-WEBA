<%@ taglib prefix="s" uri="/struts-tags" %>

<LINK href="/WEBA/theme/main_style.css" rel="stylesheet" type="text/css">
	
<s:if test="%{HANDLER == \"basicanalyze\"}">
	<LINK href="/WEBA/theme/jquery-ui-1.10.4.custom.min.css" rel="stylesheet" type="text/css">
	
	<SCRIPT SRC="/WEBA/script/jquery-2.1.1.min.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/script/jquery-ui-1.10.4.min.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/script/jsrender.min.js"></SCRIPT>
	
	<script SRC="/WEBA/script/common.js" type="text/javascript" ></script>
	<SCRIPT SRC="/WEBA/script/date_script.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/script/overlay_global.js"></SCRIPT>
	
	<SCRIPT SRC="/WEBA/analyze/analyze_helper.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/analyze/analyze_helper2.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/analyze/AjaxRequest.js" type="text/javascript"></SCRIPT>
	<SCRIPT SRC="/WEBA/ajax/addwatch_ajax.js"></SCRIPT>
	
</s:if>

<s:if test="%{HANDLER == \"dynamicanalyze\"}">
	<%-- The css/script files for dynamic analysis page to load --%>
	<LINK href="/WEBA/theme/jquery-ui-1.10.4.custom.min.css" rel="stylesheet" type="text/css">
	
	<SCRIPT SRC="/WEBA/script/jquery-2.1.1.min.js"></SCRIPT>
	<%-- The following library (version 3.1.11) seems works fine with latest JQuery 2.1.1 --%>
	<SCRIPT SRC="/WEBA/script/jquery.mousewheel.min.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/script/jquery-ui-1.10.4.min.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/script/jsrender.min.js"></SCRIPT>
	
	<script SRC="/WEBA/script/common.js" type="text/javascript" ></script>
	<SCRIPT SRC="/WEBA/script/date_script.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/script/overlay_global.js"></SCRIPT>
	
	<SCRIPT SRC="/WEBA/analyze/dynamic_helper.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/analyze/dynamic_barchart.js"></SCRIPT>
	<%--SCRIPT SRC="dynamic_drag.js"></SCRIPT--%>
	
	<SCRIPT SRC="/WEBA/analyze/AjaxRequest.js" type="text/javascript"></SCRIPT>
	<SCRIPT SRC="/WEBA/ajax/addwatch_ajax.js"></SCRIPT>
	
</s:if>

<s:if test="%{HANDLER == \"basiccalculator\"}">
	<SCRIPT SRC="/WEBA/script/common.js"></SCRIPT>
	<SCRIPT SRC="/WEBA/analyze/basic_calculator.js"></SCRIPT>
</s:if>