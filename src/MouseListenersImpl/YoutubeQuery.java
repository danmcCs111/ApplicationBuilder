package MouseListenersImpl;

public interface YoutubeQuery 
{
	public String getYoutubeQuery(String VideoName);
	public String getYoutubeVideoQuery(int parentId);
	public String getYoutubeVideoLatestQuery(int parentId);
	public String getYoutubeVideoFirstQuery(int parentId);
	public String getYoutubeVideoCount(int parentId);
	public String getYoutubeInsertPrefix();
	public String getYoutubeInsertSuffix();
	public String getSqlType();
}
