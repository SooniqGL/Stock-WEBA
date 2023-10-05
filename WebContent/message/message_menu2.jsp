<%@ taglib prefix="s" uri="/struts-tags" %>

<SCRIPT SRC="/WEBA/message/message_helper.js" type="text/javascript"></SCRIPT>
	
<s:set var="groupList" value="inputVO.myGroupList" />

<s:set var="publicGroupList" value="inputVO.publicGroupList" />

<div class="leftsidewrapper">
<span  style="display:table; margin-left:20px; padding:5px; font-size:18px;">
<%-- This JSP is used combining with groupuser_list.jsp; it works for all the users who wants to
see the group information about the groups he/she has joined. --%>
<h3>Private List:</h3>

	<s:iterator var="itemVO" value="#groupList" status="rowStatus">
		<A href="#" onclick="QQ_groupuserlist.getGroupUserList('<s:property value="%{#itemVO.groupId}"/>');" class="datalink"><s:property value='%{#itemVO.groupName}'  escapeHtml="false"/>
		(<s:property value='%{#itemVO.msgCount}'/>/<s:property value='%{#itemVO.totalCount}'/>)
		</a>
		
		&nbsp; <a href="/WEBA/g/postmessage_newmessage.do?inputVO.mode=newmessage&inputVO.type=blank&inputVO.toGroupList=<s:property value='%{#itemVO.groupId}'/>" class="datalink2">New</a>
 
		<br/>
	</s:iterator>
	<s:if test="%{#groupList == NULL || #groupList.isEmpty()}">
		Currently you have not joined any private groups. <br/>
    </s:if>
    
    <h3>Public List:</h3>
    <s:iterator var="itemVO" value="#publicGroupList" status="rowStatus">
		<A href="#" onclick="QQ_groupuserlist.getGroupUserList('<s:property value="%{#itemVO.groupId}"/>');" class="datalink"><s:property value='%{#itemVO.groupName}'  escapeHtml="false"/>
		(<s:property value='%{#itemVO.msgCount}'/>)
		</a>
		
		&nbsp; <a href="/WEBA/g/postmessage_newmessage.do?inputVO.mode=newmessage&inputVO.type=blank&inputVO.toGroupList=<s:property value='%{#itemVO.groupId}'/>" class="datalink2">New</a>
 
		<br/>
	</s:iterator>
    
	    
</span></div>