package MouseListenersImpl;

import java.util.ArrayList;

import HttpDatabaseResponse.DatabaseResponseNode;

public class YoutubeChannelVideo 
{
	private int 
		parentId = -1;
	private String
		idVideo = null,
		title = null,
		url = null,
		imageUrl = null;
	private long
		uploadDate = -1,
		insertDate = -1;
	private static final String 
		ID_COLUMN = "Id_VideoYoutube_VideoYoutubeDatabase",
		PARENT_ID_COLUMN = "ParentID_VideoYoutube_VideoYoutubeDatabase", 
		TITLE_COLUMN = "Title_VideoYoutube_VideoYoutubeDatabase",
		URL_COLUMN = "Url_VideoYoutube_VideoYoutubeDatabase",
		IMAGE_URL_COLUMN = "PosterImageUrl_VideoYoutube_VideoYoutubeDatabase",
		UPLOAD_DATE_COLUMN = "UploadDate_VideoYoutube_VideoYoutubeDatabase",
		INSERT_DATE_COLUMN = "InsertDate_VideoYoutube_VideoYoutubeDatabase";
	
	public YoutubeChannelVideo(ArrayList<DatabaseResponseNode> drns)
	{
		for(DatabaseResponseNode drn : drns)
		{
			if(drn.getNodeName().equals(ID_COLUMN))
			{
				idVideo = drn.getNodeAttributes().get("content");
			}
			else if(drn.getNodeName().equals(PARENT_ID_COLUMN))
			{
				parentId = Integer.parseInt(drn.getNodeAttributes().get("content"));
			}
			else if(drn.getNodeName().equals(TITLE_COLUMN))
			{
				title = drn.getNodeAttributes().get("content");
			}
			else if(drn.getNodeName().equals(URL_COLUMN))
			{
				url = drn.getNodeAttributes().get("content");
			}
			else if(drn.getNodeName().equals(IMAGE_URL_COLUMN))
			{
				imageUrl = drn.getNodeAttributes().get("content");
			}
			else if(drn.getNodeName().equals(UPLOAD_DATE_COLUMN))
			{
				uploadDate = Long.parseLong(drn.getNodeAttributes().get("content"));
			}
			else if(drn.getNodeName().equals(INSERT_DATE_COLUMN))
			{
				insertDate = Long.parseLong(drn.getNodeAttributes().get("content"));
			}
		}
	}
	
	public String getIdVideo()
	{
		return this.idVideo;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	
	public String getUrl()
	{
		return this.url;
	}
	
	public String getImageUrl()
	{
		return this.imageUrl;
	}
	
	public int getParentId()
	{
		return this.parentId;
	}
	
	public long getUploadDate()
	{
		return this.uploadDate;
	}
	
	public long getInsertDate()
	{
		return this.insertDate;
	}
	
	@Override
	public String toString()
	{
		return 
				this.getIdVideo() + " " + 
				this.getParentId() + " " +  
				this.getTitle() + " " + 
				this.getUrl() + " " + 
				this.getImageUrl() + " " + 
				this.getUploadDate() + " " + 
				this.getInsertDate();
	}
	
}
