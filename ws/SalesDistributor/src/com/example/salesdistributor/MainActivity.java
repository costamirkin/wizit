package com.example.salesdistributor;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.StringTokenizer;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;

public class MainActivity extends Activity implements OnItemClickListener {
	private TextView storeName;
	private TextView saleTitle;
	private TextView saleContent;
	private TextView storeSite;
	private Button PushButton;
	private EditText locationEditText;
	private String mStoreName, mSaleTitle, mSaleContent, mStoreSite, mAdrress;
	private double mLatitude, mLongitude;
	private boolean mGpsFlag;
	private boolean mPropertiesFlag;
	private ParsePush push;
	private ParseGeoPoint gp;
	private ProgressDialog progressGps;
	private String mStrAdrress;
	private AutoCompleteTextView mACTVAddress;

	private static final String LOG_TAG = "ExampleApp";

	private static final String PLACES_API_BASE = "https://maps.googleapis.com/maps/api/place";
	private static final String TYPE_AUTOCOMPLETE = "/autocomplete";
	private static final String OUT_JSON = "/json";

	private static final String API_KEY = "AIzaSyDBv3I2rUBW3Q1XG_u4WVADat9Qf5JLz3Q";

	private ArrayList<String> autocomplete(String input) {
		ArrayList<String> resultList = null;

		HttpURLConnection conn = null;
		StringBuilder jsonResults = new StringBuilder();
		try {
			StringBuilder sb = new StringBuilder(PLACES_API_BASE
					+ TYPE_AUTOCOMPLETE + OUT_JSON);
			sb.append("?sensor=false&key=" + API_KEY);
			sb.append("&location=32.078138,34.767737");
			sb.append("&radius=1000");
			sb.append("&input=" + URLEncoder.encode(input, "utf8"));

			URL url = new URL(sb.toString());
			conn = (HttpURLConnection) url.openConnection();
			InputStreamReader in = new InputStreamReader(conn.getInputStream());

			// Load the results into a StringBuilder
			int read;
			char[] buff = new char[1024];
			while ((read = in.read(buff)) != -1) {
				jsonResults.append(buff, 0, read);
			}
		} catch (MalformedURLException e) {
			Log.e(LOG_TAG, "Error processing Places API URL", e);
			return resultList;
		} catch (IOException e) {
			Log.e(LOG_TAG, "Error connecting to Places API", e);
			return resultList;
		} finally {
			if (conn != null) {
				conn.disconnect();
			}
		}

		try {
			// Create a JSON object hierarchy from the results
			JSONObject jsonObj = new JSONObject(jsonResults.toString());
			JSONArray predsJsonArray = jsonObj.getJSONArray("predictions");

			// Extract the Place descriptions from the results
			resultList = new ArrayList<String>(predsJsonArray.length());
			for (int i = 0; i < predsJsonArray.length(); i++) {
				resultList.add(predsJsonArray.getJSONObject(i).getString(
						"description"));
			}
		} catch (JSONException e) {
			Log.e(LOG_TAG, "Cannot process JSON results", e);
		}

		return resultList;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		storeName = (TextView) findViewById(R.id.textViewStoreName);
		saleTitle = (TextView) findViewById(R.id.textViewSaleTitle);
		saleContent = (TextView) findViewById(R.id.textViewSaleContent);
		storeSite = (TextView) findViewById(R.id.textViewSite);
		locationEditText = (EditText) findViewById(R.id.editTextGeoPoint);
		PushButton = (Button) findViewById(R.id.buttonPush);
		PushButton.setEnabled(false);

		mStoreName = "";
		mSaleTitle = "";
		mSaleContent = "";
		mStoreSite = "";

		mGpsFlag = false;
		mPropertiesFlag = false;

		mACTVAddress = (AutoCompleteTextView) findViewById(R.id.editTextAddress);
		mACTVAddress.setAdapter(new PlacesAutoCompleteAdapter(this,
				R.layout.list_item));
		mACTVAddress.setOnItemClickListener(this);

	}

	public void onItemClick(AdapterView<?> adapterView, View view,
			int position, long id) {
		mAdrress = (String) adapterView.getItemAtPosition(position);
		Geocoder geoCoder = new Geocoder(getApplicationContext(),
				Locale.getDefault());
		try {
			List<Address> address = geoCoder.getFromLocationName(mAdrress, 1);
			mLatitude  = address.get(0).getLatitude();
			mLongitude = address.get(0).getLongitude();

			locationEditText.setText("" + mLatitude + ", " + mLongitude);
			mGpsFlag = true;
			if (mPropertiesFlag) {
				PushButton.setEnabled(true);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void  getLocationFromString(View v)
			throws JSONException {

	}

	public void Properties(View v) {
		Intent propertiesIntent = new Intent(getApplicationContext(),
				PropertiesActivity.class);
		startActivityForResult(propertiesIntent, 1);
	}

	public void PushSale(View v) {
		new PushToParse().execute();
	}

	public void saveOnly(View v) {
		ParseObject sale = new ParseObject("Sales");
		sale.put("StoreTitle", mStoreName);
		sale.put("StoreSite", mStoreSite);
		sale.put("saleTitle", mSaleTitle);
		sale.put("SaleContent", mSaleContent);
		sale.put("address", mAdrress);

		try {
			int count = 0;
			int x;

			String GeoPoint = ((EditText) findViewById(R.id.editTextGeoPoint))
					.getText().toString();
			StringTokenizer strTokenizer = new StringTokenizer(GeoPoint, ", ");
			while (strTokenizer.hasMoreElements()) {
				if (count == 0)
					mLatitude = Double.parseDouble(strTokenizer.nextToken());
				if (count == 1)
					mLongitude = Double.parseDouble(strTokenizer.nextToken());
				if (count > 1)
					x = 1 / 0;
				count++;
			}
			gp = new ParseGeoPoint(mLatitude, mLongitude);
			// ParseGeoPoint gp = new ParseGeoPoint(31.769478, 35.293958);
			sale.put("position", gp);
		} catch (Exception e) {
			e.printStackTrace();
		}

		sale.saveInBackground();

		Log.e("PUSH", "SENT TO DATA BROWSER");
	}

	public void getLocation(View v) {
		progressGps = new ProgressDialog(this);
		progressGps.setTitle("Loading");
		progressGps.setMessage("Getting location and adrress");
		progressGps.show();

		new LocationFinder().execute();
	}

	public String ConvertPointToLocation() {
		String address = "";
		Geocoder geoCoder = new Geocoder(this, Locale.getDefault());
		try {
			List<Address> addresses = geoCoder.getFromLocation(mLatitude,
					mLongitude, 3);

			if (addresses.size() != 0) {
				for (int index = 0; index < addresses.get(0)
						.getMaxAddressLineIndex(); index++) {
					address += addresses.get(0).getAddressLine(index) + " ";
				}
			} else {
				address = "No avilable adrress";
			}
		} catch (IOException e) {
			address = "Error";
			Log.e("Adrress", e.getMessage());
		}

		return address;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (resultCode == RESULT_CANCELED) {
			Toast.makeText(getApplicationContext(), "Canceled",
					Toast.LENGTH_SHORT).show();
		}

		else {
			mStoreName = data.getStringExtra("StoreName");
			mSaleTitle = data.getStringExtra("SaleTitle");
			mSaleContent = data.getStringExtra("SaleContent");
			mStoreSite = data.getStringExtra("Site");

			storeName.setText(mStoreName);
			saleTitle.setText(mSaleTitle);
			saleContent.setText(mSaleContent);
			storeSite.setText(mStoreSite);

			if (((mStoreName.trim().equals(""))
					|| (mSaleTitle.trim().equals("")) || (mSaleContent.trim()
					.equals("")))) {
				mPropertiesFlag = false;
				Toast.makeText(getApplicationContext(),
						"Please fill all the fields", Toast.LENGTH_SHORT)
						.show();
			} else {
				mPropertiesFlag = true;
			}
		}

		if ((mPropertiesFlag == true) && (mGpsFlag == true)) {
			PushButton.setEnabled(true);
		} else {
			PushButton.setEnabled(false);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	// =================================================================================
	// LocationFinder LocationFinder LocationFinder LocationFinder
	// =================================================================================

	private class LocationFinder extends AsyncTask<Void, Void, Boolean>
			implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			mLatitude = location.getLatitude();
			mLongitude = location.getLongitude();
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub

		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub

		}

		@Override
		protected Boolean doInBackground(Void... params) {
			try {
				LocationManager lc = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// lc.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100,
				// 0, this);
				// Thread.sleep(1000);
				Location location = lc
						.getLastKnownLocation(LocationManager.GPS_PROVIDER);

				if (location != null) {
					mLatitude = location.getLatitude();
					mLongitude = location.getLongitude();

					// mLatitude = 31.788202;
					// mLongitude = 35.206416;
					gp = new ParseGeoPoint(mLatitude, mLongitude);
					mStrAdrress = ConvertPointToLocation();
					if (mStrAdrress.equals("No avilable adrress")
							|| mStrAdrress.equals("Error")) {
						mAdrress = "";
					}

					else if (mStrAdrress.equals(null)) {
						mAdrress = "  " + mStrAdrress;
					} else {
						mAdrress = "";
					}

					return true;
				} else {
					return false;
				}
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result == false) {
				Toast.makeText(getApplicationContext(), "No GPS active!",
						Toast.LENGTH_LONG).show();
				mGpsFlag = false;
				progressGps.dismiss();
			} else {
				locationEditText.setText("" + mLatitude + ", " + mLongitude);
				mGpsFlag = true;

				progressGps.dismiss();
			}
		}
	}

	// =================================================================================
	// PushToParse PushToParse PushToParse PushToParse PushToParse
	// =================================================================================

	private class PushToParse extends AsyncTask<Void, Void, Boolean> {
		private ProgressDialog progress;
		private JSONObject data;
		private ParseGeoPoint gp;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			progress = new ProgressDialog(MainActivity.this);
			progress.setTitle("Loading");
			progress.setMessage("Uploading to parse");
			progress.show();
		}

		@Override
		protected Boolean doInBackground(Void... params) {
			ParseObject gameScore = new ParseObject("Sales");
			gameScore.put("StoreTitle", mStoreName);
			gameScore.put("StoreSite", mStoreSite);
			gameScore.put("saleTitle", mSaleTitle);
			gameScore.put("SaleContent", mSaleContent);

			try {
				int count = 0;
				int x;

				String GeoPoint = ((EditText) findViewById(R.id.editTextGeoPoint))
						.getText().toString();
				StringTokenizer strTokenizer = new StringTokenizer(GeoPoint,
						", ");
				while (strTokenizer.hasMoreElements()) {
					if (count == 0)
						mLatitude = Double
								.parseDouble(strTokenizer.nextToken());
					if (count == 1)
						mLongitude = Double.parseDouble(strTokenizer
								.nextToken());
					if (count > 1)
						x = 1 / 0;
					count++;
				}
				gp = new ParseGeoPoint(mLatitude, mLongitude);
				// ParseGeoPoint gp = new ParseGeoPoint(31.769478, 35.293958);
				gameScore.put("position", gp);
			} catch (Exception e) {
				e.printStackTrace();
				return false;
			}

			gameScore.saveInBackground();

			Log.e("PUSH", "SENT TO DATA BROWSER");

			// Push
			try {
				data = new JSONObject(
						"{\"action\": \"com.example.UPDATE_STATUS\","
								+ "\"title\": \"" + mStoreName + "\","
								+ "\"alert\": \"" + mSaleTitle + "\","
								+ "\"Site\": \"" + mStoreSite + "\","
								+ "\"Latitude\": \"" + mLatitude + "\","
								+ "\"Longitude\": \"" + mLongitude + "\","
								+ "\"Adrress\": \"" + mAdrress + "\","
								+ "\"SaleContent\": \"" + mSaleContent + "\"}");

				ParsePush push1 = new ParsePush();
				push1.setChannel("Castro");
				push1.setData(data);
				push1.sendInBackground();
				Log.e("PUSH", "SENT TO PUSH");

			} catch (JSONException e) {
				e.printStackTrace();
				Log.e("PUSH", "SEND ERROR");
			}

			return true;
		}

		@Override
		protected void onPostExecute(Boolean result) {
			super.onPostExecute(result);
			if (result == false) {
				Toast.makeText(getApplicationContext(), "No GPS active!",
						Toast.LENGTH_LONG).show();
				mGpsFlag = false;
				progressGps.dismiss();
			} else {
				locationEditText.setText("" + mLatitude + ", " + mLongitude);
				mGpsFlag = true;

				if (((mStoreName.trim().equals(""))
						|| (mSaleTitle.trim().equals("")) || (mSaleContent
							.trim().equals("")))) {
					mPropertiesFlag = false;
					Toast.makeText(getApplicationContext(),
							"Please fill all the fields", Toast.LENGTH_SHORT)
							.show();
				} else {
					mPropertiesFlag = true;
				}

				if ((mPropertiesFlag == true) && (mGpsFlag == true)) {
					PushButton.setEnabled(true);
				}

				progressGps.dismiss();
			}
		}
	}

	private class PlacesAutoCompleteAdapter extends ArrayAdapter<String>
			implements Filterable {
		private ArrayList<String> resultList;

		public PlacesAutoCompleteAdapter(Context context, int textViewResourceId) {
			super(context, textViewResourceId);
		}

		@Override
		public int getCount() {
			return resultList.size();
		}

		@Override
		public String getItem(int index) {
			return resultList.get(index);
		}

		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {
				@Override
				protected FilterResults performFiltering(CharSequence constraint) {
					FilterResults filterResults = new FilterResults();
					if (constraint != null) {
						// Retrieve the autocomplete results.
						resultList = autocomplete(constraint.toString());

						// Assign the data to the FilterResults
						filterResults.values = resultList;
						filterResults.count = resultList.size();
					}
					return filterResults;
				}

				@Override
				protected void publishResults(CharSequence constraint,
						FilterResults results) {
					if (results != null && results.count > 0) {
						notifyDataSetChanged();
					} else {
						notifyDataSetInvalidated();
					}
				}
			};
			return filter;
		}
	}
}