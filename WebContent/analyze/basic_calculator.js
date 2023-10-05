// basic_calculator.js

function calculate1() {
	var form = document.calculator;
	var maxFund = form.maxfund.value.trim();
	var price = form.stockPrice1.value.trim();
	
	if (maxFund == "") {
		alert ("Enter the maximum of investment.");
		return false;
	} 
	
	if (price == "") {
		alert ("Enter the stock price.");
		return false;
	} 
	
	if (!Q.common.isValidNumber(maxFund) || !Q.common.isValidNumber(price)) {
		return false;
	}
	
	var priceD = parseFloat(price);
	var maxFundD = parseFloat(maxFund);
	if (priceD <= 0 || maxFundD <= 0) {
		alert("Enter positive numbers only.");
		return false;
	}
	
	var numShares = maxFundD / priceD;
	form.numShares1.value = Math.ceil(numShares);
}

function calculate2() {
	var form = document.calculator;
	var maxLoss = form.maxloss.value.trim();
	var price = form.stockPrice2.value.trim();
	var stopPercent = form.stopPercent.value.trim();
	var stopPrice = form.stopPrice.value.trim();
	var doLong = form.doLong.checked;
	
	if (maxLoss == "") {
		alert ("Enter the maximum of loss.");
		return false;
	} 
	
	if (price == "") {
		alert ("Enter the stock price.");
		return false;
	} 
	
	if (!Q.common.isValidNumber(maxLoss) || !Q.common.isValidNumber(price)) {
		return false;
	}
	
	if (price <= 0 || maxLoss <= 0) {
		alert("Enter positive numbers only.");
		return false;
	}
		
	if (form.ckStopType[0].checked == true) {
		if (stopPercent == "") {
			alert ("Enter the stop percentage.");
			return false;
		} 
		
		if (!Q.common.isValidNumber(stopPercent)) {
			return false;
		}
		
		if (stopPercent <= 0) {
			alert("Enter positive numbers only.");
			return false;
		}
		
		var numShares = (maxLoss * 100) / (price * stopPercent);
		form.numShares2.value = Math.ceil(numShares);
		
		var theStop = 0;
		if (doLong) {
			theStop = Math.ceil(price * (100 - parseInt(stopPercent, 10))) / 100;
		} else {
			theStop = Math.ceil(price * (100 + parseInt(stopPercent, 10))) / 100;
		}
		
		form.stopPrice.value = theStop;
	} else {
		if (stopPrice == "") {
			alert ("Enter the stop price.");
			return false;
		} 
		
		if (!Q.common.isValidNumber(stopPrice)) {
			return false;
		}
		
		if (stopPrice <= 0) {
			alert("Enter positive numbers only.");
			return false;
		}
		
		var priceDiff = price - parseInt(stopPrice, 10);
		if (priceDiff == 0) {
			alert("Stop price should be different from price.");
			return false;
		}
		
		var numShares = maxLoss / priceDiff;
		if (numShares < 0) {
			numShares = - numShares;
		}
		
		form.numShares2.value = Math.ceil(numShares);
		
		var thePercent = Math.ceil(10000 * priceDiff / price) / 100;
		if (thePercent < 0) {
			thePercent = - thePercent;
			form.doLong.checked = false;
		} else {
			form.doLong.checked = true;
		}
		
		form.stopPercent.value = thePercent;
	}
}

function calculate3() {
	var form = document.calculator;
	var currentEPS = form.currentEPS.value.trim();
	var epsRate = form.epsRate.value.trim();
	var futurePE = form.futurePE.value.trim();
	var rateOfReturn = form.rateOfReturn.value.trim();
	
	if (currentEPS == "") {
		alert ("Enter the Current EPS.");
		return false;
	} 
	
	if (epsRate == "") {
		alert ("Enter the EPS Growth Rate.");
		return false;
	} 
	
	if (futurePE == "") {
		alert ("Enter the Future PE.");
		return false;
	} 
	
	if (rateOfReturn == "") {
		alert ("Enter the Acceptable Rate Of Return.");
		return false;
	} 
	
	if (!Q.common.isValidNumber(currentEPS) || !Q.common.isValidNumber(epsRate) 
		|| !Q.common.isValidNumber(futurePE) || !Q.common.isValidNumber(rateOfReturn)) {
		return false;
	}
	
	if (currentEPS <= 0 || epsRate <= 0 || futurePE <= 0 || rateOfReturn <= 0) {
		alert("Enter positive numbers only - this calculator method makes sense only for positive numbers.");
		return false;
	}
		
	var rateP = (1 + epsRate * 0.01) / (1 + rateOfReturn * 0.01);
	var stickerP = futurePE * currentEPS * Math.pow(rateP, 10)
	form.stickerPrice.value = Math.ceil(stickerP);
	
}

function setFocus() {
	var form = document.analyzeForm;
	//form.all["inputVO.ticker"].focus();

}


function setoption(type) {
	var form = document.calculator;
	if (type == "percent") {
		form.ckStopType[0].checked = true;
		form.stopPrice.value = "";
	} else {
		form.ckStopType[1].checked = true;
		form.stopPercent.value = "";
	}
}

/* The End */