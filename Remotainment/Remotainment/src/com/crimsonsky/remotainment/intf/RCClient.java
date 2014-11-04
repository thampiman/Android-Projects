package com.crimsonsky.remotainment.intf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

import android.util.Log;

public class RCClient implements Runnable {
	// Ports
	public static int SERVER_PORT = 1309;
	public static int CLIENT_PORT = 2005;
	
	// Requests
	private static String REQUEST_GET_HOSTNAME = "gethostname";
	private static String REQUEST_GET_MEDIA = "getmedia";
	private static String REQUEST_GET_MEDIA_REFRESH = "getmediarefresh";
		
	// Responses
	private static String RESPONSE_OK = "ok";
		
	private ServerSocket serverSock;
	private Socket sock;
	private Socket clientSock;
	private PrintWriter writer;
	
	private static Thread clientThread;
	
	public RCClient() {
		try {
			serverSock = new ServerSocket(CLIENT_PORT);
			clientThread = new Thread();
			clientThread.start();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void connect() {
		try {
			if ((sock != null) && (sock.isConnected())) {
				sock.close();
				sock.shutdownOutput();
			} else {
				sock = new Socket("192.168.1.131", SERVER_PORT);
				writer = new PrintWriter(sock.getOutputStream());
				writer.println(REQUEST_GET_HOSTNAME);
				writer.flush();
				Log.w("Remotainment", "Server connection status: " + sock.isConnected());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public void run() {
		try {
			clientSock = serverSock.accept();
			
			System.out.println("Connection from Server Accepted!");
			
			InputStreamReader streamReader = new InputStreamReader(clientSock.getInputStream());
			BufferedReader reader = new BufferedReader(streamReader);
			
			String command;
			while ((command = reader.readLine()) != null) {
				Log.w("Remotainment", command);
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
