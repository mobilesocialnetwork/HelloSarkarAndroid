package com.mobilenepal.hackathon1.HelloSarkar;

import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.mobilenepal.hackathon1.HelloSarkar.database.HelloSarkarDbAdapter;

public class HelloSarkarActivity extends Activity {
    /** Called when the activity is first created. */
	
	private TextView tvName, tvAdd, tvComplain, tvDistrict,todaysDate;
	private EditText etName, etAdd, etComplain;
	private Spinner spType, spDistrict;
		
	//DatePicker dpTime;
	Button bSubmit,postButton;
	private LocationManager locManager;
	private String bestProvider;
	private Location location;
	private HelloSarkarDbAdapter sarkarAdapter;
	private LocationListener locationListener;
	MyToast errortoast=new MyToast(0,this);
	MyToast successtoast=new MyToast(1,this);
	String mDate;

	int districtsList;
	
    @Override
    public void onCreate(Bundle bundle) {
        super.onCreate(bundle);
      
        setContentView(R.layout.complainform);        
        initialize();    

        locManager = (LocationManager) HelloSarkarActivity.this.getSystemService(Context.LOCATION_SERVICE);
       	Criteria criteria=new Criteria();
    	criteria.setAccuracy(2);
  //  	criteria.setSpeedAccuracy(2);
    	criteria.setPowerRequirement(2);
   // 	criteria.setHorizontalAccuracy(2);
    	bestProvider=locManager.getBestProvider(criteria, false);
    	locationListener=new MyLocationListener();    
    	location=locManager.getLastKnownLocation(bestProvider);
        if(location == null){        	
        	locManager.requestLocationUpdates(bestProvider, 0, 0, locationListener);        	
        }
    }
    
protected void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);	
	}


	private void initialize() {
		tvName = (TextView) findViewById(R.id.tvName);
		tvAdd = (TextView) findViewById(R.id.tvAdd);
		tvComplain = (TextView) findViewById(R.id.tvComplain);
		tvDistrict = (TextView) findViewById(R.id.tvDistrict);
			
		etName = (EditText) findViewById(R.id.etName);
		etAdd = (EditText) findViewById(R.id.etAdd);
		etComplain = (EditText) findViewById(R.id.etComplain);
		
		spType = (Spinner) findViewById(R.id.spType);
		spDistrict = (Spinner) findViewById(R.id.spDistrict);
		
		bSubmit =(Button) findViewById(R.id.postButton);

		todaysDate=(TextView)findViewById(R.id.currentdate);
		Date currentDate = new Date(System.currentTimeMillis());
		String today= (currentDate.getYear()+1900)+"-"+(currentDate.getMonth()+1)+"-"+Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
		todaysDate.setText(today);
		mDate=today;
		
		Button pickDate= (Button)findViewById(R.id.pkdate);
		pickDate.setOnClickListener(new OnClickListener() {
		    public void onClick(View v) {
		    	
				Intent pick = new Intent(HelloSarkarActivity.this, PickADate.class);
				HelloSarkarActivity.this.startActivityForResult(pick, 1);
		    }
		});
		
		//ArrayAdapter: list District
		ArrayAdapter<CharSequence> adapterDistrict = ArrayAdapter.createFromResource(
				  this, R.array.district_name, android.R.layout.simple_spinner_item );
		adapterDistrict.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);	
		spDistrict.setAdapter(adapterDistrict);
		
		//ArrayAdapter: list Complaint Type
				ArrayAdapter<CharSequence> adapterComplaint = ArrayAdapter.createFromResource(
						  this, R.array.complaint_type, android.R.layout.simple_spinner_item );
				adapterComplaint.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);	
				spType.setAdapter(adapterComplaint);			
		
	}			 


	public void postComplainSubmit(View view){
		//validation form fields;
		Complain complain=new Complain(this.getApplicationContext());
		boolean valid=true;
		
		if(TextUtils.isEmpty(etName.getText().toString())){
			valid=false;			
		}else{
			complain.setName(etName.getText().toString());
		}
		if(TextUtils.isEmpty(etAdd.getText())){
			valid=false;			
		}else{
			complain.setAddress(etAdd.getText().toString());
		}
		if(TextUtils.isEmpty(etComplain.getText().toString())){
			valid=false;
		}else{
			complain.setComplain(etComplain.getText().toString());
		}		
	
		
		if(valid){	
			location=locManager.getLastKnownLocation(bestProvider);
			if(location != null){
				complain.setGps(location.getLatitude()+","+location.getLongitude());
			}else{
				complain.setGps("unavailable");	
			}
			complain.setDistrict(spDistrict.getSelectedItem().toString());
			complain.setComplainType(spType.getSelectedItem().toString());			
			complain.setDate(mDate);		
		    complain.setPostParameters(complain,this.getApplicationContext());
			
		    if(MenuActivity.availableConnection=true){
           	try {
				String response = CustomHttpClient.executeHttpPost("http://apps.mobilenepal.net/hellosarkar/public/complain/receive", complain.getPostParameters());
				complain.setServerId(response);
				sarkarAdapter=new HelloSarkarDbAdapter(this);
				sarkarAdapter.open();
				long result=sarkarAdapter.createComplain(complain);
				sarkarAdapter.close();
			 	if(result != -1){
					successtoast.setMessage("complain successfully posted and saved");
				    successtoast.displayToast();
				}
				Intent i=new Intent(Alert.context,MenuActivity.class);
				Alert.context.startActivity(i);
				
				} catch (Exception e) {					
				  Alert alert=new Alert(this,"Complain couldnot be Post at the moment.Do you want to save your complain on local.","Error on posting",complain);
				  alert.displayDialog();		
			      }
		    }else{
		    	Alert alert=new Alert(this,"Do you want localy save your complain","No Internet Connection",complain);
				alert.displayDialog();
		    }
			
		}else{			
			errortoast.setMessage( "Fields marked with asterisk(*) are mandatory");
			errortoast.displayToast();
			
		}
		
	}

	/** Register for the updates when Activity is in foreground */
	@Override
	protected void onResume() {
		super.onResume();
		
	}

	/** Stop the updates when Activity is paused */
	@Override
	protected void onPause() {
		super.onPause();
		locManager.removeUpdates(locationListener);
	}
	
	//for Picker date function
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case 1:
            	try {
                    String value = data.getStringExtra("value");
                    if (value != null && value.length() > 0) {
                    	mDate= value;
                    	todaysDate.setText(mDate);
                    }
            	}catch (Exception e) {
                }
            	break;
        }
	}		
}