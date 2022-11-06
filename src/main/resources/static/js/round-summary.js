function init(){
//   const nodeList = document.querySelectorAll(".score-pb");
//   for (let i = 0; i < nodeList.length; i++) {
//     loadingProgress(nodeList[i]);
//   }
}

function loadingProgress(progressBar){

    var max = progressBar.value;
    progressBar.value=0;
    var timeleft = max;

    var downloadTimer = setInterval(function(){
        if(timeleft <= 0){
            clearInterval(downloadTimer);
        }
        progressBar.value =  progressBar.value+3;
        timeleft -= 3;
    }, 0);
}