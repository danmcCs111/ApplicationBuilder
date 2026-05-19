package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.TitledBorder;

import Graphics2D.ColorTemplate;
import HttpDatabaseResponse.DatabaseResponseNode;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.VideoChannel;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class EditChannelsHandle extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static Dimension 
		CHANNEL_LABEL_DIM = new Dimension(50, 50),
		MIN_SIZE = new Dimension(500, 450);
	private static String
		TITLE = "Channel Properties Editor",
		HANDLE_LABEL = "Handle: ",
//		STORE_RECORD_LABEL = "Store Record",
//		COUNT_LABEL = "Count: ",
		SAVE_BUTTON_TEXT = "Update",
		CANCEL_BUTTON_TEXT = "Close";
	private static int
		HANDLE_COLUMN_LENGTH = 10,
		SCROLL_INC = 25;
	
	private ArrayList<VideoChannel>
		videoChannels;
	
	private JScrollPane
		scrollPane;
	private JPanel
		saveCancelPanel,
		channelsPanel;
	private JButton
		cancelButton;
	private Container
		parentContainer;
	
	public EditChannelsHandle(Container parentContainer)
	{
		this.parentContainer = parentContainer;
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		this.setLayout(new BorderLayout());
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	@SuppressWarnings("unchecked")
	public void setChannels(ArrayList<?> jblls)
	{
		videoChannels = new ArrayList<VideoChannel>();
		if(jblls.get(0) instanceof JButtonLengthLimited)
		{
			for(JButtonLengthLimited jbll : (ArrayList<JButtonLengthLimited>)jblls)
			{
				ArrayList <ArrayList <DatabaseResponseNode>> drns = LookupOrCreateYoutube.lookupVideoChannel(jbll.getText());
				VideoChannel vc = new VideoChannel(drns.get(1));
				videoChannels.add(vc);
			}
		}
		else if(jblls.get(0) instanceof JCheckBox)
		{
			for(JCheckBox cb : (ArrayList<JCheckBox>)jblls)
			{
				String [] args = cb.getName().split(OpenVideoChannelsUpdater.NAME_DELIMITER);
				ArrayList <ArrayList <DatabaseResponseNode>> drns = LookupOrCreateYoutube.lookupVideoChannel(args[0]);
				VideoChannel vc = new VideoChannel(drns.get(1));
				videoChannels.add(vc);
			}
		}
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		channelsPanel = new JPanel();
		channelsPanel.setLayout(new GridLayout(0,1));
		
		for(VideoChannel vc : videoChannels)
		{
			JLabel
				channelNameLabel = new JLabel(HANDLE_LABEL);
			JTextField
				channelHandleName = new JTextField(vc.getHandle());
			JPanel 
				channelPanel = new JPanel(),
				innerPanel = new JPanel();
//				innerPanel2 = new JPanel();
			
			channelNameLabel.setPreferredSize(CHANNEL_LABEL_DIM);
			channelHandleName.setColumns(HANDLE_COLUMN_LENGTH);
			
			channelPanel.setLayout(new GridLayout(0,1));
			FlowLayout flInner = new FlowLayout();
			flInner.setAlignment(FlowLayout.LEFT);
			channelPanel.setLayout(flInner);
			channelPanel.setBorder(BorderFactory.createTitledBorder(
	                BorderFactory.createBevelBorder(BevelBorder.RAISED),
	                vc.getName(),
	                TitledBorder.LEFT,
	                TitledBorder.TOP,
	               channelNameLabel.getFont(),
	                Color.LIGHT_GRAY
	        ));
			
			JButton saveButton = new JButton(SAVE_BUTTON_TEXT);
			saveButton.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					vc.setHandle(channelHandleName.getText().strip());
					performUpdate(vc);
				}
			});
			
//			JCheckBox 
//				storeRecord = new JCheckBox(STORE_RECORD_LABEL);
//			JLabel 
//				countLabel = new JLabel(COUNT_LABEL),
//				countLabelValue = new JLabel();
//			int count = LookupOrCreateYoutube.lookupCount(vc.getName(), vc.getUrl());
//			countLabelValue.setText(count+"");
			
			innerPanel.add(channelNameLabel);
			innerPanel.add(channelHandleName);
			
//			innerPanel2.add(countLabel);
//			innerPanel2.add(countLabelValue);
//			innerPanel2.add(storeRecord);
//			innerPanel2.add(saveButton);
			innerPanel.add(saveButton);
			
			channelPanel.add(innerPanel);
//			channelPanel.add(innerPanel2);
			
			channelsPanel.add(channelPanel);
		}
		
		scrollPane = new JScrollPane();
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INC);
		scrollPane.setViewportView(channelsPanel);
		
		saveCancelPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.RIGHT);
		saveCancelPanel.setLayout(fl);
		buildSaveCancel();
		
				
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(saveCancelPanel, BorderLayout.SOUTH);
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);

		this.setLocation(parentContainer.getLocation());
		this.setVisible(true);
	}
	
	private void buildSaveCancel()
	{
		cancelButton = new JButton(CANCEL_BUTTON_TEXT);
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				EditChannelsHandle.this.dispose();
			}
		});
		saveCancelPanel.add(cancelButton);
	}
	
	private void performUpdate(VideoChannel vc)
	{
		LookupOrCreateYoutube.updateVideoChannel(vc);
	}
	
}
