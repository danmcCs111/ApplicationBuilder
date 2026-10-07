package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JToggleButton;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Actions.CommandExecutor;
import ApplicationBuilder.QueryUpdateTool;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import HttpDatabaseRequest.HttpRequestProcessor;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.MouseDragScrollListener;
import MouseListenersImpl.VideoChannel;
import MouseListenersImpl.VideoSubSelectionLauncher;
import MouseListenersImpl.VideoUpdateTimespanDialog;
import MouseListenersImpl.YoutubeChannelVideo;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.FileSelection;
import ObjectTypeConversionEditors.TimestampEditor;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.RegisterArrayActionListener;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelsPlayerTab extends JPanel implements DefaultAndScaledImage, ArrayActionListener
{
	private static final long serialVersionUID = 1L;

	private static Dimension 
		DEFAULT_PIC_SIZE = new Dimension(279, 150),
		DEFAULT_SCALED_PIC_SIZE = new Dimension(279, 150);
	private static Point
		LAUNCH_LOCATION = new Point(600, 50);
	private static String
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		TIMESTAMP_APPLY = "Apply",
		COUNT_PREFIX = "Video Count: ",
		SHOW_ALL_BUTTON_TEXT = "Show All",
		SHOW_ALL_BUTTON_TOOLTIP_TEXT = "Toggle on/off channel fetch limit.",
		UPDATE_BUTTON_TEXT = "Update",
		UPDATE_VIEWER_BUTTON_TEXT = "List Update",
		ALL_SELECT_TEXT = "All Channels";
	private static int 
		CHANNEL_LIMIT_GLOBAL = -1,
		TOTAL_COUNT = 0,
		SCALED_WIDTH = 50,
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25;
	private static Border
		COUNT_BORDER = new EmptyBorder(5, 0, 5, 15);//EmptyBorder(top, left, bottom, right)
	private static FileSelection
		defaultFileImage = new FileSelection("./Properties/shapes/Default-Play-Image.xml");
	private static boolean
		IS_DRAG_SCROLL = true;
	private static Timestamp 
		AFTER_DATE_DEFAULT;
	static {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_MONTH, -1);
		AFTER_DATE_DEFAULT = new Timestamp(cal.getTimeInMillis());
	}
	private TimestampEditor
		afterDateEditor;
	private JButton
		applyButton;
	
	private JToggleButton
		showAllButton = new JToggleButton(SHOW_ALL_BUTTON_TEXT);
	private JButton 
		updateButton = new JButton(UPDATE_BUTTON_TEXT),
		updateViewer = new JButton(UPDATE_VIEWER_BUTTON_TEXT),
		imageLabel = new JButton();
	private JButtonLengthLimited
		selectedButtonParent = null;
	private Container 
		parentContainer;
	private VideoChannelListView 
		listView; 
	private JScrollPane 
		channelScroll,
		contentScrollPane;
	private AbstractButton
		selectedButton = null,
		highlightButton;
	private Border
		defaultBorder = new JButton().getBorder();
	private AbstractButton 
		allChannelsButton,
		allSelectBtn;
	
	private Date
		lastDate;
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs; 
	private LinkedHashMap<Integer, JButtonLengthLimited> 
		parentButtons;
	private HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>
		parentButtonAndYoutubeVideos = new HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>();
	private HashMap<AbstractButton, JButtonLengthLimited>
		selectionButtonAndParentButton = new HashMap<AbstractButton, JButtonLengthLimited>();
	private LinkedHashMap<JButtonLengthLimited, ImageIcon> 
		buttonAndIcon;
	private JLabel 
		countLabel = new JLabel();
	private static boolean
		isAlphaNumeric = false;
	private static FileSelection
		videoChannelsUpdateXml = new FileSelection("./Properties/data/ChannelsUpdater.xml");
	
	private HttpRequestProcessor 
		hrp;
	
	private static int 
		LIST_VIEW_WAIT = 2000;
	private ArrayList <String> 
		stripFilter = new ArrayList<String>(); 
	private boolean
		loadingOpen = false;
	private MouseDragScrollListener 
		mdsl = new MouseDragScrollListener();
	private String
		bookmarksPath,
		title;
	
	public VideoChannelsPlayerTab()
	{
		LaunchUrlActionListener.addArrayActionListener(this);
	}
	
	public void setTitle(String title)
	{
		this.title = title;
	}
	
	public String getTitle()
	{
		return this.title;
	}
	public static void setDefaultMinuteSetting(int minute)
	{
		DEFAULT_MINUTE_SETTING = minute;
	}
	
	public static void setAllChannelsDefaultDaysBefore(int daysBefore)
	{
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DAY_OF_YEAR, -daysBefore);
		AFTER_DATE_DEFAULT = new Timestamp(cal.getTimeInMillis());
	}
	
	public static void setChannelLimit(int limit)
	{
		CHANNEL_LIMIT_GLOBAL = limit;
		VideoChannelListView.setChannelLimitGlobal(limit);
	}
	
	public static void setVideoChannelsUpdaterXml(FileSelection fs)
	{
		videoChannelsUpdateXml = fs;
	}
	public static void setIsDragScroll(boolean isScroll)
	{
		IS_DRAG_SCROLL = isScroll;
	}
	public static void setAlphaNumericOrder(boolean isAlphaNumeric)
	{
		VideoChannelsPlayerTab.isAlphaNumeric = isAlphaNumeric;
	}
	
	public void build(LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon, 
			Container parentContainer, VideoChannelsPlayer vcp)
	{
		Runnable r = new Runnable()
		{
			@Override
			public void run() {
				VideoChannelsPlayerTab.this.parentButtons = new LinkedHashMap<Integer, JButtonLengthLimited>();
				VideoChannelsPlayerTab.this.parentContainer = parentContainer;
				VideoChannelsPlayerTab.this.ycvs = new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
				VideoChannelsPlayerTab.this.buttonAndIcon = buttonAndIcon;
				buildLoadingFrame(buttonAndIcon, vcp);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void buildLoadingFrame(LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon, VideoChannelsPlayer vcp) 
	{
		JFrame loadingFrame = new JFrame();
		loadingFrame.setResizable(false);
		
		if(parentContainer != null)
		{
			GraphicsUtil.rightEdgeTopWindow(parentContainer, loadingFrame);
		}
		else
		{
			loadingFrame.setLocation(LAUNCH_LOCATION);
		}
		
		loadingFrame.setMinimumSize(new Dimension(180,70));//TODO
		LoadingLabel label = new LoadingLabel();
		loadingFrame.add(label);
		
		loadingFrame.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				if(!loadingOpen)
				{
					vcp.removeVideoChannelsPlayerTab(VideoChannelsPlayerTab.this);
					return;
				}
			}
		});
		
		ColorTemplate.setBackgroundColorPanel(loadingFrame, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(loadingFrame, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(loadingFrame, ColorTemplate.getButtonForegroundColor());
		
		loadingFrame.setVisible(true);
		
		int count = 0;
		for(JButtonLengthLimited jbll : buttonAndIcon.keySet())
		{
			label.updateCount(count, buttonAndIcon.keySet().size());
			HashMap<Integer, ArrayList<YoutubeChannelVideo>> vids = LookupOrCreateYoutube.lookup(
					jbll.getText(), jbll.getName(), VideoChannelListView.getChannelLimitGlobal());
			
			if(vids != null && !vids.isEmpty())
			{
				int key = vids.keySet().iterator().next();
				VideoChannelsPlayerTab.this.ycvs.put(key, vids.get(key));
				VideoChannelsPlayerTab.this.parentButtons.put(key, jbll);
			}
			count++;
		}
		loadingOpen = true;
		loadingFrame.dispose();
		
		buildWidgets();
	}
	
	public VideoChannelListView getVideoChannelListView()
	{
		return this.listView;
	}
	
	public void buildWidgets()
	{
		this.setLayout(new BorderLayout());
		JPanel searchPanel = buildNorthPanel();
		
		addListView();
		buildWestPanel();
		JPanel southPanel = buildSouthPanel(allSelectBtn);
		
		this.add(channelScroll, BorderLayout.WEST);
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		
		refreshListView(null);
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
		
		RegisterArrayActionListener.addListener(this);
		
		urlSelect(LaunchUrlActionListener.getLastButtonOrigin());
		refreshListView(highlightButton);
		
		allChannelsButton.setSelected(true);
		refreshListViewAllSelection();	
	}
	
	public void buildWestPanel()
	{
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BorderLayout());
		
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0,1));
		channelScroll = new JScrollPane(outerPanel);
		channelScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		allSelectBtn = buildAllSelectionButton();
		listPanel.add(allSelectBtn);
		ArrayList<AbstractButton> abs = new ArrayList<AbstractButton>();
		for(int i : parentButtons.keySet())
		{
			parentButtonAndYoutubeVideos.put(parentButtons.get(i), ycvs.get(i));
			AbstractButton ab = buildSelectionButton(parentButtons.get(i));
			selectionButtonAndParentButton.put(ab, parentButtons.get(i));
			
			if(IS_DRAG_SCROLL)
			{
				ab.addMouseListener(mdsl);
				ab.addMouseMotionListener(mdsl);
			}
			
			ab.setIcon(buttonAndIcon.get(parentButtons.get(i)));
			ab.setHorizontalAlignment(AbstractButton.LEFT);
			abs.add(ab);
		}
		if(VideoChannelsPlayerTab.isAlphaNumeric)
		{
			Comparator<AbstractButton> buttonTextComparator = Comparator.comparing(
					AbstractButton::getText
			);
			abs.sort(buttonTextComparator);
		}
		for(AbstractButton ab : abs)
		{
			listPanel.add(ab);
		}
		outerPanel.add(listPanel, BorderLayout.NORTH);
	}
	
	public JPanel buildNorthPanel()
	{
		JPanel searchPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		searchPanel.setLayout(fl);
		
		showAllButton.setToolTipText(SHOW_ALL_BUTTON_TOOLTIP_TEXT);
		showAllButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean isSelect = showAllButton.isSelected();
				if(isSelect)
				{
					getVideoChannelListView().setChannelLimit(-1);
				}
				else
				{
					getVideoChannelListView().setChannelLimit(VideoChannelListView.getChannelLimitGlobal());
				}
				
				if(!isSelect || (listView.getVisibleCount() != TOTAL_COUNT))
				{				
					refreshSelectionFromDB(selectedButtonParent, selectedButton);
				}
			}
		});
		
		updateButton.addActionListener(getUpdateChannelActionListener());
		updateButton.setVisible(false);
		
		updateViewer.addActionListener(getUpdateChannelsActionListener());
		
		SearchBar sb = new SearchBar();
		sb.setColumnCharacterLength(SEARCH_COLUMN_LENGTH);
		sb.addSearchSubscriber(new SearchSubscriber() {
			@Override
			public void notifySearchText(String searchPattern) {
				listView.setVisible(searchPattern);
				updateCount();
				VideoChannelsPlayerTab.this.validate();
			}
		});
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				listView.setVisible(hour, minute, m);
				updateCount();
				VideoChannelsPlayerTab.this.validate();
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		dl.setMinuteDefault(DEFAULT_MINUTE_SETTING);
		
		afterDateEditor = new TimestampEditor();
		afterDateEditor.setComponentValue(AFTER_DATE_DEFAULT);
		afterDateEditor.setVisible(false);
		
		applyButton = new JButton(TIMESTAMP_APPLY);
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshListViewAllSelection();
			}
		});
		applyButton.setVisible(false);
		
		searchPanel.add(imageLabel);
		searchPanel.add(updateButton);
		searchPanel.add(updateViewer);
		searchPanel.add(sb);
		searchPanel.add(dl);
		searchPanel.add(showAllButton);
		searchPanel.add(afterDateEditor);
		searchPanel.add(applyButton);
		
		setImageButton(null);
		
		return searchPanel;
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
	
	public JPanel buildSouthPanel(AbstractButton parentButton)
	{
		JPanel 
			southPane = new JPanel();
		int 
			count = 0;
		
		if(parentButton instanceof JButtonLengthLimited)
		{
			count = LookupOrCreateYoutube.lookupCount(
					parentButton.getText(), parentButton.getName());
		}
		else//all select
		{
			for(JButtonLengthLimited jbll : parentButtons.values())
			{
				count += LookupOrCreateYoutube.lookupCount(
						jbll.getText(), jbll.getName());
			}
		}
		
		southPane.setLayout(new BorderLayout());
		countLabel.setBorder(COUNT_BORDER);
		countLabel.setText(COUNT_PREFIX + count);
		southPane.add(countLabel, BorderLayout.EAST);
		
		return southPane;
	}
	
	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		if(highlightButton != null)
		{
			highlightButton.setBorder(defaultBorder);
		}
		if(newButton == null)
		{
			if(listView != null) listView.urlSelect(null);
			return;
		}
		
		AbstractButton altButton = null;
		if(newButton instanceof JButtonLengthLimited)
		{
			altButton = ((JButtonLengthLimited) newButton).getHighlightButton();
		}
		for(AbstractButton ab : selectionButtonAndParentButton.keySet())
		{
			if(ab.getName().equals(newButton.getName()) || 
					(altButton != null && altButton.getName().equals(ab.getName()))
			)
			{
				//highlight.
				highlightButton = ab;
				highlightButton.setBorder(Highlighter.getBorderHighlight());
				break;
			}
		}
		
		if(channelScroll != null)
		{
			if(listView == null)
			{
				try {
					Thread.sleep(LIST_VIEW_WAIT);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			if(listView != null)
			{
				listView.urlSelect(newButton);
			}
			channelScroll.repaint();
			channelScroll.validate();
		}
	}
	
	private void updateCount()
	{
		if(listView != null)
		{
			countLabel.setText(COUNT_PREFIX + listView.getVisibleCount()  + " / " + TOTAL_COUNT);
		}
		else
		{
			countLabel.setText("");
		}
	}
	
	public void removeListView()
	{
		if(listView != null)
		{
			contentScrollPane.remove(listView);
		}
		this.remove(contentScrollPane);
	}
	
	public void setImageButton(JButtonLengthLimited jbllParent)
	{
		for(MouseListener ml : imageLabel.getMouseListeners())
		{
			imageLabel.removeMouseListener(ml);
		}
		
		if(jbllParent == null)
		{
			imageLabel.setVisible(false);
			updateButton.setVisible(false);
			return;
		}
		imageLabel.setVisible(true);
		updateButton.setVisible(true);
		
		imageLabel.setIcon(buttonAndIcon.get(jbllParent));
		imageLabel.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", jbllParent.getText()));
		
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				switch(button)
				{
				case MouseEvent.BUTTON1:
					VideoSubSelectionLauncher.launchRequest(jbllParent, -1);
					urlSelect(jbllParent);//highlight manually
					LaunchUrlActionListener.notifyActionListeners(jbllParent);
					break;
				case MouseEvent.BUTTON2:
					VideoSubSelectionLauncher.launchRequest(jbllParent, 1);
					break;
				case MouseEvent.BUTTON3://ignore
					break;
				}
			}
		});
	}
	
	public AbstractButton buildAllSelectionButton()
	{
		allChannelsButton = new JButton();
		allChannelsButton.setText(ALL_SELECT_TEXT);
		
		allChannelsButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				refreshListViewAllSelection();
			}
		});
		
		return allChannelsButton;
	}
	private void refreshListViewAllSelection()
	{
		int count = 0;
		for(JButtonLengthLimited jbll : parentButtons.values())
		{
			count += LookupOrCreateYoutube.lookupCount(
					jbll.getText(), jbll.getName());
		}
		TOTAL_COUNT = count;
		VideoChannelListView.setChannelLimitGlobal(-1);
		showAllButton.setVisible(false);
		afterDateEditor.setVisible(true);
		applyButton.setVisible(true);
		if(listView != null) listView.removeAll();
		
		selectedButtonParent = null;
		selectedButton = allChannelsButton;
		
		Timestamp afterDate = (Timestamp) afterDateEditor.getComponentValueObj();
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> chnls = getAllChannels(parentButtons, afterDate);//TODO add date
		createListViewAll(parentButtons, chnls);
		addListView();
		setImageButton(null);
		refreshListView(selectedButton);
		
		updateCount();
	}
	
	private void resetChannelLimitGlobal()
	{
		VideoChannelListView.setChannelLimitGlobal(CHANNEL_LIMIT_GLOBAL);
		if(listView != null) listView.setChannelLimit(CHANNEL_LIMIT_GLOBAL);
	}
	
	public AbstractButton buildSelectionButton(JButtonLengthLimited parentButton)
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(parentButton.getCharacterLimit());
		jbll.setText(parentButton.getText());
		jbll.setName(parentButton.getName());
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = LookupOrCreateYoutube.lookupCount(parentButton.getText(), parentButton.getName());
				TOTAL_COUNT = count;
				resetChannelLimitGlobal();
				afterDateEditor.setVisible(false);
				applyButton.setVisible(false);
				if(!showAllButton.isVisible()) showAllButton.setVisible(true);
				updateCount();
				
				if(jbll != selectedButton)
				{
					showAllButton.setSelected(false);
				}
				
				selectedButton = jbll;
				selectedButtonParent = parentButton;
				
				ArrayList<YoutubeChannelVideo> ycv = parentButtonAndYoutubeVideos.get(parentButton);
				
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
		ColorTemplate.setBackgroundColorPanel(VideoChannelsPlayerTab.this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(VideoChannelsPlayerTab.this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(VideoChannelsPlayerTab.this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), contentScrollPane);
		
		if(selectedButton != null)
		{
			selectedButton.setBackground(ColorTemplate.getButtonForegroundColor());
			selectedButton.setForeground(ColorTemplate.getButtonBackgroundColor());
		}
		if(listView != null)
		{
			listView.postFrameBuild();
		}
		VideoChannelsPlayerTab.this.validate();
	}
	
	private void createListViewAll(HashMap<Integer, JButtonLengthLimited> buttonParents, 
			Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		removeListView();
		listView = new VideoChannelListView(buttonParents, ycvs, ProcessType.child);
		if(hrp != null)
		{
			hrp.setArrayActionListener(listView, 1);//TODO. 2nd index.
		}
		addListView();
		updateCount();
	}
	
	private void createListView(JButtonLengthLimited buttonParent, ArrayList <YoutubeChannelVideo> ycv)
	{
		removeListView();
		listView = new VideoChannelListView(buttonParent, ycv, ProcessType.child);
		if(hrp != null)
		{
			hrp.setArrayActionListener(listView, 1);//TODO. 2nd index.
		}
		TOTAL_COUNT = LookupOrCreateYoutube.lookupCount(buttonParent.getText(), buttonParent.getName());
		addListView();
		updateCount();
	}
	
	private ActionListener getUpdateChannelActionListener() 
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedButton == null)
					return;
				
				updateSelection(selectedButtonParent, selectedButton);
			}
		};
	}
	
	public void setBookmarksPath(String path)
	{
		bookmarksPath = path;
	}
	
	private ActionListener getUpdateChannelsActionListener()
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				Point 
					scrnPoint = updateViewer.getLocationOnScreen();
				String 
					absPath = bookmarksPath;
				FileSelection 
					fs = new FileSelection("./Application Builder.jar");
				String 
					stripFilterStr = "";
				
				for(int i = 0; i < stripFilter.size(); i++)
				{
					String s = stripFilter.get(i);
					stripFilterStr += (i < stripFilter.size()+1)
							? s + OpenVideoChannelsUpdater.NAME_DELIMITER
							: s;
				}
				String [] args = new String [] {
					fs.getFullPath(),
					"ApplicationBuilder.ApplicationBuilder",
					videoChannelsUpdateXml.getRelativePath(),
					absPath, 
					isAlphaNumeric+"", 
					scrnPoint.x + "," + scrnPoint.y, 
					stripFilterStr,
					MouseDragScrollListener.getMouseDragDelay()+"",
					MouseDragScrollListener.getMouseWheelSpin()+"",
					MouseDragScrollListener.getUnitIncrementAdjustment()+"",
					QueryUpdateTool.ENDPOINT,
					QueryUpdateTool.PORT_NUMBER + ""
				};
				
				LoggingMessages.printOut(args);
				
				CommandBuild cb = new CommandBuild();
				cb.setCommand("java", new String [] {"-cp"}, args);
				try {
					CommandExecutor.executeProcess(cb);
				} catch (IOException ioe) {
					ioe.printStackTrace();
				}
			}
		};
	}
	
	private void refreshSelection(JButtonLengthLimited buttonParent, AbstractButton selectedButton)
	{
		ArrayList<YoutubeChannelVideo> ycv = parentButtonAndYoutubeVideos.get(buttonParent);
		createListView(buttonParent, ycv);
		refreshListView(selectedButton);
		setImageButton(buttonParent);
	}
	
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> getAllChannels(
			LinkedHashMap<Integer, JButtonLengthLimited> selectedButtonParents, Date afterDate)
	{
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		for(Integer key : selectedButtonParents.keySet())
		{
			JButtonLengthLimited sbp = selectedButtonParents.get(key);
			ycvs.putAll(
				LookupOrCreateYoutube.lookup(
					sbp.getText(), 
					sbp.getName(), 
					afterDate
				)
			);
		}
		return ycvs;
	}
	
	private void refreshSelectionFromDB(JButtonLengthLimited selectedButtonParent, AbstractButton selectedButton)
	{
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = LookupOrCreateYoutube.lookup(
				selectedButtonParent.getText(), selectedButtonParent.getName(), 
				(getVideoChannelListView()==null)
				? VideoChannelListView.getChannelLimitGlobal() 
				: getVideoChannelListView().getChannelLimit()
		);
		int key = ycvs.keySet().iterator().next();
		parentButtonAndYoutubeVideos.put(selectedButtonParent, ycvs.get(key));
		createListView(selectedButtonParent, ycvs.get(key));
		setImageButton(selectedButtonParent);//TODO
		refreshListView(selectedButton);
	}
	
	private void updateSelection(JButtonLengthLimited selectedButtonParent, AbstractButton selectedButton)
	{
		lastDate = VideoChannel.getLastDate(selectedButtonParent);
		if(lastDate == null)
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -6);
			lastDate = cal.getTime();
		}
		VideoUpdateTimespanDialog vutd = new VideoUpdateTimespanDialog(
				this, JButtonArray.getMoviesIcon(), selectedButtonParent, lastDate
		);
		vutd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(vutd.updated())
				{
					refreshSelectionFromDB(selectedButtonParent, selectedButton);
				}
			}
		});
	}
	
	public void filterText(JButtonLengthLimited jbl)
	{
		String txt = jbl.getFullLengthText();
		for(String s : stripFilter)
		{
			txt = txt.replace(s, "");
		}
		jbl.setText(txt);
	}
	
	@Override
	public String getDefaultImagePath() 
	{
		return defaultFileImage.getFullPath();
	}

	@Override
	public Dimension getScaledDefaultPic() 
	{
		return DEFAULT_SCALED_PIC_SIZE;
	}

	@Override
	public Dimension getDefaultPicSize() 
	{
		return DEFAULT_PIC_SIZE;
	}

	@Override
	public int getScaledWidth() 
	{
		return SCALED_WIDTH;
	}

	@Override
	public void setDefaultImageXmlPath(FileSelection fs) 
	{
		defaultFileImage = fs;
	}

	@Override
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension) 
	{
		DEFAULT_SCALED_PIC_SIZE = scaledDefaultPicDimension;
	}

	@Override
	public void setDefaultPicSize(Dimension defaultPicDimension) 
	{
		DEFAULT_PIC_SIZE = defaultPicDimension;
	}

	@Override
	public void setScaledWidth(int scaledWidth) 
	{
		SCALED_WIDTH = scaledWidth;
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
	public void addStripFilter(String filter) 
	{
		stripFilter.add(filter);
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}

}
