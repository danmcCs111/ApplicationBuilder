package Actions;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.SelectWebServiceQueries;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.CsvReader;
import ObjectTypeConversion.WeatherGrabCsvConverter;
import ObjectTypeConversion.WeatherReading;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class WeatherDatabaseUpdater 
{
	public static final String
		GIT_BASH = "C:\\Program Files\\Git\\git-bash.exe",
		CSV_FILE_PREFIX = "weather_grab";
	public static final SimpleDateFormat SDF_FILE = new SimpleDateFormat("_YYYY_M_d_h_m");
	public static final String []//TODO
		OPTIONS = new String [] {"-c"};
	
	private CommandBuild cb;
	
	public WeatherDatabaseUpdater(String weatherFileLocation, String uploadScriptPath, String zipCode)
	{
		Date d = Calendar.getInstance().getTime();
		String suffix = SDF_FILE.format(d);
		String 
			htmlFile = PathUtility.getPathLinux(weatherFileLocation + CSV_FILE_PREFIX + suffix + ".html"),
			csvFile = weatherFileLocation + CSV_FILE_PREFIX + suffix + ".csv";
		
		LoggingMessages.printOut(htmlFile);
		
		cb = new CommandBuild(GIT_BASH);
		cb.setCommand(GIT_BASH, OPTIONS, new String [] {uploadScriptPath + " " + htmlFile + " " + zipCode});
		try {
			CommandExecutor.executeProcess(cb, true);
			
			CsvReader cr = new CsvReader(csvFile);
			cr.read();
			WeatherGrabCsvConverter wgcc = new WeatherGrabCsvConverter(cr);
			HashMap<Date, WeatherReading> weatherReadings = wgcc.getWeatherReadings();
			String query = "";
			for(Date dt : weatherReadings.keySet())
			{
				query += weatherReadings.get(dt).buildQuery() + "; \n";
			}
			LoggingMessages.printOut("");
			
			LoggingMessages.printOut(query);
			HttpDatabaseRequest.executeGetRequest
			(
					SelectWebServiceQueries.ENDPOINT,
					SelectWebServiceQueries.PORT_NUMBER,
					query,
					SelectWebServiceQueries.REQUEST_TYPE_HEADER_KEY,
					"Update"
			);
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
			uploadScriptPath = args[1],
			zipCode = args[2];
		
		WeatherDatabaseUpdater wdu = new WeatherDatabaseUpdater(weatherFileLocation, uploadScriptPath, zipCode);
		LoggingMessages.printOut(wdu.getCommandBuild().getArgs());
	}
}
