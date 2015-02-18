package com.with.sales;

import java.util.Vector;


public class GeoPointVector 
{
	private Vector<NewSale> mVector;
	private static GeoPointVector sInstanse = null;
	

	public static GeoPointVector getInstance()
	{
		if(sInstanse == null)
		{
			sInstanse = new GeoPointVector();
		}
			return sInstanse;
	}
	
	public Vector<NewSale> getmVector() 
	{
		return mVector;
	}


	private GeoPointVector() 
	{
		mVector = new Vector<NewSale>();
	}
}
