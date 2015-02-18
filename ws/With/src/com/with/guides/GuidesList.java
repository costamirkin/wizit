package com.with.guides;

import java.util.List;

import android.app.Dialog;
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
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.with.R;
import com.with.tourbuild.CommonShared;
import com.with.tours.ToursList;

public class GuidesList extends ListActivity {

	GuidesListAdapter mGuidesListAdapter = null;
	private ProgressDialog mDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mGuidesListAdapter = new GuidesListAdapter(getApplicationContext());
        mDialog = new ProgressDialog(this);
        mDialog.setCancelable(true);
        mDialog.setMessage("Loading...");
        mDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mDialog.show();

		setListAdapter(mGuidesListAdapter);
		registerForContextMenu(getListView());
		loadGuides();

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
		getMenuInflater().inflate(R.menu.guides_list, menu);
		super.onCreateContextMenu(menu, v, menuInfo);
	}

	@Override
	public boolean onContextItemSelected(MenuItem item) {
		final AdapterContextMenuInfo info = (AdapterContextMenuInfo) item
				.getMenuInfo();

		switch (item.getItemId()) {
		case R.id.guideDetails:

			break;

		case R.id.guideTours:
			Intent intent = new Intent(getApplicationContext(), ToursList.class);
			intent.putExtra("GuideName", CommonShared.getInstance().getmGuides()
					.get(info.position).getmGuideName());
			startActivity(intent);

			break;

		case R.id.guideRate:
			final Dialog dialog = new Dialog(this);
			dialog.setContentView(R.layout.dialog_rate);
			dialog.setTitle("RATE TOUR");

			Button btnRate = (Button) dialog.findViewById(R.id.buttonRate);

			// Set On ClickListener
			btnRate.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					RatingBar ratingBar = (RatingBar) dialog.findViewById(R.id.ratingBar);
					ParseObject object = CommonShared.getInstance().getmGuides()
							.get(info.position).getmParseObject();
					object.increment("RatesNumber");
					object.increment("Rate", ratingBar.getRating());
					object.saveInBackground((new SaveCallback() {
						public void done(ParseException e) {
							Toast.makeText(getApplicationContext(), "Thank You For Rating", Toast.LENGTH_SHORT).show();
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

	public void UpdateGuides() {
		runOnUiThread(new Runnable() {
			public void run() {
				getListView().invalidateViews();
				mDialog.dismiss();
			}
		});
	}


	private void loadGuides() {
		ParseQuery<ParseObject> query = ParseQuery.getQuery("GUIDE");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if (e == null) {
					CommonShared.getInstance().ReadGuides(objects);
					UpdateGuides();
				}

			}

		});

	}

}
