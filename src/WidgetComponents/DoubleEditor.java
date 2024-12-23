package WidgetComponents;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

public class DoubleEditor extends ParameterEditor
{

	private static final int 
		SPINNER_LIMIT = 1000000,
		SPINNER_INTERVAL = 1;
	private static final String SPINNER_FORMAT = "000000.00";
	
	@Override
	public Component getComponentEditor() 
	{
		JSpinner spinnerUpDown = new JSpinner();
        spinnerUpDown.setModel(new SpinnerNumberModel(0, 0, SPINNER_LIMIT, SPINNER_INTERVAL));
        spinnerUpDown.setEditor(new JSpinner.NumberEditor(spinnerUpDown, SPINNER_FORMAT));
		return spinnerUpDown;
	}

	@Override
	public String getComponentXMLOutput() 
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isType(String parameterValueType) 
	{
		// TODO Auto-generated method stub
		return parameterValueType.toLowerCase().equals("double");
	}

}
