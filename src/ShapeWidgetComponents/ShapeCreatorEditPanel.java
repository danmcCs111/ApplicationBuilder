package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Editors.ColorEditor;
import Editors.PointEditor;
import Properties.LoggingMessages;
import ShapeEditorListeners.ShapePointChangeListener;
import ShapeWidgetComponents.ShapeCreator.DrawMode;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetUtility.WidgetBuildController;

public class ShapeCreatorEditPanel extends JPanel implements PostWidgetBuildProcessing 
{
	private static final long serialVersionUID = 1L;

	
	private static final String SHAPE_TITLE_SUFFIX = "#";
	
	private ShapeCreator sc;
	private HashMap<Integer, ArrayList<PointEditor>> indexAndPointEditors;
		
	public ShapeCreatorEditPanel()
	{
		
	}
	
	public void buildWidgets()
	{
		sc = (ShapeCreator) WidgetBuildController.getInstance().findRefByName("ShapeCreator").getInstance();//TODO
		indexAndPointEditors = new HashMap<Integer, ArrayList<PointEditor>>();
	}
	
	public void generatePointEditor(int index, Point [] points, DrawMode dm, Color colorPallette)
	{
		JPanel shapeEditPanel = new JPanel();
		
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		b = BorderFactory.createTitledBorder(b, dm.getModeText() + SHAPE_TITLE_SUFFIX + index);
		shapeEditPanel.setBorder(b);
		shapeEditPanel.setLayout(new GridLayout(0, 1));//TODO
		
		ArrayList<PointEditor> pointEditors = new ArrayList<PointEditor>();
		int i = 0;
		for(Point p : points)
		{
			if(p != null) 
			{
				PointEditor pe = new PointEditor();
				pe.setComponentValue(p);
				pe.addChangeListener(new ShapePointChangeListener(pe, sc, index, i));
				pointEditors.add(pe);
				shapeEditPanel.add(pe);
			}
			i++;
		}
		ShapeStyling shapeStyling = sc.getShapeStyling(index);
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
				// TODO Auto-generated method stub
				LoggingMessages.printOut("width changed.");
				shapeStyling.setStrokeWidth((int)strokeValue.getValue());
			}
		});
		
		shapeEditPanel.add(ce);
		shapeEditPanel.add(ceFill);
		shapeEditPanel.add(createStrokedShape);
		shapeEditPanel.add(strokeValue);
		this.add(shapeEditPanel);
		
		indexAndPointEditors.put(index, pointEditors);
		this.getRootPane().validate();
		sc.drawAll();
	}
	
	public void generatePointEditor(int index, ArrayList<Point> points, DrawMode dm)
	{
		generatePointEditor(index, points.toArray(new Point[] {}), dm, sc.getColorPallette());
	}
	
	public ArrayList<PointEditor> getPointEditors(int index)
	{
		return this.indexAndPointEditors.get(index);
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		sc.setShapeCreatorEditPanel(this);
		this.setEnabled(true);
		this.setVisible(true);
	}

}
