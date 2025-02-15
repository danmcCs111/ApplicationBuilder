package WidgetExtensions;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;

import javax.swing.JScrollPane;

import ApplicationBuilder.LoggingMessages;

public class JScrollPaneResizer extends JScrollPane implements ResizerListener
{
	private static final long serialVersionUID = 1882L;

	public JScrollPaneResizer()
	{
		super();
		defaultSettings();
	}
	
	public JScrollPaneResizer(Component c)
	{
		super(c);
		defaultSettings();
	}
	
	private void defaultSettings()
	{
		getVerticalScrollBar().setUnitIncrement(15);
	}
	
	@Override
	public void heightLimitEvent(boolean isFrameHeightLimited) 
	{
		Container parent = this.getParent();
		parent.remove(this);
		if(isFrameHeightLimited)
		{
			LoggingMessages.printOut("frame height limited");
			parent.add(this, BorderLayout.CENTER);
		}
		else
		{
			LoggingMessages.printOut("frame height not limited");
			parent.add(this, BorderLayout.NORTH);
		}
		parent.validate();
	}
}
