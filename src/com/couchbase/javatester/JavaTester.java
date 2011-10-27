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

    private static final String TAG = "JavaTester";

    private List<CouchbaseWorkload> workloads = new ArrayList<CouchbaseWorkload>();

    private final static Logger LOG = LoggerFactory
            .getLogger(JavaTester.class);

    public static void usage() {
        System.out.println("JavaTester <comma delimted list of workloads to run>");
    }

    public static void main(String[] args) throws Exception {

        JavaTester tester = new JavaTester();

        //expect 1 argument, a comma-delimited list of workloads to run
        if(args.length != 1) {
            usage();
            System.exit(1);
        }

        String startWorkloadString = args[0];
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
    public void workloadReportsFinish(CouchbaseWorkload workload,
            String finishMessage) {

    }

    @Override
    public void workloadReportsProgress(CouchbaseWorkload workload,
            String progressMessage) {

    }

}
