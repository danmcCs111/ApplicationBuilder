package Editors;

import java.awt.Component;

import javax.swing.JComboBox;

import ObjectTypeConversion.LookAndFeelClassName;
import Params.ParameterEditor;
import Properties.LoggingMessages;

public class LookAndFeelClassNameEditor extends JComboBox<String> implements ParameterEditor 
{
	private static final long serialVersionUID = 1L;
	
	private static final String [] lookAndFeelKeys = LookAndFeelClassName.getLookAndFeelKeys();

	public LookAndFeelClassNameEditor()
	{
		super(lookAndFeelKeys);
	}
	
	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		LoggingMessages.printOut("look and feel: " + value);
		if(value != null)
		{
			this.setSelectedItem(((LookAndFeelClassName) value).getKey());//using class name
		}
	}

	@Override
	public String[] getComponentValue() {
		return this == null
				? new String [] {""}
				: new String [] {(String) this.getSelectedItem()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new LookAndFeelClassName((String) this.getSelectedItem());
	}

	@Override
	public String getParameterDefintionString() 
	{
		return LookAndFeelClassName.class.getName();
	}
	
	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

}
