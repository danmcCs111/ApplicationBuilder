package ShapeWidgetComponents;

import java.awt.Color;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import Editors.ColorEditor;
import ShapeEditorListeners.OpenShapeActionListener;
import ShapeEditorListeners.SaveShapeActionListener;
import ShapeEditorListeners.ShapeDrawModeActionListener;
import ShapeWidgetComponents.ShapeCreator.DrawMode;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetUtility.WidgetBuildController;

public class ShapeCreatorToolBarPanel extends JPanel implements PostWidgetBuildProcessing
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
	
	public void buildWidgets()
	{
		shapeCreator = (ShapeCreator) WidgetBuildController.getInstance().findRefByName("ShapeCreator").getInstance();
		
		directionsLabel = new JLabel();
//		operationLabel = new JLabel(shapeCreator.getOperation().getTitleText());
//		JButton b = new JButton("draw");
//		JButton c = new JButton("clear");
		addShape = new JButton("+ Add");
		saveButton = new JButton("Save");
		openButton = new JButton("Open");
		saveButton.addActionListener(new SaveShapeActionListener(shapeCreator));
		openButton.addActionListener(new OpenShapeActionListener(shapeCreator));
		modeSelections = new JComboBox<ShapeCreator.DrawMode>(DrawMode.values());
		modeSelections.addActionListener(new ShapeDrawModeActionListener(shapeCreator, this));
		colorEditorTop = new ColorEditor();
		colorEditorTop.setComponentValue(Color.black);//TODO
		
//		this.add(directionsLabel);
//		this.add(operationLabel);
		this.add(modeSelections);
		this.add(addShape);
		this.add(saveButton);
		this.add(openButton);
		this.add(colorEditorTop);
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
}
