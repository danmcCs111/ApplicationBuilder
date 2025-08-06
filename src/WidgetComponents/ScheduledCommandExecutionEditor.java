package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import Actions.ScheduledCommand;
import Editors.CommandBuildEditor;
import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class ScheduledCommandExecutionEditor extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static final int TIME_GAP = 96;
	private static final String TIME_FORMAT = "hh:mm a";//TODO
	private int timeGap = TIME_GAP;
	
	private JComboBox<String> timeOptions;
	private CommandBuildEditor cbe;
	private JCheckBox [] daysOfWeek;
	private JCheckBox everyDay;
	private boolean singleRunDay = true;
	
	public ScheduledCommandExecutionEditor()
	{
		
	}
	
	public void setScheduledCommand(ScheduledCommand sc)
	{
		cbe.setComponentValue(sc.getCommandBuild());
		String dayOfWk = sc.getDayOfWeek();
		List<String> scSelDayWeek = Arrays.asList(dayOfWk.split(ScheduledCommand.DELIMITER_WEEKDAY));
		for(JCheckBox jc : daysOfWeek)
		{
			if(scSelDayWeek.contains(jc.getText()))
			{
				jc.setSelected(true);
			}
		}
		if(scSelDayWeek.contains(everyDay.getText()))
		{
			everyDay.setSelected(true);
		}
		Date d = sc.getDate();
		SimpleDateFormat fmt = new SimpleDateFormat(TIME_FORMAT, Locale.US);
		String date = fmt.format(d);
		LoggingMessages.printOut(date);
		timeOptions.setSelectedItem(date);
	}
	
	public ScheduledCommand getScheduledCommand()
	{
		return buildScheduledCommand();
	}
	
	public void buildWidgets()
	{
		if(singleRunDay)
		{
			buildSingleRunDay();
		}
		else
		{
			//TODO.
		}
	}
	
	public void buildSingleRunDay()
	{
		cbe = new CommandBuildEditor();
		String [] options = buildTimePickerOptions(timeGap);
		timeOptions = new JComboBox<String>(options);
		
		this.add(cbe);
		this.add(timeOptions);
		
		daysOfWeek = new JCheckBox[] {
				new JCheckBox("Sunday"),
				new JCheckBox("Monday"),
				new JCheckBox("Tuesday"),
				new JCheckBox("Wednesday"),
				new JCheckBox("Thursday"),
				new JCheckBox("Friday"),
				new JCheckBox("Saturday")
		};
		everyDay = new JCheckBox(ScheduledCommand.EVERYDAY_STR);
		
		for(JCheckBox cb : daysOfWeek)
		{
			cb.addActionListener(buildToggleActionListener(everyDay));
			this.add(cb);
		}
		everyDay.addActionListener(buildToggleActionListener(daysOfWeek));
		this.add(everyDay);
		
		LoggingMessages.printOut(options);
	}
	
	public String [] buildTimePickerOptions(double numberOfOptions)
	{
		String [] options = new String[(int)numberOfOptions];
		
		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);
		
		double div = 24.0 / numberOfOptions;
		int minInc = (int)(div * 60.0);
		options[0] = "12:00 " + ScheduledCommand.AM;
		for(int i = 1; i < numberOfOptions; i++)
		{
			cal.add(Calendar.MINUTE, minInc);
			int 
				hour = cal.get(Calendar.HOUR),
				minute = cal.get(Calendar.MINUTE),
				amOrPm = cal.get(Calendar.AM_PM);
			String 
				hourFill = (hour < 10) 
					? "0" 
					: "",
				minuteFill = (minute < 10) 
					? "0" 
					: "";
			
			options[i] = ((hour==0) ?"12" :hourFill + hour) + ":" + minuteFill + minute + " " + ((amOrPm == 0) ? ScheduledCommand.AM : ScheduledCommand.PM);
		}
		
		return options;
		
	}
	
	private ActionListener buildToggleActionListener(JCheckBox ... toggleOffCheckBox)
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(int i = 0; i < toggleOffCheckBox.length; i++)
				{
					toggleOffCheckBox[i].setSelected(false);
				}
			}
		};
	}
	
	public void setTimeGapDivisionsPerDay(int timeGap)
	{
		this.timeGap = timeGap;
	}
	
	private ScheduledCommand buildScheduledCommand()
	{
		String option = (String) timeOptions.getSelectedItem();
		String [] sp = option.split(":");
		int hour = Integer.parseInt(sp[0]);
		String [] minAmPM = sp[1].split(" ");
		int minute = Integer.parseInt(minAmPM[0]);
		String amPm = minAmPM[1];//TODO
		
		CommandBuild cb = (CommandBuild) cbe.getComponentValueObj();
		ScheduledCommand sc = new ScheduledCommand();
		sc.setCommandBuild(cb);
		sc.setHourMinuteAmOrPm(hour, minute, amPm);
		String dayOfWk = "";
		for(JCheckBox t : daysOfWeek)
		{
			if(t.isSelected())
			{
				dayOfWk += t.getText() + ScheduledCommand.DELIMITER_WEEKDAY;
			}
		}
		if(everyDay.isSelected())
		{
			dayOfWk += everyDay.getText() + ScheduledCommand.DELIMITER_WEEKDAY;
		}
		if(!dayOfWk.isEmpty())
		{
			dayOfWk = dayOfWk.substring(0, dayOfWk.length()-1);
		}
		sc.setDayOfWeek(dayOfWk);
		
		return sc;
	}
	
	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().validate();
	}
	
}
