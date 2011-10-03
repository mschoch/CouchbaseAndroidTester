package com.couchbase.androidtester.monitors;

import java.util.List;


import android.content.Context;

public interface CouchbaseMonitor {

	void setCouchbaseMonitorDisplay(CouchbaseMonitorDisplay monitorDisplay);

	void setContext(Context context);

	void start();

	void stop();

	String getName();

	List<String> currentMeasures();

}
