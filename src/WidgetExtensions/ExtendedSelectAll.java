package WidgetExtensions;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.AbstractButton;

import ObjectTypeConversion.NameId;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionInterfaces.SelectAllAction;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedSelectAll implements ExtendedAttributeParam, SelectAllAction
{
	private SelectAllAction
		selectAll = null;
	private AbstractButton
		ab = null;
	
	public void applyMethod(NameId arg0, WidgetCreatorProperty widgetProperties) 
	{
		ab = (AbstractButton) widgetProperties.getInstance();
		
		Object o = WidgetBuildController.getInstance().findRefByName(arg0.getNameId()).getInstance();
		if(o instanceof SelectAllAction)
		{
			selectAll = (SelectAllAction) o;
			selectAll.addSelectAllSubscriber(this);
		}
		ab.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				selectAll.isSelectAll(ab.isSelected());
			}
		});
	}

	@Override
	public void isSelectAll(boolean selectAll) 
	{
		if(ab != null)
		{
			ab.setSelected(selectAll);
		}
	}

	@Override
	public void addSelectAllSubscriber(SelectAllAction selectAll) {
		// TODO Auto-generated method stub
	}

}
