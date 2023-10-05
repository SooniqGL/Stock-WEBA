<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %>
<%@ page
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>
<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<meta http-equiv="expires" content="0"/>
<meta http-equiv="Cache-Control" content="no-cache" />

<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">

<script src="../jsgraph/wz_jsgraphics.js" type="text/javascript"></script>
<script src="../jsgraph/wz_dragdrop.js" type="text/javascript"></script>
<script src="../script/common.js" type="text/javascript" ></script>
<SCRIPT SRC="../script/date_script.js" type="text/javascript"></SCRIPT>
<SCRIPT SRC="exam_helper.js" type="text/javascript"></SCRIPT>
<SCRIPT SRC="exam_barchart.js" type="text/javascript"></SCRIPT>

<%--SCRIPT SRC="../script/onmouse.js"></SCRIPT--%>
<SCRIPT SRC="AjaxRequest.js" type="text/javascript"></SCRIPT>

<TITLE>analyze_basic.jsp</TITLE>

<%--
<script type="text/javascript" language="JavaScript">
// Init section

	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	run_after_body();
</script>
--%>
<style type="text/css">

.button_lrg {
   width: 65px;
}

.button_sml {
   width: 40px;
}

</style>
</HEAD>
<jsp:useBean id="examForm" scope="session" type="com.greenfield.ui.action.exam.ExamActionForm"/>
<bean:define id="inputVO" name="examForm" property="inputVO" type="com.greenfield.common.object.exam.ExamVO"/>
<bean:define id="stockExt" name="inputVO" property="stockExt" type="com.greenfield.common.object.stock.StockExt"/>
<bean:define id="stock" name="stockExt" property="stock" type="com.greenfield.common.object.stock.Stock"/>
<bean:define id="performVO" name="inputVO" property="performVO" type="com.greenfield.common.object.exam.PerformVO"/>

<BODY class="page_back" onload="setFocus()">
    <%--div id="overlay_cloak2">
        <!--[if IE 6]><iframe src="../base/empty.html" class="cloak_iframe"></iframe><![endif]-->
    </div>
    <div class="overlay_content" id="div_200">
        <table width="400" height="200"><tr><td width="100%"><h1><bean:write name='stock' property='companyName'/></h1></td></tr>
            <tr><td>
        Abstruct: <bean:write name='stock' property='aboutCompany'/>
                </td></tr></table>
    </div--%>
    <html:form action="/e/exam" onsubmit="javascript:submitTickerForExam()">
                <html:hidden property="inputVO.mode" value="S"/>
                <html:hidden property="inputVO.type" value=""/>
                <input type="hidden" name="inputVO.stockId" value="<bean:write name='inputVO' property='stockId'/>"/>
                <input type="hidden" name="inputVO.period" value="6"/>
                <%--input type="hidden" name="inputVO.option" value="<bean:write name='inputVO' property='option'/>"/>
                
                <input type="hidden" name="inputVO.pageStyle" value="S"/>

                <input type="hidden" name="inputVO.showFlag" value="<bean:write name='inputVO' property='showFlag'/>">
                <input type="hidden" name="inputVO.doSP500" value="<bean:write name='inputVO' property='doSP500'/>">

                <input type="hidden" name="backupTicker" value="<bean:write name='inputVO' property='ticker'/>"--%>
                <input type="hidden" name="dailyDataStr" value="<bean:write name='inputVO' property='dailyDataStr'/>">
                <%-- for the date calculation --%>
                <input type="hidden" name="startDate" value="<bean:write name='inputVO' property='startDate'/>">
                <input type="hidden" name="endDate" value="<bean:write name='inputVO' property='endDate'/>">


<center>
<table width="932" height="100%" class="table_back" cellpadding="0" cellspacing="0">
	<tr><td class="head_color" valign="top" width="120" height="100%"><%@include file="../base/left.jsp" %></td>
	<td valign="top" width="812" ><table width="100%" cellpadding="0" cellspacing="0" height="100%">
	<tr class="tail_color"><td height="40" align="center" colspan="2"><font  class="main_title">Self Exam Simulation</font></td></tr>

	<tr><td align="center" colspan="2" width="100%" valign="top"><center><table border="1" width="100%">

             <tr class="gray_back">
                <td height="20" colspan="2">
                Ticker: <input type="text" name="inputVO.ticker" value="<bean:write name='inputVO' property='ticker'/>" size="10"/>
                        <input type="button" name="button_self" value="Self Select" class="date_button" onClick="doSelfSelect()"/>
                        <input type="button" name="button_auto" value="Auto Select" class="date_button" onClick="doAutoSelect()"/>
                </td>
             </tr>

           <logic:equal name="inputVO" property="display" value="one">

               <tr><td colspan="2">&nbsp;<b><bean:write name='stock' property='companyName'/></b>
                                    ( <b><bean:write name='stock' property='ticker'/></b> )
                  </td>
               </tr>
                            
               <tr class="gray_back">
                <td height="20" colspan="2">
                        <input type="radio" name="ckPeriod" value="3" onClick="setPeriod('3')">3m
                        <input type="radio" name="ckPeriod" value="6" onClick="setPeriod('6')">6m
                        <input type="radio" name="ckPeriod" value="12" onClick="setPeriod('12')">1y
                        <%--
                        <input type="radio" name="ckPeriod" value="24" onClick="setPeriod('24')">2y
                        --%>
                        <input type="checkbox" name="doEMA20" onClick="setOption()" checked />20EMA
                        <input type="checkbox" name="doEMA50" onClick="setOption()" checked />50EMA
                        <input type="checkbox" name="doEMA100" onClick="setOption()" checked />100EMA &nbsp;
                        <%--
                        <input type="checkbox" name="doZone" onClick="setOption()"/>Zone
                        <input type="checkbox" name="doBARS" onClick="setOption()"/>BARS &nbsp;
                        
                        <a href="javaScript:goStockId('MKT001')" class="datalink">Nasdaq</a> |
                        <a href="javaScript:goStockId('MKT002')" class="datalink">Dow</a> |
                        <a href="javaScript:goStockId('MKT003')" class="datalink">SP500</a>
                        --%>
                </td>
                </tr>
                <tr class="gray_back"><td height="20" colspan="2">Forward
                        <input type="button" name="button_right_xd"  value=">" class="button_sml" onClick="goByDayLoop('1', 'true')" />
                    <input type="button" name="button_right_xxd" value=">>" class="button_sml" onClick="goByDayLoop('5', 'true')" />
                    <input type="button" name="button_right_1d"  value="> 1d" class="button_lrg" onClick="goByDay('1')" />
                    <input type="button" name="button_right_5d"  value="> 5d" class="button_lrg" onClick="goByDay('5')" />
                    <input type="button" name="button_right_10d" value="> 10d" class="button_lrg" onClick="goByDay('10')" />
                    <input type="button" name="button_right_1m"  value="> 1M" class="button_lrg" onClick="goByMonth('1')" />
                    &nbsp; Action
                    <input type="button" name="button_long"  value="Long" class="button_lrg" onClick="goLong()" />
                    <input type="button" name="button_short"  value="Short" class="button_lrg" onClick="goShort()" />
                    <input type="button" name="button_close"  value="Close It" class="button_lrg" onClick="goClose()" disabled />
                </td></tr>

                <tr><%-- Do not show image
                    <td width="682"><img id="stock_chart" src="../drawchart?stockId=<bean:write name='stock' property='stockId'/>&period=6&option=NNNY"></td>
                    --%>
                    <td width="682">
                    <div id="chart" class="chart" style="position:relative; width: 680px; height: 400px;">AAAABBBBB</div>
                    </td>
                    <td valign="top" width="113">
                        <div id="priceBoard"></div><hr>
                        <div id="positionBoard"></div>
                        <%--
                        <table cellpadding="0" cellspacing="0" width="100%">
                        <tr><td class="table_cell">Price</td>
                        <td class="table_cell"><b><bean:write name='stock' property='price'/></b></td>
                                </tr>
                                <tr><td class="table_cell">Power</td>
                                <td class="table_cell"><b><bean:write name="stockExt" property="powerIndex"/></b></td>
                                </tr>
                                <tr><td class="table_cell">Correlation</td>
                                <td class="table_cell"><b><bean:write name="stockExt" property="correlationIndex"/></b></td>
                                </tr>
                                <tr><td class="table_cell">Short Term</td>
                                        <td class="table_cell">
                                                <logic:equal name="stockExt" property="shortTermStatus" value="BUY">
                                                <font color="#00ff00"><b>Buy</b></font>
                                                </logic:equal>
                                                <logic:equal name="stockExt" property="shortTermStatus" value="SELL">
                                                <font color="#ff0000"><b>Sell</b></font>
                                                </logic:equal>
                                                <logic:equal name="stockExt" property="shortTermStatus" value="HOLD">
                                                <font color="#0000ff"><b>Hold</b></font>
                                                </logic:equal>
                                                <%--(<bean:write name="stockExt" property="longTermTrend"/>)
                                        </td>
                                </tr>
                                <tr><td class="table_cell">Long Term</td>
                                        <td class="table_cell">
                                                <logic:equal name="stockExt" property="majorTrend" value="U">
                                                <font color="#00ff00"><b>Up</b></font>
                                                </logic:equal>
                                                <logic:equal name="stockExt" property="majorTrend" value="D">
                                                <font color="#ff0000"><b>Down</b></font>
                                                </logic:equal>
                                        </td>
                                </tr>

                                <logic:notEqual name="stockExt" property="warningMessage" value="">
                                        <tr>
                                                <td colspan="2" class="table_cell">Warning:<br> <font color="#ff0000"><bean:write name="stockExt" property="warningMessage"/></font></td>
                                        </tr>
                                </logic:notEqual>
                                <logic:equal name="stockExt" property="warningMessage" value="">
                                        <tr>
                                                <td class="table_cell">Warning</td><td class="table_cell"><b>None</b></td>
                                        </tr>
                                </logic:equal> --%>
                            <%--
                            <tr><td class="table_cell">Market</td>
                                    <td class="table_cell">
                                            <logic:equal name="inputVO" property="marketTrend" value="Up">
                                            <font color="#00ff00"><b>Up</b></font>
                                            </logic:equal>
                                            <logic:equal name="inputVO" property="marketTrend" value="Down">
                                            <font color="#ff0000"><b>Down</b></font>
                                            </logic:equal>

                                    </td>
                            </tr>
                            <tr><td height="100%" colspan="2">&nbsp;</td>
                            </tr></table>--%>

                    </td></tr>


                <%-- performance of the holding 
                <tr class="gray_back"><td height="20" colspan="2">

                    </td></tr>--%>
                            
           </logic:equal>

           <logic:notEqual name="inputVO" property="mode" value="blank">
                <logic:equal name="inputVO" property="display" value="zero">
                        <TR><td width="800" colspan="2"><br>
                        &nbsp;&nbsp;<img src="../image/reddot.gif">
                        <font color="#ff0000"><b>This ticker has not been found in our DB.</b></font></td>
                        </TR>
                </logic:equal>
            </logic:notEqual>

            <logic:equal name="inputVO" property="display" value="error">
                    <TR><td width="800" colspan="2"><br>
                    &nbsp;&nbsp;<img src="../image/reddot.gif"><font color="#ff0000">Error</font></td></TR>
            </logic:equal>


            <logic:equal name="inputVO" property="display" value="one">
                <tr>
                <td colspan="2">&nbsp;History and performance --
                    <b><bean:write name='stock' property='companyName'/></b>
                    ( <b><bean:write name='stock' property='ticker'/></b> )
                    <br>
                    <div id="tradeHistory"></div>
                </td></tr>
            </logic:equal>

			<%--
			<logic:notEqual name="inputVO" property="display" value="one">
			<tr><td height="100%" align="center" colspan="3">&nbsp;</td></tr>
			</logic:notEqual> --%>
                </table></center></td></tr>
                <tr><td height="100%" align="center" colspan="2"></td></tr>
                <tr class="tail_color"><td colspan="2"><%@include file="../base/footer.jsp" %></td></tr>
                </table></td></tr>

		</table></center>
	</html:form>


<script type="text/javascript">

<!--  // always before the "</body>"
// make it to draggable, need wz_dragdrop.js (+ wz_jsgraphics.js)
//  make DIVs draggable; use: "chart"+SCALABLE); to make SCALABLE - use +shift and mouse.
// SET_DHTML(CURSOR_MOVE, "chart"+NO_DRAG, "reddot"+HORIZONTAL+MAXOFFLEFT+800+MAXOFFRIGHT+800);
SET_DHTML(CURSOR_MOVE, "chart");

// Can be defined only after "SET_DHTML" if there is "SET_DHTML" CALL.
var jg = null; //new jsGraphics("chart"); // set in init().

// remember the position
var chartX = 0;   // location of the DIV - if not moved
var chartY = 0;

// windown size
var chartWidth = 0;
var chartHeight = 0;

// data for the chart
var dateArray = new Array();
var openLogArray = new Array();  // take Math.log()
var closeLogArray = new Array();
var minLogArray = new Array();
var maxLogArray = new Array();
var closeArray = new Array();  // keep original val
var minArray = new Array();
var maxArray = new Array();
var logAvg20Array = new Array();
var logAvg50Array = new Array();
var logAvg100Array = new Array();
var volumeArray = new Array();
var growthArray = new Array();
var forceArray = new Array();
var trendColorArray = new Array();
var marketTrendArray = new Array();
var monthArray = new Array();
var dataType = "";

var dataSize = 0;        // Size of the data array for all data

// variables to track transactions
var tradeOpenDateArray   = new Array();
var tradeCloseDateArray  = new Array();
var tradeOpenPriceArray  = new Array();
var tradeClosePriceArray = new Array();
var tradeTypeArray       = new Array();
var tradeLengthArray     = new Array();
var tradeMaxPosArray     = new Array();
var tradeMaxNegArray     = new Array();
var numClosedPos = 0;    // closed so far
var currentOpenDate = "";
var currentOpenPrice = 0;
var currentTradeType = "";  // Long or Short
var openIndex = 0;   // position is opened, to used for num days
var tradeAmt = 5000;  // for each persition

// for moving loop
var moveFlag = "false";

// prepare the data and draw
init();
prepareData();
updateEndIndex(0);
drawChart();
adjustButtons();

// data for the chart
//dd.elements.chart.moveTo(60, 110);

function init() {
    //alert("init");
	var cnv = document.getElementById('chart');
	if (cnv != null) {
		// get the scale
		chartX = parseInt(dd.elements.chart.x, 10);  // fixed for all to remember the original location
		chartY = parseInt(dd.elements.chart.y, 10);
		chartWidth = parseInt(dd.elements.chart.w, 10);  // original size - if not change; we may change this
		chartHeight = parseInt(dd.elements.chart.h, 10);

		// setup the graphics
		jg = new jsGraphics("chart"); // or jsGraphics(cnv);
	} else {
            // alert("chart div is null");
	}

    //alert("init done");
}

function startDraw() {
	runFlag = true;
	startIt(true);
	var cnv = document.getElementById('startButton');
	cnv.disabled = true;
	var cnv2 = document.getElementById('stopButton');
	cnv2.disabled = false;
}

function stopDraw() {
	runFlag = false;
	var cnv = document.getElementById('startButton');
	cnv.disabled = false;
	var cnv2 = document.getElementById('stopButton');
	cnv2.disabled = true;
}

// has to be defined after "SET_DHTML"
function my_DragFunc() {
    if (dd.obj.name == 'reddot') {
        var xPos = parseInt(dd.elements.reddot.x, 10);
        if (prevReddotX - xPos > xStep || prevReddotX - xPos < - xStep) {
        	updateEndIndex(xPos);
        	prevReddotX = xPos;
        }

        // always move the chart back to the original location
		//dd.elements.reddot.moveTo(dd.elements.chart.x+300, dd.elements.chart.y + 30);
   }
}

function my_DropFunc()
{
    if (dd.obj.name == 'reddot')
    {
    // alert("is called");
        jg.clear();
	    drawChart();
    }
}

// not used for now
function my_ResizeFunc()
{
// alert("resized");
    if (dd.obj.name == 'reddot') {
    	jg.clear();
    	jg.setColor("#0000ff");
        jg.drawRect(0, 0, 100, 200); //dd.elements.chart.w, dd.elements.chart.h);
        jg.paint();
    }
}

function resetDataArrays() {
	dateArray = new Array();
	openLogArray = new Array();
        closeArray = new Array();
        minArray = new Array();
        maxArray = new Array();
	closeLogArray = new Array();
	minLogArray = new Array();
	maxLogArray = new Array();
	logAvg20Array = new Array();
	logAvg50Array = new Array();
	logAvg100Array = new Array();
	volumeArray = new Array();
	growthArray = new Array();
	forceArray = new Array();
	trendColorArray = new Array();
	marketTrendArray = new Array();
	monthArray = new Array();
}

// dd.elements.reddot.moveTo(dd.elements.chart.x+400, reddotY);
//dd.elements.reddot.setZ(dd.elements.chart.z+1);
//dd.elements.chart.addChild('reddot');
// dd.elements.thumb.defx = dd.elements.track.x+36;


//-->
</script>

</BODY>
</HTML>
