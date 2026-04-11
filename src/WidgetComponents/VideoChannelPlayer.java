package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.FrameMouseDragListener;
import MouseListenersImpl.YoutubeChannelVideo;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelPlayer extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		UPDATE_BUTTON_TEXT = "Update",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		TITLE_PREFIX = "Channel | ";
	private static Dimension 
		MIN_SIZE = new Dimension(775, 425);
	private static int 
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25;
	
	private JButtonLengthLimited 
		parentButton;
	private VideoChannelListView 
		listView; 
	private JScrollPane 
		scrollPane;
	private ImageIcon 
		videoImage;
	private FrameMouseDragListener 
		fmdl;

	public VideoChannelPlayer(
			ImageIcon videoImage, FrameMouseDragListener fmdl, JButtonLengthLimited parentButton, Container parent)
	{
		this.parentButton = parentButton;
		this.videoImage = videoImage;
		this.fmdl = fmdl;
		this.setTitle(TITLE_PREFIX + parentButton.getText());
		buildWidgets(fmdl.getYoutubeVideos());
		GraphicsUtil.rightEdgeCenterWindow(parent, this);
	}
	
	public static void setDefaultMinuteSetting(int minute)
	{
		DEFAULT_MINUTE_SETTING = minute;
	}
	
	public VideoChannelListView getVideoChannelListView()
	{
		return this.listView;
	}
	
	private void buildWidgets(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		JPanel searchPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		searchPanel.setLayout(fl);
		
		JButton imageLabel = new JButton(videoImage);
		imageLabel.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", parentButton.getText()));
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				switch(button)
				{
				case MouseEvent.BUTTON1:
					parentButton.doClick();
					break;
				case MouseEvent.BUTTON2:
					for(MouseListener ml : parentButton.getMouseListeners())
					{
						e.setSource(parentButton);
						ml.mouseClicked(e);
					}
					break;
				case MouseEvent.BUTTON3://ignore
					break;
				}
			}
		});
		
		JButton updateButton = new JButton(UPDATE_BUTTON_TEXT);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = new Runnable() {
					@Override
					public void run() {
						fmdl.update();
						fmdl.buildVideoChannelPlayer();
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		});
		
		SearchBar sb = new SearchBar();
		sb.setColumnCharacterLength(SEARCH_COLUMN_LENGTH);
		sb.addSearchSubscriber(new SearchSubscriber() {
			@Override
			public void notifySearchText(String searchPattern) {
				listView.setVisible(searchPattern);
				VideoChannelPlayer.this.validate();
			}
		});
		
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				listView.setVisible(hour, minute, m);
				VideoChannelPlayer.this.validate();
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		dl.setMinuteDefault(DEFAULT_MINUTE_SETTING);
		
		searchPanel.add(imageLabel);
		searchPanel.add(updateButton);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
		listView = new VideoChannelListView(parentButton, ycvs);
		scrollPane = new JScrollPane(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		this.setIconImage(videoImage.getImage());
		this.setLayout(new BorderLayout());
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		listView.postFrameBuild();
	}
	
}
