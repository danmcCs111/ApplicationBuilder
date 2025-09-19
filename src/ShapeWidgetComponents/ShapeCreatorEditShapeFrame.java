package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Editors.ColorEditor;
import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;

public class ShapeCreatorEditShapeFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension EDITOR_FRAME_SIZE = new Dimension(550,150);
	
	private ShapeCreator sc;
	private String title = "";
	
	private static ArrayList<ShapeCreatorEditShapeFrame> selves = new ArrayList<ShapeCreatorEditShapeFrame>();
	
	public ShapeCreatorEditShapeFrame(ShapeCreator sc)
	{
		this.sc = sc;
	}
	
	public static boolean containsEditor(String titleId)
	{
		for(ShapeCreatorEditShapeFrame scEdit : selves)
		{
			if(scEdit.getTitle().equals(titleId))
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public void dispose()
	{
		LoggingMessages.printOut("closing: " + title);
		selves.remove(this);
		for(int i = 0; i < selves.size(); i++)
		{
			GraphicsUtil.rightEdgeTopWindow(sc.getRootPane().getParent(), selves.get(i), i);
		}
		super.dispose();
	}
	
	public void display()
	{
		selves.add(this);
		this.setTitle(title);
		this.setSize(EDITOR_FRAME_SIZE);
		GraphicsUtil.rightEdgeTopWindow(sc.getRootPane().getParent(), this, selves.indexOf(this));
		
		this.setVisible(true);
		this.setEnabled(true);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
	}
	
	public void buildWidgets(ShapeCreator sc, int index, String title)
	{
		ShapeStyling shapeStyling = sc.getShapeStylings().get(index);
		this.setLayout(new GridLayout(0,1));
		
		this.title = title;
		JComponent parentPanel1 = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.LEFT);
		parentPanel1.setLayout(fl);
		JComponent parentPanel2 = new JPanel();
		FlowLayout f2 = new FlowLayout(FlowLayout.LEFT);
		parentPanel2.setLayout(f2);
		
		JPanel innerPanel = new JPanel();
		JPanel innerPanel2 = new JPanel();
		
		ColorEditor ce = new ColorEditor();
		ce.setComponentValue(shapeStyling.getDrawColor());
		ce.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) 
			{
				LoggingMessages.printOut("color change");
				shapeStyling.setDrawColor((Color) ce.getComponentValueObj());
			}
		});
		ColorEditor ceFill = new ColorEditor();
		ceFill.setComponentValue(shapeStyling.getFillColor());
		ceFill.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) 
			{
				LoggingMessages.printOut("color change");
				shapeStyling.setFillColor((Color) ceFill.getComponentValueObj());
			}
		});
		
		JButton applyShapeNumberGenerator = new JButton("Number Generator");
		applyShapeNumberGenerator.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				
				new NumberGeneratorConfigDialog(
						ShapeCreatorEditShapeFrame.this, 
						shapeStyling.getNumberGeneratorConfig(), 
						sc, index);
			}
		});
		
		JButton scaleShape = new JButton("Scale Shape");
		scaleShape.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {

				new ScaleDialog(sc.getRootPane().getParent(), sc, shapeStyling);
				
			}
		});
		
		JComboBox<Boolean> createStrokedShape = new JComboBox<Boolean>(new Boolean[] {false,true});
		createStrokedShape.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeStyling.createStrokedShape((boolean)createStrokedShape.getSelectedItem());
			}
		});
		
		JSpinner strokeValue = new JSpinner();
		strokeValue.setValue(shapeStyling.getStrokeWidth());
		strokeValue.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				LoggingMessages.printOut("width changed.");
				shapeStyling.setStrokeWidth((int)strokeValue.getValue());
			}
		});
		
		JCheckBox skipShapeDraw = new JCheckBox();
		skipShapeDraw.setText("Skip Shape Draw");
		skipShapeDraw.setSelected(shapeStyling.skipShapeDraw());
		skipShapeDraw.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				shapeStyling.setSkipShapeDraw(skipShapeDraw.getSelectedObjects() != null);
			}
		});
		
		innerPanel.add(ce);
		innerPanel.add(ceFill);
		innerPanel.add(applyShapeNumberGenerator);
		innerPanel2.add(scaleShape);
		innerPanel2.add(createStrokedShape);
		innerPanel2.add(strokeValue);
		innerPanel2.add(skipShapeDraw);
		
		parentPanel1.add(innerPanel);
		parentPanel2.add(innerPanel2);
		
		this.add(parentPanel1);
		this.add(parentPanel2);
	}
	
}
