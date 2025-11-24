package WidgetComponentInterfaces;

import java.io.File;
import java.util.HashMap;

public interface OpenAndSaveKeepsSubscriber 
{
	public void openKeeps(HashMap<String, String> props);
	public void saveKeeps(File filename, String [][] props);
}
