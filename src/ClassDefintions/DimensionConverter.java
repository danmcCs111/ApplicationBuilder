package ClassDefintions;

import java.awt.Dimension;

public class DimensionConverter implements StringToObjectConverter
{

	@Override
	public int numberOfArgs() 
	{
		return 2;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return new Dimension(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return Dimension.class;
	}

}
