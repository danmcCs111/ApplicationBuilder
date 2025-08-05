package WidgetComponents;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JPanel;

import Actions.ScheduledCommand;
import Editors.ScheduledCommandEditor;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetExtensions.OpenActionExtension;
import WidgetExtensions.SaveActionExtension;
public class ScheduledCommandList extends JPanel implements PostWidgetBuildProcessing, SaveActionExtension, OpenActionExtension
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ScheduledCommandEditor> scheduledCommandEditors = new ArrayList<ScheduledCommandEditor>();
	private ScheduledCommandImportExport scie = new ScheduledCommandImportExport();
	private ArrayList<ScheduledCommand> scs; 
	private ScheduledCommandEditor blankEditor;
	private ActionListener blankListener;
	
	public ScheduledCommandList()
	{
		blankListener = new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				if(!ScheduledCommandList.this.checkBlankEditorIsEmpty())
				{
					addBlankEditor();
				}
			}
		};
	}
	
	public void buildWidgets()
	{
		addBlankEditor();
	}
	
	private void buildWidgets(ArrayList<ScheduledCommand> scs)
	{
		for(ScheduledCommand sc : scs)
		{
			ScheduledCommandEditor scEditor = new ScheduledCommandEditor();
			scEditor.setComponentValue(sc);
			scheduledCommandEditors.add(scEditor);
			this.add(scEditor);
		}
		this.getRootPane().getParent().validate();
	}
	
	private void addBlankEditor()
	{
		if(blankEditor != null)
		{
			blankEditor.removeActionListener(blankListener);
		}
		blankEditor = new ScheduledCommandEditor();
		scheduledCommandEditors.add(blankEditor);
		blankEditor.addActionListener(blankListener);
		this.add(blankEditor);
	}
	
	public boolean checkBlankEditorIsEmpty()
	{
		return (blankEditor.getComponentValueObj() != null);
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.getRootPane().getParent().validate();
	}

	@Override
	public void performSave() {
		String xml = scie.toXmlFromEditorList(scheduledCommandEditors);
		scie.performSave(this, xml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void performOpen() 
	{
		scs = (ArrayList<ScheduledCommand>) scie.openXml(
				this, 
				scie.getFileTypeTitle(), 
				scie.getFileTypeFilter(), 
				scie.getDefaultDirectoryRelative()
		);
		buildWidgets(scs);
	}

}
