package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.LookupOrCreateYoutube;
import Properties.LoggingMessages;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class OpenVideoChannelsUpdater extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE_TEXT = "Open Channels Updater",
		ALL_CHECKBOX_TEXT = "Select All",
		UPDATE_BUTTON_TEXT = "Update",
		NAME_DELIMITER = ":@:";
	private static Dimension 
		MIN_SIZE = new Dimension(750, 450);
	private static final SimpleDateFormat
		SDF_DATE_LABEL = new SimpleDateFormat("MM/dd/YYYY");
	
	private JPanel
		controlPanel,
		checkBoxPanel,
		buttonPanel;
	private JButton
		updateButton;
	private ArrayList<JCheckBox>
		checkBoxes;
	private ArrayList<JButtonLengthLimited> 
		jblls;
	private HashMap<JCheckBox, Date> 
		checkBoxLatestDate;
	private LookupOrCreateYoutube
		lcv = new LookupOrCreateYoutube();
	
	public OpenVideoChannelsUpdater(ArrayList<JButtonLengthLimited> jblls, Container parentContainer)
	{
		this.jblls = jblls;
		GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		checkBoxes = new ArrayList<JCheckBox>();
		checkBoxLatestDate = new HashMap<JCheckBox, Date>();
		
		FlowLayout flControl =new FlowLayout();
		flControl.setAlignment(FlowLayout.LEFT);
		controlPanel = new JPanel(flControl);
		
		checkBoxPanel = new JPanel(new GridLayout(0,1));
		
		FlowLayout flButtonPanel =new FlowLayout();
		flButtonPanel.setAlignment(FlowLayout.RIGHT);
		buttonPanel = new JPanel(flButtonPanel);
		updateButton = new JButton(UPDATE_BUTTON_TEXT);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performUpdate();
			}
		});
		
		buildControlPanel();
		buildCheckBoxPanel();
		
		buttonPanel.add(updateButton);
		
		JScrollPane scrollPane = new JScrollPane(checkBoxPanel);
		scrollPane.getVerticalScrollBar().setUnitIncrement(25);
		
		this.setTitle(TITLE_TEXT);
		this.setMinimumSize(MIN_SIZE);
		this.setLayout(new BorderLayout());
		this.add(controlPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(buttonPanel, BorderLayout.SOUTH);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private void buildControlPanel()
	{
		JCheckBox allCheckBox = new JCheckBox(ALL_CHECKBOX_TEXT);
		allCheckBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean select = allCheckBox.isSelected();
				for(JCheckBox cb : checkBoxes)
				{
					cb.setSelected(select);
				}
			}
		});
		
		controlPanel.add(allCheckBox);
	}
	
	private void buildCheckBoxPanel()
	{
		for(JButtonLengthLimited jbll : this.jblls)
		{
			JCheckBox cb = new JCheckBox();
			cb = updateCheckBox(jbll.getText(), jbll.getName(), cb);
			
			checkBoxes.add(cb);
			checkBoxPanel.add(cb);
		}
	}
	
	private JCheckBox updateCheckBox(String text, String name, JCheckBox cb)
	{
		HashMap<Integer, Date> 
			parentIdAndLatestDate = lcv.lookupLatestDate(text, name),
			parentIdAndFirstDate = lcv.lookupFirstDate(text, name);
		Date 
			latestDate = null,
			firstDate = null;
		
		if(parentIdAndLatestDate != null)
		{
			int parentId = parentIdAndLatestDate.keySet().iterator().next();
			latestDate = parentIdAndLatestDate.get(parentId);
			firstDate = parentIdAndFirstDate.get(parentId);
		}
		String 
			dateLatestText = latestDate == null ? "" : SDF_DATE_LABEL.format(latestDate),
			dateFirstText = firstDate == null ? "" : SDF_DATE_LABEL.format(firstDate);
				
		cb.setText(text + " " + dateLatestText + " - " + dateFirstText);
		cb.setName(text + NAME_DELIMITER + name);
		
		checkBoxLatestDate.put(cb, latestDate);
		
		return cb;
	}
	
	private void performUpdate()
	{
		for(JCheckBox cb : checkBoxes)
		{
			if(cb.isSelected())
			{
				String [] args = cb.getName().split(NAME_DELIMITER);
				LoggingMessages.printOut("args: " + args[0] + args[1]);
				lcv.update(args[0], args[1], checkBoxLatestDate.get(cb));
				cb = updateCheckBox(args[0], args[1], cb);
				cb.setSelected(false);
			}
		}
	}

}
