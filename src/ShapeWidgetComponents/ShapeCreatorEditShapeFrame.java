package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.PathIterator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import BezierCurveCalculations.AffineTransformSampler;
import BezierCurveCalculations.ShapePositionOnPoints;
import Editors.ColorEditor;
import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;

public class ShapeCreatorEditShapeFrame extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension EDITOR_FRAME_SIZE = new Dimension(500,150);
	
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
	
	public void buildWidgets(ShapeStyling shapeStyling, Shape s, String title)
	{
		this.title = title;
		JComponent parentPanel = new JPanel();
		parentPanel.setLayout(new BorderLayout());
		JPanel innerPanel = new JPanel();
		FlowLayout fl = new FlowLayout(FlowLayout.RIGHT);
		parentPanel.setLayout(fl);
		
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
				Font testFont = new Font("Serif", Font.BOLD, 16); //TODO
				AffineTransformSampler afs = new AffineTransformSampler();
				ShapeStyling ss = new ShapeStyling(-1, Color.blue, Color.blue, null);
				
				PathIterator pi = s.getPathIterator(afs);
				ArrayList<Point> points = new ArrayList<Point>();
				double numOfSamples = 240.0;
				points.addAll(afs.samplePoints(pi, s, (1.0/numOfSamples)));
				LoggingMessages.printOut("Number of Steps: " + afs.getNumberOfSteps());
				double it = (12 / afs.getNumberOfSteps());//TODO
				ShapePositionOnPoints.drawNumberSequence(points, (Graphics2D)sc.getDrawPanel().getGraphics(), testFont, ss,
						(int)(numOfSamples/it), 1, 12, 3);
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
		
		innerPanel.add(ce);
		innerPanel.add(ceFill);
		innerPanel.add(applyShapeNumberGenerator);
		innerPanel.add(createStrokedShape);
		innerPanel.add(strokeValue);
		
		parentPanel.add(innerPanel, BorderLayout.EAST);
		
		this.add(parentPanel);
	}
	
}
