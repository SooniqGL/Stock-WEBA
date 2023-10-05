<%@ taglib prefix="s" uri="/struts-tags" %>

	
<s:set var="folderList" value="inputVO.folderList" />
<s:set var="portfolioList" value="inputVO.portfolioList" />


<div class="contentwrapper" style="min-height:250px;"><div style="margin:2px;">
                    
            <table width="100%" class="gridtable" >
		<tr><td width="200">&nbsp;&nbsp;<b>Watch Lists</b></td>
			<td><table width="100%" class="gridtable_noborder">
				<tr><td colspan="2"><a href="../m/watch_newfolder.do?inputVO.mode=newfolder&inputVO.type=blank&inputVO.folderId=" class="toplink">Create New Folder</a></td></tr>
				
				<s:iterator var="itemVO" value="#folderList" status="rowStatus">
				<tr><td>
					<a href="../m/watch_viewwatchlist.do?inputVO.mode=viewwatchlist&inputVO.folderId=<s:property value='%{#itemVO.folderId}'/>" class="datalink"><s:property value='%{#itemVO.folderName}'/></a>
					</td>
					<td>
					<a href="../m/watch_updatefolder.do?inputVO.mode=updatefolder&inputVO.type=blank&inputVO.folderId=<s:property value='%{#itemVO.folderId}'/>" class="datalink">Update Folder</a>
					</td>
				</tr>
				</s:iterator>
				</table>
		</td></tr>
                
                <tr><td width="200">&nbsp;&nbsp;<b>Portfolio Lists</b></td>
			<td><table width="100%" class="gridtable_noborder">
				<tr><td colspan="5"><a href="../m/portfolio_newportfolio.do?inputVO.mode=newportfolio&inputVO.type=blank&inputVO.portfolioId=" class="toplink">Create New Portfolio</a></td></tr>
				

				<s:iterator var="itemVO" value="#portfolioList" status="rowStatus">
				<tr>
                     <td>
                         <b><s:property value='%{#itemVO.portfolioName}'/></b>
				    </td>
                    <td>
					<a href="../m/portfolio_viewpositions.do?inputVO.mode=viewpositions&inputVO.displayChart=N&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink">Open Positions</a>
					</td>
					<td>
					<a href="../m/portfolio_updateportfolio.do?inputVO.mode=updateportfolio&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink">Update</a>
					</td>
                    <td>
					<a href="../m/portfolio_viewtransactions.do?inputVO.mode=viewtransactions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink">Transactions</a>
					</td>
                    <td>
					<a href="../m/portfolio_viewclosedpositions.do?inputVO.mode=viewclosedpositions&inputVO.type=blank&inputVO.portfolioId=<s:property value='%{#itemVO.portfolioId}'/>" class="datalink">Closed Positions</a>
					</td>
				</tr>
				</s:iterator>
				</table>
		</td></tr>
		<tr><td>&nbsp;&nbsp;<a href="../m/myinfo.do?inputVO.mode=blank&inputVO.type=none" class="toplink">Member Profile</a></td>
			<td>View and edit your profile</td>
			</tr>
		<tr><td>&nbsp;&nbsp;<a href="../m/watch.do?inputVO.mode=alertlist&inputVO.type=blank" class="toplink">Alert List</a></td>
			<td>The stocks in your watch lists that have either changed the trend signal or overbought or oversold.
			<br><br>
			Note: We have a program to check everybody's watch list each business day.  If we have
			found some of the stocks in the watch list have changed signal in the current day, we will
			add tickers to the Alert List.<br>
			The only thing you need to do is to set "Receive Email" field to "Yes" in
			your profile.  Make sure the email address is right as well.  We will
			also send email to you when we turn on our email services.
			</td>
			</tr>
			
			<!-- This will be only available for ADM user -->
			<s:set var="webUser" value="user"/> 
			<s:if test="%{#webUser != null && #webUser.typeCd == \"ADM\"}">
			<tr><td>Admin Links (Admin Only)</td><td>
			        <A href="/WEBA/mgr?class=c" class="datalink">Reset Cache</A>
			       </td></tr>
			</s:if>
			
			
			
		</table>
                
                </div>
            </div>
            
