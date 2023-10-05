<%@page isErrorPage="true" %>
<%@page contentType="text/html" %>
<%@page pageEncoding="UTF-8" %>

<%-- For all JSP errors, we forward to the login page ... 
     errorPage page directive is added to all JSP's.  This purpose to do this is 
     to prevent user to access JSP directly but session is expired.
--%>
<%-- request.setAttribute("error", "There was an error in processing your request.  Please retry."); --%>
<jsp:forward page="/b/login.do?inputVO.mode=blank"/>