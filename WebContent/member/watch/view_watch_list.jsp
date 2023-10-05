<%@ taglib prefix="s" uri="/struts-tags" %>



<SCRIPT language="javascript">     
  	  
	function submitRequest(mode, type) {
		var form = document.watchForm;
		document.getElementById("inputVO.mode").value = mode;
		document.getElementById("inputVO.type").value = type;
		form.submit();
	}
	
	function reloadFolder() {
		var form = document.watchForm;
		document.getElementById("inputVO.mode").value = "viewwatchlist";
		document.getElementById("inputVO.folderId").value = form.folderListOpt.value;
		form.submit();
	}
	
	function loadFolder(folderId) {
		var form = document.watchForm;
		document.getElementById("inputVO.mode").value = "viewwatchlist";
		document.getElementById("inputVO.folderId").value = folderId;
		form.submit();
	} 
		
	function updateEntry(mode, type, stockId) {
		var form = document.watchForm;
		
		if (mode == "deletewatch") {
			var ret = confirm("You are about to delete the entry. \nClick OK to continue or Cancel to abort action.");
			if (ret == false) {
				return false;
			}
		}
		
		
		document.getElementById("inputVO.mode").value = mode;
		document.getElementById("inputVO.type").value = type;
		document.getElementById("inputVO.stockId").value = stockId;
		form.submit();
	}
	
</SCRIPT>	

<script type="text/javascript" language="JavaScript">
// Init section
 
	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	//run_after_body();
</script>

<script type="text/javascript">

$( document ).ready(function() {

	// setup the dialog overlay
	QQ_chartoverlay.init("overlaycontent");
	
});

</script>



<s:set var="folderList" value="inputVO.folderList" />
<s:set var="watchList" value="inputVO.watchList" />


<%-- overlaycontent will be hidden, and show after some click --%>
<div id="overlaycontent"  title="The image ... Click outside of the Dialog (or ESC) to close." >This is in the overly</div>

<div class="contentwrapper" style="min-height:100px;"><div style="margin:2px;">             

 	<form name="watchForm" action="../m/watch_viewwatchlist.do"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="basic" />
        <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
        <input type="hidden" id="inputVO.stockId" name="inputVO.stockId" value="" />
        <input type="hidden" id="inputVO.folderId" name="inputVO.folderId" value="<s:property value='%{inputVO.folderId}'/>" />
        

		<table width="100%" class="gridtable">
			<tr>
			<td colspan="11" height="30">
				<font class="sub_title">My Watch List: </font><b><s:property value='%{inputVO.folderName}'/>(<s:property value='%{#watchList.size()}'/>)</b> 
				&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
				<s:if test="%{inputVO.type == \"chart\"}">
					<input type="button" name="sb" value=" No Charts " onClick="submitRequest('viewwatchlist', 'list')" class="reg_button"/>
				</s:if>
				<s:else>
					<input type="button" name="sb" value=" Charts " onClick="submitRequest('viewwatchlist', 'chart')" class="reg_button"/>
				</s:else>
				<input name="sb3" type="button" value=" Update Folder " onclick="submitRequest('updatefolder', 'blank')" class="reg_button"/>
			</td></tr>
			
			<tr><th>Order</th><th>Ticker</th>
			<th>Company</th><th>Price</th><th>Trade</th>
			<th>Open Date</th><th>Open Price</th>
			<th>Close Date</th><th>Close Price</th><th>Gain</th>
			<th>Action</th>
			</tr>
			

			<s:iterator var="itemVO" value="#watchList" status="rowStatus">
				<s:if test="%{#rowStatus.even == true}">
		    		<s:set var="rowColor" value="\"style='background-color:#ffffdf'\"" />
		    	</s:if>
		    	<s:else>
		    		<s:set var="rowColor" value="\"style='background-color:#ffffff'\"" />
		    	</s:else>
		    	
		    	<s:if test="%{#itemVO.comments != NULL && #itemVO.comments != \"\"}">
		    		<s:set var="rowSpan" value="2" />
		    	</s:if>
		    	<s:else>
		    		<s:set var="rowSpan" value="1" />
		    	</s:else>	
		    	
		    	
			<TR>
				<TD align="center" rowspan="<s:property value='#rowSpan'/>" <s:property value="#rowColor"/> >
					<input type="button" name="a" value=" <s:property value='%{#rowStatus.index + 1}'/> "
					onClick='QQ_chartoverlay.show_image("../drawchart?stockId=<s:property value='%{#itemVO.stockId}'/>&period=6&option=NNNY")' class="reg_button" />
				</TD>

				<TD align="center" rowspan="<s:property value='#rowSpan'/>"  <s:property value="#rowColor"/> >
					<A href="../s/analyze_basicanalyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.ticker=<s:property value='%{#itemVO.ticker}'/>&inputVO.period=6&inputVO.stockId=<s:property value='%{#itemVO.stockId}'/>" 
						class="datalink">
							<s:property value='%{#itemVO.ticker}'/>
						 </A> 
			    </td>
			    <td <s:property value="#rowColor"/> ><s:property value='%{#itemVO.companyName}'/></td>
			    <td align="center" <s:property value="#rowColor"/> ><s:property value='%{#itemVO.price}'/></td>
			    <td align="center" <s:property value="#rowColor"/> ><s:property value='%{#itemVO.tradeType}'/></td>
			    
			    <s:if test="%{#itemVO.openDate == \"\"}">	    
			    <td <s:property value="#rowColor"/> >&nbsp;</td>
			    <td <s:property value="#rowColor"/> >&nbsp;</td>
			    </s:if>
			    <s:else>		    
			    <td align="center" <s:property value="#rowColor"/> ><s:property value='%{#itemVO.openDate}'/></td>
			    <td align="center" <s:property value="#rowColor"/> ><s:property value='%{#itemVO.openPrice}'/></td>
			    </s:else>
			    
			    <s:if test="%{#itemVO.closeDate == \"\"}">		    
			    <td <s:property value="#rowColor"/> >&nbsp;</td>
			    <td <s:property value="#rowColor"/> >&nbsp;</td>
			    </s:if>
			    <s:else>		    
			    <td align="center" <s:property value="#rowColor"/> ><s:property value='%{#itemVO.closeDate}'/></td>
			    <td align="center" <s:property value="#rowColor"/> ><s:property value='%{#itemVO.closePrice}'/></td>
			    </s:else>
		    
			    <s:if test="%{#itemVO.gain < 0}">
			    	<td align="center" <s:property value="#rowColor"/> ><font color="#ff0000"><b><s:property value='%{#itemVO.gainPercent}'/></b></font></td>
			    </s:if>
			    <s:else>
			    	<td align="center" <s:property value="#rowColor"/> ><font color="#009955"><b><s:property value='%{#itemVO.gainPercent}'/></b></font></td>
			    </s:else>
			    
				<td width="20" rowspan="<s:property value='#rowSpan'/>" <s:property value="#rowColor"/> >
			    	<input name="db1" type="button" value=" Update " onclick="updateEntry('updatewatch', 'blank', '<s:property value="%{#itemVO.stockId}"/>')" class="reg_button"/>
					<%--
					<input name="db2" type="button" value="Delete" onclick="updateEntry('deletewatch', '<bean:write name="inputVO" property="type"/>', '<bean:write name="itemVO" property="stockId"/>')" class="button"/>	
				--%>
				</td>
			</TR>
			<s:if test="%{#itemVO.comments != NULL && #itemVO.comments != \"\"}">
				<tr><td colspan="8" <s:property value="#rowColor"/> ><font color="#002233"><b>Note:</b> </font>
				<font color="#008833">
				<s:property value='%{#itemVO.comments}'/>
				</font></td></tr>
			</s:if>
			
			<s:if test="%{inputVO.type == \"chart\"}">
			<tr><td align="center" colspan="11" ><img src="../drawchart?stockId=<s:property value='%{#itemVO.stockId}'/>&period=12&option=NNNY"></td></tr>
			</s:if>
			
			</s:iterator> 
			
			<s:if test="%{#watchList.size == 0}" >
				<TR>
					<TD colspan="11" align="center">
						<FONT color="red">No Items found in your watch list.</FONT>
					</TD>
				</TR>
			</s:if>
				
		</table>
	</form></div></div>
        