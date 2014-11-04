package com.crimsonsky.remotainment.tasks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.crimsonsky.remotainment.RemotainmentActivity;
import com.crimsonsky.remotainment.intf.RCClient;

import android.os.AsyncTask;

public class VlcTask extends AsyncTask<String, Integer, String>  {

	private WeakReference<RemotainmentActivity> remotainmentParentActivity = null;
	
	public VlcTask(RemotainmentActivity parentActivity) {
		remotainmentParentActivity  = new WeakReference<RemotainmentActivity>(parentActivity);
	}
	
	@Override
	protected String doInBackground(String... params) {
		RCClient.getSingletonObject().requestBasicVlcOperation(params[0]);
		
		return null;
	}

	@Override
	protected void onPostExecute(String hostname) {
		
	}
}
