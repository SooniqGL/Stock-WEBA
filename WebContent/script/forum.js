// forum.js


function showHideElem(el) {	
	if (!ver4) return;    
	if (IE4) {        
		
        whichEl1 = eval(el);
       
		if (whichEl1.style.display == "" || whichEl1.style.display == "none") {            
			whichEl1.style.display = "block";       
		} else {            
			whichEl1.style.display = "none";     
		}       
	} else {        		
		whichEl = eval(el);
		if (whichEl.visibility == "" || whichEl.visibility == "hide") {            
			whichEl.visibility = "show";
		} else {            
			whichEl.visibility = "hide";
		}               
		arrange();    
	}
}

// use "var" for the local variable -
// otherwise, may miss up with other functions.
function showAll() {
	if (!ver4) return;
 
 	var state = document.forumForm.state.value;
	if (NS4) {        
		for (var i = 0; i < document.layers.length; i++) {            
			whichEl = document.layers[i];    
                        if (whichEl.id.substring(0, 1) == "A") {
                            if (state == "show") {	 
                                    whichEl.visibility = "show";      
                            } else if (  isInt(whichEl.id.substring(1)) == false ) {
                                    whichEl.visibility = "hide";
                            }
                        }
		}        
		arrange();    
	} else {          
		divColl = document.all.tags("DIV");       
		for (var i = 0; i < divColl.length; i++) {  
			whichEl = divColl(i);
                        if (whichEl.id.substring(0, 1) == "A") {
                            if (state == "show") {	
                                    whichEl.style.display = "block"; 
                            } else if (  isInt(whichEl.id.substring(1)) == false ) {
                                    whichEl.style.display = "none"; 
                            }   
                        }
		}    
	}
	
	if (state == "show") {	
		state = "hide";
	} else {
		state = "show";
	}
	
	document.forumForm.state.value = state;
}


function showChildren(prefix) {
	if (!ver4) return;
 	var toShow = "";
	var firstChild = eval(prefix + "K1");
	var prefixLen = prefix.length;
		
	if (NS4) {   
		if (firstChild.visibility == "show") {
			toShow = "no";
		} else {
			toShow = "yes";
		}
		     
		for (var i = 0; i < document.layers.length; i++) {            
			whichEl = document.layers[i];         
			if (toShow == "yes") {	 
				whichEl.visibility = "show";      
			} else {
				whichEl.visibility = "hide";
			}
		}        
		arrange();    
	} else {          
		divColl = document.all.tags("DIV");       
		if (firstChild.style.display == "block") {
			toShow = "no";
		} else {
			toShow = "yes";
		}
		
		for (var i = 0; i < divColl.length; i++) {  
			whichEl = divColl(i);
			if (whichEl.id.substring(0, prefixLen) == prefix && whichEl.id.length > prefixLen) {
				if (toShow == "yes") {	
					whichEl.style.display = "block"; 
				} else {
					whichEl.style.display = "none"; 
				}
			}                   
		}    
	}
	
}


// To expand entirely or hide them
// for all the layers that name contains keyword as the first character!
// if the field value = "Y", hide, otherwise show
// if hiding, make sure the "sureone" is show
function doAllLayerButOne(keyword, field_name, showone) {	
	if (!ver4) return; 
	var field = eval("document." + field_name);
	var visibility = "";
	var display = "";
	if (field.value == null || field.value == "S") {  // is S, so need to hide
		visibility = "hide";
		display = "none";
		field.value = "H";
	} else {
		visibility = "show";
		display = "block";
		field.value = "S";
	}
   
	if (NS4) {        
		for (i=0; i<document.layers.length; i++) {            
			whichEl = document.layers[i];            
			if (whichEl.id != showone && whichEl.id.substring(0,1) == keyword) {
				whichEl.visibility = visibility; 
			} else if (whichEl.id == showone) {
				whichEl.visibility = "show";
			}
     
		}
        
		arrange();    
	} else {          
		divColl = document.all.tags("DIV");        
		for (i=0; i<divColl.length; i++) {  
			whichEl = divColl(i);
			if (whichEl.id != showone && whichEl.id.substring(0,1) == keyword) { 
				whichEl.style.display = display; 
			} else if (whichEl.id == showone) {
				whichEl.style.display = "block";
			}
		} 
   
	}

}

// Used for netscape browsers ...
function MM_reloadPage(init) {  //reloads the window if Nav4 resized
  	if (init==true) {
		with (navigator) {
			if ((appName=="Netscape")&&(parseInt(appVersion, 10)==4)) {
    				document.MM_pgW=innerWidth; 
				document.MM_pgH=innerHeight; 
				onresize=MM_reloadPage; 
			}
		}
 	} else if (innerWidth!=document.MM_pgW || innerHeight!=document.MM_pgH) {
		location.reload();
	}
}



/* The End */





