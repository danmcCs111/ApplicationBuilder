package WidgetComponentInterfaces;

public enum ParamOption 
{
	TextField("Text Field", "T"),
	Directory("Directory", "D"),
	File("File", "F");
	
	private String 
		displayText = "",
		typeXml = "";
	private ParamOption(String displayText, String typeXml)
	{
		this.displayText = displayText;
		this.typeXml = typeXml;
	}
	
	public String getDisplayText()
	{
		return this.displayText;
	}
	
	public String getTypeXml()
	{
		return this.typeXml;
	}
	
	public static ParamOption getParamOption(String text)
	{
		for(ParamOption po : values())
		{
			if(po.getDisplayText().equals(text))
			{
				return po;
			}
			if(po.getTypeXml().equals(text))
			{
				return po;
			}
		}
		return null;
	}
	
	public enum PathModifier
	{
		linux("l"),
		none("n");
		
		private String mod = "";
		private PathModifier(String mod)
		{
			this.mod = mod;
		}
		public String getModVal()
		{
			return this.mod;
		}
		public static PathModifier getModifier(String mod)
		{
			for(PathModifier pm : PathModifier.values())
			{
				if(pm.getModVal().equals(mod))
				{
					return pm;
				}
			}
			return null;
		}
	}
}
