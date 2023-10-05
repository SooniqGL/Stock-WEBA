
/** 
 * 1) get data from screen, and send ajax call
 * 2) display the result
 * 
 * 
 */
var QQ_answerinvite = ( function( window, undefined ) {
	var savedActionType = null;
	
	
	function acceptInvite(groupId, groupName, inviteBy) {
		var first = confirm("You have clicked the link to ACCEPT the invite from group: " + groupName + ".  Please click OK to continue and cancel to cancel.");
		 if (first == true) {
			 var second = confirm("Please confirm one more that you are going to ACCEPT the invite.  Please OK to submit, cancel to cancel.");
		     if (second == false) {
		    	 return false;
		     }
		 }  else {
			 return false;
		 }
		 
		 submitAnswerForInvite(groupId, "A", inviteBy);
	}
	
	function denyInvite(groupId, groupName, inviteBy) {
		var first = confirm("You have clicked the link to DENY the invite from group: " + groupName + ".  Please click OK to continue and cancel to cancel.");
		 if (first == true) {
			 var second = confirm("Please confirm one more that you are going to DENY the invite.  After your deny, we will remove the invite entry from your account, and you will not join the group.  Please OK to submit, cancel to cancel.");
		     if (second == false) {
		    	 return false;
		     }
		 } else {
			 return false;
		 }
		 
		submitAnswerForInvite(groupId, "D", inviteBy);
	}
	
	// actionType: A - accept; D - deny;
	function submitAnswerForInvite(groupId, actionType, inviteBy) {
		var jsonData = { "data" : { "mode": "answerinvite", "groupId": groupId, "actionType" : actionType, "inviteBy" : inviteBy }};
		savedActionType = actionType;
		
		var url = "/WEBA/s/ajax_answerinvite.do";
		Q.common.jQueryAjaxCall(url, jsonData, handleAnswerForInviteResponse);
	}
	
	function handleAnswerForInviteResponse(data) {

		if (data != null && data.success == true) {
			alert(data.message);
		} else {
			alert("Technical error in server; Please try again later.");
			return;  // give up
		}
	}
	
	
	// explicitly return public methods when this object is instantiated
	return {
		acceptInvite : acceptInvite,
		denyInvite 	: denyInvite
	};

} )( window );

/* end */