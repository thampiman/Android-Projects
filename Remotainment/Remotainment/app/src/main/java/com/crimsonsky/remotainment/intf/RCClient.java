package com.crimsonsky.remotainment.intf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.ArrayList;

public class RCClient implements Runnable {
	// Ports
	public static int SERVER_PORT = 1309;
	public static int CLIENT_PORT = 2005;
	
	// Requests
	public static String REQUEST_START = "start";
	public static String REQUEST_GET_HOSTNAME = "gethostname";
	public static String REQUEST_GET_MEDIA = "getmedia";
	public static String REQUEST_GET_MEDIA_REFRESH = "getmediarefresh";
		
	// Responses
	public static String RESPONSE_OK = "ok";
	
	// VLC Commands
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
		
	private ServerSocket serverSock = null;
	private Socket sock = null;
	private Socket clientSock = null;
	private PrintWriter writer = null;
	
	private static Thread clientThread = null;
	private static RCClient rcClient = null;
	
	private ArrayList<String> responseList = null;
	boolean responseReceived;
	
	private RCClient()
    {
		responseReceived = false;
		responseList = new ArrayList<String>();
    }

    public static RCClient getSingletonObject()
    {
    	if (rcClient == null) {
    		rcClient = new RCClient();
    	}
    	
    	return rcClient;
    }
	
	public void connect() {
		try {
			if (serverSock == null) {
				serverSock = new ServerSocket(CLIENT_PORT);
			}
			if ((clientThread == null) || (!clientThread.isAlive())) {
	    		clientThread = new Thread(rcClient);
				clientThread.start();
	    	}
			sock = new Socket();
			SocketAddress address = new InetSocketAddress("192.168.1.131", SERVER_PORT);
			sock.connect(address, 10000);
			writer = new PrintWriter(sock.getOutputStream());
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void disconnect() {
		try {
			sendRequest(VLC_QUIT);
			sock.close();
			sock.shutdownInput();
			sock.shutdownOutput();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void requestStart() {
		sendRequest(REQUEST_START);
	}
	
	public void requestHostname() {
		sendRequest(REQUEST_GET_HOSTNAME);
	}
	
	public void requestMedia() {
		sendRequest(REQUEST_GET_MEDIA);
	}
	
	public void requestMediaRefresh() {
		sendRequest(REQUEST_GET_MEDIA_REFRESH);
	}
	
	public void requestEnqueue(String filename) {
		sendRequest(VLC_ENQUEUE + " " + filename);
	}
	
	public void requestBasicVlcOperation(String operation) {
		sendRequest(operation);
	}
	
	private void sendRequest(String request) {
		clearResponseList();
		responseReceived = false;
		writer.println(request);
		writer.flush();
	}
	
	public ArrayList<String> getResponseList() {
		if (responseReceived)
			return responseList;
		
		return null;
	}
	
	public void clearResponseList() {
		synchronized (responseList) {
			responseList.clear();
		}
	}

	@Override
	public void run() {
		while (true) {
			try {
				clientSock = serverSock.accept();
					
				InputStreamReader streamReader = new InputStreamReader(clientSock.getInputStream());
				BufferedReader reader = new BufferedReader(streamReader);
					
				String command;
				while ((clientSock.isConnected()) && ((command = reader.readLine()) != null)) {
					synchronized (responseList) {
						responseList.add(command);
					}
					if (command.compareTo(RESPONSE_OK)==0) {
						responseReceived = true;
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
}
