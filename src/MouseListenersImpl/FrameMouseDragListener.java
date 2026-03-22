package MouseListenersImpl;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ActionListenersImpl.NavigationButtonActionListener;
import Graphics2D.ColorTemplate;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.HighlightListener;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.JMenuItemLaunchUrl;
import WidgetComponents.SwappableCollection;
import WidgetComponents.VideoChannelPlayer;
import WidgetComponentsTips4Java.MenuScroller;

public class FrameMouseDragListener extends MouseAdapter implements MouseListener, MouseMotionListener, HighlightListener, DefaultAndScaledImage
{
	private static final int 
		FRAME_AND_TITLE_HEIGHT = 45; 
	private static final String 
		OPEN_MENU_TEXT = "OPEN",
		VIEW_LATEST_VIDEOS = "VIEW",
		VIEW_LIST_VIDEOS = "VIEW LIST",
		UPDATE_BACKFILL = "BACKFILL UPDATE",
		UPDATE_BACKFILL_TOOLTIP = "Bulk update to backfill from a selected begin date.",
		UPDATE_VIDEOS = "UPDATE",
		UPDATE_VIDEOS_TOOLTIP = "Update after last timestamp stored.";
	
	public static int
		SCALED_WIDTH_ICON = 35;
	
	private JFrame 
		f;
	private AbstractButton 
		component;
	private JLabel 
		picLabel;
	private LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	private VideoChannelPlayer 
		vqp = null;
	private Point 
		mouseDownCompCoords = null;
	private boolean 
		mouse1Pressed = false;
	private VideoUpdateTimespanDialog 
		vutd = null;
	private ImageIcon
		icon = null;
	private DefaultAndScaledImage 
		parentScaler;
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
	
	public FrameMouseDragListener()
	{
		
	}
	
	public FrameMouseDragListener(JFrame f, DefaultAndScaledImage parentScaler, AbstractButton component, JLabel picLabel)
	{
		super();
		this.f = f;
		this.parentScaler = parentScaler;
		this.component = component;
		this.picLabel = picLabel;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) 
	{
		if(e.getButton() == MouseEvent.BUTTON3)//Offer option to keep
		{
			JButtonLengthLimited jbll = (JButtonLengthLimited) component;//TODO
			JPopupMenu pm = new JPopupMenu();
			JMenuItem mi = new JMenuItem(OPEN_MENU_TEXT);
			
			mi.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					for(ActionListener al : component.getActionListeners())
					{
						al.actionPerformed(new ActionEvent(component, 1, "Open From Image"));
						PicLabelMouseListener.highLightLabel(jbll, true);//TODO
					}
				}
			});
			pm.add(mi);
			
			LoggingMessages.printOut(jbll.getName());
			if(jbll.getName().contains("youtube.com"))
			{
				this.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
				JMenu mi2 = buildViewMenu();
				if(mi2 != null)
				{
					pm.add(mi2);
				}
				if(mi2 != null)
				{
					JMenuItem mi3 = buildOpenVideosView();
					pm.add(mi3);
				}
				JMenuItem mi4 = buildUpdateMenu();
				pm.add(mi4);
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
					JMenuItemLaunchUrl jmi = buildJMenuItem(ycv);
					mi2.add(jmi);
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
	
	private JMenuItemLaunchUrl buildJMenuItem(YoutubeChannelVideo ycv)
	{
		JMenuItemLaunchUrl jmi = new JMenuItemLaunchUrl(ycv.getTitle());
		jmi.setHighlightButton(component);
		jmi.setName(ycv.getUrl());
		jmi.setToolTipText("Upload Date: " + ycv.getUploadDate().toString());
		jmi.addActionListener(new VideoSubSelectionLauncher(component, jmi, this));
		return jmi;
	}
	
	private JMenu buildUpdateMenu()
	{
		JMenu miP = new JMenu(UPDATE_VIDEOS);
		
		JMenuItem mi1 = new JMenuItem(UPDATE_VIDEOS);
		mi1.setToolTipText(UPDATE_VIDEOS_TOOLTIP);
		mi1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				update();
				if(vqp != null && vqp.isVisible())
				{
					buildVideoChannelPlayer();
				}
			}
		});
		
		JMenuItem mi2 = new JMenuItem(UPDATE_BACKFILL);
		mi2.setToolTipText(UPDATE_BACKFILL_TOOLTIP);
		mi2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Calendar cal = Calendar.getInstance();
				cal.set(Calendar.MONTH, -6);
				if(vutd != null)
				{
					vutd.dispose();
				}
				vutd = new VideoUpdateTimespanDialog(f, component, lcv, cal.getTime());
			}
		});
		
		miP.add(mi1);
		miP.add(mi2);
		
		return miP;
	}
	
	private JMenuItem buildOpenVideosView()
	{
		JMenuItem mi4 = new JMenuItem(VIEW_LIST_VIDEOS);
		mi4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				buildVideoChannelPlayer();
			}
		});
		return mi4;
	}
	
	public void update()
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) component;
		lcv.update(jbll.getText(), jbll.getName());
		this.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
	}
	
	public void buildVideoChannelPlayer()
	{
		Point p = null;
		if(vqp != null)
		{
			p = vqp.getLocationOnScreen();
			vqp.dispose();
		}
		vqp = new VideoChannelPlayer(getImage((JButtonLengthLimited)component), this, component, f);
		if(p != null)
		{
			vqp.setLocation(p);
		}
	}
	
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
		if(vqp != null)
		{
			vqp.getVideoChannelListView().findHighlight();
		}
	}

	@Override
	public AbstractButton getMatchingButton(String name) 
	{
		if(vqp == null)
			return null;
		return vqp.getVideoChannelListView().getAbstractButton(name);
	}
	
	private ImageIcon getImage(JButtonLengthLimited jbll)
	{
		if(icon != null)
			return icon;
		
		int indexPos = NavigationButtonActionListener.getCurPosition();
		String path = SwappableCollection.indexPaths.get(indexPos);
		ImageReader buttonImageReader = new ImageReader(this, true);
		DirectorySelection ds = new DirectorySelection(path);
		File f = new File(ds.getFullPath() + "/images/" + jbll.getFullLengthText() + ".png");
		LoggingMessages.printOut(f.toString());
		icon = buttonImageReader.getImageIcon(f);
		
		return icon;
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
