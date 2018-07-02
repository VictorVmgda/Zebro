var audio = document.getElementById("audio");
var source = document.getElementById("audiosource");
var icon = document.querySelector(".playMusic .playButton i");

var myAudio = document.getElementById("myAudio");
var mySource = document.getElementById("myaudiosource");
var myIcon = document.querySelector(".playMyMusic .playButton i");

function playMusic() {
	
	console.log(audio);
	event.preventDefault();
	source.src = '#{buscaBean.music.previewUrl}';
	
	if (audio.paused == false) {
		audio.pause();
		icon.className = "fa fa-play";

	} else {

		audio.play();
		icon.className = "fa fa-pause";
		console.log("a");
	}
	
}

function playMyMusic() {
	event.preventDefault();
	mySource.src = '#{sessionScope.usuario.spotifyMusic.previewUrl}';
	
	if (myAudio.paused == false) {
		myAudio.pause();
		myIcon.className = "fa fa-play";

	} else {

		myAudio.play();
		myIcon.className = "fa fa-pause";
		console.log("a");
	}
	
}

audio.addEventListener("timeupdate", function() {
	if (audio.ended) {
		icon.className = "fa fa-play";		
	}
}, false);

myAudio.addEventListener("timeupdate", function() {
	if (myAudio.ended) {
		myIcon.className = "fa fa-play";		
	}
}, false);