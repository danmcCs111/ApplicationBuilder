package Editors;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Params.ParameterEditor;

public class FloatEditor extends JSpinner implements ParameterEditor
{
	private static final long serialVersionUID = 1995L;
	
	private static final int 
		SPINNER_LIMIT = 1000000,
		SPINNER_INTERVAL = 1;
	private static final String SPINNER_FORMAT = "000000.00";
	
	public FloatEditor()
	{
		super();
		this.setModel(new SpinnerNumberModel(0, 0, SPINNER_LIMIT, SPINNER_INTERVAL));
		this.setEditor(new JSpinner.NumberEditor(this, SPINNER_FORMAT));
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
		return "float";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		this.setValue(value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return this == null
				? new String [] {""}
				: new String [] {this.getValue()+""};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return this.getValue();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
