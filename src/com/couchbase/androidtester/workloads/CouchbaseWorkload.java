package com.couchbase.androidtester.workloads;

import org.ektorp.CouchDbInstance;


import android.os.AsyncTask;

public abstract class CouchbaseWorkload {

	protected CouchbaseWorkloadRunner workloadRunner;
	protected CouchDbInstance couchDbInstance;
	protected CouchbaseWorkloadTask task = null;
	protected int progress = 0;
	protected int total = 100;
	protected boolean indeterminate = true;

	public void setCouchbaseWorkloadRunner(CouchbaseWorkloadRunner workloadRunner) {
		this.workloadRunner = workloadRunner;
	}

	public void setCouchDbInstance(CouchDbInstance couchDbInstance) {
		this.couchDbInstance = couchDbInstance;
	}

	public String getName() {
		return this.getClass().getName();
	}

	public void start() {
		if(task == null) {
			task = new CouchbaseWorkloadTask();
			task.execute();
		}
		else {
			throw new IllegalStateException("Workload is already running");
		}
	}

	public void stop() {
		if(task != null) {
			task.cancel(true);
			task = null;
		}
		else {
			throw new IllegalStateException("Workload is not running");
		}
	}

	public boolean isRunning() {
		return (task != null);
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

	protected abstract String performWork();


	public class CouchbaseWorkloadTask extends AsyncTask<Void, String, String> {


		@Override
		protected String doInBackground(Void... params) {
			return performWork();
		}

		@Override
		protected void onProgressUpdate(String... progressMessages) {
			for (String progressMessage : progressMessages) {
				workloadRunner.workloadReportsProgress(CouchbaseWorkload.this, progressMessage);
			}
		}

		@Override
		protected void onPostExecute(String finishMessage) {
			workloadRunner.workloadReportsFinish(CouchbaseWorkload.this, finishMessage);
			task = null;
		}

		public void publishWorkProgress(String... values) {
			publishProgress(values);
		}
	}

}
