package WindowListeners;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.util.HashMap;

import javax.swing.AbstractButton;

import ActionListenersImpl.LaunchUrlActionListener;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponents.JButtonLengthLimited;
import WidgetUtility.WidgetBuildController;

public class ProcessExecutorWindowListener extends WindowAdapter
{
	private static String
		FILE_NAME = "",
		PROPERTIES_LOCATION = "./Properties/lastRunningProcess/";
	
	@Override
	public void windowOpened(WindowEvent e)
	{
		FILE_NAME = WidgetBuildController.getInstance().getFrame().getTitle() + ".txt";
		
		FileSelection fs = new FileSelection(PROPERTIES_LOCATION + FILE_NAME);
		HashMap<String, String> props = PathUtility.readProperties(fs.getFullPath(), "=");
		if(!props.isEmpty())
		{
			long procId = Long.parseLong(props.get("processID"));
			JButtonLengthLimited jbll = createVirtualButton(
					props.get("sourceButtonText"),
					props.get("sourceButtonFull"),
					props.get("sourceName"),
					props.get("highlightButtonText"),
					props.get("highlightButtonFull"),
					props.get("highlightButtonName")
					);
			boolean isSet = LaunchUrlActionListener.setProcess(procId, -1);
			if(isSet)
			{
				LaunchUrlActionListener.setLastButtonOrigin(jbll);
				LaunchUrlActionListener.performHighlight(jbll);
			}
		}
	}
	
	@Override
	public void windowClosing(WindowEvent e) 
	{
		FileSelection fs = new FileSelection(PROPERTIES_LOCATION + FILE_NAME);
		File f = new File(fs.getFullPath());
		
		AbstractButton ab = LaunchUrlActionListener.getLastButtonOrigin();
		LoggingMessages.printOut("close: " + ab);
		if(ab instanceof JButtonLengthLimited)
		{
			long procId = LaunchUrlActionListener.getProcessId();
			LoggingMessages.printOut("processId: " + procId);
			if(procId != -1)
			{
				JButtonLengthLimited jbll = (JButtonLengthLimited) ab;
				JButtonLengthLimited highlightJbll = (JButtonLengthLimited) ((JButtonLengthLimited) ab).getHighlightButton();
				//write to file.
				String [][] props = new String [][] {
					{"processID", procId+""},
					{"sourceButtonText", jbll.getText()},
					{"sourceButtonFull", jbll.getFullLengthText()},
					{"sourceName", jbll.getName()},
					{"highlightButtonText", highlightJbll.getText()},
					{"highlightButtonFull", highlightJbll.getFullLengthText()},
					{"highlightButtonName", highlightJbll.getName()}
				};
				PathUtility.writeProperties(fs.getFullPath(), props);
			}
		}
		else
		{
			//write empty.
			PathUtility.writeStringToFile(f, "");
		}
		
	}
	
	public JButtonLengthLimited createVirtualButton(
			String sourceButtonText, 
			String sourceButtonFull,
			String sourceName,
			String highlightButtonText,
			String highlightButtonFull,
			String highlightButtonName)
	{
		JButtonLengthLimited 
			virtualButton = new JButtonLengthLimited(),
			virtualButtonHighlight = new JButtonLengthLimited();
		virtualButton.setHighlightButton(virtualButtonHighlight);
		virtualButton.addActionListener(new LaunchUrlActionListener());
		virtualButtonHighlight.addActionListener(new LaunchUrlActionListener());
		//Referenced -> FileListOptionGenerator
		virtualButton.setText(sourceButtonText);
		virtualButton.setFullText(sourceButtonFull);
		virtualButton.setName(sourceName);
		virtualButtonHighlight.setText(highlightButtonText);
		virtualButtonHighlight.setFullText(highlightButtonFull);
		virtualButtonHighlight.setName(highlightButtonName);
		
		return virtualButton;
	}
	
}
