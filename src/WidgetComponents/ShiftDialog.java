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

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Graphics2D.GraphicsUtil;
import Params.KeepSelection;
import Properties.LoggingMessages;

public class ShiftDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Shift Frames",
		SHIFT_SPIN_LABEL = "Shift (x, y): ",
		SHIFT_SLIDER_LABEL = "Shift (x, y): ",
		APPLY_BUTTON_LABEL = "Apply",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 200);
	
	private JButton 
		applyButton = new JButton(APPLY_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JPanel 
		innerPanel = new JPanel(),
		innerPanelSpin = new JPanel(),
		innerPanelSlide = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JSpinner 
		xShift = new JSpinner(),
		yShift = new JSpinner();
	private JSlider 
		shiftSliderX = new JSlider(-1500, 1500, 0),//TODO
		shiftSliderY = new JSlider(JSlider.VERTICAL, -1500, 1500, 0);
	private JLabel 
		shiftSpinLabel = new JLabel(SHIFT_SPIN_LABEL),
		shiftLabelSlider = new JLabel(SHIFT_SLIDER_LABEL);
	
	private ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();
	private ArrayList<Point> keepsOriginalLocations = new ArrayList<Point>();
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
		copyKeepLocations(keepSelections);
		buildWidgets(referenceContainer);
	}
	
	public void buildWidgets(Container referenceContainer)
	{
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		
		ChangeListener cl = new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				applyAction();
			}
		};
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				if(!isSave)
				{
					ShiftDialog.this.cancelAction();
				}
			}
		});
		
		Dimension d = xShift.getPreferredSize();
		d.width = 100;
		xShift.addChangeListener(cl);
		yShift.addChangeListener(cl);
		xShift.setPreferredSize(d);
		yShift.setPreferredSize(d);
		
		shiftSliderX.addChangeListener(cl);
		shiftSliderY.addChangeListener(cl);
		shiftSliderX.setPreferredSize(d);
		Dimension vertD = shiftSliderY.getPreferredSize();
		vertD.height = 100;
		vertD.width = 100;
		shiftSliderY.setPreferredSize(vertD);
		shiftSliderY.setInverted(true);
		
		GraphicsUtil.rightEdgeTopWindow(referenceContainer, this);
		
		innerPanelSpin.setLayout(new FlowLayout());
		innerPanelSpin.add(shiftSpinLabel);
		innerPanelSpin.add(xShift);
		innerPanelSpin.add(yShift);
		
		innerPanelSlide.setLayout(new FlowLayout());
		innerPanelSlide.add(shiftLabelSlider);
		innerPanelSlide.add(shiftSliderX);
		innerPanelSlide.add(shiftSliderY);
		
		buildSaveCancel();
		innerPanel.setLayout(new BorderLayout());
		innerPanel.add(innerPanelSpin, BorderLayout.NORTH);
		innerPanel.add(innerPanelSlide, BorderLayout.CENTER);
		this.add(innerPanel, BorderLayout.NORTH);
		
		this.setVisible(true);
	}
	
	protected void buildSaveCancel()
	{
		saveCancelPanelOuter.setLayout(new BorderLayout());
		saveCancelPanel.setLayout(new GridLayout(1,2));
		
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyAction();
				isSave = true;
				ShiftDialog.this.dispose();
			}
		});
		saveCancelPanel.add(applyButton);
		
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
		}
	}
	
	private void updateKeeps()
	{
		for(int i = 0; i < keeps.size(); i++)
		{
			KeepSelection ks = keeps.get(i);
			Point p = keepsOriginalLocations.get(i);
			ks.getFrame().setLocation(
					p.x + shiftLocationSpin.x + shiftLocationSlide.x,
					p.y + shiftLocationSpin.y + shiftLocationSlide.y
			);
		}
	}
	
	private void applyAction()
	{
		int 
			spinX = (int)xShift.getValue(),
			spinY = (int)yShift.getValue(),
			slideX = shiftSliderX.getValue(),
			slideY = shiftSliderY.getValue();
		
		shiftLocationSpin = new Point(spinX, spinY);
		shiftLocationSlide = new Point(slideX, slideY);
		LoggingMessages.printOut("Shift amount: " + shiftLocationSpin);
		updateKeeps();
	}
	
	private void cancelAction()
	{
		//TODO
		shiftLocationSpin = new Point(0,0);
		shiftLocationSlide = new Point(0,0);
		updateKeeps();
		this.dispose();
	}
}
