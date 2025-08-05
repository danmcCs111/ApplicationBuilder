package Actions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ObjectTypeConversion.CommandBuild;

public class ScheduledCommand 
{
	public static final String 
		DELIMITER_COMMANDLINE_OPTION = ",";
	
	private String 
		dayOfWeek,
		amOrpm;
	private int 
		hour,
		minute;
	private CommandBuild commandBuild;
	
	public ScheduledCommand()
	{
		
	}
	
	public ScheduledCommand(String [] attributes)
	{
		this(Arrays.asList(attributes));
	}
	
	public ScheduledCommand(List<String> attributes)
	{
		for(String s : attributes)
		{
			s = s.replaceAll("\"", "");
			if(s.startsWith("Schedule"))
			{
				String arg = s.split("=")[1];
				String [] args = arg.split(DELIMITER_COMMANDLINE_OPTION);
				this.hour = Integer.parseInt(args[0].strip());
				this.minute = Integer.parseInt(args[1].strip());
				this.dayOfWeek = args[2].strip();
				this.amOrpm = args[3].strip();
			}
			else if(s.startsWith("Command"))
			{
				String arg = s.split("=")[1];
				this.commandBuild = new CommandBuild(arg);
			}
		}
	}
	
	public void setCommandBuild(CommandBuild commandBuild)
	{
		this.commandBuild = commandBuild;
	}
	
	public CommandBuild getCommandBuild()
	{
		return this.commandBuild;
	}
	
	public void setHourMinuteAmOrPm(int hour, int minute, String amOrpm)
	{
		this.hour = hour;
		this.minute = minute;
		this.amOrpm = amOrpm;
	}
	
	public void setDayOfWeek(String dayOfWeek)
	{
		this.dayOfWeek = dayOfWeek;
	}
	
	public String getDayOfWeek()
	{
		return this.dayOfWeek;
	}
	
	public Date getDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.AM_PM, amOrpm.equals("pm") ? 0 : 1);
		
		return cal.getTime();
	}
	
	public String getXmlString()
	{
		return 
				this.hour + DELIMITER_COMMANDLINE_OPTION +
				this.minute + DELIMITER_COMMANDLINE_OPTION +
				this.dayOfWeek + DELIMITER_COMMANDLINE_OPTION + 
				this.amOrpm; 
	}
}
