package Params;

import ClassDefintions.ActionListenerConverter;
import ClassDefintions.BooleanConverter;
import ClassDefintions.ColorConverter;
import ClassDefintions.DimensionConverter;
import ClassDefintions.IntConverter;
import ClassDefintions.LayoutManagerConverter;
import ClassDefintions.PointConverter;
import ClassDefintions.StringConverter;
import ClassDefintions.StringToObjectConverter;

public enum ParamTypes 
{
	String(new StringConverter()),
	Int(new IntConverter()),
	Color(new ColorConverter()),
	LayoutManager(new LayoutManagerConverter()),
	Point(new PointConverter()),
	Boolean(new BooleanConverter()),
	ActionListener(new ActionListenerConverter()),
	Dimension(new DimensionConverter());
	
	private StringToObjectConverter converter;
	private String defintionName;
	
	private ParamTypes(StringToObjectConverter converter)
	{
		this.converter = converter;
		this.defintionName = converter.getDefinitionClass().getName();
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
