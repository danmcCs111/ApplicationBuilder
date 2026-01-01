package Properties;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Scanner;

import javax.imageio.ImageIO;

public interface PathUtility 
{
	public static final String [] 
		PATH_STRIP_FILTER = new String [] {"([\\.]|[0-9\\sa-zA-Z])+[/]", ""},
		PATH_REMOVE_CURRENT_DIRECTORY = new String []{"\\./", "/"};
	public static final String 
		ESCAPE_CHARACTER = "\\",
		NEW_LINE = "\n";
	
	public static String filterPathToFilename(String path)
	{
		path = path.strip();
		if(path.endsWith("/"))
		{
			path = path.substring(0, path.length()-1);
		}
		return path.replaceAll(PATH_STRIP_FILTER[0], PATH_STRIP_FILTER[1]);
	}
	
	public static String removeCurrentWorkingDirectoryFromPath(String path)
	{
		return path.replaceAll(PATH_REMOVE_CURRENT_DIRECTORY[0], PATH_REMOVE_CURRENT_DIRECTORY[1]);
	}
	
	public static String getCurrentDirectory()
	{
		return replaceBackslash(System.getProperty("user.dir"));
	}
	
	public static boolean isWindows()
	{
		return System.getProperty("os.name").startsWith("Windows");
	}
	
	public static String getCurrentDirectoryUnix()
	{
		String dir = System.getProperty("user.dir");
		dir = getPathLinux(dir);
		return dir;
	}
	
	public static String getPathLinux(String path)
	{
		path = replaceBackslash(path);
		path = path.replace("C:", "/c");
		return path;
	}
	
	public static String getFilenameNoExtension(String filename)
	{
		return (filename.contains("."))
			? filename.substring(0, filename.lastIndexOf("."))
			: filename;
	}
	
	public static String replaceBackslash(String path)
	{
		path = path.replaceAll("\\\\", "/");
		return path;
	}
	
	public static String surroundString(String target, String surround)
	{
		return surround + target + surround;
	}
	
	public static ArrayList<String> getOSFileList(String dir, String filter) 
	{
		ArrayList<String> files = new ArrayList<String>();
		if(!PathUtility.isWindows())//remove ending forward slash
		{
			dir = dir.strip();
			dir = dir.endsWith("/")?dir.substring(0, dir.length()-1):dir;
		}
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
	
	public static boolean isFileExisting(String destinationPath)
	{
		try {
			File file = new File(destinationPath);
			BufferedImage bi = ImageIO.read(file); 
			return (bi != null);
        } catch (IOException e) {
            LoggingMessages.printOut("Image: " + destinationPath + " not found!");
        }
		return false;
	}
	
	/**
	 * 
	 * @param imageUrl
	 * @param destinationPath
	 * @param writeType the image format (e.g., "jpg", "png", "gif")
	 */
	public static void imageDownloadAndSave(String imageUrl, String destinationPath, String writeImageType)
	{
		try {
            URL url = new URL(imageUrl);
            BufferedImage img = ImageIO.read(url);
            if (img != null) 
            {
                File file = new File(destinationPath);
                ImageIO.write(img, writeImageType, file); 
                System.out.println("Image downloaded successfully to " + destinationPath);
            } 
            else 
            {
                System.out.println("Could not read image from URL.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
	}
	
	public static void createDirectoryIfNotExist(String dirPath)
	{
		Path directoryPath = Paths.get(dirPath);
		try {
			Files.createDirectories(directoryPath);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static HashMap<String, String> readProperties(String location, String delimter)
	{
		HashMap<String,String> props = new HashMap<String,String>();
		LoggingMessages.printOut(location);
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
	
	public static String readFileToString(File locationFile)
	{
		Scanner sc;
		String fileContents = "";
		
		if(!locationFile.exists())
		{
			return null;
		}
		try {
			sc = new Scanner(locationFile);
			while (sc.hasNextLine()) {
				fileContents += sc.nextLine() + "\n";
			}
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return fileContents;
	}
	
	public static void writeStringToFile(File locationFile, String contents)
	{
		try {
			FileWriter myWriter = new FileWriter(locationFile);
			myWriter.write(contents);	
			myWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public static void writeProperties(String path, String [] [] namesAndValues)
	{
		try {
			FileWriter myWriter = new FileWriter(path);
			for(int i = 0; i < namesAndValues.length; i++)
			{
				myWriter.write(namesAndValues[i][0] + "=" + namesAndValues[i][1] + "\n");	
			}
			myWriter.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
