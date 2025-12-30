package MouseListenersImpl;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectWebServiceQueries;
import Properties.PathUtility;

public class LookupOrCreateYoutube 
{
	public static final String 
		YOUTUBE_QUERY_PREFIX = "SELECT * FROM videodatabase.video WHERE VideoName_Video_VideoDatabase = ",
		YOUTUBE_QUERY_SUFFIX = "",
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
	
	public void lookup(String videoChannelName)
	{
		String query = YOUTUBE_QUERY_PREFIX + PathUtility.surroundString(videoChannelName, "\"") + ";";
		
		HttpDatabaseRequest.executeGetRequest
		(
				SelectWebServiceQueries.ENDPOINT,
				SelectWebServiceQueries.PORT_NUMBER,
				query,
				SelectWebServiceQueries.REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE_QUERY
		);
	}
	
	public static void main(String [] args)
	{
		LookupOrCreateYoutube lcy = new LookupOrCreateYoutube();
		lcy.lookup("test abc");
	}
}
