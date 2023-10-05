<%@ taglib prefix="s" uri="/struts-tags" %>


<style>
<!--
#grouprequestnotes {
	width: 95%;
	margin: 30px 0 10px 20px;
	background: #ffffff;
}

.valueField {
	COLOR: #0099cc; FONT-FAMILY:Verdana, Arial, Helvetica, sans-serif; FONT-SIZE:12pt; FONT-WEIGHT:bold;
}
-->
</style>

<SCRIPT SRC="/WEBA/group/grouprequestlist_helper.js" type="text/javascript"></SCRIPT>


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
    //var groupId = document.getElementById("groupId").value;
    //QQ_groupuserlist.getGroupUserList(groupId);
	
});

</script>

	
<s:set var="groupRequestList" value="inputVO.groupRequestList" />


<div id="overlaypage" title="Manage Group Request.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper" style="min-height:30px;"><div style="margin:2px;">
            
			<table style="width:100%;" class="gridtable">
			<tr><td colspan="5"><span class="sub_title">Group list with OPEN requests (you are the owner):</span></td></tr>
			<tr><th align="center">Order</th>
			<th align="center">Group Name</th>
			<th align="center">Group ID</th>
			<th align="center">Number Requests</th>
			<th align="center">Edit</th></tr>
			<s:iterator var="itemVO" value="#groupRequestList" status="rowStatus">
				<tr>
				<td align="center"><s:property value="%{#rowStatus.index + 1}"/></td>
				<td align="center"><s:property value="%{#itemVO.groupName}" escapeHtml="false" /></td>
				<td align="center"><s:property value="%{#itemVO.groupId}"/></td>
				<td align="center"><s:property value="%{#itemVO.numUsers}"/></td>
				<td align="center">
				<A href="#" onclick="QQ_grouprequestlist.getGroupRequestList('<s:property value="%{#itemVO.groupId}"/>');" class="datalink">Edit</a>
				</td>
				</tr>
			</s:iterator>
			<s:if test="%{#groupRequestList == NULL || #groupRequestList.isEmpty()}">
				<tr><td colspan="5"><span class="list_text">Currently there are no outstanding requests for the groups you own.</span></td></tr>
			</s:if>
			<s:else>
			<tr><td colspan="5"><span class="list_text">This lists only the groups that you are the owner, and there are outstanding requests.  
			You may accept and deny the requests.</span>
            </td></tr>
		    </s:else>
		    </table>
    

            
			       
                
       </div>
   </div>
   
   
   <div id="displayNode"></div>
 
 <%-- check this group id, if it is on the list of the owner-group-list, then hidden here; o.w. null it. --%>
<form name="grouprequestlistForm">
<input type="hidden" id="groupId" value="<s:property value='%{inputVO.groupId}'/>"/>
</form>    
       
<jsp:include page="../template/group/grouprequestlist_template.jsr"></jsp:include>

<%-- end --%>