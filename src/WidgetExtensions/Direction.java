package WidgetExtensions;

import ActionListeners.NavigationButtonActionListener;

public enum Direction implements ActionListenerExtension 
{
	BACKWARD(-1),
	FORWARD(1);
	
	private int dir = 0;
	
	private Direction(int dir)
	{
		this.dir = dir;
	}
	
	public static Direction getTypeFromString(String type)
	{
		for(Direction d : Direction.values()) 
		{
			if(d.toString().toLowerCase().equals(type.toLowerCase()))
			{
				return d;
			}
		}
		return null;
	}
	
	public int getValue()
	{
		return dir;
	}
	
	public int getIndexDirectionNext()
	{
		int 
			curPosition = NavigationButtonActionListener.getCurPosition(), 
			lastIndex = NavigationButtonActionListener.getLastIndex();
		int 
			indexEnd = lastIndex,
			indexReturn = 0;
		
		switch (this) 
		{
			case FORWARD:
				if(indexEnd < curPosition + 1)
					indexReturn = 0;
				else
					indexReturn = curPosition + 1;
				break;
				
			case BACKWARD:
				if(0 > curPosition - 1)
					indexReturn = indexEnd;
				else
					indexReturn = curPosition - 1;
				break;
		}
		return indexReturn;
	}

	@Override
	public void actionListenerEvent() 
	{
		NavigationButtonActionListener.setCurPosition(getIndexDirectionNext());
	}

	@Override
	public void applyActionListenerExtensionAttribute(String attribute) 
	{
		NavigationButtonActionListener.addActionListenerExtension(getTypeFromString(attribute));
	}

}
