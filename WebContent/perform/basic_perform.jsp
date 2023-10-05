<!DOCTYPE HTML>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@page errorPage="/error.jsp" %>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%> 

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
  
<TITLE>basic_perform.jsp</TITLE>

<%  
// meta seems not working, setHeader() works fine
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "-1");
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
%> 

<SCRIPT language="javascript">     
 	
		 
	function submitRequest(mode, trend) {
		var form = document.performForm;
		form.all["inputVO.mode"].value = mode;
		form.all["inputVO.trend"].value = trend;
		form.submit();
	}
	
	function submitRequest2(type) {
		var form = document.scanForm;
		form.all["inputVO.type"].value = type;
		form.submit();
	}
		
</SCRIPT>
<jsp:useBean id="performForm" scope="session" type="com.greenfield.ui.action.perform.PerformActionForm"/>
<bean:define id="performVO" name="performForm" property="inputVO" type="com.greenfield.common.object.perform.PerformVO"/>
<bean:define id="scanList" name="performVO" property="scanList" type="java.util.Vector" /> <%-- Collection"/> --%>
 	
</HEAD> 
<BODY topmargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<center><table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0"> 
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" cellpadding="0" cellspacing="0"> 
	<tr><td height="40" align="center" colspan="2" class="head_color"><font class="main_title">Our Performances</font></td></tr>
	
	<tr><td colspan="2">
	<html:form action="/p/perform"> 
		<html:hidden property="inputVO.mode" value="perform"/>		
		<html:hidden property="inputVO.type" value="show"/>
		<html:hidden property="inputVO.trend" value=""/>
		 
		<table border="1" width="100%" bgcolor="#cccccc">
			<tr class="gray_back"><td align="center"><input name="sb1" type="button" value="Linear Up"   onclick="submitRequest('L', 'U')" class="button"/></td>
				<td align="center"><input name="sb2" type="button" value="Linear Down" onclick="submitRequest('L', 'D')" class="button"/></td>	
			    <td align="center"><input name="sb3" type="button" value="Break Up"    onclick="submitRequest('B', 'U')" class="button"/></td>
				<td align="center"><input name="sb4" type="button" value="Break Down"  onclick="submitRequest('B', 'D')" class="button"/></td>	
			    <td align="center"><input name="sb5" type="button" value="Growth Up"   onclick="submitRequest('G', 'U')" class="button"/></td>
				<td align="center"><input name="sb6" type="button" value="Growth Down" onclick="submitRequest('G', 'D')" class="button"/></td>	
			</tr>
		</table>
	<logic:notEqual name="performVO" property="mode" value="blank">
	<table width="100%" border="1"><tr bgcolor="#bbffcc">
	   <td colspan="9">
		<logic:equal name="performVO" property="type" value="show">
			<input type="button" name="sb" value="Charts" onClick="submitRequest2('chart')">
		</logic:equal>
		<logic:notEqual name="performVO" property="type" value="show">
			<input type="button" name="sb" value="List" onClick="submitRequest2('show')">
		</logic:notEqual>
		<font color="#ff5555"><b><bean:write name="performVO" property="showTitle"/></b></font></td></tr>
		
		<tr class="gray_back"><td>Order</td><td>Ticker</td><td>Company</td><td>Long/Short</td>
			<td>Enter Date</td><td>Enter Price</td><td>Close Date</td><td>Close Price</td>
			<td>Gain/Loss</td>
			</tr>
			<%int count = 1;%>	
		    
			<logic:iterate id="itemVO" name="scanList">
			<TR bgcolor="#ffffff">
			<TD align="center"> 
				<%=count%>
			</TD>
			<TD align="center" width="80">
				 <A href="../a/analyze.do?inputVO.mode=analyze&inputVO.type=id&inputVO.ticker=<bean:write name='itemVO' property='ticker'/>&inputVO.period=6&inputVO.stockId=<bean:write name='itemVO' property='stockId'/>" class="datalink">
					<bean:write name="itemVO" property="ticker"/>
				 </A>
		         <br>
				 <A href="http://finance.yahoo.com/q/ta?s=<bean:write name='itemVO' property='ticker'/>" class="datalink" target="_blank">
					Go Yahoo
				 </A><br>
				 [<A href="../m/watch.do?inputVO.mode=addwatch&inputVO.type=one&inputVO.stockId=<bean:write name='itemVO' property='stockId'/>" class="datalink">Watch</a>]	
		
		    </td>
		    <td><bean:write name="itemVO" property="companyName"/></td>
		    <td><bean:write name="itemVO" property="longShort"/></td> 
		    <td><bean:write name="itemVO" property="scanDate"/></td>
		    <td><bean:write name="itemVO" property="scanPrice"/></td>
		    <td><bean:write name="itemVO" property="closeDate"/></td>
		    <td><bean:write name="itemVO" property="closePrice"/></td>
		    <td><bean:write name="itemVO" property="gain"/></td>
			</TR>
			<logic:notEqual name="performVO" property="type" value="show">
			<tr><td colspan="9"><img src="../drawchart?stockId=<bean:write name='itemVO' property='stockId'/>&period=6"></td></tr>
			</logic:notEqual>
			<%count++;%>
			</logic:iterate> 
			
			<%if ((count-1) == 0) {%>
				<TR>
					<TD colspan="9" align="center">&nbsp;
						<FONT color="red">No Items found for the searched criteria.</FONT>
					</TD>
				</TR>
			<%}%> 
			
		</table>   
		
	</logic:notEqual>
		 </html:form>
	</td></tr> 
	
	<tr><td height="150" colspan="2">&nbsp;</td></tr>
	</table>
</td></tr>    
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
</table></center>
</BODY>
</HTML>
