/**
 * 
 */
package com.mobilenepal.hackathon1.HelloSarkar;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MyToast {
	static Activity context;
	int condition;
	String message;
	Context application;

	/**
	 * @param message the message to set
	 */
	public void setMessage(String message) {
		MyToast.this.message = message;
	}

	/****
	 * for now we use toast message with image for two conditions only conditon
	 * value 1 for success theme & 0 for error theme message
	 *****/
	/**
	 * @param condition
	 * @param message
	 * @param context
	 */
	public MyToast(int condition, String message, Activity context) {
		MyToast.context = context;
		MyToast.this.condition = condition;
		MyToast.this.message = message;

	}

	/**
	 * @param condition
	 * @param context
	 */
	public MyToast(int condition, Activity context) {
		MyToast.context = context;
		MyToast.this.condition = condition;

	}

	public MyToast(Context applicationContext) {
		application = applicationContext;
	}

	public void displayToast() {
		LayoutInflater inflater = context.getLayoutInflater();
		View layout = inflater.inflate(R.layout.toast,(ViewGroup) context.findViewById(R.id.toast_layout));

		ImageView image = (ImageView) layout.findViewById(R.id.toast_image);
		TextView text = (TextView) layout.findViewById(R.id.toast_text);
		text.setText(message);
		if (condition == 1) {
			image.setImageResource(R.drawable.success);

		} else if (condition == 0) {
			image.setImageResource(R.drawable.error);

		}
		Toast toast = new Toast((Context) context.getApplicationContext());
		toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
		toast.setDuration(Toast.LENGTH_SHORT);
		toast.setView(layout);
		toast.show();

	}

}
