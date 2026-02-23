package Properties;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

import ObjectTypeConversion.FileSelection;

public class SaveAsDialog 
{
	private String 
		title,
		fileFilter,
		defaultPathRelative;
	private Component parent;
	
	public SaveAsDialog(Component parent, String title, String fileFilter, String defaultPathRelative)
	{
		this.parent = parent;
		this.title = title;
		this.fileFilter = fileFilter;
		this.defaultPathRelative = defaultPathRelative;
	}
	
	public File getSelectedDirectory()
	{
		JFileChooser jfc = new JFileChooser();
		jfc.setDialogType(JFileChooser.SAVE_DIALOG);
		File f = new File(new FileSelection(defaultPathRelative).getFullPath());
		LoggingMessages.printOut(f.getAbsolutePath());
		jfc.setFileFilter(new FileNameExtensionFilter(title, fileFilter));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showSaveDialog(parent);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			return chosenFile;
			
		}
		return null;
	}
}
