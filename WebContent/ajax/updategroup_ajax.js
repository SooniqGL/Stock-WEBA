
/** 
 * 1) Get data and use jQuery UI to display modal Dialog box, show the add watch form;
 * 
 * 2) Submit the data and pop up confirm message
 * 
 * QQ_pageoverlay.init("addwatchoverlay", 600, 500);
 * <div id='addwatchoverlay'><div id="displayNode"></div></div>
 * <include addwatchTemplate to page>
 * <include jsRender.js to page>
 * <write Ajax handler work on Server side>
 */
var QQ_updategroup = ( function( window, undefined ) {
	var savedGroupId = null;
	/* var savedActionType = null; */
	
	function getInitData(groupId, actionType) {
		var jsonData = { "data" : { "mode": "GET_INIT_DATA", "groupId": groupId }};
		savedGroupId = groupId;
		//savedActionType = actionType;
		
		var url = "/WEBA/s/ajax_updategroup.do";
		Q.common.jQueryAjaxCall(url, jsonData, handleGetInitDataResponse);
	}
	
	/* init data is returned from server */
	function handleGetInitDataResponse(data) {
		// alert("getinitdata returned");
		if (data != null && data.success == true) {
			
			//data['actionType'] = savedActionType;
			$("#overlaypage").html($("#updategroupTemplate").render(data));
			QQ_pageoverlay.show_div();
		} else {
			alert("Technical error in server; Please try again later.");
			return;  // give up
		}
	}
	
	function submitForm() {
		// submit data
		var accepting = document.getElementById("accepting").value;
		var groupName = document.getElementById("groupName").value;
		var publicNote = document.getElementById("publicNote").value;
		
		
		if (groupName == "") {
			alert("Group Name cannot be empty.");
			return false;
		} else if (publicNote == "") {
			alert("Public Note cannot be empty.");
			return false;
		}
		
		var jsonData = { "data" : { "mode": "SUBMIT_FORM", "groupId": savedGroupId, 
			"accepting" : accepting, "groupName" : groupName,
			"publicNote" : publicNote
		}};
		
		var url = "/WEBA/s/ajax_updategroup.do";
		
		Q.common.jQueryAjaxCall(url, jsonData, handleSubmitFormResponse);
	}
	
	function handleSubmitFormResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
			QQ_pageoverlay.close_div();
		} else {
			alert("Technical error in server; Please try again later.");
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
		cleanClose : cleanClose
	};

} )( window );

/* end */