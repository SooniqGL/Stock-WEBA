<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">

#examDiv {
    background-color: #004422; color: #ffffef; margin-bottom:10px;
}

</style>
<div class="leftsidewrapper">
<span  style="display:table; margin-left:15px; padding:5px;">

<h3>Member Links</h3>
<A href="/WEBA/m/home.do?inputVO.mode=blank" class="datalink">Member Home</A><br/>
<a href="../m/profile_profile.do?inputVO.mode=blank&inputVO.type=none" class="datalink">Member Profile</a><br/><br/>
		
		
<h3>Exam Menu Links</h3>
       <%--  a href="#" onClick="popTopsection()" class="datalink">Show/Hide</a><br />--%>
       <A href="/WEBA/s/exam_examcontent.do?inputVO.mode=blank" class="datalink">Exam Home</A><br/>
       <A href="../s/exam_examresult.do?inputVO.mode=E&inputVO.orderRange=" class="datalink">Exam Results</A> <br/>
       <a href="/WEBA/s/exam_examfaq.do?inputVO.mode=examfaq" class="datalink">Exam FAQ's</a><br/><br/>
       
                <s:if test="%{inputVO.display == \"one\"}">
                    <div id="examDiv">
                    &nbsp;Chart Options:<br/>
                    &nbsp;<input type="radio" name="ckPeriod" value="3" onClick="QQ_examhelper.setPeriod('3')">3m<br/>
                        &nbsp;<input type="radio" name="ckPeriod" value="6" onClick="QQ_examhelper.setPeriod('6')" checked >6m<br/>
                        &nbsp;<input type="radio" name="ckPeriod" value="12" onClick="QQ_examhelper.setPeriod('12')">1y<br/>
                        <%--
                        <input type="radio" name="ckPeriod" value="24" onClick="setPeriod('24')">2y<br/>
                        --%>
                        &nbsp;<input type="checkbox" id="doEMA20" name="doEMA20" onClick="QQ_examhelper.setOption()" checked />20EMA<br/>
                        &nbsp;<input type="checkbox" id="doEMA50" name="doEMA50" onClick="QQ_examhelper.setOption()" />50EMA<br/>
                        &nbsp;<input type="checkbox" id="doEMA100" name="doEMA100" onClick="QQ_examhelper.setOption()" />100EMA<br/>
                        &nbsp;<input type="checkbox" id="doZone" name="doZone" onClick="QQ_examhelper.setOption()" checked/>Zone<br/>
                        &nbsp;<input type="checkbox" id="doBARS" name="doBARS" onClick="QQ_examhelper.setOption()" checked/>BARS<br/>
                        &nbsp;<input type="checkbox" id="doReverse" name="doReverse" onClick="QQ_examhelper.setOption()" />Reverse<br/>
                        &nbsp;<input type="checkbox" id="doTradePoint" name="doTradePoint" onClick="QQ_examhelper.setOption()" />Hint<br/>
                        
                        <%--
                        <a href="javaScript:goStockId('MKT001')" class="datalink">Nasdaq</a> |
                        <a href="javaScript:goStockId('MKT002')" class="datalink">Dow</a> |
                        <a href="javaScript:goStockId('MKT003')" class="datalink">SP500</a>
                        --%>
                    </div>
                </s:if>
        
</span>
</div>