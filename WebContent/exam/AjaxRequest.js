function makeAjaxCall(url, params, callBackFunction)
{
	if(window.resetPageTimeout) {
		resetPageTimeout();		
	}
	var myReq = new ajaxReq1(params, callBackFunction);
	myReq.req(url);  
}

function ajaxReq1(params, callBackFunction) {

	this.createRequestObject = function() {
			//alert("Inside AjaxReq");
		var xmlhttp;
		try { 
			xmlhttp = new ActiveXObject('Msxml2.XMLHTTP'); 
		}
		catch (err) { 
			try { 
				xmlhttp = new ActiveXObject('Microsoft.XMLHTTP'); 
			}
			catch (err) { 
				try {
					xmlhttp = new XMLHttpRequest(); 
				}
				catch (err) { 
					xmlhttp = null; 
				}
			}
		}
		return xmlhttp;
	}
	
	this.handleReadyStateChange = function (xmlObj, callBackFunction, params) {
		if(document.getElementById("ajax_loading"))
		{
			document.getElementById("ajax_loading").style.display="";
		}		
		var timeOut;
		timeOut = window.setInterval( function() {
			if(xmlObj && xmlObj.readyState == 4 && xmlObj.status == 200){
				window.clearInterval(timeOut);
				if(document.getElementById("ajax_loading"))
				{
					document.getElementById("ajax_loading").style.display="none";
				}				
				callBackFunction(xmlObj, params);
			}
		},50);
			
	
	}
	
	this.req = function(url) {
	
			var xmlobj = this.createRequestObject();
			xmlobj.open('POST',url, true);
			xmlobj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
			if(params == null) params ="";
			xmlobj.setRequestHeader("Content-length", params.length);
			this.handleReadyStateChange(xmlobj, callBackFunction, params);
			xmlobj.send(params);
	}
}

// callback method
function fnAjaxRespone(xmlobj) {

	var outputHTML = xmlobj.responseText;	
	outputHTML = outputHTML.replace("<AJAXRESPONSE>","");
	outputHTML = outputHTML.replace("</AJAXRESPONSE>","");
	//alert(outputHTML);			
	//var accountDetail = document.getElementById("AccountDetail");
	//accountDetail.innerHTML = outputHTML;

}

/* The End */