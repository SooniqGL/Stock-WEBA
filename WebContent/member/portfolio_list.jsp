<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set var="portfolioList" value="inputVO.portfolioList" />

<div class="leftsidewrapper">
<span  style="display:table; margin-left:10px; padding:0px; ">
	<h3>My Portfolio List</h3> 
	<s:iterator var="itemVO" value="#portfolioList" status="rowStatus">
		<a href="../m/portfolio.do?inputVO.mode=viewpositions&inputVO.displayChart=N&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" 
		class="datalink"><s:property value='%{#itemVO.portfolioName}'/></a>
		| <a href="../m/portfolio.do?inputVO.mode=updateportfolio&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink2">Update</a><br/>
		
	</s:iterator>
</span></div>