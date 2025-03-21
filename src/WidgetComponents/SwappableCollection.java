package WidgetComponents;

import java.awt.Component;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ActionListenersImpl.NavigationButtonActionListener;
import Properties.PathUtility;
import WidgetExtensions.ExtendedStringCollection;

/**
 * Holds a variable number of Components and controls/rebuilds child JComponents
 */
public class SwappableCollection extends JPanel implements ExtendedStringCollection
{
	private static final long serialVersionUID = 1880L;
	
	//conceptually holding a collection of components to be swapped/redrawn
	private HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
	private String path;
	private Component pathTextComponent;
	
	@Override
	public HashMap<String, List<String>> getPathAndFileList() 
	{
		return this.pathAndFileList;
	}

	@Override
	public void setPathAndFileList(HashMap<String, List<String>> pathAndFileList) 
	{
		this.pathAndFileList = pathAndFileList;
		NavigationButtonActionListener.setLastIndex(pathAndFileList.size()-1);
	}

	@Override
	public void setPathSelected(String path) 
	{
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
	public String getPathSelected(String path) 
	{
		return this.path;
	}

	@Override
	public void setTextPathComponent(Component c) 
	{
		this.pathTextComponent = c;
	}

}
