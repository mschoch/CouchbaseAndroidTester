package com.couchbase.workloads.impl;

import org.ektorp.DbAccessException;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.workloads.CouchbaseWorkload;
import com.couchbase.workloads.WorkloadHelper;

public abstract class IntervalReplication extends CouchbaseWorkload {

    private final static Logger LOG = LoggerFactory
            .getLogger(IntervalReplication.class);

    private static String defaultReplication = "http://mschoch.ic.ht/android";

    @Override
    protected String performWork() {

        while(!thread.isCancelled()) {
            try {

                new Thread(new Runnable() {

                    @Override
                    public void run() {
                        ReplicationCommand pullReplicationCommand = new ReplicationCommand.Builder()
                        .source(defaultReplication)
                        .target(WorkloadHelper.DEFAULT_WORKLOAD_DB)
                        .continuous(false)
                        .build();

                        LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Starting Pull Replication");
                        ReplicationStatus pullStatus;
                        try {
                            pullStatus = couchDbInstance.replicate(pullReplicationCommand);
                            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + pullStatus.isOk());
                        } catch (DbAccessException e) {
                            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
                        }

                        ReplicationCommand pushReplicationCommand = new ReplicationCommand.Builder()
                        .source(WorkloadHelper.DEFAULT_WORKLOAD_DB)
                        .target(defaultReplication)
                        .continuous(false)
                        .build();

                        LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Starting Push Replication");
                        ReplicationStatus pushStatus;
                        try {
                            pushStatus = couchDbInstance.replicate(pushReplicationCommand);
                            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + pushStatus.isOk());
                        } catch (DbAccessException e) {
                            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
                        }
                    }

                }).start();


                Thread.sleep(getInterval());
            } catch (InterruptedException e) {
              //ignore
            }
        }

        return "Five Minute Interval Replication Workload was cancelled";

    }

    protected abstract int getInterval();

}
