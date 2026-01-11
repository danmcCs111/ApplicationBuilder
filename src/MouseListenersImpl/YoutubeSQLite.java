package MouseListenersImpl;

import Properties.PathUtility;

public class YoutubeSQLite implements YoutubeQuery
{
	public String
		YOUTUBE_QUERY = 
			"SELECT * FROM video WHERE VideoName_Video_VideoDatabase = <arg0>"+
			";",
		YOUTUBE_VIDEO_QUERY = 
			"SELECT * FROM videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg0> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase DESC;",
		YOUTUBE_INSERT_PREFIX = 
			"INSERT INTO video (VideoName_Video_VideoDatabase, VideoUrl_Video_VideoDatabase, InsertDate_Video_VideoDatabase) values( ",
		YOUTUBE_INSERT_SUFFIX = 
			" CURRENT_TIMESTAMP);";

	@Override
	public String getYoutubeQuery(String VideoName) 
	{
		return YOUTUBE_QUERY.replaceFirst("<arg0>", PathUtility.surroundString(VideoName, "\'"));
	}

	@Override
	public String getYoutubeVideoQuery(int parentId) 
	{
		return YOUTUBE_VIDEO_QUERY.replaceFirst("<arg0>", parentId +"");
	}

	@Override
	public String getYoutubeInsertPrefix() 
	{
		return YOUTUBE_INSERT_PREFIX;
	}

	@Override
	public String getYoutubeInsertSuffix() 
	{
		return YOUTUBE_INSERT_SUFFIX;
	}
}
