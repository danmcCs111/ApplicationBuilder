package WidgetExtensions;

import java.awt.event.ActionListener;

public class JButtonVariableOption implements VariableOptionBase 
{
	private String 
		optionData = null,
		displayName = null;
	
	private ActionListener actionListener = null;
	
	public void setVariableOptionData(String optionData)
	{
		this.optionData = optionData;
	}
	
	public void setVariableOptionDisplayName(String displayName)
	{
		this.displayName = displayName;
	}
	
	@Override
	public String getVariableOptionData() 
	{
		return this.optionData;
	}

	@Override
	public String getVariableOptionDisplayName() 
	{
		return this.displayName;
	}

	@Override
	public boolean hasVariableOptionActionListener() 
	{
		return this.actionListener != null;
	}

	@Override
	public void addVariableOptionActionListener(ActionListener al) 
	{
		this.actionListener = al;
	}
}
