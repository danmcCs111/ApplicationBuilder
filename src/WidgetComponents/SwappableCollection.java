package WidgetComponents;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.JPanel;

import ActionListeners.ConnectedComponent;
import ActionListenersImpl.NavigationButtonActionListener;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.ButtonArray;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionDefs.ExtendedStringCollection;
import WidgetExtensionInterfaces.RefreshActionExtension;

/**
 * Holds a variable number of Components and controls/rebuilds child JComponents
 */
public class SwappableCollection extends JPanel implements 
ExtendedStringCollection, SearchSubscriber, ConnectedComponent, RefreshActionExtension,  PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1880L;
	
	public static ArrayList<String> 
		indexPaths = new ArrayList<String>();
	private static int 
		fileCount = 0;
	
	private LinkedHashMap<String, List<String>> 
		pathAndFileList = new LinkedHashMap<String, List<String>>();
	private LinkedHashMap<String, String> 
		pathAndExtension = new LinkedHashMap<String, String>();
	private String 
		path;
	private Component 
		pathTextComponent;
	private boolean 
		isLoadingGraphic = false;

	public int getFileCount()
	{
		return fileCount;
	}
	
	public void setIsLoadingGraphic(boolean loadGraphic)
	{
		this.isLoadingGraphic = loadGraphic;
	}
	
	private int getCollectionSize()
	{
		int count = 0;
		for(String key : pathAndFileList.keySet())
		{
			count += pathAndFileList.get(key).size();
		}
		return count;
	}
	
	public void setPathAndExtension(String path, String extension)
	{
		this.pathAndExtension.put(path, extension);
	}
	
	@Override
	public LinkedHashMap<String, List<String>> getPathAndFileList() 
	{
		return this.pathAndFileList;
	}

	@Override
	public void setPathAndFileList(LinkedHashMap<String, List<String>> pathAndFileList) 
	{
		this.pathAndFileList = pathAndFileList;
		fileCount = getCollectionSize();
		LoggingMessages.printOut("File count: " + fileCount);
		ButtonArray buttonArray = (ButtonArray) ExtendedAttributeParam.findComponentWithInterface(ButtonArray.class);
		buttonArray.setIsLoadingSpinGraphic(isLoadingGraphic);
		buttonArray.buildLoadingFrame();
		NavigationButtonActionListener.setLastIndex(pathAndFileList.size()-1);
	}

	@Override
	public void setPathSelected(String path) 
	{
		if(this.pathTextComponent == null)
			return;
		
		this.path = path;
		Method m;
		try {
			m = this.pathTextComponent.getClass().getMethod("setText", String.class);
			m.invoke(this.pathTextComponent, PathUtility.filterPathToFilename(path));
		} catch (NoSuchMethodException | SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getPathSelected() 
	{
		return this.path;
	}

	@Override
	public void setTextPathComponent(Component c) 
	{
		this.pathTextComponent = c;
	}

	@Override
	public void notifySearchText(String searchPattern) 
	{
		ButtonArray buttonArray = (ButtonArray) ExtendedAttributeParam.findComponentWithInterface(ButtonArray.class);
		buttonArray.adjustVisibility(searchPattern);
	}

	@Override
	public void sendIndexUpdate(int index) 
	{
		String key = null;
		java.util.Iterator<String> it = pathAndFileList.keySet().iterator();
		while(index-- >= 0)
		{
			key = it.next();
		}
		
		LoggingMessages.printOut(key);
		ButtonArray buttonArray = (ButtonArray) ExtendedAttributeParam.findComponentWithInterface(ButtonArray.class);//TODO
		buttonArray.addJButtons(key, pathAndFileList.get(key), index);
	}
	
	public void rebuildPathFileList()
	{
		ArrayList<String> fileList = PathUtility.getOSFileList(getPathSelected(), pathAndExtension.get(getPathSelected()));
		pathAndFileList.put(path, fileList);
	}

	@Override
	public void postExecute() 
	{
		int indexPos = NavigationButtonActionListener.getCurPosition();
		ButtonArray buttonArray = (ButtonArray) ExtendedAttributeParam.findComponentWithInterface(ButtonArray.class);//TODO
		buttonArray.addJButtons(indexPaths.get(indexPos), pathAndFileList.get(indexPaths.get(indexPos)), indexPos);
	}

	@Override
	public void refresh() 
	{
		rebuildPathFileList();
		int indexPos = NavigationButtonActionListener.getCurPosition();
		ButtonArray buttonArray = (ButtonArray) ExtendedAttributeParam.findComponentWithInterface(ButtonArray.class);//TODO
		String path = indexPaths.get(indexPos);
		int indexPl = indexPaths.indexOf(path);
		indexPaths.remove(indexPl);
		buttonArray.refreshJButtons(path, pathAndFileList.get(path), indexPl, indexPl);
	}

}
