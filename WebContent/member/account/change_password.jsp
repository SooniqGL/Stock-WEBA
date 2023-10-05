<%@ taglib prefix="s" uri="/struts-tags" %>
 

<SCRIPT language="javascript">    
  	  
	
	function submitRequest() {
		var form = document.myinfoForm;
		document.getElementById("inputVO.type").value = "update";
		form.submit();
	}
	
	function validate() {
		var form = document.myinfoForm;
		
		var warning = "";
		if (document.getElementById("inputVO.password").value == "") {
			warning = "Old Password";
		}
		
		if (document.getElementById("inputVO.newPassword").value == "") {
			if (warning != "") {
				warning += ", ";
			}
			
			warning += "New Password";
		}
		
		if (document.getElementById("inputVO.newPassword").value != form.rePassword.value) {
			alert("Reenter password is different from the password.  Please correct.");
			return false;
		}
		
		return true;
	
	}
	
	function setFocus() {
		document.getElementById("inputVO.password").focus();
	}
	
	// set focus
	window.onload=setFocus;
</SCRIPT>	



<div class="contentWrapper">
<div style="margin:0 0 0 0; width:100%; background:#ffffef">
    
    <%-- here: inputVO.mode could be: changepassword or changepassword2; so do not hard code it. --%>
    <form name="myinfoForm" action="../m/profile_changepassword.do"> 	
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="<s:property value='%{inputVO.mode}'/>" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="update" />
       	                
            
    <table width="100%" class="gridtable">
	
	    <tr><td height="40" align="center" colspan="2"><font class="sub_title">Change My Password</font></td></tr>
		
			<s:if test="%{inputVO.message != NULL && inputVO.message != \"\"}">
				<TR><td colspan="2">
				<font color="#ff0000">Error: <s:property value='%{inputVO.message}'/></font></td>		
				</TR>
			</s:if>
			
				<tr> 
					<th width="50%" align="right">*Old Password</th>
					<td width="50%"> 
					  <input type="password" id="inputVO.password" name="inputVO.password" size="16" maxlength="16"></input>
					</td>
				 </tr>
					
				  
				  <tr>
					<th align="right">*New Password:</th>
					<td> 
						<input type="password" id="inputVO.newPassword" name="inputVO.newPassword" size="16" maxlength="16"></input>
					 </td>
				  </tr>
				  <tr>
					<th align="right">*Re-enter Password:</th>
					<td> 
					  <input type="password" id="rePassword" name="rePassword" size="16" MAXLENGTH="16">
					</td>
				  </tr>
				 
	            
			
	            <TR>
				  <td COLSPAN=2 align="center"> 
					<INPUT TYPE="submit" VALUE=" Update My Password " 
	                    ONCLICK="if (validate()){ submitRequest(); return false; }" class="reg_button">
	
	              </td>
	            </tr>
          	</table>
	 			    
        </div>
    </div>              

