<style>
<!--
/*  SECTIONS  */
.examfaqsection {
	clear: both;
	/* padding: 0px; */
	margin-top: 20px; 
	width: 97%;
	/* border: 1px solid #dfdfdf; */
}

.notes {
	width: 80%;
	margin-top: 50px;
}


-->
</style>

<style type="text/css">
<!--
.button_lrg {
   width: 65px;
}

.button_lrg2 {
   width: 90px;
}

.button_lrg3 {
   width: 110px;
}

.button_sml {
   width: 40px;
}
-->
</style>

<div class="contentwrapper2"><div style="margin:2px;">
<h3>About Exam</h3>


	<div class="examfaqsection group">
    		<div class="col_first span_tiny_of_2">&nbsp;Purpose</div>
    		<div class="col span_huge_of_2">For technical followers, reading chart is the key to be successful in the trading business.  However, 
    		in most online tools, we can see the chart in the current moment. If we cannot move back to old dates to see the chart, we will not 
    		understand how a break out look like, how a new high look like.  To recall what we saw for old dates, we design the way to 
    		let traders view the chart from history stand points and simulate it.  Trade records are also stored in our DB so you can view any
    		time later.  Practice and think, is the key to success.
						
			</div>
   		
    		<div class="col_first span_tiny_of_2">&nbsp;Is it for you?</div>
    		<div class="col span_huge_of_2">
    			Yes, if you are technical follower.  Yes, if you do not have the professional eyes to see the charts.  Yes, if you want to enhance
    			your skills to read the charts.  We have 20 years of US market data.  Real data, so enjoy.
    		</div>
    </div>

</div></div>

<div class="contentwrapper2"><div style="margin:2px;">
<h3>Technical Instructions</h3>


	<div class="aboutexamsection group">
    		<div class="col_first span_tiny_of_2">&nbsp;<input type="button" name="bt" value=" Self Select " class="button_lrg3"></div>
    		<div class="col span_huge_of_2">	
				Enter one ticker in the input box, by clicking this button, the system will load the stock data
				for this ticker.  If the ticker is not found in our system, error message will be shown below.		
			</div>
   		
    		<div class="col_first span_tiny_of_2">&nbsp;<input type="button" name="bt" value=" Auto Select " class="button_lrg3"></div>
    		<div class="col span_huge_of_2">
    			If you forget your password, you can reset password.  We will send you a temporary password to login to our site.Do not need to enter any ticker.  The system will select one randomly for you
to test your ability to look at the chart.  The system does try to eliminate these thin volume tickers and penny stocks.  If you like to do thin
and penny, you can select by yourself.
    		</div>
    		
  
                  
                     
            <div class="col_first span_tiny_of_2">&nbsp;<input type="button" id="start_over" name="start_over"  value="Start Over" class="button_lrg2" /></div>
    		<div class="col span_huge_of_2">
    			Click this button will reset the test and do it from beginning for this current stock.
    		</div> 
    		
    		<div class="col_first span_tiny_of_2">&nbsp;<input type="button" id="button_right_xd" name="button_right_xd"  value=">" class="button_sml" /></div>
    		<div class="col span_huge_of_2">
    			Click this button once to let chart begin to move automatically, one date a time.  Click again will stop the movement.
    		</div> 
    		
    		<div class="col_first span_tiny_of_2">&nbsp;<input type="button" id="button_right_xd" name="button_right_xd"  value=">>" class="button_sml" /></div>
    		<div class="col span_huge_of_2">
    			Click this button once to let chart begin to move automatically, five dates a time.  Click again will stop the movement.  Similar to ">" button, but faster.
    		</div>        
            
            <div class="col_first span_tiny_of_2">&nbsp;<input type="button" id="button_right_1d" name="button_right_1d"  value="> 1d" class="button_lrg" />
                  <input type="button" id="button_right_5d" name="button_right_5d"  value="> 5d" class="button_lrg" />
                  <br/>&nbsp;<input type="button" id="button_right_10d" name="button_right_10d" value="> 10d" class="button_lrg"  />
                  <input type="button" id="button_right_1m" name="button_right_1m"  value="> 1M" class="button_lrg" />         
             </div>
    		<div class="col span_huge_of_2">
    			1) Click these buttons once to move the chart to the right corresponding days.  2) Can let mouse stay on these buttons, and turn the mouse wheel to move the chart to the right.
    			Please notes, the exam chart is one directly only.  It is not designed to move back in the middle.  So, mouse wheel either direction will move the chart to the right.
    		</div>  
    		
    		<div class="col_first span_tiny_of_2">&nbsp;Stop Options</div>
    		<div class="col span_huge_of_2">
    			You can select three options on stop: 1) None - means no stop is added; 2) Regular - it is fix priced stop; 3) Trailing - it is 
    			a moving stop, by percentage.  If you select stop options before begin a position (Long/Short), the position will take the
    			stop effectively.  If after position is entered, you want to change the stop options, you need to push "Apply" button to apply
    			the stop the current position.
    		</div>  
    		
            <div class="col_first span_tiny_of_2">&nbsp;<input type="button" id="button_long" name="button_long"  value="Long" class="button_lrg"  />
                    <input type="button" id="button_short" name="button_short"  value="Short" class="button_lrg"  />
                    </div>
    		<div class="col span_huge_of_2">
    			Click these buttons to start a position.
    		</div>        
                    
            <div class="col_first span_tiny_of_2">&nbsp;<input type="button" id="button_close" name="button_close"  value="Close It" class="button_lrg" />
                     </div>
    		<div class="col span_huge_of_2">
    			Click this button to close a position.  This button is enabled only after a position is open.  After a position is closed, we show the
    			statistics on the bottom of the screen.  At the same time, we send records to DB to keep for your personal references later.
    		</div>  
    		
    		<div class="col_first span_tiny_of_2">&nbsp;<input type="checkbox" id="doReverse" name="doReverse" />Reverse
                     </div>
    		<div class="col span_huge_of_2">
    			Reverse the chart and see it up side down.
    		</div>  
    		
    		<div class="col_first span_tiny_of_2">&nbsp;<input type="checkbox" id="doTradePoint" name="doTradePoint" />Hint
                     </div>
    		<div class="col span_huge_of_2">
    			Display GREEN dots on the bottoms of the chart to show if any day is profitable or not if a long position is bought and have 10% 
    			trailing stop with it.  To do this, we "peak" the value that is not shown yet.  This is just another effort to help us to look
				at the chart and think what about the future might be.
    			</div>        
                    
                    
                               
                     
                     
                     
                     
                     
                     
                     
                     
                     
                     
                     
                     
                     
                     
    </div>

</div></div>