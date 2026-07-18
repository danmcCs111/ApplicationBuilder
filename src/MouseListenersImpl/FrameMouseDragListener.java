package MouseListenersImpl;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import HttpDatabaseRequest.HttpRequestProcessor;
import ObjectTypeConversion.CommandBuild;
import Params.KeepSelection;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.HighlightListener;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.JMenuItemLaunchUrl;
import WidgetComponents.JMenuLaunchUrl;
import WidgetComponents.VideoChannelListView;
import WidgetComponents.VideoChannelPlayer;
import WidgetComponents.VideoChannelPlayerJoy;
import WidgetComponentsTips4Java.MenuScroller;

public class FrameMouseDragListener extends MouseAdapter implements MouseListener, MouseMotionListener, HighlightListener, YoutubeVideosContainer
{
	private static final int 
		FRAME_AND_TITLE_HEIGHT = 45; 
	private static final String 
		YOUTUBE_CHANNELS_FILTER = "youtube.com/",
		YOUTUBE_CHANNELS_NOT_FILTER = "youtube.com/watch?v=",
		EMPTY_CHANNELS_LIST_TEXT = "Empty",
		OPEN_MENU_TEXT = "OPEN",
		VIEW_LATEST_VIDEOS = "VIEW",
		VIEW_LIST_VIDEOS = "VIEW LIST",
		UPDATE_VIDEOS = "UPDATE",
		UPDATE_VIDEOS_TOOLTIP = "Update after last timestamp stored.";
	
	private static final SimpleDateFormat
		SDF_DATE_VIEW = new SimpleDateFormat("MM/dd/yy"),
		SDF_DATE_SHORT = new SimpleDateFormat("MMM-dd-yyyy");
	
	public static int
		SCALED_WIDTH_ICON = 35;
	
	public static HashMap<FrameMouseDragListener, Integer>
		portMaskAndDragListener = new HashMap<FrameMouseDragListener, Integer>();
	
	private JFrame 
		f;
	private KeepSelection
		ks;
	private JButtonLengthLimited 
		parentButton;
	private JLabel 
		picLabel;
	private Date
		lastDate;
	private static VideoChannelPlayerJoy
		vcpj = null;
	private VideoChannelPlayer
		vcp = null;
	private Point 
		mouseDownCompCoords = null;
	private boolean 
		mouse1Pressed = false;
	private VideoUpdateTimespanDialog 
		vutd = null;
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
	private static boolean
		isTouch = false,
		isPreview = false;
	private static Dimension
		scrollBarTouchDim = new Dimension(25, 25);
	
	public FrameMouseDragListener()
	{
		
	}
	
	public FrameMouseDragListener(JFrame f, KeepSelection ks, JButtonLengthLimited parentButton, JLabel picLabel)
	{
		super();
		this.f = f;
		this.ks = ks;
		this.parentButton = parentButton;
		this.picLabel = picLabel;
	}
	
	public static boolean videoChannelPlayerJoyOpen()
	{
		return vcpj != null && vcpj.isVisible();
	}
	
	public static void setScrollWidthHeight(Dimension dim)
	{
		scrollBarTouchDim = dim;
		UIManager.put("ScrollBar.width", scrollBarTouchDim.width);
		UIManager.put("ScrollBar.height", scrollBarTouchDim.height);
	}
	
	public static void setShowRightClickPreview(boolean preview)
	{
		isPreview = preview;
	}
	
	public static void setIsTouch(boolean isTouch)
	{
		FrameMouseDragListener.isTouch = isTouch;
	}
	
	public static boolean isTouch()
	{
		return FrameMouseDragListener.isTouch;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON3)//Offer option to keep
		{
			JButtonLengthLimited jbll = parentButton;//TODO
			JPopupMenu pm = new JPopupMenu();
			JMenuItem mi = new JMenuItem(OPEN_MENU_TEXT);
			if(isTouch)
			{
				mi.setFont(VideoChannelPlayerJoy.getFontAlt());
			}
			
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(ActionListener al : parentButton.getActionListeners())
					{
						al.actionPerformed(new ActionEvent(parentButton, 1, "Open From Image"));
						PicLabelMouseListener.highLightLabel(jbll, true);//TODO
					}
				}
			});
			pm.add(mi);
			
			LoggingMessages.printOut(jbll.getName());
			if(jbll.getName().contains(YOUTUBE_CHANNELS_FILTER) &&
					!jbll.getName().contains(YOUTUBE_CHANNELS_NOT_FILTER)) //youtube channel selection items.
			{
				if(isTouch)
				{
					JMenuItem mi3 = buildOpenVideosViewTouch(jbll);
					if(isTouch)
					{
						mi3.setFont(VideoChannelPlayerJoy.getFontAlt());
					}
					if(mi3 != null)
					{
						pm.add(mi3);
					}
					JMenuItem mi4 = buildUpdateMenu(VideoChannelPlayerJoy.getFontAlt());
					pm.add(mi4);
				}
				else
				{
					if(isPreview)
					{
						this.ycvs = LookupOrCreateYoutube.lookup(jbll.getText(), jbll.getName());
						JMenu mi2 = buildViewMenu();
						if(mi2 != null)
						{
							pm.add(mi2);
						}
					}
					JMenuItem mi3 = buildOpenVideosView(jbll);
					if(mi3 != null)
					{
						pm.add(mi3);
					}
					JMenuItem mi4 = buildUpdateMenu();
					pm.add(mi4);
				}
			}
			picLabel.add(pm);
			int x = e.getPoint().x, y = e.getPoint().y;
			
			ColorTemplate.setBackgroundColorButtons(pm, ColorTemplate.getButtonBackgroundColor());//TODO
			ColorTemplate.setForegroundColorButtons(pm, ColorTemplate.getButtonForegroundColor());
			
			pm.show(picLabel, x, y);
		}
	}

	private JMenu buildViewMenu()
	{
		JMenu mi2 = null;
		if(ycvs != null)//load in menu;
		{
			mi2 = new JMenu(VIEW_LATEST_VIDEOS);
			int count = 0; 
			for(int key : ycvs.keySet())
			{
				for(YoutubeChannelVideo ycv : ycvs.get(key))
				{
					LoggingMessages.printOut("video found! " + ycv.getTitle());
					JMenu jm = buildJMenuItem(ycv);
					mi2.add(jm);
					count++;
				}
			}
			LoggingMessages.printOut("scroller count: " + count);
			if(count == 0)
			{
				//None.
				return null;
			}
			else if(count < 8)
			{
				MenuScroller.setScrollerFor(mi2, count, 125, 0, 0);
			}
			else
			{
				MenuScroller.setScrollerFor(mi2, 8, 125, 0, 0);
			}
		}
		
		return mi2;
	}
	
	private JMenu buildJMenuItem(YoutubeChannelVideo ycv)
	{
		JMenuLaunchUrl jm = new JMenuLaunchUrl(ycv.getTitle());
		String tooltipText = "<html> Upload Date: " + SDF_DATE_SHORT.format(ycv.getUploadDate()) + 
				"<br/> Duration: " + VideoChannelListView.formatDuration(ycv.getDuration()) +
				"</html>";
		jm.setName(ycv.getUrl());
		jm.setHighlightButton(parentButton);
		jm.setToolTipText(tooltipText);
		
		JMenuItemLaunchUrl jmi = new JMenuItemLaunchUrl(OPEN_MENU_TEXT);
		jmi.setHighlightButton(parentButton);
		jmi.setReqText(jm.getText());
		jmi.setName(ycv.getUrl());
		jmi.addActionListener(new VideoSubSelectionLauncher(parentButton, this, ProcessType.parent));
		
		JMenuItemLaunchUrl jmi2 = new JMenuItemLaunchUrl(OPEN_MENU_TEXT + " NEW");
		jmi2.setName(ycv.getUrl());
		jmi2.addActionListener(new VideoSubSelectionLauncher(parentButton, this, ProcessType.parent, 1));
		
		jm.add(jmi);
		jm.add(jmi2);
		
		return jm;
	}
	
	private JMenuItem buildUpdateMenu()
	{
		return buildUpdateMenu(null);
	}
	
	private JMenuItem buildUpdateMenu(Font fnt)
	{
		JMenuItem mi2 = new JMenuItem(UPDATE_VIDEOS);
		mi2.setToolTipText(UPDATE_VIDEOS_TOOLTIP);
		mi2.addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				lastDate = VideoChannel.getLastDate(parentButton);
				if(lastDate == null)
				{
					Calendar cal = Calendar.getInstance();
					cal.add(Calendar.MONTH, -6);
					lastDate = cal.getTime();
				}
				if(vutd != null)
				{
					vutd.dispose();
				}
				Runnable r = new Runnable() {
					@Override
					public void run() {
						vutd = new VideoUpdateTimespanDialog(f, f.getIconImage(), parentButton, lastDate, fnt);
						vutd.addWindowListener(new WindowAdapter() {
							@Override
							public void windowClosed(WindowEvent e) {
								if(vcp != null && vcp.isVisible() && vutd.updated())
								{
									buildVideoChannelPlayer(true);
								}
							}
						});
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		});
		
		if(f != null) {
			mi2.setFont(fnt);
		}
		
		return mi2;
	}
	
	private JMenuItem buildOpenVideosView(JButtonLengthLimited jbll)
	{
		JMenuItem mi4 = getViewItemsJMenu(jbll);
		mi4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SwingUtilities.invokeLater(() -> {
					if(!isPreview)
					{
						FrameMouseDragListener.this.ycvs = LookupOrCreateYoutube.lookup(jbll.getText(), jbll.getName());
					}
					buildVideoChannelPlayer(false);
				});
			}
		});
		return mi4;
	}
	
	private JMenuItem buildOpenVideosViewTouch(JButtonLengthLimited jbll)
	{
		JMenuItem mi4 = getViewItemsJMenu(jbll);
		mi4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				SwingUtilities.invokeLater(() -> {
					FrameMouseDragListener.this.ycvs = LookupOrCreateYoutube.lookup(jbll.getText(), jbll.getName());
				    buildVideoChannelPlayer(false);
				});
			}
		});
		return mi4;
	}
	
	private JMenuItem getViewItemsJMenu(JButtonLengthLimited jbll)
	{
		Date 
			lastDate = VideoChannel.getLastDate(jbll),
			firstDate = VideoChannel.getFirstDate(jbll);
		String 
			range = ((lastDate == null) 
					? "" 
					: SDF_DATE_VIEW.format(lastDate)) 
					+ " - " + 
					((firstDate == null) 
					? "" 
					: SDF_DATE_VIEW.format(firstDate));
		
		if(range.strip().equals("-"))
		{
			range = EMPTY_CHANNELS_LIST_TEXT;
		}
		
		JMenuItem mi4 = new JMenuItem(VIEW_LIST_VIDEOS + " (" + range + ")");
		
		return mi4;
	}
	
	private CommandBuild getLaunchVideoChannelPlayer(JButtonLengthLimited jbll, String path, Point loc)
	{
		int port = HttpRequestProcessor.getPortNumber() + VideoChannelPlayer.PORT_NUMBER_MASK;
		
		CommandBuild cb = new CommandBuild();
		cb.setCommand("java", new String[]{"-cp"}, new String[]{
				"./Application Builder.jar", 
				"ApplicationBuilder.ApplicationBuilder", 
				"./Properties/data/VideoChannelPlayer.xml",
				path,
				jbll.getText(),
				jbll.getFullLengthText(),
				jbll.getName(),
				port + "",
				loc.x + "," + loc.y
		});
		
		LoggingMessages.printOut(LoggingMessages.combine(cb.getParameters()));
		
		return cb;
	}
	
	@Override
	public void update()
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) parentButton;
		lastDate = VideoChannel.getLastDate(parentButton);
		if(lastDate == null)
		{
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.MONTH, -6);
			lastDate = cal.getTime();
		}
		VideoUpdateTimespanDialog vutd = new VideoUpdateTimespanDialog(
				f, f.getIconImage(), jbll, lastDate
		);
		vutd.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(vutd.updated())
				{
					FrameMouseDragListener.this.ycvs = LookupOrCreateYoutube.lookup(jbll.getText(), jbll.getName());
					buildVideoChannelPlayer(true);
				}
			}
		});
	}
	
	@Override
	public void buildVideoChannelPlayer(boolean reuseLocation)
	{
		if(isTouch)
		{
			if(vcpj == null)
			{
				vcpj = new VideoChannelPlayerJoy(f, null);
			}
			vcpj.setVideos(new ImageIcon(ks.getImg()), parentButton, FrameMouseDragListener.this.ycvs);
		}
		else if(!isTouch)
		{
			if(vcp == null)
			{
				vcp = new VideoChannelPlayer(ks.getImageIconSmall(), this, parentButton, f);
			}
			else
			{
				if(!vcp.isVisible())
				{
					Point loc = vcp.getLocation();
					vcp.dispose();
					vcp = (reuseLocation) 
						? new VideoChannelPlayer(ks.getImageIconSmall(), this, parentButton, loc)
						: new VideoChannelPlayer(ks.getImageIconSmall(), this, parentButton, f);
				}
				else
				{
					vcp.rebuildVideoChannelPlayerList();
				}
			}
		}
	}
	
	@Override
	public HashMap <Integer, ArrayList <YoutubeChannelVideo>> getYoutubeVideos()
	{
		return this.ycvs;
	}
	
	@Override
	public void mouseReleased(MouseEvent e) 
	{
        mouseDownCompCoords = null;
    }

	@Override
    public void mousePressed(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON1)
		{
			mouseDownCompCoords = e.getPoint();
			mouse1Pressed = true;
		}
		else
		{
			mouse1Pressed = false;
		}
    }
        
	@Override
	public void mouseDragged(MouseEvent e) 
	{
		if(mouse1Pressed)
		{
	        Point currCoords = e.getLocationOnScreen();
	        currCoords.setLocation(currCoords.x, currCoords.y - FRAME_AND_TITLE_HEIGHT);
	        f.setLocation(currCoords.x - mouseDownCompCoords.x, currCoords.y - mouseDownCompCoords.y);
		}
	}
	
	@Override
	public void highlight()
	{
		if(vcp != null)
		{
			vcp.getVideoChannelListView().findHighlight(null);
		}
	}

	@Override
	public Component getMatchingComponent(String name) 
	{
		if(vcp == null)
			return null;
		return vcp.getVideoChannelListView().getAbstractButton(name);
	}
	
}
