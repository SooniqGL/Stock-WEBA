<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%> 
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
<SCRIPT SRC="../script/onmouse.js"></SCRIPT>
  
<TITLE>New Folder List</TITLE>
<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.watchForm;
		
		if (form.all["inputVO.folderName"].value == "") {
			alert("Folder name is required.");
			return false;
		}
		
		form.submit();
	}
	
	function setFocus() { 
	  document.watchForm.elements["inputVO.folderName"].focus();
	} 
	
</SCRIPT>	

<jsp:useBean id="watchForm" scope="session" type="com.greenfield.ui.action.member.WatchListActionForm"/>
<bean:define id="inputVO" name="watchForm" property="inputVO" type="com.greenfield.common.object.member.WatchListVO"/>
<bean:define id="folderList" name="inputVO" property="folderList" type="java.util.Vector"/>

</HEAD>
<BODY topmargin="0" bottommargin="0" leftmargin ="0" marginheight="0" marginwidth="0" onload="setFocus()" class="page_back">
<center><table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0"> 
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" height="100%" cellpadding="0" cellspacing="0"> 
	<tr><td height="40" align="center" colspan="2" class="tail_color"><font class="main_title">Member Account</font></td></tr>
	
	<tr><td colspan="2" align="center" height="50"><a href="../m/home.do?inputVO.mode=blank" class="toplink">Member Home</a>
		&nbsp;|&nbsp;<a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="toplink">Profile</a>
		&nbsp;|&nbsp;<a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="toplink">Alert List</a></td></tr>
		
	<tr><td colspan="2" width="100%" valign="top">
	<html:form action="/m/watch"> 
			<html:hidden property="inputVO.mode" value="newfolder"/>		
			<html:hidden property="inputVO.type" value="add"/>
		<table border="1" width="100%">
		<tr class="member_back"><td colspan="2">&nbsp;
		<font class="sub_title">Add New Folder</font></td></tr>
		<tr><td colspan="2">
			
			        
			&nbsp;<input type="text" name="inputVO.folderName" value="" maxLength="100">
			<input type="button" name="sb" value="Create Folder" onClick="submitRequest()">
		</td>
		</tr> 
		<tr><td width="50%">Current Folder List:</td><td>
			<% int count = 0; %>
			<logic:iterate id="itemVO" name="folderList">
				<a href="../m/watch.do?inputVO.mode=viewwatch&inputVO.folderId=<bean:write name="itemVO" property="folderId" />" class="datalink"><bean:write name="itemVO"  property="folderName" /></a>
					<br>
			<% count ++; %>
			</logic:iterate>
			<% if (count == 0) { %>
			No folder exists in your account.
			<% } %>
		</td></tr>
		</table></html:form></td></tr>
	<tr><td height="100" colspan="2">&nbsp;</td></tr>
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
	</table>
</td></tr>    
	
</table></center>
</BODY>
</HTML>