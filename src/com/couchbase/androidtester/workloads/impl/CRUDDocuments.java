package com.couchbase.androidtester.workloads.impl;

import java.util.HashMap;
import java.util.Map;

import org.ektorp.UpdateConflictException;

import android.util.Log;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.androidtester.workloads.CouchbaseWorkload;

public class CRUDDocuments extends CouchbaseWorkload {

	private static int numberOfDocuments = 1000;

	public CRUDDocuments() {
		indeterminate = false;
		total = 4 * numberOfDocuments;
	}

	@Override
	protected String performWork() {

		int documentsCreated = 0;
		while(!task.isCancelled() && (documentsCreated < numberOfDocuments)) {

			//create
			HashMap<String, String> document = documentTemplate();
			couchDbConnector.create(document);
			documentsCreated++;
			progress++;
			task.publishWorkProgress("Created Document " + documentsCreated);

			String documentId = document.get("_id");
			Log.v(CouchbaseAndroidTesterActivity.TAG, "Document created got id " + documentId);

			//read
			@SuppressWarnings("unchecked")
			Map<String, Object> documentRead = couchDbConnector.get(Map.class, documentId);
			progress++;

			//update
			documentRead.put("updated", "true");
			try {
				couchDbConnector.update(documentRead);
			} catch (UpdateConflictException e) {
			    Log.v(CouchbaseAndroidTesterActivity.TAG, "Update Conflict", e);
			}
			progress++;

			//delete
			couchDbConnector.delete(documentRead);
			progress++;
		}

		String resultMessage = "" +  documentsCreated + " documents";

		if(task.isCancelled()) {
			resultMessage = "Cancelled CRUD after " + resultMessage;
		}
		else {
			resultMessage = "Finished CRUD " + resultMessage;
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
		return "CRUD 1000 Documents";
	}

}
