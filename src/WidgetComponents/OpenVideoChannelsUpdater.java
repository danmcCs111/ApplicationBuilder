package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JCheckBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

import MouseListenersImpl.LookupOrCreateYoutube;

public class OpenVideoChannelsUpdater extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static Dimension 
		MIN_SIZE = new Dimension(750, 450);
	
	private static final SimpleDateFormat
		SDF_DATE_LABEL = new SimpleDateFormat("MM/dd/YYYY");
	
	private JPanel
		checkBoxPanel,
		channelListPanel,
		datePanel,
		listPanel;
	
	private ArrayList<JButtonLengthLimited> 
		jblls;
	private LookupOrCreateYoutube
		lcv = new LookupOrCreateYoutube();
	
	public OpenVideoChannelsUpdater(ArrayList<JButtonLengthLimited> jblls)
	{
		this.jblls = jblls;
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		checkBoxPanel = new JPanel(new GridLayout(0,1));
		datePanel = new JPanel(new GridLayout(0,1));
		channelListPanel = new JPanel(new GridLayout(0,1));
		
		int gridXinc = 0;
		
		GridBagConstraints gbcCheck = new GridBagConstraints();
		gbcCheck.fill = GridBagConstraints.BOTH;
		gbcCheck.gridx = gridXinc++;
		gbcCheck.gridy = 0;
		gbcCheck.weightx = .1;
		gbcCheck.weighty = 1;
		
		GridBagConstraints gbcChannel = new GridBagConstraints();
		gbcChannel.fill = GridBagConstraints.BOTH;
		gbcChannel.gridx = gridXinc++;
		gbcChannel.gridy = 0;
		gbcChannel.weightx = .7;
		gbcChannel.weighty = 1;
		
		GridBagConstraints gbcDate = new GridBagConstraints();
		gbcDate.fill = GridBagConstraints.BOTH;
		gbcDate.gridx = gridXinc++;
		gbcDate.gridy = 0;
		gbcDate.weightx = .2;
		gbcDate.weighty = 1;
		
		for(JButtonLengthLimited jbll : this.jblls)
		{
			HashMap<Integer, Date> parentIdAndDate = lcv.lookupLatestDate(jbll.getText(), jbll.getName());
			Date lastDate = null;
			if(parentIdAndDate != null)
			{
				int parentId = parentIdAndDate.keySet().iterator().next();
				lastDate = parentIdAndDate.get(parentId);
			}
			JLabel dateLabel = new JLabel(lastDate == null ? "" : SDF_DATE_LABEL.format(lastDate));
			JLabel channelLabel = new JLabel(jbll.getText());
			JCheckBox cb = new JCheckBox();
			
			checkBoxPanel.add(cb);
			channelListPanel.add(channelLabel);
			datePanel.add(dateLabel);
		}
		
		listPanel.add(checkBoxPanel, gbcCheck);
		listPanel.add(channelListPanel, gbcChannel);
		listPanel.add(datePanel, gbcDate);

		this.setMinimumSize(MIN_SIZE);
		this.setLayout(new BorderLayout());
		this.add(listPanel, BorderLayout.NORTH);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

}
