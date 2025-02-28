package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Properties.LoggingMessages;
import Properties.PathUtility;
import WidgetComponentInterfaces.DependentRedrawableFrame;
import WidgetComponentInterfaces.DependentRedrawableFrameListener;
import WidgetUtility.WidgetBuildController;

public class OpenFileActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private static final String 
		XML_PATH_SUFFIX = "/src/ApplicationBuilder/data/ ",
		XML_FILTER_TITLE = "XML Build File",
		XML_FILTER = "xml";
	
	private String
		xmlPathSuffix = XML_PATH_SUFFIX,
		xmlFilterTitle = XML_FILTER_TITLE,
		xmlFilter = XML_FILTER;
	
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public String getXmlPathSuffix()
	{
		return xmlPathSuffix;
	}
	public void setXmlPathSuffix(String pathSuffix)
	{
		xmlPathSuffix = pathSuffix;
	}
	
	public String getXmlFilterTitle()
	{
		return xmlFilterTitle;
	}
	public void setXmlFilterTitle(String filterTitle)
	{
		xmlFilterTitle = filterTitle;
	}
	
	public String getXmlFilter()
	{
		return xmlFilter;
	}
	public void setXmlFilter(String filter)
	{
		xmlFilter = filter;
	}
	
	public void setDependentRedrawableFrame(DependentRedrawableFrame applicationLayoutEditor)
	{
		this.applicationLayoutEditor = applicationLayoutEditor;
	}
	
	@Override
	public void actionPerformed(ActionEvent e) 
	{
		JFileChooser jfc = new JFileChooser();
		String currentDirectory = PathUtility.getCurrentDirectory();
		File f = new File(currentDirectory + getXmlPathSuffix());
		jfc.setFileFilter(new FileNameExtensionFilter(getXmlFilterTitle(), getXmlFilter()));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showOpenDialog(applicationLayoutEditor);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			LoggingMessages.printOut("number generated: " + WidgetBuildController.getGeneratedNum());
			
			WidgetBuildController.getInstance().destroyEditors();
			WidgetBuildController.getInstance().destroyFrameAndCreatorProperties();
			WidgetBuildController.getInstance().readProperties(chosenFile);
			
			applicationLayoutEditor.rebuildInnerPanels();
		}
	}
}
