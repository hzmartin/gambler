var GLOBAL = {};

GLOBAL.namespace = function(str) {
	var arr = str.split("."), o = GLOBAL;
	for ( var i = (arr[0] == "GLOBAL") ? 1 : 0; i < arr.length; i++) {
		o[arr[i]] = o[arr[i]] || {};
		o = o[arr[i]];
	}
}

GLOBAL.namespace("Lang");
var Lang = GLOBAL.Lang;
Lang.trim = function(str) {
	return str.replace(/^\s+|\s+$/g, "");
}

Lang.isNumber = function(s) {
	return !isNaN(s);
}