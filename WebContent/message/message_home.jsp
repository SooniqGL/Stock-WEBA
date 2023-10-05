
<%@ taglib prefix="s" uri="/struts-tags" %>

<div class="contentwrapper2" style="min-height:350px;"><div style="margin:2px;">
<div style="background:#cfcfcf;text-align:center;">
  Message from Group: AAA, BBB, CCC, DDD
</div>
<div>
This is home for message
</div>

<%-- 
	Do not use bootstrap pagination: not nice when page too narrow. if prev/next no data, do not show.
	Can easily use <a> tag to list the page links.  No need to make it complex.
 --%>
<div style="text-align:center;"> 
	   <a href="#" class="my_pagination">&lt;&lt;</a>
	   <a href="#" class="my_pagination">11</a>
	   <a href="#" class="my_pagination">12</a>
	   <a href="#" class="my_pagination">13</a>
	   <a href="#" class="my_pagination">14</a>
	   <a href="javascript:void(0)" class="my_pagination_current">15</a>
	   <a href="#" class="my_pagination">16</a>
	   <a href="#" class="my_pagination">17</a>
	   <a href="#" class="my_pagination">18</a>
	   <a href="#" class="my_pagination">19</a>
	   <a href="#" class="my_pagination">20</a>
	   <a href="#" class="my_pagination">&gt;&gt;</a>
	   
	    
</div>

</div></div>