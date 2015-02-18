package com.with.tourbuilder;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.parse.ParseObject;
import com.with.tours.RegistationRequestsActivity;

public class PushReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();

			if (action.equals("com.with.REGISTER")) {
				// Handle chat message
//				JSONObject json = new JSONObject(intent.getExtras().getString(
//						"com.parse.Data"));
				final String userName = "costa1";//json.getString("userName");
				
				// prepare intent which is triggered if the
				// notification is selected

				Intent i = new Intent(context, RegistationRequestsActivity.class);
				PendingIntent pIntent = PendingIntent.getActivity(context, 0, i, 0);

				// build notification
				// the addAction re-use the same intent to keep the example short
				Notification n  = new NotificationCompat.Builder(context)
				        .setContentTitle("Registration from " + userName)
				        .setContentText("Subject")
				        .setContentIntent(pIntent)
				        .setSmallIcon(R.drawable.contact_new).build();
				n.flags |= Notification.FLAG_AUTO_CANCEL;
				    
				  
				NotificationManager notificationManager = 
				  (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
				notificationManager.notify(0, n); 
						
			}
		} catch (Exception e) {
			Log.d("PushReciver", "JSONException: " + e.getMessage());
		}
	}

}