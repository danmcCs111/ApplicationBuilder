package WidgetComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JPanel;

import ActionListeners.ArrayActionListener;
import Graphics2D.ColorTemplate;
import MouseListenersImpl.VideoSubSelectionActionListener;
import MouseListenersImpl.YoutubeChannelVideo;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelListView extends JPanel implements ArrayActionListener
{
	private static final long serialVersionUID = 1L;
	
	private AbstractButton parentButton;
	private static Color [] 
		foregroundAndBackgroundColor = new Color [] {
				ColorTemplate.getButtonForegroundColor(), ColorTemplate.getButtonBackgroundColor()},
		highlightForegroundAndBackgroundColor = new Color [] {
				Color.blue, Color.white};
	private static Color 
		borderColor = Color.ORANGE;
	private Highlighter 
		hlPanel,
		selectedBtn;
	
	public VideoChannelListView(AbstractButton parentButton, HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		this.parentButton = parentButton;
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
		this.setLayout(new GridLayout(0, 1));
		hlPanel = new Highlighter(this, borderColor);
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				this.add(buildVideoButton(ycv));
			}
		}
	}
	
	private JButtonLengthLimited buildVideoButton(YoutubeChannelVideo ycv)
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
				"", ycv.getTitle(), ycv.getUrl(), JButtonLengthLimited.class);
		jbll.setToolTipText("Upload Date: " + ycv.getUploadDate().toString());
		jbll.setHighlightButton(parentButton);
		jbll.addActionListener(new VideoSubSelectionActionListener(parentButton, jbll));
		Highlighter hl = new Highlighter(jbll, highlightForegroundAndBackgroundColor, foregroundAndBackgroundColor);
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				unselect();
				hlPanel.setHighlightForegroundAndBackground(true);
				hl.setHighlightForegroundAndBackground(true);
				selectedBtn = hl;
			}
		});
		return jbll;
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
