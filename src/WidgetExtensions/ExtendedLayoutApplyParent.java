package WidgetExtensions;

import java.awt.Component;

import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

public class ExtendedLayoutApplyParent 
{
	private static final String METHOD_DEF = "extendedLayoutApplyParent [java.lang.String arg0]";
	
	public static String getMethodDefinition() {
		return METHOD_DEF;
	}

	//TODO cleanup
	public static void applyMethod(JComponent parentComponent, Component component, String layoutApplyParent)
	{
		if(parentComponent instanceof JPanel)
		{
			((JPanel) parentComponent).add(component, layoutApplyParent);
		}
		else if(parentComponent instanceof JScrollPane)
		{
			((JScrollPane) parentComponent).add(component, layoutApplyParent);
		}
	}
	public static void applyMethod(Component parentComponent, Component component, String layoutApplyParent)
	{
		if(parentComponent instanceof JFrame)
		{
			((JFrame) parentComponent).add(component, layoutApplyParent);
		}
	}
}
