
<%@ taglib prefix="s" uri="/struts-tags" %>

<style>
<!--


.login_button {FONT-FAMILY:Verdana, Arial, Helvetica, sans-serif; FONT-SIZE:12pt; COLOR:#00007C; TEXT-DECORATION:none; WIDTH:125px }


.regist-form { padding:10px; }

.regist-form .field { padding: 4px;  /* margin:1px; background: #eee;*/ }

.regist-form .field label { display:inline-block; width:140px; margin-left:5px; color:#000000; FONT-SIZE:10pt; }

.regist-form .field input { display:inline-block; }
-->
</style>

<script LANGUAGE="JAVASCRIPT">  
function setFocus() {
	document.getElementById("inputVO.fname").focus();
} 

function validate() {

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
	
	if (document.getElementById("inputVO.password").value == "") {
		if (warning != "") {
			warning += ", ";
		}
		
		warning += "Password";
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
	
	if (document.getElementById("inputVO.password").value != document.getElementById("rePassword").value) {
		alert("Reenter password is different from the password.  Please correct.");
		return false;
	}
	
	var checkObj = document.getElementById("terms_check").checked;
	if (checkObj != true) {
		alert("You need to agree on the terms and conditions by checking the check box on the form.");
		return false;
	}
	
	return true;
	
}

// call set focus
window.onload=setFocus;

</script>

<div style="padding:10px; ">
<h1>Registration</h1>
<br>
<span style="font-weight: bold; font-size: 1.1em; line-height: 1.3em; color: #003355; ">
	Please read the terms and conditions, before you submit the form.

</span>
<br><br>
			<form name="registerForm" action="../p/register_submit.do" class="regist-form" >
				<input type="hidden" name="inputVO.mode" value="regist"/>	
				 
						<% 
							String error = (String)request.getAttribute("error"); 
							String confirm = (String)request.getAttribute("confirm");
							if (error == null) error = "";
						    if (confirm == null) confirm = "";
						%>
						
    <div>

	  
				
						
						<% if(!error.equals("") || !confirm.equals("")) { %>
						<div>
							<!-- THE ERROR DISPLAY FIELD -->
								<FONT color="red"><I><%= error %></I></FONT>
								<FONT color="red"><I><%= confirm %></I></FONT>
								<FONT color="red"><I><html:errors/></I></FONT> 
						
						</div>
						<% } %>

         
					    <div class="contain-fluid">
					    	<div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.fname">*First Name</label>
									<input name="inputVO.fname" id="inputVO.fname" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.fname}'/>"></input>		
								</div>
							
								<div class="col-sm-6 col-md-6 col-lg-6 field">
									<label for="inputVO.mname">Middle Name</label>
									<input name="inputVO.mname" id="inputVO.mname" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.mname}'/>"></input>		
								</div>  
						    </div>
						    
							<div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.lname">*Last Name</label>
									<input name="inputVO.lname" id="inputVO.lname" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.lname}'/>"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.email">*Email Address</label>
									<input name="inputVO.email" id="inputVO.email" size="25" maxlength="50" class="form-control" value="<s:property value='%{inputVO.email}'/>"></input>		
								</div>  
						    </div>
						    
						    <div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.phone">Phone</label>
									<input name="inputVO.phone" id="inputVO.phone" size="15" maxlength="20" class="form-control" value="<s:property value='%{inputVO.phone}'/>"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.secondPhone">Second Phone</label>
									<input name="inputVO.secondPhone" id="inputVO.secondPhone" size="15" maxlength="20" class="form-control" value="<s:property value='%{inputVO.secondPhone}'/>"></input>		
								</div>  
						    </div>
						    
						    <div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.addr1">Addr1</label>
									<input name="inputVO.addr1" id="inputVO.addr1" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.addr1}'/>"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.fax">Fax</label>
									<input name="inputVO.fax" id="inputVO.fax" size="15" maxlength="20" class="form-control" value="<s:property value='%{inputVO.fax}'/>"></input>		
								</div>  
						    </div>
						    
							<div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.addr2">Addr2</label>
									<input name="inputVO.addr2" id="inputVO.addr2" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.addr2}'/>"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.city">City</label>
									<input name="inputVO.city" id="inputVO.city" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.city}'/>"></input>		
								</div>  
						    </div>
						    
						    <div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.state">State</label>
									<input name="inputVO.state" id="inputVO.state" size="20" maxlength="20" class="form-control" value="<s:property value='%{inputVO.state}'/>"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.zip">Zip</label>
									<input name="inputVO.zip" id="inputVO.zip" size="10" maxlength="10" class="form-control" value="<s:property value='%{inputVO.zip}'/>"></input>		
								</div>  
						    </div>

							<div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.countryCd">Country</label>
									<select name="inputVO.countryCd">
										<option value="US">United States</option>
										</select>
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.loginId">*Login ID</label>
									<input name="inputVO.loginId" id="inputVO.loginId" size="16" maxlength="16" class="form-control"></input>		
								</div>  
						    </div>
						    
						    <div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.password">*Password</label>
									<input type="password" value="" id="inputVO.password" name="inputVO.password" size="16" maxlength="16" class="form-control"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="rePassword">*Re-enter Password</label>
									<input type="password" id="rePassword" name="rePassword" size="15" MAXLENGTH="16" class="form-control"></input>		
								</div>  
						    </div>
								
							<div class="row">
						    	<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    <label for="inputVO.nickname">*Nickname</label>
									<input name="inputVO.nickname" id="inputVO.nickname" size="20" maxlength="30" class="form-control" value="<s:property value='%{inputVO.nickname}'/>"></input>		
								</div>
								
								<div class="col-sm-6 col-md-6 col-lg-6 field">
					    		    &nbsp;		
								</div>  
						    </div>

							<div class="row">
								  <div class="checkbox">
								    <label>
								      <input type="checkbox" id="terms_check" > You agree the terms and conditions
								    </label>
								  </div>
								
							</div>
							
							<div class="row">
								<div><P><font face="Verdana, Arial, Helvetica, sans-serif" SIZE="+1" COLOR="#339966">
				                <b>Submit your registration form:</b>
				              </FONT></P></div>
							</div>
							
							<div class="row">
								<div class="field">
					    		    <label for="submitbt">&nbsp;</label>
					    		    <INPUT TYPE="BUTTON" id="submitbt" VALUE=" Submit "  class="btn btn-primary"
				                    ONCLICK="if (validate()){ submit() }">
				                    </div>
							</div>

          			</div>
          		</div>	
          	</form>
	 			    
		

                    
</div>           
                    