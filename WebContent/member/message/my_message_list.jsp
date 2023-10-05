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
 
<TITLE>Watch List</TITLE>
<%  
// meta seems not working, setHeader() works fine
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "-1");
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
%> 

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
	
	
</SCRIPT>	

<script type="text/javascript" language="JavaScript">
// Init section
 
	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	//run_after_body();
</script>
<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

</style>
<jsp:useBean id="myMessageForm" scope="session" type="com.greenfield.ui.action.member.MyMessageActionForm"/>
<bean:define id="inputVO" name="myMessageForm" property="inputVO" type="com.greenfield.common.object.member.MyMessageVO"/>
<bean:define id="linkList" name="inputVO" property="linkList" type="java.util.Vector"/>
<bean:define id="messageList" name="inputVO" property="messageList" type="java.util.Vector"/>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />

</HEAD> 
<BODY class="page_back">

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
            
            <table width="100%" cellpadding="0" cellspacing="0"> 
	 

	<tr><td colspan="2" valign="top">
		<html:form action="/m/mymessage"> 
		<html:hidden property="inputVO.mode" value="L"/>		
		<html:hidden property="inputVO.type" value=""/>
		<input type="hidden" name="inputVO.messageType" value="<bean:write name='inputVO' property='messageType'/>" />
		
		
		<table border="1" width="100%" class="light_back2">
			<tr class="member_back"><td colspan="3" align="center">My Messages</td></tr>
                        <tr><td>
                            <logic:equal name="inputVO" property="messageType" value="S">
                                    Message To Myself
                            </logic:equal>
                            <logic:notEqual name="inputVO" property="messageType" value="S">
                                    <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=S&inputVO.currLink=1" class="datalink">Message To Myself</a>
                            </logic:notEqual>    
                        </td>
                        <td>
                            <logic:equal name="inputVO" property="messageType" value="U">
                                    Message To Sooniq
                            </logic:equal>
                            <logic:notEqual name="inputVO" property="messageType" value="U">
                                    <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=U&inputVO.currLink=1" class="datalink">Message To Sooniq</a>
                            </logic:notEqual>    
                        </td>
                        <td>
                            <logic:equal name="inputVO" property="messageType" value="A">
                                    Message From Sooniq
                            </logic:equal>
                            <logic:notEqual name="inputVO" property="messageType" value="A">
                                    <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=A&inputVO.currLink=1" class="datalink">Message From Sooniq</a>
                            </logic:notEqual>    
                        </td>
                        </tr>
                
                        <tr><td colspan="3">
                                <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.currLink=1" class="datalink">First</a> &nbsp;
                                <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.currLink=<bean:write name='inputVO' property='prevLink'/>" class="datalink">Prev</a> &nbsp;
                                
                                <logic:iterate id="linkText" name="linkList"> 
                                    <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.currLink=<bean:write name='linkText' />" class="datalink"><bean:write name="linkText" /></a> &nbsp;
                                </logic:iterate>
                                <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.currLink=<bean:write name='inputVO' property='nextLink'/>" class="datalink">Next</a> &nbsp;
                                    
                                <a href="../m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.currLink=<bean:write name='inputVO' property='lastLink'/>" class="datalink">Last</a>
                                
                            </td>
                        </tr>
                        
			<td colspan="3" class="my_border">
                            <logic:notEqual name="inputVO" property="firstMessageSeq" value="">
                            <table border="1" width="100%"><tr><td valign="top" width="300"><table width="100%">
                                
			
			<%int count = 1;%>	 
		
			<logic:iterate id="messageItem" name="messageList">
			<%	
				String rowColor = "";
				if (count % 2 == 1) {
					rowColor = "#ffffff";
				} else {
					rowColor = "#ffffdf";
				}	
			%>
			<TR bgcolor="<%= rowColor %>">
				<TD align="center" class="my_border2">
					<bean:write name='messageItem' property='messageSeq'/>
				</TD>
                                <TD align="center" class="my_border2"><%-- Note, have to use MDate, not mDate here --%>
					<bean:write name='messageItem' property='MDate'/>
				</TD>
                                
                                <TD align="center" class="my_border2">
					
                                        <a href="../m/mymessage.do?inputVO.mode=V&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.messageSeq=<bean:write name='messageItem' property='messageSeq'/>" target="messageDisplayBox" class="datalink2"><bean:write name='messageItem' property='subject'/></a>
                                
				</TD>
                        </tr>
                        
                        <% count++; %>
                        </logic:iterate>
                                        </table></td>
                                        <td>
                                            
                                            <%-- put IFrame here --%>
                                            <iframe name="messageDisplayBox" src="../m/mymessage.do?inputVO.mode=V&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.messageSeq=<bean:write name='inputVO' property='firstMessageSeq'/>" width="450" height="500" frameBorder="0">Your browser has problem to display this info.  Please try IE9+.
                                            </iframe>
                                            
                                        </td>

                                </tr></table>
                        </logic:notEqual>
                        
                            <logic:equal name="inputVO" property="firstMessageSeq" value="">
                                No message is found.
                            </logic:equal>
                        
                        </td></tr>	
		</table>
                                
                Note: 1) You may write message to yourself.  Only you can view these messages.  You may modify these messages anytime.  
                2) You may write messages to Sooniq.  These messages cannot be modified.  
                3) This page also displays the messages that Sooniq has sent to you.
	</html:form>
	</td></tr> 
	
	</table>
        </div>
            </div>
            </div>

            <div id="leftcolumn">
            <div class="innertube"><%--@include file="../base/left2.jsp" --%>
                <a href="../m/home.do?inputVO.mode=blank" class="sublink">Member Home</a><br/>
                <a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="sublink">Member Profile</a><br />
                <a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="sublink">Alert List</a><br />
                <a href="../m/mymessage.do?inputVO.mode=N&inputVO.type=blank" class="sublink">New Message</a>
            </div>
            </div>

            <%@include file="../base/footer.jsp" %>

        </div>
    </div>              
</BODY>
</HTML>