package com.with.sales;

import java.util.List;
import java.util.Vector;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import com.with.PushReceiver;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.graphics.Point;
import android.net.Uri;
import android.view.MotionEvent;

public class OnPushMapItemizer extends ItemizedIconOverlay<OverlayItem> 
{
	protected Context mContext;
	
	public OnPushMapItemizer(final Context context, final List<OverlayItem> aList) 
	{
		super(context, aList, new OnItemGestureListener<OverlayItem>() 
		{
			@Override
			public boolean onItemSingleTapUp(final int index, final OverlayItem item) 
			{
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index, final OverlayItem item) 
			{
				return false;
			}
		});
		mContext = context;
	}

	@Override
	public boolean addItem(OverlayItem item) // add to overLay new item
	{
		return super.addItem(item);
	}
	
	@Override
	protected boolean onSingleTapUpHelper(final int index, final OverlayItem item, final MapView mapView) 
	{
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());		
		dialog.setPositiveButton("To their site", new OnClickListener() 
				{
					@Override
					public void onClick(DialogInterface dialog, int which) 
					{
						Intent siteIntent = new Intent(Intent.ACTION_VIEW, 
								Uri.parse( "" + PushReceiver.getmStoreSiteIntent() ));
						mContext.startActivity(siteIntent);
					}
				});
		dialog.show();
		return true;
	}

	
	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView)  // get the position that has tapped
	{
		GeoPoint gp = (GeoPoint) mapView.getProjection().fromPixels(e.getRawX(), e.getRawY());
		return super.onSingleTapUp(e, mapView);
	}

	@Override
	public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) 
	{
		// TODO Auto-generated method stub
		return false;
	}
}
