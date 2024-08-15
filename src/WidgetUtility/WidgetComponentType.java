package WidgetUtility;

import java.lang.reflect.Method;

public enum WidgetComponentType {
	FRAME("Frame", "createFrame"),
	PANEL("Panel", "createPanel"),
	SCROLL_PANE("ScrollPane", "createScollPane"),
	MENU_OPTION("MenuOption", "createMenuOption"),
	MENU_ITEM("MenuItem", "createMenuItem"),
	BUTTON("Button", "createButton"),
	BUTTON_ARRAY("ButtonArray", "createButtonArray"),
	LABEL("Label", "createLabel"),
	COLLECTION("Collection", ""),
	SYSTEM_TRAY("SystemTray", "createSystemTray");
	
	private String 
		componentLabel, 
		creatorMethodStr;
	private Method creatorMethod;
	private int counter = 0;
	
	private static Method [] creatorMethods = WidgetCreator.class.getDeclaredMethods();
	public static String ID_SPLIT = "#";
	
	private WidgetComponentType(String componentLabel, String creatorMethodStr)
	{
		this.componentLabel = componentLabel;
		this.creatorMethodStr = creatorMethodStr;
//		this.creatorMethod = getWidgetCreatorMethod();
	}
	
	public String getNextCounterId()
	{
		return getLabelStr() + ID_SPLIT + counter++;
	}
	
	public String getLabelStr()
	{
		return this.componentLabel;
	}
	
	public Method getCreatorMethod()
	{
		return creatorMethod;
	}
	
	public static WidgetComponentType getWidgetComponentType(String text)
	{
		for(WidgetComponentType wcType : WidgetComponentType.values())
		{
			if(wcType.getLabelStr().equals(text))
			{
				return wcType;
			}
		}
		return null;
	}
	
	public Method getWidgetCreatorMethod()
	{
		for (Method m : creatorMethods)
		{
			if(m.getName().equals(creatorMethodStr))
			{
				return m;
			}
		}
		return null;
	}
}
