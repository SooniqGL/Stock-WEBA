/* 
 * Display some stuff on the Login screen.
 */


var canvas = document.getElementById("mycanvas");
var ctx = null;
var grad = null;
var body = document.getElementsByTagName('body')[0];
var color = 255;
var wordList  = new Array();

// word list to display
wordList[0] = "Welcome!";
wordList[1] = "Make Your Dream True!";
wordList[2] = "Have a Nice Day!";
wordList[3] = "Good Luck!";
wordList[4] = "Take It Easy!";
wordList[5] = "Bear Up Under Loss.";
wordList[6] = "Fight the Bitterness of Defeat.";
wordList[7] = "Fight the Weakness of Grief.";
wordList[8] = "Be a Victor Over Anger.";
wordList[9] = "Smile When Tears Are Close.";
wordList[10] = "Resist Disease and Evil Men.";
wordList[11] = "Resist Base Instincts.";
wordList[12] = "Hate Hate and Love Love.";
wordList[13] = "Go On When It Would Seem Good To Die.";
wordList[14] = "Seek Ever the Glory and the Dream.";
wordList[15] = "Look Up With Unquenchable Faith.";
wordList[16] = "Speak Less.";
wordList[17] = "Keep Your Promise.";
wordList[18] = "Be Nice To People.";
wordList[19] = "Adapt Or Die.";

var currentWordIndex = 0;
var currentLoopCnt = 0;
var LOOP_CNT = 3;

function loadIt() {
    //self.setInterval("kickoff()", 1000);
}

    function kickoff() {
        if (canvas == null) {
            alert("canvas null");
        }

        if (canvas.getContext('2d')) {
          ctx = canvas.getContext('2d');
          ctx.clearRect(0, 0, canvas.width, canvas.height);
          //ctx.save();
          
          //ctx.fillStyle = "#00ff00";
          //ctx.fillRect(0, 0, canvas.width, canvas.height);
        
          /*
          var colorR = Math.round(Math.random() * 255);
          var colorG = Math.round(Math.random() * 255);
          var colorB = Math.round(Math.random() * 255);
          
          // Create radial gradient
          grad = ctx.createRadialGradient(0,0,0,0,0,600); 
          grad.addColorStop(0, '#000');
          grad.addColorStop(1, 'rgb(' + colorR + ', ' + colorG + ', ' + colorB + ')');

          // assign gradients to fill
          ctx.fillStyle = grad;

          // draw 600x600 fill
          ctx.fillRect(0, 0, canvas.width, canvas.height);
          */
          
          if (currentLoopCnt >= LOOP_CNT) {
              // change word
              currentWordIndex++;
              if (currentWordIndex >= wordList.length) {
                  currentWordIndex = 0;
              }
              
              // reset
              currentLoopCnt = 0;
          } else {
              currentLoopCnt++;
          }
        
          var colorR = Math.round(Math.random() * 255);
          var colorG = Math.round(Math.random() * 255);
          var colorB = Math.round(Math.random() * 255);
          
          ctx.fillStyle = "rgb(" + colorR + ", " + colorG + ", " + colorB + ")"; //"#ffffef";
          ctx.font="25pt Helvetica"; 
          ctx.fillText(wordList[currentWordIndex], 100, 60);
          
          //ctx.restore();
          
          //setTimeout("kickoff()", 5000);
        } 
    }
    

//*
  body.onmousemove = function (event) {
      if (canvas.getContext('2d')) {
        var width = window.innerWidth, 
            height = window.innerHeight, 
            x = event.clientX, 
            y = event.clientY,
            rx = 600 * x / width,
            ry = 600 * y / height;

        var xc = ~~(256 * x / width);
        var yc = ~~(256 * y / height);

        ctx = canvas.getContext('2d');
        grad = ctx.createRadialGradient(rx, ry, 0, rx, ry, 600); 
        grad.addColorStop(0, '#000000');
        grad.addColorStop(1, ['rgb(', xc, ', ', (255 - xc), ', ', yc, ')'].join(''));
        // ctx.restore();
        ctx.fillStyle = grad;
        ctx.fillRect(0, 0, canvas.width, canvas.height);
        // ctx.save();
        
        ctx.fillStyle = "#ffffef";
        ctx.font="15pt Helvetica"; 
        ctx.fillText("&copy;2000-2006 Copyright of Sooniq Technology Corporation.", 100, 60);
      }
  }// */



/* end */