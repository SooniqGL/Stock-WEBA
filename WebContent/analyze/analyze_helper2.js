/* 
 * Second set of scripts for analyze page.
 */

if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

Q.namespace("chart");

Q.chart.submitTickerForAnalyze = function() {
	var ticker = document.getElementById("inputVO.ticker").value;
	Q.chart.collectOptions();

	if (ticker == "") {
		alert("Enter the ticker.");
		return false;
	} else {
		document.getElementById("inputVO.type").value = "ticker";
		return true;
	}
};

Q.chart.setFocus = function() {

	// var form = document.analyzeForm;
	document.getElementById("inputVO.ticker").select();
	document.getElementById("inputVO.ticker").focus();

	var period = document.getElementById("inputVO.period").value;

	// find all the elements for the radio button
	var ckPeriodObj = document.getElementsByName("ckPeriod");

	if (period == "3") {
		ckPeriodObj[0].checked = true;
	} else if (period == "6") {
		ckPeriodObj[1].checked = true;
	} else if (period == "12") {
		ckPeriodObj[2].checked = true;
	} else if (period == "24") {
		ckPeriodObj[3].checked = true;
	} else {
		ckPeriodObj[1].checked = true;
		document.getElementById("inputVO.period").value = "6";
	}

	var option = document.getElementById("inputVO.option").value;
	if (option != null && option.length > 0 && option.charAt(0) == 'Y') {
		document.getElementById("doEMA20").checked = true;
	} else {
		document.getElementById("doEMA20").checked = false;
	}

	if (option != null && option.length > 1 && option.charAt(1) == 'Y') {
		document.getElementById("doEMA50").checked = true;
	} else {
		document.getElementById("doEMA50").checked = false;
	}

	if (option != null && option.length > 2 && option.charAt(2) == 'Y') {
		document.getElementById("doEMA100").checked = true;
	} else {
		document.getElementById("doEMA100").checked = false;
	}

	if (option != null && option.length > 3 && option.charAt(3) == 'N') {
		document.getElementById("doZone").checked = false;
	} else {
		document.getElementById("doZone").checked = true;
	}

	// the next option may not exist in initial screen, it is optional
	var doBARSObj = document.getElementById("doBARS");
	if (doBARSObj != null) {
		if (option != null && option.length > 4 && option.charAt(4) == 'N') {
			document.getElementById("doBARS").checked = false;
		} else {
			document.getElementById("doBARS").checked = true;
		}
	}

	// the next option may not exist in initial screen, it is optional
	var doSP500 = document.getElementById("inputVO.doSP500").value;
	var doSPObj = document.getElementById("doSP500");
	if (doSPObj != null) {
		if (doSP500 != null && doSP500 == 'Y') {
			document.getElementById("doSP500").checked = true;
		} else {
			document.getElementById("doSP500").checked = false;
		}
	}

	// set tickerlist  (hiddenText is used to disable the auto select by jQuery)
	var hiddenText = "<span class=\"ui-helper-hidden-accessible\"><input type=\"text\"/></span>";
	var btStr = "<br/><br/><center>Click outside of the dialog (or ESC) to close.</center>";
	var tickerListElem = document.getElementById("tickerList");
	var showFlag = document.getElementById("inputVO.showFlag");
	if (showFlag != null && showFlag.value == "R") {
		tickerListElem.innerHTML = hiddenText + document.getElementById("rTickerList").value
				+ "<br/>" + document.getElementById("mTickerList").value
				+ btStr;
	} else {
		tickerListElem.innerHTML = hiddenText + document.getElementById("rTickerList").value
				+ "<br/>" + document.getElementById("mTickerList").value
				+ btStr;
	}
	
	// remove the value
	document.getElementById("mTickerList").value = "";
	document.getElementById("rTickerList").value = "";

};

Q.chart.removeTicker = function() {
	document.getElementById("backupTicker").value = document
			.getElementById("inputVO.ticker").value;
	document.getElementById("inputVO.ticker").value = "";
};

Q.chart.resetTicker = function() {
	if (document.getElementById("inputVO.ticker").value == "") {
		document.getElementById("inputVO.ticker").value = document
				.getElementById("backupTicker").value;
	}
};

Q.chart.goTicker = function(ticker) {
	var form = document.analyzeForm;
	Q.chart.collectOptions();
	document.getElementById("inputVO.ticker").value = ticker;
	document.getElementById("inputVO.type").value = "ticker";
	form.submit();
};

Q.chart.goStockId = function(id){
	var form = document.analyzeForm;
	Q.chart.collectOptions();
	document.getElementById("inputVO.stockId").value = id;
	document.getElementById("inputVO.type").value = "id";
	form.submit();
};

Q.chart.setPeriod = function(period) {
	var form = document.analyzeForm;
	Q.chart.collectOptions();
	document.getElementById("inputVO.period").value = period;

	if (document.getElementById("inputVO.ticker").value != "") {
		document.getElementById("inputVO.type").value = "ticker";
		form.submit();
	} else if (document.getElementById("backupTicker").value != "") {
		document.getElementById("inputVO.ticker").value = document
				.getElementById("backupTicker").value;
		document.getElementById("inputVO.type").value = "ticker";
		form.submit();
	}
};

Q.chart.collectOptions = function() {
	var option = "";

	if (document.getElementById("doEMA20").checked == true) {
		option += "Y";
	} else {
		option += "N";
	}

	if (document.getElementById("doEMA50").checked == true) {
		option += "Y";
	} else {
		option += "N";
	}

	if (document.getElementById("doEMA100").checked == true) {
		option += "Y";
	} else {
		option += "N";
	}

	if (document.getElementById("doZone").checked == true) {
		option += "Y";
	} else {
		option += "N";
	}

	// the next option may not exist in initial screen, it is optional
	var doBARSObj = document.getElementById("doBARS");
	if (doBARSObj != null) {
		if (document.getElementById("doBARS").checked == true) {
			option += "Y";
		} else {
			option += "N";
		}
	}

	document.getElementById("inputVO.option").value = option;
};

Q.chart.setOption = function() {
	var form = document.analyzeForm;
	Q.chart.collectOptions();
	if (document.getElementById("inputVO.ticker").value != "") {
		document.getElementById("inputVO.type").value = "ticker";
		form.submit();
	} else if (document.getElementById("backupTicker").value != "") {
		document.getElementById("inputVO.ticker").value = document
				.getElementById("backupTicker").value;
		document.getElementById("inputVO.type").value = "ticker";
		form.submit();
	}
};

Q.chart.setOption2 = function() {
	var form = document.analyzeForm;
	var doSP500 = "";
	if (document.getElementById("doSP500").checked == true) {
		doSP500 += "Y";
	} else {
		doSP500 += "N";
	}

	document.getElementById("inputVO.doSP500").value = doSP500;

	if (document.getElementById("inputVO.ticker").value != "") {
		document.getElementById("inputVO.type").value = "ticker";
		form.submit();
	} else if (document.getElementById("backupTicker").value != "") {
		document.getElementById("inputVO.ticker").value = document
				.getElementById("backupTicker").value;
		document.getElementById("inputVO.type").value = "ticker";
		form.submit();
	}
};

Q.chart.submitIdForAnalyze = function(stockId) {

	document.getElementById("inputVO.type").value = "id";
	document.getElementById("inputVO.stockId").value = stockId;
	document.analyzeForm.submit();

};

Q.chart.expendIt = function() {
	var cnv = document.getElementById('tickerList');
	if (cnv.style.height != "80px") {
		cnv.style.height = "80px";
	} else {
		cnv.style.height = "40px";
	}

};

Q.chart.flipTickerListR = function() {
	//alert("flip R");
	var form = document.analyzeForm;
	var tickerListElem = document.getElementById("tickerList");
	tickerListElem.innerHTML = form.rTickerList.value;
	document.getElementById("inputVO.showFlag").value = "R";
};

Q.chart.flipTickerListM = function() {
	//alert("flip M");
	var form = document.analyzeForm;
	var tickerListElem = document.getElementById("tickerList");
	tickerListElem.innerHTML = form.mTickerList.value;
	document.getElementById("inputVO.showFlag").value = "M";
};

// ajax test
/*
function doAjaxCall() {
	//alert("call ajax");

	makeAjaxCall("../ajaxsvr?test=AAA", "", handleAjaxResponse);

	//alert("done ajax");

}

function handleAjaxResponse(xmlobj) {

	var outputHTML = xmlobj.responseText;
	outputHTML = outputHTML.replace("<AJAXRESPONSE>", "");
	outputHTML = outputHTML.replace("</AJAXRESPONSE>", "");
	//alert(outputHTML);
	//var accountDetail = document.getElementById("tickerList");
	// accountDetail.innerHTML = outputHTML;

	alert("html: " + outputHTML);
	var dom = parseXML(outputHTML);
	var err = dom.getElementsByTagName("h1");
	if (err != null && err.length > 0 && err[0].childNodes[0] != null) {
		for (var i = 0; i < err.length; i++) {
			alert("h1: " + err[i].nodeName + ", "
					+ err[i].childNodes[0].nodeValue);
		}

	}

}

function parseXML(xml) {
	try {
		dom = new ActiveXObject("Microsoft.XMLDOM");
		dom.loadXML(xml);
		alert("microsoft");
		return dom;
	} catch (e) {
		alert("not microsoft");
		return new DOMParser().parseFromString(xml, "tesxt/xml");
	}
} */

/* The End */