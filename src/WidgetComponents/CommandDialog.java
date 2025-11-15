package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import Editors.CommandBuildEditor;
import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.FileSelection;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.ParamOption;
import WidgetExtensions.ComboListDialogSelectedListener;

public class CommandDialog extends JDialog
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Command Entry",
		COMMAND_OPTION_LABEL = " + Command Option",
		PARAMETER_LABEL = " + Parameter",
		PARAM_ADD_TEXT = "+",
		PARAM_DELETE_TEXT = "X",
		PARAM_DIALOG_TITLE = "Select Type",
		PARAM_DIALOG_MESSAGE = "Select Type",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	public static final List<String> PARAM_OPTIONS = Arrays.asList(new String[] {
			ParamOption.TextField.getDisplayText(),
			ParamOption.Directory.getDisplayText(),
			ParamOption.File.getDisplayText()
	});
	
	private static Color 
		panelBackgroundColor,
		deleteBackgroundColor,
		deleteForegroundColor = Color.red,
		buttonBackgroundColor,
		buttonForegroundColor;
	
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JTextField command = new JTextField();
	private JButton 
		addCommandOptionButton = new JButton(COMMAND_OPTION_LABEL),
		addParameterButton = new JButton(PARAMETER_LABEL),
		saveButton = new JButton(SAVE_BUTTON_LABEL),
		cancelButton = new JButton(CANCEL_BUTTON_LABEL);
	private ArrayList<JTextField> 
		commandOptions = new ArrayList<JTextField>();
	private ArrayList<Parameter>
		paramters = new ArrayList<Parameter>();
	
	private String retSelection = null;
	private CommandBuildEditor commandBuildEditor;
	
	public CommandDialog(CommandBuildEditor cbe, CommandBuild cb)
	{
		buildWidgets(cbe, cb);
	}
	
	public static void setButtonForegroundColor(Color c)
	{
		buttonForegroundColor = c;
	}
	
	public static void setButtonBackgroundColor(Color c)
	{
		buttonBackgroundColor = c;
	}
	
	public static void setDeleteForegroundColor(Color c)
	{
		deleteForegroundColor = c;
	}
	
	public static void setDeleteBackgroundColor(Color c)
	{
		deleteBackgroundColor = c;
	}
	
	public static void setPanelBackgroundColor(Color c)
	{
		panelBackgroundColor = c;
	}
	
	public void buildWidgets(CommandBuildEditor cbe, CommandBuild cb)
	{
		this.commandBuildEditor = cbe;
		this.setTitle(TITLE);
		this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
		this.setLocation(cbe.getRootPane().getParent().getLocation());
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setLayout(new BorderLayout());
		
		innerPanel.setLayout(new GridLayout(0, 1));
		addCommandOptionButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				addCommandOption();
			}
		});
		addParameterButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				buildParameter();
			}
		});
		this.add(innerPanel, BorderLayout.NORTH);
		innerPanel.add(command);
		innerPanel.add(addCommandOptionButton);
		innerPanel.add(addParameterButton);
		saveCancelPanelOuter.setLayout(new BorderLayout());
		saveCancelPanel.setLayout(new GridLayout(1,2));
		
		saveButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				saveAction();
			}
		});
		saveCancelPanel.add(saveButton);
		
		cancelButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				cancelAction();
			}
		});
		saveCancelPanel.add(cancelButton);
		
		saveCancelPanelOuter.add(saveCancelPanel, BorderLayout.EAST);
		this.add(saveCancelPanelOuter, BorderLayout.SOUTH);
		
		if(cb != null)
		{
			if(!cb.getCommand().strip().isEmpty())
			{
				command.setText(cb.getCommand());
			}
			if(cb.getCommandOptions() != null && cb.getCommandOptions().length > 0)
			{
				for(String s : cb.getCommandOptions())
				{
					addCommandOption(s);
				}
			}
			
			if(buttonForegroundColor != null)
			{
				GraphicsUtil.setForegroundColorButtons(this, buttonForegroundColor);
			}
			if(buttonBackgroundColor != null)
			{
				GraphicsUtil.setBackgroundColorButtons(this, buttonBackgroundColor);
			}
			if(cb.getParameters() != null && cb.getParameters().size() > 0)
			{
				for(Parameter s : cb.getParameters())
				{
					buildParameter(s);
				}
			}
		}
		else
		{
			if(buttonForegroundColor != null)
			{
				GraphicsUtil.setForegroundColorButtons(this, buttonForegroundColor);
			}
			if(buttonBackgroundColor != null)
			{
				GraphicsUtil.setBackgroundColorButtons(this, buttonBackgroundColor);
			}
		}
		
		if(panelBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorPanel(this, panelBackgroundColor);
		}
		this.setVisible(true);
	}
	
	private void addCommandOption()
	{
		addCommandOption("");
	}
	private void addCommandOption(String s)
	{
		JTextField tf = new JTextField(s);
		if(buttonForegroundColor != null)
		{
			GraphicsUtil.setForegroundColorButtons(tf, buttonForegroundColor);
		}
		if(panelBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorPanel(tf, panelBackgroundColor);
		}
		commandOptions.add(tf);
		int index = 2 + commandOptions.size()-1;
		innerPanel.add(tf, index);
		innerPanel.getRootPane().validate();
	}
	
	private void buildParameter()
	{
		buildParameter(new Parameter());
	}
	private void buildParameter(final Parameter param)
	{
		JPanel 
			outerPanelParam = new JPanel(),
			innerPanelParam = new JPanel();
		outerPanelParam.setLayout(new BorderLayout());
		innerPanelParam.setLayout(new GridLayout(1,0));
		
		JButton deleteFieldButton = new JButton(PARAM_DELETE_TEXT);
		
		deleteFieldButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				innerPanel.remove(outerPanelParam);
				paramters.remove(param);
				innerPanel.getRootPane().validate();
			}
		});
		JButton addFieldButton = new JButton(PARAM_ADD_TEXT);
		ComboListDialogSelectedListener addListener = new ComboListDialogSelectedListener() {
			@Override
			public void selectionChosen(List<String> chosenSelection) 
			{
				//TODO
				if(chosenSelection == null || chosenSelection.isEmpty())
					return;
				for(String select : chosenSelection)
				{
					ParamOption po = ParamOption.getParamOption(select);
					switch(po)
					{
					case TextField:
						param.addParamString("");
						break;
					case Directory:
						param.addParamDirectory(new DirectorySelection("."));
						break;
					case File:
						param.addParamFile(new FileSelection("."));
						break;
					}
				}
				LoggingMessages.printOut(chosenSelection.toArray(new String[] {}));
				innerPanel.getRootPane().validate();
			}
		};
		addFieldButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboSelectionDialog fieldOrDirectory = new ComboSelectionDialog();
				fieldOrDirectory.buildAndShow(PARAM_OPTIONS, PARAM_DIALOG_TITLE, PARAM_DIALOG_MESSAGE, addListener, CommandDialog.this);
				innerPanel.getRootPane().validate();
			}
		});
		innerPanelParam.add(param);
		paramters.add(param);
		
		outerPanelParam.add(innerPanelParam, BorderLayout.CENTER);
		outerPanelParam.add(addFieldButton, BorderLayout.EAST);
		outerPanelParam.add(deleteFieldButton, BorderLayout.WEST);
		
		if(deleteForegroundColor != null)
		{
			GraphicsUtil.setForegroundColorButtons(deleteFieldButton, buttonForegroundColor);
		}
		if(deleteBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorButtons(deleteFieldButton, deleteBackgroundColor);
		}
		if(buttonForegroundColor != null)
		{
			GraphicsUtil.setForegroundColorButtons(addFieldButton, buttonForegroundColor);
		}
		if(buttonBackgroundColor != null)
		{
			GraphicsUtil.setBackgroundColorButtons(addFieldButton, buttonBackgroundColor);
		}
		
		innerPanel.add(outerPanelParam);
		innerPanel.getRootPane().validate();
	}
	
	private void saveAction()
	{
		this.retSelection = command.getText();
		for(JTextField jt : commandOptions)
		{
			if(!jt.getText().strip().isBlank())
			{
				this.retSelection += CommandBuild.DELIMITER_COMMANDLINE_OPTION + jt.getText();
			}
		}
		for(Parameter param : paramters)
		{
			retSelection += param.getCommandBuildSaveString();
		}
		commandBuildEditor.setComponentValue(new CommandBuild(this.retSelection));
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}

}
