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
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

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
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.YoutubeChannelVideo;
import Properties.StringUtility;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class VideoChannelPlayerJoy extends VideoChannelPlayer implements DurationLimitSubscriber
{
	private static final long serialVersionUID = 1L;
	
	private static String
		COUNT_PREFIX = "Video Count: ",
		UPDATE_BUTTON_TEXT = "Update",
		HOME_BUTTON_TEXT = "",
		DATE_RANGE_FORMAT = "[ <arg> - <arg> ]",
		REPLACE_ARG = "<arg>",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg> ] - Homepage",
		TITLE_PREFIX = "<arg> (Press right/left trigger to gain focus.)";
	private static Dimension 
		MIN_SIZE = new Dimension(1350, 600);
	private static int 
		SIZE = 20,
		SCROLL_UNIT_INC = 25;
	private static Border
		COUNT_BORDER = new EmptyBorder(5, 0, 5, 15);//EmptyBorder(top, left, bottom, right)
	private static Font 
		SELECT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, SIZE);
	private static SimpleDateFormat
		SDF_DATE_LABEL = new SimpleDateFormat("MM/dd/YYYY");
	
	private VideoChannelListViewJoy
		listView; 
	private JScrollPane 
		scrollPane = new JScrollPane();
	private JLabel
		dateRangeLabel,
		countLabel;
	private JButtonLengthLimited 
		parentButton;
	private JButton 
		imageHomeButton,
		updateButton;
	private KeepSelectionSelector
		kss;
	
	public VideoChannelPlayerJoy()
	{
		
	}

	public VideoChannelPlayerJoy(Container parent, KeepSelectionSelector kss)
	{
		this.kss = kss;
		listView = new VideoChannelListViewJoy();
		buildWidgets();
		GraphicsUtil.centerOnScreen(this);
	}
	
	public static void setAltFontSize(int size)
	{
		SIZE = size;
		SELECT_FONT = new Font(Font.SANS_SERIF, Font.PLAIN, SIZE);
	}
	
	public static Font getFontAlt()
	{
		return SELECT_FONT;
	}
	
	@Override
	public VideoChannelListViewJoy getVideoChannelListView()
	{
		return this.listView;
	}
	
	public void setVideos(ImageIcon videoImage, JButtonLengthLimited parentButton, HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs)
	{
		if(ycvs == null || ycvs.isEmpty())
			return;
		
		this.setTitle(StringUtility.replaceArg(TITLE_PREFIX, REPLACE_ARG, parentButton.getText()));
		this.setIconImage(videoImage.getImage());
		this.parentButton = parentButton;
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
	
	public void update() 
	{
		LookupOrCreateYoutube.update(parentButton.getText(), parentButton.getName());
		//TODO. if closing?
//		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = LookupOrCreateYoutube.lookup(parentButton.getText(), parentButton.getName());
//		setListVideos(ycvs, parentButton);
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
				if(kss != null)
				{
					kss.setSelected(true);
				}
			}
			@Override
			public void windowGainedFocus(WindowEvent e) {
				listView.setFocus();
			}
		});
		
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	private JPanel buildNorthPanel()
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
						update();
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		});
		dateRangeLabel = new JLabel();
		dateRangeLabel.setFont(SELECT_FONT);
		DurationLimiter dl = new DurationLimiter(this);
		dl.setMinuteDefault(VideoChannelListViewJoy.getMinimumMinute());
		dl.setFontChildren(SELECT_FONT);
		
		northPanel.add(updateButton);
		northPanel.add(dateRangeLabel);
		northPanel.add(dl);
		
		return northPanel;
	}
	
	private JPanel buildWestPanel()
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
	
	private void setListVideos(Map <Integer, ArrayList <YoutubeChannelVideo>> ycvs, JButtonLengthLimited parentButton)
	{
		listView.setVideos(parentButton, ycvs);
		setDateRangeText(parentButton);
	}
	
	private void setDateRangeText(JButtonLengthLimited parentButton)
	{
		Date
			firstDate = LookupOrCreateYoutube.lookupFirstDate(parentButton.getText(), parentButton.getName()).values().iterator().next(),
			latestDate = LookupOrCreateYoutube.lookupLatestDate(parentButton.getText(), parentButton.getName()).values().iterator().next();
		String 
			fDate = SDF_DATE_LABEL.format(firstDate),
			lDate = SDF_DATE_LABEL.format(latestDate);
		dateRangeLabel.setText(StringUtility.replaceArg(DATE_RANGE_FORMAT, REPLACE_ARG, new String [] {lDate, fDate}));
	}
	
	private JScrollPane buildCenterPanel()
	{
		scrollPane.setViewportView(listView);
		scrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		
		return scrollPane;
	}
	
	private JPanel buildSouthPanel()
	{
		JPanel 
			southPane = new JPanel();
		
		countLabel = new JLabel();
		countLabel.setFont(SELECT_FONT);
		southPane.setLayout(new BorderLayout());
		
		countLabel.setBorder(COUNT_BORDER);
		southPane.add(countLabel, BorderLayout.EAST);
		
		return southPane;
	}
	
	private void setCount(AbstractButton parentButton)
	{
		int 
			count = LookupOrCreateYoutube.lookupCount(
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
		imageHomeButton.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll(REPLACE_ARG, parentButton.getText()));
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

	@Override
	public void notifyDurationLimit(int hour, int minute, Mode m) 
	{
		int min = 0;
		switch(m)
		{
		case GreaterThan:
			min += hour * 60;
			min += minute;
			VideoChannelListViewJoy.setMinimumMinute(Math.abs(min));
			break;
			
		case LessThan:
			min += hour * 60;
			min += minute;
			VideoChannelListViewJoy.setMinimumMinute(-Math.abs(min));
			break;
		}
	}
	
}
