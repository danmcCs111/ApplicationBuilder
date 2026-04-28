package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
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
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.border.BevelBorder;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
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
	private JPanel 
		listPanel,
		channelListPanel;
	JList<JButtonLengthLimited> 
		listItems;
	private Highlighter 
		hlPanel,
		selectedBtn;
	private LinkedHashMap<JButtonLengthLimited, Highlighter> 
		videoButtons = new LinkedHashMap<JButtonLengthLimited, Highlighter>();
	private LinkedHashMap<JLabel, Highlighter>
		videoLabelChannel = new LinkedHashMap<JLabel, Highlighter>();
	private HashMap<Integer, VideoSubSelectionLauncher> 
		vssl = null;
	private ArrayList<JButtonLengthLimited> 
		videoButtonsFiltered;
	
	public VideoChannelListViewJoy(JButtonLengthLimited parentButton, ArrayList <YoutubeChannelVideo> ycv)
	{
		this(parentButton, (Map<Integer, ArrayList<YoutubeChannelVideo>>) Collections.singletonMap(-1, ycv));
	}
	
	public VideoChannelListViewJoy(JButtonLengthLimited parentButton, Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		int key = ycvs.keySet().iterator().next();
		this.parentButtons = new HashMap<Integer, JButtonLengthLimited>();
		this.parentButtons.put(key, parentButton);
		vssl = new HashMap<Integer, VideoSubSelectionLauncher>();
		vssl.put(key, new VideoSubSelectionLauncher(parentButton));
		buildWidgets(null, ycvs);
	}
	
	public VideoChannelListViewJoy(HashMap<Integer, JButtonLengthLimited> parentButtons, 
			Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		this.parentButtons = parentButtons;
		vssl = new HashMap<Integer, VideoSubSelectionLauncher>();
		for(int key : ycvs.keySet())
		{
			vssl.put(key, new VideoSubSelectionLauncher(parentButtons.get(key)));
		}
		buildWidgets(this.parentButtons, ycvs);
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

	private void buildWidgets(Map<Integer, JButtonLengthLimited> parentButtons,
			Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		
		listPanel = new JPanel();
		listPanel.setLayout(new GridBagLayout());
		
		double
			titleCon = .8,
			titleCon2 = .0;
		int 
			gridXinc = 0;
			
		if(parentButtons != null)
		{
			titleCon = .5;
			titleCon2 = .3;
		}
		
		GridBagConstraints gbcT2 = new GridBagConstraints();
		if(parentButtons != null)
		{
			gbcT2.fill = GridBagConstraints.BOTH;
			gbcT2.gridx = gridXinc++;
			gbcT2.gridy = 0;
			gbcT2.weightx = titleCon2;
			gbcT2.weighty = 1;
		}
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = gridXinc++;
		gbc.gridy = 0;
		gbc.weightx = titleCon;
		gbc.weighty = 1;
		
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.gridx = gridXinc++;
		gbc2.gridy = 0;
		gbc2.weightx = .1;
		gbc2.weighty = 1;
		
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.fill = GridBagConstraints.BOTH;
		gbc3.gridx = gridXinc++;
		gbc3.gridy = 0;
		gbc3.weightx = .1;
		gbc3.weighty = 1;
		
		channelListPanel = new JPanel(new GridLayout(0,1));
		
		hlPanel = new Highlighter(this, borderColor);
		ArrayList<JButtonLengthLimited> btns = new ArrayList<JButtonLengthLimited>();
		LinkedHashMap<JButtonLengthLimited, String> 
			btnAndDuration = new LinkedHashMap<JButtonLengthLimited, String>();
		
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				if(parentButtons != null)
				{
					AbstractButton parentButton = parentButtons.get(key);
					JLabel lbl = buildChannelLabel(parentButton);
					channelListPanel.add(lbl);
				}
				JButtonLengthLimited jbll = buildVideoButton(key, ycv);
				jbll.setCharacterLimit(VIDEO_TITLE_CHARACTER_LIMIT);
				String duration = formatDuration(ycv.getDuration());
				String uploadDate = SDF_UPLOAD_SHORT.format(ycv.getUploadDate());
				btnAndDuration.put(jbll, duration);
				
				jbll.setToStringPrefix(uploadDate + " | " + duration + " | ");
				btns.add(jbll);
			}
		}
		
		videoButtonsFiltered = (MINIMUM_MINUTE > 0)
				? getVisible(0, MINIMUM_MINUTE, Mode.GreaterThan, btnAndDuration)
				: btns;
		listItems = new JList<JButtonLengthLimited>(videoButtonsFiltered.toArray(new JButtonLengthLimited[] {}));
		
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
		
		if(parentButtons != null)
		{
			listPanel.add(channelListPanel, gbcT2);
		}
		listPanel.add(listItems);
		
		this.setLayout(new BorderLayout());
		this.add(listPanel, BorderLayout.NORTH);
		RegisterArrayActionListener.addListener(this);
		
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
	
	private JLabel buildChannelLabel(AbstractButton ab)
	{
		BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
		JLabel lab = new JLabel(ab.getText());
		lab.setBorder(bb);
		lab.setHorizontalAlignment(JLabel.RIGHT);
		
		Highlighter hl = videoButtons.get(ab);
		videoLabelChannel.put(lab, hl);
		return lab;
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
		if(newButton == null)
			return;
		
		AbstractButton last = (newButton == null)
				? LaunchUrlActionListener.getLastButtonOrigin()
				: newButton;
		String name = (last == null)
				? null
				: last.getName();
		
		LoggingMessages.printOut("Find highlight: " + name);
		AbstractButton ab = getAbstractButton(name);
		if(ab == null)
			return;
		
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
		listItems.setSelectedIndex(index);
	}
	
	
	
	public ArrayList<JButtonLengthLimited> getVisible(int hour, int minute, Mode m, LinkedHashMap<JButtonLengthLimited, String> buttonAndDuration)
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
