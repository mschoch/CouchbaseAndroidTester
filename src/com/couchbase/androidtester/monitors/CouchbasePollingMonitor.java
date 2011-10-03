package com.couchbase.androidtester.monitors;

import java.util.ArrayList;
import java.util.List;


import android.content.Context;
import android.os.AsyncTask;

public abstract class CouchbasePollingMonitor extends AsyncTask<Void, String, Void> implements CouchbaseMonitor {

	public static final int POLL_INTERVAL = 1000;
	protected CouchbaseMonitorDisplay monitorDisplay;
	protected Context context;
	protected List<String> currentMeasures = new ArrayList<String>();

	@Override
	protected Void doInBackground(Void... params) {
		while(!isCancelled()) {
			String[] monitorValues = getMonitorValues();
			publishProgress(monitorValues);
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				cancel(false);
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(String... values) {
		currentMeasures = new ArrayList<String>();
		for (String measure : values) {
			currentMeasures.add(measure);
		}
		monitorDisplay.valueChanged();
	}

	@Override
	public void setCouchbaseMonitorDisplay(
			CouchbaseMonitorDisplay monitorDisplay) {
		this.monitorDisplay = monitorDisplay;
	}

	@Override
	public void setContext(Context context) {
		this.context = context;
	}

	public String getName() {
		return this.getClass().getName();
	}

	@Override
	public List<String> currentMeasures() {
		return currentMeasures;
	}

	@Override
	public void start() {
		this.execute();
	}

	@Override
	public void stop() {
		this.cancel(true);
	}

	public abstract String[] getMonitorValues();

}
