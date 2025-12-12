package Params;

import ObjectTypeConversion.StringToObjectConverter;
import ObjectTypeConvertersImpl.ActionListenerConverter;
import ObjectTypeConvertersImpl.BooleanConverter;
import ObjectTypeConvertersImpl.ColorConverter;
import ObjectTypeConvertersImpl.CommandBuildConverter;
import ObjectTypeConvertersImpl.CsvReaderConverter;
import ObjectTypeConvertersImpl.DimensionConverter;
import ObjectTypeConvertersImpl.DirectorySelectionConverter;
import ObjectTypeConvertersImpl.DoubleConverter;
import ObjectTypeConvertersImpl.FileSelectionConverter;
import ObjectTypeConvertersImpl.IntConverter;
import ObjectTypeConvertersImpl.IntegerConverter;
import ObjectTypeConvertersImpl.KeyListenerConverter;
import ObjectTypeConvertersImpl.LayoutManagerConverter;
import ObjectTypeConvertersImpl.LookAndFeelClassNameConverter;
import ObjectTypeConvertersImpl.MouseAdapterConverter;
import ObjectTypeConvertersImpl.MouseListenerConverter;
import ObjectTypeConvertersImpl.MouseMotionListenerConverter;
import ObjectTypeConvertersImpl.NameIdConverter;
import ObjectTypeConvertersImpl.PageParserCollectionConverter;
import ObjectTypeConvertersImpl.PageParserConverter;
import ObjectTypeConvertersImpl.PathArrayProcessingConverter;
import ObjectTypeConvertersImpl.PointConverter;
import ObjectTypeConvertersImpl.StringConverter;
import ObjectTypeConvertersImpl.TimestampConverter;
import ObjectTypeConvertersImpl.WavReaderConverter;
import ObjectTypeConvertersImpl.WindowListenerConverter;
import Properties.LoggingMessages;

public enum ParamTypes 
{
	String(new StringConverter()),
	Int(new IntConverter()),
	Integer(new IntegerConverter()),
	Double(new DoubleConverter()),
	Point(new PointConverter()),
	Dimension(new DimensionConverter()),
	Boolean(new BooleanConverter()),
	Color(new ColorConverter()),
	Timestamp(new TimestampConverter()),
	LayoutManager(new LayoutManagerConverter()),
	ActionListener(new ActionListenerConverter()),
	mouseListener(new MouseListenerConverter()),
	mouseAdapter(new MouseAdapterConverter()),
	mouseMotionListener(new MouseMotionListenerConverter()),
	pathArrayProcess(new PathArrayProcessingConverter()),
	nameId(new NameIdConverter()),
	lookAndFeel(new LookAndFeelClassNameConverter()),
	pageParser(new PageParserConverter()),
	pageParserCollection(new PageParserCollectionConverter()),
	WindowListener(new WindowListenerConverter()),
	KeyListener(new KeyListenerConverter()),
	DirectorySelection(new DirectorySelectionConverter()),
	CsvReader(new CsvReaderConverter()),
	WavReader(new WavReaderConverter()),
	FileSelection(new FileSelectionConverter()),
	CommandBuild(new CommandBuildConverter());
	
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
		LoggingMessages.printOut(definitionName);
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
