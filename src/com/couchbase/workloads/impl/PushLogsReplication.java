package com.couchbase.workloads.impl;

import org.ektorp.DbAccessException;
import org.ektorp.ReplicationCommand;
import org.ektorp.ReplicationStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.androidtester.CouchbaseAndroidTesterActivity;
import com.couchbase.workloads.CouchbaseWorkload;

public class PushLogsReplication extends CouchbaseWorkload {

    private final static Logger LOG = LoggerFactory
            .getLogger(PushLogsReplication.class);

    @Override
    protected String performWork() {

        ReplicationCommand pushReplicationCommand = new ReplicationCommand.Builder()
        .source(CouchbaseAndroidTesterActivity.TEST_RESULTS_DB)
        .target(workloadRunner.getLogsReplicationUrl())
        .continuous(true)
        .build();

        LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Starting Continuous Push Replication of Logs");
        ReplicationStatus pushStatus;
        try {
            pushStatus = couchDbInstance.replicate(pushReplicationCommand);
            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Finished Replication of Logs: " + pushStatus.isOk());
        } catch (DbAccessException e) {
            LOG.debug(CouchbaseAndroidTesterActivity.TAG, "Replication of Logs Error: ", e);
        }

        while(!thread.isCancelled()) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
              //ignore
            }
        }

        return "Continuous Push Replication of Logs Workload was cancelled";

    }

    @Override
    public String getName() {
        return "Continuous Push Replication of Logs";
    }

}

