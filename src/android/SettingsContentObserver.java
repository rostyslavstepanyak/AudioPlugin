package com.datamart.wfpk;

import android.content.Context;
import android.database.ContentObserver;
import android.media.AudioManager;
import android.os.Handler;

/**
 * Created by r.stepanyak on 9/7/15.
 */
public class SettingsContentObserver extends ContentObserver {
    int previousVolume;
    int maxVolume;
    Context context;
    VolumeChangeCallback callback;
    private Boolean isPlaying = false;

    public SettingsContentObserver(Context context, Handler handler, VolumeChangeCallback callback, Boolean isPlaying) {
        super(handler);
        this.context = context;
        this.callback = callback;
        this.isPlaying = isPlaying;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audio.getStreamMaxVolume(streamType());
        previousVolume = audio.getStreamVolume(streamType());
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(streamType());
        callback.volumeChanged((float)(currentVolume*1.0 / maxVolume*1.0));
    }

    public Boolean getIsPlaying() {
        return isPlaying;
    }

    public void setIsPlaying(Boolean isPlaying) {
        this.isPlaying = isPlaying;

        /*In case the playback state is changed, we should get the max volume of the stream again*/
        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        maxVolume = audio.getStreamMaxVolume(streamType());
    }

    /*depending on the playback state, use either STREAM_MUSIC (when the player is playing) or STREAM_SYSTEM*/
    int streamType() {
        if(isPlaying) {
            return AudioManager.STREAM_MUSIC;
        }
        else {
            return AudioManager.STREAM_SYSTEM;
        }
    }

}
