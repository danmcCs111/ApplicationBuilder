package ApplicationBuilder;

import HttpDatabaseRequest.HttpDatabaseRequest;

public class QueryUpdateTool 
{
	public static String
		ENDPOINT = "http://localhost:";
	public static int
		PORT_NUMBER = 8000;
	
	public static final String
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		REQUEST_TYPE_HEADER_VALUE_QUERY = "Query",
		REQUEST_TYPE_HEADER_VALUE_INSERT = "Insert",
		REQUEST_TYPE_HEADER_VALUE_UPDATE = "Update",
		WEBSERVICE_QUERY_TAG_NAME = "WebserviceQuery",
		WEBSERVICE_QUERY_ATTRIBUTE_NAME = "content";
	
	public static void setEndpoint(String endpoint)
	{
		ENDPOINT = endpoint;
	}
	
	public static void setPortNumber(int portNumber)
	{
		PORT_NUMBER = portNumber;
	}
	
	public static String executeQuery(String query)
	{
		return HttpDatabaseRequest.executeGetRequest
		(
			ENDPOINT,
			PORT_NUMBER,
			query,
			REQUEST_TYPE_HEADER_KEY,
			REQUEST_TYPE_HEADER_VALUE_QUERY
		);
	}
	
	public static String executeInsert(String insert)
	{
		return HttpDatabaseRequest.executeGetRequest
		(
			ENDPOINT,
			PORT_NUMBER,
			insert,
			REQUEST_TYPE_HEADER_KEY,
			REQUEST_TYPE_HEADER_VALUE_INSERT
		);
	}
	
	public static String executeUpdate(String update)
	{
		return HttpDatabaseRequest.executeGetRequest
		(
			ENDPOINT,
			PORT_NUMBER,
			update,
			REQUEST_TYPE_HEADER_KEY,
			REQUEST_TYPE_HEADER_VALUE_UPDATE
		);
	}
		
}
