package com.with.tours;

import java.util.Vector;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.with.tourbuild.CommonShared;

public class RegisteredUsersAdapter extends BaseAdapter {

	private Context        myContext;
	private Vector<String> mUsers;
	
	public RegisteredUsersAdapter(Context context, Vector<String> users) {
		this.myContext = context;
		mUsers         = users;
	}

	@Override
	public int getCount() {
		return mUsers.size();
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
		
		tv.setText("   " + mUsers.get(position));
		tv.setTextSize(25);
		tv.setTextColor(Color.BLUE);
		
		return tv;
	}

}
