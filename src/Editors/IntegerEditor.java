package Editors;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Params.ParameterEditor;

public class IntegerEditor implements ParameterEditor
{
	private static final int 
		SPINNER_LIMIT = 1000000,
		SPINNER_INTERVAL = 1;
	private static final String SPINNER_FORMAT = "0";
	private static final Dimension SPINNER_SIZE = new Dimension(50, 50);
	private SpinnerNumberModel snm = new SpinnerNumberModel(0, 0, SPINNER_LIMIT, SPINNER_INTERVAL);
	
	private JSpinner js = null;
	
	@Override
	public Component getComponentEditor() 
	{
		if(js == null)
		{
			js = new JSpinner();
			js.setModel(snm);
			js.setEditor(new JSpinner.NumberEditor(js, SPINNER_FORMAT));
			js.setSize(SPINNER_SIZE.width, SPINNER_SIZE.height);
		}
		return js;
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
		return "int";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		js.setValue(value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return js == null
			? new String [] {""}
			: new String [] {js.getValue()+""};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return Integer.parseInt(js.getValue()+"");
	}

}
