package HttpDatabaseRequest;

import java.awt.Color;
import java.awt.Frame;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JLabel;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import ActionListenersImpl.LaunchUrlActionListener;
import Actions.CommandExecutor;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.PicLabelMouseListener;
import MouseListenersImpl.YoutubeChannelVideo;
import MouseListenersImpl.YoutubeVideosContainer;
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

public class HttpRequestHandler implements HttpHandler, YoutubeVideosContainer
{
	private static final String 
		REQUEST_TYPE_HEADER_KEY = "Get-request-type",
		FUNCTION_TYPE = "Joystick_Button";
	private static final int
		SHIFT_AMOUNT = 30;
	private static Color 
		NO_HIGHLIGHT = null,
		SELECTION_HIGHLIGHT = new Color(255, 0, 0, 204);
	private static LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	
	private KeepSelectionSelector
		kss;
	private VideoChannelPlayerJoy
		vcp;
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs;
	private JButtonArray 
		ba;
	
	public HttpRequestHandler(JButtonArray ba)
	{
		this.ba = ba;
		kss = new KeepSelectionSelector(ba);
	}
	
	@Override
	public void handle(HttpExchange exchange) throws IOException 
	{
		Headers h = exchange.getRequestHeaders();
		InputStream is = exchange.getRequestBody();
		String result = readFromInputStreamToString(is);
		
		String response = "This is the response " + "\n";
		response += getRequestHeaderAsString(h);

		String responseXml = execute(h, result);
		
		exchange.sendResponseHeaders(200, response.length() + responseXml.length());
		OutputStream os = exchange.getResponseBody();
		os.write(response.getBytes());
		os.write(responseXml.getBytes());
		os.close();
	}
	
	private static String readFromInputStreamToString(InputStream is)
	{
		InputStreamReader isr = new InputStreamReader(is);
		BufferedReader br = new BufferedReader(isr);
		String result = br.lines().collect(Collectors.joining("\n"));
		try {
			br.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return result;
	}
	
	private String execute(Headers h, String result)
	{
		String responseXml = result;
		if(h.containsKey(REQUEST_TYPE_HEADER_KEY))
		{
			if(h.get(REQUEST_TYPE_HEADER_KEY).contains(FUNCTION_TYPE))
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
							if(vcp == null || !vcp.isVisible())
							{
								kss.getSelectedKeep().getFrame().setForeground(NO_HIGHLIGHT);
								kss.advanceIndex();
								KeepSelection ks = kss.getSelectedKeep();
								JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
								PicLabelMouseListener.selectionLabel(jbll, true);//TODO
							}
						}
						else 
						{
							if(vcp == null || !vcp.isVisible())
							{
								kss.getSelectedKeep().getFrame().setForeground(NO_HIGHLIGHT);
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
						if(vcp == null || !vcp.isVisible())
						{
							kss.getSelectedKeep().getFrame().setForeground(NO_HIGHLIGHT);
							kss.decrementIndex();
							KeepSelection ks = kss.getSelectedKeep();
							JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
							PicLabelMouseListener.selectionLabel(jbll, true);//TODO
						}
					}
					else if(responseXml.startsWith("DPAD_RIGHT"))
					{
						if(vcp == null || !vcp.isVisible())
						{
							kss.getSelectedKeep().getFrame().setForeground(NO_HIGHLIGHT);
							kss.advanceIndex();
							KeepSelection ks = kss.getSelectedKeep();
							JButtonLengthLimited jbll = ks.getJButtonLengthLimited();
							PicLabelMouseListener.selectionLabel(jbll, true);//TODO
						}
					}
					
				}//End open bookmarks req.
				if(responseXml.equals("START"))
				{
					ba.toggleFocusButtonArray();
					
					kss.getSelectedKeep().getFrame().setForeground(NO_HIGHLIGHT);
					kss.advanceIndex();
					KeepSelection ks = kss.getSelectedKeep();
					ks.getFrame().getComponent(0).setForeground(SELECTION_HIGHLIGHT);
					((JLabel) ks.getFrame().getComponent(0)).setText(ks.getDisplayText());
					
				}
				else if(responseXml.equals("BACK"))
				{
					AbstractButton ab = (AbstractButton) WidgetBuildController.getInstance().findRefByName(
							LaunchUrlActionListener.CLOSE_LAUNCH_ACTION_EVENT).getInstance(); //TODO.
					ab.doClick();
				}
				
				else if(responseXml.equals("A"))
				{
					//TODO launch video channels.
					buildVideoChannelPlayer();
					
				}
				else if(responseXml.equals("B"))
				{
				}
				else if(responseXml.equals("X"))
				{
					ba.closeAll();
				}
				else if(responseXml.equals("Y"))
				{
					ba.focusButtonArray();
					ba.performOpen();
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
		
		return responseXml;
	}
	
	private long getProcessID()
	{
		
		return ProcessHandle.current().pid();
	}
	
	private static String getRequestHeaderAsString(Headers h)
	{
		String retResponse = "";
		for(String key : h.keySet())
		{
			retResponse += "[KEY] " + key + " ---> ";
			List<String> headers = h.get(key);
			for(String s : headers)
				retResponse += "[VALUE] " + s + "\n";
		}
		return retResponse;
	}

	@Override
	public void update() 
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) kss.getSelectedKeep().getJButtonLengthLimited();
		lcv.update(jbll.getText(), jbll.getName());
		this.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
	}

	@Override
	public void buildVideoChannelPlayer() 
	{
		if(vcp == null || !vcp.isVisible())
		{
			KeepSelection ks = kss.getSelectedKeep();
			vcp = new VideoChannelPlayerJoy(new ImageIcon(ks.getImg()), this, ks.getJButtonLengthLimited(), ba);
			if(!vcp.isVisible())
			{
				ks.getJButtonLengthLimited().doClick();
			}
		}
	}

	@Override
	public HashMap<Integer, ArrayList<YoutubeChannelVideo>> getYoutubeVideos() 
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) kss.getSelectedKeep().getJButtonLengthLimited();
		this.ycvs = lcv.lookup(jbll.getText(), jbll.getName());
		return this.ycvs;
	}
	

}

