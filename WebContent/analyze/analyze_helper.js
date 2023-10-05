/*
 * First set scripts for analyze page - scripts to reset the display date.
 * analyze_helper.js
 */

var QQ_staticchart = ( function( window, undefined ) {

	// current position of the chart
	var currentDisplayDate = "";
	
	// handle the date buttons
	function goNow() {
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
	
		if (startDate == null || startDate == "" || endDate == null
				|| endDate == "") {
			// something wrong with the input dates
			alert("Error ...");
			return;
		}
	
		// debug
		//	alert("now is called: " + endDate);
	
		// set to the end date
		currentDisplayDate = endDate;
	
		// show the chart
		showChart("");
	}
	
	function goByMonth(numMonth) {
	
		var numberMonth = parseInt(numMonth, 10);
	
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
	
		if (startDate == null || startDate == "" || endDate == null
				|| endDate == "") {
			// something wrong with the input dates
			alert("Error ...");
			return;
		}
	
		if (currentDisplayDate == "") {
			// set to the end date
			currentDisplayDate = endDate;
		}
	
		var currentDisplayDateObj = Q.date.parseDate(currentDisplayDate);
		var displayDate = Q.date.addMonthToDate(currentDisplayDateObj, numberMonth);
	
		if (numberMonth > 0) {
			if (Q.date.compareDates(displayDate, endDate) > 0) {
				displayDate = endDate;
			}
		} else {
			if (Q.date.compareDates(displayDate, startDate) < 0) {
				displayDate = startDate;
			}
		}
	
		// update the date
		currentDisplayDate = displayDate;
	
		//alert("start: " + startDate + "\nend: "
		// + endDate + "\nadjust month: " + numberMonth
		// + "\ndisplay date: " + displayDate);
	
		showChart(displayDate);
	}
	
	function goByDay(numDay) {
		//alert("adjust day: " + numDay);
		var dayInt = parseInt(numDay, 10);
	
		// debug
		//alert("adjust day 2: " + dayInt);
	
		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
	
		if (startDate == null || startDate == "" || endDate == null
				|| endDate == "") {
			// something wrong with the input dates
			alert("Error ...");
			return;
		}
	
		if (currentDisplayDate == "") {
			// set to the end date
			currentDisplayDate = endDate;
		}
	
		var currentDisplayDateObj = Q.date.parseDate(currentDisplayDate);
		var displayDate = Q.date.addDayToDate(currentDisplayDateObj, dayInt);
	
		if (dayInt > 0) {
			if (Q.date.compareDates(displayDate, endDate) > 0) {
				displayDate = endDate;
			}
		} else {
			if (Q.date.compareDates(displayDate, startDate) < 0) {
				displayDate = startDate;
			}
		}
	
		// update the date
		currentDisplayDate = displayDate;
	
		//alert("start: " + startDate + "\nend: "
		//+ endDate + "\nadjust day: " + dayInt
		//+ "\ndisplay date: " + displayDate + "\ncurrent display: " + currentDisplayDate);
	
		showChart(displayDate);
	}
	
	// When date is set, display the chart.
	// mode: HIS, or NOW
	function showChart(displayDate) {
		var stockId = document.getElementById("inputVO.stockId").value;
		var period = document.getElementById("inputVO.period").value;
		var option = document.getElementById("inputVO.option").value;
		var doSP500 = document.getElementById("inputVO.doSP500").value;
	
		// "" - display as default
		if (displayDate == null) {
			displayDate = "";
		}
	
		// for now
		var url = " ../drawchart?stockId=" + stockId + "&period=" + period
				+ "&option=" + option + "&inputVO.type=id" + "&displayDate="
				+ displayDate;
	
		// debug
		//alert("url: " + url);
	
		// switch the image
		var chartObj = get_obj("stock_chart");
		if (chartObj != null) {
			chartObj.src = url;
		} else {
			alert("img not found");
		}
	
		if (doSP500 != null && doSP500 == "Y") {
			// for now
			var url2 = " ../drawchart?stockId=MKT003" + "&period=" + period
					+ "&option=" + option + "&inputVO.type=id" + "&displayDate="
					+ displayDate;
	
			// debug
			//alert("url2: " + url2);
	
			// switch the image
			var chartObj2 = get_obj("stock_chart2");
			if (chartObj2 != null) {
				chartObj2.src = url2;
			} else {
				alert("img not found");
			}
		}
	
		// adjust buttons when url is shown
		//alert("c 0: " + currentDisplayDate);
		adjustButtons();
	}
	
	function adjustButtons() {

		var nowButton = document.getElementById("button_now");
		if (nowButton == null) {
			// now stock is loaded
			return;
		}

		var startDate = document.getElementById("startDate").value;
		var endDate = document.getElementById("endDate").value;
		//alert("adjust button 0: " + startDate + ", " + endDate );	
		if (startDate == null || startDate == "" || endDate == null
				|| endDate == "") {
			// something wrong with the input dates
			alert("Error ...");
			return;
		}
	
		// default current display date if not set.
		if (currentDisplayDate == "") {
			// set to the end date
			currentDisplayDate = endDate;
		}
	
		//alert("c: " + currentDisplayDate);
		// current display date
		var currentDisplayDateObj = Q.date.parseDate(currentDisplayDate);
	
		//alert("adjust button 1: " + currentDisplayDateObj.getDate() + ", " +  currentDisplayDateObj.getMonth() );
	
		// do day: -1, -5, -10, 1, 5, 10
		var nextDate = Q.date.addDayToDate(currentDisplayDateObj, -1);
		if (Q.date.compareDates(nextDate, startDate) < 0) {
			document.getElementById("button_left_1d").disabled = true;
		} else {
			document.getElementById("button_left_1d").disabled = false;
		}
	
		nextDate = Q.date.addDayToDate(currentDisplayDateObj, -5);
		if (Q.date.compareDates(nextDate, startDate) < 0) {
			document.getElementById("button_left_5d").disabled = true;
		} else {
			document.getElementById("button_left_5d").disabled = false;
		}
	
		nextDate = Q.date.addDayToDate(currentDisplayDateObj, -10);
		if (Q.date.compareDates(nextDate, startDate) < 0) {
			document.getElementById("button_left_10d").disabled = true;
		} else {
			document.getElementById("button_left_10d").disabled = false;
		}
	
		nextDate = Q.date.addDayToDate(currentDisplayDateObj, 1);
		if (Q.date.compareDates(nextDate, endDate) > 0) {
			document.getElementById("button_right_1d").disabled = true;
		} else {
			document.getElementById("button_right_1d").disabled = false;
		}
	
		nextDate = Q.date.addDayToDate(currentDisplayDateObj, 5);
		if (Q.date.compareDates(nextDate, endDate) > 0) {
			document.getElementById("button_right_5d").disabled = true;
		} else {
			document.getElementById("button_right_5d").disabled = false;
		}
	
		nextDate = Q.date.addDayToDate(currentDisplayDateObj, 10);
		if (Q.date.compareDates(nextDate, endDate) > 0) {
			document.getElementById("button_right_10d").disabled = true;
		} else {
			document.getElementById("button_right_10d").disabled = false;
		}
	
		// month: -1, -6, -12, 1, 6, 12
		nextDate = Q.date.addMonthToDate(currentDisplayDateObj, -1);
		if (Q.date.compareDates(nextDate, startDate) < 0) {
			document.getElementById("button_left_1m").disabled = true;
		} else {
			document.getElementById("button_left_1m").disabled = false;
		}
	
		nextDate = Q.date.addMonthToDate(currentDisplayDateObj, -6);
		if (Q.date.compareDates(nextDate, startDate) < 0) {
			document.getElementById("button_left_6m").disabled = true;
		} else {
			document.getElementById("button_left_6m").disabled = false;
		}
	
		nextDate = Q.date.addMonthToDate(currentDisplayDateObj, -12);
		if (Q.date.compareDates(nextDate, startDate) < 0) {
			document.getElementById("button_left_12m").disabled = true;
		} else {
			document.getElementById("button_left_12m").disabled = false;
		}
	
		nextDate = Q.date.addMonthToDate(currentDisplayDateObj, 1);
		if (Q.date.compareDates(nextDate, endDate) > 0) {
			document.getElementById("button_right_1m").disabled = true;
		} else {
			document.getElementById("button_right_1m").disabled = false;
		}
	
		nextDate = Q.date.addMonthToDate(currentDisplayDateObj, 6);
		if (Q.date.compareDates(nextDate, endDate) > 0) {
			document.getElementById("button_right_6m").disabled = true;
		} else {
			document.getElementById("button_right_6m").disabled = false;
		}
	
		nextDate = Q.date.addMonthToDate(currentDisplayDateObj, 12);
		if (Q.date.compareDates(nextDate, endDate) > 0) {
			document.getElementById("button_right_12m").disabled = true;
		} else {
			document.getElementById("button_right_12m").disabled = false;
		}
	
		// for now button
		//alert("curent/end: " + currentDisplayDate + "," +  endDate);
	
		if (Q.date.compareDates(currentDisplayDate, endDate) == 0) {
			document.getElementById("button_now").disabled = true;
		} else {
			document.getElementById("button_now").disabled = false;
		}
	
		//	alert("adjust button 1" );
	}
	
	// If the current date is not set, make sure to set it.
	/*
	 function initCurrentDate() {
	 if (currentYear == 0) {
	 var now = new Date();
	 currentDay = now.getDate();
	 currentMonth = now.getMonth() + 1;
	 currentYear = now.getYear();
	 }
	 } */
	
	// find chart
	function get_obj(id_name) {
		if (document.getElementById) {
			return document.getElementById(id_name);
		} else if (document.all) {
			return document.all[id_name];
		} else {
			return null;
		}
	}
	
	//explicitly return public methods when this object is instantiated
	return {
		goNow : goNow,
		goByMonth : goByMonth,
		goByDay : goByDay,
		adjustButtons : adjustButtons
	};

} )( window );

/* The End */

