package Editors;

import java.awt.Component;

import javax.swing.JComboBox;

import Params.ParameterEditor;

public class BooleanEditor extends JComboBox<String> implements ParameterEditor
{
	private static final long serialVersionUID = 1991L;
	
	private static final String [] options = new String[] {
			"",
			"True",
			"False"
	};
	
	public BooleanEditor()
	{
		super();
		for(String s : options) this.addItem(s);
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public String getComponentXMLOutput() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return "boolean";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		for(int i=0; i < this.getItemCount(); i++)
		{
			if(this.getItemAt(i).toLowerCase().equals(value.toString().toLowerCase()))
				this.setSelectedItem(this.getItemAt(i));
		}
	}

	@Override
	public String [] getComponentValue() 
	{
		return this==null //case during creation
			? new String [] {""}
			: new String [] {this.getSelectedItem().toString()};
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
