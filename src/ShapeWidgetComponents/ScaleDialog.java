package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;

public class ScaleDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Scale Shape",
		SCALE_LABEL = "Scale: ",
		APPLY_BUTTON_LABEL = "Apply",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	
	private JSlider scalingSlider = new JSlider(-100, 100, 0);
	private JLabel scalingLabel = new JLabel(SCALE_LABEL);
	private JButton 
		applyButton = new JButton(APPLY_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	
	private ShapeCreator sc;
	private ShapeStyling originalSs;
	private Shape originalShape;
	private ArrayList<Point> originalControlPoints;
	
	public ScaleDialog(Container referenceContainer, ShapeCreator sc, ShapeStyling ss)
	{
		int index = sc.getShapeDrawingCollection().getShapeStylings().indexOf(ss);
		this.sc = sc;
		this.originalSs = ss;
		this.originalShape = sc.getShapeDrawingCollection().getShapes().get(index);
		this.originalControlPoints = sc.getShapeDrawingCollection().getShapeControlPoints().get(index);
		
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		GraphicsUtil.centerWindow(referenceContainer, this);
		
		scalingSlider.addChangeListener(new ChangeListener() {
			@Override
			public void stateChanged(ChangeEvent e) {
				LoggingMessages.printOut("change");
				applyAction();
			}
		});
		
		innerPanel.setLayout(new GridLayout(0, 2));
		innerPanel.add(scalingLabel);
		innerPanel.add(scalingSlider);
		this.add(innerPanel, BorderLayout.NORTH);
		
		this.addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosed(WindowEvent e) {
				ScaleDialog.this.cancelAction();
			}
		});
		
		buildSaveCancel();
		this.setVisible(true);
	}
	
	protected void buildSaveCancel()
	{
		saveCancelPanelOuter.setLayout(new BorderLayout());
		saveCancelPanel.setLayout(new GridLayout(1,2));
		
		applyButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyAction();
				ScaleDialog.this.dispose();
			}
		});
		saveCancelPanel.add(applyButton);
		
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
	
	private void applyAction()
	{
		ArrayList<Point> sPoints = originalControlPoints;
		
		double scaleFactor = scalingSlider.getValue();
		scaleFactor /= 100;//adjust to percentage.
		scaleFactor += 1;
		
		LoggingMessages.printOut("Entered scale factor: " + scaleFactor);
		
		ArrayList<Point> controlPoints = ShapeUtils.scaleControlPoints(originalShape, sPoints, scaleFactor);
		Shape s = ShapeUtils.recalculateShape(originalShape, controlPoints);
		
		sc.getShapes().set(originalSs.getIndex(), s);
		sc.getControlPointsForShapes().set(originalSs.getIndex(), controlPoints);
		sc.notifyShapeAndControlPointsChangedListener(originalSs);
	}
	
	private void cancelAction()
	{
		sc.getShapes().set(originalSs.getIndex(), originalShape);
		sc.getControlPointsForShapes().set(originalSs.getIndex(), originalControlPoints);
		sc.notifyShapeAndControlPointsChangedListener(originalSs);
		this.dispose();
	}

}
