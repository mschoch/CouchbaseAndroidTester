package com.couchbase.workloads.impl;

import java.util.HashMap;
import java.util.Map;

import org.ektorp.UpdateConflictException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.workloads.CouchbaseWorkload;

public class CRUDDocuments extends CouchbaseWorkload {

    private final static Logger LOG = LoggerFactory
            .getLogger(CRUDDocuments.class);

	@Override
	protected String performWork() {

		int documentsCreated = 0;
		while(!thread.isCancelled()) {

			//create
			HashMap<String, String> document = documentTemplate();
			couchDbConnector.create(document);
			workloadRunner.publishedWorkloadDocumentWithIdandRevision((String)document.get("_id"), (String)document.get("_rev"));
			documentsCreated++;

			String documentId = document.get("_id");
			LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Document created got id " + documentId);

			//read
			@SuppressWarnings("unchecked")
			Map<String, Object> documentRead = couchDbConnector.get(Map.class, documentId);

			//update
			documentRead.put("updated", "true");
			try {
				couchDbConnector.update(documentRead);
			} catch (UpdateConflictException e) {
			    LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Update Conflict", e);
			}
			workloadRunner.publishedWorkloadDocumentWithIdandRevision((String)documentRead.get("_id"), (String)documentRead.get("_rev"));

			//delete
			couchDbConnector.delete(documentRead);
		}

		String resultMessage = "" +  documentsCreated + " documents";

		if(thread.isCancelled()) {
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
		return "CRUD Documents";
	}

}
