package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import ApplicationBuilder.LoggingMessages;
import WidgetExtensions.ActionListenerExtension;
import WidgetExtensions.Direction;

public class NavigationButtonActionListener implements ActionListener, ActionListenerSubTypeExtension 
{
	private static int 
		curPosition = 0, 
		lastIndex = 0;
	private static List<ActionListenerExtension> actionListenerExtensions = new ArrayList<ActionListenerExtension>();
	
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
		LoggingMessages.printOut(curPosition + "");
		//TODO action on component
	}
	
	@Override
	public void setActionListenerSubTypeExtension(Class<?> clazz, String attr) 
	{
		if(clazz.getName().equals(Direction.class.getName()))
		{
			direction = Direction.getTypeFromString(attr);
		}
	}
	
	public static void addActionListenerExtension(ActionListenerExtension ale)
	{
		actionListenerExtensions.add(ale);
	}
}
