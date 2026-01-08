package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Params.ParameterEditor;

public class IntEditor extends JSpinner implements ParameterEditor
{
	private static final long serialVersionUID = 1996L;
	
	private static final int 
		SPINNER_LIMIT = 1000000,
		SPINNER_INTERVAL = 1;
	private static final String SPINNER_FORMAT = "0";
	private static final Dimension SPINNER_SIZE = new Dimension(50, 50);
	private SpinnerNumberModel snm = new SpinnerNumberModel(0, 0, SPINNER_LIMIT, SPINNER_INTERVAL);
	
	public IntEditor()
	{
		super();
		this.setModel(snm);
		this.setEditor(new JSpinner.NumberEditor(this, SPINNER_FORMAT));
		this.setSize(SPINNER_SIZE.width, SPINNER_SIZE.height);
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
		return "int";
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
		return Integer.parseInt(this.getValue()+"");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
