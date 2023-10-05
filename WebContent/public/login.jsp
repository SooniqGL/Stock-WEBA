
<style>
<!--
/*  SECTIONS  */

.notes {
	width: 80%;
	margin-top: 50px;
}


.login_button {FONT-FAMILY:Verdana, Arial, Helvetica, sans-serif; FONT-SIZE:12pt; COLOR:#00007C; TEXT-DECORATION:none; WIDTH:85px }


.user-form { padding:20px; }

.user-form .field { padding: 4px; /* margin:1px; background: #eee;*/ }

.user-form .field label { display:inline-block; width:120px; margin-left:5px; color:#ffff00; FONT-SIZE:12pt; }

.user-form .field input { display:inline-block; }
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
<div id="loginoverlay" class="sidebox1" style="min-height:50px;" >
<div style="margin:2px; padding:5px; background:#005588">
			<form action="../p/login_submit.do" class="user-form">
				<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="login"/>			
                   
			<% 	String loginError = (String)request.getAttribute("error");
				if (loginError == null) loginError = ""; 	
	 			String logoutConfirm = (String)request.getAttribute("confirm"); 
				if (logoutConfirm == null) logoutConfirm = ""; %>
				
			<div>
	    		
	    		<div class="field">
	    		    <label for="inputVO.loginId">Login ID</label>
					<input type="text" id="inputVO.loginId" name="inputVO.loginId" autocomplete="off"  size="15" >		
				</div>
    		  
    		    
	    		<div class="field">
	    		    <label for="inputVO.password">Password</label>
	    			<input type="password" value="" name="inputVO.password" id="inputVO.password" size="15">
	    		</div>
	    		
	    		<div class="field">
	    		    <label for="login">&nbsp;</label>
	    			<INPUT type="submit" name="login" id="login" value=" Login " class="login_button" />
	    		</div>
	    	</div>
			<div>
			<% if (!loginError.equals("") || !logoutConfirm.equals("")) { %>
					<FONT color="red"><I>&nbsp;&nbsp;<%= loginError %></I></FONT>
					<FONT color="white"><I>&nbsp;&nbsp;<%= logoutConfirm %></I></FONT>	
			<% } %>
			</div>
	</form>
  
</div>   
</div>
                        
        