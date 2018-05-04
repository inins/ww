function playAudio(url){
//alert(url);
var musicPlay=document.getElementById('musicPlay');
musicPlay.setAttribute('src',url);
musicPlay.play();
}