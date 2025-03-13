package HttpDatabaseRequest;

import java.util.ArrayList;

import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import Properties.LoggingMessages;

public class SelectWebServiceQueries 
{
	public static final String 
		SELECT_WEBSERVICES_SQL_REQUEST = "select * from videodatabase.Webservice";//TODO
	
	private static final String 
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		REQUEST_TYPE_HEADER_VALUE = "Query",
		WEBSERVICE_QUERY_TAG_NAME = "WebserviceQuery",
		WEBSERVICE_QUERY_ATTRIBUTE_NAME = "content";
	private static final int
		PORT_NUMBER = 8000;
	
	private String [] queryOptions;
	
	public SelectWebServiceQueries()
	{
		LoggingMessages.printOut(".Build." + SelectWebServiceQueries.class.getName());
		queryOptions = collectQueryOptions();
	}
	
	public String [] getQueryOptions()
	{
		return queryOptions;
	}
	
	public static ArrayList<String> getQueries(ArrayList <ArrayList <DatabaseResponseNode>> drns)
	{
		return getAttributes(drns, WEBSERVICE_QUERY_TAG_NAME, WEBSERVICE_QUERY_ATTRIBUTE_NAME);
	}
	
	private static ArrayList<String> getAttributes(
			ArrayList <ArrayList <DatabaseResponseNode>> drns, 
			String columnNamePrefix, 
			String attributeName)
	{
		ArrayList<String> queries = new ArrayList<String>();
		
		for(ArrayList<DatabaseResponseNode> drn : drns)
		{
			for(DatabaseResponseNode d : drn)
			{
				if(d.getNodeName().startsWith(columnNamePrefix))
				{
					String attrVal = d.getNodeAttributes().get(attributeName);
					attrVal = attrVal.trim();
					queries.add(attrVal);
				}
			}
		}
		return queries;
	}
	
	private static String [] collectQueryOptions()
	{
		String response = HttpDatabaseRequest.executeGetRequest(
				ENDPOINT,
				PORT_NUMBER,
				SELECT_WEBSERVICES_SQL_REQUEST,
				REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE
		);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		return getQueries(drns).toArray(new String [] {});
	}
}
