package WidgetExtensions;

import java.util.HashMap;
import java.util.List;

import javax.swing.JPanel;

import ActionListeners.NavigationButtonActionListener;

/**
 * Holds a variable number of Components and controls/rebuilds child JComponents
 */
public class SwappableCollection extends JPanel implements ExtendedStringCollection
{
	private static final long serialVersionUID = 1L;

	//conceptually holding a collection of components to be swapped/redrawn
	private HashMap<String, List<String>> pathAndFileList = new HashMap<String, List<String>>();
	
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
	
}
