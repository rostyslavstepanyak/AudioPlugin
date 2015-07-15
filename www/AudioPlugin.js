var exec = require('cordova/exec');

var AudioPlugin = {
    create: function (successCallback, errorCallback, url) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'play', [url]);
    },
    play: function (successCallback, errorCallback, url) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'play', []);
    },
    pause: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'pause', []);
    },
    stop: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'stop', []);
    }
};

module.exports = AudioPlugin;

