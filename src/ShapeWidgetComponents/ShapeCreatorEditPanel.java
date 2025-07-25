package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.HashMap;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import Editors.PointEditor;
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
		
		String title = dm.getModeText() + SHAPE_TITLE_SUFFIX + index;
		
		Border b = BorderFactory.createBevelBorder(BevelBorder.RAISED, Color.gray, Color.gray);
		b = BorderFactory.createTitledBorder(b, title);
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
		
		
		JButton showEditButton = new JButton("editWidget");
		showEditButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(!ShapeCreatorEditShapeFrame.containsEditor(title))
				{
					ShapeStyling shapeStyling = sc.getShapeStyling(index);
					ShapeCreatorEditShapeFrame scEditFrame = new ShapeCreatorEditShapeFrame(sc);
					scEditFrame.buildWidgets(shapeStyling, sc.getShapesScaled().get(index), title);
					scEditFrame.display();
				}
			}
		});
		
		shapeEditPanel.add(showEditButton);
		
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
