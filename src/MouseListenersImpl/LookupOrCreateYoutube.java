package MouseListenersImpl;

import java.awt.Image;
import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import ApplicationBuilder.QueryUpdateTool;
import ApplicationBuilder.ShellExecutorAlt;
import Graphics2D.GraphicsUtil;
import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class LookupOrCreateYoutube 
{
	public static final String []
		YOUTUBE_CHANNEL_HANDLE_STRIP = new String [] {"/videos", "/featured"},
		APPLICATION_BUILDER_CLI_OPTIONS = new String [] {"-cp"};
	public static final String
		APPLICATION_BUILDER_JAR_LOC = "./Application Builder.jar",
		APPLICATION_BUILDER_CLASS = "ApplicationBuilder.ShellHeadlessExecutor",//TDOD
		YOUTUBE_CHANNEL_HANDLE_MATCH = "/[^/]*$",
		OPERATION = "showResult",
		PLUGIN_JAR_LOCATION = PathUtility.getCurrentDirectory() + "/" + 
				"plugin-projects/YouTube-API-list/YoutubeApiList/youtubeApiList.sh",
		SAVE_INSERT_PATH = "./VideoLaunchFiles/YoutubeChannels/video-images/", //TODO
		IS_LOOKUP_FRAME_FILTER = "";
	
	private static String
		KEY_PATH = "./Properties/api-keys/youtube-api-key.txt";
	private static YoutubeQuery 
		youtubeSql = new YoutubeSql();
	
	public LookupOrCreateYoutube()
	{
		
	}
	
	public static void setSqlType(String sqlType)//TODO
	{
		if(YoutubeSql.isType(sqlType))
		{
			youtubeSql = new YoutubeSql();
		}
		else if(YoutubeSQLite.isType(sqlType))
		{
			youtubeSql = new YoutubeSQLite();
		}
	}
	
	public static void setKeyPath(String keyPath)
	{
		KEY_PATH = keyPath;
	}
	
	public void update(String videoChannelName, String videoChannelLink)
	{
		update(videoChannelName, videoChannelLink, null);
	}
	
	public void update(String videoChannelName, String videoChannelLink, Date beginDate)
	{
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = QueryUpdateTool.executeQuery(query);
		if(response == null)
			return;
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		if(drns.isEmpty())
		{
			createIfEmpty(videoChannelName, videoChannelLink);
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("VideoId_Video_VideoDatabase"))
				{
					int parentId = Integer.parseInt(drn.getNodeAttributes().get("content"));
					LoggingMessages.printOut("parentID is: " + parentId);
					LoggingMessages.printOut("channelLink is: " + videoChannelLink);
					if(beginDate == null)
					{
						beginDate = getLastDate(parentId, videoChannelLink);
					}
					updateYoutubeChannel(parentId, videoChannelLink, (beginDate == null) ? -1 : beginDate.getTime());
				}
			}
		}
	}
	
	public HashMap<Integer, Date> lookupLatestDate(String videoChannelName, String videoChannelLink)
	{
		HashMap<Integer, Date> parentIdAndDate = new HashMap<Integer, Date>();
		
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = QueryUpdateTool.executeQuery(query);
		if(response == null)
			return null;
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		if(drns.isEmpty())
		{
			createIfEmpty(videoChannelName, videoChannelLink);
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("VideoId_Video_VideoDatabase"))
				{
					int parentId = Integer.parseInt(drn.getNodeAttributes().get("content"));
					LoggingMessages.printOut("parentID is: " + parentId);
					LoggingMessages.printOut("channelLink is: " + videoChannelLink);
					Date lastDate = getLastDate(parentId, videoChannelLink);
					parentIdAndDate.put(parentId, lastDate);
					break;
				}
			}
		}
		return parentIdAndDate;
	}
	
	public HashMap<Integer, Date> lookupFirstDate(String videoChannelName, String videoChannelLink)
	{
		HashMap<Integer, Date> parentIdAndDate = new HashMap<Integer, Date>();
		
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = QueryUpdateTool.executeQuery(query);
		if(response == null)
			return null;
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		if(drns.isEmpty())
		{
			createIfEmpty(videoChannelName, videoChannelLink);
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("VideoId_Video_VideoDatabase"))
				{
					int parentId = Integer.parseInt(drn.getNodeAttributes().get("content"));
					LoggingMessages.printOut("parentID is: " + parentId);
					LoggingMessages.printOut("channelLink is: " + videoChannelLink);
					Date lastDate = getFirstDate(parentId, videoChannelLink);
					parentIdAndDate.put(parentId, lastDate);
					break;
				}
			}
		}
		return parentIdAndDate;
	}
	
	public int lookupCount(String videoChannelName, String videoChannelLink)
	{
		int count = 0;
		
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = QueryUpdateTool.executeQuery(query);
		if(response == null)
			return -1;
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		if(drns.isEmpty())
		{
			createIfEmpty(videoChannelName, videoChannelLink);
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("VideoId_Video_VideoDatabase"))
				{
					int parentId = Integer.parseInt(drn.getNodeAttributes().get("content"));
					LoggingMessages.printOut("parentID is: " + parentId);
					LoggingMessages.printOut("channelLink is: " + videoChannelLink);
					count = getCount(parentId, videoChannelLink);
					break;
				}
			}
		}
		return count;
	}
	
	public HashMap<Integer, ArrayList<YoutubeChannelVideo>> lookup(String videoChannelName, String videoChannelLink)
	{
		HashMap<Integer, ArrayList<YoutubeChannelVideo>> parentIdAndYoutubeChannelVideos = null;
		
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = QueryUpdateTool.executeQuery(query);
		if(response == null)
			return null;
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		if(drns.isEmpty())
		{
			createIfEmpty(videoChannelName, videoChannelLink);
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("VideoId_Video_VideoDatabase"))
				{
					int parentId = Integer.parseInt(drn.getNodeAttributes().get("content"));
					LoggingMessages.printOut("parentID is: " + parentId);
					LoggingMessages.printOut("channelLink is: " + videoChannelLink);
					parentIdAndYoutubeChannelVideos = lookupYoutubeVideo(parentId, videoChannelLink);
					break;
				}
			}
		}
		return parentIdAndYoutubeChannelVideos;
	}
	
	private void createIfEmpty(String videoChannelName, String url)
	{
		String insert = youtubeSql.getYoutubeInsertPrefix() + 
				PathUtility.surroundString(videoChannelName, "\"") + ", " + 
				PathUtility.surroundString(url, "\"") + ", " +
				youtubeSql.getYoutubeInsertSuffix();
		
		QueryUpdateTool.executeInsert(insert);
		lookup(videoChannelName, url);
	}
	
	public HashMap<Integer, ArrayList<YoutubeChannelVideo>> lookupYoutubeVideo(int parentId, String videoChannelLink)
	{
		Date lastDate = getLastDate(parentId, videoChannelLink);
		return getYoutubeVideos(parentId, videoChannelLink, (lastDate == null) ? -1 : lastDate.getTime());
	}
	
	private Date getLastDate(int parentId, String videoChannelLink)
	{
		String query = youtubeSql.getYoutubeVideoLatestQuery(parentId);
		String response = QueryUpdateTool.executeQuery(query);
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		Date lastDate = null;
		if(drns.isEmpty())
		{
			LoggingMessages.printOut("Empty");
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("UploadDate_VideoYoutube_VideoYoutubeDatabase"))
				{
					lastDate = DateParser.getDate(drn.getNodeAttributes());
					LoggingMessages.printOut("last time: " + lastDate);
					break;
				}
			}
		}
		return lastDate;
	}
	
	private Date getFirstDate(int parentId, String videoChannelLink)
	{
		String query = youtubeSql.getYoutubeVideoFirstQuery(parentId);
		String response = QueryUpdateTool.executeQuery(query);
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		Date lastDate = null;
		if(drns.isEmpty())
		{
			LoggingMessages.printOut("Empty");
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("UploadDate_VideoYoutube_VideoYoutubeDatabase"))
				{
					lastDate = DateParser.getDate(drn.getNodeAttributes());
					LoggingMessages.printOut("last time: " + lastDate);
					break;
				}
			}
		}
		return lastDate;
	}
	
	private int getCount(int parentId, String videoChannelLink)
	{
		String query = youtubeSql.getYoutubeVideoCount(parentId);
		String response = QueryUpdateTool.executeQuery(query);
		
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		int count = 0;
		if(drns.isEmpty())
		{
			LoggingMessages.printOut("Empty");
		}
		else
		{
			for(DatabaseResponseNode drn : drns.get(1))
			{
				if(drn.getNodeName().equals("Count"))//TODO.
				{
					count = Integer.parseInt(drn.getNodeAttributes().get("content"));
					LoggingMessages.printOut("count: " + count);
					break;
				}
			}
		}
		return count;
	}
	
	private HashMap<Integer, ArrayList<YoutubeChannelVideo>> getYoutubeVideos(
			int parentId, String videoChannelLink, long lastDate)
	{
		HashMap<Integer, ArrayList<YoutubeChannelVideo>> parentIdAndYoutubeChannelVideos = 
				new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		
		LoggingMessages.printOut("Parent ID: " + parentId);
		LoggingMessages.printOut("Channel Link: " + videoChannelLink);
		LoggingMessages.printOut("Epoch Time: " + lastDate);
		
		String query = youtubeSql.getYoutubeVideoQuery(parentId);
		LoggingMessages.printOut(query);
		String response = QueryUpdateTool.executeQuery(query);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		
		if(drns.isEmpty())
		{
			LoggingMessages.printOut("Empty");
		}
		else
		{
			parentIdAndYoutubeChannelVideos.put(parentId, new ArrayList<YoutubeChannelVideo>());
			for(int i = 1; i < drns.size(); i++)
			{
				YoutubeChannelVideo ycv = new YoutubeChannelVideo(drns.get(i));
				//TODO. don't not storing png yet.
				//Image imgPng = getPng(ycv);
				//ycv.setImagePng(imgPng);
				
				parentIdAndYoutubeChannelVideos.get(parentId).add(ycv);
			}
		}
		
		return parentIdAndYoutubeChannelVideos;
	}
	
	private String getYoutubeHandle(String videoChannelLink)
	{
		String youtubeHandle = videoChannelLink;
		for(String strip : YOUTUBE_CHANNEL_HANDLE_STRIP)
		{
			youtubeHandle = youtubeHandle.replace(strip, "");
		}
		Pattern pattern = Pattern.compile(YOUTUBE_CHANNEL_HANDLE_MATCH);
		Matcher m = pattern.matcher(youtubeHandle);
		if(m.find())
		{
			youtubeHandle = m.group();
		}
		youtubeHandle = youtubeHandle.replace("/", "");
		return youtubeHandle;
	}
	
	private void updateYoutubeChannel(int parentId, String youtubeChannelLink, long lastDate)
	{
		String
			youtubeHandle = getYoutubeHandle(youtubeChannelLink),
			youtubeHandleMinusAt = youtubeHandle.replace("@", ""),
			saveLoc = SAVE_INSERT_PATH + youtubeHandleMinusAt,
			key = PathUtility.readFileToString(new File(KEY_PATH)).replace("\n", "").replace(" ", ""),
			saveFile = new FileSelection(SAVE_INSERT_PATH + youtubeHandleMinusAt).getFullPath() + ".sql";
		
		PathUtility.createDirectoryIfNotExist(saveLoc);
		
		
		String [] args = new String [] {
//				new FileSelection(APPLICATION_BUILDER_JAR_LOC).getFullPath(),
//				APPLICATION_BUILDER_CLASS,
				PLUGIN_JAR_LOCATION + " " +
				OPERATION + " " + 
				youtubeSql.getSqlType() + " " + 
				key + " " +
				parentId + " " + 
				youtubeHandle + " " +
				lastDate + " " + 
				saveFile
			};
		//run plugin.
//		CommandBuild cb = new CommandBuild();
//		cb.setCommand("java", APPLICATION_BUILDER_CLI_OPTIONS, args);
//		try {
//			CommandExecutor.executeProcess(cb, true);
//		} catch (IOException e) {
//			e.printStackTrace();
//		}
		
		ShellExecutorAlt.run(args, true);
		
		//run insert & image grab jobs. use loading screen.
		String contents = PathUtility.readFileToString(new File(saveFile));
		if(contents != null)
		{
			LoggingMessages.printOut(contents);
			QueryUpdateTool.executeInsert(contents);
		}
	}
	
	private Image getPng(YoutubeChannelVideo ycv)
	{
		String
			img = ycv.getImageUrl(),
			title = ycv.getTitle(),
			saveLoc = SAVE_INSERT_PATH + title;
		
		String saveFileImage = saveLoc + "/" + title + ".png";
		if(!PathUtility.isFileExisting(saveFileImage))
		{
			PathUtility.imageDownloadAndSave(img, saveFileImage, "png");
		}
		Image imgPng = GraphicsUtil.getImageFromFile(new File(saveFileImage));
		LoggingMessages.printOut(imgPng.toString());
		
		return imgPng;
	}
	
	public static void main(String [] args)
	{
//		LookupOrCreateYoutube lcy = new LookupOrCreateYoutube();
//		HashMap<Integer, ArrayList<YoutubeChannelVideo>> ycvs = null;
//		ycvs = lcy.lookup("test abc", "https://www.youtube.com/@ABCNews");
//		printYoutubeChannelVideos(ycvs);
//		ycvs = lcy.lookup("test nbc", "https://www.youtube.com/@NBCNews");
//		printYoutubeChannelVideos(ycvs);
//		ycvs = lcy.lookup("microcentertech", "https://www.youtube.com/@microcentertech");
//		printYoutubeChannelVideos(ycvs);
		LookupOrCreateYoutube lcv = new LookupOrCreateYoutube();
		int count = lcv.lookupCount("Underworld", "https://www.youtube.com/@Underworld5s/videos");
		LoggingMessages.printOut(count + "");
	}
	
	private static void printYoutubeChannelVideos(HashMap<Integer, ArrayList<YoutubeChannelVideo>> ycvs)
	{
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				LoggingMessages.printOut(ycv.toString());
			}
		}
	}
	
}
