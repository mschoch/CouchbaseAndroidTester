package com.couchbase.androidtester.monitors;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;

public class MonitorHelper {

	/**
	 * List of monitor classes
	 */
	private static List<String> getMonitorClassNames() {
		//eventually replace this with something scanning the apk for a certain subpackage
		ArrayList<String> result = new ArrayList<String>();
		result.add("com.couchbase.androidtester.monitors.impl.CouchbaseServiceMonitor");
		result.add("com.couchbase.androidtester.monitors.impl.BatteryLevelMonitor");
		result.add("com.couchbase.androidtester.monitors.impl.NetworkStatusMonitor");
		result.add("com.couchbase.androidtester.monitors.impl.MemoryMonitor");
		return result;
	}

	/**
	 * Return a list of instantiated CouchbaseMonitor objects
	 * @return the list
	 */
    public static ArrayList<CouchbaseMonitor> loadMonitors() {
    	ArrayList<CouchbaseMonitor> monitors = new ArrayList<CouchbaseMonitor>();

    	List<String> allMonitorClasses = getMonitorClassNames();
    	for (String monitorClassName : allMonitorClasses) {
    		try {
				Class<?> monitorClass = MonitorHelper.class.getClassLoader().loadClass(monitorClassName);
				CouchbaseMonitor cm = (CouchbaseMonitor)monitorClass.newInstance();
				monitors.add(cm);
			} catch (Exception e) {
				Log.e(CouchbaseAndroidTesterActivity.TAG, "Exception loading monitors", e);
			}
		}

    	return monitors;
    }

}
