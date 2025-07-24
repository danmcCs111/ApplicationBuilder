package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.PathIterator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BezierCurveCalculations.AffineTransformSampler;
import Editors.ColorEditor;
import Properties.LoggingMessages;
import ShapeEditorListeners.DrawInputActionListener;
import ShapeEditorListeners.OpenShapeActionListener;
import ShapeEditorListeners.SaveShapeActionListener;
import ShapeEditorListeners.ShapeDirectionsNotification;
import ShapeEditorListeners.ShapeDrawModeActionListener;
import ShapeWidgetComponents.ShapeCreator.DrawMode;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetUtility.WidgetBuildController;

public class ShapeCreatorToolBarPanel extends JPanel implements PostWidgetBuildProcessing, ShapeDirectionsNotification
{
	private static final long serialVersionUID = 1L;
	
	private JButton 
		saveButton,
		addShape,
		openButton;
	private JLabel 
		directionsLabel,
		operationLabel;
	private ColorEditor colorEditorTop;
	private JComboBox<DrawMode> modeSelections;
	private ShapeCreator shapeCreator; 
	
	public ShapeCreatorToolBarPanel()
	{
		
	}
	
	public void buildWidgets()
	{
		shapeCreator = (ShapeCreator) WidgetBuildController.getInstance().findRefByName("ShapeCreator").getInstance();//TODO
		
		directionsLabel = new JLabel();
		operationLabel = new JLabel(shapeCreator.getOperation().getTitleText());
		modeSelections = new JComboBox<ShapeCreator.DrawMode>(DrawMode.values());
		addShape = new JButton("+ Add");
		saveButton = new JButton("Save");
		openButton = new JButton("Open");
		saveButton.addActionListener(new SaveShapeActionListener(shapeCreator));
		openButton.addActionListener(new OpenShapeActionListener(shapeCreator));
		addShape.addActionListener(new DrawInputActionListener(shapeCreator));
		modeSelections.addActionListener(new ShapeDrawModeActionListener(shapeCreator, this));
		colorEditorTop = new ColorEditor();
		colorEditorTop.addPropertyChangeListener(new PropertyChangeListener() {
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				shapeCreator.setColorPallette((Color)colorEditorTop.getComponentValueObj());
			}
		});
		colorEditorTop.setComponentValue(Color.black);//TODO
		
		JButton test = new JButton("draw clock");
		test.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				//test
				Ellipse2D.Double s = new Ellipse2D.Double(250, 200, 150, 150);
				AffineTransformSampler afs = new AffineTransformSampler();
				
				PathIterator pi = s.getPathIterator(afs);
				ArrayList<Point> points = new ArrayList<Point>();
				points.addAll(afs.samplePoints(pi, s, (1.0/120.0)));
				int count = 3;
				for(int i = 0; i < points.size(); i+=40)
				{
					Point p = points.get(i);
					Graphics2D g2d = (Graphics2D) shapeCreator.getGraphics();
					g2d.drawString(count+"", p.x, p.y);
					LoggingMessages.printOut(p.toString());
					count++;
					if(count > 12)
					{
						count = 1;
					}
				}
			}
		});
		
		this.add(directionsLabel);
		this.add(operationLabel);
		this.add(modeSelections);
		this.add(addShape);
		this.add(saveButton);
		this.add(openButton);
		this.add(colorEditorTop);
		this.add(test);
		
		shapeCreator.addShapeDirectionsNotification(this);
	}
	
	public ShapeCreator getShapeCreator()
	{
		return (ShapeCreator) this.getParent();
	}
	
	public Color getColorPallette()
	{
		return (Color) this.colorEditorTop.getComponentValueObj();
	}
	
	public DrawMode getMode()
	{
		return (DrawMode) this.modeSelections.getSelectedItem();
	}
	
	public JComboBox<DrawMode> getModeSelectionCombo()
	{
		return this.modeSelections;
	}
	
	public JLabel getDirectionsLabel()
	{
		return this.directionsLabel;
	}
	
	public JButton getAddCurveButton()
	{
		return this.addShape;
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().validate();
	}

	@Override
	public void shapeDirectionsUpdate(String updatedDirections) 
	{
		directionsLabel.setText(updatedDirections);
		if(updatedDirections.strip().isBlank())
		{
			this.addShape.setEnabled(true);
		}
		else if(directionsLabel.getText().isBlank() && !updatedDirections.isBlank())
		{
			this.addShape.setEnabled(false);
		}
	}
}
