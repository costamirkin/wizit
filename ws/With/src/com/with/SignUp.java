package com.with;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUp extends Activity {
	protected String mName;
	protected String mEmail;
	protected String mPassword;
	protected int mLang;
	protected int mKillo;
	protected SharedPreferences preferences;
	private SharedPreferences.Editor mEditor;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_signup);
		preferences = getSharedPreferences("configur", MODE_PRIVATE);

	}

	public void Cbutton(View v) {
		Intent MyIntent2 = new Intent();
		setResult(Activity.RESULT_CANCELED, MyIntent2);
		finish();
	}

	public void OKbutton(View v) {

		EditText MyText0 = (EditText) findViewById(R.id.ETname);
		EditText MyText1 = (EditText) findViewById(R.id.ETemail);
		EditText MyText2 = (EditText) findViewById(R.id.ETpassword);
		EditText MyText3 = (EditText) findViewById(R.id.ETpassword2);
		
		mName     = MyText0.getText().toString();
		mEmail    = MyText1.getText().toString();
		mPassword = MyText2.getText().toString();

		preferences = getSharedPreferences("configur", MODE_PRIVATE);
		mEditor = preferences.edit();

		ParseUser user = new ParseUser();
		user.setUsername(mName);
		user.setEmail(mEmail);
		user.setPassword(mPassword);
		user.put("userType", 1); // Tourist

		if (mPassword.equals(MyText3.getText().toString())) {
			user.signUpInBackground(new SignUpCallback() {
				public void done(ParseException e) {
					if (e == null) {
						ParseUser user  = ParseUser.getCurrentUser();
						SharedObjects.getInstance().setmParseUser(user);
						mEditor.putString("Name", mName);
						mEditor.putString("Email", mEmail);
						mEditor.putString("Password", mPassword);
						mEditor.commit();
						final ParseObject object = new ParseObject("TOURIST");
						object.put("ID",  ParseObject.createWithoutData("ID", user.getObjectId()));
						object.put("Name", mName);
						
						Location location = SharedObjects.getInstance().getmLocationHandler().getmLocation();
						if (location != null) {
							ParseGeoPoint Point = new ParseGeoPoint(location.getLatitude(), location.getLongitude());
							object.put("Location", Point);
						}
						object.saveInBackground();

						Intent intent = new Intent(SignUp.this, StartActivity.class);
						SignUp.this.startActivity(intent);

					} else {
						// Sign up didn't succeed. Look at the ParseException
						// to figure out what went wrong
					}
				}
			});
		} else {
			Toast.makeText(getApplicationContext(), "Password doesn't match", Toast.LENGTH_SHORT).show();

		}

	}

}
