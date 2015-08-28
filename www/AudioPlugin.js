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
    },
    getVolume: function(successCallback, errorCallback) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'getVolume', []);
    },
    setVolume: function(successCallback, errorCallback, volume) {
        exec(successCallback, errorCallback, 'AudioPlugin', 'setVolume', [volume]);
    }
    
};

module.exports = AudioPlugin;

