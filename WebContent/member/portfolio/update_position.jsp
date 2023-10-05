<%@ taglib prefix="s" uri="/struts-tags" %> 
 

<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		
	var form = document.portfolioForm;

		var closePrice = document
				.getElementById("inputVO.positionInfo.closePrice").value;
		if (closePrice == "") {
			alert("Close Price is required.");
			return false;
		}

		if (Q.common.isValidNumber(closePrice) == false) {
			return false;
		}

		var numShares = document.getElementById("numShares").value;
		var numSharesToClose = document
				.getElementById("inputVO.positionInfo.numShares").value;
		if (numSharesToClose == "") {
			alert("Please make less or equal number of shares to close.");
			return false;
		}

		var numSharesInt = parseInt(numShares);
		var numSharesToCloseInt = parseInt(numSharesToClose);
		if (numSharesInt < numSharesToCloseInt) {
			alert("Please make less or equal number of shares to close.");
			return false;
		}

		form.submit();
	}

	function setFocus() {
		document.getElementById("inputVO.portfolioInfo.closePrice").focus();
	}

	window.onload = setFocus;
</SCRIPT>	


<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />
<s:set var="positionInfo" value="inputVO.positionInfo" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

		<form name="portfolioForm" action="../m/portfolio_updateposition.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="updateposition" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="update" />  
        <input type="hidden" id="numShares" name="numShares" value="<s:property value='%{#positionInfo.numShares}'/>"></input>
        <input type="hidden" id="inputVO.portfolioId" name="inputVO.portfolioId" value="<s:property value='%{inputVO.portfolioId}'/>"></input>
        <input type="hidden" id="inputVO.positionInfo.orderId" name="inputVO.positionInfo.orderId" value="<s:property value='%{#positionInfo.orderId}'/>"></input>
		  
            
            
        <table width="100%" class="gridtable"> 
		<tr><td colspan="2" align="center"><font class="sub_title">Update Position in Portfolio</font></td></tr>

        <tr><td width="50%">Portfolio Name</td><td width="50%"><s:property value='%{#portfolioInfo.portfolioName}'/></td>
        <tr><td>Trade Cost</td><td><s:property value='%{#portfolioInfo.tradeCost}'/></td>
		<tr><td>Current Cash</td><td><s:property value='%{#portfolioInfo.currentCash}'/></td>
          
              
        <tr><th colspan="2" align="center">
                        <b>Existing open order information as follows</b>
		</th>
		</tr> 
                
       <tr><td>Trade Type</td>
				<td>
					<s:if test="%{#positionInfo.tradeType != NULL && #positionInfo.tradeType != \"L\"}">
						Long
	                </s:if>
	                <s:else>Short
	                </s:else>                       
				</td>
		</tr>
		<tr><td width="50%">Ticker</td><td><s:property value='%{#positionInfo.ticker}'/></td></tr>
        <tr><td width="50%">Open Price</td><td><s:property value='%{#positionInfo.openPrice}'/></td></tr>
        <tr><td width="50%">Open Date</td><td><s:property value='%{#positionInfo.openDate}'/></td></tr>
        <tr><td width="50%">Number Shares</td><td><s:property value='%{#positionInfo.numShares}'/></td></tr>
        <tr><td width="50%">Current Price</td><td><s:property value='%{#positionInfo.currentPrice}'/></td></tr>
        
        <tr><th colspan="2" align="center">
              <b>How do you want to close?</b>
		</th>
		</tr> 
                
                <tr><td width="50%">Close Price</td><td><input type="text" id="inputVO.positionInfo.closePrice" name="inputVO.positionInfo.closePrice" value="<s:property value='%{#positionInfo.currentPrice}'/>"></td></tr>
                <tr><td width="50%">Close Shares (May do partial close)</td><td><input type="text" id="inputVO.positionInfo.numShares" name="inputVO.positionInfo.numShares" value="<s:property value='%{#positionInfo.numShares}'/>" maxLength="100"></td></tr>
                
                <tr><td colspan="2" align="center"><input type="button" name="sb" value=" Submit Order " onClick="submitRequest()" class="reg_button"></td></tr> 
                
		</table></form>
		
		</div></div>