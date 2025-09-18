package WidgetExtensionsImpl;

import java.awt.Component;
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import WidgetComponents.ComboSelectionDialog;
import WidgetExtensions.CloseActionExtension;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCloseActionListener implements ExtendedAttributeStringParam
{
	private CloseActionExtension cae = null;
	
	public static final String 
		DIALOG_TITLE = "Close Selector",
		DIALOG_MESSAGE = "Select Items to Close",
		CLOSE_BUTTON_TEXT = "Close",
		CLOSE_ALL_BUTTON_TEXT = "Close All";
//		CANCEL_BUTTON_TEXT ="Cancel";
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		Object m = widgetProperties.getInstance();
		WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
		Object o = wcp.getInstance();
		
		if(o instanceof CloseActionExtension)
		{
			cae = (CloseActionExtension) o;
		}
		if(m instanceof AbstractButton)
		{
			AbstractButton mi = (AbstractButton) m;
			mi.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					ComboSelectionDialog csd = new ComboSelectionDialog();
					csd.buildAndShow(
							cae.getSelectionValues(), 
							DIALOG_TITLE, 
							DIALOG_MESSAGE, 
							CLOSE_BUTTON_TEXT,
							CLOSE_ALL_BUTTON_TEXT,
							null,
							(ComboListDialogSelectedListener) cae.getCloseListener(), 
							((Component)cae).getParent());
				}
			});
		}
	}

}
