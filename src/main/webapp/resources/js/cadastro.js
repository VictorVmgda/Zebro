$(function (){
	navigator.geolocation.getCurrentPosition(showPosition);
	console.log($("#latUser").val());
	console.log($("#lonUser").val());

	$(".empty").val("");
})

PrimeFaces.escapeRegExp = function(text) {
	return PrimeFaces.escapeHTML(text.replace(/([.?*+^$[\]\\(){}|-])/g, "\\$1"));
}

function showPosition(position) {
	$("#latUser").val(position.coords.latitude);
    $("#lonUser").val(position.coords.longitude); 
}
