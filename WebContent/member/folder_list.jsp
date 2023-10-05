<%@ taglib prefix="s" uri="/struts-tags" %>
<s:set var="folderList" value="inputVO.folderList" />

<div class="leftsidewrapper">
<span  style="display:table; margin-left:10px; padding:0px; ">
	<h3>My Folder List</h3>

	<s:iterator var="itemVO" value="#folderList" status="rowStatus">	
		<a href="../m/watch_viewwatchlist.do?inputVO.mode=viewwatchlist&inputVO.folderId=<s:property value='%{#itemVO.folderId}'/>" class="datalink"><s:property value='%{#itemVO.folderName}'/></a>
		| <a href="../m/watch_updatefolder.do?inputVO.mode=updatefolder&inputVO.type=blank&inputVO.folderId=<s:property value='%{#itemVO.folderId}'/>" class="datalink2">Update</a><br/>
	</s:iterator>
</span></div>