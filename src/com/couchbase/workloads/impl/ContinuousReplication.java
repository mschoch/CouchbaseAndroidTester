package com.couchbase.workloads.impl;

import org.ektorp.DbAccessException;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.workloads.CouchbaseWorkload;
import com.couchbase.workloads.WorkloadHelper;

public class ContinuousReplication extends CouchbaseWorkload {

    private final static Logger LOG = LoggerFactory
            .getLogger(ContinuousReplication.class);

    private String workloadDb = WorkloadHelper.DEFAULT_WORKLOAD_DB;

    @Override
    protected String performWork() {

        if(extras.containsKey(WorkloadHelper.EXTRA_WORKLOAD_DB)) {
            workloadDb = (String)extras.get(WorkloadHelper.EXTRA_WORKLOAD_DB);
        }

        ReplicationCommand pullReplicationCommand = new ReplicationCommand.Builder()
        .source(workloadRunner.getWorkloadReplicationUrl())
        .target(workloadDb)
        .continuous(true)
        .build();

        LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Starting Continuous Pull Replication");
        ReplicationStatus pullStatus;
        try {
            pullStatus = couchDbInstance.replicate(pullReplicationCommand);
            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + pullStatus.isOk());
        } catch (DbAccessException e) {
            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
        }

        ReplicationCommand pushReplicationCommand = new ReplicationCommand.Builder()
        .source(workloadDb)
        .target(workloadRunner.getWorkloadReplicationUrl())
        .continuous(true)
        .build();

        LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Starting Continuous Push Replication");
        ReplicationStatus pushStatus;
        try {
            pushStatus = couchDbInstance.replicate(pushReplicationCommand);
            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Finished Replication: " + pushStatus.isOk());
        } catch (DbAccessException e) {
            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Replication Error: ", e);
        }

        while(!thread.isCancelled()) {
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
