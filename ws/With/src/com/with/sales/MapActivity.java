package com.with.sales;

import java.util.ArrayList;
import java.util.Vector;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.location.Location;
import android.os.Bundle;
import android.os.IBinder;
import android.view.Menu;

import com.with.DataService;
import com.with.LocationInterface;
import com.with.MyBinder;
import com.with.R;
import com.with.SharedObjects;

public class MapActivity extends Activity implements SalesListener , ServiceConnection, LocationInterface{

	private MapView mMapView;
	private GeoPoint mMyLocation;
	private Vector<NewSale> mSalesVector;
	private SalesItemizer mSalesLayout;
	
	DataService mDataService = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_map);
		mSalesVector = SharedObjects.getInstance().getmSales();

		Location myLocation = SharedObjects.getInstance().getmLocationHandler().getmLocation();
		mMyLocation = new GeoPoint(myLocation.getLatitude(), myLocation.getLongitude());
		mMapView = (MapView) findViewById(R.id.mapview);
		mMapView.setBuiltInZoomControls(true);
		mMapView.getController().setZoom(15);
		mMapView.getController().setCenter(mMyLocation);
		
		Intent intent = new Intent(this, DataService.class);
		startService(intent);
		bindService(intent, this, 0);


	}
	
	@Override
	protected void onPause() {
		SharedObjects.getInstance().getmLocationHandler()
				.setLocationInterface(null);
		SharedObjects.getInstance().getmLocationHandler()
				.unRegisterForUpdates();
		if (mDataService != null) {
			mDataService.setmSalesListener(null);
		}

		super.onPause();
	}

	@Override
	protected void onResume() {
		SharedObjects.getInstance().getmLocationHandler()
				.setLocationInterface(this);
		SharedObjects.getInstance().getmLocationHandler().registerForUpdates();
		if (mDataService != null) {
			mDataService.setmSalesListener(this);
		}
		super.onResume();
	}

	@Override
	public void onServiceConnected(ComponentName arg0, IBinder binder) {
		mDataService = ((MyBinder) binder).ContainedService;
		if (mDataService != null) {
			mDataService.setmSalesListener(this);
			mDataService.ResumeSalesListener();
		}

	}

	@Override
	public void onServiceDisconnected(ComponentName arg0) {
		mDataService.setmSalesListener(null);
		mDataService = null;
	}



	public Vector<NewSale> getmSalesVector() 
	{
		return mSalesVector;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.map, menu);
		return true;
	}

	@Override
	public void UpdateSales() {
		ArrayList<OverlayItem> overlayItemArrayList = new ArrayList<OverlayItem>();

		mMapView.getOverlays().remove(mSalesLayout);
		for (int i = 0; i < mSalesVector.size(); i++) 
		{
			GeoPoint geoPoint = new GeoPoint((mSalesVector.get(i).getmGeopoint().getLatitude()),
											 (mSalesVector.get(i).getmGeopoint().getLongitude()));
//			overlayItemArrayList.add(new OverlayItem(mSalesVector.get(i).getmTitle() + " - " + mSalesVector.get(i).getmStoreName(),
//													mSalesVector.get(i).getmContent(), geoPoint));
			overlayItemArrayList.add(new OverlayItem(mSalesVector.get(i).getmStoreName(), 
													mSalesVector.get(i).getmTitle() + "\n" +
													mSalesVector.get(i).getmContent()+ "\n" + 
													mSalesVector.get(i).getmSite(), geoPoint));
		}
		
		mSalesLayout = new SalesItemizer(this, overlayItemArrayList, mSalesVector);
		mMapView.getOverlays().add(mSalesLayout);
		
		
	}

	@Override
	public void markLocationOnMap(Location location) {
		// TODO Auto-generated method stub
		
	}

}
