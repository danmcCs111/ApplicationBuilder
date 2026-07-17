package HttpDatabaseRequest;

import java.awt.Frame;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Actions.CommandExecutor;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.PicLabelMouseListener;
import MouseListenersImpl.YoutubeChannelVideo;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.FileSelection;
import Params.KeepSelection;
import Properties.PathUtility;
import WidgetComponentDialogs.ShiftDialog;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.KeepSelectionSelector;
import WidgetComponents.TitleScroller;
import WidgetComponents.VideoChannelPlayerJoy;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class HttpJoystickFuctionRequest implements ArrayActionListener
{
	private static final int
		SHIFT_AMOUNT = 30;
	
	private static JButtonArray 
		ba;
	private static KeepSelectionSelector
		kss;
	private static VideoChannelPlayerJoy
		vcp;
	private static HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
	
	static {
		new HttpJoystickFuctionRequest();
	}
	
	private HttpJoystickFuctionRequest()
	{
		LaunchUrlActionListener.addArrayActionListener(this);
	}
	
	public static void selectCurrent()
	{
		KeepSelection ks = kss.getSelectedKeep();
		JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
		PicLabelMouseListener.selectionLabel(jbll, true);//TODO
		
		for(WidgetCreatorProperty wcp : WidgetBuildController.getInstance().getWidgetCreatorProperties())
		{
			Object o = wcp.getInstance();
			if(o instanceof TitleScroller)
			{
				((TitleScroller) o).joySelect(jbll);
			}
		}
	}
	
	public static void setButtonArray(JButtonArray ba)
	{
		HttpJoystickFuctionRequest.ba = ba;
		kss = new KeepSelectionSelector(ba);
		vcp = new VideoChannelPlayerJoy(ba, kss);
	}

	public static void buildVideoChannelPlayer() 
	{
		if(!kss.isSelected())
			return;
		
		kss.setSelected(false);
		if(vcp == null)
		{
			vcp = new VideoChannelPlayerJoy(ba, kss);
		}
		
		KeepSelection ks = kss.getSelectedKeep();
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = HttpJoystickFuctionRequest.getYoutubeVideos();
		vcp.setVideos(new ImageIcon(ks.getImg()), ks.getJButtonLengthLimited(), ycvs);
		
		if(!vcp.isVisible())
		{
			ks.getJButtonLengthLimited().doClick();
		}
		
	}

	public static HashMap<Integer, ArrayList<YoutubeChannelVideo>> getYoutubeVideos() 
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) kss.getSelectedKeep().getJButtonLengthLimited();
		HttpJoystickFuctionRequest.ycvs = LookupOrCreateYoutube.lookup(jbll.getText(), jbll.getName());
		return HttpJoystickFuctionRequest.ycvs;
	}
	
	private static void shiftMainWindow(int xPosShift, int yPosShift)
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		Point loc = frame.getLocationOnScreen();
		if(xPosShift != 0)
		{
			loc.x += xPosShift;
		}
		if(yPosShift != 0)
		{
			loc.y += yPosShift;
		}
		frame.setLocation(loc);
	}
	
	public static void process(String responseXml)
	{
		System.out.println(responseXml);
		//TODO. map input to actions.
		if(ba.getKeepSelection().size() > 0)
		{
			boolean positive = responseXml.endsWith("true");
			
			if(responseXml.equals("LEFTBUMPER"))
			{
				AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName("restore-win").getInstance();
				ab.doClick();
			}
			else if(responseXml.equals("RIGHTBUMPER"))
			{
				AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName("minimize-win").getInstance();
				ab.doClick();
			}
			
			else if(responseXml.startsWith("RIGHTX"))
			{
				//shift
				int count = 0;
				for(KeepSelection ks : ba.getKeepSelection())
				{
					if(ks.getFrame().getExtendedState() == Frame.NORMAL)
					{
						ShiftDialog.updateKeep(ks, true, false, positive?SHIFT_AMOUNT:-SHIFT_AMOUNT);
						count++;
					}
				}
				if(count == 0)
				{
					//shift main window.
					shiftMainWindow(positive?SHIFT_AMOUNT:-SHIFT_AMOUNT, 0);
				}
			}
			else if(responseXml.startsWith("RIGHTY"))
			{
				//shift
				int count = 0;
				for(KeepSelection ks : ba.getKeepSelection())
				{
					if(ks.getFrame().getExtendedState() == Frame.NORMAL)
					{
						ShiftDialog.updateKeep(ks, false, true, positive?-SHIFT_AMOUNT:SHIFT_AMOUNT);
						count++;
					}
				}
				if(count == 0)
				{
					//shift main window.
					shiftMainWindow(0, positive?-SHIFT_AMOUNT:SHIFT_AMOUNT);
				}
			}
			
			else if(responseXml.startsWith("LEFTX"))
			{
				//select move left/right
				if(positive)
				{
					if((vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen())
					{
						kss.advanceIndex();
						selectCurrent();
					}
				}
				else 
				{
					if((vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen())
					{
						kss.decrementIndex();
						selectCurrent();
					}
				}
				
			}
			else if(responseXml.startsWith("LEFTY"))
			{
				//select move up/down
			}
			
			else if(responseXml.startsWith("DPAD_LEFT"))
			{
				if((vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen())
				{
					kss.decrementIndex();
					selectCurrent();
				}
			}
			else if(responseXml.startsWith("DPAD_RIGHT"))
			{
				if((vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen())
				{
					kss.advanceIndex();
					selectCurrent();
				}
			}
			
		}//End open bookmarks req.
		else //still shift window if no bookmarks.
		{
			boolean positive = responseXml.endsWith("true");
			if(responseXml.startsWith("RIGHTX"))
			{
				//shift main window.
				shiftMainWindow(positive?SHIFT_AMOUNT:-SHIFT_AMOUNT, 0);
			}
			else if(responseXml.startsWith("RIGHTY"))
			{
				//shift main window.
				shiftMainWindow(0, positive?-SHIFT_AMOUNT:SHIFT_AMOUNT);
			}
		}
		
		if(responseXml.equals("START"))
		{
			ba.toggleFocusButtonArray();
			
		}
		else if(responseXml.equals("BACK"))
		{
			AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName(
					LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT).getInstance(); //TODO.
			ab.doClick();
		}
		
		else if(responseXml.equals("A"))
		{
			if(!ba.isVideoBookmarksOpen() && kss.isSelected())
			{
				buildVideoChannelPlayer();
			}
			
		}
		else if(responseXml.equals("B"))
		{
		}
		else if(responseXml.equals("X"))
		{
			if(vcp != null && vcp.isVisible())
			{
				vcp.doHomeButtonClick();
			}
			else
			{
				ba.closeAll();
			}
		}
		else if(responseXml.equals("Y"))
		{
			if(vcp != null && vcp.isVisible())
			{
				vcp.doUpdate();
			}
			else
			{
				ba.focusButtonArray();
				ba.performOpenAltFont();
			}
		}
		//TODO. place in config.
		else if(responseXml.equals("RIGHTSTICK"))
		{
			FileSelection fs = new FileSelection("./Application Builder.jar");
			Runnable r = new Runnable() 
			{
				String fullscreen = (PathUtility.isWindows())
						?"plugin-projects/AutoHotKey-Utils/install/v2/AutoHotkey64.exe  plugin-projects/AutoHotKey-Utils/send-pid-key-video-launcher.ahk  pid.txt  f"
						:"./plugin-projects/AutoHotKey-Utils/ahk_x11.AppImage  `pwd`/plugin-projects/AutoHotKey-Utils/send-chrome-key-fullscreen-linux.ahk";
				@Override
				public void run() 
				{
					CommandBuild cb = new CommandBuild();
					cb.setCommand("java", new String[] {"-cp"}, new String [] {
						fs.getFullPath(), 
						"ApplicationBuilder.ShellHeadlessExecutor", 
						fullscreen
					});
					try {
						CommandExecutor.executeProcess(cb);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			Thread t = new Thread(r);
			t.start();
		}
		else if(responseXml.equals("LEFTSTICK"))
		{
			FileSelection fs = new FileSelection("./Application Builder.jar");
			Runnable r = new Runnable() 
			{
				String play = (PathUtility.isWindows())
						?"plugin-projects/AutoHotKey-Utils/install/v2/AutoHotkey64.exe  plugin-projects/AutoHotKey-Utils/send-pid-key-video-launcher.ahk  pid.txt  {space}"
						:"./plugin-projects/AutoHotKey-Utils/ahk_x11.AppImage  `pwd`/plugin-projects/AutoHotKey-Utils/send-chrome-key-play-linux.ahk";
				@Override
				public void run() 
				{
					CommandBuild cb = new CommandBuild();
					cb.setCommand("java", new String[] {"-cp"}, new String [] {
						fs.getFullPath(), 
						"ApplicationBuilder.ShellHeadlessExecutor", 
						play
					});
					try {
						CommandExecutor.executeProcess(cb);
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			Thread t = new Thread(r);
			t.start();
		}
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
	}

	@Override
	public void urlSelect(AbstractButton newButton) {
		kss.setSelected(true);
	}

	@Override
	public void addArrayActionListener() {
		LaunchUrlActionListener.addArrayActionListener(this);
	}

	@Override
	public void removeArrayActionListener() {
		LaunchUrlActionListener.removeArrayActionListener(this);
	}

	@Override
	public void addStripFilter(String filter) {
		// TODO Auto-generated method stub
	}
}
