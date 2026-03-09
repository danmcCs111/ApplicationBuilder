package WidgetComponents;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerNumberModel;

import Properties.LoggingMessages;
import WidgetComponentInterfaces.DurationLimitSubscriber;

public class DurationLimiter extends JPanel
{
	public enum Mode
	{
		GreaterThan("> Greater Than"),
		LessThan("< Less Than");
		
		private String label;
		private Mode(String label)
		{
			this.label = label;
		}
		
		public Mode getMode(String label)
		{
			for(Mode m : values())
			{
				if(m.label.equals(label))
				{
					return m;
				}
			}
			return null;
		}
		public String toString()
		{
			return this.label;
		}
	}
	
	private static final long serialVersionUID = 1L;
	
	private static String
		DURATION_LIMITER_LABEL = "Duration: ",
		FILTER_BUTTON_TEXT = "Filter",
		MINUTE_FIELD = " : ";
	
	private static Dimension
		MIN_SIZE = new Dimension(400, 150);
	
	private JLabel
		durationLabel = new JLabel(DURATION_LIMITER_LABEL),
		minuteLabel = new JLabel(MINUTE_FIELD);
	private JSpinner 
		hourSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 12, 1)),
		minuteSpinner = new JSpinner(new SpinnerNumberModel(0, 0, 59, 1));
	private JComboBox<Mode>
		greaterLessThan = new JComboBox<Mode>(Mode.values());
	private JButton 
		filterButton = new JButton(FILTER_BUTTON_TEXT);
	
	private DurationLimitSubscriber 
		dls;
	
	public DurationLimiter(DurationLimitSubscriber dls)
	{
		this.dls = dls;
		buildWidgets();
	}
	
	public void buildWidgets()
	{
		this.add(durationLabel);
		this.add(greaterLessThan);
		this.add(hourSpinner);
		this.add(minuteLabel);
		this.add(minuteSpinner);
		this.add(filterButton);
		
		filterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dls.notifyDurationLimit(
					(int)hourSpinner.getValue(), 
					(int)minuteSpinner.getValue(), 
					(Mode)greaterLessThan.getSelectedItem());
			}
		});
	}
	
	
	
	public static void main(String [] args)
	{
		JFrame f = new JFrame();
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				LoggingMessages.printOut(hour + " : " + minute + " - " + m);
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		f.add(dl);
		
		f.setMinimumSize(MIN_SIZE);
		f.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		f.setVisible(true);
	}
}
