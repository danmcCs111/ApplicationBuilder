package Editors;

import java.awt.Component;

import javax.swing.JButton;

import Actions.ScheduledCommand;
import Params.ParameterEditor;
import Properties.PathUtility;

public class ScheduledCommandEditor extends JButton implements ParameterEditor
{
	
	private static final long serialVersionUID = 1L;
	
	private static final String FILE_PATH = PathUtility.getCurrentDirectory() + "src/ApplicationBuilder/scheduledCommands";
	
	private ScheduledCommand sc = null;
	
	public ScheduledCommandEditor()
	{
		this.setText("Edit Command");
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		if(value instanceof String)
			return;
		this.sc = (ScheduledCommand) value;
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {this.sc.toString()};//TODO.
	}

	@Override
	public Object getComponentValueObj() 
	{
		return sc;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return ScheduledCommand.class.getName();
	}
	
}
