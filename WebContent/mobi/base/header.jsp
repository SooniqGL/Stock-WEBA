<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />
<%
    String subtitle = request.getParameter("subtitle");
    if (subtitle == null) {
        subtitle = "";
       }
   %>
<table height="30" width="100%"  border="0" cellpadding="0" cellspacing="0">
	<tr>
		<td colspan="2" align="center" valign="middle" height="30"><font class="mobi_title">Sooniq Stock Systems </font> - <font class="mobi_subtitle"><%= subtitle %></font>
                </td>
        </tr>

        <%-- when bean.write is used here, <%@ taglib for prefix bean is necessary to declaim --%>
        <%--
        <tr>
		<td valign="bottom" align="right" colspan="2">
                    <font color="#ffff4f"><b>User: <bean:write name="security" property="fname"/> <bean:write name="security" property="lname"/></b></font></td>
		
                <td><A class="sublink" href="../b/signoff.do">Logout</A></td>
  
	</tr> --%>
</table>