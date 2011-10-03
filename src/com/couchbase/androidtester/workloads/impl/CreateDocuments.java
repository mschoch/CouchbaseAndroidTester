package com.couchbase.androidtester.workloads.impl;

import java.util.HashMap;

import org.ektorp.CouchDbConnector;

import com.couchbase.androidtester.workloads.CouchbaseWorkload;

public class CreateDocuments extends CouchbaseWorkload {

	private static int numberOfDocuments = 1000;

	public CreateDocuments() {
		indeterminate = false;
		total = numberOfDocuments;
	}

	@Override
	protected String performWork() {

		CouchDbConnector couchDbConnector = couchDbInstance.createConnector("workload", true);

		int documentsCreated = 0;
		while(!task.isCancelled() && (documentsCreated < numberOfDocuments)) {
			couchDbConnector.create(documentTemplate());
			documentsCreated++;
			progress++;
			task.publishWorkProgress("Created Document " + documentsCreated);
		}

		String resultMessage = "" +  documentsCreated + " documents";

		if(task.isCancelled()) {
			resultMessage = "Cancelled after creating " + resultMessage;
		}
		else {
			resultMessage = "Finished creating " + resultMessage;
		}
		progress = 0;
		return resultMessage;
	}

	protected HashMap<String, String> documentTemplate() {
		HashMap<String, String> result = new HashMap<String, String>();
		result.put("type", "sample");
		return result;
	}

	@Override
	public String getName() {
		return "Create 1000 Documents";
	}

}
