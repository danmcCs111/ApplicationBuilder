package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

import ActionListeners.ArrayActionListener;
import ActionListenersImpl.LaunchUrlActionListener;
import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import MouseListenersImpl.LookupOrCreateYoutube;
import MouseListenersImpl.YoutubeChannelVideo;
import ObjectTypeConversion.FileSelection;
import WidgetComponentInterfaces.DefaultAndScaledImage;
import WidgetComponentInterfaces.DurationLimitSubscriber;
import WidgetComponentInterfaces.ImageReader;
import WidgetComponentInterfaces.RegisterArrayActionListener;
import WidgetComponentInterfaces.SearchSubscriber;
import WidgetComponents.DurationLimiter.Mode;
import WidgetExtensions.ExtendedSetScrollBackgroundForegroundColor;

public class AllVideoChannelsOpenedPlayer extends JFrame implements DefaultAndScaledImage, ArrayActionListener
{
	private static final long serialVersionUID = 1L;

	private static Dimension 
		MIN_SIZE = new Dimension(1050, 450);
	private static String
		TITLE = "All Open Video Channels",
		HOME_PAGE_TOOLTIP_TEXT = "[ <arg0> ] - Homepage",
		UPDATE_BUTTON_TEXT = "Update",
		ALL_SELECT_TEXT = "All Channels";
	private static int 
		DEFAULT_MINUTE_SETTING = 10,
		SEARCH_COLUMN_LENGTH = 15,
		SCROLL_UNIT_INC = 25,
		SCALED_WIDTH_ICON = 35;
	
	private JButton 
		updateButton = new JButton(UPDATE_BUTTON_TEXT),
		imageLabel = new JButton();
	private JButtonLengthLimited
		selectedButton = null,
		selectedButtonParent = null;
	private Container 
		parentContainer;
	private VideoChannelListView 
		listView; 
	private JScrollPane 
		channelScroll,
		contentScrollPane;
	private DefaultAndScaledImage 
		parentScaler;
	private AbstractButton
		highlightButton;
	private Border
		defaultBorder = new JButton().getBorder();
	
	private HashMap <Integer, ArrayList <YoutubeChannelVideo>> 
		ycvs; 
	private HashMap<Integer, JButtonLengthLimited> 
		parentButtons;
	private HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>
		parentButtonAndYoutubeVideos = new HashMap<JButtonLengthLimited, ArrayList<YoutubeChannelVideo>>();
	private HashMap<AbstractButton, JButtonLengthLimited>
		selectionButtonAndParentButton = new HashMap<AbstractButton, JButtonLengthLimited>();
	private LinkedHashMap<JButtonLengthLimited, ImageIcon> 
		buttonAndIcon;
	
	private LookupOrCreateYoutube 
		lcv = new LookupOrCreateYoutube();
	private OpenVideoChannelsUpdater
		ovcu;
	private ImageReader //TODO keep scaled image icons.
		ir;
	
	public AllVideoChannelsOpenedPlayer(DefaultAndScaledImage parentScaler, 
			LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon, 
			Container parentContainer)
	{
		this.parentScaler = parentScaler;
		this.parentButtons = new HashMap<Integer, JButtonLengthLimited>();
		this.parentContainer = parentContainer;
		this.ycvs = new HashMap<Integer, ArrayList<YoutubeChannelVideo>>();
		this.buttonAndIcon = buttonAndIcon;
		ir = new ImageReader(this, true);
		
		Runnable r = new Runnable()
		{
			@Override
			public void run() {
				buildLoadingFrame(buttonAndIcon);
			}
		};
		Thread t = new Thread(r);
		t.start();
	}
	
	public void buildLoadingFrame(LinkedHashMap<JButtonLengthLimited, ImageIcon> buttonAndIcon) 
	{
		JFrame loadingFrame = new JFrame();
		loadingFrame.setResizable(false);
		loadingFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		loadingFrame.setVisible(true);
		GraphicsUtil.rightEdgeTopWindow(parentContainer, loadingFrame);
		
		loadingFrame.setMinimumSize(new Dimension(180,70));//TODO
		LoadingLabel label = new LoadingLabel();
		label.updateCount(SCROLL_UNIT_INC, DEFAULT_MINUTE_SETTING);
		loadingFrame.add(label);
		
		ColorTemplate.setBackgroundColorPanel(loadingFrame, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(loadingFrame, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(loadingFrame, ColorTemplate.getButtonForegroundColor());
		
		int count = 0;
		for(JButtonLengthLimited jbll : buttonAndIcon.keySet())
		{
			label.updateCount(count, buttonAndIcon.keySet().size());
			HashMap<Integer, ArrayList<YoutubeChannelVideo>> vids = lcv.lookup(
					jbll.getText(), jbll.getName());
			
			if(vids != null && !vids.isEmpty())
			{
				int key = vids.keySet().iterator().next();
				this.ycvs.put(key, vids.get(key));
				this.parentButtons.put(key, jbll);
			}
			count++;
		}
		loadingFrame.dispose();
		
		buildWidgets();
	}
	
	public static void setDefaultMinuteSetting(int minute)
	{
		DEFAULT_MINUTE_SETTING = minute;
	}
	
	public VideoChannelListView getVideoChannelListView()
	{
		return this.listView;
	}
	
	public void buildWidgets()
	{
		JPanel listPanel = new JPanel();
		listPanel.setLayout(new GridLayout(0,1));
		channelScroll = new JScrollPane(listPanel);
		channelScroll.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		listView = new VideoChannelListView(parentButtons, ycvs);
		
		JPanel searchPanel = new JPanel();
		FlowLayout fl = new FlowLayout();
		fl.setAlignment(FlowLayout.LEFT);
		searchPanel.setLayout(fl);
		
		updateButton.addActionListener(getUpdateChannelsActionListener());
		
		SearchBar sb = new SearchBar();
		sb.setColumnCharacterLength(SEARCH_COLUMN_LENGTH);
		sb.addSearchSubscriber(new SearchSubscriber() {
			@Override
			public void notifySearchText(String searchPattern) {
				listView.setVisible(searchPattern);
				AllVideoChannelsOpenedPlayer.this.validate();
			}
		});
		DurationLimitSubscriber dls = new DurationLimitSubscriber() {
			@Override
			public void notifyDurationLimit(int hour, int minute, Mode m) {
				listView.setVisible(hour, minute, m);
				AllVideoChannelsOpenedPlayer.this.validate();
			}
		};
		DurationLimiter dl = new DurationLimiter(dls);
		dl.setMinuteDefault(DEFAULT_MINUTE_SETTING);
		
		searchPanel.add(imageLabel);
		searchPanel.add(updateButton);
		searchPanel.add(sb);
		searchPanel.add(dl);
		
		AbstractButton allSelectBtn = buildAllSelectionButton();
		listPanel.add(allSelectBtn);
		for(int i : parentButtons.keySet())
		{
			parentButtonAndYoutubeVideos.put(parentButtons.get(i), ycvs.get(i));
			AbstractButton ab = buildSelectionButton(parentButtons.get(i));
			selectionButtonAndParentButton.put(ab, parentButtons.get(i));
			ab.setIcon(ir.getScaledImageIcon(buttonAndIcon.get(parentButtons.get(i)).getImage()));
			ab.setHorizontalAlignment(AbstractButton.LEFT);
			listPanel.add(ab);
		}
		setImageButton(null);
		
		this.setLayout(new BorderLayout());
		this.add(channelScroll, BorderLayout.WEST);
		addListView();
		this.add(searchPanel, BorderLayout.NORTH);
		
		refreshListView(allSelectBtn);
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), channelScroll);
		
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				for(JButtonLengthLimited jbll : parentButtonAndYoutubeVideos.keySet())
				{
					ArrayList <YoutubeChannelVideo> ycvs = parentButtonAndYoutubeVideos.get(jbll);
					ycvs.clear();
				}
				for(int key : ycvs.keySet())
				{
					ycvs.get(key).clear();
				}
				ycvs.clear();
				parentButtonAndYoutubeVideos.clear();
				selectionButtonAndParentButton.clear();
				parentButtons.clear();
				
				//remove components
				for(int i = 0; i < listPanel.getComponentCount(); i++)
				{
					Component c = listPanel.getComponent(i);
					if(c instanceof AbstractButton)
					{
						((AbstractButton) c).setIcon(null);
					}
				}
				listPanel.removeAll();
				listView.removeAll();
				AllVideoChannelsOpenedPlayer.this.removeAll();
			}
		});
		this.setTitle(TITLE);
		this.setMinimumSize(MIN_SIZE);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		this.setIconImage(JButtonArray.getMoviesIcon());
		RegisterArrayActionListener.addListener(this);
		this.setVisible(true);
		urlSelect(LaunchUrlActionListener.getLastButtonOrigin());
		GraphicsUtil.rightEdgeTopWindow(parentContainer, this);
		
	}
	
	public void addListView()
	{
		if(contentScrollPane == null)
		{
			contentScrollPane = new JScrollPane();
			contentScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_UNIT_INC);
		}
		contentScrollPane.setViewportView(listView);
		this.add(contentScrollPane, BorderLayout.CENTER);
	}
	
	public void removeListView()
	{
		contentScrollPane.remove(listView);
		this.remove(contentScrollPane);
	}
	
	public void setImageButton(JButtonLengthLimited jbllParent)
	{
		for(MouseListener ml : imageLabel.getMouseListeners())
		{
			imageLabel.removeMouseListener(ml);
		}
		for(ActionListener al : updateButton.getActionListeners())
		{
			updateButton.removeActionListener(al);
		}
		
		if(jbllParent == null)
		{
			imageLabel.setVisible(false);
			updateButton.addActionListener(getUpdateChannelsActionListener());
			return;
		}
		imageLabel.setVisible(true);
		updateButton.addActionListener(getUpdateChannelActionListener());
		
		imageLabel.setIcon(ir.getScaledImageIcon(buttonAndIcon.get(jbllParent).getImage()));//TODO
		imageLabel.setToolTipText(HOME_PAGE_TOOLTIP_TEXT.replaceAll("<arg0>", jbllParent.getText()));
		
		imageLabel.addMouseListener(new MouseAdapter() {
			@Override
			public void mouseClicked(MouseEvent e) {
				int button = e.getButton();
				switch(button)
				{
				case MouseEvent.BUTTON1:
					jbllParent.doClick();
					break;
				case MouseEvent.BUTTON2:
					for(MouseListener ml : jbllParent.getMouseListeners())
					{
						e.setSource(jbllParent);
						ml.mouseClicked(e);
					}
					break;
				case MouseEvent.BUTTON3://ignore
					break;
				}
			}
		});
	}
	
	public AbstractButton buildAllSelectionButton()
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(35);
		jbll.setText(ALL_SELECT_TEXT);
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				listView = new VideoChannelListView(parentButtons, ycvs);
				removeListView();
				addListView();
				setImageButton(null);
				refreshListView(jbll);
			}
		});
		
		return jbll;
	}
	
	public AbstractButton buildSelectionButton(JButtonLengthLimited parentButton)
	{
		JButtonLengthLimited jbll = new JButtonLengthLimited();
		jbll.setCharacterLimit(35);
		jbll.setText(parentButton.getText());
		
		jbll.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				selectedButton = jbll;
				selectedButtonParent = parentButton;
				
				ArrayList<YoutubeChannelVideo> ycv = parentButtonAndYoutubeVideos.get(parentButton);
				int count = lcv.lookupCount(parentButton.getText(), parentButton.getName());
				
				if(count > ycv.size())
				{
					refreshSelectionFromDB(selectedButtonParent, selectedButton);
				}
				else
				{
					refreshSelection(selectedButtonParent, selectedButton);
				}
			}
		});
		return jbll;
	}
	
	public void refreshListView(AbstractButton selectedButton)
	{
		ColorTemplate.setBackgroundColorPanel(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(AllVideoChannelsOpenedPlayer.this, ColorTemplate.getButtonForegroundColor());
		ExtendedSetScrollBackgroundForegroundColor.applyBackgroundForeground(
				ColorTemplate.getPanelBackgroundColor(), ColorTemplate.getButtonBackgroundColor(), contentScrollPane);
		
		selectedButton.setBackground(ColorTemplate.getButtonForegroundColor());
		selectedButton.setForeground(ColorTemplate.getButtonBackgroundColor());
		listView.postFrameBuild();
		AllVideoChannelsOpenedPlayer.this.validate();
	}
	
	private ActionListener getUpdateChannelActionListener() 
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				if(selectedButton == null)
					return;
				
				refreshSelectionFromDB(selectedButtonParent, selectedButton);
			}
		};
	}
	
	private ActionListener getUpdateChannelsActionListener()
	{
		return new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				if(ovcu != null)
				{
					ovcu.dispose();
				}
				Runnable r = new Runnable() {
					@Override
					public void run() {
						List<JButtonLengthLimited> jblls = Arrays.asList
								(parentButtons.values().toArray(new JButtonLengthLimited [] {}));
						ovcu = new OpenVideoChannelsUpdater(jblls);
						GraphicsUtil.centerWindow(AllVideoChannelsOpenedPlayer.this, ovcu);
					}
				};
				Thread t = new Thread(r);
				t.start();
			}
		};
	}
	
	private void refreshSelection(JButtonLengthLimited buttonParent, JButtonLengthLimited selectedButton)
	{
		ArrayList<YoutubeChannelVideo> ycv = parentButtonAndYoutubeVideos.get(buttonParent);
		listView = new VideoChannelListView(buttonParent, ycv);
		removeListView();
		addListView();
		refreshListView(selectedButton);
		setImageButton(buttonParent);
	}
	
	private void refreshSelectionFromDB(JButtonLengthLimited selectedButtonParent, JButtonLengthLimited selectedButton)
	{
		lcv.update(selectedButtonParent.getText(), selectedButtonParent.getName());
		HashMap <Integer, ArrayList <YoutubeChannelVideo>> ycvs = lcv.lookup(
				selectedButtonParent.getText(), selectedButtonParent.getName());
		int key = ycvs.keySet().iterator().next();
		parentButtonAndYoutubeVideos.put(selectedButtonParent, ycvs.get(key));
		removeListView();
		listView = new VideoChannelListView(selectedButton, ycvs);
		addListView();
		setImageButton(selectedButtonParent);//TODO
		refreshListView(selectedButton);
	}
	
	@Override
	public String getDefaultImagePath() 
	{
		return parentScaler.getDefaultImagePath();
	}

	@Override
	public Dimension getScaledDefaultPic() 
	{
		return parentScaler.getScaledDefaultPic();
	}

	@Override
	public Dimension getDefaultPicSize() 
	{
		return parentScaler.getDefaultPicSize();
	}

	@Override
	public int getScaledWidth() 
	{
		return SCALED_WIDTH_ICON;
	}

	@Override
	public void setDefaultImageXmlPath(FileSelection fs) 
	{
		return;
	}

	@Override
	public void setScaledDefaultPic(Dimension scaledDefaultPicDimension) 
	{
		return;
	}

	@Override
	public void setDefaultPicSize(Dimension defaultPicDimension) 
	{
		return;
	}

	@Override
	public void setScaledWidth(int scaledWidth) 
	{
		SCALED_WIDTH_ICON = scaledWidth;
	}

	@Override
	public void addActionListener(ActionListener actionListener) {
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void urlSelect(AbstractButton newButton) 
	{
		if(highlightButton != null)
		{
			highlightButton.setBorder(defaultBorder);
		}
		if(newButton == null)
		{
			return;
		}
		
		AbstractButton altButton = null;
		if(newButton instanceof JButtonLengthLimited)
		{
			altButton = ((JButtonLengthLimited) newButton).getHighlightButton();
		}
		for(AbstractButton ab : selectionButtonAndParentButton.keySet())
		{
			if(ab.getText().equals(newButton.getText()) || 
					(altButton != null && altButton.getText().equals(ab.getText()))
			)
			{
				//highlight.
				highlightButton = ab;
				highlightButton.setBorder(Highlighter.getBorderHighlight());
				break;
			}
		}
		
		channelScroll.repaint();
		channelScroll.validate();
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

	@Override
	public void addStripFilter(String filter) {
		// TODO Auto-generated method stub
	}
	
}
