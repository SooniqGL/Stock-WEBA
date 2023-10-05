<%@ taglib prefix="s" uri="/struts-tags" %>




<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

.float{
   visibility: hidden;
   position: absolute;
   left: -3000px;
   z-index: 10;
}

.big_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
	FONT-SIZE: 9pt;
	COLOR: #0000cf;
	TEXT-DECORATION: none;
	WIDTH: 133px;
}

.sml_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif;
	FONT-SIZE: 9pt;
	COLOR: #0000cf;
	TEXT-DECORATION: none;
	WIDTH: 100px;
}

.dropdown {
   FONT-SIZE: 12pt;
   width: 400px;
   COLOR: #005588;
   background-color: #fffff0;
   TEXT-DECORATION: none;
   FONT-WEIGHT: bold;
}

</style>

<s:set var="modeList" value="inputVO.modeList" />
<s:set var="dateList" value="inputVO.scanReportDateList" />


<%--

    int dateListSize = dateList.size();
    String isPrevDisabled = "";
    if (dateListSize <= 2) {
        isPrevDisabled = "disabled";
    }

--%>

<s:set var="dateListSize" value="#dateList.size()"></s:set>
<s:if test="%{#dateListSize <= 2}">
	<s:set var="isPrevDisabled" value="disabled"></s:set>
</s:if>
<s:else>
	<s:set var="isPrevDisabled" value=""></s:set>
</s:else>

<div class="contentwrapper"><div style="margin:2px;"> 

        <form name="scanForm" action="../s/scan_scanreport2.do">
				<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="report2"/>
                <input type="hidden" id="inputVO.scanKey" name="inputVO.scanKey" value="<s:property value='%{inputVO.scanKey}'/>"/>
                <input type="hidden" id="scanIndex" name="scanIndex" value="0" />
         

                
		<table width="100%"  class="gridtable" >
			
			<tr class="light_back"><td colspan="2">&nbsp;&nbsp;<b>Scan Mode: </b>&nbsp;&nbsp;<select id="dropdown" name="dropdown" onchange="Q.scanreport.submitRequest()" class="dropdown"><option></option>
					<s:iterator var="modeVO" value="#modeList" status="rowStatus">
						<s:if test="%{#modeVO.scanKey == inputVO.scanKey}">
							<option value="<s:property value='%{#modeVO.scanKey}'/>" selected><s:property value='%{#modeVO.modeStr}'/></option>
						</s:if>
						<s:else>
							<option value="<s:property value='%{#modeVO.scanKey}'/>"><s:property value='%{#modeVO.modeStr}'/></option>
						</s:else>
					</s:iterator>
				</select></td>
			</tr>
                        <%-- if (dateListSize > 0) { --%>
                        <s:if test="%{#dateListSize > 0}">
                        <tr><td colspan="2">
                                <input type="button" name="button_prev" value="Prev" class="date_button" onClick="Q.scanreport.showChartWithShift('1')" <s:property value='%{#isPrevDisabled}'/> />
								<input type="button" name="button_next" value="Next" class="date_button" onClick="Q.scanreport.showChartWithShift('-1')" disabled />
                                <%-- try to list all the dates to here --%>
                                <select id="dateList" name="dateList" onchange="Q.scanreport.showChartFromDD()">
                                <s:iterator var="dateStr" value="#dateList" status="rowStatus">
                                    <s:if test="!#rowStatus.last">                              
                                         <option value="<s:property value='%{#rowStatus.index}'/>"><s:property value='#dateStr'/></option>
                                    </s:if>
                                </s:iterator>
                                </select>

                            </td></tr>
                        <tr><td align="center" colspan="2"><img id="report_chart" src="../drawchart?chartType=SR2&scanKey=<s:property value='%{inputVO.scanKey}'/>&scanIndex=0" width="630px"></td></tr>
                        </s:if>
                        <s:else>
                        <tr><td colspan="2"><br>&nbsp;&nbsp;<font color="#0000ff">Sorry, we do not have report for this scan type.</font></td></tr>
                        </s:else> 
			
		</table>
         
            
      </form>
	
</div></div>

