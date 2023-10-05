// scan_chart_helper.js

/* framework Q */
if (typeof Q == "undefined" || !Q) {
	var Q = {};
}

// for scan only methods
Q.namespace("scanchart");

Q.scanchart.submitRequest = function() {
    
	var scanKey = document.getElementById("dropdown").value;

            if (scanKey != "") {
                document.getElementById("inputVO.scanKey").value = scanKey;
                document.scanForm.submit();
            }
};

// handle the date buttons
Q.scanchart.showChartFromDD = function() {
	//alert("scan 0: ");
        var selectIndex = document.getElementById("dateList").value;
	document.getElementById("scanIndex").value = selectIndex;

        //alert("scan Index 1: " + selectIndex);

	// show the chart
	Q.scanchart.showChart(selectIndex);

};

Q.scanchart.showChartWithShift = function(shiftStr) {
	var scanIndex  = document.getElementById("scanIndex").value;

        var newScanIndex = 0;
        if (shiftStr == "-1") {
            newScanIndex = -1 + parseInt(scanIndex, 10);
        } else if (shiftStr == "1") {
            newScanIndex = 1 + parseInt(scanIndex, 10);
        }

        Q.scanchart.showChart(newScanIndex);
};

// Shift -1, 1, or nothing and then display the chart
Q.scanchart.showChart = function(scanIndex) {
	//alert("show chart");
        var scanKey = document.getElementById("dropdown").value;
        document.getElementById("scanIndex").value = scanIndex;

        //alert("scan Index 2: " + scanIndex);

        if (scanKey == null || scanKey == "") {
            alert("Please select scan mode in the drop down.");
            return;
        }

	// for now
	var url = " ../drawchart?chartType=SC&scanKey=" + scanKey + "&scanIndex=" + scanIndex;

	// debug
	//alert("url: " + url);

	// switch the image
	var chartObj = Q.scanchart.get_obj("report_chart");
	if (chartObj != null) {
		chartObj.src = url;
	} else {
		alert("img not found" );
	}

	// adjust buttons when url is shown
	Q.scanchart.adjustButtons();
};

Q.scanchart.adjustButtons = function() {
	var form = document.scanForm;

	var scanIndex = form.scanIndex.value;
        var dateListSize = form.dateList.length;
        if (dateListSize == 0) {
            return;
        }

        //alert("adjust button 1" );

        if (scanIndex >= dateListSize - 1) {
            // which is the last one
            form.button_prev.disabled = true;
        } else {
            form.button_prev.disabled = false;
        }

        //alert("adjust button 2" );

        if (scanIndex <= 0) {
            // which is the last one
            form.button_next.disabled = true;
        } else {
            form.button_next.disabled = false;
        }

        //alert("adjust button 3" );
        // move the select index
        if (scanIndex >= 0 && scanIndex <= dateListSize - 1) {
            form.dateList.selectedIndex = scanIndex;
        }
	
};


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
Q.scanchart.get_obj = function(id_name) {
   if (document.getElementById) {
      return document.getElementById(id_name);
   } else if (document.all) {
      return document.all[id_name];
   } else {
      return null;
   }
};

/* The End */



