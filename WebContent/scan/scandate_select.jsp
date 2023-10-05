<%@ page import="java.util.Vector" %>

<SCRIPT language="javascript"> 
function fn_Show()
{ 	
	// display the layer, set focus, this will be used by onblur
	// either a frame or select drop down focus is set
	//document.all[layer].style.display = "block";
	//eval(form + "." + select_name + ".focus()");
    
	$("#divList").show();
	$("#selectIt").focus();
        
}

function fn_Hide()
{
	$("#divList").hide();
}
 
// hide a layer in the parent level
function fn_Hide_P() 
{
 	parent.document.getElementById("divList").style.display = "none";
}

// set the select value and hide the layer
function fn_Choose_Value()
{
	// var scanDt = eval(form + "." + select_name + ".value");

    var scanDt = $("#selectIt").val();
    fn_Hide();  //? not work? will not work for Chrome if has it after the submit
	Q.scan.submitRequest3(scanDt);
	
}
</SCRIPT>
<%-- 
   // form_name, date list that contains all the options
   String form_name = request.getParameter("form_name");
   Vector opt_list = (Vector) request.getAttribute("scandate_list");
   
   --%>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td  style="padding:0px;" nowrap> 
&nbsp;<input type="button" name="btn_Submit" value=" All Dates " onClick="fn_Show()">&nbsp;
</td>
</tr>
<tr><td style="padding:0px;"> 
<div id="divList" style="position:absolute; z-index:1; display:none">
<SELECT id="selectIt" NAME="selectIt" SIZE="10" MULTIPLE onChange="fn_Choose_Value()" onBlur="fn_Hide()">

<%-- Only replace the option potion below --%> 
<%-- move this load to client, as this is so heavy here for 900+ entries ...
<% if (opt_list != null && opt_list.size() > 0) {
   for (int i = 0; i < opt_list.size(); i ++) {
     String item = (String) opt_list.get(i); %>
<OPTION VALUE="<%= item %>">&nbsp;<%= item %>&nbsp;</OPTION>  
<% } } %> --%>

</SELECT>
</div>
<SCRIPT> fn_Hide(); </script>
</td>
</tr>
</table>
