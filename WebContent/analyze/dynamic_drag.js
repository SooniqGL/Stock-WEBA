/* 
 * dynamic_drag.js
 * Help to do the drag of the chart.
 */


// var canvas,ctx;  -- commented to use global variable defined
var canvasLeft = 0;  // fixed as the canvas is not moved
var currMX = 0;  // x point for the mouse after move
var prevMX = 0;   // x point for the mouse when last time the event is kicked
var infobox;

var cursor_grab ="url(data:application/cur;base64,AAACAAEAICACAAcABQAwAQAAFgAAACgAAAAgAAAAQAAAAAEAAQAAAAAAAAEAAAAAAAAAAAAAAgAAAAAAAAAAAAAA%2F%2F%2F%2FAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD8AAAA%2FAAAAfwAAAP%2BAAAH%2FgAAB%2F8AAA%2F%2FAAAd%2FwAAGf%2BAAAH9gAADbYAAA2yAAAZsAAAGbAAAAGAAAAAAAAA%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FgH%2F%2F%2F4B%2F%2F%2F8Af%2F%2F%2BAD%2F%2F%2FAA%2F%2F%2FwAH%2F%2F4AB%2F%2F8AAf%2F%2FAAD%2F%2F5AA%2F%2F%2FgAP%2F%2F4AD%2F%2F8AF%2F%2F%2FAB%2F%2F%2F5A%2F%2F%2F%2F5%2F%2F%2F8%3D), move";
var cursor_drag ="url(data:application/cur;base64,AAACAAEAICACAAcABQAwAQAAFgAAACgAAAAgAAAAQAAAAAEAAQAAAAAAAAEAAAAAAAAAAAAAAgAAAAAAAAAAAAAA%2F%2F%2F%2FAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAD8AAAA%2FAAAAfwAAAP%2BAAAH%2FgAAB%2F8AAAH%2FAAAB%2FwAAA%2F0AAANsAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2FgH%2F%2F%2F4B%2F%2F%2F8Af%2F%2F%2BAD%2F%2F%2FAA%2F%2F%2FwAH%2F%2F%2BAB%2F%2F%2FwAf%2F%2F4AH%2F%2F%2BAD%2F%2F%2FyT%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F%2F8%3D), move";

function initDrag(){
    //canvas = document.getElementById("chart");
    //ctx = canvas.getContext('2d');
    
    infobox = {
        startDate: "",
        endDate: "",
        mouse: true,
        drag: false
    }
    
    canvas.addEventListener('mousemove',updateCanvas,false);
    canvas.addEventListener('mousedown',startDrag,false);
    canvas.addEventListener('mouseup',stopDrag,false);
    
    canvas.addEventListener('selectstart',function(e){e.preventDefault();},false);	
    canvas.style.MozUserSelect = "none";
    
    // find the X position of the Canvas, this should be fixed
    var pos = findOffset(canvas);
    canvasLeft = pos.x;
}

function findOffset(obj) {
    var curX = 0, curY = 0;
    if (obj.offsetParent) {
        do {
            curX += obj.offsetLeft;
            curY += obj.offsetTop;
        } while ((obj = obj.offsetParent) != null);
        
        return {x:curX,y:curY};
    }
    
    return {x:0,y:0};
}

function updateCanvas(e){

    currMX = e.pageX - canvasLeft;  // current mouse X point
    //cy = e.pageY - canvasY;
    
    if(infobox.mouse && !infobox.drag){
        canvas.style.cursor = cursor_grab;
    } else if(infobox.drag) {
        canvas.style.cursor = cursor_drag;
    } else {
        canvas.style.cursor = 'auto';
    }
    
    if(infobox.drag) {
        // call draw function, if draw return true, else false
        if (drawCanvasForDrag(currMX - prevMX)) {
            prevMX = currMX;   // update the position
        }
    }
}

function startDrag(e){
    if(infobox.mouse == true){
        infobox.drag = true;
        prevMX = e.pageX - canvasLeft;
        ctx.save();
        ctx.shadowOffsetX = 2;
        ctx.shadowOffsetY = 2;
        ctx.shadowColor="rgba(0,0,0,.3)";
        ctx.shadowBlur = 5;
        //drawCanvas();
    }
}

function stopDrag(){
    if(infobox.drag == true){
        infobox.drag = false;
        ctx.restore();
        //drawCanvas();
    }
}

/*
function drawCanvas(){
    ctx.clearRect(0,0,canvas.width,canvas.height);
    ctx.beginPath();
    ctx.arc(circle.x,circle.y,circle.r,0,Math.PI*2,false);
    ctx.isPointInPath(mouseX,mouseY) ? circle.mouse = true : circle.mouse = false;
    ctx.fill();

} */


/* end */