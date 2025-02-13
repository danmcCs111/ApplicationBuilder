package WidgetExtensions;

import ApplicationBuilder.BuilderWindow;
import ApplicationBuilder.DependentRedrawableFrame;

public class ApplicationLayoutEditor2 extends DependentRedrawableFrame
{
	private static final long serialVersionUID = 1897L;
	
	private BuilderWindow builderWindow;
	private XmlToEditor xe;
	
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
	
}
