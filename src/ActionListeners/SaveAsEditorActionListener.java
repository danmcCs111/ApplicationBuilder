package ActionListeners;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ApplicationBuilder.DependentRedrawableFrame;
import ApplicationBuilder.DependentRedrawableFrameListener;
import ApplicationBuilder.EditorToXml;
import Properties.PathUtility;
import WidgetUtility.WidgetBuildController;

public class SaveAsEditorActionListener implements DependentRedrawableFrameListener, ActionListener
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
//		JFileChooser jfc = new JFileChooser();
//		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
//		File f = new File(PathUtility.getCurrentDirectory() + XML_PATH_SUFFIX);
//		jfc.setFileFilter(new FileNameExtensionFilter(XML_FILTER_TITLE, XML_FILTER));
//		jfc.setSelectedFile(f);
//		
//		int choice = jfc.showSaveDialog(applicationLayoutEditor);
//		File chosenFile = jfc.getSelectedFile();
//		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
//		{
//			EditorToXml.writeXml(chosenFile.getAbsolutePath(),
//					WidgetBuildController.getInstance().getWidgetCreatorProperties());
//			
//			WidgetBuildController.getInstance().readProperties(chosenFile);
//			applicationLayoutEditor.rebuildInnerPanels();
//		}
		SaveAsEditorActionListener.performSaveAs(applicationLayoutEditor);
	}
	
	/**
	 * 
	 * @return save performed.
	 */
	public static boolean performSaveAs(DependentRedrawableFrame applicationLayoutEditor)
	{
		if(WidgetBuildController.getInstance().getWidgetCreatorProperties().isEmpty())
			return false;
		
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		File f = new File(PathUtility.getCurrentDirectory() + XML_PATH_SUFFIX);
		jfc.setFileFilter(new FileNameExtensionFilter(XML_FILTER_TITLE, XML_FILTER));
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
