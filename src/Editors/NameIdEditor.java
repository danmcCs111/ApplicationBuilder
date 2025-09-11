package Editors;

import java.awt.Component;
import java.util.ArrayList;

import javax.swing.JComboBox;

import EditorAbstract.PostProcess;
import ObjectTypeConversion.NameId;
import Params.ParameterEditor;
import Properties.LoggingMessages;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class NameIdEditor extends JComboBox<NameId> implements ParameterEditor, PostProcess
{
	private static final long serialVersionUID = 1L;
	
	private static final String SET_NAME_ATTR = "setName";
	
	public NameIdEditor()
	{
		
	}
	
	private NameId [] collectAllNameIds()
	{
		ArrayList<NameId> nameIds = new ArrayList<NameId>();
		this.addItem(new NameId(""));
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
		{
			String name = wcp.getSettingsNameAndValue().get(SET_NAME_ATTR);
			String conn = wcp.getSettingsNameAndValue().get("extendedConnectedComponent");
			if(conn != null)
			{
				LoggingMessages.printOut("extendedConnectedComponent: " + conn);
			}
			if(name != null)
			{
				LoggingMessages.printOut("SetName: " + name);
				NameId tmp = new NameId(name);
				this.addItem(tmp);
			}
		}
		return nameIds.toArray(new NameId[nameIds.size()]);
	}
	
	private void clearAllNameIds()
	{
		this.removeAllItems();
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		LoggingMessages.printOut(value+"");
		NameId nameId = (NameId) value;
		this.setSelectedItem(value);
		if(this.getSelectedItem() == null)
		{
			this.addItem(nameId);
			this.setSelectedItem(nameId);
		}
	}

	@Override
	public String[] getComponentValue() 
	{
		return this.getSelectedItem() == null
				? new String [] {""}
				: new String [] {((NameId)this.getSelectedItem()).toString()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return this.getSelectedItem();
	}
	
	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return NameId.class.getName();
	}

	public void postWidgetGenerationProcessing() 
	{
		Object o = getComponentValueObj();
		this.removeAllItems();
		collectAllNameIds();
		this.setComponentValue(o);
	}
	
}
