package WidgetComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Shape;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Editors.PointEditor;
import WidgetComponents.ShapeCreator.DrawMode;

public class ShapeCreatorEditPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private HashMap<Integer, ArrayList<PointEditor>> indexAndPointEditors;
		
	
	public ShapeCreatorEditPanel()
	{
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		b = BorderFactory.createTitledBorder(b, "Edit");
		this.setBorder(b);
		this.setLayout(new GridLayout(0, 1));
		
		indexAndPointEditors = new HashMap<Integer, ArrayList<PointEditor>>();
		
	}
	
	public void generatePointEditor(int index, Point [] points, DrawMode dm)
	{
		JPanel shapeEditPanel = new JPanel();
		
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		b = BorderFactory.createTitledBorder(b, "Edit " + dm.getModeText() + "#" + index);
		shapeEditPanel.setBorder(b);
		shapeEditPanel.setLayout(new GridLayout(0, 2));
		
		ArrayList<PointEditor> pointEditors = new ArrayList<PointEditor>();
		
		for(Point p : points)
		{
			if(p != null) //built off an array
			{
				PointEditor pe = new PointEditor();
				pe.setComponentValue(p);
				pointEditors.add(pe);
				shapeEditPanel.add(pe);
			}
		}
		this.add(shapeEditPanel);
		
		indexAndPointEditors.put(index, pointEditors);
	}
	
	public void generatePointEditor(int index, ArrayList<Point> points, DrawMode dm)
	{
		generatePointEditor(index, points.toArray(new Point[] {}), dm);
	}
	
	public ArrayList<PointEditor> getPointEditors(int index)
	{
		return this.indexAndPointEditors.get(index);
	}
	
}
