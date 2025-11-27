package Params;

import java.awt.FontMetrics;
import java.lang.reflect.InvocationTargetException;

import javax.swing.JLabel;

import ObjectTypeConversionEditors.ColorEditor;

public interface ParameterUtility 
{
	public static int getFieldLabelWidth(JLabel label)
	{
		FontMetrics fm = label.getFontMetrics(label.getFont());
		int width = fm.stringWidth(label.getText());
		return width;
	}
	
	public static void setTitleText(ParameterEditor pe, String title)
	{
		if(pe instanceof ColorEditor)
		{
			((ColorEditor) pe).setTitleText(title);
		}
	}
	
	public static boolean isType(String parameterDefStringName, ParameterEditor pe)
	{
		return parameterDefStringName == null 
			? false 
			: pe.getParameterDefintionString().toLowerCase().equals(parameterDefStringName.toLowerCase());
	}
	
	public static boolean isType(Class<?> parameterDefClassName, ParameterEditor pe)
	{
		return parameterDefClassName == null 
			? false 
			: ParameterUtility.isType(parameterDefClassName.getName(), pe);
	}
	
	public static ParameterEditor newInstance(Class<?> clazz)
	{
		try {
			 Object o = Class.forName(clazz.getName()).getDeclaredConstructor().newInstance();
			 if(o instanceof ParameterEditor)
			 {
				 return ((ParameterEditor) o);
			 }
		} catch (InstantiationException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
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
		return null;
	}
}
