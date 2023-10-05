<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />

<div class="leftsidewrapper">
<span  style="display:table; margin-left:15px; padding:5px;">

<h3>Portfolio:</h3>

<b><s:property value='%{#portfolioInfo.portfolioName}'/></b>
<br/>

<a href="../m/portfolio_viewpositions.do?inputVO.mode=viewpositions&inputVO.displayChart=N&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Open Positions</a>
	<br/>				
<a href="../m/portfolio_updateportfolio.do?inputVO.mode=updateportfolio&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Update Portfolio</a>
	<br/>				
<a href="../m/portfolio_viewtransactions.do?inputVO.mode=viewtransactions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Transactions</a>
	<br/>				
<a href="../m/portfolio_viewclosedpositions.do?inputVO.mode=viewclosedpositions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Closed Positions</a>
				

</span></div>