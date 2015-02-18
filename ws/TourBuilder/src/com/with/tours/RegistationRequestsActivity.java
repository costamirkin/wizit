package com.with.tours;

import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ContextMenu.ContextMenuInfo;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.parse.SaveCallback;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.Poi;
import com.with.tourbuild.UpdateGuiListener;
import com.with.tourbuilder.R;
import com.with.tourbuilder.RegistrationRequest;
import com.with.tourbuilder.SharedObjects;

public class RegistationRequestsActivity extends Activity implements
		UpdateGuiListener {

	private ProgressDialog mDialog;
	private ListView mListView;
	private RegistertrationRequestsAdapter mAdapter;

	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registarion_requests);
		
		// This activity can be the first application activity so the user can be null
		if (CommonShared.getInstance().getmParseUser() == null) {
		ParseUser currentUser = ParseUser.getCurrentUser();
			if (currentUser != null) {
				// If so - go to start activity
				CommonShared.getInstance().setmParseUser(currentUser);
			}
			else {
				Toast.makeText(getApplicationContext(), "PLEASE LOGIN TO APPLICATION", Toast.LENGTH_LONG).show();
				finish();
			}
		}
		
		mListView = (ListView) findViewById(R.id.registrationList);
		mAdapter = new RegistertrationRequestsAdapter(getApplicationContext());
		mListView.setAdapter(mAdapter);
		mDialog = new ProgressDialog(this);
		mDialog.setCancelable(true);
		mDialog.setMessage("Loading...");
		mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
		mDialog.show();
		loadTours();
		registerForContextMenu(mListView);

		mListView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				openContextMenu(arg1);			
			}
		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.registarion_requests, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.reg_requests_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId()) {
		case R.id.register:
			RegistrationRequest r = SharedObjects.getInstance().getmRegistrationRequests().get(info.position);

			final ParseObject object =  ParseObject.createWithoutData("REGISTRATION", r.getmObjectId());
			object.put("registered", true);
			object.saveInBackground(new SaveCallback() {
				
				@Override
				public void done(ParseException arg0) {
				}
			});
			r.setmRegistered(true);
			mListView.invalidateViews();
			break;
		}
		return super.onContextItemSelected(item);

	}


	private void loadTours() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("REGISTRATION");
		query.whereEqualTo("guideName", CommonShared.getInstance()
				.getmParseUser().getUsername());
		query.whereEqualTo("registered", false);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				SharedObjects.getInstance().getmRegistrationRequests().clear();
				if (e == null) {
					for(int i = 0; i<objects.size();i++) {
						RegistrationRequest r = new RegistrationRequest();
						r.setmUserName(objects.get(i).getString("userName"));
						r.setmTourId(objects.get(i).getString("tourid"));
						r.setmTourName(objects.get(i).getString("tourName"));
						r.setmObjectId(objects.get(i).getObjectId());
						r.setmGuideName(CommonShared.getInstance().getmParseUser().getUsername());
						r.setmRegistered(false);
						SharedObjects.getInstance().getmRegistrationRequests().add(r);
					}

				}
				UpdateTours();

			}

		});

	}

	@Override
	public void UpdateTours() {
		runOnUiThread(new Runnable() {
			public void run() {
				mDialog.dismiss();
				mListView.invalidateViews();
			}
		});
	}

}
