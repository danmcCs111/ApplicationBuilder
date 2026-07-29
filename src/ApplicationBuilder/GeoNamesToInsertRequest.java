package ApplicationBuilder;

public class GeoNamesToInsertRequest extends GeoNamesToSql 
{
	public static void main(String [] args)
	{
		GeoNamesToSql gns = new GeoNamesToSql();
		String insertStr = gns.processArgs(args);
		QueryUpdateTool.executeInsert(insertStr);
	}
}
