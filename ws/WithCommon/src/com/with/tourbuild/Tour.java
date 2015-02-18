package com.with.tourbuild;

import java.util.Vector;

import com.parse.ParseObject;

public class Tour {
	private Vector<Poi>    mPois;
	private int            mTourType;
	private String         mTourDescription;
	private String         mTourName;
	private String         mGuideName;
	private String         mTourId;
	private int            mTourRate;
	private int            mRatesNumber;
	private ParseObject    mParseObject; //Tour object using for rating the tour


	public ParseObject getmParseObject() {
		return mParseObject;
	}

	public void setmParseObject(ParseObject mParseObject) {
		this.mParseObject = mParseObject;
	}

	public int getmTourRate() {
		return mTourRate;
	}

	public void setmTourRate(int mTourRate) {
		this.mTourRate = mTourRate;
	}

	public int getmRatesNumber() {
		return mRatesNumber;
	}

	public void setmRatesNumber(int mRatesNumber) {
		this.mRatesNumber = mRatesNumber;
	}

	public String getmTourId() {
		return mTourId;
	}

	public void setmTourId(String mTourId) {
		this.mTourId = mTourId;
	}

	public String getmGuideName() {
		return mGuideName;
	}

	public void setmGuideName(String mGuideName) {
		this.mGuideName = mGuideName;
	}

	public Tour() {
		mPois = new Vector<Poi>();
		mTourType        = 1;
		mTourDescription = null;
		mTourName        = null;
		mGuideName       = null;
	}
	
	public int getmTourType() {
		return mTourType;
	}
	public void setmTourType(int mTourType) {
		this.mTourType = mTourType;
	}
	public String getmTourDescription() {
		return mTourDescription;
	}
	public void setmTourDescription(String mTourDescription) {
		this.mTourDescription = mTourDescription;
	}
	public String getmTourName() {
		return mTourName;
	}
	public void setmTourName(String mTourName) {
		this.mTourName = mTourName;
	}
	public Vector<Poi> getmPois() {
		return mPois;
	}

}
