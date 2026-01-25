package MouseListenersImpl;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
import Properties.LoggingMessages;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.JMenuItemLaunchUrl;
import WidgetComponentsTips4Java.MenuScroller;

public class FrameMouseDragListener extends MouseAdapter implements MouseListener, MouseMotionListener
{
	private static final int 
		FRAME_AND_TITLE_HEIGHT = 45; 
	private static final String 
		OPEN_MENU_TEXT = "OPEN",
		VIEW_LATEST_VIDEOS = "VIEW",
		UPDATE_VIDEOS = "UPDATE";
	
	private Point mouseDownCompCoords = null;
	private JFrame f;
	private AbstractButton component;
	private JLabel picLabel;
	private LookupOrCreateYoutube lcv = new LookupOrCreateYoutube();
	private boolean mouse1Pressed = false;
	
	public FrameMouseDragListener()
	{
		
	}
	
	public FrameMouseDragListener(JFrame f, AbstractButton component, JLabel picLabel)
	{
		super();
		this.f = f;
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
				JMenu mi2 = buildViewMenu(jbll);
				JMenuItem mi3 = buildUpdateMenu(jbll);
				pm.add(mi2);
				pm.add(mi3);
			}
			picLabel.add(pm);
			int x = e.getPoint().x, y = e.getPoint().y;
			
			ColorTemplate.setBackgroundColorButtons(pm, ColorTemplate.getButtonBackgroundColor());//TODO
			ColorTemplate.setForegroundColorButtons(pm, ColorTemplate.getButtonForegroundColor());
			
			pm.show(picLabel, x, y);
		}
	}

	private JMenu buildViewMenu(JButtonLengthLimited jbll)
	{
		JMenu mi2 = new JMenu(VIEW_LATEST_VIDEOS);
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = lcv.lookup(jbll.getText(), jbll.getName());
		if(ycvs != null)//load in menu;
		{
			for(int key : ycvs.keySet())
			{
				for(YoutubeChannelVideo ycv : ycvs.get(key))
				{
					LoggingMessages.printOut("video found! " + ycv.getTitle());
					JMenuItemLaunchUrl jmi = new JMenuItemLaunchUrl(ycv.getTitle());
					jmi.setHighlightButton(jbll);
					jmi.setName(ycv.getUrl());
					jmi.setToolTipText("Upload Date: " + ycv.getUploadDate().toString());
					jmi.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							for(ActionListener al : component.getActionListeners())
							{
								if(al instanceof LaunchUrlActionListener)
								{
									al.actionPerformed(new ActionEvent(jmi, 1, "Open From Image"));
								}
								else
								{
									al.actionPerformed(new ActionEvent(jbll, 1, "Open From Image"));
									PicLabelMouseListener.highLightLabel(jbll, true);//TODO
								}
							}
						}
					});
					mi2.add(jmi);
				}
			}
			MenuScroller.setScrollerFor(mi2, 8, 125, 0, 0);
		}
		
		return mi2;
	}
	
	private JMenuItem buildUpdateMenu(JButtonLengthLimited jbll)
	{
		JMenuItem mi3 = new JMenuItem(UPDATE_VIDEOS);
		mi3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				lcv.update(jbll.getText(), jbll.getName());
			}
		});
		return mi3;
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

}
