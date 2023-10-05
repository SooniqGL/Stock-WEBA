<%@ taglib prefix="s" uri="/struts-tags" %>

<style>
<!--

.notes {
	width: 95%;
	margin: 20px 0 10px 20px;
}

-->
</style>

<SCRIPT SRC="/WEBA/ajax/updategroup_ajax.js" type="text/javascript"></SCRIPT>
<SCRIPT SRC="/WEBA/ajax/answerinvite_ajax.js" type="text/javascript"></SCRIPT>

<script type="text/javascript">

$( document ).ready(function() {

	// this is ugly, as the dialog is running different in IE
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
    var trident = ua.indexOf('Trident/');

    if (msie > 0 || trident > 0) {
		QQ_pageoverlay.init("overlaypage", 700, 400);
    } else {
    	QQ_pageoverlay.init("overlaypage", 700, 435);
    }
	
});

</script>
	
<s:set var="userGroupList" value="inputVO.userGroupList" />
<s:set var="userRequestList" value="inputVO.userRequestList" />
<s:set var="userInviteList" value="inputVO.userInviteList" />


<div id="overlaypage" title="Manage Group Info.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper" style="min-height:30px;"><div style="margin:2px;background:#dedede;">
                    
            <table width="100%" class="gridtable" >
		<tr><td width="200">&nbsp;&nbsp;<b>My Group List</b></td>
			<td style="padding:0px"><table style="width:100%;margin:0px;" class="gridtable_noborder">
			    <%-- 
				<tr><td colspan="2"><a href="../g/group_newgroup.do?inputVO.mode=newgroup&inputVO.type=blank" class="toplink">Create New Group</a></td></tr>
				--%>
				
				<tr><th width="15%" align="left">Group ID</th>
				    <th width="30%" align="left">Group Name</th>
				    <th width="15%" align="left">Num Users</th>
					<th width="20%" align="left">View/Invite</th>
					<th align="left">Owner</th>
				</tr>
				<s:iterator var="itemVO" value="#userGroupList" status="rowStatus">
				<tr><td>
					<s:property value='%{#itemVO.groupId}'/>
					</td>
					<td>
					<s:property value='%{#itemVO.groupName}'  escapeHtml="false" />
					</td>
					<td>
					<s:property value='%{#itemVO.numUsers}'/>
					</td>
					<td>
					<A href="../g/group_groupuserlist.do?inputVO.mode=groupuserlist&inputVO.groupId=<s:property value='%{#itemVO.groupId}'/>" title="See all the users in the group" class="datalink">Users</a>
					<s:if test="%{#itemVO.accepting == \"Y\"}">
					<A href="../g/group_newinvite.do?inputVO.mode=newinvite&inputVO.groupId=<s:property value='%{#itemVO.groupId}'/>" title="Invite someone to the group" class="datalink">Invite</a>
					</s:if>
					
					</td>
					
					<td>
					<s:if test="%{inputVO.userId == #itemVO.ownerId}">
						<A href="#" onclick="QQ_updategroup.getInitData('<s:property value="%{#itemVO.groupId}"/>', 'Update');" title="As owner, you may update the group properties" class="datalink">Update</a>
                    
                    	<A href="../g/group_grouprequestlist.do?inputVO.mode=grouprequestlist&inputVO.groupId=<s:property value='%{#itemVO.groupId}'/>" title="As owner, you manage the requests to join the group" class="datalink">Requests</a>
                    
                    </s:if>
					<s:else>
						Owner: <s:property value='%{#itemVO.ownerFname}'/>
					</s:else>
					
					</td>
				</tr>
				</s:iterator>
				<s:if test="%{#userGroupList == NULL || #userGroupList.isEmpty()}">
					<tr><td colspan="3">Currently you have not joined any groups.</td></tr>
			    </s:if>
				</table>
		</td></tr>
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td width="200">&nbsp;&nbsp;<b>My Request List</b></td>
			<td style="padding:0px"><table style="width:100%;margin:0px;" class="gridtable_noborder">
			    <%-- 
				<tr><td colspan="2"><a href="../g/group_newgroup.do?inputVO.mode=newgroup&inputVO.type=blank" class="toplink">Create New Group</a></td></tr>
				--%>
				
				<tr><th width="20%" align="left">Group ID</th>
				    <th width="20%" align="left">Group Name</th>
					<th width="20%" align="left">Request Date</th>
					<th width="20%" align="left">Status</th>
					<th align="left">Response Date</th>
				</tr>
				<s:iterator var="itemVO" value="#userRequestList" status="rowStatus">
				<tr><td>
					<s:property value='%{#itemVO.groupId}'/>
					</td>
					<td>
					<s:property value='%{#itemVO.groupName}' escapeHtml="false" />
					</td>
					<td>
					<s:property value='%{#itemVO.requestDt}'/>
					</td>
					<td>
					<s:property value='%{#itemVO.status}'/>
					</td>
					<td>
					<s:property value='%{#itemVO.responseDt}'/>
					</td>
				</tr>
				</s:iterator>
				<s:if test="%{#userRequestList == NULL || #userRequestList.isEmpty()}">
					<tr><td colspan="5">Currently you do not have any open requests.</td></tr>
			    </s:if>
				</table>
		</td></tr>
		
		
		<tr><td colspan="2">&nbsp;</td></tr>
		<tr><td width="200">&nbsp;&nbsp;<b>I Am Invited</b></td>
			<td style="padding:0px"><table style="width:100%;margin:0px;" class="gridtable_noborder">
			    <%-- 
				<tr><td colspan="2"><a href="../g/group_newgroup.do?inputVO.mode=newgroup&inputVO.type=blank" class="toplink">Create New Group</a></td></tr>
				--%>
				
				<tr><th width="20%" align="left">Group ID</th>
				    <th width="20%" align="left">Group Name</th>
					<th width="20%" align="left">Invite Date</th>
					<th width="20%" align="left">Invite By</th>
					<th width="20%" align="left">Actions</th>
				</tr>
				<s:iterator var="itemVO" value="#userInviteList" status="rowStatus">
				<tr><td>
					<s:property value='%{#itemVO.groupId}'/>
					</td>
					<td>
					<s:property value='%{#itemVO.groupName}' escapeHtml="false" />
					</td>
					<td>
					<s:property value='%{#itemVO.inviteDt}'/>
					</td>
					<td>
					<s:property value='%{#itemVO.byFname}'/> <s:property value='%{#itemVO.byLname}'/>
					</td>
					<td><a href="#" onclick='QQ_answerinvite.acceptInvite("<s:property value='%{#itemVO.groupId}'/>", "<s:property value='%{#itemVO.groupName}'/>", "<s:property value='%{#itemVO.inviteBy}'/>")' class="datalink">Accept</a> 
					<a href="#" onclick='QQ_answerinvite.denyInvite("<s:property value='%{#itemVO.groupId}'/>", "<s:property value='%{#itemVO.groupName}'/>", "<s:property value='%{#itemVO.inviteBy}'/>")' class="datalink">Deny</a> 
					</td>
				</tr>
				</s:iterator>
				<s:if test="%{#userInviteList == NULL || #userInviteList.isEmpty()}">
					<tr><td colspan="5">Currently you are not invited to join any group by anyone.</td></tr>
			    </s:if>
				</table>
		</td></tr>
  

		<tr><td colspan="2">
		
		
		<div class="notes list_text">
            <h3>Notes</h3>
            <ol>
            <li>The "My Group List" here list all the groups that you have already joined.  You can click the "View Users" link to see 
            all the users with this group.</li>
            <li>If you are the owner of the group, you will see a link "Update".  Click that link, you can update the group's key information
            in an overlay window.  This includes: change group name, public note, and accepting flag.  Be aware, if "accepting" is set to 
            NO, then no one will be able to join this group anymore.  If you change the group name, you may need to reload the page to see
            the changes.</li>
            <li>The "My Request List" contains the groups that you have applied to join, but not get approved by the owners yet.</li>
            <li>For requests, statuses are: O - Open, A - Approved, D - Denied.  We will remove the records with "A and D" status from your accounts
            after 7 days in that status.</li>
            <li>The "I Am Invited" list contains the invites that someone has sent to you to invite you to join a group.  You have the 
            right to accept or deny.  If you accept, you will be added to that group.  If you deny, we will not add you to that group.
            After you either accept or deny, the records will not show in your group home page.</li>
            
            </ol>
        </div> 
        </td></tr>
            </table>
                
                </div>
            </div>
            
<jsp:include page="../template/group/updategroup_template.jsr"></jsp:include>