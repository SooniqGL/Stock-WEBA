<%@ taglib prefix="s" uri="/struts-tags" %>


<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.portfolioForm;
		
        var portfolioName = document.getElementById("inputVO.portfolioInfo.portfolioName").value;
		if (portfolioName == "") {
			alert("Portfolio name is required.");
			return false;
		}
		
		var tradeCost = document.getElementById("inputVO.portfolioInfo.tradeCost").value;
		if (tradeCost == "") {
			alert("Trade Cost is required.");
			return false;
		}
		
		if (Q.common.isValidNumber(tradeCost) == false) {
			return false;
		}
		
		var tradeCostNum = parseFloat(tradeCost);
		if (tradeCostNum <= 0) {
			alert("Trade Cost has to be positive.");
			return false;
		}
		
		var currentCash = document.getElementById("inputVO.portfolioInfo.currentCash").value;
		if (currentCash == "") {
			alert("Current Cash is required.");
			return false;
		}
		
		if (Q.common.isValidNumber(currentCash) == false) {
			return false;
		}
		
		var currentCashNum = parseFloat(currentCash);
		if (currentCashNum <= 0) {
			alert("Current Cash has to be positive.");
			return false;
		}
		
		form.submit();
	}
	
	function setFocus() { 
	  document.getElementById("inputVO.portfolioInfo.portfolioName").focus();
	} 
	
	window.onload=setFocus;
	
</SCRIPT>	

<s:set var="portfolioList" value="inputVO.portfolioList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

	<form name="portfolioForm" action="../m/portfolio_newportfolio.do"> 
          <input type="hidden" id="inputVO.mode" name="inputVO.mode" value="newportfolio" />
          <input type="hidden" id="inputVO.type" name="inputVO.type" value="add" />  
            
            <table width="100%" class="gridtable"> 
	
		<tr><td colspan="2" align="center"><font class="sub_title">Add New Portfolio</font></td></tr>

        <tr><td width="50%">Portfolio Name</td><td width="50%"><input type="text" id="inputVO.portfolioInfo.portfolioName" name="inputVO.portfolioInfo.portfolioName" value="" maxLength="100"></td>
        <tr><td>Trade Cost</td><td><input type="text" id="inputVO.portfolioInfo.tradeCost" name="inputVO.portfolioInfo.tradeCost" value="" maxLength="100"></td>
		<tr><td>Current Cash</td><td><input type="text" id="inputVO.portfolioInfo.currentCash" name="inputVO.portfolioInfo.currentCash" value="" maxLength="100"></td>

		<tr><td colspan="2" align="center">
			<input type="button" name="sb" value=" Create Portfolio " onClick="submitRequest()" class="long_button">
		</td>
		</tr> 
		<tr><td width="50%">Current Portfolio List:</td><td>
			<s:iterator var="itemVO" value="#portfolioList" status="rowStatus">
				<a href="/WebApp2/m/portfolio.do?inputVO.mode=viewpositions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink"><s:property value='%{#itemVO.portfolioName}'/></a>
					<br>
			</s:iterator>

			<s:if test="%{#portfolioList.isEmpty()}">
				No portfolio exists in your account.
			</s:if>
		</td></tr>
		</table></form>

        </div>
      </div>
