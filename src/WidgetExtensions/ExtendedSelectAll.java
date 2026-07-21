package WidgetExtensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ObjectTypeConversion.NameId;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.SelectAllAction;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSelectAll implements ExtendedAttributeParam
{
	private SelectAllAction
		selectAll = null;
	
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		AbstractButton ab = (AbstractButton) widgetProperties.getInstance();
		ab.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(selectAll == null)
				{
					Object o = WidgetBuildController.getInstance().findRefByName(arg0.getNameId()).getInstance();
					if(o instanceof SelectAllAction)
					{
						selectAll = (SelectAllAction) o;
					}
				}
				selectAll.isSelectAll(ab.isSelected());
			}
		});
	}

}
