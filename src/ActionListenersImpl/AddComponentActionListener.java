package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import Params.XmlToWidgetGenerator;
import Properties.LoggingMessages;
import WidgetComponentDialogs.ComboSelectionDialog;
import WidgetComponentInterfaces.RedrawableFrame;
import WidgetComponentInterfaces.RedrawableFrameListener;
import WidgetExtensionDefs.ExtendedAttributeParam;
import WidgetExtensionDefs.ExtendedMethodArgDef;
import WidgetExtensionInterfaces.ComboListDialogSelectedListener;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetUtility.ComponentSelectorUtility;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetComponent;
import WidgetUtility.WidgetCreatorProperty;

public class AddComponentActionListener implements RedrawableFrameListener, ActionListener, ComboListDialogSelectedListener
{
	private static final String 
		DIALOG_SELECT_COMPONENT_TITLE = "Component Selection",
		DIALOG_SELECT_COMPONENT_LABEL_MESSAGE = "Select Component", 
		DIALOG_SELECT_PARENT_TITLE = "Parent Container Selection",
		DIALOG_SELECT_PARENT_LABEL_MESSAGE = "Select Parent Container", 
		DIALOG_SELECT_CHILD_COMPONENTS_TITLE = "Components Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE = "Select Child Components: ",
		STRIP_PACKAGE_NAME_FROM_CLASS_FILTER = "[a-zA-Z]+[\\.]+";
	private WidgetCreatorProperty wcpBuild;
	
	private RedrawableFrame redrawableFrame;
	
	private String
		dialogSelectComponentTitle = DIALOG_SELECT_COMPONENT_TITLE,
		dialogSelectComponentMessage = DIALOG_SELECT_COMPONENT_LABEL_MESSAGE,
		dialogSelectParentTitle = DIALOG_SELECT_PARENT_TITLE,
		dialogSelectParentMessage = DIALOG_SELECT_PARENT_LABEL_MESSAGE,
		dialogSelectChildComponentsTitle = DIALOG_SELECT_CHILD_COMPONENTS_TITLE,
		dialogSelectChildComponentsMessage = DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE;
	
	public String getDialogSelectComponentTitle()
	{
		return dialogSelectComponentTitle;
	}
	public void setDialogSelectComponentTitle(String title)
	{
		dialogSelectComponentTitle = title;
	}
	
	public String getDialogSelectComponentMessage()
	{
		return dialogSelectComponentMessage;
	}
	public void setDialogSelectComponentMessage(String message)
	{
		dialogSelectComponentMessage = message;
	}
	
	public String getDialogSelectParentTitle()
	{
		return dialogSelectParentTitle;
	}
	public void setDialogSelectParentTitle(String title)
	{
		dialogSelectParentTitle = title;
	}
	
	public String getDialogSelectParentMessage()
	{
		return dialogSelectParentMessage;
	}
	public void setDialogSelectParentMessage(String message)
	{
		dialogSelectParentMessage = message;
	}
	
	public String getDialogSelectChildComponentsTitle()
	{
		return dialogSelectChildComponentsTitle;
	}
	public void setDialogSelectChildComponentsTitle(String title)
	{
		dialogSelectChildComponentsTitle = title;
	}
	
	public String getDialogSelectChildComponentsMessage()
	{
		return dialogSelectChildComponentsMessage;
	}
	public void setDialogSelectChildComponentsMessage(String message)
	{
		dialogSelectChildComponentsMessage = message;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		List<String> componentOptions = ComponentSelectorUtility.generateComboSelectionOptions();
		String opt = (String) JOptionPane.showInputDialog(
				redrawableFrame,
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
				redrawableFrame,
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
				this, redrawableFrame);
		}
		else//new JFrame.
 		{
			ArrayList<XmlToWidgetGenerator> xmlG = WidgetAttributes.setAttribute(
 					wcpBuild.getClassType(), 
 					ExtendedAttributeParam.getMethodDefinition(
 							ExtendedLayoutApplyParent.class, 
 							ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()
 					)
 			);
 			wcpBuild.addXmlToWidgetGenerator(xmlG);
 
 			WidgetBuildController.getInstance().addWidgetCreatorProperty(wcpBuild, true);
 			redrawableFrame.rebuildInnerPanels();
 		}
	
	}

	@Override
	public void selectionChosen(List<String> chosenSelection) 
	{
		if(chosenSelection != null)
		{
			ComponentSelectorUtility.setParentRefIdOnComponents(chosenSelection, wcpBuild.getRefWithID());
		}
		
		ArrayList<XmlToWidgetGenerator> xmlG = WidgetAttributes.setAttribute(
				wcpBuild.getClassType(), 
				ExtendedAttributeParam.getMethodDefinition(
						ExtendedLayoutApplyParent.class, 
						ExtendedMethodArgDef.ExtendedAttributeStringParam.getMethodArgDef()
				)
		);
		wcpBuild.addXmlToWidgetGenerator(xmlG);

		WidgetBuildController.getInstance().addWidgetCreatorProperty(wcpBuild, true);
		redrawableFrame.rebuildInnerPanels();
	}
	@Override
	public void setRedrawableFrame(RedrawableFrame redrawableFrame) 
	{
		this.redrawableFrame = redrawableFrame;
	}
}