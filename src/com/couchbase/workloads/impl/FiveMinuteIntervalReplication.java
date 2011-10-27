package com.couchbase.workloads.impl;


public class FiveMinuteIntervalReplication extends IntervalReplication {

    protected int getInterval() {
        return 1000 * 60 * 5;
    }

    @Override
    public String getName() {
        return "Five Minute Interval Replication";
    }

}
