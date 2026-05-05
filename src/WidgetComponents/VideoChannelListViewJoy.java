package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
import HttpDatabaseRequest.HttpRequestHandler.ProcessType;
import MouseListenersImpl.VideoSubSelectionLauncher;
import MouseListenersImpl.YoutubeChannelVideo;
import Properties.LoggingMessages;
import Properties.StringUtility;
import WidgetComponentInterfaces.RegisterArrayActionListener;
import WidgetComponents.DurationLimiter.Mode;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelListViewJoy extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		POPUP_OPEN = "OPEN",
		POPUP_OPEN_NEW = "OPEN NEW";
	private static final SimpleDateFormat 
		SDF_UPLOAD_SHORT = new SimpleDateFormat("MM/dd/yyyy");
	private static int
		MINIMUM_MINUTE = -1,
		VIDEO_TITLE_CHARACTER_LIMIT = 90;
	private static final Font 
		SELECT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	
	private HashMap<Integer, JButtonLengthLimited> 
		parentButtons;
	private static Color [] 
		foregroundAndBackgroundColor = new Color [] {
				ColorTemplate.getButtonForegroundColor(), ColorTemplate.getButtonBackgroundColor()},
		highlightForegroundAndBackgroundColor = new Color [] {
				Color.blue, Color.white};
	private static Color 
		borderColor = Color.ORANGE;
	JList<JButtonLengthLimited> 
		listItems;
	private Highlighter 
		hlPanel,
		selectedBtn;
	private LinkedHashMap<JButtonLengthLimited, Highlighter> 
		videoButtons = new LinkedHashMap<JButtonLengthLimited, Highlighter>();
	private HashMap<Integer, VideoSubSelectionLauncher> 
		vssl = null;
	
	private ArrayList<JButtonLengthLimited> 
		videoButtonsFiltered;
	
	
	public VideoChannelListViewJoy()
	{
		buildWidgets();
	}
	
	public void setVideos(JButtonLengthLimited parentButton, Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		int key = ycvs.keySet().iterator().next();
		this.parentButtons = new HashMap<Integer, JButtonLengthLimited>();
		this.parentButtons.put(key, parentButton);
		vssl = new HashMap<Integer, VideoSubSelectionLauncher>();
		vssl.put(key, new VideoSubSelectionLauncher(parentButton,  ProcessType.parent));
		
		videoButtonsFiltered = getFilteredJButtons(ycvs); 
		listItems.setListData(videoButtonsFiltered.toArray(new JButtonLengthLimited[] {}));
		listItems.ensureIndexIsVisible(0);
		listItems.setSelectedIndex(0);
		
		clearSelect();
		postFrameBuild();
	}
	
	public static void setMinimumMinute(int minMinute) 
	{
		MINIMUM_MINUTE = minMinute;
	}
	
	public static void setBorderColor(Color color)
	{
		borderColor = color;
		Highlighter.setBorderColor(color);
	}
	
	public static void setHighlightForegroundBackgroundColor(Color cForeground, Color cBackground)
	{
		highlightForegroundAndBackgroundColor = new Color [] {cForeground, cBackground};
	}
	
	public static void setForegroundBackgroundColor(Color cForeground, Color cBackground)
	{
		foregroundAndBackgroundColor = new Color [] {cForeground, cBackground};
	}
	
	public static Color [] getForegroundBackgroundColor()
	{
		return foregroundAndBackgroundColor;
	}
	
	public void setFocus()
	{
		listItems.requestFocus();
		if(listItems.getComponentCount() > 0)
		{
			listItems.getComponent(0).requestFocus();
		}
	}

	private void buildWidgets()
	{
		hlPanel = new Highlighter(this, borderColor);
		
		listItems = new JList<JButtonLengthLimited>();
		
		listItems.setFont(SELECT_FONT);
		listItems.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				if(e.getClickCount() == 2 && e.getButton() == MouseEvent.BUTTON1)
				{
					sendMouseClick();
				}
			}
		});
		listItems.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) 
			{
				LoggingMessages.printOut(e.getKeyCode() + "");
				if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
				{
					close();
				}
				else if(e.getKeyCode() == KeyEvent.VK_ENTER)
				{
					sendMouseClick();
				}
			}
		});
		
		this.setLayout(new BorderLayout());
		this.add(listItems, BorderLayout.NORTH);
		RegisterArrayActionListener.addListener(this);
	}
	
	private ArrayList<JButtonLengthLimited> getFilteredJButtons(Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		videoButtons.clear();
		ArrayList<JButtonLengthLimited> 
			videoButtonsFiltered = new ArrayList<JButtonLengthLimited>(),
			btns = new ArrayList<JButtonLengthLimited>();
		LinkedHashMap<JButtonLengthLimited, String> 
			btnAndDuration = new LinkedHashMap<JButtonLengthLimited, String>();
		
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				String 
					duration = formatDuration(ycv.getDuration()),
					uploadDate = SDF_UPLOAD_SHORT.format(ycv.getUploadDate());
				JButtonLengthLimited 
					jbll = buildVideoButton(key, ycv);
				
				jbll.setCharacterLimit(VIDEO_TITLE_CHARACTER_LIMIT);
				btnAndDuration.put(jbll, duration);
				jbll.setToStringPrefix(uploadDate + " | " + duration + " | ");
				
				btns.add(jbll);
			}
		}
		
		videoButtonsFiltered = (MINIMUM_MINUTE > 0)
				? getVisible(0, MINIMUM_MINUTE, Mode.GreaterThan, btnAndDuration)
				: btns;
		
		return videoButtonsFiltered;
	}
	
	public void close()
	{
		((JFrame)VideoChannelListViewJoy.this.getTopLevelAncestor()).dispose();
	}
	
	public void sendMouseClick()
	{
		MouseEvent me = new MouseEvent(listItems, -1, WHEN_FOCUSED, WHEN_FOCUSED, 0, 0, 0, 0, 1, false, 1);
		for(MouseListener ml : listItems.getSelectedValue().getMouseListeners())
		{
			ml.mouseClicked(me);
		}
		close();
	}
	
	public void postFrameBuild()
	{
		findHighlight(LaunchUrlActionListener.getLastButtonOrigin());
	}
	
	private JButtonLengthLimited buildVideoButton(int key, YoutubeChannelVideo ycv)
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
				"", ycv.getTitle(), ycv.getUrl(), JButtonLengthLimited.class);
		
		jbll.setHighlightButton(parentButtons.get(key));
		Highlighter hl = new Highlighter(jbll, highlightForegroundAndBackgroundColor, foregroundAndBackgroundColor);
		videoButtons.put(jbll, hl);
		List<AbstractButton> abs = Arrays.asList(videoButtons.keySet().toArray(new AbstractButton[] {}));
		int index = abs.indexOf(jbll);
		
		jbll.addMouseListener(new MouseAdapter() 
		{
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int btn = e.getButton();
				switch(btn)
				{
				case MouseEvent.BUTTON1:
					vssl.get(key).performLaunch(jbll);
					performSelect(hl, index);
					break;
				case MouseEvent.BUTTON2:
					vssl.get(key).performLaunch(jbll, 1);
					break;
				case MouseEvent.BUTTON3:
					JPopupMenu rClickMenu = new JPopupMenu();
					JMenuItem open = new JMenuItem(POPUP_OPEN);
					open.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							vssl.get(key).performLaunch(jbll);
							performSelect(hl, index);
						}
					});
					JMenuItem openNewTab = new JMenuItem(POPUP_OPEN_NEW);
					openNewTab.addActionListener(new ActionListener() {
						@Override
						public void actionPerformed(ActionEvent e) {
							vssl.get(key).performLaunch(jbll, 1);
						}
					});
					rClickMenu.add(open);
					rClickMenu.add(openNewTab);
					rClickMenu.show(jbll, e.getX(), e.getY());
					break;
				}
			}
		});
		return jbll;
	}
	
	public static String formatDuration(String duration)
	{
		if(duration == null || duration.equals("null"))
		{
			return "";//no value.
		}
		
		String durText = "";
		String [] hourMinSec = duration.split(",");
		for(int i = 0; i < hourMinSec.length; i++)
		{
			String val = StringUtility.padTimeValue2(hourMinSec[i]);
			durText += (i + 1 < hourMinSec.length)
				? val + ":"
				: val;
		}
		return durText;
	}
	
	public void findHighlight(AbstractButton newButton)
	{
		AbstractButton last = (newButton == null)
				? LaunchUrlActionListener.getLastButtonOrigin()
				: newButton;
		String name = (last == null)
				? null
				: last.getName();
		
		LoggingMessages.printOut("Find highlight: " + name);
		AbstractButton ab = getAbstractButton(name);
		
		if(ab == null)
		{
			return;
		}
		
		int index = videoButtonsFiltered.indexOf(ab);
		performSelect(videoButtons.get(ab), index);
		ab.requestFocusInWindow();
		
		this.validate();
	}
	
	public AbstractButton getAbstractButton(String name)
	{
		for(AbstractButton ab : videoButtons.keySet())
		{
			if(name != null && name.equals(ab.getName()))
			{
				return ab;
			}
		}
		return null;
	}
	
	public void performSelect(Highlighter hl, int index)
	{
		clearSelect();
		if(hl != null)
		{
			hlPanel.setHighlightForegroundAndBackground(true);
			hl.setHighlightForegroundAndBackground(true);
			selectedBtn = hl;
		}
		listItems.ensureIndexIsVisible(index);
		listItems.setSelectedIndex(index);
	}
	
	public ArrayList<JButtonLengthLimited> getVisible(
			int hour, int minute, Mode m, LinkedHashMap<JButtonLengthLimited, String> buttonAndDuration)
	{
		
		ArrayList<JButtonLengthLimited> jblls = new ArrayList<JButtonLengthLimited>();
		
		int totalValue = Integer.parseInt(StringUtility.padTimeValue2(hour) + StringUtility.padTimeValue2(minute));
		LoggingMessages.printOut(totalValue + "");
		
		for(JButtonLengthLimited btn : buttonAndDuration.keySet())
		{
			String text = buttonAndDuration.get(btn);
			int totalValueVideo = -1;
			if(!text.isBlank())
			{
				text = text.substring(0, text.length()-2);//remove seconds.
				text = text.replace(":", "");
				totalValueVideo = Integer.parseInt(text);
				LoggingMessages.printOut(totalValueVideo + "");
			}
			switch(m)
			{
			case GreaterThan:
				if(totalValueVideo >= totalValue)
				{
					jblls.add(btn);
				}
				break;
				
			case LessThan:
				if(totalValueVideo < totalValue)
				{
					jblls.add(btn);
				}
				break;
			}
		}
		return jblls;
	}
	
	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		
	}

	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		clearSelect();
		findHighlight(newButton);
	}
	
	private void clearSelect()
	{
		hlPanel.setHighlightForegroundAndBackground(false);
		if(selectedBtn != null)
		{
			selectedBtn.setHighlightForegroundAndBackground(false);
		}
	}

	@Override
	public void addStripFilter(String filter) 
	{
		
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
}
