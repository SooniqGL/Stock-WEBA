<%@ taglib prefix="s" uri="/struts-tags" %>

<SCRIPT SRC="/WEBA/message/newmsg_helper.js"></SCRIPT>

<%-- div id="overlaypage" title="Choose Group.  Click outside of Dialog or ESC to close."></div>--%>

<style>
.label {
	color:#0000ff;
}

.filefield {
	background:#dfdfdf;
}
</style>

<div class="contentwrapper" style="min-height:200px;"><div style="margin:2px;padding:10px;"> 
		
      <form name="messageForm" action="/WEBA/g/postmessage_newmessage.do" method="post" enctype="multipart/form-data"> 
		<input type="hidden" id="inputVO.mode" name="inputVO.mode" value="newmessage"/>		
		<input type="hidden" id="inputVO.type" name="inputVO.type" value="update"/>
		<input type="hidden" id="inputVO.toGroupList" name="inputVO.toGroupList" value="<s:property value='%{inputVO.toGroupList}'/>"/>
		
		<font class="sub_title2">New Message</font>
		
		You are posting message to: <font class="sub_title"><s:property value='%{inputVO.toGroupNames}' escapeHtml="false" /></font><br/>
		<label for="inputVO.msgSubject">*Subject</label>
		<input name="inputVO.msgSubject" id="inputVO.msgSubject" size="20" maxlength="50" class="form-control" value="<s:property value='%{inputVO.msgSubject}'/>"></input>		
								
								
		<label for="inputVO.msgContent">*Content</label>						
		<TEXTAREA id="inputVO.msgContent" name="inputVO.msgContent"  class="form-control"  rows="10" cols="50"></TEXTAREA>

		You may upload maximum of 3 files with your message -
		<div style="color:#ff0000">
			<s:actionerror/>
			<s:fielderror/>
		</div>
		<div id="fileNode">

			<label for="file" class="label">File 1:</label>
			<input type="file" name="file" size="100" value="" class="form-control filefield" id="file"/>
			<label for="file" class="label">File 2:</label>
			<input type="file" name="file" size="100" value="" class="form-control filefield" id="file"/>
			<label for="file" class="label">File 3:</label>
			<input type="file" name="file" size="100" value="" class="form-control filefield" id="file"/>

		</div>
		<br/>
        <input type="button" name="sb" value=" Create Message " onClick="Q.message.submitRequest()" class="btn btn-primary">
    
		
		
	   </form>
	   
	   
		
</div>
</div>