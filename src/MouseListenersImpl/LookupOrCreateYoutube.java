package MouseListenersImpl;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import Actions.CommandExecutor;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectWebServiceQueries;
import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class LookupOrCreateYoutube 
{
	public static final String []
			YOUTUBE_CHANNEL_HANDLE_STRIP = new String [] {"/videos", "/featured"};
	public static final String
		YOUTUBE_CHANNEL_HANDLE_MATCH = "/[^/]*$",
		
		OPERATION = "showResult",
		PLUGIN_JAR_LOCATION = "plugin-projects/YouTube-API-list/YoutubeApiList/YoutubeApiList.jar",
		SAVE_INSERT_PATH = "./VideoLaunchFiles/YoutubeChannels/video-images/", //TODO
		
		IS_LOOKUP_FRAME_FILTER = "",
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		REQUEST_TYPE_HEADER_VALUE_QUERY = "Query",
		REQUEST_TYPE_HEADER_VALUE_INSERT = "Insert",
		WEBSERVICE_QUERY_TAG_NAME = "WebserviceQuery",
		WEBSERVICE_QUERY_ATTRIBUTE_NAME = "content";
	public static final int
		PORT_NUMBER = 8000;
	
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
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = executeQuery(query);
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
					Date lastDate = getLastDate(parentId, videoChannelLink);
					updateYoutubeChannel(parentId, videoChannelLink, (lastDate == null) ? -1 : lastDate.getTime());
				}
			}
		}
	}
	
	public HashMap<Integer, ArrayList<YoutubeChannelVideo>> lookup(String videoChannelName, String videoChannelLink)
	{
		HashMap<Integer, ArrayList<YoutubeChannelVideo>> parentIdAndYoutubeChannelVideos = null;
		
		String query = youtubeSql.getYoutubeQuery(videoChannelName);
		String response = executeQuery(query);
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
		
		executeInsert(insert);
		lookup(videoChannelName, url);
		
	}
	
	private static String executeInsert(String insert)
	{
		return HttpDatabaseRequest.executeGetRequest
		(
				SelectWebServiceQueries.ENDPOINT,
				SelectWebServiceQueries.PORT_NUMBER,
				insert,
				SelectWebServiceQueries.REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE_INSERT
		);
	}
	
	private static String executeQuery(String query)
	{
		return HttpDatabaseRequest.executeGetRequest
		(
				SelectWebServiceQueries.ENDPOINT,
				SelectWebServiceQueries.PORT_NUMBER,
				query,
				SelectWebServiceQueries.REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE_QUERY
		);
	}
	
	public HashMap<Integer, ArrayList<YoutubeChannelVideo>> lookupYoutubeVideo(int parentId, String videoChannelLink)
	{
		Date lastDate = getLastDate(parentId, videoChannelLink);
		return getYoutubeVideos(parentId, videoChannelLink, (lastDate == null) ? -1 : lastDate.getTime());
	}
	
	private Date getLastDate(int parentId, String videoChannelLink)
	{
		String query = youtubeSql.getYoutubeVideoQuery(parentId);
		String response = executeQuery(query);
		
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
	
	private HashMap<Integer, ArrayList<YoutubeChannelVideo>> getYoutubeVideos(int parentId, String videoChannelLink, long lastDate)
	{
		HashMap<Integer, ArrayList<YoutubeChannelVideo>> parentIdAndYoutubeChannelVideos = 
				new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		
		for(String strip : YOUTUBE_CHANNEL_HANDLE_STRIP)
		{
			videoChannelLink = videoChannelLink.replace(strip, "");
		}
		String youtubeHandle = videoChannelLink;
		Pattern pattern = Pattern.compile(YOUTUBE_CHANNEL_HANDLE_MATCH);
		Matcher m = pattern.matcher(youtubeHandle);
		if(m.find())
		{
			youtubeHandle = m.group();
		}
		youtubeHandle = youtubeHandle.replace("/", "");
		
		LoggingMessages.printOut("Parent ID: " + parentId);
		LoggingMessages.printOut("Channel Link: " + videoChannelLink);
		LoggingMessages.printOut("Handle: " + youtubeHandle);
		LoggingMessages.printOut("Epoch Time: " + lastDate);
		
		String query = youtubeSql.getYoutubeVideoQuery(parentId);
		LoggingMessages.printOut(query);
		String response = executeQuery(query);
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
	
	private void updateYoutubeChannel(int parentId, String youtubeHandle, long lastDate)
	{
		String 
			youtubeHandleMinusAt = youtubeHandle.replace("@", ""),
			saveLoc = SAVE_INSERT_PATH + youtubeHandleMinusAt,
			key = PathUtility.readFileToString(new File(KEY_PATH)).replace("\n", "").replace(" ", ""),
			saveFile = new FileSelection(SAVE_INSERT_PATH + youtubeHandleMinusAt).getFullPath() + ".sql";
		
		PathUtility.createDirectoryIfNotExist(saveLoc);
		String [] args = new String [] {
				PLUGIN_JAR_LOCATION,
				"YoutubeApiList.YoutubeApiList",
				OPERATION,
				youtubeSql.getSqlType(),
				key,
				parentId + "", 
				youtubeHandle, 
				lastDate + "", 
				saveFile
			};
		//run plugin.
		CommandBuild cb = new CommandBuild();
		cb.setCommand("java", new String []{"-cp"}, args);
		try {
			CommandExecutor.executeProcess(cb, true);
		} catch (IOException e) {
			e.printStackTrace();
		}
		//run insert & image grab jobs. use loading screen.
		String contents = PathUtility.readFileToString(new File(saveFile));
		if(contents != null)
		{
			LoggingMessages.printOut(contents);
			executeInsert(contents);
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
		LookupOrCreateYoutube lcy = new LookupOrCreateYoutube();
		HashMap<Integer, ArrayList<YoutubeChannelVideo>> ycvs = null;
		ycvs = lcy.lookup("test abc", "https://www.youtube.com/@ABCNews");
		printYoutubeChannelVideos(ycvs);
		ycvs = lcy.lookup("test nbc", "https://www.youtube.com/@NBCNews");
		printYoutubeChannelVideos(ycvs);
		ycvs = lcy.lookup("microcentertech", "https://www.youtube.com/@microcentertech");
		printYoutubeChannelVideos(ycvs);
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
