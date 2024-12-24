package WidgetExtensions;

import java.io.File;
import java.util.ArrayList;

public class FileListOptionGenerator 
{
	public FileListOptionGenerator(String path)
	{
		
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
