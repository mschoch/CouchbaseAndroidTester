package com.couchbase.androidtester.workloads.impl;

import org.ektorp.DbAccessException;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;

import android.util.Log;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.androidtester.workloads.CouchbaseWorkload;

public class ContinuousReplication extends CouchbaseWorkload {

    private static String defaultReplication = "http://mschoch.ic.ht/android";

    @Override
    protected String performWork() {

        ReplicationCommand pullReplicationCommand = new ReplicationCommand.Builder()
        .source(defaultReplication)
        .target(CouchbaseAndroidTesterActivity.DEFAULT_WORKLOAD_DB)
        .continuous(true)
        .build();

        Log.v(CouchbaseAndroidTesterActivity.TAG, "Starting Continuous Pull Replication");
        ReplicationStatus pullStatus;
        try {
            pullStatus = couchDbInstance.replicate(pullReplicationCommand);
            Log.v(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + pullStatus.isOk());
        } catch (DbAccessException e) {
            Log.v(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
        }

        ReplicationCommand pushReplicationCommand = new ReplicationCommand.Builder()
        .source(CouchbaseAndroidTesterActivity.DEFAULT_WORKLOAD_DB)
        .target(defaultReplication)
        .continuous(true)
        .build();

        Log.v(CouchbaseAndroidTesterActivity.TAG, "Starting Continuous Push Replication");
        ReplicationStatus pushStatus;
        try {
            pushStatus = couchDbInstance.replicate(pushReplicationCommand);
            Log.v(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + pushStatus.isOk());
        } catch (DbAccessException e) {
            Log.v(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
        }

        while(!task.isCancelled()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
              //ignore
            }
        }

        return "Continuous Replication Workload was cancelled";

    }

    @Override
    public String getName() {
        return "Continuous Replication";
    }

}
