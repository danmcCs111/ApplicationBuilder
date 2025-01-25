package Properties;

public interface PathConstants 
{
	public static final String PATH_STRIP_FILTER = "([\\.]|[0-9\\sa-zA-Z])+[\\\\]"; // only using landing folder name
	
	public static String filterPathToFilename(String path)
	{
		return path.replaceAll(PathConstants.PATH_STRIP_FILTER, "");
	}
}
