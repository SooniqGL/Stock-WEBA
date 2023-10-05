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
<li><A href="/WEBA/p/signoff.do" class="sublink">Logout</A></li>

</ul>

</div>




