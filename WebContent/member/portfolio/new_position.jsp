<%@ taglib prefix="s" uri="/struts-tags" %>

<SCRIPT SRC="/WEBA/member/portfolio/AjaxRequest.js"></SCRIPT>
   

<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.portfolioForm;
		
        var ticker = document.getElementById("inputVO.positionInfo.ticker").value;
		if (ticker == "") {
			alert("Ticker is required.");
			return false;
		}
		
		var openDate = document.getElementById("inputVO.positionInfo.openDate").value;
		if (openDate == "") {
			alert("Open Date is required.");
			return false;
		}
		
		if (Q.common.isValidDate(openDate) == false) {
			return false;
		}
		
		
		var openPrice = document.getElementById("inputVO.positionInfo.openPrice").value;
		if (openPrice == "") {
			alert("Open Price is required.");
			return false;
		}
		
		if (Q.common.isValidNumber(openPrice) == false) {
			return false;
		}
		
		var openPriceNum = parseFloat(openPrice);
		if (openPriceNum < 0) {
			alert("Open Price should be positive.");
			return false;
		}
		
		var numShares = document.getElementById("inputVO.positionInfo.numShares").value;
		if (numShares == "") {
			alert("Number of Shares is required.");
			return false;
		}

		if (Q.common.isInt(numShares) == false) {
			alert("Number of Shares should be integer.");
			return false;
		}

		var numSharesNum = parseInt(numShares);
		if (numSharesNum < 0) {
			alert("Number of Shares should be positive.");
			return false;
		}
		

		form.submit();
	}
	
	function setFocus() { 
	  document.getElementById("inputVO.portfolioInfo.tradeType").focus();
	} 
	
	window.onload=setFocus;
</SCRIPT>	

<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />
<s:set var="positionInfo" value="inputVO.positionInfo" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

	<form name="portfolioForm" action="../m/portfolio_newposition.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="newposition" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="add" />  
        <input type="hidden" id="inputVO.portfolioId" name="inputVO.portfolioId" value="<s:property value='%{inputVO.portfolioId}'/>"></input>
	
            
            
        <table width="100%" class="gridtable"> 
	
		<tr><td colspan="2" align="center"><font class="sub_title">Add New Position to Portfolio</font></td></tr>

        <tr><td width="50%">Portfolio Name</td><td width="50%"><s:property value='%{#portfolioInfo.portfolioName}'/></td>
        <tr><td>Trade Cost</td><td><s:property value='%{#portfolioInfo.tradeCost}'/></td>
		<tr><td>Current Cash</td><td><s:property value='%{#portfolioInfo.currentCash}'/></td>
                  
        <tr><th colspan="2" align="center">
			Enter Order as follows 
		</th>
		</tr> 
		
		<s:if test="%{inputVO.message != NULL && inputVO.message != \"\"}">
			<TR><td colspan="2">
			<font color="#ff0000">Error: <s:property value='%{inputVO.message}'/></font></td>		
			</TR>
		</s:if>
                
    
                    <tr><td>Trade Type</td>
				<td>
					<select id="inputVO.positionInfo.tradeType" name="inputVO.positionInfo.tradeType">
					<s:if test="%{#positionInfo.tradeType != NULL && #positionInfo.tradeType == \"L\"}">
						<option value=""></option>
						<option value="L" selected>Long</option>
						<option value="S">Short</option>
			    	</s:if>
					<s:elseif test="%{#positionInfo.tradeType != NULL && #positionInfo.tradeType == \"S\"}">
						<option value=""></option>
						<option value="L">Long</option>
						<option value="S" selected>Short</option>
			    	</s:elseif>
			    	<s:else>		    
			    		<option value=""></option>
						<option value="L">Long</option>
						<option value="S">Short</option>
			    	</s:else>
					</select>
				</td>
                    </tr>
                    <tr><td width="50%">Ticker</td><td><input type="text" id="inputVO.positionInfo.ticker" name="inputVO.positionInfo.ticker" value="<s:property value='%{#positionInfo.ticker}'/>" maxLength="100">
                            <input type="button" value=" Look UP " onclick="checkStock();" class="reg_button" />
                            <img id="ajax_loading" width="20" height="20" src="../../image/processing.gif" style="display: none">
                        </td></tr>
                    <tr><td width="50%">Company (optional)</td><td><input type="text" id="inputVO.positionInfo.companyName" name="inputVO.positionInfo.companyName" value="<s:property value='%{#positionInfo.companyName}'/>" size="50" maxLength="100"></td></tr>
                    <tr><td width="50%">Open Date (yyyy/mm/dd)</td><td><input type="text" id="inputVO.positionInfo.openDate" name="inputVO.positionInfo.openDate" value="<s:property value='%{#positionInfo.openDate}'/>" maxLength="100"></td></tr>

                    <tr><td width="50%">Price</td><td>
                    <s:if test="%{#positionInfo.openPrice != 0}">
                    	<input type="text" id="inputVO.positionInfo.openPrice" name="inputVO.positionInfo.openPrice" value="<s:property value='%{#positionInfo.openPrice}'/>" maxLength="100"></td></tr>
                    </s:if>
                    <s:else>
                    	<input type="text" id="inputVO.positionInfo.openPrice" name="inputVO.positionInfo.openPrice" value="" maxLength="100"></td></tr>
                    </s:else>
                    
                    <tr><td width="50%">Number of Shares</td><td>
                    <s:if test="%{#positionInfo.numShares != 0}">
                    	<input type="text" id="inputVO.positionInfo.numShares" name="inputVO.positionInfo.numShares" value="<s:property value='%{#positionInfo.numShares}'/>" maxLength="100"></td></tr>
                	</s:if>
                    <s:else>
                    	<input type="text" id="inputVO.positionInfo.numShares" name="inputVO.positionInfo.numShares" value="" maxLength="100"></td></tr>
                    </s:else>
                
                
                
                </logic:notEqual>
                
                
                <tr><td colspan="2" align="center"><input type="button" name="sb" value=" Submit Order " onClick="submitRequest()" class="reg_button"></td></tr> 
                
		</table></form>

        </div>
    </div>              
