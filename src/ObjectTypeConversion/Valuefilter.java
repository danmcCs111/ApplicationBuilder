package ObjectTypeConversion;

public class Valuefilter 
{
	private String [] deleteFilters;
	
	public Valuefilter(String [] deleteFilters)
	{
		this.deleteFilters = deleteFilters;
	}
	
	public String StringFilterValue(String val)
	{
		for(String delFilter : deleteFilters)
		{
			val = val.replaceAll(delFilter, "");
		}
		
		return val;
	}
}
