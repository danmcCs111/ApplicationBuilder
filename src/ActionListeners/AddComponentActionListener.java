package ActionListeners;

import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Params.XmlToWidgetGenerator;
import Properties.LoggingMessages;
import WidgetComponents.ComboListDialogSelectedListener;
import WidgetComponents.ComboSelectionDialog;
import WidgetComponents.ComponentSelectorUtility;
import WidgetComponents.DependentRedrawableFrame;
import WidgetComponents.DependentRedrawableFrameListener;
import WidgetComponents.DialogParentReferenceContainer;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetComponent;
import WidgetUtility.WidgetCreatorProperty;

public class AddComponentActionListener implements DependentRedrawableFrameListener, ActionListener, ComboListDialogSelectedListener, DialogParentReferenceContainer
{
	private static final String 
		DIALOG_SELECT_COMPONENT_LABEL_MESSAGE = "Select Component", 
		DIALOG_SELECT_COMPONENT_TITLE = "Component Selection",
		DIALOG_SELECT_PARENT_LABEL_MESSAGE = "Select Parent Container", 
		DIALOG_SELECT_PARENT_TITLE = "Parent Container Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_TITLE = "Components Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE = "Select Child Components: ",
		STRIP_PACKAGE_NAME_FROM_CLASS_FILTER = "[a-zA-Z]+[\\.]+";
	private WidgetCreatorProperty wcpBuild;
	
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		List<String> componentOptions = ComponentSelectorUtility.generateComboSelectionOptions();
		String opt = (String) JOptionPane.showInputDialog(
				applicationLayoutEditor,
				DIALOG_SELECT_COMPONENT_LABEL_MESSAGE, DIALOG_SELECT_COMPONENT_TITLE, 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				componentOptions.toArray(), "");
		if(opt == null)
		{
			LoggingMessages.printOut(this.getClass().getName() + " Cancelled.");
			return;//cancel performed.
		}
		
		String optP = (String) JOptionPane.showInputDialog(
				applicationLayoutEditor,
				DIALOG_SELECT_PARENT_LABEL_MESSAGE, DIALOG_SELECT_PARENT_TITLE, 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				ComponentSelectorUtility.getParentContainerOptions().toArray(), "");
		if(optP == null)
		{
			LoggingMessages.printOut(this.getClass().getName() + " " + opt + " Cancelled.");
			return;//cancel performed.
		}
		
		LoggingMessages.printOut(this.getClass().getName() + " " + opt + " <-> Make Parent: " + optP);
		ArrayList<String> settings = new ArrayList<String>();
		String 
			optFiltered = opt.replaceAll(STRIP_PACKAGE_NAME_FROM_CLASS_FILTER, ""),
			optPFiltered = optP.replaceAll(STRIP_PACKAGE_NAME_FROM_CLASS_FILTER, "");
		LoggingMessages.printOut("Filtered: " + optFiltered + " non-Filtered: " + opt);
		
		if(!optP.equals(""))
			settings.add("extendedLayoutApplyParent=\"\"");
		wcpBuild = new WidgetCreatorProperty(optFiltered+WidgetComponent.ID_SPLIT+WidgetComponent.nextCountId(), 
				settings, optP.equals("")?null:optPFiltered);
		
		if(!optP.equals(""))
		{
			ComboSelectionDialog csd = new ComboSelectionDialog();
			List<String> components = ComponentSelectorUtility.getComponentsFromParent(optPFiltered);
			csd.buildAndShow(components, 
				DIALOG_SELECT_CHILD_COMPONENTS_TITLE,
				DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE,
				this, this);
		}
	
	}

	@Override
	public Point getContainerCenterLocationPoint() 
	{
		return new Point((int)applicationLayoutEditor.getBounds().getCenterX(), 
				(int)applicationLayoutEditor.getBounds().getCenterY());
	}

	@Override
	public void selectionChosen(List<String> chosenSelection) 
	{
		if(chosenSelection != null)
		{
			ComponentSelectorUtility.setParentRefIdOnComponents(chosenSelection, wcpBuild.getRefWithID());
		}
		
		XmlToWidgetGenerator xmlG = WidgetAttributes.setAttribute(wcpBuild.getClassType(), 
				ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class));
		wcpBuild.addXmlToWidgetGenerator(xmlG);

		WidgetBuildController.getInstance().addWidgetCreatorProperty(wcpBuild, true);
		applicationLayoutEditor.rebuildInnerPanels();
	}
}