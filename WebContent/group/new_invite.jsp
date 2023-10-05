<%@ taglib prefix="s" uri="/struts-tags" %>



<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.newInviteForm;
		
		if (document.getElementById("inputVO.inviteId").value == "") {
			alert("Invite ID is required.");
			return false;
		}
		
		
		form.submit();
	}
	
	function setFocus() { 
		document.getElementById("inputVO.inviteId").focus();
	} 
	
	window.onload=setFocus;
	
</SCRIPT>	


<s:set var="userGroupList" value="inputVO.userGroupList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

		<form name="newInviteForm" action="../g/group_newinvite.do"> 
			<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="newinvite" />
        	<input type="hidden" id="inputVO.type" name="inputVO.type" value="update" />
            <input type="hidden" id="inputVO.groupId" name="inputVO.groupId" value="<s:property value='%{inputVO.groupId}'/>" />
            
            <table width="100%" class="gridtable" style="font-size: 18px;"> 
	
				<tr><th colspan="2">&nbsp;
				<font class="sub_title">Invite Friend To Join Group</font></th></tr>
				
				<s:if test="%{!inputVO.success && inputVO.message != NULL && inputVO.message != \"\"}">
					<TR><td colspan="2">
					<font color="#ff0000">Error: <s:property value='%{inputVO.message}'/></font></td>		
					</TR>
				</s:if>
				
				<s:if test="%{inputVO.success && inputVO.message != NULL && inputVO.message != \"\"}">
					<TR><td colspan="2">
					<font color="#00ff00">Success: <s:property value='%{inputVO.message}'/></font></td>		
					</TR>
				</s:if>
		
				<tr><td width="25%">Group Name:</td>
				    <td><s:property value='%{inputVO.groupName}' escapeHtml="false" /></td>
				    </tr>
				<tr><td>User ID:</td>
					<td><input type="text" id="inputVO.inviteId" name="inputVO.inviteId" value="" autocomplete="off" size="16" maxLength="16" class="input_area"></td>
				</tr>
				

				<tr><td colspan="2" align="center">	
					<input type="button" name="sb" value=" Invite Friend " onClick="submitRequest()" class="long_button">
				</td>
				</tr> 
				<tr><td>Some Notes: </td><td><font class="instruction_text">
				<ul><li>You may invite anyone outside of your group to the group you are in.  You need to know the friend's User ID to invite.</li>
				<li>For privacy reason, we will not allow to search any our database.  Only if you know the User ID, you can invite. </li>
				<li>After you submit, we will put a notice to the person you have invited.  If the person accepts the invite, he/she will
				be added to the group.  He/she may deny the invite.</li>
				<li>We will only ask the User ID, and we will not release any information about the person to you either.  However, we will
				need to release the group information, and your name to the person you invite.</li>
				</ul>
				</font>
				</td></tr>
		</table></form>
        </div>
      </div>
            