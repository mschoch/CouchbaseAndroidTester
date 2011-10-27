package com.couchbase.androidtester.monitors.impl;

import java.util.ArrayList;
import java.util.HashMap;

import android.os.Debug;

import com.couchbase.androidtester.monitors.CouchbasePollingMonitor;

public class MemoryMonitor extends CouchbasePollingMonitor {

	@Override
	public void getMonitorValues() {
		Debug.MemoryInfo memoryInfo = new Debug.MemoryInfo();
		Debug.getMemoryInfo(memoryInfo);
		float totalPrivateDirty = memoryInfo.getTotalPrivateDirty()/1024f;
		float totalPSS = memoryInfo.getTotalPss()/1024f;
		float totalSharedDirty = memoryInfo.getTotalSharedDirty()/1024f;

	    currentMeasures = new ArrayList<String>();
	    currentMeasures.add("Total PSS " + String.format("%.2f", totalPSS) + "MB");
	    currentMeasures.add("Total Private Dirty " + String.format("%.2f", totalPrivateDirty) + "MB");
	    currentMeasures.add("Total Shared Dirty " + String.format("%.2f", totalSharedDirty) + "MB");

	    currentMeasuresJson = new HashMap<String, Object>();
	    currentMeasuresJson.put("totalPSS", totalPSS);
	    currentMeasuresJson.put("totalPrivateDirty", totalPrivateDirty);
	    currentMeasuresJson.put("totalSharedDirty", totalSharedDirty);

	}

	@Override
	public String getDisplayName() {
		return "Memory";
	}

	@Override
	public String getSystemName() {
	    return "memory";
	}

}
