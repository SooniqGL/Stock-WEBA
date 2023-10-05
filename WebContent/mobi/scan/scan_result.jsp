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
<TITLE>scan_result.jsp</TITLE>
<SCRIPT language="javascript">    
   
	function submitRequest(type) {
		var form = document.scanForm;
		form.all["inputVO.type"].value = type;
		form.submit();
	}
	
</SCRIPT>	
<jsp:useBean id="scanForm" scope="session" type="com.greenfield.ui.action.scan.ScanActionForm"/>
<bean:define id="scanVO" name="scanForm" property="inputVO" type="com.greenfield.common.object.scan.ScanVO"/>
<bean:define id="scanList" name="scanVO" property="scanList" type="java.util.Vector" /> <%-- Collection"/> --%>
 
</HEAD>
<BODY topmargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<center><table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0"> 
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" cellpadding="0" cellspacing="0"> 
	<tr><td height="40" align="center" colspan="2" class="head_color"><font  class="main_title">Search Market</font></td></tr>
			 		
	<tr bgcolor="#33aa88">
		<td colspan="2"><font color="#ffffff">Search Results</font></td>
	</tr>
	 
	<tr>   
		<td colspan="2" height="200" valign="top">
		<html:form action="/s/scan"> 
		<input type="hidden" name="inputVO.mode" value="<bean:write name='scanVO' property='mode'/>"/>		
		<input type="hidden" name="inputVO.type" value="<bean:write name='scanVO' property='type'/>"/>
		<input type="hidden" name="inputVO.trend" value="<bean:write name='scanVO' property='trend'/>"/>
		<input type="hidden" name="inputVO.period" value="<bean:write name='scanVO' property='period'/>"/>
		<input type="hidden" name="inputVO.range" value="<bean:write name='scanVO' property='range'/>"/>
		<input type="hidden" name="inputVO.basePattern" value="<bean:write name='scanVO' property='basePattern'/>"/>
		<input type="hidden" name="inputVO.dataType" value="D" />
		
		<table width="100%" border="1"><tr class="gray_back"><td colspan="5">
		<logic:equal name="scanVO" property="type" value="show">
			<input type="button" name="sb" value="Charts" onClick="submitRequest('chart')">
		</logic:equal>
		<logic:notEqual name="scanVO" property="type" value="show">
			<input type="button" name="sb" value="List" onClick="submitRequest('show')">
		</logic:notEqual>
		<bean:write name="scanVO" property="showTitle"/></td></tr>
		
		<tr class="gray_back"><td>Order</td><td>Ticker</td><td>Company</td><td>Comments</td><td>Price</td></tr>
			<%int count = 1;%>	
		
			<logic:iterate id="itemVO" name="scanList">
			<TR>
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
		    <td><bean:write name="itemVO" property="comments"/></td> 
		    <td><bean:write name="itemVO" property="price"/></td>
			</TR>
			<logic:notEqual name="scanVO" property="type" value="show">
			<tr><td colspan="5"><img src="../drawchart?stockId=<bean:write name='itemVO' property='stockId'/>&period=6"></td></tr>
			</logic:notEqual>
			<%count++;%>
			</logic:iterate> 
			
			<%if ((count-1) == 0) {%>
				<TR>
					<TD colspan="5" align="center">
						<FONT color="red">No Items found for the searched criteria.</FONT>
					</TD>
				</TR>
			<%}%> 
			
		</table>   
		 <ul><li>We only search onces that have the average volume &gt;= 300K.</li>
		 <li>For linear model, we ask the growth per month at least 10 percent.</li>
		 <li>About RSquare --
		 RSquare is a number between 0 and 1.  If it is 1, the chart is exactly a straight line.
		 The closer to 1 the closer the chart to the line.
		 </li></ul>
		 </html:form>
	</td>
	</tr> 
	<tr><td colspan="2" height="20">&nbsp;</td></tr>
	</table>
</td></tr>
	<tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
</table></center>
</BODY>
</HTML>

