package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import ObjectTypeConversion.FileSelection;
import Params.ParameterEditor;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class FileSelectionEditor extends JPanel implements ParameterEditor 
{
	private static final long serialVersionUID = 2005L;

	private static final String
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select File";
	
	private JFileChooser 
		jcc;
	private JButton 
		editFileButton;

	public FileSelectionEditor()
	{
		buildWidgets();
		this.setLayout(new GridLayout(0,1));
		this.add(editFileButton);
	}
	
	protected void buildWidgets()
	{
		editFileButton = new JButton();
		
		jcc = new JFileChooser();
		jcc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		getFileButton().addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jcc.setDialogTitle(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
				int choice = jcc.showOpenDialog(null);
				File chosenFile = jcc.getSelectedFile();
				if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
				{
					String replPath = PathUtility.replaceBackslash(chosenFile.getAbsolutePath());
					
					getFileButton().setText(replPath.replaceAll(
							PathUtility.replaceBackslash(PathUtility.getCurrentDirectory()), "") );
					jcc.setSelectedFile(chosenFile);
				}
			}
		});
	}
	
	public JButton getFileButton()
	{
		return this.editFileButton;
	}
	
	protected JFileChooser getFileChooser()
	{
		return jcc;
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
		FileSelection ds = (FileSelection) value;
		LoggingMessages.printOut(ds.getFullPath());
		getFileButton().setText(ds.getRelativePath());
		jcc.setSelectedFile(new File(ds.getFullPath()));
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {getFileButton().getText()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new FileSelection(getFileButton().getText());
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
