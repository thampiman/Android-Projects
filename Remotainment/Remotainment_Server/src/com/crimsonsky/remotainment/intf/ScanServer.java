package com.crimsonsky.remotainment.intf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

public class ScanServer implements Runnable {
	/** Ports */
	public static int SERVER_PORT = 2404;
	public static int CLIENT_PORT = 2312;
	
	/** Port for Android Emulator */
	// Steps to setup server on emulator:
	// 1. Run emulator from eclipse or command line
	// 2. From command line, telnet localhost 5554
	// 3. Then add port redirection, redir add tcp:2300:2312
	// For more info, see http://developer.android.com/tools/devices/emulator.html#emulatornetworking
	public static int CLIENT_PORT_EMULATOR = 2300; 
	
	/** Requests */
	private static String REQUEST_GET_HOSTNAME = "gethostname";
	
	/** Responses */
	private static String RESPONSE_OK = "ok";
	
	private ServerSocket serverSock;
	private Socket sock;
	private Socket clientSock;
	private PrintWriter writer;
	
	public ScanServer() throws BindException
	{
		try {
			serverSock = new ServerSocket(SERVER_PORT);
		} catch (BindException bex) {
			throw bex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Override
	public void run() 
	{
		while (true) {
			try {
				sock = serverSock.accept();
				System.out.println("Scan Inet Address: " + sock.getInetAddress());
				
				clientSock = new Socket(sock.getInetAddress(), CLIENT_PORT);
				// Uncomment this line for Android emulator and comment above line
				// clientSock = new Socket("127.0.0.1", CLIENT_PORT_EMULATOR);
				
				System.out.println("Scan Client Connected!");
				
				// Input stream to get requests
				InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
				BufferedReader reader = new BufferedReader(streamReader);
				
				// Output stream to send responses
				writer = new PrintWriter(clientSock.getOutputStream());
					
				String command;
				while ((sock.isConnected()) && ((command = reader.readLine()) != null)) {
					decodeAndRespond(command);
				}
				
				clientSock.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}

	private void decodeAndRespond(String command)
	{
		try {
			if (command.compareTo(REQUEST_GET_HOSTNAME)==0) {
				writer.println(InetAddress.getLocalHost().getHostName());
			} 
			writer.println(RESPONSE_OK);
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
