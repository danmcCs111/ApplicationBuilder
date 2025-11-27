package ObjectTypeConversionEditors;

import java.awt.Component;

import javax.swing.JTextField;

import Params.ParameterEditor;

public class StringEditor extends JTextField implements ParameterEditor
{
	private static final long serialVersionUID = 1999L;
	
	public StringEditor()
	{
		super();
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
		return "java.lang.string";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		this.setText((String)value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return this == null
				? new String [] {""}
				: new String [] {this.getText()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return this.getText();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
