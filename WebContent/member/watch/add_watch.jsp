<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- do some dynamic include --%>
<jsp:include page="../member_include.jsp" />

<script language="javascript">    
  	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		var newFolderObj = document.getElementById("inputVO.newFolderName");
		
		document.getElementById("inputVO.mode").value = mode;
		document.getElementById("inputVO.type").value = type;
		
		if (!Q.common.isValidDate(document.getElementById("inputVO.watchInfo.openDate").value) ||
			!Q.common.isValidDate(document.getElementById("inputVO.watchInfo.closeDate").value)) {
			return false;
		} else if (!Q.common.isValidNumber(document.getElementById("inputVO.watchInfo.openPrice").value) ||
			!Q.common.isValidNumber(document.getElementById("inputVO.watchInfo.openPrice").value)) {
			return false;
		}
		
		if (document.getElementById("inputVO.watchInfo.openDate").value != "" ||
			document.getElementById("inputVO.watchInfo.closeDate").value != "") {
			if (document.getElementById("inputVO.watchInfo.tradeType").value == "") {
				alert("Trade type is needed.");
				return false;
			}
		}
		
		if (newFolderObj != null) {
			if (newFolderObj.value == "") {
				alert("To Folder field is needed.");
				return false;
			}
		}
		
		form.submit();
	}
	
	function setCurrentForOpen() {
		var form = document.watchForm;
		document.getElementById("inputVO.watchInfo.openDate").value = form.currentDate.value;
		document.getElementById("inputVO.watchInfo.openPrice").value = form.currentPrice.value;
	}
	
	function cleanOpen() {
		document.getElementById("inputVO.watchInfo.openDate").value = "";
		document.getElementById("inputVO.watchInfo.openPrice").value = "";
	}
	
	function setCurrentForClose() {
		var form = document.watchForm;
		document.getElementById("inputVO.watchInfo.closeDate").value = form.currentDate.value;
		document.getElementById("inputVO.watchInfo.closePrice").value = form.currentPrice.value;
	}
	
	function cleanClose() {
		document.getElementById("inputVO.watchInfo.closeDate").value = "";
		document.getElementById("inputVO.watchInfo.closePrice").value = "";
	}
	
</script>


<s:set var="watchInfo" value="inputVO.watchInfo" />
<s:set var="folderList" value="inputVO.folderList" />

<div class="contentwrapper" style="min-height:150px;"><div style="margin:2px;">

    
		<form name="watchForm" action="../m/watch_addwatch.do"> 		
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="addwatch" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="addone" />
        <input type="hidden" id="inputVO.watchInfo.stockId" name="inputVO.watchInfo.stockId" value="<s:property value='%{inputVO.stockId}'/>" />
        <input type="hidden" id="currentDate" name="currentDate" value="<s:property value='%{inputVO.currentDate}'/>" />
        <input type="hidden" id="currentPrice" name="currentPrice" value="<s:property value='%{inputVO.currentPrice}'/>" />
        
        
		<table width="100%" class="gridtable">
			<tr><td colspan="2" height="30">&nbsp;
			<font class="sub_title">Add One Watch List Entry</font></td></tr>
			
			<s:if test="%{inputVO.message != NULL && inputVO.message != \"\"}">
				<TR><td colspan="2">
				<font color="#ff0000">Error: <s:property value='%{inputVO.message}'/></font></td>		
				</TR>
			</s:if>
			
			<tr><td>Ticker: <s:property value='%{#watchInfo.ticker}'/></td>
			<td>Company: <s:property value='%{#watchInfo.companyName}'/></td></tr>
			
			<tr><td>To Folder: </td>
		    	<td>
					<s:if test="%{inputVO.newFolderName == \"\"}">	    
						<select id="inputVO.folderId" name="inputVO.folderId">
							<s:iterator var="folderItem" value="#folderList" status="rowStatus">
						
								<option value="<s:property value='%{#folderItem.folderId}'/>"><s:property value='%{#folderItem.folderName}'/></option>
							</s:iterator>
						</select>    		
					</s:if>
					<s:else>
						<input type="text" id="inputVO.newFolderName" name="inputVO.newFolderName" value="<s:property value='%{inputVO.newFolderName}'/>" />
					</s:else>
			</td></tr>    	
			
			
			<TR>
			<TD>Notes</td><td> 
				<TEXTAREA id="inputVO.watchInfo.comments" name="inputVO.watchInfo.comments" rows="5" cols="50"><s:property value='%{#watchInfo.comments}'/></TEXTAREA>
			</TD>
			</tr>
			
			<tr bgcolor="#bbffcc"><td colspan="2">Paper trade record</td></tr>
			<tr><td>Trade Type</td>
				<td>	
					<select id="inputVO.watchInfo.tradeType" name="inputVO.watchInfo.tradeType">
					<s:if test="%{#watchInfo.tradeType == \"L\"}">
			 			<option value=""></option>
						<option value="L" selected>Long</option>
						<option value="S">Short</option>
			 		</s:if>
			 		<s:if test="%{#watchInfo.tradeType == \"S\"}">
			 			<option value=""></option>
						<option value="L">Long</option>
						<option value="S" selected>Short</option>
			 		</s:if>
					<s:else>
					 	<option value=""></option>
						<option value="L">Long</option>
						<option value="S">Short</option>
					</s:else>
					</select>
				</td>
			</tr>
			<tr><td>Open Date</td>
				<td>
					<input type="text" id="inputVO.watchInfo.openDate" name="inputVO.watchInfo.openDate" value="<s:property value='%{#watchInfo.openDate}'/>" />
				</td>
			</tr>
			<tr><td>Open Price</td>
				<td>
					<input type="text" id="inputVO.watchInfo.openPrice" name="inputVO.watchInfo.openPrice" value="<s:property value='%{#watchInfo.openPrice}'/>" />
					<input type="button" name="bt1" value=" Use Current " onClick="setCurrentForOpen()" class="reg_button" />
					<input type="button" name="bt2" value=" Clean " onClick="cleanOpen()" class="reg_button" />
				</td>
			</tr>
			<tr><td>Close Date</td>
				<td>
					<input type="text" id="inputVO.watchInfo.closeDate" name="inputVO.watchInfo.closeDate" value="<s:property value='%{#watchInfo.closeDate}'/>" />
				</td>
			</tr>
			<tr><td>Close Price</td>
				<td>
					<input type="text" id="inputVO.watchInfo.closePrice" name="inputVO.watchInfo.closePrice" value="<s:property value='%{#watchInfo.closePrice}'/>" />
					<input type="button" name="bt3" value=" Use Current "  onClick="setCurrentForClose()" class="reg_button" />
					<input type="button" name="bt4" value=" Clean "  onClick="cleanClose()" class="reg_button" />
				</td>
			</tr>
			
		   <tr>
		    <td align="center" colspan="2"><input name="db1" type="button" value="Add Watch Entry" onclick="submitRequest('addwatch', 'addone')" class="exlong_button"/></td>
			</TR>
			
		</table>
	</form>
    </div>
</div>
            
