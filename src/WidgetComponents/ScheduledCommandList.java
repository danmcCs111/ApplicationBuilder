package WidgetComponents;

import java.util.ArrayList;

import javax.swing.JPanel;

import Actions.ScheduledCommand;
import Editors.ScheduledCommandEditor;
import WidgetComponentInterfaces.OpenActionSubscriber;
import WidgetComponentInterfaces.PostWidgetBuildProcessing;
import WidgetComponentInterfaces.SaveActionSubscriber;

public class ScheduledCommandList extends JPanel implements PostWidgetBuildProcessing, SaveActionSubscriber, OpenActionSubscriber
{
	private static final long serialVersionUID = 1L;
	
	private ArrayList<ScheduledCommandEditor> scheduledCommandEditors = new ArrayList<ScheduledCommandEditor>();
	private ScheduledCommandImportExport scie = new ScheduledCommandImportExport();
	private ArrayList<ScheduledCommand> scs; 
	
	public ScheduledCommandList()
	{
		
	}
	
	public void buildWidgets()
	{
		ScheduledCommandEditor blankEditor = new ScheduledCommandEditor();
		scheduledCommandEditors.add(blankEditor);
		this.add(blankEditor);
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
	}

	@Override
	public void postExecute() 
	{
		buildWidgets();
		this.validate();
	}

	@Override
	public void save() 
	{
		String xml = "";
		for(ScheduledCommandEditor sce : scheduledCommandEditors)
		{
			xml += sce.getComponentXMLOutput();
		}
		scie.performSave(this, xml);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void open() 
	{
		scs = (ArrayList<ScheduledCommand>) scie.openXml(this, scie.getFileTypeTitle(), scie.getFileTypeFilter(), scie.getDefaultDirectoryRelative());
		buildWidgets(scs);
	}

}
