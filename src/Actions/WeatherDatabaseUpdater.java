package Actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.CsvReader;
import ObjectTypeConversion.WeatherGrabCsvConverter;
import ObjectTypeConversion.WeatherReading;
import Properties.LoggingMessages;

public class WeatherDatabaseUpdater 
{
	public static final String
		GIT_BASH = "C:\\Program Files\\Git\\git-bash.exe",
		CSV_FILE_PREFIX = "weather_grab";
	public static final SimpleDateFormat SDF_FILE = new SimpleDateFormat("_YYYY_M_d_h_m");
	public static final String []//TODO
		OPTIONS = new String [] {"-c"};
	
	private CommandBuild cb;
	
	public WeatherDatabaseUpdater(String weatherFileLocation, String uploadScriptPath)
	{
		Date d = Calendar.getInstance().getTime();
		String suffix = SDF_FILE.format(d);
		LoggingMessages.printOut(suffix);//TODO. filename as param.
		cb = new CommandBuild(GIT_BASH);
		cb.setCommand(GIT_BASH, OPTIONS, new String [] {uploadScriptPath});
		try {
			CommandExecutor.executeProcess(cb, true);
			
			CsvReader cr = new CsvReader(weatherFileLocation + CSV_FILE_PREFIX + suffix + ".csv");
			cr.read();
			WeatherGrabCsvConverter wgcc = new WeatherGrabCsvConverter(cr);
			HashMap<Date, WeatherReading> weatherReadings = wgcc.getWeatherReadings();
			for(Date dt : weatherReadings.keySet())
			{
				LoggingMessages.printOut(weatherReadings.get(dt).buildQuery());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public CommandBuild getCommandBuild()
	{
		return cb;
	}
	
	public static void main(String [] args)
	{
		String 
			weatherFileLocation = args[0],
			uploadScriptPath = args[1];
		
		WeatherDatabaseUpdater wdu = new WeatherDatabaseUpdater(weatherFileLocation, uploadScriptPath);
		LoggingMessages.printOut(wdu.getCommandBuild().getArgs());
	}
}
