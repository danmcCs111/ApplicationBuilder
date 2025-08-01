package ObjectTypeConversion;

import java.util.ArrayList;
import java.util.HashMap;

import Properties.LoggingMessages;

public class WeatherGrabCsvConverter extends CsvConverter
{
	private ArrayList<HashMap<String, String>> csvValues = new ArrayList<HashMap<String,String>>();
	private ArrayList<WeatherReading> weatherReadings = new ArrayList<WeatherReading>();
	
	private static final ArrayList<Valuefilter> valueFiltersAndKeys = new ArrayList<Valuefilter>();
	static 
	{
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", " ,deg;"," - "}));
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", " ,deg;"}));
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", " ,deg;"}));
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", "\\(%\\)"}));
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", "\\(%\\)"}));
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", "\\(%\\)"}));
		valueFiltersAndKeys.add(new Valuefilter(new String [] {": ", "\""}));
	}
	//allow Multiples per key with a splitting on discovery of a shared spot.
	private static final String [] [] KEYS = new String [] [] 
	{
		new String [] {"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday", "Temperature"},
		new String [] {"Dewpoint"},
		new String [] {"Heat Index"},
		new String [] {"Surface Wind", "Sky Cover"},
		new String [] {"Precipitation Potential"},
		new String [] {"Relative Humidity", "Rain"},
		new String [] {"Thunder"}
	};
	
	public WeatherGrabCsvConverter(CsvReader csvReader) 
	{
		super(csvReader);
		loadCsvValues();
		
		for(HashMap<String, String> reading : csvValues)
		{
			weatherReadings.add(new WeatherReading(reading));
		}
		
	}

	public ArrayList<HashMap<String, String>> getValues()
	{
		return this.csvValues;
	}
	
	public void printValues()
	{
		int count = 0;
		for(HashMap<String, String> row : csvValues)
		{
			LoggingMessages.printOut("");
			LoggingMessages.printOut("Count: " + count);
			for(String key : row.keySet())
			{
				LoggingMessages.printOut(key, row.get(key));
			}
			count++;
		}
	}
	
	private void loadCsvValues()
	{
		for(ArrayList<String> row : csvReader.getCapturedCsvValues())
		{
			csvValues.add(getStrippedString(row));
		}
	}
	
	private HashMap<String, String>  getStrippedString(ArrayList<String> row)
	{
		HashMap<String, String> filtered = new HashMap<String, String>();
		
		for(String s : row)
		{
			int count = 0;
			for(String [] key : KEYS)
			{
				String place = "";
				String foundKey = "";
				for(int i = 0; i < key.length; i++)
				{
					String k = key[i];
					String [] sp = s.split(k);
					int indexStart = s.indexOf(k);
					
					if(indexStart != -1)
					{
						//store.
						if(sp.length > 1)
						{
							if(!place.isBlank())
							{
								String prev = filtered.get(foundKey);
								int replIndex = prev.indexOf(k);
								String t = prev.substring(0, replIndex);
								filtered.put(foundKey, t);
							}
							place = sp[1];
						}
						else
						{
							if(!place.isBlank())
							{
								int stripIndex = sp[0].indexOf(k);
								place = sp[0].substring(0, stripIndex);
							}
							else
							{
								place = sp[0].substring(0, indexStart);
							}
						}
						foundKey = k;
						place = valueFiltersAndKeys.get(count).StringFilterValue(place);
						filtered.put(k, place);
					}
				}
				count++;
			}
		}
		return filtered;
	}
}
