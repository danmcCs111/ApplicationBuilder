package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JPanel;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
import MouseListenersImpl.VideoSubSelectionLauncher;
import MouseListenersImpl.YoutubeChannelVideo;
import Properties.LoggingMessages;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelListView extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TOOLTIP_INSTRUCTION = "(Left click Primary Window, Middle click Alternate Window)";
	
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
		listPanel;
	private Highlighter 
		hlPanel,
		selectedBtn;
	private LinkedHashMap<JButton, Highlighter> 
		videoButtons = new LinkedHashMap<JButton, Highlighter>();
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
		listPanel.setLayout(new GridLayout(0,1));
		
		hlPanel = new Highlighter(this, borderColor);
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				JButtonLengthLimited jbll = buildVideoButton(ycv);
				listPanel.add(jbll);
			}
		}
		
		this.setLayout(new BorderLayout());
		this.add(listPanel, BorderLayout.NORTH);
	}
	
	public void setVisible(String searchText)
	{
		listPanel.removeAll();
		for(JButton btn : videoButtons.keySet())
		{
			if(searchText.isEmpty() || btn.getText().toLowerCase().contains(searchText.toLowerCase()))
			{
				listPanel.add(btn);
			}
		}
	}
	
	public void postFrameBuild()
	{
		findHighlight();
	}
	
	private JButtonLengthLimited buildVideoButton(YoutubeChannelVideo ycv)
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
				"", ycv.getTitle(), ycv.getUrl(), JButtonLengthLimited.class);
		jbll.setToolTipText(
				"<html>Upload Date: " + ycv.getUploadDate().toString() + "<br>" + 
				TOOLTIP_INSTRUCTION +
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
