package com.with.sales;

import com.with.MyListAdapter;

import android.app.ListActivity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ListView;

public class SalesActivity extends ListActivity 
{

	private MyListAdapter adapter;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		adapter = new MyListAdapter(getApplicationContext(), GeoPointVector.getInstance());
		setListAdapter(adapter);
		ListView lv = getListView();
//		TextView siteTv = (TextView)lv.getAdapter().get;findViewById(R.id.textViewSite);
		lv.setOnItemLongClickListener(new OnItemLongClickListener() 
		{
			@Override
			public boolean onItemLongClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
				Intent siteIntent = new Intent(Intent.ACTION_VIEW, 
										Uri.parse("" + adapter.getmSalesVector().
										getmVector().get(arg2).getmSite()));
				startActivity(siteIntent);
				return true;
			}
		});
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
		//getMenuInflater().inflate(R.menu.sales, menu);
		return true;
	}

}
