

/**
 * Do Ajax call to the list;  Keep it for sort functions to run.
 * JSRender is used to render the list.
 */
var QQ_groupuserlist = ( function( window, undefined ) {
	// hold this list and so we can reuse it
	var groupIdHolder = null;
	var rowList = null;
	var isOwner = null;
	var userIdHolder = null;
	
	/**
	 * Call Ajax to get group list, as group id is given.
	 */
	function getGroupUserList(groupId) {

		groupIdHolder = groupId;
		if (groupIdHolder == "") {
			// alert("Group id is null.");
			// return false;
		}
		
		var jsonData = { "data" : { "mode": "groupuserlist", "groupId": groupId }};
		
		var url = "/WEBA/s/ajax_groupuserlist.do";

		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleGroupUserListResponse);
	}
	

	function handleGroupUserListResponse(data) {

		if (data != null && data.success == true) {
			// alert("here ...." + data.rowList[0].scanType);
			// replace div's content:
			$("#displayNode").html($("#groupinfoTemplate").render(data));
			// loop through and append table
			
			// keep it in the function level
			rowList = data.rowList;
			isOwner = data.isOwner;
			displayIt();
			
		} else {
			// reset the boxes
			$("#displayNode").html("Error in query.");
			$("#displayNode2").empty();  // reset the table
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
			
			el["isOwner"] = isOwner;
			
		});
		
		var titleJson = {};
		titleJson["pageTitle"] = "Group User List";
		
		$("#displayNode2").empty();  // reset the table
		$("#displayNode2").append($("#groupuserlistheaderTemplate").render(titleJson));
		$("#displayNode2").append($("#groupuserlistTemplate").render(rowList));
	}
	
	
	function popUpdateUserStatus(userId) {
		var userElem = null;
		
		// hold it
		userIdHolder = userId;
		
		/** find the information from the cache */
		$.each(rowList, function(index, el) {
			if (el["userId"] == userId) {
				userElem = el;
			}
		});
		
		if (userElem != null) {
			$("#overlaypage").html($("#updateuserTemplate").render(userElem));
			QQ_pageoverlay.show_div();
		} else {
			alert("System Error.  Please try again later.");
		}
	}
	
	function changeUserGroupStatus() {
		var userGroupStatus = document.getElementById("userGroupStatus").value;
		
		var jsonData = { "data" : { "mode" : "updateuserbyowner",
			"groupId" : groupIdHolder, "userId" : userIdHolder, "userGroupStatus" : userGroupStatus }};
		
		var url = "/WEBA/s/ajax_updateuserbyowner.do";

		// alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleUpdateUserStatusResponse);
	}
	
	function handleUpdateUserStatusResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
			
			// !!! reload the parent page -- as the status is changed for one user
			getGroupUserList(groupIdHolder);
			
			QQ_pageoverlay.close_div();
		} else {
			alert("Technical error in server; Please try again later.");
			QQ_pageoverlay.close_div();
			return;  // give up
		}
	}
		
	return {
		getGroupUserList : getGroupUserList,
		popUpdateUserStatus : popUpdateUserStatus,
		changeUserGroupStatus : changeUserGroupStatus
		};


} )( window );



/* end of script */
