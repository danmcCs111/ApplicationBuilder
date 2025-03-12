package HttpDatabaseRequest;

public class SelectVideosDatabaseRequest implements HttpDatabaseRequest
{
	public static final String 
		SELECT_WEBSERVICES_SQL_REQUEST = "select * from videodatabase.Webservice";
	
	private static final String 
		ENDPOINT = "http://localhost:",
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		REQUEST_TYPE_HEADER_VALUE = "Query";
	private static final int
		PORT_NUMBER = 8000;
	
	static {
		getQueryOptions();
	}
	
	private static String getQueryOptions()
	{
		String response = HttpDatabaseRequest.executeGetRequest(
				ENDPOINT,
				PORT_NUMBER,
				REQUEST_TYPE_HEADER_KEY,
				REQUEST_TYPE_HEADER_VALUE,
				SELECT_WEBSERVICES_SQL_REQUEST
		);
		return response;
	}
}
