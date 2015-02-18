package com.with;

import java.util.Date;
import java.util.List;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.IBinder;
import android.os.SystemClock;
import android.util.Log;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;
import com.with.sales.NewSale;
import com.with.sales.SalesListener;

public class DataService extends Service implements Runnable {

	private Thread mUpdateThread = null;
	private boolean mShouldStop = false;

	private int mUpdateMeInterval = 60;
	private int mUpdateMeCounter = 60;

	private int mGetUsersInterval = 60;
	private int mGetUsersCounter = 60;

	private int mGetSalesInterval = 300;
	private int mGetSalesCounter = 300;

	private IPostListener mFriendsListener = null;

	// Flags and functions used in order to download data immediately
	// when user opens Listener Activity, like MAP
	private boolean mFriendsListenerResumed = false;

	public void ResumeFriendsListener() {
		mFriendsListenerResumed = true;
	}

	private boolean mSalesListenerResumed = false;

	public void ResumeSalesListener() {
		mSalesListenerResumed = true;
	}

	public void setmFriendsListener(IPostListener mFriendsListener) {
		this.mFriendsListener = mFriendsListener;
	}

	// Sales
	SalesListener mSalesListener = null;

	public void setmSalesListener(SalesListener mSalesListener) {
		this.mSalesListener = mSalesListener;
	}

	public void onCreate() {
		mUpdateThread = new Thread(this);
		mUpdateThread.start();
	}

	@Override
	public void onDestroy() {
		mShouldStop = true;
		super.onDestroy();

	}

	private void updateMeIfNeeded() {
		// If friends screen is not active, dont update me
		if (mFriendsListener == null) {
			return;
		}
		if (mUpdateMeCounter == mUpdateMeInterval) {
			mUpdateMeCounter = 0;
			ParseUser user = SharedObjects.getInstance().getmParseUser();
			// If location is not available yet exit
			if (SharedObjects.getInstance().getmLocationHandler()
					.getmLocation() == null) {
				return;
			}
			ParseGeoPoint Point = new ParseGeoPoint(SharedObjects.getInstance()
					.getmLocationHandler().getmLocation().getLatitude(),
					SharedObjects.getInstance().getmLocationHandler()
							.getmLocation().getLongitude());

			user.put("Location", Point);
			user.saveInBackground();
		} else {
			mUpdateMeCounter++;
		}
	}

	private void getUsersIfNeeded() {
		// If friends screen is not active, dont load users
		if (mFriendsListener == null) {
			return;
		}
		if (mGetUsersCounter == mGetUsersInterval) {
			mGetUsersCounter = 0;
			Date now = new Date();
			now.setTime(now.getTime() - 30 * 60 * 1000);
			ParseQuery<ParseUser> query = ParseUser.getQuery();
			// query.whereGreaterThan("updatedAt", now);
			// Prevent getting me from PARSE
			query.whereNotEqualTo("username", SharedObjects.getInstance()
					.getmParseUser().getUsername());
			// Get friends only
			query.whereEqualTo("userType", 1);

			// Clear friends old vector
			SharedObjects.getInstance().getmFriends().clear();
			query.findInBackground(new FindCallback<ParseUser>() {
				public void done(List<ParseUser> friends, ParseException e) {
					if (e == null) {
						for (int i = 0; i < friends.size(); i++) {
							WithUser user = new WithUser();
							user.setmName(friends.get(i).getUsername());
							ParseGeoPoint location = friends.get(i)
									.getParseGeoPoint("Location");
							if (location == null) {
								continue;
							}
							user.setmLat(location.getLatitude());
							user.setmLon(location.getLongitude());
							user.setmID(friends.get(i).getObjectId());
							user.setmEmail(friends.get(i).getEmail());
							SharedObjects.getInstance().getmFriends().add(user);
						}
						if (mFriendsListener != null) {
							mFriendsListener.updateItems();
						}
					} else {
					}
				}
			});
		} else {
			mGetUsersCounter++;
		}
	}

	private void getSalesIfNeeded() {
		ParseGeoPoint loc;

		// If sales screen is not displated, do not load sales
		if (mSalesListener == null) {
			return;
		}
		if (mGetSalesCounter == mGetSalesInterval) {
			mGetSalesCounter = 0;
			Date now = new Date();
			now.setTime(now.getTime() - 30 * 60 * 1000);
			Location location = SharedObjects.getInstance()
					.getmLocationHandler().getmLocation();
			if (SharedObjects.getInstance().getmLocationHandler()
					.getmLocation() == null) {
				return;
			}
			loc = new ParseGeoPoint(location.getLatitude(),
					location.getLongitude());

			ParseQuery<ParseObject> query = ParseQuery.getQuery("Sales");
			query.whereWithinKilometers("position", loc, 6);
			query.findInBackground(new FindCallback<ParseObject>() {
				public void done(List<ParseObject> Places, ParseException e) {
					if (e == null) {
						if (Places.size() > 0) {
							SharedObjects.getInstance().getmSales().clear();
						}

						for (int i = 0; i < Places.size(); i++) {
							String saleTitle = Places.get(i).getString(
									"saleTitle");
							String saleContent = Places.get(i).getString(
									"SaleContent");
							String placeName = Places.get(i).getString(
									"StoreTitle");
							String SiteName = Places.get(i).getString(
									"StoreSite");
							ParseGeoPoint placeLoc = Places.get(i)
									.getParseGeoPoint("position");

							SharedObjects
									.getInstance()
									.getmSales()
									.add(new NewSale(placeName, saleTitle,
											saleContent, placeLoc, SiteName));

							Log.i("Parse!!!", "Get " + placeName);
						}
						if (mSalesListener != null) {
							mSalesListener.UpdateSales();
						}
					} else {
						Log.i("Parse!!!", "Error: " + e.getMessage());
					}
				}
			});
		} else {
			mGetSalesCounter++;
		}
	}

	@Override
	public void run() {
		while (!mShouldStop) {
			if (mFriendsListener != null) {

				if (mFriendsListenerResumed) {
					mUpdateMeCounter = mUpdateMeInterval;
					mGetUsersCounter = mGetUsersInterval;
					mFriendsListenerResumed = false;
				}

				updateMeIfNeeded();
				getUsersIfNeeded();
			}
			
			if (mSalesListener != null) {
				if (mSalesListenerResumed) {
					mGetSalesCounter = mGetSalesInterval;
					mSalesListenerResumed = false;
				}

				getSalesIfNeeded();
			}
			SystemClock.sleep(1000);
		}
	}

	@Override
	public IBinder onBind(Intent intent) {
		MyBinder myBinder = new MyBinder();
		myBinder.ContainedService = this;
		Log.i("Service", "Service bind request");
		return myBinder;
	}

}
