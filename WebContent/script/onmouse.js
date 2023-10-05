/*
 * Scripts to setup the focus overlay to a page.
 * 1) call run_after_body() at page load;
 * 2) Include overlay div and overlay cloak div, check style sheet for them;
 * 3) call show_200, or show_div to display the overlay;
 */

/*
div#overlay_cloak {position:absolute;top:0;left:0;height:100%;width:100%;background-color:#000000;filter:alpha(opacity=65);-moz-opacity:.50;opacity:.50;visibility:hidden;z-index:90;}
div#overlay_cloak2 {position:absolute;top:0;left:0;height:100%;width:100%;background-color:#002222;filter:alpha(opacity=65);-moz-opacity:.50;opacity:.50;visibility:hidden;z-index:90;}

iframe.cloak_iframe {width:100%;height:100%;z-index:-1;filter:mask();}

div.overlay_content {position:absolute;visibility:hidden;background-color:#fffffc;left:-3000;z-index:100;}
*/
/*
div#overlay_cloak {position:fixed;top:0;left:0;min-height:100%;height:100%;width:100%;background-color:#333;filter:alpha(opacity=50);-moz-opacity:.50;opacity:.50;visibility:hidden;z-index:90;}

iframe.cloak_iframe {width:100%;height:100%;z-index:-1;filter:mask();}
iframe.overlay_iframe {position:absolute;z-index:-1;filter:mask();width:100%;height:550px;}
iframe.nav_iframe {position:absolute;z-index:-1;filter:mask();width:100%;height:400px;}
iframe#info_iframe {position:absolute;filter:mask();width:0;height:0;}
iframe.shim {position:absolute;bottom:0;left:0;z-index:-1;filter:mask();width:183px;height:68px;}
*/

// used for onmouse function
var obj_float_div = false;
var cloak_elm_div = false;
var show_delay = null;
        

function run_after_body() {
   //document.write('<textarea id="gate_to_clipboard" style="display:none;"></textarea>');
   document.onmousemove = document_onmousemove;
   if (window.onscroll) window.onscroll = hide_div();
   // document.write('<div class="float" id="div_200" style="left: -3000px; background: #ffffff;"><img id="img_200" class="border_b" width="700" height="350"></div>');
  
}

function get_obj(id_name) {
   if (document.getElementById) {
      return document.getElementById(id_name);
   } else if (document.all) {
      return document.all[id_name];
   } else {
      return null;
   }
}

function document_onmousemove(e) {

   if ( !obj_float_div ) return;

   var pos_X = 0, pos_Y = 0;
   if ( !e ) e = window.event;
   if ( e ) {
      if ( typeof(e.pageX) == 'number' ) {
         pos_X = e.pageX; pos_Y = e.pageY;
      } else if ( typeof(e.clientX) == 'number' ) {
         pos_X = e.clientX; pos_Y = e.clientY;
         if ( document.body && ( document.body.scrollTop || document.body.scrollLeft ) && !( window.opera || window.debug || navigator.vendor == 'KDE' ) ) {
            pos_X += document.body.scrollLeft; pos_Y += document.body.scrollTop;
         } else if ( document.documentElement && ( document.documentElement.scrollTop || document.documentElement.scrollLeft ) && !( window.opera || window.debug || navigator.vendor == 'KDE' ) ) {
            pos_X += document.documentElement.scrollLeft; pos_Y += document.documentElement.scrollTop;
         }
      }
   }
 
   var scroll_X = 0, scroll_Y = 0;
   if ( document.body && ( document.body.scrollTop || document.body.scrollLeft ) && !( window.debug || navigator.vendor == 'KDE' ) ) {
      scroll_X = document.body.scrollLeft; scroll_Y = document.body.scrollTop;
   } else if ( document.documentElement && ( document.documentElement.scrollTop || document.documentElement.scrollLeft ) && !( window.debug || navigator.vendor == 'KDE' ) ) {
      scroll_X = document.documentElement.scrollLeft; scroll_Y = document.documentElement.scrollTop;
   }
 
   var win_size_X = 0, win_size_Y = 0;
   if (window.innerWidth && window.innerHeight) {
      win_size_X = window.innerWidth; win_size_Y = window.innerHeight;
   } else if (document.documentElement && document.documentElement.clientWidth && document.documentElement.clientHeight) {
      win_size_X = document.documentElement.clientWidth; win_size_Y = document.documentElement.clientHeight;
   } else if (document.body && document.body.clientWidth && document.body.clientHeight) {
      win_size_X = document.body.clientWidth; win_size_Y = document.body.clientHeight;
   }
 
   pos_X += 15; pos_Y += 15;
 
   if (obj_float_div.offsetWidth && obj_float_div.offsetHeight) {
      //if (pos_X - scroll_X + obj_float_div.offsetWidth + 5 > win_size_X) pos_X -= (obj_float_div.offsetWidth + 25);
      // if (pos_Y - scroll_Y + obj_float_div.offsetHeight + 5 > win_size_Y) pos_Y -= (obj_float_div.offsetHeight + 20);
      // if (pos_Y - scroll_Y + obj_float_div.offsetHeight + 5 > win_size_Y) pos_Y -= (pos_Y - scroll_Y + obj_float_div.offsetHeight -  win_size_Y + 20);
      pos_X = scroll_X + (win_size_X - obj_float_div.offsetWidth) / 2;
      pos_Y = scroll_Y + (win_size_Y - obj_float_div.offsetHeight) / 2 + 20;
   }

    // adjust the overlay position
    obj_float_div.style.left     = pos_X + "px";
    obj_float_div.style.top      = pos_Y + "px";

    //alert("adjust size");

    // adjust the cloak position and size
    if ( cloak_elm_div ) {
        var cloak_left  = scroll_X - 1;
        var cloak_top   = scroll_Y - 1;
 
        //alert("adjust size, cloak: " + cloak_left + ", " + cloak_top + ", Content: " + pos_X + ", " + pos_Y);
        
        cloak_elm_div.style.left    = cloak_left + "px";
        cloak_elm_div.style.top     = cloak_top + "px";
    }
    

    //alert("adjust size 2");

 
} 

function show_200(img_src, overlay_id, cloak_id) {
   if (show_delay) {
      //alert("delay");
      clearTimeout(show_delay); show_delay = null;
   } else {
      //alert("not delay");
      obj_float_div = get_obj(overlay_id);
      cloak_elm_div = get_obj(cloak_id);
      show_delay = setTimeout('show_200("' + img_src + '","' + overlay_id + '","' + cloak_id + '");', 400);
      return;
   }
      
   var obj_base_img = get_obj('img_200');
	if (!obj_base_img) return;
	if (obj_base_img.src != img_src) {
   		obj_base_img.src    = img_src;
   	}
   	
   show_div(overlay_id, cloak_id);
}


function show_div(overlay_id, cloak_id) {
   if (show_delay) {
       //alert("delay div");
      clearTimeout(show_delay); show_delay = null;
   } else {
       //alert("delay NONE div");
      obj_float_div = get_obj(overlay_id);
      cloak_elm_div = get_obj(cloak_id);
      show_delay = setTimeout('show_div("' + overlay_id + '", "' + cloak_id + '");', 400);
      return;
   }


   if ( ! obj_float_div) {
       //alert("obj_float is null");
       return;
   }

   //alert("after check");

   if (obj_float_div.offsetWidth) {
      obj_float_div.style.width = "auto";
      obj_float_div.style.height = "auto";
      //if (obj_float_div.offsetWidth > 300) obj_float_div.style.width = "300px";
   }

   //if (cloak_elm_div) {
   //     cloak_elm_div.style.width = "auto";
   //     cloak_elm_div.style.height = "auto";
  // }

//alert("before mouse");
   document_onmousemove();

//alert("before show");

    obj_float_div.style.visibility = 'visible';

    if (cloak_elm_div) {
        cloak_elm_div.style.visibility = 'visible';
    }

   




} 

function hide_div() {
   clearTimeout(show_delay); show_delay = null;
   if ( ! obj_float_div ) return;

   obj_float_div.style.visibility = 'hidden';
   obj_float_div.style.left = "-3000px";
   obj_float_div = false;


   if (cloak_elm_div) {
       cloak_elm_div.style.visibility = "hidden";
       cloak_elm_div.style.left = "-3000px";
       cloak_elm_div = false;
   }


}



/* The End */