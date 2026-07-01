package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import MouseListenersImpl.LookupOrCreateYoutube;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;
import WidgetUtility.FileListOptionGenerator;

public class OpenVideoChannelsUpdater extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE_TEXT = "Open Channels Updater",
		ALL_CHECKBOX_TEXT = "Select All",
		EDIT_BUTTON_TEXT = "Edit",
		UPDATE_BUTTON_TEXT = "Update",
		CANCEL_BUTTON_TEXT = "Close";
	private static int
		CHARACTER_LIMIT = 35;
	public static final String
		PROPERTIES_VALUE_DELIMITER = "=",
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
		editButton,
		updateButton,
		cancelButton;
	private ArrayList<JCheckBox>
		checkBoxes;
	private List<JButtonLengthLimited> 
		jblls;
	private HashMap<JCheckBox, Date> 
		checkBoxLatestDate;
	private EditChannelsHandle 
		ech;
	private static ArrayList <String> 
		stripFilter = new ArrayList<String>(); 
	
	public OpenVideoChannelsUpdater(List<JButtonLengthLimited> jblls)
	{
		this.jblls = jblls;
		Runnable r = new Runnable()
		{
			@Override
			public void run() {
				buildWidgets();
			}
		};
		Thread t = new Thread(r);
		t.start();
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
		
		editButton = new JButton(EDIT_BUTTON_TEXT);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				performEdit();
			}
		});
		cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancel();
			}
		});
		
		buildControlPanel();
		buildCheckBoxPanel();
		
		buttonPanel.add(editButton);
		buttonPanel.add(updateButton);
		buttonPanel.add(cancelButton);
		
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
			if(cb != null)
			{
				checkBoxes.add(cb);
				checkBoxPanel.add(cb);
			}
		}
	}
	
	private JCheckBox updateCheckBox(String text, String name, JCheckBox cb)
	{
		HashMap<Integer, Date> 
			parentIdAndLatestDate = LookupOrCreateYoutube.lookupLatestDate(text, name),
			parentIdAndFirstDate = LookupOrCreateYoutube.lookupFirstDate(text, name);
		Date 
			latestDate = null,
			firstDate = null;
		
		if(!parentIdAndLatestDate.keySet().iterator().hasNext())
			return null;
		
		if(parentIdAndLatestDate != null)
		{
			int parentId = parentIdAndLatestDate.keySet().iterator().next();
			latestDate = parentIdAndLatestDate.get(parentId);
			firstDate = parentIdAndFirstDate.get(parentId);
		}
		String 
			dateLatestText = latestDate == null ? "" : SDF_DATE_LABEL.format(latestDate),
			dateFirstText = firstDate == null ? "" : SDF_DATE_LABEL.format(firstDate);
				
		cb.setText(dateLatestText + " - " + dateFirstText + " [ " + text + " ] ");
		cb.setName(text + NAME_DELIMITER + name);
		
		checkBoxLatestDate.put(cb, latestDate);
		
		return cb;
	}
	
	private void performEdit()
	{
		if(ech == null || !ech.isVisible())
		{
			ech = new EditChannelsHandle(this);
			ArrayList<JCheckBox> cbs = new ArrayList<JCheckBox>();
			for(JCheckBox cb : checkBoxes)
			{
				if(cb.isSelected())
				{
					cbs.add(cb);
				}
			}
			if(!cbs.isEmpty())
			{
				ech.setChannels(cbs);
			}
		}
	}
	
	private void performUpdate()
	{
		for(JCheckBox cbL : checkBoxes)
		{
			if(cbL.isSelected())
			{
				String [] args = cbL.getName().split(NAME_DELIMITER);
				LoggingMessages.printOut("args: " + args[0] + args[1]);
				LookupOrCreateYoutube.update(args[0], args[1], checkBoxLatestDate.get(cbL));
				cbL = updateCheckBox(args[0], args[1], cbL);
				cbL.setSelected(false);
			}
		}
	}
	
	private void cancel()
	{
		this.dispose();
	}
	
	public static void filterText(JButtonLengthLimited jbl)
	{
		String txt = jbl.getFullLengthText();
		for(String s : stripFilter)
		{
			txt = txt.replace(s, "");
		}
		jbl.setText(txt);
	}
	
	public static void main(String [] args)
	{
		String filename = args[0];
		boolean isAlphaNumeric = Boolean.getBoolean(args[1]);
		String [] ptLoc = args[2].split(",");
		Point loc = new Point(Integer.parseInt(ptLoc[0]), Integer.parseInt(ptLoc[1]));
		String [] 
			strC1 = args[3].split(","),
			strC2 = args[4].split(","),
			strC3 = args[5].split(",");
		Color
			backgroundButton = new Color(Integer.parseInt(strC1[0]), Integer.parseInt(strC1[1]), Integer.parseInt(strC1[2])),
			foregroundButton = new Color(Integer.parseInt(strC2[0]), Integer.parseInt(strC2[1]), Integer.parseInt(strC2[2])),
			backgroundPanel = new Color(Integer.parseInt(strC3[0]), Integer.parseInt(strC3[1]), Integer.parseInt(strC3[2]));
		String [] stripFilterTmp = args[6].split(NAME_DELIMITER);
		for(String s : stripFilterTmp) stripFilter.add(s);
		
		ColorTemplate.setButtonBackgroundColor(backgroundButton);
		ColorTemplate.setButtonForegroundColor(foregroundButton);
		ColorTemplate.setPanelBackgroundColor(backgroundPanel);
		
		ArrayList<JButtonLengthLimited> jblls = new ArrayList<JButtonLengthLimited>();
		HashMap<String, String> props = PathUtility.readProperties(filename, PROPERTIES_VALUE_DELIMITER);
		for(String s : props.keySet())
		{
			String path = props.get(s);
			JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
					path, s.split("@")[0]+".url", JButtonLengthLimited.class);
			if(jbll == null)
				continue;
			
			jbll.setCharacterLimit(CHARACTER_LIMIT);
			filterText(jbll);
			jblls.add(jbll);
		}
		if(isAlphaNumeric)
		{
			Comparator<AbstractButton> buttonTextComparator = Comparator.comparing(
					AbstractButton::getText
			);
			jblls.sort(buttonTextComparator);
		}
		OpenVideoChannelsUpdater ovcu = new OpenVideoChannelsUpdater(jblls);
		ovcu.setLocation(loc);
	}

}
