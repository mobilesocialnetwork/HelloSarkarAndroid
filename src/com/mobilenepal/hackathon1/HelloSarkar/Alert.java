/**
 * 
 */
package com.mobilenepal.hackathon1.HelloSarkar;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;

import com.mobilenepal.hackathon1.HelloSarkar.database.HelloSarkarDbAdapter;

public class Alert {
	static Activity context;
	private String message;
	private String title;
	private Complain complain;
	HelloSarkarDbAdapter sarkarAdapter;

	/**
	 * @param Complain
	 */
	public Alert(Activity context, String message, String title,Complain complain) {
		Alert.context = context;
		Alert.this.message = message;
		Alert.this.title = title;
		Alert.this.complain = complain;

	}

	public void displayDialog() {
		final MyToast successToast=new MyToast(1,Alert.context);
		final MyToast errorToast=new MyToast(0,Alert.context);
		if (Alert.this.complain.getLocalId() > 0) {
			new AlertDialog.Builder(context)
					// .setIcon(R.drawable.reminder)
					.setTitle(title)
					.setMessage(message)
					// .setView(imageView)
					.setPositiveButton("Post Complain",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int POSITIVE_BUTTON) {
									 complain.setPostParameters(complain,Alert.context);
										if(MenuActivity.availableConnection){
							           	try {
											String response = CustomHttpClient.executeHttpPost("http://apps.mobilenepal.net/hellosarkar/public/complain/receive", complain.getPostParameters());
											complain.setServerId(response);
											
												sarkarAdapter = new HelloSarkarDbAdapter(Alert.context);
												sarkarAdapter.open();
												boolean updateResult  = sarkarAdapter.updateComplain(Alert.this.complain);
												sarkarAdapter.close();
												if(updateResult==true){
													successToast.setMessage("successfully posted & updated on local");
													successToast.displayToast();													
												}												
												Intent i = new Intent(Alert.context,MenuActivity.class);
  							    				Alert.context.startActivity(i);
											
							           	     }catch(Exception e){
							           	    	 errorToast.setMessage("Complain couldnot be saved at the moment.Please try later");
							           		     errorToast.displayToast();
											
							                 	}
										}else{
											errorToast.setMessage("Internet connection unavailable");
						           		    errorToast.displayToast();
										}
								   }
							})
					.setNegativeButton("Discard",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int NEGATIVE_BUTTON) {
									return;

									/* User clicked Cancel so do some stuff */
								}
							}).show();

		} else {
			new AlertDialog.Builder(context)
					// .setIcon(R.drawable.reminder)
					.setTitle(title)
					.setMessage(message)
					// .setView(imageView)
					.setPositiveButton("Save Local",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int POSITIVE_BUTTON) {

									sarkarAdapter = new HelloSarkarDbAdapter(Alert.context);
									sarkarAdapter.open();
									long result = sarkarAdapter.createComplain(Alert.this.complain);
									sarkarAdapter.close();
									if (result != -1) {
										successToast.setMessage("complain successfully saved");
										successToast.displayToast();
									} else {
										errorToast.setMessage("Error occured on saving");
										errorToast.displayToast();

									}
									Intent i = new Intent(Alert.context,
											MenuActivity.class);
									Alert.context.startActivity(i);

								}
							})
					.setNegativeButton("Discard",
							new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog,
										int NEGATIVE_BUTTON) {
									return;

									/* User clicked Cancel so do some stuff */
								}
							}).show();

		}

	}

}
