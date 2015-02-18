package com.with.tours;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.with.tourbuilder.RegistrationRequest;
import com.with.tourbuilder.SharedObjects;

public class RegistertrationRequestsAdapter extends BaseAdapter {

	private Context        myContext;
	
	public RegistertrationRequestsAdapter(Context context) {
		this.myContext = context;
	}

	@Override
	public int getCount() {
		return SharedObjects.getInstance().getmRegistrationRequests().size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		TextView tv;
		if (convertView == null) {
			tv = new TextView(myContext);
		}
		else {
			tv = (TextView)convertView;
		}
		
		RegistrationRequest r = SharedObjects.getInstance().getmRegistrationRequests().get(position);
		tv.setText("   " + r.getmUserName());
		tv.setTextSize(25);
		
		if (r.ismRegistered()) {
			tv.setTextColor(Color.RED);
		}
		else {
			tv.setTextColor(Color.BLUE);
		}
		
		return tv;
	}

}
