package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import ApplicationBuilder.BuilderWindow;

public class OpenParameterEditorActionListener implements ActionListener 
{
	private BuilderWindow builderWindow; 
	
	public OpenParameterEditorActionListener()
	{
		
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		if(builderWindow == null )
		{
			builderWindow = new BuilderWindow();
			builderWindow.setVisible(true);
		}
		else if (builderWindow.isVisible() == false)
		{
			builderWindow.setVisible(true);
		}
	}

}
