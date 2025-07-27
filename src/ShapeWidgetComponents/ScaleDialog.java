package ShapeWidgetComponents;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Point;
import java.awt.Shape;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.WindowConstants;

import Graphics2D.GraphicsUtil;
import Properties.LoggingMessages;

public class ScaleDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Number Generator Command Config",
		SCALE_LABEL = "Scale To Percentage: ",
		APPLY_BUTTON_LABEL = "Apply",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	
	private JSlider scalingSlider = new JSlider(-100, 100, 0);
	private JLabel scalingLabel = new JLabel(SCALE_LABEL);
	private ShapeCreator sc;
	private JButton 
		applyButton = new JButton(APPLY_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	
	private int index;
	
	public ScaleDialog(Container referenceContainer, ShapeCreator sc, int index)
	{
		this.sc = sc;
		this.index = index;
		
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		GraphicsUtil.centerWindow(referenceContainer, this);
		
		innerPanel.setLayout(new GridLayout(0, 2));
		innerPanel.add(scalingLabel);
		innerPanel.add(scalingSlider);
		this.add(innerPanel, BorderLayout.NORTH);
		
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
		ArrayList<Point> sPoints = sc.getControlPointsForShapes().get(index);
		
		double scaleFactor = scalingSlider.getValue();
		scaleFactor /= 100;//adjust to percentage.
		scaleFactor += 1;
		
		LoggingMessages.printOut("Entered scale factor: " + scaleFactor);
		
		ArrayList<Point> controlPoints = ShapeUtils.scaleControlPoints(sc.getShapes().get(index), sPoints, scaleFactor);
		Shape s = ShapeUtils.recalculateShape(sc.getShapes().get(index), controlPoints);
		
		sc.getShapes().set(index, s);
		sc.getControlPointsForShapes().set(index, controlPoints);
		sc.notifyShapeAndControlPointsChangedListener(index);
		
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}

}
