package ObjectTypeConvertersImpl;

import ObjectTypeConversion.LookAndFeelClassName;
import ObjectTypeConversion.StringToObjectConverter;

public class LookAndFeelClassNameConverter implements StringToObjectConverter 
{

	@Override
	public int numberOfArgs() {
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() {
		return LookAndFeelClassName.class;
	}

	@Override
	public Object getDefaultNullValue() {
		return null;
	}

	@Override
	public Object conversionCall(String... args) {
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: new LookAndFeelClassName(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) {
		return args[0];
	}

}
