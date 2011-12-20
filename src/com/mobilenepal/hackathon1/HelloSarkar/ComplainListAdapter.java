package com.mobilenepal.hackathon1.HelloSarkar;

import java.util.ArrayList;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class ComplainListAdapter extends BaseAdapter {
	/**
	 * @author gyanu
	 * 
	 */
	static class ViewHolder {
		TextView status;
		TextView type;
		TextView complain;
		TextView date;		
	}

	public static final int ACTIVITY_CREATE = 6;
	private ArrayList<Complain> complainList;
	static Activity context;

	public ComplainListAdapter(ArrayList<Complain> complainList, Activity context) {
		ComplainListAdapter.this.complainList = complainList;
		ComplainListAdapter.context = context;
	}

	public int getCount() {
		return complainList.size();
	}

	public Object getItem(int position) {
		return complainList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	// create a new ImageView for each item referenced by the Adapter
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		if (convertView == null) { // if it's not recycled, initialize some
									// attributes
			LayoutInflater li = ComplainListAdapter.context.getLayoutInflater();
			convertView = li.inflate(R.layout.complain_item, parent, false);
			holder = new ViewHolder();
			holder.status = (TextView) convertView.findViewById(R.id.statusValue);
			holder.date = (TextView) convertView.findViewById(R.id.dateValue);
			holder.type = (TextView) convertView.findViewById(R.id.typeValue);
			holder.complain = (TextView) convertView.findViewById(R.id.complainValue);
			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
	    Complain complaint=complainList.get(position);
	    if(complaint.getServerId() != null){
	    	holder.status.setText("Posted");
	    }else{
	    	holder.status.setText("Not Posted");
	    }
	    String complainType="";
	    try {
			complainType=complaint.getMyXmlCode(2, complaint.getComplainType(),2);
		} catch (Exception e) {
			Log.d("error parsing complain type:", complaint.getComplainType());
		}
	    holder.type.setText(complainType);
		if(complaint.getComplain().length()>100){
			holder.complain.setText(complaint.getComplain().substring(0, 100));
		}else{
			holder.complain.setText(complaint.getComplain());
		}
	    
	    holder.date.setText(complaint.getDate());
		return convertView;
	}
}
