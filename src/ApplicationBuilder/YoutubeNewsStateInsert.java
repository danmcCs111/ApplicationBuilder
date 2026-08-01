package ApplicationBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import MouseListenersImpl.LookupOrCreateYoutube;
import ObjectTypeConversion.DirectorySelection;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class YoutubeNewsStateInsert 
{
	private static String
		DELIMITER = "@,@",
		STATES_FILE_PATH = "./VideoLaunchFiles/YoutubeNewsUnitedStates/";
	private static DirectorySelection
		STATES_DIRECTORY = new DirectorySelection(STATES_FILE_PATH);
	private HashMap<String, YoutubeParsedFile>
		titleAndParsedFile = new HashMap<String, YoutubeParsedFile>();
	
	public YoutubeNewsStateInsert()
	{
		for(String file : getFileList(STATES_DIRECTORY.getFullPath()))
		{
			ArrayList<String> records = readFile(STATES_DIRECTORY.getFullPath() + file);
			for(String rec : records)
			{
				YoutubeParsedFile ypf = getParsedFile(file, rec);
				if(ypf == null)
					continue;
				
				//process
				LookupOrCreateYoutube.createIfEmptyWithHandle(ypf.title, ypf.url, ypf.handle);
				buildYoutube(ypf);
				LoggingMessages.printOut(ypf.toString());
			}
		}
	}
	
	public ArrayList<String> getFileList(String dir)
	{
		return PathUtility.getOSFileList(dir, ".txt");
	}
	
	public ArrayList<String> readFile(String filename)
	{
		return PathUtility.readFileToStringArray(new File(filename));
	}
	
	public YoutubeParsedFile getParsedFile(String filename, String record)
	{
		YoutubeParsedFile ypf = new YoutubeParsedFile();
		String [] vals = record.split(DELIMITER);
		if(vals.length != 4)
			return null;
		
		ypf.title = vals[0];
		if(titleAndParsedFile.containsKey(ypf.title))
			return null;
		
		ypf.url = vals[1];
		if(!ypf.url.endsWith("videos"))
			return null;
		
		ypf.handle = vals[2];
		if(!ypf.handle.contains("@"))
			return null;
		
		ypf.imgLink = vals[3];
		
		titleAndParsedFile.put(ypf.title, ypf);
		String prefix = "["+filename.replaceAll(".txt", "")+"] ";
		ypf.title = ypf.title.replaceAll("- YouTube", "");
		ypf.title = prefix + ypf.title;
		ypf.title = ypf.title.replaceAll("/", "");
		ypf.title = ypf.title.replaceAll("\\|", "");
		ypf.title = ypf.title.replaceAll(":", "");
		
		return ypf;
	}
	
	public void buildYoutube(YoutubeParsedFile ypf)
	{
		String 
			contents = "[InternetShortcut]" + "\n" + "URL=" + ypf.url,
			urlFilename = ypf.title + ".url",
			pngFilename = ypf.title + ".png",
			imgUrl = ypf.imgLink,
			savePathUrl = STATES_DIRECTORY.getFullPath(),
			savePathImg = STATES_DIRECTORY.getFullPath() + "/images";
	
		PathUtility.imageDownloadAndSave(imgUrl, savePathImg + "/" + pngFilename, "png");
		PathUtility.writeStringToFile(new File(savePathUrl + "/" + urlFilename), contents);
	}
	
	public static void main(String [] args)
	{
		new YoutubeNewsStateInsert();
	}
}
