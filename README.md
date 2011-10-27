# Couchbase Android Tester

## Getting Started

This project requires the latest version of Couchbase Mobile for Android.  This is the only
dependency not included in the project source tree.  Please follow the instructions from
http://www.couchbase.org/get/couchbase-mobile-for-android/current to get and install the latest
version of Couchbase Mobile for Android.

## Running workloads from an Android device through the UI

The simplest way to start workloads is by pressing the Start button from the workloads tab in the UI.

## Running workloads from an Android device from the command-line

    adb -e shell am start -a android.intent.action.MAIN -n com.couchbase.androidtester/.CouchbaseAndroidTesterActivity -e WORKLOAD com.couchbase.workloads.impl.CRUDDocuments

### Arguments

-e WORKLOAD &lt;comma-delimited list of workloads&gt;
 
-e WORKLOAD_SYNC_URL &lt;URL to which the workload DB will be synced&gt; 
 
-e LOGS_SYNC_URL &lt;URL to which logs will be pushed&gt;

## Running workloads with Java from the command-line

    java -cp bin:libs/org.ektorp-1.2.2-SNAPSHOT.jar:libs/slf4j-api-1.6.1.jar:libs/slf4j-jdk14-1.6.1.jar:javalibs/httpclient-4.1.1.jar:javalibs/httpcore-4.1.jar:javalibs/commons-logging-1.1.1.jar:javalibs/httpclient-cache-4.1.1.jar:libs/jackson-core-asl-1.8.5.jar:libs/jackson-mapper-asl-1.8.5.jar com.couchbase.javatester.JavaTester -workload com.couchbase.workloads.impl.PhotoShare,com.couchbase.workloads.impl.FiveMinuteIntervalReplication &lt; couch_urls.txt
    
### Arguments

-workload &lt;comma-delimited list of workloads&gt;

-workload_sync_url &lt;URL to which the workload DB will be synced&gt;

-log_sync_url &lt;URL to which logs will be pushed&gt;
    
NOTE: the single command-line argument accepted is a comma-delimited list of workloads to be run.  A list of CouchDB server URLs must be provided on standard input, 1 URL per line (see the couch_urls.txt file for an example of the format)

## Provided Workloads

- CRUD Documents  -  Create, Read, Update and Delete documents in sequence

- Photo Share  -  Create Documents and Attach Photos

- Calendar Usage  -  Create and Update Calendar Events

- Continuous Replication  -  Continuous bi-directional replication of the workload database to the cloud

- Five Minute Interval Replication  -  Non-continuous replication of the workload database at 5 minute intervals

## Provided Monitors

- Battery Level  -  Records the current battery level and plug status

- Couchbase  -  Records the status of Couchbase and the host/port

- Memory  -  Records various memory statistics provided by the Android platform

- Network  -  Records the network status and network interface type

## Adding your own workloads

Workloads are built by creating a class that extends the CouchbaseWorkload class.  The
CouchbaseWorkload class is abstract and requires you implement one method performWork().  In
performWork() you are to perform the entire workload.

### Registering the workload class

To have your workload class show up in the UI, you must register it in WorkloadHelper.java.

### Customizing the name displayed in the UI

To customize the name displayed in the UI, override the getDisplayName() method of CouchbaseWorkload.

### Making sure its possible to stop the workload

To ensure your workload can be stopped when requested by the user, your workload should
periodically check the status of thread.isCancelled().  If this returns true, your performWork()
method should return as soon as possible.

## Adding your own performance monitors

Performance monitors are built by implementing the CouchbaseMonitor interface.  Two convenience
classes are available depending on the type of monitor you are building.  You can extend
CouchbasePassiveMonitor for building a monitor that can operate without a thread of execution.
Or you can extend CouchbasePollingMonitor for building a monitor that will periodically poll
conditions.

It is important to note that the architecture is such that a single performance monitor can
return multiple measures.  This is useful for supporting operations where polling a single
API periodically is expensive, but yields multiple measures.  In this case a single Monitor
implementation should manage all the measures (see the Memory Monitor example).

### Setting up the monitor

If the monitor requires any set up, the start() method can be overridden.

### Passive Monitors

Passive Monitors should call monitorDisplay.valueChanged() whenever they have determined that
the value has changed.  Then they simply must respond to the currentMeasures() method returning 
a List of Strings describing the current measures.  (see the Battery Level Monitor example)

### Polling Monitors

Polling Monitors should implement getMonitorValues() which will be called periodically to poll
the system for the measures supported by this monitor.  (see the Memory Monitor example)

### Registering the monitor class

To have your monitor class show up in the UI, you must register it in MonitorHelper.java.

## Attribution

This project uses icons from http://glyphish.com/ under the Creative Commons Attribution license.
