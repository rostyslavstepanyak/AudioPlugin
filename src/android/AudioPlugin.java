package com.datamart.wfpk;

import android.app.Activity;
import android.content.Context;
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

	public boolean execute(final String action, JSONArray args, final CallbackContext callbackContext) throws JSONException {
		Log.v(LOG_TAG, "Received: " + action);
		this.action = action;
		final Activity activity = this.cordova.getActivity();
		final Context context = activity.getApplicationContext();
		cordova.setActivityResultCallback(this);
		if (action.equals("play")) {
			play(activity, callbackContext);
			return true;
		}
		return false;
	}

	private void play(final Activity activity, final CallbackContext callbackContext) {
		cordova.getThreadPool().execute(new Runnable() {
			@Override
			public void run() {
				if(mp == null)
					mp = new MediaPlayer();
				else
					mp.stop();

				try {
					mp.setDataSource("http://europaplus.dp.ua:8000/evropa");
					mp.prepare();
					mp.start();
					callbackContext.success(handleResult());
				}
				catch (IOException e) {
					callbackContext.error(e.getLocalizedMessage());
				}
			}
		});
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
}
