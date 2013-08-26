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

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

import android.util.Log;

public class BootReceiver extends BroadcastReceiver {
	@Override
	public void onReceive(Context context, Intent intent) {
		Log.v("VanillaPlug", "Starting up VPlugService");
		context.startService(new Intent(context, VPlugService.class));
	}
}
