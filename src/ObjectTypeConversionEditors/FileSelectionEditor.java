package ObjectTypeConversionEditors;

import java.awt.Component;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JPanel;

import ObjectTypeConversion.FileSelection;
import ObjectTypeConversion.ValueChangedListener;
import Params.ParameterEditor;
import Properties.LoggingMessages;
import Properties.PathUtility;

public class FileSelectionEditor extends JPanel implements ParameterEditor 
{
	private static final long serialVersionUID = 2005L;

	private static final String
		NULL_VALUE_TEXT = "<Select File>", 
		DIRECTORY_SELECT_DIALOG_TITLE_TEXT = "Select File";
	
	private JFileChooser 
		jcc;
	private JButton 
		editFileButton;
	private ArrayList<ValueChangedListener> 
		vcls;
	private boolean 
		isRelativePath = true;
	private FileSelection
		fileSelection = null;

	public FileSelectionEditor()
	{
		buildWidgets();
		this.setLayout(new GridLayout(0,1));
		this.add(editFileButton);
	}
	
	public void setIsRelativePath(boolean relativePath)
	{
		this.isRelativePath = relativePath; 
	}
	
	public void addValueChangedListener(ValueChangedListener vcl)
	{
		if(vcls == null)
		{
			vcls = new ArrayList<ValueChangedListener>();
		}
		vcls.add(vcl);
	}
	
	protected void buildWidgets()
	{
		editFileButton = new JButton();
		
		jcc = new JFileChooser();
		jcc.setDialogType(JFileChooser.FILES_AND_DIRECTORIES);
		getFileButton().addActionListener(new ActionListener() 
		{
			@Override
			public void actionPerformed(ActionEvent e) {
				jcc.setDialogTitle(DIRECTORY_SELECT_DIALOG_TITLE_TEXT);
				int choice = jcc.showOpenDialog(null);
				File chosenFile = jcc.getSelectedFile();
				if(chosenFile != null && choice == JFileChooser.APPROVE_OPTION)
				{
					if(isRelativePath)
					{
						String replPath = PathUtility.replaceBackslash(chosenFile.getAbsolutePath());
						
						FileSelection fs = new FileSelection(
							replPath.replaceAll(
								PathUtility.replaceBackslash(PathUtility.getCurrentDirectory()), 
								""
							) 
						);
						setComponentValue(fs);
					}
					else
					{
						FileSelection ds = new FileSelection(chosenFile.getAbsolutePath(), false);
						setComponentValue(ds);
					}
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
		if(value == null)
		{
			fileSelection = null;
			getFileButton().setText(NULL_VALUE_TEXT);
		}
		if(value instanceof String)
			return;
		
		fileSelection = (FileSelection) value;
		LoggingMessages.printOut(fileSelection.getFullPath());
		getFileButton().setText(
			(isRelativePath)
				? fileSelection.getRelativePath()
				: fileSelection.getFullPath()
		);
		jcc.setSelectedFile(new File(fileSelection.getFullPath()));
		if(vcls != null)
		{
			for(ValueChangedListener vcl : vcls)
			{
				vcl.valueChanged(fileSelection);
			}
		}
	}

	@Override
	public String[] getComponentValue() 
	{
		if(fileSelection != null)
		{
			return new String[] {getFileButton().getText()};
		}
		else
		{
			return null;
		}
	}

	@Override
	public Object getComponentValueObj() 
	{
		return fileSelection;
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
