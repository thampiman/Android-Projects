package com.crimsonsky.remotainment.intf;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.BindException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

import com.crimsonsky.remotainment.util.Encrypter;
import com.crimsonsky.remotainment.util.OperatingSystem;

public class RCServer implements Runnable {
	/** Ports */
	public static int SERVER_PORT = 1309;
	public static int CLIENT_PORT = 2005;
	
	/** Port for Android Emulator */
	// Steps to setup server on emulator:
	// 1. Run emulator from eclipse or command line
	// 2. From command line, telnet localhost 5554
	// 3. Then add port redirection, redir add tcp:2000:2005
	// For more info, see http://developer.android.com/tools/devices/emulator.html#emulatornetworking
	public static int CLIENT_PORT_EMULATOR = 2000; 
	
	/** Requests */
	private static String REQUEST_GET_HOSTNAME = "gethostname";
	private static String REQUEST_GET_MEDIA = "getmedia";
	private static String REQUEST_GET_MEDIA_REFRESH = "getmediarefresh";
	
	/** Responses */
	private static String RESPONSE_AUTH_FAILED = "authfailed";
	private static String RESPONSE_OK = "ok";
	
	private ServerSocket serverSock;
	private Socket sock;
	private Socket clientSock;
	private PrintWriter writer;
	private VLCControl vlcControl;
	
	private ArrayList<String> entertainmentFoldersList;
	private ArrayList<String> mediaFileFormatList;
	private ArrayList<String> mediaFilesList;
	
	private long startTime;
	private long stopTime;
	
	private boolean refreshMediaFilesList;

	private Encrypter encrypter;
	
	private enum ServerState {LOGIN_REQUIRED, LOGIN_SUCCESSFUL};
	private ServerState serverState;
	
	private String serverPassword;
	
	public RCServer(ArrayList<String> foldersList) throws BindException
	{
		try {
			serverPassword = "";
			
			serverSock = new ServerSocket(SERVER_PORT);
			vlcControl = new VLCControl();
			
			entertainmentFoldersList = new ArrayList<String>();
			for (int i=0; i<foldersList.size(); i++) {
				entertainmentFoldersList.add(foldersList.get(i));
			}
			
			mediaFileFormatList = new ArrayList<String>();
			setMediaFileFormatList();
			
			mediaFilesList = new ArrayList<String>();
			refreshMediaFilesList = true;
			
			serverState = ServerState.LOGIN_REQUIRED;
			
			encrypter = new Encrypter();
		} catch (BindException bex) {
			throw bex;
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	public void setPassword(String password) 
	{
		serverPassword = password;
		System.out.println("Setting server password = " + serverPassword);
	}
	
	private void setMediaFileFormatList() 
	{
		mediaFileFormatList.add("*.avi");
		mediaFileFormatList.add("*.asf");
		mediaFileFormatList.add("*.wmv");
		mediaFileFormatList.add("*.wma");
		mediaFileFormatList.add("*.mp4");
		mediaFileFormatList.add("*.mp3");
		mediaFileFormatList.add("*.mov");
		mediaFileFormatList.add("*.3gp");
		mediaFileFormatList.add("*.ogg");
		mediaFileFormatList.add("*.ogm");
		mediaFileFormatList.add("*.mkv");
		mediaFileFormatList.add("*.rm");
		mediaFileFormatList.add("*.wav");
		mediaFileFormatList.add("*.flac");
		mediaFileFormatList.add("*.flv");
		mediaFileFormatList.add("*.mxf");
	}
	
	public void setEntertainmentFoldersList(ArrayList<String> foldersList)
	{
		synchronized(entertainmentFoldersList) {
			entertainmentFoldersList.clear();
			for (int i=0; i<foldersList.size(); i++) {
				entertainmentFoldersList.add(foldersList.get(i));
			}
			refreshMediaFilesList = true;
		}
	}
	
	public void closeSockets() 
	{
		try {
			sock.close();
			clientSock.close();
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
				System.out.println("Inet Address: " + sock.getInetAddress());
				
				clientSock = new Socket(sock.getInetAddress(), CLIENT_PORT);
				
				/** Uncomment this line for Android emulator and comment above line */
				// clientSock = new Socket("127.0.0.1", CLIENT_PORT_EMULATOR);
				
				System.out.println("Client Connected!");
				
				// Input stream to get requests
				InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
				BufferedReader reader = new BufferedReader(streamReader);
				
				// Output stream to send responses
				writer = new PrintWriter(clientSock.getOutputStream());
					
				String command;
				while ((!sock.isClosed()) && ((command = reader.readLine()) != null)) {
					if (serverState == ServerState.LOGIN_REQUIRED) {
						if (isValidUser(command)) {
							serverState = ServerState.LOGIN_SUCCESSFUL;
							writer.println(RESPONSE_OK);
							writer.flush();
						} else {
							writer.println(RESPONSE_AUTH_FAILED);
							writer.flush();
							reader.close();
							sock.close();
						}
					} else {
						if (command.trim().length() > 0) {
							startTime = System.currentTimeMillis();
							decodeAndRespond(command);
							stopTime = System.currentTimeMillis();
							System.out.println("Command = " + command + "; Processing Time = " + Long.toString(stopTime - startTime));
						}
					}
				}
				
				serverState = ServerState.LOGIN_REQUIRED;
				clientSock.close();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private boolean isValidUser(String encryptedPassword) 
	{
		if (encryptedPassword.trim().length() <= 0)
			return false;
		
		String decryptedPassword = "";
		try { 
			decryptedPassword = encrypter.decrypt(encryptedPassword);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (decryptedPassword.compareTo(serverPassword) == 0) 
			return true;
		
		return false;
	}

	private void decodeAndRespond(String command)
	{
		try {
			if (command.compareTo(REQUEST_GET_HOSTNAME)==0) {
				writer.println(InetAddress.getLocalHost().getHostName());
			} else if (command.compareTo(REQUEST_GET_MEDIA)==0) {
				sendMediaFilesList(false);
			} else if (command.compareTo(REQUEST_GET_MEDIA_REFRESH)==0) {
				sendMediaFilesList(true);
			} else {
				vlcControl.sendCommand(command);
				
				if (command.compareTo(VLCControl.VLC_QUIT) == 0) {
					vlcControl.disconnect();
				}
			}
			
			writer.println(RESPONSE_OK);
			writer.flush();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void sendMediaFilesList(boolean overrideRefresh)
	{
		Runtime r = Runtime.getRuntime();
		synchronized(entertainmentFoldersList) {
			try {
				if (refreshMediaFilesList || overrideRefresh) {
					mediaFilesList.clear();
					for (int i=0; i<entertainmentFoldersList.size(); i++) {
						for (int j=0; j<mediaFileFormatList.size(); j++) {
							String dirCommand = "";
							
							if (OperatingSystem.isWindows()) {
								dirCommand = "cmd.exe /k dir /s/b/a:-h \"" + entertainmentFoldersList.get(i) 
										 + "\\" + mediaFileFormatList.get(j) + "\"";
							}
							
							Process p = r.exec(dirCommand);
							BufferedReader br = new BufferedReader(new InputStreamReader(p.getInputStream()));
							String file = null;
							while (((file = br.readLine()) != null) && (file.trim().length() > 0)) {
								mediaFilesList.add(file);
								writer.println(file);
							}
						}
					}
					
					refreshMediaFilesList = false;
				} else {
					for (int i=0;i<mediaFilesList.size(); i++) {
						writer.println(mediaFilesList.get(i));
					}
				}
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		} 
	}
}
