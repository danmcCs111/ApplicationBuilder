package DrawModesAbstract;

import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;

import Properties.PathUtility;

public abstract class DrawMode 
{
	private static final String 
		DIRECTORY = "/src/DrawModes/",
		PACKAGE_PREFIX = "DrawModes",
		EDITOR_PARAMETER_FILE_PREFIX = ".java",
		EDITOR_PARAMETER_FILE_FILTER = "java";
	
	private static final ArrayList<DrawMode> drawTypes = new ArrayList<DrawMode>();
	static {
		loadParameterEditorExtensions();
	}
	
	public abstract String [] getDirections();
	public abstract String getClassName();
	public abstract String getModeText();
	public abstract int getNumberOfPoints();
	public abstract Shape constructShape(Point [] points, Graphics2D g2d);
	
	@Override
	public String toString()
	{
		return getModeText();
	}
	
	public static DrawMode getMatchingClassName(String className)
	{
		for(DrawMode dm : drawTypes)
		{
			if(dm.getClassName().equals(className))
			{
				return dm;
			}
		}
		return null;
	}
	
	public static DrawMode[] values()
	{
		return drawTypes.toArray(new DrawMode[] {});
	}
	
	private static void loadParameterEditorExtensions()
	{
		ArrayList<String> files = PathUtility.getOSFileList(PathUtility.getCurrentDirectory() + DIRECTORY, EDITOR_PARAMETER_FILE_FILTER);
		for(String file : files)
		{
			try {
				Class<?> c = Class.forName(PACKAGE_PREFIX + "." + file.replaceAll(EDITOR_PARAMETER_FILE_PREFIX, ""));
				drawTypes.add((DrawMode) c.getDeclaredConstructor().newInstance());
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
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
		}
	}
	
}
