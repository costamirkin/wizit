package com.with.guides;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.with.R;
import com.with.tourbuild.CommonShared;

public class GuidesListAdapter extends BaseAdapter {

	private Context myContext;
	
	public GuidesListAdapter(Context myContext) {
		// TODO Auto-generated constructor stub
				
		this.myContext = myContext;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return CommonShared.getInstance().getmGuides().size();
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
		
		RelativeLayout relativeLayout;
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) myContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			relativeLayout = (RelativeLayout)inflater.inflate(R.layout.guide_item, parent, false);
		}
		else {
			relativeLayout = (RelativeLayout)convertView;
		}
		
		RatingBar rating   = (RatingBar) relativeLayout.findViewById(R.id.guideRatingBar);
		TextView guideName = (TextView) relativeLayout.findViewById(R.id.guideName);
		TextView notRated  = (TextView) relativeLayout.findViewById(R.id.guideNotRatedText);
		
		guideName.setText(CommonShared.getInstance().getmGuides().get(position).getmGuideName());
		
		int rate         = CommonShared.getInstance().getmGuides().get(position).getmGuideRate();
		int ratesNumber  = CommonShared.getInstance().getmGuides().get(position).getmRatesNumber();
		if (ratesNumber == 0) {
			// Not rated
			notRated.setVisibility(View.VISIBLE);
			rating.setVisibility(View.INVISIBLE);
		}
		else {
			notRated.setVisibility(View.INVISIBLE);
			rating.setVisibility(View.VISIBLE);
			float average = (float)rate/ratesNumber;
			rating.setRating(average);
		}
		
		return relativeLayout;
	}

}
