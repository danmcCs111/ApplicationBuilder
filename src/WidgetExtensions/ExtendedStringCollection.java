package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

public interface ExtendedStringCollection
{
	public abstract HashMap<String, List<String>> getPathAndFileList();
	public abstract void setPathAndFileList(HashMap<String, List<String>> pathAndFileList);
	
}
