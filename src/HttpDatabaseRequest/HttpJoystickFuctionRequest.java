package HttpDatabaseRequest;

import java.awt.AWTException;
import java.awt.Frame;
import java.awt.Point;
import java.awt.Robot;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
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
import Params.KeepSelection;
import Properties.LoggingMessages;
import WidgetComponentDialogs.ShiftDialog;
import WidgetComponents.JButtonArray;
import WidgetComponents.JButtonLengthLimited;
import WidgetComponents.KeepSelectionSelector;
import WidgetComponents.TitleScroller;
import WidgetComponents.VideoChannelListView;
import WidgetComponents.VideoChannelPlayerJoy;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class HttpJoystickFuctionRequest implements ArrayActionListener
{
	private static int
		SHIFT_AMOUNT = 30;
	private static Point
		MAIN_WINDOW_PLACE_HOME = new Point(SHIFT_AMOUNT, SHIFT_AMOUNT);
	
	private static JButtonArray 
		ba;
	private static KeepSelectionSelector
		kss;
	private static VideoChannelPlayerJoy
		vcp;
	private static HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
	private static CommandBuild
		rightStickCommand,
		leftStickCommand,
		rightTriggerCommand,
		leftTriggerCommand;
	private static long
		leftTriggerTimer = 0,
		rightTriggerTimer = 0;
	private static int
		leftTriggerWaitMillis = 30,
		rightTriggerWaitMillis = 30;
	
	static {
		new HttpJoystickFuctionRequest();
	}
	
	private HttpJoystickFuctionRequest()
	{
		LaunchUrlActionListener.addArrayActionListener(this);
	}
	
	public static void setShiftAmount(int shift)
	{
		SHIFT_AMOUNT = shift;
	}
	public static void setMainWindowPlaceHome(Point loc)
	{
		MAIN_WINDOW_PLACE_HOME = loc;
	}
	public static void setLeftStickCommand(CommandBuild cb)
	{
		leftStickCommand = cb;
	}
	public static void setRightStickCommand(CommandBuild cb)
	{
		rightStickCommand = cb;
	}
	public static void setLeftTriggerCommand(CommandBuild cb)
	{
		leftTriggerCommand = cb;
	}
	public static void setRightTriggerCommand(CommandBuild cb)
	{
		rightTriggerCommand = cb;
	}
	public static void setLeftTriggerCommandWaitMillis(int millis)
	{
		leftTriggerWaitMillis = millis;
	}
	public static void setRightTriggerCommandWaitMillis(int millis)
	{
		rightTriggerWaitMillis = millis;
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
		HttpJoystickFuctionRequest.ycvs = LookupOrCreateYoutube.lookup(jbll.getText(), jbll.getName(), VideoChannelListView.getChannelLimitGlobal());
		return HttpJoystickFuctionRequest.ycvs;
	}
	
	private static void shiftMainWindow(int xPosShift, int yPosShift)
	{
		JFrame frame = WidgetBuildController.getInstance().getFrame();
		if(frame.getExtendedState() == Frame.NORMAL)
		{
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
		else
		{
			frame.setLocation(MAIN_WINDOW_PLACE_HOME);
		}
	}
	private static void keyPress(int key)
	{
		Robot r;
		try {
			r = new Robot();
			r.keyPress(key);
			r.keyRelease(key);
		} catch (AWTException e) {
			e.printStackTrace();
		}
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
					if( (vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen() )
					{
						kss.advanceIndex();
						selectCurrent();
					}
					else
					{
						keyPress(KeyEvent.VK_PAGE_DOWN);
					}
				}
				else 
				{
					if( (vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen() )
					{
						kss.decrementIndex();
						selectCurrent();
					}
					else
					{
						keyPress(KeyEvent.VK_PAGE_UP);
					}
				}
				
			}
			else if(responseXml.startsWith("LEFTY"))
			{
				//select move up/down
				if(positive)
				{
					keyPress(KeyEvent.VK_UP);
				}
				else
				{
					keyPress(KeyEvent.VK_DOWN);
				}
			}
			else if(responseXml.startsWith("DPAD_LEFT"))
			{
				if( (vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen() )
				{
					kss.decrementIndex();
					selectCurrent();
				}
				else if( vcp != null && vcp.isVisible() && !ba.isVideoBookmarksOpen() && ba.getKeepSelection().size() > 0)
				{
					vcp.dispose();
					vcp = null;
					
					kss.decrementIndex();
					selectCurrent();
					kss.setSelected(true);
					buildVideoChannelPlayer();
				}
			}
			else if(responseXml.startsWith("DPAD_RIGHT"))
			{
				if( (vcp == null || !vcp.isVisible()) && !ba.isVideoBookmarksOpen() )
				{
					kss.advanceIndex();
					selectCurrent();
				}
				else if( vcp != null && vcp.isVisible() && !ba.isVideoBookmarksOpen() )
				{
					vcp.dispose();
					vcp = null;
					
					kss.advanceIndex();
					selectCurrent();
					kss.setSelected(true);
					buildVideoChannelPlayer();
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
		
		//always.
		if(responseXml.equals("START"))
		{
			ba.toggleFocusButtonArray();
			
		}
		else if(responseXml.startsWith("DPAD_UP"))
		{
			keyPress(KeyEvent.VK_UP);
		}
		else if(responseXml.startsWith("DPAD_DOWN"))
		{
			keyPress(KeyEvent.VK_DOWN);
		}
		else if(responseXml.startsWith("DPAD_LEFT"))
		{
			if( (vcp == null || !vcp.isVisible()) && ba.isVideoBookmarksOpen() )
			{
				keyPress(KeyEvent.VK_PAGE_UP);
			}
		}
		else if(responseXml.startsWith("DPAD_RIGHT"))
		{
			if( (vcp == null || !vcp.isVisible()) && ba.isVideoBookmarksOpen())
			{
				keyPress(KeyEvent.VK_PAGE_DOWN);
			}
		}
		
		else if(responseXml.equals("BACK"))
		{
			AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName(
					LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT).getInstance(); //TODO.
			ab.doClick();
		}
		else if(responseXml.equals("A"))
		{
			if(!ba.isVideoBookmarksOpen() && kss.isSelected() && (vcp == null || !vcp.isVisible()))
			{
				buildVideoChannelPlayer();
			}
			else
			{
				keyPress(KeyEvent.VK_ENTER);
			}
		}
		else if(responseXml.equals("B"))
		{
			keyPress(KeyEvent.VK_ESCAPE);
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
		else if(responseXml.startsWith("TRIGGERLEFT"))
		{
			if(leftTriggerCommand != null)
			{
				LoggingMessages.printOut("process trigger left");
				long timeMillis = Calendar.getInstance().getTimeInMillis();
				
				if(Math.abs(timeMillis - leftTriggerTimer) > leftTriggerWaitMillis)
				{
					try {
						CommandExecutor.executeProcess(leftTriggerCommand);
						leftTriggerTimer = 0;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(leftTriggerTimer == 0)
				{
					leftTriggerTimer = timeMillis;
				}
			}
		}
		else if(responseXml.startsWith("TRIGGERRIGHT"))
		{
			if(rightTriggerCommand != null)
			{
				LoggingMessages.printOut("process trigger right");
				long timeMillis = Calendar.getInstance().getTimeInMillis();
				
				if(Math.abs(timeMillis - rightTriggerTimer) > rightTriggerWaitMillis)
				{
					try {
						CommandExecutor.executeProcess(rightTriggerCommand);
						rightTriggerTimer = 0;
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
				if(rightTriggerTimer == 0)
				{
					rightTriggerTimer = timeMillis;
				}
			}
		}
		
		//TODO. place in config.
		else if(responseXml.equals("RIGHTSTICK"))
		{
			if(rightStickCommand != null)
			{
				Runnable r = new Runnable() 
				{
					@Override
					public void run() 
					{
						try {
							CommandExecutor.executeProcess(rightStickCommand);
						} catch (IOException e) {
							e.printStackTrace();
						}
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		}
		else if(responseXml.equals("LEFTSTICK"))
		{
			if(leftStickCommand != null)
			{
				Runnable r = new Runnable() 
				{
					@Override
					public void run() 
					{
						try {
							CommandExecutor.executeProcess(leftStickCommand);
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
