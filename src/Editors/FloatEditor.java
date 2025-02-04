package Editors;

import java.awt.Component;

import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import WidgetComponents.ParameterEditor;

public class FloatEditor extends ParameterEditor
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
	public String getParameterDefintionString() 
	{
		return "float";
	}

}
