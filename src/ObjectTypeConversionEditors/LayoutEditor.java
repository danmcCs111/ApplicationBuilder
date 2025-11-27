package ObjectTypeConversionEditors;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.LayoutManager;

import javax.swing.JComboBox;

import Params.ParameterEditor;

public class LayoutEditor extends JComboBox<String> implements ParameterEditor 
{
	private static final long serialVersionUID = 1997L;
	
	private static final String [] layoutManagers = new String [] {
			GridLayout.class.getName(),
			BorderLayout.class.getName(),
			FlowLayout.class.getName()
	};//for now just standard library managers
	
	public LayoutEditor()
	{
		super(layoutManagers);
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		if(value != null)
		{
			this.setSelectedItem(value.getClass().getName());//using class name
		}
	}

	@Override
	public String[] getComponentValue() 
	{
		return this == null
			? new String [] {""}
			: new String [] {this.getSelectedItem().toString()};
	}

	@Override
	public String getComponentXMLOutput() {
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return LayoutManager.class.getName();
	}

	@Override
	public Object getComponentValueObj() 
	{
		return this.getSelectedItem();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
