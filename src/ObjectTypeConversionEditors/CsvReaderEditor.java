package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JFileChooser;

import ObjectTypeConversion.CsvReader;
import Params.ParameterEditor;
import Properties.PathUtility;

public class CsvReaderEditor extends JButton implements ParameterEditor
{

	private static final long serialVersionUID = 1L;
	private static final String
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select CSV File";
	protected JFileChooser jcc;
	
	public CsvReaderEditor()
	{
		jcc = new JFileChooser();
		jcc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		this.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				jcc.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
				jcc.setDialogTitle(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
				int choice = jcc.showOpenDialog(null);
				File chosenFile = jcc.getSelectedFile();
				if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
				{
					String replPath = PathUtility.replaceBackslash(chosenFile.getAbsolutePath());
					
					CsvReaderEditor.this.setText(replPath.replaceAll(
							PathUtility.replaceBackslash(PathUtility.getCurrentDirectory()), ""));
					jcc.setSelectedFile(chosenFile);
				}
			}
		});
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
		CsvReader cr = (CsvReader) value;
		this.setText(cr.getAbsolutePath());
		jcc.setSelectedFile(new File(cr.getAbsolutePath()));
	}
	
	@Override
	public String[] getComponentValue() 
	{
		return new String[] {CsvReaderEditor.this.getText()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new CsvReader(CsvReaderEditor.this.getText());
	}
	
	@Override
	public String getParameterDefintionString() 
	{
		return CsvReader.class.getName();
	}

	@Override
	public void destroy() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public String getComponentXMLOutput() {
		// TODO Auto-generated method stub
		return null;
	}
	
}
