// This set of script (fn_) is used by the editable select utility
// and drop down calendar ...
function fn_Show (form, layer, select_name, type)
{ 	
	// display the layer, set focus, this will be used by onblur
	// either a frame or select drop down focus is set
	document.all[layer].style.display = "block";
	if (type != null && type == "frame") {
		eval("document.frames['" + select_name + "'].focus()");
	} else {
		eval(form + "." + select_name + ".focus()");
	}
}

function fn_Hide (layer)
{
	document.all[layer].style.display = "none";
}

// hide a layer in the parent level
function fn_Hide_P (layer) 
{
 	parent.document.all[layer].style.display = "none";
}

// set the select value and hide the layer
function fn_Choose_Value (form, layer, input_name, select_name)
{
	eval(form + "." + input_name + ".value = " + form + "." + select_name + ".value");
	eval(form + "." + select_name + ".value = ''");
	fn_Hide (layer);
}


/* The End */


