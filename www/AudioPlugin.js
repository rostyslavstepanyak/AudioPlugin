var exec = require('cordova/exec');

var AudioPlugin = {
	play: function (successCallback, errorCallback) {
		exec(successCallback, errorCallback, 'AudioPlugin', 'play', []);
	}
};

module.exports = AudioPlugin;