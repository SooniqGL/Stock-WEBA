/* framework Q */
if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

// for scan only methods
Q.namespace("agesrch");


Q.agesrch.loadAgeList = function(selectRange) {

	if (selectRange != null && selectRange != "") {
		// save to form for late use, over write the one in form
		document.getElementById("inputVO.selectRange").value = selectRange;
	} else {
		// not set, then get from the form
		selectRange = document.getElementById("inputVO.selectRange").value;
		if (selectRange == "") {
			// use default - it is used on first time page load
			document.getElementById("inputVO.selectRange").value = "1";
			selectRange = "1";
		}
	}
	
	// update the option for display purpose
	Q.agesrch.updateOption();
	
	//alert("called 2 : " + selectRange);
	
	// Ajax and load it
	QQ_agesrch.populateAgesrch("default", selectRange);
};

// set the check box at the beginning, if existing
Q.agesrch.updateOption = function() {
    var checkBoxObj = document.getElementById("showAllCheck");
    if (checkBoxObj != null && checkBoxObj.checked == true) {
        document.getElementById("inputVO.showAllCharts").value = "Y";
    } else {
    	document.getElementById("inputVO.showAllCharts").value = "N";
    }
};

/**
 * Do Ajax call to the list;  Keep it for sort functions to run.
 * JSRender is used to render the list.
 */
var QQ_agesrch = ( function( window, undefined ) {
	// hold this list and so we can reuse it
	var rowList = null;
	
	// tracking what has been sorted; if asked again, sort to another order
	var currentSortColumn = null;
	var sortOrder  = 1;
	
	/**
	 * Call Ajax to get data
	 */
	function populateAgesrch(ageType, selectRange) {
	
		var jsonData = { "data" : { "ageType": ageType, "selectRange": selectRange }};
		
		var url = "/WEBA/s/ajax_agesrchresult.do";
		
		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleAgesrchResponse);
	}


	function handleAgesrchResponse(data) {
		/**
		var node = document.getElementById("displayNode");
		var theHtml = "<table>";
		// alert("data: " + data.success + ", message: " + data.message + ", data: " + data);
		if (data != null && data.success == true) {
			// alert("here ...");
			$.each(data.rowList, function(index, el) {
				// alert("index: " + index);
				var cnt = index + 1;
				theHtml += "<tr><td>" + cnt +"</td><td>buttons</td><td>" + el.scanType + "</td><td>";
				theHtml += el.companyName + "</td><td>" + el.scanPrice + "</td><td>" + el.currentPrice;
				theHtml += "</td><td>" + el.gain + "</td></tr>";
				
			});
		}
		
		theHtml += "</table>";
		node.innerHTML = theHtml;
		*/
	
		if (data != null && data.success == true) {
			// alert("here ...." + data.rowList[0].scanType);
			// replace div's content:
			// $("#displayNode").html($("#scanListTemplate").render(data.rowList));
			// loop through and append table
			
			// keep it in the function level
			rowList = data.rowList;
			displayIt();
			
		}
	}
	
	function displayIt() {
		var showChart = document.getElementById("inputVO.showAllCharts").value;   
		var selectRange = document.getElementById("inputVO.selectRange").value;
		
		$.each(rowList, function(index, el) {
			// alert("index: " + index);
			el["showChart"] = showChart;

			if (index % 2 == 0) {
				el["rowColor"] = "#ffffff";
			} else {
				el["rowColor"] = "#ffffdf";
			}
			
			if (el["gain"] >= 0) {
				el["gainColor"] = "#009955";
			} else {
				el["gainColor"] = "#ff0000";
			}
			
		});
		
		var titleJson = {};
		titleJson["showChart"]   = showChart;
		titleJson["selectRange"] = selectRange;
		
		$("#displayNode").empty();  // reset the table
		$("#displayNode").append($("#agesrchTitleTemplate").render(titleJson));
		$("#displayNode").append($("#agesrchListTemplate").render(rowList));
	}
	
	// sort by: cross price, current price, gain; after sort, call to display
	// if sort is called on the same column, then flip the order.
	function sortIt(sortColumn) {
		if (currentSortColumn == sortColumn) {
			sortOrder = -1 * sortOrder;
		} else {
			sortOrder = 1;
		}
		
		currentSortColumn = sortColumn;
		if (sortColumn == "cross") {
			rowList.sort(function(a, b) {
			    return (a.ptPrice - b.ptPrice) * sortOrder;
			});
		} else if (sortColumn == "current") {
			rowList.sort(function(a, b) {
			    return (a.currPrice - b.currPrice) * sortOrder;
			});
		} else if (sortColumn == "ticker") {
			rowList.sort(function(a, b) {
				var res = 1;
				if (a.ticker < b.ticker) {
					res = -1;
				} 
				
			    return res * sortOrder;
			});
		} else if (sortColumn == "company") {
			rowList.sort(function(a, b) {
				var res = 1;
				if (a.company < b.company) {
					res = -1;
				} 
				
			    return res * sortOrder;
			});
		} else {  // sort by gain column
			rowList.sort(function(a, b) {
			    return (a.gain - b.gain) * sortOrder;
			});
		}
		
		displayIt();
		
	}
		
	return {
			populateAgesrch : populateAgesrch,
			sortIt : sortIt
		};


} )( window );

/* end of script */
