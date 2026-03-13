package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.YoutubeChannelVideo;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class AllVideoChannelsOpenedPlayer extends JFrame
{
	private static final long serialVersionUID = 1L;

	private static Dimension 
		MIN_SIZE = new Dimension(950, 450);
	private static String
		TITLE = "All Open Video Channels";
	private static int 
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25;
	
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs; 
	private HashMap<Integer, AbstractButton> 
		parentButton;
	private Container 
		parentContainer;
	private VideoChannelListView 
		listView; 
	private JScrollPane 
		scrollPane;
	private LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	
	public AllVideoChannelsOpenedPlayer(ArrayList<JButtonLengthLimited> jblls, Container parentContainer)
	{
		this.parentButton = new HashMap<Integer, AbstractButton>();
		this.ycvs = new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		for(JButtonLengthLimited jbll : jblls)
		{
			HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = lcv.lookup(jbll.getText(), jbll.getName());
			Iterator<Integer> it = ycvs.keySet().iterator();
			if(it.hasNext())
			{
				int key = it.next();
				this.parentButton.put(key, jbll);
				this.ycvs.put(key, ycvs.get(key));
			}
		}
		this.parentContainer = parentContainer;
		LoggingMessages.printOut("Build player");
		buildWidgets();
	}
	
	public static void setDefaultMinuteSetting(int minute)
	{
		DEFAULT_MINUTE_SETTING = minute;
	}
	
	public VideoChannelListView getVideoChannelListView()
	{
		return this.listView;
	}
	
	public void buildWidgets()
	{
		listView = new VideoChannelListView(parentButton, ycvs);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		SearchBar sb = new SearchBar();
		sb.setColumnCharacterLength(SEARCH_COLUMN_LENGTH);
		sb.addSearchSubscriber(new SearchSubscriber() {
			@Override
			public void notifySearchText(String searchPattern) {
				listView.setVisible(searchPattern);
				AllVideoChannelsOpenedPlayer.this.validate();
			}
		});
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				listView.setVisible(hour, minute, m);
				AllVideoChannelsOpenedPlayer.this.validate();
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		dl.setMinuteDefault(DEFAULT_MINUTE_SETTING);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
		scrollPane = new JScrollPane(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		this.setLayout(new BorderLayout());
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		this.setVisible(true);
		
		listView.postFrameBuild();
	}
	
}
