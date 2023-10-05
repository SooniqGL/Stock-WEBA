/* 
 * Script to draw the dynamic chart
 * This is originally created for the analysis page, but
 * borrowed for the exam page.
 * 1/16/2011
 */

// exam_barchart.js

var X_LEFT = 45;
var X_RIGHT = 8;
var Y_TOP = 5;
var Y_BOTTOM = 8;
var VOLUME_HEIGHT = 40;    // for volume box
var GROWTH_HEIGHT = 30;    // for GROWTH box
var FORCE_HEIGHT = 30;     // for FORCE box
var RS_HEIGHT = 30;
var TITLE_HEIGHT = 20;   
var BOX_GAP = 8;
var SCALE_LENGTH = 3;      // bars in y axis
var BAR_WIDTH = 2;         // bar width in price chart
var PRICE_TOP = 8;         // draw above this height
var PRICE_BOTTOM = 20;       // draw above this height
var TEXT_HEIGHT = 15;
var VERTICAL_TEXT_X1 = 17;   // price
var SMALL_GAP = 5;

var colorBlack = "#000000";
var colorWhite = "#ffffff";
var colorRed = "#ff0000";
var colorGreen = "#00ff00";
var colorBlue = "#0000ff";
var colorDark = "#aaaaaa";
var colorYellow = "#ffff00";
var imageColor = "#ebebeb";
var chartColor = "#fffffa";

var doEMA20 = false;
var doEMA50 = false;
var doEMA100 = false;

var innerWidth = 0;
var innerHeight = 0;
var origX = 0;
var origY = 0;
var volumeY = 0;
var growthY = 0;
var forceY = 0;

var maxP = 0.0;
var minP = 0.0;
var maxPLog	= 0.0;
var minPLog = 0.0;
var maxVolume = 0.0;
var maxGrowth = 0.0;
var maxForce = 0.0;
var avgVolume = 0.0;

var numberOfItems = 0;  // for current display
var startIndex = 0;  // calculated by the period and endIndex
var endIndex = 0;  // moving if dragged
var rangeStartIndex = -1;  // initial -1
var rangeEndIndex = 0;

var xStep = 0.0;
var yStep = 0.0;
var volumeStep = 0.0;
var growthStep = 0.0;
var forceStep = 0.0;
var firstBarX = 0.0;     // position of the first bar - relatively to the chart

////////////////// functions /////////////////

// update the startIndex and endIndex
// moveStep - the movemoment, 0, 1, 5, etc.
function updateEndIndex(moveStep) {
//alert("update: " + moveStep);
        if (jg == null) {
            // do not do anything
		return;
	}

	// check if the data exists
	var MIN_DAYS = 63;
	if (dateArray == null || dateArray.length < MIN_DAYS) {
		return;  // do not draw
	}

	// figure out the start index and end index
	var form = document.examForm;
	var period = form.all["inputVO.period"].value;
	if (period == "3") {
		numberOfItems = 63;
	} else if (period == "6") {
		numberOfItems = 126;
	} else if (period == "12") {
		numberOfItems = 252;
	} else if (period == "24") {
		numberOfItems = 104;          // 52 x 2 weeks
	} else {
		// default is 3 months
		numberOfItems = 63;
	}

        // adjust, assume: moveStep >= 0
        endIndex += moveStep;

        if (endIndex > rangeEndIndex) {
            // end of data reached
        }

        // find the startIndex
        startIndex = endIndex - numberOfItems + 1;

        // reset if less than zero
        if (startIndex < 0) {
            startIndex = 0;
        }

}

// draw chart after data is prepared
function drawChart() {
//alert("draw it: " + xMove);
	if (jg == null) {
		return;
	}

	// make sure to clear it
	jg.clear();

	var form = document.examForm;

	// find what ema's
	if (form.doEMA20.checked == true) {
		doEMA20 = true;
	} else {
		doEMA20 = false;
	}

	if (form.doEMA50.checked == true) {
		doEMA50 = true;
	} else {
		doEMA50 = false;
	}

	if (form.doEMA100.checked == true) {
		doEMA100 = true;
	} else {
		doEMA100 = false;
	}

	if (dateArray == null || dateArray.length == 0) {
		// no enough data found
		jg.setColor(colorGreen);
		var stockId = form.stockId.value;
		jg.drawString("No chart for this stock with stock_id: " + stockId, 200, 290);
		return;
	}

	// find parameters / measures
	forceY 	= chartHeight - Y_BOTTOM - 2 * BOX_GAP;
	growthY = forceY - FORCE_HEIGHT - BOX_GAP;
	volumeY = growthY - GROWTH_HEIGHT - BOX_GAP;
	var gfHeight 	= FORCE_HEIGHT + GROWTH_HEIGHT + 4 * BOX_GAP;

	innerWidth  = chartWidth - X_LEFT - X_RIGHT;
	innerHeight = chartHeight - Y_TOP - TITLE_HEIGHT - BOX_GAP
				- VOLUME_HEIGHT - Y_BOTTOM - gfHeight;
	origX 		= X_LEFT;
	origY 		= volumeY - VOLUME_HEIGHT - BOX_GAP;

	// find max/min's
	findMaxMinForAll();

	// else, do draw stuff
	drawFrame();
	drawBars();
	drawAvgLabel();
	drawChartFramePost();

}

function drawFrame() {
//alert("drawFrame");
	jg.setColor(imageColor);
	jg.fillRect(0, 0, chartWidth, chartHeight);

	jg.setColor(chartColor);
	jg.fillRect(origX, volumeY - VOLUME_HEIGHT, innerWidth, VOLUME_HEIGHT);
	jg.fillRect(origX, growthY - GROWTH_HEIGHT, innerWidth, GROWTH_HEIGHT);
	jg.fillRect(origX, forceY  - FORCE_HEIGHT,  innerWidth, FORCE_HEIGHT);

	jg.setColor(chartColor);
	jg.fillRect(origX, origY - innerHeight, innerWidth, innerHeight);

	// labels
	jg.setColor(colorBlue);
	var halfV = 18;
	jg.drawString("Price", SMALL_GAP, origY - halfV);
	jg.drawString("Vol", SMALL_GAP, volumeY - halfV);
	jg.drawString("Grow", SMALL_GAP, growthY - halfV);
	jg.drawString("Force", SMALL_GAP,  forceY - halfV);


	//jg.setColor("#00ff00");
	//jg.fillEllipse(100, 200, 100, 180);
	//jg.setColor("maroon");
	//jg.drawPolyline(new Array(50, 10, 120), new Array(10, 50, 70));

	//jg.drawRect(0, 0, 680, 400);

	jg.paint();


	// dd.elements.chart.resizeTo(300, 272);         // resize draggable first DIV to extent of oval+line

}

function drawBars() {
	if (maxP == minP) {
		maxP = minP + 1;
	}

	minPLog = Math.log(minP);
	maxPLog	= Math.log(maxP);
	yStep 		= (innerHeight - PRICE_BOTTOM - PRICE_TOP) / (maxPLog - minPLog);
	volumeStep 	= VOLUME_HEIGHT / maxVolume;
	growthStep 	= GROWTH_HEIGHT / ( 2 * maxGrowth);
	forceStep 	= FORCE_HEIGHT / (2 * maxForce);
	var zeroGrowthHt = growthY - 0.5 * GROWTH_HEIGHT;
	var zeroForceHt  = forceY - 0.5 * FORCE_HEIGHT;

	var xPolylineGrowth  = new Array();
	var yPolylineGrowth  = new Array();
	var xPolylineForce 	= new Array();
	var yPolylineForce 	= new Array();
	var xPolylineAvg100     = new Array();
	var yPolylineAvg100 	= new Array();
	var xPolylineAvg50      = new Array();
	var yPolylineAvg50 	= new Array();
	var xPolylineAvg20  	= new Array();
	var yPolylineAvg20 	= new Array();

	drawPriceScaleList();


	// make this global
	var marginGap = SMALL_GAP + SMALL_GAP;
	xStep   = (innerWidth - 2 * marginGap) / (numberOfItems - 1);
	firstBarX = origX + marginGap;

	var xx = firstBarX;
	var yyOpen = 0;
	var yyClose = 0;
	var yyMin = 0;
	var yyMax = 0;

	var totalPointsAvg100 = 0;
	var totalPointsAvg50 = 0;
	var totalPointsAvg20 = 0;

	var volumeHt = 0;

//	alert("start/end: " + startIndex + "/" + endIndex + ", xStep: " + xStep);
        var chartBarBase = origY - PRICE_BOTTOM + yStep *  minPLog;
	for (var i = startIndex; i <= endIndex; i ++, xx += xStep) {
            // open/close/min/max are already Math.log()'ed ... to save some calculaton
		yyOpen 	= Math.round(chartBarBase - yStep * openLogArray[i]) ;
		yyClose = Math.round(chartBarBase - yStep * closeLogArray[i]);
		yyMin 	= Math.round(chartBarBase - yStep * minLogArray[i]);
		yyMax 	= Math.round(chartBarBase - yStep * maxLogArray[i]);
		var roundXX = Math.round(xx);

		if (yyOpen >= yyClose) {
			jg.setColor(colorGreen);
		} else {
			jg.setColor(colorRed);
		}

		// draw volume
		volumeHt =  Math.round(volumeArray[i] * volumeStep);
		jg.drawLine(roundXX, volumeY, roundXX, volumeY - volumeHt);

		// draw bar chart
		jg.drawLine(roundXX, yyMin, roundXX, yyMax);
		jg.drawLine(roundXX, yyOpen, roundXX - BAR_WIDTH, yyOpen);
		jg.drawLine(roundXX, yyClose, roundXX + BAR_WIDTH, yyClose);


		// draw the growth rate
		var growthHt =  Math.round(growthArray[i] * growthStep);

		/*
		if ((daily.getSlope() >= 0 && daily.getAcceleration() <= 0)  ||
			(daily.getSlope() <= 0 && daily.getAcceleration() >= 0)) {
			pad.setColor(colorBlue);
			pad.fillOval((int)xx - 2, (int) (growthY + 2), 4, 4);
			//pad.drawRect((int)xx, (int) (growthY - growthHt), (int) xStep + 1, (int) (growthHt - zeroGrowthHt));
		}  */

		// growth line
		if (growthArray[i] > 0) {
			jg.setColor(colorGreen);
		} else {
			jg.setColor(colorRed);
		}

		jg.drawLine(roundXX, zeroGrowthHt, roundXX, zeroGrowthHt - growthHt);


		// force line
		var forceHt =  Math.round(forceArray[i] * forceStep);
		if (forceArray[i] > 0) {
			jg.setColor(colorGreen);
		} else {
			jg.setColor(colorRed);
		}

		jg.drawLine(roundXX, zeroForceHt, roundXX, zeroForceHt - forceHt);

		// lines
		xPolylineGrowth[i - startIndex]  = roundXX;
		yPolylineGrowth[i - startIndex]  = zeroGrowthHt - growthHt;
		xPolylineForce[i - startIndex]   = roundXX;
		yPolylineForce[i - startIndex] 	 = zeroForceHt - forceHt;

		if (doEMA100) {
			if (logAvg100Array[i] <= maxPLog && logAvg100Array[i] >= minPLog) {
				xPolylineAvg100[totalPointsAvg100] = roundXX;
				yPolylineAvg100[totalPointsAvg100] = Math.round(chartBarBase - yStep * logAvg100Array[i]);
				totalPointsAvg100++;
			}
		}

		if (doEMA50) {
			if (logAvg50Array[i] <= maxPLog && logAvg50Array[i] >= minPLog) {
				xPolylineAvg50[totalPointsAvg50] = roundXX;
				yPolylineAvg50[totalPointsAvg50] = Math.round(chartBarBase - yStep * logAvg50Array[i]);
				totalPointsAvg50++;
			}
		}

		if (doEMA20) {
			if (logAvg20Array[i] <= maxPLog && logAvg20Array[i] >= minPLog) {
				xPolylineAvg20[totalPointsAvg20] = roundXX;
				yPolylineAvg20[totalPointsAvg20] = Math.round(chartBarBase - yStep * logAvg20Array[i]);
				totalPointsAvg20++;
			}
		}

		// draw the trend/market
		var trendHt = forceY + BOX_GAP;
		//var marketTrendHt = trendHt + BOX_GAP;
		if (trendColorArray[i] != null && trendColorArray[i] == "G") {
			// draw green
			jg.setColor(colorGreen);
			jg.fillOval(roundXX - 2, trendHt - 2, 4, 4);
		} else if (trendColorArray[i] != null && trendColorArray[i] == "R") {
			// do RED
			jg.setColor(colorRed);
			jg.fillOval(roundXX - 2, trendHt - 2, 4, 4);

		} else {
			// do nothing
			//pad.setColor(colorBlue);
			//pad.drawLine((int) xx - 1, (int) ballHt, (int) xx + 1, (int) ballHt);
		}

		// draw market flag list
                /*
		if (marketTrendArray != null && marketTrendArray.length > (endIndex - startIndex)) {
			var marketFlag = marketTrendArray[marketTrendArray.length - dateArray.length + i];
			if (marketFlag == "G") {
				// draw green
				jg.setColor(colorGreen);
				jg.fillOval(roundXX - 2, marketTrendHt - 2, 4, 4);
			} else if (marketFlag == "R") {
				// do RED
				jg.setColor(colorRed);
				jg.fillOval(roundXX - 2, marketTrendHt - 2, 4, 4);
			}  else if (marketFlag == "Y") {
				// do RED
				jg.setColor(colorYellow);
				jg.fillOval(roundXX - 2, marketTrendHt - 2, 4, 4);
			} else {
				// do nothing
				//pad.setColor(colorBlue);
				//pad.drawLine((int) xx - 1, (int) marketBallHt, (int) xx + 1, (int) marketBallHt);
			}
		} */

		// draw month
		if (monthArray[i] != "") {
			jg.setColor(colorDark);
			jg.drawLine(roundXX, origY, roundXX, origY - SCALE_LENGTH);

			jg.setColor(colorBlue);
			var monthStr = getShortMonthString(monthArray[i]);
			jg.drawString(monthStr, roundXX - 2 * SMALL_GAP, origY - TEXT_HEIGHT - SMALL_GAP);
		}

	}

	// average volume
	jg.setColor(colorDark);
	volumeHt =  Math.round(avgVolume * volumeStep);
	var volumeRealHt = volumeY - volumeHt;
	jg.drawLine(origX, volumeRealHt, chartWidth - X_RIGHT, volumeRealHt);

	// draw volume: max/avg string here ????...

	// draw 0 growth line / force line
	jg.setColor(colorDark);
	jg.drawLine(origX, zeroGrowthHt, chartWidth - X_RIGHT, zeroGrowthHt);
	jg.drawLine(origX, zeroForceHt, chartWidth - X_RIGHT, zeroForceHt);

	jg.setColor(colorDark);
	jg.drawPolyline(xPolylineGrowth, yPolylineGrowth);
	jg.drawPolyline(xPolylineForce, yPolylineForce);

	// stroke ---????
	// draw 50-day line
	jg.setStroke(2);
	if (doEMA50) {
		jg.setColor(colorBlue);
		jg.drawPolyline(xPolylineAvg50, yPolylineAvg50);
	}

	if (doEMA20) {
		jg.setColor(colorGreen);
		jg.drawPolyline(xPolylineAvg20, yPolylineAvg20);
	}

	if (doEMA100) {
		// draw 100-day line
		jg.setColor(colorRed);
		jg.drawPolyline(xPolylineAvg100, yPolylineAvg100);
	}

	jg.setStroke(1);

	// title
        avgVolume = Math.round(avgVolume);
	jg.setColor(colorBlue);
	jg.drawString("Last Date: " + dateArray[endIndex], origX, Y_TOP + SMALL_GAP);
        jg.drawString("Price: " + closeArray[endIndex], origX + 175, Y_TOP + SMALL_GAP);
        jg.drawString("Volume: " + volumeArray[endIndex], origX + 280, Y_TOP + SMALL_GAP);
        jg.drawString("Avg Volume: " + avgVolume, origX + 420, Y_TOP + SMALL_GAP);
}

function drawAvgLabel() {
}

function drawMonthLabel() {
}

function drawChartFramePost() {
	jg.setColor(colorBlack);
	jg.drawRect(origX, volumeY - VOLUME_HEIGHT, innerWidth, VOLUME_HEIGHT);
	jg.drawRect(origX, origY - innerHeight, innerWidth, innerHeight);

	// draw option if there is setting
	jg.drawRect(origX, growthY - GROWTH_HEIGHT, innerWidth, GROWTH_HEIGHT);
	jg.drawRect(origX, forceY  - FORCE_HEIGHT,  innerWidth, FORCE_HEIGHT);
	jg.paint();
}

// This is called to prepare the data
// either for daily or weekly
function prepareData() {
//alert("prepare data");
	if (jg == null) {
		// no container - so not to prepare
		return;
	}

	var form = document.examForm;

	var dataListStr = "";   // daily or weekly
	//var marketListStr = "";
	var period = form.all["inputVO.period"].value;
	if (period == "3" || period == "6" || period == "12") {
		if (dataType == "Daily") {
			// do not need to redo
			return;
		}

		dataType = "Daily";
		dataListStr  = form.dailyDataStr.value;
		//marketListStr = form.marketFlagList.value;
	} else if (period == "24") {
		// 24 month -> display weekly chart
		if (dataType == "Weekly") {
			// do not need to redo
			return;
		}

		dataType = "Weekly";
		dataListStr  = form.weeklyDataStr.value;
		//marketListStr = form.marketWeeklyFlagList.value;
	} else {
		if (dataType == "Daily") {
			// do not need to redo
			return;
		}

		dataType = "Daily";
		dataListStr  = form.dailyDataStr.value;
		//marketListStr = form.marketFlagList.value;
	}

        // check if no data
        if (dataListStr == null || dataListStr == "") {
            jg = null;  // so other functions will be skipped by checking this object
            return;
        }

	var dataArray = mySplit(dataListStr, "#");
	if (dataArray == null || dataArray.length < 5) {
		alert("Content error: " + dataListStr);
		// do something to let the draw to know to not draw
		return;
	}

	// initialize the size and index
	dataSize = dataArray.length;
	rangeEndIndex = dataSize - 1;

        // determine where to start - rangeEndIndex
        var startDate = form.startDate.value;

	// make sure the arrays are reset
	resetDataArrays();

	// split the data
	var prevMonth = "";
	for (var i = 0; i < dataArray.length; i ++) {
		var nodeInfo = mySplit(dataArray[i], "|");
		// format: date | open | close | min | max | logAvg9 | logAvg21 | logAvg50
		// | volume | growth | force | trendColor
		if (nodeInfo == null || nodeInfo.length != 12) {
			// error - need to notify the draw
			alert("format error: " + dataArray[i]);
			return;
		}

		dateArray[i] 		= nodeInfo[0];
		openLogArray[i] 	= Math.log(parseFloat(nodeInfo[1]));
		closeArray[i] 		= parseFloat(nodeInfo[2]);   // keep non-log just for close price
                closeLogArray[i] 	= Math.log(closeArray[i]);
                minArray[i] 		= parseFloat(nodeInfo[3]);
		minLogArray[i] 		= Math.log(minArray[i]);
                maxArray[i] 		= parseFloat(nodeInfo[4]);
		maxLogArray[i] 		= Math.log(maxArray[i]);
		logAvg20Array[i] 	= parseFloat(nodeInfo[5]);
		logAvg50Array[i] 	= parseFloat(nodeInfo[6]);
		logAvg100Array[i] 	= parseFloat(nodeInfo[7]);
		volumeArray[i]		= parseFloat(nodeInfo[8]);   // this is float too
		growthArray[i]		= parseFloat(nodeInfo[9]);
		forceArray[i]		= parseFloat(nodeInfo[10]);
		trendColorArray[i] 	= nodeInfo[11];

		var currentMonth = getMonthNum(nodeInfo[0]);
		if (currentMonth != prevMonth) {
			monthArray[i] = currentMonth;
		} else {
			monthArray[i] = "";
		}
		prevMonth = currentMonth;

                // check the date - define the first one which passes or reach the startDate
                if (rangeStartIndex == -1 && startDate <= nodeInfo[0]) {
                    rangeStartIndex = i;
                }
	}

        // the place to begin
        endIndex = rangeStartIndex;

        /*
	marketTrendArray = mySplit(marketListStr, "|");
	if (marketTrendArray == null || marketTrendArray.length < 5) {
		alert("Market content error: " + marketListStr);
		// do something to let the draw to know to not draw
		return;
	} */

	// now, data is ready in all the arrays
	//alert("prepare data - done");
}

function getMonthNum(dateStr) {
	var items = mySplit(dateStr, "/");
	if (items != null && items.length == 3) {
		return items[1];
	} else {
		return "";
	}
}

//var maxP, minP, maxVolume, maxGrowth, maxForce
function findMaxMinForAll() {
	var totalVolume = 0.0;
	for (var i = startIndex; i <= endIndex; i ++) {
		totalVolume += volumeArray[i];
		if (i == startIndex) {
			maxP = maxArray[i];
			minP = minArray[i];
			maxVolume = volumeArray[i];
			maxGrowth = growthArray[i];
			maxForce = forceArray[i];
		} else {
			if (maxArray[i] > maxP) {
				maxP = maxArray[i];
			}

			if (minArray[i] < minP) {
				minP = minArray[i];
			}

			if (volumeArray[i] > maxVolume) {
				maxVolume = volumeArray[i];
			}

			var temp = growthArray[i];
			if (temp < 0) {
				temp = - temp;
			}

			if (temp > maxGrowth) {
				maxGrowth = temp;
			}

			temp = forceArray[i];
			if (temp < 0) {
				temp = - temp;
			}

			if (temp > maxForce) {
				maxForce = temp;
			}
		}
	}

	if (endIndex - startIndex >= 0) {
		avgVolume = totalVolume / (endIndex - startIndex + 1);
	} else {
		avgVolume = 0;
	}
}

function drawPriceScaleList() {
	var scaleList = getPriceRulerList(minP, maxP);
//alert("drawPriceScaleList()");
	var lastP = "" + scaleList[scaleList.length - 1];
	var leftLen = (lastP.length + 2 ) * (SMALL_GAP + 1);
	// alert("lastP: " + lastP + ", length: " + leftLen);
	if (scaleList.length > 1) {
		var prevP = "" + scaleList[scaleList.length - 2];
		var prevLen = (prevP.length + 2 ) * (SMALL_GAP + 1);
		if (prevLen > leftLen) {
			leftLen = prevLen;
		}
	}

	for (var i = 0; i < scaleList.length; i ++) {
		var runP = scaleList[i];
		var displayHt = Math.round(origY - PRICE_BOTTOM - yStep * (Math.log(runP) - minPLog)) ;

		// draw a line
		jg.setColor(colorDark);
		jg.drawLine(origX, displayHt, chartWidth - X_RIGHT, displayHt);

		// draw text - do not display if it is out of box
		if (displayHt - SMALL_GAP - TEXT_HEIGHT >= origY - innerHeight) {
			jg.setColor(colorRed);
			jg.drawString(runP, origX - leftLen, displayHt - 3 * SMALL_GAP);
		}

	}

	jg.paint();
}

function getPriceRulerList(minP, maxP) {
	var list = new Array();
//alert("getPriceRulerList()");
	// System.out.println("min/max: " + minP + "/" + maxP);
	var range = maxP - minP;
	if (range == 0) {
		range = 1;
	}

	var dispStep = 0;
	var initP = 0;

	if (range < 1) {
		dispStep = 0.1;
	} else if (range < 5) {
		dispStep = 0.5;
	} else if (range < 10) {
		dispStep = 1;
	} else if (range < 20) {
		dispStep = 2;
	} else if (range < 30) {
		dispStep = 3;
	} else if (range < 50) {
		dispStep = 5;
	} else if (range < 120) {
		dispStep = 10;
	} else if (range < 500) {
		dispStep = 50;
	} else if (range < 1000) {
		dispStep = 100;
	} else {
		dispStep = Math.ceil(range * 0.001) * 100;
	}

	if (minP < 1) {
		initP = Math.round(minP * 100 - 20) / 100;
	} else if (minP < 30) {
		initP = Math.round(minP);
	} else if (minP < 100) {
		initP = Math.round(minP / 5 - 1) * 5;
	} else if (minP < 500) {
		initP = Math.round(minP / 50 - 1) * 50;
	} else if (minP < 1000) {
		initP = Math.round(minP / 100 - 1) * 100;
	}  else {
		initP =  Math.round(minP / 200 - 1) * 200;
	}

	var runP = initP;
	var i = 0;
	do {
		if (runP >= minP) {
                    list[i] = runP;
                    i++;
		}

		runP += dispStep;
                runP = Math.round(runP * 100) / 100;   // strang error when 7.199999999999 is displayed
	} while (runP < maxP);

	//alert("getPriceRulerList() - before return");

	return list;
}


function getShortMonthString(monthNum) {
// alert("getmonth: " + monthNum);
	var monthStr = "";
	if (monthNum != null) {
		//if (monthNum.startsWith("0")) {   // syntex error
		//	monthNum = monthNum.substring(1);
		//}

		if (monthNum == "01") {
			monthStr = "Jan";
		} else if (monthNum == "02") {
			monthStr = "Feb";
		} else if (monthNum == "03") {
			monthStr = "Mar";
		} else if (monthNum == "04") {
			monthStr = "Apr";
		} else if (monthNum == "05") {
			monthStr = "May";
		} else if (monthNum == "06") {
			monthStr = "Jun";
		} else if (monthNum == "07") {
			monthStr = "Jul";
		}  else if (monthNum == "08") {
			monthStr = "Aug";
		} else if (monthNum == "09") {
			monthStr = "Sep";
		} else if (monthNum == "10") {
			monthStr = "Oct";
		}  else if (monthNum == "11") {
			monthStr = "Nov";
		} else if (monthNum == "12") {
			monthStr = "Dec";
		}
	}

	return monthStr;
}



/* The End */

