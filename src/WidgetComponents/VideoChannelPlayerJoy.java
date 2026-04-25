package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowFocusListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.FrameMouseDragListener;
import MouseListenersImpl.YoutubeChannelVideo;
import MouseListenersImpl.YoutubeVideosContainer;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelPlayerJoy extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		COUNT_PREFIX = "Video Count: ",
		UPDATE_BUTTON_TEXT = "Update",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		TITLE_PREFIX = "Channel | ";
	private static Dimension 
		MIN_SIZE = new Dimension(1600, 425);
	private static int 
		SCROLL_UNIT_INC = 25;
	private static Border
		COUNT_BORDER = new EmptyBorder(5, 0, 5, 15);//EmptyBorder(top, left, bottom, right)
	private static final Font 
		SELECT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	
	private JButtonLengthLimited 
		parentButton;
	private VideoChannelListViewJoy
		listView; 
	private JScrollPane 
		scrollPane;
	private ImageIcon 
		videoImage;
	private YoutubeVideosContainer 
		fmdl;

	public VideoChannelPlayerJoy(
			ImageIcon videoImage, YoutubeVideosContainer fmdl, JButtonLengthLimited parentButton, Container parent)
	{
		this.parentButton = parentButton;
		this.videoImage = videoImage;
		this.fmdl = fmdl;
		this.setTitle(TITLE_PREFIX + parentButton.getText());
		buildWidgets(fmdl.getYoutubeVideos());
		GraphicsUtil.centerOnScreen(this);
	}
	
	public VideoChannelListViewJoy getVideoChannelListView()
	{
		return this.listView;
	}
	
	private void buildWidgets(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		buildCenterPanel(ycvs);
		
		this.setIconImage(videoImage.getImage());
		this.setLayout(new BorderLayout());
		this.add(buildWestPanel(), BorderLayout.WEST);
		this.add(scrollPane, BorderLayout.CENTER);
		this.add(buildSouthPanel(parentButton), BorderLayout.SOUTH);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				dispose();
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				listView.setFocus();
			}
		});
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setVisible(true);
		
		
		listView.postFrameBuild();
	}
	
	public JPanel buildWestPanel()
	{
		JPanel searchPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		searchPanel.setLayout(fl);
		
		JButton imageLabel = new JButton(videoImage);
		imageLabel.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", parentButton.getText()));
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				switch(button)
				{
				case MouseEvent.BUTTON1:
					parentButton.doClick(); 
					break;
				case MouseEvent.BUTTON2:
					for(MouseListener ml : parentButton.getMouseListeners())
					{
						e.setSource(parentButton);
						ml.mouseClicked(e);
					}
					break;
				case MouseEvent.BUTTON3://ignore
					break;
				}
			}
		});
		
		JButton updateButton = new JButton(UPDATE_BUTTON_TEXT);
		updateButton.setFont(SELECT_FONT);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = new Runnable() {
					@Override
					public void run() {
						fmdl.update();
						fmdl.buildVideoChannelPlayer();
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		});
		
		searchPanel.add(imageLabel);
//		searchPanel.add(updateButton);
		
		return searchPanel;
	}
	
	public void buildCenterPanel(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		listView = new VideoChannelListViewJoy(parentButton, ycvs);
		scrollPane = new JScrollPane(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
	}
	
	public JPanel buildSouthPanel(JButtonLengthLimited parentButton)
	{
		JPanel 
			southPane = new JPanel();
		JLabel 
			countLabel = new JLabel();
		int 
			count = FrameMouseDragListener.getLookupOrCreate().lookupCount(parentButton.getText(), parentButton.getName());
		
		southPane.setLayout(new BorderLayout());
		
		countLabel.setText(COUNT_PREFIX + count);
		countLabel.setBorder(COUNT_BORDER);
		southPane.add(countLabel, BorderLayout.EAST);
		
		return southPane;
	}
	
}
