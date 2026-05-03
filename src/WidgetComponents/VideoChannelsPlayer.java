package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
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
import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import ApplicationBuilder.QueryUpdateTool;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.HttpRequestHandler;
import HttpDatabaseRequest.HttpRequestProcessor;
import MouseListenersImpl.FrameMouseDragListener;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.VideoSubSelectionLauncher;
import MouseListenersImpl.YoutubeChannelVideo;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import WidgetComponentDialogs.VideoBookMarksDialog;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.OpenAndSaveKeepsSubscriber;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponentInterfaces.RegisterArrayActionListener;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelsPlayer extends JFrame implements ArrayActionListener, DefaultAndScaledImage, PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;

	private static Dimension 
		DEFAULT_PIC_SIZE = new Dimension(279, 150),
		DEFAULT_SCALED_PIC_SIZE = new Dimension(279, 150),
		MIN_SIZE = new Dimension(1050, 450);
	private static Point
		LAUNCH_LOCATION = new Point(600, 50);
	private static String
		TITLE = "All Open Video Channels",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		COUNT_PREFIX = "Video Count: ",
		UPDATE_BUTTON_TEXT = "Update",
		ALL_SELECT_TEXT = "All Channels";
	private static int 
		PORT_NUMBER_MASK = 5,
		CHARACTER_LIMIT = 35,
		SCALED_WIDTH = 50,
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25;
	private static Border
		COUNT_BORDER = new EmptyBorder(5, 0, 5, 15);//EmptyBorder(top, left, bottom, right)
	private static FileSelection
		defaultFileImage = new FileSelection("./Properties/shapes/Default-Play-Image.xml");
	
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
	private AbstractButton
		highlightButton;
	private Border
		defaultBorder = new JButton().getBorder();
	private AbstractButton 
		allSelectBtn;
	
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs; 
	private HashMap<Integer, JButtonLengthLimited> 
		parentButtons;
	private HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>
		parentButtonAndYoutubeVideos = new HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>();
	private HashMap<AbstractButton, JButtonLengthLimited>
		selectionButtonAndParentButton = new HashMap<AbstractButton, JButtonLengthLimited>();
	private LinkedHashMap<JButtonLengthLimited, ImageIcon> 
		buttonAndIcon;
	private JLabel 
		countLabel = new JLabel();
	
	private LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	private OpenVideoChannelsUpdater
		ovcu;
	private HttpRequestProcessor 
		hrp;
	
	private ArrayList <String> 
		stripFilter = new ArrayList<String>(); 
	
	public VideoChannelsPlayer()
	{
		
	}
	
	public static void setLaunchLocation(Point p)
	{
		LAUNCH_LOCATION = p;
	}
	
	public static void setHighlightColor(Color c)
	{
		Highlighter.setBorderColor(c);
	}
	
	public void build(LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon, 
			Container parentContainer)
	{
		this.parentButtons = new HashMap<Integer, JButtonLengthLimited>();
		this.parentContainer = parentContainer;
		this.ycvs = new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		this.buttonAndIcon = buttonAndIcon;
		
		Runnable r = new Runnable()
		{
			@Override
			public void run() {
				buildLoadingFrame(buttonAndIcon);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void buildLoadingFrame(LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon) 
	{
		JFrame loadingFrame = new JFrame();
		loadingFrame.setResizable(false);
		loadingFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		loadingFrame.setVisible(true);
		
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
		label.updateCount(SCROLL_UNIT_INC, DEFAULT_MINUTE_SETTING);
		loadingFrame.add(label);
		
		ColorTemplate.setBackgroundColorPanel(loadingFrame, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(loadingFrame, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(loadingFrame, ColorTemplate.getButtonForegroundColor());
		
		int count = 0;
		for(JButtonLengthLimited jbll : buttonAndIcon.keySet())
		{
			label.updateCount(count, buttonAndIcon.keySet().size());
			HashMap<Integer, ArrayList<YoutubeChannelVideo>> vids = lcv.lookup(
					jbll.getText(), jbll.getName());
			
			if(vids != null && !vids.isEmpty())
			{
				int key = vids.keySet().iterator().next();
				this.ycvs.put(key, vids.get(key));
				this.parentButtons.put(key, jbll);
			}
			count++;
		}
		loadingFrame.dispose();
		
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
		JPanel searchPanel = buildNorthPanel();
		addListView();
		buildEastPanel();
		JPanel southPanel = buildSouthPanel(allSelectBtn);
		
		this.setLayout(new BorderLayout());
		this.add(channelScroll, BorderLayout.WEST);
		this.add(searchPanel, BorderLayout.NORTH);
		this.add(southPanel, BorderLayout.SOUTH);
		
		refreshListView(null);
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
		
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setIconImage(JButtonArray.getMoviesIcon());
		RegisterArrayActionListener.addListener(this);
		
		urlSelect(LaunchUrlActionListener.getLastButtonOrigin());
		
		if(parentContainer != null)
		{
			GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		}
		else
		{
			this.setLocation(LAUNCH_LOCATION);
		}
		this.setVisible(true);
	}
	
	public void buildEastPanel()
	{
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0,1));
		channelScroll = new JScrollPane(listPanel);
		channelScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		allSelectBtn = buildAllSelectionButton();
		listPanel.add(allSelectBtn);
		for(int i : parentButtons.keySet())
		{
			parentButtonAndYoutubeVideos.put(parentButtons.get(i), ycvs.get(i));
			AbstractButton ab = buildSelectionButton(parentButtons.get(i));
			selectionButtonAndParentButton.put(ab, parentButtons.get(i));
			ab.setIcon(buttonAndIcon.get(parentButtons.get(i)));
			ab.setHorizontalAlignment(AbstractButton.LEFT);
			listPanel.add(ab);
		}
	}
	
	public JPanel buildNorthPanel()
	{
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
				VideoChannelsPlayer.this.validate();
			}
		});
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				listView.setVisible(hour, minute, m);
				VideoChannelsPlayer.this.validate();
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		dl.setMinuteDefault(DEFAULT_MINUTE_SETTING);
		
		searchPanel.add(imageLabel);
		searchPanel.add(updateButton);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
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
			count = FrameMouseDragListener.getLookupOrCreate().lookupCount(
					parentButton.getText(), parentButton.getName());
		}
		else//all select
		{
			for(JButtonLengthLimited jbll : parentButtons.values())
			{
				count += FrameMouseDragListener.getLookupOrCreate().lookupCount(
						jbll.getText(), jbll.getName());
			}
		}
		
		southPane.setLayout(new BorderLayout());
		countLabel.setBorder(COUNT_BORDER);
		countLabel.setText(COUNT_PREFIX + count);
		southPane.add(countLabel, BorderLayout.EAST);
		
		return southPane;
	}
	
	public void refreshSouthPanel(int count)
	{
		countLabel.setText(COUNT_PREFIX + count);
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
		for(ActionListener al : updateButton.getActionListeners())
		{
			updateButton.removeActionListener(al);
		}
		
		if(jbllParent == null)
		{
			imageLabel.setVisible(false);
			updateButton.addActionListener(getUpdateChannelsActionListener());
			return;
		}
		imageLabel.setVisible(true);
		updateButton.addActionListener(getUpdateChannelActionListener());
		
		imageLabel.setIcon(buttonAndIcon.get(jbllParent));
		imageLabel.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", jbllParent.getText()));
		
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				switch(button)
				{
				case MouseEvent.BUTTON1:
					jbllParent.doClick();
					break;
				case MouseEvent.BUTTON2:
					for(MouseListener ml : jbllParent.getMouseListeners())
					{
						e.setSource(jbllParent);
						ml.mouseClicked(e);
					}
					break;
				case MouseEvent.BUTTON3://ignore
					break;
				}
			}
		});
	}
	
	public AbstractButton buildAllSelectionButton()
	{
		JButton btn = new JButton();
		btn.setText(ALL_SELECT_TEXT);
		
		btn.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = 0;
				for(JButtonLengthLimited jbll : parentButtons.values())
				{
					count += FrameMouseDragListener.getLookupOrCreate().lookupCount(
							jbll.getText(), jbll.getName());
				}
				refreshSouthPanel(count);
				
				removeListView();
				listView.removeAll();
				listView = new VideoChannelListView(parentButtons, ycvs);
				if(hrp != null)
				{
					hrp.setArrayActionListener(listView, 1);//TODO. 2nd index.
					//update.
				}
				addListView();
				setImageButton(null);
				refreshListView(btn);
			}
		});
		
		return btn;
	}
	
	public AbstractButton buildSelectionButton(JButtonLengthLimited parentButton)
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(parentButton.getCharacterLimit());
		jbll.setText(parentButton.getText());
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int count = lcv.lookupCount(parentButton.getText(), parentButton.getName());
				
				if(!jbll.equals(selectedButton))//changed. update count.
				{
					refreshSouthPanel(count);
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
		ColorTemplate.setBackgroundColorPanel(VideoChannelsPlayer.this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(VideoChannelsPlayer.this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(VideoChannelsPlayer.this, ColorTemplate.getButtonForegroundColor());
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
		VideoChannelsPlayer.this.validate();
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
						GraphicsUtil.centerWindow(VideoChannelsPlayer.this, ovcu);
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
		removeListView();
		listView = new VideoChannelListView(buttonParent, ycv);
		if(hrp != null)
		{
			hrp.setArrayActionListener(listView, 1);//TODO. 2nd index.
		}
		addListView();
		refreshListView(selectedButton);
		setImageButton(buttonParent);
	}
	
	private void refreshSelectionFromDB(JButtonLengthLimited selectedButtonParent, JButtonLengthLimited selectedButton)
	{
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = lcv.lookup(
				selectedButtonParent.getText(), selectedButtonParent.getName());
		int key = ycvs.keySet().iterator().next();
		parentButtonAndYoutubeVideos.put(selectedButtonParent, ycvs.get(key));
		removeListView();
		listView = new VideoChannelListView(selectedButton, ycvs);
		if(hrp != null)
		{
			hrp.setArrayActionListener(listView, 1);//TODO. 2nd index.
		}
		addListView();
		setImageButton(selectedButtonParent);//TODO
		refreshListView(selectedButton);
	}
	
	private void updateSelection(JButtonLengthLimited selectedButtonParent, JButtonLengthLimited selectedButton)
	{
		lcv.update(selectedButtonParent.getText(), selectedButtonParent.getName());
		refreshSelectionFromDB(selectedButtonParent, selectedButton);
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
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
	public void addStripFilter(String filter) 
	{
		stripFilter.add(filter);
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
	
	public void open()
	{
		setupListener();
		OpenAndSaveKeepsSubscriber osks = new OpenAndSaveKeepsSubscriber() 
		{
			LinkedHashMap<JButtonLengthLimited, ImageIcon> jbllAndIcon = new LinkedHashMap<JButtonLengthLimited, ImageIcon>();
			ImageReader ir = new ImageReader(VideoChannelsPlayer.this);
			@Override
			public void saveKeeps(File filename, String[][] props) 
			{
				// TODO Auto-generated method stub
			}
			
			@Override
			public void openKeeps(HashMap<String, String> props) 
			{
				if(props == null)
					return;
				
				for(String s : props.keySet())
				{
					LoggingMessages.printOut("props: " + s.split("@")[0]+".url" + " " + props.get(s));
					String path = props.get(s);
					JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
							path, s.split("@")[0]+".url", JButtonLengthLimited.class);
					
					FileSelection fs = new FileSelection(path + "/images/" + s.split("@")[0] + ".png");
					jbll.setCharacterLimit(CHARACTER_LIMIT);
					VideoChannelsPlayer.this.filterText(jbll);
					jbllAndIcon.put(jbll, ir.getImageIcon(new File(fs.getFullPath())));
				}
				
				VideoChannelsPlayer.this.build(jbllAndIcon, null);
			}
		};
		
		VideoBookMarksDialog vbmd = new VideoBookMarksDialog(new DirectorySelection("./Properties/VideoLaunchBookmarks/"), osks, null);
		vbmd.setLocation(LAUNCH_LOCATION);
	}
	
	private void provision(int rootPort, int listenPort)
	{
		HttpDatabaseRequest.executeGetRequest(
			QueryUpdateTool.ENDPOINT,
			rootPort,
			listenPort+"",
			HttpRequestHandler.REQUEST_TYPE_HEADER_KEY,
			HttpRequestHandler.FUNCTION_TYPE_LAUNCH_REFRESH_REQUEST
			);
	}
	
	private void setupListener()
	{
		hrp = new HttpRequestProcessor(ProcessType.child, new ArrayActionListener[] { this, listView});
		int rootPort = HttpRequestProcessor.getPortNumber();
		VideoSubSelectionLauncher.setPortNumber(rootPort);
		
		int listenPort = HttpRequestProcessor.getPortNumber()+PORT_NUMBER_MASK;
		HttpRequestProcessor.setPortNumber(listenPort);
		hrp.listenHttp();
		provision(rootPort, listenPort);
	}
	
	@Override
	public void postExecute() 
	{
		open();
	}
	
}
