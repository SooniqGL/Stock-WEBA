<%@ taglib prefix="s" uri="/struts-tags" %>

<SCRIPT SRC="/WEBA/group/grouplist_helper.js" type="text/javascript"></SCRIPT>

<script type="text/javascript">

$( document ).ready(function() {

	// this is ugly, as the dialog is running different in IE
	var ua = window.navigator.userAgent;
	var msie = ua.indexOf('MSIE ');
    var trident = ua.indexOf('Trident/');

    if (msie > 0 || trident > 0) {
		QQ_pageoverlay.init("overlaypage", 700, 450);
    } else {
    	QQ_pageoverlay.init("overlaypage", 700, 500);
    }
	
    Q.grouplist.loadGroupList();
});

</script>
	
<s:set var="groupPageList" value="inputVO.groupPageList" />

<div id="overlaypage" title="Manage group list.  Click outside of Dialog or ESC to close."></div>

<div class="contentwrapper" style="min-height:30px;"><div style="margin:2px;">
            
            <table width="100%" class="gridtable">
			
                <tr><td><font color="#000000"><b>All Groups:</b></font></td>
                <td>
                <s:iterator var="itemVO" value="#groupPageList" status="rowStatus">
                	<a href="#" onclick="Q.grouplist.loadGroupList('<s:property value="%{#itemVO.selectRange}"/>')" class="datalink"><s:property value='%{#itemVO.selectRange}'/></a>
				</s:iterator>
                
                </td></tr>
                
            </table>
            
            <table id="displayNode" style="width:100%;" class="gridtable">
		
			</table>
                
          </div>
       </div>
            
<jsp:include page="../template/group/grouplisthd_template.jsr"></jsp:include>
<jsp:include page="../template/group/grouplist_template.jsr"></jsp:include>
