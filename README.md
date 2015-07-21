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



