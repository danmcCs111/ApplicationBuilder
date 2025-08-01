package ObjectTypeConversion;

import java.util.ArrayList;
import java.util.HashMap;

public abstract class CsvConverter 
{
	protected CsvReader csvReader;
	public CsvConverter (CsvReader csvReader)
	{
		this.csvReader = csvReader;
	}
	
	public abstract ArrayList<HashMap<String, String>> getValues();
}
