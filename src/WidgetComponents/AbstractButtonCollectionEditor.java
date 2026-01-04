package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import javax.swing.AbstractButton;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Graphics2D.ColorTemplate;

public class AbstractButtonCollectionEditor extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension
		COLLECTION_SIZE = new Dimension(250,0),
		MIN_DIMENSION_DIALOG = new Dimension(600,500);
	private static int 
		URL_COLUMNSIZE = 35,
		SCROLL_INCREMENT = 15;
	private static String
		APPLY_BUTTON_TEXT = "Apply",
		CANCEL_BUTTON_TEXT = "Cancel",
		URL_LABEL_TEXT = "Enter New Url: ",
		URL_ADD_BUTTON_TEXT = "Add",
		ADD_BUTTON_TEXT = "restore",
		REMOVE_BUTTON_TEXT = "delete";
	
	private JTextField 
		urlField;
	private JScrollPane 
		collectionScrollPane,
		removeScrollPane; 
	private JPanel
		collectionPanel,
		addRemovePanel,
		removePanel;
	private JList<String> 
		buttonCollection = new JList<String>(),
		buttonCollectionRemove = new JList<String>();
	private HashMap<Integer, String>
		buttonCollectionIndexAndText = new HashMap<Integer, String>(),
		buttonCollectionRemoveIndexAndText = new HashMap<Integer, String>();;
	private JButton
		removeButton,
		addButton;
	private ArrayList<AbstractButton> 
		collection;
	private String [] 
		collectionText;
	
	public AbstractButtonCollectionEditor(ArrayList<AbstractButton> collection)
	{
		this.collection = collection;
		buildWidgets();
	}
	
	private void buildWidgets()
	{
		buildCenterPanel();
		buildEastPanel();
		buildWestPanel();
		JPanel southPanel = buildSouthPanel();
		
		this.setLayout(new BorderLayout());
		this.add(collectionScrollPane, BorderLayout.WEST);
		this.add(addRemovePanel, BorderLayout.CENTER);
		this.add(removeScrollPane, BorderLayout.EAST);
		this.add(southPanel, BorderLayout.SOUTH);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		
		ColorTemplate.setBackgroundColorButtons(this, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(this, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorPanel(this, ColorTemplate.getPanelBackgroundColor());
	}
	
	private void buildWestPanel()
	{
		collectionText = new String[this.collection.size()];
		for(int i = 0; i < this.collection.size(); i++)
		{
			collectionText[i] = this.collection.get(i).getText();
			buttonCollectionIndexAndText.put(i, collectionText[i]);
		}
		buttonCollection.setListData(collectionText);
		buttonCollection.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int [] indexes = buttonCollection.getSelectedIndices();
				removeButton.setEnabled((indexes != null && indexes.length > 0));
			}
		});
		
		collectionPanel = new JPanel();
		BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
		collectionPanel.setBorder(bb);
		collectionPanel.setLayout(new GridLayout(0,1));
		collectionPanel.add(buttonCollection);
		collectionScrollPane = new JScrollPane(collectionPanel);
		collectionScrollPane.setPreferredSize(COLLECTION_SIZE);
		collectionScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
	}
	
	private void buildEastPanel()
	{
		buttonCollectionRemove.setListData(new String [] {});
		buttonCollectionRemove.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int [] indexes = buttonCollectionRemove.getSelectedIndices();
				addButton.setEnabled((indexes != null && indexes.length > 0));
			}
		});
		
		removePanel = new JPanel();
		BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
		removePanel.setBorder(bb);
		removePanel.setLayout(new GridLayout(0,1));
		removePanel.add(buttonCollectionRemove);
		removeScrollPane = new JScrollPane(removePanel);
		removeScrollPane.setPreferredSize(COLLECTION_SIZE);
		removeScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
	}
	
	private void buildCenterPanel()
	{
		addButton = new JButton(ADD_BUTTON_TEXT);
		removeButton = new JButton(REMOVE_BUTTON_TEXT);
		addButton.setEnabled(false);
		removeButton.setEnabled(false);
		
		addButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				swapCollection(
						buttonCollectionRemoveIndexAndText, buttonCollectionRemove,
						buttonCollectionIndexAndText, buttonCollection 
				);
			}
		});
		removeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				swapCollection(
						buttonCollectionIndexAndText, buttonCollection, 
						buttonCollectionRemoveIndexAndText, buttonCollectionRemove
				);
			}
		});
		
		addRemovePanel = new JPanel();
		addRemovePanel.setLayout(new FlowLayout());
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(0,1));
		innerPanel.add(removeButton);
		innerPanel.add(addButton);
		addRemovePanel.add(innerPanel);
	}
	
	private JPanel buildSouthPanel()
	{
		JPanel urlPanel = new JPanel();
		JLabel urlLabel = new JLabel(URL_LABEL_TEXT);
		urlField = new JTextField();
		urlField.setColumns(URL_COLUMNSIZE);
		JButton addUrlButton = new JButton(URL_ADD_BUTTON_TEXT);
		urlPanel.add(urlLabel);
		urlPanel.add(urlField);
		urlPanel.add(addUrlButton);

		JPanel applyCancelPanel = new JPanel();
		JButton apply = new JButton(APPLY_BUTTON_TEXT);
		JButton cancel = new JButton(CANCEL_BUTTON_TEXT);
		applyCancelPanel.add(apply);
		applyCancelPanel.add(cancel);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(0,1));
		southPanel.add(urlPanel);
		southPanel.add(applyCancelPanel);
		
		return southPanel;
	}
	
	private void swapCollection(
			HashMap<Integer, String> btnCollectionIndexed, JList<String> btnCollection,
			HashMap<Integer, String> btnCollectionIndexedInto, JList<String> btnCollectionInto)
	{
		int [] selections = btnCollection.getSelectedIndices();
		if(selections == null || selections.length == 0)
			return;
		
		for(int i = 0; i < selections.length; i++)
		{
			String value = btnCollection.getSelectedValuesList().get(i);
			int key = getKey(btnCollectionIndexed, value);
			btnCollectionIndexedInto.put(key, value);
			btnCollectionIndexed.remove(key, value);
		}
		
		copyInto(btnCollectionIndexedInto, btnCollectionInto);
		copyInto(btnCollectionIndexed, btnCollection);
		
		this.revalidate();
		this.repaint();
	}
	
	private int getKey(HashMap<Integer, String> btnCollectionIndexed, String value)
	{
		for(int key : btnCollectionIndexed.keySet())
		{
			if(btnCollectionIndexed.get(key).equals(value))
			{
				return key;
			}
		}
		return -1;
	}
	
	private void copyInto(HashMap<Integer, String> btnCollectionIndexed, JList<String> btnCollection)
	{
		String [] refesh = new String [btnCollectionIndexed.keySet().size()];
		int count = 0;
		List<Integer> keys = Arrays.asList(btnCollectionIndexed.keySet().toArray(new Integer[] {}));
		Collections.sort(keys);
		for(int key : keys)
		{
			String val = btnCollectionIndexed.get(key);
			refesh[count] = val;
			count++;
		}
		btnCollection.removeAll();
		btnCollection.setListData(refesh);
	}
	
	public static ArrayList<AbstractButton> setupTest()
	{
		ArrayList<AbstractButton> jblls = new ArrayList<AbstractButton>();
		for(int i = 0; i < 30; i++)
		{
			JButtonLengthLimited jbll = new JButtonLengthLimited();
			jbll.setText("test-" + i);
			jblls.add(jbll);
		}
		return jblls;
	}
	
	public static void main(String [] args)
	{
		AbstractButtonCollectionEditor abce = new AbstractButtonCollectionEditor(setupTest());
		abce.setMinimumSize(MIN_DIMENSION_DIALOG);
		abce.setResizable(false);
		abce.setVisible(true);
	}

}
