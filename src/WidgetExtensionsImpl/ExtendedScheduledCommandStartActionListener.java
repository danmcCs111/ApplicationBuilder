package WidgetExtensionsImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import javax.swing.AbstractButton;

import Actions.CommandExecutor;
import Actions.Schedule;
import Actions.ScheduledCommand;
import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ScheduledCommandExecuteExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedScheduledCommandStartActionListener implements ExtendedAttributeStringParam
{
	public static final int WAIT_INTERVAL = 5000;
	
	public static final SimpleDateFormat SDF_DAY_OF_WEEK = new SimpleDateFormat("EEEE");
	
	public boolean isToday(String dayOfWeek)
	{
		Calendar cal = Calendar.getInstance();
		String today = SDF_DAY_OF_WEEK.format(cal.getTime());
		List<String> dayz = Arrays.asList(dayOfWeek.split(Schedule.DELIMITER_WEEKDAY));
		
		return (dayz.contains(today) || dayz.contains(Schedule.EVERYDAY_STR));
	}
	
	private Runnable getRunnable(ScheduledCommandExecuteExtension scee)
	{
		return new Runnable() 
		{
			@Override
			public void run() 
			{
				try {
					
					int lastMinute = 0;
					boolean 
						running = false,
						tmpRunning = false;
					
					while(true)
					{
						ArrayList<ScheduledCommand> scs = getScheduledCommands(scee);//can alter while running...
						Calendar cal = Calendar.getInstance();
						int 
							nowHour = cal.get(Calendar.HOUR),
							nowMinute = cal.get(Calendar.MINUTE),
							nowAmOrPm = cal.get(Calendar.AM_PM);
						if(nowHour == 0)
						{
							nowHour = 12;
						}
						for(ScheduledCommand sc : scs)
						{
							for(Schedule sch : sc.getSchedules())
							{
								boolean isDay = isToday(sch.getDayOfWeek());
								if(!running && isDay && nowHour == sch.getHour() && nowMinute == sch.getMinute() && nowAmOrPm == sch.getAmOrPm())
								{
									CommandBuild cb = sc.getCommandBuild();
									CommandExecutor.executeProcess(cb);
									tmpRunning = true;
								}
								if(lastMinute != nowMinute)
								{
									LoggingMessages.printOut("Now: " + nowHour + " " + nowMinute + " " + nowAmOrPm + "");
									LoggingMessages.printOut("is today? " + isDay + " | " + sch.getHour() + " " + sch.getMinute() + " " + sch.getAmOrPm() + " " + sc.getCommandBuild().getCommandXmlString());
								}
							}
						}
						running = tmpRunning;
						if(lastMinute != nowMinute)//reset after a minute change
						{
							LoggingMessages.printOut("");
							running = false;
							tmpRunning = false;
						}
						lastMinute = nowMinute;
						Thread.sleep(WAIT_INTERVAL);
					}
				} catch (InterruptedException | IOException e) {
					e.printStackTrace();
				}
			}
		};
	}
	
	private ArrayList<ScheduledCommand> getScheduledCommands(ScheduledCommandExecuteExtension scee)
	{
		return scee.getScheduledCommands();
	}
	
	@Override
	public void applyMethod(String arg0, WidgetCreatorProperty widgetProperties) 
	{
		String name = arg0;
		
		Object m = widgetProperties.getInstance();
		if(m instanceof AbstractButton)
		{
			AbstractButton ab = (AbstractButton) m;
			ab.addActionListener(new ActionListener() 
			{
				@Override
				public void actionPerformed(ActionEvent e) 
				{
					WidgetCreatorProperty wcp = WidgetBuildController.getInstance().findRefByName(name);
					if(wcp != null)
					{
						Object o = wcp.getInstance();
						if(o instanceof ScheduledCommandExecuteExtension)
						{
							Runnable r = getRunnable((ScheduledCommandExecuteExtension) o);
							Thread t = new Thread(r);
							t.start();
							
							ab.setEnabled(false);
							ab.setText("Running...");
						}
					}
				}
			});
		}
	}
}
