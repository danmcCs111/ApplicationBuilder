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
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

public class AbstractButtonCollectionEditor extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension
		COLLECTION_SIZE = new Dimension(250,500),
		MIN_DIMENSION_DIALOG = new Dimension(600,500);
	private static int 
		SCROLL_INCREMENT = 15;
	private static String
		ADD_BUTTON_TEXT = "restore",
		REMOVE_BUTTON_TEXT = "delete";
	
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
	
	public void buildWidgets()
	{
		addButton = new JButton(ADD_BUTTON_TEXT);
		removeButton = new JButton(REMOVE_BUTTON_TEXT);
		addButton.setEnabled(false);
		removeButton.setEnabled(false);
		
		addRemovePanel = new JPanel();
		addRemovePanel.setLayout(new FlowLayout());
		JPanel innerPanel = new JPanel();
		innerPanel.setLayout(new GridLayout(0,1));
		innerPanel.add(removeButton);
		innerPanel.add(addButton);
		addRemovePanel.add(innerPanel);
		
		collectionText = new String[this.collection.size()];
		for(int i = 0; i < this.collection.size(); i++)
		{
			collectionText[i] = this.collection.get(i).getText();
			buttonCollectionIndexAndText.put(i, collectionText[i]);
		}
		buttonCollection.setListData(collectionText);
		buttonCollection.setPreferredSize(COLLECTION_SIZE);
		buttonCollection.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int [] indexes = buttonCollection.getSelectedIndices();
				removeButton.setEnabled((indexes != null && indexes.length > 0));
			}
		});
		collectionPanel = new JPanel();
		collectionPanel.setLayout(new GridLayout(0,1));
		collectionPanel.add(buttonCollection);
		collectionScrollPane = new JScrollPane(collectionPanel);
		collectionScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
		
		
		buttonCollectionRemove.setListData(new String [] {});
		buttonCollectionRemove.setPreferredSize(COLLECTION_SIZE);
		buttonCollectionRemove.addListSelectionListener(new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int [] indexes = buttonCollectionRemove.getSelectedIndices();
				addButton.setEnabled((indexes != null && indexes.length > 0));
			}
		});
		removePanel = new JPanel();
		removePanel.setLayout(new GridLayout(0,1));
		removePanel.add(buttonCollectionRemove);
		removeScrollPane = new JScrollPane(removePanel);
		removeScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
		
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
		
		this.setLayout(new BorderLayout());
		this.add(collectionScrollPane, BorderLayout.WEST);
		this.add(addRemovePanel, BorderLayout.CENTER);
		this.add(removeScrollPane, BorderLayout.EAST	);
		this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
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
