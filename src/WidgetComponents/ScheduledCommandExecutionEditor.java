package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;

import Actions.CommandExecutor;
import Actions.Schedule;
import Actions.ScheduledCommand;
import Editors.CommandBuildEditor;
import ObjectTypeConversion.CommandBuild;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;

public class ScheduledCommandExecutionEditor extends JPanel implements PostWidgetBuildProcessing {
	private static final long serialVersionUID = 1L;

	private static final int TIME_GAP = 96;
	private static final String TIME_FORMAT = "hh:mm a";// TODO
	private int timeGap = TIME_GAP;

	private ArrayList<JComboBox<String>> timeOptions = new ArrayList<JComboBox<String>>();
	private CommandBuildEditor cbe;
	private JCheckBox[] daysOfWeek;
	private JCheckBox everyDay;
	private ScheduledCommand scheduledCommand;
	
	private JPanel innerPanel = new JPanel();
	private JButton addTimeButton = new JButton("+ Add Run Time"); 

	public ScheduledCommandExecutionEditor() {

	}

	public void setScheduledCommand(ScheduledCommand sc) 
	{
		scheduledCommand = sc;
		
		cbe.setComponentValue(sc.getCommandBuild());
		String dayOfWk = sc.getSchedules().get(0).getDayOfWeek();
		List<String> scSelDayWeek = Arrays.asList(dayOfWk.split(Schedule.DELIMITER_WEEKDAY));
		for (JCheckBox jc : daysOfWeek) {
			if (scSelDayWeek.contains(jc.getText())) {
				jc.setSelected(true);
			}
		}
		if (scSelDayWeek.contains(everyDay.getText())) {
			everyDay.setSelected(true);
		}
		for (int i = 0; i < sc.getSchedules().size(); i++) 
		{
			Schedule sch = sc.getSchedules().get(i);
			Date d = sch.getDate();
			SimpleDateFormat fmt = new SimpleDateFormat(TIME_FORMAT, Locale.US);
			String date = fmt.format(d);
			LoggingMessages.printOut(date);
			if(i >= timeOptions.size())
			{
				addTimeOption();
			}
			timeOptions.get(i).setSelectedItem(date);
		}
	}

	public ScheduledCommand getScheduledCommand() 
	{
		return buildScheduledCommand(scheduledCommand);
	}

	public void buildWidgets() 
	{
		JButton runButton = new JButton("Run");
		JPanel 
			outerPanel = new JPanel(), 
			innerPanelControl = new JPanel();
		
		outerPanel.setLayout(new BorderLayout());
		innerPanel.setLayout(new GridLayout(0, 1));
		innerPanelControl.setLayout(new BorderLayout());

		cbe = new CommandBuildEditor();
		String[] options = buildTimePickerOptions(timeGap);
		timeOptions.add(new JComboBox<String>(options));

		innerPanel.add(cbe);
		innerPanel.add(addTimeButton);
		for (JComboBox<String> to : timeOptions) {
			innerPanel.add(to);
		}

		daysOfWeek = new JCheckBox[] 
		{ 
				new JCheckBox("Sunday"), 
				new JCheckBox("Monday"), 
				new JCheckBox("Tuesday"),
				new JCheckBox("Wednesday"), 
				new JCheckBox("Thursday"), 
				new JCheckBox("Friday"),
				new JCheckBox("Saturday") 
		};
		everyDay = new JCheckBox(Schedule.EVERYDAY_STR);

		for (JCheckBox cb : daysOfWeek) 
		{
			cb.addActionListener(buildToggleActionListener(everyDay));
			innerPanel.add(cb);
		}
		everyDay.addActionListener(buildToggleActionListener(daysOfWeek));
		innerPanel.add(everyDay);

		runButton.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				if (getScheduledCommand() != null && getScheduledCommand().getCommandBuild() != null) 
				{
					CommandBuild cb = getScheduledCommand().getCommandBuild();
					try {
						CommandExecutor.executeProcess(cb);
					} catch (IOException e1) {
						e1.printStackTrace();
					}
				}
			}
		});
		addTimeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addTimeOption();
			}
		});

		innerPanelControl.add(runButton, BorderLayout.WEST);
		outerPanel.add(innerPanel, BorderLayout.NORTH);
		outerPanel.add(innerPanelControl, BorderLayout.SOUTH);
		this.add(outerPanel);
		
		scheduledCommand = buildScheduledCommand(new ScheduledCommand());
		LoggingMessages.printOut(options);
	}

	public String[] buildTimePickerOptions(double numberOfOptions) 
	{
		String[] options = new String[(int) numberOfOptions];

		Calendar cal = Calendar.getInstance();
		cal.set(Calendar.MINUTE, 0);
		cal.set(Calendar.HOUR, 0);

		double div = 24.0 / numberOfOptions;
		int minInc = (int) (div * 60.0);
		options[0] = "12:00 " + Schedule.AM;
		for (int i = 1; i < numberOfOptions; i++) {
			cal.add(Calendar.MINUTE, minInc);
			int hour = cal.get(Calendar.HOUR), minute = cal.get(Calendar.MINUTE), amOrPm = cal.get(Calendar.AM_PM);
			String hourFill = (hour < 10) ? "0" : "", minuteFill = (minute < 10) ? "0" : "";

			options[i] = ((hour == 0) ? "12" : hourFill + hour) + ":" + minuteFill + minute + " "
					+ ((amOrPm == 0) ? Schedule.AM : Schedule.PM);
		}

		return options;
	}

	private ActionListener buildToggleActionListener(JCheckBox... toggleOffCheckBox) 
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (int i = 0; i < toggleOffCheckBox.length; i++) {
					toggleOffCheckBox[i].setSelected(false);
				}
			}
		};
	}

	public void setTimeGapDivisionsPerDay(int timeGap) 
	{
		this.timeGap = timeGap;
	}

	private ScheduledCommand buildScheduledCommand(ScheduledCommand sc) 
	{
		String dayOfWk = "";
		for (JCheckBox t : daysOfWeek) {
			if (t.isSelected()) {
				dayOfWk += t.getText() + Schedule.DELIMITER_WEEKDAY;
			}
		}
		if (everyDay.isSelected()) {
			dayOfWk += everyDay.getText() + Schedule.DELIMITER_WEEKDAY;
		}
		if (!dayOfWk.isEmpty()) {
			dayOfWk = dayOfWk.substring(0, dayOfWk.length() - 1);
		}
		
		CommandBuild cb = (CommandBuild) cbe.getComponentValueObj();
		sc.setCommandBuild(cb);
		
		Schedule sch = null;
		for(int i = 0; i < timeOptions.size(); i++)
		{
			if(i >= sc.getSchedules().size())
			{
				sch = new Schedule();
				sc.addSchedule(sch);
			}
			else
			{
				sch = sc.getSchedules().get(i);
			}
			sch = setTimeOptions(sch, i);
			sch.setDayOfWeek(dayOfWk);
		}
		
		return sc;
	}
	
	private Schedule setTimeOptions(Schedule sch, int index)
	{
		String option = (String) timeOptions.get(index).getSelectedItem();
		String[] sp = option.split(":");
		
		int hour = Integer.parseInt(sp[0]);
		
		String[] minAmPM = sp[1].split(" ");
		int minute = Integer.parseInt(minAmPM[0]);
		String amPm = minAmPM[1];// TODO
		
		sch.setHourMinuteAmOrPm(hour, minute, amPm);
		return sch;
	}
	
	private void addTimeOption()
	{
		String[] options = buildTimePickerOptions(timeGap);
		JComboBox<String> newTime = new JComboBox<String>(options);
		timeOptions.add(newTime);
		
		for(JCheckBox cb : daysOfWeek)
		{
			innerPanel.remove(cb);
		}
		innerPanel.remove(everyDay);
		
		innerPanel.add(newTime);
		for(JCheckBox cb : daysOfWeek)
		{
			innerPanel.add(cb);
		}
		innerPanel.add(everyDay);
		this.getRootPane().validate();
	}
	
	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().validate();
	}

}
