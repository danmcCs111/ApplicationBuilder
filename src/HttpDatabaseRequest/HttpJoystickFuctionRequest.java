package HttpDatabaseRequest;

import java.awt.Frame;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;

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
import WidgetComponents.VideoChannelPlayerJoy;
import WidgetUtility.WidgetBuildController;

public class HttpJoystickFuctionRequest 
{
	private static final int
		SHIFT_AMOUNT = 30;
	
	private static LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	private static JButtonArray 
		ba;
	private static KeepSelectionSelector
		kss;
	private static VideoChannelPlayerJoy
		vcp;
	private static HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
		
	private HttpJoystickFuctionRequest()
	{
		
	}
	
	private static void selectCurrent()
	{
		KeepSelection ks = kss.getSelectedKeep();
		JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
		PicLabelMouseListener.selectionLabel(jbll, true);//TODO
	}
	
	public static void setButtonArray(JButtonArray ba)
	{
		HttpJoystickFuctionRequest.ba = ba;
		vcp = new VideoChannelPlayerJoy(ba);
		kss = new KeepSelectionSelector(ba);
	}

	public static void update() 
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) kss.getSelectedKeep().getJButtonLengthLimited();
		lcv.update(jbll.getText(), jbll.getName());
		HttpJoystickFuctionRequest.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
	}

	public static void buildVideoChannelPlayer() 
	{
		if(vcp == null || !vcp.isVisible())
		{
			KeepSelection ks = kss.getSelectedKeep();
			vcp.setVideos(new ImageIcon(ks.getImg()), ks.getJButtonLengthLimited());
			
			if(!vcp.isVisible())
			{
				ks.getJButtonLengthLimited().doClick();
			}
		}
	}

	public static HashMap<Integer, ArrayList<YoutubeChannelVideo>> getYoutubeVideos() 
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) kss.getSelectedKeep().getJButtonLengthLimited();
		HttpJoystickFuctionRequest.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
		return HttpJoystickFuctionRequest.ycvs;
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
				for(KeepSelection ks : ba.getKeepSelection())
				{
					if(ks.getFrame().getExtendedState() == Frame.NORMAL)
					{
						ShiftDialog.updateKeep(ks, true, false, positive?SHIFT_AMOUNT:-SHIFT_AMOUNT);
					}
				}
			}
			else if(responseXml.startsWith("RIGHTY"))
			{
				//shift
				for(KeepSelection ks : ba.getKeepSelection())
				{
					if(ks.getFrame().getExtendedState() == Frame.NORMAL)
					{
						ShiftDialog.updateKeep(ks, false, true, positive?-SHIFT_AMOUNT:SHIFT_AMOUNT);
					}
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
						KeepSelection ks = kss.getSelectedKeep();
						JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
						PicLabelMouseListener.selectionLabel(jbll, true);//TODO
					}
				}
				else 
				{
					if((vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen())
					{
						kss.decrementIndex();
						KeepSelection ks = kss.getSelectedKeep();
						JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
						PicLabelMouseListener.selectionLabel(jbll, true);//TODO
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
			if(!ba.isVideoBookmarksOpen())
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
				ba.performOpen();
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
}
