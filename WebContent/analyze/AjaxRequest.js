if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

Q.namespace("chart");


Q.chart.makeAjaxCall = function(url, params, callBackFunction)
{
    //alert("in ajax");

	//if(window.resetPageTimeout) {
	//	resetPageTimeout();		
	//}
	var myReq = new Q.chart.ajaxReq1(params, callBackFunction);
	return myReq.req(url);  
};

Q.chart.ajaxReq1 = function(params, callBackFunction) {

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
	};
	
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
			
	
	};
	
	this.req = function(url) {
	
            var xmlobj = this.createRequestObject();
            xmlobj.open('POST',url, true);
            xmlobj.setRequestHeader("Content-type", "application/x-www-form-urlencoded");
            if(params == null) params ="";
            xmlobj.setRequestHeader("Content-length", params.length);
            this.handleReadyStateChange(xmlobj, callBackFunction, params);
            xmlobj.send(params);
	};
};

// callback method
/*
 * <doc>
 * <status></status>
 * <stockId></stockId>
 * <totalDailyItems></totalDailyItems>
 * <totalWeeklyItems></totalWeeklyItems>
 * <dailyDataStr></dailyDataStr>
 * <weeklyDataStr></weeklyDataStr>
 * </doc>
 */
Q.chart.fnAjaxRespone = function(xmlobj) {

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
        // error, or no more data
    	QQ_barchart.setHasMoreData( false );   // so that do not try next time
        QQ_barchart.setInCallFlag( false );
        return;
    }
    
    // status is true
    //alert("status: " + status);
    
    var totalDailyItems = xmlDoc.getElementsByTagName("totalDailyItems")[0].childNodes[0].nodeValue;
    //alert("totalDailyItems: " + totalDailyItems);
    
    var totalWeeklyItems = xmlDoc.getElementsByTagName("totalWeeklyItems")[0].childNodes[0].nodeValue;
    //alert("totalWeeklyItems: " + totalWeeklyItems);
    
    var dailyDataStr = Q.chart.appendChildrenToOne(xmlDoc.getElementsByTagName("dailyDataStr")[0]);
    //alert("dailyDataStr: " + dailyDataStr.length + ", content: " + dailyDataStr);
    
    var weeklyDataStr = Q.chart.appendChildrenToOne(xmlDoc.getElementsByTagName("weeklyDataStr")[0]);
    //alert("weeklyDataStr: " + weeklyDataStr.length);
    
    // append the data
    var prevDailDataStr  = document.getElementById("dailyDataStr").value;
    document.getElementById("dailyDataStr").value = dailyDataStr + "#" + prevDailDataStr;
    
    var prevWeeklyDataStr  = document.getElementById("weeklyDataStr").value;
    // need to check if the joined week is obtained twice
    var lastWeekData = weeklyDataStr.substring(weeklyDataStr.lastIndexOf("#") + 1);
    var lastDate = lastWeekData.substring(0, lastWeekData.indexOf("|"));
    //alert("lastDate: " + lastDate + ", prev: " + prevWeeklyDataStr.substring(0, 10));
    if (prevWeeklyDataStr.substring(0, 10) == lastDate) {
        // need to remove
        //alert("lastDate, removing: " + lastDate);
        weeklyDataStr = weeklyDataStr.substring(weeklyDataStr.indexOf("#") + 1);
    }
    
    // preappend new data to the existing one
    document.getElementById("weeklyDataStr").value = weeklyDataStr + "#" + prevWeeklyDataStr;
    
    var doWeekly = document.getElementById("doWeekly").checked;
    var numNewData = 0;
    if (doWeekly) {
        numNewData = parseInt(totalWeeklyItems);
    } else {
        numNewData = parseInt(totalDailyItems);
    }
    
    //alert("doWeekly: " + doWeekly);
    
    // reset the endIndex and redraw
    if (numNewData == 0) {
        // no more new data, so give up
        QQ_barchart.setHasMoreData( false );   // so that do not try next time
        QQ_barchart.setInCallFlag( false );
        //alert("nothing in ajax call");
        return;
    } else {
        //alert("something in ajax call: " + numNewData);
        // prepare data, and shift the end dates
        QQ_barchart.prepareData();
        QQ_barchart.updateEndIndex3(numNewData);
        
        // need to draw
        QQ_barchart.clearCanvas();
        QQ_barchart.drawChart();
    }
    
    //alert("here");
    QQ_barchart.setInCallFlag( false );
    
};

/*
 * Firefox is spliting the xml nodes to 4096 a piece.  
 * IE is not doing it anywhere.
 * Here need to reattach the data into one.
 */
Q.chart.appendChildrenToOne = function( docNode ) {
    var data = "";
    for (var i=0; i < docNode.childNodes.length; i++) {
        data += docNode.childNodes[i].nodeValue;
    }
    
    return data;
};

/* The End */