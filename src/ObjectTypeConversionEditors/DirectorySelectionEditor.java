package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import ObjectTypeConversion.DirectorySelection;
import Params.ParameterEditor;
import Properties.PathUtility;

public class DirectorySelectionEditor extends JButton implements ParameterEditor
{
	private static final long serialVersionUID = 2002L;

	private static final String
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select Directory",
		END_DIRECTORY_SUFFIX = "/ ";
	protected JFileChooser jcc;

	public DirectorySelectionEditor()
	{
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		jcc = new JFileChooser();
		jcc.setDialogType(JFileChooser.DIRECTORIES_ONLY);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jcc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				jcc.setDialogTitle(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
				int choice = jcc.showOpenDialog(null);
				File chosenFile = jcc.getSelectedFile();
				if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
				{
					String replPath = PathUtility.replaceBackslash(chosenFile.getAbsolutePath());
					
					DirectorySelection ds = new DirectorySelection(
							replPath.replaceAll(PathUtility.replaceBackslash(PathUtility.getCurrentDirectory()), "") + 
							END_DIRECTORY_SUFFIX);
					DirectorySelectionEditor.this.setText(ds.getRelativePath());
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
		if(value instanceof String)
			return;
		DirectorySelection ds = (DirectorySelection) value;
		this.setText(ds.getRelativePath());
		jcc.setSelectedFile(new File(ds.getFullPath()));
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {DirectorySelectionEditor.this.getText()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new DirectorySelection(DirectorySelectionEditor.this.getText());
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getParameterDefintionString() 
	{
		return DirectorySelection.class.getName();
	}

}
