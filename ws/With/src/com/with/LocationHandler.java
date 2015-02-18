package com.with;

import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.widget.Toast;

public class LocationHandler {
	Context mContext;
	LocationInterface mLocationInterface = null;
	
	// Current location
	private Location mLocation;
	
	// Last location
	Location mLastLocation;

	public Location getmLocation() {
		return mLocation;
	}

	public void setLocationInterface(LocationInterface locationInterface) {
		mLocationInterface = locationInterface;
	}

	public LocationHandler(Context mContext) {
		super();
		this.mContext = mContext;
		gpsSetup();
	}

	public void gpsSetup() {
		mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);

		mLocation = mLocationManager.getLastKnownLocation(android.location.LocationManager.GPS_PROVIDER);
		if (mLocation == null) {
			mLocation = mLocationManager.getLastKnownLocation(android.location.LocationManager.NETWORK_PROVIDER);
		}
		if (mLocation == null) {
			Toast.makeText(mContext, "Location is not available yet", Toast.LENGTH_LONG).show();
			return;
		}
		
		if (mLocationInterface != null) {
			mLocationInterface.markLocationOnMap(mLocation);
		}
		
//		Criteria criteria = new Criteria();
//		criteria.setAccuracy(Criteria.ACCURACY_FINE);
//		criteria.setAltitudeRequired(false);
//		criteria.setBearingRequired(false);
//		criteria.setCostAllowed(true);
//		criteria.setPowerRequirement(Criteria.POWER_LOW);


	}
	
	public void registerForUpdates() {
		mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 100, locationListener);

	}

	public void unRegisterForUpdates() {
		mLocationManager.removeUpdates(locationListener);

	}

	private final LocationListener locationListener = new LocationListener() {
		public void onLocationChanged(Location location) {
			mLocation = location;
			if (mLocationInterface != null)  {
				mLocationInterface.markLocationOnMap(location);
			}
			else {
				mLocation = new Location(android.location.LocationManager.NETWORK_PROVIDER);
				mLocation.setLatitude(Constants.DEFAULT_LAT);
				mLocation.setLongitude(Constants.DEFAULT_LONG);
			}

		}

		public void onProviderDisabled(String provider) {
		}

		public void onProviderEnabled(String provider) {
		}

		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	};
	private LocationManager mLocationManager;

}
