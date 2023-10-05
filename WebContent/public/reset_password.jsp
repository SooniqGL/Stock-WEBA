<%-- colstyle class - this height is used to give space for the <sup> tag --%>
<style>
<!--
/*  SECTIONS  */
.resetpasswordsection {
	clear: both;
	/* padding: 0px; */
	margin-top: 20px; 
	width: 98%;
	border: 1px solid #dfdfdf; 
	/* background:#f5f5f5; */
}

.colstyle {
	/* height:24px; */
}

.notes {
	width: 80%;
	margin-top: 50px;
}

.notes li {
  display: list-item;
  /* list-style-position: inside; */
  margin-left: 25px;
}

-->
</style>

<script LANGUAGE="JAVASCRIPT"> 
function setFocus() { 
  // Very strange! Need to call select() first, other wise fails!
  document.getElementById("inputVO.loginId").select();
  document.getElementById("inputVO.loginId").focus();
} 

// call set focus
window.onload=setFocus;
</script>
<div style="padding:10px;">
			<form name="resetpasswordForm" action="../p/resetpassword_submit.do">
				<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="submit"/>			
				<h1>Reset Password</h1>
		
			<% 
				String resetError = (String)request.getAttribute("reseterror"); 
				if (resetError == null) { resetError = ""; }	
				String resetConfirm = (String)request.getAttribute("resetconfirm");
				if (resetConfirm == null) { resetConfirm = "";} 
			%>
				
              
               
            <span style="color:red;font-style:italic;"><%= resetError %></span>
			<span style="color:green;font-style:italic;"><%= resetConfirm %></span>
    		  
    		<div class="resetpasswordsection group">
	    		<div class="col_first span_sml_of_2 colstyle">&nbsp;&nbsp;Login ID*</div>
	    		<div class="col span_lrg_of_2 colstyle">
					<input type="text" name="inputVO.loginId" id="inputVO.loginId" class="form-control" placeholder="Login ID"  size="16" maxlength="16" autocomplete="off" />
				</div>
				
				<div class="col_first span_sml_of_2 colstyle">&nbsp;&nbsp;PIN<sup>4</sup></div>
	    		<div class="col span_lrg_of_2 colstyle">
	    			<input type="text" name="inputVO.pin" id="inputVO.pin" class="form-control" placeholder="PIN" size="16" maxlength="16" autocomplete="off" />
				</div>
    		
	    		<div class="col_first span_sml_of_2 colstyle">&nbsp;&nbsp;Email*</div>
	    		<div class="col span_lrg_of_2 colstyle">
	    			<input type="text" name="inputVO.email" id="inputVO.email" class="form-control" placeholder="Email"  size="50" maxlength="50" autocomplete="off" />
				</div>
				
	    		<div class="col_first span_sml_of_2 colstyle">&nbsp;</div>
	    		<div class="col span_lrg_of_2 colstyle">
	    			<INPUT type="submit" name="resetpass" value="Reset Password"  class="btn btn-primary" style="font-size:12pt" />
				</div>
			</div>
  
			 
			<div class="notes list_text"><h3>Note:</h3>
			
				<ol>
				<li>Both login ID and email are required to reset your password.</li>
				<li>Both fields need to match our records that you have registered.</li>
				<li>After verification, we will send your email.  Please check your email for the temporary password.</li>
				<li>A PIN may be added by Sooniq for security purpose.  If it is added, you need to contact Sooniq through phone to get
				your PIN.  We will not provide any access to members to modify the PIN online for security purpose.
				</ol>
			</div>
			
			</form>

</div>
                        
        