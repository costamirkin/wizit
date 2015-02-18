package com.with.sales;

import java.util.List;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.Parse;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.with.SharedObjects;
import com.with.Utils;

public class CheckService extends Service implements LocationListener
{
	private GeoPointVector geoPointVector;
	private ParseGeoPoint userLocation;
	private CheckThread checkThread;
	
	
	@Override
	public void onCreate() 
	{
		geoPointVector = GeoPointVector.getInstance(); // vector constructor
		
		checkThread = new CheckThread();
		checkThread.start();
		super.onCreate();
	}
	
	

	@Override
	public void onLocationChanged(Location location) 
	{
       double lat = (location.getLatitude());
       double lon = (location.getLongitude());
       Log.i("GPS", "location changed: lat= "+lat+", lon= "+lon);		
       userLocation = new ParseGeoPoint(lat, lon);
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
	public int onStartCommand(Intent intent, int flags, int startId) 
	{
		// TODO Auto-generated method stub
		return super.onStartCommand(intent, flags, startId);
	}
	
	
	@Override
	public IBinder onBind(Intent arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}
	
	
	//================================================================================
	// Thread        Thread        Thread        Thread        Thread        Thread          
	//================================================================================
	
	
	private class CheckThread extends Thread 
	{
		Location location = null;
		private ParseGeoPoint loc;
		
		
		@Override
		public void run() 
		{
			super.run();
			while(true)
			{
//				THIS THREAD MISSIONS
//			   1.	get GeoPoint From GPS (my location)
//			   2.   get the Sales From Parse that close (1 kilometer) to my location
//			   3.   place the sale in geoPointVector (singelton)
				
		        location = SharedObjects.getInstance().getmLocationHandler().getmLocation();
		 
		        if (location != null) 
		        {
		        	String message = "Current Location " +  location.getLatitude() + ", " +  location.getLongitude();
		        	Log.i("!!!!!!!!!!!!!!!!!", message);
			        loc = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
		        }
		        
		        
		        else 
		        {
		    		Toast.makeText(getApplicationContext(),"Cant get any location", Toast.LENGTH_SHORT).show();
		        }				
		        
		        
		        if(loc != null)
		        {
			        ParseQuery<ParseObject> query = ParseQuery.getQuery("Sales");
			        query.whereWithinKilometers("position", loc, 6);
			        query.findInBackground(new FindCallback<ParseObject>() 
			        		{
					            public void done(List<ParseObject> Places, ParseException e) 
					            {
					                if (e == null) 
					                {
					                	if(Places.size() > 0)
					                	{
					        				geoPointVector.getmVector().clear();
					                	}
					                	
					                	for (int i = 0; i < Places.size(); i++) 
					                	{
					                		String saleTitle   = Places.get(i).getString("saleTitle");
					                		String saleContent = Places.get(i).getString("SaleContent");
					                		String placeName   = Places.get(i).getString("StoreTitle");
					                		String SiteName   = Places.get(i).getString("StoreSite");
					                		ParseGeoPoint placeLoc = Places.get(i).getParseGeoPoint("position");
					                	
					                		geoPointVector.getmVector().add(new NewSale(placeName, saleTitle, saleContent, placeLoc, SiteName));
					                		
					        				Log.i("Parse!!!", "Get " + placeName);
					                	}
					                } 
					                else 
					                {
					                    Log.i("Parse!!!", "Error: " + e.getMessage());
					                }
					            }
					        });
					SystemClock.sleep(10000);
		        }
		        
				SystemClock.sleep(30000);
			}
		}
	}
}