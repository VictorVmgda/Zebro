var audio = document.getElementById("audio");
var source = document.getElementById("audiosource");
var icon = document.querySelector(".playButton i");
function playMusic() {
	event.preventDefault();
	source.src = "#{buscaBean.music.getPreviewUrl()}"
	if (audio.paused == false) {
		audio.pause();
		icon.className = "fa fa-play";

	} else {

		audio.play();

		icon.className = "fa fa-pause";
		console.log("a");
	}
	
}

audio.addEventListener("timeupdate", function() {
	if (audio.ended) {
		icon.className = "fa fa-play";		
	}
}, false);