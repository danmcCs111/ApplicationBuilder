package MouseListenersImpl;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import HttpDatabaseResponse.DatabaseResponseNode;
import WidgetComponents.JButtonLengthLimited;

public class VideoChannel 
{
	private static final String
		VIDEO_UPDATE_HANDLE = "update video set Handle_Video_VideoDatabase='<arg0>' where videoid_Video_VideoDatabase=<arg1>;",
		ID_COLUMN = "VideoId_Video_VideoDatabase",
		NAME_COLUMN = "VideoName_Video_VideoDatabase",
		URL_COLUMN = "VideoUrl_Video_VideoDatabase",
		HANDLE_COLUMN = "Handle_Video_VideoDatabase",
		INSERT_DATE_COLUMN = "InsertDate_Video_VideoDatabase";
	
	private int 
		id = -1;
	private String
		name = null,
		url = null,
		handle = null;
	private long
		insertDate = -1;
	
	public VideoChannel(ArrayList<DatabaseResponseNode> drns)
	{
		parse(drns);
	}
	
	private void parse(ArrayList<DatabaseResponseNode> drns)
	{
		for(DatabaseResponseNode drn : drns)
		{
			String value = drn.getNodeAttributes().get("content");
			switch(drn.getNodeName())
			{
			case ID_COLUMN:
				id = Integer.parseInt(value);
				break;
			case NAME_COLUMN:
				name = value;
				break;
			case URL_COLUMN:
				url = value;
				break;
			case HANDLE_COLUMN:
				handle = value;
				break;
			case INSERT_DATE_COLUMN:
				insertDate = DateParser.getDate(drn.getNodeAttributes()).getTime();
				break;
			}
		}
	}
	
	public String getDatabaseUpdate()
	{
		return VIDEO_UPDATE_HANDLE.replaceFirst("<arg0>", getHandle()).replaceFirst("<arg1>", getId()+"");
	}
	
	public static Date getFirstDate(JButtonLengthLimited jbll)
	{
		return getDate(LookupOrCreateYoutube.lookupFirstDate(jbll.getText(), jbll.getName()));
	}
	public static Date getLastDate(JButtonLengthLimited jbll)
	{
		return getDate(LookupOrCreateYoutube.lookupLatestDate(jbll.getText(), jbll.getName()));
	}
	private static Date getDate(HashMap<Integer, Date> dateMap)
	{
		Date date = null;
		
		if(dateMap != null && !dateMap.isEmpty())
		{
			int key = dateMap.keySet().iterator().next();
			date = dateMap.get(key);
		}
		return date;
	}
	
	public int getId()
	{
		return this.id;
	}
	public String getName()
	{
		return this.name;
	}
	public String getUrl()
	{
		return this.url;
	}
	public String getHandle()
	{
		return this.handle;
	}
	public void setHandle(String handle)
	{
		this.handle = handle;
	}
	public long getInsertDate()
	{
		return this.insertDate;
	}
	
	@Override
	public String toString()
	{
		return 
			this.getId() + " " + 
			this.getName() + " " +  
			this.getUrl() + " " + 
			this.getHandle() + " " + 
			this.getInsertDate() + " "
			;
	}
}
