package WidgetExtensions;

import java.awt.event.ActionListener;

import javax.swing.AbstractButton;
import javax.swing.text.JTextComponent;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import WidgetComponents.JButtonLengthLimited;
import WidgetExtensionDefs.ExtendedAttributeStringParam;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedLaunchUrlTextSubscriber implements ExtendedAttributeStringParam, ArrayActionListener
{
	private static String 
		DEFAULT_TEXT = "<stopped>";
	private JTextComponent 
		textComp = null;
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		Object m = widgetProperties.getInstance();
		if(!arg0.isEmpty())
		{
			DEFAULT_TEXT = arg0;
		}
		if(m instanceof JTextComponent)
		{
			textComp = (JTextComponent) m;
		}
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
	}

	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		if(newButton == null)
		{
			textComp.setText(DEFAULT_TEXT);
			return;
		}
		if(newButton instanceof JButtonLengthLimited)
		{
			JButtonLengthLimited jbll = (JButtonLengthLimited)newButton;
			String 
				textParent = jbll.getHighlightButton().getText(),
				textChild = jbll.getText(),
				text = "";
			if(!textChild.equals(textParent))
			{
				text = "[ " + textParent + " ] -- ";
				text += textChild;
				textComp.setText(text);
				textComp.setCaretPosition(0);
				return;//
			}
		}
		
		textComp.setText("[ " + newButton.getText() + " ]");
		textComp.setCaretPosition(0);
	}

	@Override
	public void addArrayActionListener() {
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	@Override
	public void removeArrayActionListener() {
		LaunchUrlActionListener.removeArrayActionListener(this);
	}

	@Override
	public void addStripFilter(String filter) {
		// TODO Auto-generated method stub
		
	}

}
