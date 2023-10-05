

/**
 * Do Ajax call to the list;  Keep it for sort functions to run.
 * JSRender is used to render the list.
 */
var QQ_grouprequestlist = ( function( window, undefined ) {
	// hold this list and so we can reuse it
	var groupIdHolder = null;
	var rowList = null;
	var userIdHolder = null;
	var groupName = null;
	var allCheckedFlag = false;
	
	/**
	 * Call Ajax to get group request list, as group id is given.
	 */
	function getGroupRequestList(groupId) {

		groupIdHolder = groupId;
		allCheckedFlag = false;
		if (groupIdHolder == "") {
			return false;
		}
		
		var jsonData = { "data" : { "mode": "grouprequestlist", "groupId": groupId }};
		
		var url = "/WEBA/s/ajax_grouprequestlist.do";

		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleGroupRequestListResponse);
	}
	

	function handleGroupRequestListResponse(data) {

		if (data != null && data.success == true) {
			// alert("here ...." + data.rowList[0].scanType);
	
			// keep it in the function level
			groupName = data.groupName;
			rowList = data.rowList;
			
			if (rowList != null && rowList.length > 0) {
				displayIt();
			} else {
				// nothing is found in DB
				// alert("No entry is found.  You may need to refresh the page.");
				$("#displayNode").empty();  // reset the table
			}
			
			
		} else {
			// reset the boxes
			$("#displayNode").empty();  // reset the table
		}
	}
	
	function displayIt() {

		// alert("display it");
		$.each(rowList, function(index, el) {
			if (index % 2 == 0) {
				el["rowColor"] = "#ffffff";
			} else {
				el["rowColor"] = "#ffffdf";
			}
			
		});
		
		var newData = {};
		newData["pageTitle"] = "Group Request List for: " + groupName;
		newData["rowList"] = rowList;
		
		$("#displayNode").empty();  // reset the table
		$("#displayNode").append($("#grouprequestlistTemplate").render(newData));

	}
	
	/** switch all check boxes in one shot */
	function doallCheckboxes() {
		//alert("here ...");
		$.each(rowList, function(index, el) {
			var chkID = "CK_" + el["requestId"];
			var chkObj = $("#" + chkID);
			
			if (allCheckedFlag == true) {
				chkObj.prop("checked", false); 
			} else {
				chkObj.prop("checked", true); 
			}	
		});
		
		// switch the flag
		allCheckedFlag = !allCheckedFlag;
	}
	
	function collectCheckedList() {
		var userIdList = "";
		$.each(rowList, function(index, el) {
			var chkID = "CK_" + el["requestId"];
			
			var chkObj = $("#" + chkID);
			if (chkObj.is(":checked")) {
				if (userIdList != "") {
					userIdList += ":";
				}
				
				userIdList += el["requestId"];
			} 
			
		});
		
		return userIdList;
	}
	
	/** approve all the users that are selected */
	function approveAllSelected() {
		var userIdList = collectCheckedList();
		
		if (userIdList == "") {
			alert("Nothing is selected.  Please select some requests by checking the checkbox.");
			return;
		}
		
		processRequests(userIdList, "A");
	}
	
	function denyAllSelected() {
		var userIdList = collectCheckedList();
		
		if (userIdList == "") {
			alert("Nothing is selected.  Please select some requests by checking the checkbox.");
			return;
		}
		
		processRequests(userIdList, "D");
	}
	

	// A - approve; D - deny;
	function processRequests(userIdList, statusFlag) {
		
		var jsonData = { "data" : { "mode" : "processrequestsbyowner",
			"userIdList" : userIdList, "groupId" : groupIdHolder, "statusFlag" : statusFlag }};
		
		var url = "/WEBA/s/ajax_processrequestsbyowner.do";

		//alert("data: " + userIdList + ", " + groupIdHolder + ", " + statusFlag);
		Q.common.jQueryAjaxCall(url, jsonData, handleProcessRequestsByownerResponse);
	}
	
	function handleProcessRequestsByownerResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
			
			// !!! reload the parent page -- as the status is changed for some user
			getGroupRequestList(groupIdHolder);

		} else {
			alert("Technical error in server; Please try again later.");
			return;  // give up
		}
	}
		
	return {
		getGroupRequestList : getGroupRequestList,
		doallCheckboxes : doallCheckboxes,
		approveAllSelected : approveAllSelected,
		denyAllSelected : denyAllSelected
	};


} )( window );



/* end of script */
