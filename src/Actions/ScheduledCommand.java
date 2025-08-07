package Actions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;

public class ScheduledCommand 
{
	private ArrayList<Schedule> schedules = new ArrayList<Schedule>();
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
			if(s.startsWith(Schedule.SCHEDULE_ATTRIBUTE))
			{
				String arg = s.split("=")[1];
				String [] spl = arg.split(Schedule.DELIMITER_SCHEDULE);
				for(String a : spl)
				{
					Schedule sch = new Schedule(a);
					schedules.add(sch);
					LoggingMessages.printOut(sch + "arg");
				}
			}
			else if(s.startsWith(Schedule.COMMAND_ATTRIBUTE))
			{
				String arg = s.split("=")[1];
				this.commandBuild = new CommandBuild(arg);
			}
		}
	}
	
	public ArrayList<Schedule> getSchedules()
	{
		return this.schedules;
	}
	
	public void addSchedule(Schedule sch)
	{
		this.schedules.add(sch);
	}
	
	public void setCommandBuild(CommandBuild commandBuild)
	{
		this.commandBuild = commandBuild;
	}
	
	public CommandBuild getCommandBuild()
	{
		return this.commandBuild;
	}
	
	public String getXmlAttributesString()
	{
		String xml = "";
		xml += Schedule.SCHEDULE_ATTRIBUTE + "=\""; 
		for(Schedule sch : schedules)
		{
			xml += sch.getXmlAttributesString();
			xml += Schedule.DELIMITER_SCHEDULE_WRITE;
		}
		xml += "\" ";	
		xml += Schedule.COMMAND_ATTRIBUTE + "=\""; 
		xml += commandBuild.getCommandXmlString() + "\"";
		
		return xml;
	}
	
}
