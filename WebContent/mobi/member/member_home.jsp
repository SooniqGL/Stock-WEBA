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
 
<TITLE>Member Home List</TITLE>
<SCRIPT language="javascript">     
 	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		form.submit();
	}
	
	function submitMyInfoRequest() {
		var form = document.myinfoForm;
		form.submit();
	}
	
	function updateEntry(mode, type, stockId) {
		var form = document.watchForm;
		
		if (mode == "deletewatch") {
			var ret = confirm("You are about to delete the entry. \nClick OK to continue or Cancel to abort action.");
			if (ret == false) {
				return false;
			}
		}
		
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		form.all["inputVO.stockId"].value = stockId;
		form.submit();
	}
	
</SCRIPT>	
<jsp:useBean id="memberHomeForm" scope="session" type="com.greenfield.ui.action.member.MemberHomeActionForm"/>
<bean:define id="inputVO" name="memberHomeForm" property="inputVO" type="com.greenfield.common.object.member.MemberHomeVO"/>
<bean:define id="folderList" name="inputVO" property="folderList" type="java.util.Vector"/>

</HEAD>
<BODY topmargin="0" bottommargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<center><table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0"> 
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" height="100%" cellpadding="0" cellspacing="0"> 
	<tr><td height="40" align="center" colspan="2" class="tail_color"><font class="main_title">Member Account</font></td></tr>
	
	<tr><td colspan="2" align="center" height="50"><font class="main_title2">Member Home</font></td></tr>
	
	<tr><td colspan="2"><table border="1">
		<tr class="member_back"><td colspan="2">&nbsp;</td></tr>
		<tr><td width="200">&nbsp;&nbsp;<b>Watch Lists</b></td>
			<td><table width="100%">
				<tr><td colspan="2"><a href="../m/watch.do?inputVO.mode=newfolder&inputVO.type=blank&inputVO.folderId=" class="toplink">Create New Folder</a></td></tr>
				<logic:iterate id="itemVO" name="folderList">
				<tr><td>
					<a href="../m/watch.do?inputVO.mode=viewwatch&inputVO.folderId=<bean:write name="itemVO" property="folderId"/>" class="datalink"><bean:write name="itemVO" property="folderName" /></a>
					</td>
					<td>
					<a href="../m/watch.do?inputVO.mode=updatefolder&inputVO.type=blank&inputVO.folderId=<bean:write name="itemVO" property="folderId"/>" class="datalink">Update Folder</a>
					</td>
				</tr>
				</logic:iterate>
				</table>
		</td></tr>
		<tr><td>&nbsp;&nbsp;<a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="toplink">Member Profile</a></td>
			<td>View and edit your profile</td>
			</tr>
		<tr><td>&nbsp;&nbsp;<a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="toplink">Alert List</a></td>
			<td>The stocks in your watch lists that have either changed the trend signal or overbought or oversold.
			<br><br>
			Note: We have a program to check everybody's watch list each business day.  If we have
			found some of the stocks in the watch list have changed signal in the current day, we will
			add tickers to the Alert List.<br>
			The only thing you need to do is to set "Receive Email" field to "Yes" in
			your profile.  Make sure the email address is right as well.  We will
			also send email to you when we turn on our email services.
			</td>
			</tr>
		</table></td></tr>	        

				
	
	<tr><td height="100%" colspan="2">&nbsp;</td></tr>
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
	</table>
</td></tr>    
	
</table></center>
</BODY>
</HTML>
