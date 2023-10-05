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
<%@ page import="java.util.Vector" %>
<%@ page import="java.util.HashMap" %>
<%@ page import="com.greenfield.common.object.forum.ForumMessage" %>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
<SCRIPT SRC="../script/common.js"></SCRIPT>
<SCRIPT SRC="../script/forum.js"></SCRIPT>
<TITLE>Forum Message List</TITLE>  
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
		
		form.all["inputVO.mode"].value = mode;
		
		form.submit();
	}
	
	function submitRequestQ(subjectId) {
		var form = document.forumForm;
		
		form.all["inputVO.mode"].value = "q";
		form.all["inputVO.subjectId"].value = subjectId;
		
		form.submit();
	}

	
	
</SCRIPT>	

<script language="JavaScript1.2">
<!--

NS4 = (document.layers) ? 1 : 0;
IE4 = (document.all) ? 1 : 0;
ver4 = (NS4 || IE4) ? 1 : 0;
if (ver4) {    
	with (document) {        
		write("<STYLE TYPE='text/css'>");        
		if (NS4) {            
			write(".parent {position:absolute; visibility:visible}");            
			write(".child {position:absolute; visibility:visible}");            
			write(".regular {position:absolute; visibility:visible}")        
		} else {            
			write(".child {display:none; background-color:#ffffff}")        
		}        
		write("</STYLE>");    
	}
}

state = "hide";

//onload = initKPEIt;
//MM_reloadPage(true);

//-->
</script>

<jsp:useBean id="forumForm" scope="session" type="com.greenfield.ui.action.forum.ForumMessageActionForm"/>
<bean:define id="inputVO" name="forumForm" property="inputVO" type="com.greenfield.common.object.forum.ForumVO"/>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />

</HEAD>
<BODY class="page_back">
            <html:form action="/f/forum"> 
            <html:hidden property="inputVO.mode" value="q"/>
            <input type="hidden" name="inputVO.subjectId" value="<bean:write name='inputVO' property='subjectId'/>"/>
            <input type="hidden" name="inputVO.parentId" value="0"/>
            <input type="hidden" name="state" value="show"/>
	
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

		<table border="1" width="100%" style="background:#ffe;">
			<tr class="member_back"><td height="30" colspan="2">&nbsp;
			<font class="sub_title">Forum Messages</font> &nbsp;&nbsp;
			<input name="db1" type="button" value="Start New Topic" onclick="submitRequest('nb')" class="reg_button"/>
			<logic:equal name="inputVO" property="prevSubjectId" value="0">
				<input name="db2" type="button" value="Prev 10" disabled class="button"/>
			</logic:equal>
			<logic:notEqual name="inputVO" property="prevSubjectId" value="0">
				<input name="db2" type="button" value="Prev 10" onclick="submitRequestQ('<bean:write name='inputVO' property='prevSubjectId'/>')" class="button"/>
			</logic:notEqual>
				
			<input name="db3" type="button" value="Refresh" onclick="submitRequestQ('<bean:write name='inputVO' property='subjectId'/>')" class="button"/>
			<logic:equal name="inputVO" property="nextSubjectId" value="0">
				<input name="db4" type="button" value="Next 10" disabled class="button"/>
			</logic:equal>
			<logic:notEqual name="inputVO" property="nextSubjectId" value="0">
				<input name="db4" type="button" value="Next 10" onclick="submitRequestQ('<bean:write name='inputVO' property='nextSubjectId'/>')" class="button"/>
			</logic:notEqual>
			
			<input name="db5" type="button" value="Expand" class="button" onClick="showAll(); return false"/>
			</td>
			</tr>
			
			
			
			<tr><td width="60%"><font class="sub_title2">Subject</font></td><td width="40%"><font class="sub_title2">Author / Time</font></td></tr>    	
			<tr><td colspan="2">
			<% Vector subjectList = inputVO.getMessageList(); 
				if (subjectList != null && subjectList.size() > 0) {
				for (int i = 0; i < subjectList.size(); i ++) {
					ForumMessage fm = (ForumMessage) subjectList.get(i);
					String content = fm.getContent(); %>
				
				
				<div id='<%= fm.getTagId() %>' class='parent'><table width="100%">
				<tr bgcolor="#ffffef"><td width="60%"><% if (fm.getIndent() > 0) { 
					for (int j = 0; j < fm.getIndent(); j ++) { %>--><% } } %>
					<% if (fm.getHasChildren() != null && fm.getHasChildren().equals("Y")) { %>
				<a class=datalink href="#" onClick="showChildren('<%= fm.getTagId() %>'); return false">+/-</a>
				<% } else { %> &nbsp; 
				<% } %>
				<a class=datalink href="#" onClick="showHideElem('<%= fm.getTagId() %>C'); return false"><%= fm.getSubject() %></a></td>
				
				<td width="30%"><%= fm.getLoginId() %> (<%= fm.getMDate() %>)</td>
				<td width="10%"><a class=datalink href="../f/forum.do?inputVO.mode=nb&inputVO.parentId=<%= fm.getMessageId() %>&inputVO.subjectId=<bean:write name='inputVO' property='subjectId'/>">Reply</a></td>
				</tr></table>
				</div>
				<div id='<%= fm.getTagId() %>C' class='child'><table>
					<tr><td>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</td><td><% if (fm.getIndent() > 0) { 
					for (int j = 0; j < fm.getIndent(); j ++) { %>&nbsp;&nbsp;&nbsp;<% } } %>
					<% 
					if (content != null) {
					for (int j = 0; j < content.length(); j ++) {
						char c = content.charAt(j);
						if (c == '\n') {
							%><br><% } else { %><%= c %><% }}} else { %>Empty<% } %></td>
					</tr>
					</table></div>
								
				<% } } else {  %>&nbsp;
				<% } %>
			</td></tr>
		 
			
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
