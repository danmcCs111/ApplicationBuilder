package ApplicationBuilder;

public class SelectGeoNames 
{
	public static void main(String [] args)
	{
//		String sql = "SELECT name FROM GeoNamesDatabase.sqlite_master WHERE type='table';";
//		QueryUpdateTool.executeQuery(sql);
		String sql = "select * from GeoLocation;";
//		String sql = "select * from videodatabase.Video;";
		QueryUpdateTool.executeQuery(sql);
	}
}
