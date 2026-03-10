package HttpDatabaseRequest;

import java.util.ArrayList;

import ApplicationBuilder.QueryUpdateTool;
import HttpDatabaseResponse.DatabaseResponseNode;
import HttpDatabaseResponse.HttpDatabaseResponse;
import Properties.LoggingMessages;

public class SelectWebServiceQueries 
{
	public static final String 
		SELECT_WEBSERVICES_SQL_REQUEST = ".Webservice";//TODO
	
	private String [] 
		queryOptions;
	
	public SelectWebServiceQueries(String database)
	{
		LoggingMessages.printOut(".Build." + SelectWebServiceQueries.class.getName());
		queryOptions = collectQueryOptions(database);
	}
	
	public String [] getQueryOptions()
	{
		return queryOptions;
	}
	
	public static ArrayList<String> getQueries(ArrayList <ArrayList <DatabaseResponseNode>> drns)
	{
		return getAttributes(drns, QueryUpdateTool.WEBSERVICE_QUERY_TAG_NAME, QueryUpdateTool.WEBSERVICE_QUERY_ATTRIBUTE_NAME);
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
	
	private static String [] collectQueryOptions(String database)
	{
		String response = HttpDatabaseRequest.executeGetRequest(
				QueryUpdateTool.ENDPOINT,
				QueryUpdateTool.PORT_NUMBER,
				"Select * from " + database + SELECT_WEBSERVICES_SQL_REQUEST,
				QueryUpdateTool.REQUEST_TYPE_HEADER_KEY,
				QueryUpdateTool.REQUEST_TYPE_HEADER_VALUE_QUERY
		);
		HttpDatabaseResponse hdr = new HttpDatabaseResponse();
		ArrayList <ArrayList <DatabaseResponseNode>> drns = hdr.parseResponse(response);
		return getQueries(drns).toArray(new String [] {});
	}
}
