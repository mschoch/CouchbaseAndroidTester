package com.couchbase.androidtester.monitors.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

import com.couchbase.android.Intents.CouchbaseError;
import com.couchbase.android.Intents.CouchbaseStarted;
import com.couchbase.androidtester.monitors.CouchbasePassiveMonitor;

public class CouchbaseServiceMonitor extends CouchbasePassiveMonitor {

	private static String currentValue = "Not Running";

	@Override
	public void start() {
		context.registerReceiver(couchbaseReceiver, new IntentFilter(
				CouchbaseStarted.ACTION));
		context.registerReceiver(couchbaseReceiver, new IntentFilter(
				CouchbaseError.ACTION));
	}

	@Override
	public void stop() {
		context.unregisterReceiver(couchbaseReceiver);
	}

	private final BroadcastReceiver couchbaseReceiver = new BroadcastReceiver() {

		@Override
		public void onReceive(Context context, Intent intent) {
			if(CouchbaseStarted.ACTION.equals(intent.getAction())) {
				String host = CouchbaseStarted.getHost(intent);
				int port = CouchbaseStarted.getPort(intent);
				currentValue = "Listening on " + host + ":" + port;
				monitorDisplay.valueChanged();
			}
			else if(CouchbaseError.ACTION.equals(intent.getAction())) {
				String message = CouchbaseError.getMessage(intent);
				currentValue = "Error: " + message;
				monitorDisplay.valueChanged();
			}
		}
	};

	public String getName() {
		return "Couchbase";
	};

	@Override
	public List<String> currentMeasures() {
		ArrayList<String> result = new ArrayList<String>();
		result.add(currentValue);
		return result;
	}

}
