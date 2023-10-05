<%@ taglib prefix="s" uri="/struts-tags" %>


<style>
<!--
/*  SECTIONS  */
.groupuserlistsection {
	clear: both;
	/* padding: 0px; */
	margin-top: 10px; 
	width: 99%;
	/*background: #ffffef; */
	border: 1px solid #ffffef;
}

#groupusernotes {
	width: 95%;
	margin: 30px 0 10px 20px;
}

.valueField {
	COLOR: #0099cc; FONT-FAMILY:Verdana, Arial, Helvetica, sans-serif; FONT-SIZE:12pt; FONT-WEIGHT:bold;
}
-->
</style>

<script type="text/javascript">

$( document ).ready(function() {

	// this is ugly, as the dialog is running different in IE
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
    var trident = ua.indexOf('Trident/');

    if (msie > 0 || trident > 0) {
		QQ_pageoverlay.init("overlaypage", 700, 330);
    } else {
    	QQ_pageoverlay.init("overlaypage", 700, 350);
    }
    
    // load the list
    var groupId = document.getElementById("groupId").value;
    QQ_groupuserlist.getGroupUserList(groupId);
	
});

</script>

<%-- 	
<s:set var="userGroupList" value="inputVO.userGroupList" />
<s:set var="userRequestList" value="inputVO.userRequestList" />
--%>

<div id="overlaypage" title="Manage Group Info.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper" style="min-height:30px;"><div style="margin:2px;padding:2px;background:#ffffff;">
            
            <h3>Group Info:</h3>
            
            <div id="displayNode" class="groupuserlistsection group">
	    		
	    	</div>
	    
            
            <table id="displayNode2" style="width:100%;" class="gridtable">
		
			</table>
			       
            <div id="groupusernotes" class="list_text">
            <h3>Notes</h3>
            <ul>
            <li>These are all the users in the given group that you have joined.  If you are the owner of the group, you will be presented with
            an Edit link to update users.  The owner can change user's group status from this page.</li>
            <li>[View Profile] - not available currently.  Will be available in future releases.</li>
            </ul>
            </div>
                
       </div>
   </div>
 
<form name="groupuserlistForm">
<input type="hidden" id="groupId" value="<s:property value='%{inputVO.groupId}'/>"/>
</form>    
       
<jsp:include page="../template/group/groupuserlist_template.jsr"></jsp:include>
<jsp:include page="../template/group/groupuserlisthd_template.jsr"></jsp:include>
<jsp:include page="../template/group/groupinfo_template.jsr"></jsp:include>
<jsp:include page="../template/group/updateuserbyowner_template.jsr"></jsp:include>