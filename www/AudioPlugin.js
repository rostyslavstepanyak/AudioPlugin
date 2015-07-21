var exec = require('cordova/exec');

var AudioPlugin = {
    create: function (successCallback, errorCallback, url) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'create', [url]);
    },
    play: function (successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'play', []);
    },
    pause: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'pause', []);
    },
    stop: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'stop', []);
    },
    isPlaying: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'isPlaying', []);
    }
};

module.exports = AudioPlugin;

