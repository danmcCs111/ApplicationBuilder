package Params;

import ClassDefinitions.ActionListenerConverter;
import ClassDefinitions.BooleanConverter;
import ClassDefinitions.ColorConverter;
import ClassDefinitions.DimensionConverter;
import ClassDefinitions.DirectorySelectionConverter;
import ClassDefinitions.FileSelectionConverter;
import ClassDefinitions.IntConverter;
import ClassDefinitions.LayoutManagerConverter;
import ClassDefinitions.PointConverter;
import ClassDefinitions.StringConverter;
import ClassDefinitions.StringToObjectConverter;
import ClassDefinitions.WindowListenerConverter;

public enum ParamTypes 
{
	String(new StringConverter()),
	Int(new IntConverter()),
	Color(new ColorConverter()),
	LayoutManager(new LayoutManagerConverter()),
	Point(new PointConverter()),
	Boolean(new BooleanConverter()),
	ActionListener(new ActionListenerConverter()),
	WindowListener(new WindowListenerConverter()),
	DirectorySelection(new DirectorySelectionConverter()),
	FileSelection(new FileSelectionConverter()),
	Dimension(new DimensionConverter());
	
	private StringToObjectConverter converter;
	private String defintionName;
	private Class<?> defintionClass;
	
	private ParamTypes(StringToObjectConverter converter)
	{
		this.converter = converter;
		this.defintionName = converter.getDefinitionClass().getName();
		this.defintionClass = converter.getClass();
	}
	
	public String getDefinitionName()
	{
		return this.defintionName;
	}
	
	public Class<?> getDefinitionClass()
	{
		return this.defintionClass;
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
	
	/**
	 * @param definitionName
	 * @return ParamType or **null if not found**
	 */
	public static ParamTypes getParamType(Class<?> classDefinition)
	{
		ParamTypes retP = null;
		for(ParamTypes pt : ParamTypes.values())
		{
			if(pt.getDefinitionClass().equals(classDefinition))
			{
				retP = pt;
				break;
			}
		}
		return retP;
	}
}
