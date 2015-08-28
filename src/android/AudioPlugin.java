package com.datamart.wfpk;

import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import java.io.IOException;

public class AudioPlugin extends CordovaPlugin {
    
    private static final String LOG_TAG = "Audio Player";
    private String action;
    private MediaPlayer mp = null;
    
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        super.initialize(cordova, webView);
        
    }
    
    @Override
    public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
        Log.v(LOG_TAG, "Received: " + action);
        this.action = action;
        final Activity activity = this.cordova.getActivity();
        final Context context = activity.getApplicationContext();
        cordova.setActivityResultCallback(this);
        if(action.equals("create")) {
            String streamUrl = args.getString(0);
            create(activity, callbackContext, streamUrl);
            return true;
        }
        if (action.equals("play")) {
            play(activity, callbackContext);
            return true;
        }
        if (action.equals("pause")) {
            pause(activity, callbackContext);
            return true;
        }
        
        if (action.equals("stop")) {
            stop(activity, callbackContext);
            return true;
        }
        
        if (action.equals("isPlaying")) {
            isPlaying(activity, callbackContext);
            return true;
        }
        
        if (action.equals("getVolume")) {
            getVolume(activity, callbackContext);
            return true;
        }
        
        if (action.equals("setVolume")) {
            double volume = args.getDouble(0);
            setVolume(activity, callbackContext, (float)volume);
            return true;
        }
        return false;
    }
    
    private void create(final Activity activity, final CallbackContext callbackContext, final String url) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //Just in case
                if(mp != null && mp.isPlaying()) {
                    mp.stop();
                    mp = null;
                }
                if (mp == null) {
                    mp = new MediaPlayer();
                }
                try {
                    mp.setDataSource(url);
                    mp.prepare();
                    callbackContext.success(handleResult());
                }
                catch (IOException e) {
                    callbackContext.error(e.getLocalizedMessage());
                }
            }
        });
    }
    
    private void play(final Activity activity, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if(mp != null) {
                    if(!mp.isPlaying()) {
                        mp.start();
                    }
                }
                callbackContext.success(handleResult());
            }
        });
    }
    
    private void pause(final Activity activity, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                mp.pause();
                callbackContext.success(handleResult());
            }
        });
    }
    
    private void isPlaying(final Activity activity, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                callbackContext.success(isPlayingInJSON());
            }
        });
    }
    
    private void stop(final Activity activity, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if(mp != null) {
                    mp.stop();
                    mp = null;
                }
                callbackContext.success(handleResult());
            }
        });
    }
    
    private void setVolume(final Activity activity, final CallbackContext callbackContext, final float volume) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if(mp == null) {
                    callbackContext.error("Player not initialised");
                }
                else {
                    callbackContext.success(handleSetVolumeResult(activity, volume));
                }
            }
        });
    }
    
    private void getVolume(final Activity activity, final CallbackContext callbackContext) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                if(mp == null) {
                    callbackContext.error("Player not initialised");
                }
                else {
                    callbackContext.success(handleVolumeResult(activity));
                }
            }
        });
    }
    
    private JSONObject isPlayingInJSON() {
        JSONObject response = new JSONObject();
        try {
            if(mp != null) {
                response.put("isPlaying", mp.isPlaying());
            }
            else {
                response.put("isPlaying", false);
            }
            
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    private JSONObject handleResult() {
        JSONObject response = new JSONObject();
        try {
            response.put("status", "OK");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return response;
    }
    
    private JSONObject handleSetVolumeResult(Activity activity, float volume) {
        JSONObject response = new JSONObject();
        try {
            AudioManager audioManager = (AudioManager) activity.getSystemService(Context.AUDIO_SERVICE);
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int newVolume = (int) (maxVolume*volume);
            audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, newVolume, 0);
            response.put("status", "OK");
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }
    
    private JSONObject handleVolumeResult(Activity activity) {
        JSONObject response = new JSONObject();
        try {
            AudioManager audioManager = (AudioManager)  activity.getSystemService(Context.AUDIO_SERVICE);
            int origionalVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            int volume_level= audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            response.put("volume", volume_level*1.0 / origionalVolume*1.0);
        }
        catch (JSONException e) {
            e.printStackTrace();
        }
        catch (Exception e) {
            e.printStackTrace();
        }
        
        return response;
    }
}
