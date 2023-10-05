// draw_math.js

var colorBlack = "#000000";
var colorWhite = "#ffffff";
var colorRed = "#ff0000";
var colorGreen = "#00ff00";
var colorBlue = "#0000ff";
var colorDark = "#aaaaaa";
var colorYellow = "#ffff00";
var imageColor = "#cccccc";
var chartColor = "#fffff0";

var BAR_WIDTH = 2;         // bar width in price chart
var BAR_HEIGHT = 5;         // daily bar at bottom
var BOTTOM_SPACE = 0;
var SMALL_GAP = 5;
var X_LEFT = 30;

var MAX_PRICE = 60;
var MIN_PRICE = 20;
var MAX_PRICE_CHANGE = 5;
var MIN_PRICE_CHANGE = 1;

var maxPLog = Math.log(MAX_PRICE);
var minPLog = Math.log(MIN_PRICE);

var totalDays = 120;
var xStep = 0;
var yStep = 0;
var xPos = 0;
var currentPrice = 0;
var origX = X_LEFT;
var origY = 0;

var halfX = 0;
var halfY = 0;
	
function startIt(drawTitle) {
	if (jg == null) {
		return;
	}
	
	// initialize
	xStep = (chartWidth - X_LEFT) / totalDays;
	yStep = (chartHeight - BOTTOM_SPACE) / (maxPLog - minPLog)
	origY = chartHeight - BOTTOM_SPACE;
	halfX = Math.round(chartWidth / 2);
	halfY = Math.round(chartHeight / 2);

	if (drawTitle == true) {
		// make sure to clear it
		jg.clear();
	
		jg.setColor(colorBlue);
		jg.drawRect(10, 10, chartWidth - 20, chartHeight);
		jg.drawString("This is Chart for Stock ABC (Random Walk)", halfX - 100, halfY);
		
	}
	
	jg.paint();
	
	// Setup positions
	xPos = 0;
	currentPrice = 0.4 * (MAX_PRICE - MIN_PRICE) + MIN_PRICE;   // set to the middle
	
	if (drawTitle == true) {
		setTimeout("drawChart('true')", 2000);
	} else {
		drawChart('true');
	}
	
}

function drawChart(doClean) {
	if (doClean == "true") {
		jg.clear();
	
		drawPriceScaleList();
		jg.setColor(colorBlue);
		jg.drawRect(0, 0, chartWidth, chartHeight);
         
		
	}
	
        jg.drawLine( 10, 10, 50, 100);
        
	if (xPos < 0 || xPos >= totalDays) {
		// restart it
		startIt(false);
		return;
	}
	
	// random produce the price on range
	var aRandom = Math.random() - 0.5;
	var bRandom = Math.random() - 0.5;
	var cRandom = Math.random() - 0.5;
	var dRandom = Math.random() - 0.5;
	var eRandom = Math.random();
	
	var maxP = 0.0;
	var minP = 0.0;
	var openP = 0.0;
	var closeP = 0.0;   // next CurrentPrice
	var avgP = 0.0;
	var xx = X_LEFT + Math.round(xPos * xStep);
	
	// GO TO NEXT POSITION
	xPos ++;  // between 1 and totalDays
	
	if (aRandom > bRandom) {
		maxP = currentPrice + aRandom * MAX_PRICE_CHANGE;
		minP = currentPrice + bRandom * MAX_PRICE_CHANGE;
	} else {
		minP = currentPrice + aRandom * MAX_PRICE_CHANGE;
		maxP = currentPrice + bRandom * MAX_PRICE_CHANGE;
	}
	
	// make the adjust randomly
	if (maxP - minP < MIN_PRICE_CHANGE) {
		if (eRandom >= 0.5) {
			maxP = minP + MIN_PRICE_CHANGE;
		} else {
			minP = maxP - MIN_PRICE_CHANGE;
		}
	}
	
	if (minP < MIN_PRICE) {
		maxP += 2 * (MIN_PRICE - minP);  // bug if change minP later
		minP += 2 * (MIN_PRICE - minP);
	}
	
	if (maxP > MAX_PRICE) {
		minP -= 2 * (maxP - MAX_PRICE);
		maxP -= 2 * (maxP - MAX_PRICE);
	}
	
	// open and close may have different orders
	avgP = (maxP + minP) / 2;
	openP  = avgP + (maxP - minP) * cRandom;
	closeP = avgP + (maxP - minP) * dRandom;
	
	// draw the bars
	if (openP > closeP) {
		jg.setColor(colorRed);
	} else {
		jg.setColor(colorGreen);
	}
	
	jg.drawLine( xx, origY - Math.round((Math.log(minP) - minPLog) * yStep), xx, origY - Math.round((Math.log(maxP) - minPLog) * yStep));
	
	var openPos = origY - Math.round((Math.log(openP) - minPLog) * yStep);
	jg.drawLine( xx - BAR_WIDTH, openPos, xx, openPos);
	
	var closePos = origY - Math.round((Math.log(closeP) - minPLog) * yStep);
	jg.drawLine( xx, closePos, xx +  BAR_WIDTH, closePos);
	
	// paint
	jg.paint();
	
	// update the current price
	currentPrice = avgP;
	
	if (runFlag == true) {
		setTimeout("drawChart('false')", 200);
	}
	
}

function drawPriceScaleList() {
	var scaleList = getPriceRulerList(MIN_PRICE, MAX_PRICE);
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
		var displayHt = Math.round(origY - yStep * (Math.log(runP) - minPLog)) ;
			
		// draw a line
		jg.setColor(colorDark);
		jg.drawLine(0, displayHt, chartWidth, displayHt);
		
		// draw text - do not display if it is out of box
		if (displayHt - SMALL_GAP >= origY - chartHeight) {
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
		dispStep = 200;
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
		}
		
		runP += dispStep;
		i++;
	} while (runP < maxP);
	
	//alert("getPriceRulerList() - before return");
	
	return list;
}

/* The End */