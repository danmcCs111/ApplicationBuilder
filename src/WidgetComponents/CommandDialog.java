package WidgetComponents;

import java.awt.BorderLayout;
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
import ObjectTypeConversion.CommandBuild;
import ObjectTypeConversion.DirectorySelection;
import WidgetExtensions.ComboListDialogSelectedListener;

public class CommandDialog extends JDialog implements ComboListDialogSelectedListener
{
	private static final long serialVersionUID = 1L;
	
	private static final String 
		TITLE = "Command Entry",
		COMMAND_OPTION_LABEL = " + Command Option",
		PARAMETER_LABEL = " + Parameter",
		SAVE_BUTTON_LABEL = "Save",
		CANCEL_BUTTON_LABEL = "Cancel";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	
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
		commandOptions = new ArrayList<JTextField>(),
		paramters = new ArrayList<JTextField>();
	
	private String retSelection = null;
	private CommandBuildEditor commandBuildEditor;
	
	public CommandDialog(CommandBuildEditor cbe, CommandBuild cb)
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
				addParameter();
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
			if(cb.getParameters() != null && cb.getParameters().length > 0)
			{
				for(String s : cb.getParameters())
				{
					addParameter(s);
				}
			}
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
		commandOptions.add(tf);
		int index = 2 + commandOptions.size()-1;
		innerPanel.add(tf, index);
		innerPanel.getRootPane().validate();
	}
	
	private void addParameter()
	{
		addParameter("");
	}
	private void addParameter(String s)
	{
		JTextField tf = new JTextField(s);
		JPanel 
			outerPanelParam = new JPanel(),
			innerPanelParam = new JPanel();
		outerPanelParam.setLayout(new BorderLayout());
		innerPanelParam.setLayout(new GridLayout(1,0));
		
		JButton deleteField = new JButton("X");
		deleteField.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				paramters.remove(tf);
				innerPanel.remove(outerPanelParam);
				innerPanel.getRootPane().validate();
			}
		});
		JButton addField = new JButton("+");
		addField.addActionListener(new ActionListener() {
			public static final List<String> options = Arrays.asList(new String[] {
					"TextField","Directory","File"
			});
			@Override
			public void actionPerformed(ActionEvent e) {
				ComboSelectionDialog fieldOrDirectory = new ComboSelectionDialog();
				fieldOrDirectory.buildAndShow(options, "SelectType", "Select Type", CommandDialog.this, CommandDialog.this);
				innerPanel.getRootPane().validate();
			}
		});
		innerPanelParam.add(tf);
		outerPanelParam.add(innerPanelParam, BorderLayout.CENTER);
		outerPanelParam.add(addField, BorderLayout.EAST);
		outerPanelParam.add(deleteField, BorderLayout.WEST);
		
		paramters.add(tf);
		innerPanel.add(outerPanelParam);
		innerPanel.getRootPane().validate();
	}
	private void addParameter(DirectorySelection ds)
	{
		
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
		for(JTextField jt : paramters)
		{
			if(!jt.getText().strip().isBlank())
			{
				this.retSelection += CommandBuild.DELIMITER_PARAMETER_OPTION + jt.getText();
			}
		}
		commandBuildEditor.setComponentValue(new CommandBuild(this.retSelection));
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}

	@Override
	public void selectionChosen(List<String> chosenSelection) {
		// TODO Auto-generated method stub
		
	}
	
}
