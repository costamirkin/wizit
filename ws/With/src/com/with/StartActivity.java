package com.with;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParsePush;
import com.parse.PushService;
import com.with.chat.ChatWindow;
import com.with.guides.GuidesList;
import com.with.sales.MapActivity;
import com.with.tourbuild.CommonShared;
import com.with.tours.ToursList;

public class StartActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_start);

		// Subscribe to get chat push notifications
		ParsePush.subscribeInBackground(SharedObjects.getInstance().getmParseUser().getUsername());

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_start, menu);
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.myInfo) {
			Intent intent = new Intent(getApplicationContext(), InfoActivity.class);
			startActivity(intent);
		}
		return super.onOptionsItemSelected(item);
	}
	
	public void gotoSale(View v) {
		Intent intent = new Intent(getApplicationContext(), MapActivity.class);
		startActivity(intent);

	}

	public void gotoFriends(View v) {
		Intent intent = new Intent(getApplicationContext(), FriendsActivity.class);
		startActivity(intent);
		
	}

	public void gotoTours(View v) {
		Intent intent = new Intent(getApplicationContext(), ToursList.class);
		startActivity(intent);
		
	}
	
	public void gotoGuides(View v) {
		Intent intent = new Intent(getApplicationContext(), GuidesList.class);
		startActivity(intent);
		
	}

}
