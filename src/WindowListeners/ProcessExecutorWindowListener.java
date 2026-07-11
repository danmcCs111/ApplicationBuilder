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

public class ProcessExecutorWindowListener extends WindowAdapter
{
	private static String 
		PROPERTIES_LOCATION = "./Properties/lastRunningProcess/process.txt";
	
	@Override
	public void windowOpened(WindowEvent e)
	{
		LaunchUrlActionListener.bootCheckRunningProcess();
		
		FileSelection fs = new FileSelection(PROPERTIES_LOCATION);
		HashMap<String, String> props = PathUtility.readProperties(fs.getFullPath(), "=");
		if(!props.isEmpty())
		{
			long procId = Long.parseLong(props.get("processID"));
			JButtonLengthLimited jbll = createVirtualButton(
					props.get("sourceButtonText"),
					props.get("sourceButtonFull"),
					props.get("sourceName"),
					props.get("highlightButtonText"),
					props.get("highlightButtonFull")
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
		FileSelection fs = new FileSelection(PROPERTIES_LOCATION);
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
					{"highlightButtonFull", highlightJbll.getFullLengthText()}
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
			String highlightButtonFull)
	{
		JButtonLengthLimited 
			virtualButton = new JButtonLengthLimited(),
			virtualButtonHighlight = new JButtonLengthLimited();
		virtualButton.setHighlightButton(virtualButtonHighlight);
		virtualButton.addActionListener(new LaunchUrlActionListener());
		//Referenced -> FileListOptionGenerator
		virtualButton.setText(sourceButtonText);
		virtualButton.setFullText(sourceButtonFull);
		virtualButton.setName(sourceName);
		virtualButtonHighlight.setText(highlightButtonText);
		virtualButtonHighlight.setFullText(highlightButtonFull);
		
		return virtualButton;
	}
	
}
