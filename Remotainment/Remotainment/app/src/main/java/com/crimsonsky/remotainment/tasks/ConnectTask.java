package com.crimsonsky.remotainment.tasks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.crimsonsky.remotainment.RemotainmentActivity;
import com.crimsonsky.remotainment.intf.RCClient;

import android.os.AsyncTask;

public class ConnectTask extends AsyncTask<String, Integer, String> {

	private WeakReference<RemotainmentActivity> remotainmentParentActivity = null;
	
	public ConnectTask(RemotainmentActivity parentActivity) {
		remotainmentParentActivity  = new WeakReference<RemotainmentActivity>(parentActivity);
	}
	
	@Override
	protected String doInBackground(String... arg0) {
		RCClient.getSingletonObject().connect();
		
		RCClient.getSingletonObject().requestHostname();
		ArrayList<String> responseList = null;
		do {
			responseList = RCClient.getSingletonObject().getResponseList();
		} while (responseList == null);
		String hostname = responseList.get(0);
		
		RCClient.getSingletonObject().requestStart();
		responseList = null;
		do {
			responseList = RCClient.getSingletonObject().getResponseList();
		} while (responseList == null);
		
		return hostname;
	}

	@Override
	protected void onPostExecute(String hostname) {
		remotainmentParentActivity.get().powerOnComplete("Connected to " + hostname);
	}
}
