package com.couchbase.androidtester.monitors.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;

import com.couchbase.androidtester.monitors.CouchbasePassiveMonitor;

public class BatteryLevelMonitor extends CouchbasePassiveMonitor {

	private String batteryLevelMessage = "Unknown";
	private String pluggedMessage = "Unknown";

	@Override
	public void start() {
		context.registerReceiver(batteryReceiver, new IntentFilter(
				Intent.ACTION_BATTERY_CHANGED));
	}

	@Override
	public void stop() {
		context.unregisterReceiver(batteryReceiver);
	}

	private BroadcastReceiver batteryReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			int level = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
			batteryLevelMessage = "" + level + "%";

			int plugged = intent.getIntExtra(BatteryManager.EXTRA_PLUGGED, 0);
			switch(plugged) {
			case 0:
				pluggedMessage = "On Battery";
				break;
			case BatteryManager.BATTERY_PLUGGED_AC:
				pluggedMessage = "Plugged AC";
				break;
			case BatteryManager.BATTERY_PLUGGED_USB:
				pluggedMessage = "Plugged USB";
				break;
			}

			monitorDisplay.valueChanged();
		}
	};

	public String getName() {
		return "Battery Level";
	};

	@Override
	public List<String> currentMeasures() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(batteryLevelMessage);
		result.add(pluggedMessage);
		return result;
	}

}
