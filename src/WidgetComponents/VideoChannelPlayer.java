package WidgetComponents;

import java.awt.Container;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.YoutubeChannelVideo;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelPlayer extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE_PREFIX = "Channel | ";
	private static Dimension 
		MIN_SIZE = new Dimension(750, 450);
	private static int 
		SCROLL_UNIT_INC = 25;
	
	private AbstractButton 
		parentButton;
	private VideoChannelListView 
		listView; 

	public VideoChannelPlayer(
			HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs, AbstractButton parentButton, Container parent)
	{
		this.parentButton = parentButton;
		this.setTitle(TITLE_PREFIX + parentButton.getText());
		buildWidgets(ycvs);
		GraphicsUtil.rightEdgeCenterWindow(parent, this);
	}
	
	public VideoChannelListView getVideoChannelListView()
	{
		return this.listView;
	}
	
	private void buildWidgets(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		listView = new VideoChannelListView(parentButton, ycvs);
		JScrollPane scrollPane = new JScrollPane(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		this.add(scrollPane);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		listView.postFrameBuild();
	}
	
}
