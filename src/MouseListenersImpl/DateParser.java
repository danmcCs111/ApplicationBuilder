package MouseListenersImpl;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

import Params.ParamTypes;
import Properties.LoggingMessages;

public class DateParser 
{
	public static Date getDate(HashMap<String, String> nodeAttrs)
	{
		Date lastDate = null;
		
		ParamTypes pt = ParamTypes.getParamType(nodeAttrs.get("classType"));
		LoggingMessages.printOut("ParamType: " + pt);
		if(pt.equals(ParamTypes.String))
		{
			String timestamp = nodeAttrs.get("content");
			LoggingMessages.printOut(timestamp);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
			try {
				lastDate = sdf.parse(timestamp);
				LoggingMessages.printOut(lastDate.toString());
			} catch (ParseException e) {
				SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm");
				try {
					lastDate = sdf2.parse(timestamp);
				} catch (ParseException e1) {
					e.printStackTrace();
					e1.printStackTrace();
				}
			}
		}
		else
		{
			long timestamp = Long.valueOf(nodeAttrs.get("content"));
			lastDate = new Date(timestamp);
		}
		return lastDate;
	}
}
