<script type="text/javascript">  
// call set focus
window.onload=startIt;
</script>

<div class="contentwrapper">
<span style="display:table; margin:auto; padding:2px; width:99.6%">

<form name="calculator" onsubmit=""> 
					

<table width="100%" class="gridtable"> 
	<tr><td height="40" align="center" colspan="2"><font class="sub_title">Basic Calculator</font></td></tr>
	
	<tr><th colspan="2">Method 1 - Given maximum of investment and price, find #shares.</th></tr>
				
				<tr>
				<td width="50%">Maximum investiment($)</td><td width="50%"><input type="text" name="maxfund" value="5000"></td>
				</tr>
				<tr>
				<td>Stock Price($) </td><td><input type="text" name="stockPrice1" value=""></td>
				</tr>
				
				<tr>
				<td align="right"><input type="button" name="bt1" value=" Calculate " onClick="calculate1()" class="reg_button">
				# of shares </td><td><input type="text" name="numShares1" value=""></td>
				</tr>
				
				<tr><th colspan="2">Method 2 - Given maximum of loss and stop percent, find #shares.</th></tr>
				
				<tr>
				<td>Maximum loss($)</td><td><input type="text" name="maxloss" value="500"></td>
				</tr>
				<tr>
				<td>Stock Price($)</td><td><input type="text" name="stockPrice2" value=""></td>
				</tr>
				
				<tr>
				<td><input type="radio" name="ckStopType" value="percent" checked> Stop percent (%)
						(<input type="checkbox" name="doLong" checked />Long)
						</td><td><input type="text" name="stopPercent" value="10" onfocus="setoption('percent')"></td>
				</tr>
				
				<tr>
				<td><input type="radio" name="ckStopType" value="price"> Stop price ($)
						</td><td><input type="text" name="stopPrice" value="" onfocus="setoption('price')"></td>
				</tr>
				
				<tr>
				<td align="right"><input type="button" name="bt2" value=" Calculate " onClick="calculate2()" class="reg_button">
				# of shares</td><td><input type="text" name="numShares2" value=""></td>
				</tr>
				
				<tr><th colspan="2">Phil Town's Method to find Sticker Price</th></tr>
				
				<tr>
				<td width="50%">Current EPS($)</td><td width="50%"><input type="text" name="currentEPS" value=""></td>
				</tr>
				
				<tr>
				<td>Estimated EPS Growth Rate(%)</td><td><input type="text" name="epsRate" value=""></td>
				</tr>
				
				<tr>
				<td>Estimated Future PE</td><td><input type="text" name="futurePE" value=""></td>
				</tr>
				
				<tr>
				<td>Minimum Acceptable Rate of Return(%)</td><td><input type="text" name="rateOfReturn" value="15"></td>
				</tr>
								
				<tr>
				<td align="right"><input type="button" name="bt3" value=" Calculate " onClick="calculate3()" class="reg_button">
				Sticker Price($) </td><td><input type="text" name="stickerPrice" value=""></td>
				</tr>
				
				
			<tr><td height="100%" colspan="2">Note: This page is designed to help you to manage your risk.</td></tr>
			
			</table>
	</form>
</span></div>
