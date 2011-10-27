package com.couchbase.androidtester.monitors;


import android.content.Context;

public abstract class CouchbasePassiveMonitor implements CouchbaseMonitor {

	protected CouchbaseMonitorDisplay monitorDisplay;
	protected Context context;

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

}
