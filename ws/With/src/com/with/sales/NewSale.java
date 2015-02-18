package com.with.sales;

import com.parse.ParseGeoPoint;

public class NewSale 
{
	private String mStoreName;
	private String mTitle;
	private String mContent;
	private String mSite;
	private ParseGeoPoint mGeopoint;
	
	public NewSale(String mStoreName, String mTitle, String mContent, ParseGeoPoint mGeopoint, String mSite) 
	{
		this.mStoreName = mStoreName;
		this.mTitle = mTitle;
		this.mContent = mContent;
		this.mGeopoint = mGeopoint;
		this.mSite = mSite;
	}

	public String getmStoreName() 
	{
		return mStoreName;
	}

	public String getmTitle() 
	{
		return mTitle;
	}

	public String getmContent() 
	{
		return mContent;
	}

	public ParseGeoPoint getmGeopoint() 
	{
		return mGeopoint;
	}

	
	public String getmSite() 
	{
		return mSite;
	}

	@Override
	public String toString() 
	{
		return "NewSale [mStoreName=" + mStoreName + ", mTitle=" + mTitle
				+ ", mContent=" + mContent + ", mGeopoint=" + mGeopoint + "]";
	}
	
	
	
	
}
