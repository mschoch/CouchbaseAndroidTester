package com.couchbase.util;

import java.util.LinkedList;
import java.util.Queue;

public class MovingAverage {

    private final Queue<Long> window = new LinkedList<Long>();
    private final int period;
    private long sum;

    public MovingAverage(int period) {
        this.period = period;
    }

    public void newNum(long num) {
        sum += num;
        window.add(num);
        if (window.size() > period) {
            sum -= window.remove();
        }
    }

    public long getAvg() {
        if (window.isEmpty()) return 0; // technically the average is undefined
        return sum / window.size();
    }

}
