
/** use jQuery UI to display modal Dialog box, load images.  Hide the close button.
 * 
 *  1) Include jQuery, jQuery UI and theme files
 *  2) In JSP, has <div> tag inserted;
 *  3) In document ready, call init() to initialize the dialog;
 *  4) Attach the show_image/show_div to the buttons to trigger the dialog; 
 *  5) chartoverlay - to show chart, or loaded content.  "No close button."
 */
var QQ_chartoverlay = ( function( window, undefined ) {
	var theDialog = null;

	function init(overlay_id) {
		theDialog = $("#" + overlay_id);
		theDialog.dialog({
			autoOpen: false,
			resizable: false,
			//bgiframe: true,
			//width: 640,
	        height: 480,
	        //buttons: { "Close": function() { $(this).dialog("destroy"); }},
	        //buttons: { "Close": function() { theDialog.dialog("close"); }},
			modal: true,
			width: "auto",
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
		        	background: "#005533",
	            	border: "solid 1px #003322"
		        });
		        
		        /* make same color as the image, no border. */
		        $(".ui-dialog, .ui-widget-content").css({
		        	background: "#dfdfdf",
	            	/* border: "solid 1px #003322" */
		        });
		        
		        /* to close the dialog after click the outside */
		        $('.ui-widget-overlay').bind("click",function(){
		        	theDialog.dialog("close");
		        });  
		        
		        /*
		        $(".ui-widget-overlay").css({
		            opacity: 0.5,
		            filter: "Alpha(Opacity=100)",
		            backgroundColor: "black"
		        }); */
		    } 
		}); 
	}
	
	
	/* The jQuery dialog may not be good to call ${"#xx"}.dialog multiple times 
	when each time to call $.  So use a global variable to do it. */
	function show_image(url, imgwidth, imgheight) {

		//alert("url: " + url);

		if (imgwidth == null || imgwidth == "") {
			imgwidth = "600px";
		}
		
		if (imgheight == null || imgheight == "") {
			imgheight = "400px";
		}
		theDialog.html("<img src='" + url + "' width='" + imgwidth + "' height='" + imgheight + "'>");
		
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
		show_image 	: show_image,
		show_div	: show_div,
		close_div   : close_div
	};

} )( window );

/**
 * Show a page on overlay, here has close button, 
 * and allow to define the size.
 */
var QQ_pageoverlay = ( function( window, undefined ) {
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
		        	background: "#005533",
	            	border: "solid 1px #003322"
		        });
		        
		        /*
		        $(".ui-dialog, .ui-widget-content").css({
		        	background: "#dfdfdf",
	            	border: "solid 1px #003322" 
		        }); */
		        
		        /* to close the dialog after click the outside */
		        //*
		        $('.ui-widget-overlay').bind("click",function(){
		        	theDialog.dialog("close");
		        });  //*/
		        
		        /*
		        $(".ui-widget-overlay").css({
		            opacity: 0.5,
		            filter: "Alpha(Opacity=100)",
		            backgroundColor: "black"
		        }); */
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



/* The End */