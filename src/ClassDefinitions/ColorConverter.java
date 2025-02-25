package ClassDefinitions;

import java.awt.Color;

import Properties.LoggingMessages;

public class ColorConverter implements StringToObjectConverter
{
	public static Color getColor(String arg0, String arg1, String arg2)
	{
		Color color = new Color(Integer.parseInt(arg0), Integer.parseInt(arg1), Integer.parseInt(arg2));
		return color;
	}

	@Override
	public int numberOfArgs() 
	{
		return 3;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: getColor(args[0], args[1], args[2]);
	}
	
	@Override
	public String toString()
	{
		return ColorConverter.class.toString();
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return Color.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return Color.white;
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return LoggingMessages.combine(args);
	}
}
