package WidgetExtensions;

import java.awt.Component;
import java.util.HashMap;
import java.util.List;

public interface ExtendedStringCollection
{
	public abstract HashMap<String, List<String>> getPathAndFileList();
	public abstract void setPathAndFileList(HashMap<String, List<String>> pathAndFileList);
	
	public abstract void setTextPathComponent(Component c);
	public abstract void setPathSelected(String path);
	public abstract String getPathSelected(String path);
	
}
