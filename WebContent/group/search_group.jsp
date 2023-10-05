<%@ taglib prefix="s" uri="/struts-tags" %>

<SCRIPT SRC="/WEBA/group/searchgroup_helper.js" type="text/javascript"></SCRIPT>

<script type="text/javascript">

$( document ).ready(function() {

	// this is ugly, as the dialog is running different in IE
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
    var trident = ua.indexOf('Trident/');

    if (msie > 0 || trident > 0) {
		QQ_pageoverlay.init("overlaypage", 700, 320);
    } else {
    	QQ_pageoverlay.init("overlaypage", 700, 320);
    }
	
    // set focus
    QQ_searchgroup.setFocus();
    
    /* following needs to be called when "ready" - submit the search when return is hit! */
    $('input#keywords').keypress(function(e) {
  	  if (e.which == '13') {
  	     e.preventDefault();
  	     // submit the search
  	     QQ_searchgroup.searchGroupListFirst();
  	   }
  	});
});




</script>
	
<div id="overlaypage" title="Manage group list.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper" style="min-height:30px;"><div style="margin:2px;">
            
            <table width="100%" class="gridtable">
			
                <tr><th colspan="2"><font color="#000000"><b>All Groups:</b></font> &nbsp;
                <input type="text" id="keywords" maxlength="100"> &nbsp;
                <input type="button" value="Search" onclick="QQ_searchgroup.searchGroupListFirst()" class="long_button"></th></tr>
                
            </table>
            <div id="displayNode" style="width:100%;">
			</div>
			
            <table id="displayNode2" style="width:100%;" class="gridtable">
			</table>
                
          </div>
       </div>
            
<jsp:include page="../template/group/grouplisthd_template.jsr"></jsp:include>
<jsp:include page="../template/group/grouplist_template.jsr"></jsp:include>
<jsp:include page="../template/group/usergroup_template.jsr"></jsp:include>

