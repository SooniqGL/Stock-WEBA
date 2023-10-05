<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN">
<HTML>
<HEAD>

<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">

<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
<TITLE>random_walk.jsp</TITLE>
<script type="text/javascript" src="../jsgraph/wz_jsgraphics.js"></script>
<script type="text/javascript" src="../script/common.js"></script>
<script type="text/javascript" src="../script/draw_random.js"></script>

<script LANGUAGE="JAVASCRIPT"> 
function setFocus() {   
  document.loginForm.elements["inputVO.loginId"].focus();
} 
</script>
</HEAD> 
<BODY topmargin="0" leftmargin ="0" marginheight="0" marginwidth="0" class="page_back">
<script type="text/javascript" src="../jsgraph/wz_dragdrop.js"></script>

			<table cellpadding="0" cellspacing="0" background="../image/brick.gif">
	 		
	 		<tr><td height="30" align="center"><INPUT type="button" name="startButton" value="Start" onClick="startDraw()" disabled />
			<INPUT type="button" name="stopButton" value="Stop" onClick="stopDraw()"/></td></tr>
	 		 
	 		<tr><td height="300" width="100%" align="center">
	 		<div class="float" id="chart" style="background:#ffff00; position:relative; width: 800px; height: 300px;">
			</div></td></tr>
			 
			<tr><td height="30">&nbsp;</td></tr>
			<tr><td align="center">
				<table width="800"><tr><td>Do you believe "Random Walk"?</td></tr>
				<tr><td>We believe in many situation there is a strong force
			to drive one stock to go up or down. It is not random movement in such cases. </td></tr>
			<tr><td>When a stock is moving like a random walk, it means it lacks the force.
			Then you do need to stay away.</td></tr>
			<tr><td>Generally, there is a reason for a move.  You may just do not know.
			There are a lot of rules to make the market not random.
			</td></tr>
			
			<tr><td>Look at the following chart: </td></tr>
			</table></td></tr>
			<tr><td height="10">&nbsp;</td></tr>
			<tr><td align="center"><img src="../image/good_up.jpg"></td></tr>
						 
			<tr><td height="30">&nbsp;</td></tr>
			<tr><td align="center">
			<font size="5" color="#0066cc"><b>Our goal is to find such situations.</b></font>  
			</td></tr>
			
			<tr><td height="30">&nbsp;</td></tr>
			</table>
			


<script type="text/javascript">  
	 
<!--  // always before the "</body>"
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

// flag to do
var runFlag = true;

// data for the chart 
// var dateArray = new Array();       
//dd.elements.chart.moveTo(60, 110);
   
init(); 
   
// draw begin
startIt(true);

function init() {
//alert("init");
	var cnv = document.getElementById('chart');
	if (cnv != null) {		 
		// get the scale
		chartX = parseInt(dd.elements.chart.x, 10);  // fixed for all to remember the original location
		chartY = parseInt(dd.elements.chart.y, 10);
		chartWidth = parseInt(dd.elements.chart.w, 10);  // original size - if not change; we may change this
		chartHeight = parseInt(dd.elements.chart.h, 10);
	alert("cw: " + chartX + ", ch: " + chartY);	
		// setup the graphics
		jg = new jsGraphics("chart"); // or jsGraphics(cnv);
	} else {
		// alert("chart div is null");
	}
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

     
// dd.elements.reddot.moveTo(dd.elements.chart.x+400, reddotY);
//dd.elements.reddot.setZ(dd.elements.chart.z+1);
//dd.elements.chart.addChild('reddot');
// dd.elements.thumb.defx = dd.elements.track.x+36;

	     
//--> 
</script>

</BODY>
</HTML>
