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
<SCRIPT SRC="../script/onmouse.js"></SCRIPT>
  
<TITLE>New Message</TITLE>

<%  
// meta seems not working, setHeader() works fine
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "-1");
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
%> 

<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.myMessageForm;
		
                var subject = document.getElementById("inputVO.subject").value;
		if (subject == "") {
			alert("Subject is required.");
			return false;
		}
                
                var content = document.getElementById("inputVO.content").value;
		if (content != "" && content.length > 2000) {
			alert("Max length of the content field is 2000 characters.");
			return false;
		}
		
		form.submit();
	}
	
	function setFocus() { 
          //document.getElementById("inputVO.ticker").select();
          document.getElementById("inputVO.subject").focus();
	} 
        
        function setMessageType(messageType) {
            document.getElementById("inputVO.messageType").value = messageType;
        }
	
</SCRIPT>	

<jsp:useBean id="myMessageForm" scope="session" type="com.greenfield.ui.action.member.MyMessageActionForm"/>
<bean:define id="inputVO" name="myMessageForm" property="inputVO" type="com.greenfield.common.object.member.MyMessageVO"/>
<%--bean:define id="messageList" name="inputVO" property="messageList" type="java.util.Vector"/--%>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />

</HEAD>
<BODY onload="setFocus()" class="page_back">
<div id="outcontainer">
        <div id="maincontainer">

            <div id="topsection"><div class="innertube2">
                    <jsp:include page="../base/topmenu.jsp">
                        <jsp:param name="subtitle" value="Member Account" />
                        <jsp:param name="fname" value="<%= security.getFname() %>" />
                        <jsp:param name="lname" value="<%= security.getLname() %>" />  
                    </jsp:include>
            </div></div>

            <div id="contentwrapper">
            <div id="contentcolumn">
                <div class="innertube2">
            
            
            <table width="100%" height="100%" cellpadding="0" cellspacing="0" class="light_back2"> 
	
	<tr><td colspan="2" align="center" height="50"><a href="../m/home.do?inputVO.mode=blank" class="toplink">Member Home</a>
		&nbsp;|&nbsp;<a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="toplink">Profile</a>
		&nbsp;|&nbsp;<a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="toplink">Alert List</a></td></tr>
		
	<tr><td colspan="2" width="100%" valign="top">
	<html:form action="/m/mymessage"> 
			<html:hidden property="inputVO.mode" value="N"/>		
			<html:hidden property="inputVO.type" value="add"/>
                        <input type="hidden" id="inputVO.messageType" name="inputVO.messageType" value="S" >	
		<table border="1" width="100%">
		<tr class="member_back"><td colspan="2">&nbsp;
		<font class="sub_title">Write New Message</font></td></tr>
                
                <tr><td>To Whom</td>
                    <td><input type="radio" name="ckMessageType" value="S" onClick="setMessageType('S')" checked >To Me Only &nbsp;&nbsp;&nbsp;&nbsp;
                        <input type="radio" name="ckMessageType" value="U" onClick="setMessageType('U')" >To Sooniq</td></tr>
		<tr><td><font class="sub_title22">Subject</font></td><td>
                    <input type="text" id="inputVO.subject" name="inputVO.subject" value="" size="80" maxLength="100" >	
		</td>
		</tr> 
		<tr>
                    <td><font class="sub_title22">Content (2000ch Max)</font></td><td> 
                            <TEXTAREA id="inputVO.content" name="inputVO.content" rows="20" cols="60"></TEXTAREA>
                    </td>
                </tr>
                
                <tr><td colspan="2" align="center"><input type="button" name="sb" value="Create Message" onClick="submitRequest()">
		</td></tr>
                
		</table></html:form></td></tr>
	
	</table>
        </div>
            </div>
            </div>

            <div id="leftcolumn">
            <div class="innertube"><%--@include file="../base/left2.jsp" --%>
                <a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="sublink">Member Profile</a><br />
                <a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="sublink">Alert List</a><br />
                <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank" class="sublink">My Messages</a>
            </div>
            </div>

            <form name="goStockAgeForm" action="../s/agesrch.do">
                <input type="hidden" name="inputVO.mode" value="search"/>
                <input type="hidden" name="inputVO.selectRange" value=""/>
            </form>
            <%@include file="../base/footer.jsp" %>

        </div>
    </div>              
</BODY>
</HTML>