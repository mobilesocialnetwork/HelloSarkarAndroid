package com.mobilenepal.hackathon1.HelloSarkar;

import java.util.ArrayList;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.mobilenepal.hackathon1.HelloSarkar.database.HelloSarkarDbAdapter;

public class ComplainList extends Activity{
	HelloSarkarDbAdapter sarkarAdapter;
	
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		sarkarAdapter=new HelloSarkarDbAdapter(this);
		sarkarAdapter.open();
		Cursor cursor=sarkarAdapter.fetchAllComplains();		
		
		ArrayList<Complain> results = new ArrayList<Complain>();		
		if (cursor != null ) {
    		if  (cursor.moveToFirst()) {
    			do {
    				Complain complain=new Complain(cursor,this.getApplicationContext());  
                    results.add(complain);
    			}while (cursor.moveToNext());
    		} 
    	}
		sarkarAdapter.close();
		
		//
		setContentView(R.layout.list);
		ListView list = (ListView) findViewById(R.id.list);
		list.setAdapter(new ComplainListAdapter(results, ComplainList.this));
	    list.setOnItemClickListener(new OnItemClickListener(){

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
	    	 if(MenuActivity.availableConnection=true){
	    		 Alert alert =new Alert(this, "Do you want to post your complain!", "Localy Saved Complain", complain);
	        	 alert.displayDialog();  
	    	 }else{
	    		 MyToast errortoast=new MyToast(0,this);
	    		 errortoast.setMessage("Please enable wifi");
		         errortoast.displayToast();
	    	 }    	 
	      }	  
	}
	
	@Override
	protected void onPause() {
		super.onPause();
	    this.finish();
	}

}
