package Editors;

import java.awt.Component;

import javax.swing.JComboBox;

import Params.ParameterEditor;

public class BooleanEditor extends ParameterEditor
{
	private JComboBox<String> trueOrFalse = null;
	private static final String [] options = new String[] {
			"",
			"True",
			"False"
	};
	
	@Override
	public Component getComponentEditor() 
	{
		if(trueOrFalse == null)
		{
			trueOrFalse = new JComboBox<String>();
			for(String s : options) trueOrFalse.addItem(s);
		}
		return trueOrFalse;
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
		for(int i=0; i < trueOrFalse.getItemCount(); i++)
		{
			if(trueOrFalse.getItemAt(i).toLowerCase().equals(value.toString().toLowerCase()))
				trueOrFalse.setSelectedItem(trueOrFalse.getItemAt(i));
			
		}
	}

	@Override
	public String [] getComponentValue() 
	{
		return trueOrFalse==null //case during creation
			? new String [] {""}
			: new String [] {trueOrFalse.getSelectedItem().toString()};
	}

}
