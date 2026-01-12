package MouseListenersImpl;

public interface YoutubeQuery 
{
	public String getYoutubeQuery(String VideoName);
	public String getYoutubeVideoQuery(int parentId);
	public String getYoutubeInsertPrefix();
	public String getYoutubeInsertSuffix();
	public String getSqlType();
}
