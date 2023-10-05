
/** 
 * 1) Get data and use jQuery UI to display modal Dialog box, show the add watch form;
 * 
 * 2) Submit the data and pop up confirm message
 * 
 * QQ_pageoverlay.init("overlaypage", 600, 500);
 * <div id='overlaypage'></div>
 * <include addwatchTemplate to page>
 * <include jsRender.js to page>
 * <write Ajax handler work on Server side>
 */
var QQ_addwatch = ( function( window, undefined ) {
	var savedStockId = null;
	var savedActionType = null;
	
	function getInitData(stockId, actionType) {
		var jsonData = { "data" : { "mode": "GET_INIT_DATA", "stockId": stockId }};
		savedStockId = stockId;
		savedActionType = actionType;
		
		var url = "/WEBA/s/ajax_addwatch.do";
		Q.common.jQueryAjaxCall(url, jsonData, handleGetInitDataResponse);
	}
	
	/* init data is returned from server */
	function handleGetInitDataResponse(data) {
		// alert("getinitdata returned");
		if (data != null && data.success == true) {
			
			data['actionType'] = savedActionType;
			$("#overlaypage").html($("#addwatchTemplate").render(data));
			QQ_pageoverlay.show_div();
		} else {
			alert("Technical error in server; Please try later.");
			return;  // give up
		}
	}
	
	function submitForm() {
		// submit data
		var newFolderName = "";
		var folderId = "";
		var newFolderObj = document.getElementById("newFolderName");
		if (newFolderObj != null && (newFolderObj.value == null || newFolderObj.value == "")) {
			alert("Please enter the forlder name to be created.");
			return;
		} else if (newFolderObj != null) {
			newFolderName = newFolderObj.value;
		} else {
			folderId = document.getElementById("folderId").value;
		}
		
		//alert("submitForm newFolderName: " + newFolderName);
		//alert("submitForm folderId: " + folderId + ", stockid: " + savedStockId);
		
		
		var comments = document.getElementById("comments").value;
		var tradeType = document.getElementById("tradeType").value;
		var openDate = document.getElementById("openDate").value;
		var openPrice = document.getElementById("openPrice").value;
		var closeDate = document.getElementById("closeDate").value;
		var closePrice = document.getElementById("closePrice").value;
		
		if (!Q.common.isValidDate(openDate) ||
				!Q.common.isValidDate(closeDate)) {
			return false;
		} else if (!Q.common.isValidNumber(openPrice) ||
			!Q.common.isValidNumber(closePrice)) {
			return false;
		}
		
		var jsonData = { "data" : { "mode": "SUBMIT_FORM", "stockId": savedStockId, 
			"newFolderName" : newFolderName, "folderId" : folderId,
			"comments" : comments, "tradeType" : tradeType, "openDate" : openDate,
			"openPrice" : openPrice, "closeDate" : closeDate, "closePrice" : closePrice
		}};
		
		var url = "/WEBA/s/ajax_addwatch.do";
		
		Q.common.jQueryAjaxCall(url, jsonData, handleSubmitFormResponse);
	}
	
	function handleSubmitFormResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
			QQ_pageoverlay.close_div();
		} else {
			alert("Technical error in server; Please try later.");
			QQ_pageoverlay.close_div();
			return;  // give up
		}
	}
	
	function submitFormDelete() {
		// submit data
		var folderId = document.getElementById("folderId").value;
		
		if (folderId == null || folderId == "") {
			alert("Not saved in a folder or error.");
			return;
		} 
		
		//alert("submitFormDelete folderId: " + folderId + ", stockid: " + savedStockId);
		
		var jsonData = { "data" : { "mode": "SUBMIT_FORM", "stockId": savedStockId, 
			"folderId" : folderId
		}};
		
		var url = "/WEBA/s/ajax_deletewatch.do";
		
		Q.common.jQueryAjaxCall(url, jsonData, handleSubmitFormDeleteResponse);
	}
	
	function handleSubmitFormDeleteResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
			QQ_pageoverlay.close_div();
		} else {
			alert("Technical error in server; Please try later.");
			QQ_pageoverlay.close_div();
			return;  // give up
		}
	}
	
	function setCurrentForOpen() {
		document.getElementById("openDate").value = document.getElementById("currentDate").value;
		document.getElementById("openPrice").value = document.getElementById("currentPrice").value;
	}
	
	function cleanOpen() {
		document.getElementById("openDate").value = "";
		document.getElementById("openPrice").value = "";
	}
	
	function setCurrentForClose() {
		document.getElementById("closeDate").value = document.getElementById("currentDate").value;
		document.getElementById("closePrice").value = document.getElementById("currentPrice").value;
	}
	
	function cleanClose() {
		document.getElementById("closeDate").value = "";
		document.getElementById("closePrice").value = "";
	}
	
	// explicitly return public methods when this object is instantiated
	return {
		getInitData : getInitData,
		submitForm 	: submitForm,
		setCurrentForOpen : setCurrentForOpen,
		cleanOpen : cleanOpen,
		setCurrentForClose : setCurrentForClose,
		cleanClose : cleanClose,
		submitFormDelete : submitFormDelete
	};

} )( window );

/* end */