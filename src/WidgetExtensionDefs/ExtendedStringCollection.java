package WidgetExtensionDefs;

import java.awt.Component;
import java.util.LinkedHashMap;
import java.util.List;

public interface ExtendedStringCollection
{
	public abstract LinkedHashMap<String, List<String>> getPathAndFileList();
	public abstract void setPathAndFileList(LinkedHashMap<String, List<String>> pathAndFileList);
	
	public abstract void setTextPathComponent(Component c);
	public abstract void setPathSelected(String path);
	public abstract String getPathSelected(String path);
	
}
