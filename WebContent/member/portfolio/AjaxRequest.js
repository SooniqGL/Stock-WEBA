/* functions to make ajax call to get stock info */
function checkStock() {
    var ticker = document.getElementById("inputVO.positionInfo.ticker").value;
    if (ticker == "") {
        alert("Need to enter a ticker to populate.");
        return;
    }
    
    var url = "../m/portfolio_stockinfoajax.do";
    var params = "inputVO.mode=stockinfoajax&&inputVO.portfolioId=&inputVO.positionInfo.ticker=" + ticker;
    
    makeAjaxCall(url, params, fnAjaxRespone);
    
}


function makeAjaxCall(url, params, callBackFunction)
{
    //alert("in ajax");

	//if(window.resetPageTimeout) {
	//	resetPageTimeout();		
	//}
	var myReq = new ajaxReq1(params, callBackFunction);
	return myReq.req(url);  
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
/*
 * <doc>
<status><bean:write name='itemVO' property='success'/></status>
<stockId><bean:write name='itemVO' property='stockId'/></stockId>
<price><bean:write name='itemVO' property='openPrice'/></price>
<lastDate><bean:write name='itemVO' property='openDate'/></lastDate>
<companyName><bean:write name='itemVO' property='companyName'/></companyName>
 * </doc>
 */
function fnAjaxRespone(xmlobj) {

    var xmlString = xmlobj.responseText;	
    //alert("output: " + xmlString);

    var xmlDoc = null;
    if (window.DOMParser) {
        var parser=new DOMParser();
        xmlDoc=parser.parseFromString(xmlString,"text/xml");
    }
    else // Internet Explorer
    {
        xmlDoc=new ActiveXObject("Microsoft.XMLDOM");
        xmlDoc.async="false";
        xmlDoc.loadXML(xmlString); 
    } 

    // get values
    var status = xmlDoc.getElementsByTagName("status")[0].childNodes[0].nodeValue;
    if (status != "true") {
        // error, not found
        alert("Ticker not found in our DB ...")
        return;
    }
    
    // status is true
    // alert("status: " + status);
    
    //var stockId = xmlDoc.getElementsByTagName("stockId")[0].childNodes[0].nodeValue;
    // alert("totalDailyItems: " + totalDailyItems);
    
    var openPrice = xmlDoc.getElementsByTagName("openPrice")[0].childNodes[0].nodeValue;

    var openDate = xmlDoc.getElementsByTagName("openDate")[0].childNodes[0].nodeValue;
    
    var companyName = xmlDoc.getElementsByTagName("companyName")[0].childNodes[0].nodeValue;
    
   //alert("open price: " + openPrice);
    
    // set data
    document.getElementById("inputVO.positionInfo.openDate").value = openDate;
    document.getElementById("inputVO.positionInfo.openPrice").value = openPrice;
    document.getElementById("inputVO.positionInfo.companyName").value = companyName;

    //alert("companyName: " + companyName);
}



/* The End */