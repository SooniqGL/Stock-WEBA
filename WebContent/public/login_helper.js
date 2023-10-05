/**
 * Show a page on overlay, here has close button, 
 * and allow to define the size.
 */
var QQ_loginoverlay = ( function( window, undefined ) {
	var theDialog = null;

	function init(overlay_id, pageWidth, pageHeight) {
		theDialog = $("#" + overlay_id);
		theDialog.dialog({
			autoOpen: false,
			resizable: false,
			// bgiframe: true,  - bgiframe is used to solve IE6 problem, so no need
			width: pageWidth,  // auto, 
	        height: pageHeight,
	        // buttons: { "Close": function() { $(this).dialog("destroy"); }},
	        //buttons: { "Close": function() { theDialog.dialog("close"); }},
			modal: true,
			show: {
				effect: "fade",
				duration: 250
			},
			/*
			hide: {
				effect: "fade",
				duration: 250
			}, */
			
			/*  titlebar/overlay seem have to define in "open" here */
			open: function (event, ui) {
		        $(".ui-dialog .ui-dialog-titlebar").css({
		        	background: "#005588",
	            	border: "solid 1px #005588"
		        });
		        

		        $(".ui-dialog, .ui-widget-content").css({
		        	background: "#005588",
	            	border: "solid 1px #005588" 
		        }); 
		        
		        /* to close the dialog after click the outside */
		        //*
		        $('.ui-widget-overlay').bind("click",function(){
		        	theDialog.dialog("close");
		        });  //*/
		        

		        $(".ui-widget-overlay").css({
		            opacity: 0.5,
		            filter: "Alpha(Opacity=100)"

		            
		            
		            /* unless find a good image, full screen
		        background: "url(../image/clouds.gif) no-repeat center center fixed",
		        "-webkit-background-size": "cover",
		        "-moz-background-size": "cover",
		        "-o-background-size": "cover",
		        "background-size": "cover"
       */
		        }); 
		    } 
		}); 
	}
	
	function load_page(url) {
		theDialog.load(url);
		//$('#myIframe').show();
		//$('#myIframe').attr('src',url);

		theDialog.dialog("option", "position", {
			my: "center",
			at: "center",
			of: window
		});
		if (theDialog.dialog("isOpen") == false) {
			theDialog.dialog("open");
		}
	}
	
	/** in general, just bring up the content */
	function show_div() {

		theDialog.dialog("option", "position", {
			my: "center",
			at: "center",
			of: window
		});
		if (theDialog.dialog("isOpen") == false) {
			theDialog.dialog("open");
		}
	}
	
	function close_div() {
		if (theDialog.dialog("isOpen") == true) {
			theDialog.dialog("close");
		}
	}
	
	// explicitly return public methods when this object is instantiated
	return {
		init  		: init,
		load_page 	: load_page,
		show_div	: show_div,
		close_div   : close_div
	};

} )( window );

