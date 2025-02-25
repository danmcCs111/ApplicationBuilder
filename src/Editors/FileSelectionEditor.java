package Editors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import ClassDefinitions.FileSelection;
import Params.ParameterEditor;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class FileSelectionEditor extends JButton implements ParameterEditor 
{
	private static final long serialVersionUID = 2005L;

	private static final String
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select File";
	private JFileChooser jcc;

	public FileSelectionEditor()
	{
		jcc = new JFileChooser();
		jcc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jcc.setDialogTitle(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
				int choice = jcc.showOpenDialog(null);
				File chosenFile = jcc.getSelectedFile();
				if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
				{
					String replPath = PathUtility.replaceBackslash(chosenFile.getAbsolutePath());
					
					FileSelectionEditor.this.setText(replPath.replaceAll(
							PathUtility.replaceBackslash(PathUtility.getCurrentDirectory()), "") );
					jcc.setSelectedFile(chosenFile);
				}
			}
		});
	}
	
	@Override
	public void destroy() {
		// TODO Auto-generated method stub
	}

	@Override
	public Component getComponentEditor() 
	{
		return this;
	}

	@Override
	public void setComponentValue(Object value) 
	{
		FileSelection ds = (FileSelection) value;
		LoggingMessages.printOut(ds.getFullPath());
		this.setText(ds.getRelativePath());
		jcc.setSelectedFile(new File(ds.getFullPath()));
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {FileSelectionEditor.this.getText()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new FileSelection(FileSelectionEditor.this.getText());
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return FileSelection.class.getName();
	}

}
