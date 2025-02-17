package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Properties.PathUtility;
import WidgetComponents.DependentRedrawableFrame;
import WidgetComponents.DependentRedrawableFrameListener;
import WidgetComponents.LoggingMessages;
import WidgetUtility.WidgetBuildController;

public class OpenFileActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private static final String 
		XML_PATH_SUFFIX = "/src/ApplicationBuilder/data/ ",
		XML_FILTER_TITLE = "XML Build File",
		XML_FILTER = "xml";
	
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JFileChooser jfc = new JFileChooser();
		String currentDirectory = PathUtility.getCurrentDirectory();
		File f = new File(currentDirectory + XML_PATH_SUFFIX);
		jfc.setFileFilter(new FileNameExtensionFilter(XML_FILTER_TITLE, XML_FILTER));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showOpenDialog(applicationLayoutEditor);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			LoggingMessages.printOut("number generated: " + WidgetBuildController.getGeneratedNum());
			
			WidgetBuildController.getInstance().destroyFrameAndCreatorProperties();
			WidgetBuildController.getInstance().readProperties(chosenFile);
			
			applicationLayoutEditor.rebuildInnerPanels();
		}
	}
}
