<%@ taglib prefix="s" uri="/struts-tags" %>

<SCRIPT SRC="/WEBA/group/groupuserlist_helper.js" type="text/javascript"></SCRIPT>
	
<s:set var="userGroupList" value="inputVO.userGroupList" />

<div class="leftsidewrapper">
<span  style="display:table; margin-left:20px; padding:5px; font-size:18px;">
<%-- This JSP is used combining with groupuser_list.jsp; it works for all the users who wants to
see the group information about the groups he/she has joined. --%>
<h3>My Group List:</h3>

	<s:iterator var="itemVO" value="#userGroupList" status="rowStatus">
		<A href="#" onclick="QQ_groupuserlist.getGroupUserList('<s:property value="%{#itemVO.groupId}"/>');" class="datalink"><s:property value='%{#itemVO.groupName}'  escapeHtml="false"/></a>
		<s:if test="%{inputVO.userId == #itemVO.ownerId}">
			(Owner)
        </s:if> <br/>
	</s:iterator>
	<s:if test="%{#userGroupList == NULL || #userGroupList.isEmpty()}">
		Currently you have not joined any groups.
    </s:if>
			    
</span></div>