package com.couchbase.androidtester;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.ektorp.CouchDbConnector;

import android.content.Context;
import android.os.AsyncTask;
import android.provider.Settings.Secure;

import com.couchbase.androidtester.monitors.CouchbaseMonitor;

public class MonitorLogAsyncTask extends AsyncTask<Void, Void, Void> {

    private static final int logInterval = 30 * 1000;

    private List<CouchbaseMonitor> monitors;
    private CouchDbConnector connector;
    private Context context;
    private String deviceId;

    public MonitorLogAsyncTask(Context context, CouchDbConnector connector, List<CouchbaseMonitor> monitors) {
        this.connector = connector;
        this.monitors = monitors;
        this.context = context;

        //lookup device id
        deviceId = Secure.getString(this.context.getContentResolver(), Secure.ANDROID_ID);
    }

    @Override
    protected Void doInBackground(Void... params) {
        while(!isCancelled()) {

            //create an object to contain the monitor stats
            Map<String, Object> logMessage = new HashMap<String, Object>();

            //add a timestamp
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");//spec for RFC3339
            String currentTimeString = sdf.format(new Date());
            logMessage.put("timestamp", currentTimeString);

            //add something to identify the device
            logMessage.put("deviceId", deviceId);

            //collect the stats
            for (CouchbaseMonitor monitor : monitors) {
                Map<String, Object> measureLog = monitor.currentMeasuresJson();
                logMessage.put(monitor.getSystemName(), measureLog);
            }

            //write to stats database
            connector.create(logMessage);

            //sleep the prescribed amount of time
            try {
                Thread.sleep(logInterval);
            } catch (InterruptedException e) {
                //ignore
            }
        }

        return null;
    }

}
