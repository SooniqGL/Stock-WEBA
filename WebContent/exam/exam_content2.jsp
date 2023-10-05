<%-- 
    Document   : exam_content
    Created on : Dec 10, 2011, 3:07:46 PM
    Author     : qin
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
  <head>
   <script type="text/javascript" language="JavaScript">
       
       
var can; 
var ctx; 
var addAngle; 
var addScale; 
var step; 
var steps = 50; 
var delay = 20; 


 


function init() { 


    can = document.getElementById("canvas"); 


    //ctx = can.getContext("2d"); 


    addAngle = Math.PI * 2 / steps; 


    addScale = 1 / steps; 


    step = 0; 


    ctx.fillStyle = "blue"; 


    ctx.font = "48pt Helvetica"; 


    ctx.textAlign = "center"; 


    ctx.textBaseline = "middle"; 


    spinText(); 


} 


 


function spinText() { 


    step++; 


    ctx.clearRect(0,0, can.width, can.height); 

    ctx.save(); 


    ctx.translate(can.width / 2, can.height / 2); 


    ctx.scale(addScale * step, addScale * step); 


    ctx.rotate(Math.PI / 2); //addAngle * step); 


    ctx.fillText("Hello, world!", 0,0); 


    ctx.restore(); 


      if (step < steps) 


          var t = setTimeout('spinText()', delay); 


} 


 

       function drawBowtie(ctx, fillStyle) {
 
   ctx.fillStyle = "rgba(200,200,200,0.3)";
   ctx.fillRect(-30, -30, 60, 60);
 
   ctx.fillStyle = fillStyle;
   ctx.globalAlpha = 1.0;
   ctx.beginPath();
   ctx.moveTo(25, 25);
   ctx.lineTo(-25, -25);
   ctx.lineTo(25, -25);
   ctx.lineTo(-25, 25);
   ctx.closePath();
   ctx.fill();
 }
 
 function dot(ctx) {
   ctx.save();
   ctx.fillStyle = "black";
   ctx.fillRect(-2, -2, 4, 4);
   ctx.restore();
 }
 
 function draw() {
   var canvas = document.getElementById("canvas");
   ctx = canvas.getContext("2d");

   // note that all other translates are relative to this
   // one
   ctx.translate(45, 45);

   ctx.save();
   //ctx.translate(0, 0); // unnecessary
   drawBowtie(ctx, "red");
   dot(ctx);
   ctx.restore();
 
   ctx.save();
   ctx.translate(85, 0);
   ctx.rotate(45 * Math.PI / 180);
   drawBowtie(ctx, "green");
   dot(ctx);
   ctx.restore();
 
   ctx.save();
   ctx.translate(0, 85);
   ctx.rotate(135 * Math.PI / 180);
   drawBowtie(ctx, "blue");
   dot(ctx);
   ctx.restore();
 
   ctx.save();
   ctx.translate(85, 85);
   ctx.rotate(90 * Math.PI / 180);
   drawBowtie(ctx, "yellow");
   dot(ctx);
   ctx.restore();
   
   ctx.fillStyle = "blue"; 
    ctx.font="24pt Helvetica"; 
    ctx.fillText("Figure 1", 10, 100); 
    
    init();

 }
    </script>
  </head>
  <body onload="draw()">
<a href="https://developer.mozilla.org/en/Drawing_Graphics_with_Canvas">Drawing Graphics with Canvas</a>

    <canvas id="canvas" width="300" height="300" style="position:relative; width: 680px; height: 400px; background-color: #ff0; "></canvas>
  </body>
 </html>