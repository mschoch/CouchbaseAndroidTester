package com.couchbase.workloads.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import com.couchbase.workloads.CouchbaseWorkload;

public class Calendar extends CouchbaseWorkload {

    @Override
    protected String performWork() {

        int calendarEventsCreated = 0;

        while(!thread.isCancelled()) {

            //create a calendar item
            Map<String,Object> document = documentTemplate();
            couchDbConnector.create(document);
            workloadRunner.publishedWorkloadDocumentWithIdandRevision((String)document.get("_id"), (String)document.get("_rev"));
            calendarEventsCreated++;

            //wait
            try {
                int delayBetweenPosts = 1 * (500 + new Random().nextInt(500));
                Thread.sleep(delayBetweenPosts);
            } catch (InterruptedException e) {
                //ignore
            }

            //update the calendar item
            document = cancelEvent(document);
            couchDbConnector.update(document);
            workloadRunner.publishedWorkloadDocumentWithIdandRevision((String)document.get("_id"), (String)document.get("_rev"));

            //wait some more
            try {
                int delayBetweenPosts = 1 * (500 + new Random().nextInt(500));
                Thread.sleep(delayBetweenPosts);
            } catch (InterruptedException e) {
                //ignore
            }

        }


        String resultMessage = "" +  calendarEventsCreated + " events created";

        if(thread.isCancelled()) {
            resultMessage = "Cancelled Calendar Usage after " + resultMessage;
        }
        else {
            resultMessage = "Finished Calendar Usage " + resultMessage;
        }
        progress = 0;
        return resultMessage;

    }

    @SuppressWarnings("unchecked")
    protected Map<String, Object> cancelEvent(Map<String, Object> document) {
        Map<String, Object> data = (Map<String,Object>)document.get("data");
        data.put("status", "canceled");
        document.put("data", data);
        return document;
    }

    protected HashMap<String, Object> documentTemplate() {
        HashMap<String, Object> when = new HashMap<String, Object>();
        when.put("start", "2010-04-17T15:00:00.000Z");
        when.put("end", "2010-04-17T17:00:00.000Z");


        HashMap<String, Object> data = new HashMap<String, Object>();
        data.put("title", "CouchConf");
        data.put("details", "Join us at a CouchConf near you!");
        data.put("transparency", "opaque");
        data.put("status", "confirmed");
        data.put("location", "New York, NY");
        data.put("when", when);

        HashMap<String, Object> result = new HashMap<String, Object>();
        result.put("data", data);
        return result;
    }

    @Override
    public String getName() {
        return "Calendar Usage";
    }

}
