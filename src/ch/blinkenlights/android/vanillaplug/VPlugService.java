/*
 * Copyright (C) 2013-2015 Adrian Ulrich
 *
 *   This file is part of VanillaPlug.
 *
 *  VanillaPlug is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  VanillaPlug is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 */


package ch.blinkenlights.android.vanillaplug;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.ComponentName;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Binder;
import android.os.IBinder;
import android.widget.Toast;
import android.media.AudioManager;
import android.preference.PreferenceManager;


public class VPlugService extends Service 
	implements SharedPreferences.OnSharedPreferenceChangeListener {

	private final IBinder vplug_binder = new LocalBinder();
	private SharedPreferences mSharedPreferences;

	@Override
	public IBinder onBind(Intent i) {
		return vplug_binder;
	}

	public class LocalBinder extends Binder {
		public VPlugService getService() {
			return VPlugService.this;
		}
	}

	@Override
	public void onCreate() {
		mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
		mSharedPreferences.registerOnSharedPreferenceChangeListener(this);

		IntentFilter inf = new IntentFilter(); 
		inf.addAction("android.intent.action.HEADSET_PLUG"); 
		registerReceiver(vplug_receiver, inf);

		stopOrKeepService();
	}

	@Override
	public void onDestroy() {
		mSharedPreferences.unregisterOnSharedPreferenceChangeListener(this);
		unregisterReceiver(vplug_receiver);
	}

	@Override
	public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
		stopOrKeepService();
	}

	private void stopOrKeepService() {
		boolean serviceEnabled = mSharedPreferences.getBoolean("serviceEnabled", false);
		if (serviceEnabled == false)
			stopSelf();
	}

	private final BroadcastReceiver vplug_receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			String ia = intent.getAction();
			int state = intent.getIntExtra("state", -1);

			if(state == 1) { /* Headset was plugged in */
				AudioManager aM = (AudioManager)ctx.getSystemService(Context.AUDIO_SERVICE);

				if(aM != null && !aM.isMusicActive()) {
					Toast.makeText(ctx, R.string.ntfy_autolaunch, Toast.LENGTH_SHORT).show();
					ComponentName service = new ComponentName("ch.blinkenlights.android.vanilla","ch.blinkenlights.android.vanilla.PlaybackService");
					Intent x = new Intent("ch.blinkenlights.android.vanilla.action.PLAY").setComponent(service);
					x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					startService(x);
				}
				else {
					Toast.makeText(ctx, R.string.ntfy_nolaunch, Toast.LENGTH_SHORT).show();
				}
			}
		}
		
	};
}
