//common.js

if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

/** used to define name space */
Q.namespace = function () {
    var a = arguments,
        o = null,
        i, j, d;
    for (i = 0; i < a.length; i = i + 1) {
        d = ("" + a[i]).split(".");
        o = Q;
        for (j = (d[0] == "Q") ? 1 : 0; j < d.length; j = j + 1) {
            o[d[j]] = o[d[j]] || {};
            o = o[d[j]];
        }
    }
    return o;
};

/** this name space in this file */
Q.namespace("common");


String.prototype.trim = function() {
	a = this.replace(/^\s+/, '');
	return a.replace(/\s+$/, '');
};

Q.common.openWindow = function(url, width, height)
{
   var str = "dependent=yes,toolbar=0,location=0,directories=0,status=1,menubar=0,scrollbars=1,resizable=1,";
   str += "width=";
   str += width;
   str += ",";
   str += "height=";
   str += height;
   

   popup = window.open(url,'SOONIQ',str);
   return popup;
};


Q.common.isInt = function(str)
{
	var validform = "1234567890";
	var bool=true;
	var len = str.length;

	if(str == null || len == 0)
	{
		return true;
	} 
	
	if (str.charAt(0) == '-') {
		str = str.substring(1);
		len = len - 1;
	}
	
	for (var i = 0; i < len; i++)
	{
		if (validform.indexOf(str.charAt(i)) == -1) {
			bool = false;
			break;
		}
	}
	return bool;	
};



Q.common.isValidDate = function( str )
{
    var month = "";
	var day = "";
	var year = "";
	var i = 0;
	var j = 0;
    var message = "Please follow yyyy/mm/dd format.\nExample: 1982/09/07";
	var good = true;

   if ( str == null || str.length == 0 ) {
	   return true;
   }
    
   if ( str.length < 5 || str.length > 10 ) {
	  good = false;
   } 
   
   
   if (good == true) {
	   i = str.indexOf("/");
	   if (i > -1) {
		   year = str.substring(0, i);
		   if (Q.common.isInt(year) == false || year.length < 2) {
				good = false;
		   }
	   } else {
		   good = false;
	   }
   }

   if (good == true) {
	   j = str.indexOf("/", i + 1);
	   if (j > -1) {
		   month = str.substring(i + 1, j);
		   if (Q.common.isInt(month) == false) {
			 good = false;
		   }
	   } else {
		   good = false;
	   }
   }

   if (good == true) {
	   day = str.substring(j + 1);
	   if (Q.common.isInt(day) == false) {
		   good = false;
	   } 
   }
   

  if (good == false) {
		alert(message);
		return false;
   } else {
      return true;
   } 
};

Q.common.isValidNumber = function(origStr)
{ 
	if (origStr == null || origStr.length == 0) {
		return true;
	}
	
	var str = origStr;
	if (origStr.substring(0, 1) == "-") {
		str = origStr.substring(1);
	}
	
	if (str.length == 0) {
		alert("Sorry, \"" + origStr + "\" is not a number.");
		return false;
	}

	var index = str.indexOf(".");
	if (index == -1) {
		if (Q.common.isInt(str) == true) {
			return true;
		} else {
			alert("Sorry, \"" + str + "\" is not a number.");
			return false;
		}
	} else if (index > -1) {
		before = str.substring(0, index);
		after = str.substring(index + 1);
		if (Q.common.isInt(before) == false || Q.common.isInt(after) == false) {
			alert("Sorry, \"" + str + "\" is not a number.");
			return false;
		}
		if (after.length > 2) {
			alert("Enter at most two digits after the decimal point, please.");
			return false;
		}
		if (before.length > 10) {
			alert("The number is too big. Try again.");
			return false;
		}
	}
	
	return true;
};


Q.common.isValidLoginId = function( str )
{
	if (str == null || str.length < 4 || str.length > 16) {
		alert("User Id has to be 4 to 16 characters");
		return false;
	}

	if (str.indexOf(" ") > -1) {
		alert("No space is allowed in the login id.");
		return false;
	}

	return true;
};

Q.common.isValidPassword = function(str)
{
	if (str == null || str.length < 5 || str.length > 16) {
		alert("Password has to be 5 to 16 characters");
		return false;
	}
    
	return true;
};




/** Used to save or pass string from page to pop up
 *  Or the string in XML text
 * 
 * % -> %%
 * < -> %1
 * > -> %2
 * # -> %3
 * | -> %4
 * : -> %5
 * ; -> %6
 * " -> %7
 */

Q.common.encodeString = function(text) {
	if (text == null || text == "") {
		return "";
	}

	var reg1 = new RegExp("\r", "g");
	text = text.replace(reg1, "");
	reg1 = new RegExp("%", "g");
	text= text.replace(reg1, "%%");
	reg1 = new RegExp("<", "g");
	text = text.replace(reg1, "%1");
	reg1 = new RegExp(">", "g");
	text = text.replace(reg1, "%2");
	reg1 = new RegExp("#", "g");
	text = text.replace(reg1, "%3");
	reg1 = new RegExp("\\|", "g");
	text = text.replace(reg1, "%4");
	reg1 = new RegExp(":", "g");
	text = text.replace(reg1, "%5");
	reg1 = new RegExp(";", "g");
	text = text.replace(reg1, "%6");
	reg1 = new RegExp("\"", "g");
	text = text.replace(reg1, "%7");
	reg1 = new RegExp("@", "g");
	text = text.replace(reg1, "%8");
	
	return text;
};

Q.common.decodeString = function(text) {
	if (text == null || text == "") {
		return "";
	}

	var tmp = "";
	var t_1 = "";
	var t_2 = "";
	while (text.length > 0) {
		if (text.length == 1) {
			tmp += text;
			text = "";
		} else {
			t_1 = text.substring(0, 1);
			t_2 = text.substring(0, 2);
			if (t_2 == "%%") {
				tmp += "%";
				text = text.substring(2);
			} else if (t_2 == "%1") {
				tmp += "<";
				text = text.substring(2);
			} else if (t_2 == "%2") {
				tmp += ">";
				text = text.substring(2);
			} else if (t_2 == "%3") {
				tmp += "#";
				text = text.substring(2);
			} else if (t_2 == "%4") {
				tmp += "|";
				text = text.substring(2);
			} else if (t_2 == "%5") {
				tmp += ":";
				text = text.substring(2);
			} else if (t_2 == "%6") {
				tmp += ";";
				text = text.substring(2);
			} else if (t_2 == "%7") {
				tmp += "\"";
				text = text.substring(2);
			} else if (t_2 == "%8") {
				tmp += "@";
				text = text.substring(2);
			} else {
				tmp += t_1;
				text = text.substring(1);
			}
		}
	}

	return tmp;
};


/**
 * Split input, and put to array in the order
 * Allow empty tokens.
 */
Q.common.mySplit = function(input, separator) {
	if (input == null || separator == null) return null;
	
	/*
	var items = new Array();
	var i = 0;
	while(input != null) {
		var index = input.indexOf(separator);
		if (index > -1) {
			items[i] = input.substring(0, index);
			input = input.substring(index + 1);
		} else {
			items[i] = input;
			input = null;
		}
		
		i++;
	} */
	
	var items = input.split(separator);
	
	return items;
	
};



/** use jQuery to do Ajax */
Q.common.jQueryAjaxCall = function(url, params, callBackFunction)
{
	
    //alert("in ajax: " + JSON.stringify( params ));
	var request = $.ajax({
		url : url,
		type : "POST",
		data :   JSON.stringify( params ),   /* Need this to work */
		dataType: 'json', /* 'text',    /* if want to see the data, set to text, then alert(response) will display the text */
		contentType: 'application/json; charset=utf-8'  /* format sent to server */
	});

	request.done(function(msg) {
		if (msg == null) {
			alert("msg is null");
		} else {
			//alert("msg: " + msg); //.rowList[1].companyName);
		}
		
		callBackFunction(msg);
	
	});

	request.fail(function(jqXHR, textStatus) {

		// debug
		alert("Request failed: " + textStatus);
		
		if (jqXHR.status === 0) 
	    {
	        $('#displayNode').html("err");
	    } else if (jqXHR.status == 404) 
	    {
	        $('#displayNode').html('err!');
	    } else if (jqXHR.status == 500) 
	    {
	        alert("err!");
	    } else if (exception === 'parsererror') 
	    {
	        $('#displayNode').html('err parsererror');
	    } else if (exception === 'timeout') 
	    {
	        $('#displayNode').html('err!');
	    } else if (exception === 'abort') 
	    {
	        $('#displayNode').html('err!');
	    } else 
	    {
	    $('#displayNode').html('err!');
	    window.location='/WEBA/p/login_input.do';
	    }



	}); 
	
}; 

/* menuids = ["sidebarmenu1"] - Enter id(s) of each Side Bar Menu's main UL, separated by commas */
Q.common.initsidebarmenu = function(menuids) {
	for (var i = 0; i < menuids.length; i++) {
		var ultags = document.getElementById(menuids[i])
				.getElementsByTagName("ul");
		for (var t = 0; t < ultags.length; t++) {
			ultags[t].parentNode.getElementsByTagName("a")[0].className += " subfolderstyle";
			if (ultags[t].parentNode.parentNode.id == menuids[i]) { //if this is a first level submenu
				ultags[t].style.top = ultags[t].parentNode.offsetHeight
						+ "px"; //dynamically position first level submenus to be width of main menu item
			} else {
				//else if this is a sub level submenu (ul)
				ultags[t].style.left = ultags[t - 1]
						.getElementsByTagName("a")[0].offsetWidth
						+ "px"; //position menu to the right of menu item that activated it
			}
			
			ultags[t].parentNode.onmouseover = function() {
				this.getElementsByTagName("ul")[0].style.display = "block";
			};
			ultags[t].parentNode.onmouseout = function() {
				this.getElementsByTagName("ul")[0].style.display = "none";
			};
		}
		for (var t = ultags.length - 1; t > -1; t--) { //loop through all sub menus again, and use "display:none" to hide menus (to prevent possible page scrollbars
			ultags[t].style.visibility = "visible";
			ultags[t].style.display = "none";
		}
	}
};


/**  May be used by some pages */
Q.common.popTopsection = function() {
    var top = document.getElementById("topsection");
    if (top.style.visibility == "" || top.style.visibility == "visible") {
        top.style.visibility = "hidden";
        top.style.display = "none";
    } else {
        top.style.visibility = "visible";
        top.style.display = "block";
    }
};

/** End of Script */




