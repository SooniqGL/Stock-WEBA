<%@ taglib prefix="s" uri="/struts-tags" %>


<script type="text/javascript" language="JavaScript">
    function goRange(orderRange) {
        //alert("here 2 ...");
        var form = document.examForm;
        document.getElementById("inputVO.orderRange").value = orderRange;
        form.submit();
    }
</script>

<style type="text/css">

.button_lrg {
   width: 65px;
}

.button_lrg2 {
   width: 90px;
}

.button_sml {
   width: 40px;
}

#examDiv {
    background-color: #222222; color: #ffffef;
}

</style>


<s:set var="rangeList" value="inputVO.rangeList" />
<s:set var="transList" value="inputVO.transList" />

<div class="contentwrapper"><div style="margin:2px;">

    
         <form name="examForm" action="../s/exam_examresult.do">
                <input type="hidden" id="inputVO.mode" name="inputVO.mode" value="E"/>
                <input type="hidden" id="inputVO.type" name="inputVO.type" value=""/>

                <%-- for the range calculation --%>
                <input type="hidden" id="inputVO.orderRange" name="inputVO.orderRange" value="<s:property value='%{inputVO.orderRange}'/>">
                

        <table width="100%" height="100%" class="gridtable">
	
             <tr>
                <td height="20" colspan="2" class="mycell1">&nbsp;&nbsp;
                <s:iterator var="runRange" value="#rangeList" status="rowStatus">	
                        <A href="javaScript:goRange('<s:property />')" class="datalink"><s:property /></a> &nbsp;
                </s:iterator>
                </td>
             </tr>

           <s:if test="%{inputVO.orderRange != \"NONE\"}">
               <TR><td width="800" colspan="2">
                    &nbsp;&nbsp;<img src="../image/reddot.gif">
                    <font color="#0000ff"><b>Showing transactions for <s:property value='%{inputVO.orderRange}'/></b></font></td>
               </TR>

               <TR><td width="800" colspan="2" style="padding:0px">
                       <table width="100%" class="gridtable" style="font-size:10px; margin:0px;">
                           <tr><td>Order #</td><td>Exam Date</td>
                               <td>Ticker</td><td>Amount</td>
                               <td>Open Date</td><td>Open Price</td>
                               <td>Close Date</td><td>Close Price</td>
                               <td>Max Positive</td><td>Max Negative</td>
                               <td>Profit</td><td>Profit(Percent)</td>
                               <td>Hold days</td>
                               <%--<td>Comments</td--%>
                           </tr>
                           
                           <s:iterator var="transItem" value="#transList" status="rowStatus">
                           <tr>
                               <td align="center"><b><s:property value='%{#transItem.orderNumber}'/></b></td>
                               <td align="center"><s:property value='%{#transItem.examDate}'/></td>
                               <td align="center">
                               <A href="../s/analyze_dynamicanalyze.do?inputVO.mode=analyze&inputVO.pageStyle=D&inputVO.option=&inputVO.type=id&inputVO.ticker=<s:property value='%{#transItem.ticker}'/>&inputVO.period=6&inputVO.stockId=<s:property value='%{#transItem.stockId}'/>" class="datalink" target="new"><s:property value='%{#transItem.ticker}'/></A>
                               </td>
                               <td align="center">$<s:property value='%{#transItem.tradeAmount}'/></td>
                               <td align="center"><s:property value='%{#transItem.openDate}'/></td>
                               <td align="center"><s:property value='%{#transItem.openPrice}'/></td>
                               <td align="center"><s:property value='%{#transItem.closeDate}'/></td>
                               <td align="center"><s:property value='%{#transItem.closePrice}'/></td>
                               

                                <s:if test="%{#transItem.maxPositive < 0}">
                                    <td align="center"><font color="#ff0000"><b><s:property value='%{#transItem.maxPositive}'/>%</b></font></td>
                                </s:if>
                                <s:else>
                                    <td align="center"><font color="#009955"><b><s:property value='%{#transItem.maxPositive}'/>%</b></font></td>
                               </s:else>
                               
                               <s:if test="%{#transItem.maxNegative < 0}">
                                    <td align="center"><font color="#ff0000"><b><s:property value='%{#transItem.maxNegative}'/>%</b></font></td>
                                </s:if>
                                <s:else>
                                    <td align="center"><font color="#009955"><b><s:property value='%{#transItem.maxNegative}'/>%</b></font></td>
                               </s:else>
                               
                               <s:if test="%{#transItem.profit < 0}">
                                    <td align="center"><font color="#ff0000"><b>$<s:property value='%{#transItem.profit}'/></b></font></td>
                                </s:if>
                                <s:else>
                                    <td align="center"><font color="#009955"><b>$<s:property value='%{#transItem.profit}'/></b></font></td>
                               </s:else>
                        
								<s:if test="%{#transItem.profitPercent < 0}">
                                    <td align="center"><font color="#ff0000"><b><s:property value='%{#transItem.profitPercent}'/>%</b></font></td>
                                </s:if>
                                <s:else>
                                    <td align="center"><font color="#009955"><b><s:property value='%{#transItem.profitPercent}'/>%</b></font></td>
                               </s:else>
                            
                                    
                               <td align="center"><s:property value='%{#transItem.totalDays}'/></td>
                               <%--td align="center"><bean:write name="transItem" property="comments"/></td--%>
                               
                           </tr>
                           </s:iterator>
                       </table>
               </TR>
               
               <%--
                <tr><td height="20" colspan="2" class="mycell1">
                        &nbsp; Summary:
                </td></tr>--%>

                

                            
           </s:if>

			<s:if test="%{inputVO.orderRange == \"NONE\"}">
                    <TR><td width="800" colspan="2"><br>
                    &nbsp;&nbsp;<img src="../image/reddot.gif">
                    <font color="#ff0000"><b>No Exam Transactions are found in our DB.</b></font></td>
                    </TR>
            </s:if>


            <s:if test="%{inputVO.orderRange == \"error\"}">
                    <TR><td width="800" colspan="2"><br>
                    &nbsp;&nbsp;<img src="../image/reddot.gif"><font color="#ff0000">Error - in our server.</font></td></TR>
            </s:if>


                </table>
	</form>

                </div>
            </div>
            