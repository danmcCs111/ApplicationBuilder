package WidgetExtensions;

import java.awt.event.ActionListener;

public interface VariableOptionBase
{
	public abstract String getVariableOptionData();
	
	public abstract String getVariableOptionDisplayName();
	
	public abstract boolean hasVariableOptionActionListener();
	
	public abstract void addVariableOptionActionListener(ActionListener al);
}
