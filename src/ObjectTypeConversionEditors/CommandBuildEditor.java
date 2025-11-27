package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;

import Graphics2D.ColorTemplate;
import ObjectTypeConversion.CommandBuild;
import Params.ParameterEditor;
import WidgetComponentDialogs.CommandDialog;

public class CommandBuildEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 1L;
	
	private static final int CHARACTER_LIMIT = 100;
	private static final String 
		LIMIT_POSTFIX = "..",
		DEFAULT_EDITOR_TEXT = "<Click to Enter Command>";
	
	private String commandText = DEFAULT_EDITOR_TEXT;
	private CommandBuild commandArg;
	private CommandDialog commandDialog;
	
	public CommandBuildEditor()
	{
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		this.setText(commandText);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(commandDialog != null && commandDialog.isVisible())
				{
					commandDialog.dispose();
				}
				commandDialog = new CommandDialog(CommandBuildEditor.this, commandArg);
			}
		});
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
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
	
	@Override
	public void setText(String text)
	{
		String t;
		if(text.length() > CHARACTER_LIMIT)
		{
			t=text.substring(0, CHARACTER_LIMIT) + LIMIT_POSTFIX;
		}
		else
		{
			t = text;
		}
		super.setToolTipText(text);
		super.setText(t);
	}
	
}
