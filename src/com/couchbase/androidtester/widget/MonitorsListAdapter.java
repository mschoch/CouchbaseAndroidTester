package com.couchbase.androidtester.widget;

import java.util.List;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import com.couchbase.androidtester.monitors.CouchbaseMonitor;
import com.couchbase.androidtester.monitors.CouchbaseMonitorDisplay;

public class MonitorsListAdapter extends BaseExpandableListAdapter implements CouchbaseMonitorDisplay {

	protected Context context;
	protected List<CouchbaseMonitor> monitors;

	public MonitorsListAdapter(Context context, List<CouchbaseMonitor> monitors) {
		this.context = context;
		this.monitors = monitors;
		//set self as monitor display and start
		for (CouchbaseMonitor couchbaseMonitor : monitors) {
			couchbaseMonitor.setCouchbaseMonitorDisplay(this);
			couchbaseMonitor.start();
		}
	}

	public Object getChild(int groupPosition, int childPosition) {
    	CouchbaseMonitor monitor = monitors.get(groupPosition);
    	List<String> measures = monitor.currentMeasures();
    	return measures.get(childPosition);
    }

    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    public int getChildrenCount(int groupPosition) {
    	CouchbaseMonitor monitor = monitors.get(groupPosition);
    	List<String> measures = monitor.currentMeasures();
    	return measures.size();
    }

    public TextView getGenericView() {
        // Layout parameters for the ExpandableListView
        AbsListView.LayoutParams lp = new AbsListView.LayoutParams(
                ViewGroup.LayoutParams.FILL_PARENT, 64);

        TextView textView = new TextView(context);
        textView.setLayoutParams(lp);
        // Center the text vertically
        textView.setGravity(Gravity.CENTER_VERTICAL | Gravity.LEFT);
        // Set the text starting position
        textView.setPadding(64, 0, 0, 0);
        return textView;
    }

    public View getChildView(int groupPosition, int childPosition, boolean isLastChild,
            View convertView, ViewGroup parent) {

        TextView textView = getGenericView();
        textView.setText(getChild(groupPosition, childPosition).toString());
        return textView;

    }

    public Object getGroup(int groupPosition) {
        CouchbaseMonitor monitor = monitors.get(groupPosition);
        return monitor.getDisplayName();
    }

    public int getGroupCount() {
        return monitors.size();
    }

    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    public View getGroupView(int groupPosition, boolean isExpanded, View convertView,
            ViewGroup parent) {
        TextView textView = getGenericView();
        textView.setText(getGroup(groupPosition).toString());
        return textView;
    }

    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }

    public boolean hasStableIds() {
        return true;
    }

	//Couchbase Monitory Display Interface Method

    @Override
    public void valueChanged() {
    	notifyDataSetChanged();
    }

}