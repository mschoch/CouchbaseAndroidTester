package com.couchbase.androidtester.workloads;

import java.util.ArrayList;
import java.util.List;

import android.util.Log;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;

public class WorkloadHelper {

    /**
     * List of workload classes
     */
    private static List<String> getWorkloadClassNames() {
		//eventually replace this with something scanning the apk for a certain subpackage
		ArrayList<String> result = new ArrayList<String>();
		result.add("com.couchbase.androidtester.workloads.impl.CreateDocuments");
		result.add("com.couchbase.androidtester.workloads.impl.CRUDDocuments");
		return result;
    }

    /**
     * Return a list of instantiated CouchbaseWorkload objects
     * @return the list
     */
    public static ArrayList<CouchbaseWorkload> loadWorkloads() {
    	ArrayList<CouchbaseWorkload> workloads = new ArrayList<CouchbaseWorkload>();

    	List<String> allWorkloadClasses = getWorkloadClassNames();
    	for (String workloadClassName : allWorkloadClasses) {
			try {
				Class<?> workloadClass = WorkloadHelper.class.getClassLoader().loadClass(workloadClassName);
				CouchbaseWorkload cw = (CouchbaseWorkload)workloadClass.newInstance();
				workloads.add(cw);
			} catch (Exception e) {
				Log.e(CouchbaseAndroidTesterActivity.TAG, "Exception loading monitors", e);
			}
		}

    	return workloads;
    }

}
