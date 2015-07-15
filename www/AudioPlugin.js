var exec = require('cordova/exec');

var AudioPlugin = {
    play: function (successCallback, errorCallback, url) {
        alert('Before exec');
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
