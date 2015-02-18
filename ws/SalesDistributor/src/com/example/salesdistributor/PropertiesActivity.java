package com.example.salesdistributor;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class PropertiesActivity extends Activity 
{
	private EditText StoreName;
	private EditText SaleTitle;
	private EditText SaleContent;
	private EditText Site;
	private Intent backIntent;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_properties);
		
		StoreName = (EditText) findViewById(R.id.editTextStoreName);
		SaleTitle = (EditText) findViewById(R.id.EditTextSaleTitle);
		SaleContent = (EditText) findViewById(R.id.EditTextSaleContent);
		Site = (EditText) findViewById(R.id.EditTextSite);
		
		backIntent = getIntent();
	}

	public void onCancel (View v)
	{
		setResult(RESULT_CANCELED, backIntent);
		finish();
	}
	
	public void onOK (View v)
	{
		backIntent.putExtra("StoreName", StoreName.getText().toString());
		backIntent.putExtra("SaleTitle", SaleTitle.getText().toString());
		backIntent.putExtra("SaleContent", SaleContent.getText().toString());
		backIntent.putExtra("Site", Site.getText().toString());
		
		setResult(RESULT_OK, backIntent);
		finish();
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.properties, menu);
		return true;
	}

}
