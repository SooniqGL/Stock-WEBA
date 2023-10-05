<%@ taglib prefix="s" uri="/struts-tags" %>

<style type="text/css">

#topDate {
    color:#ffffef;
    font-size:14px;
}


</style>

<script>
    <%-- this is used in mobile case --%>
	function checkbuttons() {
		if (QQ_layout.hasNext() == false) {
			$('#nextpage').hide(); //attr('disabled', 'disabled');
		} else {
			$('#nextpage').show(); // attr('disabled', '');
		}
		
		if (QQ_layout.hasPrev() == false) {
			$('#prevpage').hide(); // attr('disabled', 'disabled');
		} else {
			$('#prevpage').show(); //attr('disabled', '');
		}
	}
</script>

<%
String theDate = com.greenfield.common.util.DateUtil.getDateStringInLongFormat(null);
%>

<%-- Adjust the head part, if it is mobile --%>
<s:if test="%{sessionContext.contentMap[\"ISMOBILE\"] == \"Y\"}">
	<div style="text-align:center;" class="titlearea">
        <h1><a href="#" onclick="QQ_layout.showPrevDiv();checkbuttons();" id="prevpage" class="sublink">Prev</a> Sooniq <span id="topDate"><%= theDate %></span> <a href="#" onclick="QQ_layout.showNextDiv();checkbuttons();" id="nextpage" class="sublink">Next</a></h1>
	</div>
</s:if>

<%-- restore the regular head, if it is not mobile --%>
<s:else>
	<div style="text-align:center;" class="titlearea">
	        <h1>Sooniq Stock System <span id="topDate"><%= theDate %></span></h1>
	</div>
</s:else>
				


       
