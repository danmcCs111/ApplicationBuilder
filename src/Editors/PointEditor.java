package Editors;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.Point;

import javax.swing.JPanel;
import javax.swing.JSpinner;

import ApplicationBuilder.LoggingMessages;
import Params.ParameterEditor;

public class PointEditor extends ParameterEditor 
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
		return Point.class.getName();
	}

	@Override
	public void setComponentValue(Object value) 
	{
		Point p = (Point) value;
		spin1.setValue((int)p.x);
		spin2.setValue((int)p.y);
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {Integer.parseInt(spin1.getValue()+"")+"", Integer.parseInt(spin2.getValue()+"")+""};
	}

}
