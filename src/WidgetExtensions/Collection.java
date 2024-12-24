package WidgetExtensions;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

/**
 * Holds a variable number of Components
 */
public class Collection extends JComponent
{
	private static final long serialVersionUID = 1L;
	
	public List<VariableOptionBase> variableOptions = new ArrayList<VariableOptionBase>();
	
	public void setVariableOptions(List<VariableOptionBase> variableOptions)
	{
		this.variableOptions = variableOptions;
	}
	
}
