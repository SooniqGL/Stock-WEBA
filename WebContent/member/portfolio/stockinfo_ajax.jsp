<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- Set content type to xml that will prevent data truncation in FireFox;
Actually, Firefox split data to multiple nodes if one exceeds 4096 bytes
xmlDoc.getElementsByTagName("dailyDataStr")[0].childNodes[i].nodeValue for i = 0, 1, ...
--%>

<s:set var="itemVO" value="inputVO.positionInfo" />

<doc>
<status><s:property value='%{inputVO.success}'/></status>
<stockId><s:property value='%{#itemVO.stockId}'/></stockId>
<openPrice><s:property value='%{#itemVO.openPrice}'/></openPrice>
<openDate><s:property value='%{#itemVO.openDate}'/></openDate>
<companyName><s:property value='%{#itemVO.companyName}'/></companyName>
</doc>
