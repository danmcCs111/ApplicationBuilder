package WidgetComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Editors.PointEditor;

public class ShapeCreatorEditPanel extends JPanel
{
	private static final long serialVersionUID = 1L;

	private JLabel titleLabel = new JLabel("Edit");
	private HashMap<Integer, ArrayList<PointEditor>> indexAndPointEditors;
		
	
	public ShapeCreatorEditPanel()
	{
		this.add(titleLabel);
		this.add(new JLabel());
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		this.setBorder(b);
		this.setLayout(new GridLayout(0, 2));
		
		indexAndPointEditors = new HashMap<Integer, ArrayList<PointEditor>>();
		
	}
	
	public void generatePointEditor(int index, Point [] points)
	{
		ArrayList<PointEditor> pointEditors = new ArrayList<PointEditor>();
		
		for(Point p : points)
		{
			if(p != null) //built off an array
			{
				PointEditor pe = new PointEditor();
				pe.setComponentValue(p);
				pointEditors.add(pe);
				this.add(pe);
			}
		}
		
		indexAndPointEditors.put(index, pointEditors);
	}
	
	public void generatePointEditor(int index, ArrayList<Point> points)
	{
		generatePointEditor(index, points.toArray(new Point[] {}));
	}
	
	public ArrayList<PointEditor> getPointEditors(int index)
	{
		return this.indexAndPointEditors.get(index);
	}
	
}
