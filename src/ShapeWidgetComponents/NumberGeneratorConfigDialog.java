package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.WindowConstants;

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
	
	private ShapeCreator sc;
	private int index;
	private NumberGeneratorConfig ngConfig;
	
	
	public NumberGeneratorConfigDialog(Container referenceContainer, ShapeCreator sc, int index)
	{
		this(referenceContainer, null, sc, index);
	}
	
	public NumberGeneratorConfigDialog(Container referenceContainer, NumberGeneratorConfig ngConfig, ShapeCreator sc, int index)
	{
		if(ngConfig == null)
		{
			this.ngConfig = new NumberGeneratorConfig(1, 12, 3, 18, Color.black);
		}
		else
		{
			this.ngConfig = ngConfig;
		}
		
		this.sc = sc;
		this.index = index;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		GraphicsUtil.centerWindow(referenceContainer, this);
		
		innerPanel.setLayout(new GridLayout(0, 2));
		
		setDefaults(this.ngConfig);
		buildOptionsPanel();
		buildSaveCancel();
		this.setVisible(true);
	}
	
	protected void setDefaults(NumberGeneratorConfig ngConfig)
	{
		range1Spinner.setValue(ngConfig.getRangeValLow());
		range2Spinner.setValue(ngConfig.getRangeValHigh());
		startingNumberSpinner.setValue(ngConfig.getStartingNumber());
		fontSizeSpinner.setValue(ngConfig.getFontSize());
		fillColorEditor.setComponentValue(ngConfig.getFillColor());
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
		ShapeStyling ss = sc.getShapeStylings().get(index);
		Shape s = sc.getShapesScaled().get(index);
		int 
			rangeValLow = getVal(range1Spinner),
			rangeValHigh = getVal(range2Spinner),
			startingNumber = getVal(startingNumberSpinner),
			fontSize = getVal(fontSizeSpinner);
		Color selectColor = (Color)fillColorEditor.getComponentValueObj();
		NumberGeneratorConfig ngConfig = new NumberGeneratorConfig(rangeValLow, rangeValHigh, startingNumber, fontSize, selectColor);
		ss.setNumberGeneratorConfig(ngConfig, s);
		
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
		ShapeStyling ss = sc.getShapeStylings().get(index);
		ss.setNumberGeneratorConfig(null, null);//TODO
		LoggingMessages.printOut("Cancel");
		this.dispose();
	}
	
}
