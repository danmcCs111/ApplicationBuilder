package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ActionListeners.ActionListenerExtension;
import ActionListeners.ActionListenerSubTypeExtension;
import ActionListeners.ConnectedComponent;
import Properties.LoggingMessages;
import WidgetComponents.Direction;

public class NavigationButtonActionListener implements ActionListener, ActionListenerSubTypeExtension
{
	private static int 
		curPosition = 0, 
		lastIndex = 0;
	private static List<ActionListenerExtension> actionListenerExtensions = new ArrayList<ActionListenerExtension>();
	private static ConnectedComponent connectedComp;
	
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
		curPosition = direction.getIndexDirectionNext(getCurPosition(), getLastIndex());
		LoggingMessages.printOut(curPosition + " " + e.getActionCommand() + " last index: " + lastIndex);
		connectedComp.sendIndexUpdate(curPosition);
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
	public void setConnectedComp(ConnectedComponent comp) 
	{
		connectedComp = comp;
	}
	
}
