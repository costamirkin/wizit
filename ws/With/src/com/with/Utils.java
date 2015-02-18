package com.with;

import android.content.Context;
import android.telephony.TelephonyManager;

public class Utils {
	protected final int USER_ID_LENGTH = 9;
	
	private Utils() { 
		CreateUniqueId();
	}
	
	private static Utils   instance = null;
	private static Context mContext  = null;
	
	public void setContext(Context context) {
		Utils.mContext = context;
	}

	protected String uniqueId  = null;
	protected String userId    = null;
	protected String mPassword = null;
	
	public static Utils getInstance() {
		if (instance == null) {
			instance = new Utils();
		}
		
		return instance;
	}

	protected void CreateUniqueId() {
		if (mContext != null) {
			TelephonyManager tManager = (TelephonyManager)mContext.getSystemService(Context.TELEPHONY_SERVICE);
			uniqueId = tManager.getDeviceId();
			
			userId   = "w" + uniqueId.substring(0,USER_ID_LENGTH);
		}
	}
	
	public String getUserId() {
		return userId;
	}

	public String getmPassword() {
		return mPassword;
	}

	public String getUniqueId() {
		return uniqueId;
	}
	
	public boolean stringEqual(String first, String second) {
		if (first == null) {
			if (second == null) {
				return true;
			}
			else {
				return false;
			}
		}
		else if (second == null) {
			return false;
		}
		return first.equals(second);
	}
	

}
