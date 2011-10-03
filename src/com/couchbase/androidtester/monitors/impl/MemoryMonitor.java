package com.couchbase.androidtester.monitors.impl;

import android.os.Debug;

import com.couchbase.androidtester.monitors.CouchbasePollingMonitor;

public class MemoryMonitor extends CouchbasePollingMonitor {

	@Override
	public String[] getMonitorValues() {
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);
		float totalPrivateDirty = memoryInfo.getTotalPrivateDirty()/1024f;
		float totalPSS = memoryInfo.getTotalPss()/1024f;
		float totalSharedDirty = memoryInfo.getTotalSharedDirty()/1024f;

		String[] result = { "Total PSS " + String.format("%.2f", totalPSS) + "MB", "Total Private Dirty " + String.format("%.2f", totalPrivateDirty) + "MB", "Total Shared Dirty " + String.format("%.2f", totalSharedDirty) + "MB" };
		return result;
	}

	@Override
	public String getName() {
		return "Memory";
	}

}
