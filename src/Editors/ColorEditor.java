package Editors;

import java.awt.Color;
import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JDialog;

import Params.ParameterEditor;

public class ColorEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 1992L;
	
	private JColorChooser jcc = null;
	
	public ColorEditor()
	{
		super();
		jcc = new JColorChooser();
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				JDialog d = new JDialog();
				d.add(jcc);
				d.setVisible(true);
				d.pack();
			}
		});
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
		return "java.awt.color";
	}

	@Override
	public void setComponentValue(Object value) 
	{
		this.setForeground((Color)value);
		this.setText("[-Color Select-]");
		jcc.setColor((Color)value);
	}

	@Override
	public String[] getComponentValue() 
	{
		return jcc == null
			? new String [] {""}
			: new String [] {jcc.getColor().getRed()+"", 
				jcc.getColor().getGreen()+"",
				jcc.getColor().getBlue()+""};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return jcc.getColor();
	}
}
