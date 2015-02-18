package com.with.tours;

import java.util.List;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Dialog;
import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.with.R;
import com.with.SharedObjects;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.UpdateGuiListener;
import com.with.tourbuild.TourDetailsActivity;

public class ToursList extends ListActivity implements UpdateGuiListener {

	private TourListAdapter mTourListAdapter = null;
	private String mGuideName = null;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mTourListAdapter = new TourListAdapter(getApplicationContext());
		setListAdapter(mTourListAdapter);
		registerForContextMenu(getListView());
		Intent intent = getIntent();
		mGuideName = intent.getStringExtra("GuideName");
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
	public void onCreateContextMenu(ContextMenu menu, View v,
			ContextMenuInfo menuInfo) {
		getMenuInflater().inflate(R.menu.tours_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	private void registerUser(int tourIndex) {
		JSONObject data;
		try {

			final ParseObject object = new ParseObject("REGISTRATION");

			object.put("tourId",
					CommonShared.getInstance().getmTours().get(tourIndex)
							.getmTourId());
			object.put("userName", SharedObjects.getInstance().getmParseUser().getUsername());
			object.put("guideName", CommonShared.getInstance().getmTours().get(tourIndex).getmGuideName());
			object.put("tourName", CommonShared.getInstance().getmTours().get(tourIndex).getmTourName());
			object.put("registered", false);

			object.saveInBackground((new SaveCallback() {
				public void done(ParseException e) {
					Toast.makeText(getApplicationContext(),
							"Registration request sent",
							Toast.LENGTH_LONG).show();
				}
			}));

			data = new JSONObject("{\"action\": \"com.with.REGISTER\","
					+ "\"tourId\": \""
					+ CommonShared.getInstance().getmTours().get(tourIndex)
							.getmTourId() + "\",\"userName\": \""
					+ SharedObjects.getInstance().getmParseUser().getUsername()
					+ "\"}");
			ParsePush push1 = new ParsePush();
			push1.setChannel(CommonShared.getInstance().getmTours().get(tourIndex).getmGuideName());
			push1.setData(data);
			push1.sendInBackground();
		} catch (JSONException e) {
			System.out.println(e);
		}

	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.seeOnMap:
			Intent intent = new Intent(getApplicationContext(),
					ToursActivity.class);
			intent.putExtra("tourId", info.position);
			startActivity(intent);

			break;

		case R.id.tourDetails:
			Intent detailsIntent = new Intent(getApplicationContext(),
					TourDetailsActivity.class);
			detailsIntent.putExtra("tourId", info.position);
			startActivity(detailsIntent);

			break;

		case R.id.register:
			registerUser(info.position);
			break;

		case R.id.tourRate:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_rate);
			dialog.setTitle("RATE TOUR");

			Button btnRate = (Button) dialog.findViewById(R.id.buttonRate);

			// Set On ClickListener
			btnRate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					RatingBar ratingBar = (RatingBar) dialog
							.findViewById(R.id.ratingBar);
					ParseObject object = CommonShared.getInstance().getmTours()
							.get(info.position).getmParseObject();
					object.increment("RatesNumber");
					object.increment("Rate", ratingBar.getRating());
					object.saveInBackground((new SaveCallback() {
						public void done(ParseException e) {
							Toast.makeText(getApplicationContext(),
									"Thank You For Rating", Toast.LENGTH_SHORT)
									.show();
							loadTours();
							UpdateTours();
						}
					}));
					dialog.dismiss();

				}
			});
			Button btnCancel = (Button) dialog.findViewById(R.id.buttonCancel);

			// Set On ClickListener
			btnCancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					dialog.dismiss();
				}
			});
			dialog.show();

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
		// UpdateTours();
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
					if (mGuideName != null) {
						query.whereEqualTo("GuideName", mGuideName);
					}
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
