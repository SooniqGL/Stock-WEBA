<%@ taglib prefix="s" uri="/struts-tags" %> 
  

	
<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />
<s:set var="positionList" value="inputVO.positionList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">           
            
       <table width="100%" class="gridtable"> 
		<tr><td colspan="2" align="center"><font class="sub_title">Portfolio Closed Positions</font></td></tr>

		<tr><td width="50%">Portfolio Name</td><td width="50%"><s:property value='%{#portfolioInfo.portfolioName}'/></td>
        <tr><td>Trade Cost</td><td><s:property value='%{#portfolioInfo.tradeCost}'/></td>
		<tr><td>Current Cash</td><td><s:property value='%{#portfolioInfo.currentCash}'/></td>
        
        <tr><td colspan="2" align="center">
			Closed Positions (Trade History) &nbsp;|&nbsp;
			<a href="../m/portfolio_viewpositions.do?inputVO.mode=viewpositions&inputVO.displayChart=N&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Open Positions</a>
			&nbsp;|&nbsp;				
			<a href="../m/portfolio_viewtransactions.do?inputVO.mode=viewtransactions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Transactions</a>
			
		</td>
		</tr> 
		<tr><td colspan="2" style="padding:0px;"><table style="width:100%; margin:0px;border-collapse: collapse;"  >
                            <tr><th>Order Id</th><th>Long/Short</th><th>Open Date</th><th>Ticker</th><th>Num Shares</th><th>Open Price</th>
                                <th>Close Date</th><th>Close Price</th><th>Gain Percent</th><th>Gain</th><th>Value</th>
                            
                            </tr>

			<s:iterator var="itemVO" value="#positionList" status="rowStatus">
                            <tr><td align="center"><s:property value='%{#itemVO.orderId}'/></td>
                            <td align="center"><s:property value='%{#itemVO.tradeType}'/></td>
                            <td align="center"><s:property value='%{#itemVO.openDate}'/></td>
                            <td align="center"><s:property value='%{#itemVO.ticker}'/></td>
                            <td align="center"><s:property value='%{#itemVO.numShares}'/></td>
                            <td align="center"><s:property value='%{#itemVO.openPrice}'/></td>
                            <td align="center"><s:property value='%{#itemVO.closeDate}'/></td>
                            <td align="center"><s:property value='%{#itemVO.closePrice}'/></td>
                            
                            <s:if test="%{#itemVO.gainPercent < 0}">
                            <td align="center"><font color="#ff0000"><b><s:property value='%{#itemVO.gainPercent}'/>%</b></font></td>
                            <td align="center"><font color="#ff0000"><b><s:property value='%{#itemVO.gain}'/></b></font></td>
                            </s:if>
                            
                            <s:else>	
                            <td align="center"><font color="#00ff00"><b><s:property value='%{#itemVO.gainPercent}'/>%</b></font></td>
                            <td align="center"><font color="#00ff00"><b><s:property value='%{#itemVO.gain}'/></b></font></td>
                            </s:else>
                            
                            <td align="center"><s:property value='%{#itemVO.closeAmount}'/></td>
                            </tr>

			</s:iterator>
			
			<s:if test="%{#positionList.isEmpty()}">
			<tr><td colspan="11">
				No closed positions for this portfolio.
				</td></tr>
			</s:if>
        </table>
		</td></tr>
                

                
		</table>
            </div>
            </div>
