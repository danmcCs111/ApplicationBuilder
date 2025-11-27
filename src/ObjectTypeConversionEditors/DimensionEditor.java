package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Params.ParameterEditor;

public class DimensionEditor extends JPanel implements ParameterEditor 
{
	private static final long serialVersionUID = 1993L;
	
	private static final int 
		SPINNER_LIMIT = 1000000,
		SPINNER_INTERVAL = 1;
	private static final String SPINNER_FORMAT = "0";
	private SpinnerNumberModel snm1 = new SpinnerNumberModel(0, 0, SPINNER_LIMIT, SPINNER_INTERVAL);
	private SpinnerNumberModel snm2 = new SpinnerNumberModel(0, 0, SPINNER_LIMIT, SPINNER_INTERVAL);

	private JSpinner spin1, spin2;
	
	public DimensionEditor()
	{
		super();
		spin1 = new JSpinner();
		spin1.setModel(snm1);
		spin1.setEditor(new JSpinner.NumberEditor(spin1, SPINNER_FORMAT));
		
		spin2 = new JSpinner();
		spin2.setModel(snm2);
		spin2.setEditor(new JSpinner.NumberEditor(spin2, SPINNER_FORMAT));
		
		this.setLayout(new GridLayout(0,1));
		this.add(spin1);
		this.add(spin2);
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
		return Dimension.class.getName();
	}

	@Override
	public void setComponentValue(Object value) 
	{
		Dimension d = (Dimension) value;
		spin1.setValue((int)d.getWidth());
		spin2.setValue((int)d.getHeight());
	}

	@Override
	public String[] getComponentValue() 
	{
		return this == null
			? new String [] {""}
			: new String [] {Integer.parseInt(spin1.getValue()+"")+"", Integer.parseInt(spin2.getValue()+"")+""};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new Dimension(Integer.parseInt(spin1.getValue()+""), Integer.parseInt(spin2.getValue()+""));
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

}
