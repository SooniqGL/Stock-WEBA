
<!DOCTYPE HTML>


<html>
<head>

<title>Basic Exam</title>
<meta name="description" content="website description" />
<meta name="keywords" content="website keywords, website keywords" />
<meta http-equiv="content-type"
	content="text/html; charset=windows-1252" />
<meta http-equiv="robots" content="noindex,nofollow" />



<link rel="stylesheet" type="text/css"
	media="screen and (max-width: 1288px)"
	href="../theme/baselayout2_fix.css" />

<link rel="stylesheet" type="text/css"
	media="screen and (min-width: 1289px)"
	href="../theme/baselayout2_lrg.css" />

<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">



<SCRIPT SRC="../script/jquery-2.1.1.min.js"></SCRIPT>

<SCRIPT SRC="../script/jquery.mousewheel.min.js"></SCRIPT>

<script src="../script/common.js" type="text/javascript"></script>
<SCRIPT SRC="../script/date_script.js" type="text/javascript"></SCRIPT>

<SCRIPT SRC="/WEBA/exam/exam_helper.js" type="text/javascript"></SCRIPT>
<SCRIPT SRC="/WEBA/exam/exam_barchart.js" type="text/javascript"></SCRIPT>

<SCRIPT SRC="/WEBA/exam/AjaxRequest.js" type="text/javascript"></SCRIPT>
</head>

<!-- This layout is designed for all the general purpose pages, after customers logged in to the site. -->
<body>
	<div id="main">
		<header>
			<div id="logo">


				<style type="text/css">
#toparea {
	background: #ffff00;
	width: 1008px;
	margin-left: auto;
	margin-right: auto;
}

#toptitle {
	float: left;
	width: 700px; /*Width of left column*/
	margin-left: -1008px; /*Set left margin to -(MainContainerWidth)*/
	height: 85px;
}

#toptitletube {
	position: relative;
	margin: 20px auto;
	height: 85px;
	width: 350px;
	/*background: #ff5588; */
}

#toptitletube H1 {
	color: #ff0;
	font-style: italic;
}

#topuser {
	margin-left: 700px; /*Set left margin to LeftColumnWidth*/
	height: 85px;
}

#topcontentwrapper {
	float: left;
	width: 100%;
	height: 85px;
}

#topsubtitle {
	clear: both;
	background: #ffffdf;
	height: 60px;
	z-index: 49;
	filter: progid:DXImageTransform.Microsoft.gradient(startColorstr='#005522',
		endColorstr='#0000aa'); /* for IE */
	background: -webkit-gradient(linear, left top, left bottom, from(#005522),
		to(#0000aa)); /* for webkit browsers */
	background: -moz-linear-gradient(top, #005522, #0000aa);
	/* for firefox 3.6+ */
	background-image: -ms-linear-gradient(top, #005522 0%, #0000aa 100%);
	/* for IE 10 */
}

#topsubtitletube {
	position: relative;
	top: 40px;
	/*background: #ffffdf;  */
	text-align: center;
	margin: 0 auto;
	width: 450px;
	height: 26px;
	z-index: 50; /* defined so less than drop menu z-index = 90 */
	FONT-FAMILY: Arial, Helvetica, sans-serif, Verdana;
	COLOR: #ff0066;
	FONT-SIZE: 14pt;
	FONT-WEIGHT: bold;
	TEXT-DECORATION: none;
}
</style>






				<div id="toparea">
					<div id="topcontentwrapper">
						<div id="topuser">
							<br /> <font color="#ffffcc"><b>Admin </b></font> <br />
							<font color="#ffffef"><b>May 18, 2014</b></font>
						</div>
					</div>
					<div id="toptitle">
						<div id="toptitletube">
							<h1>Sooniq Stock System</h1>
						</div>
					</div>

				</div>


			</div>
			<nav>
				<div id="menu_container">




					<script type="text/javascript">
						// Nested Side Bar Menu (Mar 20th, 09)
						var menuids = [ "sidebarmenu1" ]; //Enter id(s) of each Side Bar Menu's main UL, separated by commas
						if (window.addEventListener) {
							window.addEventListener("load", function() {
								Q.common.initsidebarmenu(menuids);
							}, false);
						} else if (window.attachEvent) {
							window.attachEvent("onload", function() {
								Q.common.initsidebarmenu(menuids);
							});
						}
					</script>


					<div style="height: 10px;"></div>
					<div class="sidebarmenu">
						<ul id="sidebarmenu1">
							<li><A href="/WEBA/s/home.do?inputVO.mode=blank"
								class="sublink">Home</A></li>
							<li><A href="#" class="sublink">Search</A>
								<ul>
									<li><A href="/WEBA/s/scan_basicscan.do?inputVO.mode=basic">Search
											Home</A></li>
									<li><a
										href="/WEBA/s/scan_scanreport.do?inputVO.mode=report">Current
											View Reports</a></li>
									<li><a
										href="/WEBA/s/scan_scanreport2.do?inputVO.mode=report2">Historic
											View Reports</a></li>
									<li>
									<a href="/WEBA/s/scan_scanchart.do?inputVO.mode=chart" class="datalink">Historic Chart Reports</a></li>
									
									<li><a
										href="/WEBA/s/agesrch_agesrchresult.do?inputVO.mode=search&inputVO.selectRange=">Stock
											Age Search</a></li>
								</ul></li>

							<li><A href="#" class="sublink">Analysis</A>
								<ul>
									<li><a
										href="/WEBA/s/analyze_basicanalyze.do?inputVO.mode=blank&inputVO.pageStyle=S">Basic
											Chart Analysis</a></li>
									<li><a
										href="/WEBA/s/analyze_dynamicanalyze.do?inputVO.mode=blank&inputVO.pageStyle=D">Dynamic
											Chart Analysis</a></li>
									<li><a
										href="/WEBA/s/analyze_basiccalculator.do?inputVO.mode=basiccalculator">Risk
											Calculator</a></li>
								</ul></li>

							<li><A
								href="/WEBA/s/market_marketpulse.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MB&inputVO.period=24"
								class="sublink">Market</A></li>

							<li><A
								href="/WEBA/f/forum.do?inputVO.mode=q&inputVO.subjectId=0"
								class="sublink">Forum</A></li>
							<li><A href="#" class="sublink">Member</A>
								<ul>
									<li><A href="/WEBA/m/home.do?inputVO.mode=blank">Member
											Home</A></li>
									<li><a
										href="/WEBA/m/profile_profile.do?inputVO.mode=blank&inputVO.marketType=none">My
											Profile</a></li>
									<li><a
										href="/WEBA/m/watch.do?inputVO.mode=alertlist&inputVO.marketType=blank">My
											Alert List</a></li>
									<li><a
										href="/WEBA/m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=S&inputVO.currLink=1">My
											Messages</a></li>
								</ul></li>
							<li><A href="/WEBA/s/exam_examcontent.do?inputVO.mode=blank"
								class="sublink">Exam</A></li>
							<li><A href="/WEBA/h/help.do?inputVO.mode=blank"
								class="sublink">Help</A></li>




							<li><A href="/WEBA/d/stockadmin.do?inputVO.mode=blank"
								class="sublink">Admin</A></li>


							<li><A class="sublink" href="/WEBA/p/signoff.do">Logout</A></li>

						</ul>

					</div>





				</div>
			</nav>
		</header>
		<div id="site_content">
			<div id="leftside_container">
				<div id="subtitle">
					<span
						style="FONT-FAMILY: Times New Roman, sans-serif, Verdana; COLOR: #006699; FONT-SIZE: 28pt; FONT-WEIGHT: bold; display: table; margin: auto;">Basic
						Exam</span>
				</div>

				<div id="leftsidebar">



					<div class="leftsidewrapper">
						<span style="display: table; margin-left: 15px; padding: 5px;">
							<h3>Some menu links</h3> <a
							href="../s/scan_scanreport.do?inputVO.mode=report"
							class="datalink">Current Reports</a><br /> <a
							href="../s/scan_scanreport2.do?inputVO.mode=report2"
							class="datalink">Historic Reports</a><br /> <a
							href="../s/scan_scanchart.do?inputVO.mode=chart"
							class="datalink">Historic Charts</a><br /><a
							href="../s/agesrch_agesrchresult.do?inputVO.mode=search&inputVO.selectRange="
							class="datalink">Stock Age Search</a><br /> <a href="#"
							class="sublink">Exam FAQ's</a><br /> <a href="#"
							onClick="popTopsection()" class="datalink">Show/Hide</a><br /> <A
							href="../e/exam.do?inputVO.mode=E&inputVO.orderRange="
							class="datalink">Exam Results</A> <br />
						<br />
						<br />



						</span>
					</div>




				</div>
				<div id="content">



					<style type="text/css">
.button_lrg {
	width: 65px;
}

.button_lrg2 {
	width: 90px;
}

.button_lrg3 {
	width: 110px;
}

.button_sml {
	width: 40px;
}

#examDiv {
	background-color: #222222;
	color: #ffffef;
}
</style>

					<script type="text/javascript">
						$(document).ready(function() {
							// set focus, buttons
							QQ_examhelper.setFocus();

						});

						/*
						 $(window).resize(function() {
						 QQ_exambarchart.clearCanvas();
						 QQ_exambarchart.adjustCanvasWidth();
						 QQ_exambarchart.drawChart();
						 }); */
					</script>






					<div class="contentwrapper">
						<div
							style="margin:2px;>
		<form name="examForm" action="../s/exam_examcontent.do"> 
				<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="S" />
                <input type="hidden" id="inputVO.type" name="inputVO.type" value="" />
                <input type="hidden" id="inputVO.stockId" name="inputVO.stockId" value=""/>
                <input type="hidden" id="inputVO.period" name="inputVO.period" value="6"/>
                
                
                
                
                <input type="hidden" id="startDate" name="startDate" value="">
                <input type="hidden" id="endDate" name="endDate" value="">
                <input type="hidden" id="stopType" value="N"/>

        <table width="100%" class="mytable1">
	
             <tr>
                <td height="20" colspan="2" class="mycell1">&nbsp;&nbsp;Ticker: <input type="text" id="inputVO.ticker" name="inputVO.ticker" value="" size="10"/>
                        <input type="submit" name="button_self" value=" Self Select " class="button_lrg3" onClick="QQ_examhelper.doSelfSelect()"/>
                        <input type="button" name="button_auto" value=" Auto Select " class="button_lrg3" onClick="QQ_examhelper.doAutoSelect()"/>
                </td>
             </tr>

          

			
			
            
            
                        <TR><td style="width:800px; padding:5px; " colspan="10"><br>
                        <span class="instruction_text">
                        &nbsp;&nbsp;<img src="../image/reddot.gif">Please enter a ticker, click Self Select or click Auto Select to let system to random assign stock for you.
                        </span>
                        <br><br>
                        <span class="instruction_text">
                        <h3>Some Instructions to use this page goes to here</h3>
                        
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        This is how to use the page.<br/>
                        </span>
                        </td></TR>
                    

			

        </table>
	</form>
<form name="dataForm" action=""> 
<input type="hidden" id="dailyDataStr" name="dailyDataStr" value="">
</form>
                </div>
            </div>
            
			<div class="contentwrapper2"><div style="margin:2px;">
<h3>About exam ...</h3>
</div></div>
		    
		    
		    
        </div>
      </div>
      <div id="rightsidebar">
      	    <div class="rightsidewrapper">
<span  style="display:table; margin:auto;">
<br>
<h2>Most talked topics.</h2>
<br>
<ol>
<li>Number one</li>
<li>Number two</li>
<li>Number three</li>
</ol>

<br><br>
<h2>Great stock list</h2>
<br>
<img src="../image/huangshan.gif" style="width:95%;max-width:400px;">

<h2>Most talked topics.</h2>
<br>
<ol>
<li>Number one</li>
<li>Number two</li>
<li>Number three</li>
</ol>

<h2>Most talked topics.</h2>
<br>
<ol>
<li>Number one</li>
<li>Number two</li>
<li>Number three</li>
<li>Number one</li>
<li>Number three</li>
</ol>
</span>
</div>
      	    
      	    
      	    
      	    
      </div>
      
      <div class="clear"></div>
	    <div id="tail">
			
	    </div>
	    <div id="scroll">
	      
	    </div>
       </div>
    
	    <footer>
	      <div id="pre_footer"></div>
<div id="footer"><table width="100%" border="0" cellpadding="0" cellspacing="0" height="20">
	<tr><td align="center" colspan="2"><font color="#0000ff">&copy;2000-2006 Copyright of Sooniq Technology Corporation.</font>
             
            </td></tr>
</table></div>
	    </footer>
  </div>
 
</body>
</html>
