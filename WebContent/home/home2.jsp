<div class="contentwrapper3">
<span style="padding:10px"><br>
 &nbsp;&nbsp;<font color="#ffff55" size="5"><b>Welcome To Use Our Services!</b></font>
 <br><br>
    
	
		 <%--
		<table width="100%">
			<tr><td height="25" align="center"><h2><A href="../s/scan.do?inputVO.mode=blank" class="toplink">Screen Market</A></h2></td></tr>
			<tr><td height="25" align="center"><h2><A href="../a/analyze.do?inputVO.mode=blank" class="toplink">Analyze Stocks</A></h2></td></tr>
			
			<tr><td height="25" align="center"><h2><A href="../p/perform.do?inputVO.mode=blank" class="toplink">Our Performances</A></h2></td></tr>
			
			<tr><td height="25" align="center"><h2><A href="../m/watch.do?inputVO.mode=blank" class="toplink">Member</A></h2></td></tr>
			<logic:present name="security" scope="session">
				<logic:equal name="security" property="typeCd" value="ADM">
					<tr><td height="25" align="center"><h2><A href="../d/admin.do?inputVO.mode=blank" class="toplink">Admin Page</A></h2></td></tr>
				</logic:equal>
			</logic:present>               
			</table> --%> 
		
		<table width="100%" height="50" border="0">
			<tr>
				<td align="center"><A href="../s/analyze_basicanalyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.period=6&inputVO.stockId=MKT001" class="sublink">Nasdaq Index</A></td>
				<td align="center"><A href="../s/analyze_basicanalyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.period=6&inputVO.stockId=MKT002" class="sublink">Dow Index</A></td>
				<td align="center"><A href="../s/analyze_basicanalyze.do?inputVO.mode=analyze&inputVO.pageStyle=S&inputVO.option=&inputVO.type=id&inputVO.period=6&inputVO.stockId=MKT003" class="sublink">SP500 Index</A></td>
			</tr>
			<tr>
				<td align="center"><img src="../drawchart?stockId=MKT001&period=3&option=NNN&type=small"></td>
				<td align="center"><img src="../drawchart?stockId=MKT002&period=3&option=NNN&type=small"></td>
				<td align="center"><img src="../drawchart?stockId=MKT003&period=3&option=NNN&type=small"></td>
			</tr> 
			
			<%
				String marketSkill = com.greenfield.ui.cache.MarketCachePool.getMarketIndicators().getMarketSkill();
			%>
			<tr><td height="5" colspan="3" align="left"><br>
			<ul>
			<li><font color="#ffffef">The key is to stay on the right side of the market.</font></li>
			<li><font color="#ffffef">Our suggestion for current market: </font><b><font color="#ff0000"><%= marketSkill %>.</font></b></li>
			</ul></td></tr> 
	
		</table>
	</span>
	</div>