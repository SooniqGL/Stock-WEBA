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
<SCRIPT SRC="../script/common.js"></SCRIPT>
   
<TITLE>Update Folder</TITLE> 
<SCRIPT language="javascript">    
 	 
	function submitRequest(mode, type) {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		
		if (mode == "updatefolder") {
			if (type == "addlist") {
				if (collectValues()) {
					form.submit();
				}
			} else if (type == "delete") {
				var ret = confirm("You are about to delete the entire folder. \nClick OK to continue or Cancel to abort action.");
				if (ret == false) {
					return false;
				}
				
				form.submit();
			} else if (type == "rename") {
				if (form.all["inputVO.folderName"].value == "") {
					alert("Folder name cannot be empty.");
					return false;
				}
				
				form.submit();
			}
		} else {
			form.submit();
		}
	}
	
	function collectValues() {
		var form = document.watchForm;
		var rows = 10;
		var tickerListStr = "";
	
		// loop through all the elements
		var ticker = "";
		var comments = "";
		for (var id = 1; id <= rows; id ++) {
			ticker = eval("document.watchForm.TIK" + id + ".value");
			
			if (ticker != "") {
				comments = eval("document.watchForm.COM" + id + ".value");
				if (tickerListStr != "") {
					tickerListStr += "#";
				}
				
				comments = encodeString(comments);
				tickerListStr += ticker + ":" + comments;
			}
		}
	
		if (tickerListStr == "") {
			alert("Need at least enter one ticker.");
			return false;
		} else {
			form.all["inputVO.tickerListStr"].value = tickerListStr;
			return true;
		}
	}
	
</SCRIPT>	
<jsp:useBean id="watchForm" scope="session" type="com.greenfield.ui.action.member.WatchListActionForm"/>
<bean:define id="inputVO" name="watchForm" property="inputVO" type="com.greenfield.common.object.member.WatchListVO"/>
<bean:define id="watchList" name="inputVO" property="watchList" type="java.util.Vector"/>

</HEAD>
<BODY topmargin="0" bottommargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<center><table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0"> 
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" height="100%" cellpadding="0" cellspacing="0"> 
	<tr><td height="40" align="center" colspan="2" class="tail_color"><font class="main_title">Member Account</font></td></tr>
	
	<tr><td colspan="2" align="center" height="50"><a href="../m/home.do?inputVO.mode=blank" class="toplink">Member Home</a>
		&nbsp;|&nbsp;<a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="toplink">Profile</a>
		&nbsp;|&nbsp;<a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="toplink">Alert List</a></td></tr>
		
	<tr><td colspan="2">
		<html:form action="/m/watch"> 
		<html:hidden property="inputVO.mode" value="basic"/>		
		<html:hidden property="inputVO.type" value=""/>
		<html:hidden property="inputVO.tickerListStr" value=""/>
		<input type="hidden" name="inputVO.folderId" value="<bean:write name="inputVO" property="folderId"/>"/>
		        
		<table border="1" width="100%">
			<tr class="member_back"><td colspan="2" height="30">&nbsp;
			<font class="sub_title">Update Watch List Folder: </font>
			<a href="../m/watch.do?inputVO.mode=viewwatch&inputVO.folderId=<bean:write name="inputVO" property="folderId"/>" class="toplink"><bean:write name="inputVO" property="folderName"/></a>
			</td></tr>
			
			<logic:notEqual name="inputVO" property="message" value="">
				<TR class="member_back"><td colspan="2">
				<font color="#ff0000">Error: <bean:write name="inputVO" property="message"/></font></td>		
				</TR>
			</logic:notEqual>
			<tr><td>Add New Tickers</td>
			<td><table width="100%">
				<tr class="member_back"><td>Ticker</td><td>Notes</td><td>Ticker</td><td>Notes</td></tr>
	
				<TR class="gray_back">
					<TD valign="top"><input type="text" name="TIK1" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM1" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" name="TIK2" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM2" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR class="gray_back">
					<TD valign="top"><input type="text" name="TIK3" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM3" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" name="TIK4" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM4" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR class="gray_back">
					<TD valign="top"><input type="text" name="TIK5" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM5" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" name="TIK6" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM6" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR class="gray_back">
					<TD valign="top"><input type="text" name="TIK7" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM7" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" name="TIK8" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM8" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR class="gray_back">
					<TD valign="top"><input type="text" name="TIK9" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM9" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" name="TIK10" size="10" maxlength="10" value=""></TD>
					<TD><textarea name="COM10" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR>
					<TD colspan="4" align="center">
						<input name="sb4" type="button" value="Add To Watch List" onclick="submitRequest('updatefolder', 'addlist')" class="exlong_button"/>	
					</TD>
				</TR>
			</table></td>
		<tr><td>Rename Folder</td>
			<td>
			<input type="text" name="inputVO.folderName" value="<bean:write name="inputVO" property="folderName"/>" size="50" maxLength="100">
			<input type="button" name="sb" value="Rename Folder" onClick="submitRequest('updatefolder', 'rename')">
			</td>
		</tr>
		
		<tr><td>Delete the Folder</td>
			<td>
			<input type="button" name="sb" value="Delete Folder and All Content" onClick="submitRequest('updatefolder', 'delete')">
			This will delete all things associated to this folder.
			</td>
			</tr>
		</table>
	</html:form>
	</td></tr> 
	<tr><td colspan="2">Notes: We only allow to add the tickers we have in our DB.  Anything not exists in our DB will be ignored.
	We currently hold most of the tickers in Nasdaq and New York Exchanges.</td></tr>
	<tr><td height="50" colspan="2">&nbsp;</td></tr>
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
	</table>
</td></tr>    
	
</table></center>
</BODY>
</HTML>