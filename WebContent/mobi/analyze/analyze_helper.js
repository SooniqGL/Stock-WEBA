// analyze_helper.js

// current position of the chart
var currentDisplayDate = "";

// handle the date buttons
function goNow() {
	var form = document.mobiAnalyzeForm;
	var startDate 	= form.startDate.value;
	var endDate 	= form.endDate.value;
	
	if (startDate == null || startDate == "" ||
		endDate == null || endDate == "") {
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

function goByMonth( numMonth ) {
	var form = document.mobiAnalyzeForm;
	var numberMonth = parseInt(numMonth, 10);
	
	var startDate 	= form.startDate.value;
	var endDate 	= form.endDate.value;
	
	if (startDate == null || startDate == "" ||
		endDate == null || endDate == "") {
		// something wrong with the input dates
		alert("Error ...");
		return;
	}
	
	if (currentDisplayDate == "") {
		// set to the end date
		currentDisplayDate = endDate;
	}
	
	var currentDisplayDateObj 	= parseDate(currentDisplayDate);
	var displayDate 			= addMonthToDate(currentDisplayDateObj, numberMonth);

	if (numberMonth > 0) {
		if (compareDates(displayDate, endDate) > 0) {
			displayDate = endDate;
		}
	} else {
		if (compareDates(displayDate, startDate) < 0) {
			displayDate = startDate;
		}
	}
	
	// update the date
	currentDisplayDate = displayDate;
	
//	alert("start: " + startDate + "\nend: "
//	 + endDate + "\nadjust month: " + numberMonth
//	 + "\ndisplay date: " + displayDate);
	
	showChart(displayDate);
}

function goByDay( numDay ) {
	var form = document.mobiAnalyzeForm;
	var dayInt = parseInt(numDay, 10);
	
	// debug
	// alert("adjust day: " + dayInt);
	
	var startDate 	= form.startDate.value;
	var endDate 	= form.endDate.value;
	
	if (startDate == null || startDate == "" ||
		endDate == null || endDate == "") {
		// something wrong with the input dates
		alert("Error ...");
		return;
	}
	
	if (currentDisplayDate == "") {
		// set to the end date
		currentDisplayDate = endDate;
	}
	
	var currentDisplayDateObj 	= parseDate(currentDisplayDate);
	var displayDate 			= addDayToDate(currentDisplayDateObj, dayInt);

	if (dayInt > 0) {
		if (compareDates(displayDate, endDate) > 0) {
			displayDate = endDate;
		}
	} else {
		if (compareDates(displayDate, startDate) < 0) {
			displayDate = startDate;
		}
	}
	
	// update the date
	currentDisplayDate = displayDate;
	
//	alert("start: " + startDate + "\nend: "
//	 + endDate + "\nadjust day: " + dayInt
//	 + "\ndisplay date: " + displayDate);
	 
	showChart(displayDate);
}


// When date is set, display the chart.
// mode: HIS, or NOW
function showChart(displayDate) {
	var form = document.mobiAnalyzeForm;
	var stockId = form.all["inputVO.stockId"].value;
	var period  = form.all["inputVO.period"].value;
	var option  = form.all["inputVO.option"].value;
		
	// for now
	var url = " ../../drawchart?chartType=MC&stockId=" + stockId
		+ "&period=" + period + "&option=" + option + "&inputVO.type=id";
	
	// "" - display as default
	if (displayDate == null) {
		displayDate = "";
	}
	
	url += "&displayDate=" + displayDate;
	
	// debug
	//alert("url: " + url);
	
	// switch the image
	var chartObj = get_obj("stock_chart");
	if (chartObj != null) {
		chartObj.src = url;
	} else {
		alert("img not found" );
	}
	
	// adjust buttons when url is shown
	adjustButtons();
}

function adjustButtons() {
	var form = document.mobiAnalyzeForm;
	
	var nowButton = form.button_now;
	if (nowButton == null) {
		// now stock is loaded
		return;
	}
	
	
	
	var startDate 	= form.startDate.value;
	var endDate 	= form.endDate.value;
//alert("adjust button 0: " + startDate + ", " + endDate );	
	if (startDate == null || startDate == "" ||
		endDate == null || endDate == "") {
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
	var currentDisplayDateObj = parseDate(currentDisplayDate);

//alert("adjust button 1: " + currentDisplayDateObj.getDate() + ", " +  currentDisplayDateObj.getMonth() );	
	
	// do day: -1, -5, -10, 1, 5, 10
	var nextDate = addDayToDate(currentDisplayDateObj, -1);
	if (compareDates(nextDate, startDate) < 0) {
		form.button_left_1d.disabled = true;
	} else {
		form.button_left_1d.disabled = false;
	}
	
	nextDate = addDayToDate(currentDisplayDateObj, -5);
	if (compareDates(nextDate, startDate) < 0) {
		form.button_left_5d.disabled = true;
	} else {
		form.button_left_5d.disabled = false;
	}
	
	nextDate = addDayToDate(currentDisplayDateObj, -10);
	if (compareDates(nextDate, startDate) < 0) {
		form.button_left_10d.disabled = true;
	} else {
		form.button_left_10d.disabled = false;
	}
	
	nextDate = addDayToDate(currentDisplayDateObj, 1);
	if (compareDates(nextDate, endDate) > 0) {
		form.button_right_1d.disabled = true;
	} else {
		form.button_right_1d.disabled = false;
	}
	
	nextDate = addDayToDate(currentDisplayDateObj, 5);
	if (compareDates(nextDate, endDate) > 0) {
		form.button_right_5d.disabled = true;
	} else {
		form.button_right_5d.disabled = false;
	}
	
	nextDate = addDayToDate(currentDisplayDateObj, 10);
	if (compareDates(nextDate, endDate) > 0) {
		form.button_right_10d.disabled = true;
	} else {
		form.button_right_10d.disabled = false;
	}
	
	// month: -1, -6, -12, 1, 6, 12
	nextDate = addMonthToDate(currentDisplayDateObj, -1);
	if (compareDates(nextDate, startDate) < 0) {
		form.button_left_1m.disabled = true;
	} else {
		form.button_left_1m.disabled = false;
	}
	
	nextDate = addMonthToDate(currentDisplayDateObj, -6);
	if (compareDates(nextDate, startDate) < 0) {
		form.button_left_6m.disabled = true;
	} else {
		form.button_left_6m.disabled = false;
	}
	
	nextDate = addMonthToDate(currentDisplayDateObj, -12);
	if (compareDates(nextDate, startDate) < 0) {
		form.button_left_12m.disabled = true;
	} else {
		form.button_left_12m.disabled = false;
	}
	
	nextDate = addMonthToDate(currentDisplayDateObj, 1);
	if (compareDates(nextDate, endDate) > 0) {
		form.button_right_1m.disabled = true;
	} else {
		form.button_right_1m.disabled = false;
	}
	
	nextDate = addMonthToDate(currentDisplayDateObj, 6);
	if (compareDates(nextDate, endDate) > 0) {
		form.button_right_6m.disabled = true;
	} else {
		form.button_right_6m.disabled = false;
	}
	
	nextDate = addMonthToDate(currentDisplayDateObj, 12);
	if (compareDates(nextDate, endDate) > 0) {
		form.button_right_12m.disabled = true;
	} else {
		form.button_right_12m.disabled = false;
	}
	
	// for now button
	//alert("curent/end: " + currentDisplayDate + "," +  endDate);
	
	if (compareDates(currentDisplayDate, endDate) == 0) {
		form.button_now.disabled = true;
	} else {
		form.button_now.disabled = false;
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
// End of script



