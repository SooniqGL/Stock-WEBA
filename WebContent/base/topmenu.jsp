<%@ taglib prefix="s" uri="/struts-tags" %>

<%-- Since Tomcat 7, seems we need to include the taglib declaration in the cluded JSP's if it is used there. --%>

<script type="text/javascript">

	// Nested Side Bar Menu (Mar 20th, 09)
	var menuids=["sidebarmenu1"]; //Enter id(s) of each Side Bar Menu's main UL, separated by commas
	if (window.addEventListener) {
		window.addEventListener("load", function(){
    		Q.common.initsidebarmenu(menuids); }, false);
	} else if (window.attachEvent) {
		window.attachEvent("onload", function(){
    		Q.common.initsidebarmenu(menuids); });
	}
		
</script>


<!-- div style="height:5px;"></div> -->
<div class="sidebarmenu">
<ul id="sidebarmenu1">

<%--
<li><A href="/WEBA/s/home.do?inputVO.mode=blank" class="sublink">Home</A></li>

<li><A href="/WEBA/g/getmessage_messagehome.do?inputVO.mode=messagehome" class="sublink">Message</A></li>
--%>
<li><A href="#" class="sublink">Home</A>
<ul>
  <li><A href="/WEBA/m/home.do?inputVO.mode=blank">Member</A></li>
  <li><a href="/WEBA/m/profile_profile.do?inputVO.mode=blank&inputVO.marketType=none">My Profile</a></li>
  <li><A href="/WEBA/s/exam_examcontent.do?inputVO.mode=blank">My Exam</A></li>
  <li><a href="/WEBA/g/group_grouphome.do?inputVO.mode=grouphome&inputVO.type=blank">Manage Groups</a></li>
  <li><a href="/WEBA/m/watch.do?inputVO.mode=alertlist&inputVO.marketType=blank">My Alert List</a><%--ul>
    <li><a href="#">Sub Item 2.1.1</a></li>
    <li><a href="#">Sub Item 2.1.2</a></li>
    <li><a href="#">Sub Item 2.1.3</a></li>
    <li><a href="#">Sub Item 2.1.4</a></li>
    </ul--%>
  </li>
  <li><a href="/WEBA/m/mymessage.do?inputVO.mode=L&inputVO.type=blank&inputVO.messageType=S&inputVO.currLink=1">My Messages</a></li>
</ul>
</li>

<li><A href="#" class="sublink">Search</A>
<ul>
  <li><A href="/WEBA/s/scan_basicscan.do?inputVO.mode=basic">Search Home</A></li>
  <li><a href="/WEBA/s/scan_scanreport.do?inputVO.mode=report">Current View Reports</a></li>
  <li><a href="/WEBA/s/scan_scanreport2.do?inputVO.mode=report2">Historic View Reports</a></li>
  <li><a href="/WEBA/s/scan_scanchart.do?inputVO.mode=chart" class="datalink">Historic Chart Reports</a></li>
  <li><a href="/WEBA/s/agesrch_agesrchresult.do?inputVO.mode=search&inputVO.selectRange=">Stock Age Search</a></li>
  </ul>
</li>

<li><A href="#" class="sublink">Analysis</A>
<ul>
  <li><a href="/WEBA/s/analyze_basicanalyze.do?inputVO.mode=blank&inputVO.pageStyle=S">Basic Chart Analysis</a></li>
  <li><a href="/WEBA/s/analyze_dynamicanalyze.do?inputVO.mode=blank&inputVO.pageStyle=D">Dynamic Chart Analysis</a></li>
  <li><a href="/WEBA/s/analyze_basiccalculator.do?inputVO.mode=basiccalculator">Risk Calculator</a></li>
  </ul>
</li>

<li><A href="/WEBA/s/market_marketpulse.do?inputVO.mode=blank&inputVO.marketType=X&inputVO.chartType=MB&inputVO.period=24" class="sublink">Market</A></li>

<li><A href="/WEBA/h/help.do?inputVO.mode=blank" class="sublink">Help</A></li>
<%--
<li><A href="/WEBA/a/analyze.do?inputVO.mode=basiccalculator" class="sublink">Calculator</A></li>


<s:set var="webUser" value="user"/> 
<s:if test="%{#webUser != null && #webUser.typeCd == \"ADM\"}">
        <li><A href="/WEBA/d/stockadmin.do?inputVO.mode=blank" class="sublink">Admin</A></li>
</s:if>
--%>


</ul>

</div>




