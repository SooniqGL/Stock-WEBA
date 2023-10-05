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
	
	function submitRequestForDelete() {
		var form = document.portfolioForm;

        var checkIfDelete = document.getElementById("checkIfDelete").checked;
		if (checkIfDelete == false) {
			alert("Please check the check box to confirm if you want to delete everything about the portfolio.");
			return false;
		}
		
		// warning one more time
		var r = confirm("Do you really want to remove?  Please click OK to confirm again or Cancel to cancel the action.");
		if (r == true) {
			// submit the form
			document.getElementById("inputVO.type").value = "delete";
			form.submit();
		}

	}
	
	
</SCRIPT>	

<s:set var="portfolioInfo" value="inputVO.portfolioInfo" />
<s:set var="portfolioList" value="inputVO.portfolioList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">
        
        <form name="portfolioForm" action="../m/portfolio_updateportfolio.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="updateportfolio" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="update" />  
        <input type="hidden" id="inputVO.portfolioId" name="inputVO.portfolioId" value="<s:property value='%{inputVO.portfolioId}'/>" />
	  
            
            <table width="100%" class="gridtable"> 
	
		<tr><td colspan="2" align="center"><font class="sub_title">Update Portfolio</font></td></tr>


		<tr><td width="50%">Portfolio Name</td><td width="50%"><input type="text" id="inputVO.portfolioInfo.portfolioName" name="inputVO.portfolioInfo.portfolioName" value="<s:property value='%{#portfolioInfo.portfolioName}'/>" maxLength="100"></td>
        <tr><td>Trade Cost</td><td><input type="text" id="inputVO.portfolioInfo.tradeCost" name="inputVO.portfolioInfo.tradeCost" value="<s:property value='%{#portfolioInfo.tradeCost}'/>" maxLength="100"></td>
		<tr><td>Current Cash</td><td><input type="text" id="inputVO.portfolioInfo.currentCash" name="inputVO.portfolioInfo.currentCash" value="<s:property value='%{#portfolioInfo.currentCash}'/>" maxLength="100"></td>

                <tr><td colspan="2" align="center">
			<input type="button" name="sb" value=" Update Portfolio " onClick="submitRequest()" class="reg_button">
			</td></tr>
			<tr><td colspan="2" align="center">
			<input type="checkbox" id="checkIfDelete"/> Delete everything related to is portfolio?  <input type="button" name="sb" value=" Delete Portfolio " onClick="submitRequestForDelete()" class="reg_button">
			
		</td>
		</tr> 
		<%--  
		<tr><td width="50%">Current Portfolio List:</td><td>
			<s:iterator var="itemVO" value="#portfolioList" status="rowStatus">
			<a href="../m/portfolio_viewpositions.do?inputVO.mode=viewpositions&inputVO.displayChart=N&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink"><s:property value='%{#itemVO.portfolioName}'/></a>
			<br/>
			</s:iterator>
		</td></tr>--%>
		</table></form>
	
        </div>
    </div>              
