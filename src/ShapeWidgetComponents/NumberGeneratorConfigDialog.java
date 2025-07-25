package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.PathIterator;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;

import BezierCurveCalculations.AffineTransformSampler;
import BezierCurveCalculations.ShapePositionOnPoints;
import Editors.ColorEditor;
import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;

public class NumberGeneratorConfigDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Number Generator Command Config",
		RANGE_LABEL = "Range",
		STARTING_NUMBER_LABEL = "Starting number",
		FONT_SIZE_LABEL = "Font",
		COLOR_FILL_LABEL = "Color Fill",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JLabel
		startingNumber = new JLabel(STARTING_NUMBER_LABEL),
		rangeLabel = new JLabel(RANGE_LABEL),
		fontSizeLabel = new JLabel(FONT_SIZE_LABEL),
		fillColorLabel = new JLabel(COLOR_FILL_LABEL);
	private JSpinner 
		range1Spinner = new JSpinner(),
		range2Spinner = new JSpinner(),
		startingNumberSpinner = new JSpinner(),
		fontSizeSpinner = new JSpinner();
	private ColorEditor
		fillColorEditor = new ColorEditor();
	private JButton 
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	
	private Shape s;
	private ShapeCreator sc;
	
	
	public NumberGeneratorConfigDialog(Container referenceContainer, ShapeCreator sc, Shape s)
	{
		this.s = s;
		this.sc = sc;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		GraphicsUtil.centerWindow(referenceContainer, this);
		
		innerPanel.setLayout(new GridLayout(0, 2));
		
		setDefaults();
		buildOptionsPanel();
		buildSaveCancel();
		
		
		this.setVisible(true);
	}
	
	protected void setDefaults()//TODO remove
	{
		range1Spinner.setValue(1);
		range2Spinner.setValue(12);
		startingNumberSpinner.setValue(3);
		fontSizeSpinner.setValue(18);
	}
	
	protected void buildOptionsPanel()
	{
		innerPanel.add(startingNumber);
		innerPanel.add(startingNumberSpinner);
		
		innerPanel.add(rangeLabel);
		JPanel rangePanel = new JPanel();
		rangePanel.setLayout(new GridLayout(0,2));
		rangePanel.add(range1Spinner);
		rangePanel.add(range2Spinner);
		innerPanel.add(rangePanel);
		
		innerPanel.add(fontSizeLabel);
		innerPanel.add(fontSizeSpinner);
		
		innerPanel.add(fillColorLabel);
		fillColorEditor.setComponentValue(Color.black);
		innerPanel.add(fillColorEditor);
		this.add(innerPanel, BorderLayout.NORTH);
	}
	
	protected void buildSaveCancel()
	{
		saveCancelPanelOuter.setLayout(new BorderLayout());
		saveCancelPanel.setLayout(new GridLayout(1,2));
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		saveCancelPanel.add(saveButton);
		
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
	
	private void saveAction()
	{
		double numOfSamples = 240.0;
		int 
			rangeValLow = getVal(range1Spinner),
			rangeValHigh = getVal(range2Spinner),
			startingNumber = getVal(startingNumberSpinner),
			fontSize = getVal(fontSizeSpinner);
		
		Font testFont = new Font("Serif", Font.BOLD, fontSize); //TODO
		AffineTransformSampler afs = new AffineTransformSampler();
		Color selectColor = (Color)fillColorEditor.getComponentValueObj();
		if(selectColor == null)
		{
			selectColor = Color.black;
		}
		ShapeStyling ss = new ShapeStyling(-1, selectColor, selectColor, null);//TODO
		
		PathIterator pi = s.getPathIterator(afs);
		ArrayList<Point> points = new ArrayList<Point>();
		points.addAll(afs.samplePoints(pi, s, (1.0/numOfSamples)));
		LoggingMessages.printOut("Number of Steps: " + afs.getNumberOfSteps());
		double it = ((rangeValHigh - (rangeValLow-1)) / afs.getNumberOfSteps());
		ShapePositionOnPoints.drawNumberSequence(points, (Graphics2D)sc.getDrawPanel().getGraphics(), testFont, ss,
				(int)(numOfSamples/it), rangeValLow, rangeValHigh, startingNumber);	
		
		this.dispose();
	}
	
	private int getVal(JSpinner spin)
	{
		int spinInt = (int)spin.getValue();
		if(spinInt > 0)
		{
			return spinInt;
		}
		return -1;
	}
	
	private void cancelAction()
	{
		this.dispose();
	}
	
}
