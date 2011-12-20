package com.mobilenepal.hackathon1.HelloSarkar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

public class MenuActivity extends Activity{
	
	static boolean availableConnection=false;
	MyToast errortoast=new MyToast(0,this);
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.menu);
		  if(HaveNetworkConnection()){
	        	availableConnection=true;
				Log.d("INTERNET_CONNECITON", "CONNNECTION SUCCESSFUL");
		        
	        } else {	        	
	        	errortoast.setMessage("Please enable wifi");
	        	errortoast.displayToast();
	        	Log.d("INTERNET_CONNECITON", "CONNNECTION Failed");
	        }
	}
	
	public void  makeComplainClick(View view) {
		Intent makeComplain = new Intent(MenuActivity.this, HelloSarkarActivity.class);
		startActivity(makeComplain);
	}
	
	public void  listComplainClick(View view) {
		Intent i=new Intent(this,ComplainList.class);
		startActivity(i);  

}
	
	private boolean HaveNetworkConnection(){
		boolean HaveConnectedWifi = false;
		boolean HaveConnectedMobile = false;

		ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
		NetworkInfo[] netInfo = cm.getAllNetworkInfo();
		for (NetworkInfo ni : netInfo)
		{
			if (ni.getTypeName().equalsIgnoreCase("WIFI"))
				if (ni.isConnected())
					HaveConnectedWifi = true;
			if (ni.getTypeName().equalsIgnoreCase("MOBILE"))
				if (ni.isConnected())
					HaveConnectedMobile = true;
		}
		return HaveConnectedWifi || HaveConnectedMobile;
	}
}
