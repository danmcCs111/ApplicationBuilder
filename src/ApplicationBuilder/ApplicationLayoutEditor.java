package ApplicationBuilder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;

import ActionListeners.AddComponentActionListener;
import ActionListeners.CloseProjectActionListener;
import ActionListeners.GenerateActionListener;
import ActionListeners.NewEditorActionListener;
import ActionListeners.OpenFileActionListener;
import ActionListeners.OpenParameterEditorActionListener;
import ActionListeners.SaveEditorActionListener;

public class ApplicationLayoutEditor extends DependentRedrawableFrame
{
	private static final long serialVersionUID = 1887L;
	
	private static final String 
		TITLE = "Application Layout Editor",
		FILE_MENU_TEXT = "File",
		MENU_ITEM_NEW_TEXT = "New",
		MENU_ITEM_OPEN_TEXT = "Open",
		MENU_ITEM_CLOSE_TEXT = "Close Project",
		EDITOR_SAVE_BUTTON_TEXT = "Save",
		EDITOR_GENERATE_BUTTON_TEXT = "Generate",
		EDITOR_ADD_PROPERTY_BUTTON_TEXT = "Add Component Property",
		EDITOR_ADD_COMPONENT_BUTTON_TEXT = "Add Component";
	
	public static final Dimension 
		WINDOW_LOCATION = new Dimension(550, 10),
		WINDOW_SIZE = new Dimension(680, 640);
	
	private JButton openParameterButton;
	private XmlToEditor xe;
	private OpenParameterEditorActionListener opListener;
	
	public ApplicationLayoutEditor()
	{
		this.setTitle(TITLE);
		this.setLocation(WINDOW_LOCATION.width, WINDOW_LOCATION.height);
		
		JMenuBar menuBar = new JMenuBar();
		JMenu menu = new JMenu(FILE_MENU_TEXT);
		
		JMenuItem newItem = new JMenuItem(MENU_ITEM_NEW_TEXT);
		newItem.addActionListener(new NewEditorActionListener(this));
		menu.add(newItem);
		
		JMenuItem openItem = new JMenuItem(MENU_ITEM_OPEN_TEXT);
		openItem.addActionListener(new OpenFileActionListener(this));
		menu.add(openItem);
		
		JMenuItem closeProj = new JMenuItem(MENU_ITEM_CLOSE_TEXT);
		closeProj.addActionListener(new CloseProjectActionListener());
		menu.add(closeProj);
		
		menuBar.add(menu);
		this.setJMenuBar(menuBar);
		
		buildEditorButtons();
		
		this.setSize(WINDOW_SIZE.width, WINDOW_SIZE.height);
		this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		this.setVisible(true);
	}
	
	public void buildEditorButtons()
	{
		JPanel p = new JPanel(new GridLayout(0,2));
		
		openParameterButton = new JButton(EDITOR_ADD_PROPERTY_BUTTON_TEXT);
		JButton generateButton = new JButton(EDITOR_GENERATE_BUTTON_TEXT);
		generateButton.addActionListener(new GenerateActionListener());
		
		p.add(openParameterButton);
		p.add(generateButton);
		
		JButton addComponent = new JButton(EDITOR_ADD_COMPONENT_BUTTON_TEXT);
		addComponent.addActionListener(new AddComponentActionListener(this));
		JButton saveButton = new JButton(EDITOR_SAVE_BUTTON_TEXT);
		saveButton.addActionListener(new SaveEditorActionListener(this));
		
		p.add(addComponent);
		p.add(saveButton);
		
		this.add(p, BorderLayout.SOUTH);
		
		rebuildInnerPanels();
	}
	
	@Override
	public void updateDependentWindow()
	{
		if(opListener.getBuilderWindow() != null)
		{
			opListener.getBuilderWindow().clearInnerPanels();
			opListener.getBuilderWindow().rebuildInnerPanels();
		}
	}

	@Override
	public void clearInnerPanels() 
	{
		xe.destroyPanel();
	}

	@Override
	public void rebuildInnerPanels() 
	{
		if(xe == null)
		{
			xe = new XmlToEditor(ApplicationLayoutEditor.this);
			xe.rebuildPanel();
		}
		else if(xe != null)
		{
			xe.rebuildPanel();
		}
		
		if(opListener != null)
		{
			openParameterButton.removeActionListener(opListener);
		}
		opListener = new OpenParameterEditorActionListener(xe, ApplicationLayoutEditor.this);
		openParameterButton.addActionListener(opListener);
	}
	
}
