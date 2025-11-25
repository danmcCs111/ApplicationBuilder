package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Graphics2D.ColorTemplate;
import Graphics2D.GraphicsUtil;
import Params.KeepSelection;
import WidgetUtility.WidgetBuildController;

public class ShiftDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static String 
		TITLE = "Shift Frames",
		SHIFT_SPIN_LABEL = "Shift (x, y): ",
		SHIFT_SPIN_TOOLTIP_TEXT = "Adjust with numerical pixel offset",
		SHIFT_SLIDER_LABEL = "Shift (x, y): ",
		SLIDER_SETTING_LABEL = "Slider (x, y)",
		SLIDER_TOOLTIP_TEXT = "Max Pixel adjustment in - And + directions",
		APPLY_BUTTON_LABEL = "Apply",
		APPLY_AND_CLOSE_BUTTON_LABEL = "Apply And Close",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(600, 325);
	private static int 
		SLIDER_PIXEL_SIZE = 150,
		SLIDER_MAX_SETTING_X = 1500,
		SLIDER_MAX_SETTING_Y = 1500;  
	
	private JButton 
		applyButton = new JButton(APPLY_BUTTON_LABEL),
		applyAndCloseButton = new JButton(APPLY_AND_CLOSE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JPanel keepPanel = new JPanel();
	private JPanel 
		innerPanel = new JPanel(),
		innerPanelSpin = new JPanel(),
		innerPanelSlide = new JPanel(),
		innerPanelSlideSetting = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JSpinner 
		xShift = new JSpinner(),
		yShift = new JSpinner(),
		xSliderMinMax = new JSpinner(),
		ySliderMinMax = new JSpinner();
	private JSlider 
		shiftSliderX = new JSlider(-SLIDER_MAX_SETTING_X, SLIDER_MAX_SETTING_X, 0),
		shiftSliderY = new JSlider(JSlider.VERTICAL, -SLIDER_MAX_SETTING_Y, SLIDER_MAX_SETTING_Y, 0);
	private JLabel 
		shiftSpinLabel = new JLabel(SHIFT_SPIN_LABEL),
		shiftLabelSlider = new JLabel(SHIFT_SLIDER_LABEL),
		sliderMaxLabel = new JLabel(SLIDER_SETTING_LABEL);
	private JList<String> keepList;
	
	private ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();
	private ArrayList<Point> 
		keepsOriginalLocations = new ArrayList<Point>(),
		keepsNewLocations = new ArrayList<Point>();
	private Point 
		shiftLocationSpin = new Point(0,0),
		shiftLocationSlide = new Point(0,0);
	private boolean isSave = false;
	
	public ShiftDialog(Container referenceContainer, ArrayList<KeepSelection> keepSelections)
	{
		keeps = keepSelections;
		if(keeps.isEmpty()) {
			return;
		}
		Collections.sort(keeps, new KeepSelection());
		copyKeepLocations(keeps);
		buildWidgets(referenceContainer);
	}
	
	public void buildWidgets(Container referenceContainer)
	{
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!isSave)
				{
					ShiftDialog.this.cancelAction();
				}
			}
		});
		
		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				applyAction();
			}
		};
		ChangeListener settingsCL = new ChangeListener() {
			
			@Override
			public void stateChanged(ChangeEvent e) {
				changeSliderSettings();
			}
		};
		
		buildSliders(cl);
		buildSpinners(cl);
		buildKeepList();
		buildSliderSettings(settingsCL);
		buildSaveCancel();
		
		
		innerPanel.setLayout(new BorderLayout());
		JPanel fillPanel = new JPanel();
		fillPanel.setLayout(new GridLayout(2,1));
		fillPanel.add(innerPanelSlideSetting);
		fillPanel.add(innerPanelSpin);
		innerPanel.add(innerPanelSlide, BorderLayout.NORTH);
		innerPanel.add(fillPanel, BorderLayout.SOUTH);
		this.add(innerPanel, BorderLayout.CENTER);
		this.add(keepPanel, BorderLayout.EAST);
		GraphicsUtil.centerReferenceOnlyWindow(WidgetBuildController.getInstance().getFrame(), this);
		
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		
		this.pack();
		
		this.setVisible(true);
	}
	
	protected void buildSliders(ChangeListener cl)
	{
		Dimension vertXD = shiftSliderY.getPreferredSize();
		vertXD.width = SLIDER_PIXEL_SIZE;
		Dimension vertYD = shiftSliderY.getPreferredSize();
		vertYD.height = SLIDER_PIXEL_SIZE;
		vertYD.width = SLIDER_PIXEL_SIZE;
		
		shiftSliderX.addChangeListener(cl);
		shiftSliderY.addChangeListener(cl);
		shiftSliderX.setPreferredSize(vertXD);
		shiftSliderY.setPreferredSize(vertYD);
		shiftSliderY.setInverted(true);
		
		innerPanelSlide.setLayout(new FlowLayout());
		innerPanelSlide.add(shiftLabelSlider);
		innerPanelSlide.add(shiftSliderX);
		innerPanelSlide.add(shiftSliderY);
	}
	
	protected void buildSliderSettings(ChangeListener cl)
	{
		sliderMaxLabel.setToolTipText(SLIDER_TOOLTIP_TEXT);
		Dimension d = xSliderMinMax.getPreferredSize();
		d.width = SLIDER_PIXEL_SIZE;
		xSliderMinMax.setPreferredSize(d);
		ySliderMinMax.setPreferredSize(d);
		
		xSliderMinMax.setValue(SLIDER_MAX_SETTING_X);
		ySliderMinMax.setValue(SLIDER_MAX_SETTING_Y);
		
		innerPanelSlideSetting.setLayout(new FlowLayout());
		innerPanelSlideSetting.add(sliderMaxLabel);
		innerPanelSlideSetting.add(xSliderMinMax);
		innerPanelSlideSetting.add(ySliderMinMax);
		
		xSliderMinMax.addChangeListener(cl);
		ySliderMinMax.addChangeListener(cl);
	}
	
	protected void buildSpinners(ChangeListener cl)
	{
		shiftSpinLabel.setToolTipText(SHIFT_SPIN_TOOLTIP_TEXT);
		Dimension d = xShift.getPreferredSize();
		d.width = SLIDER_PIXEL_SIZE;
		xShift.addChangeListener(cl);
		yShift.addChangeListener(cl);
		xShift.setPreferredSize(d);
		yShift.setPreferredSize(d);
		
		innerPanelSpin.setLayout(new FlowLayout());
		innerPanelSpin.add(shiftSpinLabel);
		innerPanelSpin.add(xShift);
		innerPanelSpin.add(yShift);
	}
	
	private void changeSliderSettings()
	{
		SLIDER_MAX_SETTING_X = (int) xSliderMinMax.getValue();
		SLIDER_MAX_SETTING_Y = (int) ySliderMinMax.getValue();
		
		shiftSliderY.setMinimum(-SLIDER_MAX_SETTING_Y);
		shiftSliderX.setMinimum(-SLIDER_MAX_SETTING_X);
		
		shiftSliderY.setMaximum(SLIDER_MAX_SETTING_Y);
		shiftSliderX.setMaximum(SLIDER_MAX_SETTING_X);
	}
	
	protected void buildKeepList()
	{
		String [] options = new String[keeps.size()];
		int [] selected = new int [keeps.size()];
		int i = 0;
		for(KeepSelection ks : keeps) 
		{
			options[i] = ks.getText();
			selected[i] = i;
			i++;
		}
		keepList = new JList<String>(options);
		keepList.setSelectedIndices(selected);//select all.
		
		keepPanel = new JPanel();
		keepPanel.setLayout(new BorderLayout());
		keepPanel.add(keepList, BorderLayout.CENTER);
	}
	
	protected void buildSaveCancel()
	{
		saveCancelPanelOuter.setLayout(new BorderLayout());
		saveCancelPanel.setLayout(new GridLayout(1,2));
		
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyActionAndUpdate();
			}
		});
		saveCancelPanel.add(applyButton);
		
		applyAndCloseButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyActionAndUpdate();
				isSave = true;
				ShiftDialog.this.dispose();
			}
		});
		saveCancelPanel.add(applyAndCloseButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		saveCancelPanel.add(cancelButton);
		
		saveCancelPanelOuter.add(saveCancelPanel, BorderLayout.EAST);
		this.add(saveCancelPanelOuter, BorderLayout.SOUTH);
	}
	
	private void copyKeepLocations(ArrayList<KeepSelection> keepSelections)
	{
		for(KeepSelection ks : keepSelections)
		{
			Point loc = ks.getFrame().getLocation();
			keepsOriginalLocations.add(loc);
			keepsNewLocations.add(loc);
		}
	}
	
	private void updateKeepsListFilter(boolean store)
	{
		int [] is = keepList.getSelectedIndices();
		List<Integer> selected = Arrays.stream(is).boxed().collect(Collectors.toList());
		for(int i = 0; i < keeps.size(); i++)
		{
			Integer key = i;
			if(!selected.contains(key))
			{
				continue;
			}
			if(store)
			{
				updateAndStoreKeep(i);
			}
			else
			{
				updateKeep(i);
			}
		}
	}
	
	private void updateKeepOriginal(int index)
	{
		KeepSelection ks = keeps.get(index);
		Point p = keepsOriginalLocations.get(index);
		int 
			newX = p.x + shiftLocationSpin.x + shiftLocationSlide.x,
			newY =  p.y + shiftLocationSpin.y + shiftLocationSlide.y;
		ks.getFrame().setLocation(newX, newY);
	}
	private void updateKeep(int index)
	{
		KeepSelection ks = keeps.get(index);
		Point p = keepsNewLocations.get(index);
		int 
			newX = p.x + shiftLocationSpin.x + shiftLocationSlide.x,
			newY =  p.y + shiftLocationSpin.y + shiftLocationSlide.y;
		ks.getFrame().setLocation(newX, newY);
	}
	private void updateAndStoreKeep(int index)
	{
		KeepSelection ks = keeps.get(index);
		Point p = keepsNewLocations.get(index);
		int 
			newX = p.x + shiftLocationSpin.x + shiftLocationSlide.x,
			newY =  p.y + shiftLocationSpin.y + shiftLocationSlide.y;
		ks.getFrame().setLocation(newX, newY);
		keepsNewLocations.set(index, new Point(newX, newY));
	}
	private void clearAllKeeps()
	{
		shiftLocationSpin = new Point(0,0);
		shiftLocationSlide = new Point(0,0);
		for(int i = 0; i < keeps.size(); i++)
		{
			updateKeepOriginal(i);
		}
	}
	
	private void applyAction()
	{
		copyShiftValues();
		updateKeepsListFilter(false);
	}
	private void applyActionAndUpdate()
	{
		copyShiftValues();
		updateKeepsListFilter(true);
		
		shiftLocationSpin = new Point(0, 0);
		shiftLocationSlide = new Point(0, 0);
		setSpinAndSlide(shiftLocationSpin, shiftLocationSlide);
	}
	
	private void setSpinAndSlide(Point spinPoint, Point slidePoint)
	{
		int 
			spinX = (int) spinPoint.getX(),
			spinY = (int) spinPoint.getY(),
			slideX = (int) slidePoint.getX(),
			slideY = (int) slidePoint.getY();
		
		xShift.setValue(spinX);
		yShift.setValue(spinY);
		
		shiftSliderX.setValue(slideX);
		shiftSliderY.setValue(slideY);
	}
	
	private void copyShiftValues()
	{
		int 
			spinX = (int)xShift.getValue(),
			spinY = (int)yShift.getValue(),
			slideX = shiftSliderX.getValue(),
			slideY = shiftSliderY.getValue();
	
		shiftLocationSpin = new Point(spinX, spinY);
		shiftLocationSlide = new Point(slideX, slideY);
	}
	
	private void cancelAction()
	{
		clearAllKeeps();
		this.dispose();
	}
}
