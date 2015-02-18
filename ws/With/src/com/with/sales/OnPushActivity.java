package com.with.sales;

import java.util.ArrayList;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.OverlayItem;

import com.with.PushReceiver;
import com.with.R;
import com.with.R.id;
import com.with.R.layout;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class OnPushActivity extends Activity 
{
	private TextView mStoreName;
	private TextView mSaleTitle;
	private TextView mSaleContent;
	private TextView mStoreSite;
	private TextView mAdrress;
	private MapView mMap;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_on_push);
		mStoreName   = (TextView)findViewById(R.id.textViewInPushStoreTitle);
		mSaleTitle   = (TextView)findViewById(R.id.textViewInPushSaleTitle);
		mSaleContent = (TextView)findViewById(R.id.textViewInPushSaleContent);
		mStoreSite   = (TextView)findViewById(R.id.textViewInPushSite);
		mAdrress     = (TextView)findViewById(R.id.textViewAdrress);
		mMap	     = (MapView) findViewById(R.id.mapviewInPush);
		try
		{
			mStoreName.setText(PushReceiver.getmStoreNameIntent());
			mSaleTitle.setText(PushReceiver.getmSaleTitleIntent());
			mSaleContent.setText(PushReceiver.getmSaleContentIntent());
			mStoreSite.setText(PushReceiver.getmStoreSiteIntent());
			mAdrress.setText(PushReceiver.getmStoreAdrress());
			
			double lati = Double.parseDouble(PushReceiver.getmLatitudeIntent().trim());
			double longti = Double.parseDouble(PushReceiver.getmLongitudeIntent().trim());
			GeoPoint gp = new GeoPoint(lati, longti);
			
			ArrayList<OverlayItem> overlayItemArrayList = new ArrayList<OverlayItem>();
			overlayItemArrayList.add(new OverlayItem((mStoreName.getText() + "  " +mSaleTitle.getText()), (String) ( mSaleContent.getText() ), gp));
			OnPushMapItemizer layout = new OnPushMapItemizer(this, overlayItemArrayList);
			mMap.getOverlays().add(layout);
			
			mMap.setBuiltInZoomControls(true);
			mMap.getController().setZoom(15);
			mMap.getController().setCenter(gp);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
		

		
	
		
//		mStoreSite.setOnClickListener(new OnClickListener() 
//		{
//			
//			@Override
//			public void onClick(View v) 
//			{
//				Intent siteIntent = new Intent(Intent.ACTION_VIEW, 
//						Uri.parse( "" +  mStoreSite.getText()));				
//			}
//		});
		
	}

	@Override
	protected void onDestroy() 
	{
		super.onDestroy();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		//getMenuInflater().inflate(R.menu.on_push, menu);
		return true;
	}
}
