/* 
 * basic_scan_helper.js - used by basic_scan.jsp
 */

/* framework Q */
if (typeof Q == "undefined" || !Q) {
	var Q = {};
}

// for scan only methods
Q.namespace("scan");

Q.scan.loadScanList = function() {
	//document.getElementById("inputVO.mode").value = "basic";
	var scanKey = document.getElementById("inputVO.scanKey").value;
	var scanDate = document.getElementById("inputVO.scanDate").value;
	var selectType = document.getElementById("inputVO.selectType").value; // define select type to D, B, W
	var period = document.getElementById("inputVO.period").value;
	var sortColumn = document.getElementById("inputVO.sortColumn").value;
	var sortOrder = document.getElementById("inputVO.sortOrder").value;
	
	Q.scan.populateBasicScan(scanKey, selectType, period, scanDate, sortColumn, sortOrder);
};


Q.scan.submitRequest = function(key) {
	//document.getElementById("inputVO.mode").value = "basic";
	document.getElementById("inputVO.scanKey").value = key;
	document.getElementById("inputVO.scanDate").value = "";
	document.getElementById("inputVO.selectType").value = "D"; // define select type to D
	document.getElementById("inputVO.sortColumn").value = "cp";
	document.getElementById("inputVO.sortOrder").value = "0";
	document.scanForm.submit();
};

// switch list/charts
Q.scan.submitRequest2 = function() {
	//document.getElementById("inputVO.mode").value = "basic";
	var type = document.getElementById("inputVO.type").value;
	if (type == "list") {
		type = "charts";
	} else {
		type = "list";
	}
	
	document.getElementById("inputVO.type").value = type;
	Q.scan.loadScanList();   // may not need to reload from DB
	QQ_basicscan.adjustButtons();

};

// when date is given.  We combine prev/next to this case
Q.scan.submitRequest3 = function(scanDate) {
	var realScanDate = "";
	if (scanDate == "prev") {
		realScanDate = QQ_basicscan.getPrevScanDate();
	} else if (scanDate == "next") {
		realScanDate = QQ_basicscan.getNextScanDate();
	} else {
		realScanDate = scanDate;
	}
	
	//alert("scan date: " + realScanDate);
	// date is given
	document.getElementById("inputVO.selectType").value = "D";
	document.getElementById("inputVO.scanDate").value = realScanDate;
	Q.scan.loadScanList(); 
	QQ_basicscan.adjustCurrentIndex();
	QQ_basicscan.adjustButtons();

	
};

// for Best/Worst calls
Q.scan.submitRequest4 = function(selectType) {
	//document.getElementById("inputVO.mode").value = "basic";
	document.getElementById("inputVO.selectType").value = selectType;
	document.getElementById("inputVO.sortColumn").value = "g";

	if (selectType == "B") {
		document.getElementById("inputVO.sortOrder").value = "1";
	} else {
		document.getElementById("inputVO.sortOrder").value = "0";
	}

	Q.scan.loadScanList(); 
	QQ_basicscan.adjustButtons();
};

// for sort purpose  -- order ? switch
Q.scan.submitRequest5 = function(sortColumn) {
	var existSortColumn = document.getElementById("inputVO.sortColumn").value;
	if (existSortColumn == sortColumn) {
		// toggle the sort order
		var sortOrder = document.getElementById("inputVO.sortOrder").value;
		if (sortOrder == "0") {
			sortOrder = "1";
		} else {
			sortOrder = "0";
		}
		
		document.getElementById("inputVO.sortOrder").value = sortOrder;
	} else {
		document.getElementById("inputVO.sortColumn").value = sortColumn;
		document.getElementById("inputVO.sortOrder").value = "0";
	}
	
	Q.scan.loadScanList(); 
	// QQ_basicscan.adjustButtons();
};

Q.scan.submitReport = function() {
	document.getElementById("inputVO.mode").value = "report";
	document.scanForm.submit();
};

Q.scan.submitReport2 = function() {
	document.getElementById("inputVO.mode").value = "report2";
	document.scanForm.submit();
};

Q.scan.setPeriod = function(period) {
	//document.getElementById("inputVO.mode").value = "basic";
	var selectType = document.getElementById("inputVO.selectType").value;
	document.getElementById("inputVO.period").value = period;

	if (selectType == "B" || selectType == "W") {
		// need to submit the form
		Q.scan.loadScanList();
		QQ_basicscan.adjustButtons();
	}
};

// Do not use same name for a form and a script function, it will not work
Q.scan.goStockAge = function() {
	var form = document.goStockAgeForm;
	form.submit();
};

var QQ_basicscan = ( function( window, undefined ) {
		var currentIndex = 0;
		var showChart = "";
		var selectType = "";   // D, B, W
		var period = "";
		var sortOrder = "0";
		var scanDate = "";
		
		function reset() {
			showChart = document.getElementById("inputVO.type").value;
			selectType = document.getElementById("inputVO.selectType").value;
			period = document.getElementById("inputVO.period").value;
		}
		
		
		function getNextScanDate() {
			var selectObj = document.getElementById("selectIt").options;
			if (currentIndex < selectObj.length - 1) {
				currentIndex += 1;
			} else {
				currentIndex = selectObj.length - 1;
			}
			
			return selectObj[currentIndex].value;
		}
		
		function getPrevScanDate() {
			var selectObj = document.getElementById("selectIt").options;
			if (currentIndex > 0) {
				currentIndex -= 1;
			} else {
				currentIndex = 0;
			}
			
			return selectObj[currentIndex].value;
		}
		
		/**
		 * find the index, when the scan date is not defined from current index.
		 * Submit event is kicked by the given date.
		 */
		function adjustCurrentIndex() {
			// date is reset, but current index needs to be adusted
			var scanDate = document.getElementById("inputVO.scanDate").value;
			var selectObj = document.getElementById("selectIt").options;

			for (var i = 0; i < selectObj.length; i++){
				//alert(selectobject.options[i].text+" "+selectobject.options[i].value)
				if (scanDate == selectObj[i].value) {
					currentIndex = i;
					break;
				}
			}
		}
		
		
		/* called very first at the page load
		 * 1) build the drop down; 2) setup the buttons */
		function addDateButtons() {
			// add options to the drop down
			var dateListStr = document.getElementById("dateListStr").value;
			if (dateListStr == null || dateListStr == "") {
				// empty
				return;
			}
			
			var dateHtml = "";
			var option  = null;
			// disable the first one, as it is selected by default
			//alert(selectobject.options[i].text+" "+selectobject.options[i].value)
			var selectObj = document.getElementById("selectIt");

			var dateArray = Q.common.mySplit(dateListStr, "#");
			for (var i = 0; i < dateArray.length; i++){
				var theDate = dateArray[i];
				option = document.createElement( 'option' );
				option.value = option.text = theDate;
			    selectObj.add( option );
				
			    if (i <= 31) {
					var shortDt = theDate.substring(3, 5);
					//if (i > 0) {
						dateHtml += "<input type='button' name='sb' value=' " + shortDt + " ' title='" + theDate + "' onClick=\"Q.scan.submitRequest3('" + theDate + "')\"> ";
					//} else {
						//dateHtml += "<input type='button' name='sb' value='" + shortDt + "' disabled> ";
					//}
			    }
				
			}

			document.getElementById("dateListStr").value = "";
			document.getElementById("dateButtons").innerHTML = dateHtml;
		}
		
		
		/** set prev/next buttons */
		function adjustButtons() {
			reset();
			
			if (period == "10") {
				//document.getElementById("inputVO.period")[0].selected;
				//$('#').val("0");
				$('input[id="inputVO.period"][value="10"]').attr('checked',true);

			} else if (period == "60") {
				//document.getElementById("inputVO.period")[2].selected;
				$('input[id="inputVO.period"][value="60"]').attr('checked',true);

			} else if (period == "120") {
				//document.getElementById("inputVO.period")[3].selected;
				$('input[id="inputVO.period"][value="120"]').attr('checked',true);

			} else {
				//document.getElementById("inputVO.period")[1].selected;
				$('input[id="inputVO.period"][value="30"]').attr('checked',true);
			}
			
			// the button value is what means, next move
			var typeBt = document.getElementById("typeButton");
			if (showChart == "list") {
				typeBt.setAttribute("value", "Charts");
			} else if (showChart == "charts") {
				typeBt.setAttribute("value", "List");
			}
			
			var selectObj = document.getElementById("selectIt").options;
			var displayDate = "";
			if (selectType == "B") {
				document.getElementById("bestButton").disabled = true;
				document.getElementById("worstButton").disabled = false;
				
				displayDate = "BEST(" + document.getElementById("inputVO.period").value + ")";
			} else if (selectType == "W") {
				document.getElementById("bestButton").disabled = false;
				document.getElementById("worstButton").disabled = true;
				
				displayDate = "WORST(" + document.getElementById("inputVO.period").value + ")";
			} else {
				document.getElementById("bestButton").disabled = false;
				document.getElementById("worstButton").disabled = false;
				
				/* safe check in case the drop down is empty */
				if (selectObj.length > currentIndex) {
					displayDate = selectObj[currentIndex].value;
					document.getElementById("inputVO.scanDate").value = displayDate;  // at the load up time, need to set this back;
				}
				
			}

			$("#displayDate").html(displayDate);

			var prevBt = document.getElementById("prevButton");
			if (currentIndex <= 0) {
				prevBt.disabled = true;
			} else {
				prevBt.disabled = false;
			}
			
			var len = selectObj.length;
			var nextBt = document.getElementById("nextButton");
			if (currentIndex >= (len - 1)) {
				nextBt.disabled = true;
			} else {
				nextBt.disabled = false;
			}

		}
		
		return {
			getNextScanDate : getNextScanDate,
			getPrevScanDate : getPrevScanDate,
			adjustButtons   : adjustButtons,
			adjustCurrentIndex : adjustCurrentIndex,
			addDateButtons : addDateButtons
		};

} )( window );

/* end of script */
