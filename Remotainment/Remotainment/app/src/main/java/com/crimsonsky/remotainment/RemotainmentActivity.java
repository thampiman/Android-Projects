package com.crimsonsky.remotainment;

import android.app.Activity;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crimsonsky.remotainment.intf.*;
import com.crimsonsky.remotainment.tasks.ConnectTask;
import com.crimsonsky.remotainment.tasks.MediaListTask;
import com.crimsonsky.remotainment.tasks.VlcTask;

public class RemotainmentActivity extends Activity {
	
	private TextView txtStatus;
	private ImageButton btnPower, btnMedia, btnStop, btnPlay, btnPause, btnPrevious, btnNext;
	private ImageButton btnMute, btnVolup, btnVoldown;
	
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        
        txtStatus = (TextView) findViewById(R.id.txtStatus);
        txtStatus.setText(getResources().getString(R.string.power_on_to_begin));
        Typeface font = Typeface.createFromAsset(getAssets(), "BebasNeue.otf");
        txtStatus.setTypeface(font);
        
        btnPower 	= (ImageButton) findViewById(R.id.btnPower);
        btnMedia 	= (ImageButton) findViewById(R.id.btnMedia);
        btnStop 	= (ImageButton) findViewById(R.id.btnStop);
        btnPlay 	= (ImageButton) findViewById(R.id.btnPlay);
        btnPause 	= (ImageButton) findViewById(R.id.btnPause);
        btnPrevious = (ImageButton) findViewById(R.id.btnPrevious);
        btnNext 	= (ImageButton) findViewById(R.id.btnNext);
        btnMute 	= (ImageButton) findViewById(R.id.btnMute);
        btnVolup 	= (ImageButton) findViewById(R.id.btnVolup);
        btnVoldown 	= (ImageButton) findViewById(R.id.btnVoldown);
        
        btnPower.setBackgroundColor(Color.argb(255, 224, 224, 224));
        /* btnMedia.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnStop.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnPlay.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnPause.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnPrevious.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnNext.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnMute.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnVolup.setBackgroundColor(Color.argb(255, 224, 224, 224));
        btnVoldown.setBackgroundColor(Color.argb(255, 224, 224, 224)); */
    }
    
    /** Called when the user taps the power button */
    public void powerOn(View view) {
    	if (txtStatus.getText().toString().startsWith(getResources().getString(R.string.power_on_to_begin))) {
    		txtStatus.setText(getResources().getString(R.string.connecting) + "...");
        	ConnectTask task = new ConnectTask(this);
        	task.execute("connect");
    	} else {
    		RCClient.getSingletonObject().disconnect();
    		txtStatus.setText(getResources().getString(R.string.power_on_to_begin));
    		btnPower.setBackgroundColor(Color.argb(255, 224, 224, 224));
    	}
    }
    
    public void powerOnComplete(String status) {
    	txtStatus.setText(status);
    	btnPower.setBackgroundColor(Color.argb(255, 143, 255, 143));
    }
    
    /** Called when the user taps the media button */
    public void getMedia(View view) {
    	MediaListTask task = new MediaListTask(this);
    	task.execute("getmedia");
    }
    
    /** Called when the user taps the stop button */
    public void stop(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute("stop");
    }
    
    /** Called when the user taps the play button */
    public void play(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute(RCClient.VLC_PLAY);
    }
    
    /** Called when the user taps the pause button */
    public void pause(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute(RCClient.VLC_PAUSE);
    }
    
    /** Called when the user taps the previous button */
    public void previous(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute(RCClient.VLC_PREV);
    }
    
    /** Called when the user taps the next button */
    public void next(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute(RCClient.VLC_NEXT);
    }
    
    /** Called when the user taps the mute button */
    public void mute(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute("mute");
    }
    
    /** Called when the user taps the volup button */
    public void volUp(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute(RCClient.VLC_VOLUP);
    }
    
    /** Called when the user taps the voldown button */
    public void volDown(View view) {
    	VlcTask task = new VlcTask(this);
    	task.execute(RCClient.VLC_VOLDOWN);
    }
}