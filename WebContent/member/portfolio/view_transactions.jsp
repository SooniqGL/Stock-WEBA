<%@ taglib prefix="s" uri="/struts-tags" %> 
  

<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />
<s:set var="transactionList" value="inputVO.transactionList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

    
            
            <table width="100%" class="gridtable"> 
	
	<tr><th colspan="2" align="center"><font class="sub_title">Portfolio Transactions</font></td></tr>

        <tr><td width="50%">Portfolio Name</td><td width="50%"><s:property value='%{#portfolioInfo.portfolioName}'/></td>
        <tr><td>Trade Cost</td><td><s:property value='%{#portfolioInfo.tradeCost}'/></td>
		<tr><td>Current Cash</td><td><s:property value='%{#portfolioInfo.currentCash}'/></td>
             
        <tr><td colspan="2" align="center">
			History for money added or retrieved from this portfolio.
			
			&nbsp;|&nbsp;
			<a href="../m/portfolio_viewpositions.do?inputVO.mode=viewpositions&inputVO.displayChart=N&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Open Positions</a>
			&nbsp;|&nbsp;	
			<a href="../m/portfolio_viewclosedpositions.do?inputVO.mode=viewclosedpositions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Closed Positions</a>
			
		</td>
		</tr> 
                
                <tr><td colspan="2" align="center" style="padding:0px;">
                        <table style="width:100%; margin:0px;border-collapse: collapse;"> 
                            <th>ID</th><th>Date</th><th>Type</th><th>Amount</th><th>Current Cash</th>
                        <% int count = 0; %>
                        <s:iterator var="itemVO" value="#transactionList" status="rowStatus">
                            
                            <tr><td align="center"><s:property value='%{#itemVO.transId}'/></td>
                                <%-- mDate will not work in the next line, need to use MDate --%>
                                <td align="center"><s:property value='%{#itemVO.MDate}'/></td>
                                <td align="center"><s:property value='%{#itemVO.transType}'/></td>
                                <td align="center"><s:property value='%{#itemVO.transAmount}'/></td>
                                <td align="center"><s:property value='%{#itemVO.currentCash}'/></td>
					</s:iterator>
                        </table>
                    </td>
                    
                </tr>
                </table>

        </div>
    </div>              
