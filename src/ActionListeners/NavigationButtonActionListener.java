package ActionListeners;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import Properties.LoggingMessages;
import WidgetComponents.Direction;
import WidgetComponents.JButtonArray;
import WidgetComponents.SwappableCollection;
import WidgetExtensions.ExtendedAttributeParam;

public class NavigationButtonActionListener implements ActionListener, ActionListenerSubTypeExtension 
{
	private static int 
		curPosition = 0, 
		lastIndex = 0;
	private static List<ActionListenerExtension> actionListenerExtensions = new ArrayList<ActionListenerExtension>();
	private static Component connectedComp;
	
	private Direction direction = null;
	
	public static void setCurPosition(int position)
	{
		curPosition = position;
	}
	public static int getCurPosition()
	{
		return curPosition;
	}
	
	public static void setLastIndex(int indexLength)
	{
		lastIndex = indexLength;
	}
	public static int getLastIndex()
	{
		return lastIndex;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		curPosition = direction.getIndexDirectionNext();
		LoggingMessages.printOut(curPosition + " " + e.getActionCommand());
		
		SwappableCollection comp = (SwappableCollection) connectedComp;
		HashMap<String, List<String>> pathAndFileList = comp.getPathAndFileList();
		
		int index = curPosition;
		String key = null;
		java.util.Iterator<String> it = pathAndFileList.keySet().iterator();
		while(index-- >= 0)
		{
			key = it.next();
		}
		
		LoggingMessages.printOut(key);
		JButtonArray buttonArray = (JButtonArray) ExtendedAttributeParam.findComponent(JButtonArray.class);
		buttonArray.addJButtons(key, pathAndFileList.get(key), curPosition);
	}
	
	@Override
	public void setActionListenerSubTypeExtension(Class<?> clazz, String attr) 
	{
		if(clazz.equals(Direction.class))
		{
			direction = Direction.getTypeFromString(attr);
		}
	}
	
	public static void addActionListenerExtension(ActionListenerExtension ale)
	{
		actionListenerExtensions.add(ale);
	}
	
	@Override
	public void setConnectedComp(Component comp) 
	{
		connectedComp = comp;
	}
	
}
