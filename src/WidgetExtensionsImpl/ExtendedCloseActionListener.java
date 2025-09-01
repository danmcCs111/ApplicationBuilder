package WidgetExtensionsImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenuItem;

import WidgetComponentInterfaces.DialogParentReferenceContainer;
import WidgetComponents.ComboSelectionDialog;
import WidgetExtensions.CloseActionExtension;
import WidgetExtensions.ComboListDialogSelectedListener;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedCloseActionListener implements ExtendedAttributeStringParam
{

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
		
		if(m instanceof JMenuItem)
		{
			JMenuItem mi = (JMenuItem) m;
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
					if(wcp != null)
					{
						Object o = wcp.getInstance();
						if(o instanceof CloseActionExtension)
						{
							CloseActionExtension cae = ((CloseActionExtension) o);
							ComboSelectionDialog csd = new ComboSelectionDialog();
							csd.buildAndShow(
									cae.getSelectionValues(), 
									DIALOG_TITLE, 
									DIALOG_MESSAGE, 
									CLOSE_BUTTON_TEXT,
									CLOSE_ALL_BUTTON_TEXT,
									null,
									(ComboListDialogSelectedListener) cae.getCloseListener(), 
									(DialogParentReferenceContainer) cae.getCloseListener());
						}
					}
				}
			});
		}
	}

}
