package com.with.gui;

import java.util.List;

import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.ItemizedIconOverlay;
import org.osmdroid.views.overlay.OverlayItem;

import com.with.SharedObjects;
import com.with.chat.ChatWindow;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.graphics.Point;
import android.net.Uri;
import android.view.MotionEvent;
import android.widget.Toast;

public class FriendsOverlay extends ItemizedIconOverlay<OverlayItem> {

	protected Context mContext;

	public FriendsOverlay(final Context context, final List<OverlayItem> aList) {
		super(context, aList, new OnItemGestureListener<OverlayItem>() {
			@Override
			public boolean onItemSingleTapUp(final int index,
					final OverlayItem item) {
				return false;
			}

			@Override
			public boolean onItemLongPress(final int index,
					final OverlayItem item) {
				return false;
			}
		});
		mContext = context;
	}

	@Override
	public boolean addItem(OverlayItem item) {
		return super.addItem(item);
	}
	@Override
	protected boolean onSingleTapUpHelper(final int index,
			final OverlayItem item, final MapView mapView) {
		AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
		dialog.setTitle(item.getTitle());
		dialog.setMessage(item.getSnippet());
		dialog.setPositiveButton("CHAT", new OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				Intent intent = new Intent(mContext, ChatWindow.class);
				intent.putExtra("Name", 
						SharedObjects.getInstance().getmFriends().get(index).getmName());
				mContext.startActivity(intent);
			}
		});
		dialog.show();
		return true;
	}

	@Override
	public boolean onSingleTapUp(MotionEvent e, MapView mapView) {
		float x = e.getRawX();
		float y = e.getRawY();
		GeoPoint gp = (GeoPoint) mapView.getProjection().fromPixels(x,y);
		return super.onSingleTapUp(e, mapView);
	}

	@Override
	public boolean onSnapToItem(int arg0, int arg1, Point arg2, MapView arg3) {
		// TODO Auto-generated method stub
		return false;
	}

}