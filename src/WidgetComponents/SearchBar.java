package WidgetComponents;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;

import WidgetComponentInterfaces.SearchSubscriber;

public class SearchBar extends JPanel
{
	private static final long serialVersionUID = 1L;
	
	private JTextField searchField;
	private JButton searchButton;
	private static int columnCharacterLength = 10;
	
	private ArrayList<SearchSubscriber> searchSubscribers = new ArrayList<SearchSubscriber>();

	public SearchBar()
	{
		buildWidgets();
	}
	
	public void addSearchSubscriber(SearchSubscriber subscriber)
	{
		searchSubscribers.add(subscriber);
	}
	
	public void setColumnCharacterLength(int length)
	{
		columnCharacterLength = length;
		searchField.setColumns(columnCharacterLength);
	}
	
	public void buildWidgets()
	{
		this.setLayout(new BorderLayout());
		searchField = new JTextField(columnCharacterLength);
		searchButton = new JButton("Search");
		searchButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for(SearchSubscriber ss : searchSubscribers)
				{
					ss.notifySearchText(searchField.getText());
				}
			}
		});
		this.add(searchField, BorderLayout.CENTER);
		this.add(searchButton, BorderLayout.EAST);
	}
}
