package com.with;

import java.util.Iterator;
import java.util.Vector;

import com.parse.ParseUser;
import com.with.sales.NewSale;


public class SharedObjects  {
	
	private static SharedObjects instance = null;
	
	private SharedObjects() {
		mWithUser      = new WithUser();
		mFriends       = new Vector<WithUser>();
		mSavedMessages = new Vector<String>();
		mSales         = new Vector<NewSale>();
	}

	public WithUser getmWithUser() {
		return mWithUser;
	}

	public void setmWithUser(WithUser mWithUser) {
		this.mWithUser = mWithUser;
	}

	public static SharedObjects getInstance()
	{
		if (instance == null)
			instance = new SharedObjects();
		return instance;
	}

	private ParseUser mParseUser;
	private WithUser  mWithUser;

	
	// Location based issues
	
	protected LocationHandler mLocationHandler;

	public LocationHandler getmLocationHandler() {
		return mLocationHandler;
	}

	public void setmLocationHandler(LocationHandler mLocationHandler) {
		this.mLocationHandler = mLocationHandler;
	}

	public ParseUser getmParseUser() {
		return mParseUser;
	}

	public void setmParseUser(ParseUser mParseUser) {
		this.mParseUser = mParseUser;
	}


	// Friends
	private Vector<WithUser> mFriends;

	public Vector<WithUser> getmFriends() {
		return mFriends;
	}

	public void setmFriends(Vector<WithUser> mFriends) {
		this.mFriends = mFriends;
	}
	
	WithUser findUserByName(String name) {
		for (Iterator iterator = mFriends.iterator(); iterator.hasNext();) {
			WithUser user = (WithUser) iterator.next();
			if (user.getmName().equals(name)) {
				return user;
			}
		}
		return null;
	}
	
	// Received message
	private String  mMessageSender;
	// Messages which were sent before user opened chat window
	Vector<String>  mSavedMessages; 

	public Vector<String> getmSavedMessages() {
		return mSavedMessages;
	}

	public String getmMessageSender() {
		return mMessageSender;
	}

	public void setmMessageSender(String mMessageSender) {
		this.mMessageSender = mMessageSender;
	}


	// Sales
	private Vector<NewSale> mSales;

	public Vector<NewSale> getmSales() {
		return mSales;
	}


	
}
