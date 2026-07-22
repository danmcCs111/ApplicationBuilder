package MouseListenersImpl;

import java.util.Date;

public interface YoutubeQuery 
{
	public String getYoutubeQuery(String VideoName);
	public String getYoutubeVideoQuery(int parentId);
	public String getYoutubeVideoLatestQuery(int parentId);
	public String getYoutubeVideoFirstQuery(int parentId);
	public String getYoutubeVideoCount(int parentId);
	public String getYoutubeRemoveQuery(int parentId, Date fromDate);
	public String getYoutubeInsertPrefix();
	public String getYoutubeInsertSuffix();
	public String getSqlType();
}
