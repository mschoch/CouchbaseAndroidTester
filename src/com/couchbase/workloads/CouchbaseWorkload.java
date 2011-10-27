package com.couchbase.workloads;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;

public abstract class CouchbaseWorkload {

	protected CouchbaseWorkloadRunner workloadRunner;
	protected CouchDbInstance couchDbInstance;
	protected CouchDbConnector couchDbConnector;
	protected int progress = 0;
	protected int total = 100;
	protected boolean indeterminate = true;
	protected CouchbaseWorkloadThread thread = null;

	public void setCouchbaseWorkloadRunner(CouchbaseWorkloadRunner workloadRunner) {
		this.workloadRunner = workloadRunner;
	}

	public void setCouchDbInstance(CouchDbInstance couchDbInstance) {
		this.couchDbInstance = couchDbInstance;
	}

	public void setCouchDbConnector(CouchDbConnector couchDbConnector) {
	    this.couchDbConnector = couchDbConnector;
	}

	public String getName() {
		return this.getClass().getName();
	}

	public void start() {
		if(thread == null) {
			thread = new CouchbaseWorkloadThread();
			thread.start();
		}
		else {
			throw new IllegalStateException("Workload is already running");
		}
	}

	public void stop() {
		if(thread != null) {
			thread.cancel(true);
		}
		else {
			throw new IllegalStateException("Workload is not running");
		}
	}

	public boolean isRunning() {
		return (thread != null);
	}

	public int getProgress() {
		return progress;
	}

	public int getTotal() {
		return total;
	}

	public boolean isIndeterminate() {
		return indeterminate;
	}

	public void waitForCompletion() throws InterruptedException {
	    thread.join();
	}

	protected abstract String performWork();


	public class CouchbaseWorkloadThread extends Thread {

	    protected boolean cancelled = false;

	    @Override
	    public void run() {
	        try {
                performWork();
            } finally {
                thread = null;
            }
	    }

	    public void cancel(boolean interrupt) {
	        cancelled = true;
	        if(interrupt) {
	            interrupt();
	        }
	    }

	    public boolean isCancelled() {
            return cancelled;
        }


//		@Override
//		protected String doInBackground(Void... params) {
//			return performWork();
//		}
//
//		@Override
//		protected void onProgressUpdate(String... progressMessages) {
//			for (String progressMessage : progressMessages) {
//				workloadRunner.workloadReportsProgress(CouchbaseWorkload.this, progressMessage);
//			}
//		}
//
//		@Override
//		protected void onPostExecute(String finishMessage) {
//			workloadRunner.workloadReportsFinish(CouchbaseWorkload.this, finishMessage);
//			task = null;
//		}
//
//		public void publishWorkProgress(String... values) {
//			publishProgress(values);
//		}
	}

}
