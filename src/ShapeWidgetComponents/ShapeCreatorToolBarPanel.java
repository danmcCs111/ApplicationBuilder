package ShapeWidgetComponents;

import java.awt.Color;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Ellipse2D.Double;
import java.awt.geom.Line2D;
import java.awt.geom.PathIterator;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import BezierCurveCalculations.AffineTransformSampler;
import BezierCurveCalculations.BezierCurveSample;
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
		
		JButton test = new JButton("draw curve");
		test.addActionListener(new ActionListener() {
			private Point 
				st = new Point(149, 205),
				c2 = new Point(360, 290),
				c1 = new Point(278, 219),
				end = new Point(303, 473),
				pointOld = st;
			@Override
			public void actionPerformed(ActionEvent e) {
				for(double i = 0.1; i < 1; i+=.01)
				{
					Point ret = BezierCurveSample.getPointAtCubicCurve(st, c1, c2, end, i);
					LoggingMessages.printOut(ret.toString());
					getShapeCreator().drawShape(new Line2D.Double(pointOld, ret), Color.black);
					pointOld = ret;
				}
				
				//test
				Ellipse2D.Double s = new Ellipse2D.Double(246, 192, 153, 185);
				AffineTransformSampler afs = new AffineTransformSampler();
				
				PathIterator pi = s.getPathIterator(afs);
				ArrayList<Shape> shapes = afs.sample(pi, s);
				
				shapeCreator.drawShapes(shapes);
				
				LoggingMessages.printOut(pi + "");
				LoggingMessages.printOut(s + "");
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
