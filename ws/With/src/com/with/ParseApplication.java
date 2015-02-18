package com.with;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseACL;
import com.parse.ParseInstallation;
import com.with.WithUser.UserType;

public class ParseApplication extends Application {

	private SharedObjects mSharedObjects;
	@Override
	public void onCreate() {
		super.onCreate();

		// Add your initialization code here
		Parse.initialize(this, "OzYWKFRMuZiSM42TCU9jvYhl5wlQohkkvQhJptfY",
				"O3gSqnIuGqPDSqCPgwvNp85bjaBNOIKB8B4kD3i9");
		ParseInstallation.getCurrentInstallation().saveInBackground();

		//ACL MAKES BUGS!!
		ParseACL defaultACL = new ParseACL();
		defaultACL.setPublicReadAccess(true);
		defaultACL.setPublicWriteAccess(true);
		ParseACL.setDefaultACL(defaultACL, true);

		mSharedObjects = SharedObjects.getInstance();
		Utils.getInstance().setContext(getApplicationContext());
		Utils.getInstance().CreateUniqueId();
		SharedObjects.getInstance().getmWithUser().setmType(UserType.TOURIST);

		
	}

}
