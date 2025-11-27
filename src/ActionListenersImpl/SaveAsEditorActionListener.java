package ActionListenersImpl;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import Properties.EditorToXml;
import Properties.PathUtility;
import WidgetComponentInterfaces.DependentRedrawableFrame;
import WidgetComponentInterfaces.DependentRedrawableFrameListener;
import WidgetUtility.WidgetBuildController;

public class SaveAsEditorActionListener implements DependentRedrawableFrameListener, ActionListener
{
	private static final String 
		XML_PATH_SUFFIX = "/Properties/data/ ",
		XML_FILTER_TITLE = "XML Build File",
		XML_FILTER = "xml";

	private String 
		xmlPathSuffix = XML_PATH_SUFFIX,
		xmlFilterTitle = XML_FILTER_TITLE,
		xmlFilter = XML_FILTER;
	private DependentRedrawableFrame applicationLayoutEditor;
	
	public String getXmlPathDefault()
	{
		return xmlPathSuffix;
	}
	public void setXmlPathDefault(String pathSuffix)
	{
		xmlPathSuffix = pathSuffix;
	}
	
	public String getXmlFilterTitle()
	{
		return xmlFilterTitle;
	}
	public void setXmlFilterTitle(String title)
	{
		xmlFilterTitle = title;
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
		performSaveAs(applicationLayoutEditor);
	}
	
	/**
	 * @return save performed.
	 */
	public boolean performSaveAs(DependentRedrawableFrame applicationLayoutEditor)
	{
		if(WidgetBuildController.getInstance().getWidgetCreatorProperties().isEmpty())
			return false;
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		File f = new File(PathUtility.getCurrentDirectory() + getXmlPathDefault());
		jfc.setFileFilter(new FileNameExtensionFilter(getXmlFilterTitle(), getXmlFilter()));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showSaveDialog(applicationLayoutEditor);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			EditorToXml.writeXml(chosenFile.getAbsolutePath(),
					WidgetBuildController.getInstance().getWidgetCreatorProperties());
			
			WidgetBuildController.getInstance().readProperties(chosenFile);
			applicationLayoutEditor.rebuildInnerPanels();
			return true;
		}
		return false;
	}
}
