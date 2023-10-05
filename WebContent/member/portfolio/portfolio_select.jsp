<%@ page import="java.util.Vector" %>

<SCRIPT language="javascript"> 
function fn_Show (form, layer, select_name)
{ 	
	// display the layer, set focus, this will be used by onblur
	// either a frame or select drop down focus is set
	document.all[layer].style.display = "block";
	eval(form + "." + select_name + ".focus()");
}

function fn_Hide (layer)
{
	document.all[layer].style.display = "none";
}
 
// hide a layer in the parent level
function fn_Hide_P (layer) 
{
 	parent.document.all[layer].style.display = "none";
}

// set the select value and hide the layer
function fn_Choose_Value (form, layer, select_name)
{
	var folderId = eval(form + "." + select_name + ".value");
	loadFolder(folderId);
	fn_Hide (layer);
}
</SCRIPT>
<style type="text/css">
.fix-size{
   width: 250px;
   background-color: #FFFFEC;
}
</style>
<% 
   // form_name, date list that contains all the options
   String form_name = request.getParameter("form_name");
   String folder_name = request.getParameter("folder_name");
   Vector opt_list = (Vector) request.getAttribute("folder_list");
   
   %>

<table width="100%" border="0" cellspacing="0" cellpadding="0">
<tr><td nowrap> 
<input type="text" name="showText" value="<%= folder_name %>" class="fix-size" onClick="fn_Show('<%= form_name %>', 'divList', 'selectIt')" title="Click to pull the drop down" readonly>
<%--input type="button" name="sbt" value="Folders" onClick="fn_Show('<%= form_name %>', 'divList', 'selectIt')"--%>
</td>
</tr>
<tr><td> 
<div id="divList" style="position:absolute; z-index:1; display:none">
<SELECT NAME="selectIt" SIZE="10" MULTIPLE onChange="fn_Choose_Value('<%= form_name %>', 'divList', 'selectIt')" onBlur="fn_Hide('divList')" class="fix-size">

<%-- Only replace the option potion below --%> 
<% if (opt_list != null && opt_list.size() > 0) {
   for (int i = 0; i < opt_list.size(); i ++) {
   	 com.greenfield.common.object.member.FolderInfo info = (com.greenfield.common.object.member.FolderInfo) opt_list.get(i);
     %>
<OPTION VALUE="<%= info.getFolderId() %>">&nbsp;<%= info.getFolderName() %>&nbsp;</OPTION>  
<% } } %> 

</SELECT>
</div>
<SCRIPT> fn_Hide ("divList"); </script>
</td>
</tr>
</table>
