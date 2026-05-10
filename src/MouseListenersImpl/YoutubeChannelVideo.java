package MouseListenersImpl;

import java.util.ArrayList;
import java.util.Date;

import HttpDatabaseResponse.DatabaseResponseNode;

public class YoutubeChannelVideo 
{
	private static final String 
		ID_COLUMN = "Id_VideoYoutube_VideoYoutubeDatabase",
		PARENT_ID_COLUMN = "ParentID_VideoYoutube_VideoYoutubeDatabase", 
		TITLE_COLUMN = "Title_VideoYoutube_VideoYoutubeDatabase",
		URL_COLUMN = "Url_VideoYoutube_VideoYoutubeDatabase",
		IMAGE_URL_COLUMN = "PosterImageUrl_VideoYoutube_VideoYoutubeDatabase",
		DURATION_COLUMN = "Duration_VideoYoutube_VideoYoutubeDatabase",
		UPLOAD_DATE_COLUMN = "UploadDate_VideoYoutube_VideoYoutubeDatabase",
		INSERT_DATE_COLUMN = "InsertDate_VideoYoutube_VideoYoutubeDatabase";
	
	private int 
		parentId = -1;
	private String
		idVideo = null,
		title = null,
		url = null,
		imageUrl = null,
		duration;
	private long
		uploadDate = -1,
		insertDate = -1;
//	private Image 
//		imgPng;
	
	public YoutubeChannelVideo(ArrayList<DatabaseResponseNode> drns)
	{
		for(DatabaseResponseNode drn : drns)
		{
			String value = drn.getNodeAttributes().get("content");
			switch(drn.getNodeName())
			{
			case ID_COLUMN:
				idVideo = value;
				break;
			case PARENT_ID_COLUMN:
				parentId = Integer.parseInt(value);
				break;
			case TITLE_COLUMN:
				title = value;
				break;
			case URL_COLUMN:
				url = value;
				break;
			case IMAGE_URL_COLUMN:
				imageUrl = value;
				break;
			case DURATION_COLUMN:
				duration = value;
				break;
			case UPLOAD_DATE_COLUMN:
				uploadDate = DateParser.getDate(drn.getNodeAttributes()).getTime();
				break;
			case INSERT_DATE_COLUMN:
				insertDate = DateParser.getDate(drn.getNodeAttributes()).getTime();
				break;
			}
		}
	}
	
//	public void setImagePng(Image img)
//	{
//		this.imgPng = img;
//	}
//	
//	public Image getImagePng()
//	{
//		return this.imgPng;
//	}
	
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
	
	public String getDuration()
	{
		return this.duration;
	}
	
	public int getParentId()
	{
		return this.parentId;
	}
	
	public long getUploadDateEpoch()
	{
		return this.uploadDate;
	}
	public Date getUploadDate()
	{
		return new Date(this.uploadDate);
	}
	
	public long getInsertDateEpoch()
	{
		return this.insertDate;
	}
	public Date getInsertDate()
	{
		return new Date(this.insertDate);
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
			this.getInsertDate() + " " //+ 
//			"[" + this.getImagePng().toString() + "]"
			;
	}
	
}
