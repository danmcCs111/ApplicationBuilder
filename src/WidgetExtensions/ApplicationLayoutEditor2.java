package WidgetExtensions;

import java.awt.BorderLayout;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JPanel;

import ActionListeners.AddComponentActionListener;
import ActionListeners.GenerateActionListener;
import ActionListeners.OpenParameterEditorActionListener;
import ActionListeners.SaveEditorActionListener;
import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.LoggingMessages;

public class ApplicationLayoutEditor2 extends DependentRedrawableFrame
{
	private static final long serialVersionUID = 1897L;
	
	private static final String 
		EDITOR_SAVE_BUTTON_TEXT = "Save",
		EDITOR_GENERATE_BUTTON_TEXT = "Generate",
		EDITOR_ADD_PROPERTY_BUTTON_TEXT = "Add Component Property",
		EDITOR_ADD_COMPONENT_BUTTON_TEXT = "Add Component";
	
	private JButton openParameterButton;
	private XmlToEditor xe;
	private OpenParameterEditorActionListener opListener;
	
	public ApplicationLayoutEditor2()
	{
		LoggingMessages.printOut("Build.");
		buildEditorButtons();
	}
	
	public void buildEditorButtons()
	{
		JPanel p = new JPanel(new GridLayout(0,2));
		
		openParameterButton = new JButton(EDITOR_ADD_PROPERTY_BUTTON_TEXT);
		JButton generateButton = new JButton(EDITOR_GENERATE_BUTTON_TEXT);
		generateButton.addActionListener(new GenerateActionListener());
		
		p.add(openParameterButton);
		p.add(generateButton);
		
		JButton addComponent = new JButton(EDITOR_ADD_COMPONENT_BUTTON_TEXT);
		AddComponentActionListener acal = new AddComponentActionListener();
		acal.setDependentRedrawableFrame(this);
		addComponent.addActionListener(acal);
		JButton saveButton = new JButton(EDITOR_SAVE_BUTTON_TEXT);
		SaveEditorActionListener seal = new SaveEditorActionListener();
		seal.setDependentRedrawableFrame(this);
		saveButton.addActionListener(seal);
		
		p.add(addComponent);
		p.add(saveButton);
		
		this.add(p, BorderLayout.SOUTH);
		
//		rebuildInnerPanels();
	}
	
	@Override
	public void updateDependentWindow()
	{
		if(opListener.getBuilderWindow() != null)
		{
			opListener.getBuilderWindow().clearInnerPanels();
			opListener.getBuilderWindow().rebuildInnerPanels();
		}
	}

	@Override
	public void clearInnerPanels() 
	{
		xe.destroyPanel();
	}

	@Override
	public void rebuildInnerPanels() 
	{
		if(xe == null)
		{
			xe = new XmlToEditor();
			xe.setDependentRedrawableFrame(this);
//			xe.rebuildPanel();
		}
		else if(xe != null)
		{
			xe.rebuildPanel();
		}
		
		if(opListener != null)
		{
			openParameterButton.removeActionListener(opListener);
		}
		opListener = new OpenParameterEditorActionListener(xe, ApplicationLayoutEditor2.this);
		openParameterButton.addActionListener(opListener);
	}
	
}
