package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Editors.ColorEditor;
import Editors.PointEditor;
import Properties.LoggingMessages;
import ShapeEditorListeners.ShapePointChangeListener;
import ShapeWidgetComponents.ShapeCreator.DrawMode;

public class ShapeCreatorEditPanel extends JPanel 
{
	private static final long serialVersionUID = 1L;

	private ShapeCreator sc;
	private HashMap<Integer, ArrayList<PointEditor>> indexAndPointEditors;
		
	
	public ShapeCreatorEditPanel(ShapeCreator sc)
	{
		this.sc = sc;
		this.setLayout(new GridLayout(0,1));
		
		indexAndPointEditors = new HashMap<Integer, ArrayList<PointEditor>>();
	}
	
	public void generatePointEditor(int index, Point [] points, DrawMode dm, Color colorPallette)
	{
		JPanel shapeEditPanel = new JPanel();
		
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		b = BorderFactory.createTitledBorder(b, dm.getModeText() + "#" + index);
		shapeEditPanel.setBorder(b);
		shapeEditPanel.setLayout(new GridLayout(0, 1));
		
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
		ColorEditor ce = new ColorEditor();
		ce.setComponentValue(colorPallette);
		ce.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) 
			{
				LoggingMessages.printOut("color change");
				ShapeStyling ss = sc.getShapeStyling(index);
				ss.setDrawColor((Color) ce.getComponentValueObj());
			}
		});
		ColorEditor ceFill = new ColorEditor();
		ceFill.setComponentValue(colorPallette);
		ceFill.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) 
			{
				LoggingMessages.printOut("color change");
				ShapeStyling ss = sc.getShapeStyling(index);
				ss.setFillColor((Color) ceFill.getComponentValueObj());
			}
		});
		
		shapeEditPanel.add(ce);
		shapeEditPanel.add(ceFill);
		this.add(shapeEditPanel);
		
		indexAndPointEditors.put(index, pointEditors);
	}
	
	public void generatePointEditor(int index, ArrayList<Point> points, DrawMode dm)
	{
		generatePointEditor(index, points.toArray(new Point[] {}), dm, sc.getColorPallette());
	}
	
	public ArrayList<PointEditor> getPointEditors(int index)
	{
		return this.indexAndPointEditors.get(index);
	}

}
