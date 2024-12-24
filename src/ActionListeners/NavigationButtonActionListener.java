package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import WidgetExtensions.Direction;

public class NavigationButtonActionListener implements ActionListener, ActionListenerSubTypeExtension 
{
	private static int 
		curPosition = 0, 
		lastIndex = 0;
	
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
		//TODO action on component
	}
	
	@Override
	public void setActionListenerSubTypeExtension(Class<?> clazz, String type) 
	{
		if(clazz.getName().equals(Direction.class.getName()))
		{
			direction = Direction.getTypeFromString(type);
		}
	}
}
