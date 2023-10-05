<%-- 
    Document   : dynamic_ajax
    Created on : Dec 28, 2011, 10:05:09 AM
    Author     : qin
--%>
<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- Set content type to xml that will prevent data truncation in FireFox;
Actually, Firefox split data to multiple nodes if one exceeds 4096 bytes
xmlDoc.getElementsByTagName("dailyDataStr")[0].childNodes[i].nodeValue for i = 0, 1, ...
--%>
<%@page contentType="text/xml" pageEncoding="UTF-8"%>

<doc>
<status><s:property value='%{inputVO.success}'/></status>
<stockId><s:property value='%{inputVO.stockId}'/></stockId>
<totalDailyItems><s:property value='%{inputVO.totalDailyItems}'/></totalDailyItems>
<totalWeeklyItems><s:property value='%{inputVO.totalWeeklyItems}'/></totalWeeklyItems>
<dailyDataStr><s:property value='%{inputVO.dailyDataStr}'/></dailyDataStr>
<weeklyDataStr><s:property value='%{inputVO.weeklyDataStr}'/></weeklyDataStr>
</doc>
