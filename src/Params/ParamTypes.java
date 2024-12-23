package Params;

import ClassDefintions.ActionListenerConverter;
import ClassDefintions.ColorConverter;
import ClassDefintions.IntConverter;
import ClassDefintions.LayoutManagerConverter;
import ClassDefintions.PointConverter;
import ClassDefintions.StringConverter;
import ClassDefintions.StringToObjectConverter;

public enum ParamTypes 
{
	String(new StringConverter(), String.class.getName()),
	Int(new IntConverter(), int.class.getName()),
	Color(new ColorConverter(), java.awt.Color.class.getName()),
	LayoutManager(new LayoutManagerConverter(), java.awt.LayoutManager.class.getName()),
	Point(new PointConverter(), java.awt.Point.class.getName()),
	ActionListener(new ActionListenerConverter(), java.awt.event.ActionListener.class.getName());
	
	private StringToObjectConverter converter;
	private String defintionName;
	
	private ParamTypes(StringToObjectConverter converter, String defintionName)
	{
		this.converter = converter;
		this.defintionName = defintionName;
	}
	
	public String getDefinitionName()
	{
		return this.defintionName;
	}
	
	public StringToObjectConverter getConverter()
	{
		return this.converter;
	}
	
	/**
	 * @param definitionName
	 * @return ParamType or **null if not found**
	 */
	public static ParamTypes getParamType(String definitionName)
	{
		ParamTypes retP = null;
		for(ParamTypes pt : ParamTypes.values())
		{
			if(pt.getDefinitionName().equals(definitionName))
			{
				retP = pt;
				break;
			}
		}
		return retP;
	}
}
