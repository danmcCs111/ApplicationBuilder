package ObjectTypeConversionEditors;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Params.ParameterEditor;

public class FloatEditor extends JSpinner implements ParameterEditor
{
	private static final long serialVersionUID = 1995L;
	
	private static final int 
		SPINNER_LIMIT = 1000000;
	private static final float
		SPINNER_INTERVAL = .01f;
	private static final String 
		SPINNER_FORMAT = "0.00";
	
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
		if(this.getValue() == null)
		{
			return new String [] {""};
		}
		if(this.getValue() instanceof Double)
		{
			float val = ((Double) this.getValue()).floatValue();
			return new String [] {val+""};
		}
		return new String [] {this.getValue()+""};
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
