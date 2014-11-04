package com.crimsonsky.remotainment.tasks;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import com.crimsonsky.remotainment.RemotainmentActivity;
import com.crimsonsky.remotainment.intf.RCClient;

import android.os.AsyncTask;

public class MediaListTask extends AsyncTask<String, Integer, String> {

	private WeakReference<RemotainmentActivity> remotainmentParentActivity = null;
	
	public MediaListTask(RemotainmentActivity parentActivity) {
		remotainmentParentActivity  = new WeakReference<RemotainmentActivity>(parentActivity);
	}
	
	@Override
	protected String doInBackground(String... params) {
		RCClient.getSingletonObject().requestEnqueue("\"D:\\Videos\\TV Shows\\Big Bang Theory\\Season 06\\The.Big.Bang.Theory.S06E01.HDTV.x264-LOL.mp4\"");
		RCClient.getSingletonObject().requestEnqueue("\"G:\\Videos\\TV Shows\\Top Gear\\Top Gear - [11x05] - 2008.07.20 [RiVER].avi\"");
		RCClient.getSingletonObject().requestEnqueue("\"G:\\Videos\\TV Shows\\The Daily Show\\The.Daily.Show.09.16.2008.DSR.XviD-iHT.[VTV].avi\"");
		
		return null;
	}

	@Override
	protected void onPostExecute(String hostname) {
		
	}
}
