package com.couchbase.androidtester.workloads.impl;

import org.ektorp.DbAccessException;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;

import android.util.Log;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.androidtester.workloads.CouchbaseWorkload;

public class CancelPullReplication extends CouchbaseWorkload {

    public static final String DATABASE_NAME = "workload";

    @Override
    protected String performWork() {

        couchDbInstance.createConnector(DATABASE_NAME, true);


        Thread replicate = new Thread(new Runnable() {

            @Override
            public void run() {

                ReplicationCommand pullReplicationCommand = new ReplicationCommand.Builder()
                .source("http://couchbase.iriscouch.com/grocery-sync")
                .target(DATABASE_NAME)
                .build();

                Log.v(CouchbaseAndroidTesterActivity.TAG, "Starting Replication");
                ReplicationStatus status;
                try {
                    status = couchDbInstance.replicate(pullReplicationCommand);
                    Log.v(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + status.isOk());
                } catch (DbAccessException e) {
                    Log.v(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
                }

            }
        });

        Thread replicateCancel = new Thread(new Runnable() {

            @Override
            public void run() {

                ReplicationCommand pullReplicationCancelCommand = new ReplicationCommand.Builder()
                .source("http://couchbase.iriscouch.com/grocery-sync")
                .target(DATABASE_NAME)
                .cancel(true)
                .build();

                Log.v(CouchbaseAndroidTesterActivity.TAG, "Starting Replication Cancelation");
                ReplicationStatus status;
                try {
                    status = couchDbInstance.replicate(pullReplicationCancelCommand);
                    Log.v(CouchbaseAndroidTesterActivity.TAG, "Finished Replication Cancelation: " + status.isOk());
                } catch (DbAccessException e) {
                    Log.v(CouchbaseAndroidTesterActivity.TAG, "Replication Cancellation Error: ", e);
                }

            }
        });


        try {
            replicate.start();
            //wait 2 seconds
            Thread.sleep(2 * 1000);
            replicateCancel.start();

            //wait for these threads to finish to end workload
            replicateCancel.join();
            replicate.join();
        } catch (InterruptedException e) {
            Log.v(CouchbaseAndroidTesterActivity.TAG, "Thread was interuppted");
        }

        return null;
    }

    @Override
    public String getName() {
        return "Cancel Pull Replication";
    }
}
