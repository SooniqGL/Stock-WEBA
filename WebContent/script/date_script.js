// date_script.js
// Some common functions used in the client side
// to manupulate the date strings and objects.
// 
// cntDate - date object
// numberMonth - +/- number of months to add
// assume: numberMonth >= - 12 and <= 12

if (typeof Q == "undefined" || !Q) {
        var Q = {};
}

Q.namespace("date");


Q.date.addMonthToDate = function(cntDate, numberMonth) {

	var day = cntDate.getDate();
		
	var month = cntDate.getMonth() + numberMonth + 1;
	var year = cntDate.getFullYear();

 //alert("Add month, Month: " + month + ", day: " + day + ", year: " + year
 //	+ ", num: " + numberMonth);
	
	if (month <= 0) {
		month += 12;
		year -= 1;
	} else if (month > 12) {
		month -= 12;
		year += 1;
	}
	
	// adjust day
	var numDays = Q.date.getDaysInMonth(month, year) ;
	if (day > numDays) {
		day = numDays;
	}

 //alert("Result: " + year + "/" + month + "/" + day);
 		
	return year + "/" + month + "/" + day;
};

// Note: 
// numberDay - +/-, from -30 to 30  (no good for others)
Q.date.addDayToDate = function(cntDate, numberDay) {
	var day = cntDate.getDate() + numberDay;	
	var month = cntDate.getMonth() + 1;
	var year = cntDate.getFullYear();

//alert("Add day, Month: " + month + ", day: " + day + ", year: " + year
 //	+ ", num: " + numberDay);
 		
	// Adjust the numbers if the day is out 
	// the limit of the current month or year.
	if (month == 1) {
		if (day > 0) {
			// no year change
			if (day > 31) {
				// need month change
				month = 2;
				day -= 31;
			}
		} else {
			// need year change
			day += 31;
			month = 12;
			year -= 1;
		}
	} else if (month == 12) {
		if (day > 31) {
			// need year change
			day -= 31;
			month = 1;
			year += 1;
		} else {
			// do not need year change
			if (day <= 0) {
				day += 30;
				month = 11;
			}
		}
	} else {
		// year will not be changed
		var numDays = Q.date.getDaysInMonth(month, year) ;
		if (day > numDays) {
			day -= numDays;
			month += 1;
		} else if (day <= 0) {
			month -= 1;
			var prevNumDays = Q.date.getDaysInMonth(month, year) ;
			day += prevNumDays;
		} 
	}
//alert("Result: " + year + "/" + month + "/" + day);
	
	return year + "/" + month + "/" + day;
};


Q.date.isDigit = function(ch) {
	return(ch >= '0' && ch <= '9');
};

Q.date.getDaysInMonth = function(intMonth, intYear) {
	var arrDaysInMonth = new Array(31,28,31,30,31,30,31,31,30,31,30,31);
	var intCount = arrDaysInMonth[intMonth-1];
 	if(intMonth == 2) {
		if((intYear%4) == 0 ) {	// post-2000 leap year - ignore 2100 etc.
  			intCount++;
  		}
 	}
 	
	return intCount;
};

// Please note the common.js has the isValidDate already.
Q.date.isValidDate2 = function(intDate, intMonth, intYear) {
	if( (intYear > 2050) || (intYear < 1900) ) {return false;}
	
 	if( (intMonth > 12) || (intMonth < 1) ) {return false;}
 	
	if(intDate < 1) {return false;}
	
	var intMaxDate = Q.date.getDaysInMonth(intMonth, intYear);
	
	if(intDate > intMaxDate) {return false;}
	return true;
};

// From String to Date object
Q.date.parseDate = function(theDate) {
	if (theDate == null) {
		return null;
	}

 //alert("parseDate input: " + theDate);
	var index1 = theDate.indexOf("/");
	var month = "";
	var day = "";
	var year = "";
	var str = "";
	
	if (index1 > -1) {
		year = theDate.substring(0, index1);
		str = theDate.substring(index1 + 1);
	} else {
		// error - should not happen, after validation
		return null;
	}
	
	var index2 = str.indexOf("/");
	if (index2 > -1) {
		month = str.substring(0, index2);
		day = str.substring(index2 + 1);
	} else {
		// error - should not happen, after validation
		return null;
	}

        //alert("parseDate day: " + day + ", month: " + month + ", year: " + year);

	// Note: parseInt("03") = 0!
        // 1) Very important! Set Month/year first, then the day;
        // If you set the day first, if the current month has fewer days, you lose days.
        // for example, 2009/7/31 is input, current month is June, then if you set day first, the result will be 2009/7/1!
        // 2) Need to set Date to 1 first.  Other wise, if # days in current month > input month days,
        // Then, the month will be hidely increase by 1, after you set the month again, result is not good.
        // For example, on 2010/6/29, try tp parse("2010/2/25"), the result will be: 2010/3/25!
	var dateObj = new Date();
        dateObj.setDate(1);  // so after month set, date will not over-flow
	dateObj.setMonth(month - 1);
	dateObj.setFullYear(year);
        dateObj.setDate(day);
//alert("parseDate output: " + dateObj.getMonth() + ", " + dateObj.getDate() + ", " + dateObj.getFullYear());
	return dateObj;
	
};

// cntDate is a Date,
// numberMonth is the number of months to add.
// If numberMonth == 0, then return the current date


Q.date.getLongDateString = function(cntDate) {	
	return Q.date.getLongMonth(cntDate.getMonth() + 1) + " " + cntDate.getDate() + ", " + cntDate.getFullYear();
};


Q.date.getLongMonth = function(intMonth) {
	var monthStr = "";
	
	if (intMonth == 1) {
		monthStr = "January";
	} else if (intMonth == 2) {
		monthStr = "February";
	} else if (intMonth == 3) {
		monthStr = "March";
	} else if (intMonth == 4) {
		monthStr = "April";
	} else if (intMonth == 5) {
		monthStr = "May";
	} else if (intMonth == 6) {
		monthStr = "June";
	} else if (intMonth == 7) {
		monthStr = "July";
	} else if (intMonth == 8) {
		monthStr = "August";
	} else if (intMonth == 9) {
		monthStr = "September";
	} else if (intMonth == 10) {
		monthStr = "October";
	} else if (intMonth == 11) {
		monthStr = "November";
	} else if (intMonth == 12) {
		monthStr = "December";	
	} else {
		alert("Error in the month: " + intMonth);
	}
	
	return monthStr;
};

// Returns -1, 0, 1, or 2 (error)
// compare two date strings.
Q.date.compareDates = function(startDate, endDate) {
	var date1 = Q.date.parseDate(startDate);
	var date2 = Q.date.parseDate(endDate);
	var ret = 0;
	
	
	if (date1 == null || date2 == null) {
		alert("date format error (yyyy/mm/dd) for: " + startDate + " or " + endDate);
		ret = 2;
	} else {
		var date1M = date1.getTime();
		var date2M = date2.getTime();
		
		if (date1M < date2M) {
			ret = -1;
		} else if (date1M == date2M) {
			//alert("here ..." + date1M + "..." + date2M);
			ret = 0;
		} else {
			ret = 1;
		}
	}
	
	//alert("comparing:  " + startDate + " with " + endDate + "\nresult: " + ret);
	
	return ret;
	
};

/* The End */