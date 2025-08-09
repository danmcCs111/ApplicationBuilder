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
		CSV_FILE_LOCATION = "C:\\Users\\danie\\codebase\\danmcCs111\\PlayrightCopyUrl\\PlayrightVideo\\GrabFolder\\Weather\\weather_grab";
	public static final SimpleDateFormat SDF_FILE = new SimpleDateFormat("_YYYY_M_d_h_m");
	public static final String []//TODO
		OPTIONS = new String [] {"-c"},
		UPLOAD_SCRIPT_PATH = new String [] {"/c/Users/danie/codebase/danmcCs111/PlayrightCopyUrl/PlayrightVideo/collectWeatherToCsv.sh"};
	
	private CommandBuild cb;
	
	public WeatherDatabaseUpdater()
	{
		Date d = Calendar.getInstance().getTime();
		String suffix = SDF_FILE.format(d);
		LoggingMessages.printOut(suffix);//TODO. filename as param.
		cb = new CommandBuild(GIT_BASH);
		cb.setCommand(GIT_BASH, OPTIONS, UPLOAD_SCRIPT_PATH);
		try {
			CommandExecutor.executeProcess(cb, true);
			
			CsvReader cr = new CsvReader(CSV_FILE_LOCATION + suffix + ".csv");
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
		WeatherDatabaseUpdater wdu = new WeatherDatabaseUpdater();
		LoggingMessages.printOut(wdu.getCommandBuild().getArgs());
	}
}
