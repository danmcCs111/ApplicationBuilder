package ObjectTypeConvertersImpl;

import java.awt.event.KeyListener;
import java.lang.reflect.InvocationTargetException;

import ObjectTypeConversion.StringToObjectConverter;

public class KeyListenerConverter implements StringToObjectConverter 
{
	public static KeyListener getKeyListener(String arg0)
	{
		KeyListener kl = null;
		Class<?> c;
		try {
			c = Class.forName(arg0);
			try {
				kl = (KeyListener) c.getConstructor().newInstance();
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			} catch (IllegalArgumentException e) {
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				e.printStackTrace();
			} catch (NoSuchMethodException e) {
				e.printStackTrace();
			} catch (SecurityException e) {
				e.printStackTrace();
			}
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return kl;
	}

	@Override
	public int numberOfArgs() {
		// TODO Auto-generated method stub
		return 1;
	}

	@Override
	public Class<?> getDefinitionClass() 
	{
		return KeyListener.class;
	}

	@Override
	public Object getDefaultNullValue() 
	{
		return null;
	}

	@Override
	public Object conversionCall(String... args) 
	{
		return conversionCallIsBlankCheck(args)
				? getDefaultNullValue()
				: getKeyListener(args[0]);
	}

	@Override
	public String conversionCallStringXml(String... args) 
	{
		return null;
	}

}
