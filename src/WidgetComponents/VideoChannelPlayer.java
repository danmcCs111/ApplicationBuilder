package WidgetComponents;

import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.AbstractButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.VideoSubSelectionActionListener;
import MouseListenersImpl.YoutubeChannelVideo;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;
import WidgetUtility.FileListOptionGenerator;

public class VideoChannelPlayer extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		TITLE_PREFIX = "Channel | ";
	private static Dimension 
		MIN_SIZE = new Dimension(750, 450);
	private static int 
		SCROLL_UNIT_INC = 25;
	
	private AbstractButton parentButton;

	public VideoChannelPlayer(
			HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs, AbstractButton parentButton, Container parent)
	{
		this.parentButton = parentButton;
		this.setTitle(TITLE_PREFIX + parentButton.getText());
		buildWidgets(ycvs);
		GraphicsUtil.rightEdgeCenterWindow(parent, this);
	}
	
	private void buildWidgets(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		JPanel listView = new JPanel();
		listView.setLayout(new GridLayout(0, 1));
		JScrollPane scrollPane = new JScrollPane(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		for(int key : ycvs.keySet())
		{
			for(YoutubeChannelVideo ycv : ycvs.get(key))
			{
				listView.add(buildVideoButton(ycv));
			}
		}
		this.add(scrollPane);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
	}
	
	private JButtonLengthLimited buildVideoButton(YoutubeChannelVideo ycv)
	{
		JButtonLengthLimited jbll = (JButtonLengthLimited) FileListOptionGenerator.buildComponent(
				"", ycv.getTitle(), ycv.getUrl(), JButtonLengthLimited.class);
		jbll.setToolTipText("Upload Date: " + ycv.getUploadDate().toString());
		jbll.setHighlightButton(parentButton);
		jbll.addActionListener(new VideoSubSelectionActionListener(parentButton, jbll));
		return jbll;
	}
}
