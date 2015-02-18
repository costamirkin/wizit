package com.with;

import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParsePush;
import com.parse.ParseQuery;
import com.parse.PushService;

public class ParseStarterProjectActivity extends Activity {
	/** Called when the activity is first created. */
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		PushService.subscribe(this, "costa", ParseStarterProjectActivity.class);
		PushService.setDefaultPushCallback(this, ParseStarterProjectActivity.class);
	}
	
	public void parse(View v) {
		ParsePush push = new ParsePush();
		push.setMessage("From emulator");
		push.setChannel("costa");
		push.sendInBackground();

	}

	public void save(View v) {
		ParseObject gameScore1 = new ParseObject("Makolet1");
		gameScore1.put("Product", "Milk");
		gameScore1.put("Amount", 2);
		gameScore1.put("skills", Arrays.asList("pwnage", "flying"));
		gameScore1.saveInBackground();

		ParseObject gameScore2 = new ParseObject("Makolet1");
		gameScore2.put("Product", "Crembo");
		gameScore2.put("Amount", 8);
		gameScore2.saveInBackground();

		ParseObject gameScore3 = new ParseObject("Makolet1");
		gameScore3.put("Product", "Candy");
		gameScore3.put("Amount", 22);
		gameScore3.saveInBackground();

	}

	
//	public void save(View v) {
//		final ParseObject gameScore = new ParseObject("Hackeru");
//		gameScore.put("Manager", "Ofir");
//		gameScore.put("City", "RamatGan");
//		gameScore.put("IsBest", true);
//		// gameScore.saveInBackground();
//		gameScore.saveInBackground(new SaveCallback() {
//			public void done(ParseException e) {
//				gameScore.put("Manager", "OfirM");
//				gameScore.put("City", "RamatGan");
//				gameScore.put("IsBest", true);
//				gameScore.saveInBackground();
//
//			}
//		});
//
//	}

	@SuppressWarnings("unchecked")
	public void load(View v) {
		ParseQuery<ParseObject> query = new ParseQuery<ParseObject>("Makolet1");
		query.whereEqualTo("Product", "Milk");
		query.findInBackground(new FindCallback() {
			public void done(List resultsList, ParseException e) {
				if (e == null) {
					Log.d("PARSE", "Retrieved " + resultsList.size()
							+ " objects");
					for (Iterator iterator = resultsList.iterator(); iterator
							.hasNext();) {
						ParseObject parseObject = (ParseObject) iterator.next();
						Log.d("PARSE", parseObject.getString("Product"));
					}
				} else {
					Log.d("PARSE", "Error: " + e.getMessage());
				}
			}
		});
	}

	public void json(View v) {
		try {
			int myNumber = 42;
			String myString = "the number is " + myNumber;
			Date myDate = new Date();

			JSONArray myArray = new JSONArray();
			myArray.put(myString);
			myArray.put(myNumber);

			JSONObject myObject = new JSONObject();
			myObject.put("number", myNumber);
			myObject.put("string", myString);

			byte[] myData = { 4, 8, 16, 32 };

			ParseObject bigObject = new ParseObject("BigObject");
			bigObject.put("myNumber", myNumber);
			bigObject.put("myString", myString);
			bigObject.put("myDate", myDate);
			bigObject.put("myData", myData);
			bigObject.put("myArray", myArray);
			bigObject.put("myObject", myObject);

			bigObject.saveInBackground();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void addGeo(View v) {
		ParseObject geoObject1 = new ParseObject("GeoObject");
		geoObject1.put("Name", "Hackeru");
		ParseGeoPoint point = new ParseGeoPoint(32.084611, 34.800889);
		geoObject1.put("location", point);
		geoObject1.saveInBackground();

		ParseObject geoObject2 = new ParseObject("GeoObject");
		geoObject2.put("Name", "Bursa");
		ParseGeoPoint point2 = new ParseGeoPoint(32.083974, 34.801812);
		geoObject2.put("location", point2);
		geoObject2.saveInBackground();

		ParseObject geoObject3 = new ParseObject("GeoObject");
		geoObject3.put("Name", "PetahTikva");
		ParseGeoPoint point3 = new ParseGeoPoint(32.092492, 34.848697);
		geoObject3.put("location", point3);
		geoObject3.saveInBackground();

	}

	public void pushJson(View v) {
		JSONObject data;
		try {
			data = new JSONObject("{\"action\": \"com.example.UPDATE_STATUS\"," +
					"\"alert\": \"The Mets scored!\",\"badge\": \"Increment\"}");
			ParsePush push = new ParsePush();
			push.setChannel("costa");
			push.setData(data);
			push.sendInBackground();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public void getGeo(View v) {
		ParseGeoPoint point = new ParseGeoPoint(32.084611, 34.81);
		ParseQuery query = new ParseQuery("GeoObject");
		// query.whereNear("location", point);
		query.whereWithinKilometers("location", point, 1.0);
		query.setLimit(10);
		query.findInBackground(new FindCallback() {
			public void done(List resultsList, ParseException e) {
				if (e == null) {
					Log.d("PARSE", "Retrieved " + resultsList.size() + " objects");
					
					for (int i = 0; i < resultsList.size(); i++) {
						ParseObject parseObject =  (ParseObject) resultsList.get(i);
						Log.d("PARSE", parseObject.getString("Name"));
					}
				} else {
					Log.d("PARSE", "Error: " + e.getMessage());
				}
			}
		});
	}


}
