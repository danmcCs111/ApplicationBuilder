package Properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import ApplicationBuilder.LoggingMessages;

public class PropertiesFileLoader {
	
	private static final HashMap<Paths, HashMap<String,String>> PROPERTIES = 
			new HashMap<Paths, HashMap<String, String>>();
	static {
		try {
			readLauncherProperties();
		} catch (FileNotFoundException e) {
			LoggingMessages.printFileNotFound(e.getMessage());
			e.printStackTrace();
		}
	}
	
	private static void readLauncherProperties() throws FileNotFoundException 
	{
		for(Paths p : Paths.values())
		{
			HashMap<String, String> tmp = null;
			String [] propLocs = p.getPaths();
			for(String loc : propLocs)
			{
				tmp = readProperties(loc, "=");
				if(tmp != null)
				{
					PROPERTIES.put(p, tmp);
					break;
				}
			}
			if (tmp == null) {
				FileNotFoundException fe = new FileNotFoundException("Launcher.properties not found!");
				throw fe;
			}
		}
	}
	
	public static HashMap<String,String> getLauncherProperties(Paths p)
	{
		return PROPERTIES.get(p);
	}
	
	public static void reloadLauncherProperties()
	{
		PROPERTIES.clear();
		try {
			readLauncherProperties();
		} catch (FileNotFoundException e) {
			LoggingMessages.printFileNotFound(e.getMessage());
			e.printStackTrace();
		}
	}
	
	public static HashMap<String,String> readProperties(String location, String delimter)
	{
		HashMap<String,String> props = new HashMap<String,String>();
		File file = new File(location);
		Scanner sc;
		
		if(!file.exists())
		{
			return null;
		}
		try {
			sc = new Scanner(file);
			while (sc.hasNextLine()) {
				String s = sc.nextLine();
				String [] ss = s.split(delimter);
				if(ss.length == 2)
					props.put(ss[0], ss[1]);
			}
		} catch (FileNotFoundException e) {
			LoggingMessages.printFileNotFound(e.getMessage());
			e.printStackTrace();
		}
		return props;
	}
	
	public static ArrayList<String> getOSFileList(String dir, String filter) 
	{
		ArrayList<String> files = new ArrayList<String>();
		File [] fs = new File(dir).listFiles();
		
		for (File f : fs)
		{
			if(f.getName().contains(filter))
			{
				files.add(f.getName());
			}
		}
		return files;
	}
}
