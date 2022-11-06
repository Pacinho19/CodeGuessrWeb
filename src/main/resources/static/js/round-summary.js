function init(){
//   const nodeList = document.querySelectorAll(".score-pb");
//   for (let i = 0; i < nodeList.length; i++) {
//     loadingProgress(nodeList[i]);
//   }

    var hitPB = document.getElementById('hit-pb');
    if(hitPB==null) return;

    decreaseScore(hitPB);
}

function decreaseScore(progressBar){
    document.getElementById('hit-score').style.backgroundColor = "red";

    var prevHealth = document.getElementById('prevHealth').value;
    var actualScore = document.getElementById('health').value;
    var timeleft = prevHealth;

        progressBar.value = prevHealth;
        var downloadTimer = setInterval(function(){
            if(timeleft<=actualScore){
                document.getElementById('hit-score').style.backgroundColor = "";
                clearInterval(downloadTimer);
            }
            progressBar.value =  progressBar.value-1;
            timeleft -= 1;
        }, 0);
}

function loadingProgress(progressBar){

    var max = progressBar.value;
    progressBar.value=0;
    var timeleft = max;

    var downloadTimer = setInterval(function(){
        if(timeleft <= 0){
            clearInterval(downloadTimer);
        }
        progressBar.value =  progressBar.value-1;
        timeleft -= 1;
    }, 0);
}