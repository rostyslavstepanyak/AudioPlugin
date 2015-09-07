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

    public SettingsContentObserver(Context context, Handler handler, VolumeChangeCallback callback) {
        super(handler);
        this.context = context;
        this.callback = callback;

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int maxVolume = audio.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
        previousVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);
    }

    @Override
    public boolean deliverSelfNotifications() {
        return super.deliverSelfNotifications();
    }

    @Override
    public void onChange(boolean selfChange) {
        super.onChange(selfChange);

        AudioManager audio = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        int currentVolume = audio.getStreamVolume(AudioManager.STREAM_MUSIC);

        /*int delta=previousVolume-currentVolume;

        if(delta>0)
        {
            previousVolume=currentVolume;
        }
        else if(delta<0)
        {
            previousVolume=currentVolume;
        }*/
        callback.volumeChanged(currentVolume*1.0 / maxVolume*1.0);
    }
}
