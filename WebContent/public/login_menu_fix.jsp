

<script type="text/javascript">

//Nested Side Bar Menu (Mar 20th, 09)

var menuids=["sidebarmenu1"]; //Enter id(s) of each Side Bar Menu's main UL, separated by commas

	function initsidebarmenu() {
		for (var i = 0; i < menuids.length; i++) {
			var ultags = document.getElementById(menuids[i]).getElementsByTagName("ul");
			for (var t = 0; t < ultags.length; t++) {
				ultags[t].parentNode.getElementsByTagName("a")[0].className += " subfolderstyle";
				if (ultags[t].parentNode.parentNode.id == menuids[i]) {
					ultags[t].style.top = ultags[t].parentNode.offsetHeight + "px"; 
				} else {
					ultags[t].style.left = ultags[t - 1].getElementsByTagName("a")[0].offsetWidth + "px"; 
				}
				
				ultags[t].parentNode.onmouseover = function() {
					this.getElementsByTagName("ul")[0].style.display = "block";
				};
				ultags[t].parentNode.onmouseout = function() {
					this.getElementsByTagName("ul")[0].style.display = "none";
				};
			}
			for (var t = ultags.length - 1; t > -1; t--) { 
				ultags[t].style.visibility = "visible";
				ultags[t].style.display = "none";
			}
		}
	}

	if (window.addEventListener) {
		window.addEventListener("load", initsidebarmenu, false);
	} else if (window.attachEvent) {
		window.attachEvent("onload", initsidebarmenu);
	}
</script>



<!--  div style="height:10px;"></div>-->
<div class="sidebarmenu">
<ul id="sidebarmenu1">
<%--
<li><A href="#" onclick="QQ_loginoverlay.show_div()" class="sublink2">Login</A></li>
--%>
<li><A href="../p/login_input.do?inputVO.mode=blank" class="sublink">Welcome</A></li>
<li><A href="../p/register_input.do?inputVO.mode=blank" class="sublink">Registration</A></li>

<li><A href="../p/public_contactus.do?inputVO.mode=blank" class="sublink">Contact Us</A></li>
<li><A href="../p/public_aboutus.do?inputVO.mode=blank" class="sublink">About Us</A></li>


<%--li><A href="../b/login.do?inputVO.mode=random" class="sublink">Random Walk</A></li>
<%--li><A href="../../product/index.htm" class="sublink">Medical</A>
<ul>
  <li><a href="../m/myinfo.do?inputVO.mode=blank&inputVO.marketType=none">My Profile</a></li>
  <li><a href="../m/watch.do?inputVO.mode=alertlist&inputVO.marketType=blank">My Alert List</a>
    <ul>
    <li><a href="#">Sub Item 2.1.1</a></li>
    <li><a href="#">Sub Item 2.1.2</a></li>
    <li><a href="#">Sub Item 2.1.3</a></li>
    <li><a href="#">Sub Item 2.1.4</a></li>
    </ul>
  </li>
</ul>
</li--%>

</ul>
</div>





