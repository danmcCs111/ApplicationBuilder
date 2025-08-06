package Actions;

import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ObjectTypeConversion.CommandBuild;

public class ScheduledCommand 
{
	public static final String 
		DELIMITER_COMMANDLINE_OPTION = ",",
		DELIMITER_WEEKDAY = "@",
		SCHEDULE_ATTRIBUTE = "Schedule",
		COMMAND_ATTRIBUTE = "Command",
		AM = "AM",
		PM = "PM",
		EVERYDAY_STR = "Everyday";
	
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
			if(s.startsWith(SCHEDULE_ATTRIBUTE))
			{
				String arg = s.split("=")[1];
				String [] args = arg.split(DELIMITER_COMMANDLINE_OPTION);
				this.hour = Integer.parseInt(args[0].strip());
				this.minute = Integer.parseInt(args[1].strip());
				this.amOrpm = args[2].strip();
				this.dayOfWeek = args[3].strip();
			}
			else if(s.startsWith(COMMAND_ATTRIBUTE))
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
	
	public int getHour()
	{
		return this.hour;
	}
	
	public int getMinute()
	{
		return this.minute;
	}
	
	public int getAmOrPm()
	{
		return this.amOrpm.equals(PM) ? 1 : 0;
	}
	
	public Date getDate()
	{
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.HOUR, hour == 12 ? 0 : hour);
		cal.set(Calendar.MINUTE, minute);
		cal.set(Calendar.AM_PM, amOrpm.equals(PM) ? 1 : 0);
		
		return cal.getTime();
	}
	
	public String getXmlAttributesString()
	{
		return
			SCHEDULE_ATTRIBUTE + "=\"" + 
			this.hour + DELIMITER_COMMANDLINE_OPTION +
			this.minute + DELIMITER_COMMANDLINE_OPTION +
			this.amOrpm + DELIMITER_COMMANDLINE_OPTION +
			this.dayOfWeek + "\" " + 
			COMMAND_ATTRIBUTE + "=\"" + 
			commandBuild.getCommandXmlString() + "\"";
	}
	
}
