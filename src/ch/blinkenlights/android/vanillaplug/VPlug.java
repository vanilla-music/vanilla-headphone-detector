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


import android.content.ComponentName;
import android.content.SharedPreferences;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceActivity;
import android.preference.PreferenceManager;

import android.util.Log;

public class VPlug extends PreferenceActivity 
	implements SharedPreferences.OnSharedPreferenceChangeListener {

	@Override
	public void onCreate(Bundle savedInstanceState) {
		ComponentName ss_ok;
		super.onCreate(savedInstanceState);
		addPreferencesFromResource(R.xml.preferences);
		PreferenceManager.getDefaultSharedPreferences(this).registerOnSharedPreferenceChangeListener(this);
		// Ensure that the service gets started if it was not running
		tickleService();
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
	}

	@Override
	public void onSharedPreferenceChanged (SharedPreferences sharedPreferences, String key) {
		tickleService();
	}

	/**
	 * Re-Launches the vplug service.
	 * The service will stay alive (or stop itself) depending on the settings.
	 */
	private void tickleService() {
		Intent intent= new Intent(getApplicationContext(), VPlugService.class);
		startService(intent);
	}

}
