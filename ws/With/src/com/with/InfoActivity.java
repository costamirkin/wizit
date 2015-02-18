package com.with;

import com.with.tourbuild.CommonShared;

import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.widget.TextView;

public class InfoActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_info);
		TextView name = (TextView)findViewById(R.id.myName);
		TextView email = (TextView)findViewById(R.id.myMail);
		
		name.setText(SharedObjects.getInstance().getmParseUser().getUsername());
		email.setText(SharedObjects.getInstance().getmParseUser().getEmail());
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.info, menu);
		return true;
	}

}
