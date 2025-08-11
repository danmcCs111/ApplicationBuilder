package HttpDatabaseRequest;

import java.util.Date;

public interface SQLUtility 
{
	public static String getDateToMySqlString(Date d)
	{
		return "(SELECT FROM_UNIXTIME(" + (d.getTime() / 1000) + "))"; //milliseconds to seconds
	}
}
