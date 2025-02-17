package Properties;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

public interface PathUtility 
{
	public static final String PATH_STRIP_FILTER = "([\\.]|[0-9\\sa-zA-Z])+[/]"; // only using landing folder name
	
	public static String filterPathToFilename(String path)
	{
		return path.replaceAll(PathUtility.PATH_STRIP_FILTER, "");
	}
	
	public static String getCurrentDirectory()
	{
		return System.getProperty("user.dir");
	}
	
	public static ArrayList<String> getOSFileList(String dir, String filter) 
	{
		ArrayList<String> files = new ArrayList<String>();
		LoggingMessages.printOut(dir);
		File [] fs = new File(dir).listFiles();
		
		for (File f : fs)
		{
			if(f.getName().contains(filter))
			{
				files.add(f.getName());
				LoggingMessages.printOut(f.getName());
			}
		}
		return files;
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
				String [] ss = s.split(delimter, 2);
				if(ss.length == 2)
					props.put(ss[0], ss[1]);
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return props;
	}
}
