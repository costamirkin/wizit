package com.with;

public class WithUser {
	public enum UserType {
		TOURIST, GUIDE
	};

	private String    mName;
	private String    mEmail;
	private String    mDesc;
	private int       mLang;
	private String    mID;
	private UserType  mType;
	private double    mLat;
	private double    mLon;
	
	public WithUser() {
		
	}
	
	public String getmEmail() {
		return mEmail;
	}

	public void setmEmail(String mEmail) {
		this.mEmail = mEmail;
	}
	public String getmName() {
		return mName;
	}
	public void setmName(String mName) {
		this.mName = mName;
	}
	public String getmDesc() {
		return mDesc;
	}
	public void setmDesc(String mDesc) {
		this.mDesc = mDesc;
	}
	public int getmLang() {
		return mLang;
	}
	public void setmLang(int mLang) {
		this.mLang = mLang;
	}
	public String getmID() {
		return mID;
	}
	public void setmID(String mID) {
		this.mID = mID;
	}
	public UserType getmType() {
		return mType;
	}
	public void setmType(UserType mType) {
		this.mType = mType;
	}
	public double getmLat() {
		return mLat;
	}
	public void setmLat(double mLat) {
		this.mLat = mLat;
	}
	public double getmLon() {
		return mLon;
	}
	public void setmLon(double mLon) {
		this.mLon = mLon;
	}


}
