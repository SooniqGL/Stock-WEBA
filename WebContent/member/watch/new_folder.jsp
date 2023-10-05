<%@ taglib prefix="s" uri="/struts-tags" %>



<SCRIPT language="javascript">     
 	  
	function submitRequest() {
		var form = document.watchForm;
		
		if (document.getElementById("inputVO.folderName").value == "") {
			alert("Folder name is required.");
			return false;
		}
		
		form.submit();
	}
	
	function setFocus() { 
		document.getElementById("inputVO.folderName").focus();
	} 
	
	window.onload=setFocus;
	
</SCRIPT>	

<s:set var="folderList" value="inputVO.folderList" />

<div class="contentwrapper" style="min-height:50px;"><div style="margin:2px;">

		<form name="watchForm" action="../m/watch_newfolder.do"> 
			<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="newfolder" />
        	<input type="hidden" id="inputVO.type" name="inputVO.type" value="add" />
            
            
            <table width="100%" class="gridtable" style="font-size: 18px;"> 
	
				<tr><th colspan="2">&nbsp;
				<font class="sub_title">Add New Folder</font></th></tr>
				<tr><td colspan="2">
			
			        
					&nbsp;<input type="text" id="inputVO.folderName" name="inputVO.folderName" value="" maxLength="100">
					<input type="button" name="sb" value=" Create Folder " onClick="submitRequest()">
				</td>
				</tr> 
				<tr><td width="50%">Current Folder List:</td><td>
					<s:iterator var="itemVO" value="#folderList" status="rowStatus">
						<a href="../m/watch_viewwatchlist.do?inputVO.mode=viewwatchlist&inputVO.folderId=<s:property value='%{#itemVO.folderId}'/>" class="datalink"><s:property value='%{#itemVO.folderName}'/></a>
							<br>
					</s:iterator>

					<s:if test="%{#folderList == NULL || #folderList.isEmpty()}">
					No folder exists in your account.
					</s:if>
				</td></tr>
		</table></form>
        </div>
      </div>
            