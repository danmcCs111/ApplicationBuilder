package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
import MouseListenersImpl.VideoSubSelectionLauncher;
import MouseListenersImpl.YoutubeChannelVideo;
import Properties.LoggingMessages;
import Properties.StringUtility;
import WidgetComponents.DurationLimiter.Mode;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelListView extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
//	private static final String 
//		TOOLTIP_INSTRUCTION = "[Left click Primary Window, Middle click Alternate Window]";
	private static final SimpleDateFormat 
//		SDF_UPLOAD = new SimpleDateFormat("MM/dd/yyyy hh:mm a"),
		SDF_UPLOAD_SHORT = new SimpleDateFormat("MM/dd/yyyy");
	private static final int
		VIDEO_TITLE_CHARACTER_LIMIT = 80;
	
	private AbstractButton 
		parentButton;
	private static Color [] 
		foregroundAndBackgroundColor = new Color [] {
				ColorTemplate.getButtonForegroundColor(), ColorTemplate.getButtonBackgroundColor()},
		highlightForegroundAndBackgroundColor = new Color [] {
				Color.blue, Color.white};
	private static Color 
		borderColor = Color.ORANGE;
	private JPanel 
		listPanel,
		videoListPanel,
		uploadDatePanel,
		durationPanel;
	private Highlighter 
		hlPanel,
		selectedBtn;
	private LinkedHashMap<JButton, Highlighter> 
		videoButtons = new LinkedHashMap<JButton, Highlighter>();
	private LinkedHashMap<JLabel, Highlighter>
		videoLabelDuration = new LinkedHashMap<JLabel, Highlighter>();
	private LinkedHashMap<JLabel, Highlighter>
		videoLabelDate = new LinkedHashMap<JLabel, Highlighter>();
	private VideoSubSelectionLauncher 
		vssl = null;
	
	public VideoChannelListView(AbstractButton parentButton, HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		this.parentButton = parentButton;
		vssl = new VideoSubSelectionLauncher(parentButton);
		buildWidgets(ycvs);
	}
	
	public static void setBorderColor(Color color)
	{
		borderColor = color;
	}
	
	public static void setHighlightForegroundBackgroundColor(Color cForeground, Color cBackground)
	{
		highlightForegroundAndBackgroundColor = new Color [] {cForeground, cBackground};
	}
	
	public static void setForegroundBackgroundColor(Color cForeground, Color cBackground)
	{
		foregroundAndBackgroundColor = new Color [] {cForeground, cBackground};
	}

	private void buildWidgets(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		listPanel = new JPanel();
		listPanel.setLayout(new GridBagLayout());
		
		GridBagConstraints gbc = new GridBagConstraints();
		gbc.fill = GridBagConstraints.BOTH;
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.weightx = .8;
		gbc.weighty = 1;
		
		GridBagConstraints gbc2 = new GridBagConstraints();
		gbc2.fill = GridBagConstraints.BOTH;
		gbc2.gridx = 1;
		gbc2.gridy = 0;
		gbc2.weightx = .1;
		gbc2.weighty = 1;
		
		GridBagConstraints gbc3 = new GridBagConstraints();
		gbc3.fill = GridBagConstraints.BOTH;
		gbc3.gridx = 2;
		gbc3.gridy = 0;
		gbc3.weightx = .1;
		gbc3.weighty = 1;
		
		videoListPanel = new JPanel(new GridLayout(0,1));
		durationPanel = new JPanel(new GridLayout(0,1));
		uploadDatePanel = new JPanel(new GridLayout(0,1));
		
		hlPanel = new Highlighter(this, borderColor);
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				JButtonLengthLimited jbll = buildVideoButton(ycv);
				jbll.setCharacterLimit(VIDEO_TITLE_CHARACTER_LIMIT);
				videoListPanel.add(jbll);
				
				JLabel labDuration = buildDurationLabel(ycv, jbll);
				durationPanel.add(labDuration);
				
				JLabel labDate = buildUploadLabel(ycv, jbll);
				uploadDatePanel.add(labDate);
			}
		}
		
		listPanel.add(videoListPanel, gbc);
		listPanel.add(durationPanel, gbc2);
		listPanel.add(uploadDatePanel, gbc3);
		
		this.setLayout(new BorderLayout());
		this.add(listPanel, BorderLayout.NORTH);
	}
	
	public void setVisible(String searchText)
	{
		videoListPanel.removeAll();
		uploadDatePanel.removeAll();
		durationPanel.removeAll();
		
		Iterator<Entry<JLabel, Highlighter>>
			itDate = videoLabelDate.entrySet().iterator(),
			itDuration = videoLabelDuration.entrySet().iterator();
		
		for(JButton btn : videoButtons.keySet())
		{
			Map.Entry<JLabel, Highlighter> 
				entryDate = itDate.next(),
				entryDuration = itDuration.next();
			
			if(searchText.isEmpty() || btn.getText().toLowerCase().contains(searchText.toLowerCase()))
			{
				videoListPanel.add(btn);
				uploadDatePanel.add(entryDate.getKey());
				durationPanel.add(entryDuration.getKey());
			}
		}
	}
	
	public void setVisible(int hour, int minute, Mode m)
	{
		videoListPanel.removeAll();
		uploadDatePanel.removeAll();
		durationPanel.removeAll();
		
		Iterator<Entry<JLabel, Highlighter>>
			itDate = videoLabelDate.entrySet().iterator(),
			itDuration = videoLabelDuration.entrySet().iterator();
		
		int totalValue = Integer.parseInt(StringUtility.padTimeValue2(hour) + StringUtility.padTimeValue2(minute));
		
		LoggingMessages.printOut(totalValue + "");
		
		for(JButton btn : videoButtons.keySet())
		{
			Map.Entry<JLabel, Highlighter> 
				entryDate = itDate.next(),
				entryDuration = itDuration.next();
			
			String text = entryDuration.getKey().getText();
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
					videoListPanel.add(btn);
					uploadDatePanel.add(entryDate.getKey());
					durationPanel.add(entryDuration.getKey());
				}
				break;
				
			case LessThan:
				if(totalValueVideo < totalValue)
				{
					videoListPanel.add(btn);
					uploadDatePanel.add(entryDate.getKey());
					durationPanel.add(entryDuration.getKey());
				}
				break;
			}
		}
	}
	
	public void postFrameBuild()
	{
		findHighlight();
	}
	
	private JLabel buildUploadLabel(YoutubeChannelVideo ycv, JButtonLengthLimited jbll)
	{
		BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
		String uploadDate = SDF_UPLOAD_SHORT.format(ycv.getUploadDate());
		JLabel lab = new JLabel(uploadDate);
		lab.setBorder(bb);
		
		Highlighter hl = videoButtons.get(jbll);
		videoLabelDate.put(lab, hl);
		return lab;
	}
	
	private JLabel buildDurationLabel(YoutubeChannelVideo ycv, JButtonLengthLimited jbll)
	{
		BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
		String dur = formatDuration(ycv.getDuration());
		JLabel lab = new JLabel(dur);
		lab.setBorder(bb);
		
		Highlighter hl = videoButtons.get(jbll);
		videoLabelDuration.put(lab, hl);
		return lab;
	}
	
	private JButtonLengthLimited buildVideoButton(YoutubeChannelVideo ycv)
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
				"", ycv.getTitle(), ycv.getUrl(), JButtonLengthLimited.class);
//		String duration = ycv.getDuration();
//		String durText = "";
//		durText = formatDuration(duration);
//		durText += "<br>";
		
		jbll.setToolTipText(
				"<html>" +
				ycv.getTitle() + "<br>" + 
//				SDF_UPLOAD.format(ycv.getUploadDate()) + "<br>" +
//				durText +  
//				TOOLTIP_INSTRUCTION +
				"</html>"
				);
		jbll.setHighlightButton(parentButton);
		Highlighter hl = new Highlighter(jbll, highlightForegroundAndBackgroundColor, foregroundAndBackgroundColor);
		videoButtons.put(jbll, hl);
		jbll.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) 
			{
				int btn = e.getButton();
				switch(btn)
				{
				case MouseEvent.BUTTON1:
					vssl.performLaunch(jbll);
					performSelect(hl);
					break;
				case MouseEvent.BUTTON2:
					vssl.performLaunch(jbll, 1);
					break;
				case MouseEvent.BUTTON3:
					break;
				}
			}
		});
		return jbll;
	}
	
	private static String formatDuration(String duration)
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
	
	public void findHighlight()
	{
		AbstractButton last = LaunchUrlActionListener.getLastButtonOrigin();
		String name = (last == null)
				? null
				: last.getName();
		
		LoggingMessages.printOut("Find highlight: " + name);
		AbstractButton ab = getAbstractButton(name);
		if(ab == null)
			return;
		
		LaunchUrlActionListener.setLastButtonOrigin(ab);
		performSelect(videoButtons.get(ab));
		ab.requestFocusInWindow();
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
	
	public void performSelect(Highlighter hl)
	{
		if(hl == null)
			return;
		
		unselect();
		hlPanel.setHighlightForegroundAndBackground(true);
		hl.setHighlightForegroundAndBackground(true);
		selectedBtn = hl;
	}

	@Override
	public void addActionListener(ActionListener actionListener) 
	{
		
	}

	@Override
	public void unselect() 
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
}
