package ObjectTypeConversion;

import java.util.Comparator;

import Properties.LoggingMessages;

public class NameId implements Comparator<NameId> 
{
	private String nameId;
	
	public NameId(String arg0)
	{
		arg0 = arg0.replaceAll("\"", "");
		LoggingMessages.printOut(arg0);
		this.nameId = arg0;
	}
	
	public String getNameId()
	{
		return this.nameId;
	}
	
	@Override 
	public String toString()
	{
		return this.nameId;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof NameId)
		{
			return this.getNameId().equals(((NameId) o).getNameId());
		}
		return false;
	}

	@Override
	public int compare(NameId o1, NameId o2) 
	{
		return o1.getNameId().compareTo(o2.getNameId());
	}
}
