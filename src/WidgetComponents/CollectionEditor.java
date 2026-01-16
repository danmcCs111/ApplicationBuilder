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
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import Graphics2D.ColorTemplate;
import ObjectTypeConversion.ParseAttribute;
import Properties.LoggingMessages;
import WidgetComponentInterfaces.EditButtonArrayUrls;

public class CollectionEditor extends JFrame 
{
	private static final long serialVersionUID = 1L;
	
	private static final Dimension
		COLLECTION_SIZE = new Dimension(250,0),
		MIN_DIMENSION_DIALOG = new Dimension(600,500);
	private static int 
		SCROLL_INCREMENT = 15;
	private static String
		APPLY_BUTTON_TEXT = "Apply",
		APPLY_BUTTON_TOOLTIP_TEXT = "Delete Selected",
		APPLY_AND_CLOSE_BUTTON_TEXT = "Apply And Close",
		APPLY_AND_CLOSE_BUTTON_TOOLTIP_TEXT = "Delete Selected And Close",
		CANCEL_BUTTON_TEXT = "Close",
		URL_LABEL_TEXT = "Enter New Url: ",
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
		apply,
		applyAndClose,
		removeButton,
		addButton;
	private ArrayList<?> 
		collection;
	private ArrayList<String>
		collectionText = new ArrayList<String>();
	
	private EditButtonArrayUrls ebau;
	private String path;
	private ArrayList<String> addUrls = new ArrayList<String>();
	
	public CollectionEditor(String path, ArrayList<?> collection, EditButtonArrayUrls ebau, String title)
	{
		this.path = path;
		this.collection = collection;
		this.ebau = ebau;
		this.setTitle(title);
		buildWidgets();
		
		this.setMinimumSize(MIN_DIMENSION_DIALOG);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	private void addToCollection(Object o)
	{
		int i = collectionText.size();
		if(o instanceof AbstractButton) //TODO
		{
			collectionText.add(((AbstractButton) o).getText());
			buttonCollectionIndexAndText.put(i, collectionText.get(i));
		}
		else if (o instanceof String)
		{
			collectionText.add((String) o);
			buttonCollectionIndexAndText.put(i, collectionText.get(i));
		}
		else if(o instanceof ParseAttribute)
		{
			ParseAttribute pa = (ParseAttribute)o;
			collectionText.add(pa.name());
			buttonCollectionIndexAndText.put(i, collectionText.get(i));
		}
		else
		{
			LoggingMessages.printOut("collection type not recognized.");
		}
	}
	
//	public void updateAddToCollection(Object o)
//	{
//		addToCollection(o);
//		buttonCollection.setListData(collectionText.toArray(new String [] {}));
//		for(ListSelectionListener lsl : buttonCollection.getListSelectionListeners())
//		{
//			buttonCollection.removeListSelectionListener(lsl);
//		}
//		addListSelectionListener(buttonCollection, removeButton);
//	}
	
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
		for(int i = 0; i < this.collection.size(); i++)
		{
			Object o = this.collection.get(i);
			addToCollection(o);
		}
		buttonCollection.setListData(collectionText.toArray(new String [] {}));
		addListSelectionListener(buttonCollection, removeButton);
		
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
		addListSelectionListener(buttonCollectionRemove, addButton);
		
		removePanel = new JPanel();
		BevelBorder bb = new BevelBorder(BevelBorder.RAISED);
		removePanel.setBorder(bb);
		removePanel.setLayout(new GridLayout(0,1));
		removePanel.add(buttonCollectionRemove);
		removeScrollPane = new JScrollPane(removePanel);
		removeScrollPane.setPreferredSize(COLLECTION_SIZE);
		removeScrollPane.getVerticalScrollBar().setUnitIncrement(SCROLL_INCREMENT);
	}
	
	private void addListSelectionListener(JList<String> collList, JButton btn)
	{
		collList.addListSelectionListener( new ListSelectionListener() {
			@Override
			public void valueChanged(ListSelectionEvent e) {
				int [] indexes = collList.getSelectedIndices();
				btn.setEnabled((indexes != null && indexes.length > 0));
			}
		});
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
		JPanel applyCancelPanel = new JPanel();
		apply = new JButton(APPLY_BUTTON_TEXT);
		apply.setToolTipText(APPLY_BUTTON_TOOLTIP_TEXT);
		apply.setEnabled(false);
		apply.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyAction();
			}
		});
		applyAndClose = new JButton(APPLY_AND_CLOSE_BUTTON_TEXT);
		applyAndClose.setToolTipText(APPLY_AND_CLOSE_BUTTON_TOOLTIP_TEXT);
		applyAndClose.setEnabled(false);
		applyAndClose.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				applyAction();
				closeAction();
			}
		});
		JButton cancel = new JButton(CANCEL_BUTTON_TEXT);
		cancel.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				closeAction();
			}
		});
		applyCancelPanel.add(apply);
		applyCancelPanel.add(applyAndClose);
		applyCancelPanel.add(cancel);
		
		JPanel southPanel = new JPanel();
		southPanel.setLayout(new GridLayout(0,1));
		new CollectionEditorAddPanel(southPanel, ebau, path, URL_LABEL_TEXT);
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
		
		if(buttonCollectionRemoveIndexAndText.size() > 0)
		{
			applyAndClose.setEnabled(true);
			apply.setEnabled(true);
		}
		else
		{
			applyAndClose.setEnabled(false);
			apply.setEnabled(false);
		}
	}
	
	private ArrayList<?> copyToRemove()
	{
		ArrayList<Object> collectionRemove = new ArrayList<Object>();
		for(int key : this.buttonCollectionRemoveIndexAndText.keySet())
		{
			Object o = this.collection.get(key);
			collectionRemove.add(o);
		}
		return collectionRemove;
	}
	
	private void clearRemovePanel()
	{
		this.buttonCollectionRemoveIndexAndText = new HashMap<Integer, String>();
		removePanel.remove(buttonCollectionRemove);
		this.buttonCollectionRemove = new JList<String>();
		addButton.setEnabled(false);
		addListSelectionListener(buttonCollectionRemove, addButton);
		removePanel.add(buttonCollectionRemove);
		
		ColorTemplate.setBackgroundColorButtons(removePanel, ColorTemplate.getButtonBackgroundColor());
		ColorTemplate.setForegroundColorButtons(removePanel, ColorTemplate.getButtonForegroundColor());
		ColorTemplate.setBackgroundColorPanel(removePanel, ColorTemplate.getPanelBackgroundColor());
		
		removePanel.repaint();
	}
	
	private void applyAction()
	{
		ebau.updateButtonArrayCollection(this.path, addUrls, copyToRemove());
		clearRemovePanel();
		addUrls.clear();
		applyAndClose.setEnabled(false);
		apply.setEnabled(false);
	}
	
	private void closeAction()
	{
		this.dispose();
	}
}
