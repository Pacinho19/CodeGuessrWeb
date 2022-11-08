function init(){
//   const nodeList = document.querySelectorAll(".score-pb");
//   for (let i = 0; i < nodeList.length; i++) {
//     loadingProgress(nodeList[i]);
//   }

    var hitPB = document.getElementById('hit-pb');
    if(hitPB==null)  loadingGame();;

    decreaseScore(hitPB);
}

function decreaseScore(progressBar){
    var hitDiv = document.getElementById('hit-div');
    if(hitDiv==null) return;

    hitDiv.classList.add("zoom-in-out-box");
    document.getElementById('damage-value').classList.add("w3-animate-left");

    var hitScoreLabel = document.getElementById('hit-score');
    hitScoreLabel.style.color = "red";
    var prevHealth = document.getElementById('prevHealth').value;
    hitScoreLabel.innerHTML = prevHealth;
    var actualScore = document.getElementById('health').value;

    document.getElementById('damage-value').innerHTML = '-' + (prevHealth - actualScore);

    var timeleft = prevHealth;
        progressBar.value = prevHealth;
        var downloadTimer = setInterval(function(){
            timeleft -= 1;
            progressBar.value =  progressBar.value-1;
            hitScoreLabel.innerHTML = progressBar.value;

            if(timeleft<=actualScore || progressBar.value==0){
                document.getElementById('hit-score').style.color = "";
                document.getElementById('hit-div').classList.remove("zoom-in-out-box");
                clearInterval(downloadTimer);
                loadingGame();
            }
        }, 0);

        setTimeout(
            function() {
                document.getElementById('damage-value').remove();
            },
        5000);

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

 function loadingGame(){
    document.getElementById("startRoundDiv").style.display = 'block';
    var max = document.getElementById("progressBar").max;
    var timeleft = max;
    var downloadTimer = setInterval(function(){
        if(timeleft <= 0){
            clearInterval(downloadTimer);
            window.location.href = '/code-guessr/games/'+ document.getElementById('gameId').value;
        }
        var nextPbValue =  max - timeleft;
        document.getElementById("progressBar").value = nextPbValue;
        timeleft -= 1;
    }, 1000);
}