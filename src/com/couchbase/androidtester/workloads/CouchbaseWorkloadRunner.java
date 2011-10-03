package com.couchbase.androidtester.workloads;


public interface CouchbaseWorkloadRunner {

	public void workloadReportsProgress(CouchbaseWorkload workload, String progressMessage);

	public void workloadReportsFinish(CouchbaseWorkload workload, String finishMessage);

}
