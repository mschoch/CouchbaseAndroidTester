package com.couchbase.androidtester.widget;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import android.content.Context;
import android.content.res.AssetManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.couchbase.androidtester.R;
import com.couchbase.workloads.CouchbaseWorkload;
import com.couchbase.workloads.CouchbaseWorkloadRunner;

public class WorkloadsListAdapter extends ArrayAdapter<CouchbaseWorkload> implements OnClickListener, CouchbaseWorkloadRunner {

	private  Context context;

	public WorkloadsListAdapter(Context context, List<CouchbaseWorkload> workloads) {
		super(context, 0, workloads);
		this.context = context;

		//set self as the workload runner
		for (CouchbaseWorkload couchbaseWorkload : workloads) {
			couchbaseWorkload.setCouchbaseWorkloadRunner(this);
		}
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        if (v == null) {
            LayoutInflater vi = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            v = vi.inflate(R.layout.workload_row, null);
        }

        CouchbaseWorkload workload = getItem(position);
        if(workload != null) {
        	//label
        	TextView workloadTextView = (TextView)v.findViewById(R.id.workloadTextView);
        	workloadTextView.setText(workload.getName());

        	//start button
        	Button workloadStartButton = (Button)v.findViewById(R.id.workloadStartButton);
        	workloadStartButton.setOnClickListener(this);
        	workloadStartButton.setTag(position);
        	workloadStartButton.setEnabled(!workload.isRunning());

        	//stop button
        	Button workloadStopButton = (Button)v.findViewById(R.id.workloadStopButton);
        	workloadStopButton.setOnClickListener(this);
        	workloadStopButton.setTag(position);
        	workloadStopButton.setEnabled(workload.isRunning());


        	//progress bar
        	ProgressBar progressBar = (ProgressBar)v.findViewById(R.id.workloadProgressBar);
        	if(workload.isRunning()) {
        		if(!workload.isIndeterminate()) {
        			progressBar.setProgress(workload.getProgress());
        			progressBar.setMax(workload.getTotal());
        		}
        		else {
        			progressBar.setProgress(0);
        		}
        	}
        	else {
        		progressBar.setProgress(0);
        	}
        }

        return v;
	}

    //On Click Listener Interface Methods
    public void onClick(View v) {
    	if(v.getId() == R.id.workloadStartButton) {
	    	int position = (Integer)v.getTag();
	    	CouchbaseWorkload workload = getItem(position);
	    	workload.start();
	    	//trigger update of buttons
	    	notifyDataSetChanged();
    	}
    	else if(v.getId() == R.id.workloadStopButton){
	    	int position = (Integer)v.getTag();
	    	CouchbaseWorkload workload = getItem(position);
	    	workload.stop();
	    	//trigger update of buttons
	    	notifyDataSetChanged();
    	}
    }

    //Couchbase Workload Runner Interface Methods
    @Override
    public void workloadReportsFinish(CouchbaseWorkload workload, String finishMessage) {
    	notifyDataSetChanged();
    };

    @Override
    public void workloadReportsProgress(CouchbaseWorkload workload,
    		String progressMessage) {

    	notifyDataSetChanged();
    }

    @Override
    public InputStream openResource(String path) throws IOException {
        AssetManager assetManager = context.getAssets();
        return assetManager.open(path);
    }

}