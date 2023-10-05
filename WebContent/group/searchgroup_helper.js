

/**
 * Do Ajax call to the list;  Keep it for sort functions to run.
 * JSRender is used to render the list.
 */
var QQ_searchgroup = ( function( window, undefined ) {
	// hold this list and so we can reuse it
	var keywordsHolder = null;
	var rowList = null;
	var pageTitle = null;
	
	function setFocus() {
		document.getElementById("keywords").select();
		document.getElementById("keywords").focus();
	}
	/**
	 * Call Ajax to get data - pageIndex is "0" in the first call
	 */
	function searchGroupListFirst() {
	
		keywordsHolder = document.getElementById("keywords").value;
		if (keywordsHolder == "") {
			// alert("When keywords field is empty in search, we retrieve a port of groups and show on the page.  To better search, you may type anything in the keywords field, we use startWith to match the group names in search.");
			// return false;
		}
		
		var jsonData = { "data" : { "keywords": keywordsHolder, "pageIndex": "0" }};
		
		var url = "/WEBA/s/ajax_searchgroup.do";

		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleGroupSearchResponse);
	}
	
	/**
	 *  after first search is made, this one will retrieve following pages 
	 *  pageIndex - is the page #.
	 **/
	function searchGroupListNext(pageIndex) {
		
		var jsonData = { "data" : { "keywords": keywordsHolder, "pageIndex": pageIndex }};
		
		var url = "/WEBA/s/ajax_searchgroup.do";

		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleGroupSearchResponse);
	}


	function handleGroupSearchResponse(data) {

		var node = document.getElementById("displayNode");
		var hasFound = false;
		var theHtml = "<table style=\"width:100%;\" class=\"gridtable\"><tr><td>";
		// alert("data: " + data.success + ", message: " + data.message + ", data: " + data);
		if (data != null && data.success == true) {
			$.each(data.indexList, function(index, el) {
				// alert("index: " + index);  
				// index -> pageIndex; 
				hasFound = true;
				var startCnt = parseInt(el.startIndex) + 1;
				var endCnt = parseInt(el.endIndex) + 1;
				theHtml += "<a href=\"#\" onclick=\"QQ_searchgroup.searchGroupListNext('" + index + "')\">" + startCnt + "-" + endCnt + "</a> ";
				
			});
		}
		
		theHtml += "</td></tr></table>";
		if (hasFound == true) {
			node.innerHTML = theHtml;
		} else {
			node.innerHTML = "<font class=\"warn_text\">Sorry, we have not found any matches for your search.  Please try again.</font>";
		}
		
	
		if (data != null && (data.success == true) && (hasFound == true)) {
			// alert("here ...." + data.rowList[0].scanType);
			// replace div's content:
			// $("#displayNode").html($("#scanListTemplate").render(data.rowList));
			// loop through and append table
			
			// keep it in the function level
			rowList = data.rowList;
			pageTitle = data.pageTitle;
			displayIt();
			
		} else {
			// reset the boxes
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
			
		});
		
		var titleJson = {};
		titleJson["pageTitle"] = pageTitle;
		
		$("#displayNode2").empty();  // reset the table
		$("#displayNode2").append($("#grouplistheaderTemplate").render(titleJson));
		$("#displayNode2").append($("#grouplistTemplate").render(rowList));
	}
	
		
	return {
		searchGroupListFirst : searchGroupListFirst,
		searchGroupListNext : searchGroupListNext,
		setFocus : setFocus
		};


} )( window );


var QQ_usergroup = ( function( window, undefined ) {
	// hold this list and so we can reuse it
	var groupIdHolder = null;
	
	

	/**
	 * Call Ajax to get data - and setup the overlay for user group.
	 * Check if the user is in the group or not; allow it to apply to join if not in group;
	 * In the future, we can allow to exit the group as well.
	 */
	function popUserGroup(groupId) {
	
		groupIdHolder = groupId;
		if (groupIdHolder == "") {
			// do not do anything, group id is not passed.
			return false;
		}
		
		var jsonData = { "data" : { "mode": "GET_INIT_DATA", "groupId" : groupId }};
		
		var url = "/WEBA/s/ajax_usergroup.do";

		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handlePopUserGroupResponse);
	}
	
	/**
	 *  Apply to join the group.
	 **/
	function applyToJoin() {
		
		var jsonData = { "data" : { "mode": "APPLY_TO_JOIN", "groupId": groupIdHolder }};
		
		var url = "/WEBA/s/ajax_usergroup.do";

		//alert("data: " + jsonData);
		Q.common.jQueryAjaxCall(url, jsonData, handleApplyToJoinResponse);
	}


	function handlePopUserGroupResponse(data) {

		if (data != null && data.success == true) {
			$("#overlaypage").html($("#usergroupTemplate").render(data));
			QQ_pageoverlay.show_div();
		} else {
			alert("Technical error in server; Please try again later.");
			return;  // give up
		}
	}
	
	
	function handleApplyToJoinResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
			QQ_pageoverlay.close_div();
		} else {
			alert("Technical error in server; Please try again later.");
			QQ_pageoverlay.close_div();
			return;  // give up
		}
	}
	
		
	return {
		popUserGroup : popUserGroup,
		applyToJoin : applyToJoin
		};


} )( window );

/* end of script */
