package com.with.tours;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.with.tourbuild.CommonShared;
import com.with.tourbuilder.R;

public class TourListAdapter extends BaseAdapter {

	private Context myContext;
	
	public TourListAdapter(Context myContext) {
		// TODO Auto-generated constructor stub
				
		this.myContext = myContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CommonShared.getInstance().getmTours().size();
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
		
		LinearLayout linearLayout;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			linearLayout = (LinearLayout)inflater.inflate(R.layout.tour_item, parent, false);
		}
		else {
			linearLayout = (LinearLayout)convertView;
		}
		
		ImageView image = (ImageView) linearLayout.findViewById(R.id.imageView1);
		TextView tourName = (TextView) linearLayout.findViewById(R.id.listTourName);
		
//		image.setImageResource(victor.get(position).getFlag());
		tourName.setText(CommonShared.getInstance().getmTours().get(position).getmTourName());
		tourName.setTextSize(20);
//		capital.setText(victor.get(position).getCapital());
//		capital.setTextSize(20);
		
		
		return linearLayout;
	}

}
