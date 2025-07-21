package Properties;

import java.awt.Component;
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OpenDialog 
{
	public File performOpen(Component parent, String title, String fileFilter, String defaultPathRelative)
	{
		JFileChooser jfc = new JFileChooser();
		String currentDirectory = PathUtility.getCurrentDirectory();
		File f = new File(currentDirectory + defaultPathRelative);
		jfc.setFileFilter(new FileNameExtensionFilter(title, fileFilter));
		jfc.setSelectedFile(f);
		
		int choice = jfc.showOpenDialog(parent);
		File chosenFile = jfc.getSelectedFile();
		if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
		{
			return chosenFile;
		}
		return null;
	}
}
