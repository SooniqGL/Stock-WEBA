<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">

<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1" %>

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<TITLE>_signon.jsp</TITLE>
</HEAD>
<BODY class="page_back">
<DIV align="left">
<TABLE width="812">
	<TBODY> 
		<TR>
			<TD colspan="2" bgcolor="#ffffff" height="50px"><%@include file="header.jsp" %>
			</TD>
		</TR>
		<TR>
			<TD colspan="2" bgcolor="#dfffff" width="768" align="center">
				<FONT color="red">System Error Occurred Processing your request.<BR></FONT>
				<FONT color="red">Report This Error To Your Administrator.</FONT>
			</TD>
		</TR>
		<TR>
			<TD bgcolor="#adadad" colspan="2" align="center"><b><i>
			<%
				Throwable th = (Throwable) request.getAttribute("exception");
				if(th != null) {
			%>
				<%=th.getMessage()%>
					
			<%}%>			
			</i></b>
			</TD>
		</TR>
		<TR>
			<%@include file="footer.jsp" %>
		</TR>
	</TBODY>
</TABLE>
</DIV>
</BODY>
</HTML>
