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
		if (document.getElementById("inputVO.fname").value == "") {
			warning = "First Name";
		}
		
		if (document.getElementById("inputVO.lname").value == "") {
			if (warning != "") {
				warning += ", ";
			}
			
			warning += "Last Name";
		}
		
		if (document.getElementById("inputVO.email").value == "") {
			if (warning != "") {
				warning += ", ";
			}
			
			warning += "Email";
		}
		
		if (document.getElementById("inputVO.loginId").value == "") {
			if (warning != "") {
				warning += ", ";
			}
			
			warning += "Login Id";
		}
		
		if (document.getElementById("inputVO.nickname").value == "") {
			if (warning != "") {
				warning += ", ";
			}
			
			warning += "Nickname";
		}
		
		if (warning != "") {
			alert("Required fields: " + warning);
			return false;
		}
		
		
		return true;
	
	}
	
 
</SCRIPT>	



<div class="contentWrapper">
<div style="margin:0 0 0 0; width:100%; background:#ffffef">
            
    <form name="myinfoForm" action="../m/profile_updatemyinfo.do"> 	
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="updatemyinfo" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="update" />
       	                
            
    <table width="100%" class="gridtable">
	
	    <tr><td height="40" align="center" colspan="4"><font class="sub_title">Update My Profile</font></td></tr>
		
			<s:if test="%{inputVO.message != NULL && inputVO.message != \"\"}">
				<TR><td colspan="4">
				<font color="#ff0000">Error: <s:property value='%{inputVO.message}'/></font></td>		
				</TR>
			</s:if>
			
				<tr> 
					<th width="20%">*First Name:</th>
					<td width="30%"> 
					  <input id="inputVO.fname" name="inputVO.fname" size="20" maxlength="30" value="<s:property value='%{inputVO.fname}'/>"></input>
					</td>
				  
					<th width="20%">Middle Name:</th>
					<td width="30%"> 
					  <input id="inputVO.mname" name="inputVO.mname" size="20" maxlength="30" value="<s:property value='%{inputVO.mname}'/>"></input>
					</td>
				  </tr>
				  <tr> 
					<th>*Last Name:</th>
					<td> 
					<input id="inputVO.lname" name="inputVO.lname" size="20" maxlength="30" value="<s:property value='%{inputVO.lname}'/>"></input>
					</td>
				   
					<th>*Email Address:</th>
					<td>
					<input id="inputVO.email" name="inputVO.email" size="25" maxlength="50" value="<s:property value='%{inputVO.email}'/>"></input> 
					</td>
				  </tr>
				  <tr> 
					<th>Phone:</th>
					<td> 
					  <input id="inputVO.phone" name="inputVO.phone" size="15" maxlength="20" value="<s:property value='%{inputVO.phone}'/>"></input>
					</td>
				 
					<th>Second Phone:</th>
					<td> 
					  <input id="inputVO.secondPhone" name="inputVO.secondPhone" size="15" maxlength="20" value="<s:property value='%{inputVO.secondPhone}'/>"></input>
					</td>
				  </tr>
				  <tr> 
					  <th>Addr1:</th>
					  <td> 
						 <input id="inputVO.addr1" name="inputVO.addr1" size="20" maxlength="30" value="<s:property value='%{inputVO.addr1}'/>"></input>
					   </td>
					<th>Fax:</th>
					<td> 
					  <input id="inputVO.fax" name="inputVO.fax" size="15" maxlength="20" value="<s:property value='%{inputVO.fax}'/>"></input>
					</td>
				  </tr>
				  <tr> 
					<th>Addr2:</th>
					<td> 
					  <input id="inputVO.addr2" name="inputVO.addr2" size="20" maxlength="30" value="<s:property value='%{inputVO.addr2}'/>"></input>
					</td>
				  
					<th>City:</th>
					<td> 
					  <input id="inputVO.city" name="inputVO.city" size="20" maxlength="30" value="<s:property value='%{inputVO.city}'/>"></input>
					</td>
				  </tr>
				  <tr> 
					<th>State:</th>
					<td> 
					  <input id="inputVO.state" name="inputVO.state" size="20" maxlength="20" value="<s:property value='%{inputVO.state}'/>"></input>
					</td>
				   
					<th>Zip:</th>
					<td> 
					  <input id="inputVO.zip" name="inputVO.zip" size="10" maxlength="10" value="<s:property value='%{inputVO.zip}'/>"></input>
					</td>
				  </tr>
				  <tr> 
					<th>Country:</th>
					<td> 
						<select id="inputVO.countryCd" name="inputVO.countryCd" size="1">
						<option value="US">United States</option>
						</select>
					</td>
					
					<th>*Login ID:</th>
					<td>
						<input id="inputVO.loginId" name="inputVO.loginId" size="16" maxlength="16" value="<s:property value='%{inputVO.loginId}'/>"></input> 
					</td>
				  </tr>
				  <tr>
					<th>Receive Email:</th>
					<td> 
					<s:if test="%{inputVO.receiveEmail == \"Y\"}">
			
						<select id="inputVO.receiveEmail" name="inputVO.receiveEmail" size="1">
						<option value="Y" selected>Yes</option>
						<option value="N">No</option>
						</select>
					</s:if>
					<s:else>
						<select id="inputVO.receiveEmail" name="inputVO.receiveEmail" size="1">
						<option value="Y">Yes</option>
						<option value="N" selected>No</option>
						</select>
					</s:else>
					 </td>
				  
					<th>*Nickname:</th>
					<td>
						<input id="inputVO.nickname" name="inputVO.nickname" size="16" maxlength="30" value="<s:property value='%{inputVO.nickname}'/>"></input> 
					</td>
				  </tr>
	            
			
	            <TR>
				  <td COLSPAN=4 align="center"> 
					<INPUT TYPE="BUTTON" VALUE=" Update User Profile " 
	                    ONCLICK="if (validate()){ submitRequest() }" class="reg_button">
	
	              </td>
	            </tr>
          	</table>
	 			    
        </div>
    </div>              

