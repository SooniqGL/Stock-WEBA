<!DOCTYPE HTML>


<html>
<head>

  <title>Basic Scan</title>
  <meta name="description" content="website description" />
  <meta name="keywords" content="website keywords, website keywords" />
  <meta http-equiv="content-type" content="text/html; charset=windows-1252" />
  <meta http-equiv="robots" content="noindex,nofollow" />
  
   

  <link rel="stylesheet" type="text/css"
  		media="screen and (max-width: 1288px)" href="../theme/baselayout2_fix.css" />
  
  <link rel="stylesheet" type="text/css"
  		media="screen and (min-width: 1289px)" href="../theme/baselayout2_lrg.css" />  		
   
   <LINK href="/WEBA/theme/main_style.css" rel="stylesheet" type="text/css">

<SCRIPT SRC="/WEBA/scan/basic_scan_helper.js"></SCRIPT>

<SCRIPT SRC="/WEBA/script/overlay_global.js"></SCRIPT>

<SCRIPT SRC="/WEBA/script/jquery-2.1.1.min.js"></SCRIPT>
</head>

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
    <div id="topcontentwrapper"><div id="topuser"><br/>
        <font color="#ffffcc"><b>Admin Zhang</b></font>
        <br/><font color="#ffffef"><b>Apr 29, 2014</b></font>
    </div>
    </div>
    <div id="toptitle"><div id="toptitletube">
        <h1>Sooniq Stock System</h1>
        </div>
    </div>
    
</div>

       
      </div>
      <nav>
        <div id="menu_container">
          



<style type="text/css">

/* menu */
    
.sidebarmenu ul{
margin: 0;
padding: 0;
list-style-type: none;
font: bold 13px Verdana;
background-color: #000; 
/* width: 180px; Main Menu Item widths */
/* border-bottom: 1px solid #fcc; */
z-index:90;  /* to make sure the menu is on the top of other layers */
}

/* second level or under */
.sidebarmenu ul ul li{
position: relative;
float:left;
width: 200px;
}

/* first child level */
.sidebarmenu ul li{
position: relative;
float:left;
width: 95px;
}



/* Top level menu links style */
.sidebarmenu ul ul li a{
display: block;
overflow: auto; /*force hasLayout in IE7 */
color: white;
text-decoration: none;
padding: 6px;
border: 1px solid #778;
/*border-right: 1px solid #778; */
height:20px;
}

.sidebarmenu ul li a{
display: block;
overflow: auto; /*force hasLayout in IE7 */
color: white;
text-decoration: none;
padding: 6px;
border-left: 1px solid #778;
/*border-right: 1px solid #778; */
height:20px;
}

.sidebarmenu ul li a:link, .sidebarmenu ul li a:visited, .sidebarmenu ul li a:active{
background-color: #004422; /*background of tabs (default state)*/
color: white;
}

.sidebarmenu ul li a:visited {
color: white;
}

.sidebarmenu ul li a:hover{
background-color: #aa0000;
}

/*Sub level menu items */
.sidebarmenu ul li ul{
position: absolute;
width: 110px; /*Sub Menu Items width */
top: 0;
visibility: hidden;
}

.sidebarmenu a.subfolderstyle{
background: url(../image/right.gif) no-repeat 97% 50%;
}

 
/* Holly Hack for IE \*/
* html .sidebarmenu ul li { float: left; height: 1%; }
* html .sidebarmenu ul li a { height: 1%; }
/* End */

</style>

<script type="text/javascript">

//Nested Side Bar Menu (Mar 20th, 09)

var menuids=["sidebarmenu1"] //Enter id(s) of each Side Bar Menu's main UL, separated by commas

function initsidebarmenu(){
for (var i=0; i<menuids.length; i++){
  var ultags=document.getElementById(menuids[i]).getElementsByTagName("ul")
    for (var t=0; t<ultags.length; t++){
    ultags[t].parentNode.getElementsByTagName("a")[0].className+=" subfolderstyle"
  if (ultags[t].parentNode.parentNode.id==menuids[i]) //if this is a first level submenu
   ultags[t].style.top=ultags[t].parentNode.offsetHeight+"px" //dynamically position first level submenus to be width of main menu item
  else //else if this is a sub level submenu (ul)
    ultags[t].style.left=ultags[t-1].getElementsByTagName("a")[0].offsetWidth+"px" //position menu to the right of menu item that activated it
    ultags[t].parentNode.onmouseover=function(){
    this.getElementsByTagName("ul")[0].style.display="block"
    }
    ultags[t].parentNode.onmouseout=function(){
    this.getElementsByTagName("ul")[0].style.display="none"
    }
    }
  for (var t=ultags.length-1; t>-1; t--){ //loop through all sub menus again, and use "display:none" to hide menus (to prevent possible page scrollbars
  ultags[t].style.visibility="visible"
  ultags[t].style.display="none"
  }
  }
}

if (window.addEventListener)
window.addEventListener("load", initsidebarmenu, false)
else if (window.attachEvent)
window.attachEvent("onload", initsidebarmenu)


function popTopsection() {
    var top = document.getElementById("topsection");
    if (top.style.visibility == "" || top.style.visibility == "visible") {
        top.style.visibility = "hidden";
        top.style.display = "none";
    } else {
        top.style.visibility = "visible";
        top.style.display = "block";
    }
}

</script>





<div style="height:10px;"></div>
<div class="sidebarmenu">
<ul id="sidebarmenu1">
<li><A href="/WEBA/s/home.do?inputVO.mode=blank" class="sublink">Home</A></li>
<li><A href="#" class="sublink">Search</A>
<ul>
    <li><A href="/WEBA/s/scan_basicscan.do?inputVO.mode=blank">Search Home</A></li>
  <li><a href="/WEBA/s/scan_report.do?inputVO.mode=report">Current View Reports</a></li>
  <li><a href="/WEBA/s/scan_report2.do?inputVO.mode=report2">Historic View Reports</a></li>
  <li><a href="/WEBA/s/scan_report2.do?inputVO.mode=report2">Historic View Reports</a></li>
  <li><a href="/WEBA/s/agesrch_agesrchbasic.do?inputVO.mode=search&inputVO.selectRange=">Stock Age Search</a></li>
  </ul>
</li>

<li><A href="#" class="sublink">Analysis</A>
<ul>
  <li><a href="/WEBA/s/analyze_basicanalyze.do?inputVO.mode=blank&inputVO.pageStyle=S">Basic Chart Analysis</a></li>
  <li><a href="/WEBA/s/analyze_dynamicanalyze.do?inputVO.mode=blank&inputVO.pageStyle=D">Dynamic Chart Analysis</a></li>
  <li><a href="/WEBA/s/analyze_basiccalculator.do?inputVO.mode=basiccalculator">Risk Calculator</a></li>
  </ul>
</li>

<li><A href="/WEBA/k/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MB&inputVO.period=24" class="sublink">Market</A></li>

<li><A href="/WEBA/f/forum.do?inputVO.mode=q&inputVO.subjectId=0" class="sublink">Forum</A></li>
<li><A href="#" class="sublink">Member</A>
<ul>
  <li><A href="/WEBA/m/home.do?inputVO.mode=blank">Member Home</A></li>
  <li><a href="/WEBA/m/myinfo.do?inputVO.mode=blank&inputVO.marketType=none">My Profile</a></li>
  <li><a href="/WEBA/m/watch.do?inputVO.mode=alertlist&inputVO.marketType=blank">My Alert List</a>
    
  </li>
  <li><a href="/WEBA/m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=S&inputVO.currLink=1">My Messages</a></li>
</ul>
</li>
<li><A href="/WEBA/e/exam.do?inputVO.mode=blank" class="sublink">Exam</A></li>
<li><A href="/WEBA/h/help.do?inputVO.mode=blank" class="sublink">Help</A></li>


 

        <li><A href="/WEBA/d/stockadmin.do?inputVO.mode=blank" class="sublink">Admin</A></li>


<li><A class="sublink" href="/WEBA/p/signoff.do">Logout</A></li>

</ul>
</div>





        </div>
      </nav>
    </header>
    <div id="site_content">
      <div id="leftside_container">
        <div id="subtitle">
			<span style="FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; COLOR:#006699; FONT-SIZE:22pt; FONT-WEIGHT:bold; display:table; margin:auto;">Basic Scan</span>
      	</div>
      	
      	<div id="leftsidebar">
      		<div class="leftsidewrapper" style="border: 1px solid #003355;">
<span  style="display:table; margin:auto; padding:5px;">

Some links for different scan's
</span>
</div>
      		<div class="leftsidewrapper" style="border: 1px solid #003355;">
<span  style="display:table; margin:auto; padding:5px;">
Some menu links ...

<a href="../s/scan.do?inputVO.mode=report" class="sublink">Current Reports</a><br/>
                <a href="../s/scan.do?inputVO.mode=report2" class="sublink">Historic Reports</a><br/>
                <a href="../s/agesrch.do?inputVO.mode=search&inputVO.selectRange=" class="sublink">Age Search</a>
</span>
</div>
      		
      		
      		
      	</div>
        <div id="content">
        	



<script type="text/javascript" >
// Init section 
	window.onerror = function() { return true; }
	// window.onload = function(e) { if (document.getElementById && document.createElement) tooltip.define(); }

	//run_after_body();
</script>
<style type="text/css">
.border_b{
   border: 1px solid #000000;
}

.big_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; 
	FONT-SIZE: 9pt;
	COLOR: #0000cf; 
	TEXT-DECORATION: none; 
	WIDTH: 130px;
}

.sml_button {
	FONT-FAMILY: Verdana, Arial, Helvetica, sans-serif; 
	FONT-SIZE: 9pt; 
	COLOR: #0000cf; 
	TEXT-DECORATION: none; 
	WIDTH: 80px;
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







<div id="overlay_cloak">
    <!--[if IE 6]><iframe src="../base/empty.html" class="cloak_iframe"></iframe><![endif]-->
</div>
<div id="overlay_content"><img id="img_200" class="border_b" width="680" height="400"></div>

<div class="contentwrapper"><div style="margin:2px;">               
                <form name="scanForm" action="../s/scan_basicscan.do"> 
		<input type="hidden" name="inputVO.mode" id="inputVO.mode" value="basic"/>		
		<input type="hidden" name="inputVO.type" id="inputVO.type" value="show"/>
		<input type="hidden" name="inputVO.sortColumn" id="inputVO.sortColumn" value=""/>
		<input type="hidden" name="inputVO.sortOrder" id="inputVO.sortOrder" value=""/>
		<input type="hidden" name="inputVO.scanKey" id="inputVO.scanKey" value=""/>
		<input type="hidden" name="inputVO.scanDate" id="inputVO.scanDate" value=""/>
                <input type="hidden" name="inputVO.selectType" id="inputVO.selectType" value=""/>
                <input type="hidden" name="inputVO.period" id="inputVO.period" value="0"/>
		<table border="1" width="100%" class="light_back">
			
			 
			 
			 
			 	
			 
			 
					
			<tr>
				<td colspan="8">&nbsp;&nbsp;<b>Scan Mode: </b>&nbsp;&nbsp;<select id="dropdown" name="dropdown" onchange="submitRequestDrop()" class="dropdown"><option></option>
					
					
						
						
							<option value="CU" selected>Cross-Over 20/50 Mode, Up Trend</option>
						
					
						
						
							<option value="XU" selected>Cross-Over 20/100 Mode, Up Trend</option>
						
					
						
						
							<option value="MU" selected>Momentum Mode, Up Trend</option>
						
					
						
						
							<option value="NU" selected>New High Mode, Up Trend</option>
						
					
						
						
							<option value="PU" selected>Progress Mode, Up Trend</option>
						
					
						
						
							<option value="RU" selected>Reverse Mode, Up Trend</option>
						
					
						
						
							<option value="LU" selected>Linear Mode, Up Trend</option>
						
					
						
						
							<option value="ZU" selected>Zone Mode, Up Trend</option>
						
					
						
						
							<option value="WU" selected>Wild Horse Mode, Up Trend</option>
						
					
						
						
							<option value="CD" selected>Cross-Over 20/50 Mode, Down Trend</option>
						
					
						
						
							<option value="XD" selected>Cross-Over 20/100 Mode, Down Trend</option>
						
					
						
						
							<option value="MD" selected>Momentum Mode, Down Trend</option>
						
					
						
						
							<option value="ND" selected>New Low Mode, Down Trend</option>
						
					
						
						
							<option value="PD" selected>Progress Mode, Down Trend</option>
						
					
						
						
							<option value="RD" selected>Reverse Mode, Down Trend</option>
						
					
						
						
							<option value="LD" selected>Linear Mode, Down Trend</option>
						
					
						
						
							<option value="ZD" selected>Zone Mode, Down Trend</option>
						
					
			
				</select>
				</td>	
			</tr> 
			

	
	</table>
		 </form>



	
        <table>
	<tr><td colspan="2">&nbsp;<font color="#ffffef"> Please notice our suggestion for current market:</font> <b><font color="#ff0000">Sell short - market is going down.</font></b>
	<br>&nbsp; <font color="#ffffef">So only look for proper group of search results to get the best performance.</font>
		</td></tr>

	
	</table>




            <form name="goStockAgeForm" action="../s/agesrch_agesrchbasic.do">
                <input type="hidden" name="inputVO.mode" value="search"/>
                <input type="hidden" name="inputVO.selectRange" value=""/>
            </form>
              
</div>

			<div class="contentwrapper"><div style="margin:2px;">
<h3>About scan ...</h3>
</div>
		    
		    
		    
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
