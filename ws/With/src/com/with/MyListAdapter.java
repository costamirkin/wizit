package com.with;

import com.with.sales.GeoPointVector;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

public class MyListAdapter extends BaseAdapter 
{
	private Context mContext;
	private GeoPointVector mSalesVector;
	private LinearLayout ll;

	public MyListAdapter(Context context, GeoPointVector SalesVector)
	{
		mContext = context;
		mSalesVector = SalesVector;
	}
	
	
	public GeoPointVector getmSalesVector() 
	{
		return mSalesVector;
	}


	@Override
	public int getCount() 
	{
		// TODO Auto-generated method stub
		return mSalesVector.getmVector().size();
	}

	@Override
	public Object getItem(int arg0) 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) 
	{
		LayoutInflater inflater=(LayoutInflater)mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		ll = (LinearLayout)inflater.inflate(R.layout.activity_sales, parent, false);
	
		TextView StoreName   = (TextView)ll.findViewById(R.id.textViewStoreName);
		TextView SaleTitle   = (TextView)ll.findViewById(R.id.textViewSaleTitle);
		TextView SaleContent = (TextView)ll.findViewById(R.id.textViewSaleContent);
		TextView StoreSite   = (TextView)ll.findViewById(R.id.textViewSite);
		
		StoreName.setText(mSalesVector.getmVector().get(position).getmStoreName());
		SaleTitle.setText(mSalesVector.getmVector().get(position).getmTitle());
		SaleContent.setText(mSalesVector.getmVector().get(position).getmContent());
		StoreSite.setText(mSalesVector.getmVector().get(position).getmSite());
		
		StoreName.setTextColor(Color.BLACK);
		SaleTitle.setTextColor(Color.BLACK);
		SaleContent.setTextColor(Color.BLACK);
		StoreSite.setTextColor(Color.BLACK);
		return ll;
	}

}
