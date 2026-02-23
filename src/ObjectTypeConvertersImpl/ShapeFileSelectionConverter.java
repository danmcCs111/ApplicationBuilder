package ObjectTypeConvertersImpl;

import ObjectTypeConversion.ShapeFileSelection;

public class ShapeFileSelectionConverter extends FileSelectionConverter
{
	@Override
	public Class<?> getDefinitionClass() 
	{
		return ShapeFileSelection.class;
	}
	
	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new ShapeFileSelection(args[0]);
	}
}
