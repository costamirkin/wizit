package com.with.tourbuild;

import com.parse.ParseObject;

public class Guide {
	private String         mGuideName;
	private int            mGuideRate;
	private int            mRatesNumber;
	private ParseObject    mParseObject; //Tour object using for rating the guide
	public String getmGuideName() {
		return mGuideName;
	}
	public void setmGuideName(String mGuideName) {
		this.mGuideName = mGuideName;
	}
	public int getmGuideRate() {
		return mGuideRate;
	}
	public void setmGuideRate(int mGuideRate) {
		this.mGuideRate = mGuideRate;
	}
	public int getmRatesNumber() {
		return mRatesNumber;
	}
	public void setmRatesNumber(int mRatesNumber) {
		this.mRatesNumber = mRatesNumber;
	}
	public ParseObject getmParseObject() {
		return mParseObject;
	}
	public void setmParseObject(ParseObject mParseObject) {
		this.mParseObject = mParseObject;
	}

}
