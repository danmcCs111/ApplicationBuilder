package EditorInterfaces;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextField;

import Graphics2D.GraphicsUtil;
import ObjectTypeConversion.DirectorySelection;
import ObjectTypeConversion.PathArrayProcessing;
import ObjectTypeConversionEditors.DirectorySelectionEditor;
import Properties.LoggingMessages;

public class PathArrayProcessingFrame extends JFrame
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension
		FRAME_SIZE = new Dimension(450, 300);
	private static final String
		CANCEL_BUTTON_TEXT = "Cancel",
		SAVE_BUTTON_TEXT = "Save",
		ADD_FOLDER_ACTION = "Add Folder",
		TITLE = "Add/Edit Folders";
	
	private PathArrayProcessing pa;
	private PathArrayChangeListener pacl;
	private Container refContainer;
	private JPanel innerPanel;
	private JButton addFolderButton;
	
	private ArrayList<DirectorySelectionEditor> directories;
	private ArrayList<JTextField> extensions;

	public PathArrayProcessingFrame(Container refContainer, PathArrayChangeListener pacl)
	{
		this.refContainer = refContainer;
		this.pacl = pacl;
	}
	
	private void buildWidgets(PathArrayProcessing pa)
	{
		directories = new ArrayList<DirectorySelectionEditor>();
		extensions = new ArrayList<JTextField>();
		innerPanel = new JPanel();
		addFolderButton = new JButton(ADD_FOLDER_ACTION);
		
		this.setTitle(TITLE);
		this.setLayout(new BorderLayout());
		addFolderButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) 
			{
				innerPanel.add(addFolder("",""));
				PathArrayProcessingFrame.this.validate();
			}
		});
		innerPanel.add(addFolderButton);
		
		innerPanel.setLayout(new GridLayout(0,1));
		
		int count = pa.getSize();
		
		for(int i = 0; i < count; i++)
		{
			JPanel innerPanel2 = addFolder(pa.getPathValue(i), pa.getExtensionValue(i));
			innerPanel.add(innerPanel2);
		}
		
		addSaveCancelButtons();
		
		GraphicsUtil.rightEdgeTopWindow(refContainer, PathArrayProcessingFrame.this);
		this.add(innerPanel, BorderLayout.NORTH);
		this.setSize(FRAME_SIZE);
		
		this.addWindowListener(new WindowAdapter() 
		{
	         @Override
	         public void windowClosing(WindowEvent e) 
	         {
	        	 close();
	         }
	     });
	}
	private JPanel addFolder(String pathValue, String extensionValue)
	{
		JPanel innerPanelItem = new JPanel();
		innerPanelItem.setLayout(new BorderLayout());
		
		DirectorySelectionEditor dse = new DirectorySelectionEditor();
		dse.setComponentValue(new DirectorySelection(pathValue));
		directories.add(dse);
		
		JTextField extension = new JTextField();
		extension.setColumns(8);
		extension.setText(extensionValue);
		extensions.add(extension);
		
		JButton del = new JButton("X");
		del.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				removeDirectory(dse, innerPanelItem);
				PathArrayProcessingFrame.this.validate();
			}
		});
		
		innerPanelItem.add(del, BorderLayout.WEST);
		innerPanelItem.add(dse, BorderLayout.CENTER);
		innerPanelItem.add(extension, BorderLayout.EAST);
		
		return innerPanelItem;
	}
	private void addSaveCancelButtons()
	{
		JButton save = new JButton(SAVE_BUTTON_TEXT);
		save.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				copyValues();
				close();
			}
		});
		JButton cancel = new JButton(CANCEL_BUTTON_TEXT);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				close();
			}
		});
		
		JPanel southPane = new JPanel(new BorderLayout());
		JPanel eastPane = new JPanel(new GridLayout(1,2));
		eastPane.add(save);
		eastPane.add(cancel);
		southPane.add(eastPane, BorderLayout.EAST);
		
		this.add(southPane, BorderLayout.SOUTH);
	}
	private void removeDirectory(DirectorySelectionEditor dse, JPanel innerPanelItem)
	{
		innerPanel.remove(innerPanelItem);
		for(int i = 0; i < directories.size(); i++)
		{
			if(directories.get(i).equals(dse))
			{
				directories.remove(i);
				extensions.remove(i);
			}
		}
	}
	
	public void setOpen(PathArrayProcessing pa)
	{
		this.pa = pa;
		LoggingMessages.printOut(pa.toString());
		buildWidgets(pa);
		this.setVisible(true);
	}
	
	public void close()
	{
		pacl.setPathArrayProcessingValue(pa);
		this.removeAll();
		this.setVisible(false);
	}
	private void copyValues()
	{
		PathArrayProcessing paTmp = new PathArrayProcessing("");
		for(int i=0; i < directories.size(); i++)
		{
			paTmp.addPathAndExtension(
					((DirectorySelection)directories.get(i).getComponentValueObj()).getRelativePath(),
					extensions.get(i).getText()
			);
		}
		this.pa = paTmp;
	}
	
}
