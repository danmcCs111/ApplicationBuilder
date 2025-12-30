package MouseListenersImpl;

import java.util.ArrayList;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectWebServiceQueries;
import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import Properties.PathUtility;

public class LookupOrCreateYoutube 
{
	public static final String 
		YOUTUBE_QUERY_PREFIX = "SELECT * FROM videodatabase.video WHERE VideoName_Video_VideoDatabase = ",
		YOUTUBE_QUERY_SUFFIX = ";",
		YOUTUBE_INSERT_PREFIX = "INSERT INTO videodatabase.video (VideoName_Video_VideoDatabase, VideoUrl_Video_VideoDatabase) values( ",
		YOUTUBE_INSERT_SUFFIX = ");",
		IS_LOOKUP_FRAME_FILTER = "";
	
	public static final String 
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
	
	public void lookup(String videoChannelName, String url)
	{
		String query = YOUTUBE_QUERY_PREFIX + PathUtility.surroundString(videoChannelName, "\"") + YOUTUBE_QUERY_SUFFIX;
		
		String response = HttpDatabaseRequest.executeGetRequest
		(
				SelectWebServiceQueries.ENDPOINT,
				SelectWebServiceQueries.PORT_NUMBER,
				query,
				SelectWebServiceQueries.REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE_QUERY
		);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		if(drns.isEmpty())
		{
			createIfEmpty(videoChannelName, url);
		}
		
	}
	
	private void createIfEmpty(String videoChannelName, String url)
	{
		String insert = YOUTUBE_INSERT_PREFIX + 
				PathUtility.surroundString(videoChannelName, "\"") + ", " + 
				PathUtility.surroundString(url, "\"") +
				YOUTUBE_INSERT_SUFFIX;
		
		String response = HttpDatabaseRequest.executeGetRequest
		(
				SelectWebServiceQueries.ENDPOINT,
				SelectWebServiceQueries.PORT_NUMBER,
				insert,
				SelectWebServiceQueries.REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE_INSERT
		);
//		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
//		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
	}
	
	public static void main(String [] args)
	{
		LookupOrCreateYoutube lcy = new LookupOrCreateYoutube();
		lcy.lookup("test abc", "https://www.youtube.com/@ABCNews");
		lcy.lookup("test nbc", "https://www.youtube.com/@NBCNews");
	}
}
