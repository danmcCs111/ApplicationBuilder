package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JTabbedPane;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import ApplicationBuilder.QueryUpdateTool;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpDatabaseRequest;
import HttpDatabaseRequest.HttpRequestHandler;
import HttpDatabaseRequest.HttpRequestProcessor;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import MouseListenersImpl.MouseDragScrollListener;
import MouseListenersImpl.VideoSubSelectionLauncher;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentDialogs.VideoBookMarksDialog;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.OpenAndSaveKeepsSubscriber;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensionInterfaces.OpenActionExtension;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelsPlayer extends JFrame implements ArrayActionListener, PostWidgetBuildProcessing, OpenActionExtension
{
	private static final long serialVersionUID = 1L;
	
	private static Dimension 
		MIN_SIZE = new Dimension(1050, 450);
	private static Point
		LAUNCH_LOCATION = new Point(600, 50),
		ERROR_DIALOG_LOCATION = LAUNCH_LOCATION;
	private static DirectorySelection
		videoBookmarksDirectory = new DirectorySelection("./Properties/VideoLaunchBookmarks/");
	private static String
		FILE_MENU_TEXT = "File",
		OPEN_BUTTON_TEXT = "Open",
		ACTION_MENU_TEXT = "Action",
		CONNECT_BUTTON_TEXT = "Reconnect";
	private static int 
		PORT_NUMBER_MASK = 6,
		CHARACTER_LIMIT = 35,
		ROOT_PORT = HttpRequestProcessor.getPortNumber(),
		LISTEN_PORT = HttpRequestProcessor.getPortNumber()+PORT_NUMBER_MASK;
	private static Color
		FOREGROUND_MENUBAR = null,
		BACKGROUND_MENUBAR = null;
	
	private static boolean
		OVERRIDE_ERROR_DIALOG = false;
	private static Timestamp 
		AFTER_DATE_DEFAULT;
	static {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.WEEK_OF_MONTH, -1);
		AFTER_DATE_DEFAULT = new Timestamp(cal.getTimeInMillis());
	}
	
	private VideoChannelListView 
		listView; 
	
	private HttpRequestProcessor 
		hrp;
	
	private ArrayList <String> 
		stripFilter = new ArrayList<String>(); 
	private boolean
		frameBuilt = false,
		open = false;
	private VideoBookMarksDialog 
		vbmd;
	
	private Container 
		parentContainer;
	
	private ArrayList<VideoChannelsPlayerTab> 
		vcpts = new ArrayList<VideoChannelsPlayerTab>();
	
	private JTabbedPane 
		jtPane;
	public static int 
		bookMarksCounter = 0;
	
	public VideoChannelsPlayer()
	{
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}
	
	public static void setDefaultMinuteSetting(int minute)
	{
		VideoChannelsPlayerTab.setDefaultMinuteSetting(minute);
	}
	
	public static void setAllChannelsDefaultDaysBefore(int daysBefore)
	{
		VideoChannelsPlayerTab.setAllChannelsDefaultDaysBefore(daysBefore);
	}
	
	public static void setChannelLimit(int limit)
	{
		VideoChannelsPlayerTab.setChannelLimit(limit);
	}
	
	public static void setMenuBarForegroundAndBackground(Color foreground, Color background)
	{
		FOREGROUND_MENUBAR = foreground;
		BACKGROUND_MENUBAR = background;
	}
	public static void setVideoChannelsUpdaterXml(FileSelection fs)
	{
		VideoChannelsPlayerTab.setVideoChannelsUpdaterXml(fs);
	}
	public static void setIsDragScroll(boolean isScroll)
	{
		VideoChannelsPlayerTab.setIsDragScroll(isScroll);
	}
	public static void setMouseDragUnitIncrementAdjustment(int unitInc)
	{
		MouseDragScrollListener.setUnitIncrementAdjustment(unitInc);
	}
	public static void setMouseDragDelay(int delay)
	{
		MouseDragScrollListener.setMouseDragDelay(delay);
	}
	public static void setMouseWheelSpin(int spin)
	{
		MouseDragScrollListener.setMouseWheelSpin(spin);
	}
	public static void setParentRootPort(int rootPort)
	{
		ROOT_PORT = rootPort;
	}
	public static void setPortNumberMask(int portNumberMask)
	{
		PORT_NUMBER_MASK = portNumberMask;
		LISTEN_PORT = HttpRequestProcessor.getPortNumber()+PORT_NUMBER_MASK;
	}
	public static void setOverrideErrorDialog(boolean override)
	{
		OVERRIDE_ERROR_DIALOG = override;
	}
	public static void setAlphaNumericOrder(boolean isAlphaNumeric)
	{
		VideoChannelsPlayerTab.setAlphaNumericOrder(isAlphaNumeric);
	}
	public static void setDirectorySelection(DirectorySelection ds)
	{
		videoBookmarksDirectory = ds;
	}
	public static void setLaunchLocation(Point p)
	{
		LAUNCH_LOCATION = p;
		ERROR_DIALOG_LOCATION = LAUNCH_LOCATION;
	}
	public static void setHighlightColor(Color c)
	{
		Highlighter.setBorderColor(c);
	}
	
	private void buildFrame()
	{
		if(frameBuilt)
			return;
		
		JMenuBar jmb = buildMenuBar();
		
		this.setJMenuBar(jmb);
		this.setLayout(new BorderLayout());
		this.setMinimumSize(MIN_SIZE);
		this.setIconImage(JButtonArray.getMoviesIcon());
		
		if(parentContainer != null)
		{
			GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		}
		else
		{
			this.setLocation(LAUNCH_LOCATION);
		}
		this.setVisible(true);
		
		frameBuilt = true;
	}
	
	public JMenuBar buildMenuBar()
	{
		JMenuBar mb = new JMenuBar();
		JMenu 
			actionMenu = new JMenu(ACTION_MENU_TEXT),
			fileMenu = new JMenu(FILE_MENU_TEXT);
		JMenuItem 
			connect = new JMenuItem(CONNECT_BUTTON_TEXT),
			open = new JMenuItem(OPEN_BUTTON_TEXT);
		
		connect.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				provision(ROOT_PORT, LISTEN_PORT);
			}
		});
		open.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				open();
			}
		});
		
		actionMenu.add(connect);
		fileMenu.add(open);
		mb.add(fileMenu);
		mb.add(actionMenu);
		
		if(BACKGROUND_MENUBAR != null )
		{
			mb.setBackground(BACKGROUND_MENUBAR);
			fileMenu.setBackground(BACKGROUND_MENUBAR);
			open.setBackground(BACKGROUND_MENUBAR);
			actionMenu.setBackground(BACKGROUND_MENUBAR);
			connect.setBackground(BACKGROUND_MENUBAR);
		}
		if(FOREGROUND_MENUBAR != null)
		{
			mb.setForeground(FOREGROUND_MENUBAR);
			fileMenu.setForeground(FOREGROUND_MENUBAR);
			open.setForeground(FOREGROUND_MENUBAR);
			actionMenu.setForeground(FOREGROUND_MENUBAR);
			connect.setForeground(FOREGROUND_MENUBAR);
		}
		
		return mb;
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
	
	public void build(LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon, 
			Container parentContainer)
	{
		VideoChannelsPlayerTab vcpt = new VideoChannelsPlayerTab();
		vcpt.build(buttonAndIcon, null);
		VideoChannelsPlayer.this.add(vcpt, BorderLayout.CENTER);
	}
	
	public void open()
	{
		OpenAndSaveKeepsSubscriber osks = new OpenAndSaveKeepsSubscriber() 
		{
			ImageReader ir = new ImageReader(new VideoChannelsPlayerTab());
			
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
				
				//TODO. multiple tabs.
				LinkedHashMap<JButtonLengthLimited, ImageIcon> jbllAndIcon = new LinkedHashMap<JButtonLengthLimited, ImageIcon>();
				VideoChannelsPlayerTab vcpt = new VideoChannelsPlayerTab();
				for(String s : stripFilter)
					vcpt.addStripFilter(s);
				vcpts.add(vcpt);
				
				open = true;
				for(String s : props.keySet())
				{
					String channel = s.split("@")[0];
					
					LoggingMessages.printOut("props: " + channel+".url" + " " + props.get(s));
					String path = props.get(s);
					JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
							path, channel + ".url", JButtonLengthLimited.class);
					
					if(jbll == null)
						continue;
					
					FileSelection fs = new FileSelection(path + "/images/" + channel + ".png");
					jbll.setCharacterLimit(CHARACTER_LIMIT);
					vcpt.filterText(jbll);
					jbllAndIcon.put(jbll, ir.getImageIcon(new File(fs.getFullPath())));
				}
				buildFrame();
				vcpt.build(jbllAndIcon, null);
				String path = vbmd.getFileSelection().get(bookMarksCounter++).getAbsolutePath();
				vcpt.setTitle(PathUtility.removeProjectPath(path));
				vcpt.setBookmarksPath(path);
				if(jtPane == null)
				{
					jtPane = new JTabbedPane();
					jtPane.addTab(vcpt.getTitle(), vcpt);
					VideoChannelsPlayer.this.add(jtPane, BorderLayout.CENTER);
				}
				else
				{
					jtPane.addTab(vcpt.getTitle(), vcpt);
				}
				provision(ROOT_PORT, LISTEN_PORT);
			}
		};
		
		vbmd = new VideoBookMarksDialog(videoBookmarksDirectory, osks, null, false, false);
		vbmd.setLocation(LAUNCH_LOCATION);
		vbmd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!open)
				{
					System.exit(0);
				}
				else
				{
					bookMarksCounter = 0;
				}
			}
		});
	}
	
	private void setupListener()
	{
		hrp = new HttpRequestProcessor(ProcessType.child, new ArrayActionListener[] { this, listView});
		hrp.setDialogLocation(ERROR_DIALOG_LOCATION);
		hrp.setOverrideError(OVERRIDE_ERROR_DIALOG);
		
		VideoSubSelectionLauncher.setPortNumber(ROOT_PORT);
		HttpRequestProcessor.setPortNumber(LISTEN_PORT);
		hrp.listenHttp();
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		for(VideoChannelsPlayerTab vcpt : vcpts)
		{
			vcpt.urlSelect(newButton);
		}
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
	public void postExecute() 
	{
		setupListener();
		open();
		ColorTemplate.setBackgroundColorPanel(VideoChannelsPlayer.this, ColorTemplate.getPanelBackgroundColor());
		setVisible(true);
	}

	@Override
	public void performOpen() 
	{
		open();
	}

	@Override
	public void performOpenAltFont() {
		// TODO Auto-generated method stub
		
	}

}
