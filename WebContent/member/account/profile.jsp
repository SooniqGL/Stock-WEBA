<%@ taglib prefix="s" uri="/struts-tags" %>


<%-- s:set var="folderList" value="inputVO.folderList" />--%>

<div class="contentWrapper">
<div style="margin:0 0 0 0; width:100%; background:#ffffef;">


		<s:if test="%{inputVO.message != NULL && inputVO.message != \"\"}">
				<font color="#00ff00">Success: <s:property value='%{inputVO.message}'/></font>	
			</s:if>
 
            <table width="100%" class="gridtable">
		<tr><td height="40" align="center" colspan="4" class="tail_color"><font class="sub_title">My Profile</font>
		( <a href="/WEBA/m/profile_updatemyinfo.do?inputVO.mode=updatemyinfo" class="datalink">Update</a> )
		
		
		</td></tr>
		
		<tr><td><span class="list_text">
		Your user ID: <br/> &nbsp;&nbsp;&nbsp;&nbsp;<s:property value='%{inputVO.userId}'/></span></td>
		<td colspan="3"><span class="list_text">Your nickname and user ID will be used for other people to identify you. 
		The nickname can be changed any time.  User ID will be unique, and assigned by the system, and cannot be changed.</span>
		</td></tr>
	                                    
				  <tr> 
					<th width="20%">First Name:</th>
					<td width="30%"><s:property value='%{inputVO.fname}'/></td>
				  
					<th width="20%">Middle Name:</th>
					<td width="30%"> <s:property value='%{inputVO.mname}'/>
					  </td>
				  </tr>
				  <tr> 
					<th>Last Name:</th>
					<td> <s:property value='%{inputVO.lname}'/>
					</td>
				   
					<th>Email Address:</th>
					<td><s:property value='%{inputVO.email}'/>
					</td>
				  </tr>
				  <tr> 
					<th>Phone:</th>
					<td><s:property value='%{inputVO.phone}'/></td>
				 
					<th>Second Phone:</th>
					<td><s:property value='%{inputVO.secondPhone}'/> 
					</td>
				  </tr>
				  <tr> 
					  <th>Addr1:</th>
					  <td><s:property value='%{inputVO.addr1}'/> 
						</td>
					<th>Fax:</th>
					<td> <s:property value='%{inputVO.fax}'/>
					  </td>
				  </tr>
				  <tr> 
					<th>Addr2:</th>
					<td> <s:property value='%{inputVO.addr2}'/>
					</td>
				  
					<th>City:</th>
					<td> <s:property value='%{inputVO.city}'/>
					  </td>
				  </tr>
				  <tr> 
					<th>State:</th>
					<td> <s:property value='%{inputVO.state}'/>
					</td>
				   
					<th>Zip:</th>
					<td> <s:property value='%{inputVO.zip}'/>
					</td>
				  </tr>
				  <tr> 
					<th>Country:</th>
					<td> 
						United States
					</td>
					
					<th>Login ID:</th>
					<td><s:property value='%{inputVO.loginId}'/>
						</td>
				  </tr>
				  
				 <tr>
					<th>Receive Email:</th>
					<td><s:property value='%{inputVO.receiveEmail}'/>	
					 </td>
				  
					<th>Nickname:</th>
					<td> 
					  <s:property value='%{inputVO.nickname}'/>
					</td>
				  </tr>
	            
          			</table>
	 			    


</div></div>