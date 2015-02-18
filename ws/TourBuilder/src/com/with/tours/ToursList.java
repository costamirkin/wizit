package com.with.tours;

import java.util.List;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.TourDetailsActivity;
import com.with.tourbuild.UpdateGuiListener;
import com.with.tourbuilder.R;

public class ToursList extends ListActivity  implements UpdateGuiListener {

	private TourListAdapter mTourListAdapter = null;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTourListAdapter = new TourListAdapter(getApplicationContext());
		setListAdapter(mTourListAdapter);
		registerForContextMenu(getListView());
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();
		loadTours();
		
		getListView().setOnItemClickListener(new OnItemClickListener() {

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
		getMenuInflater().inflate(R.menu.tours_list, menu);
		return true;
	}
	
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.tours_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}
	
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		
		switch (item.getItemId()) {
		case R.id.seeOnMap:
			Intent intent1 = new Intent(getApplicationContext(), ToursActivity.class);
			intent1.putExtra("tourId", info.position);
			startActivity(intent1);
			
			break;

		case R.id.tourDetails:
			Intent detailsIntent = new Intent(getApplicationContext(),
					TourDetailsActivity.class);
			detailsIntent.putExtra("tourId", info.position);
			startActivity(detailsIntent);
			
			break;

		case R.id.registeredUsers:
			Intent intent2 = new Intent(getApplicationContext(), RegisteredUsersList.class);
			intent2.putExtra("tourId", CommonShared.getInstance().getmTours().get(info.position).getmTourId());
			startActivity(intent2);
			break;

		default:
			break;
		}
		return super.onContextItemSelected(item);
	}

	@Override
	public void UpdateTours() {
		runOnUiThread(new Runnable() {
			public void run() {
				mDialog.dismiss();
				getListView().invalidateViews();
			}
		});
	}

	
	@Override
	protected void onPause() {
		CommonShared.getInstance().setmUpdateGuiListener(null);
		super.onPause();
	}

	@Override
	protected void onResume() {
		CommonShared.getInstance().setmUpdateGuiListener(this);
		//UpdateTours();
		super.onResume();
	}

	private void loadTours() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("POI");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					CommonShared.getInstance().ReadPois(objects);

					ParseQuery<ParseObject> query = ParseQuery.getQuery("TOUR");
					query.whereEqualTo("GuideName", CommonShared.getInstance().getmParseUser().getUsername());
					query.findInBackground(new FindCallback<ParseObject>() {

						@Override
						public void done(List<ParseObject> objects,
								ParseException e) {
							if (e == null) {
								CommonShared.getInstance().ReadTours(objects);
							}
						}

					});

				}

			}

		});

	}


}
