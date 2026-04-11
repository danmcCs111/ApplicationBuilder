package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.YoutubeChannelVideo;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class AllVideoChannelsOpenedPlayer extends JFrame implements DefaultAndScaledImage
{
	private static final long serialVersionUID = 1L;

	private static Dimension 
		MIN_SIZE = new Dimension(1050, 450);
	private static String
		TITLE = "All Open Video Channels",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		ALL_SELECT_TEXT = "All Channels";
	private static int 
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25,
		SCALED_WIDTH_ICON = 35;
	
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs; 
	private HashMap<Integer, JButtonLengthLimited> 
		parentButtons;
	private JButton 
		imageLabel = new JButton();
	private Container 
		parentContainer;
	private VideoChannelListView 
		listView; 
	private JScrollPane 
		channelScroll,
		contentScrollPane;
	private DefaultAndScaledImage 
		parentScaler;
	private HashMap<JButtonLengthLimited, ImageIcon>
		icon = new HashMap<JButtonLengthLimited, ImageIcon>();
//	private LookupOrCreateYoutube 
//		lcv = new LookupOrCreateYoutube();
	
	public AllVideoChannelsOpenedPlayer(DefaultAndScaledImage parentScaler, ArrayList<JButtonLengthLimited> jblls, Container parentContainer)
	{
		this.parentScaler = parentScaler;
		this.parentButtons = new HashMap<Integer, JButtonLengthLimited>();
		this.parentContainer = parentContainer;
		this.ycvs = new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		ArrayList<Thread> threads = new ArrayList<Thread>();
		ArrayList<ChannelLoadingRunnable> clrs = new ArrayList<ChannelLoadingRunnable>();
		
		Runnable r = new Runnable()
		{
			@Override
			public void run() {
				for(JButtonLengthLimited jbll : jblls)
				{
					ChannelLoadingRunnable clr = new ChannelLoadingRunnable(jbll);
					clrs.add(clr);
					Thread t = new Thread(clr);
					t.start();
					threads.add(t);
				}
				
				buildLoadingFrame(threads, clrs);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void buildLoadingFrame(ArrayList<Thread> threads, ArrayList<ChannelLoadingRunnable> clrs) 
	{
		JFrame loadingFrame = new JFrame();
		loadingFrame.setResizable(false);
		loadingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadingFrame.setVisible(true);
		GraphicsUtil.rightEdgeTopWindow(parentContainer, loadingFrame);
		
		loadingFrame.setMinimumSize(new Dimension(180,70));//TODO
		LoadingLabel label = new LoadingLabel();
		label.updateCount(SCROLL_UNIT_INC, DEFAULT_MINUTE_SETTING);
		loadingFrame.add(label);
		
		ColorTemplate.setBackgroundColorPanel(loadingFrame, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(loadingFrame, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(loadingFrame, ColorTemplate.getButtonForegroundColor());
		
		Runnable r = new Runnable() 
		{
			@Override
			public void run() {
				while(true)
				{
					int count = getTotalLoaded(threads);
					label.updateCount(count, threads.size());
					if(count == threads.size())
					{
						break;
					}
					try {
						Thread.sleep(100);
					} catch (InterruptedException e) {
						e.printStackTrace();
					}
				}
				loadingFrame.dispose();
				collectYoutubeChannelsFromThreads(clrs);
				buildWidgets();
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	private int getTotalLoaded(ArrayList<Thread> threads)
	{
		int totalLoaded = 0;
		for(int i = 0; i < threads.size(); i++)
		{
			Thread t = threads.get(i);
			if(!t.isAlive())
			{
				totalLoaded++;
			}
		}
		return totalLoaded;
	}
	
	private void collectYoutubeChannelsFromThreads(
			ArrayList<ChannelLoadingRunnable> clrs)
	{
		for(ChannelLoadingRunnable clr : clrs)
		{
			HashMap <Integer, ArrayList <YoutubeChannelVideo>> vids = clr.getYoutubeChannels();
			if(vids != null && !vids.isEmpty())
			{
				int key = vids.keySet().iterator().next();
				this.ycvs.put(key, vids.get(key));
				this.parentButtons.put(key, clr.getButton());
			}
		}
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
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0,1));
		channelScroll = new JScrollPane(listPanel);
		channelScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		AbstractButton allSelectBtn = buildAllSelectionButton();
		listPanel.add(allSelectBtn);
		for(int i : parentButtons.keySet())
		{
			AbstractButton ab = buildSelectionButton(parentButtons.get(i), ycvs.get(i));
			listPanel.add(ab);
		}
		
		listView = new VideoChannelListView(parentButtons, ycvs);
		
		JPanel searchPanel = new JPanel();
		searchPanel.setLayout(new FlowLayout());
		setImageButton(null);
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
		
		searchPanel.add(imageLabel);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
		this.setLayout(new BorderLayout());
		this.add(channelScroll, BorderLayout.WEST);
		addListView();
		this.add(searchPanel, BorderLayout.NORTH);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), contentScrollPane);
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
		
		allSelectBtn.setBackground(ColorTemplate.getButtonForegroundColor());
		allSelectBtn.setForeground(ColorTemplate.getButtonBackgroundColor());
		
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		listView.postFrameBuild();
	}
	
	public void addListView()
	{
		contentScrollPane = new JScrollPane(listView);
		contentScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		this.add(contentScrollPane, BorderLayout.CENTER);
	}
	
	public void removeListView()
	{
		this.remove(contentScrollPane);
	}
	
	public void setImageButton(JButtonLengthLimited jbll)
	{
		for(MouseListener ml : imageLabel.getMouseListeners())
		{
			imageLabel.removeMouseListener(ml);
		}
		
		if(jbll == null)
		{
			imageLabel.setVisible(false);
			return;
		}
		
		imageLabel.setVisible(true);
		
		ImageIcon ii = getImage(jbll);
		imageLabel.setIcon(ii);
		imageLabel.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", jbll.getText()));
		
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				switch(button)
				{
				case MouseEvent.BUTTON1:
					jbll.doClick();
					break;
				case MouseEvent.BUTTON2:
					for(MouseListener ml : jbll.getMouseListeners())
					{
						e.setSource(jbll);
						ml.mouseClicked(e);
					}
					break;
				case MouseEvent.BUTTON3://ignore
					break;
				}
			}
		});
	}
	
	private ImageIcon getImage(JButtonLengthLimited jbll)
	{
		if(icon.containsKey(jbll))
			return icon.get(jbll);
		
		String path = jbll.getPath();
		ImageReader buttonImageReader = new ImageReader(this, true);
		DirectorySelection ds = new DirectorySelection(path);
		File f = new File(ds.getFullPath() + "/images/" + jbll.getFullLengthText() + ".png");
		LoggingMessages.printOut(f.toString());
		
		icon.put(jbll, buttonImageReader.getImageIcon(f));
		
		return icon.get(jbll);
	}
	
	public AbstractButton buildAllSelectionButton()
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(35);
		jbll.setText(ALL_SELECT_TEXT);
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listView = new VideoChannelListView(parentButtons, ycvs);
				removeListView();
				addListView();
				
				setImageButton(null);
				
				AllVideoChannelsOpenedPlayer.this.validate();
				ColorTemplate.setBackgroundColorPanel(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getPanelBackgroundColor());
				ColorTemplate.setBackgroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonBackgroundColor());
				ColorTemplate.setForegroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonForegroundColor());
				ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
						ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), contentScrollPane);
				ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
						ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
				jbll.setBackground(ColorTemplate.getButtonForegroundColor());
				jbll.setForeground(ColorTemplate.getButtonBackgroundColor());
				listView.postFrameBuild();
			}
		});
		
		return jbll;
	}
	
	public AbstractButton buildSelectionButton(JButtonLengthLimited parentButton, ArrayList<YoutubeChannelVideo> ycv)
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(35);
		jbll.setText(parentButton.getText());
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listView = new VideoChannelListView(parentButton, ycv);
				removeListView();
				addListView();
				
				setImageButton(parentButton);//TODO
				
				AllVideoChannelsOpenedPlayer.this.validate();
				ColorTemplate.setBackgroundColorPanel(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getPanelBackgroundColor());
				ColorTemplate.setBackgroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonBackgroundColor());
				ColorTemplate.setForegroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonForegroundColor());
				ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
						ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), contentScrollPane);
				ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
						ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
				jbll.setBackground(ColorTemplate.getButtonForegroundColor());
				jbll.setForeground(ColorTemplate.getButtonBackgroundColor());
				listView.postFrameBuild();
			}
		});
		return jbll;
	}

	@Override
	public String getDefaultImagePath() 
	{
		return parentScaler.getDefaultImagePath();
	}

	@Override
	public Dimension getScaledDefaultPic() 
	{
		return parentScaler.getScaledDefaultPic();
	}

	@Override
	public Dimension getDefaultPicSize() 
	{
		return parentScaler.getDefaultPicSize();
	}

	@Override
	public int getScaledWidth() 
	{
		return SCALED_WIDTH_ICON;
	}

	@Override
	public void setDefaultImageXmlPath(FileSelection fs) 
	{
		return;
	}

	@Override
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension) 
	{
		return;
	}

	@Override
	public void setDefaultPicSize(Dimension defaultPicDimension) 
	{
		return;
	}

	@Override
	public void setScaledWidth(int scaledWidth) 
	{
		SCALED_WIDTH_ICON = scaledWidth;
	}
	
}
