/* framework Q */
if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

// for scan only methods
Q.namespace("scan");

/*
 * The .getJSON() may not always fired, so need to use post
 * */
Q.scan.populateBasicScan = function (scanKey, selectType, period, scanDate, sortColumn, sortOrder) {
	/*
	var jsonData = { "dataList" : [ scanKey, selectType, scanDate, period ],
	
			"dataMap" : { "scanKey": scanKey, "selectType": selectType, "scanDate" : scanDate, "period" : period }};
	var jsonData = { "scanKey": scanKey, "selectType": selectType, "scanDate" : scanDate, "period" : period};
	}; */
	
	/*
	var jsonData = {"data" : {}};
	jsonData.dataMap["scanKey"] = scanKey;
	jsonData.dataMap["selectType"] = selectType;
	*/
	var jsonData = { "data" : { "scanKey": scanKey, "selectType": selectType, "scanDate" : scanDate, "period" : period,
		"sortColumn" : sortColumn, "sortOrder" : sortOrder }};
	
	var url = "/WEBA/s/ajax_basicscan.do";
	
	Q.common.jQueryAjaxCall(url, jsonData, Q.scan.handleBasicScanResponse);
	resetTimer();  // renew the timer for client ajax call
};


Q.scan.handleBasicScanResponse = function (data) {
	/*
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
	/*
        $('body').append($('<div>', {
            text: element.name
        })); */
	
	var showChart = document.getElementById("inputVO.type").value;   /* get from field */
	var selectType = document.getElementById("inputVO.selectType").value;
	
	var colspan = "8";
	if (selectType != "D") {
		colspan = "9";
	}

	if (data != null && data.success == true) {
		// alert("here ...." + data.rowList[0].scanType);
		// $("#scanListTemplate").render(data.rowList).appendTo("#displayNode"); -- not working
		
		// replace div's content: $("#displayNode").html($("#scanListTemplate").render(data.rowList));
		// loop through and append table
		
		var rowList = data.rowList;
		$.each(rowList, function(index, el) {
			// alert("index: " + index);
			el["showChart"] = showChart;
			el["colspan"] = colspan;
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
		titleJson["colspan"] = colspan;
		
		$("#displayNode").empty();  // reset the table
		$("#displayNode").append($("#scanTitleTemplate").render(titleJson));
		$("#displayNode").append($("#scanListTemplate").render(rowList));
	}
};