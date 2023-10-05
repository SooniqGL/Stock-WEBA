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
<SCRIPT SRC="../script/common.js"></SCRIPT>
<TITLE>New Forum Message</TITLE>  

<%  
// meta seems not working, setHeader() works fine
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "-1");
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
%> 

<SCRIPT language="javascript">    
  	  
	function submitRequest(mode) {
		var form = document.forumForm;
		
		if (form.all["inputVO.forumMessage.subject"].value == "") {
			alert("Subject is required.");
			return false;
		}
		
		if (form.all["inputVO.forumMessage.content"].value.length > 2000) {
			alert("Content is too long, limit to 2000ch please.");
			return false;
		}
		
		
		form.all["inputVO.mode"].value = mode;
		
		form.submit();
	}
	
	function submitRequest2(mode) {
		var form = document.forumForm;
		
		form.all["inputVO.mode"].value = mode;
		
		form.submit();
	}

	
	
</SCRIPT>	
<jsp:useBean id="forumForm" scope="session" type="com.greenfield.ui.action.forum.ForumMessageActionForm"/>
<bean:define id="inputVO" name="forumForm" property="inputVO" type="com.greenfield.common.object.forum.ForumVO"/>
<bean:define id="forumMessage" name="inputVO" property="forumMessage" type="com.greenfield.common.object.forum.ForumMessage"/>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />
</HEAD>
<BODY class="page_back">

		<html:form action="/f/forum"> 
		<html:hidden property="inputVO.mode" value="q"/>
		<input type="hidden" name="inputVO.subjectId" value="<bean:write name='inputVO' property='subjectId'/>"/>
		<input type="hidden" name="inputVO.parentId" value="<bean:write name='inputVO' property='parentId'/>"/>
		
	<div id="outcontainer">
        <div id="maincontainer">

            <div id="topsection"><div class="innertube2">
                    <jsp:include page="../base/topmenu.jsp">
                        <jsp:param name="subtitle" value="Forum" />
                        <jsp:param name="fname" value="<%= security.getFname() %>" />
                        <jsp:param name="lname" value="<%= security.getLname() %>" />  
                    </jsp:include>
            </div></div>
            <div id="contentwrapper">
            <div id="contentcolumn">
                <div class="innertube2">
                    
		<table border="1" width="100%" style="background:#ffffef;">
			<tr class="member_back">
				<td height="30">&nbsp;
				<font class="sub_title">Forum Messages - New Message</font></td>
				<td>
				<input name="db1" type="button" value="Back to Message" onclick="submitRequest2('q')" class="reg_button"/>
				</td>
			</tr>
			  	
			<tr><td><font class="sub_title2">Subject</font></td>
				<td>
					<input type="text" name="inputVO.forumMessage.subject" size="60" maxlength="100" value="<bean:write name='forumMessage' property='subject'/>" />
				</td>
			</tr>
			<TR>
			<TD><font class="sub_title2">Content (2000ch Max)</font></td><td> 
				<TEXTAREA name="inputVO.forumMessage.content" rows="10" cols="60"><bean:write name="forumMessage" property="content"/></TEXTAREA>
			</TD>
			</tr>
			<tr><td colspan="2" align="center">
			<input name="db2" type="button" value="Create Message" onclick="submitRequest('na')" class="reg_button"/>
			</td></tr>
			
		   <tr>
		    <td align="center" colspan="2"></td>
			</TR>
			
		</table>
            </div>
            </div>
            </div>

            <div id="leftcolumn">
            <div class="innertube"><%--@include file="../base/left2.jsp" --%>
                <a href="#" class="sublink">Nasdaq Index</a><br/>
                <a href="#" class="sublink">Dow Index</a><br/>
                <a href="#" class="sublink">SP500 Index</a>
            </div>
            </div>

            <%@include file="../base/footer.jsp" %>

        </div>
    </div>        
	</html:form>
	
</BODY>
</HTML>
