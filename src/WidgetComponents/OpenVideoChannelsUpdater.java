package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.GridBagConstraints;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import javax.swing.JFrame;
import javax.swing.JPanel;

import MouseListenersImpl.LookupOrCreateYoutube;

public class OpenVideoChannelsUpdater extends JFrame
{
	private static final long serialVersionUID = 1L;
	
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
		gbcChannel.weightx = .1;
		gbcChannel.weighty = 1;
		
		GridBagConstraints gbcDate = new GridBagConstraints();
		gbcDate.fill = GridBagConstraints.BOTH;
		gbcDate.gridx = gridXinc++;
		gbcDate.gridy = 0;
		gbcDate.weightx = .1;
		gbcDate.weighty = 1;
		
		for(JButtonLengthLimited jbll : this.jblls)
		{
			HashMap<Integer, Date> parentIdAndDate = lcv.lookupLatestDate(jbll.getText(), jbll.getName());
			
		}
		
		listPanel.add(checkBoxPanel, gbcCheck);
		listPanel.add(channelListPanel, gbcChannel);
		listPanel.add(datePanel, gbcDate);
		
		this.setLayout(new BorderLayout());
		this.add(listPanel, BorderLayout.NORTH);
		
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}

}
