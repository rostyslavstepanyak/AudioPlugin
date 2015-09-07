# cordova-datamart-wfpk
Cordova/PhoneGap plugin to use Audio Stream



### Install
cordova add plugin https://github.com/rostyslavstepanyak/AudioPlugin


### Usage
To create the player and play the stream:

````create```` method is asynchroonous. Its first parameter is the success callback, second is the failure callback, third is the stream url.
````play```` meyhod starts the playback right away. However it also accepts success and failure callback. In case the stream cannot be processed, it'll respond with failure and message.


````
AudioPlugin.create(function() { playerCreated();},
                   function() { playerFailure(e);},
                   'http://some/url');

........
playerCreated: function() {
    AudioPlugin.play(function() { alert('Playback started');},
                     function(e){ alert(e);});
},
playerFailure: function(e) {
    alert(e);
}
........

````

To pause the stream call ````AudioPlugin.pause(sucess,failure);````

To stop the stream call ````AudioPlugin.stop(sucess, failure);````

To check if the player is playing:

````
AudioPlugin.isPlaying(function(result) {
                         if(result.isPlaying) {
                             alert('Got the sound!');
                         }
                         else {
                             alert('Either not created or paused/stopped');
                         }
                      }, 
                      function(e) {alert(e);});

````

To get the volume:

````
AudioPlugin.getVolume(function(result) {
                        if(result.volume) {
                            alert('The volume: ' + result.volume);
                        }
                        else {
                            alert('Someting weird. We should have received the volume');
                        }
                    }, 
                    function(e) {alert(e);});

````


To set the volume:

````
AudioPlugin.setVolume(function(result) {
                        if(result) {
                            alert('The volume is set');
                        }
                     }, 
                     function(e) {alert(e);}, 0.7);

````

To start tracking the volume, for example hardware keys "music_up"/"music_down". (Please note that in case the playback of the stream not started yet it will respond with the system volume level not with the music volume level)

AudioPlugin.subscribe(function(result) {
                            if(result) {
                                alert('Current volume is: ' + result.volume);
                            }
                      }, 
                      function(e) {alert('Normally this should never happen');});







