package com.couchbase.workloads;

import java.io.IOException;
import java.io.InputStream;


public interface CouchbaseWorkloadRunner {

	public InputStream openResource(String path) throws IOException;

	public String getWorkloadReplicationUrl();

	public String getLogsReplicationUrl();

}
