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
 
<TITLE>Watch List</TITLE>
<SCRIPT language="javascript">     
  	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.type"].value = type;
		form.submit();
	}
	
	function reloadFolder() {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = "viewwatch";
		form.all["inputVO.folderId"].value = form.folderListOpt.value;
		form.submit();
	}
	
	function loadFolder(folderId) {
		var form = document.watchForm;
		form.all["inputVO.mode"].value = "viewwatch";
		form.all["inputVO.folderId"].value = folderId; 
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

<script type="text/javascript" language="JavaScript">
// Init section
	var obj_float_div = false;
	var show_delay = null;
 
	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	run_after_body();
</script>
<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

.float{
   visibility: hidden;
   position: absolute;
   left: -3000px;
   z-index: 10;
}

</style>
<jsp:useBean id="watchForm" scope="session" type="com.greenfield.ui.action.member.WatchListActionForm"/>
<bean:define id="inputVO" name="watchForm" property="inputVO" type="com.greenfield.common.object.member.WatchListVO"/>
<bean:define id="folderList" name="inputVO" property="folderList" type="java.util.Vector"/>
<bean:define id="watchList" name="inputVO" property="watchList" type="java.util.Vector"/>

</HEAD> 
<BODY topmargin="0" bottommargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<div class="float" id="div_200" style="left: -3000px; background: #ffffff;"><img id="img_200" class="border_b" width="680" height="400"></div>
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
		<html:hidden property="inputVO.stockId" value=""/>
		<input type="hidden" name="inputVO.folderId" value="<bean:write name="inputVO" property="folderId"/>"/>
		<% String folderId = inputVO.getFolderId();
		   String folderName = inputVO.getFolderName(); %>
		
		<table border="1" width="100%">
			<tr class="member_back">
			<td colspan="11" height="30" class="my_border"><table><tr><td>&nbsp;
				<font class="sub_title">My Watch List: </font></td>
				<td>
				<%  request.setAttribute("folder_list", folderList); %>
				<jsp:include page="folderlist_select.jsp" flush="true" >
	            	<jsp:param name="form_name" value="watchForm" />
	            	<jsp:param name="folder_name" value="<%= folderName %>" />
				</jsp:include>
				</td><td> 
			<%-- This is nice, but shows when the chart shows up...
				<select name="folderListOpt" onchange="reloadFolder()">
				<logic:iterate id="folderItem" name="folderList">
					<logic:equal name="folderItem" property="folderId" value="<%= folderId %>">
						<option value="<bean:write name="folderItem" property="folderId"/>" selected><bean:write name="folderItem" property="folderName"/></option>
					</logic:equal>
					<logic:notEqual name="folderItem" property="folderId" value="<%= folderId %>">
						<option value="<bean:write name="folderItem" property="folderId"/>"><bean:write name="folderItem" property="folderName"/></option>
					</logic:notEqual>
				</logic:iterate>
				</select>
				
				<input type="button" name="sb4" value="Load" onClick="reloadFolder()">
			--%>
				<logic:equal name="inputVO" property="type" value="chart">
					<input type="button" name="sb" value="No Charts" onClick="submitRequest('viewwatch', 'list')">
				</logic:equal>
				<logic:notEqual name="inputVO" property="type" value="chart">
					<input type="button" name="sb" value="Charts" onClick="submitRequest('viewwatch', 'chart')">
				</logic:notEqual>
				
				<input name="sb3" type="button" value="Update Watch List" onclick="submitRequest('updatefolder', 'blank')"/>
			</td></tr></table></td></tr>
			
			<tr class="member_back"><td class="my_border">Order</td><td class="my_border">Ticker</td>
			<td class="my_border">Company</td><td class="my_border">Price</td><td class="my_border">Trade</td>
			<td class="my_border">Open Date</td><td class="my_border">Open Price</td>
			<td class="my_border">Close Date</td><td class="my_border">Close Price</td><td class="my_border">Gain</td>
			<td class="my_border">Action</td>
			</tr>
			
			<%int count = 1;%>	 
		
			<logic:iterate id="itemVO" name="watchList">
			<%
				String comments = ((com.greenfield.common.object.member.WatchInfo) itemVO).getComments();
				String rowspan = "1";
				if (comments != null && !comments.equals("")) {
					rowspan = "2";
				}
				
				String rowColor = "";
				if (count % 2 == 1) {
					rowColor = "#ffffff";
				} else {
					rowColor = "#ffffdf";
				}
				
				String label = String.valueOf(count);
				if (label.length() < 2) {
					label = "0" + label;
				}
			%>
			<TR bgcolor="<%= rowColor %>">
				<TD align="center" rowspan="<%= rowspan %>" class="my_border">
					<input type="button" name="a" value="<%= label %>"
					onClick='show_200("../drawchart?stockId=<bean:write name='itemVO' property='stockId'/>&period=6&option=NNNY","71895",400,350)' onBlur="hide_div()">
				</TD>

				<TD align="center" rowspan="<%= rowspan %>" class="my_border">
					<logic:equal name="inputVO" property="type" value="chart">
						<A href="../a/analyze.do?inputVO.mode=analyze&inputVO.type=id&inputVO.ticker=<bean:write name='itemVO' property='ticker'/>&inputVO.period=6&inputVO.stockId=<bean:write name='itemVO' property='stockId'/>" class="datalink">
							<bean:write name="itemVO" property="ticker"/>
						 </A>
					</logic:equal>
					<logic:notEqual name="inputVO" property="type" value="chart">
						<A href="../a/analyze.do?inputVO.mode=analyze&inputVO.type=id&inputVO.ticker=<bean:write name='itemVO' property='ticker'/>&inputVO.period=6&inputVO.stockId=<bean:write name='itemVO' property='stockId'/>" 
						class="datalink">
							<bean:write name="itemVO" property="ticker"/>
						 </A>
					</logic:notEqual>			 					 
					 
					 <%--
			         <br>
					 <A href="http://finance.yahoo.com/q/ta?s=<bean:write name='itemVO' property='ticker'/>" class="datalink" target="_blank">
						Go Yahoo
					 </A>
					 --%>
			    </td>
			    <td class="my_border"><bean:write name="itemVO" property="companyName"/>&nbsp;</td>
			    <td align="center" class="my_border"><bean:write name="itemVO" property="price"/>&nbsp;</td>
			    <td align="center" class="my_border"><bean:write name="itemVO" property="tradeType"/>&nbsp;</td>
			    
			    <logic:equal name="itemVO" property="openDate" value="">		    
			    <td class="my_border">&nbsp;</td>
			    <td class="my_border">&nbsp;</td>
			    </logic:equal>
			    <logic:notEqual name="itemVO" property="openDate" value="">		    
			    <td align="center" class="my_border"><bean:write name="itemVO" property="openDate"/></td>
			    <td align="center" class="my_border"><bean:write name="itemVO" property="openPrice"/></td>
			    </logic:notEqual>
			    
			    <logic:equal name="itemVO" property="closeDate" value="">		    
			    <td class="my_border">&nbsp;</td>
			    <td class="my_border">&nbsp;</td>
			    </logic:equal>
			    <logic:notEqual name="itemVO" property="closeDate" value="">		    
			    <td align="center" class="my_border"><bean:write name="itemVO" property="closeDate"/></td>
			    <td align="center" class="my_border"><bean:write name="itemVO" property="closePrice"/></td>
			    </logic:notEqual>
		    
			    <% double gain = ((com.greenfield.common.object.member.WatchInfo) itemVO).getGainPercent();
			    if (gain < 0) { %>
			    	<td align="center" class="my_border"><font color="#ff0000"><b><bean:write name="itemVO" property="gainPercent"/></b></font></td>
			    <% } else { %>
			    	<td align="center" class="my_border"><font color="#009955"><b><bean:write name="itemVO" property="gainPercent"/></b></font></td>
			     <% } // end if %>
			    
				<td width="20" rowspan="<%= rowspan %>" class="my_border">
			    	<input name="db1" type="button" value="Update" onclick="updateEntry('updatewatch', 'blank', '<bean:write name="itemVO" property="stockId"/>')" class="reg_button"/>
					<%--
					<input name="db2" type="button" value="Delete" onclick="updateEntry('deletewatch', '<bean:write name="inputVO" property="type"/>', '<bean:write name="itemVO" property="stockId"/>')" class="button"/>	
				--%>
				</td>
			</TR>
			<logic:notEqual name="itemVO" property="comments" value="">
				<tr bgcolor="<%= rowColor %>"><td colspan="8" class="my_border"><table><tr><td valign="top"><font color="#ff0000">Note: </font></td>
				<td><font color="#008833">
				<% 
					for (int i = 0; i < comments.length(); i ++) {
						char c = comments.charAt(i);
						if (c == '\n') {
							%><br><% } else { %><%= c %><% }} %>
				</font></td></tr></table></td></tr>
			</logic:notEqual>
			
			<logic:equal name="inputVO" property="type" value="chart">
			<tr><td align="center" colspan="11" class="my_border"><img src="../drawchart?stockId=<bean:write name='itemVO' property='stockId'/>&period=6&option=NNNY"></td></tr>
			</logic:equal>
			<%count++;%>
			</logic:iterate> 
			
			<%if ((count-1) == 0) {%>
				<TR>
					<TD colspan="11" align="center" class="my_border">
						<FONT color="red">No Items found in your watch list.</FONT>
					</TD>
				</TR>
			<%}%>
				
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