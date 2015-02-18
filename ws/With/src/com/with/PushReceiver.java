package com.with;

import java.util.Iterator;

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

import com.with.chat.ChatWindow;

public class PushReceiver extends BroadcastReceiver {
	private static String mStoreNameIntent;
	private static String mSaleTitleIntent;
	private static String mSaleContentIntent;
	private static String mStoreSiteIntent;
	private static String mStoreAdrress;
	private static String mLatitudeIntent;
	private static String mLongitudeIntent;

	@Override
	public void onReceive(Context context, Intent intent) {
		try {
			String action = intent.getAction();

			if (action.equals("com.with.CHAT")) {
				// Handle chat message
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));
				String message = json.getString("message");
				// Save message in order to show it in chat window
				String sender = json.getString("sender");
				SharedObjects.getInstance().getmSavedMessages().add(message);
				SharedObjects.getInstance().setmMessageSender(sender);
				if (ChatWindow.mThis != null) {
					ChatWindow.mThis.displayMessage(message);
				} else {
					Intent resultIntent = new Intent(context, ChatWindow.class);
					resultIntent.putExtra("Name", sender);
					PendingIntent pIntent = PendingIntent.getActivity(context, 0, resultIntent, 0);
					Notification noti = new NotificationCompat.Builder(
							context).setSmallIcon(R.drawable.ic_launcher)
							.setContentIntent(pIntent)
							.setContentTitle(sender + " sends you a message")
							.setContentText(message).build();

					NotificationManager mNotificationManager = (NotificationManager) context
							.getSystemService(Context.NOTIFICATION_SERVICE);
					noti.flags |= Notification.FLAG_AUTO_CANCEL;
					noti.flags |= Notification.DEFAULT_SOUND;
					mNotificationManager.notify(1, noti);

				}
			} else if (action.equals("com.example.UPDATE_STATUS")) {
				// Handle sale message
				String channel = intent.getExtras().getString(
						"com.parse.Channel");
				JSONObject json = new JSONObject(intent.getExtras().getString(
						"com.parse.Data"));

				Log.d("PushReciver", "got action " + action + " on channel "
						+ channel + " with:");
				Iterator itr = json.keys();

				mStoreNameIntent = json.getString("title");
				mSaleTitleIntent = json.getString("alert");
				mSaleContentIntent = json.getString("SaleContent");
				mStoreSiteIntent = json.getString("Site");
				mLatitudeIntent = json.getString("Latitude");
				mLongitudeIntent = json.getString("Longitude");
				mStoreAdrress = json.getString("Adrress");

				while (itr.hasNext()) {
					String key = (String) itr.next();
					Log.d("PushReciver",
							"..." + key + " => " + json.getString(key));
				}
			}
		} catch (JSONException e) {
			Log.d("PushReciver", "JSONException: " + e.getMessage());
		}
	}

	public static String getmStoreNameIntent() {
		return mStoreNameIntent;
	}

	public static String getmSaleTitleIntent() {
		return mSaleTitleIntent;
	}

	public static String getmSaleContentIntent() {
		return mSaleContentIntent;
	}

	public static String getmStoreSiteIntent() {
		return mStoreSiteIntent;
	}

	public static String getmLatitudeIntent() {
		return mLatitudeIntent;
	}

	public static String getmLongitudeIntent() {
		return mLongitudeIntent;
	}

	public static String getmStoreAdrress() {
		return mStoreAdrress;
	}

}