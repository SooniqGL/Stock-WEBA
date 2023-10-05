<%@ taglib prefix="s" uri="/struts-tags" %>


 
<s:set var="modeList" value="inputVO.modeList" />


<div class="contentwrapper"><div style="margin:2px;"> 
	       
		<table width="100%"  class="gridtable" >
			<s:iterator var="itemVO" value="#modeList" status="rowStatus">
			<tr class="gray_back">
				<td height="20">Scan Mode: <s:property value="#itemVO.modeStr"/>
				</td>
			</tr>
			<tr><td align="center"><img src="../drawchart?chartType=SR&scanKey=<s:property value='#itemVO.scanKey'/>"></td></tr>
			</s:iterator>
		</table>
		
</div></div>           

