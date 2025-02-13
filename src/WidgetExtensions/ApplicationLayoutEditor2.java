package WidgetExtensions;

import javax.swing.JButton;

import ActionListeners.OpenParameterEditorActionListener;
import ApplicationBuilder.DependentRedrawableFrame;

public class ApplicationLayoutEditor2 extends DependentRedrawableFrame
{
	private static final long serialVersionUID = 1897L;
	
	private JButton openParameterButton;
	private XmlToEditor xe;
	private OpenParameterEditorActionListener opListener;
	
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
			xe.rebuildPanel();
		}
		else if(xe != null)
		{
			xe.rebuildPanel();
		}
		
//		if(opListener != null)
//		{
//			openParameterButton.removeActionListener(opListener);
//		}
//		opListener = new OpenParameterEditorActionListener(xe, ApplicationLayoutEditor2.this);
//		openParameterButton.addActionListener(opListener);
	}
	
}
