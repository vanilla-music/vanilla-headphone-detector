/*
 * Copyright (C) 2013 Adrian Ulrich
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
import android.os.Binder;
import android.os.IBinder;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ComponentName;
import android.widget.Toast;
import android.util.Log;

public class VPlugService extends Service {
	private final IBinder vplug_binder = new LocalBinder();
	
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
		IntentFilter inf = new IntentFilter(); 
		inf.addAction("android.intent.action.HEADSET_PLUG"); 
		registerReceiver(vplug_receiver, inf);
	}

	public void onDestroy() {
		unregisterReceiver(vplug_receiver);
	}


	private final BroadcastReceiver vplug_receiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context ctx, Intent intent) {
			String ia = intent.getAction();
			int state = intent.getIntExtra("state", -1);

			Log.v("VanillaPlug", "Intent "+ia+" received with state "+state);

			if(state == 1) { /* Headset was plugged in */
				Toast.makeText(ctx, R.string.ntfy_autolaunch, Toast.LENGTH_SHORT).show();

				ComponentName service = new ComponentName("ch.blinkenlights.android.vanilla","ch.blinkenlights.android.vanilla.PlaybackService");
				Intent x = new Intent("ch.blinkenlights.android.vanilla.action.PLAY").setComponent(service);
				x.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startService(x);
			}
		}
		
	};
}
