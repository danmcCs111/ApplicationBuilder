package MouseListenersImpl;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
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
	public static final String 
		YOUTUBE_CHANNEL_HANDLE_STRIP = "/[^/]*$",
		OPERATION = "showResult",
		PLUGIN_JAR_LOCATION = "plugin-projects/YouTube-API-list/YoutubeApiList/YoutubeApiList.jar",
		SAVE_INSERT_PATH = "./VideoLaunchFiles/YoutubeChannels/video-images/", //TODO
		KEY_PATH = "C:\\Users\\danie\\OneDrive\\Documents\\api-key.txt", //TODO
		
		YOUTUBE_QUERY = 
			"SELECT * FROM videodatabase.video WHERE VideoName_Video_VideoDatabase = <arg0>"+
			";",
		YOUTUBE_VIDEO_QUERY = 
			"SELECT * FROM videodatabase.videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg0> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase DESC;",
		
		YOUTUBE_INSERT_PREFIX = "INSERT INTO videodatabase.video (VideoName_Video_VideoDatabase, VideoUrl_Video_VideoDatabase, InsertDate_Video_VideoDatabase) values( ",
		YOUTUBE_INSERT_SUFFIX = " CURRENT_TIMESTAMP);",
		
		IS_LOOKUP_FRAME_FILTER = "",
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		REQUEST_TYPE_HEADER_VALUE_QUERY = "Query",
		REQUEST_TYPE_HEADER_VALUE_INSERT = "Insert",
		WEBSERVICE_QUERY_TAG_NAME = "WebserviceQuery",
		WEBSERVICE_QUERY_ATTRIBUTE_NAME = "content";
	public static final int
		PORT_NUMBER = 8000;
	
	public LookupOrCreateYoutube()
	{
		
	}
	
	public void lookup(String videoChannelName, String videoChannelLink)
	{
		String query = YOUTUBE_QUERY.replaceFirst("<arg0>",PathUtility.surroundString(videoChannelName, "\""));
		String response = executeQuery(query);
		
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
					lookupYoutubeVideo(parentId, videoChannelLink);
				}
			}
		}
	}
	
	private void createIfEmpty(String videoChannelName, String url)
	{
		String insert = YOUTUBE_INSERT_PREFIX + 
				PathUtility.surroundString(videoChannelName, "\"") + ", " + 
				PathUtility.surroundString(url, "\"") + ", " +
				YOUTUBE_INSERT_SUFFIX;
		
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
	
	public void lookupYoutubeVideo(int parentId, String videoChannelLink)
	{
		String query = YOUTUBE_VIDEO_QUERY.replaceFirst("<arg0>", parentId+"");
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
					long timestamp = Long.valueOf(drn.getNodeAttributes().get("content"));
					lastDate = new Date(timestamp);
					LoggingMessages.printOut("last time: " + lastDate);
					break;
				}
			}
		}
		getYoutubeVideos(parentId, videoChannelLink, (lastDate == null) ? -1 : lastDate.getTime());
	}
	
	private void getYoutubeVideos(int parentId, String videoChannelLink, long lastDate)
	{
		Pattern pattern = Pattern.compile(YOUTUBE_CHANNEL_HANDLE_STRIP);
		Matcher m = pattern.matcher(videoChannelLink);
		String youtubeHandle = "";
		if(m.find())
		{
			youtubeHandle = m.group();
		}
		youtubeHandle = youtubeHandle.replace("/", "");
		
		LoggingMessages.printOut("Parent ID: " + parentId);
		LoggingMessages.printOut("Channel Link: " + videoChannelLink);
		LoggingMessages.printOut("Handle: " + youtubeHandle);
		LoggingMessages.printOut("Epoch Time: " + lastDate);
		
		String youtubeHandleMinusAt = youtubeHandle.replace("@", "");
		String key = PathUtility.readFileToString(new File(KEY_PATH)).replace("\n", "").replace(" ", "");
		String saveFile = new FileSelection(SAVE_INSERT_PATH + youtubeHandleMinusAt).getFullPath() + ".sql";
		String [] args = new String [] {
			PLUGIN_JAR_LOCATION,
			"YoutubeApiList.YoutubeApiList",
			OPERATION, 
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
		LoggingMessages.printOut(contents);
		executeInsert(contents);
		
		String query = YOUTUBE_VIDEO_QUERY.replaceFirst("<arg0>", parentId+"");
		String response = executeQuery(query);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		
		String saveLoc = SAVE_INSERT_PATH + youtubeHandleMinusAt;
		if(drns.isEmpty())
		{
			LoggingMessages.printOut("Empty");
		}
		else
		{
			PathUtility.createDirectoryIfNotExist(saveLoc);
			
			for(int i = 1; i < drns.size(); i++)
			{
				String 
					img = "",
					title = "";
				for(DatabaseResponseNode drn : drns.get(i))
				{
					if(drn.getNodeName().equals("Title_VideoYoutube_VideoYoutubeDatabase"))
					{
						title = drn.getNodeAttributes().get("content");
					}
					else if(drn.getNodeName().equals("PosterImageUrl_VideoYoutube_VideoYoutubeDatabase"))
					{
						img = drn.getNodeAttributes().get("content");
						break;
					}
				}
				String saveFileImage = saveLoc + "/" + title + ".png";
				if(!PathUtility.isFileExisting(saveFileImage))
				{
					PathUtility.imageDownloadAndSave(img, saveFileImage, "png");
				}
				Image imgPng = GraphicsUtil.getImageFromFile(new File(saveFileImage));
				LoggingMessages.printOut(imgPng.toString());
			}
		}
		
		//load. using loading screen again.
		
	}
	
	public static void main(String [] args)
	{
		LookupOrCreateYoutube lcy = new LookupOrCreateYoutube();
//		String query = YOUTUBE_VIDEO_QUERY.replaceFirst("<arg0>", 8+"");
//		LoggingMessages.printOut(query);
//		String response = executeQuery(query);
		
		lcy.lookup("test abc", "https://www.youtube.com/@ABCNews");
		lcy.lookup("test nbc", "https://www.youtube.com/@NBCNews");
	}
}
