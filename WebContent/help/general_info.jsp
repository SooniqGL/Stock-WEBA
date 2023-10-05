<!DOCTYPE HTML>

<%@ taglib uri="/WEB-INF/struts-html.tld" prefix="html" %>
<%@ taglib uri="/WEB-INF/struts-bean.tld" prefix="bean" %>
<%@ taglib uri="/WEB-INF/struts-logic.tld" prefix="logic" %> 
<%@page errorPage="/error.jsp" %>
<%@ page 
language="java"
contentType="text/html; charset=ISO-8859-1"
pageEncoding="ISO-8859-1"
%> 

<HTML>
<HEAD>
<META http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<META name="GENERATOR" content="IBM WebSphere Studio">
<META http-equiv="Content-Style-Type" content="text/css">
<LINK href="../theme/Master.css" rel="stylesheet" type="text/css">
<LINK href="../theme/main_style.css" rel="stylesheet" type="text/css">
<TITLE>general_info.jsp</TITLE>  

<%  
response.setHeader("Cache-Control","no-cache"); //HTTP 1.1
response.setHeader("Cache-Control","no-store"); //HTTP 1.1
response.setHeader("Pragma","no-cache"); //HTTP 1.0
response.setHeader("Expires", "-1");
response.setHeader("Expires", "Mon, 01 Jan 1990 12:00:00 GMT"); //prevents caching at the proxy server
%> 

<jsp:useBean id="security" scope="session" type="com.greenfield.common.object.user.User" />
</HEAD>
<BODY class="page_back">
<div id="outcontainer">
        <div id="maincontainer">

            <div id="topsection"><div class="innertube2">
                    <jsp:include page="../base/topmenu.jsp">
                        <jsp:param name="subtitle" value="Help" />
                        <jsp:param name="fname" value="<%= security.getFname() %>" />
                        <jsp:param name="lname" value="<%= security.getLname() %>" />  
                    </jsp:include>
            </div></div>

            <div id="contentwrapper">
            <div id="contentcolumn">
                <div class="innertube2">
            
            
            <table width="100%" cellpadding="0" cellspacing="0" class="light_back"> 
	
	<tr><td height="40">&nbsp;</td><td><h1>Best Advice</h1></td></tr>
	<tr><td colspan="2" height="60">
	<ol>
	<li><font color="#009955"><b>Majority people lose money since they do not have consistent reasoned approach to the market; they are gambling randomly.</b></font></li>
    <li><font color="#009955"><b>Shallow men believe in luck.  Strong men believe in cause and effect.  - Ralph W. Emerson</b></font></li>
    <li><font color="#009955"><b>Make money, not excuses.</b></font></li>
	</ol>
	</td></tr>
	<tr><td>&nbsp;</td><td><a name="search"></a><h1>About Search</h1></td></tr>
	<tr> 
		<td colspan="2"> 
		 
	<table width="100%"><tr><td>
		<ul> 
			<li>The "Linear" search is trying to find the stocks going straight up and down.  
			This is the place to find strong stocks which may go up for a long time as big
			winners.</li>
			<li>The "Break" search is trying to find the stocks that have just broken the trend lines
			with great volume.  It is believed that a break up is the start of an up trend.  A
			break down is the start of a down trend.  It is usually true.  But make sure the
			market is on your side.  And always protect your positions for a fake break out.</li>
			<li>The "Momentum" search is trying to find the stocks that have just started 
			next trend.  So that traders can catch the trend (possibly short term) at the beginning.
			You may need to know the greater trend of the stock to enter such momentum trade.
			Generally, if the stock is going up in a major trend, you may try to find the 
			long momentum opportunities about it.  If you are doing it against the major trend,
			you may need to exit quickly if the stock is moving against you.</li>
			<li>Again, as always, general market condition is
			usurally critical for your trades.  That means, for an up market, our up trend 
			search returns make more chances to gain - even we are trying to make our
			search stable in various market conditions.
			</li>
			<li>Always protect your positions with stop loss orders.</li>
			<li>We only search the stocks that have the average volume &gt;= 100K.</li>
			<li>For linear model, we ask the growth per month at least 10 percent.</li>
			<li>About RSquare --
			 RSquare is a number between 0 and 1.  If it is 1, the chart is exactly a straight line.
			 The closer to 1 the closer the chart to the line.  In linear search, we are 
			 asking our search engine to find stocks with RSquare closing to 1.
			 </li>
		 	<li>About Correlation Index --
		 	Correlation Index is a number between 0 and 1.  If it is 1 or close to 1, the trend
		 	is close to the market trend.  It tells how the stock is responding to the general stock market. 
		 	</li>
		</ul>
	</td></tr></table>

	</td></tr>   
	<tr><td>&nbsp;</td><td><a name="analysis"></a><h1>About Analysis</h1></td></tr>
	<tr><td colspan="2">
		<table width="100%"><tr><td><br>
			<ul>
				<li>In this analysis page, we provide regular bar chart and volume information for a stock.  
				We display two of our indicators at the bottom of the chart.  These two
				indicators are invented by our team.  They are named "Growth" and "Force".
				</li> 
				<li>The "Growth" is calculated from the price information only.  It is very senstive
				to the trend of the stock.  When stock is going up, it is green and above the zero line.
				If the stock is going down, it is red and bellow the zero line.</li>
				<li>The "Force" is calculated from the price and volume information.  It tells
				how the stock is accumulated or distributed.  When it is green and above the zero,
				we believe the stock is accumulating.  When it is red and bellow the zero line, 
				we believe the stock is having more sellings.</li>
				<li>When both indicators are agree in signs, we display little dot for that
				date.  A green dot means a buy signal.  A red dot means a sell signal.</li>
				<li>Generally, we suggest buy if the green dot appears and sell (or sell short)
				if the red dot appears.  Since the market is so violent, there is possibly some
				faked signals.  You may need some general knowledge to avoid such 
				false signals.  As a good practice, you may not buy long at the very first green dot.  You may
				like to wait some confirmation to enter.  For short, you may need to see
				if there is indeed some sell off going on.</li>
				<li>Had better make sure the general market indexes have the same signal signs as the
				stock you are going to buy or short.</li>
				<li>You may learn that in most cases holding a position against the dot sign in the chart
				is not so nice.  So, do not do it.</li>
			</ul>
			</td></tr>
		</table>
			</td></tr>
        <tr><td>&nbsp;</td><td><a name="data"></a><h1>About Data</h1></td></tr>
	<tr><td height="5" colspan="2"><ul><li>We currently only have stock data for these tickers in the Nasdaq and New York markets.  About 6000+ stocks.</li>
	<li>We update our database every night at 9:30PM eastern time.
	It should be ready no later than 10PM.</li>
	</ul></td></tr>
	
	</table>
        </div>
            </div>
            </div>

            <div id="leftcolumn">
            <div class="innertube"><%--@include file="../base/left2.jsp" --%>
                <a href="#search" class="sublink">About Search</a><br/>
                <a href="#analysis" class="sublink">About Analysis</a><br/>
                <a href="#data" class="sublink">About Data</a>
            </div>
            </div>

            <%@include file="../base/footer.jsp" %>

        </div>
    </div>              
</BODY>
</HTML>
