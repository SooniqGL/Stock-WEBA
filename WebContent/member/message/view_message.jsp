<%-- 
    Document   : view_message.jsp
    Created on : Mar 24, 2013, 9:00:03 AM
    Author     : Qin
--%>
<!DOCTYPE HTML>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>JSP Page</title>
        
        <%  
        // meta seems not working, setHeader() works fine
        response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
        response.setHeader("Cache-Control","no-store"); //HTTP 1.1
        response.setHeader("Pragma","no-cache"); //HTTP 1.0
        response.setHeader("Expires", "-1");
        response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
        %> 

        <LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
        <LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
        
        <SCRIPT language="javascript">     
 	  
	function submitDelete() {
		var form = document.myMessageForm;
		
		var ret = confirm("You are about to delete the entry. \nClick OK to continue or Cancel to abort action.");
		if (ret == false) {
			return false;
		}
		
                document.getElementById("inputVO.mode").value = "D";
		form.submit();
	}
        
        function submitUpdate() {
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

            var form = document.myMessageForm;
            document.getElementById("inputVO.mode").value = "M";
            document.getElementById("inputVO.type").value = "Update";

            form.submit();
	}
	
        
        function updateStatus() {
            var positionElem = document.getElementById("updateStatusBox");
            var html = "<font color=\"#ff0000\">Not Saved</font>";
            positionElem.innerHTML = html;
        }
	
	
</SCRIPT>
        <style type="text/css">

            .sub_title  {FONT-FAMILY:Arial, Helvetica, sans-serif, Verdana; COLOR:#006699; FONT-SIZE:11pt; FONT-WEIGHT:bold; TEXT-DECORATION:none; }
       </style>

<jsp:useBean id="myMessageForm" scope="session" type="com.greenfield.ui.action.member.MyMessageActionForm"/>
<bean:define id="inputVO" name="myMessageForm" property="inputVO" type="com.greenfield.common.object.member.MyMessageVO"/>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />
    </head>
    <body>
        <html:form action="/m/mymessage"> 
                <input type="hidden" id="inputVO.mode" name="inputVO.mode" value="D" />
                <input type="hidden" id="inputVO.type" name="inputVO.type" value="D" />
                <input type="hidden" name="inputVO.messageType" value="<bean:write name='inputVO' property='messageType'/>" />
		<input type="hidden" name="inputVO.messageSeq" value="<bean:write name='inputVO' property='messageSeq'/>" />
                
        <%-- display nothing if  the MDate is not set, this means the query does not get data --%>
        <logic:notEqual name="inputVO" property="MDate" value="">
         <logic:equal name="inputVO" property="mode" value="V">   
            <% String content = inputVO.getContent();
                if (content != null) {
                    content = content.replaceAll("\n", "<br />");
                }
            %>
        
        <table border="0" width="100%"><tr><td class="sub_title" valign="top">Subject: </td><td><bean:write name='inputVO' property='subject'/></td></tr>
                <tr><td class="sub_title" valign="top">Time: </td><td><bean:write name='inputVO' property='MDate'/></td></tr>
                
                <logic:equal name="inputVO" property="messageType" value="S">
                
                <tr><td class="sub_title" valign="top">Functions: </td>
                    <td>
                        <%--
                        <a href="#" onclick="JavaScript: submitDelete(); return false;" target="_top" class="datalink2">Delete</a>
                        &nbsp;|&nbsp; 
                        --%>
                        <a href="../m/mymessage.do?inputVO.mode=M&inputVO.messageType=<bean:write name='inputVO' property='messageType'/>&inputVO.messageSeq=<bean:write name='inputVO' property='messageSeq'/>" class="datalink" tooltip="can modify">Modify</a>     
                        
                    </td></tr>
                
                </logic:equal>
                        
            <tr><td class="sub_title" valign="top">Content: </td><td><%= content %></td></tr>
        </table>
        </logic:equal>
        
         <logic:equal name="inputVO" property="mode" value="M">   
            
        
        <table border="0" width="100%">
                <tr><td class="sub_title" valign="top">Time: </td><td><bean:write name='inputVO' property='MDate'/></td></tr>       
                <tr><td class="sub_title" valign="top">Status </td><td><div id="updateStatusBox"><font color="#00ff00">Saved</font></div></td></tr>
            
            
            <tr><td><font class="sub_title">Subject</font></td><td>
                    <input type="text" id="inputVO.subject" name="inputVO.subject" value="<bean:write name='inputVO' property='subject'/>" onchange="updateStatus()" size="60" maxLength="100" >	
		</td>
		</tr> 
		<tr>
                    <td><font class="sub_title">Content (2000ch Max)</font></td><td> 
                            <TEXTAREA id="inputVO.content" name="inputVO.content" rows="20" cols="45" onchange="updateStatus()"><bean:write name='inputVO' property='content'/></TEXTAREA>
                    </td>
                </tr>
            
            <tr><td colspan="2" align="center"><input type="button" name="sb" value="Modify Message" onClick="submitUpdate()"></td></tr>
        </table>
        </logic:equal>
            
        </logic:notEqual>
        
        
        </html:form>
    </body>
</html>
