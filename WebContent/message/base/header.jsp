<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>

<style type="text/css">
#topDate {
    color:#ffffef;
    font-size:14px;
}
</style>

<%
String theDate = com.greenfield.common.util.DateUtil.getDateStringInLongFormat(null);
%>

<div style="text-align:center;" class="titlearea">
        <h1>Sooniq Message Board <span id="topDate"><%= theDate %> <c:out value="${user.nickname}" /> &nbsp;
        
        <a href="/WEBA/s/home.do?inputVO.mode=blank"  class="sublink22">Home</a> &nbsp;&nbsp;
        <A href="/WEBA/p/signoff.do" class="sublink2">Logout</A></span></h1>
</div>




       