/* 
 * Scripts to help the exam page ...
 * 1/16/2011
 */

// exam_helper.js

function setFocus() {
    var form = document.examForm;
    form.all["inputVO.ticker"].select();
    form.all["inputVO.ticker"].focus();
}

function doSelfSelect() {
    //alert("here 1 ...");

    var form = document.examForm;
    if (form.all["inputVO.ticker"].value == "") {
        alert("Enter ticker ...");
        return;
    }

    form.all["inputVO.mode"].value = "S";
    form.submit();
   
}

function doAutoSelect() {
    var form = document.examForm;
    form.all["inputVO.mode"].value = "A";
    form.submit();
}

// do not need to submit
function setPeriod(period) {
        var form = document.examForm;
        form.all["inputVO.period"].value = period;
        if (form.all["inputVO.ticker"].value != "") {
                drawChart();
        } 
}

function setOption() {
        var form = document.examForm;

        // do not need to submit, but just redraw if has data already
        if (form.all["inputVO.ticker"].value != "") {
                drawChart();
        } 
}

// move the chart to the right by number of days
function goByDay( numDay ) {
	var form = document.examForm;
	var dayInt = parseInt(numDay, 10);

        // if reach the end, do not move
        if (endIndex + dayInt > rangeEndIndex) {
            return;
        }

        if (form.all["inputVO.ticker"].value != "") {
            // update the index and redraw the chart
            updateEndIndex(dayInt);
            drawChart();
            adjustButtons();
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
        if (endIndex + dayInt > rangeEndIndex) {
            return;
        } else {
            setTimeout("goByDayLoop('" + numDay + "', 'false')", 200);
        }
    }


}


function goByMonth( numMonth ) {
	var form = document.examForm;
	var numberMonth = parseInt(numMonth, 10);
        var dayInt = numberMonth * 22;

        // if reach the end, do not move
        if (endIndex + dayInt > rangeEndIndex) {
            return;
        }

        if (form.all["inputVO.ticker"].value != "") {
            // update indexes, draw
            updateEndIndex(dayInt);
            drawChart();
            adjustButtons();
        } 

}


function adjustButtons() {
	var form = document.examForm;

	var oneDButton = form.button_right_1d;
	if (oneDButton == null) {
		// now stock is loaded
		return;
	}

	// do day: -1, -5, -10, 1, 5, 10
	if (rangeEndIndex < endIndex + 1) {
		form.button_right_1d.disabled = true;
                form.button_right_xd.disabled = true;
	} else {
		form.button_right_1d.disabled = false;
                form.button_right_xd.disabled = false;
	}

	if (rangeEndIndex < endIndex + 5) {
		form.button_right_5d.disabled = true;
                form.button_right_xxd.disabled = true;
	} else {
		form.button_right_5d.disabled = false;
                form.button_right_xxd.disabled = false;
	}

	if (rangeEndIndex < endIndex + 10) {
		form.button_right_10d.disabled = true;
	} else {
		form.button_right_10d.disabled = false;
	}

	// month: -1, -6, -12, 1, 6, 12 * 22
	if (rangeEndIndex < endIndex + 22) {
		form.button_right_1m.disabled = true;
	} else {
		form.button_right_1m.disabled = false;
	}

        adjustPrice();
        updatePositionBoard();

//	alert("adjust button 1" );
}

// closeArray[endIndex] is the current price
function adjustPrice() {
    //alert("flip M");
    //var form = document.examForm;
    var priceElem = document.getElementById("priceBoard");
    priceElem.innerHTML = "Price: " + closeArray[endIndex];
}

// right at the endIndex time, go long
function goLong() {
    currentOpenDate  = dateArray[endIndex];
    currentOpenPrice = closeArray[endIndex];
    currentTradeType = "Long";
    openIndex = endIndex;
    
    // disable the long and short bottons
    var form = document.examForm;
    form.button_long.disabled  = true;
    form.button_short.disabled = true;
    form.button_close.disabled = false;

    // update position board
    var positionElem = document.getElementById("positionBoard");
    var html = "<table><tr><td>Trade:</td><td>" + currentTradeType +
        "</td></tr><tr><td>Date: </td><td>" + currentOpenDate +
        "</td></tr><tr><td>Price:</td><td>" + currentOpenPrice +
        "</td></tr><tr><td>Profit:</td><td>"  +
        "0.0</td></tr></table>";

    positionElem.innerHTML = html;
}

function goShort() {
    currentOpenDate  = dateArray[endIndex];
    currentOpenPrice = closeArray[endIndex];
    currentTradeType = "Short";
    openIndex = endIndex;

    // disable the long and short bottons
    var form = document.examForm;
    form.button_long.disabled  = true;
    form.button_short.disabled = true;
    form.button_close.disabled = false;

    // update position board
    var positionElem = document.getElementById("positionBoard");
    var html = "<table><tr><td>Trade: " + currentTradeType +
        "</td></tr><tr><td>Open Date: </td></tr><tr><td>" + currentOpenDate +
        "</td></tr><tr><td>Open Price:</td></tr><tr><td>" + currentOpenPrice +
        "</td></tr><tr><td>Profit:</td></tr><tr><td>"  +
        "0.0</td></tr><tr><td>Status: Open</td></tr></table>";

    positionElem.innerHTML = html;
}

function goClose() {
    // record to array
    tradeOpenDateArray[numClosedPos]   = currentOpenDate;
    tradeOpenPriceArray[numClosedPos]  = currentOpenPrice;
    tradeCloseDateArray[numClosedPos]  = dateArray[endIndex];
    tradeClosePriceArray[numClosedPos] = closeArray[endIndex];
    tradeTypeArray[numClosedPos]       = currentTradeType;
    tradeLengthArray[numClosedPos]     = endIndex - openIndex;

    // check the max pos and loss
    var maxPosProfit = 0;
    var maxNegProfit = 0;
    var locProfit = 0;

    for (var i = openIndex + 1; i <= endIndex; i ++) {
        locProfit = Math.round(10000* (closeArray[i] - closeArray[openIndex]) / closeArray[openIndex]) / 100;
        if (currentTradeType == "Short") {
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
    var form = document.examForm;
    form.button_long.disabled  = false;
    form.button_short.disabled = false;
    form.button_close.disabled = true;

    // update position board
    var profitPercent = Math.round(10000* (closeArray[endIndex] - currentOpenPrice) / currentOpenPrice) / 100;
    var profit = Math.round(tradeAmt * profitPercent) / 100;
    if (currentTradeType == "Short") {
        profitPercent *= -1;
        profit *= -1;
    }

    var theColor = "#ff0000";
    if (profit >= 0) {
        theColor = "#00ff00";
    }
    var positionElem = document.getElementById("positionBoard");
    var html = "<table><tr><td>Trade: " + currentTradeType +
        "</td></tr><tr><td>Open Date: </td></tr><tr><td>" + currentOpenDate +
        "</td></tr><tr><td>Open Price:</td></tr><tr><td>" + currentOpenPrice +
        "</td></tr><tr><td>Close Date: </td></tr><tr><td>" + dateArray[endIndex] +
        "</td></tr><tr><td>Close Price:</td></tr><tr><td>" + closeArray[endIndex] +
        "</td></tr><tr><td>Profit(%):</td></tr><tr><td><font color=\""
        + theColor + "\">" + profitPercent +
        "</font></td></tr><tr><td>Profit($):</td></tr><tr><td><font color=\""
        + theColor + "\">" +  + profit +
        "</font></td></tr><tr><td>Status: Closed</td></tr></table>";

    positionElem.innerHTML = html;

    // update the history list
    updateTradeHistory();

    // indicate the close
    currentTradeType = "";

    // update the performance history
}

function updatePositionBoard() {
    // whenever endIndex is changed, update the board
    if (currentTradeType != "") {
        var profitPercent = Math.round(10000* (closeArray[endIndex] - currentOpenPrice) / currentOpenPrice) / 100;
        var profit = Math.round(tradeAmt * profitPercent) / 100;
        if (currentTradeType == "Short") {
            profitPercent *= -1;
            profit *= -1;
        }

        var theColor = "#ff0000";
        if (profit >= 0) {
            theColor = "#00ff00";
        }

        var positionElem = document.getElementById("positionBoard");
        var html = "<table><tr><td>Trade: " + currentTradeType +
            "</td></tr><tr><td>Open Date: </td></tr><tr><td>" + currentOpenDate +
            "</td></tr><tr><td>Open Price:</td></tr><tr><td>" + currentOpenPrice +
            "</td></tr><tr><td>Profit(%):</td></tr><tr><td><font color=\""
            + theColor + "\">"   + profitPercent +
            "</font></td></tr><tr><td>Profit($):</td></tr><tr><td><font color=\""
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
    var html = "<table border=\"1\" width=\"100%\"><tr><td>Type</td><td>Open Date</td><td>Open Price</td>" +
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

    html += "&nbsp;Total Profit(%): <font color=\"" + theColor2 + "\"><b>" + runSumProfitPercent + "%</b></font><br>";
    html += "&nbsp;Total Profit($): <font color=\"" + theColor2 + "\"><b>" + runSumProfit + "</b></font><br>"
    html += "&nbsp;Trade amount($): " + tradeAmt + "<br>";
    html += "&nbsp;Total days in market: " + totalDays;

    performElem.innerHTML = html;

}

/* end */