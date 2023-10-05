/* 
 * newmsg_helper.js - used by new_message.jsp
 */

/* framework Q */
if (typeof Q == "undefined" || !Q) {
	var Q = {};
}

// for scan only methods
Q.namespace("message");



Q.message.submitRequest = function(key) {
	var subj = document.getElementById("inputVO.msgSubject").value;
	if (subj == "") {
		alert("Subject is required.");
		return false;
	}
	
	
	document.messageForm.submit();
};



/* end of script */