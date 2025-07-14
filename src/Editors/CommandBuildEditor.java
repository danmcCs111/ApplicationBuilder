package Editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import ObjectTypeConversion.CommandBuild;
import Params.ParameterEditor;
import WidgetComponents.CommandDialog;

public class CommandBuildEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 1L;
	
	private String commandText = "<Click to Enter Command>";
	private CommandBuild commandArg;

	public CommandBuildEditor()
	{
		this.setText(commandText);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				new CommandDialog(CommandBuildEditor.this, commandArg);
			}
		});
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
		if(value != null)
		{
			this.commandArg = (CommandBuild) value;
			if(!getComponentValue()[0].isEmpty())
			{
				this.setText(getComponentValue()[0]);
			}
			else {
				this.commandArg = null;
			}
		}
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String [] {commandArg.getCommandXmlString()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return this.commandArg;
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return CommandBuild.class.getName();
	}
	
}
