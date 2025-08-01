package ObjectTypeConversion;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import ObjectTypeConvertersImpl.IntConverter;
import Params.ParamTypes;
import Properties.LoggingMessages;

public class WeatherReading 
{
	public static final String 
		NUMBER_STRIP = "[\\(\\)\\/a-zA-Z\\%\\\\]",
		TABLE = "WeatherReading";
	
	public static final List<String> dayOfTheWeek = (List<String>) Arrays.asList(new String [] {
			"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
	});
	
	public String 
		dayOfWeek,
		surfaceWind;
	
	public int 
		temperature,
		heatIndex,
		dewPoint,
		rain,
		thunder,
		precipitationPotential,
		relativeHumidity,
		skyCover;
	
	private HashMap<String, ValueModifier> valueModifiers = new HashMap<String, WeatherReading.ValueModifier>();
	private HashMap<String, Object> queryValues = new HashMap<String, Object>();
	private Date date;
	
	public WeatherReading(HashMap<String, String> reading)
	{
		loadWeatherReading(reading);
		buildQuery();
	}
	
	public HashMap<String, Object> getQueryValues()
	{
		return this.queryValues;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	public String buildQuery() //TODO.
	{
		String query = "INSERT INTO " + TABLE + " (";
		String values = "(";
		
		for(String key : queryValues.keySet())
		{
			Object o = queryValues.get(key);
			if(dayOfTheWeek.contains(key))
			{
				Date d = getDate((String) o);
				this.date = d;
				query += "Date" + ", ";//Convert to date... primary key (date + zip code / location)
				values += "" + d.getTime() + ", ";
			}
			else
			{
				query += key + ", ";
				String clazzName = o.getClass().getName();
				
				if(clazzName.equals(Integer.class.getName()))
				{
					values += ((int)o) + ", ";
				}
				else if(clazzName.equals(String.class.getName()))
				{
					values += "'" + ((String)o) + "', ";
				}
			}
		}
		
		query = query.substring(0, query.length()-2);
		values = values.substring(0, values.length()-2);
		
		query += ")";
		values += ")";
		query += "\n VALUES " + values;
		
		LoggingMessages.printOut(query);
		
		return query;
	}
	
	private Date getDate(String date)
	{
		String time = date.substring(date.length()-5);
		String dateSt = date.replaceAll("at", "").replaceAll("pm", "").replaceAll("am", "");
		dateSt = dateSt.substring(0, dateSt.length()-2);
		IntConverter sc = new IntConverter();
		int hour = (int) sc.conversionCall(time.substring(0, time.length()-2).strip());
		
		if(time.endsWith("pm") && hour != 12)
		{
			hour += 12;
		}
		Calendar cal = Calendar.getInstance();
		int year = cal.get(Calendar.YEAR);
		
		LoggingMessages.printOut("before parse: " + dateSt.strip() + " " + hour + " " + year);
		SimpleDateFormat fmt = new SimpleDateFormat("MMMM dd HH yyyy", Locale.US);
		Date d = null;
		try {
			d = fmt.parse(dateSt.strip() + " " + hour + " " + year);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		LoggingMessages.printOut("After parse: " + d.toString());
		
		return d;
	}
	
	private void loadWeatherReading(HashMap<String, String> reading)
	{
		for(String key : reading.keySet())
		{
			String value = reading.get(key);
			ValueDataType vdt = ValueDataType.getDataTypeFromKey(key);
			
			if(vdt == null)
			{
				if(key.equals("Surface Wind"))
				{
					surfaceWind = value;
				}
				else //day of week;
				{
					dayOfWeek = value;
				}
				LoggingMessages.printOut(key + " | " + value);
				queryValues.put(key, value.strip());
			}
			else
			{
				
				StringToObjectConverter stc = ParamTypes.getParamType(vdt.clazzType.getName()).getConverter();
				value = value.replaceAll(NUMBER_STRIP, "");
				
				for(ValueModifier vm : ValueModifier.values())
				{
					if(value.contains(vm.modifier)) 
					{
						valueModifiers.put(key, vm);
						value = value.replaceAll(vm.modifier, "").strip();
					}
				}
				
				int val = (int) stc.conversionCall(value);
				
				switch(vdt)
				{
					case temperature:
						temperature = val;
						break;
					
					case heatIndex:
						heatIndex = val;
						break;
						
					case dewPoint:
						dewPoint = val;
						break;
						
					case rain:
						rain = val;
						break;
						
					case thunder:
						thunder = val;
						break;
						
					case precipitationPotential:
						precipitationPotential = val;
						break;
						
					case relativeHumidity:
						relativeHumidity = val;
						break;
						
					case skyCover:
						skyCover = val;
						break;
				}
				LoggingMessages.printOut(key + " | " + val);
				queryValues.put(key, val);
			}
		}
	}
	
	protected enum ValueModifier
	{
		none("", ""),
		lessThan("<", ""),
		greaterThan(">", ""),
		range("-", "");
		
		public String modifier;
		public String repl;
		
		private ValueModifier(String modifier, String repl)
		{
			this.modifier = modifier;
			this.repl = repl;
		}
		
		public ValueModifier getModifier(String modifier)
		{
			for(ValueModifier vm : ValueModifier.values())
			{
				if(vm.modifier.equals(modifier))
				{
					return vm;
				}
			}
			return null;
		}
	}
	
	protected enum ValueDataType
	{
		temperature("Temperature", int.class, NUMBER_STRIP),
		rain("Rain", int.class, NUMBER_STRIP),
		thunder("Thunder", int.class, NUMBER_STRIP),
		precipitationPotential("Precipitation Potential", int.class, NUMBER_STRIP),
		relativeHumidity("Relative Humidity", int.class, NUMBER_STRIP),
		skyCover("Sky Cover", int.class, NUMBER_STRIP),
		heatIndex("Heat Index", int.class, NUMBER_STRIP),
		dewPoint("Dewpoint", int.class, NUMBER_STRIP);
		
		public String keyValue = "";
		public Class<?> clazzType = null;
		public String strip = "";
		
		private ValueDataType(String keyValue, Class<?> clazzType, String strip)
		{
			this.keyValue = keyValue;
			this.clazzType = clazzType;
			this.strip = strip;
		}
		
		public static ValueDataType getDataTypeFromKey(String key)
		{
			for(ValueDataType vdt : ValueDataType.values())
			{
				if(vdt.keyValue.equals(key))
				{
					return vdt;
				}
			}
			return null;
		}
	}
}
