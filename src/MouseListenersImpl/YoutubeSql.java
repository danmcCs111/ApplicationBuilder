package MouseListenersImpl;

import Properties.PathUtility;

public class YoutubeSql implements YoutubeQuery
{
	public static String
		YOUTUBE_QUERY = 
			"SELECT * FROM videodatabase.video WHERE VideoName_Video_VideoDatabase = <arg0>"+
			";",
		YOUTUBE_VIDEO_QUERY = 
			"SELECT * FROM videodatabase.videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg0> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase DESC;",
		YOUTUBE_INSERT_PREFIX = 
			"INSERT INTO videodatabase.video (VideoName_Video_VideoDatabase, VideoUrl_Video_VideoDatabase, InsertDate_Video_VideoDatabase) values( ",
		YOUTUBE_INSERT_SUFFIX = 
			" CURRENT_TIMESTAMP);";

	private static String TYPE = "SQL";
	
	public static boolean isType(String type)
	{
		return TYPE.equals(type);
	}
	
	@Override
	public String getYoutubeQuery(String VideoName) 
	{
		return YOUTUBE_QUERY.replaceFirst("<arg0>", PathUtility.surroundString(VideoName, "\""));
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

	@Override
	public String getSqlType() 
	{
		return TYPE;
	}

}
