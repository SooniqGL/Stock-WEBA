<%@ taglib prefix="s" uri="/struts-tags" %> 
  

<SCRIPT language="javascript">     
 	  
  
        function submitRequestForChart() {
            var checkBox 	= document.getElementById("showAllCheck").checked;
            if (checkBox == true) {
                document.getElementById("inputVO.displayChart").value = "Y";
            } else {
                document.getElementById("inputVO.displayChart").value = "N";
            }

            var form = document.reloadForm;
            form.submit();
        }

        // set the check box at the beginning, if existing
        function setOption() {
            var checkBoxObj 	= document.getElementById("showAllCheck");
            if (checkBoxObj != null) {
                var showFlag = document.getElementById("inputVO.displayChart").value;
                if (showFlag != null && showFlag == "Y") {
                    checkBoxObj.checked = true;
                } else {
                    checkBoxObj.checked = false;
                }
            }
        }
        
        window.onload=setOption;
	
</SCRIPT>	

<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />
<s:set var="positionList" value="inputVO.positionList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">   
              
            
            <table width="100%" class="gridtable"> 

			<tr><td colspan="2" align="center"><font class="sub_title">Portfolio Open Positions</font></td></tr>
	
	        <tr><td width="50%">Portfolio Name</td><td width="50%"><s:property value='%{#portfolioInfo.portfolioName}'/></td>
	        <tr><td>Trade Cost</td><td><s:property value='%{#portfolioInfo.tradeCost}'/></td>
			<tr><td>Current Cash</td><td><s:property value='%{#portfolioInfo.currentCash}'/></td>
	          
                <tr><td colspan="2" align="center">
							Open Positions &nbsp;|&nbsp;
                        <a href="../m/portfolio_newposition.do?inputVO.mode=newposition&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">New Position</a>
		
                        &nbsp;|&nbsp; Charts <input type="checkbox" id="showAllCheck" onClick="submitRequestForChart()"/>
                    
						&nbsp;|&nbsp;
						<a href="../m/portfolio_viewtransactions.do?inputVO.mode=viewtransactions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Transactions</a>
						&nbsp;|&nbsp;			
						<a href="../m/portfolio_viewclosedpositions.do?inputVO.mode=viewclosedpositions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>" class="datalink">Closed Positions</a>
                    
                    </td>
					</tr> 
					<tr><td colspan="2" align="center" style="padding:0px;">
                        <table style="width:100%; margin:0px;border-collapse: collapse;">
                            <tr><th>Order Id</th><th>Long/Short</th><th>Open Date</th><th>Ticker</th>
                                <th>Num Shares</th><th>Open Price</th>
                                <th>Current Price</th><th>Gain Percent</th><th>Gain</th>
                                <th>Value</th><th>Update</th>
                            
                            </tr>
                            
                    <s:iterator var="itemVO" value="#positionList" status="rowStatus">
                            <tr><td align="center"><s:property value='%{#itemVO.orderId}'/></td>
                            <td align="center"><s:property value='%{#itemVO.tradeType}'/></td>
                            <td align="center"><s:property value='%{#itemVO.openDate}'/></td>
                            <td align="center"><s:property value='%{#itemVO.ticker}'/></td>
                            <td align="center"><s:property value='%{#itemVO.numShares}'/></td>
                            <td align="center"><s:property value='%{#itemVO.openPrice}'/></td>
                            <td align="center"><s:property value='%{#itemVO.currentPrice}'/></td>
                            
                            <s:if test="%{#itemVO.gainPercent < 0}">
                            <td align="center"><font color="#ff0000"><b><s:property value='%{#itemVO.gainPercent}'/>%</b></font></td>
                            <td align="center"><font color="#ff0000"><b><s:property value='%{#itemVO.gain}'/></b></font></td>
                            </s:if>
                            
                            <s:else>	
                            <td align="center"><font color="#00ff00"><b><s:property value='%{#itemVO.gainPercent}'/>%</b></font></td>
                            <td align="center"><font color="#00ff00"><b><s:property value='%{#itemVO.gain}'/></b></font></td>
                            </s:else>
                            
                            <td align="center"><s:property value='%{#itemVO.currentAmount}'/></td>
                            <td align="center">
                            <a href="../m/portfolio_updateposition.do?inputVO.mode=updateposition&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{inputVO.portfolioId}'/>&inputVO.positionInfo.orderId=<s:property value='%{#itemVO.orderId}'/>" class="datalink">Close Position</a></td>
                            
                            </td>
                            </tr>
                            
                            <s:if test="%{inputVO.displayChart == \"Y\"}">
                            <TR>
                                    <TD colspan="11" align="center">
                            <img id="stock_chart2" src="../drawchart?stockId=<s:property value='%{#itemVO.stockId}'/>&period=12&option=NNNY">
                                </TD></TR>
							</s:if>
							</s:iterator>
			
							<s:if test="%{#positionList.isEmpty()}">
								<tr><td colspan="11">No positions for this portfolio.</td></tr>
							</s:if>        

   
                        </table>
		</td></tr>
                
                <tr><td colspan="2" align="center">
			Invest Value: <s:property value='%{inputVO.investValue}'/>; Total Value (Cash + Invest): <s:property value='%{inputVO.totalValue}'/>
                    </td></tr>
                
		</table>
	
	
            </div>
            </div>


            <form name="reloadForm" action="../m/portfolio_viewpositions.do">
                <input type="hidden" name="inputVO.mode" value="viewpositions"/>
                <input type="hidden" name="inputVO.portfolioId" value="<s:property value='%{inputVO.portfolioId}'/>"/>
                <input type="hidden" id="inputVO.displayChart" name="inputVO.displayChart" value="<s:property value='%{inputVO.displayChart}'/>" />
            </form>
           