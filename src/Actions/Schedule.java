package Actions;

import java.util.Calendar;
import java.util.Date;

public class Schedule 
{
	public static final String 
		DELIMITER_COMMANDLINE_OPTION = ",",
		DELIMITER_WEEKDAY = "@",
		DELIMITER_SCHEDULE = "\\|",
		DELIMITER_SCHEDULE_WRITE = "|",
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
	
	public Schedule()
	{
		
	}
	
	public Schedule(String attribute)
	{
		String [] args = attribute.split(Schedule.DELIMITER_COMMANDLINE_OPTION);
		this.hour = Integer.parseInt(args[0].strip());
		this.minute = Integer.parseInt(args[1].strip());
		this.amOrpm = args[2];
		this.dayOfWeek = args[3];
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
			this.hour + DELIMITER_COMMANDLINE_OPTION +
			this.minute + DELIMITER_COMMANDLINE_OPTION +
			this.amOrpm + DELIMITER_COMMANDLINE_OPTION +
			this.dayOfWeek; 
	}
}
