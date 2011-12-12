package com.mobilenepal.hackathon1.HelloSarkar;

import java.util.ArrayList;

import android.app.ListActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.mobilenepal.hackathon1.HelloSarkar.database.HelloSarkarDbAdapter;

public class ComplainList extends ListActivity{
	HelloSarkarDbAdapter sarkarAdapter;
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		Bundle b=getIntent().getExtras();
		ArrayList<String> results=b.getStringArrayList("complainList");		
		this.setListAdapter(new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1,results));
	    ListView complainList=getListView();
	    complainList.setOnItemClickListener(new OnItemClickListener(){

			public void onItemClick(AdapterView<?> arg0, View view, int position,long id) {
				getComplain(++position);				
				
			};
	    	
	    });
	}
	
	public void getComplain(int id){
		sarkarAdapter=new HelloSarkarDbAdapter(this);
		sarkarAdapter.open();
		Cursor cursor=sarkarAdapter.fetchComplain(id);
		Complain complain = null;
		if (cursor != null ) {
			complain=new Complain(cursor,this.getApplicationContext());
    		} 
		sarkarAdapter.close();
     if(complain.getServerId()==null){
    	 Alert alert =new Alert(this, "Do you want to post your complain!", "Localy Saved Complain", complain);
    	 alert.displayDialog(); 
     }	  
	}

}
