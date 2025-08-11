package ObjectTypeConversion;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import HttpDatabaseRequest.SQLUtility;
import HttpDatabaseResponse.DatabaseResponseNode;
import ObjectTypeConvertersImpl.IntConverter;
import Params.ParamTypes;
import Properties.LoggingMessages;

public class WeatherReading 
{
	public static final String 
		NUMBER_STRIP = "[\\(\\)\\/a-zA-Z\\%\\\\]",
		DATABASE = "WeatherDatabase",
		TABLE = "WeatherReading";
	
	public static final List<String> dayOfTheWeek = (List<String>) Arrays.asList(new String [] {
			"Monday","Tuesday","Wednesday","Thursday","Friday","Saturday","Sunday"
	});
	
	private HashMap<String, ValueModifier> valueModifiers = new HashMap<String, WeatherReading.ValueModifier>();
	private HashMap<String, Object> queryValues = new HashMap<String, Object>();
	private Date date;
	
	public WeatherReading(HashMap<String, String> reading)
	{
		loadWeatherReading(reading);
		buildQuery();
	}
	
	public WeatherReading(ArrayList<DatabaseResponseNode> value, String table, String database)
	{
		loadWeatherReading(value, table, database);
	}
	
	public HashMap<String, Object> getQueryValues()
	{
		return this.queryValues;
	}
	
	public Date getDate()
	{
		return this.date;
	}
	
	private void loadWeatherReading(ArrayList<DatabaseResponseNode> value, String table, String database)
	{
		for(DatabaseResponseNode drn : value)
		{
			String key = "";
			key = drn.getNodeName().replace("_"+table, "");
			key = key.replace("_"+database, "");
			String classTypeName = drn.getNodeAttributes().get(DatabaseResponseNode.CLASS_TYPE_KEY);
			String contentValue = drn.getNodeAttributes().get(DatabaseResponseNode.CONTENT_KEY);
			ParamTypes pt = ParamTypes.getParamType(classTypeName);
			
			Object o = pt.getConverter().conversionCall(contentValue);
			if(o instanceof Timestamp)
			{
				if(key.equals("Date"))
				{
					date = (Date) o;
				}
			}
			LoggingMessages.printOut(key + " " + o);
			queryValues.put(key, o);
		}
	}
	
	private void loadWeatherReading(HashMap<String, String> reading)
	{
		for(String key : reading.keySet())
		{
			String value = reading.get(key);
			ValueDataType vdt = ValueDataType.getDataTypeFromKey(key);
			
			if(vdt == null)
			{
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
				
				queryValues.put(key, val);
			}
		}
	}
	
	public String buildQuery() //TODO.
	{
		String query = "REPLACE INTO " + DATABASE + "." + TABLE + " (";
		String values = "(";
		
		for(String key : queryValues.keySet())
		{
			Object o = queryValues.get(key);
			if(dayOfTheWeek.contains(key))
			{
				Date d = getDate((String) o);
				this.date = d;
				query += "Date" + "_" + TABLE + "_" + DATABASE + ", ";//Convert to date... primary key (date + zip code / location)
				values += SQLUtility.getDateToMySqlString(d) + ", "; //milliseconds to seconds
			}
			else
			{
				query += key+ "_" + TABLE + "_" + DATABASE + ", ";
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
		precipitationPotential("PrecipitationPotential", int.class, NUMBER_STRIP),
		relativeHumidity("RelativeHumidity", int.class, NUMBER_STRIP),
		skyCover("SkyCover", int.class, NUMBER_STRIP),
		heatIndex("HeatIndex", int.class, NUMBER_STRIP),
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
