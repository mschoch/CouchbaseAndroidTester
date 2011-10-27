package com.couchbase.androidtester.monitors;

import java.util.List;
import java.util.Map;

import android.content.Context;

public interface CouchbaseMonitor {

	void setCouchbaseMonitorDisplay(CouchbaseMonitorDisplay monitorDisplay);

	void setContext(Context context);

	void start();

	void stop();

	String getDisplayName();

	String getSystemName();

	List<String> currentMeasures();
	Map<String, Object> currentMeasuresJson();

}
