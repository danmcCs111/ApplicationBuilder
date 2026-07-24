package MouseListenersImpl;

import java.text.SimpleDateFormat;
import java.util.Date;

import Properties.PathUtility;
import Properties.StringUtility;

public class YoutubeSQLite implements YoutubeQuery
{
	private static SimpleDateFormat
		SDF = new SimpleDateFormat("yyyy-MM-dd");
	public static String
		REPLACE="<arg>",
		YOUTUBE_QUERY = 
			"SELECT * FROM video WHERE VideoUrl_Video_VideoDatabase = <arg>" +
			";",
		YOUTUBE_REMOVE_QUERY = "DELETE FROM videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg> " + 
			" AND UploadDate_VideoYoutube_VideoYoutubeDatabase < <arg>;",
		YOUTUBE_VIDEO_QUERY = 
			"SELECT * FROM videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase DESC;",
		YOUTUBE_VIDEO_LATEST_QUERY = 
			"SELECT * FROM videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase DESC LIMIT 1;",
		YOUTUBE_VIDEO_FIRST_QUERY = 
			"SELECT * FROM videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase ASC LIMIT 1;",
		YOUTUBE_VIDEO_COUNT_QUERY = 
			"SELECT count(*) as Count FROM videoYoutube WHERE ParentID_VideoYoutube_VideoYoutubeDatabase = <arg> "+
			" ORDER BY UploadDate_VideoYoutube_VideoYoutubeDatabase;",
		YOUTUBE_INSERT_PREFIX = 
			"INSERT INTO video (VideoName_Video_VideoDatabase, VideoUrl_Video_VideoDatabase, InsertDate_Video_VideoDatabase) values( ",
		YOUTUBE_INSERT_SUFFIX = 
			" CURRENT_TIMESTAMP);";

	private static String 
		TYPE = "SQLite";
	
	public static boolean isType(String type)
	{
		return TYPE.equals(type);
	}
	
	@Override
	public String getYoutubeQuery(String VideoUrl) 
	{
		return YOUTUBE_QUERY.replaceFirst(REPLACE, PathUtility.surroundString(VideoUrl, "\'"));
	}

	@Override
	public String getYoutubeVideoQuery(int parentId) 
	{
		return YOUTUBE_VIDEO_QUERY.replaceFirst(REPLACE, parentId +"");
	}
	
	@Override
	public String getYoutubeVideoLatestQuery(int parentId) 
	{
		return YOUTUBE_VIDEO_LATEST_QUERY.replaceFirst(REPLACE, parentId +"");
	}
	
	@Override
	public String getYoutubeVideoFirstQuery(int parentId) 
	{
		return YOUTUBE_VIDEO_FIRST_QUERY.replaceFirst(REPLACE, parentId +"");
	}
	
	@Override
	public String getYoutubeVideoCount(int parentId) 
	{
		return YOUTUBE_VIDEO_COUNT_QUERY.replaceFirst(REPLACE, parentId +"");
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

	@Override
	public String getYoutubeRemoveQuery(int parentId, Date fromDate) 
	{
		String date = "'" + SDF.format(fromDate) + "'";
		return StringUtility.replaceArg(YOUTUBE_REMOVE_QUERY, REPLACE, new String[] {parentId+"", date});
	}

}
