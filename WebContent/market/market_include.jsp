<%@ taglib prefix="s" uri="/struts-tags" %>

<LINK href="/WEBA/theme/main_style.css" rel="stylesheet" type="text/css">
	
<s:if test="%{HANDLER == \"marketpulse\"}">
	<script SRC="/WEBA/script/common.js" type="text/javascript" ></script>
	<script SRC="/WEBA/market/market_pulse_helper.js" type="text/javascript" ></script>
</s:if>