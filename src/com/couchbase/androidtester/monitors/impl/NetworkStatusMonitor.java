package com.couchbase.androidtester.monitors.impl;

import java.util.ArrayList;
import java.util.List;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.couchbase.androidtester.monitors.CouchbasePassiveMonitor;

public class NetworkStatusMonitor extends CouchbasePassiveMonitor {

    protected String connectedMessage = "Unknown";
    protected String typeMessage = "Unknown";

    @Override
    public void start() {
        context.registerReceiver(networkReceiver, new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION));
    }

    @Override
    public void stop() {
        context.unregisterReceiver(networkReceiver);
    }

    @Override
    public List<String> currentMeasures() {
        ArrayList<String> result = new ArrayList<String>();
        result.add(connectedMessage);
        result.add(typeMessage);
        return result;
    }

    private BroadcastReceiver networkReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            NetworkInfo netInfo = (NetworkInfo)intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);

            if(netInfo.isConnected()) {
                connectedMessage = "Connected";
            }
            else {
                connectedMessage = "Disconnected";
            }

            if(netInfo.getTypeName() != null) {
                typeMessage = netInfo.getTypeName();
            }
        }
    };

    public String getName() {
        return "Network Status";
    }

}