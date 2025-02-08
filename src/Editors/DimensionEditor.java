package Editors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;

import ApplicationBuilder.LoggingMessages;
import Params.ParameterEditor;

public class DimensionEditor extends ParameterEditor 
{

	private JPanel dimPanel;
	private JSpinner spin1, spin2;
	
	@Override
	public Component getComponentEditor() 
	{
		if(dimPanel == null)
		{
			dimPanel = new JPanel();
			dimPanel.setLayout(new GridLayout(0,1));
			spin1 = new JSpinner();
			spin2 = new JSpinner();
			dimPanel.add(spin1);
			dimPanel.add(spin2);
		}
		return dimPanel;
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
		return Dimension.class.getName();
	}

	@Override
	public void setComponentValue(Object value) 
	{
		LoggingMessages.printOut("Dimension Value: " + value);
		Dimension d = (Dimension) value;
		spin1.setValue((int)d.getWidth());
		spin2.setValue((int)d.getHeight());
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {Integer.parseInt(spin1.getValue()+"")+"", Integer.parseInt(spin2.getValue()+"")+""};
	}


}
