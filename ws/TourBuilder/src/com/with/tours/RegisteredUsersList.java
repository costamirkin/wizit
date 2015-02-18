package com.with.tours;

import java.util.List;
import java.util.Vector;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;

public class RegisteredUsersList extends ListActivity {
	private Vector<String> mRegisteredUsers;
	private String         mTourId = null;
	private ProgressDialog mDialog;

	RegisteredUsersAdapter mRegisteredUsersAdapter = null;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mRegisteredUsers = new Vector<String>();
		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			mTourId = bundle.getString("tourId");
		}
		
		mRegisteredUsersAdapter = 
				new RegisteredUsersAdapter(getApplicationContext(), mRegisteredUsers);
		setListAdapter(mRegisteredUsersAdapter);
		registerForContextMenu(getListView());
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();

		loadmRegisteredUsers();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				openContextMenu(arg1);			
			}
		});
	}

	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
//		getMenuInflater().inflate(R.menu.tours_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId()) {
		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	public void UpdateUsers() {
		runOnUiThread(new Runnable() {
			public void run() {
				mDialog.dismiss();
				getListView().invalidateViews();
			}
		});
	}

	

	private void loadmRegisteredUsers() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("REGISTRATION");
		mRegisteredUsers.clear();
		if (mTourId == null) {
			return;
		}
		query.whereEqualTo("tourId", mTourId);
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					for(int i = 0; i<objects.size();i++) {
						mRegisteredUsers.add(objects.get(i).getString("userName"));
					}
					UpdateUsers();
				}

			}

		});

	}


}
