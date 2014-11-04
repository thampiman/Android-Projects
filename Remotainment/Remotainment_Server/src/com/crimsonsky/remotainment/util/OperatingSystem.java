package com.crimsonsky.remotainment.util;

public class OperatingSystem {
	private static String PROPERTY_OS_NAME = "os.name";
	private static String PROPERTY_OS_VERSION = "os.version";
	private static String PROPERTY_OS_ARCHITECTURE = "os.architecture";
	
	private static String OS_NAME_WINDOWS = "windows";
	private static String OS_NAME_LINUX = "linux";
	private static String OS_NAME_MAC = "mac";
	
	public static String getOperatingSystem() 
	{
		return System.getProperty(PROPERTY_OS_NAME);
	}
	
	public static String getOperatingSystemVersion() 
	{
		return System.getProperty(PROPERTY_OS_VERSION);
	}
	
	public static String getOperatingSystemArchitecture() 
	{
		return System.getProperty(PROPERTY_OS_ARCHITECTURE);
	}
	
	public static boolean isWindows() 
	{
		if (getOperatingSystem().toLowerCase().indexOf(OS_NAME_WINDOWS) > -1)
			return true;
		
		return false;
	}
	
	public static boolean isLinux()
	{
		if (getOperatingSystem().toLowerCase().indexOf(OS_NAME_LINUX) > -1)
			return true;
		
		return false;
	}
	
	public static boolean isMac()
	{
		if (getOperatingSystem().toLowerCase().indexOf(OS_NAME_MAC) > -1)
			return true;
		
		return false;
	}
}
