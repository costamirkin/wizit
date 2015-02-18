package com.with;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Vector;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;
import org.osmdroid.views.overlay.PathOverlay;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;
import android.view.MenuItem;

import com.with.chat.ChatWindow;
import com.with.gui.FriendsOverlay;
import com.with.gui.GoogleParser;
import com.with.gui.Route;

public class FriendsActivity extends Activity implements IPostListener,
		ServiceConnection, LocationInterface {

	private MapView mMapView;
	FriendsOverlay myItemizedOverlay = null;
	private GeoPoint mMyLocation = null;

	DataService mDataService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		Intent intent = new Intent(this, DataService.class);
		startService(intent);
		bindService(intent, this, 0);

		Location myLocation = SharedObjects.getInstance().getmLocationHandler()
				.getmLocation();

		if (myLocation == null) {
			mMyLocation = new GeoPoint(31.892225, 34.81312);
		} else {
			mMyLocation = new GeoPoint(myLocation.getLatitude(),
					myLocation.getLongitude());
		}
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(14);
		mMapView.getController().setCenter(mMyLocation);

		Bundle bundle = getIntent().getExtras();
		if (bundle != null) {
			String friendName = bundle.getString("FriendName");
			if (friendName != null) {
				addDirections(friendName);
			}
		}
	}

	@Override
	protected void onPause() {
		SharedObjects.getInstance().getmLocationHandler()
				.setLocationInterface(null);
		SharedObjects.getInstance().getmLocationHandler()
				.unRegisterForUpdates();
		if (mDataService != null) {
			mDataService.setmFriendsListener(null);
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		SharedObjects.getInstance().getmLocationHandler()
				.setLocationInterface(this);
		SharedObjects.getInstance().getmLocationHandler().registerForUpdates();
		if (mDataService != null) {
			mDataService.setmFriendsListener(this);
		}
		super.onResume();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.chat:
			Intent intent = new Intent(getApplicationContext(),
					ChatWindow.class);
			intent.putExtra("phone", "a");
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}

	@Override
	public void updateItems() {
		runOnUiThread(new Runnable() {
			public void run() {
				addPlaces();
				mMapView.postInvalidate();
			}
		});
	}

	private void addPlaces() {
		ArrayList<OverlayItem> overlayItemArrayList = new ArrayList<OverlayItem>();

		Vector<WithUser> friends = SharedObjects.getInstance().getmFriends();
		// If no friends, exit
		if (friends.size() == 0) {
			return;
		}
		for (int i = 0; i < friends.size(); i++) {
			GeoPoint geoPoint = new GeoPoint((friends.get(i).getmLat()),
					(friends.get(i).getmLon()));
			overlayItemArrayList.add(new OverlayItem(friends.get(i).getmName(),
					friends.get(i).getmID(), geoPoint));
		}

		myItemizedOverlay = new FriendsOverlay(this, overlayItemArrayList);
		mMapView.getOverlays().add(myItemizedOverlay);
	}

	@Override
	public void onServiceConnected(ComponentName arg0, IBinder binder) {
		mDataService = ((MyBinder) binder).ContainedService;
		if (mDataService != null) {
			mDataService.setmFriendsListener(this);
			mDataService.ResumeFriendsListener();
		}

	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		mDataService.setmFriendsListener(null);
		mDataService = null;
	}

	@Override
	public void markLocationOnMap(Location location) {
		mMyLocation = new GeoPoint(location.getLatitude(),
				location.getLongitude());
		mMapView.getController().setCenter(mMyLocation);

	}

	private void showDirections(Route route) {
		List<GeoPoint> points = route.getPoints();

		PathOverlay myPath = new PathOverlay(Color.RED, this);
		for (Iterator iterator = points.iterator(); iterator.hasNext();) {
			GeoPoint geoPoint = (GeoPoint) iterator.next();
			myPath.addPoint(geoPoint);

		}
		mMapView.getOverlays().add(myPath);
	}

	private Route directions(final GeoPoint start, final GeoPoint dest) {
		GoogleParser parser;
		String jsonURL = "http://maps.google.com/maps/api/directions/json?";
		final StringBuilder sBld = new StringBuilder(jsonURL);
		sBld.append("origin=");
		sBld.append(start.getLatitudeE6() / 1E6);
		sBld.append(',');
		sBld.append(start.getLongitudeE6() / 1E6);
		sBld.append("&destination=");
		sBld.append(dest.getLatitudeE6() / 1E6);
		sBld.append(',');
		sBld.append(dest.getLongitudeE6() / 1E6);
		sBld.append("&sensor=true&mode=walking");
		parser = new GoogleParser(sBld.toString());
		Route route = parser.parse();
		return route;
	}

	protected void addDirections(String friendName) {
		WithUser user = SharedObjects.getInstance().findUserByName(friendName);
		if (user != null) {
			GeoPoint destGeoPoint = new GeoPoint(user.getmLat(), user.getmLon());

			Route route = directions(mMyLocation, destGeoPoint);
			showDirections(route);
		}
	}

}
