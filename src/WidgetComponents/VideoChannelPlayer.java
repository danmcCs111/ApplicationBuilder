package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;

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
import ApplicationBuilder.ApplicationBuilder;
import ApplicationBuilder.QueryUpdateTool;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.HttpRequestHandler;
import HttpDatabaseRequest.HttpRequestProcessor;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.VideoSubSelectionLauncher;
import MouseListenersImpl.YoutubeChannelVideo;
import MouseListenersImpl.YoutubeVideosContainer;
import ObjectTypeConversion.FileSelection;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelPlayer extends JFrame implements DefaultAndScaledImage, YoutubeVideosContainer, ArrayActionListener, PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1L;
	
	private static String
		COUNT_PREFIX = "Video Count: ",
		UPDATE_BUTTON_TEXT = "Update",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		TITLE_PREFIX = "Channel | ";
	private static Dimension 
		MIN_SIZE = new Dimension(800, 435);
	private static int 	
		SCALED_HEIGHT = 25,
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25;
	public static int
		PORT_NUMBER_MASK = 6;
	private static Border
		COUNT_BORDER = new EmptyBorder(5, 0, 5, 15);//EmptyBorder(top, left, bottom, right)
	public static String
		DEFAULT_IMG = "./Properties/shapes/Default-Play-Image.xml";
	private static Dimension
		DIM_DEFAULT_PIC = new Dimension(279,150),
		SCALED_DEFAULT_PIC = new Dimension(279, 150);
	
	private int
		totalCount = 0;
	private JButtonLengthLimited 
		parentButton;
	private VideoChannelListView 
		listView; 
	private JScrollPane 
		scrollPane;
	private JLabel
		countLabel;
	private ImageIcon 
		videoImage;
	private YoutubeVideosContainer 
		fmdl;
	private HashMap<Integer, ArrayList<YoutubeChannelVideo>>
		ycvs;
	private HttpRequestProcessor 
		hrp;
	
	public VideoChannelPlayer()
	{
		
	}
	
	public VideoChannelPlayer(
			ImageIcon videoImage, YoutubeVideosContainer fmdl, JButtonLengthLimited parentButton, Container parent)
	{
		this.parentButton = parentButton;
		this.videoImage = videoImage;
		this.fmdl = fmdl;
		this.setTitle(TITLE_PREFIX + parentButton.getText());
		buildWidgets(fmdl.getYoutubeVideos());
		GraphicsUtil.rightEdgeCenterWindow(parent, this);
	}
	
	public void setVideos(JButtonLengthLimited jbll, String path, Point loc)
	{
		ImageReader ir = new ImageReader(VideoChannelPlayer.this);
		FileSelection fs = new FileSelection(path + "/" + jbll.getFullLengthText() + ".png");
		ImageIcon ii = ir.getImageIcon(new File(fs.getFullPath()));
		ii = ImageReader.getScaledImageIcon(ii.getImage(), SCALED_HEIGHT);
		VideoChannelPlayer.this.videoImage = ii;
		VideoChannelPlayer.this.parentButton = jbll;
		VideoChannelPlayer.this.fmdl =VideoChannelPlayer. this;
		VideoChannelPlayer.this.setTitle(TITLE_PREFIX + parentButton.getFullLengthText());
		VideoChannelPlayer.this.setLocation(loc);
		VideoChannelPlayer.this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		buildWidgets(fmdl.getYoutubeVideos());
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
		buildCenterPanel(ycvs);
		
		this.setIconImage(videoImage.getImage());
		this.setLayout(new BorderLayout());
		this.add(buildNorthPanel(), BorderLayout.NORTH);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(buildSouthPanel(parentButton), BorderLayout.SOUTH);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				// TODO Auto-generated method stub
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				listView.setFocus();
			}
		});
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);

		listView.postFrameBuild();
		
		this.validate();
	}
	
	private JPanel buildNorthPanel()
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
				updateCount();
				VideoChannelPlayer.this.validate();
			}
		});
		
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				listView.setVisible(hour, minute, m);
				updateCount();
				VideoChannelPlayer.this.validate();
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		dl.setMinuteDefault(DEFAULT_MINUTE_SETTING);
		
		searchPanel.add(imageLabel);
		searchPanel.add(updateButton);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
		return searchPanel;
	}
	
	private void buildCenterPanel(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		listView = new VideoChannelListView(parentButton, ycvs, ProcessType.parent);
		scrollPane = new JScrollPane(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
	}
	
	private JPanel buildSouthPanel(JButtonLengthLimited parentButton)
	{
		JPanel 
			southPane = new JPanel();
		
		countLabel = new JLabel();
		totalCount = LookupOrCreateYoutube.lookupCount(parentButton.getText(), parentButton.getName());
		
		southPane.setLayout(new BorderLayout());
		
		updateCount();
		countLabel.setBorder(COUNT_BORDER);
		southPane.add(countLabel, BorderLayout.EAST);
		
		return southPane;
	}
	
	private void updateCount()
	{
		countLabel.setText(COUNT_PREFIX + listView.getVisibleCount()  + " / " + totalCount);
	}
	
	public static void main(String [] args)
	{
	}

	@Override
	public String getDefaultImagePath() 
	{
		return DEFAULT_IMG;
	}

	@Override
	public Dimension getScaledDefaultPic() 
	{
		return SCALED_DEFAULT_PIC;
	}

	@Override
	public Dimension getDefaultPicSize() 
	{
		return DIM_DEFAULT_PIC;
	}

	@Override
	public int getScaledWidth() 
	{
		return SCALED_HEIGHT;
	}

	@Override
	public void setDefaultImageXmlPath(FileSelection fs) 
	{
		DEFAULT_IMG = fs.getRelativePath();
	}

	@Override
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension) 
	{
		SCALED_DEFAULT_PIC = scaledDefaultPicDimension;
	}

	@Override
	public void setDefaultPicSize(Dimension defaultPicDimension) 
	{
		DIM_DEFAULT_PIC = defaultPicDimension; 
	}

	@Override
	public void setScaledWidth(int scaledWidth) 
	{
		SCALED_HEIGHT = scaledWidth;
	}

	@Override
	public void update() 
	{
		LookupOrCreateYoutube.update(parentButton.getText(), parentButton.getName());
	}

	@Override
	public void buildVideoChannelPlayer() 
	{
		//refresh.
		listView.clearListViewPanel();
		ycvs = LookupOrCreateYoutube.lookup(parentButton.getText(), parentButton.getName());
		listView.buildListViewPanel(null, ycvs);
		updateCount();
	}

	@Override
	public HashMap<Integer, ArrayList<YoutubeChannelVideo>> getYoutubeVideos() 
	{
		if(ycvs == null)
		{
			ycvs = LookupOrCreateYoutube.lookup(parentButton.getText(), parentButton.getName());	
		}
		return ycvs;
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		listView.urlSelect(newButton);
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
		parseArgs(ApplicationBuilder.argsApp);
		setupListener();
	}
	
	private void parseArgs(String [] args)
	{
		String
			path = args[1],
			buttonText = args[2],
			fullText = args[3],
			url = args[4],
			portNumber = args[5];
		
		PORT_NUMBER_MASK = Integer.parseInt(portNumber);
		String [] pointStr = args[6].split(",");
		int
			x = Integer.parseInt(pointStr[0].strip()),
			y = Integer.parseInt(pointStr[1].strip());
		Point 
			loc = new Point(x, y);
		
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setText(buttonText);
		jbll.setFullText(fullText);
		jbll.setName(url);
		
		this.setVideos(jbll, path, loc);
	}

	
}
