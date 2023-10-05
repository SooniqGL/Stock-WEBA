<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />

<font color="#fffff0"><b><bean:write name="security" property="fname"/> <bean:write name="security" property="lname"/></b></font>

<div class="sidebarmenu">
<ul id="sidebarmenu1">
<li><A href="../b/home.do?inputVO.mode=blank" class="sublink">Home</A></li>
<li><A href="../s/scan.do?inputVO.mode=blank" class="sublink">Search</A>
<ul>
  <li><a href="#" onClick="submitReport()">Current View Reports</a></li>
  <li><a href="#" onClick="submitReport2()">Historic View Reports</a></li>
  <li><a href="#" onClick="goStockAge()">Stock Age Search</a></li>
  </ul>
</li>

<li><A href="../a/analyze.do?inputVO.mode=blank&inputVO.pageStyle=S" class="sublink">Analysis</A></li>
<li><A href="../k/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MB&inputVO.period=24" class="sublink">Market</A></li>

<li><A href="../f/forum.do?inputVO.mode=q&inputVO.subjectId=0" class="sublink">Forum</A>
  <ul>
  <li><a href="#">Sub Item 2.1</a></li>
  <li><a href="#">Folder 2.1</a>
    <ul>
    <li><a href="#">Sub Item 2.1.1</a></li>
    <li><a href="#">Sub Item 2.1.2</a></li>
    <li><a href="#">Sub Item 2.1.3</a></li>
    <li><a href="#">Sub Item 2.1.4</a></li>
    </ul>
  </li>
</ul>
</li>
<li><A href="../m/home.do?inputVO.mode=blank" class="sublink">Member</A></li>
<li><A href="../e/exam.do?inputVO.mode=blank" class="sublink">Exam</A></li>
<li><A href="../h/help.do?inputVO.mode=blank" class="sublink">Help</A></li>
<%--
<li><A href="../a/analyze.do?inputVO.mode=basiccalculator" class="sublink">Calculator</A></li>
--%>

<logic:present name="security" scope="session">
    <logic:equal name="security" property="typeCd" value="ADM">
        <li><A href="../d/stockadmin.do?inputVO.mode=blank" class="sublink">Admin</A></li>
    </logic:equal>
</logic:present>

<li><A class="sublink" href="../b/signoff.do">Logout</A></li>

</ul>
</div>

<form name="goStockAgeForm" action="../s/agesrch.do">
    <input type="hidden" name="inputVO.mode" value="search"/>
    <input type="hidden" name="inputVO.selectRange" value=""/>
</form>
<script type="text/javascript">

//Nested Side Bar Menu (Mar 20th, 09)

var menuids=["sidebarmenu1"] //Enter id(s) of each Side Bar Menu's main UL, separated by commas

function initsidebarmenu() {
    for (var i=0; i<menuids.length; i++) {
        var ultags=document.getElementById(menuids[i]).getElementsByTagName("ul")
        for (var t=0; t<ultags.length; t++) {
            ultags[t].parentNode.getElementsByTagName("a")[0].className+=" subfolderstyle"
            if (ultags[t].parentNode.parentNode.id==menuids[i]) //if this is a first level submenu
                ultags[t].style.left=ultags[t].parentNode.offsetWidth+"px" //dynamically position first level submenus to be width of main menu item
            else //else if this is a sub level submenu (ul)
                ultags[t].style.left=ultags[t-1].getElementsByTagName("a")[0].offsetWidth+"px" //position menu to the right of menu item that activated it
            
            ultags[t].parentNode.onmouseover=function() {
                this.getElementsByTagName("ul")[0].style.display="block"
            }
            ultags[t].parentNode.onmouseout=function() {
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

</script>

<style type="text/css">
/* left menu */
.sidebarmenu ul{
margin: 0;
padding: 0;
list-style-type: none;
font: bold 13px Verdana;
width: 160px; /* Main Menu Item widths */
/* border-bottom: 1px solid #ccc; */
}
 
.sidebarmenu ul li{
position: relative;
}

/* Top level menu links style */
.sidebarmenu ul li a{
display: block;
overflow: auto; /*force hasLayout in IE7 */
color: white;
text-decoration: none;
padding: 6px;
border: 1px solid #007788;
}

.sidebarmenu ul li a:link, .sidebarmenu ul li a:visited, .sidebarmenu ul li a:active{
background-color: #005588; /*background of tabs (default state)*/
}

.sidebarmenu ul li a:visited{
color: white;
}

.sidebarmenu ul li a:hover{
background-color: black;
}

/*Sub level menu items */
.sidebarmenu ul li ul{
position: absolute;
width: 150px; /*Sub Menu Items width */
top: 0;
visibility: hidden;
}

.sidebarmenu a.subfolderstyle{
background: url(../image/right.gif) no-repeat 97% 50%;
}

 
/* Holly Hack for IE \*/
* html .sidebarmenu ul li { float: left; height: 1%; }
* html .sidebarmenu ul li a { height: 1%; }

</style>