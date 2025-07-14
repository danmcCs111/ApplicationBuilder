package Actions;

import java.io.IOException;

import Properties.LoggingMessages;
import Properties.PathUtility;

public class LoadVideos 
{
	//TODO replace with pickers.
	private static final String 
		AUTO_HOT_KEY = "C:\\Program Files\\AutoHotkey\\v2\\AutoHotkey",
		AUTO_HOT_KEY_YOUTUBE_SCRAPE = "C:\\Users\\danie\\codebase\\danmcCs111\\AutoHotKey-Utils\\youtube_scrape.ahk",
		GIT_SHELL_PROCESS = "\"C:\\Program Files\\Git\\git-bash.exe\"",
		PROCESSING_DIRECTORY = "/src/ApplicationBuilder/YoutubeFreeMovies2",
		YOUTUBE_SCRAPE_SCRIPT = "./youtube_strip.sh",
		YOUTUBE_SCRAPE_FILENAME = "/c/Users/danie/OneDrive/Desktop/page_scrapes/view-source_https___www.youtube.com_feed_storefront_bp=EgCSAQMI9gOiBQIoBg3D.html";
	private static final String [] GIT_SHELL_OPTIONS = new String [] {"-i", "-c"};
	
	private static Process runningProcess = null;
	
	public static void executeProcess(String ... args) throws IOException
	{
		ProcessBuilder pb = new ProcessBuilder(args);
		runningProcess = pb.start();
		while(runningProcess.isAlive())
		{
			try {
				Thread.sleep(1000l);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	public static void loadYoutubeScrape()
	{
		try {
			executeProcess(AUTO_HOT_KEY, AUTO_HOT_KEY_YOUTUBE_SCRAPE);
			
			String curDir = PathUtility.getCurrentDirectoryUnix();
			String cdDir = "cd " + curDir + PROCESSING_DIRECTORY;
			String shellCommand = cdDir;
			shellCommand += "; pwd";
			shellCommand += "; " + YOUTUBE_SCRAPE_SCRIPT + " " + YOUTUBE_SCRAPE_FILENAME;
			LoggingMessages.printOut(shellCommand);
			
			String [] params = new String[GIT_SHELL_OPTIONS.length + 2];
			params[0] = GIT_SHELL_PROCESS;
			for(int i = 0; i < GIT_SHELL_OPTIONS.length; i++)
			{
				params[i+1] = GIT_SHELL_OPTIONS[i]; 
			}
			params[GIT_SHELL_OPTIONS.length + 1] = "\"" + shellCommand + "\"";
			
			executeProcess(params);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
