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
		SHIFT_LABEL = "Axis Shift: ",
		APPLY_BUTTON_LABEL = "Apply",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension 
		MIN_DIMENSION_DIALOG = new Dimension(400, 150);
	
	private JLabel 
		shiftLabel = new JLabel(SHIFT_LABEL);
	private JButton 
		applyButton = new JButton(APPLY_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JSpinner 
		xShift = new JSpinner(),
		yShift = new JSpinner();
	
	//TODO list of original locations.
	private ArrayList<KeepSelection> keeps = new ArrayList<KeepSelection>();
	private ArrayList<Point> keepsOriginalLocations = new ArrayList<Point>();
	private Point scaleLocation = new Point(0,0);
	private boolean isSave = false;
	
	public ShiftDialog(Container referenceContainer, ArrayList<KeepSelection> keepSelections)
	{
		keeps = keepSelections;
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
		
		GraphicsUtil.rightEdgeTopWindow(referenceContainer, this);
		
		innerPanel.setLayout(new FlowLayout());
		innerPanel.add(shiftLabel);
		innerPanel.add(xShift);
		innerPanel.add(yShift);
		
		buildSaveCancel();
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
			ks.getFrame().setLocation(p.x + scaleLocation.x, p.y + scaleLocation.y);
		}
	}
	
	private void applyAction()
	{
		scaleLocation = new Point((int)xShift.getValue(), (int)yShift.getValue());
		LoggingMessages.printOut("Shift amount: " + scaleLocation);
		updateKeeps();
	}
	
	private void cancelAction()
	{
		//TODO
		scaleLocation = new Point(0,0);
		updateKeeps();
		this.dispose();
	}
}
