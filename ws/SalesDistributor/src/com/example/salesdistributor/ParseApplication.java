package com.example.salesdistributor;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseUser;
import com.parse.PushService;

public class ParseApplication extends Application {

	@Override
	public void onCreate() 
	{
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "OzYWKFRMuZiSM42TCU9jvYhl5wlQohkkvQhJptfY",
				"O3gSqnIuGqPDSqCPgwvNp85bjaBNOIKB8B4kD3i9");

		//ACL MAKES BUGS!!
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);
	}
}