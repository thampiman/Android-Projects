package com.crimsonsky.remotainment.ui;

import java.awt.EventQueue;
import java.awt.Image;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.Toolkit;
import java.awt.TrayIcon;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JSeparator;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.event.*;
import java.io.*;
import java.net.BindException;
import java.util.ArrayList;

import com.crimsonsky.remotainment.intf.RCServer;
import com.crimsonsky.remotainment.intf.ScanServer;
import com.crimsonsky.remotainment.util.Encrypter;
import com.crimsonsky.remotainment.util.OperatingSystem;

public class MainApplication {
	private static String REMOTAINMENT_FOLDER = "Remotainment";
	private static String ENTERTAINMENT_FILE = "Entertainment_Folders.txt";
	private static String PASSWORD_FILE = "Settings.txt";
	private static String OS_NOT_SUPPORTED = "OS Currently Not Supported";
	private static String HIDE_IN_SYSTEM_TRAY = "Hide in System Tray";
	
	private JFrame frmRemotainmentServer;
	private JButton btnEntertainmentFolders;
	private JButton btnRunInBackground;
	private JButton btnVlc;
	private JButton btnStart;
	
	private ArrayList<String> entertainmentFoldersList;
	private FoldersDialog dlgFolders;
	private PasswordDialog dlgPassword;
	
	private ScanServer scanServer;
	private Thread scanServerThread;
	
	private RCServer rcServer;
	private Thread rcServerThread;
	
	private SystemTray tray;
	private TrayIcon trayIcon;
	private MenuItem trayIconMenuItem;
	
	private String serverPassword;
	private Encrypter encrypter;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) 
	{
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					new MainApplication();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public MainApplication() 
	{
		// Load Settings Folder
		try {
			loadFolder();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		// Initialise UI Components
		boolean successfulInit = initialize();
		if (!successfulInit) {
			frmRemotainmentServer.setVisible(false);
		} else {
			frmRemotainmentServer.setVisible(true);
			
			// Operating System Checks
			osChecks();
		}
	}

	/**
	 * Initialise the contents of the frame.
	 */
	private boolean initialize() 
	{
		dlgFolders = new FoldersDialog(entertainmentFoldersList);
		dlgPassword = new PasswordDialog();
		
		frmRemotainmentServer = new JFrame();
		frmRemotainmentServer.setResizable(false);
		frmRemotainmentServer.setTitle("Remotainment Server");
		frmRemotainmentServer.setBounds(100, 100, 282, 294);
		frmRemotainmentServer.getContentPane().setLayout(null);
		
		try {
			scanServer = new ScanServer();
			rcServer = new RCServer(entertainmentFoldersList);
		} catch (BindException bex) {
			JOptionPane.showMessageDialog(null, "Another instance of the server application seems to be running. Please close it or use that instance instead.", "Remotainment server already running", JOptionPane.INFORMATION_MESSAGE);
			return false;
		}
		scanServerThread = new Thread(scanServer);
		rcServerThread = new Thread(rcServer);
		
		try {
			encrypter = new Encrypter();
			serverPassword = "";
			loadPassword();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		if (SystemTray.isSupported()) {
			tray = SystemTray.getSystemTray();
	        Image image = Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/img/remotainment_low.png"));
	        PopupMenu popup = new PopupMenu();
	        trayIconMenuItem = new MenuItem("Restore");
	        popup.add(trayIconMenuItem);
	        trayIcon = new TrayIcon(image, "Remotainment Server", popup);
		} else {
			tray = null;
		}
		
		try {
			Image img = Toolkit.getDefaultToolkit().getImage(getClass().getResource("res/img/remotainment.png"));
			frmRemotainmentServer.setIconImage(img);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		
		frmRemotainmentServer.addWindowListener(
				new WindowListener() {
					public void windowActivated(WindowEvent we) {
						
					}
					public void windowIconified(WindowEvent we) {
						
					}
					public void windowOpened(WindowEvent we) {
						
					}
					public void windowDeactivated(WindowEvent we) {
						
					}
					public void windowClosing(WindowEvent we) {
						try {
							savePassword();
							saveFolder();
						} catch (IOException ex) {
							ex.printStackTrace();
						}
						System.exit(0);
					}
					public void windowClosed(WindowEvent we) {
						
					}
					public void windowDeiconified(WindowEvent we) {
						
					}
				}
		);
		
		btnEntertainmentFolders = new JButton("Folders");
		btnEntertainmentFolders.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				setFolders();
			}
		});
		btnEntertainmentFolders.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnEntertainmentFolders.setBounds(33, 125, 206, 23);
		frmRemotainmentServer.getContentPane().add(btnEntertainmentFolders);
		
		JLabel lblPlayerExpectedFor = new JLabel("Media Player:");
		lblPlayerExpectedFor.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPlayerExpectedFor.setHorizontalAlignment(SwingConstants.LEFT);
		lblPlayerExpectedFor.setBounds(46, 182, 101, 23);
		frmRemotainmentServer.getContentPane().add(lblPlayerExpectedFor);
		
		btnRunInBackground = new JButton(HIDE_IN_SYSTEM_TRAY);
		if (SystemTray.isSupported()) {
			btnRunInBackground.setEnabled(true);
		} else {
			btnRunInBackground.setEnabled(false);
		}
		btnRunInBackground.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					if (btnRunInBackground.getText().compareTo(OS_NOT_SUPPORTED) == 0) {
						try { 
							Runtime.getRuntime().exec("cmd /c start https://github.com/thampiman/Remotainment_Server"); 
						} 
						catch(IOException e1) { 
							e1.printStackTrace();
						} 
					} else {
						ActionListener listener = new ActionListener() {
					    	public void actionPerformed(ActionEvent e) {
					    		frmRemotainmentServer.setVisible(true);
					            tray.remove(trayIcon);
					    	}
					    };
					    trayIconMenuItem.addActionListener(listener);
					    trayIcon.addActionListener(listener);
					    tray.add(trayIcon);
					    frmRemotainmentServer.setVisible(false); 
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnRunInBackground.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnRunInBackground.setBounds(33, 229, 206, 23);
		frmRemotainmentServer.getContentPane().add(btnRunInBackground);
		
		JSeparator separator = new JSeparator();
		separator.setBounds(45, 22, 221, 2);
		frmRemotainmentServer.getContentPane().add(separator);
		
		btnVlc = new JButton("VLC");
		btnVlc.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try { 
					Runtime.getRuntime().exec("cmd /c start http://www.videolan.org/vlc/index.html"); 
				} 
				catch(IOException e1) { 
					e1.printStackTrace(); 
				} 
			}
		});
		btnVlc.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnVlc.setBounds(125, 182, 114, 23);
		frmRemotainmentServer.getContentPane().add(btnVlc);
		
		JLabel lblPlayerExpectedFor_1 = new JLabel("Server");
		lblPlayerExpectedFor_1.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPlayerExpectedFor_1.setBounds(10, 11, 47, 23);
		frmRemotainmentServer.getContentPane().add(lblPlayerExpectedFor_1);
		
		btnStart = new JButton("Start");
		btnStart.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					// Start server only after user has set password
					if (serverPassword.length() == 0) {
						setPassword();
					}
					// Start server only after user has added folders to share with client
					if (dlgFolders.getEntertainmentFolders().size() <= 0) {
						setFolders();
					}
					if (btnStart.getText().compareTo("Start")==0) {
						if (!scanServerThread.isAlive())
							scanServerThread.start();
						
						if (!rcServerThread.isAlive())
							rcServerThread.start();
						
						btnStart.setText("Stop");
					} else {
						rcServer.closeSockets();
						btnStart.setText("Start");
					}
				} catch (Exception ex) {
					ex.printStackTrace();
				}
			}
		});
		btnStart.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnStart.setBounds(33, 35, 206, 23);
		frmRemotainmentServer.getContentPane().add(btnStart);
		
		JLabel lblEntertainment = new JLabel("Entertainment");
		lblEntertainment.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblEntertainment.setBounds(10, 102, 81, 23);
		frmRemotainmentServer.getContentPane().add(lblEntertainment);
		
		JSeparator separator_1 = new JSeparator();
		separator_1.setBounds(82, 112, 184, 2);
		frmRemotainmentServer.getContentPane().add(separator_1);
		
		JLabel lblPrerequisiteSoftware = new JLabel("Pre-Requisite Software");
		lblPrerequisiteSoftware.setFont(new Font("Tahoma", Font.PLAIN, 11));
		lblPrerequisiteSoftware.setBounds(10, 159, 126, 23);
		frmRemotainmentServer.getContentPane().add(lblPrerequisiteSoftware);
		
		JSeparator separator_2 = new JSeparator();
		separator_2.setBounds(125, 169, 141, 2);
		frmRemotainmentServer.getContentPane().add(separator_2);
		
		JSeparator separator_3 = new JSeparator();
		separator_3.setBounds(10, 216, 255, 2);
		frmRemotainmentServer.getContentPane().add(separator_3);
		
		JButton btnSetPassword = new JButton("Set Password");
		btnSetPassword.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				setPassword();
			}
		});
		btnSetPassword.setFont(new Font("Tahoma", Font.PLAIN, 11));
		btnSetPassword.setBounds(33, 68, 206, 23);
		frmRemotainmentServer.getContentPane().add(btnSetPassword);
		
		return true;
	}
	
	private void osChecks() 
	{
		if (!OperatingSystem.isWindows()) {
			btnStart.setEnabled(false);
			btnEntertainmentFolders.setEnabled(false);
			btnVlc.setEnabled(false);
			btnRunInBackground.setText(OS_NOT_SUPPORTED);
		}
	}
	
	private void setPassword() 
	{
		dlgPassword.setVisible(true);
		serverPassword = dlgPassword.getPassword();
		rcServer.setPassword(serverPassword);
	}
	
	private void setFolders()
	{
		dlgFolders.setVisible(true);
		rcServer.setEntertainmentFoldersList(dlgFolders.getEntertainmentFolders());
	}
	
	private void loadPassword() throws IOException 
	{
		// Check if REMOTAINMENT_FOLDER exists
		String remotainmentFolderPath = System.getProperty("user.home") + "\\" + REMOTAINMENT_FOLDER;
		File remotainmentFolder = new File(remotainmentFolderPath);
		if (!remotainmentFolder.exists()) {
			remotainmentFolder.mkdir();
		}
		
		String passwordFilename = System.getProperty("user.home") + "\\" + REMOTAINMENT_FOLDER 
				  + "\\" + PASSWORD_FILE;
		File passwordFile = new File(passwordFilename);
		if (passwordFile.exists()) {
			FileInputStream inputStream = new FileInputStream(passwordFile);
			byte [] bytes = new byte[(int)passwordFile.length()];
			inputStream.read(bytes);
			try {
				serverPassword = encrypter.decrypt(new String(bytes, "UTF8"));
				rcServer.setPassword(serverPassword);
			} catch (Exception ex) {
				ex.printStackTrace();
			}
		}
	}
	
	private void savePassword() throws IOException
	{	
		String passwordFilename = System.getProperty("user.home") + "\\" + REMOTAINMENT_FOLDER 
				+ "\\" + PASSWORD_FILE;
		File passwordFile = new File(passwordFilename);
		if (passwordFile.exists()) {
			passwordFile.delete();
		}
		passwordFile.createNewFile();
		FileOutputStream outputStream = new FileOutputStream(passwordFile);
			
		try {
			outputStream.write(encrypter.encrypt(serverPassword).getBytes("UTF8"));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
			
		outputStream.flush();
		outputStream.close();
	}
	
	private void loadFolder() throws IOException
	{
		// Check if REMOTAINMENT_FOLDER exists
		String remotainmentFolderPath = System.getProperty("user.home") + "\\" + REMOTAINMENT_FOLDER;
		File remotainmentFolder = new File(remotainmentFolderPath);
		if (!remotainmentFolder.exists()) {
			remotainmentFolder.mkdir();
		}
		
		// Load Entertainment Folders
		entertainmentFoldersList = new ArrayList<String>();
		String entertainmentFoldersFilename = System.getProperty("user.home") + "\\" + REMOTAINMENT_FOLDER 
											  + "\\" + ENTERTAINMENT_FILE;
		File entertainmentFoldersFile = new File(entertainmentFoldersFilename);
		if (entertainmentFoldersFile.exists()) {
			FileReader fr = new FileReader(entertainmentFoldersFile);
			BufferedReader br = new BufferedReader(fr);
			String line;
			while ((line = br.readLine()) != null) {
				entertainmentFoldersList.add(line);
			}
			br.close();
			fr.close();
		}
	}
	
	private void saveFolder() throws IOException
	{
		// Save Entertainment Folders
		ArrayList<String> entertainmentFolders = dlgFolders.getEntertainmentFolders();
		String entertainmentFoldersFilename = System.getProperty("user.home") + "\\" + REMOTAINMENT_FOLDER 
				  + "\\" + ENTERTAINMENT_FILE;
		File entertainmentFoldersFile = new File(entertainmentFoldersFilename);
		if (entertainmentFoldersFile.exists()) {
			entertainmentFoldersFile.delete();
		}
		entertainmentFoldersFile.createNewFile();
		FileWriter fw = new FileWriter(entertainmentFoldersFile);
		BufferedWriter bw = new BufferedWriter(fw);
		for (int i=0; i<entertainmentFolders.size(); i++) {
			bw.write(entertainmentFolders.get(i));
			bw.write("\n");
		}
		bw.close();
		fw.close();
	}
}
