package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;
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

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import HttpDatabaseRequest.HttpJoystickFuctionRequest;
import MouseListenersImpl.FrameMouseDragListener;
import MouseListenersImpl.YoutubeChannelVideo;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelPlayerJoy extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static String
		COUNT_PREFIX = "Video Count: ",
		UPDATE_BUTTON_TEXT = "Update",
		HOME_BUTTON_TEXT = "",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		TITLE_PREFIX = "Channel | ";
	private static Dimension 
		MIN_SIZE = new Dimension(1600, 600);
	private static int 
		SCROLL_UNIT_INC = 25;
	private static Border
		COUNT_BORDER = new EmptyBorder(5, 0, 5, 15);//EmptyBorder(top, left, bottom, right)
	private static final Font 
		SELECT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, 24);
	
	private VideoChannelListViewJoy
		listView; 
	private JScrollPane 
		scrollPane = new JScrollPane();
	private JLabel
		countLabel;
	private JButton 
		imageHomeButton,
		updateButton;

	public VideoChannelPlayerJoy(Container parent)
	{
		listView = new VideoChannelListViewJoy();
		buildWidgets();
		GraphicsUtil.centerOnScreen(this);
	}
	
	public VideoChannelListViewJoy getVideoChannelListView()
	{
		return this.listView;
	}
	
	public void setVideos(ImageIcon videoImage, JButtonLengthLimited parentButton)
	{
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = HttpJoystickFuctionRequest.getYoutubeVideos();
		if(ycvs == null || ycvs.isEmpty())
			return;
		
		this.setTitle(TITLE_PREFIX + parentButton.getText());
		this.setIconImage(videoImage.getImage());
		setListVideos(ycvs, parentButton);
		imageHomeButton.setIcon(videoImage);
		setHomeButton(parentButton);
		setCount(parentButton);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), scrollPane);
		
		this.setVisible(true);
		listView.postFrameBuild();
	}
	
	private void buildWidgets()
	{
		this.setLayout(new BorderLayout());
		this.add(buildWestPanel(), BorderLayout.WEST);
		this.add(buildNorthPanel(), BorderLayout.NORTH);
		this.add(buildCenterPanel(), BorderLayout.CENTER);
		this.add(buildSouthPanel(), BorderLayout.SOUTH);
		
		this.addWindowFocusListener(new WindowFocusListener() {
			@Override
			public void windowLostFocus(WindowEvent e) {
				setVisible(false);
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				listView.setFocus();
			}
		});
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public JPanel buildNorthPanel()
	{
		JPanel northPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		northPanel.setLayout(fl);
		
		updateButton = new JButton(UPDATE_BUTTON_TEXT);
		updateButton.setFont(SELECT_FONT);
		updateButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Runnable r = new Runnable() {
					@Override
					public void run() {
						HttpJoystickFuctionRequest.update();
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		});
		northPanel.add(updateButton);
		
		return northPanel;
	}
	
	public JPanel buildWestPanel()
	{
		JPanel westPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		westPanel.setLayout(fl);
		
		imageHomeButton = new JButton();
		imageHomeButton.setFont(SELECT_FONT);
		imageHomeButton.setText(HOME_BUTTON_TEXT);
		westPanel.add(imageHomeButton);
		
		return westPanel;
	}
	
	public void setListVideos(HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs, JButtonLengthLimited parentButton)
	{
		listView.setVideos(parentButton, ycvs);
	}
	
	public JScrollPane buildCenterPanel()
	{
		scrollPane.setViewportView(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		return scrollPane;
	}
	
	public JPanel buildSouthPanel()
	{
		JPanel 
			southPane = new JPanel();
		
		countLabel = new JLabel();
		southPane.setLayout(new BorderLayout());
		
		countLabel.setBorder(COUNT_BORDER);
		southPane.add(countLabel, BorderLayout.EAST);
		
		return southPane;
	}
	
	private void setCount(AbstractButton parentButton)
	{
		int 
			count = FrameMouseDragListener.getLookupOrCreate().lookupCount(
					parentButton.getText(), 
					parentButton.getName()
			);
		countLabel.setText(COUNT_PREFIX + count);
	}
	
	private void setHomeButton(AbstractButton parentButton)
	{
		for(MouseListener ml : imageHomeButton.getMouseListeners())
		{
			imageHomeButton.removeMouseListener(ml);
		}
		imageHomeButton.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", parentButton.getText()));
		imageHomeButton.addMouseListener(new MouseAdapter() {
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
	}
	
	public void doUpdate()
	{
		updateButton.doClick();
	}
	
	public void doHomeButtonClick()
	{
		sendMouseClick(imageHomeButton);
	}
	
	public void sendMouseClick(Component source)
	{
		MouseEvent me = new MouseEvent(source, -1, JComponent.WHEN_FOCUSED, JComponent.WHEN_FOCUSED, 0, 0, 0, 0, 1, false, 1);
		for(MouseListener ml : source.getMouseListeners())
		{
			ml.mouseClicked(me);
		}
	}
	
}
