<%@ taglib prefix="s" uri="/struts-tags" %>
 

<SCRIPT language="javascript">    
 	 
	function submitRequest(mode, type) {
		var form = document.watchForm;
		document.getElementById("inputVO.mode").value = mode;
		document.getElementById("inputVO.type").value = type;
		
		if (mode == "updatefolder") {
			if (type == "addlist") {
				if (collectValues()) {
					form.submit();
				}
			} else if (type == "delete") {
				var ret = confirm("You are about to delete the entire folder. \nClick OK to continue or Cancel to abort action.");
				if (ret == false) {
					return false;
				}
				
				form.submit();
			} else if (type == "rename") {
				if (document.getElementById("inputVO.folderName").value == "") {
					alert("Folder name cannot be empty.");
					return false;
				}
				
				form.submit();
			}
		} else {
			form.submit();
		}
	}
	
	function collectValues() {
		var form = document.watchForm;
		var rows = 10;
		var tickerListStr = "";
	
		// loop through all the elements
		var ticker = "";
		var comments = "";
		for (var id = 1; id <= rows; id ++) {
			ticker = eval("document.getElementById('TIK" + id + "').value");
			
			if (ticker != "") {
				comments = eval("document.getElementById('COM" + id + "').value");
				if (tickerListStr != "") {
					tickerListStr += "#";
				}
				
				comments = Q.common.encodeString(comments);
				tickerListStr += ticker + ":" + comments;
			}
		}
	
		if (tickerListStr == "") {
			alert("Need at least enter one ticker.");
			return false;
		} else {
			document.getElementById("inputVO.tickerListStr").value = tickerListStr;
			return true;
		}
	}
</script>
	

<s:set var="watchList" value="inputVO.watchList" />

<div class="contentwrapper" style="min-height:150px;"><div style="margin:2px;"> 


		<form name="watchForm" action="../m/watch_updatefolder.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="basic" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
        <input type="hidden" id="inputVO.tickerListStr" name="inputVO.tickerListStr" value="" />
        <input type="hidden" id="inputVO.folderId" name="inputVO.folderId" value="<s:property value='%{inputVO.folderId}'/>"/>
		        
		<table width="100%" class="gridtable">
			<tr><th colspan="2" height="30">&nbsp;
			<font class="sub_title">Update Watch List Folder: </font>
			<a href="../m/watch_viewwatchlist.do?inputVO.mode=viewwatchlist&inputVO.folderId=<s:property value='%{inputVO.folderId}'/>" class="toplink"><s:property value='%{inputVO.folderName}'/></a>
			</td></tr>
			
			<s:if test="%{inputVO.message != NULL && inputVO.message != \"\"}">
				<TR><td colspan="2">
				<font color="#ff0000">Error: <s:property value='%{inputVO.message}'/></font></td>		
				</TR>
			</s:if>

			<tr><td width="125">Add New Tickers</td>
			<td style="padding:0px;" ><table style="width:100%;" class="gridtable_noborder">
				<tr><td style="background-color:#efefff;">Ticker</td><td style="background-color:#efefff;">Notes</td>
					<td style="background-color:#efefff;">Ticker</td><td style="background-color:#efefff;">Notes</td></tr>
	
				<TR>
					<TD valign="top"><input type="text" id="TIK1" name="TIK1" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM1" name="COM1" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" id="TIK2" name="TIK2" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM2" name="COM2" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR>
					<TD valign="top"><input type="text" id="TIK3" name="TIK3" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM3" name="COM3" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" id="TIK4" name="TIK4" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM4" name="COM4" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR>
					<TD valign="top"><input type="text" id="TIK5" name="TIK5" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM5" name="COM5" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" id="TIK6" name="TIK6" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM6" name="COM6" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR>
					<TD valign="top"><input type="text" id="TIK7" name="TIK7" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM7" name="COM7" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" id="TIK8" name="TIK8" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM8" name="COM8" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR>
					<TD valign="top"><input type="text" id="TIK9" name="TIK9" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM9" name="COM9" cols="25" rows="2"></textarea></TD>
					<TD valign="top"><input type="text" id="TIK10" name="TIK10" size="10" maxlength="10" value=""></TD>
					<TD><textarea id="COM10" name="COM10" cols="25" rows="2"></textarea></TD>
				</TR>
				<TR>
					<TD colspan="4" align="center">
						<input name="sb4" type="button" value="Add To Watch List" onclick="submitRequest('updatefolder', 'addlist')" class="exlong_button"/>	
					</TD>
				</TR>
			</table></td>
		<tr><td>Rename Folder</td>
			<td>
			<input type="text" id="inputVO.folderName" name="inputVO.folderName" value="<s:property value='%{inputVO.folderName}'/>" size="80" maxLength="100">
			<input type="button" name="sb" value=" Rename Folder " onClick="submitRequest('updatefolder', 'rename')" class="reg_button">
			</td>
		</tr>
		
		<tr><td>Delete the Folder</td>
			<td>
			<input type="button" name="sb" value=" Delete Folder and All Content " onClick="submitRequest('updatefolder', 'delete')" class="reg_button" />
			This will delete all things associated to this folder.
			</td>
			</tr>
                        <tr><td colspan="2">Notes: We only allow to add the tickers we have in our DB.  Anything not exists in our DB will be ignored.
	We currently hold most of the tickers in Nasdaq and New York Exchanges.</td></tr>
		</table>
                
	</form>
	
	   </div>
            </div>
            