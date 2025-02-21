package ActionListeners;

public class KeepSelection
{
	private String 
		path,
		text;
	private boolean keep;
	
	public KeepSelection(String path, String text)
	{
		this.path = path;
		this.text = text;
	}
	
	public boolean setKeepFrame(boolean keep)
	{
		return this.keep = keep;
	}
	
	public boolean getKeepFrame()
	{
		return keep;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public String getPath()
	{
		return this.path;
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof KeepSelection)
		{
			if(((KeepSelection) obj).getPath().equals(this.getPath()) && 
					((KeepSelection) obj).getText().equals(this.getText()))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public String toString()
	{
		return this.path + " " + this.text;
	}
}
