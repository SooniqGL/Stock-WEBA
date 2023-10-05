<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>

<?xml version="1.0" encoding="UTF-8" ?>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.1//EN" "http://www.w3c.org/TR/xhtml11/DTD/xhtml11.dtd">
<html xmlns="http://www.w3.org/1999/xhtml" xml:lang="en">
<HEAD>
    
    <%--@ page
    language="java"
    contentType="text/html; charset=ISO-8859-1"
    pageEncoding="ISO-8859-1"
    --%>
    <meta http-equiv="content-type" content="application/xhtml+xml; charset=UTF-8"/>
    <meta http-equiv="expires" content="0"/>
    <meta http-equiv="Cache-Control" content="no-cache" />

    <META name="GENERATOR" content="IBM WebSphere Studio">
    <META http-equiv="Content-Style-Type" content="text/css">

    <LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
    <LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
    <TITLE>login.jsp</TITLE>
    <!--script type="text/javascript" src="../../script/common.js"></script-->
    <script LANGUAGE="JAVASCRIPT">
        function setFocus() {
          document.mobiLoginForm.elements["inputVO.loginId"].focus();
        }
    </script>
</HEAD> 
<BODY topmargin="0" leftmargin ="0" marginheight="0" marginwidth="0" onload="setFocus()">
<center><TABLE width="450" cellspacing="0"  cellpadding="0" cellspacing="0">
		<TR>
			<TD class="head_color"><%@include file="login_header.jsp" %></TD>
		</TR>  
		<TR>
			<TD align="center"  height="100%" width="100%"  background="../../image/brick.gif">
			<CENTER>  
			<html:form action="/mobi/login">
				<html:hidden property="inputVO.mode" value="mobilogin"/>
				<TABLE border="1" width="300"> 
					<TBODY> 
						
						 
						
						<TR>
							<TD height="35">&nbsp;<FONT color="#0000ff"><b>Login ID</b></FONT></TD>
							<TD height="35">
								<html:text property="inputVO.loginId" size="15"></html:text>
							</TD> 
						</TR>   
						<TR>
							<TD height="35">&nbsp;<FONT color="#0000ff"><b>Password</b></FONT></TD>
							<TD height="35">
								<html:password value="" property="inputVO.password" size="15"></html:password>
							</TD>  
						</TR>
						<TR>
							<TD>&nbsp;</TD>
                                                        <TD align="left" height="40">
                                                                <INPUT type="submit" name="login" value="Login"/>

                                                        </TD>
						</TR>
					</TBODY>
				</TABLE>

				<table><tr><td height="30">&nbsp;</td></tr></table>
			</html:form>
			<FONT color="red"><I>
				<% String loginError = (String)request.getAttribute("error"); %>
				<% if(loginError == null) loginError = ""; %>
				<%=loginError%>
			</I></FONT>
			<FONT color="blue"><I>
				<% String logoutConfirm = (String)request.getAttribute("confirm"); %>
				<% if(logoutConfirm == null) logoutConfirm = ""; %>
				<%=logoutConfirm%>
			</I></FONT>
			<FONT color="red"><I><html:errors/></I></FONT>
			</CENTER>
			</TD>
		</TR>
		
		   
	<tr class="tail_color"><td><%@include file="login_footer.jsp" %></td></tr>
        <tr><td>&nbsp;</td></tr>
	</TABLE>
</center>
</BODY>
</HTML>
