package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JOptionPane;

import ApplicationBuilder.ComponentSelector;
import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetCreatorProperty;

public class AddComponentActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private static final String 
		DIALOG_SELECT_COMPONENT_LABEL_MESSAGE = "Select Component", 
		DIALOG_SELECT_COMPONENT_TITLE = "Component Selection",
		DIALOG_SELECT_PARENT_LABEL_MESSAGE = "Select Parent Container", 
		DIALOG_SELECT_PARENT_TITLE = "Parent Container Selection";
	
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		List<String> componentOptions = ComponentSelector.generateComboSelectionOptions();
		String opt = (String) JOptionPane.showInputDialog(
				applicationLayoutEditor,
				DIALOG_SELECT_COMPONENT_LABEL_MESSAGE, DIALOG_SELECT_COMPONENT_TITLE, 
				JOptionPane.PLAIN_MESSAGE, 
				null, 
				componentOptions.toArray(), "");
		if(opt!=null)
		{
			String optP = (String) JOptionPane.showInputDialog(
					applicationLayoutEditor,
					DIALOG_SELECT_PARENT_LABEL_MESSAGE, DIALOG_SELECT_PARENT_TITLE, 
					JOptionPane.PLAIN_MESSAGE, 
					null, 
					ComponentSelector.getParentContainerOptions().toArray(), "");
			LoggingMessages.printOut("Add Component: " + opt + " <-> Make Parent: " + optP);
			ArrayList<String> settings = new ArrayList<String>();
			String optFiltered = opt.replaceAll("[a-zA-Z]+[\\.]+", "");
			LoggingMessages.printOut("Filtered: " + optFiltered + " non-Filtered: " + opt);
			
			if(!optP.equals(""))
				settings.add("extendedLayoutApplyParent=\"\"");
			WidgetCreatorProperty wcp = new WidgetCreatorProperty(optFiltered, settings, optP.equals("")?null:optP);
			if(!optP.equals(""))
			{
				XmlToWidgetGenerator xmlG = WidgetAttributes.setAttribute(wcp.getClassType(), 
						ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class));
				wcp.addXmlToWidgetGenerator(xmlG);
			}
			
			WidgetBuildController.getInstance().addWidgetCreatorProperty(wcp, true);
			applicationLayoutEditor.rebuildInnerPanels();
		}
		else LoggingMessages.printOut("cancelled");
	}
}
