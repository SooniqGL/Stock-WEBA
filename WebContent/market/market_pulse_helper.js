// market_pulse_helper.js

/* framework Q */
if (typeof Q == "undefined" || !Q) {
	var Q = {};
}

// for scan only methods
Q.namespace("market");

// handle the date buttons
Q.market.showChartFromDD = function() {

	var selectIndex = document.getElementById("dateList").value;
	document.getElementById("screenIndex").value = selectIndex;

	//alert("screen Index 1: " + selectIndex);

	// show the chart
	Q.market.showChart(selectIndex);

};

Q.market.showChartWithShift = function(shiftStr) {
	var screenIndex = document.getElementById("screenIndex").value;

	var newScreenIndex = 0;
	if (shiftStr == "-1") {
		newScreenIndex = -1 + parseInt(screenIndex, 10);
	} else if (shiftStr == "1") {
		newScreenIndex = 1 + parseInt(screenIndex, 10);
	}

	Q.market.showChart(newScreenIndex);
};

// Shift -1, 1, or nothing and then display the chart
Q.market.showChart = function(screenIndex) {
	var chartType = document.getElementById("chartType").value;
	var marketType = document.getElementById("marketType").value;
	var extraOption = document.getElementById("extraOption").value;
	document.getElementById("screenIndex").value = screenIndex;

	//alert("screen Index 2: " + screenIndex);

	// for now
	var url = " ../drawchart?chartType=" + chartType + "&marketType="
			+ marketType + "&screenIndex=" + screenIndex
			+ "&extraOption=" + extraOption;

	// debug
	//alert("url: " + url);

	// switch the image
	var chartObj = Q.market.get_obj("market_chart");
	if (chartObj != null) {
		chartObj.src = url;
	} else {
		alert("img not found");
	}

	// adjust buttons when url is shown
	Q.market.adjustButtons();
};

Q.market.showChartForDate = function() {
	var chartType = document.getElementById("chartType").value;
	var marketType = document.getElementById("marketType").value;
	var startDate = document.getElementById("startDate").value;
	var endDate = document.getElementById("endDate").value;
	var extraOption = document.getElementById("extraOption").value;
	
	//alert("screen Index 2: " + screenIndex);
	if (endDate == "" || startDate == "") {
		alert("Enter <start/end dates> to load ...");
		return;
	}
	// for now
	var url = " ../drawchart?chartType=" + chartType + "&marketType="
			+ marketType + "&startDate=" + startDate + "&endDate=" + endDate 
			+ "&extraOption=" + extraOption;

	// debug
	//alert("url: " + url);

	// switch the image
	var chartObj = Q.market.get_obj("market_chart");
	if (chartObj != null) {
		chartObj.src = url;
	} else {
		alert("img not found");
	}

	// adjust buttons when url is shown
	// Q.market.adjustButtons();
};

Q.market.adjustButtons = function() {
	var form = document.marketForm;

	var screenIndex = form.screenIndex.value;
	var dateListSize = form.dateList.length;
	if (dateListSize == 0) {
		return;
	}

	//alert("adjust button 1" );

	if (screenIndex >= dateListSize - 1) {
		// which is the last one
		form.button_prev.disabled = true;
	} else {
		form.button_prev.disabled = false;
	}

	//alert("adjust button 2" );

	if (screenIndex <= 0) {
		// which is the last one
		form.button_next.disabled = true;
	} else {
		form.button_next.disabled = false;
	}

	//alert("adjust button 3" );
	// move the select index
	if (screenIndex >= 0 && screenIndex <= dateListSize - 1) {
		form.dateList.selectedIndex = screenIndex;
	}

};

// find chart
Q.market.get_obj = function(id_name) {
	if (document.getElementById) {
		return document.getElementById(id_name);
	} else if (document.all) {
		return document.all[id_name];
	} else {
		return null;
	}
};

/* The End */

