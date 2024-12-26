package WidgetExtensions;

public class ExtendedActionListenerSubTypeAttribute implements ExtendedAttributeStringParam
{
	private String subTypeAttribute = null;
	@Override
	public void applyMethod(String arg0)
	{
		this.subTypeAttribute = arg0;
	}
	
	public String getSubTypeAttribute()
	{
		return this.subTypeAttribute;
	}
}
