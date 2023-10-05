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
<TITLE>Update Watch List</TITLE>  
<SCRIPT language="javascript">    
  	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		
		if (!isValidDate(form.all["inputVO.watchInfo.openDate"].value) ||
			!isValidDate(form.all["inputVO.watchInfo.closeDate"].value)) {
			return false;
		} else if (!isValidNumber(form.all["inputVO.watchInfo.openPrice"].value) ||
			!isValidNumber(form.all["inputVO.watchInfo.openPrice"].value)) {
			return false;
		}
		
		if (form.all["inputVO.watchInfo.openDate"].value != "" ||
			form.all["inputVO.watchInfo.closeDate"].value != "") {
			if (form.all["inputVO.watchInfo.tradeType"].value == "") {
				alert("Trade type is needed.");
				return false;
			}
		}
		
		form.submit();
	}
	
	function submitDeleteRequest() {
		var form = document.watchForm;

		var ret = confirm("You are about to delete the entry. \nClick OK to continue or Cancel to abort action.");
		if (ret == false) {
			return false;
		}

		form.all["inputVO.mode"].value = "deletewatch";
		form.submit();
	}
	
	function submitMoveRequest() {
		var form = document.watchForm;
		if (form.all["inputVO.folderId"].value == form.all["inputVO.newFolderId"].value) {
			alert("It is already in this folder.  Select another folder to move.");
			return;
		}
		
		form.all["inputVO.mode"].value = "updatefolder";
		form.all["inputVO.type"].value = "move";
		form.submit();
	}
	
	function submitCopyRequest() {
		var form = document.watchForm;
		if (form.all["inputVO.folderId"].value == form.all["inputVO.newFolderId"].value) {
			alert("It is already in this folder.  Select another folder to copy.");
			return;
		}
		
		form.all["inputVO.mode"].value = "updatefolder";
		form.all["inputVO.type"].value = "copy";
		form.submit();
	}
	
	function setCurrentForOpen() {
		var form = document.watchForm;
		form.all["inputVO.watchInfo.openDate"].value = form.currentDate.value;
		form.all["inputVO.watchInfo.openPrice"].value = form.currentPrice.value;
	}
	
	function cleanOpen() {
		var form = document.watchForm;
		form.all["inputVO.watchInfo.openDate"].value = "";
		form.all["inputVO.watchInfo.openPrice"].value = "";
	}
	
	function setCurrentForClose() {
		var form = document.watchForm;
		form.all["inputVO.watchInfo.closeDate"].value = form.currentDate.value;
		form.all["inputVO.watchInfo.closePrice"].value = form.currentPrice.value;
	}
	
	function cleanClose() {
		var form = document.watchForm;
		form.all["inputVO.watchInfo.closeDate"].value = "";
		form.all["inputVO.watchInfo.closePrice"].value = "";
	}
	
	
</SCRIPT>	
<jsp:useBean id="watchForm" scope="session" type="com.greenfield.ui.action.member.WatchListActionForm"/>
<bean:define id="inputVO" name="watchForm" property="inputVO" type="com.greenfield.common.object.member.WatchListVO"/>
<bean:define id="watchInfo" name="inputVO" property="watchInfo" type="com.greenfield.common.object.member.WatchInfo"/>
<bean:define id="folderList" name="inputVO" property="folderList" type="java.util.Vector"/>
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
		<input type="hidden" name="inputVO.watchInfo.stockId" value="<bean:write name='inputVO' property='stockId'/>"/>
		<input type="hidden" name="inputVO.folderId" value="<bean:write name='inputVO' property='folderId'/>"/>
		<input type="hidden" name="inputVO.watchInfo.folderId" value="<bean:write name='inputVO' property='folderId'/>"/>
		<input type="hidden" name="currentDate" value="<bean:write name='inputVO' property='currentDate'/>"/>
		<input type="hidden" name="currentPrice" value="<bean:write name='inputVO' property='currentPrice'/>"/>
		<% String folderId = inputVO.getFolderId(); %>
		        
		<table border="1" width="100%">
			<tr class="member_back"><td colspan="2" height="30">&nbsp;
			<font class="sub_title">Update Watch List Entry</font>
			in <a href="../m/watch.do?inputVO.mode=viewwatch&inputVO.folderId=<bean:write name="inputVO" property="folderId"/>" class="toplink"><bean:write name="inputVO" property="folderName"/></a>
			</td></tr>
			
			<tr class="member_back"><td>Ticker: <bean:write name="watchInfo" property="ticker"/></td>
			<td>Company: <bean:write name="watchInfo" property="companyName"/></td></tr>
			
			<TR>
			<TD>Notes</td><td> 
				<TEXTAREA name="inputVO.watchInfo.comments" rows="5" cols="50"><bean:write name="watchInfo" property="comments"/></TEXTAREA>
			</TD>
			</tr>
			
			<tr bgcolor="#bbffcc"><td colspan="2">Paper trade record</td></tr>
			<tr><td>Trade Type</td>
				<td>
					<select name="inputVO.watchInfo.tradeType">
					<logic:equal name="watchInfo" property="tradeType" value="L">		    
			    		<option value=""></option>
						<option value="L" selected>Long</option>
						<option value="S">Short</option>
			    	</logic:equal>
					<logic:equal name="watchInfo" property="tradeType" value="S">		    
			    		<option value=""></option>
						<option value="L">Long</option>
						<option value="S" selected>Short</option>
			    	</logic:equal>
			    	<logic:equal name="watchInfo" property="tradeType" value="">		    
			    		<option value=""></option>
						<option value="L">Long</option>
						<option value="S">Short</option>
			    	</logic:equal>
					</select>
				</td>
			</tr>
			<tr><td>Open Date</td>
				<td>
					<input type="text" name="inputVO.watchInfo.openDate" value="<bean:write name='watchInfo' property='openDate'/>" />
				</td>
			</tr>
			<tr><td>Open Price</td>
				<td>
					<input type="text" name="inputVO.watchInfo.openPrice" value="<bean:write name='watchInfo' property='openPrice'/>" />
					<input type="button" name="bt1" value="Use Current" onClick="setCurrentForOpen()" class="button" />
					<input type="button" name="bt2" value="Clean" onClick="cleanOpen()" class="button" />
				</td>
			</tr>
			<tr><td>Close Date</td>
				<td>
					<input type="text" name="inputVO.watchInfo.closeDate" value="<bean:write name='watchInfo' property='closeDate'/>" />
				</td>
			</tr>
			<tr><td>Close Price</td>
				<td>
					<input type="text" name="inputVO.watchInfo.closePrice" value="<bean:write name='watchInfo' property='closePrice'/>" />
					<input type="button" name="bt3" value="Use Current"  onClick="setCurrentForClose()" class="button" />
					<input type="button" name="bt4" value="Clean"  onClick="cleanClose()" class="button" />
				</td>
			</tr>
			
		   <tr>
		    <td align="center" colspan="2"><input name="db1" type="button" value="Update Watch Entry" onclick="submitRequest('updatewatch', 'update')" class="exlong_button"/></td>
			</TR>
			
			<tr bgcolor="#bbffcc"><td colspan="2">Move, Copy, or Delete Record</td></tr>
			<tr><td>To Folder: </td>
		    	<td>
		    	<select name="inputVO.newFolderId">
				<logic:iterate id="folderItem" name="folderList">
					<logic:equal name="folderItem" property="folderId" value="<%= folderId %>">
						<option value="<bean:write name="folderItem" property="folderId"/>" selected><bean:write name="folderItem" property="folderName"/></option>
					</logic:equal>
					<logic:notEqual name="folderItem" property="folderId" value="<%= folderId %>">
						<option value="<bean:write name="folderItem" property="folderId"/>"><bean:write name="folderItem" property="folderName"/></option>
					</logic:notEqual>
				</logic:iterate>
				</select>
		    	
		    	<input name="db11" type="button" value="Move" onclick="submitMoveRequest()" class="button"/>
		    	<input name="db12" type="button" value="Copy" onclick="submitCopyRequest()" class="button"/>
				<input name="db3" type="button" value="Delete" onclick="submitDeleteRequest()" class="button"/>
			    </td>
			</TR>
		</table>
	</html:form>
	</td></tr> 
	
	<tr><td height="50" colspan="2">&nbsp;</td></tr>
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
	</table>
</td></tr>    
	
</table></center>
</BODY>
</HTML>
