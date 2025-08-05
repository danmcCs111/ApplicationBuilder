package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Calendar;

import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import Actions.ScheduledCommand;
import Editors.CommandBuildEditor;
import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class ScheduledCommandExecutor extends JPanel implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static final int TIME_GAP = 96;
	private int timeGap = TIME_GAP;
	
	private JComboBox<String> timeOptions;
	private CommandBuildEditor cbe;
	
	public ScheduledCommandExecutor()
	{
		
	}
	
	public ScheduledCommand getScheduledCommand()
	{
		
		return buildScheduledCommand();
	}
	
	public void buildWidgets()
	{
		cbe = new CommandBuildEditor();
		String [] options = buildTimePickerOptions(timeGap);
		timeOptions = new JComboBox<String>(options);
		
		this.add(cbe);
		this.add(timeOptions);
		
		JCheckBox [] daysOfWeek = new JCheckBox[] {
				new JCheckBox("Sunday"),
				new JCheckBox("Monday"),
				new JCheckBox("Tuesday"),
				new JCheckBox("Wednesday"),
				new JCheckBox("Thursday"),
				new JCheckBox("Friday"),
				new JCheckBox("Saturday")
		};
		JCheckBox everyDay = new JCheckBox("Everyday");
		
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
		cal.set(Calendar.HOUR, 12);
		
		double div = 24.0 / numberOfOptions;
		int minInc = (int)(div * 60.0);
		options[0] = "12:00 am";
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
			
			options[i] = ((hour==0) ?"12" :hourFill + hour) + ":" + minuteFill + minute + " " + ((amOrPm == 0) ? "pm" : "am");
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
		sc.setDayOfWeek("Friday");
		
		return sc;
	}
	
	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().getParent().validate();
	}
	
}
