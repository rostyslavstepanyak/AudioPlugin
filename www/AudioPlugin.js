var exec = require('cordova/exec');

var AudioPlugin = {
	play: function (successCallback, errorCallback) {
        alert('Before exec');
		exec(successCallback, errorCallback, 'AudioPlugin', 'play', []);
	}
};

module.exports = AudioPlugin;