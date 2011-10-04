# Couchbase Android Tester

## Quick Start

A binary of this utility can be downloaded from https://github.com/downloads/mschoch/CouchbaseAndroidTester/CouchbaseAndroidTester.apk

## Getting Started

This project requires the latest version of Couchbase Mobile for Android.  This is the only
dependency not included in the project source tree.  Please follow the instructions from
http://www.couchbase.org/get/couchbase-mobile-for-android/current to get and install the latest
version of Couchbase Mobile for Android.

## Adding your own workloads

Workloads are built by creating a class that extends the CouchbaseWorkload class.  The
CouchbaseWorkload class is abstract and requires you implement one method performWork().  In
performWork() you are to perform the entire workload.

### Registering the workload class

To have your workload class show up in the UI, you must register it in WorkloadHelper.java.

### Customizing the name displayed in the UI

To customize the name displayed in the UI, override the getName() method of CouchbaseWorkload.

### Displaying workload progress in the UI

To show the progress of the workload in the UI, add a constructor to your workload class and
initialize the following variables.

<pre>
        indeterminate = false;
        total = //some integer representing the total number of steps
</pre> 

Then, as your workload performs work, update the progress variable accordingly:

<pre>
        progress++;
</pre>

### Making sure its possible to stop the workload

To ensure your workload can be stopped when requested by the user, your workload should
periodically check the status of task.isCancelled().  If this returns true, your performWork()
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
