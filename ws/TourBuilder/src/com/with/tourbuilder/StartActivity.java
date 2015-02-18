package com.with.tourbuilder;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.parse.ParsePush;
import com.with.tourbuild.CommonShared;
import com.with.tours.RegisteredUsersList;
import com.with.tours.ToursList;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// Subscribe to get chat push notifications
		String str = CommonShared.getInstance().getmParseUser().getUsername();
		System.out.println(str);
		ParsePush.subscribeInBackground(CommonShared.getInstance().getmParseUser().getUsername());
		

	}

	public void gotoBuild(View v) {
		Intent intent = new Intent(getApplicationContext(), MainActivity.class);
		startActivity(intent);

	}

	public void gotoRegisteredUsers(View v) {
		Intent intent = new Intent(getApplicationContext(), RegisteredUsersList.class);
		startActivity(intent);
		
	}

	public void gotoTours(View v) {
		Intent intent = new Intent(getApplicationContext(), ToursList.class);
		startActivity(intent);
		
	}
}
