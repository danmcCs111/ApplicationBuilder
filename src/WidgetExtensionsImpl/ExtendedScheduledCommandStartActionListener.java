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
import Actions.ScheduledCommand;
import ObjectTypeConversion.CommandBuild;
import WidgetExtensions.ExtendedAttributeParam;
import WidgetExtensions.ScheduledCommandExecuteExtension;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class ExtendedScheduledCommandStartActionListener implements ExtendedAttributeParam
{
	public static final int WAIT_INTERVAL = 5000;
	
	public static final SimpleDateFormat SDF_DAY_OF_WEEK = new SimpleDateFormat("EEEE");
	
	public boolean isToday(String dayOfWeek)
	{
		Calendar cal = Calendar.getInstance();
		String today = SDF_DAY_OF_WEEK.format(cal.getTime());
		List<String> dayz = Arrays.asList(dayOfWeek.split(ScheduledCommand.DELIMITER_WEEKDAY));
		if(dayz.contains(today) || today.equals(ScheduledCommand.EVERYDAY_STR))
		{
			return true;
		}
		return false;
	}
	
	private Runnable getRunnable(ScheduledCommandExecuteExtension scee)
	{
		return new Runnable() 
		{
			@Override
			public void run() 
			{
				try {
					while(true)
					{
						ArrayList<ScheduledCommand> scs = getScheduledCommands(scee);//can alter while running...
						for(ScheduledCommand sc : scs)
						{
							boolean isDay = isToday(sc.getDayOfWeek());
							Calendar cal = Calendar.getInstance();
							int 
								nowHour = cal.get(Calendar.HOUR),
								nowMinute = cal.get(Calendar.MINUTE),
								nowAmOrPm = cal.get(Calendar.AM_PM);
							
							if(isDay && nowHour == sc.getHour() && nowMinute == sc.getMinute() && nowAmOrPm == sc.getAmOrPm())
							{
								CommandBuild cb = sc.getCommandBuild();
								CommandExecutor.executeProcess(cb);
							}
						}
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
