package com.with.chat;

import java.util.ArrayList;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.parse.ParsePush;
import com.parse.ParseUser;
import com.with.FriendsActivity;
import com.with.MessagesInfo;
import com.with.R;
import com.with.SharedObjects;
import com.with.SplashActivity;
import com.with.StartActivity;

public class ChatWindow extends Activity {
	
	public static ChatWindow mThis = null;

	private String mClientNumber;
	private String message;
	private String VIEW_BY_ME = "me";
	private String VIEW_BY_SERVER = "client";
	EditText et_Msg;
	LinearLayout ll;
	ListView lvChat;
	MyChatAdapter adapter;

	private ArrayList<MessagesInfo> chatConversation;

	private JSONObject data;

	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chat_window);
		Intent i = getIntent();

		Bundle bundle = i.getExtras();
		if (bundle != null) {
			mClientNumber = i.getExtras().getString("Name");
		}

		chatConversation = new ArrayList<MessagesInfo>();
		
		for (int j = 0; j < SharedObjects.getInstance().getmSavedMessages().size(); j++) {
			chatConversation.add(new MessagesInfo(SharedObjects.getInstance().getmSavedMessages().get(j), VIEW_BY_SERVER));
		}
		SharedObjects.getInstance().getmSavedMessages().clear();

		// DB Connection
		lvChat = (ListView) findViewById(R.id.lvChat);
		lvChat.setDivider(null);

		adapter = new MyChatAdapter(ChatWindow.this, chatConversation);

		lvChat.setAdapter(adapter);

		ll = (LinearLayout) findViewById(R.id.lladd);
		et_Msg = (EditText) findViewById(R.id.et_txtMsg);

		Button b = (Button) findViewById(R.id.btnAdd);
		b.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				message = et_Msg.getText().toString();
				addMessageToView(message, VIEW_BY_ME);
				// TODO: this is patch, should be changed - the problem is when user
				// didn't started application and gets push the application crashes due to
				// parse user is null
				if (SharedObjects.getInstance().getmParseUser() == null) {
					// Check if user was already signed in
					ParseUser currentUser = ParseUser.getCurrentUser();
					if (currentUser != null) {
						SharedObjects.getInstance().setmParseUser(currentUser);
					} 
					else {
						return;
					}

				}
				sendMessage(message, SharedObjects.getInstance().getmParseUser().getUsername(), mClientNumber);
				et_Msg.setText("");
			}
		});

	}
	
	@Override
	protected void onResume() {
		mThis = this;
		super.onResume();
	}
	
	@Override
	protected void onPause() {
		mThis = null;
		super.onPause();
	}
	
	public void displayMessage(final String message) {
		runOnUiThread(new Runnable() {
			public void run() {
				addMessageToView(message, VIEW_BY_SERVER);

			}
		});
	}


	/**
	 * Method that send the message to parse with data.
	 * 
	 * @param message
	 * @param fromNumber
	 * @param toNumber
	 */
	private void sendMessage(String message, String fromNumber, String toNumber) {
		System.out.println("***" + fromNumber + "***" + toNumber);
		try {
			data = new JSONObject("{\"action\": \"com.with.CHAT\"," + "\"sender\": \"" + fromNumber + "\"," +
				      "\"message\": \"" + message + "\"}");
			ParsePush push1 = new ParsePush();
			push1.setChannel(toNumber);
			push1.setData(data);
			push1.sendInBackground();

		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}


	}

	// Specific adapter for chat window
	public class MyChatAdapter extends BaseAdapter {
		private ArrayList<MessagesInfo> stringArray;
		private Context context;

		public MyChatAdapter(Context _context, ArrayList<MessagesInfo> arr) {
			context = _context;
			stringArray = arr;
		}

		public int getCount() {
			return stringArray.size();
		}

		public Object getItem(int arg0) {
			return stringArray.get(arg0);
		}

		public long getItemId(int arg0) {
			return 0;
		}

		public View getView(int position, View v, ViewGroup parent) {
			LayoutInflater inflate = ((Activity) context).getLayoutInflater();
			View view = (View) inflate
					.inflate(R.layout.chat_listview_row, null);
			TextView tvChatMessage = (TextView) view
					.findViewById(R.id.tvChatMessage);
			RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) tvChatMessage
					.getLayoutParams();
			tvChatMessage.setGravity(Gravity.RIGHT);
			if (stringArray.get(position).getFrom().equals(VIEW_BY_ME)) {
				// Checks if message was by me than align to right
				tvChatMessage.setBackgroundResource(R.drawable.me);
				params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
			} else if (stringArray.get(position).getFrom()
					.equals(VIEW_BY_SERVER)) {// Checks if message from server
												// than align to left
				tvChatMessage.setBackgroundResource(R.drawable.client);
				params.addRule(RelativeLayout.ALIGN_PARENT_LEFT);
			}
			tvChatMessage.setLayoutParams(params);
			tvChatMessage.setText(stringArray.get(position).getMessage());
			return view;
		}
	}


	private void addMessageToView(String message, String source) {
		chatConversation.add(new MessagesInfo(message, source));// Add to array
																// message with
																// source.
		lvChat.invalidateViews(); // Updating list
		lvChat.post(new Runnable() {
	        @Override
	        public void run() {
	        	lvChat.setSelection(lvChat.getCount());
	        }
	    });
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chat, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.meet:
			Intent intent = new Intent(getApplicationContext(),	FriendsActivity.class);
			intent.putExtra("FriendName", mClientNumber);
			intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
			startActivity(intent);
			break;

		default:
			break;
		}
		return super.onOptionsItemSelected(item);
	}
}
