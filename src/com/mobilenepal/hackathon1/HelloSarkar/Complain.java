package com.mobilenepal.hackathon1.HelloSarkar;

import java.util.ArrayList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.xmlpull.v1.XmlPullParser;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.database.Cursor;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.mobilenepal.hackathon1.HelloSarkar.database.HelloSarkarDbAdapter;

public class Complain {
	//local and server id
	private long localId;
	private String serverId;
	
	//form fields
	private String name;
	private String district;
	private String complainType;
	private String address;
	private String Complain;
	private String date;
  
	//hidden fields
	private String phone;	
	private String gps;
	private String mobileInfo;
	 
	//data for post to server
	private ArrayList<NameValuePair> postParameters; 
	
	Context context;
	public Complain(Context givenContext){
		Complain.this.context=givenContext;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDistrict() {
		return district;
	}
	public void setDistrict(String district) {
		try {
			this.district = getMyXmlCode(1,district,1);
		} catch (Exception e) {
			this.district = "not found";
		}
	}
	public String getComplainType() {
		return complainType;
	}
	public void setComplainType(String complainType) {
		try {
			this.complainType = getMyXmlCode(2,complainType,1);
		} catch (Exception e) {
			this.complainType="not found";			
		}
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public String getComplain() {
		return Complain;
	}
	public void setComplain(String complain) {
		Complain = complain;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}
	
	//process name value pair for post to server
	public ArrayList<NameValuePair> getPostParameters() {
		return postParameters;
	}

	public String getPhone() {		
		return getMyPhoneNumber();
	}
	public String getGps() {
		return gps;
	}
	public void setGps(String gps) {
		this.gps = gps;
	}
	public long getLocalId() {
		return localId;
	}
	public void setLocalId(long localId) {
		this.localId = localId;
	}
	public String getServerId() {
		return serverId;
	}
	public void setServerId(String serverId) {
		this.serverId = serverId;
	}
	public String getMobileInfo() {
		return "Android";
	}
	public void setPostParameters(Complain complain,Context givenContext) {
		Complain.this.context=givenContext;
		ArrayList<NameValuePair> nameValue = new ArrayList<NameValuePair>();
		nameValue.add(new BasicNameValuePair("name", complain.getName()));
		nameValue.add(new BasicNameValuePair("complain_type", complain.getComplainType()));
		nameValue.add(new BasicNameValuePair("district_id", complain.getDistrict()));
		nameValue.add(new BasicNameValuePair("address", complain.getAddress()));
		nameValue.add(new BasicNameValuePair("complain_text", complain.getComplain()));
		nameValue.add(new BasicNameValuePair("date", complain.getDate()));
		nameValue.add(new BasicNameValuePair("mobile", complain.getMyPhoneNumber()));
		nameValue.add(new BasicNameValuePair("mobile_info",complain.getGps() ));
		this.postParameters = nameValue;
	}
	
	private String getMyPhoneNumber(){
	    TelephonyManager mTelephonyMgr;
	    String phone="9841497111";
	    mTelephonyMgr = (TelephonyManager) Complain.this.context.getSystemService(Context.TELEPHONY_SERVICE); 
	    if(mTelephonyMgr.getLine1Number()!=null){
	    	 phone= mTelephonyMgr.getLine1Number();
	    }
	    return phone;
	}
	
    /****
     * @param(cursor)
     * this function will ease in initialising Complain Object directly with cursor return from query
     * ****/
	
	public Complain(Cursor cursor,Context context){
		Complain.this.context=context;
		this.address=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_ADDRESS));
		this.district=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_DISTRICT));
		this.localId=cursor.getInt(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_LOCAL_ID));
		this.serverId=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_SERVER_ID));
		this.complainType=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_COMPLAIN_TYPE));
		this.date=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_DATE));
		this.Complain=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_COMPLAIN));
		this.name=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_NAME));
		this.phone=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_PHONE));
		this.gps=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_GPS));
		this.mobileInfo=cursor.getString(cursor.getColumnIndex(HelloSarkarDbAdapter.KEY_MOBILE_INFO));
		
	}
	
	/*****
	 * function to process xml for setting district code and complain code
	 * and also retriving  district name and compalin name from their code 
	 * (type == 1) district
	 * (type == 2) complain type
	 * (mycase == 1) get code from supplied @value(name) 
	 * (mycase == 2)get name from supplied @value(code)
	 * *****/
	
	public String getMyXmlCode(int type, String value,int mycase ) throws Exception {
		String code = "";
		XmlResourceParser xmldoc;
		if (type == 1) {
			xmldoc = Complain.this.context.getResources().getXml(R.xml.districts);
		} else if (type == 2) {
			xmldoc = Complain.this.context.getResources().getXml(R.xml.complaintype);
		} else {
			throw new Exception();
		}
		xmldoc.next();
		int eventType = xmldoc.getEventType();
		String NodeValue;
		while (eventType != XmlPullParser.END_DOCUMENT
				&& code.equalsIgnoreCase("")) {
			if (eventType == XmlPullParser.START_DOCUMENT) {
				Log.i("parsing xml document", "start parsing doc");
			} else if (eventType == XmlPullParser.START_TAG) {
				NodeValue = xmldoc.getName();// Start of a Node
				if (NodeValue.equalsIgnoreCase("item")) {
					if(mycase==1){
						if (xmldoc.getAttributeValue(1).equalsIgnoreCase(value)) {
							code = xmldoc.getAttributeValue(0);
						}
					}else if(mycase==2){
						if (xmldoc.getAttributeValue(0).equalsIgnoreCase(value)) {
							code = xmldoc.getAttributeValue(1);
						}					
					}else{
						throw new Exception();
					}
              	}
				// etc for each node name
			} else if (eventType == XmlPullParser.END_TAG) {
				// End of document
			} else if (eventType == XmlPullParser.TEXT) {
				// Any XML text
			}

			eventType = xmldoc.next(); // Get next event from xml parser
		}
		return code;
	}
	
}
