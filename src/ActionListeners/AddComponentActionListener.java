package ActionListeners;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

import ApplicationBuilder.ComponentSelector;
import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.LoggingMessages;
import Params.XmlToWidgetGenerator;
import WidgetExtensions.ExtendedAttributeStringParam;
import WidgetExtensions.ExtendedLayoutApplyParent;
import WidgetUtility.WidgetAttributes;
import WidgetUtility.WidgetBuildController;
import WidgetUtility.WidgetComponent;
import WidgetUtility.WidgetCreatorProperty;

public class AddComponentActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private static final String 
		DIALOG_SELECT_COMPONENT_LABEL_MESSAGE = "Select Component", 
		DIALOG_SELECT_COMPONENT_TITLE = "Component Selection",
		DIALOG_SELECT_PARENT_LABEL_MESSAGE = "Select Parent Container", 
		DIALOG_SELECT_PARENT_TITLE = "Parent Container Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_TITLE = "Components Selection",
		DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE = "Select Child Components: ",
		ADD_BUTTON_TEXT = "Add",
		CLOSE_BUTTON_TEXT = "Close",
		STRIP_PACKAGE_NAME_FROM_CLASS_FILTER = "[a-zA-Z]+[\\.]+";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(300,150);
	
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
				ComponentSelector.getParentContainerOptions().toArray(), "");
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
		WidgetCreatorProperty wcp = new WidgetCreatorProperty(optFiltered+WidgetComponent.ID_SPLIT+WidgetComponent.nextCountId(), 
				settings, optP.equals("")?null:optPFiltered);
		
		if(!optP.equals(""))
		{
			JDialog d = new JDialog();
			JLabel messageLabel = new JLabel();
			messageLabel.setText(DIALOG_SELECT_CHILD_COMPONENTS_MESSAGE);
			d.setTitle(DIALOG_SELECT_CHILD_COMPONENTS_TITLE);
			d.setLocation((int)applicationLayoutEditor.getBounds().getCenterX() - (MIN_DIMENSION_DIALOG.width/2), 
					(int)applicationLayoutEditor.getRootPane().getBounds().getCenterY() - (MIN_DIMENSION_DIALOG.height /2));
			d.setMinimumSize(MIN_DIMENSION_DIALOG);
			JList<String> componentMethods = new JList<String>();
			List<String> components = ComponentSelector.getComponentsFromParent(optPFiltered);
			
			componentMethods.setListData(components.toArray(new String[components.size()]));
			d.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
			d.addWindowListener(new WindowAdapter() {
				@Override
				public void windowClosed(WindowEvent e) {
					applicationLayoutEditor.rebuildInnerPanels();
				}
			});
			
			JButton save = new JButton(ADD_BUTTON_TEXT);
			save.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					List<String> selected = componentMethods.getSelectedValuesList();
					if(selected != null && !selected.isEmpty())
					{
						ComponentSelector.setParentRefIdOnComponents(selected, wcp.getRefWithID());
					}
					d.dispose();
				}
			});
			JButton cancel = new JButton(CLOSE_BUTTON_TEXT);
			cancel.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					applicationLayoutEditor.rebuildInnerPanels();
					d.dispose();
				}
			});
			
			d.setLayout(new BorderLayout());
			d.add(messageLabel, BorderLayout.NORTH);
			d.add(componentMethods, BorderLayout.CENTER);
			JPanel southPane = new JPanel(new BorderLayout());
			JPanel eastPane = new JPanel(new GridLayout(1,2));
			eastPane.add(save);
			eastPane.add(cancel);
			southPane.add(eastPane, BorderLayout.EAST);
			d.add(southPane, BorderLayout.SOUTH);
			d.setVisible(true);
			d.pack();
			
			XmlToWidgetGenerator xmlG = WidgetAttributes.setAttribute(wcp.getClassType(), 
					ExtendedAttributeStringParam.getMethodDefinition(ExtendedLayoutApplyParent.class));
			wcp.addXmlToWidgetGenerator(xmlG);
		}
		
		WidgetBuildController.getInstance().addWidgetCreatorProperty(wcp, true);
		applicationLayoutEditor.rebuildInnerPanels();
		
	}
	
}
