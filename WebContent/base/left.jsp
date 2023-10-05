<script type="text/javascript">
<!--
	function handleSignout() {
		top.window.location.href="../b/signoff.do"
	}
	
	function handleHelp() {
		helpWindow = window.open("../help/help_home.jsp", 'Helps', 'width=790,height=600,scrollbars=1,resizable=1');
		helpWindow.focus();
		return helpWindow;
	}	
	
//-->
</script>

<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />
<table width="120"  border="0" cellpadding="0" cellspacing="0">
	<tr><td colspan="2" height="10">&nbsp;&nbsp;</td></tr>
	<tr>
		<td align="center" height="50" colspan="2"><h1><font color="#ffff80">Sooniq Stock Systems</font></h1></td>
	</tr>
	
	<tr>
		<td align="center" colspan="2"><font color="#fffff0"><b><bean:write name="security" property="fname"/> <bean:write name="security" property="lname"/></b></font></td>
	</tr>
	<tr><td colspan="2" height="20">&nbsp;&nbsp;</td></tr>
	<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../b/home.do?inputVO.mode=blank" class="sublink">Home</A></td></tr>
		
	<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../s/scan.do?inputVO.mode=blank" class="sublink">Search</A></td></tr>
	<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../a/analyze.do?inputVO.mode=blank&inputVO.pageStyle=S" class="sublink">Analysis</A></td></tr>
	<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../k/market.do?inputVO.mode=blank&inputVO.marketType=N&inputVO.chartType=MB&inputVO.period=24" class="sublink">Market</A></td></tr>
		
	<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../f/forum.do?inputVO.mode=q&inputVO.subjectId=0" class="sublink">Forum</A></td></tr>
	
	<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../m/home.do?inputVO.mode=blank" class="sublink">Member</A></td></tr>
        <tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../e/exam.do?inputVO.mode=blank" class="sublink">Exam</A></td></tr>
        <%--
	<tr><td width="35" height="30">&nbsp;</td><td><A href="../a/analyze.do?inputVO.mode=basiccalculator" class="sublink">Calculator</A></td></tr>
	--%>
        <tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../h/help.do?inputVO.mode=blank" class="sublink">Help</A></td></tr>

		<logic:present name="security" scope="session">
			<logic:equal name="security" property="typeCd" value="ADM">
			<tr><td width="35" height="30">&nbsp;</td><td align="left"><A href="../d/stockadmin.do?inputVO.mode=blank" class="sublink">Admin</A></td></tr>
			</logic:equal>
		</logic:present>
		<tr><td width="35" height="30">&nbsp;</td><td align="left"><A class="sublink" href="../b/signoff.do">Logout</A></td></tr>
</table>
