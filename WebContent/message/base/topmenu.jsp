<div class="container" >
    <div class="input-group" style="margin-top:10px;">
      <input type="text" class="form-control" placeHolder="Enter text to search message">
      <span class="input-group-btn">
        <button class="btn btn-default" type="button">Search!</button>
      </span>
    </div>
</div> 

<%-- 
<style>
<!--

.has-form-field {
	padding-left: 10px;
}

-->
</style>


<script type="text/javascript">

$( document ).ready(function() {

	$('.dropdown-menu').on('click', function(e) {
	    if($(this).hasClass('has-form-field')) {
	        e.stopPropagation();
	    }
	});
});

</script>
<div class="container" > 
	<div class="input-group" style="margin-top:10px;">
	      <input type="text" class="form-control" placeHolder="Enter text to search message">
	      <div class="input-group-btn">
	        <button type="button" class="btn btn-default">Search!</button>
	        <button type="button" class="btn btn-default dropdown-toggle" data-toggle="dropdown"><span class="caret"></span></button>
	        <ul class="dropdown-menu dropdown-menu-right has-form-field" role="menu">
	          <li><label class="checkbox">
                <input type="checkbox"> <a href="#">All Groups</a>
            	</label></li>
	          <li class="divider"></li>
	          <li><label class="checkbox">
                <input type="checkbox">
                Checkbox 1
            	</label></li>
	          <li><label class="checkbox">
                <input type="checkbox">
                Checkbox 2
            	</label></li>
            	<li><label class="checkbox">
                <input type="checkbox">
                Checkbox 3
            	</label></li>
	        </ul>
	      </div>
	    </div>
</div>    

--%>