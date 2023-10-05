/* 
 * Script to draw the dynamic chart
 * This is originally created for the analysis page, but
 * borrowed for the exam page.
 * 1/16/2011
 */

// exam_barchart.js
var QQ_exambarchart = (function(window, undefined) {

	var X_LEFT = 25;
	var X_RIGHT = 8;
	var Y_TOP = 5;
	var Y_BOTTOM = 8;
	var VOLUME_HEIGHT = 30; // for volume box
	var GROWTH_HEIGHT = 30; // for GROWTH box
	var FORCE_HEIGHT = 30; // for FORCE box
	var RS_HEIGHT = 30;
	var TITLE_HEIGHT = 20;
	var BOX_GAP = 8;
	var SCALE_LENGTH = 3; // bars in y axis
	var BAR_WIDTH = 2; // bar width in price chart
	var PRICE_TOP = 8; // draw above this height
	var PRICE_BOTTOM = 20; // draw above this height
	var TEXT_HEIGHT = 15;
	var VERTICAL_TEXT_X1 = 17; // price
	var SMALL_GAP = 5;
	var CHART_LEFT_INNER = 25;
	var CHART_RIGHT_INNER = 5;

	// colors
	var colorBlack = "#000000";
	var colorWhite = "#ffffff";
	var colorRed = "#ff0000";
	var colorGreen = "#00ff00";
	var colorBlue = "#0000ff";
	var colorDark = "#aaaaaa";
	var colorLightDark = "#dddddd";
	var colorYellow = "#ffff00";
	var colorPink = "#ff00ff";
	var colorDarkGreen = "#335500";
	var imageColor = "#dfdfdf";
	var chartColor = "#ffffff";

	var doEMA20 = false;
	var doEMA50 = false;
	var doEMA100 = false;
	var doBARS = false;
	var doZone = false;
	var doReverse = false;
	var doTradePoint = false;

	var innerWidth = 0;
	var innerHeight = 0;
	var origX = 0;
	var origY = 0;
	var volumeY = 0;
	var growthY = 0;
	var forceY = 0;
	var chartCenter2Y = 0; // 2 * center Y

	var maxP = 0.0;
	var minP = 0.0;
	var maxPLog = 0.0;
	var minPLog = 0.0;
	var maxVolume = 0.0;
	var maxGrowth = 0.0;
	var maxForce = 0.0;
	var avgVolume = 0.0;

	var numberOfItems = 0; // for current display
	var startIndex = 0; // calculated by the period and endIndex
	var endIndex = 0; // moving if dragged
	var rangeStartIndex = -1; // initial -1
	var rangeEndIndex = 0;

	var xStep = 0.0;
	var yStep = 0.0;
	var volumeStep = 0.0;
	var growthStep = 0.0;
	var forceStep = 0.0;
	var firstBarX = 0.0; // position of the first bar - relatively to the chart

	// canvas/context - for graph
	var canvas = null;
	var ctx = null;

	// remember the position
	var chartX = 0; // location of the DIV - if not moved
	var chartY = 0;

	// windown size
	var chartWidth = 0;
	var chartHeight = 0;

	// data for the chart
	var dateArray = new Array();
	var openLogArray = new Array(); // take Math.log()
	var closeLogArray = new Array();
	var minLogArray = new Array();
	var maxLogArray = new Array();
	var closeArray = new Array(); // keep original val
	var minArray = new Array();
	var maxArray = new Array();
	var logAvg20Array = new Array();
	var logAvg50Array = new Array();
	var logAvg100Array = new Array();
	var volumeArray = new Array();
	var growthArray = new Array();
	var forceArray = new Array();
	var trendColorArray = new Array();
	var marketTrendArray = new Array();
	var monthArray = new Array();
	var tradePointArray = new Array();
	var dataType = "";

	var dataSize = 0; // Size of the data array for all data

	////////////////// functions /////////////////

	// update the startIndex and endIndex
	// moveStep - the movemoment, 0, 1, 5, etc.
	function updateEndIndex(moveStep) {
		//alert("update: " + moveStep);
		if (canvas == null) {
			// do not do anything
			return;
		}
		//alert("update 2: " + moveStep);
		// check if the data exists
		var MIN_DAYS = 1; //63;
		if (dateArray == null || dateArray.length < MIN_DAYS) {
			return; // do not draw
		}

		// figure out the start index and end index
		var period = document.getElementById("inputVO.period").value;
		var intPeriod = parseInt(period);
		if (intPeriod >= 1 && intPeriod <= 18) {
			numberOfItems = 21 * intPeriod;
		} else if (period == "24") {
			numberOfItems = 104; // 52 x 2 weeks
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

		//alert("update 3: " + startIndex);
	}

	function init() {
		//alert("init");
		canvas = document.getElementById('canvas');
		if (canvas != null) {
			// get the scale
			//chartX = parseInt(dd.elements.chart.x, 10);  // fixed for all to remember the original location
			//chartY = parseInt(dd.elements.chart.y, 10);
			chartWidth = canvas.width; // parseInt(canvas.width, 10); 
			chartHeight = canvas.height; // parseInt(canvas.height, 10);

			// get context for the graphics
			ctx = canvas.getContext("2d");

			//alert("canvas is found - " + chartWidth + ":" + chartHeight);
		} else {
			//alert("canvas div is null");
		}

		//alert("init done");
	}

	// called at the load time, and the time when window resize()
	function adjustCanvasWidth() {
		if (canvas == null) {
			return;
		}

		// window size
		var winWidth = $(window).width();

		// this matches the responsive design on the screen
		if (winWidth <= 1288) {
			chartWidth = 825;
		} else if (winWidth <= 1588) {
			chartWidth = winWidth * 0.63;
		} else {
			chartWidth = 1588 * 0.63;
		}

		// update the canvas width
		canvas.width = chartWidth;
	}

	// draw chart after data is prepared
	function drawChart() {
		//alert("draw it 0: ");
		if (ctx == null) {
			return;
		}

		// make sure to clear it
		ctx.clearRect(0, 0, canvas.width, canvas.height);

		// find what ema's
		if (document.getElementById("doEMA20").checked == true) {
			doEMA20 = true;
		} else {
			doEMA20 = false;
		}

		if (document.getElementById("doEMA50").checked == true) {
			doEMA50 = true;
		} else {
			doEMA50 = false;
		}

		if (document.getElementById("doEMA100").checked == true) {
			doEMA100 = true;
		} else {
			doEMA100 = false;
		}

		if (document.getElementById("doBARS").checked == true) {
			doBARS = true;
		} else {
			doBARS = false;
		}

		if (document.getElementById("doZone").checked == true) {
			doZone = true;
		} else {
			doZone = false;
		}

		if (document.getElementById("doReverse").checked == true) {
			doReverse = true;
		} else {
			doReverse = false;
		}

		if (document.getElementById("doTradePoint").checked == true) {
			doTradePoint = true;
		} else {
			doTradePoint = false;
		}

		if (dateArray == null || dateArray.length == 0) {
			// no enough data found
			var stockId = document.getElementById("stockId").value;
			ctx.fillStyle = "green";
			ctx.font = "24pt Helvetica";
			ctx.fillText("No chart for this stock with stock_id: " + stockId,
					200, 290);
			return;
		}

		// find parameters / measures
		forceY = chartHeight - Y_BOTTOM - 2 * BOX_GAP;
		growthY = forceY - FORCE_HEIGHT - BOX_GAP;
		volumeY = growthY - GROWTH_HEIGHT - BOX_GAP;
		var gfHeight = FORCE_HEIGHT + GROWTH_HEIGHT + 4 * BOX_GAP;

		innerWidth = chartWidth - X_LEFT - X_RIGHT;
		innerHeight = chartHeight - Y_TOP - TITLE_HEIGHT - BOX_GAP
				- VOLUME_HEIGHT - Y_BOTTOM - gfHeight;
		origX = X_LEFT;
		origY = volumeY - VOLUME_HEIGHT - BOX_GAP;
		chartCenter2Y = 2 * origY - PRICE_BOTTOM + PRICE_TOP - innerHeight;

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
		//ctx.lineWidth = 0.3;  // set line width

		ctx.fillStyle = imageColor; // "rgba(200,200,200,0.3)";
		ctx.fillRect(0, 0, chartWidth, chartHeight);

		ctx.fillStyle = chartColor;
		ctx.fillRect(origX, volumeY - VOLUME_HEIGHT, innerWidth, VOLUME_HEIGHT);
		ctx.fillRect(origX, growthY - GROWTH_HEIGHT, innerWidth, GROWTH_HEIGHT);
		ctx.fillRect(origX, forceY - FORCE_HEIGHT, innerWidth, FORCE_HEIGHT);

		ctx.fillStyle = chartColor;
		ctx.fillRect(origX, origY - innerHeight, innerWidth, innerHeight);

		// labels
		ctx.save();
		ctx.fillStyle = colorBlue; // do this after .save() is called!  -- very strange, if called before .save()
		var halfV = 5;

		ctx.rotate(1.5 * Math.PI);
		ctx.font = "9pt Helvetica";
		ctx.fillText("Price", -origY + halfV, VERTICAL_TEXT_X1);
		ctx.fillText("Vol", -volumeY + halfV, VERTICAL_TEXT_X1);
		ctx.fillText("Grow", -growthY + halfV, VERTICAL_TEXT_X1);
		ctx.fillText("Force", -forceY + halfV, VERTICAL_TEXT_X1);
		ctx.restore();

		//ctx.setColor("#00ff00");
		//ctx.fillEllipse(100, 200, 100, 180);
		//ctx.setColor("maroon");
		//ctx.drawPolyline(new Array(50, 10, 120), new Array(10, 50, 70));

		//ctx.drawRect(0, 0, 680, 400);

		// ctx.stroke();  // It will strange if call .stroke() here!!!
		// for a path - need to call .stroke() or .fill() before call next begainPath().

		// dd.elements.chart.resizeTo(300, 272);         // resize draggable first DIV to extent of oval+line

	}

	function drawBars() {
		if (maxP == minP) {
			maxP = minP + 1;
		}

		if (maxGrowth == 0) {
			maxGrowth = 1;
		}

		if (maxForce == 0) {
			maxForce = 1;
		}

		minPLog = Math.log(minP);
		maxPLog = Math.log(maxP);
		yStep = (innerHeight - PRICE_BOTTOM - PRICE_TOP) / (maxPLog - minPLog);
		volumeStep = VOLUME_HEIGHT / maxVolume;
		growthStep = GROWTH_HEIGHT / (2 * maxGrowth);
		forceStep = FORCE_HEIGHT / (2 * maxForce);
		var zeroGrowthHt = growthY - 0.5 * GROWTH_HEIGHT;
		var zeroForceHt = forceY - 0.5 * FORCE_HEIGHT;

		var xPolylineGrowth = new Array();
		var yPolylineGrowth = new Array();
		var xPolylineForce = new Array();
		var yPolylineForce = new Array();
		var xPolylineAvg100 = new Array();
		var yPolylineAvg100 = new Array();
		var xPolylineAvg50 = new Array();
		var yPolylineAvg50 = new Array();
		var xPolylineAvg20 = new Array();
		var yPolylineAvg20 = new Array();
		var xPolylineAvg100Zone = new Array();
		var yPolylineAvg100Zone = new Array();
		var xPolylineAvg50Zone = new Array();
		var yPolylineAvg50Zone = new Array();

		// find scale list
		var scaleList = getPriceRulerList(minP, maxP);
		var maxPriceLength = getMaxPriceLength(scaleList);

		// alert("maxPriceLength: " + maxPriceLength);
		// after price scale is drawn, maxPriceLength is set
		if (maxPriceLength <= 2) {
			// make this global
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER;
		} else if (maxPriceLength <= 3) {
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER - 8)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER + 8;
		} else if (maxPriceLength <= 4) {
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER - 16)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER + 16;
		} else if (maxPriceLength <= 5) {
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER - 24)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER + 24;
		} else {
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER - 32)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER + 32;
		}

		var xx = firstBarX;
		var yyOpen = 0;
		var yyClose = 0;
		var yyMin = 0;
		var yyMax = 0;
		var volumeHt = 0;

		var totalPointsAvg100 = 0;
		var totalPointsAvg50 = 0;
		var totalPointsAvg20 = 0;
		var totalPointsAvg100Zone = 0;
		var totalPointsAvg50Zone = 0;

		// hold all the bar lines
		var lineList = new Array();
		var lineItemCnt = 0;

		//	alert("start/end: " + startIndex + "/" + endIndex + ", xStep: " + xStep);
		var chartBarBase = origY - PRICE_BOTTOM + yStep * minPLog;
		for (var i = startIndex; i <= endIndex; i++, xx += xStep) {
			// open/close/min/max are already Math.log()'ed ... to save some calculaton
			yyOpen = Math.round(chartBarBase - yStep * openLogArray[i]);
			yyClose = Math.round(chartBarBase - yStep * closeLogArray[i]);
			yyMin = Math.round(chartBarBase - yStep * minLogArray[i]);
			yyMax = Math.round(chartBarBase - yStep * maxLogArray[i]);
			var roundXX = Math.round(xx);

			var lineColor = "";
			if (yyOpen >= yyClose) {
				lineColor = colorGreen;
			} else {
				lineColor = colorRed;
			}

			// draw volume, open, min/max, close
			volumeHt = Math.round(volumeArray[i] * volumeStep);
			var lineItem = {
				color : lineColor,
				x1 : roundXX,
				y1 : volumeY,
				x2 : roundXX,
				y2 : volumeY - volumeHt,
				bar : 0
			};
			lineList[lineItemCnt++] = lineItem;

			if (doBARS) {
				lineItem = {
					color : lineColor,
					x1 : roundXX,
					y1 : yyMin,
					x2 : roundXX,
					y2 : yyMax,
					bar : 1
				};
				lineList[lineItemCnt++] = lineItem;
				lineItem = {
					color : lineColor,
					x1 : roundXX,
					y1 : yyOpen,
					x2 : roundXX - BAR_WIDTH,
					y2 : yyOpen,
					bar : 1
				};
				lineList[lineItemCnt++] = lineItem;
				lineItem = {
					color : lineColor,
					x1 : roundXX,
					y1 : yyClose,
					x2 : roundXX + BAR_WIDTH,
					y2 : yyClose,
					bar : 1
				};
				lineList[lineItemCnt++] = lineItem;
			}

			// growth line - bar
			if (growthArray[i] > 0) {
				lineColor = colorGreen;
			} else {
				lineColor = colorRed;
			}

			var growthHt = Math.round(growthArray[i] * growthStep);
			lineItem = {
				color : lineColor,
				x1 : roundXX,
				y1 : zeroGrowthHt,
				x2 : roundXX,
				y2 : zeroGrowthHt - growthHt,
				bar : 0
			};
			lineList[lineItemCnt++] = lineItem;

			// force line - bar
			if (forceArray[i] > 0) {
				lineColor = colorGreen;
			} else {
				lineColor = colorRed;
			}

			var forceHt = Math.round(forceArray[i] * forceStep);
			lineItem = {
				color : lineColor,
				x1 : roundXX,
				y1 : zeroForceHt,
				x2 : roundXX,
				y2 : zeroForceHt - forceHt,
				bar : 0
			};
			lineList[lineItemCnt++] = lineItem;

			//alert(" draw 2");

			// lines
			xPolylineGrowth[i - startIndex] = roundXX;
			yPolylineGrowth[i - startIndex] = zeroGrowthHt - growthHt;
			xPolylineForce[i - startIndex] = roundXX;
			yPolylineForce[i - startIndex] = zeroForceHt - forceHt;

			if (doEMA100) {
				if (logAvg100Array[i] <= maxPLog
						&& logAvg100Array[i] >= minPLog) {
					xPolylineAvg100[totalPointsAvg100] = roundXX;
					yPolylineAvg100[totalPointsAvg100] = Math
							.round(chartBarBase - yStep * logAvg100Array[i]);
					totalPointsAvg100++;
				}
			}

			if (doEMA50) {
				if (logAvg50Array[i] <= maxPLog && logAvg50Array[i] >= minPLog) {
					xPolylineAvg50[totalPointsAvg50] = roundXX;
					yPolylineAvg50[totalPointsAvg50] = Math.round(chartBarBase
							- yStep * logAvg50Array[i]);
					totalPointsAvg50++;
				}
			}

			if (doEMA20) {
				if (logAvg20Array[i] <= maxPLog && logAvg20Array[i] >= minPLog) {
					xPolylineAvg20[totalPointsAvg20] = roundXX;
					yPolylineAvg20[totalPointsAvg20] = Math.round(chartBarBase
							- yStep * logAvg20Array[i]);
					totalPointsAvg20++;
				}
			}

			if (doZone) {
				// ignore avgLog=20 as this is used for undefined, so ignore
				if ((logAvg100Array[i] <= maxPLog
						&& logAvg100Array[i] >= minPLog && logAvg50Array[i] < 20)
						|| (logAvg50Array[i] <= maxPLog
								&& logAvg50Array[i] >= minPLog && logAvg100Array[i] < 20)) {
					if (logAvg100Array[i] <= maxPLog
							&& logAvg100Array[i] >= minPLog) {
						xPolylineAvg100Zone[totalPointsAvg100Zone] = roundXX;
						yPolylineAvg100Zone[totalPointsAvg100Zone] = Math
								.round(chartBarBase - yStep * logAvg100Array[i]);
						totalPointsAvg100Zone++;
					} else if (logAvg100Array[i] > maxPLog) {
						xPolylineAvg100Zone[totalPointsAvg100Zone] = roundXX;
						yPolylineAvg100Zone[totalPointsAvg100Zone] = Math
								.round(chartBarBase - yStep * maxPLog);
						totalPointsAvg100Zone++;
					} else {
						xPolylineAvg100Zone[totalPointsAvg100Zone] = roundXX;
						yPolylineAvg100Zone[totalPointsAvg100Zone] = Math
								.round(chartBarBase - yStep * minPLog);
						totalPointsAvg100Zone++;
					}

					if (logAvg50Array[i] <= maxPLog
							&& logAvg50Array[i] >= minPLog) {
						xPolylineAvg50Zone[totalPointsAvg50Zone] = roundXX;
						yPolylineAvg50Zone[totalPointsAvg50Zone] = Math
								.round(chartBarBase - yStep * logAvg50Array[i]);
						totalPointsAvg50Zone++;
					} else if (logAvg50Array[i] > maxPLog) {
						xPolylineAvg50Zone[totalPointsAvg50Zone] = roundXX;
						yPolylineAvg50Zone[totalPointsAvg50Zone] = Math
								.round(chartBarBase - yStep * maxPLog);
						totalPointsAvg50Zone++;
					} else {
						xPolylineAvg50Zone[totalPointsAvg50Zone] = roundXX;
						yPolylineAvg50Zone[totalPointsAvg50Zone] = Math
								.round(chartBarBase - yStep * minPLog);
						totalPointsAvg50Zone++;
					}
				}
			}

			if (doTradePoint) {
				// draw the trade point
				var tradePointHt = growthY + 4;
				if (tradePointArray[i] != null && tradePointArray[i] == "L") {
					ctx.beginPath();
					// draw green
					ctx.strokeStyle = colorGreen;

					//ctx.arc(x,y,radius,startAngle,endAngle, false)
					ctx.arc(roundXX, tradePointHt, 2, 0, Math.PI * 2);

					ctx.fillStyle = colorGreen;
					ctx.closePath();
					ctx.fill();
					//ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
					//ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
					ctx.stroke();
				}
			}

			//alert(" draw 3");
			var trendHt = forceY + BOX_GAP;
			//var marketTrendHt = trendHt + BOX_GAP;
			if (trendColorArray[i] != null && trendColorArray[i] == "G") {
				// draw green
				ctx.strokeStyle = colorGreen;

				//ctx.arc(x,y,radius,startAngle,endAngle, false)
				//ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorGreen;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
			} else if (trendColorArray[i] != null && trendColorArray[i] == "R") {
				// do RED
				ctx.strokeStyle = colorRed;
				//ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorRed;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
			} else if (trendColorArray[i] != null && trendColorArray[i] == "B") {
				// do BLUE
				ctx.strokeStyle = colorDarkGreen;
				//ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorDarkGreen;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
			} else if (trendColorArray[i] != null && trendColorArray[i] == "Y") {
				// do YELLOW
				ctx.strokeStyle = colorPink;
				//ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorPink;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);

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
			ctx.setColor(colorGreen);
			ctx.fillOval(roundXX - 2, marketTrendHt - 2, 4, 4);
			} else if (marketFlag == "R") {
			// do RED
			ctx.setColor(colorRed);
			ctx.fillOval(roundXX - 2, marketTrendHt - 2, 4, 4);
			}  else if (marketFlag == "Y") {
			// do RED
			ctx.setColor(colorYellow);
			ctx.fillOval(roundXX - 2, marketTrendHt - 2, 4, 4);
			} else {
			// do nothing
			//pad.setColor(colorBlue);
			//pad.drawLine((int) xx - 1, (int) marketBallHt, (int) xx + 1, (int) marketBallHt);
			}
			} */

			// draw month
			if (monthArray[i] != "") {
				ctx.beginPath();
				ctx.strokeStyle = colorBlue;
				ctx.moveTo(roundXX, origY);
				ctx.lineTo(roundXX, origY - SCALE_LENGTH);
				ctx.stroke();

				if (roundXX < origX + innerWidth - 12) {
					ctx.fillStyle = colorBlue;
					var monthStr = getShortMonthString(monthArray[i]);
					ctx.font = "9pt Helvetica";
					ctx.fillText(monthStr, roundXX - 2 * SMALL_GAP, origY
							- SMALL_GAP);
				}
			}

		}

		// average volume
		ctx.strokeStyle = colorDark;
		volumeHt = Math.round(avgVolume * volumeStep);
		var volumeRealHt = volumeY - volumeHt;
		ctx.beginPath();
		ctx.moveTo(origX, volumeRealHt);
		ctx.lineTo(chartWidth - X_RIGHT, volumeRealHt);

		// draw volume: max/avg string here ????...

		// draw 0 growth line / force line

		ctx.moveTo(origX, zeroGrowthHt);
		ctx.lineTo(chartWidth - X_RIGHT, zeroGrowthHt);

		ctx.moveTo(origX, zeroForceHt);
		ctx.lineTo(chartWidth - X_RIGHT, zeroForceHt);
		ctx.stroke();

		ctx.strokeStyle = colorDark;
		//ctx.drawPolyline(xPolylineGrowth, yPolylineGrowth);
		//ctx.drawPolyline(xPolylineForce, yPolylineForce);

		//drawPlotData(xPolylineGrowth, yPolylineGrowth);
		//drawPlotData(xPolylineForce, yPolylineForce);

		// draw dark zone
		if (doZone) {
			drawDarkZone(xPolylineAvg50Zone, yPolylineAvg50Zone,
					xPolylineAvg100Zone, yPolylineAvg100Zone, true);
		}
		// draw price scale after dark zone
		drawPriceScaleList(scaleList);

		// draw the lines
		drawLines(lineList);

		// stroke ---????
		// draw 50-day line
		//ctx.setStroke(2);
		ctx.lineWidth = 2;

		if (doEMA50) {
			ctx.strokeStyle = colorBlue;
			drawPlotData(xPolylineAvg50, yPolylineAvg50, true);
		}

		if (doEMA20) {
			ctx.strokeStyle = colorGreen;
			drawPlotData(xPolylineAvg20, yPolylineAvg20, true);
		}

		if (doEMA100) {
			ctx.strokeStyle = colorRed;
			// draw 100-day line
			drawPlotData(xPolylineAvg100, yPolylineAvg100, true);
		}

		ctx.lineWidth = 1;

		// title
		avgVolume = Math.round(avgVolume);
		var titleY = Y_TOP + 2 * SMALL_GAP;
		ctx.fillStyle = colorBlue;
		ctx.font = "9pt Helvetica";
		ctx.fillText("Last Date: " + dateArray[endIndex], origX, titleY);
		ctx.fillText("Price: " + closeArray[endIndex], origX + 175, titleY);
		ctx.fillText("Volume: " + volumeArray[endIndex], origX + 280, titleY);
		ctx.fillText("Avg Volume: " + avgVolume, origX + 420, titleY);

	}

	function drawPlotData(xPoly, yPoly, doRR) {
		if (xPoly.length >= 2) {
			ctx.beginPath();
			if (doRR && doReverse) {
				ctx.moveTo(xPoly[0], chartCenter2Y - yPoly[0]);
				for (i = 1; i < xPoly.length; i++) {
					ctx.lineTo(xPoly[i], chartCenter2Y - yPoly[i]);
				}
			} else {
				ctx.moveTo(xPoly[0], yPoly[0]);
				for (i = 1; i < xPoly.length; i++) {
					ctx.lineTo(xPoly[i], yPoly[i]);
				}
			}

			ctx.stroke();
		}
	}

	function drawLines(lineList) {
		if (lineList != null && lineList.length > 0) {
			for (var i = 0; i < lineList.length; i++) {
				var lineItem = lineList[i];
				ctx.beginPath();
				ctx.strokeStyle = lineItem.color;
				if (doReverse && lineItem.bar == 1) {
					ctx.moveTo(lineItem.x1, chartCenter2Y - lineItem.y1);
					ctx.lineTo(lineItem.x2, chartCenter2Y - lineItem.y2);
				} else {
					ctx.moveTo(lineItem.x1, lineItem.y1);
					ctx.lineTo(lineItem.x2, lineItem.y2);
				}
				ctx.stroke(); // have to stroke one a time, as the style may change from one to antoher
			}

		}
	}

	function drawDarkZone(xPolylineAvg50, yPolylineAvg50, xPolylineAvg100,
			yPolylineAvg100, doRR) {
		//alert("aaa");
		if (xPolylineAvg50 != null && xPolylineAvg50.length > 0
				&& xPolylineAvg100 != null && xPolylineAvg100.length > 0) {
			ctx.beginPath();
			ctx.fillStyle = colorLightDark;

			if (doRR && doReverse) {
				ctx
						.moveTo(xPolylineAvg50[0], chartCenter2Y
								- yPolylineAvg50[0]);
				for (var i = 1; i < xPolylineAvg50.length; i++) {
					ctx.lineTo(xPolylineAvg50[i], chartCenter2Y
							- yPolylineAvg50[i]);
				}

				for (var i = xPolylineAvg100.length - 1; i >= 0; i--) {
					ctx.lineTo(xPolylineAvg100[i], chartCenter2Y
							- yPolylineAvg100[i]);
				}
			} else {
				ctx.moveTo(xPolylineAvg50[0], yPolylineAvg50[0]);
				for (var i = 1; i < xPolylineAvg50.length; i++) {
					ctx.lineTo(xPolylineAvg50[i], yPolylineAvg50[i]);
				}

				for (var i = xPolylineAvg100.length - 1; i >= 0; i--) {
					ctx.lineTo(xPolylineAvg100[i], yPolylineAvg100[i]);
				}
			}

			ctx.closePath();
			ctx.fill(); // fill with dark color
			ctx.stroke();
		}
	}

	function drawAvgLabel() {
	}

	function drawMonthLabel() {
	}

	function drawChartFramePost() {
		//ctx.setColor(colorBlack);
		ctx.strokeStyle = colorDark;
		ctx.strokeRect(origX, volumeY - VOLUME_HEIGHT, innerWidth,
				VOLUME_HEIGHT);
		ctx.strokeRect(origX, origY - innerHeight, innerWidth, innerHeight);

		// draw option if there is setting
		ctx.strokeRect(origX, growthY - GROWTH_HEIGHT, innerWidth,
				GROWTH_HEIGHT);
		ctx.strokeRect(origX, forceY - FORCE_HEIGHT, innerWidth, FORCE_HEIGHT);

	}

	// This is called to prepare the data
	// either for daily or weekly
	function prepareData() {
		//alert("prepare data");
		if (canvas == null) {
			// no container - so not to prepare
			return;
		}

		var dataListStr = ""; // daily or weekly
		//var marketListStr = "";
		var period = document.getElementById('inputVO.period').value;
		if (period == "3" || period == "6" || period == "12") {
			if (dataType == "Daily") {
				// do not need to redo
				return;
			}

			dataType = "Daily";
			dataListStr = document.getElementById('dailyDataStr').value;
			//marketListStr = form.marketFlagList.value;
		} else if (period == "24") {
			// 24 month -> display weekly chart
			if (dataType == "Weekly") {
				// do not need to redo
				return;
			}

			dataType = "Weekly";
			dataListStr = document.getElementById('weeklyDataStr').value;
			//marketListStr = form.marketWeeklyFlagList.value;
		} else {
			if (dataType == "Daily") {
				// do not need to redo
				return;
			}

			dataType = "Daily";
			dataListStr = document.getElementById('dailyDataStr').value;
			//marketListStr = form.marketFlagList.value;
		}

		// check if no data
		if (dataListStr == null || dataListStr == "") {
			ctx = null; // so other functions will be skipped by checking this object
			alert("no data");
			return;
		}

		var dataArray = Q.common.mySplit(dataListStr, "#");
		if (dataArray == null || dataArray.length < 1) {
			alert("Content error: " + dataListStr);
			// do something to let the draw to know to not draw
			return;
		}

		// initialize the size and index
		dataSize = dataArray.length;
		rangeEndIndex = dataSize - 1;

		// determine where to start - rangeEndIndex
		var startDate = document.getElementById('startDate').value;

		// make sure the arrays are reset
		resetDataArrays();

		// split the data
		var prevMonth = "";
		for (var i = 0; i < dataArray.length; i++) {
			var nodeInfo = Q.common.mySplit(dataArray[i], "|");
			// format: date | open | close | min | max | logAvg9 | logAvg21 | logAvg50
			// | volume | growth | force | trendColor | tradePoint
			if (nodeInfo == null || nodeInfo.length != 13) {
				// error - need to notify the draw
				alert("format error: " + dataArray[i]);
				return;
			}

			dateArray[i] = nodeInfo[0];
			openLogArray[i] = Math.log(parseFloat(nodeInfo[1]));
			closeArray[i] = parseFloat(nodeInfo[2]); // keep non-log just for close price
			closeLogArray[i] = Math.log(closeArray[i]);
			minArray[i] = parseFloat(nodeInfo[3]);
			minLogArray[i] = Math.log(minArray[i]);
			maxArray[i] = parseFloat(nodeInfo[4]);
			maxLogArray[i] = Math.log(maxArray[i]);
			logAvg20Array[i] = parseFloat(nodeInfo[5]);
			logAvg50Array[i] = parseFloat(nodeInfo[6]);
			logAvg100Array[i] = parseFloat(nodeInfo[7]);
			volumeArray[i] = parseFloat(nodeInfo[8]); // this is float too
			growthArray[i] = parseFloat(nodeInfo[9]);
			forceArray[i] = parseFloat(nodeInfo[10]);
			trendColorArray[i] = nodeInfo[11];
			tradePointArray[i] = nodeInfo[12];

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

		//alert("prepare data: " + startIndex + "," + endIndex);
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
		var items = Q.common.mySplit(dateStr, "/");
		if (items != null && items.length == 3) {
			return items[1];
		} else {
			return "";
		}
	}

	//var maxP, minP, maxVolume, maxGrowth, maxForce
	function findMaxMinForAll() {
		var totalVolume = 0.0;
		for (var i = startIndex; i <= endIndex; i++) {
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
					temp = -temp;
				}

				if (temp > maxGrowth) {
					maxGrowth = temp;
				}

				temp = forceArray[i];
				if (temp < 0) {
					temp = -temp;
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

	function getMaxPriceLength(scaleList) {
		var max = 0;
		for (var i = 0; i < scaleList.length; i++) {
			var runP = scaleList[i];
			//alert("runP: " + runP.toString().length + ", " + runP);
			if (runP.toString().length > max) {
				max = runP.toString().length;
			}
		}

		return max;
	}

	function drawPriceScaleList(scaleList) {
		//var scaleList = getPriceRulerList(minP, maxP);
		//alert("drawPriceScaleList()");
		var lastP = "" + scaleList[scaleList.length - 1];
		var leftLen = (lastP.length + 2) * (SMALL_GAP + 1);
		// alert("lastP: " + lastP + ", length: " + leftLen);
		if (scaleList.length > 1) {
			var prevP = "" + scaleList[scaleList.length - 2];
			var prevLen = (prevP.length + 2) * (SMALL_GAP + 1);
			if (prevLen > leftLen) {
				leftLen = prevLen;
			}
		}

		var prevHt = origY;
		ctx.lineWidth = 0.7;
		for (var i = 0; i < scaleList.length; i++) {
			var runP = scaleList[i];
			var displayHt = Math.round(origY - PRICE_BOTTOM - yStep
					* (Math.log(runP) - minPLog));
			var displayHt20 = displayHt;
			if (doReverse) { // chnage before display
				displayHt20 = chartCenter2Y - displayHt;
			}

			// draw a line
			ctx.strokeStyle = colorDark;
			ctx.beginPath();
			ctx.moveTo(origX, displayHt20);
			ctx.lineTo(chartWidth - X_RIGHT, displayHt20);
			ctx.stroke();

			if (prevHt - displayHt > 12) { // do not display if too close
				// draw text - do not display if it is out of box
				if (displayHt20 - SMALL_GAP - TEXT_HEIGHT >= origY
						- innerHeight) {
					ctx.fillStyle = colorRed;
					ctx.font = "9pt Verdana";
					ctx.fillText(runP, origX + SMALL_GAP, displayHt20 - 2);
				}
			}

		}

		ctx.lineWidth = 1;
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
		} else if (range < 2) {
			dispStep = 0.2;
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
		} else if (range < 250) {
			dispStep = 25;
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
		} else {
			initP = Math.round(minP / 200 - 1) * 200;
		}

		var runP = initP;
		var i = 0;
		do {
			if (runP >= minP) {
				list[i] = runP;
				i++;
			}

			runP += dispStep;
			runP = Math.round(runP * 100) / 100; // strang error when 7.199999999999 is displayed
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
			} else if (monthNum == "08") {
				monthStr = "Aug";
			} else if (monthNum == "09") {
				monthStr = "Sep";
			} else if (monthNum == "10") {
				monthStr = "Oct";
			} else if (monthNum == "11") {
				monthStr = "Nov";
			} else if (monthNum == "12") {
				monthStr = "Dec";
			}
		}

		return monthStr;
	}

	function resetDataArrays() {
		dateArray = new Array();
		openLogArray = new Array();
		closeArray = new Array();
		minArray = new Array();
		maxArray = new Array();
		closeLogArray = new Array();
		minLogArray = new Array();
		maxLogArray = new Array();
		logAvg20Array = new Array();
		logAvg50Array = new Array();
		logAvg100Array = new Array();
		volumeArray = new Array();
		growthArray = new Array();
		forceArray = new Array();
		trendColorArray = new Array();
		marketTrendArray = new Array();
		monthArray = new Array();
		tradePointArray = new Array();
	}
	
	function checkInRange(dayInt) {
		if (endIndex + dayInt > rangeEndIndex) {
            return false;
        } else {
        	return true;
        }
	}
	
	function startOver() {
		rangeStartIndex = -1;
        dataType = "";  // so that the data is recalculated
        
        prepareData();
        updateEndIndex(0);
        drawChart();
	}
	
	function getEndIndex() {
		return endIndex;
	}
	
	function getCurrentClose() {
		return closeArray[endIndex];
	}
	
	function getCurrentDate() {
		return dateArray[endIndex];
	}
	
	function getCloseByIndex(i) {
		return closeArray[i];
	}
	
	function getDateByIndex(i) {
		return dateArray[i];
	}

	// explicitly return public methods when this object is instantiated
	return {
		updateEndIndex : updateEndIndex,
		init : init,
		drawChart : drawChart,
		prepareData : prepareData,
		adjustCanvasWidth : adjustCanvasWidth,
		checkInRange : checkInRange,
		startOver : startOver,
		getEndIndex : getEndIndex,
		getCurrentClose : getCurrentClose,
		getCurrentDate : getCurrentDate,
		getCloseByIndex : getCloseByIndex,
		getDateByIndex : getDateByIndex
		
	};

})(window);

/* The End */

