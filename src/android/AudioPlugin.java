package com.datamart.wfpk;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Handler;
import android.util.Log;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CordovaWebView;
import org.apache.cordova.PluginResult;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Intent;

import java.io.IOException;

public class AudioPlugin extends CordovaPlugin {
    
    private static final String LOG_TAG = "Audio Player";
    private String action;
    private MediaPlayer mp = null;
    private CallbackContext volumeChangesCallback;
    private SettingsContentObserver mSettingsContentObserver;
    
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
            setVolume(activity, callbackContext, (float) volume);
            return true;
        }

        if (action.equals("subscribe")) {
            subscribe(activity, callbackContext);
            return true;
        }
        return false;
    }
    
    private void create(final Activity activity, final CallbackContext callbackContext, final String url) {
        cordova.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                //Just in case
                if(mp != null) {
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
                        setContentObserverPlayingState(true);
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
                setContentObserverPlayingState(false);
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

                    setContentObserverPlayingState(false);
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

    private void subscribe(final Activity activity, final CallbackContext callbackContext) {
        volumeChangesCallback = callbackContext;

        mSettingsContentObserver = new SettingsContentObserver(activity.getApplicationContext(), new Handler(), new VolumeChangeCallback() {
            @Override
            public void volumeChanged(float newVolume) {
                PluginResult dataResult = new PluginResult(PluginResult.Status.OK, handleVolumeResult(activity));
                dataResult.setKeepCallback(true);
                volumeChangesCallback.sendPluginResult(dataResult);
            }
        }, isPlayerPlaying());
        activity.getApplicationContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mSettingsContentObserver);

        //send initially the first callback as the confirmation that you subscribed for volume changes */
        PluginResult dataResult = new PluginResult(PluginResult.Status.OK, handleVolumeResult(activity));
        dataResult.setKeepCallback(true);
        volumeChangesCallback.sendPluginResult(dataResult);
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
            int maxVolume = audioManager.getStreamMaxVolume(streamType());
            int newVolume = (int) (maxVolume*volume);
            audioManager.setStreamVolume(streamType(), newVolume, 0);
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
            int origionalVolume = audioManager.getStreamMaxVolume(streamType());
            int volume_level= audioManager.getStreamVolume(streamType());
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

    Boolean isPlayerPlaying() {
        if(mp != null) {
            return mp.isPlaying();
        }
        else {
            return false;
        }
    }

    int streamType() {
        if(isPlayerPlaying()) {
            return AudioManager.STREAM_MUSIC;
        }
        else {
            return AudioManager.STREAM_SYSTEM;
        }
    }

    private void setContentObserverPlayingState(Boolean isPlaying) {
        if(mSettingsContentObserver != null) {
            mSettingsContentObserver.setIsPlaying(isPlaying);
        }
    }
}
