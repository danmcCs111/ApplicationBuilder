package WidgetComponents;

import WidgetUtility.WidgetBuildController;

public class ApplicationLayoutEditor extends DependentRedrawableFrame implements PostWidgetBuildProcessing
{
	private static final long serialVersionUID = 1897L;
	
	private BuilderWindow builderWindow;
	private XmlToEditor xe;
	private boolean doPostExecute = true;
	
	public XmlToEditor getXmlToEditor()
	{
		return xe;
	}
	
	public void setBuilderWindow(BuilderWindow builderWindow)
	{
		this.builderWindow = builderWindow;
	}
	
	@Override
	public void updateDependentWindow()
	{
		if(builderWindow != null)
		{
			builderWindow.clearInnerPanels();
			builderWindow.rebuildInnerPanels();
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
			xe.rebuildPanel();
		}
		else if(xe != null)
		{
			xe.rebuildPanel();
		}
	}

	@Override
	public void execute() 
	{
		if(doPostExecute)
		{
			//advance to remove focus from layout editor.
			WidgetBuildController.getInstance().newWidgetBuild();
		}
	}
	
	/**
	 * set false to unlock generation of this editor itself, but may cause buggy behavior.
	 **/
	@Override
	public void setDoPostExecute(boolean doPostExecute) 
	{
		this.doPostExecute = doPostExecute;
	}
	
}
