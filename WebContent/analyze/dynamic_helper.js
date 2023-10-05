/* 
 * dynamic_helper.js
 * Java scripts for dynamic analsis page
 */

if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

Q.namespace("chart");

/**
 * Use the jquery and the mousewheel plug in to control the period.
 * The plugin (3.0.6) seems working fine on the lastest JQuery 2.1.1.
 */
$(function() {
	$('#chart').mousewheel(function(event, delta, deltaX, deltaY) {
		var canvas = document.getElementById('chart');
		if (canvas != null) {
			//alert("scroll: " + delta + "," + deltaX + "," + deltaY + ", " + event.pageX + ", " + event.pageY);   
			//this.scrollLeft -= (delta * 30);    

			var inWhich = QQ_barchart.inWhichBox(event.pageX, event.pageY);
			//alert("in which: " + inWhich);
			if (inWhich == "3") {
				// scroll range by month
				Q.chart.setPeriodBymousewheel(delta);
			} else if (inWhich == "1") {
				// scroll end dates by month
				QQ_barchart.updateEndIndexAndDraw("M", delta);
			} else if (inWhich == "2") {
				// scroll end dates by day
				QQ_barchart.updateEndIndexAndDraw("D", delta);
			}

			event.stopPropagation();
			event.preventDefault();
		}
	});
});

Q.chart.submitTickerForAnalyze = function(){
	var ticker = document.getElementById("inputVO.ticker").value;

	if (ticker == "") {
		alert("Enter the ticker.");
		return false;
	} else {
		document.getElementById("inputVO.type").value = "ticker";
		Q.chart.resetData();
		return true;
	}
};

Q.chart.resetData = function() {
	// clean up - to enhance performance
	document.getElementById("dailyDataStr").value = "";
	document.getElementById("weeklyDataStr").value = "";
	document.getElementById("marketFlagList").value = "";
	document.getElementById("marketWeeklyFlagList").value = "";
};

Q.chart.setFocus = function() {
	//alert("set focus");

	document.getElementById("inputVO.ticker").select();
	document.getElementById("inputVO.ticker").focus();

	var period = document.getElementById("inputVO.period").value;

	var ckPeriodObj = document.getElementsByName("ckPeriod");
	if (period == "3") {
		ckPeriodObj[0].checked = true;
	} else if (period == "6") {
		ckPeriodObj[1].checked = true;
	} else if (period == "12") {
		ckPeriodObj[2].checked = true;
	} else if (period == "24") {
		ckPeriodObj[3].checked = true;
	} else if (period != "") {
		//ckPeriodObj[1].checked  = true;
		//document.getElementById("inputVO.period").value = "6";
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

	// set tickerlist (hiddenText is used to disable the auto select by jQuery)
	var hiddenText = "<span class=\"ui-helper-hidden-accessible\"><input type=\"text\"/></span>";
	var btStr = "<br/><br/><center>Click outside of the dialog (or ESC) to close.</center>";
	var tickerListElem = document.getElementById("tickerList");
	var showFlag = document.getElementById("inputVO.showFlag");
	if (showFlag != null && showFlag.value == "R") {
	    tickerListElem.innerHTML = hiddenText + document.getElementById("rTickerList").value 
	    + "<br/>" + document.getElementById("mTickerList").value + btStr;
	} else {
	    tickerListElem.innerHTML = hiddenText + document.getElementById("rTickerList").value 
	    + "<br/>" + document.getElementById("mTickerList").value + btStr ;
	} 
	
	// remove the value
	document.getElementById("mTickerList").value = "";
	document.getElementById("rTickerList").value = "";

	// kick off draw process here
	QQ_barchart.init();
	QQ_barchart.prepareData();
	QQ_barchart.updateEndIndex(-1);
	QQ_barchart.drawChart();
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
	document.getElementById("inputVO.ticker").value = ticker;
	document.getElementById("inputVO.type").value = "ticker";
	Q.chart.resetData();
	form.submit();
};

Q.chart.goStockId = function(id) {
	var form = document.analyzeForm;
	document.getElementById("inputVO.stockId").value = id;
	document.getElementById("inputVO.type").value = "id";
	Q.chart.resetData();
	form.submit();
};

Q.chart.setOption = function() {
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

	// do not need to submit, but just redraw if has data already
	if (document.getElementById("inputVO.ticker").value != "") {
		QQ_barchart.drawChart();
	} else if (document.getElementById("backupTicker").value != "") {
		QQ_barchart.drawChart();
	}
};

// do not need to submit
// When period is changed, no need to switch data.
Q.chart.setPeriod = function (period) {
	document.getElementById("inputVO.period").value = period;

	if (document.getElementById("inputVO.ticker").value != ""
			|| document.getElementById("backupTicker").value != "") {
		// redraw the chart
		QQ_barchart.clearCanvas();
		//prepareData();
		QQ_barchart.updateEndIndex(-1); // always reset the endDate
		QQ_barchart.drawChart();
	}
};

// weekly option is changed from daily to weekly or the other way
// the data has to be re-prepared.
Q.chart.setWeekly = function() {
	if (document.getElementById("inputVO.ticker").value != ""
			|| document.getElementById("backupTicker").value != "") {
		// redraw the chart
		QQ_barchart.clearCanvas();
		QQ_barchart.prepareData(); // need to pick the data
		QQ_barchart.updateEndIndex(-1); // reset end index in this case
		QQ_barchart.drawChart();
	}
};

Q.chart.setReverse = function() {
	if (document.getElementById("inputVO.ticker").value != ""
			|| document.getElementById("backupTicker").value != "") {
		// redraw the chart
		//QQ_barchart.clearCanvas();
		//QQ_barchart.prepareData();   // need to pick the data
		//QQ_barchart.updateEndIndex(-1);  // reset end index in this case
		QQ_barchart.drawChart();
	}
};

Q.chart.setPeriodBymousewheel = function(delta) {
	
	//alert("set period by mouse");
	var oldPeriod = document.getElementById("inputVO.period").value;
	var changed = false;
	if (delta > 0) {
		if (oldPeriod < 36) {
			oldPeriod++;
			document.getElementById("inputVO.period").value = oldPeriod;
			changed = true;
		}
	} else {
		if (oldPeriod > 1) {
			oldPeriod--;
			document.getElementById("inputVO.period").value = oldPeriod;
			changed = true;
		}
	}

	if (changed && document.getElementById("inputVO.ticker").value != ""
			|| document.getElementById("backupTicker").value != "") {
		// redraw the chart
		QQ_barchart.clearCanvas();
		//prepareData();
		QQ_barchart.updateEndIndex(0); // 0 - do not reset the end index
		QQ_barchart.drawChart();
		//adjustButtons();
	}
};

Q.chart.submitIdForAnalyze = function(stockId) {
	var form = document.analyzeForm;

	document.getElementById("inputVO.type").value = "id";
	document.getElementById("inputVO.stockId").value = stockId;
	form.submit();

};

Q.chart.getChart = function() {
	var form = document.getChartForm;

	// displayDate format: yyyy/mm/dd - the current last date in the display
	document.getElementById("displayDate").value = QQ_barchart.getDisplayDate();

	// retrieve most current period and options
	document.getElementById("period").value = document
			.getElementById("inputVO.period").value;
	document.getElementById("option").value = document
			.getElementById("inputVO.option").value;

	var doWeekly = document.getElementById("doWeekly").checked;
	if (doWeekly) {
		document.getElementById("weekly").value = "Y";
	} else {
		document.getElementById("weekly").value = "N";
	}
	
	var doMacd = document.getElementById("doMacd").checked;
	if (doMacd) {
		document.getElementById("chartType").value = "BC2";
	} else {
		document.getElementById("chartType").value = "LG";
	}
	
	var gfFlag = document.getElementById("gfFlag").checked;
	if (gfFlag) {
		document.getElementById("drawGF").value = "Y";
	} else {
		document.getElementById("drawGF").value = "N";
	}

	form.submit();
};

/* end */