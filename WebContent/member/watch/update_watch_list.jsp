<%@ taglib prefix="s" uri="/struts-tags" %>
   

<SCRIPT language="javascript">    
  	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		document.getElementById("inputVO.mode").value = mode;
		document.getElementById("inputVO.type").value = type;

		if (!Q.common.isValidDate(document.getElementById("inputVO.watchInfo.openDate").value) ||
			!Q.common.isValidDate(document.getElementById("inputVO.watchInfo.closeDate").value)) {
			return false;
		} else if (!Q.common.isValidNumber(document.getElementById("inputVO.watchInfo.openPrice").value) ||
			!Q.common.isValidNumber(document.getElementById("inputVO.watchInfo.closePrice").value)) {
			return false;
		} 

		if (document.getElementById("inputVO.watchInfo.openDate").value != "" ||
				document.getElementById("inputVO.watchInfo.closeDate").value != "") {
			if (document.getElementById("inputVO.watchInfo.tradeType").value == "") {
				alert("Trade type is needed.");
				return false;
			}
		}
		
		form.submit();
	}
	
	function submitDeleteRequest() {
		var form = document.watchForm;

		var ret = confirm("You are about to delete the entry. \nClick OK to continue or Cancel to abort action.");
		if (ret == false) {
			return false;
		}

		document.getElementById("inputVO.mode").value = "deletewatch";
		form.submit();
	}
	
	function submitMoveRequest() {
		var form = document.watchForm;
		if (document.getElementById("inputVO.folderId").value == document.getElementById("inputVO.newFolderId").value) {
			alert("It is already in this folder.  Select another folder to move.");
			return;
		}
		
		document.getElementById("inputVO.mode").value = "updatefolder";
		document.getElementById("inputVO.type").value = "move";
		form.submit();
	}
	
	function submitCopyRequest() {
		var form = document.watchForm;
		if (document.getElementById("inputVO.folderId").value == document.getElementById("inputVO.newFolderId").value) {
			alert("It is already in this folder.  Select another folder to copy.");
			return;
		}
		
		document.getElementById("inputVO.mode").value = "updatefolder";
		document.getElementById("inputVO.type").value = "copy";
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
	
	
</SCRIPT>	

<s:set var="watchInfo" value="inputVO.watchInfo" />
<s:set var="folderList" value="inputVO.folderList" />

<div class="contentwrapper" style="min-height:150px;"><div style="margin:2px;"> 

		<form name="watchForm" action="../m/watch_updatewatchlist.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="basic" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
        <input type="hidden" id="inputVO.watchInfo.stockId" name="inputVO.watchInfo.stockId" value="<s:property value='%{inputVO.stockId}'/>" />
        <input type="hidden" id="inputVO.folderId" name="inputVO.folderId" value="<s:property value='%{inputVO.folderId}'/>" />
        <input type="hidden" id="inputVO.watchInfo.folderId" name="inputVO.watchInfo.folderId" value="<s:property value='%{inputVO.folderId}'/>" />
        <input type="hidden" id="currentDate" name="currentDate" value="<s:property value='%{inputVO.currentDate}'/>" />
        <input type="hidden" id="currentPrice" name="currentPrice" value="<s:property value='%{inputVO.currentPrice}'/>" />
		
		        
		<table width="100%" class="gridtable">
			<tr><td colspan="2" height="30">&nbsp;
			<font class="sub_title">Update Watch List Entry</font>
			in <a href="../m/watch_viewwatchlist.do?inputVO.mode=viewwatchlist&inputVO.folderId=<s:property value='%{inputVO.folderId}'/>"><s:property value='%{inputVO.folderName}'/></a>
			</td></tr>
			
			<tr><td>Ticker: <s:property value='%{#watchInfo.ticker}'/></td>
			<td>Company: <s:property value='%{#watchInfo.companyName}'/></td></tr>
			
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
					<input type="button" name="bt1" value="Use Current" onClick="setCurrentForOpen()" class="reg_button2" />
					<input type="button" name="bt2" value="Clean" onClick="cleanOpen()" class="reg_button2" />
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
					<input type="button" name="bt3" value="Use Current"  onClick="setCurrentForClose()" class="reg_button2" />
					<input type="button" name="bt4" value="Clean"  onClick="cleanClose()" class="reg_button2" />
				</td>
			</tr>
			
		   <tr>
		    <td align="center" colspan="2"><input name="db1" type="button" value="Update Watch Entry" onclick="submitRequest('updatewatch', 'update')" class="exlong_button"/></td>
			</TR>
			
			<tr bgcolor="#bbffcc"><td colspan="2">Move, Copy, or Delete Record</td></tr>
			<tr><td>To Folder: </td>
		    	<td>
		    	<select id="inputVO.newFolderId" name="inputVO.newFolderId">
				<s:iterator var="folderItem" value="#folderList" status="rowStatus">
					<s:if test="%{#folderItem.folderId == inputVO.forlderId}">
						<option value="<s:property value='%{#folderItem.folderId}'/>" selected><s:property value='%{#folderItem.folderName}'/></option>
					</s:if>
					<s:else>
						<option value="<s:property value='%{#folderItem.folderId}'/>"><s:property value='%{#folderItem.folderName}'/></option>
					</s:else>
				</s:iterator>
				</select>
		    	
		    	<input name="db11" type="button" value="Move" onclick="submitMoveRequest()" class="reg_button2"/>
		    	<input name="db12" type="button" value="Copy" onclick="submitCopyRequest()" class="reg_button2"/>
				<input name="db3" type="button" value="Delete" onclick="submitDeleteRequest()" class="reg_button2"/>
			    </td>
			</TR>
		</table>
	</form>
	</div>
    </div>
           