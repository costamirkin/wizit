package com.with.tourbuilder;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapController;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.SaveCallback;
import com.with.tourbuild.CommonShared;
import com.with.tourbuild.Poi;
import com.with.tourbuild.Tour;
import com.with.tourbuilder.SharedObjects.OpearionalMode;

public class MainActivity extends Activity implements IPostListener{
	
	MapController mMapController;
	MapView       mMapView; 
	GeoPoint      LOCATION_ME = new GeoPoint(32.341609,34.863321); 

	PoisOverlay            mPointOverlay = null;
	ArrayList<OverlayItem> mOverlayItems;
	PathOverlay            mPathOverlay = null;
	
	
	private void createPathOverlay() {
		mPathOverlay  = new PathOverlay(Color.BLUE, this);
		Paint pPaint = mPathOverlay.getPaint();
	    pPaint.setStrokeWidth(5);
	    mPathOverlay.setPaint(pPaint);
		
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mOverlayItems = new ArrayList<OverlayItem>();

		SharedObjects.getInstance().setmMode(OpearionalMode.NONE);
		
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.setMultiTouchControls(true);
		GeoPoint mMyLocation = new GeoPoint(31.892225, 34.81312);
		mMapView.getController().setCenter(mMyLocation);
		mMapView.getController().setZoom(14);

		mMapController = mMapView.getController();
		mMapController.setZoom(15);
		mMapController.setCenter(LOCATION_ME);
		createPathOverlay();
		mPointOverlay = new PoisOverlay(mMapView.getContext(), mOverlayItems, this);
		mMapView.getOverlays().add(mPointOverlay);
		addPois(mOverlayItems);
		
	}
	
	public void addPois(final ArrayList<OverlayItem> newoverlay) {

		ParseQuery<ParseObject> query = ParseQuery.getQuery("POI");
		query.findInBackground(new FindCallback<ParseObject>() {

			@Override
			public void done(List<ParseObject> objects, ParseException e) {

				if(e == null) {
					CommonShared.getInstance().getmPois().clear();
					CommonShared.getInstance().ReadPois(objects);
					updateGui("Pois");
				}

			}

		});

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_normal_mode, menu);
		
		return true;
	}
	
	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if(menu != null) {
			menu.clear();
		}
		if(SharedObjects.getInstance().getmMode() == OpearionalMode.NONE){
			menu.setGroupVisible(R.menu.menu_normal_mode, false);
			getMenuInflater().inflate(R.menu.menu_normal_mode, menu);
			
		}
		else if(SharedObjects.getInstance().getmMode() == OpearionalMode.NEW_POI){
			menu.setGroupVisible(R.menu.menu_normal_mode, false);
			getMenuInflater().inflate(R.menu.menu_normal_mode, menu);
			
		}
		else if(SharedObjects.getInstance().getmMode() == OpearionalMode.BUILD_TOUR) {
			menu.setGroupVisible(R.menu.menu_tour, false);
			getMenuInflater().inflate(R.menu.menu_tour, menu);
			
		}
		return super.onPrepareOptionsMenu(menu);
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		Intent intent;
		switch (item.getItemId()) {
		case R.id.starttour:
			CommonShared.getInstance().setmCurrentTour(new Tour());
			SharedObjects.getInstance().setmMode(OpearionalMode.BUILD_TOUR);	
			break;
		case R.id.addpoint:
			SharedObjects.getInstance().setmMode(OpearionalMode.NEW_POI);	
			break;

		case R.id.tourDetails:
			intent = new Intent(getApplicationContext(), TourDetails.class);
			startActivity(intent);
			break;
		case R.id.clearTour:
			break;
		case R.id.save:
			Tour tour = CommonShared.getInstance().getmCurrentTour();
			if (tour.getmTourName() == null || tour.getmTourDescription() == null) {
				Toast.makeText(getApplicationContext(), "Please fill tour details", Toast.LENGTH_LONG).show();
				intent = new Intent(getApplicationContext(), TourDetails.class);
				startActivity(intent);
			}
			else {
				SharedObjects.getInstance().setmMode(OpearionalMode.NONE);
				tourToParse(this);
			}
			break;
		case R.id.cancel:
			SharedObjects.getInstance().setmMode(OpearionalMode.NONE);	
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	
	@Override
	public void updateGui(final String dataType) {
		runOnUiThread(new Runnable() {
			public void run() {
				if (dataType.equals("Pois")) {
					Vector<Poi> pois = CommonShared.getInstance().getmPois();
					
					// Clear old overlay
					mMapView.getOverlays().remove(mPointOverlay);
					mOverlayItems.clear();
					
					// Build new overlay
					for (Iterator iterator = pois.iterator(); iterator.hasNext();) {
						Poi poi = (Poi) iterator.next();
						OverlayItem item = new OverlayItem(poi.getmName(),
								poi.getmDescription(), new GeoPoint(poi.getmLat(), poi.getmLong()));
								mOverlayItems.add(item);
					}
	
					mPointOverlay = new PoisOverlay(mMapView.getContext(), mOverlayItems, MainActivity.this);
					mMapView.getOverlays().add(mPointOverlay);
				}
				else if (dataType.equals("Tour")) {
					mMapView.getOverlays().remove(mPathOverlay);
					Vector<Poi> tourPois = CommonShared.getInstance().getmCurrentTour().getmPois();
					createPathOverlay();
					for (Iterator iterator = tourPois.iterator(); iterator.hasNext();) {
						Poi poi = (Poi) iterator.next();
						GeoPoint geoPoint = new GeoPoint(poi.getmLat(), poi.getmLong());
						mPathOverlay.addPoint(geoPoint);
						mMapView.getOverlays().add(mPathOverlay);
					}

				}
				mMapView.postInvalidate();
				
			}
		});
	}

	
	public static void tourToParse(final Activity a) {

		final ParseObject object = new ParseObject("TOUR");
		Tour tour = CommonShared.getInstance().getmCurrentTour();
				
		object.put("Name", tour.getmTourName());
		object.put("Description", tour.getmTourDescription());
		object.put("Type", tour.getmTourType());
		object.put("GuideName", CommonShared.getInstance().getmParseUser().getUsername());
		
		for (Iterator iterator = tour.getmPois().iterator(); iterator.hasNext();) {
			Poi poi = (Poi) iterator.next();
			object.add("pois",  ParseObject.createWithoutData("Poi", poi.getmObjectId()));
			
		}
		
		object.saveInBackground((new SaveCallback() {
			public void done(ParseException e) {
				if (e == null) {
					Toast.makeText(a.getApplicationContext(), "Tour saved", Toast.LENGTH_LONG).show();
				}
				else {
					Toast.makeText(a.getApplicationContext(), "Problem with network!", Toast.LENGTH_LONG).show();
				}
			}
		}));
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			// Add new item to overlay
			OverlayItem item = new OverlayItem(data.getStringExtra("name"), data.getStringExtra("description"), 
					new GeoPoint(data.getDoubleExtra("lat", 0), data.getDoubleExtra("lon", 0)));
			mPointOverlay.addItem(item);
			
		}
		super.onActivityResult(requestCode, resultCode, data);
	}
}	
