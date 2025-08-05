package Actions;

import java.util.Calendar;
import java.util.Date;

import ObjectTypeConversion.CommandBuild;

public class ScheduledCommand 
{
	public static final String 
		DELIMITER_COMMANDLINE_OPTION = "@";
	
	private String 
		dayOfWeek,
		amOrpm;
	private int 
		hour,
		minute;
	private CommandBuild commandBuild;
	
	public ScheduledCommand(String arg)
	{
		String [] args = arg.split(DELIMITER_COMMANDLINE_OPTION);
		hour = Integer.parseInt(args[0]);
		minute = Integer.parseInt(args[1]);
		dayOfWeek = args[2];
		amOrpm = args[3];
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
}
