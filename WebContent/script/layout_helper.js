var QQ_layout = ( function( window, undefined ) {
	
	var currentDivIndex = 0;
	var totalLength = 0;
	var divList = {};
	var W = window.innerWidth;

	
	function showCurrentDiv() {
		$("#" + divList[currentDivIndex]).show();
	}
	
	// screen move from right to left
	function showNextDiv(){
		//alert("show next: " + currentDivIndex);
	    if ( currentDivIndex + 1 > (totalLength - 1)){  
	    	alert("no more");
	        
	    } else {
	    	var mv = "-=" + W + "px";
	    	var nextObj = $("#" + divList[currentDivIndex + 1]);
	    	var currObj = $("#" + divList[currentDivIndex]);
	    	nextObj.show();
	    	currObj.animate({
	    	     left: mv
	    	   }, 250); 
	    	 
	    	 nextObj.animate({
	    	     left: '0'
	    	   }, 250); 
	    	 
	    	 currObj.hide(500);
	        currentDivIndex = currentDivIndex + 1;
	        
	        
	        // adjust the footer position
	        var newTop = nextObj.offset().top + nextObj.height();
	        var footerObj = $("footer");
	        
	        footerObj.parent().css({position: 'absolute'});
	        footerObj.css("position", "absolute");
	        footerObj.css("top", newTop);
	    }
	
	}
	
	// screen move from left to right
	function showPrevDiv(){
		//alert("show prev: " + currentDivIndex);
	    if ( currentDivIndex <= 0){    
	        alert("no more");
	    } else {
	    	var mv = "+=" + W + "px";
	    	var prevObj = $("#" + divList[currentDivIndex - 1]);
	    	var currObj = $("#" + divList[currentDivIndex]);
	    	
	    	prevObj.show();
	    	
	    	currObj.animate({
	    	     left: mv
	    	   }, 250); 
	    	 
	    	prevObj.animate({
	    	     left: '0'
	    	   }, 250); 
	    	 
	    	 currObj.hide(500);
	        currentDivIndex = currentDivIndex - 1;
	        
	     // adjust the footer position
        var newTop = prevObj.offset().top + prevObj.height();
        alert("top: " + prevObj.offset().top);
        alert("top 2: " + prevObj.height());
        
        var footerObj = $("footer");
        
        footerObj.parent().css({position: 'absolute'});
        footerObj.css("position", "absolute");
        footerObj.css("top", newTop);
	        
	    }
	
	}
	
	function hasNext() {
		if (currentDivIndex >= (totalLength - 1)) {
			return false;
		} else {
			return true;
		}
	}
	
	function hasPrev() {
		if (currentDivIndex > 0) {
			return true;
		} else {
			return false;
		}
	}
	
	function fullHeight(divId) {
	    if (divId) {
	        //$("#" + divId).innerHeight(window.innerHeight);
	        //IE7 fix
	        $("#" + divId).innerHeight($(window).innerHeight());
	    }
	}
	
	function init(divListStr) {
		// adjust the parents to 100% - 
		// divparent convers all the tags that are the parents, with bootstrap classes such as row/columns
		$(".divparent").css("width", "100%");
		
		// the divList is defined in tiles, so different from page to page
		// the first one will be shown
		divList = divListStr.split(",");
		totalLength = divList.length;
		currentDivIndex = 0;
		
		// hide all
		for (var i = 0; i < totalLength; i ++) {
			var obj = $("#" + divList[i]);
			
			obj.parent().css({position: 'absolute'});
			obj.css("position", "absolute");
			obj.css("width", "100%");
			
			
			//alert("width: " + window.innerWidth);
			
			if (i < currentDivIndex) {
				obj.css({
					position: "absolute",
					"left": - window.innerWidth});
				
			
			} else if (i > currentDivIndex) {
				obj.css({
					position: "absolute",
					"left": window.innerWidth});
			} else {
				obj.css({
					position: "absolute",
					"left": 0});
			}
			
			
			obj.hide();
		}
		
		// show the current
		$("#" + divList[currentDivIndex]).show();
	}
	
	
	return {
		init : init,
		showPrevDiv : showPrevDiv,
		showNextDiv   : showNextDiv,
		showCurrentDiv : showCurrentDiv,
		hasNext : hasNext,
		hasPrev : hasPrev
	};

} )( window );

/* end of script */
