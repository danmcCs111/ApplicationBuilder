package WidgetComponents;

import java.awt.Component;
import java.awt.Dimension;

import javax.swing.JSpinner;

public class IntegerEditor extends ParameterEditor{

	private static final Dimension SPINNER_SIZE = new Dimension(50, 50);
	
	@Override
	public Component getComponentEditor() {
		JSpinner js = new JSpinner();
		js.setSize(SPINNER_SIZE.width, SPINNER_SIZE.height);
		return js;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isType(String parameterValueType) {
		// TODO Auto-generated method stub
		return parameterValueType.toLowerCase().equals("int");
	}

}
