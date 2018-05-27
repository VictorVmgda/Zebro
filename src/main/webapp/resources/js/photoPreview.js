var nome = $('.nomeInput');

nome.on('input', function() {
	$("#nome").text(nome.val());
	if (nome.val().length == 0) {
		$("#nome").text("Digite seu nome");
	}
})

function previewPrimary() {
	var image = document.querySelector(".imageView");
	var file = document.querySelectorAll("input[type=file]")[0].files[0];
	var reader = new FileReader();

	reader.addEventListener("load", function() {
		image.src = reader.result;
	}, false);

	if (file) {
		reader.readAsDataURL(file);
	}

}

function previewSecondary() {
	var image = document.querySelector(".photoLabelSec");
	var file = document.querySelectorAll("input[type=file]")[1].files[0];
	var reader = new FileReader();

	reader.addEventListener("load", function() {
		image.textContent = "";
		image.style.backgroundColor = 'transparent';
		image.style.backgroundImage = 'url(' + reader.result + ')';
		image.style.backgroundRepeat = 'no-repeat';
		image.style.backgroundSize = 'cover';
		image.style.width = '100%';
	}, false);

	if (file) {
		reader.readAsDataURL(file);
	}

}
function previewTerciary() {
	var image = document.querySelector(".photoLabelTer");
	var file = document.querySelectorAll("input[type=file]")[2].files[0];
	var reader = new FileReader();

	reader.addEventListener("load", function() {
		image.textContent = "";
		image.style.backgroundColor = 'transparent';
		image.style.backgroundImage = 'url(' + reader.result + ')';
		image.style.backgroundRepeat = 'no-repeat';
		image.style.backgroundSize = 'cover';
		image.style.width = '100%';
	}, false);

	if (file) {
		reader.readAsDataURL(file);
	}

}
function previewQuaternary() {
	var image = document.querySelector(".photoLabelQuat");
	var file = document.querySelectorAll("input[type=file]")[3].files[0];
	var reader = new FileReader();

	reader.addEventListener("load", function() {
		image.textContent = "";
		image.style.backgroundColor = 'transparent';
		image.style.backgroundImage = 'url(' + reader.result + ')';
		image.style.backgroundRepeat = 'no-repeat';
		image.style.backgroundSize = 'cover';
		image.style.width = '100%';
	}, false);

	if (file) {
		reader.readAsDataURL(file);
	}

}

function previewFifth() {
	var image = document.querySelector(".photoLabelFive");
	var file = document.querySelectorAll("input[type=file]")[4].files[0];
	var reader = new FileReader();

	reader.addEventListener("load", function() {
		image.textContent = "";
		image.style.backgroundColor = 'transparent';
		image.style.backgroundImage = 'url(' + reader.result + ')';
		image.style.backgroundRepeat = 'no-repeat';
		image.style.backgroundSize = 'cover';
		image.style.width = '100%';
	}, false);

	if (file) {
		reader.readAsDataURL(file);
	}

}

function removeSec(){
	var image = document.querySelector(".photoLabelSec");
	
	image.backgroundColor = '#000';
	image.backgroundImage = 'none !important';
	image.textContent = "+";
	
}

function removeTer(){
	var image = document.querySelector(".photoLabelTer");
	
	image.backgroundColor = '#000';
	image.backgroundImage = 'none !important';
	image.textContent = "+";
	
}

function removeQua(){
	var image = document.querySelector(".photoLabelQua");
	
	image.backgroundColor = '#000';
	image.backgroundImage = 'none !important';
	image.textContent = "+";
	
}

function removeQui(){
	var image = document.querySelector(".photoLabelFive");
	
	image.backgroundColor = '#000';
	image.backgroundImage = 'none !important';
	image.textContent = "+";
	
}