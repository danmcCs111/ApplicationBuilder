package ApplicationBuilder;

import java.nio.file.Paths;

public class LoggingMessages {
	
	public static void printOut(String ... out)
	{
		for(String s : out)
		{
			System.out.println(s);
		}
	}
	
	public static void printFileNotFound(String filename)
	{
		printOut("Launcher Properties File: " + filename + " not Found!");
		printCurrentPath();
	}
	public static void printCurrentPath()
	{
		String s = Paths.get("").toAbsolutePath().toString();
		printOut("Current absolute path is: " + s);
	}
}
