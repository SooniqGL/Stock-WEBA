<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="s" uri="/struts-tags" %>

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
        <h1>Sooniq Stock System <span id="topDate"><%= theDate %> <c:out value="${user.nickname}" /> <A class="sublink2" href="/WEBA/p/signoff.do">Logout</A>

				<s:if test="%{sessionContext.contentMap[\"ISMOBILE\"] == \"Y\"}">
					(mobile)	
				</s:if>
				
		</span></h1>
</div>