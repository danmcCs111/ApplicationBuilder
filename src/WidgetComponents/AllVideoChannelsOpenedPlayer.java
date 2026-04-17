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
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.YoutubeChannelVideo;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.RegisterArrayActionListener;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class AllVideoChannelsOpenedPlayer extends JFrame implements DefaultAndScaledImage, ArrayActionListener
{
	private static final long serialVersionUID = 1L;

	private static Dimension 
		MIN_SIZE = new Dimension(1050, 450);
	private static String
		TITLE = "All Open Video Channels",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		UPDATE_BUTTON_TEXT = "Update",
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
		updateButton = new JButton(UPDATE_BUTTON_TEXT),
		imageLabel = new JButton();
	private JButtonLengthLimited
		selectedButton = null,
		selectedButtonParent = null;
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
	private HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>
		parentButtonAndYoutubeVideos = new HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>();
	private HashMap<AbstractButton, JButtonLengthLimited>
		selectionButtonAndParentButton = new HashMap<AbstractButton, JButtonLengthLimited>();
	private LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	private OpenVideoChannelsUpdater
		ovcu;
	private AbstractButton
		highlightButton;
	private Border
		defaultBorder = new JButton().getBorder();
	
	public AllVideoChannelsOpenedPlayer(DefaultAndScaledImage parentScaler, 
			ArrayList<JButtonLengthLimited> jblls, 
			Container parentContainer)
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
		
		listView = new VideoChannelListView(parentButtons, ycvs);
		
		JPanel searchPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		searchPanel.setLayout(fl);
		
		updateButton.addActionListener(getUpdateChannelsActionListener());
		
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
		searchPanel.add(updateButton);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
		AbstractButton allSelectBtn = buildAllSelectionButton();
		listPanel.add(allSelectBtn);
		for(int i : parentButtons.keySet())
		{
			parentButtonAndYoutubeVideos.put(parentButtons.get(i), ycvs.get(i));
			AbstractButton ab = buildSelectionButton(parentButtons.get(i));
			selectionButtonAndParentButton.put(ab, parentButtons.get(i));
			ImageIcon ii = getImage(parentButtons.get(i));
			ab.setIcon(ii);
			ab.setHorizontalAlignment(AbstractButton.LEFT);
			listPanel.add(ab);
		}
		setImageButton(null);
		
		this.setLayout(new BorderLayout());
		this.add(channelScroll, BorderLayout.WEST);
		addListView();
		this.add(searchPanel, BorderLayout.NORTH);
		
		refreshListView(allSelectBtn);
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
		
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(JButtonArray.getMoviesIcon());
		RegisterArrayActionListener.addListener(this);
		this.setVisible(true);
		unselect(LaunchUrlActionListener.getLastButtonOrigin());
		GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		
	}
	
	public void addListView()
	{
		if(contentScrollPane == null)
		{
			contentScrollPane = new JScrollPane();
			contentScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		}
		contentScrollPane.setViewportView(listView);
		this.add(contentScrollPane, BorderLayout.CENTER);
	}
	
	public void removeListView()
	{
		contentScrollPane.remove(listView);
		this.remove(contentScrollPane);
	}
	
	public void setImageButton(JButtonLengthLimited jbll)
	{
		for(MouseListener ml : imageLabel.getMouseListeners())
		{
			imageLabel.removeMouseListener(ml);
		}
		for(ActionListener al : updateButton.getActionListeners())
		{
			updateButton.removeActionListener(al);
		}
		
		if(jbll == null)
		{
			imageLabel.setVisible(false);
			updateButton.addActionListener(getUpdateChannelsActionListener());
			return;
		}
		imageLabel.setVisible(true);
		updateButton.addActionListener(getUpdateChannelActionListener());
		
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
				refreshListView(jbll);
			}
		});
		
		return jbll;
	}
	
	public AbstractButton buildSelectionButton(JButtonLengthLimited parentButton)
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(35);
		jbll.setText(parentButton.getText());
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedButton = jbll;
				selectedButtonParent = parentButton;
				
				ArrayList<YoutubeChannelVideo> ycv = parentButtonAndYoutubeVideos.get(parentButton);
				int count = lcv.lookupCount(parentButton.getText(), parentButton.getName());
				
				if(count > ycv.size())
				{
					refreshSelectionFromDB(selectedButtonParent, selectedButton);
				}
				else
				{
					refreshSelection(selectedButtonParent, selectedButton);
				}
			}
		});
		return jbll;
	}
	
	public void refreshListView(AbstractButton selectedButton)
	{
		ColorTemplate.setBackgroundColorPanel(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), contentScrollPane);
		
		selectedButton.setBackground(ColorTemplate.getButtonForegroundColor());
		selectedButton.setForeground(ColorTemplate.getButtonBackgroundColor());
		listView.postFrameBuild();
		AllVideoChannelsOpenedPlayer.this.validate();
	}
	
	private ActionListener getUpdateChannelActionListener() 
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedButton == null)
					return;
				
				refreshSelectionFromDB(selectedButtonParent, selectedButton);
			}
		};
	}
	
	private ActionListener getUpdateChannelsActionListener()
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(ovcu != null)
				{
					ovcu.dispose();
				}
				Runnable r = new Runnable() {
					@Override
					public void run() {
						List<JButtonLengthLimited> jblls = Arrays.asList
								(parentButtons.values().toArray(new JButtonLengthLimited [] {}));
						ovcu = new OpenVideoChannelsUpdater(jblls);
						GraphicsUtil.centerWindow(AllVideoChannelsOpenedPlayer.this, ovcu);
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		};
	}
	
	private void refreshSelection(JButtonLengthLimited buttonParent, JButtonLengthLimited selectedButton)
	{
		ArrayList<YoutubeChannelVideo> ycv = parentButtonAndYoutubeVideos.get(buttonParent);
		listView = new VideoChannelListView(buttonParent, ycv);
		removeListView();
		addListView();
		refreshListView(selectedButton);
		setImageButton(buttonParent);
	}
	
	private void refreshSelectionFromDB(JButtonLengthLimited selectedButtonParent, JButtonLengthLimited selectedButton)
	{
		lcv.update(selectedButtonParent.getText(), selectedButtonParent.getName());
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = lcv.lookup(
				selectedButtonParent.getText(), selectedButtonParent.getName());
		int key = ycvs.keySet().iterator().next();
		parentButtonAndYoutubeVideos.put(selectedButtonParent, ycvs.get(key));
		removeListView();
		listView = new VideoChannelListView(selectedButton, ycvs);
		addListView();
		setImageButton(selectedButtonParent);//TODO
		refreshListView(selectedButton);
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

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void unselect(AbstractButton newButton) 
	{
		if(highlightButton != null)
		{
			highlightButton.setBorder(defaultBorder);
		}
		if(newButton == null)
		{
			return;
		}
		
		AbstractButton altButton = null;
		if(newButton instanceof JButtonLengthLimited)
		{
			altButton = ((JButtonLengthLimited) newButton).getHighlightButton();
		}
		for(AbstractButton ab : selectionButtonAndParentButton.keySet())
		{
			if(ab.getText().equals(newButton.getText()) || 
					(altButton != null && altButton.getText().equals(ab.getText()))
			)
			{
				//highlight.
				highlightButton = ab;
				highlightButton.setBorder(Highlighter.getBorderHighlight());
				break;
			}
		}
		
		channelScroll.repaint();
		channelScroll.validate();
	}

	@Override
	public void addArrayActionListener() 
	{
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	@Override
	public void removeArrayActionListener() 
	{
		LaunchUrlActionListener.removeArrayActionListener(this);
	}

	@Override
	public void addStripFilter(String filter) {
		// TODO Auto-generated method stub
	}
	
}
