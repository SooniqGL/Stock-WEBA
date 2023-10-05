/* 
 * dynamic_barchart.js
 * used to draw the bar chart on the client side
 */


var QQ_barchart = ( function( window, undefined ) {

	var X_LEFT = 25;
	var X_RIGHT = 8;
	var Y_TOP = 10;
	var Y_BOTTOM = 10;
	var VOLUME_HEIGHT = 40; // for volume box
	var GROWTH_HEIGHT = 30; // for GROWTH box
	var FORCE_HEIGHT = 30; // for FORCE box
	var RS_HEIGHT = 30;
	var TITLE_HEIGHT = 20;
	var BOX_GAP = 8;
	var SCALE_LENGTH = 3; // bars in y axis
	var BAR_WIDTH = 2; // bar width in price chart
	var PRICE_TOP = 10; // draw above this height
	var PRICE_BOTTOM = 15; // draw above this height
	var TEXT_HEIGHT = 15;
	var VERTICAL_TEXT_X1 = 17; // price
	var SMALL_GAP = 5;
	var CHART_LEFT_INNER = 25;
	var CHART_RIGHT_INNER = 5;
	
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
	
	// options
	var doEMA20 = false;
	var doEMA50 = false;
	var doEMA100 = false;
	var doZone = false;
	var doBARS = false;
	var doReverse = false;
	
	// measure matrix
	var innerWidth = 0;
	var innerHeight = 0;
	var origX = 0;
	var origY = 0;
	var volumeY = 0;
	var growthY = 0;
	var forceY = 0;
	var chartCenter2Y = 0; // 2 * center chart Y - used for reverse chart
	
	// price attributes
	var maxP = 0.0;
	var minP = 0.0;
	var maxPLog = 0.0;
	var minPLog = 0.0;
	var maxVolume = 0.0;
	var maxGrowth = 0.0;
	var maxForce = 0.0;
	var avgVolume = 0.0;
	
	// display control
	var numberOfItems = 0; // for current display
	var startIndex = 0; // calculated by the period and endIndex
	var endIndex = 0; // moving if dragged
	var dataSize = 0; // this is different for daily and weekly
	
	// steps - for display
	var xStep = 0.0;
	var yStep = 0.0;
	var volumeStep = 0.0;
	var growthStep = 0.0;
	var forceStep = 0.0;
	var firstBarX = 0.0; // position of the first bar - relatively to the chart
	
	// display two boxes - two box for mouse wheel
	var box1_X = 0;
	var box_Y = 0;
	var box_W = 0;
	var box_H = 0;
	var box2_X = 0;
	
	// Following variables are used for the Ajax call
	var hasMoreData = true; // When one Ajax fails to get data, do not try the next
							// one
	var firstDate = ""; // make this global - used in the Ajax call, set in the
						// parse data time
	var numToMove = 0; // make this global so that it is availble for Ajax Call
						// back function
	var inCallFlag = false; // when Ajax is fired, ignore the event, so that no same
							// call be fired twice
	
	// keep drag, only display if certain data passed
	var dragLengthSofar = 0;
	
	// canvas and context
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
	var dataType = "";
	
	// dot related, currently, not used
	var reddotY = 0;
	var halfReddotW = 7; // half image size
	var prevReddotX = 0;
	
	// if resetFlag = -1, set endIndex to dataSize - 1;
	// if resetFlag = 0, do not change endIndex.
	function updateEndIndex(resetFlag) {
		if (canvas == null) {
			// do not do anything
			return;
		}
		//alert("update: " + resetFlag);
	
		// check if the data exists
		var MIN_DAYS = 1; // 63;
		if (dateArray == null || dateArray.length < MIN_DAYS) {
			return; // do not draw
		}
	
		// figure out the start index and end index
		// figure out the start index and end index
		var doWeekly = document.getElementById("doWeekly").checked;
		var period = document.getElementById("inputVO.period").value;
		var intPeriod = parseInt(period);
		if (doWeekly) {
			if (intPeriod >= 1 && intPeriod <= 36) {
				numberOfItems = 4 * intPeriod;
			} else {
				// default is 12 months
				numberOfItems = 52;
			}
		} else {
			if (intPeriod >= 1 && intPeriod <= 36) {
				numberOfItems = 21 * intPeriod;
			} else {
				// default is 3 months
				numberOfItems = 63;
			}
		}
	
		// figure out the end index
		if (resetFlag < 0) {
			endIndex = dataSize - 1;
		} else if (resetFlag == 0) {
			// do not do now
		}
	
	}
	
	// return false - if not need to redraw -- used by the drag function
	// this function is called to prepare redraw.
	function updateEndIndex2(deltaPosX) {
		if (canvas == null) {
			// do not do anything
			return false;
		}
		// alert("updateEndIndex2: " + deltaPosX);
	
		// check if the data exists
		var MIN_DAYS = 1; // 63;
		if (dateArray == null || dateArray.length < MIN_DAYS) {
			return false; // do not draw
		}
	
		// figure out the start index and end index
		// numberOfItems is already defined
	
		// figure out the end index
		if (deltaPosX == 0 || xStep == 0) {
			return false; // no need to redraw
		} else {
			var cnt = -Math.round(deltaPosX / xStep);
			dragLengthSofar += cnt;
	
			if (dragLengthSofar > -10 && dragLengthSofar < 10) {
				return false;
			} else if (dragLengthSofar > 0) {
				// move to right if there is space
				if (endIndex >= dataSize - 1) {
					// not need to move, end reached
					return false;
				} else {
					if (dragLengthSofar + endIndex > dataSize - 1) {
						// over
						endIndex = dataSize - 1;
					} else {
						endIndex += dragLengthSofar;
					}
	
					dragLengthSofar = 0; // reset
					return true;
				}
			} else {
				// move to the left if there is space
				if (endIndex <= numberOfItems - 1) {
					//alert("has more data: " + hasMoreData + ", inCallFlag: " + inCallFlag);
					// need to see if there is more data
					if (hasMoreData == false) {
						// previously tried to get data and get nothing, so do not
						// try again
						return false;
					}
	
					// Use inCallFlag to prevent multiple
					// event is fired to get the same set of data
					if (inCallFlag == false) {
						// need to see if we have more data for this stock
						numToMove = dragLengthSofar;
						inCallFlag = true;
						dragLengthSofar = 0;
						getMoreData(); // get data and put to form hidden fields
					}
					return false;
	
				} else {
					if (dragLengthSofar + endIndex < numberOfItems - 1) {
						// only move to the limit
						endIndex = numberOfItems - 1;
					} else {
						// move left
						endIndex += dragLengthSofar;
					}
	
					dragLengthSofar = 0; // reset
					return true;
				}
			}
		}
	
	}
	
	// monthOrDay - M for month, D for Day
	// delta - > 0 or < 0
	// this function is used by mousewheel when the range scroll
	// is requested.
	function updateEndIndexAndDraw(monthOrDay, delta) {
		if (canvas == null) {
			// do not do anything
			return;
		}
		//alert("updateEndIndexAndDraw: monthOrDay: " + monthOrDay + ", delta: " + delta);
	
		// check if the data exists
		var MIN_DAYS = 1; // 63;
		if (dateArray == null || dateArray.length < MIN_DAYS) {
			return; // do not draw
		}
	
		// figure out the start index and end index
		// numberOfItems is already defined
	
		// figure out the end index
		if (delta == 0) {
			return; // no need to redraw
		} else if (delta > 0) {
			// In Firefox, the delta = 0.3333333, which is strange.
			delta = 1;
		} else if (delta < 0) {
			// In Firefox, the delta = - 0.3333333, which is strange.
			delta = -1;
		}
	
		var cnt = -delta;
		if (monthOrDay == "M") {
			cnt *= 21;
		}
	
		//alert("cnt: " + cnt + ", dataSize: " + dataSize + ", endIndex: " + endIndex + ", numberOfItems: " + numberOfItems);
		if (cnt > 0) {
			// move to right if there is space
			if (endIndex >= dataSize - 1) {
				// not need to move, end reached
				return;
			} else {
				if (cnt + endIndex > dataSize - 1) {
					// over
					endIndex = dataSize - 1;
				} else {
					endIndex += cnt;
				}
			}
		} else {
			// move to the left if there is space
			if (endIndex <= numberOfItems - 1) {
				if (hasMoreData == false) {
					//alert("hasMoreData: " + hasMoreData);
					// previously tried to get data and get nothing, so do not try
					// again
					return;
				}
	
				// Use inCallFlag to prevent multiple
				// event is fired to get the same set of data
				//alert("inCallFlag: " + inCallFlag);
				if (inCallFlag == false) {
					// need to see if we have more data for this stock
					numToMove = cnt;
					inCallFlag = true;
					
					//alert("before getMoreData: ");
					getMoreData(); // get data and put to form hidden fields
				}
				return;
			} else {
				if (cnt + endIndex < numberOfItems - 1) {
					// only move to the limit
					endIndex = numberOfItems - 1;
				} else {
					// move left
					endIndex += cnt;
				}
			}
		}
	
		// need to draw
		ctx.clearRect(0, 0, canvas.width, canvas.height);
		drawChart();
	}
	
	// Ajax call to get more data from server
	// Put the data into the hidden box and
	// return the length of new data list
	// firstDate - the very first date in Daily Data Str! (Do not use the weekly
	// data.)
	function getMoreData() {
		//alert("in get data");
		var stDate = Q.date.addDayToDate(Q.date.parseDate(firstDate), -1);
	
		//alert("ajax call: " + stDate + ", " + firstDate);
	
		var url = "../s/analyze_ajax.do";
		var params = "inputVO.mode=ajax&inputVO.pageStyle=A&inputVO.displayDate="
				+ stDate + "&inputVO.stockId="
				+ document.getElementById("inputVO.stockId").value;
	
		Q.chart.makeAjaxCall(url, params, Q.chart.fnAjaxRespone);
	}
	
	// 1 - for box 1, 2 - for box 2, 3 - for others
	// box position and size: box1_X, box_W, box_Y, box_H, box2_X
	function inWhichBox(pageX, pageY) {
		var pos = findOffset(canvas);
		var xx = pageX - pos.x;
		var yy = pageY - pos.y;
	
		// box position and size: box1_X, box_W, box_Y, box_H, box2_X
		var whichIs = "";
		if (xx >= box1_X && xx <= box1_X + box_W && yy >= box_Y
				&& yy <= box_Y + box_H) {
			whichIs = "1";
		} else if (xx >= box2_X && xx <= box2_X + box_W && yy >= box_Y
				&& yy <= box_Y + box_H) {
			whichIs = "2";
		} else {
			whichIs = "3";
		}
	
		return whichIs;
	}
	
	function findOffset(obj) {
		var curX = 0, curY = 0;
		if (obj.offsetParent) {
			do {
				curX += obj.offsetLeft;
				curY += obj.offsetTop;
			} while ((obj = obj.offsetParent) != null);
	
			return {
				x : curX,
				y : curY
			};
		}
	
		return {
			x : 0,
			y : 0
		};
	}
	
	function init() {
		 //alert("init");
		canvas = document.getElementById('chart');
		if (canvas != null) {
			// reset width
			adjustCanvasWidth();
			
			// get the scale
			// chartX = parseInt(dd.elements.chart.x, 10); // fixed for all to
			// remember the original location
			// chartY = parseInt(dd.elements.chart.y, 10);
			chartWidth = canvas.width; // parseInt(canvas.width, 10);
			chartHeight = canvas.height; // parseInt(canvas.height, 10);
	
			// get context for the graphics
			ctx = canvas.getContext("2d");
	
			 //alert("canvas is found - " + chartWidth + ":" + chartHeight);
	
			// init the drag feather
			// initDrag();
		} else {
			//alert("canvas div is null");
		}
	
	}
	
	/*
	 * when delta is > xStep, move to right; when delta is < - xStep, move to left;
	 * if the end is reached, do not move anymore; return true if moved, else return
	 * false;
	 */
	function drawCanvasForDrag(delta) {
		var doIt = updateEndIndex2(delta);
		if (doIt == false) {
			// no need to draw
			return false;
		} else {
			// draw the chart - same set of data
			ctx.clearRect(0, 0, canvas.width, canvas.height);
			drawChart();
			return true;
		}
	}
	
	// called at the load time, and the time when window resize()
	function adjustCanvasWidth() {
		if (canvas == null) {
			return;
		}
		
		// window size
		var winWidth = $( window ).width();
		
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
	
	// new Functions
	function drawChart() {
		if (canvas == null) {
			return;
		}
		//alert("draw it: ");
	
		// make sure to clear it
		ctx.clearRect(0, 0, canvas.width, canvas.height);
	
		// the start index
		startIndex = endIndex - numberOfItems + 1;
		if (startIndex < 0) {
			startIndex = 0;
		}
	
		// find what ema's
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
	
		if (document.getElementById("doZone").checked == true) {
			doZone = true;
		} else {
			doZone = false;
		}
	
		if (document.getElementById("doBARS").checked == true) {
			doBARS = true;
		} else {
			doBARS = false;
		}
	
		if (document.getElementById("doReverse").checked == true) {
			doReverse = true;
		} else {
			doReverse = false;
		}
	
		if (dateArray == null || dateArray.length == 0) {
			// no enough data found
			var stockId = document.getElementById("stockId").value;
			ctx.fillStyle = "green";
			ctx.font = "24pt Helvetica";
			ctx.fillText("No chart for this stock with stock_id: " + stockId, 200,
					290);
			return;
		}
	
		// find parameters / measures
		forceY = chartHeight - Y_BOTTOM - 2 * BOX_GAP;
		// growthY = forceY - FORCE_HEIGHT - BOX_GAP;
		// volumeY = growthY - GROWTH_HEIGHT - BOX_GAP;
		// var gfHeight = FORCE_HEIGHT + GROWTH_HEIGHT + 4 * BOX_GAP;
		volumeY = forceY - FORCE_HEIGHT - BOX_GAP;
		var gfHeight = FORCE_HEIGHT + 3 * BOX_GAP;
	
		innerWidth = chartWidth - X_LEFT - X_RIGHT;
		innerHeight = chartHeight - Y_TOP - TITLE_HEIGHT - BOX_GAP - VOLUME_HEIGHT
				- Y_BOTTOM - gfHeight;
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
	
		ctx.fillStyle = imageColor; // "rgba(200,200,200,0.3)";
		ctx.fillRect(0, 0, chartWidth, chartHeight);
	
		ctx.fillStyle = chartColor;
		ctx.fillRect(origX, volumeY - VOLUME_HEIGHT, innerWidth, VOLUME_HEIGHT);
		//ctx.fillRect(origX, growthY - GROWTH_HEIGHT, innerWidth, GROWTH_HEIGHT);
		ctx.fillRect(origX, forceY - FORCE_HEIGHT, innerWidth, FORCE_HEIGHT);
	
		ctx.fillStyle = chartColor;
		ctx.fillRect(origX, origY - innerHeight, innerWidth, innerHeight);
	
		// labels
		ctx.save();
		ctx.fillStyle = colorBlue; // do this after .save() is called! -- very
									// strange, if called before .save()
		var halfV = 5;
	
		ctx.rotate(1.5 * Math.PI);
		ctx.font = "9pt Helvetica";
		ctx.fillText("Price", -origY + halfV, VERTICAL_TEXT_X1);
		ctx.fillText("Vol", -volumeY + halfV, VERTICAL_TEXT_X1);
		//ctx.fillText("Grow", -growthY + halfV, VERTICAL_TEXT_X1);
		ctx.fillText("Force", -forceY, VERTICAL_TEXT_X1);
		ctx.restore();
	
		// ctx.drawRect(0, 0, chartWidth, chartHeight);
	}
	
	function drawBars() {
		if (maxP == minP) {
			maxP = minP + 1;
		}
	
		minPLog = Math.log(minP);
		maxPLog = Math.log(maxP);
		yStep = (innerHeight - PRICE_BOTTOM - PRICE_TOP) / (maxPLog - minPLog);
		volumeStep = VOLUME_HEIGHT / maxVolume;
		//growthStep = GROWTH_HEIGHT / (2 * maxGrowth);
		forceStep = FORCE_HEIGHT / (2 * maxForce);
		//var zeroGrowthHt = growthY - 0.5 * GROWTH_HEIGHT;
		var zeroForceHt = forceY - 0.5 * FORCE_HEIGHT;
	
		//var xPolylineGrowth = new Array();
		//var yPolylineGrowth = new Array();
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
	
		var totalPointsAvg100 = 0;
		var totalPointsAvg50 = 0;
		var totalPointsAvg20 = 0;
		var totalPointsAvg100Zone = 0;
		var totalPointsAvg50Zone = 0;
	
		// hold all the bar lines
		var lineList = new Array();
		var lineItemCnt = 0;
	
		// draw price scale list
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
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER - 18)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER + 16;
		} else {
			xStep = (innerWidth - CHART_LEFT_INNER - CHART_RIGHT_INNER - 24)
					/ (numberOfItems - 1);
			firstBarX = origX + CHART_LEFT_INNER + 24;
		}
	
		// display measures
		var xx = firstBarX;
		var yyOpen = 0;
		var yyClose = 0;
		var yyMin = 0;
		var yyMax = 0;
		var volumeHt = 0;
	
		// variable to control the month display
		var prevMonthX = -10; // still in case, there are overlap
		var showHalf = false;
		if (dataType == "Weekly") {
			if (endIndex - startIndex > 102) {
				showHalf = true;
			}
		} else {
			if (endIndex - startIndex > 500) {
				showHalf = true;
			}
		}
	
		// alert("start/end: " + startIndex + "/" + endIndex + ", yStep: " + yStep);
		var chartBarBase = origY - PRICE_BOTTOM + yStep * minPLog;
		for (var i = startIndex; i <= endIndex; i++, xx += xStep) {
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
	
			/*
			 * if ((daily.getSlope() >= 0 && daily.getAcceleration() <= 0) ||
			 * (daily.getSlope() <= 0 && daily.getAcceleration() >= 0)) {
			 * pad.setColor(colorBlue); pad.fillOval((int)xx - 2, (int) (growthY +
			 * 2), 4, 4); //pad.drawRect((int)xx, (int) (growthY - growthHt), (int)
			 * xStep + 1, (int) (growthHt - zeroGrowthHt)); }
			 */
	
			// growth line - bar
			/*
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
			*/
			
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
	
			// lines - not used
			//xPolylineGrowth[i - startIndex] = roundXX;
			//yPolylineGrowth[i - startIndex] = zeroGrowthHt - growthHt;
			xPolylineForce[i - startIndex] = roundXX;
			yPolylineForce[i - startIndex] = zeroForceHt - forceHt;
	
			if (doEMA100) {
				if (logAvg100Array[i] <= maxPLog && logAvg100Array[i] >= minPLog) {
					xPolylineAvg100[totalPointsAvg100] = roundXX;
					yPolylineAvg100[totalPointsAvg100] = Math.round(chartBarBase
							- yStep * logAvg100Array[i]);
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
				if ((logAvg100Array[i] <= maxPLog && logAvg100Array[i] >= minPLog && logAvg50Array[i] < 20)
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
	
					if (logAvg50Array[i] <= maxPLog && logAvg50Array[i] >= minPLog) {
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
	
			// draw the trend/market
			ctx.beginPath();
			var trendHt = forceY + BOX_GAP;
			var marketTrendHt = trendHt + BOX_GAP;
			if (trendColorArray[i] != null && trendColorArray[i] == "G") {
				// draw green
				ctx.strokeStyle = colorGreen;
	
				// ctx.arc(x,y,radius,startAngle,endAngle, false)
				// ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorGreen;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
			} else if (trendColorArray[i] != null && trendColorArray[i] == "R") {
				// do RED
				ctx.strokeStyle = colorRed;
				// ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorRed;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
			} else if (trendColorArray[i] != null && trendColorArray[i] == "B") {
				// do BLUE
				ctx.strokeStyle = colorDarkGreen;
				// ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorDarkGreen;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
			} else if (trendColorArray[i] != null && trendColorArray[i] == "Y") {
				// do YELLOW
				ctx.strokeStyle = colorPink;
				// ctx.arc(roundXX - 2, trendHt - 2, 2, 0, Math.PI * 2);
				ctx.fillStyle = colorPink;
				ctx.fillRect(roundXX - 2, trendHt - 2, 4, 8);
				ctx.strokeRect(roundXX - 2, trendHt - 2, 4, 8);
	
			} else {
				// do nothing
				// pad.setColor(colorBlue);
				// pad.drawLine((int) xx - 1, (int) ballHt, (int) xx + 1, (int)
				// ballHt);
			}
	
			ctx.stroke(); // if want to change style, stroke/fill has to be called
							// to commit
			/*
			 * ctx.beginPath(); // arc is path - need to begin path/stroke! // draw
			 * market flag list if (marketTrendArray != null &&
			 * marketTrendArray.length > (endIndex - startIndex)) { var marketFlag =
			 * marketTrendArray[marketTrendArray.length - dateArray.length + i]; if
			 * (marketFlag == "G") { // draw green ctx.strokeStyle = colorGreen;
			 * ctx.arc(roundXX - 2, marketTrendHt - 2, 2, 0, Math.PI * 2); } else if
			 * (marketFlag == "R") { // do RED ctx.strokeStyle = colorRed;
			 * ctx.arc(roundXX - 2, marketTrendHt - 2, 2, 0, Math.PI * 2); } else if
			 * (marketFlag == "Y") { // do RED ctx.strokeStyle = colorYellow;
			 * ctx.arc(roundXX - 2, marketTrendHt - 2, 2, 0, Math.PI * 2); } else { //
			 * do nothing //pad.setColor(colorBlue); //pad.drawLine((int) xx - 1,
			 * (int) marketBallHt, (int) xx + 1, (int) marketBallHt); } }
			 * 
			 * ctx.stroke();
			 */
	
			// draw month
			if (monthArray[i] != "") {
				ctx.beginPath();
				ctx.strokeStyle = colorBlue;
				ctx.moveTo(roundXX, origY);
				ctx.lineTo(roundXX, origY - SCALE_LENGTH);
				ctx.stroke();
	
				if (showHalf && isEvenMonth(monthArray[i])) {
					// skip this one
				} else {
					// do not get too close to the previous month,
					// when display the year at the end, make sure have enough space
					if (roundXX - prevMonthX > 20
							&& ((roundXX < origX + innerWidth - 12 && monthArray[i].length <= 3) || (roundXX < origX
									+ innerWidth - 16 && monthArray[i].length == 4))) {
						ctx.fillStyle = colorBlue;
						var monthStr = getShortMonthString(monthArray[i]);
						ctx.font = "9pt Helvetica";
						ctx.fillText(monthStr, roundXX - 2 * SMALL_GAP, origY
								- SMALL_GAP);
						prevMonthX = roundXX;
					}
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
	
		//ctx.moveTo(origX, zeroGrowthHt);
		//ctx.lineTo(chartWidth - X_RIGHT, zeroGrowthHt);
	
		ctx.moveTo(origX, zeroForceHt);
		ctx.lineTo(chartWidth - X_RIGHT, zeroForceHt);
		ctx.stroke();
	
		// draw dark zone
		if (doZone) {
			drawDarkZone(xPolylineAvg50Zone, yPolylineAvg50Zone,
					xPolylineAvg100Zone, yPolylineAvg100Zone, true);
		}
		// draw price scale after dark zone
		drawPriceScaleList(scaleList);
	
		// draw the lines
		drawLines(lineList);
	
		ctx.strokeStyle = colorRed;
		// ctx.drawPolyline(xPolylineGrowth, yPolylineGrowth);
		// ctx.drawPolyline(xPolylineForce, yPolylineForce);
	
		// drawPlotData(xPolylineGrowth, yPolylineGrowth);
		// drawPlotData(xPolylineForce, yPolylineForce);
	
		// stroke ---????
		// draw 50-day line
		// ctx.setStroke(2);
	
		// draw average volume
		var volumeStr = getMaxVolumeString();
		var volumeStrLen = ctx.measureText(volumeStr).width;
		var volumeStrY = volumeY - VOLUME_HEIGHT + SMALL_GAP + TEXT_HEIGHT;
		ctx.fillStyle = chartColor;
		ctx.fillRect(origX + SMALL_GAP, volumeStrY - TEXT_HEIGHT + 3,
				volumeStrLen + 8, TEXT_HEIGHT);
		// ctx.beginPath(); // no need to call for the following strokeRect(), but,
		// if call, need to call stroke().
		ctx.strokeStyle = colorBlack;
		ctx.lineWidth = 0.5;
		ctx.strokeRect(origX + SMALL_GAP, volumeStrY - TEXT_HEIGHT + 3,
				volumeStrLen + 8, TEXT_HEIGHT);
	
		ctx.fillStyle = colorBlue;
		ctx.fillText(volumeStr, origX + SMALL_GAP + SMALL_GAP, volumeStrY);
	
		// display EMA lines
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
	
		// draw two boxes
		box1_X = origX + 580;
		box_Y = titleY - 15;
		box_W = 50;
		box_H = 20;
		box2_X = origX + 640;
	
		ctx.fillStyle = colorBlue;
		ctx.fillRect(box1_X, box_Y, box_W, box_H);
	
		ctx.fillStyle = colorRed;
		ctx.fillRect(box2_X, box_Y, box_W, box_H);
	
	}
	
	function drawPlotData(xPoly, yPoly, doRR) {
		if (xPoly.length >= 2) {
			ctx.beginPath();
			if (doRR && doReverse) {
				ctx.moveTo(xPoly[0], chartCenter2Y - yPoly[0]);
				for (var i = 1; i < xPoly.length; i++) {
					ctx.lineTo(xPoly[i], chartCenter2Y - yPoly[i]);
				}
			} else {
				ctx.moveTo(xPoly[0], yPoly[0]);
				for (var i = 1; i < xPoly.length; i++) {
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
				ctx.stroke(); // have to stroke one a time, as the style may
								// change from one to antoher
			}
		}
	}
	
	function drawDarkZone(xPolylineAvg50, yPolylineAvg50, xPolylineAvg100,
			yPolylineAvg100, doRR) {
		// alert("aaa");
		if (xPolylineAvg50 != null && xPolylineAvg50.length > 0
				&& xPolylineAvg100 != null && xPolylineAvg100.length > 0) {
			ctx.beginPath();
			ctx.fillStyle = colorLightDark;
	
			if (doRR && doReverse) {
				ctx.moveTo(xPolylineAvg50[0], chartCenter2Y - yPolylineAvg50[0]);
				for (var i = 1; i < xPolylineAvg50.length; i++) {
					ctx
							.lineTo(xPolylineAvg50[i], chartCenter2Y
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
		// for strokeRect(), no need to call beginPath() and stroke();
		// But, it does not hurt if you call both.
		// If you call just one without another, it is not good.
		// For example, just call ctx.stroke(), it will stroke() something already
		// stroked earler!
		// ctx.beginPath(); ctx.stroke() !!!
		// ctx.rect() is part of the path; ctx.strokeRect() strokes itself.
		ctx.strokeStyle = colorBlack;
		ctx.lineWidth = 0.5;
		ctx.strokeRect(origX, volumeY - VOLUME_HEIGHT, innerWidth, VOLUME_HEIGHT);
		ctx.strokeRect(origX, origY - innerHeight, innerWidth, innerHeight);
	
		// draw option if there is setting
		// ctx.strokeRect(origX, growthY - GROWTH_HEIGHT, innerWidth, GROWTH_HEIGHT);
		ctx.strokeRect(origX, forceY - FORCE_HEIGHT, innerWidth, FORCE_HEIGHT);
	
	}
	
	// This is called to prepare the data
	// either for daily or weekly
	function prepareData() {
		if (canvas == null) {
			// no container - so not to prepare
			return;
		}
		//alert("prepare data");
	
		var dataListStr = ""; // daily or weekly
		var marketListStr = "";
		var doWeekly = document.getElementById("doWeekly").checked;
		if (doWeekly) {
			dataType = "Weekly";
			dataListStr = document.getElementById("weeklyDataStr").value;
			marketListStr = document.getElementById("marketWeeklyFlagList").value;
		} else {
			dataType = "Daily";
			dataListStr = document.getElementById("dailyDataStr").value;
			marketListStr = document.getElementById("marketFlagList").value;
		}
	
		var dataArray = Q.common.mySplit(dataListStr, "#");
		if (dataArray == null || dataArray.length < 1) {
			alert("Content error: " + dataListStr);
			// do something to let the draw to know to not draw
			return;
		}
	
		// initialize the size and index
		dataSize = dataArray.length;
		// endIndex = dataSize - 1;
	
		// make sure the arrays are reset
		resetDataArrays();
	
		//alert("data array: " + dataListStr);
	
		// split the data
		var prevMonth = "";
		for (var i = 0; i < dataArray.length; i++) {
			var nodeInfo = Q.common.mySplit(dataArray[i], "|");
			// format: date | open | close | min | max | logAvg9 | logAvg21 |
			// logAvg50
			// | volume | growth | force | trendColor
			if (nodeInfo == null || nodeInfo.length != 12) {
				// error - need to notify the draw
				alert("format error: " + dataArray[i]);
				return;
			}
	
			dateArray[i] = nodeInfo[0];
			openLogArray[i] = Math.log(parseFloat(nodeInfo[1]));
			closeArray[i] = parseFloat(nodeInfo[2]); // keep non-log just for
														// close price
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
	
			var currentMonth = getMonthNum(nodeInfo[0]);
			if (currentMonth != prevMonth) {
				monthArray[i] = currentMonth;
			} else {
				monthArray[i] = "";
			}
			prevMonth = currentMonth;
		}
	
		if (doWeekly) {
			// need to grap from the daily str
			var dataStr = document.getElementById("dailyDataStr").value;
			firstDate = dataStr.substring(0, dataStr.indexOf("|"));
		} else {
			firstDate = dateArray[0];
		}
	
		marketTrendArray = Q.common.mySplit(marketListStr, "|");
		if (marketTrendArray == null || marketTrendArray.length < 5) {
			alert("Market content error: " + marketListStr);
			// do something to let the draw to know to not draw
			return;
		}
	
		// now, data is ready in all the arrays
		//alert("prepare data - done");
	}
	
	// dateStr format: yyyy/mm/dd
	// if mm = "01", return yyyy; else return mm;
	function getMonthNum(dateStr) {
		var monthNum = "";
		var items = Q.common.mySplit(dateStr, "/");
		if (items != null && items.length == 3) {
			if (items[1] == "01") {
				monthNum = items[0];
			} else {
				monthNum = items[1];
			}
	
		}
	
		return monthNum;
	}
	
	// var maxP, minP, maxVolume, maxGrowth, maxForce
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
			// alert("runP: " + runP.toString().length + ", " + runP);
			if (runP.toString().length > max) {
				max = runP.toString().length;
			}
		}
	
		return max;
	}
	
	function drawPriceScaleList(scaleList) {
		// var scaleList = getPriceRulerList(minP, maxP);
		// alert("drawPriceScaleList()");
	
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
				if (displayHt20 - SMALL_GAP - TEXT_HEIGHT >= origY - innerHeight) {
					ctx.fillStyle = colorRed;
					ctx.font = "9pt Verdana";
					ctx.fillText(runP, origX + SMALL_GAP, displayHt20 - 2);
					prevHt = displayHt;
				}
			}
	
		}
		ctx.lineWidth = 1;
	
	}
	
	function getPriceRulerList(minP, maxP) {
		var list = new Array();
		// alert("getPriceRulerList(): min/max: " + minP + "/" + maxP);
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
	
		// alert("getPriceRulerList(): dispStep: " + dispStep);
	
		var runP = initP;
		var i = 0;
		do {
			if (runP >= minP) {
				list[i] = formatNumber(runP, dispStep);
				i++;
			}
	
			runP += dispStep;
	
		} while (runP < maxP);
	
		// alert("getPriceRulerList() - before return: " + list.length);
	
		return list;
	}
	
	// in some case
	// 4.1 + 0.1 = 4.19999999
	function formatNumber(runP, dispStep) {
	
		var tmpNumStr = new String(runP);
	
		var index = tmpNumStr.indexOf(".");
		if (index > 0 && tmpNumStr.length - index > 2) {
			tmpNumStr = tmpNumStr.substring(0, index + 2);
		}
	
		if (dispStep >= 1) {
			runP = parseInt(tmpNumStr); // If step >= 1, do not use decimal
		} else {
			runP = parseFloat(tmpNumStr); // do not use parseInt()!!!
		}
	
		return runP;
	}
	
	function isEvenMonth(monthNum) {
		if (monthNum != null) {
			if (monthNum == "02" || monthNum == "04" || monthNum == "06"
					|| monthNum == "08" || monthNum == "10" || monthNum == "12") {
				return true;
			}
		}
	
		return false;
	}
	
	function getShortMonthString(monthNum) {
		// alert("getmonth: " + monthNum);
	
		var monthStr = "";
		if (monthNum != null) {
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
			} else {
				monthStr = monthNum;
			}
		}
	
		return monthStr;
	}
	
	function getMaxVolumeString() {
		var ret = formatVolume(maxVolume);
		ret += " (Average = " + formatVolume(avgVolume) + ")";
	
		return ret;
	}
	
	/** format the volume */
	function formatVolume(volume) {
		var displayStr = "";
		if (volume <= 1000) {
			displayStr = parseInt(volume);
		} else if (volume > 1000 && volume < 1000000) {
			displayStr = parseInt(volume / 1000) + "K";
		} else {
			// million
			displayStr = parseInt(volume / 1000000) + "M";
		}
	
		return displayStr;
	}
	
	function clearCanvas() {
		ctx.clearRect(0, 0, canvas.width, canvas.height);
	}
	
	function updateEndIndex3(numNewData) {

        endIndex += numNewData;
        if (endIndex + numToMove < numberOfItems - 1) {
        	endIndex = numberOfItems - 1;
        } else {
        	endIndex += numToMove;
        }
	}
	
	function setHasMoreData(val) {
		hasMoreData = val;
	}
	
	function setInCallFlag(val) {
		inCallFlag = val;
	}
	
	function getDisplayDate() {
		return dateArray[endIndex];
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
	}
	
	// explicitly return public methods when this object is instantiated
	return {
		updateEndIndex  		: updateEndIndex,
		updateEndIndex2 		: updateEndIndex2,
		updateEndIndex3 		: updateEndIndex3,
		updateEndIndexAndDraw 	: updateEndIndexAndDraw,
		inWhichBox 				: inWhichBox,
		init        			: init,
		drawChart   			: drawChart,
		prepareData 			: prepareData,
		clearCanvas 			: clearCanvas,
		setHasMoreData 			: setHasMoreData,
		setInCallFlag 			: setInCallFlag,
		adjustCanvasWidth		: adjustCanvasWidth,
		getDisplayDate			: getDisplayDate
	};

} )( window );

/* The End */