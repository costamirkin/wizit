package com.with;

import android.os.Binder;

public class MyBinder extends Binder {// we will make some member in type
										// service so that it can return it
	public DataService ContainedService;

	public MyBinder() {
		// TODO Auto-generated constructor stub
	}

}
