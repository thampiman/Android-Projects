package com.crimsonsky.remotainment;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.crimsonsky.remotainment.intf.*;

public class RemotainmentActivity extends Activity {
	
	private TextView txtStatus;
	private ImageButton btnPower, btnMedia, btnStop, btnPlay, btnPause, btnPrevious, btnNext;
	private ImageButton btnMute, btnVolup, btnVoldown;
	private RCClient rcClient;
	
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
        
        rcClient = new RCClient();
    }
    
    /** Called when the user taps the power button */
    public void powerOn(View view) {
    	txtStatus.setText(getResources().getString(R.string.connecting) + "...");
    	rcClient.connect();
    	txtStatus.setText(getResources().getString(R.string.connected_to) + " AJAY-PC");
    }
    
    /** Called when the user taps the media button */
    public void getMedia(View view) {
    	
    }
    
    /** Called when the user taps the stop button */
    public void stop(View view) {
    	
    }
    
    /** Called when the user taps the play button */
    public void play(View view) {
    	
    }
    
    /** Called when the user taps the pause button */
    public void pause(View view) {
    	
    }
    
    /** Called when the user taps the previous button */
    public void previous(View view) {
    	
    }
    
    /** Called when the user taps the next button */
    public void next(View view) {
    	
    }
    
    /** Called when the user taps the mute button */
    public void mute(View view) {
    	
    }
    
    /** Called when the user taps the volup button */
    public void volUp(View view) {
    	
    }
    
    /** Called when the user taps the voldown button */
    public void volDown(View view) {
    	
    }
}