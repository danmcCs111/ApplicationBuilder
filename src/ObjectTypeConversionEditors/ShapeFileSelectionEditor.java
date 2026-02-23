package ObjectTypeConversionEditors;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;

import Actions.CommandExecutor;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.FileSelection;
import ObjectTypeConversion.ShapeFileSelection;
import Properties.LoggingMessages;

public class ShapeFileSelectionEditor extends FileSelectionEditor 
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		EDIT_COMMAND = "java",
		EDIT_OPTION = "-cp",
		JAR_PATH = "./Application Builder.jar",
		EDIT_XML_PATH = "./Properties/data/ShapeCreator.xml",
		EDIT_CLASS_FILE= "ApplicationBuilder.ApplicationBuilder",
		EDIT_BUTTON_TEXT = "Open Editor";
	
	private JButton 
		editButton;
	
	public ShapeFileSelectionEditor()
	{
		buildWidgets();
		this.setLayout(new BorderLayout());
		this.add(getFileButton(), BorderLayout.CENTER);
		this.add(editButton, BorderLayout.EAST);
	}
	
	@Override
	protected void buildWidgets()
	{
		super.buildWidgets();
		editButton = new JButton(EDIT_BUTTON_TEXT);
		editButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				CommandBuild cb = new CommandBuild();
				FileSelection 
					appFs = new FileSelection(JAR_PATH),
					fs = new FileSelection(EDIT_XML_PATH);
				String [] 
					cliOptions = new String [] {EDIT_OPTION},
					params = new String []{appFs.getRelativePath(), EDIT_CLASS_FILE , fs.getRelativePath()};
				
				cb.setCommand(EDIT_COMMAND, cliOptions, params);
				try {
					CommandExecutor.executeProcess(cb, true);
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		});
		this.add(editButton);
	}
	
	@Override
	public void setComponentValue(Object value) 
	{
		if(value instanceof String)
			return;
		ShapeFileSelection ds = (ShapeFileSelection) value;
		LoggingMessages.printOut(ds.getFullPath());
		getFileButton().setText(ds.getRelativePath());
		getFileChooser().setSelectedFile(new File(ds.getFullPath()));
	}

	@Override
	public String[] getComponentValue() 
	{
		return new String[] {getFileButton().getText()};
	}

	@Override
	public Object getComponentValueObj() 
	{
		return new ShapeFileSelection(getFileButton().getText());
	}
	
	@Override
	public String getParameterDefintionString() 
	{
		return ShapeFileSelection.class.getName();
	}
}
