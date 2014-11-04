package com.crimsonsky.remotainment.intf;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

import org.apache.commons.net.telnet.*;
import com.crimsonsky.remotainment.util.WinRegistry;

public class VLCControl extends TelnetClient {

	public static final int VLC_PORT = 1005;
	
	public static final String VLC_ADD = "add";
	public static final String VLC_ENQUEUE = "enqueue";
	public static final String VLC_IS_PLAYING = "is_playing";
	public static final String VLC_PLAY = "play";
	public static final String VLC_PAUSE = "pause";
	public static final String VLC_NEXT = "next";
	public static final String VLC_PREV = "prev";
	public static final String VLC_VOLUP = "volup 2";
	public static final String VLC_VOLDOWN = "voldown 2";
	public static final String VLC_FULLSCREEN = "fullscreen";
	public static final String VLC_QUIT = "quit";
	
	public static final String VLC_IS_PLAYING_YES = "1";
	public static final String VLC_IS_PLAYING_NO = "0";
	
	public void connect() throws IOException 
	{
		if (isVlcRunning()) {
			return;
		}
		
		if (isConnected()) {
			disconnect();
		}
		
		// Start VLC if not running
		startVlc();
		while (!isVlcRunning()) {
			// Do nothing but wait for VLC to run and open RC port
			try {
				Thread.sleep(100);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
		
		connect("localhost", VLC_PORT);
	}
	
	public boolean isVlcRunning() 
	{
		boolean vlcRunning = false;
		
		try { 
			Socket sock = new Socket();
			sock.connect(new InetSocketAddress("localhost", VLC_PORT));
			vlcRunning = sock.isConnected();
			sock.close();
		} catch (Exception ex) {
			vlcRunning = false;
		}
		
		return vlcRunning;
	}
	
	public String sendCommand(String s) throws IOException 
	{
		connect();
		if (s == null) {
			return null;
		}
		if (!s.endsWith("\n")) {
			s = s + "\n";
		}
		getOutputStream().write(s.getBytes());
		getOutputStream().flush();
		
		return s;
	}
	
	public void startVlc()
	{
		try {
			String vlcDir = WinRegistry.readString (
					WinRegistry.HKEY_LOCAL_MACHINE,  //HKEY
					"SOFTWARE\\VideoLAN\\VLC",       //Key
					"InstallDir");                   //ValueName
			String vlcStartCmd = "\""+ vlcDir + "\\vlc\" --rc-host=localhost:" + Integer.toString(VLC_PORT) 
								 + " --extraintf rc -f";
			Runtime.getRuntime().exec(vlcStartCmd);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
