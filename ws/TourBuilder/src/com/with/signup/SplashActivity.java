package com.with.signup;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseException;
import com.parse.ParseInstallation;
import com.parse.ParseUser;
import com.parse.PushService;
import com.with.tourbuild.CommonShared;
import com.with.tourbuilder.R;
import com.with.tourbuilder.SharedObjects;
import com.with.tourbuilder.StartActivity;

public class SplashActivity extends Activity {
	private static long SLEEP_TIME = 1; // Sleep for some time
	private SharedPreferences mPreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		setContentView(R.layout.activity_splash);
		// Check if user was already signed in
		ParseUser currentUser = ParseUser.getCurrentUser();
		if (currentUser != null) {
			// If so - go to start activity
			CommonShared.getInstance().setmParseUser(currentUser);
			Intent intent = new Intent(SplashActivity.this, StartActivity.class);
			startActivity(intent);
		} else {
			// If no check if login info saved in shared prefs
			mPreferences = getSharedPreferences("configur", MODE_PRIVATE);
			String Email = mPreferences.getString("Email", "");
			String Name = mPreferences.getString("Name", "");
			String Password = mPreferences.getString("Password", "");
			// String Email = "costa@one.co.il";
			// String Name = "costa";
			// String Password = "qwe";
			// String Email = "costa@mail.com";
			// String Name = "costa1";
			// String Password = "qwe";
			// If user was no signed up, start SIGNUP
			// if (Email.equals("")) {
			// } else {}
			if (!Email.equals("")) {
				ParseUser.logInInBackground(Name, Password,
						new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// Login successful
									CommonShared.getInstance().setmParseUser(
											user);
									Intent intent = new Intent(
											SplashActivity.this,
											StartActivity.class);
									SplashActivity.this.startActivity(intent);

								} else {
									// Login failed
									// Toast.makeText(getApplicationContext(),
									// "LOGIN FAILED", Toast.LENGTH_LONG)
									// .show();
								}
							}
						});
			}
		}
	}

	public void signUp(View v) {
		Intent intent = new Intent(SplashActivity.this, SignUp.class);
		startActivity(intent);
	}

	public void signIn(View v) {
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.dialog_signin);
	    dialog.setTitle("Login");

		final EditText name    = (EditText)dialog.findViewById(R.id.editTextUserNameToLogin);
		final EditText password = (EditText)dialog.findViewById(R.id.editTextPasswordToLogin);

		Button btnSignIn=(Button)dialog.findViewById(R.id.buttonSignIn);
		
		// Set On ClickListener
		btnSignIn.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View  v) {
				ParseUser.logInInBackground(name.getText().toString(), password.getText().toString(),
						new LogInCallback() {
							public void done(ParseUser user, ParseException e) {
								if (user != null) {
									// Login successful
									CommonShared.getInstance().setmParseUser(
											user);
									Intent intent = new Intent(
											SplashActivity.this,
											StartActivity.class);
									SplashActivity.this.startActivity(intent);

								} else {
									 // Login failed
									 Toast.makeText(getApplicationContext(),
									 "LOGIN FAILED", Toast.LENGTH_LONG)
									 .show();
								}
							}
						});
			}
		});
		dialog.show();
	}

}
