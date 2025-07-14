package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.WindowConstants;

import Editors.CommandBuildEditor;
import ObjectTypeConversion.CommandBuild;

public class CommandDialog extends JDialog 
{
	private static final long serialVersionUID = 1L;
	
	private static final String TITLE = "Command Entry";
	private static final Dimension MIN_DIMENSION_DIALOG = new Dimension(400, 300);
	
	private JPanel 
		innerPanel = new JPanel(),
		saveCancelPanel = new JPanel(),
		saveCancelPanelOuter = new JPanel();
	private JTextField command = new JTextField();
	private JButton 
		addCommandOptionButton = new JButton(" + Command Option"),
		addParameterButton = new JButton(" + Parameter"),
		saveButton = new JButton("Save"),
		cancelButton = new JButton("Cancel");
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
		paramters.add(tf);
		innerPanel.add(tf);
		innerPanel.getRootPane().validate();
	}
	
	private void saveAction()
	{
		this.retSelection = command.getText();
		for(JTextField jt : commandOptions)
		{
			if(!jt.getText().strip().isBlank())
			{
				this.retSelection += "@" + jt.getText();
			}
		}
		for(JTextField jt : paramters)
		{
			if(!jt.getText().strip().isBlank())
			{
				this.retSelection += "|" + jt.getText();
			}
		}
		commandBuildEditor.setComponentValue(new CommandBuild(this.retSelection));
		this.dispose();
	}
	
	private void cancelAction()
	{
		this.dispose();
	}
	
}
