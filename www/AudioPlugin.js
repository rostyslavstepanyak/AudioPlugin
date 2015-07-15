cordova.define("com-datamart-wfpk.AudioPlugin", function(require, exports, module) { cordova.define("com-datamart-wfpk.AudioPlugin", function(require, exports, module) { var exec = require('cordova/exec');

var AudioPlugin = {
	play: function (successCallback, errorCallback, url) {
		exec(successCallback, errorCallback, 'AudioPlugin', 'play', [url]);
	},
	pause: function(successCallback, errorCallback) {
		exec(successCallback, errorCallback, 'AudioPlugin', 'pause', []);
	},
	changeBitrate: function(successCallback, errorCallback, bitrate) {
    	exec(successCallback, errorCallback, 'AudioPlugin', 'changeBitrate', [bitrate]);
    }
};

module.exports = AudioPlugin;
});

});
