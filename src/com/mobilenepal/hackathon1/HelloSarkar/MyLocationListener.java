package com.mobilenepal.hackathon1.HelloSarkar;

import android.location.Location;
import android.location.LocationListener;
import android.os.Bundle;
import android.util.Log;

public class MyLocationListener implements LocationListener {
   
    public void onLocationChanged(Location loc) {		
			Log.i("location changed", " latitude:"+loc.getLatitude()+" longitude:"+loc.getLongitude());
	
	}

	public void onProviderDisabled(String provider) {
		Log.i("Provider disabled", " Provider disabled");
		
	}

	public void onProviderEnabled(String provider) {
		Log.i("Provider enabled", "Provider enabled ");
			
	}

	public void onStatusChanged(String provider, int status, Bundle extras) {
		Log.i("status changed", " status:"+status);
				
	}

}
