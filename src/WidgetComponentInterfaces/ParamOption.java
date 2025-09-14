package WidgetComponentInterfaces;

public enum ParamOption 
{
	TextField("TextField"),
	Directory("Directory"),
	File("File");
	
	private String displayText = "";
	private ParamOption(String displayText)
	{
		this.displayText = displayText;
	}
	
	public String getDisplayText()
	{
		return this.displayText;
	}
	
	public static ParamOption getParamOption(String text)
	{
		for(ParamOption po : values())
		{
			if(po.getDisplayText().equals(text))
			{
				return po;
			}
		}
		return null;
	}
}
