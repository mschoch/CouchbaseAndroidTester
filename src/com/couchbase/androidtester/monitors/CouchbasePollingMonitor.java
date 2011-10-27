package com.couchbase.androidtester.monitors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.os.AsyncTask;

public abstract class CouchbasePollingMonitor extends AsyncTask<Void, Void, Void> implements CouchbaseMonitor {

	public static final int POLL_INTERVAL = 1000;
	protected CouchbaseMonitorDisplay monitorDisplay;
	protected Context context;
	protected List<String> currentMeasures = new ArrayList<String>();
	protected Map<String, Object> currentMeasuresJson = new HashMap<String, Object>();

	@Override
	protected Void doInBackground(Void... params) {
		while(!isCancelled()) {
			getMonitorValues();
			publishProgress();
			try {
				Thread.sleep(POLL_INTERVAL);
			} catch (InterruptedException e) {
				cancel(false);
			}
		}
		return null;
	}

	@Override
	protected void onProgressUpdate(Void... progress) {
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

	public String getDisplayName() {
		return this.getClass().getName();
	}

	@Override
	public List<String> currentMeasures() {
		return currentMeasures;
	}

	@Override
	public Map<String, Object> currentMeasuresJson() {
	    return currentMeasuresJson;
	}

	@Override
	public void start() {
		this.execute();
	}

	@Override
	public void stop() {
		this.cancel(true);
	}

	public abstract void getMonitorValues();

}
