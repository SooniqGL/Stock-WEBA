/* 
 * Scripts to help the exam page ...
 * 1/16/2011
 */

// exam_helper.js

/**
 * Use the jquery and the mousewheel plug in to control the period.
 * The plugin (3.0.6) seems working fine on the lastest JQuery 1.7.1. button_right_1d
 */
$(function() {   
    $('#canvas').mousewheel(function(event, delta, deltaX, deltaY) {   
        var canvas = document.getElementById('canvas');
        if (canvas != null) {
            //alert("scroll: " + delta + "," + deltaX + "," + deltaY );   
            //this.scrollLeft -= (delta * 30);      
        	QQ_examhelper.setPeriodBymousewheel(delta);
            
            event.stopPropagation();
            event.preventDefault();
        }
      });
 
    $('#button_right_1d').mousewheel(function(event, delta, deltaX, deltaY) {   
        var canvas = document.getElementById('canvas');
        if (canvas != null) {
            //alert("scroll: " + delta + "," + deltaX + "," + deltaY );   
            // In Firefox, the delta = 0.3333333, which is strange.
            delta = 1;
        
            QQ_examhelper.goByDay(delta);
            
            event.stopPropagation();
            event.preventDefault();
        }
      });
 
    $('#button_right_5d').mousewheel(function(event, delta, deltaX, deltaY) {   
        var canvas = document.getElementById('canvas');
        if (canvas != null) {
            //alert("scroll: " + delta + "," + deltaX + "," + deltaY );   
            // In Firefox, the delta = 0.3333333, which is strange.
            delta = 1;
            
            delta *= 5;
            QQ_examhelper.goByDay(delta);
            
            event.stopPropagation();
            event.preventDefault();
        }
      });
  
    $('#button_right_10d').mousewheel(function(event, delta, deltaX, deltaY) {   
        var canvas = document.getElementById('canvas');
        if (canvas != null) {
            //alert("scroll: " + delta + "," + deltaX + "," + deltaY );   
            // In Firefox, the delta = 0.3333333, which is strange.
            delta = 1; 
            
            delta *= 10;
            QQ_examhelper.goByDay(delta);
            
            event.stopPropagation();
            event.preventDefault();
        }
      });
 
    $('#button_right_1m').mousewheel(function(event, delta, deltaX, deltaY) {   
        var canvas = document.getElementById('canvas');
        if (canvas != null) {
            //alert("scroll: " + delta + "," + deltaX + "," + deltaY );   
            // In Firefox, the delta = 0.3333333, which is strange.
            delta = 1; 
            
            QQ_examhelper.goByMonth(delta);
 
            event.stopPropagation();
            event.preventDefault();
        }
      });
});

var QQ_examhelper = (function(window, undefined) {
	
	// variables to track transactions
	var tradeOpenDateArray = new Array();
	var tradeCloseDateArray = new Array();
	var tradeOpenPriceArray = new Array();
	var tradeClosePriceArray = new Array();
	var tradeTypeArray = new Array();
	var tradeLengthArray = new Array();
	var tradeMaxPosArray = new Array();
	var tradeMaxNegArray = new Array();
	var numClosedPos = 0; // closed so far
	var tradeAmt = 5000; // for each position

	// tracking current trade position
	// var currentOpenDate = "";
	// var currentOpenPrice = 0;
	// var currentTradeType = "";  // Long or Short
	// var openIndex = 0;   // position is opened, to used for num days
	// var stopType = "N";   // N - none, R - Regular, T - Trailing
	// var stopAmount = 0;     // either $ amount or % amount
	// var stopPrice = 0;    // If R stop, it is stopAmount; if T stop, then P * (1 +/- 0.01 * stopAmount)
	// var lastCheckedIndex = 0;   // tracking the items passed stop checking
	var currentTrade = {
		tradeType : "",
		openDate : "",
		openPrice : 0,
		openIndex : 0,
		stopType : "N",
		stopAmount : 0,
		stopPrice : 0,
		lastCheckedIndex : 0
	};
	
	// for moving loop
	var moveFlag = "false";

	/* begin on functions */
	function setFocus() {
	    document.getElementById("inputVO.ticker").select();
	    document.getElementById("inputVO.ticker").focus();
	    
	    // prepare the data and draw
	    QQ_exambarchart.init();
	    QQ_exambarchart.prepareData();
	    QQ_exambarchart.updateEndIndex(0);
	    QQ_exambarchart.drawChart();
	    adjustButtons();

	}
	
	function doSelfSelect() {
	    //alert("here 1 ...");
	
	    var form = document.examForm;
	    if (document.getElementById("inputVO.ticker").value == "") {
	        alert("Enter ticker ...");
	        return;
	    }
	
	    // remove the huge data before submit, otherwise, get http header error
	    //document.getElementById("dailyDataStr").value = "";
	    document.getElementById("inputVO.mode").value = "S";
	    form.submit();
	   
	}
	
	function doAutoSelect() {
	    var form = document.examForm;  
	    document.getElementById("inputVO.mode").value = "A";
	    // remove the huge data before submit, otherwise, get http header error
	    // or use different form to hold big data
	    //document.getElementById("dailyDataStr").value = "";
	    form.submit();
	}
	
	// do not need to submit
	function setPeriod(period) {
	        document.getElementById("inputVO.period").value = period;
	        if (document.getElementById("inputVO.ticker").value != "") {
	        	QQ_exambarchart.updateEndIndex(0);
	        	QQ_exambarchart.drawChart();
	            adjustButtons();
	        } 
	}
	
	
	function setPeriodBymousewheel(delta) {
	        var oldPeriod = document.getElementById("inputVO.period").value;
	        if (delta > 0){
	            if (oldPeriod < 18) {
	                oldPeriod ++;
	                document.getElementById("inputVO.period").value = oldPeriod;
	            }
	        } else {
	            if (oldPeriod > 1) {
	                oldPeriod --;
	                document.getElementById("inputVO.period").value = oldPeriod;
	            }
	        }
	        
	        if (document.getElementById("inputVO.ticker").value != "") {
	        	QQ_exambarchart.updateEndIndex(0);
	        	QQ_exambarchart.drawChart();
	            adjustButtons();
	        } 
	}
	
	function setOption() {
	        // do not need to submit, but just redraw if has data already
	        if (document.getElementById("inputVO.ticker").value != "") {
	        	QQ_exambarchart.drawChart();
	        } 
	}
	
	// move the chart to the right by number of days
	function goByDay( numDay ) {
		var dayInt = parseInt(numDay, 10);
	
	        // if reach the end, do not move
	        if (QQ_exambarchart.checkInRange(dayInt) == false) {
	        	return;
	        }
	
	        if (document.getElementById("inputVO.ticker").value != "") {
	            // update the index and redraw the chart
	        	QQ_exambarchart.updateEndIndex(dayInt);
	        	QQ_exambarchart.drawChart();
	            adjustButtons();
	            
	        }  
	}
	
	function startOver() {
	//alert("start over");
	        if (document.getElementById("inputVO.ticker").value != "") {
	            // reset
	            moveFlag = "false";   // stop the move, if any
	            
	            // reset all the tracking system
	            tradeOpenDateArray   = new Array();
	            tradeCloseDateArray  = new Array();
	            tradeOpenPriceArray  = new Array();
	            tradeClosePriceArray = new Array();
	            tradeTypeArray       = new Array();
	            tradeLengthArray     = new Array();
	            tradeMaxPosArray     = new Array();
	            tradeMaxNegArray     = new Array();
	            numClosedPos = 0;    // closed so far
	            
	            // reset the trade position 
	            currentTrade.tradeType = "";  // empty, Long, Short
	            currentTrade.openDate = "";
	            currentTrade.openPrice = 0;
	            currentTrade.openIndex = 0;
	                        
	            // reset the position and history elements
	            var positionElem = document.getElementById("positionBoard");
	            positionElem.innerHTML = "";
	            var performElem = document.getElementById("tradeHistory");
	            performElem.innerHTML = "";
	            
	            // redo everything from beginning
	            QQ_exambarchart.startOver();
	            adjustButtons();
	            
	            // enable the long and short buttons
	            document.getElementById("button_long").disabled  = false;
	            document.getElementById("button_short").disabled = false;
	            document.getElementById("button_close").disabled = true;
	            document.getElementById("adjustStop").disabled = true;
	            
	            // reset stop option
	            // tradeStop = {stopType: "N", stopAmount: 0, stopPrice: 0, lastCheckedIndex: 0};
	            currentTrade.stopType = "N";
	            document.getElementById("stopAmount").value = "";
	            document.getElementsByName("ckStop")[0].checked = true;              
	        }     
	}
	
	function goByDayLoop( numDay, tugIt ) {
	    if (tugIt == "true") {
	        if (moveFlag == "false") {
	            moveFlag = "true";
	        } else {
	            moveFlag = "false";
	            return;
	        }
	    }

	    if (moveFlag == "true") {
	        var dayInt = parseInt(numDay, 10);
	        // call one time:
	        goByDay( numDay );

	        // if reach the end, do not move
	        if (QQ_exambarchart.checkInRange(dayInt) == false) {
	            return;
	        } else {
	        	// Note: the package name is needed, even in the same package.
	            setTimeout("QQ_examhelper.goByDayLoop('" + numDay + "', 'false')", 200);
	        }
	    }
	}
	
	
	function goByMonth( numMonth ) {
		var numberMonth = parseInt(numMonth, 10);
	        var dayInt = numberMonth * 22;
	
	        // if reach the end, do not move
	        if (QQ_exambarchart.checkInRange(dayInt) == false) {
	            return;
	        }
	
	        if (document.getElementById("inputVO.ticker").value != "") {
	            // update indexes, draw
	        	QQ_exambarchart.updateEndIndex(dayInt);
	        	QQ_exambarchart.drawChart();
	            adjustButtons();
	        } 
	
	}
	
	
	function adjustButtons() {
	
		var oneDButton = document.getElementById("button_right_1d");
		if (oneDButton == null) {
			// now stock is not loaded
			return;
		}
	
		// do day: -1, -5, -10, 1, 5, 10
		if (QQ_exambarchart.checkInRange(1) == false) {
			document.getElementById("button_right_1d").disabled = true;
	                document.getElementById("button_right_xd").disabled = true;
		} else {
			document.getElementById("button_right_1d").disabled = false;
	                document.getElementById("button_right_xd").disabled = false;
		}
	
		if (QQ_exambarchart.checkInRange(5) == false) {
			document.getElementById("button_right_5d").disabled = true;
	                document.getElementById("button_right_xxd").disabled = true;
		} else {
			document.getElementById("button_right_5d").disabled = false;
	                document.getElementById("button_right_xxd").disabled = false;
		}
	
		if (QQ_exambarchart.checkInRange(10) == false) {
			document.getElementById("button_right_10d").disabled = true;
		} else {
			document.getElementById("button_right_10d").disabled = false;
		}
	
		// month: -1, -6, -12, 1, 6, 12 * 22
		if (QQ_exambarchart.checkInRange(22) == false) {
			document.getElementById("button_right_1m").disabled = true;
		} else {
			document.getElementById("button_right_1m").disabled = false;
		}
	
	        // show the latest price info
	        adjustPrice();
	        
	        // check if the stop will kick off the position
	        // if not, then need to update the position board with the latest price info
	        var isClosed = checkIfNeedToClosePosition();
	        if (isClosed == false) {
	            updatePositionBoard();
	        }
	}
	
	// closeArray[endIndex] is the current price
	function adjustPrice() {
	    //alert("flip M");
	    //var form = document.examForm;
	    var priceElem = document.getElementById("priceBoard");
	    priceElem.innerHTML = "&nbsp;Price: " + QQ_exambarchart.getCurrentClose();
	}
	
	// right at the endIndex time, go long
	function goLong() {
	    if (checkStopOptions("Long", QQ_exambarchart.getCurrentClose()) == false) {
	        return;
	    }
	    
	    // do the long ...
	    currentTrade.tradeType = "Long";  // empty, Long, Short
	    currentTrade.openDate  = QQ_exambarchart.getCurrentDate();
	    currentTrade.openPrice = QQ_exambarchart.getCurrentClose();
	    currentTrade.openIndex = QQ_exambarchart.getEndIndex();
	    
	    // initial the tracking index
	    currentTrade.lastCheckedIndex = QQ_exambarchart.getEndIndex();
	    
	    // disable the long and short bottons
	    document.getElementById("button_long").disabled  = true;
	    document.getElementById("button_short").disabled = true;
	    document.getElementById("button_close").disabled = false;
	    document.getElementById("adjustStop").disabled = false;
	
	    // update position board
	    var positionElem = document.getElementById("positionBoard");
	    var html = "<table><tr><td>Trade: </td><td>" + currentTrade.tradeType +
	        "</td></tr><tr><td>Date: </td><td>" + currentTrade.openDate +
	        "</td></tr><tr><td>Price: </td><td>" + currentTrade.openPrice +
	        "</td></tr><tr><td>Stop: </td><td>" + formatNumber(currentTrade.stopPrice) +
	        "</td></tr><tr><td>Profit: </td><td>"  +
	        "0.0</td></tr></table>";
	
	    positionElem.innerHTML = html;
	}
	
	function goShort() {
	    if (checkStopOptions("Short", QQ_exambarchart.getCurrentClose()) == false) {
	        return;
	    }
	    
	    // set short position
	    currentTrade.tradeType = "Short";  // empty, Long, Short
	    currentTrade.openDate  = QQ_exambarchart.getCurrentDate();
	    currentTrade.openPrice = QQ_exambarchart.getCurrentClose();
	    currentTrade.openIndex = QQ_exambarchart.getEndIndex();
	
	    // initial the tracking index
	    currentTrade.lastCheckedIndex = QQ_exambarchart.getEndIndex();
	        
	    // disable the long and short bottons
	    document.getElementById("button_long").disabled  = true;
	    document.getElementById("button_short").disabled = true;
	    document.getElementById("button_close").disabled = false;
	    document.getElementById("adjustStop").disabled = false;
	
	    // update position board
	    var positionElem = document.getElementById("positionBoard");
	    var html = "<table><tr><td>Trade: " + currentTrade.tradeType +
	        "</td></tr><tr><td>Date: " + currentTrade.openDate +
	        "</td></tr><tr><td>Price: " + currentTrade.openPrice +
	        "</td></tr><tr><td>Stop: " + formatNumber(currentTrade.stopPrice) +
	        "</td></tr><tr><td>Profit: "  +
	        "0.0</td></tr><tr><td>Status: Open</td></tr></table>";
	
	    positionElem.innerHTML = html;
	}
	
	// do not use the element ID as function name, it will not work.
	function adjustStopFunc() {
	    //alert("adjust stop called: " + currentTrade.stopType);
	    // in the middle of trade, one may adjust the stop
	    checkStopOptions(currentTrade.tradeType, QQ_exambarchart.getCurrentClose());
	    currentTrade.lastCheckedIndex = QQ_exambarchart.getEndIndex();   // very important to reset this!
	    updatePositionBoard();   // show the stop price
	    //alert("You have changed the stop option.");
	}
	
	/*
	 * The stop type/ stop amount has to be verified before 
	 * A long or short order is defined.
	 */
	function checkStopOptions(theTradeType, theOpenPrice) {
	    var bool = true;
	    // save the original value, in not set right, restore back
	    var stopTypeSave = currentTrade.stopType;
	    var stopAmountSave = currentTrade.stopAmount;
	    var stopPriceSave = currentTrade.stopPrice;
	    var stopAmountStrSave = document.getElementById("stopAmount").value;
	    
	    // check the stop type
	    var ckStopObj = document.getElementsByName("ckStop"); 
	    if (ckStopObj[0].checked == true) {
	        currentTrade.stopType = "N";
	        
	        // set the default values
	        currentTrade.stopAmount = 0;
	        currentTrade.stopPrice  = 0;
	        document.getElementById("stopAmount").value = "";
	    } else if (ckStopObj[1].checked == true) {
	        currentTrade.stopType = "R";
	    } else if (ckStopObj[2].checked == true) {
	        currentTrade.stopType = "T";
	    } else {
	        // need to check at lease one option
	        alert("At least one stop option has to be selected.");
	        return false;
	    }
	    
	    // do this only if R/T is set
	    if (currentTrade.stopType != "R" && currentTrade.stopType != "T") {
	        return true;
	    }
	    
	    // check if stop option is given
	    // tradeStop = {stopType: "N", stopAmount: 0, stopPrice: 0, lastCheckedIndex: 0};
	    if (currentTrade.stopType == "R" || currentTrade.stopType == "T") {
	        // stopAmount has to be defined in this case
	        var stopAmountStr = document.getElementById("stopAmount").value;
	        if (stopAmountStr == null || stopAmountStr == "" || Q.common.isValidNumber(stopAmountStr) != true) {
	            alert("Please give the stop $ for regular stop or % for trailing stop.");
	            bool = false;
	        } else {
	            currentTrade.stopAmount = parseFloat(stopAmountStr);     
	            if (currentTrade.stopAmount <= 0) {
	                alert("Please enter POSITIVE NUMBER for the Stop Amount box.");
	                bool = false;
	            }
	        }   
	    } 
	    
	    if (bool == true) {
	        if (currentTrade.stopType == "R") {
	            // need to check if the stopAmount < openPrice for long; > for short
	            if (theTradeType == "Long") {
	                if (currentTrade.stopAmount >= theOpenPrice) {
	                    alert("Stop has to be less than the open price for Long.");
	                    bool = false;
	                }
	            } else {
	                if (currentTrade.stopAmount <= theOpenPrice) {
	                    alert("Stop has to be greater than the open price for Short.");
	                    bool = false;
	                }
	            }
	        } else if (currentTrade.stopType == "T" && theTradeType == "Long") {
	            // stopAmount has to be <= 100
	            if (currentTrade.stopAmount >= 100) {
	                alert("For trailing Stop with long order, the % amount should be less than or equal to 100.");
	                bool = false;
	            }
	        }
	    }
	    
	    // define the stop price
	    if (bool == true) {
	        if (currentTrade.stopType == "R") {
	            currentTrade.stopPrice = currentTrade.stopAmount;
	        } else if (currentTrade.stopType == "T") {
	            if (theTradeType == "Long") {
	                currentTrade.stopPrice = theOpenPrice * (1 - currentTrade.stopAmount * 0.01);
	            } else if (theTradeType == "Short") {
	                currentTrade.stopPrice = theOpenPrice * (1 + currentTrade.stopAmount * 0.01);
	            }
	        }
	        
	        // debug
	        //alert("info: trade: " + theTradeType + ", stopType: " + currentTrade.stopType 
	        //    + ", stop Amount: " + currentTrade.stopAmount + ", stopPrice: " + currentTrade.stopPrice);
	    } 
	    
	    if (bool == false) {
	        // something is wrong on this checking, restore the original values
	        // This is used when a reset is failed, still keep the original
	        currentTrade.stopType   = stopTypeSave;
	        currentTrade.stopAmount = stopAmountSave;
	        currentTrade.stopPrice  = stopPriceSave
	        
	        // recover the UI as well
	        document.getElementById("stopAmount").value = stopAmountStrSave;
	        if (stopTypeSave == "N") {
	            ckStopObj[0].checked = true;
	        } else if (stopTypeSave == "R") {
	            ckStopObj[1].checked = true;
	        } else if (stopTypeSave == "T") {
	            ckStopObj[2].checked = true;
	        }
	    }
	    
	    return bool;
	}
	
	// If there is a trade position, check if need to be closed
	// during to the price movement.  
	// Return true if closed, false if not.
	function checkIfNeedToClosePosition() {
	    if (currentTrade.tradeType == "") {
	        // no position is open, so nothing to close
	        return false;
	    }
	    
	    if (currentTrade.stopType != "R" && currentTrade.stopType != "T") {
	        // no stop is defined, do nothing in this case
	        return false;
	    }
	    
	    // there is a trading position open, either Long or Short
	    // and stopType/stopAmount are defined - then check from open index to current index
	    // see if the position should be closed in the middle
	    // -- start with the (last check position + 1)
	    
	    // !!!!! lastCheckedIndex + 1  // need to init/reset ....
	    var isClosed = false;
	    var locEndIndex = QQ_exambarchart.getEndIndex();
	    for (var i = currentTrade.lastCheckedIndex + 1; i <= locEndIndex; i ++) {
	        //alert("checking for: " + dateArray[i]);
	    	var closeAtDay = QQ_exambarchart.getCloseByIndex(i);
	        
	    	if (currentTrade.stopType == "T") {
	            // adjust the stopPrice for trailing stop
	            var newStop = 0;
	            if (currentTrade.tradeType == "Long") {
	                newStop = closeAtDay * (1 - 0.01 * currentTrade.stopAmount);
	                if (newStop > currentTrade.stopPrice) {
	                    // update stop price
	                    currentTrade.stopPrice = newStop;
	                    
	                    // debug
	                    //alert("new stop: " + currentTrade.stopPrice);
	                }
	            } else if (currentTrade.tradeType == "Short") {
	                newStop = closeAtDay * (1 + 0.01 * currentTrade.stopAmount);
	                if (newStop < currentTrade.stopPrice) {
	                    // update stop price
	                    currentTrade.stopPrice = newStop;
	                    
	                    // debug
	                    //alert("new stop: " + currentTrade.stopPrice);
	                }
	            }
	        }
	
	        // check the current price with the stopPrice
	        if (currentTrade.tradeType == "Long" && closeAtDay <= currentTrade.stopPrice) {
	            // position should be closed at the index i place
	            alert("closing ...");
	            goCloseForIndex(i);
	            isClosed = true;
	            break;   // stop checking
	        } else if (currentTrade.tradeType == "Short" && closeAtDay >= currentTrade.stopPrice) {
	            // position should be closed at the index i place
	            alert("closing ...");
	            goCloseForIndex(i);
	            isClosed = true;;
	            break;   // stop checking
	        }
	    }
	
	    // update the tracking Index
	    if (isClosed == false) {
	        currentTrade.lastCheckedIndex = QQ_exambarchart.getEndIndex();
	    }
	    
	    //alert("last index: " + currentTrade.lastCheckedIndex + ", endIndex: " + endIndex);
	    
	    return isClosed;
	
	}
	
	// This one is called as the close button is 
	// clicked, so the current endIndex is used as the close index.
	function goClose() {
	    goCloseForIndex(QQ_exambarchart.getEndIndex());
	}
	
	// The current trade position is closed
	function goCloseForIndex(closeIndex) {
		var dateAtClose = QQ_exambarchart.getDateByIndex(closeIndex);
		var closeAtClose = QQ_exambarchart.getCloseByIndex(closeIndex);
		
	    // record to array
	    tradeOpenDateArray[numClosedPos]   = currentTrade.openDate;
	    tradeOpenPriceArray[numClosedPos]  = currentTrade.openPrice;
	    tradeCloseDateArray[numClosedPos]  = dateAtClose;
	    tradeClosePriceArray[numClosedPos] = closeAtClose;
	    tradeTypeArray[numClosedPos]       = currentTrade.tradeType;
	    tradeLengthArray[numClosedPos]     = closeIndex - currentTrade.openIndex;
	
	    // check the max pos and loss
	    var maxPosProfit = 0;
	    var maxNegProfit = 0;
	    var locProfit = 0;

	    var closeAtOpen = QQ_exambarchart.getCloseByIndex(currentTrade.openIndex);
	    for (var i = currentTrade.openIndex + 1; i <= closeIndex; i ++) {
	    	var closeAtDay = QQ_exambarchart.getCloseByIndex(i);
	        
	        locProfit = Math.round(10000* (closeAtDay - closeAtOpen) / closeAtOpen) / 100;
	        if (currentTrade.tradeType == "Short") {
	            locProfit *= -1;
	        }
	
	        if (locProfit > 0) {
	            if (locProfit > maxPosProfit) {
	                maxPosProfit = locProfit;
	            }
	        } else {
	            if (locProfit < maxNegProfit) {
	                maxNegProfit = locProfit;
	            }
	        }
	    }
	
	    // keep these to array
	    tradeMaxPosArray[numClosedPos]     = maxPosProfit;
	    tradeMaxNegArray[numClosedPos]     = maxNegProfit;
	    
	    
	    // update the total number of trades
	    numClosedPos += 1;
	
	    // enable the long and short buttons
	    document.getElementById("button_long").disabled  = false;
	    document.getElementById("button_short").disabled = false;
	    document.getElementById("button_close").disabled = true;
	    document.getElementById("adjustStop").disabled = true;
	    
	    // update position board
	    var profitPercent = Math.round(10000* (closeAtClose - currentTrade.openPrice) / currentTrade.openPrice) / 100;
	    var profit = Math.round(tradeAmt * profitPercent) / 100;
	    if (currentTrade.tradeType == "Short") {
	        profitPercent *= -1;
	        profit *= -1;
	    }

	    var theColor = "#ff0000";
	    if (profit >= 0) {
	        theColor = "#00ff00";
	    }
	    var positionElem = document.getElementById("positionBoard");
	    var html = "<table><tr><td>Trade: " + currentTrade.tradeType +
	        "</td></tr><tr><td>Date: " + currentTrade.openDate +
	        "</td></tr><tr><td>Price: " + currentTrade.openPrice +
	        "</td></tr><tr><td>C Date: " + dateAtClose +
	        "</td></tr><tr><td>C Price: " + closeAtClose +
	        "</td></tr><tr><td>Profit(%): <font color=\""
	        + theColor + "\">" + profitPercent +
	        "</font></td></tr><tr><td>Profit($): <font color=\""
	        + theColor + "\">" +  + profit +
	        "</font></td></tr><tr><td>Status: Closed</td></tr></table>";
	
	    positionElem.innerHTML = html;

	    // update the history list
	    updateTradeHistory();

	    // Ajax call to report the result to server
	    var url = "../s/exam_ajaxupdate.do?inputVO.mode=AU";
	    
	    // format: trade type|stock id|trade amount|open date|open price|close date|close price|profit|profit percent|max postive|max negative|num days
	    var stockId = document.getElementById("inputVO.stockId").value;
	    var ajaxParams = "inputVO.ajaxStr=" 
	        + currentTrade.tradeType + "|" + stockId + "|" + tradeAmt + "|"
	        + currentTrade.openDate + "|" + currentTrade.openPrice + "|"
	        + dateAtClose + "|" + closeAtClose + "|"
	        + profit + "|" + profitPercent + "|"
	        + maxPosProfit + "|" + maxNegProfit + "|"
	        + tradeLengthArray[numClosedPos - 1];
	    
	    makeAjaxCall(url, ajaxParams, fnAjaxRespone);
	
	    // indicate the close
	    currentTrade.tradeType = "";
	
	    // reset stop option - but keep the stop type; only when regular case, reset stopAmount
	    // currentTrade.stopType = "N";
	    // document.getElementsByName("ckStop")[0].checked = true;
	    if (currentTrade.stopType == "N" || currentTrade.stopType == "T") {
	        // keep the same set for next
	    } else if (currentTrade.stopType == "R") {
	        // for regular stop, change to none stop
	        currentTrade.stopType = "N";
	        currentTrade.stopAmount = 0;
	        document.getElementById("stopAmount").value = "";
	        document.getElementsByName("ckStop")[0].checked = true;
	    }
	    
	     
	    
	}
	
	function updatePositionBoard() {
	    // whenever endIndex is changed, update the board
	    if (currentTrade.tradeType != "") {
	    	var closeAtEnd = QQ_exambarchart.getCurrentClose();
	        var profitPercent = Math.round(10000* (closeAtEnd - currentTrade.openPrice) / currentTrade.openPrice) / 100;
	        var profit = Math.round(tradeAmt * profitPercent) / 100;
	        if (currentTrade.tradeType == "Short") {
	            profitPercent *= -1;
	            profit *= -1;
	        }
	
	        var theColor = "#ff0000";
	        if (profit >= 0) {
	            theColor = "#00ff00";
	        }
	
	        var positionElem = document.getElementById("positionBoard");
	        var html = "<table><tr><td>Trade: " + currentTrade.tradeType +
	            "</td></tr><tr><td>Date: " + currentTrade.openDate +
	            "</td></tr><tr><td>Price: " + currentTrade.openPrice +
	            "</td></tr><tr><td>Stop: " + formatNumber(currentTrade.stopPrice) +
	            "</td></tr><tr><td>Profit(%): <font color=\""
	            + theColor + "\">"   + profitPercent +
	            "</font></td></tr><tr><td>Profit($): <font color=\""
	            + theColor + "\">"  + profit +
	            "</font></td></tr><tr><td>Status: Open</td></tr></table>";
	
	        positionElem.innerHTML = html;
	
	    }
	}
	
	function updateTradeHistory() {
	    if (numClosedPos <= 0) {
	        return;
	    }

	    var performElem = document.getElementById("tradeHistory");
	    var runSumProfit = 0;
	    var runSumProfitPercent = 0;
	    var totalDays = 0;
	    var html = "<table class=\"gridtable\" width=\"100%\"><tr><td>Type</td><td>Open Date</td><td>Open Price</td>" +
	        "<td>Close Date</td><td>Close Price</td><td>Profit(%)</td><td>Profit($)</td><td>Num Days</td>" +
	        "<td>Max Pos(%)</td><td>Max Neg(%)</td></tr>";
	    for (var i = 0; i < numClosedPos; i++) {
	
	        // find profit
	        var profitPercent = Math.round(10000* (tradeClosePriceArray[i] - tradeOpenPriceArray[i]) / tradeOpenPriceArray[i]) / 100;
	        var profit = Math.round(tradeAmt * profitPercent) / 100;
	        if (tradeTypeArray[i] == "Short") {
	            profitPercent *= -1;
	            profit *= -1;
	        }
	
	        runSumProfit += profit;
	        runSumProfitPercent += profitPercent;
	        totalDays += tradeLengthArray[i];
	
	        var theColor = "#ff0000";
	        if (profit >= 0) {
	            theColor = "#00ff00";
	        }
	
	        html += "<tr><td>" + tradeTypeArray[i] +
	        "</td><td>" + tradeOpenDateArray[i] +
	        "</td><td>" + tradeOpenPriceArray[i] +
	        "</td><td>" + tradeCloseDateArray[i] +
	        "</td><td>" + tradeClosePriceArray[i] +
	        "</td><td><font color=\""
	        + theColor + "\">" + profitPercent +
	        "%</font></td><td><font color=\""
	        + theColor + "\">" +  + profit +
	        "</font></td><td>" + tradeLengthArray[i] +
	        "</td><td><font color=\"#00ff00\">" + tradeMaxPosArray[i] +
	        "%</font></td><td><font color=\"#ff0000\">" + tradeMaxNegArray[i] +
	        "%</font></td></tr>";
	
	    }
	
	    html += "</table>";
	
	    var theColor2 = "#ff0000";
	    if (runSumProfit >= 0) {
	        theColor2 = "#00ff00";
	    }
	
	    runSumProfitPercent = Math.round(runSumProfitPercent * 100) / 100;
	    runSumProfit = Math.round(runSumProfit * 100) / 100;
	
	    html += "<p align=\"left\">&nbsp;Total Profit(%): <font color=\"" + theColor2 + "\"><b>" + runSumProfitPercent + "%</b></font><br>";
	    html += "&nbsp;Total Profit($): <font color=\"" + theColor2 + "\"><b>" + runSumProfit + "</b></font><br>"
	    html += "&nbsp;Trade amount($): " + tradeAmt + "<br>";
	    html += "&nbsp;Total days in market: " + totalDays + "</p>";
	
	    performElem.innerHTML = html;
	
	}
	
	
	function formatNumber(inNumber) {
	    inNumber = Math.round(inNumber * 100) / 100;
	    var tmpNumStr = new String(inNumber);
	    
	    var index = tmpNumStr.indexOf(".");
	    if (index > 0 && tmpNumStr.length - index > 2) {
	        tmpNumStr = tmpNumStr.substring(0, index + 3);
	    }
	
	   return tmpNumStr;
	}
	
	// explicitly return public methods when this object is instantiated
	return {
		setFocus : setFocus,
		doSelfSelect : doSelfSelect,
		doAutoSelect : doAutoSelect,
		setPeriodBymousewheel : setPeriodBymousewheel,
		setPeriod : setPeriod,
		goByDay : goByDay,
		startOver: startOver,
		goByDayLoop : goByDayLoop,
		setOption : setOption,
		goByMonth : goByMonth,
		goLong : goLong,
		goShort : goShort,
		adjustButtons : adjustButtons,
		adjustPrice : adjustPrice,
		adjustStopFunc : adjustStopFunc,
		checkStopOptions : checkStopOptions,
		goCloseForIndex : goCloseForIndex,
		goClose : goClose,
		updatePositionBoard : updatePositionBoard,
		updateTradeHistory : updateTradeHistory
		
	};

})(window);
	
/* end */