// draw_login.js

var colorBlack = "#000000";
var colorWhite = "#ffffff";
var colorRed = "#ff0000";
var colorGreen = "#00ff00";
var colorBlue = "#0000ff";
var colorDark = "#aaaaaa";
var colorYellow = "#ffff00";
var imageColor = "#cccccc";
var chartColor = "#fffff0";

var MAX_MOVE = 3;

var moveCnt = 0;
var titleCnt = 0;
var colorCnt = 0;

var lastXDir = "up";
var lastYDir = "up";

function init() {
//alert("init");
	var cnv = document.getElementById('chart');
	if (cnv != null) {		 
		// get the scale
		chartX = parseInt(dd.elements.chart.x, 10);  // fixed for all to remember the original location
		chartY = parseInt(dd.elements.chart.y, 10);
		chartWidth = parseInt(dd.elements.chart.w, 10);  // original size - if not change; we may change this
		chartHeight = parseInt(dd.elements.chart.h, 10);
		
		// setup the graphics
		jg = new jsGraphics("chart"); // or jsGraphics(cnv);
		
		// build wordlist
		wordList[0] = "Welcome!";
		wordList[1] = "Make Your Dream True!";
		wordList[2] = "Have a Nice Day!";
		wordList[3] = "Good Luck!";
		wordList[4] = "Take It Easy!";
		wordList[5] = "Bear Up Under Loss.";
		wordList[6] = "Fight the Bitterness of Defeat.";
		wordList[7] = "Fight the Weakness of Grief.";
		wordList[8] = "Be a Victor Over Anger.";
		wordList[9] = "Smile When Tears Are Close.";
		wordList[10] = "Resist Disease and Evil Men.";
		wordList[11] = "Resist Base Instincts.";
		wordList[12] = "Hate Hate and Love Love.";
		wordList[13] = "Go On When It Would Seem Good To Die.";
		wordList[14] = "Seek Ever the Glory and the Dream.";
		wordList[15] = "Look Up With Unquenchable Faith.";
		wordList[16] = "Speak Less.";
		wordList[17] = "Keep Your Promise.";
		wordList[18] = "Be Nice To People.";
		wordList[19] = "Adapt Or Die.";

		     
		colorList[0] = "#ff0000";
		colorList[1] = "#006699";
		colorList[2] = "#669900";
		colorList[3] = "#0000ff";
		colorList[4] = "#0055ff";
		colorList[5] = "#ff00ff";
		colorList[6] = "#ff5500";
	} else {
		// alert("chart div is null");
	}
}
	
function startIt(drawTitle) {
	if (jg == null) {
		return;
	}
	
	moveCnt = 0;
	if (drawTitle == true) {
		// make sure to clear it
		jg.clear();
	
		//	page.setColor(colorDark);
		//	page.drawRect(minX, minY, maxX - minX, maxY - minY);

		var title = "";
		if (titleCnt < wordList.length) {
			title = wordList[titleCnt];
			titleCnt ++;
		} else {
			title = wordList[0];
			titleCnt = 1;
		}
		
		var color = "";
		if (colorCnt < colorList.length) {
			color = colorList[colorCnt];
			colorCnt ++;
		} else {
			color = colorList[0];
			colorCnt = 1;
		}
		
		jg.setColor(color);
		jg.setFont("verdana,geneva,sans-serif", "20px", Font.BOLD);
		jg.drawString(title, 80, 20);
		jg.paint();
		
	}
	
	setTimeout("startIt(true)", 3000);
	//moveIt();
	
}

function moveIt() {
	if (moveCnt >= MAX_MOVE) {
		
		// restart it
		startIt(true);
		return;
	}
	
	var nextX = 0.0;
	var nextY = 0.0;
	
	// update
	moveCnt ++;
	
	// move it
	if (lastXDir == "up") {
		nextX = prevX + xStep;
	} else {
		nextX = prevX - xStep;
	}
	
	if (lastYDir == "up") {
		nextY = prevY + yStep;
	} else {
		nextY = prevY - yStep;
	}
	
	// do not pass the boundaries
	if (nextX > maxX) {
		nextX -= 2 * (nextX - maxX); 
		lastXDir = "down";
	}
	
	if (nextX < minX) {
		nextX += 2 * (minX - nextX); 
		lastXDir = "up";
	}
	
	if (nextY > maxY) {
		nextY -= 2 * (nextY - maxY); 
		lastYDir = "down";
	}
	
	if (nextY < minY) {
		nextY += 2 * (minY - nextY); 
		lastYDir = "up";
	}
	
	// move it
	dd.elements.chart.moveTo(nextX, nextY);
		
	// update position
	prevX = nextX;
	prevY = nextY;
	
	// setup next move
	if (runFlag == true) {
		setTimeout("moveIt()", 1000);
	}

	
}

/* The End */
