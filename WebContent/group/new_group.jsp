<%@ taglib prefix="s" uri="/struts-tags" %>



<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.groupForm;
		
		if (document.getElementById("inputVO.groupName").value == "") {
			alert("Group name is required.");
			return false;
		}
		
		if (document.getElementById("inputVO.publicNote").value == "") {
			alert("Public note field is required.");
			return false;
		}
		
		form.submit();
	}
	
	function setFocus() { 
		document.getElementById("inputVO.groupName").focus();
	} 
	
	window.onload=setFocus;
	
</SCRIPT>	


<s:set var="userGroupList" value="inputVO.userGroupList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

		<form name="groupForm" action="../g/group_newgroup.do"> 
			<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="newgroup" />
        	<input type="hidden" id="inputVO.type" name="inputVO.type" value="update" />
            
            
            <table width="100%" class="gridtable" style="font-size: 18px;"> 
	
				<tr><th colspan="2">&nbsp;
				<font class="sub_title">Add New Group</font></th></tr>
				<tr><td>Group Name:</td>
					<td><input type="text" id="inputVO.groupName" name="inputVO.groupName" value="" maxLength="100" class="input_area"></td>
				</tr>
				<tr><td>Public Notes (2000 max):</td>
					<td><TEXTAREA id="inputVO.publicNote" name="inputVO.publicNote" rows="10" cols="80" maxLength="2000" class="input_area"></TEXTAREA>
					</td>
				</tr>
				
				
				<tr><td colspan="2" align="center">	
					<input type="button" name="sb" value=" Create Group " onClick="submitRequest()" class="long_button">
				</td>
				</tr> 
				<tr><td>Some Notes: </td><td><font class="instruction_text">
				<ul><li>When you create a group, you will be automatically assigned as the owner of the group.  You are 
				the only person who have permission to edit group's name, public note, and accepting.</li>
				<li>Currently, only owner can accept other users to join the group.  In the future, we may allow owner to assign the rights to some other
				users in the group.  This will then help the owner to manage the group.</li>
				<li>Public note - will be show to public, to tell other users about the group.</li>
				<li>Accepting - to tell if the group is still accepting request or not.  Only if a group is accepting requests, other users
				can apply to join the group.</li>
				<li>Please use only letters, numbers, space, dash, and underscore when create group name.
				</ul>
				</font>
				</td></tr>
		</table></form>
        </div>
      </div>
            