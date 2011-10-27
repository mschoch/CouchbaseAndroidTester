package com.couchbase.javatester;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.ektorp.CouchDbConnector;
import org.ektorp.CouchDbInstance;
import org.ektorp.http.HttpClient;
import org.ektorp.http.StdHttpClient;
import org.ektorp.impl.StdCouchDbInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.couchbase.workloads.CouchbaseWorkload;
import com.couchbase.workloads.CouchbaseWorkloadRunner;
import com.couchbase.workloads.WorkloadHelper;

public class JavaTester implements CouchbaseWorkloadRunner {

    private final static Logger LOG = LoggerFactory
            .getLogger(JavaTester.class);

    private static final String TAG = "JavaTester";

    private List<CouchbaseWorkload> workloads = new ArrayList<CouchbaseWorkload>();
    private String workloadReplicaitonUrl;
    private String logReplicationUrl;

    public static void usage() {
        System.out.println("JavaTester <options>");
        System.out.println("\t-workload <comma-delimited list of workloads>  *REQUIRED*");
        System.out.println("\t-workload_sync_url <url>");
        System.out.println("\t-log_sync_url <url>");
    }

    public JavaTester(String workloadReplicaitonUrl, String logReplicationUrl) {
        this.workloadReplicaitonUrl = workloadReplicaitonUrl;
        this.logReplicationUrl = logReplicationUrl;
    }

    public static void main(String[] args) throws Exception {

        String startWorkloadString = null;
        String workloadSyncUrl = null;
        String logSyncUrl = null;

        int i = 0;
        String arg = null;
        while (i < args.length && args[i].startsWith("-")) {
            arg = args[i++];

            if(arg.equals("-workload")) {
                if (i < args.length) {
                    startWorkloadString = args[i++];
                }
                else {
                    System.err.println("-workload requires a string argument");
                    System.exit(1);
                }
            }
            else if(arg.equals("-workload_sync_url")) {
                if (i < args.length) {
                    workloadSyncUrl = args[i++];
                }
                else {
                    System.err.println("-workload_sync_url requires a string argument");
                    System.exit(1);
                }
            }
            else if(arg.equals("-log_sync_url")) {
                if (i < args.length) {
                    logSyncUrl = args[i++];
                }
                else {
                    System.err.println("-log_sync_url requires a string argument");
                    System.exit(1);
                }
            }

        }

        //startWorkloadString is required
        if(startWorkloadString == null) {
            usage();
            System.exit(1);
        }


        JavaTester tester = new JavaTester(workloadSyncUrl, logSyncUrl);
        tester.run(startWorkloadString);
    }

    public void run(String startWorkloadString) throws Exception {
        List<String> startWorkloads = null;
        if(startWorkloadString != null) {
            LOG.debug(TAG, "Requested to start workload " + startWorkloadString);
            startWorkloads = Arrays.asList(startWorkloadString.split(","));
        }
        else {
            System.out.println("Must provide at least one workload to run.");
            System.exit(1);
        }

        BufferedReader stdin = new BufferedReader(new InputStreamReader(System.in));
        String str;
        while ((str = stdin.readLine()) != null) {
            //expect each line to be a URL
            HttpClient httpClient = new StdHttpClient.Builder().url(str).build();
            CouchDbInstance couchDbInstance = new StdCouchDbInstance(httpClient);
            CouchDbConnector couchDbConnector = couchDbInstance.createConnector(WorkloadHelper.DEFAULT_WORKLOAD_DB, true);


            for(String workloadName : startWorkloads) {
                CouchbaseWorkload workload = WorkloadHelper.loadWorkload(workloadName);
                workload.setCouchDbInstance(couchDbInstance);
                workload.setCouchDbConnector(couchDbConnector);
                workload.setCouchbaseWorkloadRunner(this);
                LOG.debug(TAG, "Starting workload " + workload.getName());
                workload.start();
                //add to our list
                workloads.add(workload);
            }

        }

        //wait for all workloads to finish (they never will)
        for (CouchbaseWorkload workload : workloads) {
            workload.waitForCompletion();
        }
    }

    //implementation of CouchbaseWorkloadRunner interface

    @Override
    public InputStream openResource(String path) throws IOException {
        File resource = new File("assets/" + path);
        FileInputStream is = new FileInputStream(resource);
        return is;
    }

    @Override
    public String getLogsReplicationUrl() {
        String result = logReplicationUrl;
        if(result == null) {
            result = WorkloadHelper.DEFAULT_LOGS_SYNC_URL;
        }
        return result;
    }

    @Override
    public String getWorkloadReplicationUrl() {
        String result = workloadReplicaitonUrl;
        if(result == null) {
            result = WorkloadHelper.DEFAULT_WORKLOAD_SYNC_URL;
        }
        return result;
    }

}
