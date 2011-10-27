package com.couchbase.workloads;

import java.io.IOException;
import java.io.InputStream;


public interface CouchbaseWorkloadRunner {

	public void workloadReportsProgress(CouchbaseWorkload workload, String progressMessage);

	public void workloadReportsFinish(CouchbaseWorkload workload, String finishMessage);

	public InputStream openResource(String path) throws IOException;

}
